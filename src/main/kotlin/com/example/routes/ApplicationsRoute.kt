package com.example.routes

import com.example.database.model.Resources
import com.example.database.services.ApplicationService
import com.example.model.requests.*
import com.example.notification.OneSignalService
import com.example.notification.OneSignalServiceImpl
import com.example.notification.data.Notification
import com.example.notification.data.NotificationMessage
import com.example.res.StringRes
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.applications(applicationService: ApplicationService, notificationService: OneSignalServiceImpl) {

    post("/makeAnApplication") {
        val body = call.receive<ApplicationBody>()
        val resource = applicationService.makeAnApplication(body)
        if (resource is Resources.Error) {
            call.respond(HttpStatusCode.BadRequest,  Error(resource.message?:"", HttpStatusCode.BadRequest.value))
            return@post
        }
        if (resource.data != null) {
            if (notificationService.sendNotification(
                Notification(
                    includeExternalUserIds = listOf(resource.data),
                    headings = NotificationMessage(en = "test"),
                    contents = NotificationMessage(en = "test"),
                    appId = OneSignalService.ONESIGNAL_APP_ID
                )
            )){
                call.respond(HttpStatusCode.OK)
            }else{
                call.respond(HttpStatusCode.InternalServerError,Error(StringRes.somethingWentWrong, HttpStatusCode.InternalServerError.value))
            }
        }
    }

    post("/replyOnApplication") {
        val body = call.receive<ApplicationReply>()
        val resource = applicationService.replyOnApplication(body)
        if (resource is Resources.Error) {
            call.respond(HttpStatusCode.BadRequest,  Error(resource.message?:"", HttpStatusCode.BadRequest.value))
            return@post
        }
        if (resource.data != null) {
            if(notificationService.sendNotification(
                Notification(
                    includeExternalUserIds = listOf(resource.data.token ),
                    headings = NotificationMessage(en = resource.data.pushMessage),
                    contents = NotificationMessage(en = resource.data.comment),
                    appId = OneSignalService.ONESIGNAL_APP_ID
                )
            )){
                call.respond(HttpStatusCode.OK)
            }else{
                call.respond(HttpStatusCode.InternalServerError, Error(StringRes.somethingWentWrong, HttpStatusCode.InternalServerError.value))
            }
        }
    }

    get("/applications/{mentorId}") {
        val id = try {
            call.parameters["mentorId"]?.toInt()
        } catch (e: NumberFormatException) {
            call.respond(HttpStatusCode.NotFound,Error(StringRes.somethingWentWrong, HttpStatusCode.NotFound.value))
            return@get
        }
        call.respond(HttpStatusCode.OK, applicationService.getMentorsApplication(id ?: -1).data ?: emptyList())
    }

    post("/applications") {
        val body = call.receive<List<Int>>()
        val resources = applicationService.getApplications(body)
        if (resources is Resources.Error) {
            call.respond(HttpStatusCode.NotFound,Error(resources.message ?: "", HttpStatusCode.NotFound.value))
            return@post
        }
        call.respond(HttpStatusCode.OK, resources.data ?: emptyList())
    }

    post("/makeMentorRequest") {
        val body = call.receive<MentorRequestBody>()
        val resources = applicationService.makeMentorRequest(body)
        if (resources is Resources.Error) {
            call.respond(HttpStatusCode.NotFound,Error(resources.message ?: "", HttpStatusCode.NotFound.value))
            return@post
        }
        call.respond(HttpStatusCode.OK)
    }

    post("/cancelMentorRequest") {
        val body = call.receive<CancelMentorRequestBody>()
        val resources = applicationService.cancelMentorRequest(body)
        if (resources is Resources.Error) {
            call.respond(HttpStatusCode.BadRequest,Error(resources.message ?: "", HttpStatusCode.BadRequest.value))
            return@post
        }
        call.respond(HttpStatusCode.OK)
    }

    post("/mentorReplyOnRequest") {
        val body = call.receive<MentorRequestReplyBody>()
        val resource = applicationService.replyToMentorRequest(body)
        if (resource is Resources.Error) {
            call.respond(HttpStatusCode.BadRequest,Error(resource.message ?: "", HttpStatusCode.BadRequest.value))
            return@post
        }
        if (resource.data != null) {
            if(notificationService.sendNotification(
                Notification(
                    includeExternalUserIds = listOf(resource.data.token ),
                    headings = NotificationMessage(en = resource.data.pushMessage),
                    contents = NotificationMessage(en = resource.data.comment),
                    appId = OneSignalService.ONESIGNAL_APP_ID
                )
            )){
                call.respond(HttpStatusCode.OK)
            }else{
                call.respond(HttpStatusCode.InternalServerError, Error(StringRes.somethingWentWrong, HttpStatusCode.InternalServerError.value))
            }
        }
    }


}