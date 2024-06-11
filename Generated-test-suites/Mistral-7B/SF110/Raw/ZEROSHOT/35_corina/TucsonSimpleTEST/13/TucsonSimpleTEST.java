import org.junit.Test;
import org.junit.Assert;
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

 @Test
 public void testToString() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Assert.assertNotNull(tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Assert.assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadException() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
 tucsonSimple.load(reader);
 }

 @Test
 public void testMake6digitCode() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.setMeta(new HashMap<String, Object>() {{
 put("id", "123456");
 }});
 Assert.assertEquals("123456", tucsonSimple.make6digitCode(sample));

 Sample sample2 = new Sample();
 Assert.assertEquals("000000", tucsonSimple.make6digitCode(sample2));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.setMeta(new HashMap<String, Object>() {{
 put("id", "123456");
 }});
 String code = "ABCDEF";
 int yearWidth = 4;
 Year start = new Year(2000);
 Year end = new Year(2001);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
 Assert.assertEquals("ABCDEF2000", writer.toString());

 Year start2 = new Year(2001);
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start2);
 Assert.assertEquals("ABCDEF2001", writer.toString());
 }

 @Test
 public void testSaveData() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.setData(new Double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0});
 sample.setCount(new Double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0});
 sample.setMeta(new HashMap<String, Object>() {{
 put("id", "123456");
 }});
 Year start = new Year(2000);
 Year end = new Year(2001);
 tucsonSimple.saveData(sample, writer);

 String expectedOutput = "123456  2000\n" +
 "1.0  1.0\n" +
 "2.0  2.0\n" +
 "3.0  3.0\n" +
 "4.0  4.0\n" +
 "5.0  5.0\n" +
 "6.0  6.0\n" +
 "7.0  7.0\n" +
 "8.0  8.0\n" +
 "9.0  9.0\n" +
 "10.0 10.0\n" +
 "    \n" +
 "9990  0\n";
 Assert.assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testComputeRange() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();
 sample.setRange(new Range(new Year(1), new Year(1000)));
 Year start = sample.getRange().getStart();
 Assert.assertEquals(new Year(1), tucsonSimple.computeRange(sample).getStart());

 Sample sample2 = new Sample();
 sample2.setRange(new Range(new Year(8001), new Year(10000)));
 Assert.assertEquals(sample2.getRange().redateBy(8000), tucsonSimple.computeRange(sample2));

 Sample sample3 = new Sample();
 sample3.setRange(new Range(new Year(-1), new Year(1001)));
 Assert.assertEquals(sample3.getRange().redateStartTo(new Year(1001)), tucsonSimple.computeRange(sample3));
 }

 @Test
 public void testSave() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 Sample sample = new Sample();
 sample.setData(new Double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0});
 sample.setCount(new Double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0});
 sample.setMeta(new HashMap<String, Object>() {{
 put("id", "123456");
 }});
 sample.setRange(new Range(new Year(2000), new Year(2001)));
 tucsonSimple.save(sample, writer);
 String expectedOutput = "123456  2000\n" +
 "1.0  1.0\n" +
 "2.0  2.0\n" +
 "3.0  3.0\n" +
 "4.0  4.0\n" +
 "5.0  5.0\n" +
 "6.0  6.0\n" +
 "7.0  7.0\n" +
 "8.0  8.0\n" +
 "9.0  9.0\n" +
 "10.0 10.0\n" +
 "    \n" +
 "9990  0\n";
 Assert.assertEquals(expectedOutput, writer.toString());
 }
}