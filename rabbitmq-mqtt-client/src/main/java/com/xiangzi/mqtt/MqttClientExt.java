package com.xiangzi.mqtt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttClientExt {

	private static Logger logger = LoggerFactory.getLogger(MqttClientExt.class);
	
	private final int QOS = 2;

	private ScheduledExecutorService scheduler;

	private MqttClient mqttClient;

	private MqttConnectOptions options;

	private String clientId;

	private String host;

	private Integer port;

	private Integer keepAlive;

	private Integer connectionTimeout;

	private String userName = "00000";

	private String passwd = "0000000000000000000";

	private Queue<Map<String,Object>> wait = null;
	
	public MqttClientExt() {
		wait = new ConcurrentLinkedQueue<Map<String,Object>>();
	}
	

	private void connect() {
		try {

			logger.info("初始化MQTT消息服务......");
			
			String serverURI = "tcp://" + this.getHost() + ":" + this.getPort();

			MemoryPersistence persistence = new MemoryPersistence();
			
			this.mqttClient = new MqttClient(serverURI, this.getClientId(), persistence);

			// MQTT的连接设置
			this.options = new MqttConnectOptions();
			
			//设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
			//如果是true，那么清理所有离线消息，即QoS1或者2的所有未接收内容
			this.options.setCleanSession(false);

			this.options.setMaxInflight(10000);
			
			// 设置超时时间 单位为秒  
			this.options.setConnectionTimeout(this.getConnectionTimeout());  
            // 设置会话心跳时间 单位为秒 服务器会每隔20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制  
			this.options.setKeepAliveInterval(this.getKeepAlive());
            
			this.options.setUserName(this.getUserName());
			
			this.options.setPassword(this.getPasswd().toCharArray());            
			//设置是否自动重连
			this.options.setAutomaticReconnect(false);// this.options.setAutomaticReconnect(true);            
			// 设置回调  
            mqttClient.setCallback(new PushCallback()); 
           
            mqttClient.connect(options); 
            
		} catch (Exception ex) {
			
			mqttClient = null;
			
			logger.error("连接MQTT消息中心异常",ex);
		}
	}
	
	public void init() {
		
		try {

			this.connect();
            
		} catch (Exception ex) {
			logger.error("连接MQTT消息中心异常",ex);
		}
		finally {
			logger.info("连接MQTT消息中心状态:" + this.isConnected());
			
	        scheduler = Executors.newSingleThreadScheduledExecutor();  
	        scheduler.scheduleAtFixedRate(new Runnable() {  
	            public void run() {  
	            	try {  
	            		logger.info("MQTT消息中心状态<{}>,消息队列<{}>",isConnected(),wait.size());	
	            		
	            		if(!isConnected()) {
	            			
	            			logger.info("重新连接MQTT消息中心......");
	            			
	            			connect(); 
	            		}

	            		if(!wait.isEmpty()) {
	            			//循环次数
	            			int loop = wait.size() > 100 ? 100 : wait.size();
	            			
	            			for(int i = 0;i<loop;i++) {
	            				
	            				Map<String,Object> data = wait.poll();
		            			
		            			Date date = (Date) data.get("date");
	            				Integer count = (Integer) data.get("count");
	            				
	            				long currDate = new Date().getTime();
	            				//发送超过10次或超过5分钟丢弃
	            				if(count > 10 || currDate - date.getTime() > 5*60*60*1000) {
	            					return;
	            				}
	            				
		            			if(isConnected()) {
		            				
		            				String topic = data.get("topic").toString();
		            				String payload = data.get("payload").toString();
		            				
		            				Publish(topic,payload);
		            			}
		            			else {	            				
		            				data.put("date", new Date());
		            				
		            				count++;
		            				data.put("count", count);
		            				
		            				wait.add(data);
		            			}
		            			
		            			Thread.sleep(10);
	            			}
	            		}
	            		
	                } catch (Exception e) { 
	                	logger.error("检测MQTT消息中心状态异常",e);
	                } 	            	
	            }  
	        }, 20 * 1000, 30 * 1000, TimeUnit.MILLISECONDS); 
		}
	}

	public void destroy() {
		try {  
			
			if(this.isConnected()) {
				
				this.mqttClient.disconnect(); 
				
				logger.info("断开MQTT消息中心连接");
			}
        	 
			this.mqttClient = null;
			
       } catch (MqttException ex) {  
    	   logger.error("断开MQTT消息中心连接异常",ex);
       }  
	}
	
	public boolean isConnected() {		
		return this.mqttClient != null && this.mqttClient.isConnected();
	}
	
	public void Publish(String topic, String payload,Integer count) {
		
		try {
			if(this.isConnected()) {
				
				MqttMessage message = new MqttMessage(payload.getBytes());
		        
		        message.setQos(this.QOS);

		        mqttClient.publish(topic, message);
		        
		        logger.debug("<{}>进行第<{}>次发布...",topic,count);
			}
			else 
			{
				Map<String,Object> data = new HashMap<String,Object>();
				data.put("topic", topic);
				data.put("payload", payload);
				
				data.put("date", new Date());
				count++;
				data.put("count", count);
				
				this.wait.add(data);
			}
		}catch (Exception ex) {				
			logger.error("MQTT发布消息异常",ex);
		}
		
		
	}
	
	public void Publish(String topic, String payload) {		
		Publish(topic, payload,1);
    }
	
	
	public void Subscribe(String topicFilter) throws MqttException {
		
		try {
			if(this.isConnected()) {				
				mqttClient.subscribe(topicFilter, this.QOS);
			}
		}catch (Exception ex) {	
			logger.error("MQTT订阅异常",ex);
		}
	}

	public void Unsubscribe(String topicFilter) throws MqttException {
		
		try {
			if(this.isConnected()) {				
				mqttClient.unsubscribe(topicFilter);
			}
		}catch (Exception ex) {	
			logger.error("MQTT退订异常",ex);
		}
    }
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(Integer keepAlive) {
		this.keepAlive = keepAlive;
	}

	public Integer getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(Integer connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
