package corina.formats.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.formats.Filetype;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private static final String I18N_TEXT = "format.tucsonsimple";

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
 public void testLoadException() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
 tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.meta = new HashMap<String, Object>();
 sample.meta.put("id", "123456");
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("123456", tucsonSimple.make6digitCode(sample));

 Sample sampleWithoutId = new Sample();
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("000000", tucsonSimple.make6digitCode(sampleWithoutId));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.meta = new HashMap<String, Object>();
 sample.meta.put("id", "123456");
 String code = "001234";
 int yearWidth = 6;
 Year year = new Year(2000);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
 ByteArrayOutputStream outputStream = (ByteArrayOutputStream) writer.getOutputStream();
 assertEquals("0012342000", outputStream.toString());
 }

 @Test
 public void testSaveData() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta = new HashMap<String, Object>();
 sample.meta.put("id", "123456");
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 String code = "001234";
 Year start = new Year(2000);
 Year end = new Year(2002);
 tucsonSimple.saveData(sample, writer);
 ByteArrayOutputStream outputStream = (ByteArrayOutputStream) writer.getOutputStream();
 String expectedOutput = "0012342000\n" +
                         "56 55 54 53 52 51\n" +
                         "51 50 49 48 47 46\n" +
                         "46 45 44 43 42 41\n" +
                         "41 40 39 38 37 36\n" +
                         "36 35 34 33 32 31\n" +
                         "31 30 29  999\n";
 assertEquals(expectedOutput, outputStream.toString());
 }

 @Test
 public void testComputeRange() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(1990), new Year(1991));
 assertEquals(sample.range, tucsonSimple.computeRange(sample));

 Sample sample1 = new Sample();
 sample1.range = new Range(new Year(21000), new Year(21001));
 assertEquals(sample1.range.redateBy(8000), tucsonSimple.computeRange(sample1));

 Sample sample2 = new Sample();
 sample2.range = new Range(new Year(200), new Year(2001));
 assertEquals(new Range(new Year(1001), new Year(1002)), tucsonSimple.computeRange(sample2));
 }

 @Test
 public void testSave() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta = new HashMap<String, Object>();
 sample.meta.put("id", "123456");
 sample.data = new String[10];
 Arrays.fill(sample.data, "5");
 sample.count = new int[10];
 Arrays.fill(sample.count, 5);
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 tucsonSimple.save(sample, writer);
 ByteArrayOutputStream outputStream = (ByteArrayOutputStream) writer.getOutputStream();
 String expectedOutput = "0012342000\n" +
                         "55555555555555\n" +
                         "55555555555555\n" +
                         "55555555555555\n" +
                         "55555555555555\n" +
                         "55555555555555\n" +
                         "55555555555555\n" +
                         "55555555555555\n" +
                         "55555555555555\n" +
                         "55555555555555\n" +
                         "9990  0\n";
 assertEquals(expectedOutput, outputStream.toString());
 }

}