package com.test.handler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.test.parser.MyXSOMParser;
import com.test.utils.CSVUtils;



public class Handler extends DefaultHandler {

	private static Random randomNo = new Random();

	Stack<HashMap<String, String>> complexTypeRowStack = new Stack<HashMap<String, String>>();
	HashMap<String, List<String>> hierarchy = new HashMap<String, List<String>>();
	
	public Handler(){
		MyXSOMParser xsParser=new MyXSOMParser();
		hierarchy=xsParser.getXMLElements("resource\\simple-admissions-schema.xsd");
	}
	
	
	
	// Data Structures

	
	Stack<Long> complexIdLast = new Stack<Long>();
	
	String simpleTypeLast = null;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		if (hierarchy.containsKey(qName)) {
			long self_id = randomNo.nextLong();
			HashMap<String, String> complexRowMap = new HashMap<String, String>();
			complexRowMap.put("self_id", Long.toString(self_id));
			if (!complexIdLast.isEmpty()) {
				long parent_id = complexIdLast.peek();
				complexRowMap.put("parent_id", Long.toString(parent_id));
			}
			
			complexIdLast.push(self_id);
			complexTypeRowStack.push(complexRowMap);
		} else {
			simpleTypeLast=qName;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		complexTypeRowStack.peek().put(simpleTypeLast, new String(ch, start, length));
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(hierarchy.containsKey(qName)){
			File csvFile=new File(qName+".csv");
			
			if(!csvFile.exists()){
				try {
					FileWriter writer = new FileWriter(csvFile);
					List<String> headerList=new ArrayList<String>();
					headerList.add("self_id");
					long self_id=complexIdLast.pop();
					if(!complexIdLast.isEmpty()){
						headerList.add("parent_id");
					}
					complexIdLast.push(self_id);
					
					for(String simpleType:hierarchy.get(qName)){
						headerList.add(simpleType);
					}
					
					CSVUtils.writeLine(writer,headerList);
					writer.flush();
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			
			
		List<String> currentComplexTypeList=new ArrayList<String>();
		currentComplexTypeList.add(complexTypeRowStack.peek().get("self_id"));
		complexIdLast.pop();
		if(!complexIdLast.isEmpty()){
			currentComplexTypeList.add(complexTypeRowStack.peek().get("parent_id"));
		}
		
		for(String simpleType:hierarchy.get(qName)){
			if(complexTypeRowStack.peek().containsKey(simpleType)){
				currentComplexTypeList.add(complexTypeRowStack.peek().get(simpleType));
			}else{
				currentComplexTypeList.add("");
			}
		}
		
		//complexIdLast.pop();
		complexTypeRowStack.pop();	
		
		try {
			FileWriter writer = new FileWriter(csvFile,true);
			CSVUtils.writeLine(writer,currentComplexTypeList);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		}else{
			simpleTypeLast=null;
		}
	}
	
	
}
