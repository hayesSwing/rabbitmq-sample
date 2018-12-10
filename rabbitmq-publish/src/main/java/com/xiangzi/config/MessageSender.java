package com.xiangzi.config;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xiangzi.RabbitConstant;
import com.xiangzi.util.JSONUtil;

@Component
public class MessageSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void send(String tenantId, Map<String, String> message, CorrelationData correlationData) throws Exception {
		message.put("tenantId", tenantId);
		this.rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE_NAME, RabbitConstant.ROUTING_KEY, JSONUtil.toJSONString(message), correlationData);
	}

	public void send4Order(String tenantId, Map<String, String> message, CorrelationData correlationData) throws Exception {
		message.put("tenantId", tenantId);
		this.rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE_NAME, RabbitConstant.ROUTING_KEY_ORDER, JSONUtil.toJSONString(message), correlationData);
	}
	
}
