package org.jdom2.output;

import org.jdom2.*;
import org.jdom2.output.SAXOutputter;
import org.jdom2.output.support.SAXTarget;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SAXOutputterTest {

 private SAXOutputter outputter;
 private SAXTarget targetMock;

 @Before
 public void setUp() {
 targetMock = mock(SAXTarget.class);
 outputter = new SAXOutputter(targetMock);
 }

 @Test
 public void testOutputDocument() throws JDOMException {
 Document doc = new Document();
 Element root = new Element("root");
 doc.setRootElement(root);
 outputter.output(doc);
 verify(targetMock).startDocument();
 verify(targetMock).startElement("root");
 // Add elements and attributes to the root element and verify that they are processed correctly
 root.addContent(new Text("Hello World"));
 root.addAttribute("id", "1");
 root.addAttribute("class", "example");
 assertEquals(3, root.getChildren().size());
 assertEquals(3, root.getAttributes().size());
 verify(targetMock).endElement("root");
 verify(targetMock).endDocument();
 }

 @Test
 public void testOutputListOfContents() throws JDOMException {
 List<Element> elements = new ArrayList<>();
 elements.add(new Element("element1"));
 elements.add(new Element("element2"));
 outputter.output(elements);
 verify(targetMock, times(2)).startElement("element");
 verify(targetMock, times(2)).endElement("element");
 }

 @Test
 public void testOutputElement() throws JDOMException {
 Element element = new Element("element");
 outputter.output(element);
 verify(targetMock).startElement("element");
 assertEquals(0, element.getChildren().size());
 verify(targetMock).endElement("element");
 }

 @Test(expected = JDOMException.class)
 public void testOutputNullDocument() throws JDOMException {
 outputter.output(null);
 }

 @Test(expected = JDOMException.class)
 public void testOutputNullContent() throws JDOMException {
 outputter.output(null);
 }

 @Test
 public void testOutputFragmentNullContent() throws JDOMException {
 outputter.outputFragment(null);
 }

 @Test
 public void testOutputFragmentCDATA() throws JDOMException {
 CDATA cdata = new CDATA("<![CDATA[Hello World]]>");
 outputter.outputFragment(cdata);
 verify(targetMock).characters(cdata.getText().toCharArray(), 0, cdata.getText().length());
 }

 @Test
 public void testOutputFragmentComment() throws JDOMException {
 Comment comment = new Comment("This is a comment");
 outputter.outputFragment(comment);
 verify(targetMock).comment(comment.getText().toCharArray(), 0, comment.getText().length());
 }

 @Test
 public void testOutputFragmentText() throws JDOMException {
 Text text = new Text("Hello World");
 outputter.outputFragment(text);
 verify(targetMock).characters(text.getText().toCharArray(), 0, text.getText().length());
 }
}