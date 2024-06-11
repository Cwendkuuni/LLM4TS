package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

public class AbstractLoaderTest {

 private AbstractLoader abstractLoader;
 private Map<String, String> publicIdToResourceNameMap;
 private SAXParserFactory saxParserFactory;
 private XMLReader parser;

 @Before
 public void setup() throws ParserConfigurationException, SAXException {
 publicIdToResourceNameMap = Maps.newHashMap();
 saxParserFactory = SAXParserFactory.newInstance();
 parser = saxParserFactory.newSAXParser().getXMLReader();
 abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
 }

 @Test
 public void testConstructorWithPublicIdAndDtdResourceName() throws ParserConfigurationException, SAXException {
 String publicId = "publicId";
 String dtdResourceName = "dtdResourceName";
 abstractLoader = new AbstractLoader(publicId, dtdResourceName);

 assertNotNull(abstractLoader.getPublicIdToResourceNameMap());
 assertEquals(publicId, abstractLoader.getPublicIdToResourceNameMap().get(publicId));
 }

 @Test
 public void testConstructorWithPublicIdToResourceNameMap() throws ParserConfigurationException, SAXException {
 Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
 publicIdToResourceNameMap.put("publicId", "dtdResourceName");
 abstractLoader = new AbstractLoader(publicIdToResourceNameMap);

 assertNotNull(abstractLoader.getPublicIdToResourceNameMap());
 assertEquals(publicIdToResourceNameMap, abstractLoader.getPublicIdToResourceNameMap());
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
 InputSource inputSource = mock(InputSource.class);
 when(inputSource.getSystemId()).thenReturn("systemId");
 abstractLoader.parseInputSource(inputSource);

 // Verify that parse method is called on XMLReader
 parser.parse(inputSource);
 }

 @Test
 public void testResolveEntity() throws IOException, SAXException {
 String publicId = "publicId";
 String dtdResourceName = "dtdResourceName";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);
 InputSource expectedInputSource = new InputSource(new FileInputStream("dtdResourceName"));
 when(parser.getEntityResolver()).thenReturn(abstractLoader);

 InputSource actualInputSource = abstractLoader.resolveEntity(publicId, null);

 assertEquals(expectedInputSource, actualInputSource);
 }

 @Test
 public void testResolveEntityWithNullPublicId() throws SAXException, IOException {
 when(parser.getEntityResolver()).thenReturn(abstractLoader);
 InputSource actualInputSource = abstractLoader.resolveEntity(null, null);

 assertNull(actualInputSource);
 }

 @Test(expected = SAXException.class)
 public void testError() throws SAXException {
 SAXParseException exception = mock(SAXParseException.class);
 abstractLoader.error(exception);
 }

 @Test(expected = SAXException.class)
 public void testFatalError() throws SAXException {
 SAXParseException exception = mock(SAXParseException.class);
 abstractLoader.fatalError(exception);
 }
}