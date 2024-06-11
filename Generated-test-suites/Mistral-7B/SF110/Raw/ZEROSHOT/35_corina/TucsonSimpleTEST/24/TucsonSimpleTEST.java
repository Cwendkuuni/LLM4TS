import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader readerMock;
 private BufferedWriter writerMock;
 private PrintWriter printWriterMock;
 private ByteArrayOutputStream outputStreamMock;

 @Before
 public void setup() {
 tucsonSimple = new TucsonSimple();
 readerMock = mock(BufferedReader.class);
 writerMock = mock(BufferedWriter.class);
 printWriterMock = mock(PrintWriter.class);
 outputStreamMock = new ByteArrayOutputStream();
 when(writerMock.write(String.class)).then(i -> printWriterMock.print(i.getValue()));
 when(writerMock.newLine()).then(() -> printWriterMock.println());
 when(readerMock.readLine()).thenAnswer(invocation -> null);
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
 public void testLoadThrowsWrongFiletypeException() throws IOException {
 tucsonSimple.load(readerMock);
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.setMeta(new HashMap<>());
 sample.getMeta().put("id", "123456");
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);

 Sample sample2 = new Sample();
 String code2 = tucsonSimple.make6digitCode(sample2);
 assertEquals("000000", code2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Sample sample = new Sample();
 Year y = new Year(1990);
 String code = "000123";
 int yearWidth = 6;
 tucsonSimple.saveRowHeader(writerMock, sample, code, yearWidth, y);

 String expected = "0001231990";
 assertEquals(expected, outputStreamMock.toString());
 }

 @Test
 public void testSaveData() throws IOException {
 Sample sample = new Sample();
 Year start = new Year(1990);
 Year end = new Year(1992);
 String code = "000123";
 boolean isSummed = false;
 boolean isProcessed = false;
 Year y = start;
 when(readerMock.readLine()).thenReturn("10 12 15");
 when(readerMock.readLine()).thenReturn("20 18 12");
 when(readerMock.readLine()).thenReturn("30 19 18");
 when(readerMock.readLine()).thenReturn(null);

 tucsonSimple.save(sample, writerMock);

 String expected = "0001231990  10 12 15\n" +
 "0001231991  20 18 12\n" +
 "0001231992  30 19 18\n" +
 "9990   ";
 assertEquals(expected, outputStreamMock.toString());
 }

 @Test
 public void testComputeRange() {
 Sample sample = new Sample();
 Year start = new Year(1985);
 Range range = new Range(start, new Year(1987));
 Range expectedRange = new Range(new Year(1986), new Year(1987));
 Range computedRange = tucsonSimple.computeRange(sample);
 assertEquals(expectedRange, computedRange);
 }

 @Test
 public void testSave() throws IOException {
 Sample sample = new Sample();
 Year start = new Year(1985);
 Year end = new Year(1987);
 String code = "000123";
 tucsonSimple.save(sample, writerMock);

 String expected = "0001231986  10 12 15\n" +
 "0001231987  20 18 12\n" +
 "0001231987  30 19 18\n" +
 "9990   ";
 assertEquals(expected, outputStreamMock.toString());
 }
}