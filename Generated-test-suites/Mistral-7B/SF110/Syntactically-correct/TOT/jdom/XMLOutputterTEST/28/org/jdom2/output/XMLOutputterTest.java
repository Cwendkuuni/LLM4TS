package org.jdom2.output.test;

import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.*;
import java.io.*;
import org.junit.*;
import static org.junit.Assert.*;

public class XMLOutputterTest {

 @BeforeClass
 static public void setUp() {
 // Initialize any static variables or objects here
 XMLOutputter.DEFAULTPROCESSOR = new DefaultXMLProcessor();
 }

 @AfterClass
 static public void tearDown() {
 // Release any static resources here
 }

 @Test
 public void testXMLOutputterWithDocument() throws IOException {
 // Test case for outputting a Document
 Document document = new Document();
 Element root = new Element("root");
 document.setRootElement(root);
 root.addContent(new Element("child1").setText("Child 1"));
 root.addContent(new Element("child2").setText("Child 2"));

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "<root><child1>Child 1</child1><child2>Child 2</child2></root>";
 String actualXml = "";

 try (StringWriter out = new StringWriter()) {
 xmlOutputter.output(document, out);
 actualXml = out.toString();
 }

 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithDocType() throws IOException {
 // Test case for outputting a DocType
 DocType docType = new DocType("html", null, null);

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "<!DOCTYPE html SYSTEM \"\">";
 String actualXml = "";

 try (StringWriter out = new StringWriter()) {
 xmlOutputter.output(docType, out);
 actualXml = out.toString();
 }

 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithElement() throws IOException {
 // Test case for outputting an Element
 Element element = new Element("element");
 element.setText("Element");

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "<element>Element</element>";
 String actualXml = "";

 try (StringWriter out = new StringWriter()) {
 xmlOutputter.output(element, out);
 actualXml = out.toString();
 }

 assertEquals(expectedXml, actualXml);
 }

 @Test
 public void testXMLOutputterWithEntityRef() throws IOException {
 // Test case for outputting an EntityRef
 EntityRef entityRef = new EntityRef("entity");

 XMLOutputter xmlOutputter = new XMLOutputter();
 String expectedXml = "&entity;";
 String actualXml = "";

 try (StringWriter out = new StringWriter()) {
 xmlOutputter.output(entityRef, out);
 actualXml = out.toString();
 }

 assertEquals(expectedXml, actualXml);
 }

 @Test(expected = IOException.class)
 public void testXMLOutputterWithNullDocument() throws IOException {
 // Test case for exception when outputting a null Document
 XMLOutputter xmlOutputter = new XMLOutputter();

 try {
 xmlOutputter.output((Document)null, new StringWriter());
 fail("Expected IOException for null Document");
 } catch (IOException e) {
 // Expected
 }
 }

 @Test(expected = IOException.class)
 public void testXMLOutputterWithNullWriter() throws IOException {
 // Test case for exception when outputting to a null Writer
 XMLOutputter xmlOutputter = new XMLOutputter();
 Document document = new Document();

 try {
 xmlOutputter.output(document, (Writer)null);
 fail("Expected IOException for null Writer");
 } catch (IOException e) {
 // Expected
 }
 }
}