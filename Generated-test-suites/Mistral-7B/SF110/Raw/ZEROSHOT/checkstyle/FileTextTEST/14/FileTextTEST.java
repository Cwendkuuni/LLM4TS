package com.example.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.io.CharStreams;

public class FileTextTest {

 private File file;
 private FileText fileText;
 private static final String TEST_FILE_NAME = "testFile.txt";
 private static final String TEST_CHARSET = "UTF-8";
 private static final String TEST_CONTENT = "Line 1\nLine 2\nLine 3";

 @Before
 public void setUp() throws IOException {
 file = new File(TEST_FILE_NAME);
 FileUtils.writeStringToFile(file, TEST_CONTENT, TEST_CHARSET);
 fileText = new FileText(file, TEST_CHARSET);
 }

 @Test
 public void testFileTextConstructionWithFileAndCharset() throws IOException {
 assertEquals(TEST_FILE_NAME, fileText.getFile().getName());
 assertEquals(TEST_CHARSET, fileText.getCharset().name());
 assertEquals(TEST_CONTENT, fileText.getFullText());
 }

 @Test
 public void testFileTextConstructionWithFileText() {
 FileText fileTextCopied = new FileText(fileText);
 assertEquals(fileText, fileTextCopied);
 }

 @Test
 public void testFileTextConstructionWithFileAndList() {
 List<String> lines = Arrays.asList(TEST_CONTENT.split("\n"));
 FileText fileTextFromLines = FileText.fromLines(file, lines);
 assertEquals(fileText, fileTextFromLines);
 }

 @Test
 public void testGetFile() {
 assertEquals(fileText.getFile(), file);
 }

 @Test
 public void testGetCharset() {
 assertEquals(TEST_CHARSET, fileText.getCharset().name());
 }

 @Test
 public void testGetFullText() {
 assertEquals(TEST_CONTENT, fileText.getFullText());
 }

 @Test
 public void testToLinesArray() {
 String[] lines = fileText.toLinesArray();
 assertEquals(3, lines.length);
 assertArrayEquals(new String[]{"Line 1", "Line 2", "Line 3"}, lines);
 }

 @Test
 public void testFindLineBreaks() {
 int[] lineBreaks = fileText.findLineBreaks();
 assertEquals(4, lineBreaks.length);
 assertArrayEquals(new int[]{0, 2, 5, 8}, lineBreaks);
 }

 @Test
 public void testLineColumn() {
 int pos = TEST_CONTENT.indexOf("Line");
 LineColumn lineColumn = fileText.lineColumn(pos);
 assertEquals(1, lineColumn.lineNumber);
 assertEquals(5, lineColumn.columnNumber);
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFoundException() throws IOException {
 File nonExistentFile = new File("nonExistentFile.txt");
 try {
 new FileText(nonExistentFile, TEST_CHARSET);
 fail();
 } catch (FileNotFoundException e) {
 // expected
 }
 }

 @Test
 public void testIOException() throws IOException {
 Charset decoder = Charset.forName("nonExistentCharset");
 try {
 new FileText(file, decoder);
 fail();
 } catch (IOException e) {
 // expected
 }
 }

 @Test
 public void testUnsupportedCharsetException() throws IOException {
 Charset decoder = Charset.forName("ISO-8859-15");
 try {
 new FileText(file, decoder.name());
 fail();
 } catch (UnsupportedCharsetException e) {
 // expected
 }
 }

 @After
 public void tearDown() throws IOException {
 if (file.exists()) {
 file.delete();
 }
 }

}