package com.jiuzhe.core.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class XmlDemo01 {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse("Jiuzhe-core\\src\\main\\resources\\xml01.xml");
            // 节点集
            NodeList nodeList = document.getElementsByTagName("person");

            int nodeCnt = nodeList.getLength();
            System.err.println("一共获取到" + nodeCnt);

            for (int i = 0; i < nodeCnt; i++) {
                Node book = nodeList.item(i);
                NamedNodeMap attrs = book.getAttributes();
                for (int j = 0; j < attrs.getLength(); j++) {
                    Node attr = attrs.item(j);
                    System.err.println(attr.getNodeName() + "---" + attr.getNodeValue());//id

                }

                NodeList childNodes = book.getChildNodes();
                for (int k = 0; k < childNodes.getLength(); k++) {
                    if (childNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
                        System.out.println(childNodes.item(k).getNodeName() + "---" + childNodes.item(k).getTextContent());
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
}
