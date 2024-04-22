package org.invoice

class ChatFilter internal constructor() {
    private var filteredWords: List<String>

    init {
        val element = server.getResourceAsJson("filtered_words.json")
        val array = element.getAsJsonArray("filter")

        filteredWords = array.map { it.asString }
    }

    fun checkMessage(message: String): Boolean {
        return filteredWords.contains(message.trim())
    }
}