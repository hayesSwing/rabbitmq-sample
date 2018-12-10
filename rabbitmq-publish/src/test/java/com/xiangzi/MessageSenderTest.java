package com.xiangzi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiangzi.config.MessageSender;
import com.xiangzi.util.RandomUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageSenderTest {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MessageSender messageSender;

//	@Ignore
	@Repeat(value = 20)
	@Test
	public void send() throws Exception {
		String tenantId = "10001";
		String message = RandomUtils.generateString(6);
		logger.info("message:" + message);
		
		Map<String, String> mqMsg = new HashMap<>();
		mqMsg.put("tenantId", tenantId);
		mqMsg.put("message", message);
		CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
		messageSender.send(tenantId, mqMsg, correlationData);
		Thread.sleep(500);
	}

	@Ignore
	@Repeat(value = 20)
	@Test
	public void send4Order() throws Exception {
		String tenantId = "10001";
		String message = RandomUtils.generateString(6);
		logger.info("message:" + message);
		
		Map<String, String> mqMsg = new HashMap<>();
		mqMsg.put("type", "order");
		mqMsg.put("tenantId", tenantId);
		mqMsg.put("message", message);
		CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
		messageSender.send4Order(tenantId, mqMsg, correlationData);
		Thread.sleep(500);
	}
	
}
