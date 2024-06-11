package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class DataUtilTest {

 private static final String HTML_UTF8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n<title>Test Page</title>\n</head>\n<body>\n<p>Test content.</p>\n</body>\n</html>";
 private static final String HTML_GB2312 = "<?xml version=\"1.0\" encoding=\"GB2312\"?>\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"/>\n<title>Test Page</title>\n</head>\n<body>\n<p>Test content.</p>\n</body>\n</html>";

 @BeforeClass
 public static void setup() throws IOException {
 File tempFile = File.createTempFile("test-data", null);
 try (FileWriter writer = new FileWriter(tempFile)) {
 writer.write(HTML_UTF8);
 }
 }

 @Test
 public void testLoadUtf8File() throws IOException {
 Document doc = DataUtil.load(new File("test-data"), "UTF-8", null);
 assertNotNull(doc);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadGb2312File() throws IOException {
 Document doc = DataUtil.load(new File("test-data"), null, null);
 assertNotNull(doc);
 assertEquals("gb2312", doc.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testLoadNullFile() throws IOException {
 DataUtil.load(null, null, null);
 }

 @Test(expected = IOException.class)
 public void testLoadNonExistentFile() throws IOException {
 DataUtil.load(new File("non-existent-file"), null, null);
 }

 @Test
 public void testLoadInputStreamUtf8() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 outStream.write(HTML_UTF8.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
 Document doc = DataUtil.load(inStream, "UTF-8", null);
 assertNotNull(doc);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testLoadInputStreamDefault() throws IOException {
 ByteArrayOutputStream outStream = new ByteArrayOutputStream();
 outStream.write(HTML_UTF8.getBytes(StandardCharsets.UTF_8));
 ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
 Document doc = DataUtil.load(inStream, null, null);
 assertNotNull(doc);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testLoadInputStreamNull() throws IOException {
 DataUtil.load(null, null, null);
 }

 @Test
 public void testParseByteDataUTF8() throws IOException {
 String docData = HTML_UTF8;
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(docData.getBytes(StandardCharsets.UTF_8)));
 Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
 assertNotNull(doc);
 assertEquals("UTF-8", doc.outputSettings().charset());
 }

 @Test
 public void testParseByteDataGb2312() throws IOException {
 String docData = HTML_GB2312;
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(docData.getBytes(StandardCharsets.UTF_8)));
 Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
 assertNotNull(doc);
 assertEquals("gb2312", doc.outputSettings().charset());
 }

 @Test(expected = IOException.class)
 public void testParseByteDataNull() throws IOException {
 Document doc = DataUtil.parseByteData(null, null, null, Parser.htmlParser());
 }

 @Test(expected = IOException.class)
 public void testParseByteDataInvalidCharset() throws IOException {
 String docData = HTML_UTF8;
 ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(docData.getBytes(StandardCharsets.UTF_8)));
 Document doc = DataUtil.parseByteData(byteData, "invalid", null, Parser.htmlParser());
 }
}