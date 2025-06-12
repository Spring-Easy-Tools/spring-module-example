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
import ru.virgil.spring.tools.security.Security.getCreator
import ru.virgil.spring.tools.security.cors.GlobalCors
import ru.virgil.spring.tools.util.Http.orBadRequest
import ru.virgil.spring.tools.util.logging.Logger

@GlobalCors
@Controller
class ChatController(
    private val faker: Faker,
    private val webSocketMessaging: SimpMessagingTemplate,
    private val chatMessageRepository: ChatMessageRepository,
) {

    private val logger = Logger.inject(this::class.java)

    @MessageMapping("/chat/send/{username}")
    fun sendToUser(@Payload chatMessageDto: ChatMessageDto, @DestinationVariable username: String) {
        logger.trace { "New user message! ${pprint(chatMessageDto)}" }
        var chatMessage = ChatMessage(chatMessageDto.text.orBadRequest("Message should contain text"), chatMessageDto.author)
        chatMessage = chatMessageRepository.save(chatMessage)
        logger.trace { "Message saved to repository! ${pprint(chatMessage)}" }
        webSocketMessaging.convertAndSendToUser(username, "/chat/my", chatMessageDto)
    }

    /**
     * Чтобы прислать сюда сообщение, нужно отправлять в "/app/chat/send"
     *
     * Важно: разные способы отправки сообщений в Spring WebSocket (аннотации @SendTo, @MessageMapping и ручной вызов SimpMessagingTemplate)
     * могут по-разному сериализовать ("заворачивать") payload в итоговое сообщение для клиента.
     *
     * Например:
     * - @SendTo возвращает результат метода, который Spring может обернуть в GenericMessage или Map (см. https://docs.spring.io/spring-framework/reference/web/websocket/stomp/message-handling.html#websocket-stomp-message-mapping-return-values)
     * - SimpMessagingTemplate.convertAndSend[ToUser] отправляет объект как есть, без дополнительной обёртки (https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/messaging/simp/SimpMessagingTemplate.html)
     *
     * Это может привести к разной структуре JSON на клиенте. Если важна единообразная структура сообщений — стоит явно контролировать сериализацию.
     * */
    @MessageMapping("/chat/send")
    @SendTo("/chat")
    fun send(@Payload chatMessageDto: ChatMessageDto): GenericMessage<ChatMessageDto> {
        logger.trace { "New message! ${pprint(chatMessageDto)}" }
        val chatMessage = ChatMessage(chatMessageDto.text.orBadRequest("Message should contain text"), chatMessageDto.author)
        chatMessageRepository.save(chatMessage)
        return GenericMessage(chatMessageDto.copy(author = chatMessageDto.author ?: getCreator()))
    }

    @Scheduled(cron = "*/30 * * * * *")
    private fun simulateChatMessage() {
        val simMessage = ChatMessageDto(faker.backToTheFuture().quote(), faker.backToTheFuture().character())
        logger.trace { "Simulate message! ${pprint(simMessage)}" }
        val genericMessage = GenericMessage<ChatMessageDto>(simMessage)
        webSocketMessaging.convertAndSend("/chat", genericMessage)
    }
}
