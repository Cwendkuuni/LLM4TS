package org.jsoup.helper.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

 private Document doc;
 private Parser parser;

 @Before
 public void setUp() {
 parser = Parser.htmlParser();
 }

 @Test
 public void testLoadFileWithDefaultCharset() throws IOException {
 File inputFile = new File("input/sample.html");
 doc = DataUtil.load(inputFile, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadFileWithSpecifiedCharset() throws IOException {
 File inputFile = new File("input/sample_utf8.html");
 doc = DataUtil.load(inputFile, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithDefaultCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/input/sample.html");
 doc = DataUtil.load(inputStream, null, null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/input/sample_utf8.html");
 doc = DataUtil.load(inputStream, "UTF-8", null);
 assertNotNull(doc);
 }

 @Test
 public void testLoadInputStreamWithParser() throws IOException {
 InputStream inputStream = getClass().getResourceAsStream("/input/sample.html");
 doc = DataUtil.load(inputStream, null, null, parser);
 assertNotNull(doc);
 }

 @Test
 public void testCrossStreams() throws IOException {
 File inputFile = new File("input/sample.html");
 InputStream inputStream = new FileInputStream(inputFile);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 DataUtil.crossStreams(inputStream, outputStream);
 inputStream.close();
 assertEquals(new String(outputStream.toByteArray()), new String(Files.readAllBytes(Paths.get("input/sample.html"))));
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("input/sample.html"));
 doc = DataUtil.parseByteData(byteData, null, null, parser);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithSpecifiedCharset() {
 String charsetName = "UTF-8";
 ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("input/sample_utf8.html"));
 doc = DataUtil.parseByteData(byteData, charsetName, null, parser);
 assertNotNull(doc);
 }

 @Test
 public void testParseByteDataWithEmptyByteBuffer() {
 ByteBuffer byteData = DataUtil.emptyByteBuffer();
 doc = DataUtil.parseByteData(byteData, null, null, parser);
 assertNotNull(doc);
 }
}