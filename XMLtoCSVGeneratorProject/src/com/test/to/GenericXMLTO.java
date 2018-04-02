package com.test.to;

import java.util.ArrayList;
import java.util.List;

/**
* The Transition Object contains the generic tree of the
* XML key values.
*
* @author  abguha
* @version 1.0
* @since   2017-02-16 
*/
public class GenericXMLTO {
	
	private String key;
	private String value;
	private String type;
	private List<GenericXMLTO> children = null;
	
	public GenericXMLTO(String key, String value, String type) {
		super();
		this.key = key;
		this.value = value;
		this.type = type;
		this.children = new ArrayList<GenericXMLTO>();
	}	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<GenericXMLTO> getChildren() {
		return children;
	}
	public void setChildren(List<GenericXMLTO> children) {
		this.children = children;
	}
	
}
