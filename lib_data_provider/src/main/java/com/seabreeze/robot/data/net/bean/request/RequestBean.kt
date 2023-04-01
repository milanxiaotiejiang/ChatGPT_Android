package com.seabreeze.robot.data.net.bean.request


data class RequestChat(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>,
    /*
    temperature：默认值为 1.0，推荐值在 0.5 到 1.5 之间，具体取决于生成文本的需求。
    较低的 temperature 值会导致生成文本更加确定性，而较高的 temperature 值会导致生成文本更加随机和多样性。
    在一般情况下，推荐使用 1.0 左右的 temperature 值。
     */
    val temperature: Double = 1.0,
    /*
    top_p：默认值为 1.0，推荐值在 0.1 到 0.9 之间，具体取决于生成文本的需求。
    较高的 top_p 值会导致生成文本更加确定性，而较低的 top_p 值会导致生成文本更加随机和多样性。
    在一般情况下，推荐使用 0.5 左右的 top_p 值。
     */
    val top_p: Double = 0.5,
    val n: Int = 1,
    val stream: Boolean = true,
    val stop: List<String>? = null,
    /*
    max_tokens：默认值为 2048，推荐值在 64 到 512 之间，具体取决于生成文本的需求。
    较高的 max_tokens 值会导致生成文本更长，但可能会增加生成时间和资源消耗。
    在一般情况下，推荐使用 128 到 256 的 max_tokens 值。
     */
    val max_tokens: Int = 512,
    /*
    presence_penalty：默认值为 0.0，推荐值在 -2.0 到 2.0 之间，具体取决于生成文本的需求。
    正数的 presence_penalty 值会降低模型生成已经出现过的 token 的概率，从而鼓励模型生成更多新的文本内容。
    在一般情况下，推荐使用 0.5 到 1.0 的 presence_penalty 值。
     */
    val presence_penalty: Double = 0.5,
    /*
    frequency_penalty：默认值为 0.0，推荐值在 -2.0 到 2.0 之间，具体取决于生成文本的需求。
    正数的 frequency_penalty 值会降低模型生成已经出现过的 token 的概率，从而鼓励模型生成更多新的文本内容。
    在一般情况下，推荐使用 0.5 到 1.0 的 frequency_penalty 值。
     */
    val frequency_penalty: Double = 0.5,
    val logit_bias: Map<String, Double>? = null,
    val user: String = "ChatGPT_Android"
) {
    data class Message(
        val role: String,
        val content: String
    )
}

data class RequestImage(
    val prompt: String,
    val n: Int = 1,
    val size: String,
    val response_format: String = "url",
    val user: String = "user"
)