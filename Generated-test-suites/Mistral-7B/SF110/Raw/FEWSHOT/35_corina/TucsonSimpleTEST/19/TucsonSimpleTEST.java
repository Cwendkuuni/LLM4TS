package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

import corina.util.I18n;
import corina.util.StringUtils;
import corina.ui.Year;
import corina.formats.TucsonSimple;
import corina.formats.WrongFiletypeException;
import corina.Sample;

public class TucsonSimpleTest {

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta.put("id", "12345678");
        String code = new TucsonSimple().make6digitCode(sample);
        assertEquals("123456", code);

        sample.meta.clear();
        code = new TucsonSimple().make6digitCode(sample);
        assertEquals("000000", code);

        sample.meta.put("id", "12");
        code = new TucsonSimple().make6digitCode(sample);
        assertEquals("000120", code);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = new Sample();
        Year start = new Year(1999);
        String code = "123456";
        int yearWidth = 6;

        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
        String expectedOutput = "1234561999";
        String actualOutput = new StringWriter().toString();
        when(writer.toString()).thenReturn(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        start = new Year(2001);
        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, start);
        expectedOutput = "1234562001";
        actualOutput = new StringWriter().toString();
        when(writer.toString()).thenReturn(actualOutput);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = new Sample();
        Year start = new Year(1999);
        Year end = new Year(2000);
        String code = "123456";
        boolean isSummed = false;
        boolean isProcessed = false;

        tucsonSimple.saveData(sample, writer);

        String expectedOutput = "1234561999  " + "Data1" + "\n";
        String actualOutput = new StringWriter().toString();
        when(writer.toString()).thenReturn(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        String data = "Data1";
        sample.data.put(start, data);
        isSummed = true;

        tucsonSimple.saveData(sample, writer);

        expectedOutput = "1234561999  " + "Data1" + " 3\n";
        actualOutput = new StringWriter().toString();
        when(writer.toString()).thenReturn(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        isProcessed = true;

        tucsonSimple.saveData(sample, writer);

        expectedOutput = "1234561999 000000\n";
        actualOutput = new StringWriter().toString();
        when(writer.toString()).thenReturn(actualOutput);
        assertEquals(expectedOutput, actualOutput);

        start = new Year(2001);
        end = new Year(2002);

        tucsonSimple.saveData(sample, writer);

        expectedOutput = "1234562001   999\n";
        actualOutput = new StringWriter().toString();
        when(writer.toString()).thenReturn(actualOutput);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        Year start = new Year(1999);
        Range range = new Range(start, start.add(1));
        sample.range = range;

        Range expectedRange = new Range(start, start.add(1));
        Range computedRange = new TucsonSimple().computeRange(sample);

        assertEquals(expectedRange, computedRange);

        start = new Year(3000);
        range = new Range(start, start.add(100));
        sample.range = range;

        expectedRange = new Range(start, start.add(8000));
        computedRange = new TucsonSimple().computeRange(sample);

        assertEquals(expectedRange, computedRange);

        start = new Year(1001);
        range = new Range(new Year(1), start);
        sample.range = range;

        expectedRange = new Range(new Year(1), start);
        computedRange = new TucsonSimple().computeRange(sample);

        assertEquals(expectedRange, computedRange);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = new Sample();

        tucsonSimple.save(sample, writer);
    }
}