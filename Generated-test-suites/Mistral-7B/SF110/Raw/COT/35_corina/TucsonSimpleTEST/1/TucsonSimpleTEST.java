import org.junit.Test;
import org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

public class TucsonSimpleTest {

 private static final String I18N_TEXT = "format.tucsonsimple";

 @Test
 public void testToString() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testGetDefaultExtension() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new StringReader(""));
 tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.meta.put("id", "123456");
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("123456", tucsonSimple.make6digitCode(sample));

 Sample sample2 = new Sample();
 TucsonSimple tucsonSimple2 = new TucsonSimple();
 assertEquals("000000", tucsonSimple2.make6digitCode(sample2));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 Year year = new Year(2000);
 Sample sample = new Sample();
 String code = "111111";
 int yearWidth = 4;
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
 assertEquals("1111112000", writer.toString());

 Year year2 = new Year(2001);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year2);
 assertEquals("1111112001", writer.toString());
 }

 @Test
 public void testSaveData() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 ByteArrayOutputStream dataOutputStream = new ByteArrayOutputStream();
 dataOutputStream.write("5".getBytes());
 Sample sample = new Sample();
 sample.data = new int[100];
 sample.data[0] = 5;
 sample.range = new Range(new Year(2000), new Year(2000));
 tucsonSimple.saveData(sample, writer);

 assertEquals("5\n", writer.toString());
 }

 @Test
 public void testComputeRange() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.range = new Range(new Year(1985), new Year(1985));
 assertEquals(sample.range, tucsonSimple.computeRange(sample));

 Sample sample2 = new Sample();
 sample2.range = new Range(new Year(1952), new Year(1952));
 assertEquals(sample2.range.redateBy(8000), tucsonSimple.computeRange(sample2));

 Sample sample3 = new Sample();
 sample3.range = new Range(new Year(1978), new Year(1978));
 assertEquals(sample3.range.redateStartTo(new Year(1991)), tucsonSimple.computeRange(sample3));
 }

 @Test
 public void testSave() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 Sample sample = new Sample();
 sample.data = new int[100];
 sample.data[0] = 5;
 sample.count = new int[100];
 sample.count[0] = 1;
 sample.range = new Range(new Year(2000), new Year(2000));
 tucsonSimple.save(sample, writer);

 String expected = "5\n";
 assertEquals(expected, writer.toString());
 }

 @Test(expected = IOException.class)
 public void testSaveThrowsIOException() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 Sample sample = new Sample();
 sample.data = new int[100];
 sample.data[0] = 5;
 sample.count = new int[100];
 sample.count[0] = 1;
 sample.range = new Range(new Year(2000), new Year(2000));
 tucsonSimple.save(sample, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveThrowsIllegalArgumentException() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 Sample sample = new Sample();
 sample.data = new int[100];
 sample.data[0] = 5;
 sample.count = new int[100];
 sample.count[0] = 1;
 sample.range = null;
 tucsonSimple.save(sample, writer);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSaveThrowsIndexOutOfBoundsException() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 Sample sample = new Sample();
 sample.data = new int[100];
 sample.data[101] = 5;
 sample.count = new int[100];
 sample.count[0] = 1;
 sample.range = new Range(new Year(2000), new Year(2000));
 tucsonSimple.save(sample, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveThrowsIllegalArgumentException2() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 Sample sample = new Sample();
 sample.data = new int[100];
 sample.data[0] = 5;
 sample.count = null;
 sample.range = new Range(new Year(2000), new Year(2000));
 tucsonSimple.save(sample, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveThrowsIllegalArgumentException3() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 Sample sample = new Sample();
 sample.data = null;
 sample.count = new int[100];
 sample.count[0] = 1;
 sample.range = new Range(new Year(2000), new Year(2000));
 tucsonSimple.save(sample, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveThrowsIllegalArgumentException4() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 Sample sample = new Sample();
 sample.data = new int[100];
 sample.data[0] = 5;
 sample.count = new int[100];
 sample.count[0] = 1;
 sample.range = new Range(null, new Year(2000));
 tucsonSimple.save(sample, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveThrowsIllegalArgumentException5() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 Sample sample = new Sample();
 sample.data = new int[100];
 sample.data[0] = 5;
 sample.count = new int[100];
 sample.count[0] = 1;
 sample.range = new Range(new Year(2000), null);
 tucsonSimple.save(sample, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveThrowsIllegalArgumentException6() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 Sample sample = new Sample();
 sample.data = new int[100];
 sample.data[0] = 5;
 sample.count = new int[100];
 sample.count[0] = 1;
 sample.range = new Range(new Year(2000), new Year(2000));
 sample.range.setStart(null);
 tucsonSimple.save(sample, writer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveThrowsIllegalArgumentException7() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new StringWriter());
 Sample sample = new Sample();
 sample.data = new int[100];
 sample.data[0] = 5;
 sample.count = new int[100];
 sample.count[0] = 1;
 sample.range = new Range(new Year(2000), new Year(2000));
 sample.range.setEnd(null);
 tucsonSimple.save(sample, writer);
 }
}