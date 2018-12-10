package com.xiangzi.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseController {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	// 获取Parameter
	public String getParameter(HttpServletRequest request, String name) {
		return request.getParameter(name);
	}

	// 获取Parameter数组
	public String[] getParameterValues(HttpServletRequest request, String name) {
		return request.getParameterValues(name);
	}

	public String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

}
