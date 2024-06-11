package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

public class AbstractLoaderTest {

 private AbstractLoader loader;

 @Before
 public void setup() throws Exception {
 Map<String, String> publicIdToResourceNameMap = Maps.newHashMap();
 publicIdToResourceNameMap.put("publicId1", "dtdResourceName1.dtd");
 loader = new AbstractLoader(publicIdToResourceNameMap);
 }

 @Test
 public void testParseInputSource() throws Exception {
 String xml = "<xml/>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 loader.parseInputSource(inputSource);
 //Assertions for verification of parsed data can be added here
 }

 @Test
 public void testResolveEntityWithPublicIdInMap() throws Exception {
 String dtdResourceName = "dtdResourceName1.dtd";
 InputSource expectedInputSource = new InputSource(new FileInputStream(dtdResourceName));
 InputSource actualInputSource = loader.resolveEntity("publicId1", null);
 assertEquals(expectedInputSource.toString(), actualInputSource.toString());
 }

 @Test(expected = SAXParseException.class)
 public void testResolveEntityWithPublicIdNotInMap() throws Exception {
 loader.resolveEntity("publicId2", null);
 }

 @Test(expected = SAXException.class)
 public void testError() throws Exception {
 loader.parseInputSource(new InputSource(new StringReader("<xml><error/></xml>")));
 }

 @Test(expected = SAXException.class)
 public void testFatalError() throws Exception {
 loader.parseInputSource(new InputSource(new StringReader("<xml><fatalError/></xml>")));
 }

 @Test
 public void testParseValidInput() throws Exception {
 String xml = "<xml/>";
 InputSource inputSource = new InputSource(new StringReader(xml));
 StringWriter writer = new StringWriter();
 loader.parseInputSource(new InputSource(new StringReader(xml)), new PrintWriter(writer));
 //Assertions for verification of parsed data can be added here
 }
}