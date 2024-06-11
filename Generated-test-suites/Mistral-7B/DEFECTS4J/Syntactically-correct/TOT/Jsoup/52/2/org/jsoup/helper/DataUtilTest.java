package org.jsoup.helper;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Tests for DataUtil class.
 */
public final class DataUtilTest {

 private Document doc;
 private InputStream testInputStream;
 private Charset testCharset;
 private String testBaseUri;
 private Parser parser;

 @Before
 public void setUp() {
 testInputStream = new ByteArrayInputStream("<html><head><meta charset=\"UTF-8\"/></head><body><p>Test</p></body></html>"
 .getBytes(Charset.forName("UTF-8")));
 testCharset = Charset.forName("UTF-8");
 testBaseUri = "testbaseuri";
 parser = Parser.htmlParser();
 }

 @Test
 public void testLoadDocumentFromFileWithValidCharset() throws IOException {
 File testFile = new File("testfile.html");
 doc = DataUtil.load(testFile, testCharset.name(), testBaseUri);
 assertEquals("Test", doc.select("p").text());
 }

 @Test
 public void testLoadDocumentFromFileWithInvalidCharset() throws IOException, IllegalCharsetNameException {
 File testFile = new File("testfile.html");
 assertThrows(IllegalCharsetNameException.class, () -> DataUtil.load(testFile, "InvalidCharset", testBaseUri));
 }

 @Test
 public void testLoadDocumentFromInputStreamWithValidCharsetAndParser() throws IOException {
 doc = DataUtil.load(testInputStream, testCharset.name(), testBaseUri, parser);
 assertEquals("Test", doc.select("p").text());
 }

 @Test
 public void testLoadDocumentFromInputStreamWithNullCharset() throws IOException {
 assertNull(DataUtil.load(testInputStream, null, testBaseUri, parser));
 }

 @Test
 public void testLoadDocumentFromInputStreamWithInvalidCharset() throws IOException, IllegalCharsetNameException {
 assertThrows(IllegalCharsetNameException.class, () -> DataUtil.load(testInputStream, "InvalidCharset", testBaseUri, parser));
 }

 @Test
 public void testParseByteDataWithValidCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("Test".getBytes(Charset.forName("UTF-8")));
 doc = DataUtil.parseByteData(byteData, testCharset.name(), testBaseUri, parser);
 assertEquals("Test", doc.select("p").text());
 }

 @Test
 public void testParseByteDataWithDefaultCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("Test".getBytes(Charset.forName("UTF-16")));
 doc = DataUtil.parseByteData(byteData, null, testBaseUri, parser);
 assertEquals("Test", doc.select("p").text());
 }

 @Test
 public void testParseByteDataWithInvalidCharset() {
 ByteBuffer byteData = ByteBuffer.wrap("Test".getBytes(Charset.forName("InvalidCharset")));
 assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, null, testBaseUri, parser));
 }

 @Test
 public void testCrossStreams() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 InputStream inputStream = new ByteArrayInputStream("Test".getBytes(Charset.forName("UTF-8")));
 DataUtil.crossStreams(inputStream, outputStream);
 assertEquals("Test", new String(outputStream.toByteArray(), Charset.forName("UTF-8")));
 }
}