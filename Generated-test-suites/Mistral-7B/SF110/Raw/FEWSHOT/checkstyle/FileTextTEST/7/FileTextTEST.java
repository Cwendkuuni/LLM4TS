package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FileTextTest {

 private static final int READ_BUFFER_SIZE = 1024;
 private static final Pattern LINE_TERMINATOR = Pattern.compile("\\n|\\r\\n?");

 @Mock
 private BufferedReader mockBufferedReader;

 @Mock
 private CharsetDecoder mockCharsetDecoder;

 @Mock
 private FileInputStream mockFileInputStream;

 @Mock
 private InputStreamReader mockInputStreamReader;

 @Mock
 private StringReader mockStringReader;

 @Test
 public void testFileTextConstructionWithFileAndCharset() throws Exception {
     final File file = mock(File.class);
     final String charsetName = "UTF-8";
     final FileText fileText = new FileText(file, charsetName);

     verify(file).exists();
     verify(mockCharsetDecoder, times(1)).newDecoder();
     verify(mockCharsetDecoder, times(1)).onMalformedInput(eq(CodingErrorAction.REPLACE));
     verify(mockCharsetDecoder, times(1)).onUnmappableCharacter(eq(CodingErrorAction.REPLACE));
     verify(mockInputStreamReader, times(1)).newInstance(eq(mockFileInputStream), eq(mockCharsetDecoder));
     verify(mockStringReader, times(1)).newInstance(eq(mockInputStreamReader));
     verify(mockBufferedReader, times(1)).newInstance(eq(mockStringReader));

     assertEquals(file, fileText.getFile());
     assertNotNull(fileText.getCharset());
     assertNotNull(fileText.getFullText());
     assertNotNull(fileText.toLinesArray());
     assertEquals(fileText.size(), 1);
 }

 @Test
 public void testFileTextConstructionWithFileText() {
     final FileText fileText1 = mock(FileText.class);
     final FileText fileText2 = new FileText(fileText1);

     assertEquals(fileText1.getFile(), fileText2.getFile());
     assertEquals(fileText1.getCharset(), fileText2.getCharset());
     assertEquals(fileText1.getFullText(), fileText2.getFullText());
     assertArrayEquals(fileText1.toLinesArray(), fileText2.toLinesArray());
     assertNull(fileText2.lineBreaks);
 }

 @Test
 public void testFileTextConstructionWithFileAndLines() {
     final File file = mock(File.class);
     final List<String> lines = Arrays.asList("line1", "line2");
     final FileText fileText = FileText.fromLines(file, lines);

     assertEquals(file, fileText.getFile());
     assertNull(fileText.getCharset());
     assertEquals(lines.toArray(new String[0]), fileText.toLinesArray());
 }

 @Test
 public void testGetFullText() throws IOException {
     final File file = mock(File.class);
     when(file.exists()).thenReturn(true);
     final String charsetName = "UTF-8";
     final FileText fileText = new FileText(file, charsetName);

     final StringBuilder expected = new StringBuilder();
     expected.append("line1\nline2");
     assertEquals(expected.toString(), fileText.getFullText());
 }

 @Test
 public void testGetLinesArray() {
     final FileText fileText = new FileText(new File("test.txt"), "UTF-8");
     final String[] expected = new String[]{"line1", "line2"};
     assertArrayEquals(expected, fileText.toLinesArray());
 }

 @Test
 public void testFindLineBreaks() {
     final StringBuilder text = new StringBuilder();
     text.append("line1\nline2\nline3");
     final FileText fileText = new FileText(new File("test.txt"), text.toString());

     final int[] expected = new int[]{0, 5, 11, 19};
     assertArrayEquals(expected, fileText.findLineBreaks());
 }

 @Test
 public void testLineColumn() {
     final StringBuilder text = new StringBuilder();
     text.append("line1\nline2\nline3");
     final FileText fileText = new FileText(new File("test.txt"), text.toString());

     final LineColumn expected = new LineColumn(1, 5);
     assertEquals(expected, fileText.lineColumn(5));
 }

 @Test
 public void testReadFile() throws IOException {
     final File inputFile = mock(File.class);
     when(inputFile.exists()).thenReturn(true);
     final CharsetDecoder decoder = mock(CharsetDecoder.class);

     final String expected = "expected";
     final String actual = FileText.readFile(inputFile, decoder);
     assertEquals(expected, actual);
 }

 @Test
 public void testFromLines() {
     final List<String> lines = Arrays.asList("line1", "line2");
     final File file = new File("test.txt");
     final FileText fileText = FileText.fromLines(file, lines);

     assertEquals("line1\nline2", fileText.getFullText());
     assertEquals(lines.toArray(new String[0]), fileText.toLinesArray());
 }
}