package com.test.generator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
* The CSVGenerator tool brings in the parsed XSD in form
* of a tree, makes an Hashmap of the complex and the simple types/ 
*
* @author  abguha
* @version 1.0
* @since   2017-02-16 
*/
public class CSVGenerator {
	
	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final String ID = "id";
	private static final String PARENT_ID = "parentID";
	private static final String FILE_PATH = "C:\\Users\\abguha\\Downloads\\file_load_prototype\\output";

	
	/**
	   * This method is used to create the number of CSV files 
	   * as number of keys in the map and 
	   * with headers according to the number of simple nodes in the map
	   * @param HashMap<String,List<String>> This is the Hashmap of the complex and the simple types
	   * @return 
	   */
	public void createCSVs(HashMap<String,List<String>> map) {
		
		if (map==null
				|| map.isEmpty())
	        return;
		
		for(String key: map.keySet()) {
			
			createCSVFileWithHeaders(key, map.get(key));
			
		}
	}
	
	private void createCSVFileWithHeaders(String fileName, List<String> simpleTypes) {
		
					FileWriter fileWriter = null;
		
					try {
						fileWriter = new FileWriter(getAbsoluteFileName(fileName));
						int cnt = 0;
						
						fileWriter.append(ID);
						fileWriter.append(COMMA_DELIMITER);
						fileWriter.append(PARENT_ID);
						fileWriter.append(COMMA_DELIMITER);
						
						for (String simpleType : simpleTypes) {
							cnt++;
							fileWriter.append(simpleType);
							if(cnt<simpleTypes.size())
							fileWriter.append(COMMA_DELIMITER);
			            }
						fileWriter.append(NEW_LINE_SEPARATOR);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					try {
						fileWriter.flush();
						fileWriter.close();
					} catch (IOException e) {
						System.out.println("Error while flushing/closing fileWriter !!!");
						e.printStackTrace();
					}

					
					

	}
	
	private String getAbsoluteFileName(String fileName) {
		
		return FILE_PATH + "\\" + fileName + ".txt";
	}

}
