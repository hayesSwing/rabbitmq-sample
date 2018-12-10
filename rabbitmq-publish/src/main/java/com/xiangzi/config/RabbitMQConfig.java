package com.xiangzi.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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

	// rabbitmq的模板配置
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) // 必须是prototype类型(因为要设置回调类，所以应是prototype类型，如果是singleton类型，则回调类为最后一次设置)
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(this.connectionFactory());
		template.setConfirmCallback(messageCallBackSender());// 设置消息确认
		// template.setReturnCallback();
		return template;
	}

	@Bean(name = "mqttRabbitTemplate")
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public RabbitTemplate mqttRabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(this.connectionFactory());
		
		return template;
	}
	
	@Bean
	public MessageCallBackSender messageCallBackSender() {
		return new MessageCallBackSender();
	}

	// 直连交换机
	@Bean
	public DirectExchange defaultExchange() {
		return new DirectExchange(RabbitConstant.EXCHANGE_NAME);
	}

	// 队列
	@Bean
	public Queue queue() {
		return QueueBuilder.durable(RabbitConstant.QUEUE_NAME).build();
	}

	// 绑定
	@Bean
	public Binding binding() {
		return BindingBuilder.bind(this.queue()).to(this.defaultExchange()).with(RabbitConstant.ROUTING_KEY);
	}

	// 队列-订单
	@Bean(name = "queue4Order")
	public Queue queue4Order() {
		return QueueBuilder.durable(RabbitConstant.QUEUE_NAME_ORDER).build();
	}

	// 绑定-订单
	@Bean(name = "binding4Order")
	public Binding binding4Order() {
		return BindingBuilder.bind(this.queue4Order()).to(this.defaultExchange()).with(RabbitConstant.ROUTING_KEY_ORDER);
	}

}
