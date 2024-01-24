package com.example.instagramclone.utils.validator


import com.example.instagramclone.R

class EmptyValidator(private val input: String) : BaseValidator() {
    override fun validate(): ValidateResult {
        val isValid = input.isNotEmpty()
        return ValidateResult(
            isValid,
            if (isValid) 0 else R.string.text_validation_error_empty_field
        )
    }
}