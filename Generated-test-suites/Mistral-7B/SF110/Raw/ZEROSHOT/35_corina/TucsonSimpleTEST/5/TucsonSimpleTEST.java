import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TucsonSimpleTest {

 private static final String I18N_TEXT = "format.tucsonsimple";
 private static final String SAMPLE_DATA = "{\"meta\":{\"id\":\"sample1\"},\"range\":{\"start\":{\"year\":1995,\"month\":1,\"day\":1},\"end\":{\"year\":1995,\"month\":1,\"day\":31}},\"data\":[1,2,3,4],\"count\":[1,2,3,4]}";

 private TucsonSimple tucsonSimple;
 private BufferedWriter writer;
 private StringWriter stringWriter;

 @Before
 public void setup() {
 tucsonSimple = new TucsonSimple();
 stringWriter = new StringWriter();
 writer = new BufferedWriter(stringWriter);
 }

 @After
 public void teardown() throws IOException {
 writer.close();
 }

 @Test
 public void testToString() {
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
 tucsonSimple.load(null);
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "sample1");
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("sample1", code);

 sample.meta.clear();
 code = tucsonSimple.make6digitCode(sample);
 assertEquals("000000", code);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Sample sample = new Sample();
 Year year = new Year(1995);
 String code = "sample1";
 int yearWidth = 6;
 tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
 assertEquals("sample11995", stringWriter.toString());
 }

 @Test
 public void testSaveData() throws IOException {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "sample1");
 sample.range = new Range();
 sample.range.setStart(new Year(1995));
 sample.range.setEnd(new Year(1995));
 sample.data = Arrays.asList(1, 2, 3, 4);
 sample.count = Arrays.asList(1, 2, 3, 4);

 Year start = new Year(1995);
 Year end = new Year(1995);
 String code = "sample1";
 boolean isSummed = false;
 boolean isProcessed = false;
 tucsonSimple.saveData(sample, writer);

 assertEquals("sample11995  1 1\n" +
 "sample11995  2 2\n" +
 "sample11995  3 3\n" +
 "sample11995  4 4\n", stringWriter.toString());
 }

 @Test
 public void testComputeRange() {
 Sample sample = new Sample();
 sample.range = new Range();
 sample.range.setStart(new Year(-1));

 Year start = new Year(-1);
 Year end = new Year(-1);
 Range range = tucsonSimple.computeRange(sample);
 assertEquals(start, range.getStart());
 assertEquals(start, range.getEnd());

 sample.range.setStart(new Year(1995));
 range = tucsonSimple.computeRange(sample);
 assertEquals(sample.range.getStart(), range.getStart());
 assertEquals(sample.range.getEnd(), range.getEnd());

 sample.range.setStart(new Year(8001));
 range = tucsonSimple.computeRange(sample);
 assertEquals(new Year(8001), range.getStart());
 assertEquals(new Year(8001), range.getEnd());

 sample.range.setStart(new Year(1000));
 range = tucsonSimple.computeRange(sample);
 assertEquals(new Year(1001), range.getStart());
 assertEquals(new Year(1001), range.getEnd());
 }

 @Test
 public void testSave() throws IOException {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "sample1");
 sample.range = new Range();
 sample.range.setStart(new Year(1995));
 sample.range.setEnd(new Year(1995));
 sample.data = Arrays.asList(1, 2, 3, 4);
 sample.count = Arrays.asList(1, 2, 3, 4);

 String code = "sample1";
 boolean isSummed = false;
 boolean isProcessed = false;
 tucsonSimple.save(sample, writer);
 assertEquals("sample11995  1 1\n" +
 "sample11995  2 2\n" +
 "sample11995  3 3\n" +
 "sample11995  4 4\n", stringWriter.toString());
 }

 @Test
 public void testSaveWithProcessedAndSummed() throws IOException {
 Sample sample = new Sample();
 sample.meta = new HashMap<>();
 sample.meta.put("id", "sample1");
 sample.range = new Range();
 sample.range.setStart(new Year(1995));
 sample.range.setEnd(new Year(1995));
 sample.data = Arrays.asList(1, 2, 3, 4);
 sample.count = Arrays.asList(1, 2, 3, 4);

 String code = "sample1";
 boolean isSummed = true;
 boolean isProcessed = true;
 tucsonSimple.save(sample, writer);
 assertEquals("sample11995  1 1\n" +
 "sample11995  2 2\n" +
 "sample11995  3 3\n" +
 "sample11995  4 4\n" +
 "           \n" +
 "9990  0\n", stringWriter.toString());
 }

}