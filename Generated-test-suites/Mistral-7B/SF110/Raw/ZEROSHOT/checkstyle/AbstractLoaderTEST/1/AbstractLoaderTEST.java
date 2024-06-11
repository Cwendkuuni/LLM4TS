package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.AbstractLoader;

public class AbstractLoaderTest {

 private AbstractLoader loader;

 @Before
 public void setUp() throws Exception {
 loader = new AbstractLoader(new HashMap<String, String>() {{
 put("TestPublicId", "TestDtd.dtd");
 }});
 }

 @Test
 public void testParseInputSource() throws Exception {
 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 loader.parseInputSource(inputSource);
 // Verify that no exception was thrown
 }

 @Test(expected = IOException.class)
 public void testParseInputSourceWithIOException() throws Exception {
 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 inputSource.setByteStream(new ByteArrayInputStream(new byte[1]));
 loader.parseInputSource(inputSource);
 }

 @Test(expected = SAXException.class)
 public void testResolveEntityWithUnknownPublicId() throws Exception {
 loader.resolveEntity("UnknownPublicId", "");
 }

 @Test
 public void testResolveEntityWithKnownPublicId() throws Exception {
 final String dtdResourceName = "TestDtd.dtd";
 final ClassLoader loader = AbstractLoaderTest.class.getClassLoader();
 final InputStream dtdIs = loader.getResourceAsStream(dtdResourceName);
 final InputSource expectedInputSource = new InputSource(dtdIs);
 final InputSource actualInputSource = loader.resolveEntity("TestPublicId", "");
 assertEquals(expectedInputSource.getClass(), actualInputSource.getClass());
 assertEquals(expectedInputSource.getByteStream(), actualInputSource.getByteStream());
 }

 @Test(expected = IOException.class)
 public void testResolveEntityWithIOException() throws Exception {
 final String dtdResourceName = "TestDtd.dtd";
 final ClassLoader loader = AbstractLoaderTest.class.getClassLoader();
 loader.getResourceAsStream(dtdResourceName);
 final InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 loader.resolveEntity("TestPublicId", "");
 }

 @Test(expected = SAXException.class)
 public void testError() throws Exception {
 loader.error(new SAXParseException("Test Error", null, 0, 0, null));
 }
}