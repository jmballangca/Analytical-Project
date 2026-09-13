package com.ketchupzzz.analytical.repository.user

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import com.ketchupzzz.analytical.models.SchoolLevel
import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.StudentsWithSubmissions
import com.ketchupzzz.analytical.utils.UiState
import kotlinx.coroutines.flow.Flow

interface StudentRepository {


    fun getStudent() : Flow<Students?>
    fun setStudent(student : Students ? = null)

    fun currentUser() : FirebaseUser?
    fun login(studentID : String, password : String ,result : (UiState<Students>) -> Unit)

    fun register(
        studentID : String,
        firstName : String,
        middleName: String,
        lastName : String,
        schoolLevel: SchoolLevel,
        email : String,
        password : String,
        result : (UiState<String>) -> Unit
    )
    fun saveStudent(
        student : Students,
        password : String,
        result : (UiState<String>) -> Unit
    )

    fun getStudentByID(studentID : String, result : (UiState<Boolean>) -> Unit)

    fun getUserByEmail(email : String, result : (UiState<Students>) -> Unit)

    suspend  fun logout(result : (UiState<String>) -> Unit)
    suspend fun changePassword(oldPassword : String,newPassword : String,result: (UiState<String>) -> Unit)

    suspend fun editProfile(
        id : String,
        firstName: String,
        middleName: String,
        lastName: String,
        result: (UiState<String>) -> Unit
    )

    suspend fun getStudents(result: (UiState<List<Students>>) -> Unit)

    suspend fun updateProfile(
        uid : String,
        uri : Uri,
        result: (UiState<String>) -> Unit
    )
    suspend fun getStudentProfile(result: (UiState<Students?>) -> Unit)


    suspend fun forgotPassword(email: String) : Result<String>

}