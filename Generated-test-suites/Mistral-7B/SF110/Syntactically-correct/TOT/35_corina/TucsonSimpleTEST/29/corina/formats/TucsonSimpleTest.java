package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.formats.Filetype;
import corina.formats.TucsonSimple;
import corina.model.Sample;
import corina.model.MetaData;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader readerMock;
 private BufferedWriter writerMock;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 readerMock = mock(BufferedReader.class);
 writerMock = mock(BufferedWriter.class);
 }

 @Test
 public void testToString() {
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadThrowsException() throws IOException {
 when(readerMock.readLine()).thenReturn("Invalid data");
 tucsonSimple.load(readerMock);
 fail("load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 MetaData meta = new MetaData();
 meta.put("id", "123456");
 sample.setMeta(meta);
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);

 sample = new Sample();
 meta = new MetaData();
 sample.setMeta(meta);
 code = tucsonSimple.make6digitCode(sample);
 assertEquals("000000", code);
 }

 @Test(expected = IOException.class)
 public void testSaveDataThrowsIOException() throws IOException {
 when(writerMock.write(anyString())).thenThrow(IOException.class);
 Sample sample = new Sample();
 tucsonSimple.saveData(sample, writerMock);
 fail("saveData method should throw IOException");
 }

 @Test
 public void testSaveDataWritesCorrectData() throws IOException {
 Sample sample = new Sample();
 MetaData meta = new MetaData();
 meta.put("id", "123456");
 sample.setMeta(meta);
 Year start = new Year(2000);
 Year end = new Year(2002);
 Range range = new Range(start, end);
 sample.setRange(range);
 String data = "10 20 30";
 sample.setData(new int[] { 10, 20, 30 });
 int sum = 40;
 sample.setCount(new int[] { 10, 20, 30, sum });
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 when(readerMock.readLine()).then(invocation -> inputStream.readLine());
 when(writerMock.write(anyString())).thenAnswer(invocation -> {
 outputStream.write(invocation.getArgument(0).toString().getBytes());
 outputStream.flush();
 return null;
 });
 tucsonSimple.saveData(sample, writerMock);
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray())));
 String line;
 String expected = "0000002000 10\n" +
 "0000002001 20\n" +
 "0000002002 30\n" +
 "0000002003 40\n";
 while ((line = reader.readLine()) != null) {
 assertEquals(expected.substring(0, line.length()), line);
 expected = expected.substring(line.length());
 }
 }

}