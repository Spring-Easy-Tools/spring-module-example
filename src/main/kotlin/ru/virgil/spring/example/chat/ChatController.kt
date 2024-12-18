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
import ru.virgil.spring.example.security.SecurityUserRepository
import ru.virgil.spring.example.security.SecurityUserService
import ru.virgil.spring.tools.security.cors.GlobalCors
import ru.virgil.spring.tools.security.oauth.Security.getPrincipal
import ru.virgil.spring.tools.util.logging.Logger
import java.util.*

// todo: Можно как-то так. А зачем?
// @MessagingGateway
@GlobalCors
@Controller
class ChatController(
    private val faker: Faker,
    private val webSocketMessaging: SimpMessagingTemplate,
    private val securityUserService: SecurityUserService,
    private val securityUserRepository: SecurityUserRepository,
    private val chatMessageRepository: ChatMessageRepository,
) {

    private val logger = Logger.inject(this::class.java)

    @MessageMapping("/chat/send/{username}")
    fun sendToUser(@Payload chatMessageDto: ChatMessageDto, @DestinationVariable username: UUID) {
        val userDetails = securityUserService.loadUserByUsername(username.toString())
        logger.trace("New user message! {}", pprint(chatMessageDto))
        var chatMessage = ChatMessage(chatMessageDto.text, chatMessageDto.author)
        chatMessage = chatMessageRepository.save(chatMessage)
        logger.trace("Message saved to repository! {}", pprint(chatMessage))
        // Аннотации и методы по разному заворачивают пейлоад в сообщение
        webSocketMessaging.convertAndSendToUser(userDetails!!.username, "/chat/my", chatMessageDto)
    }

    /**
     * Чтобы прислать сюда сообщение, нужно отправлять в "/app/chat/send"
     * */
    @MessageMapping("/chat/send")
    @SendTo("/chat")
    fun send(@Payload chatMessageDto: ChatMessageDto): GenericMessage<ChatMessageDto> {
        logger.trace("New message! {}", chatMessageDto)
        val chatMessage = ChatMessage(chatMessageDto.text, chatMessageDto.author)
        chatMessageRepository.save(chatMessage)
        return GenericMessage(chatMessageDto.copy(author = chatMessageDto.author ?: getPrincipal().username))
    }

    @Scheduled(cron = "*/30 * * * * *")
    private fun simulateChatMessage() {
        val simMessage = ChatMessageDto(faker.backToTheFuture().quote(), faker.backToTheFuture().character())
        logger.trace("Simulate message! {}", simMessage)
        val genericMessage = GenericMessage<ChatMessageDto>(simMessage)
        webSocketMessaging.convertAndSend("/chat", genericMessage)
    }
}
