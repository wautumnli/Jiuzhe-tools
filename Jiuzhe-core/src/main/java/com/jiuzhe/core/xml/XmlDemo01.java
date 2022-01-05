package com.jiuzhe.core.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XmlDemo01 {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse("Jiuzhe-core\\src\\main\\resources\\xml01.xml");
        NodeList nameNodeList = document.getElementsByTagName("name");
        for (int n = 0; n < nameNodeList.getLength(); n++) {
            Node node = nameNodeList.item(n);
            String textContent = node.getTextContent();
            System.out.println(textContent);
        }
        NodeList p1NodeList = document.getElementsByTagName("p1");
        for (int n = 0; n < p1NodeList.getLength(); n++) {
            Node node = p1NodeList.item(n);
            String textContent = node.getTextContent();
            System.out.println(textContent);
        }
    }
}
