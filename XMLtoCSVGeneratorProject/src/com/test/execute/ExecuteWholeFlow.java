package com.test.execute;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.test.handler.Handler;

public class ExecuteWholeFlow {
	public static void main(String[] args){
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		try{
			SAXParser saxParser = saxParserFactory.newSAXParser();
		Handler handler=new Handler();

		saxParser.parse("resource\\simple-admissions-instance.xml",handler);	
				}	catch(ParserConfigurationException | SAXException | IOException e){
			e.printStackTrace();
		}
	}
}
