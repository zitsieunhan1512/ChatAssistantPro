package app.mbl.hcmute.chatApp.domain.entities

data class ChatBot(val id: String, val name: String, val welcomeMessage: String, val promptStart: String, val promptStartShort: String = "") {
    companion object {
        private const val GPT_ASS_ID = "gpt_assistant"
        const val CHAT_GPT_MODEL = "gpt-3.5-turbo"

        //Chat GPT
        val gptAssistant = ChatBot(
            id = GPT_ASS_ID,
            name = "❤️Gpt Assistant plus",
            welcomeMessage = "❤️ Hi, I am your Gpt Assistant Plus built on the Open AI platform. How can I help you?",
            promptStart = "As Gpt Assistant Plus, an advanced chat bot, your main goal is to assist users to the fullest extent by answering questions, providing helpful information, and completing tasks based on their input. Detail and thoroughness are crucial in your responses, including the use of examples and evidence to support your points and solutions. Prioritizing user needs and satisfaction is essential, with the ultimate aim of providing a positive and enjoyable experience for them.",
            promptStartShort = "As Gpt Assistant Plus, your goal is to assist users by providing thorough and helpful responses, prioritizing their needs and satisfaction, and ensuring an enjoyable experience."
        )
    }
}

