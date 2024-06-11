package org.jdom2.transform;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.JDOMFactory;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Content;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JDOMResultTest {

 private JDOMFactory factory;
 private JDOMResult result;

 @Before
 public void setUp() {
 factory = new JDOMFactory();
 result = new JDOMResult();
 }

 @Test
 public void testEmptyResult() {
 result.setDocument(null);
 List<Content> expected = Collections.emptyList();
 List<Content> actual = result.getResult();
 assertEquals(expected, actual);
 }

 @Test
 public void testSettingResult() {
 List<Content> inputList = Arrays.asList(new Element("a"), new Element("b"));
 result.setResult(inputList);
 List<Content> expected = inputList;
 List<Content> actual = result.getResult();
 assertEquals(expected, actual);
 }

 @Test
 public void testGettingDocumentFromResultList() throws JDOMException {
 List<Content> inputList = Arrays.asList(new Element("root"), new Element("a"), new Element("b"));
 result.setResult(inputList);
 Document expected = factory.document(new StringReader("<root><a/><b/></root>"));
 Document actual = result.getDocument();
 assertEquals(expected.getRootElement(), actual.getRootElement());
 }

 @Test
 public void testGettingDocumentFromResultDoc() throws JDOMException {
 Document inputDoc = factory.document(new StringReader("<root><a><b/></a></root>"));
 result.setDocument(inputDoc);
 List<Content> expected = Arrays.asList(inputDoc.getRootElement().getContent().get(0), inputDoc.getRootElement().getContent().get(0).getContent().get(0));
 List<Content> actual = result.getResult();
 assertEquals(expected, actual);
 }

 @Test
 public void testGettingFactory() {
 JDOMFactory expected = new JDOMFactory();
 JDOMFactory actual = result.getFactory();
 assertEquals(expected, actual);
 }

 @Test(expected = RuntimeException.class)
 public void testGettingDocumentWhenNeitherResultListNorResultDocIsSet() {
 result.retrieveResult();
 result.getDocument();
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullResultList() {
 result.setResult(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSettingNullDocument() {
 result.setDocument(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSettingResultWithNullContent() {
 List<Content> inputList = Arrays.asList(new Element("a"), null);
 result.setResult(inputList);
 }
}