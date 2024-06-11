import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private ByteArrayOutputStream outputStream;
 private ByteArrayInputStream inputStream;
 private BufferedWriter writer;
 private BufferedReader reader;

 @Before
 public void setup() {
 tucsonSimple = new TucsonSimple();
 outputStream = new ByteArrayOutputStream();
 inputStream = new ByteArrayInputStream(new byte[0]);
 writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 reader = new BufferedReader(new InputStreamReader(inputStream));
 }

 @After
 public void tearDown() {
 try {
 writer.close();
 reader.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 tucsonSimple = null;
 }

 @Test
 public void testToString() {
 String expected = I18n.getText("format.tucsonsimple");
 String actual = tucsonSimple.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testgetDefaultExtension() {
 String expected = ".RWL";
 String actual = tucsonSimple.getDefaultExtension();
 assertEquals(expected, actual);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadThrowsException() throws IOException {
 BufferedReader mockReader = mock(BufferedReader.class);
 when(mockReader.readLine()).thenReturn(null);
 Sample expectedSample = new Sample(new HashMap<>(), new Range(new Year(1), new Year(1)), null, null, null);
 when(mockReader.readLine()).thenReturn("Sample data");
 when(mockReader.readLine()).thenReturn("End of file");
 Sample actualSample = tucsonSimple.load(mockReader);
 fail("load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 Map<String, String> meta = new HashMap<>();
 meta.put("id", "123456");
 Sample sample = new Sample(meta, new Range(new Year(1), new Year(1)), null, null, null);
 String expectedCode = "123456";
 String actualCode = tucsonSimple.make6digitCode(sample);
 assertEquals(expectedCode, actualCode);

 meta.clear();
 meta.put("id", "TestID");
 Sample sampleWithLongID = new Sample(meta, new Range(new Year(1), new Year(1)), null, null, null);
 String expectedCodeWithSpace = "TestID   ";
 String actualCodeWithSpace = tucsonSimple.make6digitCode(sampleWithLongID);
 assertEquals(expectedCodeWithSpace, actualCodeWithSpace);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Sample sample = new Sample(new HashMap<>(), new Range(new Year(2000), new Year(2000)), null, null, null);
 String expectedCode = "000001";
 int yearWidth = 6;
 Year year = new Year(2000);
 tucsonSimple.saveRowHeader(writer, sample, expectedCode, yearWidth, year);
 String actualCode = outputStream.toString().trim();
 assertEquals(expectedCode, actualCode);
 }

 @Test
 public void testSaveData() throws IOException {
 Map<Integer, Double> data = new HashMap<>();
 data.put(0, 1.0);
 data.put(1, 2.0);
 data.put(2, 3.0);
 Map<Integer, Integer> count = new HashMap<>();
 count.put(0, 1);
 count.put(1, 2);
 count.put(2, 3);
 Sample sample = new Sample(new HashMap<>(), new Range(new Year(2000), new Year(2001)), data, count, null);
 String expectedOutput = "000001 2000\n" +
 "1 1.0\n" +
 "2 2.0\n" +
 "3 3.0\n" +
 "   999\n";
 tucsonSimple.saveData(sample, writer);
 String actualOutput = outputStream.toString().trim();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testComputeRange() {
 Sample sample = new Sample(new HashMap<>(), new Range(new Year(1999), new Year(2000)), null, null, null);
 Range expectedRange = new Range(new Year(1999), new Year(2000));
 Range actualRange = tucsonSimple.computeRange(sample);
 assertEquals(expectedRange, actualRange);
 }

 @Test
 public void testSave() throws IOException {
 Map<Integer, Double> data = new HashMap<>();
 data.put(0, 1.0);
 data.put(1, 2.0);
 data.put(2, 3.0);
 Map<Integer, Integer> count = new HashMap<>();
 count.put(0, 1);
 count.put(1, 2);
 count.put(2, 3);
 Sample sample = new Sample(new HashMap<>(), new Range(new Year(2000), new Year(2001)), data, count, null);
 String expectedOutput = "000001 2000\n" +
 "1 1.0\n" +
 "2 2.0\n" +
 "3 3.0\n" +
 "   999\n";
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
 tucsonSimple.save(sample, writer);
 String actualOutput = outputStream.toString().trim();
 assertEquals(expectedOutput, actualOutput);
 }
}