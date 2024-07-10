package ru.virgil.spring.example

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.common.truth.Truth
import io.exoquery.pprint
import org.awaitility.kotlin.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.AbstractSubscribableChannel
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageBuilder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.util.AntPathMatcher
import ru.virgil.spring.example.chat.ChatMessageDto
import ru.virgil.spring.example.chat.ChatMessageRepository
import ru.virgil.spring.tools.security.mock.MockSecurityContextFactory.Companion.mockSecurityContext
import ru.virgil.spring.tools.security.oauth.SecurityUserService
import ru.virgil.spring.tools.toolsBasePackage
import ru.virgil.spring.tools.util.logging.Logger
import java.time.Duration
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit


@DirtiesContext
@SpringBootTest
@ComponentScan(toolsBasePackage)
@AutoConfigureMockMvc
class ChatApiTest @Autowired constructor(
    /** Название каналов важно, они инжектятся по квалификатору */
    val clientInboundChannel: AbstractSubscribableChannel,
    val clientOutboundChannel: AbstractSubscribableChannel,
    val brokerChannel: AbstractSubscribableChannel,
    val securityUserService: SecurityUserService,
    val objectMapper: ObjectMapper,
    private val chatMessageRepository: ChatMessageRepository,
) {

    private val logger = Logger.inject(this::class.java)

    private lateinit var clientOutboundChannelInterceptor: TestChannelInterceptor
    private lateinit var clientInboundChannelInterceptor: TestChannelInterceptor
    private lateinit var brokerChannelInterceptor: TestChannelInterceptor

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        this.brokerChannelInterceptor = TestChannelInterceptor(objectMapper)
        this.clientOutboundChannelInterceptor = TestChannelInterceptor(objectMapper)
        this.clientInboundChannelInterceptor = TestChannelInterceptor(objectMapper)
        brokerChannel.addInterceptor(this.brokerChannelInterceptor)
        clientOutboundChannel.addInterceptor(this.clientOutboundChannelInterceptor)
        clientInboundChannel.addInterceptor(this.clientInboundChannelInterceptor)
    }

    /* todo: протестировать:
    *   1. Подписку на канал
    *   1. Рассылку всем подписчикам
    *   2. Пересылку конкретному пользователю
    *   3. Запланированные сообщения
    */
    @Test
    fun `Chat Channel Sending`() {

        val destination = "/app/chat/send"
        val testingText = "STOMP Chat Test"
        val authenticatedToken = securityUserService.mockSecurityContext()

        brokerChannelInterceptor.destinationPatterns.add("/chat")

        val subscribeHeaders = StompHeaderAccessor.create(StompCommand.SUBSCRIBE)
        subscribeHeaders.subscriptionId = "0"
        subscribeHeaders.destination = destination
        subscribeHeaders.sessionId = "0"
        subscribeHeaders.user = authenticatedToken
        subscribeHeaders.sessionAttributes = HashMap()
        val subscribeChatMessageDto = ChatMessageDto("Subscribing message", authenticatedToken.name)
        val subscribeMessage = MessageBuilder.createMessage(
            objectMapper.writeValueAsBytes(subscribeChatMessageDto), subscribeHeaders.messageHeaders
            // byteArrayOf(), subscribeHeaders.messageHeaders
        )

        clientInboundChannel.send(subscribeMessage)

        val headers = StompHeaderAccessor.create(StompCommand.SEND)
        headers.subscriptionId = "0"
        headers.destination = destination
        headers.sessionId = "0"
        headers.user = authenticatedToken
        headers.sessionAttributes = HashMap()
        val chatMessageDto = ChatMessageDto(testingText, authenticatedToken.name)
        val message = MessageBuilder.createMessage(
            objectMapper.writeValueAsBytes(chatMessageDto), headers.messageHeaders
            // objectMapper.writeValueAsString(chatMessageDto), headers.messageHeaders
        )

        clientInboundChannel.send(message)

        val reply: Message<*>? = brokerChannelInterceptor.awaitMessage(Duration.ofSeconds(30))
        // Такая десериализация работает если вернуть сообщение из метода с аннотацией
        val decodedPayload = (reply!!.payload as ByteArray).decodeToString()
        val payloadMap = objectMapper.readValue<Map<String, *>>(decodedPayload)
        val messageDto = objectMapper.convertValue(payloadMap["payload"], ChatMessageDto::class.java)

        Truth.assertThat(reply).isNotNull()
        Truth.assertThat(messageDto.text).isEqualTo(testingText)
        Truth.assertThat(messageDto.author).isEqualTo(authenticatedToken.name)
        val chatMessage = chatMessageRepository.findAll().find { it.text == testingText }
        Truth.assertThat(chatMessage).isNotNull()
        Truth.assertThat(chatMessage?.text).isEqualTo(testingText)
        Truth.assertThat(chatMessage?.author).isEqualTo(authenticatedToken.name)
    }

    @Test
    fun `Scheduled Chat Sending`() {
        val reply: Message<*>? = brokerChannelInterceptor.awaitMessage(Duration.ofMinutes(1))
        Truth.assertThat(reply).isNotNull()
    }

    @Test
    fun `Chat User Sending`() {

        val authenticatedToken = securityUserService.mockSecurityContext()

        val destination = "/app/chat/send/${authenticatedToken.name}"
        val subscription = "/user/${authenticatedToken.name}/chat/my"
        val testingText = "STOMP User Chat Test"

        // clientInboundChannelInterceptor.destinationPatterns.add(subscription)

        val subscribeHeaders = StompHeaderAccessor.create(StompCommand.SUBSCRIBE)
        subscribeHeaders.subscriptionId = "0"
        subscribeHeaders.destination = subscription
        subscribeHeaders.sessionId = "0"
        subscribeHeaders.user = authenticatedToken
        subscribeHeaders.sessionAttributes = HashMap()
        val subscribeChatMessageDto = ChatMessageDto("Subscribing Message", authenticatedToken.name)
        val subscribeMessage = MessageBuilder.createMessage(
            objectMapper.writeValueAsBytes(subscribeChatMessageDto), subscribeHeaders.messageHeaders
        )

        clientInboundChannel.send(subscribeMessage)
        val awaitSubscribeMessage = clientInboundChannelInterceptor.awaitMessage(Duration.ofSeconds(5))

        val headers = StompHeaderAccessor.create(StompCommand.SEND)
        headers.subscriptionId = "0"
        headers.destination = destination
        headers.sessionId = "0"
        headers.user = authenticatedToken
        headers.sessionAttributes = HashMap()
        val chatMessageDto = ChatMessageDto(testingText, authenticatedToken.name)
        val message = MessageBuilder.createMessage(
            objectMapper.writeValueAsBytes(chatMessageDto), headers.messageHeaders
        )

        clientInboundChannel.send(message)

        val reply: Message<*>? = clientInboundChannelInterceptor.awaitMessage(Duration.ofSeconds(5))
        // Такая десериализация работает если послать сообщение через SimpMessagingTemplate
        val decodedPayload = (reply!!.payload as ByteArray).decodeToString()
        // val payloadMap = objectMapper.readValue<Map<String, *>>(decodedPayload)
        // val messageDto = objectMapper.convertValue(payloadMap["payload"], ChatMessageDto::class.java)
        val messageDto = objectMapper.readValue<ChatMessageDto>(decodedPayload)

        Truth.assertThat(reply).isNotNull()
        Truth.assertThat(messageDto.text).isEqualTo(testingText)
        Truth.assertThat(messageDto.author).isEqualTo(authenticatedToken.name)

        // Сравнивать надо только payload, потому что структура заголовков разная
        // Еще это позволяет дождаться появления сообщения в БД
        // Thread.sleep(Duration.ofSeconds(5))
        // with().pollInterval(fibonacci(5)).await().until { }
        await withPollInterval Duration.ofMinutes(1) until {
            chatMessageRepository.findAll().toList().find { it.text == testingText } != null
        }
        // val brokerMessage: Message<*>? = brokerChannelInterceptor.awaitMessage(Duration.ofSeconds(10))
        // Truth.assertThat((reply.payload as ByteArray).decodeToString()).isEqualTo((brokerMessage!!.payload as ByteArray).decodeToString())

        val chatMessage = chatMessageRepository.findAll().find { it.text == testingText }
        Truth.assertThat(chatMessage).isNotNull()
        Truth.assertThat(chatMessage?.text).isEqualTo(testingText)
        Truth.assertThat(chatMessage?.author).isEqualTo(authenticatedToken.name)
    }

    /**
     * A ChannelInterceptor that caches messages.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    class TestChannelInterceptor(
        private val objectMapper: ObjectMapper,
        val destinationPatterns: ArrayList<String> = ArrayList(),
        val messagesArray: MutableList<Message<*>> = arrayListOf(),
    ) : ChannelInterceptor {

        private val logger = Logger.inject(this::class.java)
        private val messages = ArrayBlockingQueue<Message<*>>(100)
        private val matcher = AntPathMatcher()

        /**
         * @return the next received message or `null` if the specified time elapses
         */
        @Throws(InterruptedException::class)
        fun awaitMessage(duration: Duration): Message<*>? {
            logger.trace("Awaiting for message ({})...", pprint(duration))
            return messages.poll(duration.toMillis(), TimeUnit.MILLISECONDS)
        }

        override fun preSend(message: Message<*>, channel: MessageChannel): Message<*> {
            val headers = StompHeaderAccessor.wrap(message)
            logger.debug("Message intercepted at {}", pprint(headers.destination))
            logger.trace("Message headers: {}", pprint(message.headers))
            logger.trace("Message payload: {}", pprint(objectMapper.readValue<Map<*, *>>(message.payload as ByteArray)))
            // todo: упростить
            if (destinationPatterns.isEmpty()) {
                logger.trace("Message added to interceptor's poll")
                messages.add(message)
                messagesArray.add(message)
            } else {
                logger.trace("Destination patterns added {}. Checking...", pprint(destinationPatterns))
                if (headers.destination != null) {
                    for (pattern in this.destinationPatterns) {
                        if (matcher.match(pattern, headers.destination!!)) {
                            logger.trace("Message added to interceptor's poll")
                            messages.add(message)
                            messagesArray.add(message)
                            break
                        }
                    }
                }
            }
            return message
        }
    }
}
