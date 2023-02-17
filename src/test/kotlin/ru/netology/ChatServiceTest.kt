package ru.netology

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class ChatServiceTest {

    @Before
    fun clearBeforeTest() {
        ChatService.clear()
    }

    @Test
    fun add() {
        ChatService.add(2, Message("Привет"))
        ChatService.add(3, Message("Привет33"))
        assertEquals(1, ChatService.getChatMessages(3, 1).size)
    }

    @Test
    fun getChatList() {
        ChatService.add(2, Message("Привет"))
        assertEquals(listOf("Привет"), ChatService.getChatList())
    }

    @Test
    fun restoreMessage() {
        ChatService.add(2, Message("Привет", deleted = false))
        assertTrue(ChatService.restoreMessage(2, 1))
    }

    @Test
    fun deleteMessage() {
        ChatService.add(2, Message("Привет"))
        assertTrue(ChatService.deleteMessage(2, 1))
    }

    @Test
    fun deleteChat() {
        ChatService.add(2, Message("Привет"))
        ChatService.add(3, Message("Привет111"))
        ChatService.deleteChat(3)
        assertEquals(listOf("Привет"), ChatService.getChatList())
    }

    @Test
    fun getUnreadChatsCount() {
        ChatService.add(2, Message("Привет", read = true))
        ChatService.add(3, Message("Привет111"))
        assertEquals(1, ChatService.getUnreadChatsCount())
    }

    @Test
    fun getChatMessages() {
        ChatService.add(2, Message("Привет", deleted = true))
        ChatService.add(2, Message("Привет111"))
        assertEquals(listOf(Message("Привет111", false, true, 2)), ChatService.getChatMessages(2, 2))
    }
}