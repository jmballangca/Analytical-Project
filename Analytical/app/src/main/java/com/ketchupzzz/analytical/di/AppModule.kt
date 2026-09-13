package com.ketchupzzz.analytical.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.ketchupzzz.analytical.repository.questions.QuestionRepository
import com.ketchupzzz.analytical.repository.questions.QuestionRepositoryImpl
import com.ketchupzzz.analytical.repository.quiz.QuizRepository
import com.ketchupzzz.analytical.repository.quiz.QuizRepositoryImpl
import com.ketchupzzz.analytical.repository.submissions.SubmissionRepository
import com.ketchupzzz.analytical.repository.submissions.SubmissionRepositoryImpl
import com.ketchupzzz.analytical.repository.user.StudentRepository
import com.ketchupzzz.analytical.repository.user.StudentRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseAuth: FirebaseAuth,
        firestore : FirebaseFirestore,
        storage: FirebaseStorage
    ) : StudentRepository {
        return  StudentRepositoryImpl(firebaseAuth,firestore,storage)
    }


    @Provides
    @Singleton
    fun provideQuizRepository(
        firestore: FirebaseFirestore,
    ) : QuizRepository {
        return  QuizRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideQuestionRepository(
        firestore: FirebaseFirestore,
    ) : QuestionRepository {
        return  QuestionRepositoryImpl(firestore)
    }

    @Provides
    @Singleton
    fun provideSubmissionRepository(
        firestore: FirebaseFirestore,
    ) : SubmissionRepository {
        return  SubmissionRepositoryImpl(firestore)
    }
}