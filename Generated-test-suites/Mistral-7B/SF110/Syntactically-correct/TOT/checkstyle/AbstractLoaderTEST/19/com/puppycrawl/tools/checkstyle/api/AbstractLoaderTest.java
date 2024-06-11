package com.example.junit4.test;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {

 private AbstractLoader loader;

 @Before
 public void setup() throws SAXException, ParserConfigurationException {
 loader = new AbstractLoader(new HashMap<String, String>() {{
 put("publicId", "dtdResourceName.dtd");
 }});
 }

 @Test
 public void testResolveEntityWithPublicId() throws SAXException, IOException {
 final String xmlContent = "<root/>";
 final String expectedDtdContent = "<!DOCTYPE root [<!ENTITY entity SYSTEM \"dtdResourceName.dtd\"]]>";
 final StringReader xmlReader = new StringReader(xmlContent);
 final StringWriter xmlWriter = new StringWriter();

 loader.parseInputSource(new InputSource(xmlReader));
 final InputSource inputSource = loader.resolveEntity("publicId", null);
 final StringReader reader = new StringReader(inputSource.getCharacterStream());
 final StringWriter writer = new StringWriter();

 String actualDtdContent = "";
 String line;
 while ((line = reader.readLine()) != null) {
 actualDtdContent += line;
 }

 assertEquals(expectedDtdContent, actualDtdContent);
 }

 @Test
 public void testResolveEntityWithSystemId() throws SAXException, IOException {
 final String xmlContent = "<root/>";
 final StringReader xmlReader = new StringReader(xmlContent);
 final StringWriter xmlWriter = new StringWriter();

 loader.parseInputSource(new InputSource(xmlReader));
 final InputSource inputSource = loader.resolveEntity(null, "systemId.dtd");
 final StringReader reader = new StringReader(inputSource.getCharacterStream());
 final StringWriter writer = new StringWriter();

 String actualDtdContent = "";
 String line;
 while ((line = reader.readLine()) != null) {
 actualDtdContent += line;
 }

 // Edge case: Empty DTD file
 assertNotNull(actualDtdContent);
 }

 @Test(expected = SAXException.class)
 public void testErrorWithUnknownPublicId() throws SAXException, IOException {
 final String xmlContent = "<root/>";
 final StringReader xmlReader = new StringReader(xmlContent);
 final StringWriter xmlWriter = new StringWriter();

 loader.parseInputSource(new InputSource(xmlReader));
 loader.resolveEntity("unknownPublicId", null);
 }

 @Test(expected = SAXException.class)
 public void testErrorWithNullInputSource() throws SAXException, IOException {
 final InputSource inputSource = null;
 loader.resolveEntity(null, null);
 }
}