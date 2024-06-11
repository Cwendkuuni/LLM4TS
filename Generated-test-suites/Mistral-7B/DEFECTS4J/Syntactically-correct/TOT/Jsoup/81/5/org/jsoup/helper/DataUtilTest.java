package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

 @Test
 public void testLoadFileWithValidCharset() throws IOException {
 Document doc = DataUtil.load(new File("testFile.html"), "UTF-8", null);
 assertTrue(doc != null);
 }

 @Test
 public void testLoadFileWithInvalidCharset() throws IOException {
 Document doc = DataUtil.load(new File("testFile.html"), "invalidCharset", null);
 assertTrue(doc != null);
 // add assertions to check if the document is correctly decoded with the defaultCharset
 }

 @Test
 public void testLoadFileWithNoCharset() throws IOException {
 Document doc = DataUtil.load(new File("testFile.html"), null, null);
 assertTrue(doc != null);
 // add assertions to check if the document is correctly decoded with the defaultCharset
 }

 @Test
 public void testLoadInputStreamWithValidCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("testFile.html"));
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 assertTrue(doc != null);
 inputStream.close();
 }

 @Test
 public void testLoadInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("testFile.html"));
 Document doc = DataUtil.load(inputStream, "invalidCharset", null);
 assertTrue(doc != null);
 inputStream.close();
 // add assertions to check if the document is correctly decoded with the defaultCharset
 }

 @Test
 public void testLoadInputStreamWithNoCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("testFile.html"));
 Document doc = DataUtil.load(inputStream, null, null);
 assertTrue(doc != null);
 inputStream.close();
 // add assertions to check if the document is correctly decoded with the defaultCharset
 }

 @Test
 public void testLoadInputStreamWithBOM() throws IOException {
 InputStream inputStream = new FileInputStream(new File("testFileWithBOM.html"));
 Document doc = DataUtil.load(inputStream, null, null);
 assertTrue(doc != null);
 inputStream.close();
 // add assertions to check if the document is correctly decoded considering the BOM
 }

 @Test
 public void testParseInputStreamWithValidCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("testFile.html"));
 Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", null, Parser.htmlParser());
 assertTrue(doc != null);
 inputStream.close();
 }

 @Test
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("testFile.html"));
 String charsetName = "invalidCharset";
 Document doc = null;
 try {
 doc = DataUtil.parseInputStream(inputStream, charsetName, null, Parser.htmlParser());
 } catch (IOException e) {
 // expected exception
 }
 inputStream.close();
 assertNotNull(doc);
 }

 @Test
 public void testParseInputStreamWithNoCharset() throws IOException {
 InputStream inputStream = new FileInputStream(new File("testFile.html"));
 Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 assertTrue(doc != null);
 inputStream.close();
 }

 @Test
 public void testParseInputStreamWithBOM() throws IOException {
 InputStream inputStream = new FileInputStream(new File("testFileWithBOM.html"));
 Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
 assertTrue(doc != null);
 inputStream.close();
 // add assertions to check if the document is correctly decoded considering the BOM
 }

 @Test
 public void testCrossStreams() throws IOException {
 File inputFile = new File("testFile.html");
 File outputFile = new File("testFile_output.html");
 InputStream inputStream = new FileInputStream(inputFile);
 OutputStream outputStream = new FileOutputStream(outputFile);
 DataUtil.crossStreams(inputStream, outputStream);
 inputStream.close();
 outputStream.close();
 // add assertions to check if the files are equal
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithInvalidCharset() {
 String contentType = "text/html; charset=invalidCharset";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(DataUtil.defaultCharset, charset);
 }

 @Test
 public void testGetCharsetFromContentTypeWithNoCharset() {
 String contentType = "text/html";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals(DataUtil.defaultCharset, charset);
 }

 @Test
 public void testValidateCharset() {
 String charset = "UTF-8";
 String validatedCharset = DataUtil.validateCharset(charset);
 assertEquals("UTF-8", validatedCharset);
 }

 @Test
 public void testValidateCharsetWithInvalidCharset() {
 String charset = "invalidCharset";
 String validatedCharset = DataUtil.validateCharset(charset);
 assertEquals(DataUtil.defaultCharset, validatedCharset);
 }

 @Test
 public void testValidateCharsetWithEmptyCharset() {
 String charset = "";
 String validatedCharset = DataUtil.validateCharset(charset);
 assertEquals(DataUtil.defaultCharset, validatedCharset);
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 int boundaryLength = DataUtil.boundaryLength();
 String expectedBoundary = new String(DataUtil.mimeBoundaryChars, 0, boundaryLength);
 assertEquals(expectedBoundary, boundary);
 }
}