import org.junit.Assert;
import org.junit.Test;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple = new TucsonSimple();

 @Test
 public void testToString() {
 String expected = I18n.getText("format.tucsonsimple");
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
 public void testLoadThrowsException() throws IOException {
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
 Sample sample = tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCodeWithSampleId() {
 Sample sample = new Sample();
 sample.meta.put("id", "123456");
 String expected = "123456";
 String actual = tucsonSimple.make6digitCode(sample);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeWithoutSampleId() {
 Sample sample = new Sample();
 String expected = "000000";
 String actual = tucsonSimple.make6digitCode(sample);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeWithLongSampleId() {
 Sample sample = new Sample();
 sample.meta.put("id", "123456789012");
 String expected = "123456";
 String actual = tucsonSimple.make6digitCode(sample);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 tucsonSimple.saveRowHeader(writer, new Sample(), "code", 6, new Year(2000));
 String expected = "code2000";
 String actual = outputStream.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveRowHeaderWithNegativeYear() throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 tucsonSimple.saveRowHeader(writer, new Sample(), "code", 6, new Year(1999));
 String expected = "1999code";
 String actual = outputStream.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataWithIsProcessedTrue() throws IOException {
 Sample sample = new Sample();
 Range range = new Range(new Year(2001), new Year(2005));
 sample.range = range;
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 tucsonSimple.saveData(sample, writer);
 String expected = "   9990  0\n" +
         "0002  2\n" +
         "0003  3\n" +
         "0004  4\n" +
         "0005  5\n" +
         "9990   ";
 String actual = outputStream.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataWithIsProcessedFalse() throws IOException {
 Sample sample = new Sample();
 Range range = new Range(new Year(2001), new Year(2005));
 sample.range = range;
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 tucsonSimple.saveData(sample, writer, false);
 String expected = "0002  2\n" +
         "0003  3\n" +
         "0004  4\n" +
         "0005  5\n";
 String actual = outputStream.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataWithIsSummedTrue() throws IOException {
 Sample sample = new Sample();
 Range range = new Range(new Year(2001), new Year(2005));
 sample.range = range;
 Map<Integer, Double> data = new HashMap<>();
 data.put(1, 10.0);
 data.put(2, 20.0);
 data.put(3, 30.0);
 data.put(4, 40.0);
 data.put(5, 50.0);
 sample.data = data;
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 tucsonSimple.saveData(sample, writer, true);
 String expected = "0002  2\n" +
         "0003  3\n" +
         "0004  4\n" +
         "0005  5\n" +
         "10.000  110.0\n";
 String actual = outputStream.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = IOException.class)
 public void testSaveDataWithInvalidYear() throws IOException {
 Sample sample = new Sample();
 Range range = new Range(new Year(2001), new Year(2005));
 sample.range = range;
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 tucsonSimple.saveData(sample, writer, false, new Year(-1));
 }

 @Test
 public void testComputeRangeWithValidStartYear() {
 Sample sample = new Sample();
 Range expected = new Range(new Year(1001), new Year(1006));
 Range actual = tucsonSimple.computeRange(sample);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testComputeRangeWithStartYearBefore1() {
 Sample sample = new Sample();
 Range expected = new Range(new Year(1), new Year(8001));
 Range actual = tucsonSimple.computeRange(sample);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testComputeRangeWithStartYearGreaterThan8000() {
 Sample sample = new Sample();
 Range expected = new Range(new Year(10001), new Year(10006));
 Range actual = tucsonSimple.computeRange(sample);
 Assert.assertEquals(expected, actual);
 }
}