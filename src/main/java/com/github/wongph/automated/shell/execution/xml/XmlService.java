package com.github.wongph.automated.shell.execution.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


@Service
public class XmlService {

	private static Logger LOGGER = LogManager.getLogger(XmlService.class);


	public String modifyXml(File xmlFile) throws Exception {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		String modifiedPath = "LOCAL TMP NEW XML";
		try (InputStream is = new FileInputStream(xmlFile)){
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);
			NodeList systemDateList = doc.getElementsByTagName("system_date");
			Node systemDateNodes = systemDateList.item(0);
			if (systemDateNodes.getNodeType() == Node.ELEMENT_NODE) {
				Node systemDateNode = systemDateNodes.getChildNodes().item(0);
				String systemDateValue = systemDateNode.getNodeValue();
				LOGGER.info("Old System Date: {}", systemDateValue);
				systemDateNode.setNodeValue("01012019");

			}
			
			try (FileOutputStream output =
					new FileOutputStream(modifiedPath)) {
				writeXml(doc, output);
			}
		}
		return modifiedPath;
	}


		// write doc to output stream
		private static void writeXml(Document doc,
				OutputStream output)
						throws TransformerException, UnsupportedEncodingException {

			TransformerFactory transformerFactory = TransformerFactory.newInstance();

			// The default add many empty new line, not sure why?
			// https://mkyong.com/java/pretty-print-xml-with-java-dom-and-xslt/
			// Transformer transformer = transformerFactory.newTransformer();

			// add a xslt to remove the extra newlines
			Transformer transformer = transformerFactory.newTransformer();

			// pretty print
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(output);

			transformer.transform(source, result);

		}


	}
