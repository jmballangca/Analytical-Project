package com.ketchupzzz.analytical.repository.questions


import android.util.Log
import com.google.firebase.FirebaseError
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.models.QuizWithQuestions

import com.ketchupzzz.analytical.utils.UiState
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class QuestionRepositoryImpl(private val firestore: FirebaseFirestore): QuestionRepository {



    override fun getAllQuestionByID(questionID: String, result: (UiState<List<Questions>>)-> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection("quiz")
            .document(questionID)
            .collection("questions")
            .addSnapshotListener { value, error ->
            error?.let {
                result.invoke(UiState.Error(it.message.toString()))
            }
            value?.let {
                val questions = it.toObjects(Questions::class.java)
                result.invoke(UiState.Success(questions))
            }
        }
    }

    override fun getQuizWithQuestions(
        quizID: String,
        result: (UiState<QuizWithQuestions>) -> Unit
    ) {
        val quizRef = firestore.collection("quiz").document(quizID)
        val questionsRef = quizRef.collection("questions")

        quizRef.get().addOnSuccessListener { quizSnapshot ->
            if (quizSnapshot.exists()) {
                val quiz = quizSnapshot.toObject(Quiz::class.java)
                if (quiz != null) {
                    questionsRef.addSnapshotListener { value, error ->
                        error?.let {
                            result.invoke(UiState.Error(it.message.toString()))
                        }
                        value?.let {
                            val questions = it.toObjects(Questions::class.java)
                            val quizWithQuestions = QuizWithQuestions(quiz, questions)
                            result.invoke(UiState.Success(quizWithQuestions))
                        }
                    }
                } else {
                    result.invoke(UiState.Error("Quiz not found"))
                }
            } else {
                result.invoke(UiState.Error("Quiz not found"))
            }
        }.addOnFailureListener {
            result.invoke(UiState.Error(it.message.toString()))
        }
    }

    override suspend fun getRandomQuestions(
        gameID : String,
        levelID: String,
        count: Int,
        result: (UiState<List<Questions>>) -> Unit
    ) {
        withContext(NonCancellable) {
            try {
                result.invoke(UiState.Loading)
                Log.d(QUESTION_COLLECTION, "Starting to fetch random questions")
                val questions = firestore.collection(QUESTION_COLLECTION)
                    .whereEqualTo("gameID",gameID)
                    .whereEqualTo("levelID",levelID)
                    .get()
                    .await()
                    .toObjects(Questions::class.java)
                val results = questions.subList(0,count).shuffled()
                Log.d(QUESTION_COLLECTION, "question fetch $questions")
                delay(1000)


                result.invoke(UiState.Success(results))
            } catch (e: CancellationException) {
                Log.e(QUESTION_COLLECTION, "Job was cancelled", e)
                result.invoke(UiState.Error("Operation was cancelled"))
            } catch (e: FirebaseException) {
                Log.e(QUESTION_COLLECTION, e.message, e)
                result.invoke(UiState.Error(e.message.toString()))
            } catch (e: Exception) {
                Log.e(QUESTION_COLLECTION, e.message, e)
                result.invoke(UiState.Error(e.message.toString()))
            }
        }
    }

    companion object {
        const val  QUESTION_SETTINGS = "QuestionSettings"
        const val QUESTION_COLLECTION = "questions"
    }
}