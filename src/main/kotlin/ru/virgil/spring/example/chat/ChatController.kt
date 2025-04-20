package ru.virgil.spring.example.chat

import io.exoquery.pprint
import net.datafaker.Faker
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.support.GenericMessage
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller
import ru.virgil.spring.tools.security.Security.getSimpleCreator
import ru.virgil.spring.tools.security.cors.GlobalCors
import ru.virgil.spring.tools.util.Http.orBadRequest
import ru.virgil.spring.tools.util.logging.Logger

// todo: Можно как-то так. А зачем?
// @MessagingGateway
@GlobalCors
@Controller
class ChatController(
    private val faker: Faker,
    private val webSocketMessaging: SimpMessagingTemplate,
    private val chatMessageRepository: ChatMessageRepository,
) {

    private val logger = Logger.inject(this::class.java)

    // todo: переделать на дефолтную рассылку пользователям?
    @MessageMapping("/chat/send/{username}")
    fun sendToUser(@Payload chatMessageDto: ChatMessageDto, @DestinationVariable username: String) {
        logger.trace { "New user message! ${pprint(chatMessageDto)}" }
        var chatMessage = ChatMessage(chatMessageDto.text.orBadRequest("Message should contain text"), chatMessageDto.author)
        chatMessage = chatMessageRepository.save(chatMessage)
        logger.trace { "Message saved to repository! ${pprint(chatMessage)}" }
        // Аннотации и методы по разному заворачивают пейлоад в сообщение
        webSocketMessaging.convertAndSendToUser(username, "/chat/my", chatMessageDto)
    }

    /**
     * Чтобы прислать сюда сообщение, нужно отправлять в "/app/chat/send"
     * */
    @MessageMapping("/chat/send")
    @SendTo("/chat")
    fun send(@Payload chatMessageDto: ChatMessageDto): GenericMessage<ChatMessageDto> {
        logger.trace { "New message! ${pprint(chatMessageDto)}" }
        val chatMessage = ChatMessage(chatMessageDto.text.orBadRequest("Message should contain text"), chatMessageDto.author)
        chatMessageRepository.save(chatMessage)
        return GenericMessage(chatMessageDto.copy(author = chatMessageDto.author ?: getSimpleCreator()))
    }

    @Scheduled(cron = "*/30 * * * * *")
    private fun simulateChatMessage() {
        val simMessage = ChatMessageDto(faker.backToTheFuture().quote(), faker.backToTheFuture().character())
        logger.trace { "Simulate message! ${pprint(simMessage)}" }
        val genericMessage = GenericMessage<ChatMessageDto>(simMessage)
        webSocketMessaging.convertAndSend("/chat", genericMessage)
    }
}
