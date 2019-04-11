package automerge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

	public static void mergeXML(String s1, String s2, String s3){


	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = null;
	    Document doc = null;
	    Document doc2 = null;

	    try {
	            db = dbf.newDocumentBuilder();
	            doc = db.parse(new File(s1));
	            doc2 = db.parse(new File(s2));
	            Map<String, Node> xmlFile = new HashMap<>();
	            NodeList ndListFirstFile = doc.getElementsByTagName("beans");
	            NodeList ndListFirstFile2 = doc2.getElementsByTagName("bean");
	            
	            for (int j = 0; j < ndListFirstFile.item(0).getChildNodes().getLength(); j++) {
					Node nNode = ndListFirstFile.item(0).getChildNodes().item(j);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						if(xmlFile.containsKey(eElement.getAttribute("id"))) {
							ndListFirstFile.item(0).removeChild(nNode);
							
						}else {
							xmlFile.put(eElement.getAttribute("id"), nNode);
						}
						
						
					}

				}
	            
	            for (int j=0; j<ndListFirstFile2.getLength();j++) {
	            	Node nodeArea = doc.importNode(ndListFirstFile2.item(j), true);
	            	if (nodeArea.getNodeType() == Node.ELEMENT_NODE) {
	            		Element eElement = (Element) nodeArea;
	            		//System.out.println("file 2");
	            		if(eElement.getAttribute("id").length()>=1) {	            		
	            			//System.out.println(" Current element: " + nodeArea.getNodeName());
	            			//System.out.println(" ID :" + eElement.getAttribute("id"));
	            			if(xmlFile.containsKey(eElement.getAttribute("id"))) {
								//ndListFirstFile.item(0).removeChild(nodeArea);
								
							}else {
								ndListFirstFile.item(0).appendChild(nodeArea);
							}
	            			//ndListFirstFile.item(0).appendChild(nodeArea);
						}
	            	}
		            
	            }
	            
	            
	          TransformerFactory tFactory = TransformerFactory.newInstance();
	          Transformer transformer = tFactory.newTransformer();
	          transformer.setOutputProperty(OutputKeys.INDENT, "yes");  

	          DOMSource source = new DOMSource(doc);
	          StreamResult result = new StreamResult(new StringWriter());
	          transformer.transform(source, result); 

	          Writer output = new BufferedWriter(new FileWriter(s3));
	          String xmlOutput = result.getWriter().toString();  
	          output.write(xmlOutput);
	          output.close();

	    } catch (ParserConfigurationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (SAXException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } catch (TransformerException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }


	}
	public static void main(String[] args) {
		if(args.length < 3){
			System.out.println("NO arguement enter, -> java -jar XMLMerg.jar file1.xml file2.xml result.xml ");
			System.exit(0);
		}
		File file2 = new File(args[0]);
		File file1 = new File(args[1]);
		Document doc;
		try {
			
			//doc = XMLMerge.startMerge("/beans/bean", file1, file2);
			//XMLMerge.printFileStructure("/beans/bean", file1, file2);
			//XMLMerge.print(doc, args[2]);
			mergeXML(args[0], args[1],args[2]);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

}
