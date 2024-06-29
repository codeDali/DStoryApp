package com.dicoding.picodiploma.loginwithanimation.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.loginwithanimation.R

class Password(
    context: Context, attrs: AttributeSet
) : AppCompatEditText(context,attrs), View.OnTouchListener {

    private var hideButtonImage: Drawable
    private var showButtonImage: Drawable
    private var isPasswordVisible = false

    init {
        hideButtonImage = ContextCompat.getDrawable(context, R.drawable.hide) as Drawable
        showButtonImage = ContextCompat.getDrawable(context, R.drawable.show) as Drawable
        setOnTouchListener(this)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    error = if (it.length < 8) {
                        context.getString(R.string.error_password)
                    } else {
                        null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                showTogglePasswordVisibilityButton()
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        showTogglePasswordVisibilityButton()
    }

    private fun showTogglePasswordVisibilityButton() {
        val buttonDrawable = if (isPasswordVisible) hideButtonImage else showButtonImage
        setButtonDrawables(endOfTheText = buttonDrawable)
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (compoundDrawables[2] != null) {
            val buttonStart: Float
            val buttonEnd: Float
            var isButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                buttonEnd = (hideButtonImage.intrinsicWidth + paddingStart).toFloat()
                if (event != null) {
                    when {
                        event.x < buttonEnd -> isButtonClicked = true
                    }
                }
            } else {
                buttonStart = (width - paddingEnd - hideButtonImage.intrinsicWidth).toFloat()
                if (event != null) {
                    when {
                        event.x > buttonStart -> isButtonClicked = true
                    }
                }
            }
            if (isButtonClicked) {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        showTogglePasswordVisibilityButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        isPasswordVisible = !isPasswordVisible
                        updatePasswordVisibility()
                        showTogglePasswordVisibilityButton()
                        return true
                    }
                    else -> return false
                }
            } else return false
        }
        return false
    }

    private fun updatePasswordVisibility() {
        if (isPasswordVisible) {
            inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        setSelection(text?.length ?: 0)
    }
}