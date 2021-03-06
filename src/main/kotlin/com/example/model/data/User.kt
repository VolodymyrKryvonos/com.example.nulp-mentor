package com.example.model.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int = -1,
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val course: Int = -1,
    val specialty: String = "",
    val isMentor: Boolean = false,
    val registerDate: Long = System.currentTimeMillis(),
    val rate: Float = 0F,
){
    companion object {
        val DefaultUser = User()
    }
}



@Serializable
data class Mentor(
    val id: Int = -1,
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val course: Int = -1,
    val specialty: String = "",
    val isMentor: Boolean = false,
    val registerDate: Long = System.currentTimeMillis(),
    val rate: Float = 0F,
    var mentees: List<User> = emptyList(),
    var subjects: List<SubjectWithoutMentors> = emptyList()
)

@Serializable
data class UserAllData(
    val id: Int = -1,
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val course: Int = -1,
    val specialty: String = "",
    val isMentor: Boolean = false,
    val registerDate: Long = System.currentTimeMillis(),
    val rate: Float = 0F,
    val token: String? = null,
    val comments: List<Comment> = emptyList(),
    val userApplications: List<Application> = emptyList(),
    val appeals: List<Application> = emptyList(),
    val requests: List<Request> = emptyList(),
    var subjects: List<SubjectWithoutMentors> = emptyList()
)
