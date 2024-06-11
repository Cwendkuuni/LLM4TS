package corina.formats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import corina.formats.*;
import corina.ui.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader mockBufferedReader;
 private BufferedWriter mockBufferedWriter;
 private Sample mockSample;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 tucsonSimple = new TucsonSimple();
 mockBufferedReader = mock(BufferedReader.class);
 mockBufferedWriter = mock(BufferedWriter.class);
 mockSample = new Sample();
 }

 @Test
 public void testToString() {
 String result = tucsonSimple.toString();
 assertEquals("format.tucsonsimple", result);
 }

 @Test
 public void testgetDefaultExtension() {
 String result = tucsonSimple.getDefaultExtension();
 assertEquals(".RWL", result);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadThrowsException() throws IOException {
 tucsonSimple.load(mockBufferedReader);
 fail("load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCodeWithSampleMeta() {
 String inputId = "sampleId123";
 mockSample.meta.put("id", inputId);
 String result = tucsonSimple.make6digitCode(mockSample);
 assertEquals(inputId.substring(0, 6), result);
 }

 @Test
 public void testMake6digitCodeWithNoSampleMeta() {
 String result = tucsonSimple.make6digitCode(mockSample);
 assertEquals("000000", result);
 }

 @Test
 public void testSaveRowHeaderWithStartYearBefore() throws IOException {
 Year start = new Year(1990);
 tucsonSimple.saveRowHeader(mockBufferedWriter, mockSample, "123456", 6, start);
 String expected = "123456 1990";
 verify(mockBufferedWriter).write(expected);
 }

 @Test
 public void testSaveRowHeaderWithStartYearAfter() throws IOException {
 Year start = new Year(2000);
 Year currentYear = new Year(2005);
 tucsonSimple.saveRowHeader(mockBufferedWriter, mockSample, "123456", 6, start, currentYear);
 String expected = "123456 2005";
 verify(mockBufferedWriter).write(expected);
 }

 @Test
 public void testSaveDataWithIsProcessedFalse() throws IOException {
 String data = "1.23";
 mockSample.data.put(new Year(2005), data);
 tucsonSimple.saveData(mockSample, mockBufferedWriter);
 String expectedCode = "123456 2005 1.23";
 String expectedSummedData = "";
 String expectedProcessedData = "   ";
 verify(mockBufferedWriter).write(expectedCode);
 verify(mockBufferedWriter, atLeastOnce()).write(expectedSummedData);
 verify(mockBufferedWriter, atLeastOnce()).write(expectedProcessedData);
 }

 @Test
 public void testSaveDataWithIsProcessedTrue() throws IOException {
 String data = "1.23";
 mockSample.data.put(new Year(2005), data);
 tucsonSimple.saveData(mockSample, mockBufferedWriter, true);
 String expectedCode = "123456 2005 1.23";
 String expectedSummedData = "9990  0";
 verify(mockBufferedWriter).write(expectedCode);
 verify(mockBufferedWriter).write(expectedSummedData);
 }

 @Test
 public void testSaveDataWithInvalidYear() throws IOException {
 tucsonSimple.saveData(mockSample, mockBufferedWriter);
 String expectedCode = "123456 ";
 String expectedData = "999";
 verify(mockBufferedWriter).write(expectedCode);
 verify(mockBufferedWriter).write(expectedData);
 }

 @Test
 public void testComputeRangeWithValidYear() {
 Year inputStart = new Year(2005);
 Range result = tucsonSimple.computeRange(mockSample);
 assertEquals(inputStart, result.getStart());
 }

 @Test
 public void testComputeRangeWithOlderStartYear() {
 Year inputStart = new Year(1995);
 Range result = tucsonSimple.computeRange(mockSample);
 assertEquals(new Year(1001), result.getStart());
 }

 @Test
 public void testComputeRangeWithNewerStartYear() {
 Year inputStart = new Year(2010);
 Range result = tucsonSimple.computeRange(mockSample);
 assertEquals(inputStart, result.getStart());
 }

 @Test
 public void testSaveMethod() throws IOException {
 String data = "1.23";
 mockSample.data.put(new Year(2005), data);
 tucsonSimple.save(mockSample, mockBufferedWriter);
 tucsonSimple.saveData(mockSample, mockBufferedWriter);
 verify(mockBufferedWriter).write(mockSample.toString());
 verify(mockBufferedWriter).write(expectedCode);
 verify(mockBufferedWriter).write(expectedSummedData);
 verify(mockBufferedWriter).write(expectedProcessedData);
 }
}