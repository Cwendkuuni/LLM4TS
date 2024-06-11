package org.jsoup.helper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;

class DataUtilTest {

 private DataUtil dataUtil;
 private Document document;
 private InputStream inputStream;
 private ByteArrayOutputStream outputStream;

 @BeforeEach
 void setUp() {
 dataUtil = new DataUtil();
 }

 @Test
 void testLoadFile() throws IOException {
 File file = new File("test.html");
 document = dataUtil.load(file, "UTF-8", null);
 Assertions.assertNotNull(document);
 }

 @Test
 void testLoadInputStream() throws IOException {
 byte[] html = "<html><body></body></html>".getBytes("UTF-8");
 inputStream = new ByteArrayInputStream(html);
 document = dataUtil.load(inputStream, "UTF-8", null);
 Assertions.assertNotNull(document);
 }

 @Test
 void testParseByteData() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html><body></body></html>".getBytes("UTF-8"));
 document = dataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 Assertions.assertNotNull(document);
 }

 @Test
 void testParseByteDataWithInvalidCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<?xml version=\"1.0\" encoding=\"UTF-16\"?>\n<html><body></body></html>".getBytes("UTF-8"));
 document = dataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 Assertions.assertNull(document);
 }

 @Test
 void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream("test".getBytes("UTF-8"));
 dataUtil.crossStreams(inputStream, outputStream);
 Assertions.assertEquals("test", outputStream.toString("UTF-8"));
 }

 @Test
 void testParseByteDataWithDetectedCharset() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<html><head><meta charset=\"UTF-16\"></head><body></body></html>".getBytes("UTF-8"));
 document = dataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 Assertions.assertEquals("UTF-16", document.outputSettings().charset());
 }

 @Test
 void testParseByteDataWithDetectedCharsetUsingXmlDeclaration() throws IOException {
 ByteBuffer byteData = ByteBuffer.wrap("<html xmlns:msxml=\"urn:schemas-microsoft-com:xml-document\" xml:xmlversion=\"1.0\" encoding=\"UTF-16\"?>\n<head></head>\n<body></body></html>".getBytes("UTF-8"));
 document = dataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 Assertions.assertEquals("UTF-16", document.outputSettings().charset());
 }

 @Test
 void testGetCharsetFromContentType() {
 String contentType = "text/html; charset=UTF-8";
 String charset = dataUtil.getCharsetFromContentType(contentType);
 Assertions.assertEquals("UTF-8", charset);
 }

 @Test
 void testGetCharsetFromContentTypeInvalidCharset() {
 String contentType = "text/html; charset=x-invalid";
 String charset = dataUtil.getCharsetFromContentType(contentType);
 Assertions.assertNull(charset);
 }

 @Test
 void testMimeBoundary() {
 String boundary = dataUtil.mimeBoundary();
 Assertions.assertNotNull(boundary);
 Assertions.assertEquals(boundary.length(), 32);
 }
}