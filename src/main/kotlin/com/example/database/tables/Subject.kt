package com.example.database.tables

import com.example.model.data.Subject
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

object Subjects : IntIdTable() {
    val name = varchar("name", 255)
    val course = integer("course")
}

class SubjectDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SubjectDao>(Subjects)

    var name by Subjects.name
    var course by Subjects.course
    var mentors by UserDao via MentorsSubjects
    fun toSubject() = Subject(
        name = name,
        course = course,
        id = id.value,
        mentors = mentors.map { it.toUser() }
    )
}


object MentorsSubjects : Table() {
    val mentor = reference("mentor", Users)
    val subject = reference("subject", Subjects)
    override val primaryKey = PrimaryKey(mentor, subject, name = "PK_StarWarsFilmActors")
}