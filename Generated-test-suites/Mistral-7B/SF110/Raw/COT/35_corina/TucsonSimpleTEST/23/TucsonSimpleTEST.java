package corina.formats.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.formats.Filetype;
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
 public void testLoadThrowsWrongFiletypeException() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("test data".getBytes())));
 tucsonSimple.load(reader);
 fail("load method should have thrown WrongFiletypeException");
 }

 @Test
 public void testMake6digitCodeWithSample() {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "123456");
 TucsonSimple tucsonSimple = new TucsonSimple();
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);

 sample.meta = new HashMap<>();
 assertEquals("000000", tucsonSimple.make6digitCode(sample));
}

 @Test
 public void testMake6digitCodeWithNullMeta() {
 Sample sample = new Sample();
 TucsonSimple tucsonSimple = new TucsonSimple();
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("000000", code);
}

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 String code = "123456";
 Year year = new Year(2000);
 tucsonSimple.saveRowHeader(writer, sample, code, 6, year);
 String expected = "1234562000";
 String actual = writer.toString();
 assertEquals(expected, actual);

 year = new Year(1999);
 tucsonSimple.saveRowHeader(writer, sample, code, 6, year);
 expected = "1234561999";
 actual = writer.toString();
 assertEquals(expected, actual);
}

 @Test
 public void testSaveDataWithIsSummedFalseAndIsProcessedFalse() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.data = new double[]{1.2, 3.4, 5.6, 7.8, 9.0};
 sample.count = new double[]{1, 2, 3, 4, 5};
 Year start = new Year(2000);
 Year end = new Year(2001);
 String code = "123456";
 tucsonSimple.saveData(sample, writer);
 String expected = "1.2  1\n" +
                 "3.4  2\n" +
                 "5.6  3\n" +
                 "7.8  4\n" +
                 "9.0  5\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
}

 @Test
 public void testSaveDataWithIsSummedFalseAndIsProcessedTrue() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.data = new double[]{1.2, 3.4, 5.6, 7.8, 9.0};
 sample.count = new double[]{1, 2, 3, 4, 5};
 Year start = new Year(2000);
 Year end = new Year(2001);
 String code = "123456";
 tucsonSimple.saveData(sample, writer, true, false);
 String expected = "1.2  1\n" +
                 "3.4  2\n" +
                 "5.6  3\n" +
                 "7.8  4\n" +
                 "9.0  5\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
}

 @Test
 public void testSaveDataWithIsSummedTrueAndIsProcessedFalse() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.data = new double[]{1.2, 3.4, 5.6, 7.8, 9.0};
 sample.count = new double[]{1, 2, 3, 4, 5};
 Year start = new Year(2000);
 Year end = new Year(2001);
 String code = "123456";
 tucsonSimple.saveData(sample, writer, false, true);
 String expected = "1.2  1\n" +
                 "3.4  2\n" +
                 "5.6  3\n" +
                 "7.8  4\n" +
                 "9.0  5\n" +
                 "5.2  1\n" +
                 "7.6  2\n" +
                 "10.0  3\n" +
                 "12.4  4\n" +
                 "15.0  5\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
}

 @Test
 public void testSaveDataWithIsSummedTrueAndIsProcessedTrue() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.data = new double[]{1.2, 3.4, 5.6, 7.8, 9.0};
 sample.count = new double[]{1, 2, 3, 4, 5};
 Year start = new Year(2000);
 Year end = new Year(2001);
 String code = "123456";
 tucsonSimple.saveData(sample, writer, true, true);
 String expected = "1.2  1\n" +
                 "3.4  2\n" +
                 "5.6  3\n" +
                 "7.8  4\n" +
                 "9.0  5\n" +
                 "5.2  1\n" +
                 "7.6  2\n" +
                 "10.0  3\n" +
                 "12.4  4\n" +
                 "15.0  5\n" +
                 "16.6  1\n" +
                 "19.2  2\n" +
                 "22.0  3\n" +
                 "24.8  4\n" +
                 "27.6  5\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
}

 @Test
 public void testComputeRange() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new YearRange(new Year(2000), new Year(2005));
 Year start = new Year(2000);
 Year end = new Year(2001);
 Range range = tucsonSimple.computeRange(sample);
 Year expectedStart = new Year(2001);
 Year expectedEnd = new Year(2001);
 assertEquals(expectedStart, range.getStart());
 assertEquals(expectedEnd, range.getEnd());

 sample.range = new YearRange(new Year(1995), new Year(1998));
 range = tucsonSimple.computeRange(sample);
 expectedStart = new Year(1996);
 expectedEnd = new Year(1998);
 assertEquals(expectedStart, range.getStart());
 assertEquals(expectedEnd, range.getEnd());

 sample.range = new YearRange(new Year(1950), new Year(1955));
 range = tucsonSimple.computeRange(sample);
 expectedStart = new Year(1951);
 expectedEnd = new Year(1955);
 assertEquals(expectedStart, range.getStart());
 assertEquals(expectedEnd, range.getEnd());

 sample.range = new YearRange(new Year(1), new Year(1));
 range = tucsonSimple.computeRange(sample);
 expectedStart = new Year(1001);
 expectedEnd = new Year(1001);
 assertEquals(expectedStart, range.getStart());
 assertEquals(expectedEnd, range.getEnd());
}

 @Test
 public void testSave() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.data = new double[]{1.2, 3.4, 5.6, 7.8, 9.0};
 sample.count = new double[]{1, 2, 3, 4, 5};
 Year start = new Year(2000);
 Year end = new Year(2001);
 String code = "123456";
 tucsonSimple.save(sample, writer);
 String expected = "1.2  1\n" +
                 "3.4  2\n" +
                 "5.6  3\n" +
                 "7.8  4\n" +
                 "9.0  5\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
}

}