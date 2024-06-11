import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import corina.formats.TucsonSimple;
import corina.formats.Filetype;
import corina.util.StringUtils;
import corina.util.Year;
import corina.ui.I18n;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.Map;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private Sample sample;
 private BufferedWriter writer;
 private StringWriter stringWriter;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 String data = "2010 10 15 25 33 41 52 65 78 90 100 110 120 130 140 150 160 170 180 190 200 210 225 240 255 270 285 295 305 315 325 335 345 355 365 375 385 395 400";
 sample = new Sample("sample", "id123", "data", new Range(new Year(2010), new Year(2010)), new HashMap<String, String>() {{ put("id", "id123"); }} );
 sample.setData(new int[41]);
 for (int i = 0; i < data.split(" ").length; i++) {
 sample.getData()[i] = Integer.parseInt(data.split(" ")[i]);
 }
 stringWriter = new StringWriter();
 writer = new BufferedWriter(stringWriter);
 }

 @Test
 public void testToString() {
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testDefaultExtension() {
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoadThrowsException() throws IOException {
 BufferedReader reader = new BufferedReader(new StringReader(""));
 tucsonSimple.load(reader);
 fail("load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 String code = tucsonSimple.make6digitCode(sample);
 assertEquals("000123", code);

 Sample sample2 = new Sample("sample2", null, null, null, null);
 String code2 = tucsonSimple.make6digitCode(sample2);
 assertEquals("000000", code2);
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year(2010));
 assertEquals("1234562010", writer.getBuffer().toString());

 Year year = new Year(2009);
 tucsonSimple.saveRowHeader(writer, sample, "123456", 6, year);
 assertEquals("1234562009", writer.getBuffer().toString());
 }

 @Test
 public void testSaveData() throws IOException {
 tucsonSimple.saveData(sample, writer);
 String expectedOutput = "1234562010   25\n" +
                        "1234562010   33\n" +
                        "1234562010   41\n" +
                        "1234562010   52\n" +
                        "1234562010   65\n" +
                        "1234562010   78\n" +
                        "1234562010   90\n" +
                        "1234562010  100\n" +
                        "1234562010  110\n" +
                        "1234562010  120\n" +
                        "1234562010  130\n" +
                        "1234562010  140\n" +
                        "1234562010  150\n" +
                        "1234562010  160\n" +
                        "1234562010  170\n" +
                        "1234562010  180\n" +
                        "1234562010  190\n" +
                        "1234562010  200\n" +
                        "1234562010  210\n" +
                        "1234562010  225\n" +
                        "1234562010  240\n" +
                        "1234562010  255\n" +
                        "1234562010  270\n" +
                        "1234562010  285\n" +
                        "1234562010  295\n" +
                        "1234562010  305\n" +
                        "1234562010  315\n" +
                        "1234562010  325\n" +
                        "1234562010  335\n" +
                        "1234562010  345\n" +
                        "1234562010  355\n" +
                        "1234562010  365\n" +
                        "1234562010  375\n" +
                        "1234562010  385\n" +
                        "1234562010  395\n" +
                        "1234562010  400\n" +
                        "\n";
 assertEquals(expectedOutput, writer.getBuffer().toString());
 }

 @Test
 public void testComputeRange() {
 Year start = new Year(1980);
 Range range = tucsonSimple.computeRange(sample);
 Year expectedStart = new Year(1981);
 assertEquals(expectedStart, range.getStart());

 Year expectedEnd = new Year(1981);
 assertEquals(expectedEnd, range.getEnd());
 }

 @Test
 public void testSave() throws IOException {
 tucsonSimple.save(sample, writer);
 assertEquals(expectedOutput, writer.getBuffer().toString());
 }
}