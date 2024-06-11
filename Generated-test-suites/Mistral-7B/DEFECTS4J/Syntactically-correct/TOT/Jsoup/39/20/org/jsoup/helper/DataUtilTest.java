package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import static org.junit.Assert.*;

public class DataUtilTest {

private Document doc;
private InputStream inputStream;

@Before
public void setUp() {
// Set up any mocks or initializations here
}

@After
public void tearDown() {
// Clean up any mocks or test artifacts here
if (doc != null) {
doc.outputSettings().clear();
doc = null;
}
if (inputStream != null) {
try {
inputStream.close();
} catch (IOException e) {
// Ignore close exception here
}
inputStream = null;
}
}

/**
* Test for loading a file with a specified charset and baseUri.
* @throws IOException
*/
@Test
public void testLoadFileWithCharsetAndBaseUri() throws IOException {
File file = new File("testFile.html");
String charsetName = "UTF-8";
String baseUri = "http://example.com";
Document expectedDoc = new Parser().parse(new FileReader("testFile.html"));
Document actualDoc = DataUtil.load(file, charsetName, baseUri);
assertEquals(expectedDoc.html(), actualDoc.html());
}

/**
* Test for loading a file with null charsetName and default charset.
* @throws IOException
*/
@Test
public void testLoadFileWithNullCharsetName() throws IOException {
File file = new File("testFile.html");
String baseUri = "http://example.com";
InputStream expectedInputStream = new FileInputStream(file);
ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
byte[] buffer = new byte[1024];
int bytesRead;

// Read the file into a byte array for input stream
while ((bytesRead = expectedInputStream.read(buffer)) != -1) {
outputStream.write(buffer, 0, bytesRead);
}

inputStream = new ByteArrayInputStream(outputStream.toByteArray());
Document actualDoc = DataUtil.load(file, null, baseUri);
Document expectedDoc = new Parser().parseInput(outputStream.toByteArray(), baseUri);
assertEquals(expectedDoc.html(), actualDoc.html());

// Clean up input stream and output stream
outputStream.close();
expectedInputStream.close();
inputStream.close();
}

/**
* Test for loading an input stream with a specified charset and baseUri.
* @throws IOException
*/
@Test
public void testLoadInputStreamWithCharsetAndBaseUri() throws IOException {
String charsetName = "UTF-8";
String baseUri = "http://example.com";
String htmlContent = "<html><body><h1>Test</h1></body></html>";
ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
Document expectedDoc = new Parser().parseInput(new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8)), baseUri);
Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri);
assertEquals(expectedDoc.html(), actualDoc.html());
}

/**
* Test for loading an input stream with null charsetName and default charset.
* @throws IOException
*/
@Test
public void testLoadInputStreamWithNullCharsetName() throws IOException {
String baseUri = "http://example.com";
String htmlContent = "<html><body><h1>Test</h1></body></html>";
ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
Document actualDoc = DataUtil.load(inputStream, null, baseUri);
Document expectedDoc = new Parser().parseInput(htmlContent.getBytes(), baseUri);
assertEquals(expectedDoc.html(), actualDoc.html());
}

/**
* Test for loading an input stream, specifying an alternate parser, and setting a charset.
* @throws IOException
*/
@Test
public void testLoadInputStreamWithParserAndCharset() throws IOException {
String charsetName = "UTF-8";
String baseUri = "http://example.com";
String xmlContent = "<xml><root><child>1</child></root></xml>";
ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
Parser parser = DataUtil.xmlParser();
Document expectedDoc = parser.parseInput(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)), baseUri);
Document actualDoc = DataUtil.load(inputStream, charsetName, baseUri, parser);
assertEquals(expectedDoc.xml(), actualDoc.xml());
}

/**
* Test for parsing byte data with a specified charset and baseUri.
* @throws IOException
*/
@Test
public void testParseByteDataWithCharsetAndBaseUri() throws IOException {
String charsetName = "UTF-8";
String baseUri = "http://example.com";
String htmlContent = "<html><body><h1>Test</h1></body></html>";
ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
byte[] buffer = new byte[1024];
int bytesRead;

// Write htmlContent to byte array for input stream
outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
Document expectedDoc = new Parser().parseInput(outputStream.toByteArray(), baseUri);
Document actualDoc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), charsetName, baseUri, Parser.htmlParser());
assertEquals(expectedDoc.html(), actualDoc.html());

// Clean up input stream and output stream
outputStream.close();
inputStream.close();
}

/**
* Test for parsing byte data with null charsetName and default charset.
* @throws IOException
*/
@Test
public void testParseByteDataWithNullCharsetName() throws IOException {
String baseUri = "http://example.com";
String htmlContent = "<html><body><h1>Test</h1></body></html>";
ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
byte[] buffer = new byte[1024];
int bytesRead;

// Write htmlContent to byte array for input stream
outputStream.write(htmlContent.getBytes(StandardCharsets.UTF_8));
ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
Document actualDoc = DataUtil.parseByteData(DataUtil.readToByteBuffer(inputStream), null, baseUri, Parser.htmlParser());
Document expectedDoc = new Parser().parseInput(outputStream.toByteArray(), baseUri);
assertEquals(expectedDoc.html(), actualDoc.html());

// Clean up input stream and output stream
outputStream.close();
inputStream.close();
}
}