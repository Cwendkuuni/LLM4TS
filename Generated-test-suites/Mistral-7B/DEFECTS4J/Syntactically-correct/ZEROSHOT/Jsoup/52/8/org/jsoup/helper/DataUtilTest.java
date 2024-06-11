package org.jsoup.helper;

import org.junit.Assert;import org.junit.Test;import org.jsoup.nodes.Document;import org.jsoup.nodes.Element;import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;import java.io.ByteArrayOutputStream;import java.io.IOException;import java.io.InputStream;import java.nio.charset.Charset;import java.util.Locale;import java.util.Random;import java.util.regex.Matcher;import java.util.regex.Pattern;

/**
 * Test class for DataUtil.java
 */
public class DataUtilTest {

 @Test
 public void testLoadFileDocument() throws IOException {
 // Given
 String filePath = "src/test/resources/test.html";
 File inputFile = new File(filePath);
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 // When
 Document doc = DataUtil.load(inputFile, charsetName, baseUri);
 // Then
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamDocument() throws IOException {
 // Given
 String htmlContent = "<html><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 // When
 Document doc = DataUtil.load(inputStream, charsetName, baseUri);
 // Then
 Assert.assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamDocumentWithParser() throws IOException {
 // Given
 String htmlContent = "<html><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 Parser parser = DataUtil.Parser.htmlParser();
 // When
 Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
 // Then
 Assert.assertNotNull(doc);
 }

 @Test
 public void testCrossStreams() throws IOException {
 // Given
 String input = "Hello World!";
 InputStream inputStream = new ByteArrayInputStream(input.getBytes(Charset.forName("UTF-8")));
 OutputStream outputStream = new ByteArrayOutputStream();
 // When
 DataUtil.crossStreams(inputStream, outputStream);
 String output = outputStream.toString(Charset.forName("UTF-8"));
 // Then
 Assert.assertEquals(input, output);
 }

 @Test
 public void testParseByteData() throws IOException {
 // Given
 String htmlContent = "<html><body></body></html>";
 InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
 String charsetName = "UTF-8";
 String baseUri = "http://example.com";
 // When
 Document doc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charsetName, baseUri, DataUtil.Parser.htmlParser());
 // Then
 Assert.assertNotNull(doc);
 }

 @Test
 public void testDetectCharsetFromBom() {
 // Given
 String bomUTF8 = "\uFEFF";
 String bomUTF16LE = "\uFFFE\u0000";
 String bomUTF16BE = "\u0000\uFFFE";
 String bomUTF32LE = "\u0000\u0000\u0000\uFFFE";
 String bomUTF32BE = "\uFFFE\u0000\u0000\u0000";
 // When
 ByteBuffer byteDataUTF8 = ByteBuffer.wrap(bomUTF8.getBytes(Charset.forName("UTF-8")));
 String charsetNameUTF8 = DataUtil.detectCharsetFromBom(byteDataUTF8, null);
 ByteBuffer byteDataUTF16LE = ByteBuffer.wrap(bomUTF16LE.getBytes(Charset.forName("UTF-16")));
 String charsetNameUTF16LE = DataUtil.detectCharsetFromBom(byteDataUTF16LE, null);
 ByteBuffer byteDataUTF16BE = ByteBuffer.wrap(bomUTF16BE.getBytes(Charset.forName("UTF-16")));
 String charsetNameUTF16BE = DataUtil.detectCharsetFromBom(byteDataUTF16BE, null);
 ByteBuffer byteDataUTF32LE = ByteBuffer.wrap(bomUTF32LE.getBytes(Charset.forName("UTF-32")));
 String charsetNameUTF32LE = DataUtil.detectCharsetFromBom(byteDataUTF32LE, null);
 ByteBuffer byteDataUTF32BE = ByteBuffer.wrap(bomUTF32BE.getBytes(Charset.forName("UTF-32")));
 String charsetNameUTF32BE = DataUtil.detectCharsetFromBom(byteDataUTF32BE, null);
 // Then
 Assert.assertEquals("UTF-8", charsetNameUTF8);
 Assert.assertNull(charsetNameUTF16LE);
 Assert.assertNull(charsetNameUTF16BE);
 Assert.assertNull(charsetNameUTF32LE);
 Assert.assertNull(charsetNameUTF32BE);
 }

 @Test
 public void testGetCharsetFromContentType() {
 // Given
 String contentType1 = "text/html; charset=UTF-8";
 String contentType2 = "text/html; charset=ISO-8859-1";
 String contentType3 = "text/html";
 // When
 String charset1 = DataUtil.getCharsetFromContentType(contentType1);
 String charset2 = DataUtil.getCharsetFromContentType(contentType2);
 String charset3 = DataUtil.getCharsetFromContentType(contentType3);
 // Then
 Assert.assertEquals("UTF-8", charset1);
 Assert.assertEquals("ISO-8859-1", charset2);
 Assert.assertNull(charset3);
 }

 @Test
 public void testValidateCharset() {
 // Given
 String charset1 = "UTF-8";
 String charset2 = "UTF-16";
 String charset3 = "UTF-32";
 String charset4 = "EUC-JP";
 // When
 String validatedCharset1 = DataUtil.validateCharset(charset1);
 String validatedCharset2 = DataUtil.validateCharset(charset2);
 String validatedCharset3 = DataUtil.validateCharset(charset3);
 String validatedCharset4 = DataUtil.validateCharset(charset4);
 // Then
 Assert.assertEquals("UTF-8", validatedCharset1);
 Assert.assertEquals("UTF-16", validatedCharset2);
 Assert.assertEquals("UTF-32", validatedCharset3);
 Assert.assertEquals("EUC-JP", validatedCharset4);
 }

 @Test
 public void testMimeBoundary() {
 // Given
 // When
 String mimeBoundary = DataUtil.mimeBoundary();
 // Then
 Assert.assertNotNull(mimeBoundary);
 Assert.assertFalse(mimeBoundary.isEmpty());
 }
}