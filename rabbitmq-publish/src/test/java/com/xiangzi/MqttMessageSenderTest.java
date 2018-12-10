package com.xiangzi;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Repeat;
import org.springframework.test.context.junit4.SpringRunner;

import com.xiangzi.config.MqttMessageSender;
import com.xiangzi.util.RandomUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MqttMessageSenderTest {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MqttMessageSender mqttMessageSender;

	@Ignore
	@Repeat(value = 20)
	@Test
	public void send() throws Exception {
		String topic = "test";
		String message = RandomUtils.generateString(6);
		logger.info("topic:" + topic);
		logger.info("message:" + message);
		mqttMessageSender.send(topic, message);
		Thread.sleep(500);
	}

}
