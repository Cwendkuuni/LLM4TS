import org.junit.Test;
import org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple = new TucsonSimple();

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
 public void testLoadThrowsWrongFiletypeException() throws IOException {
     BufferedReader reader = new BufferedReader(new StringReader(""));
     tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCodeWithID() {
     Sample sample = new Sample();
     sample.meta.put("id", "123456");
     String expected = "123456";
     String actual = tucsonSimple.make6digitCode(sample);
     assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeWithoutID() {
     Sample sample = new Sample();
     String expected = "000000";
     String actual = tucsonSimple.make6digitCode(sample);
     assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeWithIDLongerThan6Digits() {
     Sample sample = new Sample();
     sample.meta.put("id", "1234567890");
     String expected = "123456";
     String actual = tucsonSimple.make6digitCode(sample);
     assertEquals(expected, actual);
 }

 @Test
 public void testSaveRowHeaderWithStartYearBefore() throws IOException {
     StringWriter stringWriter = new StringWriter();
     BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
     Sample sample = new Sample();
     Year start = new Year(1999);
     String code = "abcdef";
     int yearWidth = 6;
     tucsonSimple.saveRowHeader(bufferedWriter, sample, code, yearWidth, start);
     String expected = "abcdef1999";
     String actual = stringWriter.toString();
     assertEquals(expected, actual);
 }

 @Test
 public void testSaveRowHeaderWithStartYearSameAs() throws IOException {
     StringWriter stringWriter = new StringWriter();
     BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
     Sample sample = new Sample();
     Year start = new Year(2005);
     String code = "abcdef";
     int yearWidth = 6;
     tucsonSimple.saveRowHeader(bufferedWriter, sample, code, yearWidth, start);
     String expected = "abcdef2005";
     String actual = stringWriter.toString();
     assertEquals(expected, actual);
 }

 @Test
 public void testSaveRowHeaderWithStartYearLater() throws IOException {
     StringWriter stringWriter = new StringWriter();
     BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
     Sample sample = new Sample();
     Year start = new Year(2006);
     Year end = new Year(2007);
     String code = "abcdef";
     int yearWidth = 6;
     tucsonSimple.saveData(sample, bufferedWriter);
     String expected = "abcdef2006\n";
     String actual = stringWriter.toString();
     assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataWithIsProcessedFalse() throws IOException {
     StringWriter stringWriter = new StringWriter();
     BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
     Sample sample = new Sample();
     Year start = new Year(2006);
     Year end = new Year(2007);
     String code = "abcdef";
     int yearWidth = 6;
     tucsonSimple.saveData(sample, bufferedWriter);
     String expected = "abcdef2006\n" +
             "9990  0\n" +
             "9990   " +
             "9990   ";
     String actual = stringWriter.toString();
     assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataWithIsProcessedTrue() throws IOException {
     StringWriter stringWriter = new StringWriter();
     BufferedWriter bufferedWriter = new BufferedWriter(stringWriter);
     Sample sample = new Sample();
     Range range = new Range(new Year(2006), new Year(2007));
     sample.range = range;
     Year start = new Year(2006);
     Year end = new Year(2010);
     String code = "abcdef";
     int yearWidth = 4;
     tucsonSimple.saveData(sample, bufferedWriter);
     String expected = "abcdef2006\n" +
             "9990  0\n" +
             "9990  0\n" +
             "9990   " +
             "9990   \n" +
             "9990   " +
             "9990   \n" +
             "9990   " +
             "9990    ";
     String actual = stringWriter.toString();
     assertEquals(expected, actual);
 }

 @Test
 public void testComputeRangeWithStartYearBeforeYear1() {
     Sample sample = new Sample();
     Year start = new Year(1999);
     Range expected = new Range(new Year(1999), new Year(1999));
     Range actual = tucsonSimple.computeRange(sample);
     assertEquals(expected, actual);
 }

 @Test
 public void testComputeRangeWithStartYearBeforeYear1001() {
     Sample sample = new Sample();
     Year start = new Year(1000);
     Range expected = new Range(new Year(1001), new Year(1001));
     Range actual = tucsonSimple.computeRange(sample);
     assertEquals(expected, actual);
 }

 @Test
 public void testComputeRangeWithStartYearBetweenYear1And8000() {
     Sample sample = new Sample();
     Year start = new Year(3000);
     Range expected = new Range(new Year(3001), new Year(3001));
     Range actual = tucsonSimple.computeRange(sample);
     assertEquals(expected, actual);
 }

 @Test
 public void testComputeRangeWithStartYearAfterYear8000() {
     Sample sample = new Sample();
     Year start = new Year(8001);
     Range expected = new Range(new Year(8002), new Year(8002));
     Range actual = tucsonSimple.computeRange(sample);
     assertEquals(expected, actual);
 }

 @Test(expected = IllegalStateException.class)
 public void testSaveThrowsIllegalStateExceptionWhenStartYearIsAfterEndYear() throws IOException {
     Sample sample = new Sample();
     Year start = new Year(2007);
     Year end = new Year(2006);
     BufferedWriter bufferedWriter = new BufferedWriter(new StringWriter());
     tucsonSimple.save(sample, bufferedWriter);
 }

 @Test
 public void testSaveWithIsProcessedFalse() throws IOException {
     Sample sample = new Sample();
     Year start = new Year(2006);
     Year end = new Year(2007);
     BufferedWriter bufferedWriter = new BufferedWriter(new StringWriter());
     tucsonSimple.save(sample, bufferedWriter);
     String expected = "abcdef2006\n" +
             "9990  0\n" +
             "9990   ";
     String actual = new StringWriter().toString();
     StringWriter stringWriter = new StringWriter();
     BufferedWriter bufferedWriter2 = new BufferedWriter(stringWriter);
     tucsonSimple.save(sample, bufferedWriter2);
     assertEquals(expected, actual);
 }

 @Test
 public void testSaveWithIsProcessedTrue() throws IOException {
     Sample sample = new Sample();
     Range range = new Range(new Year(2006), new Year(2007));
     sample.range = range;
     Year start = new Year(2006);
     Year end = new Year(2010);
     BufferedWriter bufferedWriter = new BufferedWriter(new StringWriter());
     tucsonSimple.save(sample, bufferedWriter);
     String expected = "abcdef2006\n" +
             "9990  0\n" +
             "9990  0\n" +
             "9990   " +
             "9990   \n" +
             "9990   " +
             "9990   \n" +
             "9990   " +
             "9990    ";
     String actual = new StringWriter().toString();
     StringWriter stringWriter = new StringWriter();
     BufferedWriter bufferedWriter2 = new BufferedWriter(stringWriter);
     tucsonSimple.save(sample, bufferedWriter2);
     assertEquals(expected, actual);
 }
}