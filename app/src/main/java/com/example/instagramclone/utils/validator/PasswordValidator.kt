package com.example.instagramclone.utils.validator

import com.example.instagramclone.R

class PasswordValidator(val password: String) : BaseValidator() {
    private val minPasswordLength = 6
    private val maxPasswordLength = 12

    override fun validate(): ValidateResult {
        if (password.length < minPasswordLength)
            return ValidateResult(false, R.string.text_validation_error_min_pass_length)
        if (password.length > maxPasswordLength)
            return ValidateResult(false, R.string.text_validation_error_max_pass_length)
        return ValidateResult(true, 0)
    }
}