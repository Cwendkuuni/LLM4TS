package org.jdom2.transform;

import org.jdom2.JDOMException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Content;
import org.jdom2.input.SAXBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMResultTest {

 private final JDOMFactory factory = new JDOMFactory();
 private final JDOMResult result = new JDOMResult();

 @Test
 public void testEmptyResult() throws JDOMException, SAXException {
 JAXPSaxResult jaxpResult = new JAXPSaxResult(new DefaultHandler());
 JDOMResult jdomResult = new JDOMResult();
 SAXBuilder builder = new SAXBuilder();
 builder.setEntityResolver(jaxpResult);
 builder.setErrorHandler(jaxpResult);
 builder.setHandler(jdomResult);
 builder.build(getClass().getResource("/empty.xml"));

 List<Content> expectedResult = Collections.emptyList();
 List<Content> actualResult = result.getResult();

 Assert.assertEquals("Expected empty result", expectedResult, actualResult);
 }

 @Test
 public void testSingleElementResult() throws JDOMException, SAXException {
 JAXPSaxResult jaxpResult = new JAXPSaxResult(new DefaultHandler());
 JDOMResult jdomResult = new JDOMResult();
 SAXBuilder builder = new SAXBuilder();
 builder.setEntityResolver(jaxpResult);
 builder.setErrorHandler(jaxpResult);
 builder.setHandler(jdomResult);
 builder.build(getClass().getResource("/single-element.xml"));

 Document expectedDocument = new Document(new Element("root", null, null));
 Document actualDocument = result.getDocument();

 Assert.assertNotNull("Expected document", actualDocument);
 Assert.assertEquals("Expected single element document", expectedDocument, actualDocument);
 }

 @Test
 public void testMultipleElementsResult() throws JDOMException, SAXException {
 JAXPSaxResult jaxpResult = new JAXPSaxResult(new DefaultHandler());
 JDOMResult jdomResult = new JDOMResult();
 SAXBuilder builder = new SAXBuilder();
 builder.setEntityResolver(jaxpResult);
 builder.setErrorHandler(jaxpResult);
 builder.setHandler(jdomResult);
 builder.build(getClass().getResource("/multiple-elements.xml"));

 List<Content> expectedResult = new ArrayList<>();
 expectedResult.add(new Element("element1", null, null));
 expectedResult.add(new Element("element2", null, null));
 expectedResult.add(new Element("element3", null, null));
 List<Content> actualResult = result.getResult();

 Assert.assertEquals("Expected multiple elements result", expectedResult, actualResult);
 }

 @Test
 public void testDocumentResult() throws JDOMException, SAXException {
 JAXPSaxResult jaxpResult = new JAXPSaxResult(new DefaultHandler());
 JDOMResult jdomResult = new JDOMResult();
 SAXBuilder builder = new SAXBuilder();
 builder.setEntityResolver(jaxpResult);
 builder.setErrorHandler(jaxpResult);
 builder.setHandler(jdomResult);
 builder.build(getClass().getResource("/document.xml"));

 Document expectedDocument = new Document(new Element("root", null, null));
 expectedDocument.setContent(new Element("element1", null, null), new Element("element2", null, null));
 Document actualDocument = result.getDocument();

 Assert.assertEquals("Expected document result", expectedDocument, actualDocument);
 }

 @Test
 public void testFactoryResult() throws JDOMException, SAXException {
 JAXPSaxResult jaxpResult = new JAXPSaxResult(new DefaultHandler());
 JDOMResult jdomResult = new JDOMResult();
 JDOMFactory factory = new DefaultJDOMFactory();
 SAXBuilder builder = new SAXBuilder();
 builder.setEntityResolver(jaxpResult);
 builder.setErrorHandler(jaxpResult);
 builder.setHandler(jdomResult);
 result.setFactory(factory);
 builder.build(getClass().getResource("/factory.xml"));

 Document expectedDocument = new Document(new Element("root", null, null));
 expectedDocument.setContent(new Element("element1", null, null), new Element("element2", null, null));
 Document actualDocument = result.getDocument();

 Assert.assertEquals("Expected document result using factory", expectedDocument, actualDocument);
 }
}