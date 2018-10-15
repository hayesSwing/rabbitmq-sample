package com.xiangzi.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rabbitmq.client.Channel;
import com.xiangzi.RabbitConstant;

@Configuration
public class RabbitMQConfig {

	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.port}")
	private String port;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String password;

	@Value("${spring.rabbitmq.publisher-confirms}")
	private Boolean publisherConfirms;

	@Value("${spring.rabbitmq.virtual-host}")
	private String virtualHost;

	// 创建工厂连接
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(this.host);
		connectionFactory.setUsername(this.username);
		connectionFactory.setPassword(this.password);
		connectionFactory.setVirtualHost(this.virtualHost);
		connectionFactory.setPublisherConfirms(this.publisherConfirms); // 必须要设置
		return connectionFactory;
	}

	// 队列
	@Bean
	public Queue queue() {
		return QueueBuilder.durable(RabbitConstant.QUEUE_NAME).build();
	}

	@Bean
	public SimpleMessageListenerContainer messageContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(this.connectionFactory());
		container.setQueues(this.queue()); // 设置要监听的队列
		container.setExposeListenerChannel(true);
		container.setMaxConcurrentConsumers(1);
		container.setConcurrentConsumers(1);
		container.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 设置确认模式手工确认
		container.setMessageListener(new ChannelAwareMessageListener() {

			public void onMessage(Message message, Channel channel) throws Exception {
				byte[] body = message.getBody();
				System.out.println("receive msg : " + new String(body));
				channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
				// 确认消息成功消费
			}

		});
		return container;
	}

}