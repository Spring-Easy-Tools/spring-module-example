package ru.virgil.spring.example.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.truth.Truth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.messaging.Message
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.AbstractSubscribableChannel
import org.springframework.messaging.support.MessageBuilder
import org.springframework.test.annotation.DirtiesContext
import ru.virgil.spring.example.chat.ChatMessageDto
import ru.virgil.spring.example.chat.ChatMessageRepository
import ru.virgil.spring.example.roles.user.WithMockedUser
import ru.virgil.spring.tools.security.Security
import ru.virgil.spring.tools.testing.MessagingChannelInterceptor
import ru.virgil.spring.tools.testing.MessagingTestUtils.awaitResult
import ru.virgil.spring.tools.testing.MessagingTestUtils.deserializeFromMessagingAnnotation
import ru.virgil.spring.tools.testing.MessagingTestUtils.deserializeFromMessagingTemplate
import ru.virgil.spring.tools.SpringToolsConfig.Companion.BASE_PACKAGE
import ru.virgil.spring.tools.util.logging.Logger
import java.time.Duration

@DirtiesContext
@SpringBootTest
@ComponentScan(BASE_PACKAGE)
@WithMockedUser
@AutoConfigureMockMvc
class ChatApiTest @Autowired constructor(
    /** Название каналов важно, они инжектятся по квалификатору */
    val clientInboundChannel: AbstractSubscribableChannel,
    val clientOutboundChannel: AbstractSubscribableChannel,
    val brokerChannel: AbstractSubscribableChannel,
    // val securityUserService: SecurityUserService,
    val objectMapper: ObjectMapper,
    private val chatMessageRepository: ChatMessageRepository,
) {

    private val logger = Logger.inject(this::class.java)

    private lateinit var clientOutboundChannelInterceptor: MessagingChannelInterceptor
    private lateinit var clientInboundChannelInterceptor: MessagingChannelInterceptor
    private lateinit var brokerChannelInterceptor: MessagingChannelInterceptor

    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        this.brokerChannelInterceptor = MessagingChannelInterceptor(objectMapper)
        this.clientOutboundChannelInterceptor = MessagingChannelInterceptor(objectMapper)
        this.clientInboundChannelInterceptor = MessagingChannelInterceptor(objectMapper)
        brokerChannel.addInterceptor(this.brokerChannelInterceptor)
        clientOutboundChannel.addInterceptor(this.clientOutboundChannelInterceptor)
        clientInboundChannel.addInterceptor(this.clientInboundChannelInterceptor)
    }

    @Test
    fun `Chat Channel Sending`() {

        val destination = "/app/chat/send"
        val testingText = "STOMP Chat Test"
        val authenticatedToken = Security.getAuthentication()

        brokerChannelInterceptor.destinationPatterns.add("/chat")

        val subscribeHeaders = StompHeaderAccessor.create(StompCommand.SUBSCRIBE)
        subscribeHeaders.subscriptionId = "0"
        subscribeHeaders.destination = destination
        subscribeHeaders.sessionId = "0"
        subscribeHeaders.user = authenticatedToken
        subscribeHeaders.sessionAttributes = HashMap()
        val subscribeDto = ChatMessageDto("Subscribing message", authenticatedToken.name)
        val subscribeMessage = MessageBuilder.createMessage(
            objectMapper.writeValueAsBytes(subscribeDto), subscribeHeaders.messageHeaders
        )

        clientInboundChannel.send(subscribeMessage)
        clientInboundChannelInterceptor.awaitForMessage(subscribeDto.text!!)

        val sendHeaders = StompHeaderAccessor.create(StompCommand.SEND)
        sendHeaders.subscriptionId = "0"
        sendHeaders.destination = destination
        sendHeaders.sessionId = "0"
        sendHeaders.user = authenticatedToken
        sendHeaders.sessionAttributes = HashMap()
        val sendDto = ChatMessageDto(testingText, authenticatedToken.name)
        val sendMessage = MessageBuilder.createMessage(
            objectMapper.writeValueAsBytes(sendDto), sendHeaders.messageHeaders
        )

        clientInboundChannel.send(sendMessage)

        val sendReply: Message<*> = brokerChannelInterceptor.awaitForMessage(sendDto.text!!)
        val replyDto: ChatMessageDto = sendReply.deserializeFromMessagingAnnotation(objectMapper)

        Truth.assertThat(sendReply).isNotNull()
        Truth.assertThat(replyDto.text).isEqualTo(testingText)
        Truth.assertThat(replyDto.author).isEqualTo(authenticatedToken.name)

        val chatMessage = awaitResult { chatMessageRepository.findAll().find { it.text == testingText } }
        Truth.assertThat(chatMessage).isNotNull()
        Truth.assertThat(chatMessage.text).isEqualTo(testingText)
        Truth.assertThat(chatMessage.author).isEqualTo(authenticatedToken.name)
    }

    @Test
    fun `Scheduled Chat Sending`() {
        brokerChannelInterceptor.awaitLastMessage(Duration.ofMinutes(1))
    }

    @Test
    fun `Chat User Sending`() {

        val authenticatedToken = Security.getAuthentication()

        val destination = "/app/chat/send/${authenticatedToken.name}"
        val subscription = "/user/${authenticatedToken.name}/chat/my"
        val testingText = "STOMP User Chat Test"

        val subscribeHeaders = StompHeaderAccessor.create(StompCommand.SUBSCRIBE)
        subscribeHeaders.subscriptionId = "0"
        subscribeHeaders.destination = subscription
        subscribeHeaders.sessionId = "0"
        subscribeHeaders.user = authenticatedToken
        subscribeHeaders.sessionAttributes = HashMap()
        val subscribeDto = ChatMessageDto("Subscribing Message", authenticatedToken.name)
        val subscribeMessage = MessageBuilder.createMessage(
            objectMapper.writeValueAsBytes(subscribeDto), subscribeHeaders.messageHeaders
        )

        clientInboundChannel.send(subscribeMessage)
        clientInboundChannelInterceptor.awaitForMessage(subscribeDto.text!!)

        val sendHeaders = StompHeaderAccessor.create(StompCommand.SEND)
        sendHeaders.subscriptionId = "0"
        sendHeaders.destination = destination
        sendHeaders.sessionId = "0"
        sendHeaders.user = authenticatedToken
        sendHeaders.sessionAttributes = HashMap()
        val sendDto = ChatMessageDto(testingText, authenticatedToken.name)
        val sendMessage = MessageBuilder.createMessage(
            objectMapper.writeValueAsBytes(sendDto), sendHeaders.messageHeaders
        )

        clientInboundChannel.send(sendMessage)

        val sendReply: Message<*> = clientInboundChannelInterceptor.awaitForMessage(sendDto.text!!)
        val replyDto: ChatMessageDto = sendReply.deserializeFromMessagingTemplate(objectMapper)

        Truth.assertThat(sendReply).isNotNull()
        Truth.assertThat(replyDto.text).isEqualTo(testingText)
        Truth.assertThat(replyDto.author).isEqualTo(authenticatedToken.name)

        val chatMessage = awaitResult { chatMessageRepository.findAll().find { it.text == testingText } }
        Truth.assertThat(chatMessage).isNotNull()
        Truth.assertThat(chatMessage.text).isEqualTo(testingText)
        Truth.assertThat(chatMessage.author).isEqualTo(authenticatedToken.name)
    }
}
