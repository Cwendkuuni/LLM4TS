package corina.formats.test;

import static org.junit.Assert.*;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.ui.*;
import java.io.*;
import static corina.util.StringUtils.leftPad;
import corina.*;

public class TucsonSimpleTest {

 private static final String TEST_DATA = "sample_data.txt"; // Replace with the path to your test data file

 @Test
 public void testToString() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadThrowsWrongFiletypeException() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new FileReader(TEST_DATA));
 tucsonSimple.load(reader); // This method is expected to throw WrongFiletypeException
 }

 @Test
 public void testMake6digitCodeWithMetaId() {
 Sample sample = new Sample();
 sample.meta.put("id", "123456");
 TucsonSimple tucsonSimple = new TucsonSimple();
 String expected = "123456";
 String actual = tucsonSimple.make6digitCode(sample);
 assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeWithoutMetaId() {
 Sample sample = new Sample();
 TucsonSimple tucsonSimple = new TucsonSimple();
 String expected = "000000";
 String actual = tucsonSimple.make6digitCode(sample);
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveRowHeaderWithStartYearLaterThanSampleYear() throws IOException {
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_DATA));
 Sample sample = new Sample();
 TucsonSimple tucsonSimple = new TucsonSimple();
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(2022);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 String expected = "1234562022";
 String actual = writer.toString();
 assertEquals(expected, actual.substring(0, expected.length() + yearWidth));
 writer.close();
 }

 @Test
 public void testSaveRowHeaderWithStartYearEarlierThanSampleYear() throws IOException {
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_DATA));
 Sample sample = new Sample();
 TucsonSimple tucsonSimple = new TucsonSimple();
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(2021);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 String expected = "2021123456";
 String actual = writer.toString();
 assertEquals(expected, actual.substring(0, expected.length() + yearWidth));
 writer.close();
 }

 @Test
 public void testSaveDataWithIsProcessedFalse() throws IOException {
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_DATA));
 TucsonSimple tucsonSimple = new TucsonSimple();
 int yearWidth = 6;
 Year start = new Year(2021);
 Year end = new Year(2022);
 String code = "123456";
 tucsonSimple.saveData(sample, writer);
 String expected = "1234562021 1\n" +
                 "1234562022 2\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 writer.close();
 }

 @Test
 public void testSaveDataWithIsProcessedTrue() throws IOException {
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_DATA));
 TucsonSimple tucsonSimple = new TucsonSimple();
 int yearWidth = 4;
 Range range = new Range(new Year(2021), new Year(2023));
 sample.range = range;
 tucsonSimple.saveData(sample, writer);
 String expected = "123456  1\n" +
                 "123456  2\n" +
                 "123456  3\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 writer.close();
 }

 @Test
 public void testSaveDataWithIsSummedFalse() throws IOException {
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_DATA));
 TucsonSimple tucsonSimple = new TucsonSimple();
 int yearWidth = 6;

 // Add some data
 sample.data.put(new Year(2021), "1");
 sample.data.put(new Year(2022), "2");

 tucsonSimple.saveData(sample, writer, false);
 String expected = "1234562021 1\n" +
                 "1234562022 2\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 writer.close();
 }

 @Test
 public void testSaveDataWithIsSummedTrue() throws IOException {
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_DATA));
 TucsonSimple tucsonSimple = new TucsonSimple();
 int yearWidth = 3;

 // Add some data
 sample.data.put(new Year(2021), "1");
 sample.data.put(new Year(2022), "2");
 sample.count.put(new Year(2021), "3");
 sample.count.put(new Year(2022), "4");

 tucsonSimple.saveData(sample, writer, true);
 String expected = "1234562021 3\n" +
                 "1234562022 4\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 writer.close();
 }

 @Test
 public void testSaveDataWithDataBeyondSampleRange() throws IOException {
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_DATA));
 TucsonSimple tucsonSimple = new TucsonSimple();
 int yearWidth = 6;
 Year start = new Year(2021);
 Year end = new Year(2022);
 String code = "123456";
 tucsonSimple.saveData(sample, writer);
 String expected = "1234562021 1\n" +
                 "1234562022 2\n" +
                 "9990  0\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 writer.close();
 }

 @Test
 public void testSaveDataWithDataBeforeSampleStart() throws IOException {
 Sample sample = new Sample();
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_DATA));
 TucsonSimple tucsonSimple = new TucsonSimple();
 int yearWidth = 6;
 Year start = new Year(2023);
 Year end = new Year(2024);
 String code = "123456";
 tucsonSimple.saveData(sample, writer);
 String expected = "   999\n" +
                 "9990  0\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 writer.close();
 }

 @Test
 public void testComputeRangeWithStartYearBefore1() throws IOException {
 Sample sample = new Sample();
 TucsonSimple tucsonSimple = new TucsonSimple();
 Range expected = new Range(new Year(1001), new Year(1001));
 Range actual = tucsonSimple.computeRange(sample);
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRangeWithStartYearBetween1And8000() throws IOException {
 Sample sample = new Sample();
 TucsonSimple tucsonSimple = new TucsonSimple();
 Year start = new Year(1999);
 Range expected = new Range(new Year(1999), new Year(1999));
 Range actual = tucsonSimple.computeRange(sample);
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRangeWithStartYearAfter8000() throws IOException {
 Sample sample = new Sample();
 TucsonSimple tucsonSimple = new TucsonSimple();
 Year start = new Year(9001);
 Range expected = new Range(new Year(9001), new Year(9001).redateBy(8000));
 Range actual = tucsonSimple.computeRange(sample);
 assertEquals(expected, actual);
 }
}