package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.time.Year;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import corina.Sample;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader readerMock;
 private BufferedWriter writerMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
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
 public void testLoadWithWrongFiletypeException() throws IOException {
 when(readerMock.readLine()).thenReturn("invalid data");
 tucsonSimple.load(readerMock);
 verify(readerMock).close();
 }

 @Test
 public void testMake6digitCodeWithSample() {
 Sample s = new Sample();
 s.meta.put("id", "123456");
 String code = tucsonSimple.make6digitCode(s);
 assertEquals("123456", code);

 s = new Sample();
 assertEquals("000000", tucsonSimple.make6digitCode(s));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 BufferedWriter writer = new BufferedWriter(new FileWriter("tempFile.rwl"));
 writerMock = writer;
 Year year = new Year(1995);
 Sample s = new Sample();
 s.range = new Range(new Year(1995), new Year(1995));
 String code = "123456";
 tucsonSimple.saveRowHeader(writerMock, s, code, 4, year);
 assertEquals("1234561995", writer.getBuffer().toString());
 }

 @Test
 public void testSaveDataWithValidData() throws IOException {
 BufferedWriter writer = new BufferedWriter(new FileWriter("tempFile.rwl"));
 writerMock = writer;
 Sample s = new Sample();
 s.range = new Range(new Year(1990), new Year(1995));
 String[] data = {"1", "2", "3", "4"};
 int[] counts = {1, 2, 3, 4};
 s.data = data;
 s.count = counts;
 String code = "123456";
 tucsonSimple.saveData(s, writerMock);
 String expectedOutput = "1234561990  1\n" +
                        "1234561991  2\n" +
                        "1234561992  3\n" +
                        "1234561993  4\n" +
                        "   999\n";
 assertEquals(expectedOutput, writer.getBuffer().toString());
 }

 @Test
 public void testSaveDataWithEdgeCases() throws IOException {
 BufferedWriter writer = new BufferedWriter(new FileWriter("tempFile.rwl"));
 writerMock = writer;
 Sample s = new Sample();
 s.range = new Range(new Year(1995), new Year(1995));
 String code = "123456";
 tucsonSimple.saveData(s, writerMock);
 assertEquals("", writer.getBuffer().toString());

 s = new Sample();
 s.range = new Range(new Year(1995), new Year(1995));
 code = "000001";
 tucsonSimple.saveData(s, writerMock);
 assertEquals("0000011995\n", writer.getBuffer().toString());
 }
}