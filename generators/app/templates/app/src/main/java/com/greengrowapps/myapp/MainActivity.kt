package <%= packageName %>

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.greengrowapps.jhiusers.dto.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
<% if (facebookLogin) { %>
import com.facebook.login.LoginManager
<% } if(googleLogin) { %>
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
<% } %>
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun clearTopIntent(from: Context) : Intent {
            val intent = Intent(from, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

      getJhiUsers().getLogedUser { user -> populateUser(user) }
    }

    private fun populateUser(user: User?) {
      val hView = nav_view.getHeaderView(0)
      val navUser = hView.findViewById<TextView>(R.id.drawer_user_name)
      val navEmail = hView.findViewById<TextView>(R.id.drawer_user_email)
      navUser.text = "${user?.firstName} ${user?.lastName}"
      navEmail.text = "${user?.email}"
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_logout -> {
                performLogout()
            }
            R.id.nav_account -> {
              startActivity(AccountActivity.openIntent(this))
            }
//options-needle
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun performLogout(){
      <% if(googleLogin) { %>
      try {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          .requestServerAuthCode(getString(R.string.google_login_web_client_id))
          .requestEmail()
          .build()
        GoogleSignIn.getClient(this, gso).signOut()
      }
      catch (e:Exception){
        //Ignore
      }
      <% } if(facebookLogin) { %>
      try {
        LoginManager.getInstance().logOut()
      }
      catch (e:Exception){
        //Ignore
      }
      <% } %>
      getJhiUsers().logout()
      startActivity(LoginActivity.clearTopIntent(this))
    }
}
