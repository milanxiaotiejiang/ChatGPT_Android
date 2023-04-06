package com.milan.chat.openai.gpt.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.milan.chat.openai.gpt.R

/**
 * User: milan
 * Time: 2023/4/6 16:55
 * Des:
 */
abstract class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppMaterialTheme {
                ProvideContent()
            }
        }
    }

    @Composable
    abstract fun ProvideContent()


    @Composable
    fun AppMaterialTheme(content: @Composable () -> Unit) {
        val colorPrimaryDark = colorResource(id = R.color.colorPrimaryDark)
        val colorPrimary = colorResource(id = R.color.colorPrimary)
        val colorAccent = colorResource(id = R.color.colorAccent)

        val myColors = darkColors(
            primary = colorPrimary, primaryVariant = colorPrimaryDark, secondary = colorAccent
        )

        MaterialTheme(
            colors = myColors, content = content
        )
    }
}