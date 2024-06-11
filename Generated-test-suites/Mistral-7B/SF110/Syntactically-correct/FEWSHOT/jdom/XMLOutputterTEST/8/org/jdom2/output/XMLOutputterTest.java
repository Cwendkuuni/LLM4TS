package org.jdom2.output;

import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.support.*;
import org.junit.Test;
import java.io.*;

public class XMLOutputterTest {

 XMLOutputter xmlo = new XMLOutputter();
 Document doc = new JDOMFactory().newDocument("TestXML");
 Element root = doc.getRootElement();

 @Test
 public void testOutputDocument() throws IOException {
 String expected = "<TestXML>\n</TestXML>";
 String result = xmlo.outputString(doc);
 assertEquals(expected, result);
 }

 @Test
 public void testOutputDocType() throws IOException {
 DocType docType = doc.getDocumentType();
 String expected = "<!DOCTYPE TestXML []\n";
 String result = xmlo.outputString(docType);
 assertEquals(expected, result);
 }

 @Test
 public void testOutputElement() throws IOException {
 Element element = new JDOMFactory().newElement("TestElement");
 root.addContent(element);
 String expected = "<TestElement/>\n";
 String result = xmlo.outputString(element);
 assertEquals(expected, result);
 }

 @Test
 public void testOutputElementContent() throws IOException {
 Text text = new JDOMFactory().newText("TestText");
 element.addContent(text);
 String expected = "<TestElement><TestText/></TestElement>\n";
 String result = xmlo.outputStringElementContent(element);
 assertEquals(expected, result);
 }

 @Test
 public void testOutputList() throws IOException {
 List<Element> list = new ArrayList<>();
 list.add(new JDOMFactory().newElement("TestElement1"));
 list.add(new JDOMFactory().newElement("TestElement2"));
 root.addContent(new JDOMFactory().newElement("TestList").addContent(list));
 String expected = "<TestList>\n<TestElement1/>\n<TestElement2/>\n</TestList>\n";
 String result = xmlo.outputString(list);
 assertEquals(expected, result);
 }

 @Test
 public void testOutputCDATA() throws IOException {
 CDATA cdata = new JDOMFactory().newCDATA("TestCDATA");
 element.addContent(cdata);
 String expected = "<TestElement><![CDATA[TestCDATA]]></TestElement>\n";
 String result = xmlo.outputString(cdata);
 assertEquals(expected, result);
 }

 @Test
 public void testOutputText() throws IOException {
 Text text = new JDOMFactory().newText("TestText");
 element.addContent(text);
 String expected = "<TestElement><TestText>TestText</TestText></TestElement>\n";
 String result = xmlo.outputString(text);
 assertEquals(expected, result);
 }

 @Test
 public void testOutputComment() throws IOException {
 Comment comment = new JDOMFactory().newComment("TestComment");
 root.addContent(comment);
 String expected = "<!--TestComment-->\n";
 String result = xmlo.outputString(comment);
 assertEquals(expected, result);
 }

 @Test
 public void testOutputProcessingInstruction() throws IOException {
 ProcessingInstruction pi = new JDOMFactory().newProcessingInstruction("TestPI", "TestData");
 root.addContent(pi);
 String expected = "<?TestPI TestData?>\n";
 String result = xmlo.outputString(pi);
 assertEquals(expected, result);
 }

 @Test
 public void testOutputEntityRef() throws IOException {
 EntityRef entityRef = new EntityRef("entityName");
 root.addContent(entityRef);
 String expected = "<TestXML entityName=\"entityName\"/>\n";
 xmlo.setFormat(new Format("entityName", null, null));
 String result = xmlo.outputString(doc);
 assertEquals(expected, result);
 }

 @Test
 public void testEscapeAttributeEntities() {
 String str = "Test<Test&Test/>";
 String expected = "Test&lt;Test&amp;Test&gt;&amp;amp;Test&lt;/Test&amp;gt;";
 String result = xmlo.escapeAttributeEntities(str);
 assertEquals(expected, result);
 }

 @Test
 public void testEscapeElementEntities() {
 String str = "<Test>&Test&</Test>";
 String expected = "&lt;Test&gt;Test&lt;/Test&gt;";
 String result = xmlo.escapeElementEntities(str);
 assertEquals(expected, result);
 }
}