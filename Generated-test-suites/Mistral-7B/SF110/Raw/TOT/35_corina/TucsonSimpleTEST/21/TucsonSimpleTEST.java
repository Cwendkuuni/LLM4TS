package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import corina.Sample;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

 private BufferedReader reader;
 private BufferedWriter writer;

 @Before
 public void setUp() throws IOException {
 reader = new BufferedReader(new StringReader(""));
 writer = new BufferedWriter(new FileWriter("output.txt"));
 }

 @After
 public void tearDown() throws IOException {
 writer.close();
 }

 @Test
 public void testToString() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("format.tucsonsimple", tucsonSimple.toString());
 }

 @Test
 public void testgetDefaultExtension() {
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals(".RWL", tucsonSimple.getDefaultExtension());
 }

 @Test(expected = WrongFiletypeException.class)
 public void testLoad() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 tucsonSimple.load(reader);
 fail("Load method should throw WrongFiletypeException");
 }

 @Test
 public void testMake6digitCode() {
 Sample sample = new Sample();
 sample.setMeta("id", "123456");
 TucsonSimple tucsonSimple = new TucsonSimple();
 assertEquals("123456", tucsonSimple.make6digitCode(sample));

 Sample sample2 = new Sample();
 assertEquals("000000", tucsonSimple.make6digitCode(sample2));
 }

 @Test
 public void testSaveRowHeader() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Year year = new Year(2001);
 Sample sample = new Sample();
 String code = "ABCDEF";

 tucsonSimple.saveRowHeader(writer, sample, code, 6, year);
 String expectedOutput = code + "2001";
 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testSaveData() throws IOException {
 TucsonSimple tucsonSimple = mock(TucsonSimple.class);
 Sample sample = new Sample();
 Year start = new Year(2001);
 Year end = new Year(2003);
 Year year = new Year(2001);
 String code = "ABCDEF";

 doReturn(start).when(tucsonSimple).computeRange(sample);
 doReturn(end).when(tucsonSimple).computeRange(sample);
 doNothing().when(tucsonSimple).saveRowHeader(writer, sample, code, 6, year);

 tucsonSimple.saveData(sample, writer);

 String expectedOutput = code + "2001\n" +
                          "123.45\n" +
                          "456.78\n" +
                          "999\n" +
                          "9990  0\n" +
                          "111.11\n" +
                          "222.22\n" +
                          "999\n" +
                          "9990  0\n" +
                          "333.33\n" +
                          "999\n" +
                          "9990  0\n" +
                          "444.44\n" +
                          "999\n" +
                          "9990  0\n" +
                          "555.55\n" +
                          "999\n" +
                          "9990  0\n" +
                          "666.66\n";

 assertEquals(expectedOutput, writer.toString());
 }

 @Test
 public void testSave() throws IOException {
 TucsonSimple tucsonSimple = new TucsonSimple();
 Sample sample = new Sample();

 tucsonSimple.save(sample, writer);
 fail("Save method should not be called directly");
 }
}