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
    println(ChatService.deleteMessage(2, 8))
    println(ChatService.getChatMessages(2, 3))
    println("--------------")
    ///println("Восстановление сообщения ${ChatService.restoreMessage(2, 4)}")
    println("--------------")
    //println(ChatService.getChatMessages(2, 3))
    println("--------------")
    //println("Удаление чата ${ChatService.deleteChat(2)} ")
    println("--------------")
    //println(ChatService.getChatMessages(1, 3))
    //println(ChatService.getChatMessages(3, 3))
    println("--------------")
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
    val deleted: Boolean = false,
    val read: Boolean = false,
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
        var listRead: MutableList<Message> = chat.messages.filter { it.messageId == messageId }.toMutableList()
        return if (listRead.size > 0) {
            for ((index, list) in chats[userId]?.messages!!.withIndex()) {
                if (listRead[0].messageId == list.messageId) chats[userId]?.messages?.set(index, Message(text = listRead[0].text, deleted = false, read = true, messageId = list.messageId))
            }
            true
        } else {
            false
        }

    }

    fun deleteMessage(userId: Int, messageId: Int): Boolean {
        val chat = chats[userId] ?: throw NoSuchChatsFound()
        var listRead: MutableList<Message> = chat.messages.filter { it.messageId == messageId }.toMutableList()
        if (chat.messages.size == 1) {
            val deleteChat = deleteChat(userId)
            return true
        }
        if (listRead.size > 0) {
            for ((index, list) in chats[userId]?.messages!!.withIndex()) {
                if (listRead[0].messageId == list.messageId) {
                    chats[userId]?.messages?.set(index, Message(text = listRead[0].text, deleted = true, read = true, messageId = list.messageId))
                    return true
                }
            }
        } else {
            return false
        }
        return false
    }

    fun deleteChat(userId: Int): Boolean {
        //val chat = chats.remove(userId)
        return chats.remove(userId) != null
    }

    fun getUnreadChatsCount(): Int {
        return chats.map { it.value.messages.filter { !it.read } }.filter { it.isNotEmpty() }.size
    }

    fun getChatMessages(userId: Int, count: Int): List<Message> {
        val chat = chats[userId] ?: throw NoSuchChatsFound()
        var listRead: MutableList<Message> = chat.messages.filter { !it.deleted  }.takeLast(count).toMutableList()

        for (tmpList in listRead) {
            for ((index, list) in chats[userId]?.messages!!.withIndex()) {
                if (tmpList.messageId == list.messageId) chats[userId]?.messages?.set(index, Message(text = tmpList.text, deleted = tmpList.deleted, read = true, messageId = list.messageId))
            }
        }
        return chat.messages.filter { !it.deleted }.takeLast(count).toMutableList()
    }

    fun printChats() = println(chats)
}