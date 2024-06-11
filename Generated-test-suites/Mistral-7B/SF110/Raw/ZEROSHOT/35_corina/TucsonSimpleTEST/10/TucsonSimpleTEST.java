import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Year;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.sample.Sample;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedWriter writerMock;
 private ByteArrayOutputStream output;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 writerMock = Mockito.mock(BufferedWriter.class);
 output = new ByteArrayOutputStream();
 }

 @Test
 public void testToString() {
 Assert.assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 Assert.assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
 tucsonSimple.load(Mockito.mock(BufferedReader.class));
 }

 @Test
 public void testMake6digitCode() throws Exception {
 Sample sample = new Sample();
 sample.setMeta("id", "12345678");
 Assert.assertEquals("123456", tucsonSimple.make6digitCode(sample));

 Sample sampleWithoutId = new Sample();
 Assert.assertEquals("000000", tucsonSimple.make6digitCode(sampleWithoutId));
 }

 @Test
 public void testSaveData() throws IOException {
 Sample sample = new Sample();
 sample.setData(new int[]{-1, -2, -3, -4, -5, -6, -7, -8, -9});
 sample.setCount(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
 Year start = new Year(2000);
 Year end = new Year(2001);
 Year year = start;
 tucsonSimple.saveData(sample, writerMock);

 String expectedOutput = createExpectedOutput();
 Mockito.verify(writerMock).write(expectedOutput.split("\n"));
 }

 @Test
 public void testSaveData_isProcessedAndIsSummed() throws IOException {
 Sample sample = new Sample();
 sample.setData(new int[]{-1, -2, -3, -4, -5, -6, -7, -8, -9});
 sample.setCount(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
 Year start = new Year(2000);
 Year end = new Year(2002);
 Year year = start;
 tucsonSimple.setProcessed(true);
 tucsonSimple.setSummed(true);
 tucsonSimple.saveData(sample, writerMock);

 String expectedOutput = createExpectedOutput_isProcessedAndIsSummed();
 Mockito.verify(writerMock).write(expectedOutput.split("\n"));
 }

 @Test
 public void testSaveData_isProcessedAndIsNotSummed() throws IOException {
 Sample sample = new Sample();
 sample.setData(new int[]{-1, -2, -3, -4, -5, -6, -7, -8, -9});
 sample.setCount(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
 Year start = new Year(2000);
 Year end = new Year(2002);
 Year year = start;
 tucsonSimple.setProcessed(true);
 tucsonSimple.setSummed(false);
 tucsonSimple.saveData(sample, writerMock);

 String expectedOutput = createExpectedOutput_isProcessedAndIsNotSummed();
 Mockito.verify(writerMock).write(expectedOutput.split("\n"));
 }

 @Test
 public void testComputeRange() throws Exception {
 Sample sample = new Sample();
 Year start = new Year(1995);
 Range range = new Range(start, start.add(1));
 Assert.assertEquals(range, tucsonSimple.computeRange(sample));

 Sample sample2 = new Sample();
 Year start2 = new Year(1995);
 Year end2 = new Year(1996);
 Range range2 = new Range(start2, end2);
 Assert.assertEquals(range2, tucsonSimple.computeRange(sample2));

 Sample sample3 = new Sample();
 Year start3 = new Year(1995);
 Year end3 = new Year(2000);
 Range range3 = new Range(start3, end3.add(1));
 Assert.assertEquals(range3, tucsonSimple.computeRange(sample3));

 Sample sample4 = new Sample();
 Year start4 = new Year(2001);
 Range range4 = new Range(start4, start4.add(1));
 Assert.assertEquals(range4, tucsonSimple.computeRange(sample4));
}

 @Test
 public void testSave() throws IOException {
 Sample sample = new Sample();
 sample.setData(new int[]{-1, -2, -3, -4, -5, -6, -7, -8, -9});
 sample.setCount(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
 Year start = new Year(2000);
 Year end = new Year(2001);
 tucsonSimple.save(sample, writerMock);

 String expectedOutput = createExpectedOutput();
 Mockito.verify(writerMock).write(expectedOutput.split("\n"));
 }

 @After
 public void tearDown() throws Exception {
 tucsonSimple = null;
 writerMock.close();
 output.close();
 }

 private String createExpectedOutput() {
 StringWriter stringWriter = new StringWriter(output);
 BufferedReader reader = new BufferedReader(new StringReader(output.toString()));
 tucsonSimple.saveData(Mockito.mock(Sample.class), writerMock);
 String result = "";
 String line;
 while ((line = reader.readLine()) != null) {
 result += line + "\n";
 }
 return result;
 }

 private String createExpectedOutput_isProcessedAndIsSummed() {
 StringWriter stringWriter = new StringWriter(output);
 BufferedWriter writer = new BufferedWriter(new StringWriter(output));
 tucsonSimple.saveData(Mockito.mock(Sample.class), writer);
 String result = "";
 String line;
 while ((line = stringWriter.toString().split("\n")[0]).startsWith("000000")) {
 result += line.substring(6) + "\n";
 }
 return result;
 }

 private String createExpectedOutput_isProcessedAndIsNotSummed() {
 StringWriter stringWriter = new StringWriter(output);
 BufferedWriter writer = new BufferedWriter(new StringWriter(output));
 tucsonSimple.saveData(Mockito.mock(Sample.class), writer);
 String result = "";
 String line;
 while ((line = stringWriter.toString().split("\n")[0]).startsWith("000000")) {
 result += line.substring(6) + "   ";
 }
 return result;
 }
}