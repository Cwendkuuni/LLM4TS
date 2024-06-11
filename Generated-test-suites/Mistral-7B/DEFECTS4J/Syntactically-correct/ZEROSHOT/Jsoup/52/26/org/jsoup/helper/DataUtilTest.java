package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.nodes.Document;
import org.jsoup. nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class DataUtilTest {

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File inputFile = new File("testFile.html");
 Document doc = DataUtil.load(inputFile, null, null);
 assertEquals("Expected document to not be null", doc, notNull(doc));
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File inputFile = new File("testFile.html");
 Document doc = DataUtil.load(inputFile, "UTF-8", null);
 assertEquals("Expected document to not be null", doc, notNull(doc));
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
 Document doc = DataUtil.load(inputStream, null, null);
 assertEquals("Expected document to not be null", doc, notNull(doc));
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("testData".getBytes("UTF-8"));
 Document doc = DataUtil.load(inputStream, "UTF-8", null);
 assertEquals("Expected document to not be null", doc, notNull(doc));
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
 Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
 assertEquals("Expected document to not be null", doc, notNull(doc));
 }

 @Test
 public void testCrossStreams() throws IOException {
 File inputFile = new File("testFile.html");
 File outputFile = new File("testOutput.html");
 DataUtil.crossStreams(DataUtil.load(inputFile, null, null).inputStream(), DataUtil.save(outputFile, null, null, "testOutput.html", DataUtil.Parser.htmlParser()));
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("testData".getBytes());
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertEquals("Expected document to not be null", doc, notNull(doc));
 }

 @Test
 public void testParseByteDataWithDetectedCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("testData".getBytes("UTF-16"));
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertEquals("Expected document to not be null", doc, notNull(doc));
 assertEquals("Expected charset to be UTF-16", "UTF-16", doc.outputSettings().charset());
 }

 @Test
 public void testReadToByteBuffer() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
 assertEquals("Expected byte buffer size to be greater than zero", byteData.limit(), greaterThan(0));
 }

 @Test
 public void testReadToByteBufferWithMaxSize() throws IOException {
 InputStream inputStream = new ByteArrayInputStream("testData".getBytes());
 ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 10);
 assertEquals("Expected byte buffer size to be less than or equal to 10", byteData.limit(), lessThanOrEqualTo(10));
 }

 @Test
 public void testReadFileToByteBuffer() throws IOException {
 File inputFile = new File("testFile.html");
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(inputFile);
 assertEquals("Expected byte buffer size to be greater than zero", byteData.limit(), greaterThan(0));
 }

 @Test
 public void testEmptyByteBuffer() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 assertEquals("Expected byte buffer size to be zero", byteData.limit(), equalTo(0));
 }

 @Test
 public void testGetCharsetFromContentType() {
 String contentType = "text/plain; charset=ISO-8859-1";
 String charset = DataUtil.getCharsetFromContentType(contentType);
 assertEquals("Expected charset to be ISO-8859-1", "ISO-8859-1", charset);
 }

 @Test
 public void testValidateCharset() {
 String charset = "UTF-8";
 assertEquals("Expected validateCharset to return charset", charset, DataUtil.validateCharset(charset));
 }

 @Test
 public void testValidateCharsetWithInvalidCharset() {
 String charset = "x-invalid";
 assertNull("Expected validateCharset to return null for invalid charset", DataUtil.validateCharset(charset));
 }

 @Test
 public void testMimeBoundary() {
 String boundary = DataUtil.mimeBoundary();
 assertEquals("Expected boundary length to be 32", 32, boundary.length());
 }

 @Test
 public void testDetectCharsetFromBomWithUTF8Bom() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x61, 0x73, 0x73, 0x53, 0x75, 0x61, 0x72, 0x65, 0x2F, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x4D, 0x79, 0x53, 0x43, 0x6F, 0x6D, 0x70, 0x75, 0x74, 0x65, 0x2F, 0x68, 0x6F, 0x6D, 0x65, 0x2F, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x48, 0x6F, 0x73, 0x74, 0x3E });
 String charset = DataUtil.detectCharsetFromBom(byteData, null);
 assertEquals("Expected charset to be UTF-8", "UTF-8", charset);
 }

 @Test
 public void testDetectCharsetFromBomWithUTF16LEBom() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0xFF, 0xFE, 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x61, 0x73, 0x73, 0x53, 0x75, 0x61, 0x72, 0x65, 0x2F, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x4D, 0x79, 0x53, 0x43, 0x6F, 0x6D, 0x70, 0x75, 0x74, 0x65, 0x2F, 0x68, 0x6F, 0x6D, 0x65, 0x2F, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x49, 0x6D, 0x73, 0x4E, 0x61, 0x6D, 0x65, 0x3E });
 String charset = DataUtil.detectCharsetFromBom(byteData, null);
 assertEquals("Expected charset to be UTF-16LE", "UTF-16LE", charset);
 }

 @Test
 public void testDetectCharsetFromBomWithUTF16BEBom() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0xFF, 0xFE, 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x61, 0x73, 0x73, 0x53, 0x75, 0x61, 0x72, 0x65, 0x2F, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x4D, 0x79, 0x53, 0x43, 0x6F, 0x6D, 0x70, 0x75, 0x74, 0x65, 0x2F, 0x68, 0x6F, 0x6D, 0x65, 0x2F, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x4D, 0x79, 0x54, 0x3E });
 String charset = DataUtil.detectCharsetFromBom(byteData, null);
 assertEquals("Expected charset to be UTF-16BE", "UTF-16BE", charset);
 }

 @Test
 public void testDetectCharsetFromBomWithNoBom() {
 ByteBuffer byteData = ByteBuffer.wrap(new byte[] { 0x3C, 0x3F, 0x78, 0x6D, 0x6C, 0x20, 0x61, 0x73, 0x73, 0x53, 0x75, 0x61, 0x72, 0x65, 0x2F, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x4D, 0x79, 0x53, 0x43, 0x6F, 0x6D, 0x70, 0x75, 0x74, 0x65, 0x2F, 0x68, 0x6F, 0x6D, 0x65, 0x2F, 0x63, 0x72, 0x65, 0x61, 0x74, 0x65, 0x41, 0x75, 0x74, 0x69, 0x6F, 0x6E, 0x3E });
 assertNull("Expected charset to be null when no BOM found", DataUtil.detectCharsetFromBom(byteData, null));
 }

 @Test
 public void testParseInputWithNullInputStream() {
 assertThrows(NullPointerException.class, () -> DataUtil.parseByteData(null, null, null, Parser.htmlParser()));
 }

 @Test
 public void testParseInputWithNullCharset() {
 assertThrows(NullPointerException.class, () -> DataUtil.parseByteData(DataUtil.readFileToByteBuffer(new File("testFile.html")), null, null, Parser.htmlParser()));
 }

 @Test
 public void testParseInputWithNullBaseUri() {
 assertThrows(NullPointerException.class, () -> DataUtil.parseByteData(DataUtil.readFileToByteBuffer(new File("testFile.html")), "UTF-8", null, Parser.htmlParser()));
 }

 @Test
 public void testParseInputWithNullParser() {
 assertThrows(NullPointerException.class, () -> DataUtil.parseByteData(DataUtil.readFileToByteBuffer(new File("testFile.html")), "UTF-8", null, null));
 }
}