package <%= packageName %>.core.validation

import android.text.TextUtils

class UserValidation {

    companion object {
        fun isValidEmail(email:String) : Boolean{
            return !TextUtils.isEmpty(email)
        }
        fun isValidFirstName(firstName:String) : Boolean{
            return true
        }
        fun isValidLastName(lastName:String) : Boolean{
            return true
        }
        fun isValidPassword(password:String) : Boolean{
            return !TextUtils.isEmpty(password)
        }
    }
}
