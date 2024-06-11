package corina.formats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.*;

import org.junit.*;
import org.junit.rules.*;
import org.mockito.*;

import corina.formats.*;
import corina.ui.*;
import corina.util.*;
import corina.*;

public class TucsonSimpleTest {

 private TucsonSimple tucsonSimple;
 private BufferedReader mockReader;
 private BufferedWriter mockWriter;
 private Sample mockSample;

 @Before
 public void setUp() {
 tucsonSimple = new TucsonSimple();
 mockReader = mock(BufferedReader.class);
 mockWriter = mock(BufferedWriter.class);
 mockSample = Mockito.spy(new Sample());
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
 public void testLoadThrowsException() throws IOException {
 when(mockReader.readLine()).thenReturn("Invalid file content");
 tucsonSimple.load(mockReader);
 fail("Load method should have thrown WrongFiletypeException");
 }

 @Test
 public void testMake6digitCodeWithSampleId() {
 String expected = "123456";
 mockSample.meta.put("id", 123456);
 assertEquals(expected, tucsonSimple.make6digitCode(mockSample));
 }

 @Test
 public void testMake6digitCodeWithoutSampleId() {
 String expected = "000000";
 assertNotNull(tucsonSimple.make6digitCode(mockSample));
 assertEquals(expected, tucsonSimple.make6digitCode(mockSample).trim());
 }

 @Test
 public void testSaveRowHeaderWithStartYearBeforeSampleYear() throws IOException {
 String expected = "0000012345 1999";
 tucsonSimple.saveRowHeader(mockWriter, mockSample, "000001", 4, new Year(1999));
 assertEquals(expected, mockWriter.toString());
 reset(mockWriter);
 }

 @Test
 public void testSaveRowHeaderWithStartYearSameAsSampleYearAndNotProcessed() throws IOException {
 String expected = "0000012345 2001";
 tucsonSimple.saveRowHeader(mockWriter, mockSample, "000001", 6, new Year(2001));
 assertEquals(expected, mockWriter.toString());
 reset(mockWriter);
 }

 @Test
 public void testSaveRowHeaderWithStartYearSameAsSampleYearAndProcessed() throws IOException {
 String expected = "0000012345  2001";
 tucsonSimple.saveRowHeader(mockWriter, mockSample, "000001", 4, new Year(2001));
 assertEquals(expected, mockWriter.toString());
 }

 @Rule
 public ExpectedException thrown = ExpectedException.none();

 @Test
 public void testSaveDataThrowsExceptionWhenLoadFails() throws IOException, InterruptedException {
 when(mockReader.readLine()).thenThrow(IOException.class);
 thrown.expect(IOException.class);
 tucsonSimple.save(mockSample, mockWriter);
 }
}