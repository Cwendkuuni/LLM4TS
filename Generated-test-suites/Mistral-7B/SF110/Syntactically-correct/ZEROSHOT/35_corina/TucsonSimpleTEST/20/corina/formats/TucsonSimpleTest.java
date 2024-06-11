import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import corina.formats.TucsonSimple;
import corina.model.Sample;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private Sample sample;
 private BufferedWriter writer;
 private File file;

 @Before
 public void setUp() throws Exception {
 tucsonSimple = new TucsonSimple();
 sample = new Sample();
 sample.setName("Test Sample");
 sample.setData(new double[]{-1.2, 3.1, 5.5, 2.0, 7.4, 1.3, 6.2, 4.5, 8.7, 9.9});
 sample.setCount(new int[]{1, 2, 3, 1, 4, 3, 2, 1, 5, 6});
 sample.setRange(new Year(1995), new Year(1996));
 }

 @After
 public void tearDown() throws Exception {
 if (file != null) {
 file.delete();
 }
 }

 @Test
 public void testToString() throws Exception {
 String expected = "format.tucsonsimple";
 String actual = tucsonSimple.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testgetDefaultExtension() throws Exception {
 String expected = ".RWL";
 String actual = tucsonSimple.getDefaultExtension();
 assertEquals(expected, actual);
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws Exception {
 BufferedReader reader = new BufferedReader(new FileReader("testFile.rwl"));
 Sample loadedSample = tucsonSimple.load(reader);
 fail("Should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() throws Exception {
 String sampleId = "123456";
 String expected = sampleId;
 String actual = tucsonSimple.make6digitCode(sample);
 sample.setMeta(new HashMap<>());
 sample.getMeta().put("id", sampleId);
 String idlessSampleId = "000001";
 String actualIdless = tucsonSimple.make6digitCode(sample);
 assertEquals(expected, actual);
 assertEquals(idlessSampleId, actualIdless);
 }

 @Test
 public void testSaveRowHeader() throws Exception {
 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("testFile.rwl")));
 tucsonSimple.saveRowHeader(writer, sample, "123456", 6, new Year(1995));
 String expected = "1234561995";
 String actual = readFile("testFile.rwl");
 assertEquals(expected, actual);
 }

 @Test
 public void testSaveData() throws Exception {
 writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("testFile.rwl")));
 tucsonSimple.saveData(sample, writer);
 String expected = "1.2 1\n3.1 2\n5.5 3\n2.0 1\n4.5 4\n7.4 5\n1.3 6\n6.2 7\n4.5 8\n8.7 9\n9.9 10\n999\n";
 String actual = readFile("testFile.rwl");
 assertEquals(expected, actual);
 }

 @Test
 public void testComputeRange() throws Exception {
 Year expectedStart = new Year(1995);
 Year expectedEnd = new Year(1996);
 Year actualStart = tucsonSimple.computeRange(sample).getStart();
 Year actualEnd = tucsonSimple.computeRange(sample).getEnd();
 assertEquals(expectedStart, actualStart);
 assertEquals(expectedEnd, actualEnd);
 }

 @Test
 public void testSave() throws Exception {
 writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("testFile.rwl")));
 tucsonSimple.save(sample, writer);
 String expected = "1.2 1\n3.1 2\n5.5 3\n2.0 1\n4.5 4\n7.4 5\n1.3 6\n6.2 7\n4.5 8\n8.7 9\n9.9 10\n999\n";
 String actual = readFile("testFile.rwl");
 assertEquals(expected, actual);
 }

 private String readFile(String fileName) throws IOException {
 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
 byte[] buffer = new byte[4096];
 int bytesRead = 0;
 BufferedReader inputReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
 while ((bytesRead = inputReader.read(buffer)) != -1) {
 outputStream.write(buffer, 0, bytesRead);
 }
 inputReader.close();
 return outputStream.toString();
 }
}