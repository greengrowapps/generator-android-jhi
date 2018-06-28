package <%= packageName %>

import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.greengrowapps.jhiusers.listeners.OnLoginListener
import com.facebook.AccessToken


class SplashActivity : BaseActivity(), OnLoginListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val users = getJhiUsers()

        if(users.isLoginSaved){
            users.autoLogin(this)
        }
        else if(users.isGoogleLoginSaved){
            googleSignInSetup()
        }
        else if(users.isFacebookLoginSaved){
            facebookSignInSetup()
        }
        else{
            toLoginActivity()
        }
    }

    private fun facebookSignInSetup() {
        val accessToken = AccessToken.getCurrentAccessToken()
        if(accessToken!=null){
            getJhiUsers().loginWithFacebook(accessToken.token,this)
        }
    }

    override fun onLoginSuccess() {
        toMainActivity()
    }

    private fun toMainActivity() {
        startActivity(MainActivity.clearTopIntent(this))
    }

    override fun onLoginError(error: String?) {
        Toast.makeText(this,getString(R.string.loginErrorMsg), Toast.LENGTH_SHORT).show()
        toLoginActivity()
    }

    private fun toLoginActivity() {
        startActivity(LoginActivity.clearTopIntent(this))
    }

    private fun googleSignInSetup() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        mGoogleSignInClient?.silentSignIn()?.addOnSuccessListener(this, OnSuccessListener<GoogleSignInAccount> { account -> handleSignInResult(account) } )
    }

    private fun handleSignInResult(account: GoogleSignInAccount){
        getJhiUsers().loginWithGoogle(account.serverAuthCode?:"", this)
    }
}
