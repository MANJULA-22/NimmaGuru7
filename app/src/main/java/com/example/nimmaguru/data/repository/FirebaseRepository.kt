package com.example.nimmaguru.data.repository

import android.net.Uri
import com.example.nimmaguru.data.model.Guru
import com.example.nimmaguru.data.model.MentorshipClass
import com.example.nimmaguru.data.model.Review
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val isFirebaseInitialized: Boolean
        get() = try {
            FirebaseApp.getInstance()
            true
        } catch (e: Exception) {
            false
        }

    private val firestore by lazy { if (isFirebaseInitialized) FirebaseFirestore.getInstance() else null }
    private val storage by lazy { if (isFirebaseInitialized) FirebaseStorage.getInstance() else null }
    private val guruCollection by lazy { firestore?.collection("gurus") }
    private val reviewCollection by lazy { firestore?.collection("reviews") }
    private val classCollection by lazy { firestore?.collection("classes") }

    // Save or Update Guru Profile
    suspend fun saveGuruProfile(guru: Guru): Result<Unit> {
        return try {
            val collection = guruCollection ?: return Result.failure(Exception("Firebase not initialized"))
            val docRef = if (guru.id.isEmpty()) collection.document() else collection.document(guru.id)
            val finalGuru = guru.copy(id = docRef.id)
            docRef.set(finalGuru).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Upload Profile Image to Firebase Storage
    suspend fun uploadProfileImage(uri: Uri, guruId: String): Result<String> {
        return try {
            val s = storage ?: return Result.failure(Exception("Firebase not initialized"))
            val ref = s.reference.child("profile_images/$guruId.jpg")
            ref.putFile(uri).await()
            val downloadUrl = ref.downloadUrl.await().toString()
            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fetch all Gurus
    suspend fun getAllGurus(): Result<List<Guru>> {
        if (!isFirebaseInitialized) {
            return Result.success(getMockGurus())
        }
        return try {
            val snapshot = guruCollection?.get()?.await()
            val gurusList = snapshot?.toObjects(Guru::class.java) ?: emptyList()
            if (gurusList.isEmpty()) Result.success(getMockGurus()) else Result.success(gurusList)
        } catch (e: Exception) {
            Result.success(getMockGurus())
        }
    }

    // Post a Review
    suspend fun postReview(review: Review): Result<Unit> {
        return try {
            reviewCollection?.add(review)?.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fetch reviews for a specific Guru
    suspend fun getReviewsForGuru(guruId: String): Result<List<Review>> {
        if (!isFirebaseInitialized) return Result.success(emptyList())
        return try {
            val snapshot = reviewCollection
                ?.whereEqualTo("guruId", guruId)
                ?.orderBy("timestamp", Query.Direction.DESCENDING)
                ?.get()?.await()
            val reviews = snapshot?.toObjects(Review::class.java) ?: emptyList()
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Class Calendar Operations ---

    suspend fun scheduleClass(mentorshipClass: MentorshipClass): Result<Unit> {
        return try {
            val collection = classCollection ?: return Result.failure(Exception("Firebase not initialized"))
            val docRef = collection.document()
            collection.document(docRef.id).set(mentorshipClass.copy(id = docRef.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUpcomingClasses(): Result<List<MentorshipClass>> {
        if (!isFirebaseInitialized) return Result.success(getMockClasses())
        return try {
            val snapshot = classCollection
                ?.orderBy("date", Query.Direction.ASCENDING)
                ?.get()?.await()
            val classesList = snapshot?.toObjects(MentorshipClass::class.java) ?: emptyList()
            if (classesList.isEmpty()) Result.success(getMockClasses()) else Result.success(classesList)
        } catch (e: Exception) {
            Result.success(getMockClasses())
        }
    }

    private fun getMockGurus(): List<Guru> {
        return listOf(
            Guru(id = "1", name = "Suresh Kumar", village = "Doddaballapur", skills = listOf("Agriculture", "Gardening"), rating = 4.8, reviewCount = 12, description = "Expert in organic farming and water management."),
            Guru(id = "2", name = "Mala Devi", village = "Channapatna", skills = listOf("Handicrafts", "Carpentry"), rating = 4.9, reviewCount = 25, description = "Master craftswoman specializing in traditional toys."),
            Guru(id = "3", name = "Ramesh Hegde", village = "Sirsi", skills = listOf("Agriculture", "Science"), rating = 4.7, reviewCount = 8, description = "Specialist in areca nut and pepper cultivation."),
            Guru(id = "4", name = "Gowramma", village = "Tumkur", skills = listOf("Yoga", "English"), rating = 4.5, reviewCount = 15, description = "Community yoga teacher for 20 years.")
        )
    }

    private fun getMockClasses(): List<MentorshipClass> {
        return listOf(
            MentorshipClass(
                id = "c1",
                guruId = "1",
                guruName = "Suresh Kumar",
                subject = "Organic Farming (Agriculture)",
                location = "Samudaya Bhavana",
                date = "2025-06-15",
                time = "10:00 AM",
                village = "Doddaballapur"
            ),
            MentorshipClass(
                id = "c2",
                guruId = "4",
                guruName = "Gowramma",
                subject = "Yoga for Wellness",
                location = "Samudaya Bhavana",
                date = "2025-06-16",
                time = "06:30 AM",
                village = "Tumkur"
            ),
            MentorshipClass(
                id = "c3",
                guruId = "2",
                guruName = "Mala Devi",
                subject = "Traditional Toy Making",
                location = "Samudaya Bhavana",
                date = "2025-06-20",
                time = "02:00 PM",
                village = "Channapatna"
            )
        )
    }
}