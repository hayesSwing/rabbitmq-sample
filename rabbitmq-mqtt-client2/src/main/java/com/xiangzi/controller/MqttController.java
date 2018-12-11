package com.xiangzi.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiangzi.config.MqttGateway;

@RestController("MqttController")
@RequestMapping("mqtt")
public class MqttController extends BaseController {

	@Autowired
	private MqttGateway mqttGateway;

	// mqtt/send?sendData=i am zhangsan
	@RequestMapping("send")
	public Object send(String sendData) {
		String tenantId = "10001";

		String topic = tenantId + "/sales/order";
		mqttGateway.sendToMqtt(sendData, topic);

		Map<String, Object> result = new HashMap<>();
		result.put("status", 1);
		result.put("message", "发送成功");
		return result;
	}

}
