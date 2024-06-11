package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {

 private static final String VALID_DTD_XML = "<?xml version=\"1.0\"?>\n" +
         "<!DOCTYPE root [ \n" +
         "<!ENTITY ent SYSTEM \"entity.xml\">\n" +
         "]>\n" +
         "<root>\n" +
         "<element>Value</element>\n" +
         "</root>";

 private static final String ENTITY_XML = "<entity>\n" +
         "<name>EntityName</name>\n" +
         "</entity>";

 private AbstractLoader loader;

 @Test
 public void testAbstractLoaderWithValidDtdAndEntity() throws Exception {
     Map<String, String> publicIdToResourceNameMap = new HashMap<>();
     publicIdToResourceNameMap.put("publicId", "dtd.xml");
     loader = new AbstractLoader(publicIdToResourceNameMap);
     StringReader dtdReader = new StringReader(VALID_DTD_XML);
     InputSource dtdInputSource = new InputSource(dtdReader);
     loader.parseInputSource(dtdInputSource);
     InputSource entityInputSource = loader.resolveEntity("ent", null);
     StringReader entityReader = new StringReader(ENTITY_XML);
     assertEquals(entityReader.toString(), new String(entityInputSource.getByteStream()));
 }

 @Test(expected = SAXParseException.class)
 public void testAbstractLoaderWithInvalidPublicId() throws Exception {
     Map<String, String> publicIdToResourceNameMap = new HashMap<>();
     publicIdToResourceNameMap.put("invalidPublicId", "dtd.xml");
     loader = new AbstractLoader(publicIdToResourceNameMap);
     InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
     loader.parseInputSource(inputSource);
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithInvalidDtdResource() throws Exception {
     Map<String, String> publicIdToResourceNameMap = new HashMap<>();
     publicIdToResourceNameMap.put("publicId", "nonexistent.dtd");
     loader = new AbstractLoader(publicIdToResourceNameMap);
     InputSource inputSource = new InputSource(new ByteArrayInputStream(VALID_DTD_XML.getBytes()));
     loader.parseInputSource(inputSource);
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithNullPublicId() throws Exception {
     Map<String, String> publicIdToResourceNameMap = new HashMap<>();
     loader = new AbstractLoader(publicIdToResourceNameMap);
     InputSource inputSource = new InputSource(new ByteArrayInputStream(VALID_DTD_XML.getBytes()));
     loader.parseInputSource(inputSource);
     InputSource entityInputSource = loader.resolveEntity(null, null);
 }

 @Test(expected = IOException.class)
 public void testAbstractLoaderWithNullSystemId() throws Exception {
     Map<String, String> publicIdToResourceNameMap = new HashMap<>();
     publicIdToResourceNameMap.put("publicId", "dtd.xml");
     loader = new AbstractLoader(publicIdToResourceNameMap);
     InputSource inputSource = new InputSource(new ByteArrayInputStream(VALID_DTD_XML.getBytes()));
     loader.parseInputSource(inputSource);
     loader.resolveEntity("ent", (String) null);
 }

 @Test
 public void testAbstractLoaderWithEmptyPublicIdToResourceNameMap() throws Exception {
     loader = new AbstractLoader(Maps.newHashMap());
     InputSource inputSource = new InputSource(new ByteArrayInputStream(VALID_DTD_XML.getBytes()));
     loader.parseInputSource(inputSource);
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithEmptyPublicIdToResourceNameMapAndInvalidPublicId() throws Exception {
     loader = new AbstractLoader(Maps.newHashMap());
     InputSource inputSource = new InputSource(new ByteArrayInputStream(VALID_DTD_XML.getBytes()));
     loader.parseInputSource(inputSource);
     loader.resolveEntity("invalidPublicId", null);
 }

}