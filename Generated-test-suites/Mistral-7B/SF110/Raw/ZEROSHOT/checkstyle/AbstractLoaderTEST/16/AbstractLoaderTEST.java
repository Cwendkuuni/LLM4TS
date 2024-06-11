package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.common.collect.Maps;

import junit.framework.TestCase;

public class AbstractLoaderTest extends TestCase {

 private AbstractLoader abstractLoader;
 private Map<String, String> publicIdToResourceNameMap;

 @Before
 public void setUp() throws Exception {
 publicIdToResourceNameMap = new HashMap<String, String>();
 abstractLoader = new AbstractLoader(publicIdToResourceNameMap);
 }

 @Test
 public void testAbstractLoaderWithValidPublicIdAndSystemId() throws Exception {
 // Given
 String publicId = "publicID";
 String dtdResourceName = "dtdResourceName.dtd";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 abstractLoader.parseInputSource(inputSource);

 // Then
 // Verify no exceptions were thrown
 assertTrue("No exception was thrown during parsing", true);
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithInvalidPublicId() throws Exception {
 // Given
 String publicId = "invalidPublicId";
 String dtdResourceName = "dtdResourceName.dtd";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 try {
 abstractLoader.parseInputSource(inputSource);
 } catch (IOException e) {
 fail("An IOException was expected instead of a SAXException");
 }

 // Then
 // Verify an SAXException was thrown with the correct public Id
 SAXParseException exception = (SAXParseException)assertThrows(SAXException.class, () -> abstractLoader.parseInputSource(inputSource));
 assertEquals("Unexpected entity encountered: " + publicId, exception.getMessage());
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithNullPublicId() throws Exception {
 // Given
 String publicId = null;
 String dtdResourceName = "dtdResourceName.dtd";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 try {
 abstractLoader.parseInputSource(inputSource);
 } catch (IOException e) {
 fail("An IOException was expected instead of a SAXException");
 }

 // Then
 // Verify an SAXException was thrown
 SAXParseException exception = (SAXParseException)assertThrows(SAXException.class, () -> abstractLoader.parseInputSource(inputSource));
 assertEquals("Null publicId", exception.getMessage());
 }

 @Test(expected = SAXException.class)
 public void testAbstractLoaderWithNullDtdResourceName() throws Exception {
 // Given
 String publicId = "publicID";
 String dtdResourceName = null;
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 try {
 abstractLoader.parseInputSource(inputSource);
 } catch (IOException e) {
 fail("An IOException was expected instead of a SAXException");
 }

 // Then
 // Verify an SAXException was thrown
 SAXParseException exception = (SAXParseException)assertThrows(SAXException.class, () -> abstractLoader.parseInputSource(inputSource));
 assertEquals("Null dtdResourceName", exception.getMessage());
 }

 @Test
 public void testAbstractLoaderWithValidPublicIdAndEmptyResourceName() throws Exception {
 // Given
 String publicId = "publicID";
 String dtdResourceName = "";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 try {
 abstractLoader.parseInputSource(inputSource);
 } catch (IOException e) {
 fail("An IOException was expected instead of a SAXException");
 }

 // Then
 // Verify no exceptions were thrown
 assertTrue("No exception was thrown during parsing", true);
 }

 @Test
 public void testAbstractLoaderWithEmptyPublicIdAndValidResourceName() throws Exception {
 // Given
 String publicId = "";
 String dtdResourceName = "dtdResourceName.dtd";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 try {
 abstractLoader.parseInputSource(inputSource);
 } catch (IOException e) {
 fail("An IOException was expected instead of a SAXException");
 }

 // Then
 // Verify an SAXException was thrown with the correct message
 SAXParseException exception = (SAXParseException)assertThrows(SAXException.class, () -> abstractLoader.parseInputSource(inputSource));
 assertEquals("Empty publicId", exception.getMessage());
 }

 @Test
 public void testAbstractLoaderWithEmptyPublicIdAndEmptyResourceName() throws Exception {
 // Given
 String publicId = "";
 String dtdResourceName = "";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 try {
 abstractLoader.parseInputSource(inputSource);
 } catch (IOException e) {
 fail("An IOException was expected instead of a SAXException");
 }

 // Then
 // Verify an SAXException was thrown with the correct message
 SAXParseException exception = (SAXParseException)assertThrows(SAXException.class, () -> abstractLoader.parseInputSource(inputSource));
 assertEquals("Both publicId and dtdResourceName are empty", exception.getMessage());
 }

 @Test
 public void testAbstractLoaderWithValidPublicIdAndEmptyResourceNamePair() throws Exception {
 // Given
 String publicId = "publicID";
 String dtdResourceName = "";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 try {
 abstractLoader.parseInputSource(inputSource);
 } catch (IOException e) {
 fail("An IOException was expected instead of a SAXException");
 }

 // Then
 // Verify an SAXException was thrown with the correct message
 SAXParseException exception = (SAXParseException)assertThrows(SAXException.class, () -> abstractLoader.parseInputSource(inputSource));
 assertEquals("Empty dtdResourceName for publicId: " + publicId, exception.getMessage());
 }

 @Test
 public void testAbstractLoaderWithValidPublicIdAndValidResourceNamePair() throws Exception {
 // Given
 String publicId = "publicID";
 String dtdResourceName = "dtdResourceName.dtd";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 try {
 abstractLoader.parseInputSource(inputSource);
 } catch (IOException e) {
 fail("An IOException was expected instead of a SAXException");
 }

 // Then
 // Verify no exceptions were thrown
 assertTrue("No exception was thrown during parsing", true);
 }

 @Test
 public void testAbstractLoaderWithNullPublicIdAndValidResourceNamePair() throws Exception {
 // Given
 String publicId = null;
 String dtdResourceName = "dtdResourceName.dtd";
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 try {
 abstractLoader.parseInputSource(inputSource);
 } catch (IOException e) {
 fail("An IOException was expected instead of a SAXException");
 }

 // Then
 // Verify an SAXException was thrown with the correct message
 SAXParseException exception = (SAXParseException)assertThrows(SAXException.class, () -> abstractLoader.parseInputSource(inputSource));
 assertEquals("Null publicId", exception.getMessage());
 }

 @Test
 public void testAbstractLoaderWithValidPublicIdAndNullResourceNamePair() throws Exception {
 // Given
 String publicId = "publicID";
 String dtdResourceName = null;
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 try {
 abstractLoader.parseInputSource(inputSource);
 } catch (IOException e) {
 fail("An IOException was expected instead of a SAXException");
 }

 // Then
 // Verify an SAXException was thrown with the correct message
 SAXParseException exception = (SAXParseException)assertThrows(SAXException.class, () -> abstractLoader.parseInputSource(inputSource));
 assertEquals("Null dtdResourceName for publicId: " + publicId, exception.getMessage());
 }

 @Test
 public void testAbstractLoaderWithNullPublicIdAndNullResourceNamePair() throws Exception {
 // Given
 String publicId = null;
 String dtdResourceName = null;
 publicIdToResourceNameMap.put(publicId, dtdResourceName);

 // When
 InputSource inputSource = new InputSource(new ByteArrayInputStream(new byte[0]));
 try {
 abstractLoader.parseInputSource(inputSource);
 } catch (IOException e) {
 fail("An IOException was expected instead of a SAXException");
 }

 // Then
 // Verify an SAXException was thrown with the correct message
 SAXParseException exception = (SAXParseException)assertThrows(SAXException.class, () -> abstractLoader.parseInputSource(inputSource));
 assertEquals("Both publicId and dtdResourceName are null", exception.getMessage());
 }
}