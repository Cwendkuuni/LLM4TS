package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class AbstractLoaderTest extends DefaultHandler {

 private static final String VALID_DTD_PUBLIC_ID = "testPublicId";
 private static final String VALID_DTD_RESOURCE_NAME = "testValidDtd.dtd";
 private static final String INVALID_DTD_PUBLIC_ID = "invalidPublicId";
 private static final String NON_EXISTENT_DTD_RESOURCE_NAME = "nonExistentDtd.dtd";

 private Map<String, String> publicIdToResourceNameMap;
 private XMLReader parser;

 @Test
 public void testAbstractLoaderWithValidDto() throws ParserConfigurationException, SAXException, IOException {
 final AbstractLoader loader = new AbstractLoader(VALID_DTD_PUBLIC_ID, VALID_DTD_RESOURCE_NAME);
 final InputStream dtdInputStream = new ByteArrayInputStream(new byte[0]);
 final InputSource dtdInputSource = new InputSource(dtdInputStream);
 loader.parseInputSource(dtdInputSource);
 // Add assertions here if needed
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithMissingDto() throws ParserConfigurationException, SAXException, IOException {
 final AbstractLoader loader = new AbstractLoader( Vallid_DTD_PUBLIC_ID, NON_EXISTENT_DTD_RESOURCE_NAME);
 final InputStream dtdInputStream = new ByteArrayInputStream(new byte[0]);
 final InputSource dtdInputSource = new InputSource(dtdInputStream);
 loader.parseInputSource(dtdInputSource);
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithWrongPublicIdDto() throws ParserConfigurationException, SAXException, IOException {
 final AbstractLoader loader = new AbstractLoader(new HashMap<>());
 loader.parseInputSource(new InputSource(new ByteArrayInputStream(new byte[0])));
 loader.setPublicId(INVALID_DTD_PUBLIC_ID);
 final InputStream dtdInputStream = new ByteArrayInputStream(new byte[0]);
 final InputSource dtdInputSource = new InputSource(dtdInputStream);
 loader.parseInputSource(dtdInputSource);
 }

 @Test
 public void testAbstractLoaderWithProvidedDto() throws ParserConfigurationException, SAXException, IOException {
 final Map<String, String> publicIdToResourceNameMap = new HashMap<>();
 publicIdToResourceNameMap.put(VALID_DTD_PUBLIC_ID, VALID_DTD_RESOURCE_NAME);
 final AbstractLoader loader = new AbstractLoader(publicIdToResourceNameMap);
 final InputStream dtdInputStream = new ByteArrayInputStream(new byte[0]);
 final InputSource dtdInputSource = new InputSource(dtdInputStream);
 loader.parseInputSource(dtdInputSource);
 }

 @Test
 public void testAbstractLoaderWithEmptyMap() throws ParserConfigurationException, SAXException, IOException {
 final AbstractLoader loader = new AbstractLoader(Collections.emptyMap());
 final InputStream dtdInputStream = new ByteArrayInputStream(new byte[0]);
 final InputSource dtdInputSource = new InputSource(dtdInputStream);
 loader.parseInputSource(dtdInputSource);
 }

 @Test
 public void testAbstractLoaderWithNullMap() throws ParserConfigurationException, SAXException, IOException {
 try {
 final AbstractLoader loader = new AbstractLoader(null);
 final InputStream dtdInputStream = new ByteArrayInputStream(new byte[0]);
 final InputSource dtdInputSource = new InputSource(dtdInputStream);
 loader.parseInputSource(dtdInputSource);
 fail("Expected SAXException or ParserConfigurationException");
 } catch (SAXException | ParserConfigurationException e) {
 // Expected
 }
 }

 @Test
 public void testAbstractLoaderResolvesEntity() throws ParserConfigurationException, SAXException, IOException {
 final AbstractLoader loader = new AbstractLoader(Collections.singletonMap(VALID_DTD_PUBLIC_ID, VALID_DTD_RESOURCE_NAME));
 final InputStream dtdInputStream = new ByteArrayInputStream(new byte[0]);
 final InputSource dtdInputSource = new InputSource(dtdInputStream);
 loader.parseInputSource(dtdInputSource);
 final InputSource resolvedInputSource = loader.resolveEntity(VALID_DTD_PUBLIC_ID, null);
 assertTrue(resolvedInputSource != null);
 }

 @Test
 public void testAbstractLoaderDoesNotResolveEntityForUnknownPublicId() throws ParserConfigurationException, SAXException, IOException {
 final AbstractLoader loader = new AbstractLoader(Collections.singletonMap(VALID_DTD_PUBLIC_ID, VALID_DTD_RESOURCE_NAME));
 final InputStream dtdInputStream = new ByteArrayInputStream(new byte[0]);
 final InputSource dtdInputSource = new InputSource(dtdInputStream);
 loader.parseInputSource(dtdInputSource);
 final InputSource resolvedInputSource = loader.resolveEntity(INVALID_DTD_PUBLIC_ID, null);
 assertNull(resolvedInputSource);
 }
}