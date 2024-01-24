package com.example.instagramclone.utils.validator

import android.net.Uri
import com.example.instagramclone.R

class EmptyUriValidation (val uri: Uri?) : BaseValidator() {

    override fun validate(): ValidateResult {
          uri?.let{
                return ValidateResult(true, 0)
            }
            return ValidateResult(false, R.string.text_validation_empty_uri)

    }
}