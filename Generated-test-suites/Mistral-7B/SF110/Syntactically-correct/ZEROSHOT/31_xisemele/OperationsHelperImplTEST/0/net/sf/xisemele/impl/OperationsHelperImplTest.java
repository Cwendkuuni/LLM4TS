package net.sf.xisemele.impl;

import static org.junit.Assert.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Node;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class OperationsHelperImplTest {

 private static final String TEST_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
         "<root>\n" +
         "<element1>\n" +
         "<subelement1>\n" +
         "<subsubelement1/>\n" +
         "</subelement1>\n" +
         "</element1>\n" +
         "<element2>\n" +
         "</element2>\n" +
         "</root>";

 @Test
 public void testChildren() {
     try {
         SAXBuilder builder = new SAXBuilder();
         Document document = builder.build(new StringReader(TEST_XML));
         Node node = document.getRootElement();
         List<Node> children = new OperationsHelperImpl().children(node);
         List<String> expectedChildrenNames = new ArrayList<>();
         expectedChildrenNames.add("element1");
         expectedChildrenNames.add("element2");
         assertEquals(expectedChildrenNames.size(), children.size());
         for (int i = 0; i < expectedChildrenNames.size(); i++) {
             assertEquals(expectedChildrenNames.get(i), children.get(i).getName());
         }
     } catch (JDOMException e) {
         e.printStackTrace();
         fail("Unexpected JDOMException: " + e.getMessage());
     }
 }

 @Test
 public void testFind() {
     try {
         SAXBuilder builder = new SAXBuilder();
         Document document = builder.build(new StringReader(TEST_XML));
         String path = "root/element1/subelement1";
         Node node = new OperationsHelperImpl().find(document, path);
         assertNotNull(node);
         assertEquals("subelement1", node.getName());
     } catch (JDOMException e) {
         e.printStackTrace();
         fail("Unexpected JDOMException: " + e.getMessage());
     }
 }

 @Test
 public void testFindNull() {
     try {
         SAXBuilder builder = new SAXBuilder();
         Document document = builder.build(new StringReader(TEST_XML));
         String path = "root/nonExistentElement";
         Node node = new OperationsHelperImpl().find(document, path);
         assertNull(node);
     } catch (JDOMException e) {
         e.printStackTrace();
         fail("Unexpected JDOMException: " + e.getMessage());
     }
 }

 @Test
 public void testNodeWithName() {
     try {
         SAXBuilder builder = new SAXBuilder();
         Document document = builder.build(new StringReader(TEST_XML));
         List<Node> nodes = document.getRootElement().getChildren();
         String name = "element1";
         Node node = new OperationsHelperImpl().nodeWithName(nodes, name);
         assertNotNull(node);
         assertEquals(name, node.getName());
     } catch (JDOMException e) {
         e.printStackTrace();
         fail("Unexpected JDOMException: " + e.getMessage());
     }
 }

 @Test
 public void testNodeWithNameNull() {
     try {
         SAXBuilder builder = new SAXBuilder();
         Document document = builder.build(new StringReader(TEST_XML));
         List<Node> nodes = document.getRootElement().getChildren();
         String name = null;
         Node node = new OperationsHelperImpl().nodeWithName(nodes, name);
         assertNull(node);
     } catch (JDOMException e) {
         e.printStackTrace();
         fail("Unexpected JDOMException: " + e.getMessage());
     }
 }

 @Test
 public void testSublistWithName() {
     try {
         SAXBuilder builder = new SAXBuilder();
         Document document = builder.build(new StringReader(TEST_XML));
         List<Node> nodes = document.getRootElement().getChildren();
         String name = "element1";
         List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, name);
         List<String> expectedSublistNames = new ArrayList<>();
         expectedSublistNames.add("subelement1");
         assertEquals(expectedSublistNames.size(), sublist.size());
         for (int i = 0; i < expectedSublistNames.size(); i++) {
             assertEquals(expectedSublistNames.get(i), sublist.get(i).getName());
         }
     } catch (JDOMException e) {
         e.printStackTrace();
         fail("Unexpected JDOMException: " + e.getMessage());
     }
 }

 @Test
 public void testSublistWithNameEmpty() {
     try {
         SAXBuilder builder = new SAXBuilder();
         Document document = builder.build(new StringReader(TEST_XML));
         List<Node> nodes = document.getRootElement().getChildren();
         String name = "nonExistentElement";
         List<Node> sublist = new OperationsHelperImpl().sublistWithName(nodes, name);
         assertTrue(sublist.isEmpty());
     } catch (JDOMException e) {
         e.printStackTrace();
         fail("Unexpected JDOMException: " + e.getMessage());
     }
 }
}