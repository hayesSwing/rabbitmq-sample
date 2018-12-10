package com.xiangzi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.xiangzi.mqtt.MqttClientExt;

@Configuration
public class MqttConfiguration {

	@Value("${mqtt.clientId:clientId}")
	private String clientId;

	@Value("${mqtt.host:0.0.0.0}")
	private String mqttHost;

	@Value("${mqtt.port:1883}")
	private Integer mqttPort;

	@Value("${mqtt.keepalive:60}")
	private int keepAlive;

	@Value("${mqtt.connection.timeout:10}")
	private Integer connectionTimeout;

	@Value("${mqtt.username:admin}")
	private String userName;

	@Value("${mqtt.passwd:admin}")
	private String passwd;

	@Bean(initMethod = "init")
	public MqttClientExt mqttClient() {

		MqttClientExt mqttClient = new MqttClientExt();

		mqttClient.setClientId(clientId);

		mqttClient.setHost(mqttHost);
		mqttClient.setPort(mqttPort);
		mqttClient.setKeepAlive(keepAlive);
		mqttClient.setConnectionTimeout(connectionTimeout);
		mqttClient.setUserName(userName);
		mqttClient.setPasswd(passwd);

		return mqttClient;
	}

}
