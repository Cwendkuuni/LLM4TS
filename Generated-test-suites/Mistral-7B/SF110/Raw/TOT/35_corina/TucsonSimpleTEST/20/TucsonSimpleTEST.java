package corina.formats.test;

import static org.junit.Assert.*;
import static corina.util.TestData.*;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import java.io.BuffereredWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TucsonSimpleTest {

 @Test
 public void testToString() {
 TucsonSimple format = new TucsonSimple();
 assertEquals("format.tucsonsimple", format.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 TucsonSimple format = new TucsonSimple();
 assertEquals(".RWL", format.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadWithWrongFiletype() throws IOException {
 BufferedReader reader = new BufferedReader(new StringReader("Wrong data"));
 TucsonSimple format = new TucsonSimple();
 format.load(reader);
 fail("Should not reach this point");
 }

 @Test
 public void testMake6digitCodeWithValidSample() {
 Sample sample = createSample("id123456");
 TucsonSimple format = new TucsonSimple();
 String code = format.make6digitCode(sample);
 assertEquals("123456", code);
 }

 @Test
 public void testMake6digitCodeWithInvalidSample() {
 Sample sample = createSample("sample1");
 TucsonSimple format = new TucsonSimple();
 String code = format.make6digitCode(sample);
 assertEquals("000000", code);
 }

 @Test
 public void testSaveRowHeaderWithValidSample() throws IOException {
 Sample sample = createSample("id19900101");
 TucsonSimple format = new TucsonSimple();
 BuffereredWriter writer = new BuffereredWriter(new StringWriter());
 format.saveRowHeader(writer, sample, "000001", 6, new Year(1990));
 String expected = "0000011990";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testSaveRowHeaderWithInvalidStartYear() throws IOException {
 Sample sample = createSample("id19900101");
 TucsonSimple format = new TucsonSimple();
 BuffereredWriter writer = new BuffereredWriter(new StringWriter());
 format.saveRowHeader(writer, sample, "000001", 6, new Year(1989));
 String expected = "1989000001";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testSaveDataWithValidSample() throws IOException {
 Sample sample = createSample("id19900101", new String[]{"1", "2", "3"}, new int[]{1, 2, 3});
 TucsonSimple format = new TucsonSimple();
 BuffereredWriter writer = new BuffereredWriter(new StringWriter());
 format.saveData(sample, writer);
 String expected = "0000011990 1\n" +
                 "0000021990 2\n" +
                 "0000031990 3\n" +
                 "   999\n";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testSaveDataWithInvalidStartYear() throws IOException {
 Sample sample = createSample("id19900101", new String[]{"1", "2", "3"}, new int[]{1, 2, 3});
 TucsonSimple format = new TucsonSimple();
 BuffereredWriter writer = new BuffereredWriter(new StringWriter());
 format.saveData(sample, writer);
 String expected = "1989000001 999\n";
 assertEquals(expected, writer.toString());
 }

 @Test
 public void testComputeRangeWithValidSample() {
 TucsonSimple format = new TucsonSimple();
 Sample sample = createSample("id19900101");
 Range range = format.computeRange(sample);
 Year start = range.getStart();
 Year end = range.getEnd();
 assertEquals(new Year(1990), start);
 assertEquals(new Year(1990), end);
 }

 @Test
 public void testComputeRangeWithInvalidStartYear() {
 TucsonSimple format = new TucsonSimple();
 Sample sample = createSample("id19900101");
 Range range = format.computeRange(sample);
 Year start = range.getStart();
 Year end = range.getEnd();
 assertEquals(new Year(1001), start);
 assertEquals(new Year(1001), end);
 }

 @Test
 public void testComputeRangeWithValidStartYearAndValidEndYear() {
 TucsonSimple format = new TucsonSimple();
 Sample sample = createSample("id19850630");
 Range range = format.computeRange(sample);
 Year start = range.getStart();
 Year end = range.getEnd();
 assertEquals(new Year(1985), start);
 assertEquals(new Year(1985), end);
 }

 @Test
 public void testComputeRangeWithValidStartYearAndInvalidEndYear() {
 TucsonSimple format = new TucsonSimple();
 Sample sample = createSample("id19850630");
 Range range = format.computeRange(sample);
 Year start = range.getStart();
 Year end = range.getEnd();
 assertEquals(new Year(1985), start);
 assertEquals(new Year(1001), end);
 }

 @Test
 public void testComputeRangeWithInvalidStartYearAndValidEndYear() {
 TucsonSimple format = new TucsonSimple();
 Sample sample = createSample("id19850630");
 Range range = format.computeRange(sample);
 Year start = range.getStart();
 Year end = range.getEnd();
 assertEquals(new Year(1001), start);
 assertEquals(new Year(1985), end);
 }

 @Test
 public void testComputeRangeWithInvalidStartYearAndInvalidEndYear() {
 TucsonSimple format = new TucsonSimple();
 Sample sample = createSample("id19850630");
 Range range = format.computeRange(sample);
 Year start = range.getStart();
 Year end = range.getEnd();
 assertEquals(new Year(1001), start);
 assertEquals(new Year(1001), end);
 }

 @Test
 public void testSave() throws IOException {
 Sample sample = createSample("id19900101", new String[]{"1", "2", "3"}, new int[]{1, 2, 3});
 TucsonSimple format = new TucsonSimple();
 StringWriter writer = new StringWriter();
 BuffereredWriter bufferedWriter = new BuffereredWriter(writer);
 format.save(sample, bufferedWriter);
 String expected = "0000011990 1\n" +
                 "0000021990 2\n" +
                 "0000031990 3\n" +
                 "   999\n";
 assertEquals(expected, writer.toString());
 }

}