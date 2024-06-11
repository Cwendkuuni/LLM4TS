import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.util.*;
import static corina.util.StringUtils.*;

import corina.formats.*;
import corina.ui.*;
import corina.*;

public class TucsonSimpleTest {

 private static final String TEST_FILE = "testFile.rwl";

 @BeforeAll
 static void setup() throws IOException {
 File file = new File(TEST_FILE);
 if (file.exists()) {
 file.delete();
 }
 }

 @AfterAll
 static void teardown() throws IOException {
 File file = new File(TEST_FILE);
 if (file.exists()) {
 file.delete();
 }
 }

 @Test
 void testToString() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 void testgetDefaultExtension() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test
 void testLoadThrowsWrongFiletypeException() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new StringReader("invalid data"));
 assertThrows(WrongFiletypeException.class, () -> tucsonSimple.load(reader));
 }

 @ParameterizedTest
 @MethodSource("provideSamplesForTestSave")
 void testSave(Sample sample, int expectedDataLength, int expectedCountLength) throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE));
 tucsonSimple.save(sample, writer);
 writer.close();

 File file = new File(TEST_FILE);
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line;
 String data = "";
 String count = "";
 int lineNumber = 1;
 while ((line = reader.readLine()) != null) {
 if (line.startsWith(" ")) {
 data += line.substring(1);
 } else {
 count += line;
 }
 lineNumber++;
 }
 reader.close();
 file.delete();

 assertEquals(expectedDataLength, data.split("\\s+").length);
 assertEquals(expectedCountLength, count.split("\\s+").length);
 assertThrows(WrongFiletypeException.class, () -> tucsonSimple.load(new BufferedReader(new StringReader("invalid data"))));
 }

 @Test
 void testMake6digitCodeWithIdPresent() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.meta.put("id", 123456);
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("123456", code);
 }

 @Test
 void testMake6digitCodeWithNoId() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("000000", code);
 }

 @Test
 void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE));
 Sample sample = new Sample();
 Year start = new Year(1995);
 String code = "123456";
 int yearWidth = 4;
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 writer.close();

 File file = new File(TEST_FILE);
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line = reader.readLine();
 reader.close();
 file.delete();
 assertEquals("1234561995", line);
 }

 @Test
 void testSaveDataWithNoIsSummedAndNoIsProcessed() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.data = new ArrayList<>();
 sample.data.add(new Number(1.0));
 sample.count = new ArrayList<>();
 sample.count.add(new Number(1));
 Range range = new Range(new Year(2000), new Year(2001));
 sample.range = range;
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE));
 tucsonSimple.save(sample, writer);
 writer.close();

 File file = new File(TEST_FILE);
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line;
 int lineNumber = 1;
 while ((line = reader.readLine()) != null) {
 if (line.startsWith(" ")) {
 assertEquals("1.0", line.substring(1));
 } else {
 assertEquals("1", line);
 }
 lineNumber++;
 }
 reader.close();
 file.delete();
 }

 @Test
 void testSaveDataWithIsSummedAndNoIsProcessed() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.data = new ArrayList<>();
 sample.data.add(new Number(1.0));
 sample.data.add(new Number(2.0));
 sample.count = new ArrayList<>();
 sample.count.add(new Number(1));
 sample.count.add(new Number(2));
 Range range = new Range(new Year(2000), new Year(2001));
 sample.range = range;
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE));
 tucsonSimple.save(sample, writer, true, false);
 writer.close();

 File file = new File(TEST_FILE);
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line;
 int lineNumber = 1;
 while ((line = reader.readLine()) != null) {
 if (line.startsWith(" ")) {
 assertEquals("1.0", line.substring(1));
 assertEquals("1", reader.readLine());
 } else if (line.equals("9990    ")) {
 assertEquals("2.0", reader.readLine());
 assertEquals("2", reader.readLine());
 }
 lineNumber++;
 }
 reader.close();
 file.delete();
 }

 @Test
 void testSaveDataWithNoIsSummedAndIsProcessed() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.data = new ArrayList<>();
 sample.data.add(new Number(1.0));
 sample.data.add(new Number(2.0));
 sample.count = new ArrayList<>();
 sample.count.add(new Number(1));
 sample.count.add(new Number(2));
 Range range = new Range(new Year(2001), new Year(2002));
 sample.range = range;
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE));
 tucsonSimple.save(sample, writer, false, true);
 writer.close();

 File file = new File(TEST_FILE);
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line;
 int lineNumber = 1;
 while ((line = reader.readLine()) != null) {
 if (line.startsWith("   999")) {
 assertEquals("1.0", reader.readLine());
 assertEquals("1", reader.readLine());
 assertEquals(" ", reader.readLine());
 } else if (line.startsWith("9990   ")) {
 assertEquals("2.0", reader.readLine());
 assertEquals("2", reader.readLine());
 }
 lineNumber++;
 }
 reader.close();
 file.delete();
 }

 @Test
 void testSaveDataWithIsSummedAndIsProcessed() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.data = new ArrayList<>();
 sample.data.add(new Number(1.0));
 sample.data.add(new Number(2.0));
 sample.data.add(new Number(3.0));
 sample.count = new ArrayList<>();
 sample.count.add(new Number(1));
 sample.count.add(new Number(2));
 sample.count.add(new Number(3));
 Range range = new Range(new Year(2001), new Year(2003));
 sample.range = range;
 BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE));
 tucsonSimple.save(sample, writer, true, true);
 writer.close();

 File file = new File(TEST_FILE);
 BufferedReader reader = new BufferedReader(new FileReader(file));
 String line;
 int lineNumber = 1;
 while ((line = reader.readLine()) != null) {
 if (line.startsWith("   999")) {
 assertEquals("1.0", reader.readLine());
 assertEquals("1", reader.readLine());
 assertEquals("", reader.readLine());
 assertEquals("", reader.readLine());
 } else if (line.equals("9990    ")) {
 assertEquals("2.0", reader.readLine());
 assertEquals("2", reader.readLine());
 assertEquals("", reader.readLine());
 assertEquals("", reader.readLine());
 } else if (line.equals("9990  0   ")) {
 assertEquals("3.0", reader.readLine());
 assertEquals("3", reader.readLine());
 }
 lineNumber++;
 }
 reader.close();
 file.delete();
 }

 @Test
 void testComputeRange() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year start = new Year(2001);
 Range expected = new Range(start, start.add(1));
 Range actual = tucsonSimple.computeRange(sample);
 assertEquals(expected, actual);
 }

 @Test
 void testComputeRangeWithStartBeforeYear1() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year start = new Year(1900);
 Range expected = new Range(new Year(1001), new Year(1001));
 Range actual = tucsonSimple.computeRange(sample);
 assertEquals(expected, actual);
 }

 @Test
 void testComputeRangeWithStartAfterYear8000() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 Year start = new Year(99999);
 Range expected = new Range(new Year(10001), new Year(10001));
 Range actual = tucsonSimple.computeRange(sample);
 assertEquals(expected, actual);
 }

 private static Stream<Arguments> provideSamplesForTestSave() {
 return Stream.of(
 Arguments.arguments(new Sample(), 1, 1),
 Arguments.arguments(new Sample(new Number(1.0), new Number(2.0), new Year(2000), new Range(new Year(2000), new Year(2001))), 2, 2),
 Arguments.arguments(new Sample(new Number(1.0), new Number(2.0), new Number(3.0), new Number(4.0), new Year(2000), new Range(new Year(2001), new Year(2003))), 4, 3)
 );
 }
}