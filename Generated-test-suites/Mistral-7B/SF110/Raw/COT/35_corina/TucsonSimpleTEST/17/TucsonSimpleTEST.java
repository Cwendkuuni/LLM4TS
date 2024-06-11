package corina.formats.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

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
import corina.formats.Filetype;
import corina.ui.I18n;
import corina.util.Range;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.Exception.WrongFiletypeException;

public class TucsonSimpleTest {

 private static final String TEST_DATA = "id:1 name:Test Sample range:1980-1985 data:1,2,3,4,5 count:1,2,3,4,5";

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

 @Test
 public void testLoadThrowsWrongFiletypeException() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes())));
 assertThrows(WrongFiletypeException.class, () -> tucsonSimple.load(reader));
 }

 @Test
 public void testMake6digitCodeWithSample() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Map<String, String> meta = new HashMap<>();
 meta.put("id", "123456");
 Sample sample = new Sample("Test Sample", new Range(new Year(1980), new Year(1985)), meta, new double[5], new int[5]);
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);
 }

 @Test
 public void testMake6digitCodeWithoutID() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample("Test Sample", new Range(new Year(1980), new Year(1985)), null, new double[5], new int[5]);
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("000000", code);
 }

 @Test
 public void testMake6digitCodeWithIDLongerThan6Digits() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Map<String, String> meta = new HashMap<>();
 meta.put("id", "12345678901234");
 Sample sample = new Sample("Test Sample", new Range(new Year(1980), new Year(1985)), meta, new double[5], new int[5]);
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample("Test Sample", new Range(new Year(1980), new Year(1985)), null, new double[5], new int[5]);
 String code = "000001";
 int yearWidth = 6;
 Year start = new Year(1981);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 assertEquals("0000011981", writer.toString());
 }

 @Test
 public void testSaveRowHeaderWithYearBeforeStart() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_DATA.getBytes())));
 BufferedWriter w = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample("Test Sample", new Range(new Year(1980), new Year(1985)), null, new double[5], new int[5]);
 String code = "000001";
 Year start = new Year(1979);
 tucsonSimple.saveRowHeader(w, sample, code, 6, start);
 assertEquals("0000011979", w.toString());
 }

 @Test
 public void testSaveData() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 Sample sample = new Sample("Test Sample", new Range(new Year(1980), new Year(1985)), null, new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, new int[]{1, 2, 3, 4, 5});
 tucsonSimple.saveData(sample, writer);
 String expectedOutput = "0000011980  1.0  1\n" +
                        "0000011981  2.0  2\n" +
                        "0000011982  3.0  3\n" +
                        "0000011983  4.0  4\n" +
                        "0000011984  5.0  5\n" +
                        "9990   ";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testSaveDataWithSummed() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 Sample sample = new Sample("Test Sample", new Range(new Year(1980), new Year(1985)), null, new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, new int[]{1, 2, 3, 4, 5});
 tucsonSimple.saveData(sample, writer, true);
 String expectedOutput = "0000011980  1.0  1\n" +
                        "0000011981  2.0  2\n" +
                        "0000011982  3.0  3\n" +
                        "0000011983  4.0  4\n" +
                        "0000011984  5.0  5\n" +
                        "15.0  ";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testSaveDataWithProcessed() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 Sample sample = new Sample("Test Sample", new Range(new Year(1980), new Year(1985)), null, new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, new int[]{1, 2, 3, 4, 5});
 tucsonSimple.saveData(sample, writer, true, true);
 String expectedOutput = "0000011980    1.0\n" +
                        "0000011981    2.0\n" +
                        "0000011982    3.0\n" +
                        "0000011983    4.0\n" +
                        "0000011984    5.0\n" +
                        "   999\n" +
                        "9990   ";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testComputeRange() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample("Test Sample", new Range(new Year(1980), new Year(1985)), null, new double[5], new int[5]);
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(new Range(new Year(1980), new Year(1985)), range);
 }

 @Test
 public void testComputeRangeWithStartBeforeYear1() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample("Test Sample", new Range(new Year(-1), new Year(1985)), null, new double[5], new int[5]);
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(new Range(new Year(1001), new Year(1985)), range);
 }

 @Test
 public void testComputeRangeWithStartAfterYear8000() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample("Test Sample", new Range(new Year(8001), new Year(1985)), null, new double[5], new int[5]);
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(new Range(new Year(8001), new Year(1985)), range.redateBy(8000));
 }

 @Test
 public void testSave() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 Sample sample = new Sample("Test Sample", new Range(new Year(1980), new Year(1985)), null, new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, new int[]{1, 2, 3, 4, 5});
 tucsonSimple.save(sample, writer);
 String expectedOutput = "0000011980  1.0  1\n" +
                        "0000011981  2.0  2\n" +
                        "0000011982  3.0  3\n" +
                        "0000011983  4.0  4\n" +
                        "0000011984  5.0  5\n";
 assertEquals(expectedOutput, outputStream.toString());
 }
}