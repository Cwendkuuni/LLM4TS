package corina.formats;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void testMake6digitCode() {
        Sample sample = Mockito.mock(Sample.class);
        Map<String, String> meta = new HashMap<>();
        meta.put("id", "123456789");
        when(sample.meta).thenReturn(meta);

        TucsonSimple tucsonSimple = new TucsonSimple();
        String code = tucsonSimple.make6digitCode(sample);
        assertEquals("123456", code);

        Sample sample2 = Mockito.mock(Sample.class);
        when(sample2.meta).thenReturn(null);

        String code2 = tucsonSimple.make6digitCode(sample2);
        assertEquals("000000", code2);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        String code = "123456";
        int yearWidth = 5;
        Year year = new Year(2000);

        tucsonSimple.saveRowHeader(writer, sample, code, yearWidth, year);
        Mockito.verify(writer).write(code + " 2000");
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Range range = new Range(new Year(2000), new Year(2001));
        when(sample.range).thenReturn(range);
        String code = "123456";

        tucsonSimple.saveData(sample, writer);

        Mockito.verify(writer).write("123456 2000");
        Mockito.verify(writer).newLine();
        Mockito.verify(writer).write("   999");
        Mockito.verify(writer).newLine();
    }

    @Test
    public void testComputeRange() {
        Sample sample = Mockito.mock(Sample.class);
        Year start = new Year(1999);
        when(sample.range.getStart()).thenReturn(start);

        TucsonSimple tucsonSimple = new TucsonSimple();
        Range range = tucsonSimple.computeRange(sample);

        assertEquals(new Range(start, new Year(2000)), range);

        Year start2 = new Year(2000001);
        when(sample.range.getStart()).thenReturn(start2);

        Range range2 = tucsonSimple.computeRange(sample);

        assertEquals(new Range(start2.redateStartTo(new Year(1001)), start2.redateBy(8000)), range2);
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);
        Sample sample = Mockito.mock(Sample.class);
        Range range = new Range(new Year(2000), new Year(2001));
        when(sample.range).thenReturn(range);
        String code = "123456";

        tucsonSimple.save(sample, writer);

        Mockito.verify(writer).write("123456 2000");
        Mockito.verify(writer).newLine();
        Mockito.verify(writer).write("   999");
        Mockito.verify(writer).newLine();
    }

    @Test
    public void testNullSample() {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);

        assertNull(tucsonSimple.save(null, writer));
    }
}