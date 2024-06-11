package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AbstractLoaderTest {

 private static final String TEST_DTD_RESOURCE_NAME = "test.dtd";

 @Test
 public void testDefaultConstructor() {
     Assert.assertNull(new AbstractLoader(null).getPublicIdToResourceNameMap());
 }

 @Test(expected = SAXException.class)
 @Test(expected = ParserConfigurationException.class)
 public void testInvalidConstructor() throws SAXException, ParserConfigurationException {
     new AbstractLoader(null, null);
 }

 @Test
 public void testValidConstructor() throws SAXException, ParserConfigurationException {
     final Map<String, String> publicIdToResourceNameMap = new HashMap<>();
     publicIdToResourceNameMap.put("testPublicId", TEST_DTD_RESOURCE_NAME);

     final AbstractLoader abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
     assertEquals(publicIdToResourceNameMap, abstractLoader.getPublicIdToResourceNameMap());
 }

 @Test
 public void testParseInputSource() throws IOException, SAXException {
     final String xmlInput = "<test><element/></test>";
     final AbstractLoader abstractLoader = new AbstractLoader();
     final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

     doAnswer(invocation -> {
         final InputSource inputSource = invocation.getArgument(0);
         final InputStream inputStream = inputSource.getByteStream();
         IOUtils.copy(inputStream, outputStream);
         return null;
     }).when(abstractLoader).parseInputSource(any(InputSource.class));

     abstractLoader.parseInputSource(new InputSource(new ByteArrayInputStream(xmlInput.getBytes())));
     final String output = outputStream.toString();
     assertEquals("<test><element/></test>", output);
 }

 @Test
 public void testResolveEntityWithPublicId() throws IOException, SAXException {
     final AbstractLoader abstractLoader = new AbstractLoader();
     final String dtdResourceName = "test.dtd";
     final Map<String, String> expectedPublicIdToResourceNameMap = new HashMap<>();
     expectedPublicIdToResourceNameMap.put("testPublicId", dtdResourceName);

     doReturn(expectedPublicIdToResourceNameMap).when(abstractLoader).getPublicIdToResourceNameMap();
     doAnswer(invocation -> {
         final InputSource inputSource = invocation.getArgument(0);
         final InputStream inputStream = inputSource.getByteStream();
         final String resourceName = inputSource.getSystemId();
         assertEquals(dtdResourceName, resourceName);
         return new InputSource(inputStream);
     }).when(abstractLoader).resolveEntity("testPublicId", anyString());

     InputSource inputSource = new InputSource(new File(TEST_DTD_RESOURCE_NAME).toURI().toURL().openStream());
     InputSource result = abstractLoader.resolveEntity("testPublicId", null);
     assertEquals(inputSource, result);
 }

 @Test
 public void testResolveEntityWithSystemId() throws IOException, SAXException {
     final AbstractLoader abstractLoader = new AbstractLoader();
     final String systemId = "test.xml";
     final InputStream expectedInputStream = mock(InputStream.class);
     doReturn(expectedInputStream).when(abstractLoader).getClass().getClassLoader().getResourceAsStream(TEST_DTD_RESOURCE_NAME);
     doReturn(new InputSource(expectedInputStream)).when(abstractLoader).resolveEntity("testPublicId", TEST_DTD_RESOURCE_NAME);

     doAnswer(invocation -> {
         final InputSource inputSource = invocation.getArgument(0);
         final String systemId = inputSource.getSystemId();
         assertEquals(systemId, "test.xml");
         return inputSource;
     }).when(abstractLoader).resolveEntity("test", systemId);

     InputSource result = abstractLoader.resolveEntity("test", null);
     assertEquals(null, result);
 }

 @Test(expected = SAXException.class)
 public void testError() throws IOException, SAXException {
     final AbstractLoader abstractLoader = mock(AbstractLoader.class);
     final SAXParseException expectedException = mock(SAXParseException.class);

     doAnswer(invocation -> {
         final SAXParseException exception = invocation.getArgument(0);
         throw expectedException;
     }).when(abstractLoader).error(any(SAXParseException.class));

     abstractLoader.error(mock(SAXParseException.class));
 }

 @Test(expected = SAXException.class)
 public void testFatalError() throws IOException, SAXException {
     final AbstractLoader abstractLoader = mock(AbstractLoader.class);
     final SAXParseException expectedException = mock(SAXParseException.class);

     doAnswer(invocation -> {
         final SAXParseException exception = invocation.getArgument(0);
         throw expectedException;
     }).when(abstractLoader).fatalError(any(SAXParseException.class));

     abstractLoader.fatalError(mock(SAXParseException.class));
 }
}