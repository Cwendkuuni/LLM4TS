import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple = new TucsonSimple();
 private BufferedReader reader = mock(BufferedReader.class);
 private BufferedWriter writer = mock(BufferedWriter.class);
 private I18n i18n = mock(I18n.class);
 private StringWriter sw = new StringWriter();
 private StringReader sr = new StringReader("");

 @Test
 public void testToString() {
  when(i18n.getText(eq("format.tucsonsimple"))).thenReturn("TucsonSimple format");
  assertEquals("TucsonSimple format", tucsonSimple.toString());
 }

 @Test
 public void testGetDefaultExtension() {
  assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = IOException.class)
 public void testLoadThrowsWrongFiletypeException() throws IOException {
  when(reader.readLine()).thenReturn("Not a TucsonSimple file");
  assertThrows(WrongFiletypeException.class, () -> tucsonSimple.load(reader));
 }

 @Test
 public void testMake6digitCodeEmptyId() {
  Sample s = new Sample();
  String code = tucsonSimple.make6digitCode(s);
  assertEquals("000000", code);
 }

 @Test
 public void testMake6digitCodeValidId() {
  Sample s = new Sample();
  s.meta.put("id", "123456");
  String code = tucsonSimple.make6digitCode(s);
  assertEquals("123456", code);
 }

 @Test
 public void testMake6digitCodeIdLongerThan6Digits() {
  Sample s = new Sample();
  s.meta.put("id", "1234567890");
  String code = tucsonSimple.make6digitCode(s);
  assertEquals("123456", code);
 }

 @Test
 public void testSaveRowHeaderCorrectValues() throws IOException {
  Year year = new Year(2001);
  String code = "123456";
  tucsonSimple.saveRowHeader(writer, s, code, 6, year);
  assertEquals("1234562001", sw.getBuffer().toString());
 }

 @Test
 public void testSaveRowHeaderStartYearBeforeSampleYear() throws IOException {
  Year start = new Year(2000);
  Year year = new Year(2001);
  String code = "123456";
  tucsonSimple.saveRowHeader(writer, s, code, 6, start, year);
  assertEquals("1234562000", sw.getBuffer().toString());
 }

 @Test
 public void testSaveRowHeaderStartYearAfterSampleYear() throws IOException {
  Year start = new Year(2002);
  Year year = new Year(2001);
  String code = "123456";
  tucsonSimple.saveRowHeader(writer, s, code, 6, start, year);
  assertEquals("", sw.getBuffer().toString());
 }

 @Test
 public void testComputeRangeCorrectValues() {
  Sample s = new Sample();
  Year start = new Year(2001);
  Range range = tucsonSimple.computeRange(s);
  assertEquals(start, range.getStart());
  assertEquals(start.add(5000), range.getEnd());
 }

 @Test
 public void testComputeRangeStartYearBeforeYear1() {
  Sample s = new Sample();
  Year start = new Year(1999);
  Range range = tucsonSimple.computeRange(s);
  assertEquals(new Year(1001), range.getStart());
  assertEquals(new Year(1001).add(5000), range.getEnd());
 }

 @Test
 public void testComputeRangeStartYearAfterYear8000() {
  Sample s = new Sample();
  Year start = new Year(8001);
  Range range = tucsonSimple.computeRange(s);
  assertEquals(new Year(8001), range.getStart());
  assertEquals(new Year(8001).add(5000), range.getEnd());
 }

 @Test
 public void testSaveDataCorrectValues() throws IOException {
  Sample s = new Sample();
  Year start = new Year(2001);
  Year end = new Year(2005);
  String code = "123456";
  BufferedWriter writer = mock(BufferedWriter.class);
  when(writer.write(eq(code + start.toString()))).thenAnswer(a -> { sw.write(code + start.toString()); return null; });
  when(writer.write("   ")).thenAnswer(a -> { sw.write("   "); return null; });
  when(writer.write("5")).thenAnswer(a -> { sw.write("5"); return null; });
  when(writer.write("   ")).thenAnswer(a -> { sw.write("   "); return null; });
  when(writer.write("10")).thenAnswer(a -> { sw.write("10"); return null; });
  when(writer.newLine()).thenAnswer(a -> { sw.write("\n"); return null; });
  tucsonSimple.saveData(s, writer);
  assertEquals("1234562001\n5   \n10   \n", sw.getBuffer().toString());
 }

 @Test
 public void testSaveDataCorrectValuesWhenProcessed() throws IOException {
  Sample s = new Sample();
  Year start = new Year(2001);
  Year end = new Year(2005);
  String code = "123456";
  boolean isProcessed = true;
  BufferedWriter writer = mock(BufferedWriter.class);
  when(writer.write(eq(code + start.toString()))).thenAnswer(a -> { sw.write(code + start.toString()); return null; });
  when(writer.write(eq("       "))).thenAnswer(a -> { sw.write("       "); return null; });
  when(writer.write("9990  0")).thenAnswer(a -> { sw.write("9990  0"); return null; });
  when(writer.newLine()).thenAnswer(a -> { sw.write("\n"); return null; });
  tucsonSimple.saveData(s, writer, isProcessed);
  assertEquals("1234562001\n       \n9990  0\n", sw.getBuffer().toString());
 }

 @Test(expected = NumberFormatException.class)
 public void testSaveDataNonNumericData() throws IOException {
  Sample s = new Sample();
  Year start = new Year(2001);
  String code = "123456";
  BufferedWriter writer = mock(BufferedWriter.class);
  when(writer.write(eq(code + start.toString()))).thenAnswer(a -> { sw.write(code + start.toString()); return null; });
  when(writer.write(eq("Not a number"))).thenThrow(NumberFormatException.class);
  when(writer.newLine()).thenAnswer(a -> { sw.write("\n"); return null; });
  tucsonSimple.saveData(s, writer);
  assertThrows(NumberFormatException.class, () -> sw.getBuffer().toString());
 }

 @Test(expected = IOException.class)
 public void testSaveDataIOException() throws IOException {
  Sample s = new Sample();
  Year start = new Year(2001);
  String code = "123456";
  BufferedWriter writer = mock(BufferedWriter.class);
  when(writer.write(eq(code + start.toString()))).thenAnswer(a -> { throw new IOException(); });
  when(writer.newLine()).thenAnswer(a -> { throw new IOException(); });
  tucsonSimple.saveData(s, writer);
  assertThrows(IOException.class, () -> sw.getBuffer().toString());
 }

}