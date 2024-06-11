import org.junit.Test;
import static org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple = new TucsonSimple();

 @Test
 public void testToString() {
 String expected = "format.tucsonsimple";
 assertEquals(expected, tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 String expected = ".RWL";
 assertEquals(expected, tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
 BufferedReader bufferedReader = new BufferedReader(new FileReader("testFile.rwl"));
 Sample sample = tucsonSimple.load(bufferedReader);
 fail("Load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCodeWithId() {
 Sample sample = new Sample();
 sample.meta.put("id", "123456");
 String expected = "123456";
 assertEquals(expected, tucsonSimple.make6digitCode(sample));
 }

 @Test
 public void testMake6digitCodeWithoutId() {
 Sample sample = new Sample();
 String expected = "000000";
 assertEquals(expected, tucsonSimple.make6digitCode(sample));
 }

 @Test
 public void testSaveRowHeaderWithStartYearBigger() throws IOException {
 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("testFile.rwl"));
 Sample sample = new Sample();
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(2000);
 Year end = new Year(2001);
 tucsonSimple.saveRowHeader(bufferedWriter, sample, code, yearWidth, start);
 String expected = "1234562000";
 assertEquals(expected, bufferedWriter.getBuffer().toString());
 }

 @Test
 public void testSaveRowHeaderWithStartYearSmaller() throws IOException {
 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("testFile.rwl"));
 Sample sample = new Sample();
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(1999);
 Year end = new Year(2000);
 tucsonSimple.saveRowHeader(bufferedWriter, sample, code, yearWidth, start);
 String expected = "1999123456";
 assertEquals(expected, bufferedWriter.getBuffer().toString());
 }

 @Test
 public void testSaveDataWithProcessedFlagTrue() throws IOException {
 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("testFile.rwl"));
 Sample sample = new Sample();
 String code = "123456";
 Year start = new Year(1999);
 Year end = new Year(2000);
 tucsonSimple.saveData(sample, bufferedWriter);
 String expected = "1999   9990  0\n";
 assertEquals(expected, bufferedWriter.getBuffer().toString());
 }

 @Test
 public void testSaveDataWithProcessedFlagFalse() throws IOException {
 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("testFile.rwl"));
 Sample sample = new Sample();
 String code = "123456";
 Year start = new Year(1999);
 Year end = new Year(2000);
 tucsonSimple.saveData(sample, bufferedWriter);
 String expected = "1999  123456   \n";
 assertEquals(expected, bufferedWriter.getBuffer().toString());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testSaveWithNullSample() throws IOException {
 Sample sample = null;
 BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("testFile.rwl"));
 tucsonSimple.save(sample, bufferedWriter);
 fail("Save method should throw WrongFiletypeException");
 }

 @Test(expected = IOException.class)
 public void testSaveWithNullWriter() throws IOException {
 BufferedWriter bufferedWriter = null;
 Sample sample = new Sample();
 tucsonSimple.save(sample, bufferedWriter);
 fail("Save method should throw IOException");
 }

 @Test(expected = WrongFiletypeException.class)
 public void testComputeRangeWithInvalidStartYear() {
 Sample sample = new Sample();
 Year start = new Year(-1);
 Range expectedRange = new Range(new Year(1001), new Year(1001));
 Range computedRange = tucsonSimple.computeRange(sample);
 assertEquals(expectedRange, computedRange);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testComputeRangeWithStartYearBiggerThan1() {
 Sample sample = new Sample();
 Year start = new Year(1);
 Range expectedRange = new Range(new Year(1), new Year(1));
 Range computedRange = tucsonSimple.computeRange(sample);
 assertEquals(expectedRange, computedRange);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testComputeRangeWithStartYearGreaterThan8000() {
 Sample sample = new Sample();
 Year start = new Year(8001);
 Range expectedRange = new Range(new Year(1001), new Year(1001));
 Range computedRange = tucsonSimple.computeRange(sample);
 assertEquals(expectedRange, computedRange);
 }

 @Test
 public void testComputeRangeWithValidStartYear() {
 Sample sample = new Sample();
 Year start = new Year(1999);
 Range expectedRange = new Range(new Year(1999), new Year(2000));
 Range computedRange = tucsonSimple.computeRange(sample);
 assertEquals(expectedRange, computedRange);
 }

 @Test
 public void testComputeRangeWithStartYearLessThan1001() {
 Sample sample = new Sample();
 Year start = new Year(1000);
 Range expectedRange = new Range(new Year(1001), new Year(1001));
 Range computedRange = tucsonSimple.computeRange(sample);
 assertEquals(expectedRange, computedRange);
 }
}