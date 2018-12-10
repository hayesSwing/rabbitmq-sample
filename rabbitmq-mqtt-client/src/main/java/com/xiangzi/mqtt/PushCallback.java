package com.xiangzi.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushCallback implements MqttCallback {

	private static Logger logger = LoggerFactory.getLogger(PushCallback.class);

	@Override
	public void connectionLost(Throwable cause) {
		logger.error("连接失败,原因", cause);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// subscribe后得到的消息会执行到这里面
		logger.error("接收消息主题:" + topic);
		logger.error("接收消息Qos:" + message.getQos());
		logger.error("接收消息内容:" + new String(message.getPayload()));
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// publish后会执行到这里
		logger.error("消息发送成功!" + ((token == null || token.getResponse() == null) ? "null" : token.getResponse().getKey()));
	}

}
