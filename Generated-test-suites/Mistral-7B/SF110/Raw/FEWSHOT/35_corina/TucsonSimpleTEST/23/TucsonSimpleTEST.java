package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    @Test
    public void testComputeRange() {
        Sample sample = Mockito.mock(Sample.class);
        when(sample.range.getStart()).thenReturn(new Year(100));

        TucsonSimple tucsonSimple = new TucsonSimple();
        Range expectedRange = new Range(new Year(1001), new Year(1001));
        Range result = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange, result);

        when(sample.range.getStart()).thenReturn(new Year(1900));
        expectedRange = new Range(new Year(1901), new Year(1901));
        result = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange, result);

        when(sample.range.getStart()).thenReturn(new Year(3000));
        expectedRange = new Range(new Year(3000), new Year(3000));
        result = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange, result);

        when(sample.range.getStart()).thenReturn(new Year(-100));
        expectedRange = new Range(new Year(1), new Year(1));
        result = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange, result);

        when(sample.range.getStart()).thenReturn(new Year(-8001));
        expectedRange = new Range(new Year(7999), new Year(7999));
        result = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange, result);

        when(sample.range.getStart()).thenReturn(new Year(-8002));
        expectedRange = new Range(new Year(8001), new Year(8001));
        result = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange, result);
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = Mockito.mock(Sample.class);
        when(sample.meta.containsKey("id")).thenReturn(true);
        when(sample.meta.get("id")).thenReturn("123456");

        TucsonSimple tucsonSimple = new TucsonSimple();
        String expectedCode = "123456";
        String result = tucsonSimple.make6digitCode(sample);

        assertEquals(expectedCode, result);

        when(sample.meta.containsKey("id")).thenReturn(false);
        when(sample.meta.size()).thenReturn(0);

        expectedCode = "000000";
        result = tucsonSimple.make6digitCode(sample);

        assertEquals(expectedCode, result);

        when(sample.meta.size()).thenReturn(1);
        when(sample.meta.get("id")).thenReturn(new Object());

        expectedCode = "000001";
        result = tucsonSimple.make6digitCode(sample);

        assertEquals(expectedCode, result);
    }

    @Test
    public void testSaveData() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        Year start = new Year(1999);
        Year end = new Year(2001);
        String code = "123456";
        String dataValue1 = "1.5";
        String dataValue2 = "2.5";
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, Double.parseDouble(dataValue1));
        data.put(1, Double.parseDouble(dataValue2));
        when(sample.data).thenReturn(data);
        when(sample.count).thenReturn(new HashMap<>());
        when(sample.range.getStart()).thenReturn(start);
        when(sample.range.getEnd()).thenReturn(end);

        TucsonSimple tucsonSimple = new TucsonSimple();

        tucsonSimple.saveData(sample, writer);

        String expectedLine1 = "1234561999";
        String expectedLine2 = "1.5   1.5";
        String expectedLine3 = "1234562000";
        String expectedLine4 = "2.5   2.5";
        String line1 = "";
        String line2 = "";
        String line3 = "";
        String line4 = "";

        verify(writer, times(4)).write(anyString());
        verify(writer, times(4)).newLine();

        inOrder(writer).then().write(expectedLine1);
        inOrder(writer).then().write(expectedLine2);
        inOrder(writer).then().write(expectedLine3);
        inOrder(writer).then().write(expectedLine4);

        when(writer.readLine()).thenReturn(line1);
        assertEquals(expectedLine1, line1);

        when(writer.readLine()).thenReturn(line2);
        assertEquals(expectedLine2, line2);

        when(writer.readLine()).thenReturn(line3);
        assertEquals(expectedLine3, line3);

        when(writer.readLine()).thenReturn(line4);
        assertEquals(expectedLine4, line4);
    }
}