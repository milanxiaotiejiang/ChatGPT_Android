package com.milan.chat.openai.gpt.ui.api

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.milan.chat.openai.gpt.R
import com.milan.chat.openai.gpt.ui.ComposeActivity
import com.seabreeze.robot.base.ext.tool.toast
import com.seabreeze.robot.data.settings.DataSettings.open_ai_api

/**
 * User: milan
 * Time: 2023/4/6 16:39
 * Des:
 */
class ApiActivity : ComposeActivity() {

    lateinit var apiViewModel: ApiViewModel

    @Composable
    override fun ProvideContent() {
        apiViewModel = viewModel()
        val used = remember { mutableStateOf(false) }

        apiViewModel.resultLiveData.observe(this) { result ->
            used.value = result

            toast { "Unavailable ... " }
            if (result) {
                finish()
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            val inputText = remember { mutableStateOf(open_ai_api) }
            BuildTopAppBar(inputText)
            BuildInputField(inputText)
        }
    }

    @Composable
    private fun BuildTopAppBar(inputText: MutableState<String>) {
        TopAppBar(
            title = { Text(stringResource(R.string.app_name)) },
            backgroundColor = colorResource(id = R.color.colorPrimary),
            navigationIcon = {
                IconButton(onClick = { finish() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { verifySave(inputText.value) }) {
                    Icon(Icons.Filled.Done, contentDescription = "Done")
                }
            }
        )
    }

    @Composable
    private fun BuildInputField(inputText: MutableState<String>) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = inputText.value,
                onValueChange = {
                    inputText.value = it
                },
                label = { Text("Enter api") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    backgroundColor = Color.White,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    disabledTextColor = Color.Black,
                    disabledLabelColor = Color.Black,
                    errorBorderColor = Color.Red,
                    errorLabelColor = Color.Red,
                    leadingIconColor = Color.Black,
                    trailingIconColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
            )
        }
    }

    private fun verifySave(text: String) {
        open_ai_api = text
        apiViewModel.tryRequest()
    }
}
