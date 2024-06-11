package corina.formats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import corina.formats.Sample;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", LocalDate.now().toString());

        assertEquals("123456", new TucsonSimple().make6digitCode(sample));

        sample.meta.clear();
        assertEquals("000001", new TucsonSimple().make6digitCode(sample));

        Sample sampleWithLongId = new Sample();
        sampleWithLongId.meta = new HashMap<>();
        sampleWithLongId.meta.put("id", "12345678901234567890");
        assertEquals("123456", new TucsonSimple().make6digitCode(sampleWithLongId));
    }

    @Test
    public void testSaveDataNullSample() throws IOException {
        TucsonSimple tucsonSimple = mock(TucsonSimple.class);
        BufferedWriter writer = mock(BufferedWriter.class);

        doThrow(IllegalArgumentException.class).when(tucsonSimple).saveData(null, writer);

        assertThrows(IllegalArgumentException.class, () -> tucsonSimple.save(null, writer));
    }

    @Test
    public void testSaveDataEmptySample() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.saveData(sample, writer);

        verify(writer, times(1)).write("   999");
        verify(writer, times(1)).newLine();
    }

    @Test
    public void testSaveDataSampleWithValidData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Sample data = new Sample();
        data.meta = new HashMap<>();
        data.meta.put("value", "123");

        sample.data = new HashMap<>();
        sample.data.put(new Year(2000), data);

        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.saveData(sample, writer);

        verify(writer, times(1)).write("123");
        verify(writer, times(1)).write(" ");
        verify(writer, times(1)).write(" ");
        verify(writer, times(1)).write(StringUtils.LEFT_PADDING);
        verify(writer, times(1)).write(" ");
        verify(writer, times(1)).write("\n");
        verify(writer, times(1)).newLine();
    }

    @Test
    public void testSaveDataSampleWithLargeData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Sample data = new Sample();
        data.meta = new HashMap<>();
        data.meta.put("value", "1234567890123456789012345678901234567890");

        sample.data = new HashMap<>();
        sample.data.put(new Year(2000), data);

        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.saveData(sample, writer);

        verify(writer, times(1)).write(StringUtils.LEFT_PADDING);
        verify(writer, times(1)).write("1234567890123456789012345678901234567890");
        verify(writer, times(1)).write(" ");
        verify(writer, times(1)).write("\n");
        verify(writer, times(1)).newLine();
    }

    @Test
    public void testSaveDataSampleWithProcessedData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Sample data = new Sample();
        data.data = new String[]{"123", "456"};
        data.count = new int[]{1, 2};

        sample.data = new HashMap<>();
        sample.data.put(new Year(2000), data);
        sample.isProcessed = true;

        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.saveData(sample, writer);

        verify(writer, times(1)).write("123");
        verify(writer, times(1)).write("   ");
        verify(writer, times(1)).write(" ");
        verify(writer, times(1)).write("\n");

        verify(writer, times(1)).write("456");
        verify(writer, times(1)).write("   ");
        verify(writer, times(1)).write("\n");

        verify(writer, times(1)).newLine();
    }

    @Test
    public void testSaveDataSampleWithSummedData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        Sample data = new Sample();
        data.data = new String[]{"123", "456"};
        data.count = new int[]{1, 2};

        sample.data = new HashMap<>();
        sample.data.put(new Year(2000), data);
        sample.isSummed = true;

        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.saveData(sample, writer);

        verify(writer, times(1)).write("123");
        verify(writer, times(1)).write("001");
        verify(writer, times(1)).write("\n");

        verify(writer, times(1)).write("456");
        verify(writer, times(1)).write("002");
        verify(writer, times(1)).write("\n");

        verify(writer, times(1)).newLine();
    }

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        Year start = new Year(1995);
        Range range = new Range(start, start.add(1));

        assertEquals(range, new TucsonSimple().computeRange(sample));

        sample.range = new Range(new Year(1980), new Year(1981));

        Range expectedRange = new Range(new Year(1980), new Year(1981).add(8000));
        assertEquals(expectedRange, new TucsonSimple().computeRange(sample));
    }

    @Test
    public void testSaveSampleWithValidData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        Sample sample = new Sample();
        sample.range = new Range(new Year(2000), new Year(2001));

        BufferedWriter writer = mock(BufferedWriter.class);

        tucsonSimple.save(sample, writer);

        verify(writer, times(1)).write("123456"); // make6digitCode
        verify(writer, times(1)).write("2000"); // saveRowHeader
        verify(writer, times(1)).write("2000"); // saveRowHeader
        verify(writer, times(1)).write("123");
        verify(writer, times(1)).write(" ");
        verify(writer, times(1)).write("\n");
        verify(writer, times(1)).newLine();

        verify(writer, times(1)).write("123");
        verify(writer, times(1)).write(" ");
        verify(writer, times(1)).write("\n");

        verify(writer, times(1)).newLine();
    }
}