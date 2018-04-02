package com.test.factory;

import com.test.generator.CSVGenerator;
import com.test.parser.HashMapGenerator;
import com.test.parser.MyXSOMParser;
import com.test.parser.XMLHandler;

public class ObjectFactory {
	
	CSVGenerator csvGenerator;
	MyXSOMParser myXSOMParser;
	XMLHandler xmlHandler;
	HashMapGenerator hashMapGenerator;
	
	public ObjectFactory() {
		
		csvGenerator = new CSVGenerator();
		myXSOMParser = new MyXSOMParser();
		hashMapGenerator = new HashMapGenerator();
		xmlHandler = new XMLHandler();
		
		
	}
	
	
	public Object getInstance(String name) {
		
		if(IObjectFactoryConstants.INSTANCE_XSOM_PARSER.equalsIgnoreCase(name)) {
			return myXSOMParser;
		}
		
		if(IObjectFactoryConstants.INSTANCE_HASHMAP_GEN.equalsIgnoreCase(name)) {
			return hashMapGenerator;
		}
		
		if(IObjectFactoryConstants.INSTANCE_CSV_GENERATOR.equalsIgnoreCase(name)) {
			return csvGenerator;
		}
		
		if(IObjectFactoryConstants.INSTANCE_XML_HANDLER.equalsIgnoreCase(name)) {
			return xmlHandler;
		}
		
		return null;
		
	}
	

}
