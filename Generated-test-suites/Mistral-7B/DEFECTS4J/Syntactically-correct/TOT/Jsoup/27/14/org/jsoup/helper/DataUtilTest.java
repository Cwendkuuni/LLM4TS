package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

import static org.junit.Assert.*;

public class DataUtilTest {

 private Document document;
 private Document expectedDocument;
 private Parser parser;

 @Before
 public void setUp() {
 parser = Parser.htmlParser();
 }

 @Test
 public void testLoadFile() throws IOException {
 File inputFile = new File("src/test/resources/testFile.html");
 document = DataUtil.load(inputFile, "UTF-8", null);
 expectedDocument = parser.parseInput(new String(Files.readAllBytes(inputFile.toPath()), StandardCharsets.UTF_8), null);
 assertEquals("Document contents should be equal", expectedDocument.html(), document.html());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 document = DataUtil.load(inputStream, "UTF-8", null);
 expectedDocument = parser.parseInput("Test content", null);
 assertEquals("Document contents should be equal", expectedDocument.html(), document.html());
 }

 @Test
 public void testLoadInputStreamWithWrongCharset() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 outputStream.write("Test content".getBytes(StandardCharsets.ISO_8859_1));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 document = DataUtil.load(inputStream, null, null);
 // Since no charset is provided, the default charset (UTF-8) should be used
 expectedDocument = parser.parseInput("Testcontent", null); // note the missing 'e' in 'content'
 assertEquals("Document contents should be equal", expectedDocument.html(), document.html());
 }

 @Test(expected = IOException.class)
 public void testLoadFileWithInvalidFile() throws IOException {
 document = DataUtil.load(new File("src/test/resources/nonexistentfile.html"), null, null);
 fail("Should throw IOException");
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullInputStream() throws IOException {
 document = DataUtil.load(null, null, null);
 fail("Should throw IOException");
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamWithNullInput() throws IOException {
 document = DataUtil.load(null, null, null);
 fail("Should throw IOException");
 }

 @Test
 public void testParseByteData() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 // Write a file with a Content-Type header and a charset different than UTF-8
 outputStream.write("Meta-Charset: gb2312\r\n".getBytes(StandardCharsets.UTF_8));
 outputStream.write("Content-Type: text/html; charset=gb2312\r\n".getBytes(StandardCharsets.UTF_8));
 outputStream.write("<html></html>".getBytes(StandardCharsets.UTF_8));
 InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 document = DataUtil.load(inputStream, null, null).parseByteData(ByteBuffer.wrap(outputStream.toByteArray()), null, null, Parser.htmlParser());
 expectedDocument = parser.parseInput("<html></html>", null);
 assertEquals("Document contents should be equal", expectedDocument.html(), document.html());
 }

 @Test(expected = IOException.class)
 public void testParseByteDataWithNullByteData() throws IOException {
 document = DataUtil.parseByteData(null, null, null, parser);
 fail("Should throw IOException");
 }

 @Test(expected = IOException.class)
 public void testParseByteDataWithNullCharsetName() throws IOException {
 document = DataUtil.parseByteData(ByteBuffer.wrap("Testdata".getBytes(StandardCharsets.UTF_8)), null, null, parser);
 fail("Should throw IOException");
 }
}