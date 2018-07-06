package <%= packageName %>.core

import android.util.Log
import android.content.SharedPreferences
import com.greengrowapps.ggarest.GgaRest
import com.greengrowapps.ggarest.serialization.Serializer
import com.greengrowapps.jhiusers.JhiUsers
import <%= packageName %>.core.cache.CombinedCache
import <%= packageName %>.core.config.CoreConfiguration
import <%= packageName %>.core.data.firebase_token.FirebaseTokenDto
import com.greengrowapps.ggarest.listeners.OnObjResponseListener
import com.greengrowapps.jhiusers.listeners.OnLoginStatusListener
<% if(pushNotification) { %>
import com.google.firebase.iid.FirebaseInstanceId
<% } %>
//import-needle

class Core(private val jhiUsers: JhiUsers, private val configuration: CoreConfiguration, private val preferences: SharedPreferences, private val serializer: Serializer) {

<% if(pushNotifications) { %>
    init {
      jhiUsers.setOnLoginStatusListener(LoginStatusListener(this))
    }

    companion object {
      private const val FIREBASE_TOKEN_KEY = "FirebaseToken"
      private const val FIREBASE_TOKEN_CONNECTED_WITH_USER_KEY = "FirebaseTokenConnectedWithUser"
    }

    fun sendFirebaseToken(token: String) {

      saveFirebaseTokenConnectedWithUser(false)

      val dto = FirebaseTokenDto()
      dto.token = token

      GgaRest.ws()
        .post("${configuration.serverUrl}/api/firebase-tokens")
        .withBody(dto)
        .onResponse(FirebaseTokenDto::class.java, 201, OnObjResponseListener { code, responseDto, fullResponse -> onFirebaseTokenSended(responseDto) })
        .execute()
    }

    private fun onFirebaseTokenSended(responseDto: FirebaseTokenDto?) {
      responseDto?.let {
        saveFirebaseToken(it)
        val authToken = jhiUsers.authToken
        if ("" != authToken) {
          connectFirebaseTokenWithUser(it, authToken);
        }
      }

    }

    private fun connectFirebaseTokenWithUser(firebaseToken: FirebaseTokenDto, authToken: String?) {
      GgaRest.ws()
        .put("${configuration.serverUrl}/api/firebase-tokens")
        .addHeader("Authorization", "Bearer ${authToken}")
        .withBody(firebaseToken)
        .onSuccess(FirebaseTokenDto::class.java, OnObjResponseListener { code, responseDto, fullResponse -> saveFirebaseTokenConnectedWithUser(true) })
        .execute()
    }

    private fun saveFirebaseTokenConnectedWithUser(connected: Boolean) {
      preferences.edit().putBoolean(FIREBASE_TOKEN_CONNECTED_WITH_USER_KEY, connected).apply()
    }

    private fun isFirebaseTokenConnectedWithUser(): Boolean {
      return preferences.getBoolean(FIREBASE_TOKEN_CONNECTED_WITH_USER_KEY, false)
    }


    private fun saveFirebaseToken(dto: FirebaseTokenDto) {
      preferences.edit().putString(FIREBASE_TOKEN_KEY, serializer.fromObject(dto)).apply()
    }

    private fun loadFirebaseToken(): FirebaseTokenDto? {
      if (preferences.contains(FIREBASE_TOKEN_KEY)) {
        val json = preferences.getString(FIREBASE_TOKEN_KEY, null)
        try {
          return serializer.fromString(json, FirebaseTokenDto::class.java)
        } catch (e: Exception) {
          //Ignore
        }
      }
      return null
    }

    internal class LoginStatusListener(private val parent: Core) : OnLoginStatusListener {
      override fun onLogout() {
        parent.saveFirebaseTokenConnectedWithUser(false)
      }

      override fun onLogin(authToken: String?) {
        authToken?.let { parent.onUserLoggedIn(it) }
      }

    }

    private fun onUserLoggedIn(authToken: String) {
      loadFirebaseToken()?.let {
        if(!isFirebaseTokenConnectedWithUser()) {
          connectFirebaseTokenWithUser(it, authToken)
        }
      }?: run {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { sendFirebaseToken(it.token) }
      }
    }
<% } %>

  //services-needle

}
