import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockedStatic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedWriter writerMock;
 private BufferedReader readerMock;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 writerMock = Mockito.mock(BufferedWriter.class);
 readerMock = Mockito.mock(BufferedReader.class);
 }

 @Test
 public void testToString() {
 String expected = "format.tucsonsimple";
 String actual = tucsonSimple.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testgetDefaultExtension() {
 String expected = ".RWL";
 String actual = tucsonSimple.getDefaultExtension();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadException() throws IOException {
 BufferedReader input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("File content".getBytes())));
 tucionSimple.load(input);
 }

 @Test
 public void testMake6digitCode() throws IOException {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 String expected = "123456";
 String actual = tucsonSimple.make6digitCode(sample);
 Assert.assertEquals(expected, actual);

 Sample sample2 = new Sample();
 String code = tucsonSimple.make6digitCode(sample2);
 Assert.assertEquals("000000", code);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Sample sample = new Sample();
 String code = "A12345";
 int yearWidth = 4;
 Year y = new Year(2000);
 BufferedWriter output = Mockito.mock(BufferedWriter.class);
 tucsonSimple.saveRowHeader(output, sample, code, yearWidth, y);
 String expected = "A123452000";
 String actual = "";
 with(mockStatic(System.out::println))
 .callRealMock()
 .when(() -> System.out.println(anyString()))
 .thenAnswer(invocation -> actual = invocation.getValue(0).toString());
 tucsonSimple.saveData(sample, output);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveData() throws IOException {
 Sample sample = new Sample();
 Range range = new Range(new Year(2000), new Year(2002));
 sample.range = range;
 Year start = new Year(2000);
 Year end = new Year(2002);
 String code = "A12345";
 boolean isSummed = false;
 boolean isProcessed = false;
 BufferedWriter output = writerMock;
 Year y = start;
 when(output.write(anyString())).thenAnswer(invocation -> {
 if (invocation.getArgument(0).toString().equalsIgnoreCase("999")) {
 return 3;
 }
 return 1;
 });
 tucsonSimple.saveData(sample, output);
 String expected = "A123452000\n" +
 "   999\n" +
 "9990   ";
 String actual = "";
 with(mockStatic(System.out::println))
 .callRealMock()
 .when(() -> System.out.println(anyString()))
 .thenAnswer(invocation -> actual = invocation.getValue(0).toString());
 tucsonSimple.saveData(sample, writerMock);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testComputeRange() {
 Sample sample = new Sample();
 Range expected = new Range(new Year(1001), new Year(1005));
 Range actual = tucsonSimple.computeRange(sample);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSave() throws IOException {
 Sample sample = new Sample();
 Range range = new Range(new Year(2000), new Year(2002));
 sample.range = range;
 String code = "A12345";
 BufferedWriter output = Mockito.mock(BufferedWriter.class);
 tucsonSimple.save(sample, output);
 String expected = "A123452000\n" +
 "   999\n" +
 "9990   ";
 String actual = "";
 with(mockStatic(System.out::println))
 .callRealMock()
 .when(() -> System.out.println(anyString()))
 .thenAnswer(invocation -> actual = invocation.getValue(0).toString());
 tucsonSimple.save(sample, writerMock);
 Assert.assertEquals(expected, actual);
 }
}