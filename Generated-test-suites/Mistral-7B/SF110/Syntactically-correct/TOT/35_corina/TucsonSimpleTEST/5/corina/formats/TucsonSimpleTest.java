import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import static org.junit.Assert.*;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedWriter writerMock;
 private BufferedReader readerMock;
 private Sample sample;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 writerMock = mock(BufferedWriter.class);
 readerMock = mock(BufferedReader.class);
 }

 @Test
 public void testToString() throws IOException {
 String expected = "format.tucsonsimple";
 String actual = tucsonSimple.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetDefaultExtension() {
 String expected = ".RWL";
 String actual = tucsonSimple.getDefaultExtension();
 assertEquals(expected, actual);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadMethodThrowsException() throws IOException {
 when(readerMock.readLine()).thenReturn("Invalid file format");
 tucsonSimple.load(readerMock);
 fail("Load method did not throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 Map<String, String> meta = new HashMap<>();
 meta.put("id", "123456");
 Sample sample = new Sample("testSample", meta, new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 });
 String expectedCode = "123456";
 String actualCode = tucsonSimple.make6digitCode(sample);
 assertEquals(expectedCode, actualCode);
 }

 @Test
 public void testMake6digitCodeWhenNoIdPresent() {
 Sample sample = new Sample("testSample", null, new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 });
 String expectedCode = "000000";
 String actualCode = tucsonSimple.make6digitCode(sample);
 assertEquals(expectedCode, actualCode);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Year year = new Year(2005);
 String code = "123456";
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 BufferedWriter writerMockWithOutput = mock(BufferedWriter.class);
 when(writerMock.write(Mockito.anyString())).then(i -> writerMockWithOutput.write(i.arguments[0]));
 when(writerMockWithOutput.flush()).thenReturn(true);
 tucsonSimple.saveRowHeader(writerMockWithOutput, sample, code, 6, year);
 String expected = "1234562005";
 String actual = "";
 String line;
 while ((line = writer.readLine()) != null) {
 actual += line;
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveData() throws IOException {
 Year start = new Year(2001);
 Year end = new Year(2003);
 Sample sample = new Sample("testSample", null, new int[] { 1, 2, 3 }, new int[] { 4, 5, 6 });
 int[] data = { 1, 2, 3 };
 int[] count = { 1, 2, 3 };
 when(readerMock.readLine()).thenReturn("Sample data");
 when(readerMock.readLine()).thenReturn("Sample metadata");
 when(readerMock.readLine()).thenReturn("Range start: 2001");
 when(readerMock.readLine()).thenReturn("Range end: 2003");
 when(readerMock.readLine()).thenReturn("Sample id: 123456");
 when(readerMock.readLine()).thenReturn("Data: 1,2,3");
 when(readerMock.readLine()).thenReturn("Count: 1,2,3");
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 BufferedWriter writerMockWithOutput = mock(BufferedWriter.class);
 when(writerMock.write(Mockito.anyString())).then(i -> writerMockWithOutput.write(i.arguments[0]));
 when(writerMockWithOutput.flush()).thenReturn(true);
 tucsonSimple.save(sample, writerMock);
 String expected = "1234562001\n" +
 "Sample data\n" +
 "Sample metadata\n" +
 "Range start: 2001\n" +
 "Range end: 2003\n" +
 "Data: 1 2 3\n" +
 "Count: 1 2 3\n" +
 "9990  0\n" +
 "9990  0\n" +
 "9990  0\n" +
 "999\n";
 String actual = "";
 String line;
 while ((line = writer.readLine()) != null) {
 actual += line;
 }
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataWhenProcessedIsTrue() throws IOException {
 Year start = new Year(2001);
 Year end = new Year(2003);
 Sample sample = new Sample("testSample", null, new int[] { 1, 2, 3 }, new int[] { 1, 2, 3 });
 int[] data = { 10, 20, 30 };
 int[] count = { 1, 2, 3 };
 when(readerMock.readLine()).thenReturn("Sample data");
 when(readerMock.readLine()).thenReturn("Sample metadata");
 when(readerMock.readLine()).thenReturn("Range start: 2001");
 when(readerMock.readLine()).thenReturn("Range end: 2003");
 when(readerMock.readLine()).thenReturn("Sample id: 123456");
 when(readerMock.readLine()).thenReturn("Data: 10 20 30");
 when(readerMock.readLine()).thenReturn("Count: 1 2 3");
 when(readerMock.readLine()).thenReturn("Processed: true");
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 BufferedWriter writerMockWithOutput = mock(BufferedWriter.class);
 when(writerMock.write(Mockito.anyString())).then(i -> writerMockWithOutput.write(i.arguments[0]));
 when(writerMockWithOutput.flush()).thenReturn(true);
 tucsonSimple.save(sample, writerMock);
 String expected = "1234562001\n" +
 "Sample data\n" +
 "Sample metadata\n" +
 "Range start: 2001\n" +
 "Range end: 2003\n" +
 "Data:    \n" +
 "Count:    \n" +
 "9990  0\n" +
 "9990  0\n" +
 "9990  0\n" +
 "999\n";
 String actual = "";
 String line;
 while ((line = writer.readLine()) != null) {
 actual += line;
 }
 assertEquals(expected, actual);
 }
}