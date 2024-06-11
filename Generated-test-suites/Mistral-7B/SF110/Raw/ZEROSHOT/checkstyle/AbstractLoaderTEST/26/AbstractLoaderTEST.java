package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {

 private AbstractLoader loader;

 @Before
 public void setUp() throws SAXException, ParserConfigurationException {
 loader = new AbstractLoader(Maps.newHashMap());
 }

 @Test
 public void testParseInputSource() throws SAXException, IOException {
 final InputSource inputSource = new InputSource(new StringReader("test XML data"));
 loader.parseInputSource(inputSource);
 // Add assertions here if needed
 }

 @Test(expected = IOException.class)
 public void testParseInputSourceWithIOException() throws SAXException, IOException {
 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 loader.parseInputSource(inputSource);
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithPublicIdAndValidResource() throws SAXException, IOException {
 loader.publicIdToResourceNameMap.put("DTD1", "dtd1.dtd");
 final InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<root>\n</root>"));
 final InputSource resolvedEntity = loader.parseInputSource(inputSource);
 final String entityContent = readEntityContent(resolvedEntity);
 assertEquals("expected DTD content", "<!DOCTYPE root [ \n<!ENTITY % entity1 SYSTEM \"dtd1.dtd\"> \n]\n>", entityContent);
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithPublicIdAndInvalidResource() throws SAXException, IOException {
 loader.publicIdToResourceNameMap.put("DTD1", "non_existent_resource.dtd");
 final InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<root>\n</root>"));
 loader.parseInputSource(inputSource);
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithSystemIdAndValidResource() throws SAXException, IOException {
 final InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<root>\n<!DOCTYPE root SYSTEM \"test.dtd\">\n</root>"));
 final InputSource resolvedEntity = loader.parseInputSource(inputSource);
 final String entityContent = readEntityContent(resolvedEntity);
 assertEquals("expected DTD content", "<!DOCTYPE root [ \n<!ENTITY % entity1 SYSTEM \"test.dtd\">\n]<!ENTITY % entity2 SYSTEM \"test2.dtd\">\n]", entityContent);
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithSystemIdAndInvalidResource() throws SAXException, IOException {
 final InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<root>\n<!DOCTYPE root SYSTEM \"non_existent_resource.dtd\">\n</root>"));
 loader.parseInputSource(inputSource);
 }

 @Test
 public void testError() throws SAXException, IOException {
 loader.publicIdToResourceNameMap.put("DTD1", "dtd1.dtd");
 final InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<root>\n<!DOCTYPE root SYSTEM \"dtd1.dtd\" [ \n<!ENTITY % entity1 SYSTEM \"non_existent_resource.dtd\">\n]>"));
 try {
 loader.parseInputSource(inputSource);
 fail("An error should have been thrown");
 } catch (SAXException e) {
 // Assertions for error message here
 }
 }

 @Test
 public void testFatalError() throws SAXException, IOException {
 loader.publicIdToResourceNameMap.put("DTD1", "dtd1.dtd");
 final InputSource inputSource = new InputSource(new StringReader("<?xml version=\"1.0\"?>\n<root>\n<!DOCTYPE root [ \n<!ENTITY % entity1 SYSTEM \"dtd1.dtd\" [ \n<!ENTITY % entity2 SYSTEM \"\">\n]>"));
 try {
 loader.parseInputSource(inputSource);
 fail("A fatal error should have been thrown");
 } catch (SAXException e) {
 // Assertions for error message here
 }
 }

 private String readEntityContent(final InputSource inputSource) throws IOException, SAXException {
 final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 final SAXParserFactory factory = SAXParserFactory.newInstance();
 factory.setNamespaceAware(true);
 final SAXParser saxParser = factory.newSAXParser();
 saxParser.parse(inputSource, new StreamReader(outputStream));
 return outputStream.toString();
}

}