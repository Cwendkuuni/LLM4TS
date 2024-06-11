import org.junit.Test;
import static org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple = new TucsonSimple();
 private StringWriter stringWriter = new StringWriter();
 private BufferedWriter bufferedWriter;

 @Test
 public void testToString() {
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testDefaultExtension() {
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadWithWrongFiletypeException() throws IOException {
 String testData = "This is not a TucsonSimple file.";
 BufferedReader reader = new BufferedReader(new StringReader(testData));
 tucsonSimple.load(reader);
 fail();
 }

 @Test
 public void testMake6digitCode() throws IOException {
 Sample sample = new Sample();
 sample.setMeta("id", "123456");
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);

 Sample sample2 = new Sample();
 String code2 = tucsonSimple.make6digitCode(sample2);
 assertEquals("000000", code2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 bufferedWriter = new BufferedWriter(new BufferedWriter(new OutputStreamWriter(stringWriter)));
 Sample sample = new Sample();
 sample.setMeta("id", "123456");
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(2000);
 tucsonSimple.saveRowHeader(bufferedWriter, sample, code, yearWidth, start);
 assertEquals("123456 2000", stringWriter.toString());
 }

 @Test
 public void testSaveDataWithNoProcessed() throws IOException {
 Sample sample = new Sample();
 sample.setData(new double[] {1.2, 3.4, 5.6, 7.8, 9.0, 11.1, 13.2, 15.3});
 sample.setCount(new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 Range range = new Range(new Year(2001), new Year(2002));
 Sample expectedSample = new Sample();
 expectedSample.setData(new double[] {1.2, 3.4, 5.6, 7.8, 9.0, 11.1, 13.2, 15.3});
 expectedSample.setCount(new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 expectedSample.setMeta("id", "000001");
 expectedSample.setRange(range);
 tucsonSimple.saveData(expectedSample, bufferedWriter);
 String result = stringWriter.toString();
 String expectedResult = "000001 2001\n" +
 "1.2  1.2\n" +
 "3.4  3.4\n" +
 "5.6  5.6\n" +
 "7.8  7.8\n" +
 "9.0  9.0\n" +
 "11.1 11.1\n" +
 "13.2 13.2\n" +
 "15.3 15.3\n" +
 "\n";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSaveDataWithProcessed() throws IOException {
 Sample sample = new Sample();
 Sample processedSample = new Sample();
 processedSample.setData(new double[] {1.2, 3.4, 5.6, 7.8, 9.0, 11.1, 13.2, 15.3});
 processedSample.setCount(new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 Range range = new Range(new Year(2001), new Year(2002));
 Sample expectedSample = new Sample();
 expectedSample.setData(new double[] {1.2, 3.4, 5.6, 7.8, 9.0, 11.1, 13.2, 15.3});
 expectedSample.setCount(new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 expectedSample.setMeta("id", "000001");
 expectedSample.setRange(range);
 expectedSample.setProcessed(true);
 tucsonSimple.saveData(processedSample, bufferedWriter);
 String result = stringWriter.toString();
 String expectedResult = "000001 2001\n" +
 "   999\n" +
 "9990  0\n";
 assertEquals(expectedResult, result);
 }

 @Test
 public void testComputeRange() throws IOException {
 Sample sample = new Sample();
 Range range = new Range(new Year(1995), new Year(1996));
 Sample expectedSample = new Sample();
 expectedSample.setRange(range);
 Sample resultSample = tucsonSimple.computeRange(sample);
 assertEquals(expectedSample, resultSample);
 }

 @Test
 public void testSave() throws IOException {
 Sample sample = new Sample();
 sample.setData(new double[] {1.2, 3.4, 5.6, 7.8, 9.0, 11.1, 13.2, 15.3});
 sample.setCount(new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 Range range = new Range(new Year(2001), new Year(2002));
 sample.setMeta("id", "000001");
 sample.setProcessed(false);
 String expectedData = "000001 2001\n" +
 "1.2  1.2\n" +
 "3.4  3.4\n" +
 "5.6  5.6\n" +
 "7.8  7.8\n" +
 "9.0  9.0\n" +
 "11.1 11.1\n" +
 "13.2 13.2\n" +
 "15.3 15.3\n" +
 "\n";
 tucsonSimple.save(sample, bufferedWriter);
 String result = stringWriter.toString();
 assertEquals(expectedData, result);
 }

 @Test
 public void testSaveWithProcessed() throws IOException {
 Sample sample = new Sample();
 Sample processedSample = new Sample();
 processedSample.setData(new double[] {1.2, 3.4, 5.6, 7.8, 9.0, 11.1, 13.2, 15.3});
 processedSample.setCount(new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 Range range = new Range(new Year(2001), new Year(2002));
 sample.setData(new double[] {1.2, 3.4, 5.6, 7.8, 9.0, 11.1, 13.2, 15.3});
 sample.setCount(new int[] {1, 2, 3, 4, 5, 6, 7, 8});
 sample.setMeta("id", "000001");
 sample.setRange(range);
 sample.setProcessed(true);
 String expectedData = "000001 2001\n" +
 "   999\n" +
 "9990  0\n";
 tucsonSimple.save(sample, bufferedWriter);
 String result = stringWriter.toString();
 assertEquals(expectedData, result);
 }
}