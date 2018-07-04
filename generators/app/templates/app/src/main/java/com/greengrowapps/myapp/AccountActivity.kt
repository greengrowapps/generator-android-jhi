package <%= packageName %>

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.text.TextUtils
import android.view.View

import android.content.Context
import android.content.Intent
import android.widget.Toast
import <%= packageName %>.core.validation.UserValidation
import com.greengrowapps.jhiusers.dto.User
import com.greengrowapps.jhiusers.listeners.OnUpdateUserListener
import com.greengrowapps.jhiusers.listeners.OnChangePasswordListener

import kotlinx.android.synthetic.main.activity_account.*

class AccountActivity() : BaseActivity(), OnUpdateUserListener, OnChangePasswordListener {

    companion object {
        fun openIntent(from:Context): Intent {
            return Intent(from,AccountActivity::class.java)
        }
    }

    private lateinit var user: User
    private var isSaving: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        setupActionBar()

        save_button.setOnClickListener { attemptSave() }

        change_password_button.setOnClickListener{ attemptChangePassword() }

        showProgress(true)
    }

    override fun onResume() {
        super.onResume()
        getJhiUsers().getLogedUser { user -> populateUser(user) }
    }

    private fun populateUser(user: User) {
        this.user = user
        email.setText(user.email)
        first_name.setText(user.firstName)
        last_name.setText(user.lastName)
        showProgress(false)
    }

    private fun setupActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun attemptSave() {
        if (isSaving) {
            return
        }

        email.error = null
        first_name.error = null
        last_name.error = null

        user.email = email.text.toString()
        user.lastName = last_name.text.toString()
        user.firstName = first_name.text.toString()

        var cancel = false
        var focusView: View? = null

        if (!UserValidation.isValidFirstName(user.firstName)) {
            first_name.error = getString(R.string.error_invalid_first_name)
            focusView = first_name
            cancel = true
        }
        if (!UserValidation.isValidLastName(user.lastName)) {
            last_name.error = getString(R.string.error_invalid_last_name)
            focusView = first_name
            cancel = true
        }
        if (!UserValidation.isValidEmail(user.email)) {
            email.error = getString(R.string.error_invalid_email)
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            getJhiUsers().update(user,this)
        }
    }

    private fun attemptChangePassword() {
        isSaving = true
        // Reset errors.
        password.error = null
        repeatPassword.error = null

        val passwordStr = password.text.toString()
        val repeatPasswordStr = repeatPassword.text.toString()

        var cancel = false
        var focusView: View? = null

        if (!UserValidation.isValidPassword(passwordStr)) {
          password.error = getString(R.string.error_invalid_password)
          focusView = password
          cancel = true
        }
        if (!passwordStr.equals(repeatPasswordStr)) {
          repeatPassword.error = getString(R.string.error_password_not_match)
          focusView = repeatPassword
          cancel = true
        }

        if (cancel) {
          focusView?.requestFocus()
        } else {
          showProgress(true)
          getJhiUsers().changePassword(passwordStr,this)
        }
    }

    override fun onUpdateUserSuccess(user: User?) {
        isSaving=false
        showProgress(false)
        Toast.makeText(this,R.string.save_success,Toast.LENGTH_SHORT).show()
    }

    override fun onUpdateUserError(error: String?) {
        isSaving=false
        showProgress(false)
        Toast.makeText(this,R.string.save_error,Toast.LENGTH_SHORT).show()
    }

    override fun onChangePasswordSuccess() {
      isSaving=false
      showProgress(false)
      Toast.makeText(this,R.string.save_success,Toast.LENGTH_SHORT).show()
    }

    override fun onChangePasswordError(error: String?) {
      isSaving=false
      showProgress(false)
      Toast.makeText(this,R.string.save_error,Toast.LENGTH_SHORT).show()
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        account_form.visibility = if (show) View.GONE else View.VISIBLE
        account_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        account_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        account_progress.visibility = if (show) View.VISIBLE else View.GONE
        account_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        account_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }
}
