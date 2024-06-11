import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedWriter mockWriter;
 private Sample sample;

 @Before
 public void setup() {
 tucsonSimple = new TucsonSimple();
 mockWriter = Mockito.mock(BufferedWriter.class);
 sample = new Sample() {{
 meta = new HashMap<>();
 meta.put("id", "123");
 data = new double[] {1.1, 2.2, 3.3};
 count = new int[] {1, 2, 3};
 range = new Range(new Year(2000), new Year(2002));
 }};
 }

 @Test
 public void testToString() {
 String expected = "format.tucsonsimple";
 String actual = tucsonSimple.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testgetDefaultExtension() {
 String expected = ".RWL";
 String actual = tucsonSimple.getDefaultExtension();
 assertEquals(expected, actual);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadFails() throws IOException {
 BufferedReader mockReader = Mockito.mock(BufferedReader.class);
 tucsonSimple.load(mockReader);
 }

 @Test
 public void testMake6digitCodeWithId() {
 String expected = "123";
 String actual = tucsonSimple.make6digitCode(sample);
 assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeWithoutId() {
 String expected = "000001";
 String actual = tucsonSimple.make6digitCode(new Sample());
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveData() throws IOException {
 StringWriter stringWriter = new StringWriter();
 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> yearWidthCaptor = ArgumentCaptor.forClass(Integer.class);
 ArgumentCaptor<Year> yearCaptor = ArgumentCaptor.forClass(Year.class);
 tucsonSimple.saveData(sample, mockWriter);
 verify(mockWriter).write(codeCaptor.capture());
 verify(mockWriter).write(Mockito.eq(" "), Mockito.anyInt());
 verify(mockWriter).write(codeCaptor.capture());
 verify(mockWriter).write(yearWidthCaptor.capture());
 verify(mockWriter).write(yearCaptor.capture());
 String actualCode = codeCaptor.getValue();
 int yearWidth = yearWidthCaptor.getValue();
 Year actualYear = yearCaptor.getValue();
 String expectedCode = "123";
 String expectedPrefix = "2000";
 String expectedCodeWithPrefix = "1232000";
 assertEquals(expectedCode, actualCode.substring(0, 6));
 assertEquals(expectedPrefix, StringUtils.leftPad(actualYear.toString(), yearWidth));
 assertEquals(expectedCodeWithPrefix, codeCaptor.getValue());
 }

 @Test
 public void testSaveDataWithProcessed() throws IOException {
 sample.setProcessed(true);
 StringWriter stringWriter = new StringWriter();
 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> yearWidthCaptor = ArgumentCaptor.forClass(Integer.class);
 ArgumentCaptor<Year> yearCaptor = ArgumentCaptor.forClass(Year.class);
 tucsonSimple.saveData(sample, mockWriter);
 verify(mockWriter).write(codeCaptor.capture());
 verify(mockWriter).write(Mockito.eq(" "), Mockito.anyInt());
 verify(mockWriter).write(codeCaptor.capture());
 verify(mockWriter).write(yearWidthCaptor.capture());
 verify(mockWriter).write(yearCaptor.capture());
 String actualCode = codeCaptor.getValue();
 int yearWidth = yearWidthCaptor.getValue();
 Year actualYear = yearCaptor.getValue();
 String expectedCode = "123";
 String expectedPrefix = "2000";
 String expectedCodeWithPrefix = "1232000";
 String expectedData = "1.1";
 String expectedSummedData = "1.100";
 assertEquals(expectedCode, actualCode.substring(0, 6));
 assertEquals(expectedPrefix, StringUtils.leftPad(actualYear.toString(), yearWidth));
 assertEquals(expectedCodeWithPrefix, codeCaptor.getValue());
 assertEquals(expectedData, sample.data[0] > 0 ? sample.data[0].toString() : "");
 assertEquals(expectedSummedData, sample.count[0] > 0 ? StringUtils.leftPad(sample.count[0].toString(), 3) : "");
 }

 @Test
 public void testSaveDataWithOutOfRangeYear() throws IOException {
 sample.setRange(new Range(new Year(1999), new Year(2000)));
 StringWriter stringWriter = new StringWriter();
 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> yearWidthCaptor = ArgumentCaptor.forClass(Integer.class);
 ArgumentCaptor<Year> yearCaptor = ArgumentCaptor.forClass(Year.class);
 tucsonSimple.saveData(sample, mockWriter);
 verify(mockWriter).write(codeCaptor.capture());
 verify(mockWriter).write(Mockito.eq(" "), Mockito.anyInt());
 verify(mockWriter).write(codeCaptor.capture());
 verify(mockWriter).write(yearWidthCaptor.capture());
 verify(mockWriter).write(yearCaptor.capture());
 String actualCode = codeCaptor.getValue();
 int yearWidth = yearWidthCaptor.getValue();
 Year actualYear = yearCaptor.getValue();
 String expectedCode = "123";
 String expectedPrefix = "1999";
 String expectedCodeWithPrefix = "1231999";
 assertEquals(expectedCode, actualCode.substring(0, 6));
 assertEquals(expectedPrefix, StringUtils.leftPad(actualYear.toString(), yearWidth));
 assertEquals(expectedCodeWithPrefix, codeCaptor.getValue());
 }

 @Test
 public void testSaveDataWithNegativeYear() throws IOException {
 sample.setRange(new Range(new Year(-1), new Year(2000)));
 StringWriter stringWriter = new StringWriter();
 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> yearWidthCaptor = ArgumentCaptor.forClass(Integer.class);
 ArgumentCaptor<Year> yearCaptor = ArgumentCaptor.forClass(Year.class);
 tucsonSimple.saveData(sample, mockWriter);
 verify(mockWriter).write(codeCaptor.capture());
 verify(mockWriter).write(Mockito.eq(" "), Mockito.anyInt());
 verify(mockWriter).write(codeCaptor.capture());
 verify(mockWriter).write(yearWidthCaptor.capture());
 verify(mockWriter).write(yearCaptor.capture());
 String actualCode = codeCaptor.getValue();
 int yearWidth = yearWidthCaptor.getValue();
 Year actualYear = yearCaptor.getValue();
 String expectedCode = "123";
 String expectedPrefix = "";
 String expectedCodeWithPrefix = "123";
 assertEquals(expectedCode, actualCode.substring(0, 6));
 assertEquals(expectedPrefix, "");
 assertEquals(expectedCodeWithPrefix, codeCaptor.getValue());
 }

 @Test
 public void testSaveDataWithYearBeyondEnd() throws IOException {
 sample.setRange(new Range(new Year(2000), new Year(2001)));
 StringWriter stringWriter = new StringWriter();
 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> yearWidthCaptor = ArgumentCaptor.forClass(Integer.class);
 ArgumentCaptor<Year> yearCaptor = ArgumentCaptor.forClass(Year.class);
 tucsonSimple.saveData(sample, mockWriter);
 verify(mockWriter).write("999");
 }

 @Test
 public void testSave() throws IOException {
 BufferedWriter mockWriter = Mockito.mock(BufferedWriter.class);
 StringReader stringReader = new StringReader(getResource("sample.txt").toString());
 BufferedReader mockBufferedReader = new BufferedReader(stringReader);
 tucsonSimple.save(sample, mockWriter);
 StringWriter stringWriter = new StringWriter();
 BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 ArgumentCaptor<Sample> sampleCaptor = ArgumentCaptor.forClass(Sample.class);
 ArgumentCaptor<BufferedWriter> writerCaptor = ArgumentCaptor.forClass(BufferedWriter.class);
 tucsonSimple.save(sample, bufferedWriter);
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferrerReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedReader).readLine();
 verify(mockBufferedWriter).write(sampleCaptor.capture());
 String actualSample = sampleCaptor.getValue();
 assertEquals(sample, actualSample);
 verify(mockWriter).flush();
 verify(mockWriter).close();
 }

 @Test
 public void testSaveWithInvalidFile() throws IOException {
 BufferedWriter mockWriter = Mockito.mock(BufferedWriter.class);
 Sample mockSample = Mockito.mock(Sample.class);
 tucsonSimple.save(mockSample, mockWriter);
 verify(mockWriter).write("This is not a valid file.");
 }
}