package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import corina.formats.I18n;
import corina.formats.StringUtils;
import corina.ui.Sample;
import corina.util.Year;

public class TucsonSimpleTest {

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "123456");
        String code = new TucsonSimple().make6digitCode(sample);
        assertEquals("123456", code);

        sample.meta.clear();
        code = new TucsonSimple().make6digitCode(sample);
        assertEquals("000000", code);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        String code = "abcdef";
        String yearString = "2022";
        int yearWidth = 6;
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = new Sample();
        Year start = new Year(2021);

        tucson.saveRowHeader(writer, sample, code, yearWidth, start);

        verify(writer).write(code + yearString);
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucson = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = new Sample();
        Range range = new Range(new Year(2021), new Year(2021));
        sample.range = range;
        String code = "abcdef";
        Year start = new Year(2021);
        Year end = new Year(2022);

        when(writer.write(eq("999"))).thenReturn(writer);
        when(writer.write(eq("9990  0"))).thenReturn(writer);

        tucson.saveData(sample, writer);

        verify(writer, times(3)).write(anyString());
        verify(writer).newLine();
    }

    @Test
    public void testComputeRange() {
        TucsonSimple tucson = new TucsonSimple();
        Sample sample = new Sample();
        Year start = new Year(1995);
        Range expectedRange = new Range(new Year(1995), new Year(1995));

        Range range = tucson.computeRange(sample);

        assertEquals(expectedRange, range);
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucson = spy(new TucsonSimple());
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = new Sample();
        Range range = new Range(new Year(2021), new Year(2021));
        sample.range = range;

        doThrow(WrongFiletypeException.class).when(tucson).load(any(BufferedReader.class));

        tucson.save(sample, writer);

        verifyZeroInteractions(tucson);
    }

    @Test
    public void testSaveWithData() throws IOException {
        TucsonSimple tucson = spy(new TucsonSimple());
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = new Sample();
        Range range = new Range(new Year(2021), new Year(2022));
        sample.range = range;
        sample.data = new double[]{-1.0, 2.0, 3.0, 4.0};
        sample.count = new int[]{1, 2, 3, 4};

        doReturn(null).when(tucson).load(any(BufferedReader.class));

        tucson.save(sample, writer);

        verify(writer).write("abcdef2021");
        verify(writer).write(" abcdef 1  ");
        verify(writer).write("2.0  2  ");
        verify(writer).write("3.0  3  ");
        verify(writer).write("4.0  4  ");
        verify(writer).write("    ");
        verify(writer).write("1   ");
        verify(writer).write("2   ");
        verify(writer).write("3   ");
        verify(writer).write("4   ");
        verify(writer).newLine();
        verify(writer).newLine();
    }
}