package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import corina.Sample;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.WrongFiletypeException;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedWriter writerMock;
 private BufferedReader readerMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 tucsonSimple = new TucsonSimple();
 writerMock = mock(BufferedWriter.class);
 readerMock = mock(BufferedReader.class);
 }

 @Test
 public void testToString() {
 String result = tucsonSimple.toString();
 String expected = I18n.getText("format.tucsonsimple");
 assertEquals(expected, result);
 }

 @Test
 public void testgetDefaultExtension() {
 String result = tucsonSimple.getDefaultExtension();
 String expected = ".RWL";
 assertEquals(expected, result);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadThrowsException() throws IOException {
 BufferedReader input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("invalid data".getBytes())));
 Sample sample = new Sample();
 tucsonSimple.load(input);
 fail("load method should have thrown WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.setMeta("id", 123456);
 String result = tucsonSimple.make6digitCode(sample);
 String expected = "123456";
 assertEquals(expected, result);

 Sample sample2 = new Sample();
 String result2 = tucsonSimple.make6digitCode(sample2);
 String expected2 = "000000";
 assertEquals(expected2, result2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Sample sample3 = new Sample();
 BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Year year = new Year(2000);
 tucsonSimple.saveRowHeader(output, sample3, "code", 6, year);
 String result = output.toString();
 String expected = "code2000";
 assertEquals(expected, result);
 }

 @Test
 public void testSaveData() throws IOException {
 Sample sample4 = new Sample();
 sample4.setData(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
 sample4.setCount(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10});
 BufferedWriter output = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Year start = new Year(2001);
 Year end = new Year(2003);
 tucsonSimple.saveData(sample4, output);
 String result = output.toString();
 String expected = "000001 2001\n" +
 "000001 01 1\n" +
 "000001 02 2\n" +
 "000001 03 3\n" +
 "000001 04 4\n" +
 "000001 05 5\n" +
 "000001 06 6\n" +
 "000001 07 7\n" +
 "000001 08 8\n" +
 "000001 09 9\n" +
 "9990   \n" +
 "9990   \n" +
 "9990   \n" +
 "9990   0\n" +
 "9990   0\n" +
 "9990   0\n" +
 "9990   0\n" +
 "9990   0\n" +
 "9990   0\n" +
 "9990   0\n";
 assertEquals(expected, result);
 }

 @Test
 public void testComputeRange() {
 Sample sample5 = new Sample();
 Year start = new Year(98);
 Year end = new Year(99);
 Range result = tucsonSimple.computeRange(sample5);
 Year expectedStart = new Year(1001);
 Year expectedEnd = new Year(1002);
 assertEquals(expectedStart, result.getStart());
 assertEquals(expectedEnd, result.getEnd());
 }

 @Test
 public void testSave() throws IOException {
 Sample sample6 = new Sample();
 BufferedWriter writer = writerMock;
 sample6.setData(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
 sample6.setCount(new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10});
 tucsonSimple.save(sample6, writer);
 verify(writer).write(eq("code"));
 verify(writer).write(anyString());
 verify(writer).write(anyString());
 verify(writer).write(eq("2"));
 verify(writer).write(anyString());
 verify(writer).write(eq("1"));
 verify(writer).write(anyString());
 verify(writer).write(eq("3"));
 verify(writer).write(anyString());
 verify(writer).write(eq("4"));
 verify(writer).write(anyString());
 verify(writer).write(eq("5"));
 verify(writer).write(anyString());
 verify(writer).write(eq("6"));
 verify(writer).write(anyString());
 verify(writer).write(eq("7"));
 verify(writer).write(anyString());
 verify(writer).write(eq("8"));
 verify(writer).write(anyString());
 verify(writer).write(eq("9"));
 verify(writer).write(anyString());
 verify(writer).write(eq("9990"));
 verify(writer).write(eq(""));
 verify(writer).write(eq("9990"));
 verify(writer).write(eq(""));
 verify(writer).write(eq("9990"));
 verify(writer).write(eq(""));
 verify(writer).write(eq("9990"));
 verify(writer).write(eq(""));
 verify(writer).write(eq("9990"));
 verify(writer).write(eq(""));
 verify(writer).write(eq("9990"));
 verify(writer).write(eq(""));
 verify(writer).write(eq("9990"));
 verify(writer).write(eq(""));
 verify(writer).write(eq(""));
 verify(writer).write(eq("999\n"));
 verify(writer).write(eq("\n"));
 }
}