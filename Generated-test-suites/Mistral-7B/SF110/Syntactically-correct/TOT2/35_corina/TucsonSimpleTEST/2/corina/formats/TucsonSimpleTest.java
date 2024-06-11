import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;

    @Before
    public void setup() {
        tucsonSimple = new TucsonSimple();
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

    @Test
    public void testMake6digitCodeWithID() {
        Sample sample = new Sample("sampleName", new Range(new Year(2000), new Year(2001)), new ArrayList<>());
        sample.meta.put("id", "123456");
        String expected = "123456";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testMake6digitCodeWithoutID() {
        Sample sample = new Sample("sampleName", new Range(new Year(2000), new Year(2001)), new ArrayList<>());
        String expected = "000000";
        String actual = tucsonSimple.make6digitCode(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testSaveRowHeaderWithValidStart() throws IOException {
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        Sample sample = new Sample("sampleName", new Range(new Year(2000), new Year(2001)), new ArrayList<>());
        String code = "001234";
        int yearWidth = 6;
        String prefix = "2000";
        when(bufferedWriter.write(anyString())).thenReturn(true);
        tucsonSimple.saveRowHeader(bufferedWriter, sample, code, yearWidth, new Year(2000));
        verify(bufferedWriter).write(eq("001232000"));
    }

    @Test
    public void testSaveRowHeaderWithValidStartAndProcessed() throws IOException {
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        Sample sample = new Sample("sampleName", new Range(new Year(2000), new Year(2001)), new ArrayList<>());
        String code = "001234";
        int yearWidth = 4;
        String prefix = "2000";
        when(bufferedWriter.write(anyString())).thenReturn(true);
        tucsonSimple.saveRowHeader(bufferedWriter, sample, code, yearWidth, new Year(2000), true);
        verify(bufferedWriter).write(eq("001232000"));
    }

    @Test(expected = IOException.class)
    public void testSaveRowHeaderWithInvalidYearWidth() throws IOException {
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        Sample sample = new Sample("sampleName", new Range(new Year(2000), new Year(2001)), new ArrayList<>());
        String code = "001234";
        int yearWidth = -1;
        tucsonSimple.saveRowHeader(bufferedWriter, sample, code, yearWidth, new Year(2000));
    }

    @Test(expected = IOException.class)
    public void testSaveDataWithInvalidStartAndIsProcessed() throws IOException {
        Sample sample = new Sample("sampleName", new Range(new Year(-1), new Year(-1)), new ArrayList<>());
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        tucsonSimple.saveData(sample, bufferedWriter);
    }

    @Test(expected = IOException.class)
    public void testSaveDataWithInvalidEndAndIsProcessed() throws IOException {
        Sample sample = new Sample("sampleName", new Range(new Year(1), new Year(1)), new ArrayList<>());
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        tucsonSimple.saveData(sample, bufferedWriter);
    }

    @Test
    public void testSaveDataWithValidData() throws IOException {
        Sample sample = new Sample("sampleName", new Range(new Year(2000), new Year(2001)), new ArrayList<>());
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        String data = "5";
        when(bufferedWriter.write(anyString())).thenReturn(true);
        when(bufferedWriter.newLine()).thenReturn(true);
        tucsonSimple.saveData(sample, bufferedWriter);
        verify(bufferedWriter, times(13)).write(anyString());
    }

    @Test
    public void testSaveDataWithValidDataAndIsProcessed() throws IOException {
        Sample sample = new Sample("sampleName", new Range(new Year(2000), new Year(2001)), new ArrayList<>());
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        String data = "5";
        when(bufferedWriter.write(anyString())).thenReturn(true);
        when(bufferedWriter.newLine()).thenReturn(true);
        tucsonSimple.saveData(sample, bufferedWriter, true);
        verify(bufferedWriter, times(13)).write(anyString());
    }

    @Test
    public void testSaveDataWithValidDataAndIsSummed() throws IOException {
        Sample sample = new Sample("sampleName", new Range(new Year(2000), new Year(2001)), new ArrayList<>());
        BufferedWriter bufferedWriter = mock(BufferedWriter.class);
        String data = "5";
        when(bufferedWriter.write(anyString())).thenReturn(true);
        when(bufferedWriter.newLine()).thenReturn(true);
        tucsonSimple.saveData(sample, bufferedWriter, true, true);
        verify(bufferedWriter, times(15)).write(anyString());
    }

    @Test
    public void testComputeRangeWhenStartIsLessThan1() {
        Sample sample = new Sample("sampleName", new Range(new Year(-1), new Year(-1)), new ArrayList<>());
        Range expected = new Range(new Year(1001), new Year(1001));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRangeWhenStartIsBetween1And8000() {
        Sample sample = new Sample("sampleName", new Range(new Year(4000), new Year(4001)), new ArrayList<>());
        Range expected = new Range(new Year(4000), new Year(4001));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRangeWhenStartIsGreaterThan8000() {
        Sample sample = new Sample("sampleName", new Range(new Year(8001), new Year(8002)), new ArrayList<>());
        Range expected = new Range(new Year(8001), new Year(8002)).redateStartTo(new Year(1001));
        Range actual = tucsonSimple.computeRange(sample);
        assertEquals(expected, actual);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadThrowsWrongFiletypeException() throws IOException {
        TucsonSimple tucsonSimple = mock(TucsonSimple.class);
        BufferedReader bufferedReader = mock(BufferedReader.class);
        when(bufferedReader.readLine()).thenReturn("not a valid file");
        when(bufferedReader.readLine()).thenReturn(null);
        Sample sample = tucsonSimple.load(bufferedReader);
        assertNull(sample);
    }
}