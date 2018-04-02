package com.test.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.test.to.XMLSchemaDefinitionTO;

/**
* The HashMapGenerator tool brings in the parsed XSD in form
* of a tree, makes an Hashmap of the complex and the simple types/ 
*
* @author  abguha
* @version 1.0
* @since   2017-02-16 
*/
public class HashMapGenerator {
	
	
	/**
	   * This method is used to traverse through the tree and
	   * makes an Hashmap of the complex and the simple type
	   * @param XMLSchemaDefinitionTO This is the tree of the XSD file
	   * @return HashMap<String,List<String>> This returns Hashmap of the complex and the simple types .
	   */
	public HashMap<String,List<String>> traverseTreeAndMakeHashMap(XMLSchemaDefinitionTO node) {
		
			HashMap<String,List<String>> map = new HashMap<String,List<String>>();
		
		    if (node==null)
		        return map;
		    
		    Stack<XMLSchemaDefinitionTO> stk = new Stack<XMLSchemaDefinitionTO>();
		    stk.push(node);

		    while (!stk.empty()) {
		    	XMLSchemaDefinitionTO top = stk.pop();
		        for (XMLSchemaDefinitionTO child : top.getChildren()) {
		            stk.push(child);
		        }
		        makeHashMap(top,map);
		    }
		    
		    return map;
		    
	}
	
	private void makeHashMap(XMLSchemaDefinitionTO node, HashMap<String,List<String>> map) {
		
		if (node==null)
	        return;
		
		if("simple type".equalsIgnoreCase(node.getTagType()))
			return;
		
		String str = node.getName();
		List<String> childList = null;
		
		if(node.getChildren()!=null
				&& !node.getChildren().isEmpty()) {
			
			childList = new ArrayList<String>();
			
			for(XMLSchemaDefinitionTO eachNode : node.getChildren()) {
				childList.add(eachNode.getName());
			}
			
		}
		
		map.put(str, childList);
		
	}
	
	
}

