package com.seabreeze.robot.data.net.bean.response

data class ResponseChat(
    val id: String,
    val `object`: String,
    val created: Int,
    val model: String,
    val usage: Usage,
    val choices: List<Choice>
) {
    data class Usage(
        val prompt_tokens: Int,
        val completion_tokens: Int,
        val total_tokens: Int
    )

    data class Choice(
        val delta: Delta,
        val message: Message,
        val finish_reason: String,
        val index: Int
    ) {
        data class Message(
            val role: String?, val content: String?
        )

        data class Delta(
            val role: String?, val content: String?
        )
    }
}

data class OpenAiError(
    val error: Error
)

data class Error(
    val code: String, val message: String, val `param`: String, val type: String
)

data class ChatMajor(
    val id: String,
    val index: Int,
    val role: String? = null,
    val content: String? = null,
    val done: Boolean = false
)

data class ResponseImage(
    val created: Int,
    val `data`: List<Data>
) {
    data class Data(
        val url: String
    )
}