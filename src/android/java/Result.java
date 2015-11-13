package com.cordova.qiniu.yumemor.bean;

/**
 * @author yy 结果
 */
public class Result {
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 状态
	 */
	private int status;
	/**
	 * 消息
	 */
	private String msg;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("{'name':'%s','status':'%s','msg':'%s'}", name,status, msg);
	}
}
