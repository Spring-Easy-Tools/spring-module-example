package ru.virgil.spring.example.chat

import net.datafaker.Faker
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.support.GenericMessage
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import ru.virgil.spring.tools.security.oauth.getPrincipal
import ru.virgil.spring.tools.util.logging.Logger
import kotlin.time.Duration.Companion.milliseconds

private val delay = 1000.milliseconds

@Controller
class ChatController(
    private val faker: Faker,
    private val webSocketMessaging: SimpMessagingTemplate,
) {

    private val logger = Logger.inject(this.javaClass)

    /**
     * Чтобы прислать сюда сообщение, нужно отправлять в "/app/chat/send"
     * */
    @MessageMapping("/chat/send")
    @SendTo("/chat")
    fun send(chatMessage: ChatMessage): GenericMessage<ChatMessage> {
        logger.trace("New message! {}", chatMessage)
        Thread.sleep(delay.inWholeMilliseconds)
        return GenericMessage(chatMessage.copy(author = chatMessage.author ?: getPrincipal().username))
    }

    @Scheduled(cron = "*/30 * * * * *")
    private fun simulateChatMessage() {
        val simMessage = ChatMessage(faker.backToTheFuture().quote(), faker.backToTheFuture().character())
        logger.trace("Simulate message! {}", simMessage)
        val genericMessage = GenericMessage<ChatMessage>(simMessage)
        webSocketMessaging.convertAndSend("/chat", genericMessage)
    }
}
