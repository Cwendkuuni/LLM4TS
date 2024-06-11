package corina.formats.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.formats.Filetype;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private static final String SAMPLE_DATA = "id:1 name:Test Sample\ndata: [1, 2, 3, 4]\ncount: [1, 2, 3, 4]\nrange: start:1 end:4";

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
 public void testLoadWithWrongFiletypeException() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("Wrong data".getBytes())));
 tucsonSimple.load(reader);
 }

 @Test
 public void testLoadWithCorrectData() throws IOException, ParseException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 Sample sample = new Sample(SAMPLE_DATA);
 tucsonSimple.save(sample, writer);
 writer.flush();
 ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
 Sample loadedSample = tucsonSimple.load(reader);
 assertEquals(sample, loadedSample);
 reader.close();
 }

 @Test
 public void testMake6digitCodeWithId() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta.put("id", "123456");
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);
 }

 @Test
 public void testMake6digitCodeWithoutId() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("000000", code);
 }

 @Test
 public void testMake6digitCodeWithLongId() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta.put("id", "1234567890");
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);
 }

 @Test
 public void testSaveRowHeaderWithStartYearGreaterThanCodeYear() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta.put("id", "123456");
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(2000);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 String expectedOutput = "1234562000";
 assertEquals(expectedOutput, writer.toString());
 writer.close();
 }

 @Test
 public void testSaveRowHeaderWithStartYearLessThanCodeYear() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta.put("id", "123456");
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(1999);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 String expectedOutput = "1999123456";
 assertEquals(expectedOutput, writer.toString());
 writer.close();
 }

 @Test
 public void testSaveDataWithIsSummedFalseAndIsProcessedFalse() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta.put("id", "123456");
 sample.data = new double[] {1.0, 2.0, 3.0, 4.0};
 sample.count = new double[] {1.0, 2.0, 3.0, 4.0};
 sample.range = new Range(new Year(1), new Year(4));
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(1);
 boolean isSummed = false;
 boolean isProcessed = false;
 tucsonSimple.saveData(sample, writer, isSummed, isProcessed);
 String expectedOutput = "1 2 3 4\n";
 assertEquals(expectedOutput, writer.toString());
 writer.close();
 }

 @Test
 public void testSaveDataWithIsSummedFalseAndIsProcessedTrue() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta.put("id", "123456");
 sample.data = new double[] {1.0, 2.0, 3.0, 4.0};
 sample.count = new double[] {1.0, 2.0, 3.0, 4.0};
 sample.range = new Range(new Year(1), new Year(4));
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 String code = "123456";
 int yearWidth = 4;
 Year start = new Year(1);
 Year end = new Year(3);
 boolean isSummed = false;
 boolean isProcessed = true;
 tucsonSimple.saveData(sample, writer, isSummed, isProcessed);
 String expectedOutput = "1 2 3 4\n0 0 0 0\n";
 assertEquals(expectedOutput, writer.toString());
 writer.close();
 }

 @Test
 public void testSaveDataWithIsSummedTrue() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta.put("id", "123456");
 sample.data = new double[] {1.0, 2.0, 3.0, 4.0};
 sample.count = new double[] {1.0, 2.0, 3.0, 4.0};
 sample.range = new Range(new Year(1), new Year(4));
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 String code = "123456";
 int yearWidth = 6;
 Year start = new Year(1);
 boolean isSummed = true;
 boolean isProcessed = false;
 tucsonSimple.saveData(sample, writer, isSummed, isProcessed);
 String expectedOutput = "1 2 3 4\n1 2 3 4\n";
 assertEquals(expectedOutput, writer.toString());
 writer.close();
 }

 @Test
 public void testComputeRangeWithStartYearLessThanOrEqualToOne() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(1), new Year(4));
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(sample.range, range);
 }

 @Test
 public void testComputeRangeWithStartYearGreaterThanOneButLessThanTenThousand() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(5000), new Year(5003));
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(new Range(new Year(5001), new Year(5003)), range);
 }

 @Test
 public void testComputeRangeWithStartYearGreaterThanTenThousand() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(12001), new Year(12004));
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(new Range(new Year(12001), new Year(12001)).redateStartTo(new Year(1001)), range);
 }
}