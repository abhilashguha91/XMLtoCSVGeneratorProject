package com.test.to;

import java.util.ArrayList;
import java.util.List;

/**
* The Transition Object conrtains the tree structure of the XSD.
*
* @author  abguha
* @version 1.0
* @since   2017-02-16 
*/
public class XMLSchemaDefinitionTO {

	private String tagType;
	private String name;
	private String type;
	private List<XMLSchemaDefinitionTO> children = null;
	private XMLSchemaDefinitionTO baap = null;
	
	public XMLSchemaDefinitionTO(String tagType,String name,String type){
		this.tagType = tagType;
		this.name = name;
		this.type = type;
		this.children = new ArrayList<>();
	}
	
	
	public String getTagType() {
		return tagType;
	}
	public void setTagType(String tagType) {
		this.tagType = tagType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public List<XMLSchemaDefinitionTO> getChildren() {
		return children;
	}

	public void setChildren(List<XMLSchemaDefinitionTO> children) {
		this.children = children;
	}

	public void addChild(XMLSchemaDefinitionTO child)
    {
        children.add(child);
    }

	public XMLSchemaDefinitionTO getBaap() {
		return baap;
	}

	public void setBaap(XMLSchemaDefinitionTO baap) {
		this.baap = baap;
	}
	
	
}
