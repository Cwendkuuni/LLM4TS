package com.example.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.collect.Maps;

import junit.framework.TestCase;

public class AbstractLoaderTest extends TestCase {

 private AbstractLoader abstractLoader;
 private Map<String, String> publicIdToResourceNameMap;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 publicIdToResourceNameMap = new HashMap<>();
 publicIdToResourceNameMap.put("publicId1", "dtdResourceName1.dtd");
 abstractLoader = new AbstractLoader(publicIdToResourceNameMap) {
 @Override
 public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
 if (publicId != null && publicId.equals("publicId1")) {
 return new InputSource(new ByteArrayInputStream(new byte[]{}));
 }
 return super.resolveEntity(publicId, systemId);
 }
 };
 }

 @Test
 public void testParseInputSource() throws Exception {
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[]{}));
 abstractLoader.parseInputSource(inputSource);
 verify(abstractLoader, times(1)).parse(inputSource);
 }

 @Test
 public void testResolveEntity() throws Exception {
 InputStream dtdIs = mock(InputStream.class);
 InputSource dtdInputSource = new InputSource(dtdIs);
 when(dtdIs.available()).thenReturn(0);
 when(dtdIs.readAllBytes()).thenReturn(new byte[0]);
 publicIdToResourceNameMap.put("publicId2", "dtdResourceName2.dtd");
 InputSource result = abstractLoader.resolveEntity("publicId2", null);
 assertEquals(dtdInputSource, result);

 verify(abstractLoader, times(1)).getResourceAsStream(eq("dtdResourceName2.dtd"));
 }

 @Test(expected = SAXParseException.class)
 public void testError() throws Exception {
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[]{}));
 abstractLoader.parseInputSource(inputSource);
 doThrow(new SAXParseException("Test error")).when(abstractLoader).parse(inputSource);
 abstractLoader.parseInputSource(inputSource);
 fail("Exception should have been thrown");
 }

 @Test(expected = SAXParseException.class)
 public void testFatalError() throws Exception {
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[]{}));
 doThrow(new SAXParseException("Test fatal error")).when(abstractLoader).parse(inputSource);
 abstractLoader.parseInputSource(inputSource);
 fail("Exception should have been thrown");
 }

}