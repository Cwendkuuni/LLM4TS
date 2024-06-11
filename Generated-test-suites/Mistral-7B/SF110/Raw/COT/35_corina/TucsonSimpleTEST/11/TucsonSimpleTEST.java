package corina.formats.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.formats.Filetype;
import corina.formats.Sample;
import corina.formats.WrongFiletypeException;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple = new TucsonSimple();

 @Test
 public void testToString() {
  String expected = I18n.getText("format.tucsonsimple");
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
 public void testLoadWithWrongFiletypeException() throws IOException {
  BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
  tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCodeWithSample() {
  Sample sample = new Sample(new Range(new Year(2021), new Year(2021)), "Sample Data");
  String expected = "000000";
  String actual = tucsonSimple.make6digitCode(sample);
  assertEquals(expected, actual);

  sample.meta.put("id", "123456");
  expected = "123456";
  actual = tucsonSimple.make6digitCode(sample);
  assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeWithNoId() {
  Sample sample = new Sample(new Range(new Year(2021), new Year(2021)), "Sample Data");
  String expected = "000000";
  String actual = tucsonSimple.make6digitCode(sample);
  assertEquals(expected, actual);
 }

 @Test
 public void testMake6digitCodeWithLongId() {
  Sample sample = new Sample(new Range(new Year(2021), new Year(2021)), "Sample Data");
  sample.meta.put("id", "1234567890");
  String expected = "123456";
  String actual = tucsonSimple.make6digitCode(sample);
  assertEquals(expected, actual);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
  Sample sample = new Sample(new Range(new Year(2021), new Year(2021)), "Sample Data");
  String code = "123456";
  int yearWidth = 6;
  Year start = new Year(2021);
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
  String expected = code + start.toString();
  String actual = writer.toString();
  assertEquals(expected, actual.substring(0, expected.length() + yearWidth));
 }

 @Test
 public void testSaveDataWithIsSummedFalse() throws IOException {
  Sample sample = new Sample(new Range(new Year(2021), new Year(2022)), "Sample Data");
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  tucsonSimple.saveData(sample, writer);
  String expected = "9990  0\n123456 2021\n";
  String actual = writer.toString();
  assertEquals(expected, actual);
 }

 @Test
 public void testSaveDataWithIsSummedTrue() throws IOException {
  Sample sample = new Sample(new Range(new Year(2021), new Year(2022)), "Sample Data");
  sample.count = new int[sample.data.size()];
  sample.data.set(0, 12);
  sample.data.set(1, 34);
  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
  tucsonSimple.saveData(sample, writer, true);
  String expected = "9990  0\n123456 2021  12\n123456 2022  34\n";
  String actual = writer.toString();
  assertEquals(expected, actual);
 }

 @Test
 public void testComputeRangeWithSampleWithValidStartYear() {
  Sample sample = new Sample(new Range(new Year(2021), new Year(2022)), "Sample Data");
  Range expected = new Range(new Year(2021), new Year(2022));
  Range actual = tucsonSimple.computeRange(sample);
  assertEquals(expected, actual);
 }

 @Test
 public void testComputeRangeWithSampleWithStartYearBelowMinimum() {
  Sample sample = new Sample(new Range(new Year(0), new Year(2022)), "Sample Data");
  Range expected = new Range(new Year(1001), new Year(2022));
  Range actual = tucsonSimple.computeRange(sample);
  assertEquals(expected, actual);
 }

 @Test
 public void testComputeRangeWithSampleWithStartYearAboveMaximum() {
  Sample sample = new Sample(new Range(new Year(8001), new Year(2022)), "Sample Data");
  Range expected = new Range(new Year(1), new Year(8010));
  Range actual = tucsonSimple.computeRange(sample);
  assertEquals(expected, actual.redateStartTo(new Year(1001)));
  assertEquals(expected.getEnd().diff(expected.getStart()), 8009);
 }

 @Test
 public void testSaveWithValidData() throws IOException {
  Sample sample = new Sample(new Range(new Year(2021), new Year(2022)), "Sample Data");
  sample.data = new double[sample.getSize()];
  sample.data[0] = 12.0;
  sample.data[1] = 34.0;
  BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0]))){
      @Override
      public BufferedWriter writer() {
          return new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
      }
  };
  tucsonSimple.save(sample, writer);
  String expected = "123456 2021  12\n123456 2022  34\n";
  String actual = writer.toString();
  assertEquals(expected, actual);
 }

}