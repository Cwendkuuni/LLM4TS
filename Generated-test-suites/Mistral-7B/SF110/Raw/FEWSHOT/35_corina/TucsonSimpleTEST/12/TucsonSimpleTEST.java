package corina.formats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import corina.formats.Filetype;
import corina.formats.Sample;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        sample.range = new Year(1995);

        TucsonSimple tucsonSimple = new TucsonSimple();
        Range expectedRange = new Year(1995);
        Range actualRange = tucsonSimple.computeRange(sample);
        assertEquals(expectedRange, actualRange);

        sample.range = new Year(2010);
        expectedRange = new Year(2010);
        actualRange = tucsonSimple.computeRange(sample);
        assertEquals(expectedRange, actualRange);

        sample.range = new Year(1950);
        expectedRange = new Year(1001);
        actualRange = tucsonSimple.computeRange(sample);
        assertEquals(expectedRange, actualRange);

        sample.range = new Year(1985);
        expectedRange = new Year(1985).redateStartTo(new Year(1001));
        actualRange = tucsonSimple.computeRange(sample);
        assertEquals(expectedRange, actualRange);

        sample.range = new Year(2001);
        expectedRange = new Year(2001).redateBy(8000);
        actualRange = tucsonSimple.computeRange(sample);
        assertEquals(expectedRange, actualRange);
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123456");

        TucsonSimple tucsonSimple = new TucsonSimple();
        String expectedCode = "123456";
        String actualCode = tucsonSimple.make6digitCode(sample);
        assertEquals(expectedCode, actualCode);

        sample.meta.clear();
        expectedCode = "000000";
        actualCode = tucsonSimple.make6digitCode(sample);
        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testSaveData() throws IOException {
        Sample sample = new Sample();
        sample.data = new HashMap<>();
        sample.count = new HashMap<>();
        sample.meta = new HashMap<>();
        sample.range = new Year(1995);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.saveData(sample, writer);

        String expectedData = "1995 123456 12.0 12.0\n";
        String actualData = outputStream.toString();
        assertEquals(expectedData, actualData);

        sample.data.put(new Year(1995), new Double(12.0));
        sample.count.put(new Year(1995), new Integer(12));

        outputStream.reset();
        writer.reset();
        tucsonSimple.saveData(sample, writer);

        expectedData = "1995 123456 12.0 12\n";
        actualData = outputStream.toString();
        assertEquals(expectedData, actualData);

        Year start = new Year(1995);
        Year end = new Year(1996);
        sample.range = new Range(start, end);

        outputStream.reset();
        writer.reset();
        tucsonSimple.saveData(sample, writer);

        expectedData = "1995 123456 1995 999\n";
        actualData = outputStream.toString();
        assertEquals(expectedData, actualData);

        sample.data.clear();
        sample.count.clear();

        outputStream.reset();
        writer.reset();
        tucsonSimple.saveData(sample, writer);

        expectedData = "1995 123456    \n";
        actualData = outputStream.toString();
        assertEquals(expectedData, actualData);

        sample.meta.put("id", null);

        outputStream.reset();
        writer.reset();
        tucsonSimple.saveData(sample, writer);

        expectedData = "1995 123456\n";
        actualData = outputStream.toString();
        assertEquals(expectedData, actualData);
    }

    @Test
    public void testToString() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expectedString = "format.tucsonsimple";
        String actualString = tucsonSimple.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetDefaultExtension() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        String expectedExtension = ".RWL";
        String actualExtension = tucsonSimple.getDefaultExtension();
        assertEquals(expectedExtension, actualExtension);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadThrowsWrongFiletypeException() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("not a valid line");
        when(reader.readLine()).thenThrow(IOException.class);

        tucsonSimple.load(reader);
    }
}

class Range {
    private Year start;
    private Year end;

    public Range(Year start, Year end) {
        this.start = start;
        this.end = end;
    }

    public Year getStart() {
        return start;
    }

    public Year getEnd() {
        return end;
    }
}

class Year implements Comparable<Year> {
    private int year;
    private int column;

    public Year(int year) {
        this.year = year;
        this.column = 0;
    }

    public Year add(int columns) {
        Year newYear = new Year(this.year + columns);
        newYear.column += columns;
        return newYear;
    }

    public int compareTo(Year other) {
        return Integer.compare(this.year, other.year);
    }

    public String toString() {
        String string = Integer.toString(this.year);
        if (column > 0) {
            string += String.format("%0" + (column * 2) + "d", this.column);
        }
        return string;
    }
}