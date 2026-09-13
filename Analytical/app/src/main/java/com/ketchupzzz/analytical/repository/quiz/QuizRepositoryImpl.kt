package com.ketchupzzz.analytical.repository.quiz

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.quiz.CategoryWithQuiz
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.models.submissions.RecentlyPlayed
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.presentation.main.games.data.QuizWithLevels
import com.ketchupzzz.analytical.repository.submissions.SubmissionRepositoryImpl
import com.ketchupzzz.analytical.utils.UiState
import com.ketchupzzz.analytical.utils.createLog
import kotlinx.coroutines.tasks.await


class QuizRepositoryImpl(private val firestore : FirebaseFirestore): QuizRepository {
    override suspend fun getAllGames(result: (UiState<List<Quiz>>) -> Unit) {
        firestore.collection(QUIZ_COLLECTION)
            .addSnapshotListener { value, error ->
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Quiz::class.java)))
                }
                error?.let {
                    result.invoke(UiState.Error(it.localizedMessage.toString()))
                }
            }
    }

    override suspend fun getAllQuiz(result: (UiState<List<CategoryWithQuiz>>) -> Unit) {
        result.invoke(UiState.Loading)
        Log.d(QUIZ_COLLECTION,"LOADING")
        firestore.collection(QUIZ_COLLECTION).addSnapshotListener { value, error ->
            error?.let {
                Log.d(QUIZ_COLLECTION,error.message.toString())
                result.invoke(UiState.Error(error.message.toString()))
            }

            value?.let {
                val quizList = it.toObjects(Quiz::class.java)
                Log.d(QUIZ_COLLECTION,quizList.toString())
                val categories: List<String> = listOf("ALL") + Category.entries.map { category -> category.name }
                val categoryWithQuiz = categories.map { category ->
                    val list = if (category == "ALL") quizList
                    else quizList.filter { quiz -> quiz.category?.name == category }
                    CategoryWithQuiz(
                        category.replace("_"," "),
                        list
                    )
                }
                Log.d(QUIZ_COLLECTION,categoryWithQuiz.toString())
                result.invoke(UiState.Success(categoryWithQuiz))
            }
        }
    }

    override suspend fun getQuizByID(id: String, result: (UiState<QuizWithLevels>) -> Unit) {
        try {
            result.invoke(UiState.Loading)
            val quizSnapshot = firestore.collection(QUIZ_COLLECTION)
                .document(id)
                .get().await()

            val levelsSnapshot = firestore.collection(QUIZ_COLLECTION)
                .document(id)
                .collection(LEVELS_COLLECTION)
                .orderBy("levelNumber",Query.Direction.ASCENDING)
                .get().await()
            val submissions = firestore.collection(SubmissionRepositoryImpl.SUBMISSIONS_COLLECTION)
                .whereEqualTo("quizInfo.id",id)
                .orderBy("createdAt",Query.Direction.DESCENDING)
                .get().await()

            val quiz = QuizWithLevels(
                quiz = quizSnapshot.toObject(Quiz::class.java),
                levels = levelsSnapshot.toObjects(Levels::class.java).sortedBy { it.levelNumber },
                submissions = submissions.toObjects(Submissions::class.java)
            )
            result.invoke(UiState.Success(quiz))
        } catch (e: Exception) {
            Log.d(QUIZ_COLLECTION,e.toString())
            result.invoke(UiState.Error(e.message.toString()))
        }
    }

    override suspend fun getAllQuizBySchoolLevel(schoolLevel: String,result: (UiState<List<Quiz>>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(QUIZ_COLLECTION)
            .addSnapshotListener { value, error ->
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Quiz::class.java)))
                }
                error?.let {
                    result.invoke(UiState.Error(it.message.toString()))
                }
            }
    }

    override suspend fun getAllQuizBySchoolLevelAndCategory(
        schoolLevel: String,
        category: String,
        result: (UiState<List<Quiz>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(QUIZ_COLLECTION)
            .whereEqualTo("category",category)
            .addSnapshotListener { value, error ->
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Quiz::class.java)))
                }
                error?.let {
                    Log.d(QUIZ_COLLECTION,it.message.toString())
                    result.invoke(UiState.Error(it.message.toString()))
                }
            }
    }


    override suspend fun getRecentlyPlayedGame(
        userID: String,
        result: (UiState<RecentlyPlayed?>) -> Unit
    ) {
        try {
            val recentSubmission = firestore.collection("submissions")
                .whereEqualTo("studentID", userID)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()
                .toObjects(Submissions::class.java)

            QUIZ_COLLECTION.createLog(recentSubmission.toString())
            if (recentSubmission.isEmpty()) {
                result(UiState.Success(null))
                return
            }


            val recentGame = recentSubmission.first()


            val gameData = firestore.collection(QUIZ_COLLECTION)
                .document(recentGame.id!!)
                .get()
                .await()
                .toObject(Quiz::class.java)
            QUIZ_COLLECTION.createLog(gameData.toString())
            if (gameData == null) {
                result(UiState.Success(null))
                return
            }


            val recentlyPlayed = RecentlyPlayed(
                name = gameData.title,
                type = gameData.category,
                subject = gameData.subject,
                image = gameData.cover_photo,
                maxLevel = gameData.levels,
                currentLevel = recentGame.quizInfo?.levels?.levelNumber,
                gameDate = gameData.createdAt
            )
            QUIZ_COLLECTION.createLog(recentlyPlayed.toString())
            result(UiState.Success(recentlyPlayed))

        } catch (e: FirebaseException) {
            QUIZ_COLLECTION.createLog(e.message.toString())
            result(UiState.Error("Firebase error: ${e.message}"))
        } catch (e: Exception) {
            QUIZ_COLLECTION.createLog(e.message.toString())
            result(UiState.Error("An error occurred: ${e.message}"))
        }
    }


    companion object {
        const val QUIZ_COLLECTION = "quiz"
        const val LEVELS_COLLECTION = "levels"
    }
}