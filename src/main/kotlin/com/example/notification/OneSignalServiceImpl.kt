package com.example.notification

import com.example.notification.data.Notification
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*

class OneSignalServiceImpl(
    private val client: HttpClient,
    private val apiKey: String
): OneSignalService {

    override suspend fun sendNotification(notification: Notification): Boolean {
        return try {
            client.post<String> {
                url(OneSignalService.NOTIFICATIONS)
                contentType(ContentType.Application.Json)
                println(apiKey)
                header("Authorization", "Basic $apiKey")
                body = notification
            }
            true
        } catch(e: Exception) {
            e.printStackTrace()
            false
        }
    }
}