package com.xiangzi;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiangzi.util.RandomUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqTest {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Ignore
	@Repeat(value = 20)
	@Test
	public void hello() throws Exception {
		String message = RandomUtils.generateString(6);
		logger.info("message:" + message);
		CorrelationData correlationData = null;
		this.rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE_NAME, RabbitConstant.ROUTING_KEY, message, correlationData);
		Thread.sleep(500);
	}

	@Ignore
	@Repeat(value = 20)
	@Test
	public void order() throws Exception {
		String message = "order-" + RandomUtils.generateString(6);
		logger.info("message:" + message);
		CorrelationData correlationData = null;
		this.rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE_NAME, RabbitConstant.ROUTING_KEY_ORDER, message, correlationData);
		Thread.sleep(500);
	}

}
