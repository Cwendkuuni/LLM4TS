package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DataUtilTest {

 private DataUtil dataUtil;
 private Document document;
 private Parser parser;
 private InputStream inputStream;
 private OutputStream outputStream;
 private RandomAccessFile randomAccessFile;
 private File file;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 dataUtil = new DataUtil();
 parser = mock(Parser.class);
 inputStream = mock(InputStream.class);
 outputStream = mock(OutputStream.class);
 randomAccessFile = mock(RandomAccessFile.class);
 file = mock(File.class);
 }

 @After
 public void tearDown() {
 // Release mocks
 }

 @Test
 public void testLoadFile() throws IOException {
 // Arrange
 String content = "<html><head></head><body><h1>Hello World</h1></body></html>";
 File file = new File("test.html");
 FileWriter fileWriter = new FileWriter(file);
 fileWriter.write(content);
 fileWriter.close();

 // Act
 Document document = dataUtil.load(file, "UTF-8", null);

 // Assert
 assertNotNull(document);
 assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", document.html());
 }

 @Test
 public void testLoadInputStream() throws IOException {
 // Arrange
 String content = "<html><head></head><body><h1>Hello World</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

 // Act
 Document document = dataUtil.load(inputStream, "UTF-8", null);

 // Assert
 assertNotNull(document);
 assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", document.html());
 }

 @Test
 public void testParseInputStream() throws IOException {
 // Arrange
 String content = "<html><head></head><body><h1>Hello World</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

 // Act
 Document document = dataUtil.parseInputStream(inputStream, "UTF-8", null, parser);

 // Assert
 assertNotNull(document);
 assertEquals("<html><head></head><body><h1>Hello World</h1></body></html>", document.html());
 }

 @Test
 public void testCrossStreams() throws IOException {
 // Arrange
 ByteArrayInputStream inputStream = new ByteArrayInputStream("Hello World".getBytes());
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

 // Act
 dataUtil.crossStreams(inputStream, outputStream);

 // Assert
 assertEquals("Hello World", outputStream.toString());
 }

 @Test
 public void testParseInputStreamWithInvalidCharset() throws IOException {
 // Arrange
 String content = "<html><head></head><body><h1>Hello World</h1></body></html>";
 InputStream inputStream = new ByteArrayInputStream(content.getBytes("ISO-8859-1"));

 // Act and Assert
 assertThrows(IOException.class, () -> dataUtil.parseInputStream(inputStream, null, null, parser));
 }

 @Test
 public void testGetCharsetFromContentType() {
 // Arrange
 String contentType = "text/html; charset=UTF-8";

 // Act
 String charset = DataUtil.getCharsetFromContentType(contentType);

 // Assert
 assertEquals("UTF-8", charset);
 }

 @Test
 public void testValidateCharset() {
 // Act
 String charset = DataUtil.validateCharset("UTF-8");

 // Assert
 assertEquals("UTF-8", charset);

 // Act and Assert
 assertNull(DataUtil.validateCharset("InvalidCharset"));
 }

 @Test
 public void testMimeBoundary() {
 // Act
 String boundary = DataUtil.mimeBoundary();

 // Assert
 assertNotNull(boundary);
 }

 @Test
 public void testDetectCharsetFromBom() throws IOException {
 // Arrange
 ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00});

 // Act
 DataUtil.BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteBuffer);

 // Assert
 assertNotNull(bomCharset);
 assertEquals("UTF-16", bomCharset.charset);
 assertEquals(0, bomCharset.offset);
 }

 @Test
 public void testDetectCharsetFromBomNull() {
 // Act and Assert
 assertNull(DataUtil.detectCharsetFromBom(null));
 }
}