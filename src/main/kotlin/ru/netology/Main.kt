package ru.netology

import java.lang.Exception

fun main() {
    ChatService.add(1, Message("gfdg"))
    ChatService.add(2, Message("Привет"))
    ChatService.add(3, Message("Привет33"))
    ChatService.add(4, Message("Привет44"))
    ChatService.add(5, Message("Привет55"))
    ChatService.add(2, Message("Привет1"))
    ChatService.add(2, Message("Привет2"))
    ChatService.add(2, Message("Привет3"))
    ChatService.add(2, Message("Привет4"))
    println("--------------")
    //println("Количество непрочитанных чатов: ${ChatService.getUnreadChatsCount()}")
    println("Удаление сообщения ${ChatService.deleteMessage(2, 9)}")
    println("--------------")
    println(ChatService.getChatList())
    println("--------------")
    //println(ChatService.getChatMessages(2, 3))
    println("--------------")
    println("Восстановление сообщения ${ChatService.restoreMessage(2, 9)}")
    println("--------------")
    println(ChatService.getChatList())
    //ChatService.printChats()
    println("--------------")
    ///println("Восстановление сообщения ${ChatService.restoreMessage(2, 4)}")
    //println("--------------")
    //println(ChatService.getChatMessages(2, 3))
    //println("--------------")
    //println("Удаление чата ${ChatService.deleteChat(2)} ")
    //println("--------------")
    //println(ChatService.getChatMessages(1, 3))
    //println(ChatService.getChatMessages(3, 3))
    //println("--------------")
    //println("Количество непрочитанных чатов: ${ChatService.getUnreadChatsCount()}")
    //ChatService.printChats()
    //println(ChatService.getChatList())
}

object CorrectId {
    private var lastId = 0
    //смотрим в то место где у нас хранятся id постов и выдаём свободный

    fun getNewId(id: Int): Int {
        if (id == 0) {
            lastId += 1
            return lastId
        }

        return 0
    }

    fun clearId() {
        lastId = 0
    }
}

data class Chat(
    val messages: MutableList<Message> = mutableListOf(),
)

data class Message(
    val text: String,
    var deleted: Boolean = false,
    var read: Boolean = false,
    val messageId: Int = CorrectId.getNewId(0)
)

class NoSuchChatsFound: Exception()

object ChatService{
    private var chats = mutableMapOf<Int, Chat>()

    fun clear() {
        CorrectId.clearId()
        chats = mutableMapOf<Int, Chat>()
    }

    fun add(userId: Int, message: Message) {
        chats.getOrPut(userId) { Chat() }.messages.add(message)
    }

    fun getChatList() =
        chats.values.map { it.messages.lastOrNull { message ->  !message.deleted}?.text ?: "Нет сообщений" }

    fun restoreMessage(userId: Int, messageId: Int): Boolean {
        val chat = chats[userId] ?: throw NoSuchChatsFound()
        return chat.messages.filter { it.messageId == messageId }.onEach { it.deleted = false }.size == 1

    }

    fun deleteMessage(userId: Int, messageId: Int): Boolean {
        val chat = chats[userId] ?: throw NoSuchChatsFound()
        return chat.messages.filter { it.messageId == messageId }.onEach { it.deleted = true }.size == 1
    }

    fun deleteChat(userId: Int): Boolean {
        return chats.remove(userId) != null
    }

    fun getUnreadChatsCount(): Int {
        return chats.map { it.value.messages.filter { !it.read } }.filter { it.isNotEmpty() }.size
    }

    fun getChatMessages(userId: Int, count: Int): List<Message> {
        val chat = chats[userId] ?: throw NoSuchChatsFound()
        return chat.messages.filter { !it.deleted  }.takeLast(count).onEach { it.read = true }.toMutableList()
    }

    fun printChats() = println(chats)
}