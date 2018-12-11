package com.xiangzi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xiangzi.mqtt.MqttClientExt;
import com.xiangzi.util.DateUtils;
import com.xiangzi.util.JSONUtil;
import com.xiangzi.util.RandomUtils;

@Controller("MqttController")
@RequestMapping("mqtt")
public class MqttController extends BaseController {

	@Resource
	private MqttClientExt mqttClient;

	@RequestMapping(value = { "send" })
	@ResponseBody
	public Object send(Model model) throws MqttException {
		String tenantId = "10001";
		Map<String, Object> message = new HashMap<>();
		message.put("id", RandomUtils.getUUID());
		message.put("date", DateUtils.getCurrentDateTime());
		mqttClient.Publish(tenantId + "/sales/order", JSONUtil.toJSONString(message));

		Map<String, Object> result = new HashMap<>();
		result.put("status", 1);
		result.put("message", message);
		return result;
	}

	@RequestMapping(value = { "subscribe" })
	@ResponseBody
	public Object subscribe(Model model) throws MqttException {
		String tenantId = "10001";
		mqttClient.Subscribe(tenantId + "/sales/order");

		Map<String, Object> result = new HashMap<>();
		result.put("status", 1);
		result.put("message", "订阅成功");
		return result;
	}

	@RequestMapping(value = { "unsubscribe" })
	@ResponseBody
	public Object unsubscribe(Model model) throws MqttException {
		String tenantId = "10001";
		mqttClient.Unsubscribe(tenantId + "/sales/order");

		Map<String, Object> result = new HashMap<>();
		result.put("status", 1);
		result.put("message", "取消订阅成功");
		return result;
	}

}
