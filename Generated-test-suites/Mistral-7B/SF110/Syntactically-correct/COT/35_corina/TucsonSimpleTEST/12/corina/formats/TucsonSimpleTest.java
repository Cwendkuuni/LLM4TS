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

 private static final String TEST_FILE_CONTENTS = "Your test file contents here";

 @Test
 public void testToString() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 String actual = tucsonSimple.toString();
 String expected = I18n.getText("format.tucsonsimple");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testgetDefaultExtension() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 String actual = tucsonSimple.getDefaultExtension();
 String expected = ".RWL";
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_FILE_CONTENTS.getBytes())));
 tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCodeNullSample() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = null;
 String actual = tucsonSimple.make6digitCode(sample);
 String expected = "000000";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeSampleWithId() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.setMetaData(new HashMap<>());
 sample.meta.put("id", "123456");
 String actual = tucsonSimple.make6digitCode(sample);
 String expected = "123456";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeSampleWithoutId() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 String actual = tucsonSimple.make6digitCode(sample);
 String expected = "000000";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeSampleWithIdLongerThan6Digits() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.setMetaData(new HashMap<>());
 sample.meta.put("id", "1234567890123456");
 String actual = tucsonSimple.make6digitCode(sample);
 String expected = "123456";
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveRowHeaderNullWriter() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 String code = "123456";
 Year year = new Year(2000);
 int yearWidth = 6;
 tucsonSimple.saveRowHeader(null, sample, code, yearWidth, year);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 String code = "123456";
 Year start = new Year(2000);
 Year end = new Year(2001);
 int yearWidth = 6;
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 String expected = "1234562000";
 String actual = writer.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveRowHeaderStartBeforeYear() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 String code = "123456";
 Year start = new Year(1999);
 Year end = new Year(2000);
 int yearWidth = 6;
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 String expected = "1999123456";
 String actual = writer.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataNullWriter() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = null;
 tucsonSimple.saveData(sample, writer);
 }

 @Test
 public void testSaveDataSampleWithNoDataAndNoCount() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.saveData(sample, writer);
 String expected = "";
 String actual = writer.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataSampleWithDataAndNoCount() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.setData(new double[]{-1.0, 0.0, 1.0});
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.saveData(sample, writer);
 String expected = "   -1.0\n  0.0\n   1.0\n";
 String actual = writer.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataSampleWithCount() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.setData(new double[]{1.0, 2.0, 3.0});
 sample.setCount(new int[]{1, 2, 3});
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.saveData(sample, writer);
 String expected = "  1.0\n  2.0\n  3.0\n  1\n  2\n  3\n";
 String actual = writer.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testSaveNullSample() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.save(null, writer);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testSaveEmptySample() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.save(sample, writer);
 }

 @Test
 public void testComputeRangeNullSample() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = null;
 Range actual = tucsonSimple.computeRange(sample);
 Year expectedStart = new Year();
 Year expectedEnd = new Year();
 Assert.assertEquals(expectedStart, actual.getStart());
 Assert.assertEquals(expectedEnd, actual.getEnd());
 }

 @Test
 public void testComputeRangeSampleWithValidStart() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year start = new Year(2000);
 Range actual = tucsonSimple.computeRange(sample);
 Year expectedStart = new Year(2000);
 Year expectedEnd = new Year(2000);
 Assert.assertEquals(expectedStart, actual.getStart());
 Assert.assertEquals(expectedEnd, actual.getEnd());
 }

 @Test
 public void testComputeRangeSampleWithStartBeforeYear1() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year start = new Year(1999);
 Range actual = tucsonSimple.computeRange(sample);
 Year expectedStart = new Year(1001);
 Year expectedEnd = new Year(1001);
 Assert.assertEquals(expectedStart, actual.getStart());
 Assert.assertEquals(expectedEnd, actual.getEnd());
 }

 @Test
 public void testComputeRangeSampleWithStartAfterYear8000() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year start = new Year(8001);
 Range actual = tucsonSimple.computeRange(sample);
 Year expectedStart = new Year(8001);
 Year expectedEnd = new Year(8001);
 Assert.assertEquals(expectedStart, actual.getStart());
 Assert.assertEquals(expectedEnd, actual.getEnd());
 }

 @Test
 public void testComputeRangeSampleWithValidEnd() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year end = new Year(2001);
 Range actual = tucsonSimple.computeRange(sample);
 Year expectedStart = new Year(2000);
 Year expectedEnd = new Year(2001);
 Assert.assertEquals(expectedStart, actual.getStart());
 Assert.assertEquals(expectedEnd, actual.getEnd());
 }

 @Test
 public void testComputeRangeSampleWithEndAfterYear1() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year end = new Year(1);
 Range actual = tucsonSimple.computeRange(sample);
 Year expectedStart = new Year(1001);
 Year expectedEnd = new Year(1001);
 Assert.assertEquals(expectedStart, actual.getStart());
 Assert.assertEquals(expectedEnd, actual.getEnd());
 }

 @Test
 public void testComputeRangeSampleWithEndBeforeStart() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year end = new Year(1999);
 Range actual = tucsonSimple.computeRange(sample);
 Year expectedStart = new Year(1001);
 Year expectedEnd = new Year(1001);
 Assert.assertEquals(expectedStart, actual.getStart());
 Assert.assertEquals(expectedEnd, actual.getEnd());
 }

 @Test
 public void testComputeRangeSampleWithValidRange() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year start = new Year(2000);
 Year end = new Year(2001);
 Range actual = tucsonSimple.computeRange(sample);
 Year expectedStart = new Year(2000);
 Year expectedEnd = new Year(2001);
 Assert.assertEquals(expectedStart, actual.getStart());
 Assert.assertEquals(expectedEnd, actual.getEnd());
 }

 @Test
 public void testSaveDataOutOfRange() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.saveData(sample, writer);
 }

 @Test
 public void testSaveDataProcessedRowBeforeStart() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.saveData(sample, writer, true, true);
 String expected = "";
 String actual = writer.toString();
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataProcessedRowAfterEnd() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Year start = new Year(2000);
 Year end = new Year(2001);
 tucsonSimple.saveData(sample, writer, true, true);
 String expected = "9990   ";
 String actual = writer.toString().substring(0, 11);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataProcessedRowInMiddle() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Year start = new Year(2000);
 Year end = new Year(2001);
 tucsonSimple.saveData(sample, writer, true, true);
 String expected = "9990   ";
 String actual = writer.toString().substring(0, 11);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataUnprocessedRowBeforeStart() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.saveData(sample, writer);
 String expected = "   999";
 String actual = writer.toString().substring(0, 4);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataUnprocessedRowInMiddle() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Year start = new Year(2000);
 Year end = new Year(2001);
 tucsonSimple.saveData(sample, writer);
 String expected = "9990   ";
 String actual = writer.toString().substring(lastIndexOf( writer.toString(), "\n") + 1, lastIndexOf( writer.toString(), "\n") + 11);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataUnprocessedRowAfterEnd() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Year start = new Year(2000);
 Year end = new Year(2001);
 tucsonSimple.saveData(sample, writer);
 String expected = "";
 String actual = writer.toString().substring(lastIndexOf( writer.toString(), "\n") + 1);
 Assert.assertEquals(expected, actual);
 }

 private static int lastIndexOf(String str, String sub) {
 int index = -1;
 int len = str.length();
 for (int i = 0; i < len; i++) {
 if (str.regionMatches(i, sub, 0, sub.length())) {
 index = i;
 break;
 }
 }
 return index;
 }
}