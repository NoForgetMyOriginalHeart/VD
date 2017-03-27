package com.tc.vd.addressBook;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 通讯目标配置
 * @author tangcheng
 *
 */
public class ContactGoalConfig {
	private StringProperty name;			//名称
	private StringProperty protocol;		//协议
	private StringProperty ip;				//ip
	private IntegerProperty port;			//port
	private IntegerProperty sendLenBegin;	//发送开始位置
	private IntegerProperty sendLenEnd;		//发送结束位置
	private IntegerProperty recvLenBegin;	//接收开始位置
	private IntegerProperty recvLenEnd;		//接收结束位置
	private IntegerProperty timeOut;			//超时时间
	private StringProperty encoding;		//ͨ编码
	private IntegerProperty bufferSize;		//缓冲大小
	private StringProperty comment;		//备注
//	private String name;			//名称
//	private String protocol;		//协议
//	private String ip;				//ip
//	private String port;			//port
//	private int sendLenBegin;	//发送开始位置
//	private int sendLenEnd;		//发送结束位置
//	private int recvLenBegin;	//接收开始位置
//	private int recvLenEnd;		//接收结束位置
//	private int timeOut;			//超时时间
//	private String encoding;		//ͨ编码
//	private int bufferSize;		//缓冲大小
//	private String comment;		//备注
	
	public ContactGoalConfig(String name, String protocol, String ip, Integer port) {
		setName(name);
		setProtocol(protocol);
		setIp(ip);
		setPort(port);
		setSendLenBegin(8);
		setSendLenEnd(8);
		setRecvLenBegin(8);
		setRecvLenEnd(8);
		setTimeOut(60);
		setEncoding("utf8");
		setBufferSize(1000);
		setComment("");
	}

	public int getRecvLenBegin() {
		return recvLenBegin.get();
	}

	public void setRecvLenBegin(int recvLenBegin) {
		if(null == this.recvLenBegin){
			this.recvLenBegin = new SimpleIntegerProperty(8);
			return;
		}
		this.recvLenBegin.set(recvLenBegin);
	}

	public int getSendLenBegin() {
		return sendLenBegin.get();
	}

	public void setSendLenBegin(int sendLenBegin) {
		if(null == this.sendLenBegin){
			this.sendLenBegin = new SimpleIntegerProperty(8);
			return;
		}
		this.sendLenBegin.set(sendLenBegin);
	}

	public int getRecvLenEnd() {
		return recvLenEnd.get();
	}

	public void setRecvLenEnd(int recvLenEnd) {
		if(null == this.recvLenEnd){
			this.recvLenEnd = new SimpleIntegerProperty(8);
			return;
		}
		this.recvLenEnd.set(recvLenEnd);
	}

	public int getSendLenEnd() {
		return sendLenEnd.get();
	}

	public void setSendLenEnd(int sendLenEnd) {
		if(null == this.sendLenEnd){
			this.sendLenEnd = new SimpleIntegerProperty(8);
			return;
		}
		this.sendLenEnd.set(sendLenEnd);
	}

	public Integer getTimeOut() {
		return timeOut.get();
	}

	public void setTimeOut(int timeOut) {
		if(null == this.timeOut){
			this.timeOut = new SimpleIntegerProperty(60);
			return;
		}
		this.timeOut.set(timeOut);
	}

	public String getEncoding() {
		return encoding.get();
	}

	public void setEncoding(String encoding) {
		if(null == this.encoding){
			this.encoding = new SimpleStringProperty("utf8");
			return;
		}
		this.encoding.set(encoding);
	}

	public int getBufferSize() {
		return bufferSize.get();
	}

	public void setBufferSize(int bufferSize) {
		if(null == this.bufferSize){
			this.bufferSize = new SimpleIntegerProperty(1000);
			return;
		}
		this.bufferSize.set(bufferSize);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		if(null == this.name){
			this.name = new SimpleStringProperty("");
			return;
		}
		this.name.set(name);
	}

	public URI getUri() {
		try {
			String protocol = this.protocol.get();
			String ip = this.ip.get();
			String port = String.valueOf(this.port.get());
			return new URI(protocol, ip, port, "");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getComment() {
		return comment.get();
	}

	public void setComment(String comment) {
		if(null == this.comment){
			this.comment = new SimpleStringProperty("");
			return;
		}
		this.comment.set(comment);
	}

	public String getProtocol() {
		return protocol.get();
	}

	public void setProtocol(String protocol) {
		if(null == this.protocol){
			this.protocol = new SimpleStringProperty("http");
			return;
		}
		this.protocol.set(protocol);
	}

	public String getIp() {
		return ip.get();
	}

	public void setIp(String ip) {
		if(null == this.ip){
			this.ip = new SimpleStringProperty("127.0.0.1");
			return;
		}
		this.ip.set(ip);
	}

	public Integer getPort() {
		return port.get();
	}

	public void setPort(Integer port) {
		if(null == this.port){
			this.port = new SimpleIntegerProperty(9001);
			return;
		}
		this.port.set(port);
	}

	@Override
	public String toString() {
		return "ContactGoalConfig{" +
				"name=" + name +
				", protocol=" + protocol +
				", ip=" + ip +
				", port=" + port +
				", sendLenBegin=" + sendLenBegin +
				", sendLenEnd=" + sendLenEnd +
				", recvLenBegin=" + recvLenBegin +
				", recvLenEnd=" + recvLenEnd +
				", timeOut=" + timeOut +
				", encoding=" + encoding +
				", bufferSize=" + bufferSize +
				", comment=" + comment +
				'}';
	}
}
