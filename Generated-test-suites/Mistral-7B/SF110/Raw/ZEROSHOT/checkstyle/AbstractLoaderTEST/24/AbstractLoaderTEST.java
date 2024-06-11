package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest extends TestCase {

 private AbstractLoader loader;

 @Before
 public void setUp() throws Exception {
 loader = new AbstractLoader(Maps.newHashMap());
 }

 @Test
 public void testAbstractLoaderConstructionWithPublicIdAndDtdResourceName() throws Exception {
 final String publicId = "testPublicId";
 final String dtdResourceName = "testDtdResourceName.dtd";
 loader = new AbstractLoader(publicId, dtdResourceName);
 assertNotNull(loader.getParser());
 }

 @Test
 public void testAbstractLoaderConstructionWithPublicIdToResourceNameMap() throws Exception {
 final String publicId1 = "testPublicId1";
 final String dtdResourceName1 = "testDtdResourceName1.dtd";
 final String publicId2 = "testPublicId2";
 final String dtdResourceName2 = "testDtdResourceName2.dtd";

 Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
 publicIdToResourceNameMap.put(publicId1, dtdResourceName1);
 publicIdToResourceNameMap.put(publicId2, dtdResourceName2);

 loader = new AbstractLoader(publicIdToResourceNameMap);
 assertNotNull(loader.getParser());
 }

 @Test
 public void testParseInputSource() throws Exception {
 final InputSource inputSource = new InputSource(new ByteArrayInputStream("test data".getBytes()));
 loader.parseInputSource(inputSource);
 // Add assertions here if needed
 }

 @Test
 public void testResolveEntity() throws Exception {
 final String publicId = "testPublicId";
 final String dtdResourceName = "testDtdResourceName.dtd";
 final ClassLoader classLoader = mock(ClassLoader.class);
 final InputStream dtdIs = mock(InputStream.class);
 when(classLoader.getResourceAsStream(dtdResourceName)).thenReturn(dtdIs);

 loader = new AbstractLoader(Maps.newHashMap());
 loader.getClass().getDeclaredField("parser").setAccessible(true);
 loader.getClass().getDeclaredField("classLoader").setAccessible(true);
 loader.getClass().getDeclaredField("parser").set(loader, mock(XMLReader.class));
 loader.getClass().getDeclaredField("classLoader").set(loader, classLoader);

 final InputSource inputSource = mock(InputSource.class);
 when(inputSource.getPublicId()).thenReturn(publicId);

 final InputSource result = loader.resolveEntity(publicId, null);
 assertNotNull(result);
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithUnkownPublicId() throws Exception {
 loader = new AbstractLoader(Maps.newHashMap());
 final InputSource inputSource = new InputSource(new ByteArrayInputStream("test data".getBytes()));
 loader.resolveEntity("unknownPublicId", null);
 }

 private SAXParserFactory getSaxParserFactory() throws ParserConfigurationException {
 final SAXParserFactory factory = SAXParserFactory.newInstance();
 factory.setValidating(true);
 factory.setNamespaceAware(true);
 return factory;
 }

 @Test(expected = ParserConfigurationException.class)
 public void testAbstractLoaderConstructionWithParserConfigurationException() throws Exception {
 final SAXParserFactory factory = mock(SAXParserFactory.class);
 when(factory.newSAXParser()).thenThrow(new ParserConfigurationException());
 loader = new AbstractLoader(getSaxParserFactory());
 }

 @Test(expected = SAXException.class)
 public void testError() throws Exception {
 loader = new AbstractLoader(Maps.newHashMap());
 loader.getClass().getDeclaredField("parser").setAccessible(true);
 final SAXParseException exception = new SAXParseException("Test error", null);
 loader.getClass().getDeclaredField("errorHandler").setAccessible(true);
 loader.getClass().getDeclaredField("parser").set(loader, mock(XMLReader.class));
 loader.getClass().getDeclaredField("errorHandler").set(loader, this);
 loader.error(exception);
 }

 @Test(expected = SAXException.class)
 public void testFatalError() throws Exception {
 loader = new AbstractLoader(Maps.newHashMap());
 loader.getClass().getDeclaredField("parser").setAccessible(true);
 final SAXParseException exception = new SAXParseException("Test error", null);
 loader.getClass().getDeclaredField("errorHandler").setAccessible(true);
 loader.getClass().getDeclaredField("parser").set(loader, mock(XMLReader.class));
 loader.getClass().getDeclaredField("errorHandler").set(loader, this);
 loader.fatalError(exception);
 }
}