package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import org.xml.sax.helpers.DefaultHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReader;

public class AbstractLoaderTest {

 private static final String DTD_RESOURCE_NAME = "test.dtd";

 @Test
 public void testAbstractLoaderWithValidPublicId() throws Exception {
 AbstractLoader loader = new AbstractLoader(new HashMap<>()) {
 @Override
 public void warning(SAXParseException exception) throws SAXException {
 // Do nothing
 }
 };
 InputSource inputSource = new InputSource(new ByteArrayInputStream("test xml".getBytes()));
 loader.parseInputSource(inputSource);
 // Verify that the entity with the given public id was resolved correctly
 assertEquals(DTD_RESOURCE_NAME, loader.publicIdToResourceNameMap.get("testPublicId"));
 }

 @Test
 public void testAbstractLoaderWithInvalidPublicId() throws Exception {
 AbstractLoader loader = new AbstractLoader(new HashMap<>()) {
 @Override
 public void warning(SAXParseException exception) throws SAXException {
 // Do nothing
 }
 };
 InputSource inputSource = new InputSource(new ByteArrayInputStream("test xml".getBytes()));
 loader.parseInputSource(inputSource);
 // Verify that the entity with an invalid public id was not resolved
 assertNull(loader.publicIdToResourceNameMap.get("invalidPublicId"));
 }

 @Test
 public void testAbstractLoaderWithNullPublicId() throws Exception {
 AbstractLoader loader = new AbstractLoader(new HashMap<>()) {
 @Override
 public void warning(SAXParseException exception) throws SAXException {
 // Do nothing
 }
 };
 InputSource inputSource = new InputSource(new ByteArrayInputStream("test xml".getBytes()));
 loader.parseInputSource(inputSource);
 // Verify that the entity with a null public id was not resolved
 assertNull(loader.publicIdToResourceNameMap.get(null));
 }

 @Test
 public void testAbstractLoaderWithValidSystemId() throws Exception {
 AbstractLoader loader = new AbstractLoader(Collections.singletonMap("testPublicId", DTD_RESOURCE_NAME)) {
 @Override
 public void warning(SAXParseException exception) throws SAXException {
 // Do nothing
 }
 };
 InputSource inputSource = new InputSource(new File("test.xml").toURI().toURL().openStream());
 loader.parseInputSource(inputSource);
 // Verify that the entity with the given system id was not resolved, as it was not overridden
 assertNull(loader.publicIdToResourceNameMap.get("testSystemId"));
 }

 @Test
 public void testAbstractLoaderWithCustomEntityResolver() throws Exception {
 InputStream testDtdStream = new ByteArrayInputStream("test dtd".getBytes());
 XMLReader parser = new SAXParser().getXMLReader();
 parser.setEntityResolver(new CustomEntityResolver() {
 @Override
 public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
 if (publicId.equals("testPublicId")) {
 return new InputSource(testDtdStream);
 }
 return null;
 }
 });
 parser.parse(new InputSource(new ByteArrayInputStream("test xml".getBytes())));
 AbstractLoader loader = (AbstractLoader) parser.getContentHandler();
 // Verify that the custom entity resolver was used and the entity with the given public id was resolved correctly
 assertEquals(DTD_RESOURCE_NAME, loader.publicIdToResourceNameMap.get("testPublicId"));
 }

 private static class CustomHandler extends DefaultHandler {
 // Do nothing
 }

 private static class CustomEntityResolver extends DefaultHandler {
 // Do nothing
 }
}