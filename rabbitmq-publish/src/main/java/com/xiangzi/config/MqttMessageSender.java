package com.xiangzi.config;

import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Component;

@Component
public class MqttMessageSender {

	@Resource(name = "mqttRabbitTemplate")
	private RabbitTemplate mqttRabbitTemplate;

	public void send(String topic, String message) {
		CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
		System.out.println("消息id:" + correlationData.getId());
		// 用RabbitMQ发送MQTT需将exchange配置为amq.topic
		// Messages published to MQTT topics use a topic exchange (amq.topic by default) internally. 
		this.mqttRabbitTemplate.convertAndSend("amq.topic", topic, message, correlationData);
	}
	
}
