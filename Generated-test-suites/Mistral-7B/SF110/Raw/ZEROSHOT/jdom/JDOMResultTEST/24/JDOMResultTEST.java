package org.jdom2.transform;

import org.junit.Test;
import org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.input.sax.*;
import org.xml.sax.*;
import static org.mockito.Mockito.*;

import java.util.*;

public class JDOMResultTest {

 private JDOMResult jdomResult;
 private List<Content> resultList;
 private Document resultDoc;
 private JDOMFactory factory;

 @Before
 public void setUp() {
 jdomResult = new JDOMResult();
 resultList = new ArrayList<>();
 resultDoc = mock(Document.class);
 factory = mock(JDOMFactory.class);
 }

 @Test
 public void testSetResult() {
 jdomResult.setResult(resultList);
 assertEquals(resultList, jdomResult.getResult());
 }

 @Test
 public void testGetResult_whenResultListIsNotNull_returnResultList() {
 resultList.add(mock(Content.class));
 jdomResult.setResult(resultList);
 List<Content> result = jdomResult.getResult();
 assertEquals(resultList.size(), result.size());
 for (int i = 0; i < result.size(); i++) {
 assertEquals(resultList.get(i), result.get(i));
 }
 }

 @Test
 public void testGetResult_whenResultDocIsNotNull_returnDocumentContent() {
 Element root = new Element("root", null, null);
 resultDoc.setContent(root.getChildren());
 jdomResult.setDocument(resultDoc);
 List<Content> result = jdomResult.getResult();
 assertEquals(root.getChildren().size(), result.size());
 for (int i = 0; i < result.size(); i++) {
 assertEquals(root.getChildren().get(i), result.get(i));
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testGetResult_whenResultListAndDocAreBothNull_throwException() {
 jdomResult.retrieveResult();
 jdomResult.getResult();
 }

 @Test
 public void testSetDocument() {
 jdomResult.setDocument(resultDoc);
 assertEquals(resultDoc, jdomResult.getDocument());
 }

 @Test
 public void testGetDocument_whenResultListIsNotNull_returnEmptyList() {
 resultList.add(mock(Content.class));
 jdomResult.setResult(resultList);
 Document doc = jdomResult.getDocument();
 assertNull(doc);
 }

 @Test
 public void testGetDocument_whenResultDocIsNotNull_returnDocument() {
 Element root = new Element("root", null, null);
 resultDoc.setContent(root.getChildren());
 jdomResult.setDocument(resultDoc);
 Document doc = jdomResult.getDocument();
 assertEquals(root, doc);
 }

 @Test
 public void testSetFactory() {
 jdomResult.setFactory(factory);
 assertEquals(factory, jdomResult.getFactory());
 }

 @Test
 public void testGetFactory_whenFactoryIsNull_returnDefaultFactory() {
 JDOMFactory defaultFactory = mock(JDOMFactory.class);
 when(defaultFactory.document(null)).thenReturn(resultDoc);
 jdomResult.setFactory(null);
 Document doc = jdomResult.getDocument();
 assertEquals(resultDoc, doc);
 verify(defaultFactory).document(null);
 }

 @Test
 public void testGetFactory_whenFactoryIsNotNull_returnFactory() {
 jdomResult.setFactory(factory);
 assertEquals(factory, jdomResult.getFactory());
 }

 @Test
 public void testRetrieveResult() {
 jdomResult.setHandler(mock(ContentHandler.class));
 DocumentBuilder builder = new DocumentBuilder();
 builder.startDocument();
 jdomResult.setResult(builder.getResult());
 jdomResult.retrieveResult();
 verify(jdomResult.getHandler(), times(1)).startDocument();
 }
}