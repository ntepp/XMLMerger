package automerge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

public class XMLMerge {


	public static Document startMerge(String expression, File... files) {
		try {
			System.out.println("starting...");
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xpath = xPathFactory.newXPath();
			XPathExpression compiledExpression = xpath.compile(expression);
			return merge(compiledExpression, files);
		} catch (Exception e) {
			System.out.println("error getting the xpath" + e.getMessage());
			System.exit(0);
		}
		return null;
	}

	public static Document merge(XPathExpression expression, File... files) {
		try {
			Map<String, Node> xmlFile = new HashMap<>();
			System.out.println("merging...");
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			docBuilderFactory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document base = (Document) docBuilder.parse(files[0]);

			/* START CREATING MAP FROM THE FIRST FILE */
			XPath xPath0 = XPathFactory.newInstance().newXPath();
			String nExpression0 = "/beans/bean";
			NodeList nodeList0 = (NodeList) xPath0.compile(nExpression0).evaluate(base, XPathConstants.NODESET);
			for (int j = 0; j < nodeList0.getLength(); j++) {
				Node nNode = nodeList0.item(j);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					xmlFile.put(eElement.getAttribute("id"), nNode);
					System.out.println("file 0");
					System.out.println(" Current element: " + nNode.getNodeName());
					System.out.println(" ID :" + eElement.getAttribute("id"));
					Node nNodeF1 = nNode.getFirstChild();
					Element eElementF1 = (Element) nNodeF1;
				}

			}

			// Iterator<String> keys = xmlFile.keySet().iterator();
			// keys.forEachRemaining(p ->{System.out.print(p+"-");});
			/* END CREATING MAP */

			Node results = (Node) expression.evaluate(base, XPathConstants.NODE);

			if (results == null) {
				throw new IOException(files[0] + ": expression does not evaluate to node");
			}

			Document merge = (Document) docBuilder.parse(files[1]);

			Node nextResults = (Node) expression.evaluate(merge, XPathConstants.NODE);
			XPath xPath = XPathFactory.newInstance().newXPath();

			String nExpression = "/beans/bean";
			NodeList nodeList = (NodeList) xPath.compile(nExpression).evaluate(merge, XPathConstants.NODESET);

			// NodeList nodeList = (NodeList)
			// XPathFactory.newInstance().newXPath().compile(expression).evaluate(merge,
			// XPathConstants.NODESET);
			
			
			/*
			 * while (nextResults.hasChildNodes()) { Node kid = (Node)
			 * nextResults.getFirstChild(); nextResults.removeChild(kid); kid =
			 * base.importNode(kid, true);
			 * 
			 * results.appendChild(kid); }
			 */
			System.out.println("file 1");
			// System.out.println("Size: "+nodeList.getLength());
			for (int j = 0; j < nodeList.getLength(); j++) {

				Node nNode = nodeList.item(j);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					//Node nodebean = base.importNode(merge, true);
					//nodeList0.item(0).appendChild(nodebean);
					System.out.println(" Current element: " + nNode.getNodeName());
					System.out.println(" ID :" + eElement.getAttribute("id"));
					

					if (!xmlFile.containsKey(eElement.getAttribute("id"))) {
						// System.out.println("true");
						//results.appendChild(nNode);
					}
				}

			}
			return base;
		} catch (Exception e) {
			System.out.println("error merging :" + e.getMessage());
			System.exit(0);
		}
		return null;
	}

	public static void print(Document doc, String resultPath) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new StringWriter());
			transformer.transform(source, result);
			Writer output = new BufferedWriter(new FileWriter(resultPath));
			String xmlOutput = result.getWriter().toString();

			output.write(xmlOutput);
			output.close();
			System.out.println("done");
			
		} catch (Exception e) {
			System.out.println("error printing :" + e.getMessage());
			System.exit(0);
		}
		

	}
//	public static List<Node> getListNode (Document doc){
//		Element el = doc.getDocumentElement();
//		String tag = "bean";
//		NodeList liste = el.getElementsByTagName(tag);
//		liste.
//		return null;
//	}

	public static void printFileStructure(String string, File file1, File file2) {
		// TODO Auto-generated method stub
		
	}

	

}
