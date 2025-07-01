package com.yourorg.issuebadge

import com.intellij.ide.util.PropertiesComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

data class Badge(val id: String, val name: String)

object BadgeService {
  private val client = OkHttpClient()
  private const val BASE = "https://app.issuebadge.com/api/v1"

  fun getApiKey(): String? =
    PropertiesComponent.getInstance().getValue("issuebadge.apiKey")

  fun saveApiKey(key: String) {
    PropertiesComponent.getInstance().setValue("issuebadge.apiKey", key)
  }

  fun fetchBadges(apiKey: String): List<Badge> {
    val req = Request.Builder()
      .url("$BASE/badge/getall")
      .header("Authorization", "Bearer $apiKey")
      .build()
    val body = client.newCall(req).execute().body?.string() ?: return emptyList()
    return listOf(Badge("W238GD8PK", "Sample")) // Replace with real parsing
  }

  fun sendBadge(key: String, badgeName: String, name: String, email: String): String {
    val badgeId = fetchBadges(key).find { it.name == badgeName }?.id ?: throw Exception()
    val json = """
      {"name":"$name","email":"$email","badge_id":"$badgeId","idempotency_key":"${UUID.randomUUID()}"}
    """
    val req = Request.Builder()
      .url("$BASE/issue/create")
      .header("Authorization", "Bearer $key")
      .header("Content-Type", "application/json")
      .post(json.toRequestBody("application/json".toMediaType()))
      .build()
    return client.newCall(req).execute().body!!.string()
  }
}
