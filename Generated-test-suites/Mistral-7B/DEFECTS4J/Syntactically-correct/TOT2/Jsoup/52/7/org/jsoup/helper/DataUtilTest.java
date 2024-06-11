package org.jsoup.helper.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class DataUtilTest {

 // Test for load(File, String, String) method
 @Test
 public void testLoadFile() {
 Document doc = null;
 try {
 doc = DataUtil.load(new File("sample.html"), "UTF-8", "baseUri");
 assertNotNull(doc);
 } catch (IOException e) {
 e.printStackTrace();
 }
 }

 // Test for load(InputStream, String, String) method
 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testString = "Test String";
 outputStream.write(testString.getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = null;
 try {
 doc = DataUtil.load(inputStream, "UTF-8", "baseUri");
 assertNotNull(doc);
 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 inputStream.close();
 }
 }

 // Test for load(InputStream, String, String, Parser) method
 @Test
 public void testLoadInputStreamParser() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 String testString = "<html><body><h1>Test String</h1></body></html>";
 outputStream.write(testString.getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 Document doc = null;
 try {
 doc = DataUtil.load(inputStream, "UTF-8", "baseUri", Parser.xmlParser());
 assertNotNull(doc);
 } catch (IOException e) {
 e.printStackTrace();
 } finally {
 inputStream.close();
 }
 }

 // Test for crossStreams(InputStream, OutputStream) method
 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
 ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream(new byte[]{'H', 'e', 'l', 'l', 'o'});
 try {
 DataUtil.crossStreams(inputStream, outputStream1);
 DataUtil.crossStreams(inputStream, outputStream2);
 } finally {
 inputStream.close();
 }
 String outputString1 = new String(outputStream1.toByteArray(), StandardCharsets.UTF_8);
 String outputString2 = new String(outputStream2.toByteArray(), StandardCharsets.UTF_8);
 assertNotNull(outputString1);
 assertNotNull(outputString2);
 assertTrue(outputString1.equals(outputString2));
 }

 // Test for parseByteData(ByteBuffer, String, String, Parser) method
 @Test
 public void testParseByteData() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 String charsetName = "UTF-8";
 String baseUri = "baseUri";
 Document doc = null;
 try {
 doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
 assertNotNull(doc);
 } catch (Exception e) {
 e.printStackTrace();
 }
 }

 // Test for parseByteData(ByteBuffer, String, String, Parser) with invalid charset
 @Test(expected = IllegalArgumentException.class)
 public void testParseByteDataInvalidCharset() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 String charsetName = "invalidCharset";
 String baseUri = "baseUri";
 Document doc = null;
 try {
 doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
 } catch (Exception e) {
 // Expected
 }
 }

 // Test for getCharsetFromContentType(String) method
 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertNotNull(charset);
 assertEquals("UTF-8", charset);
 }

 // Test for validateCharset(String) method
 @Test
 public void testValidateCharset() {
 String charset1 = "UTF-8";
 String charset2 = "invalidCharset";
 assertNotNull(DataUtil.validateCharset(charset1));
 assertNull(DataUtil.validateCharset(charset2));
 }
}