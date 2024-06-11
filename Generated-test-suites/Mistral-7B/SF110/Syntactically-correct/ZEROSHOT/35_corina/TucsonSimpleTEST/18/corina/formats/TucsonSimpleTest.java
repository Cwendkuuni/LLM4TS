import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import org. mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TucsonSimpleTest {

 private static TucsonSimple tucsonSimple;
 private static BufferedWriter writer;
 private static BufferedReader reader;

 @BeforeClass
 public static void setup() {
 tucsonSimple = new TucsonSimple();
 writer = new BufferedWriter(new OutputStreamWriter(new ByteArrayOutputStream()));
 reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
 }

 @AfterClass
 public static void tearDown() {
 try {
 writer.close();
 reader.close();
 } catch (IOException e) {
 // do nothing
 }
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
 Sample sample = Mockito.mock(Sample.class);
 BufferedReader reader = Mockito.mock(BufferedReader.class);
 when(reader.readLine()).thenReturn("dummy line");
 when(sample.meta.containsKey("id")).thenReturn(true);
 when(sample.meta.get("id")).thenReturn("123456");
 tucsonSimple.load(reader, sample);
 fail("load method should have thrown WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = Mockito.mock(Sample.class);
 when(sample.meta.containsKey("id")).thenReturn(true);
 when(sample.meta.get("id")).thenReturn("123456");
 String expected = "123456";
 String actual = tucsonSimple.make6digitCode(sample);
 assertEquals(expected, actual);

 Sample sample2 = Mockito.mock(Sample.class);
 when(sample2.meta.containsKey("id")).thenReturn(false);
 String actual2 = tucsonSimple.make6digitCode(sample2);
 assertEquals("000000", actual2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 Sample sample = Mockito.mock(Sample.class);
 when(sample.range.getStart()).thenReturn(new Year(2000));
 when(sample.meta.containsKey("id")).thenReturn(true);
 when(sample.meta.get("id")).thenReturn("123456");
 when(sample.range.getEnd()).thenReturn(new Year(2002));
 Year y = new Year(2001);
 String expected = "123456 2001";
 tucsonSimple.saveRowHeader(writer, sample, "123456", 6, y);
 String actual = writer.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveData() throws IOException {
 Sample sample = Mockito.mock(Sample.class);
 Year start = new Year(2000);
 Year end = new Year(2002);
 when(sample.range.getStart()).thenReturn(start);
 when(sample.range.getEnd()).thenReturn(end);
 when(sample.data.get(start.diff(start))).thenReturn(new Double(1.2));
 when(sample.count.get(start.diff(start))).thenReturn(new Integer(2));
 when(sample.data.get(start.diff(start).add(1))).thenReturn(new Double(3.4));
 when(sample.count.get(start.diff(start).add(1))).thenReturn(new Integer(3));
 when(sample.data.get(start.diff(start).add(2))).thenReturn(new Double(5.6));
 when(sample.count.get(start.diff(start).add(2))).thenReturn(new Integer(4));
 String expected = "1.2  2\n" +
 "3.4  3\n" +
 "5.6  4\n" +
 "    ";
 tucsonSimple.saveData(sample, writer);
 String actual = writer.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRange() {
 Sample sample = Mockito.mock(Sample.class);
 Year start = new Year(1999);
 when(sample.range.getStart()).thenReturn(start);
 Year expected = new Year(1001);
 Year actual = tucsonSimple.computeRange(sample).getStart();
 assertEquals(expected, actual);
 }

 @Test
 public void testSave() throws IOException {
 Sample sample = Mockito.mock(Sample.class);
 when(sample.range.getStart()).thenReturn(new Year(2000));
 when(sample.range.getEnd()).thenReturn(new Year(2002));
 when(sample.data.get(Integer.valueOf(0))).thenReturn(new Double(1.2));
 when(sample.count.get(Integer.valueOf(0))).thenReturn(new Integer(2));
 when(sample.data.get(Integer.valueOf(1))).thenReturn(new Double(3.4));
 when(sample.count.get(Integer.valueOf(1))).thenReturn(new Integer(3));
 when(sample.data.get(Integer.valueOf(2))).thenReturn(new Double(5.6));
 when(sample.count.get(Integer.valueOf(2))).thenReturn(new Integer(4));
 Map<String, Serializable> meta = new HashMap<>();
 meta.put("id", "123456");
 when(sample.meta).thenReturn(meta);
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream));
 tucsonSimple.save(sample, w);
 String expected = "1.2  2\n" +
 "3.4  3\n" +
 "5.6  4\n" +
 "    \n" +
 "123456 2000\n";
 String actual = writer.toString();
 assertEquals(expected, actual);
 }
}