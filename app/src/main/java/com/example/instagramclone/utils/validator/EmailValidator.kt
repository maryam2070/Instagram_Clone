package com.example.instagramclone.utils.validator

import android.text.TextUtils
import com.example.instagramclone.R

class EmailValidator(val email: String) : BaseValidator() {
    override fun validate(): ValidateResult {
        val isValid =
            !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()
        return ValidateResult(
            isValid,
            if (isValid) 0 else R.string.text_validation_error_email
        )
    }
}