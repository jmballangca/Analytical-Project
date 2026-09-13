package com.ketchupzzz.analytical.repository.user

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.ketchupzzz.analytical.models.SchoolLevel
import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.StudentsWithSubmissions
import com.ketchupzzz.analytical.utils.UiState
import com.ketchupzzz.analytical.utils.generateRandomString
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.tasks.await


class StudentRepositoryImpl(
    private val auth : FirebaseAuth,
    private val firestore : FirebaseFirestore,
    private val storage : FirebaseStorage
): StudentRepository {

    private val student = MutableStateFlow<Students?>(null)

    override fun getStudent(): Flow<Students?> {
        return student
    }

    override fun setStudent(student: Students?) {
        this.student.value = student
    }


    override fun currentUser(): FirebaseUser ? {
        return auth.currentUser
    }

    override fun login(studentID: String, password: String, result: (UiState<Students>) -> Unit) {
        val document = firestore.collection(STUDENTS_COLLECTION).document(studentID)
        result.invoke(UiState.Loading)
        document.get().addOnSuccessListener {
            if (it.exists()) {
                val student = it.toObject(Students::class.java)
                student?.let {stud->
                    auth.signInWithEmailAndPassword(student.email ?: "", password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            setStudent(stud)
                            result.invoke(UiState.Success(stud))
                        } else {
                            result.invoke(UiState.Error("Failed to login"))
                        }
                    }.addOnFailureListener {
                        result.invoke(UiState.Error("${it.message}"))
                    }
                }


            } else {
                result(UiState.Error("User not exists"))

            }
        }.addOnFailureListener {
            result(UiState.Error("${it.message}"))
        }
    }



    override fun register(
        studentID: String,
        firstName: String,
        middleName: String,
        lastName: String,
        schoolLevel: SchoolLevel,
        email: String,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        result(UiState.Loading)
        val document = firestore.collection(STUDENTS_COLLECTION).document(studentID)
        document.get().addOnSuccessListener {
            if (it.exists()) {
                result(UiState.Error("Student ID already exists"))
            } else {
                val student = Students(
                    id = studentID,
                    fname = firstName,
                    mname = middleName,
                    lname = lastName,
                    schoolLevel  = schoolLevel,
                    email = email
                )
                saveStudent(student,password,result)
            }
        }.addOnFailureListener {
            result(UiState.Error("${it.message}"))
        }
    }

    override fun saveStudent(
        student: Students,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        val document = firestore.collection(STUDENTS_COLLECTION).document(student.id ?: "")
        document.set(student)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.createUserWithEmailAndPassword(
                        student.email ?: "",
                        password
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            setStudent(student)
                            result(UiState.Success("Student saved successfully"))
                        } else {
                            document.delete()
                            result(UiState.Error("Failed to save student"))
                        }
                    }
                } else {
                    result(UiState.Error("Failed to save student"))
                }
            }.addOnFailureListener {
                result(UiState.Error("${it.message}"))
            }
    }

    override fun getStudentByID(studentID: String, result: (UiState<Boolean>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(STUDENTS_COLLECTION).document(studentID).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    result.invoke(UiState.Error("Student ID already exists"))
                } else {
                    result.invoke(UiState.Success(true))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Error("${it.message}"))
            }
    }

    override fun getUserByEmail(email: String, result: (UiState<Students>) -> Unit) {
        result.invoke(UiState.Loading)
        firestore.collection(STUDENTS_COLLECTION).whereEqualTo("email",email)
            .limit(1)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.isEmpty) {
                        result.invoke(UiState.Error("User not found"))
                        auth.currentUser?.let { currentUser ->
                            currentUser.delete()
                            auth.signOut()
                        }
                    } else {
                        setStudent(it.result.toObjects(Students::class.java)[0])
                        result.invoke(UiState.Success(it.result.toObjects(Students::class.java)[0]))
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Error("${it.message}"))
            }
    }

    override suspend  fun logout(result: (UiState<String>) -> Unit) {
        result.invoke(UiState.Loading)
        auth.signOut()
        delay(1000)
        setStudent(null)
        result.invoke(UiState.Success("Logged out successfully"))
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        result: (UiState<String>) -> Unit
    ) {
        try {
            result.invoke(UiState.Loading)
            val currentUser = auth.currentUser

            if (currentUser != null) {
                val credential = EmailAuthProvider.getCredential(currentUser.email!!, oldPassword)
                currentUser.reauthenticate(credential).await()
                currentUser.updatePassword(newPassword).await()

                result.invoke(UiState.Success("Password updated successfully."))
            } else {

                result.invoke(UiState.Error("User is not logged in."))
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {

            result.invoke(UiState.Error("Old password is incorrect."))
        } catch (e: Exception) {
            result.invoke(UiState.Error(e.message.toString()))
        }
    }

    override suspend fun editProfile(
        id : String,
        firstName: String,
        middleName: String,
        lastName: String,
        result: (UiState<String>) -> Unit
    ) {
       // fname,mname.lname

        firestore
            .collection(STUDENTS_COLLECTION)
            .document(id)
            .update(
                "fname",firstName,
                "mname",middleName,
                "lname",lastName
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    result.invoke(UiState.Success("Successfully Updated!"))
                } else {
                    result.invoke(UiState.Error("Unknown error"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Error(it.message.toString()))
            }

    }

    override suspend fun getStudents(result: (UiState<List<Students>>) -> Unit) {
        firestore.collection(
            STUDENTS_COLLECTION
        ).addSnapshotListener { value, error ->
            value?.let {
                val students = it.toObjects(Students::class.java)
                result.invoke(UiState.Success(students))
            }
        }
    }

    override suspend fun updateProfile(
        uid : String,
        uri: Uri,
        result: (UiState<String>) -> Unit
    ) {
        try {
            val storageRef = storage.reference.child(
                "${STUDENTS_COLLECTION}/${uid}/${generateRandomString(10)}"
            )
            val uploadTask = storageRef.putFile(uri).await()
            val downloadUrl =storageRef.downloadUrl.await()
            firestore.collection(STUDENTS_COLLECTION)
                .document(uid)
                .update(
                    "profile",downloadUrl
                ).await()
            result(UiState.Success("Successfully Updated!"))
        } catch (e: StorageException) {
            result(UiState.Error("Storage error: ${e.message}"))
        } catch (e: FirebaseFirestoreException) {
            result(UiState.Error("Firestore error: ${e.message}"))
        } catch (e: Exception) {
            result(UiState.Error("Unexpected error: ${e.message}"))
        }
    }

    override suspend fun getStudentProfile(result : (UiState<Students?>) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            result(UiState.Error("User not found!"))
            return
        }
        firestore.collection(STUDENTS_COLLECTION)
            .whereEqualTo("email",currentUser.email)
            .limit(1)
            .addSnapshotListener { value, error ->
                error?.let {
                    result(UiState.Error(it.message.toString()))
                }
                value?.let {
                    val user =    it.toObjects(Students::class.java)
                    result(UiState.Success(
                        user.firstOrNull()
                    ))
                }
            }
    }

    override suspend fun forgotPassword(email: String): Result<String> {
        return  try {
            val result = auth.sendPasswordResetEmail(email).await()
            Result.success("We sent an email to ${email}.")
        } catch (e : Exception) {
            Result.failure(e)
        }
    }


    companion object {
        const val STUDENTS_COLLECTION = "students"
    }
}