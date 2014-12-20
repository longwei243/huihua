package com.saiman.smcall.domain;

import java.io.Serializable;
import java.util.List;

import com.saiman.smcall.options.netmeeting.SelectContactBean;

public class Group implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer groupId;
	
	private String groupName;
	
	private List<SelectContactBean> groupList;

	public Group() {}

	public Integer getGroupId() {
		return groupId;
	}
	
	public List<SelectContactBean> getGroupList() {
		return groupList;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public void setGroupList(List<SelectContactBean> groupList) {
		this.groupList = groupList;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
