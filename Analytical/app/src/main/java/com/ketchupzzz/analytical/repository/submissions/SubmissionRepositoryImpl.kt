package com.ketchupzzz.analytical.repository.submissions

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ketchupzzz.analytical.models.games.GamesHistory
import com.ketchupzzz.analytical.models.submissions.GroupedSubmissions
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.presentation.main.games.data.LevelsWithSubmissions
import com.ketchupzzz.analytical.utils.UiState
import com.ketchupzzz.analytical.utils.generateRandomString

const val GAME_COLLECTION = "recentlyPlayed"
class SubmissionRepositoryImpl(private val firestore: FirebaseFirestore): SubmissionRepository {


    override suspend fun submitQuiz(submission: Submissions, result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        val  batch = firestore.batch()
        submission.id = firestore.collection(SUBMISSIONS_COLLECTION).document().id
        result.invoke(UiState.Loading)
        firestore.collection(SUBMISSIONS_COLLECTION)
            .document(submission.id ?: "")
            .set(submission)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Submission Successful"))
                } else {
                    result.invoke(UiState.Error("Submission Failed"))
                }

            }.addOnFailureListener {
                result(UiState.Error("Submission Failed"))
            }

    }

    override suspend fun getAllSubmissions(
        id: String,
        result: (UiState<List<Submissions>>) -> Unit
    ) {
        result.invoke(UiState.Loading)
        firestore.collection(SUBMISSIONS_COLLECTION)
            .whereEqualTo("studentID",id)
            .orderBy("createdAt",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.e(SUBMISSIONS_COLLECTION,"${it.message}",it)
                    result.invoke(UiState.Error(it.message.toString()))
                }
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Submissions::class.java)))
                }
            }
    }

    override suspend fun getAllSubmissions(result: (UiState<List<Submissions>>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(SUBMISSIONS_COLLECTION)
            .addSnapshotListener { value, error ->
                value?.let {
                    result.invoke(UiState.Success(it.toObjects(Submissions::class.java)))
                }
            }
    }

//    override suspend fun getAllSubmissions(
//        uid: String,
//        result: (UiState<List<LevelsWithSubmissions>>) -> Unit
//    ) {
//        result.invoke(UiState.Loading)
//        firestore.collection(SUBMISSIONS_COLLECTION)
//            .whereEqualTo("studentID", uid)
//            .orderBy("createdAt", Query.Direction.DESCENDING)
//            .addSnapshotListener { value, error ->
//                error?.let {
//                    Log.e(SUBMISSIONS_COLLECTION, "${it.message}", it)
//                    result.invoke(UiState.Error(it.message.toString()))
//                    return@addSnapshotListener
//                }
//                value?.let {
//                    val levelsWithSubmissions = mutableListOf<LevelsWithSubmissions>()
//                    val submissions = it.toObjects(Submissions::class.java)
//
//                    val groupedSubmissions = submissions.groupBy { it.quizInfo?.levels }
//                    groupedSubmissions.forEach { (levels, submissionsList) ->
//                        val levelsWithSubmission = LevelsWithSubmissions(
//                            levels = levels!!,
//                            submissions = submissionsList
//                        )
//                        levelsWithSubmissions.add(levelsWithSubmission)
//                    }
//                    result.invoke(UiState.Success(levelsWithSubmissions))
//                }
//            }
//    }

    companion object {
        const val SUBMISSIONS_COLLECTION = "submissions"
    }
}