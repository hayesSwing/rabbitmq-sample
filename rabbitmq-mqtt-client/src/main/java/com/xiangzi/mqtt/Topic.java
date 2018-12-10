package com.xiangzi.mqtt;

import java.util.Map;

public class Topic {

//	public static String cardPayMsg = "%s:%s:pay";

	private String topicName; // 主题名称

	private String tenantId; // 企业编号

	private String shopNo; // 门店编号

	private String posNo; // pos编号

	private String workerNo; // 员工编号

	private Map<String, Object> ext; // 附加信息

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getShopNo() {
		return shopNo;
	}

	public void setShopNo(String shopNo) {
		this.shopNo = shopNo;
	}

	public String getPosNo() {
		return posNo;
	}

	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}

	public String getWorkerNo() {
		return workerNo;
	}

	public void setWorkerNo(String workerNo) {
		this.workerNo = workerNo;
	}

	public Map<String, Object> getExt() {
		return ext;
	}

	public void setExt(Map<String, Object> ext) {
		this.ext = ext;
	}

	@Override
	public String toString() {
		return "Topic [topicName=" + topicName + ", shopNo=" + shopNo + ", posNo=" + posNo + ", workerNo=" + workerNo + ", ext=" + ext + "]";
	}

}
