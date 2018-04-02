package com.test.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.xml.sax.SAXException;

import com.sun.xml.xsom.ForeignAttributes;
import com.sun.xml.xsom.XSAttGroupDecl;
import com.sun.xml.xsom.XSAttributeDecl;
import com.sun.xml.xsom.XSAttributeUse;
import com.sun.xml.xsom.XSComplexType;
import com.sun.xml.xsom.XSContentType;
import com.sun.xml.xsom.XSElementDecl;
import com.sun.xml.xsom.XSModelGroup;
import com.sun.xml.xsom.XSParticle;
import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.XSTerm;
import com.sun.xml.xsom.XSType;
import com.sun.xml.xsom.parser.XSOMParser;
import com.test.to.XMLSchemaDefinitionTO;




/**
* The MyXSOMParser tool brings in the path of the XSD File as a parameter
* and reads the contents of the XSD to make a tree structure and returns it.
*
* @author  abguha
* @version 1.1
* @since   2017-02-16 
*/
public class MyXSOMParser {	
	
	/**
	   * This method is used to read the initial / root XSD tag 
	   * distinguish if it is a complex node, if so pass it for
	   * further processing to the other overloaded method.
	   * @param path This is the path of the XSD file
	   * @return XMLSchemaDefinitionTO This returns tree of the XSD structure.
	   */
	public HashMap<String,List<String>> getXMLElements(String path) {

		List<String> list = new ArrayList<String>();
		XMLSchemaDefinitionTO rootNode = null;
		HashMap<String,List<String>> map = new HashMap<String,List<String>>();

		XSOMParser parser = new XSOMParser();
		try {
			parser.parse(path);
			XSSchemaSet schemaSet = parser.getResult();

			Iterator<XSElementDecl> itrElements = schemaSet.iterateElementDecls();

			while (itrElements.hasNext()) {

				XSElementDecl xsElementDecl = (XSElementDecl) itrElements.next();
				
				System.out.println("Element Name : " + xsElementDecl.getName());
				System.out.println("Element Type : " + xsElementDecl.getType());

				XSComplexType xsComplexType = xsElementDecl.getType().asComplexType();
				if (xsComplexType != null) {
					//START: Code to store Data into a Tree
					rootNode = new XMLSchemaDefinitionTO("complex type", xsElementDecl.getName(), xsElementDecl.getType().toString());
					rootNode.setBaap(null);
					//END: Code to store Data into a Tree
					
					//START: Code to store Data into a HashMap
					List<String> listOfChild = new ArrayList<String>();
					map.put(xsElementDecl.getName(), listOfChild);
					//END: Code to store Data into a HashMap
					
					XSContentType xsContentType = xsComplexType.getContentType();
					XSParticle particle = xsContentType.asParticle();
					getXMLElements(list, particle, rootNode, map, xsElementDecl.getName());
				}
			}
			
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return map;
	}


	
	/**
	   * This method is used to reads all the XSD tags 
	   * distinguish if it is a complex node, if so then it
	   * recursively calls itself.
	   * @param path list This maintains a list of all the elements
	   * @param xsParticle This is the XSD particle which carries the information of each parent node into the method
	   * @param node list This is the XMLSchemaDefinitionTO tree node
	   * @return XMLSchemaDefinitionTO This returns tree of the XSD structure.
	   */
	private void getXMLElements(List<String> list, XSParticle xsParticle, XMLSchemaDefinitionTO node, HashMap<String,List<String>> map, String parentKey) {

		
		if (xsParticle != null) {

			XSTerm pterm = xsParticle.getTerm();

			
			if (pterm.isElementDecl()) {
				// When it is an element	
				if (xsParticle.getMinOccurs() == 0) {
					list.add(pterm.getSourceDocument().getTargetNamespace() + ":" + pterm.asElementDecl().getName());
				}

				XSComplexType xsComplexType = (pterm.asElementDecl()).getType().asComplexType();
				System.out.println("Min Occurs : " + xsParticle.getMinOccurs());
				
				if (xsComplexType != null && !(pterm.asElementDecl().getType()).toString().contains("Enumeration")) {
					System.out.println();
				}
				System.out.println("Element Name : " + pterm.asElementDecl().getName());
				System.out.println("Element Type : " + pterm.asElementDecl().getType());
				
				// This is the check to see if this is a complex node or a leaf node
				if (xsComplexType != null && !(pterm.asElementDecl().getType()).toString().contains("Enumeration")) {

					XSContentType xsContentType = xsComplexType.getContentType();

					XSParticle xsParticleInside = xsContentType.asParticle();
					
					//START: Code to store Data into a Tree
					XMLSchemaDefinitionTO newChild = new XMLSchemaDefinitionTO("complex type", null, null);
					if(node!=null){
						node.addChild(newChild);
						newChild.setBaap(node);
					}
					
					newChild.setName(pterm.asElementDecl().getName());
					newChild.setType(pterm.asElementDecl().getType().toString());
					//END: Code to store Data into a Tree
					
					//START: Code to store Data into a HashMap
					List<String> listOfChild = new ArrayList<String>();
					map.put(pterm.asElementDecl().getName(), listOfChild);
					//END: Code to store Data into a HashMap
					
					
					// As this is a complex node and not a leaf node recursive call
					getXMLElements(list, xsParticleInside, newChild, map, pterm.asElementDecl().getName());
				}else {
					
					//START: Code to store Data into a Tree
					XMLSchemaDefinitionTO newChild = new XMLSchemaDefinitionTO("simple type", null, null);
					if(node!=null){
						node.addChild(newChild);
						newChild.setBaap(node);
					}
					newChild.setName(pterm.asElementDecl().getName());
					newChild.setType(pterm.asElementDecl().getType().toString());
					//END: Code to store Data into a Tree
					
					//START: Code to store Data into a HashMap
					map.get(parentKey).add(pterm.asElementDecl().getName());
					//END: Code to store Data into a HashMap
					
				}
				
					
				
			} else if (pterm.isModelGroup()) {
				
				// When it group of elements
				XSModelGroup xsModelGroup2 = pterm.asModelGroup();
				XSParticle[] xsParticleArray = xsModelGroup2.getChildren();
				for (XSParticle xsParticleTemp : xsParticleArray) {
					
					getXMLElements(list, xsParticleTemp, node, map, parentKey);
					
				}
			}
		}
	}

	@Deprecated
	public void parse() {
		
	    try {
	        File file = new File("C:/Users/abguha/Downloads/file_load_prototype/simple-admissions-schema.xsd");
	        
	        XSOMParser parser = new XSOMParser(); 
            parser.parse(file); 
            XSSchemaSet xs= parser.getResult(); 
            Iterator<XSElementDecl> jtr = xs.iterateElementDecls();
            System.out.println("XML Schema:\n");
            System.out.println("Elements:");
            while( jtr.hasNext() ) {
              System.out.println();
              XSElementDecl e = (XSElementDecl)jtr.next();
              
              XSType xtype=e.getType();
              if (xtype.isComplexType()) {
            	  List<? extends ForeignAttributes> fAttr = xtype.getForeignAttributes();
            	  Iterator<? extends ForeignAttributes> iter = fAttr.iterator();
            	  while(iter.hasNext()) {
            		  System.out.println(iter.next());
            	  }
              }
              

              System.out.print( e.getName() );
              if( e.isAbstract() )
                System.out.print(" (abstract)");
              System.out.println();
            }
            
            System.out.println();
            Iterator<XSComplexType> itr = xs.iterateComplexTypes();
            System.out.println("Complex Types:");
            int cnt = 0;
            while( itr.hasNext() ) {
            	cnt++;
            	
            	
            	XSComplexType cmplxType = (XSComplexType)itr.next();
            	if(cnt == 1)continue;
            	
            	System.out.println();

              System.out.print( cmplxType.getName() );
              if( cmplxType.isAbstract() )
                System.out.print(" (abstract)");
              System.out.println();
              
              // Trying method getElementDecls()
              List<XSElementDecl> ktr = cmplxType.getElementDecls();
              System.out.println("Elements:");
              for(XSElementDecl e1 : ktr ) {
                System.out.println();

                System.out.print( e1.getName() );
                if( e1.isAbstract() )
                  System.out.print(" (abstract)");
              }
              
              // Trying method iterateAttGroups()
              Iterator<? extends XSAttGroupDecl> ktr1 = cmplxType.iterateAttGroups();
              System.out.println("Elements:");
              while( ktr1.hasNext() ) {
                System.out.println();

                XSAttGroupDecl elem = (XSAttGroupDecl)ktr1.next();
                System.out.print( elem.getName() );
              }
              
           // Trying method getAttributeUses()
              Collection<? extends XSAttributeUse> c = cmplxType.getAttributeUses();
              Iterator<? extends XSAttributeUse> i = c.iterator();
              System.out.println("Elements:");
              while (i.hasNext())
              {
                XSAttributeDecl attributeDecl = i.next().getDecl();
                System.out.println("type: " + attributeDecl.getType());
                System.out.println("name:" + attributeDecl.getName());
              }
              
              
            }
	        
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    } catch (Throwable e) {
	        e.printStackTrace();
	    } 
	
}
}
