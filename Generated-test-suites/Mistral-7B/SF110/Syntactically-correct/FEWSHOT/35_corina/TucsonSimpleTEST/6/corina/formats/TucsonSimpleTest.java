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

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    @Test
    public void testComputeRange() {
        Sample sample = Mockito.mock(Sample.class);
        when(sample.range.getStart()).thenReturn(new Year(500));
        TucsonSimple tucsonSimple = new TucsonSimple();
        Range range = tucsonSimple.computeRange(sample);
        assertEquals(new Year(1001), range.getStart());
    }

    @Test
    public void testSaveData_ValidData() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        TucsonSimple tucsonSimple = new TucsonSimple();
        Year start = new Year(1);
        Year end = new Year(3);
        String code = "123456";
        when(sample.range.getStart()).thenReturn(start);
        when(sample.meta.get("id")).thenReturn("sampleId");
        doWrite(writer, "1234561");

        tucsonSimple.saveData(sample, writer);

        verify(writer).write(code + "1");
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void testSaveData_EmptyId() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        TucsonSimple tucsonSimple = new TucsonSimple();
        Year start = new Year(1);
        Year end = new Year(3);
        String code = "000000";
        when(sample.range.getStart()).thenReturn(start);

        tucsonSimple.saveData(sample, writer);

        verify(writer).write(code);
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void testSaveData_ValidDataWithProcessedFlag() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        TucsonSimple tucsonSimple = new TucsonSimple();
        Year start = new Year(1);
        Year end = new Year(3);
        String code = "123456";
        when(sample.range.getStart()).thenReturn(start);
        when(sample.meta.get("id")).thenReturn("sampleId");
        when(sample.isProcessed()).thenReturn(true);
        doWrite(writer, "1234561   ");

        tucsonSimple.saveData(sample, writer);

        verify(writer).write(code + "1   ");
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void testSaveData_ValidDataWithSummedFlag() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        TucsonSimple tucsonSimple = new TucsonSimple();
        Year start = new Year(1);
        Year end = new Year(3);
        String code = "123456";
        when(sample.range.getStart()).thenReturn(start);
        when(sample.meta.get("id")).thenReturn("sampleId");
        when(sample.isSummed()).thenReturn(true);
        doWrite(writer, "1234561 123 0");

        tucsonSimple.saveData(sample, writer);

        verify(writer).write(code + "1 123 0");
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void testSaveData_ValidDataWithInvalidYear() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        TucsonSimple tucsonSimple = new TucsonSimple();
        Year start = new Year(1);
        Year end = new Year(3);
        String code = "123456";
        when(sample.range.getStart()).thenReturn(start);
        when(sample.meta.get("id")).thenReturn("sampleId");
        when(sample.isProcessed()).thenReturn(false);
        when(sample.isSummed()).thenReturn(false);
        doWrite(writer, "1234561");
        when(sample.getData()).thenReturn(new HashMap<Year, Double>() {{
            put(new Year(0), 123.5);
        }});
        when(sample.getCount()).thenReturn(new HashMap<Year, Integer>() {{
            put(new Year(0), 123);
        }});

        tucsonSimple.saveData(sample, writer);

        verify(writer).write(code + "1 123.5");
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void testSaveData_ValidDataWithProcessedFlagAndInvalidYear() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        TucsonSimple tucsonSimple = new TucsonSimple();
        Year start = new Year(1);
        Year end = new Year(3);
        String code = "123456";
        when(sample.range.getStart()).thenReturn(start);
        when(sample.meta.get("id")).thenReturn("sampleId");
        when(sample.isProcessed()).thenReturn(true);
        when(sample.isSummed()).thenReturn(false);
        when(sample.getData()).thenReturn(new HashMap<Year, Double>() {{
            put(new Year(0), 123.5);
        }});
        when(sample.getCount()).thenReturn(new HashMap<Year, Integer>() {{
            put(new Year(0), 123);
        }});
        doWrite(writer, "1234561  123.5");

        tucsonSimple.saveData(sample, writer);

        verify(writer).write(code + "1  123.5");
        verifyNoMoreInteractions(writer);
    }

    @Test
    public void testSaveData_ValidDataWithSummedFlagAndInvalidYear() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        TucsonSimple tucsonSimple = new TucsonSimple();
        Year start = new Year(1);
        Year end = new Year(3);
        String code = "123456";
        when(sample.range.getStart()).thenReturn(start);
        when(sample.meta.get("id")).thenReturn("sampleId");
        when(sample.isProcessed()).thenReturn(false);
        when(sample.isSummed()).thenReturn(true);
        when(sample.getData()).thenReturn(new HashMap<Year, Double>() {{
            put(new Year(0), 123.5);
        }});
        when(sample.getCount()).thenReturn(new HashMap<Year, Integer>() {{
            put(new Year(0), 123);
        }});
        doWrite(writer, "1234561 123 123.5");

        tucsonSimple.saveData(sample, writer);

        verify(writer).write(code + "1 123 123.5");
        verifyNoMoreInteractions(writer);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testSave_WrongFiletype() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);
        TucsonSimple tucsonSimple = new TucsonSimple();
        when(sample.getData()).thenThrow(new WrongFiletypeException());
        tucsonSimple.save(sample, writer);
    }

    private void doWrite(BufferedWriter writer, String... lines) throws IOException {
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
    }
}