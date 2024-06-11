package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import corina.Sample;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.formats.TucsonSimple;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader mockBufferedReader;
 private BufferedWriter mockBufferedWriter;

 @Before
 public void setup() {
 tucsonSimple = new TucsonSimple();
 mockBufferedReader = mock(BufferedReader.class);
 mockBufferedWriter = mock(BufferedWriter.class);
 }

 @Test
 public void testToString() {
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadThrowsException() throws IOException {
 tucsonSimple.load(mockBufferedReader);
 fail("load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.setMeta(new HashMap<String, String>() {{
 put("id", "123456");
 }});
 assertEquals("123456", tucsonSimple.make6digitCode(sample));

 Sample sample2 = new Sample();
 assertEquals("000000", tucsonSimple.make6digitCode(sample2));

 Sample sample3 = new Sample();
 sample3.setMeta(null);
 assertEquals("000000", tucsonSimple.make6digitCode(sample3));

 Sample sample4 = new Sample();
 sample4.setMeta(new HashMap<String, String>() {{
 put("id", "1234567890");
 }});
 assertEquals("123456", tucsonSimple.make6digitCode(sample4));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 BufferedWriter writer = Mockito.spy(mockBufferedWriter);
 Sample sample = new Sample();
 Year year = new Year(2000);
 tucsonSimple.saveRowHeader(writer, sample, "000001", 6, year);
 assertEquals("0000012000", writer.getWrittenString());

 Year year2 = new Year(1999);
 tucsonSimple.saveRowHeader(writer, sample, "000001", 6, year2);
 assertEquals("0000011999", writer.getWrittenString());
 }

 @Test
 public void testSaveData() throws IOException {
 BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
 Sample sample = new Sample();
 Year start = new Year(2001);
 Year end = new Year(2003);
 String code = "000001";
 tucsonSimple.saveData(sample, writer);
 assertEquals("", writer.getWrittenString());
 writer.close();

 Sample sample2 = new Sample();
 sample2.setData(new int[] { 1, 2, 3 });
 sample2.setCount(new int[] { 1, 2, 3 });
 sample2.setRange(new YearRange(start, end));
 Year start2 = new Year(2002);
 Year end2 = new Year(2004);
 String code2 = "000002";
 tucsonSimple.saveData(sample2, writer);
 writer.close();

 File file = new File("output.txt");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 assertEquals("0000012001\n", line);
 line = reader.readLine();
 assertEquals("0000022002\n\n", line);
 reader.close();
 file.delete();
 }

 @Test
 public void testComputeRange() {
 Sample sample = new Sample();
 sample.setRange(new YearRange(new Year(1995), new Year(1998)));
 YearRange expected = new YearRange(new Year(1995), new Year(1998));
 YearRange result = tucsonSimple.computeRange(sample);
 assertEquals(expected, result);
 }

 @Test
 public void testSave() throws IOException {
 BufferedWriter writer = mock(BufferedWriter.class);
 Sample sample = new Sample();
 tucsonSimple.save(sample, writer);
 verify(writer, never()).write(anyString());
 }

 @Test
 public void testSaveWithData() throws IOException {
 BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
 Sample sample = new Sample();
 sample.setData(new int[] { 1, 2, 3 });
 sample.setCount(new int[] { 1, 2, 3 });
 sample.setRange(new YearRange(new Year(2001), new Year(2003)));
 tucsonSimple.save(sample, writer);
 writer.close();

 File file = new File("output.txt");BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 assertEquals("0000012001\n", line);
 line = reader.readLine();
 assertEquals("0000022002\n\n", line);
 reader.close();
 file.delete();
 }

 @Test
 public void testSaveWithEmptyData() throws IOException {
 BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
 Sample sample = new Sample();
 sample.setData(new int[]{});
 sample.setCount(new int[]{});
 sample.setRange(new YearRange(new Year(2001), new Year(2003)));
 tucsonSimple.save(sample, writer);
 writer.close();

 File file = new File("output.txt");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 assertEquals("", line);
 reader.close();
 file.delete();
 }

 @Test
 public void testSaveWithProcessedData() throws IOException {
 BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
 Sample sample = new Sample();
 sample.setData(new int[]{1, 2, 3});
 sample.setCount(new int[]{1, 2, 3});
 sample.setRange(new YearRange(new Year(2001), new Year(2003)));
 sample.setProcessed(true);
 tucsonSimple.save(sample, writer);
 writer.close();

 File file = new File("output.txt");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 assertEquals("0000012001\n", line);
 line = reader.readLine();
 assertEquals("9990    \n", line);
 reader.close();
 file.delete();
 }

 @Test
 public void testSaveWithSummedData() throws IOException {
 BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
 Sample sample = new Sample();
 sample.setData(new int[]{1, 2, 3});
 sample.setCount(new int[]{1, 2, 3});
 sample.setRange(new YearRange(new Year(2001), new Year(2003)));
 sample.setSummed(true);
 tucsonSimple.save(sample, writer);
 writer.close();

 File file = new File("output.txt");
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 assertEquals("0000012001\n", line);
 line = reader.readLine();
 assertEquals("9990  0\n", line);
 reader.close();
 file.delete();
 }
}