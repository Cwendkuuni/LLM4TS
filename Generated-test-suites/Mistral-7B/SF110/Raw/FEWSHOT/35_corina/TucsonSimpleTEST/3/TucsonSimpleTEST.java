package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    @Test
    public void testComputeRange() {
        Year start = new Year(1990);
        Sample sample = Mockito.mock(Sample.class);
        when(sample.getRange()).thenReturn(new Range(start, new Year(1992)));

        TucsonSimple tucsonSimple = new TucsonSimple();
        Range expectedRange = new Range(new Year(1001), new Year(1003));
        Range actualRange = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange.getStart(), actualRange.getStart());
        assertEquals(expectedRange.getEnd(), actualRange.getEnd());
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = Mockito.mock(Sample.class);
        when(sample.getMeta()).thenReturn(Map.of("id", "123"));

        TucsonSimple tucsonSimple = new TucsonSimple();
        String expectedCode = "123";
        String actualCode = tucsonSimple.make6digitCode(sample);

        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testMake6digitCode_noId() {
        Sample sample = Mockito.mock(Sample.class);

        TucsonSimple tucsonSimple = new TucsonSimple();
        String expectedCode = "000000";
        String actualCode = tucsonSimple.make6digitCode(sample);

        assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testSaveData_summed() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        when(sample.getData()).thenReturn(Map.of(new Year(1991), "data1", new Year(1992), "data2"));
        when(sample.getCount()).thenReturn(Map.of(new Year(1991), 1, new Year(1992), 2));
        when(sample.getRange()).thenReturn(new Range(new Year(1991), new Year(1992)));

        BufferedWriter writer = Mockito.mock(BufferedWriter.class);

        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.saveData(sample, writer);

        String expectedLine1 = "199101 1";
        String expectedLine2 = "199102  2";
        String expectedLine3 = "199201 9990  0";

        Mockito.verify(writer, Mockito.times(3)).write(ArgumentMatchers.anyString());
        Mockito.verify(writer).write(expectedLine1);
        Mockito.verify(writer).write(expectedLine2);
        Mockito.verify(writer).write(expectedLine3);
    }

    @Test
    public void testSaveData_processed() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        when(sample.getData()).thenReturn(Map.of(new Year(1991, 5), "data1", new Year(1992, 5), "data2"));
        when(sample.getCount()).thenReturn(Map.of(new Year(1991, 5), 1, new Year(1992, 5), 2));
        when(sample.getRange()).thenReturn(new Range(new Year(1991, 1), new Year(1992, 12)));

        BufferedWriter writer = Mockito.mock(BufferedWriter.class);

        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.saveData(sample, writer, true);

        String expectedLine1 = "199100005 1";
        String expectedLine2 = "199200005 2";
        String expectedLine3 = "199209999   ";

        Mockito.verify(writer, Mockito.times(3)).write(ArgumentMatchers.anyString());
        Mockito.verify(writer).write(expectedLine1);
        Mockito.verify(writer).write(expectedLine2);
        Mockito.verify(writer).write(expectedLine3);
    }

    @Test
    public void testSaveData_startBefore1000() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        when(sample.getData()).thenReturn(Map.of(new Year(1985), "data1", new Year(1986), "data2"));
        when(sample.getCount()).thenReturn(Map.of(new Year(1985), 1, new Year(1986), 2));
        when(sample.getRange()).thenReturn(new Range(new Year(1985), new Year(1986)));

        BufferedWriter writer = Mockito.mock(BufferedWriter.class);

        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.saveData(sample, writer);

        String expectedLine1 = "8500001  ";
        String expectedLine2 = "8600001  ";

        Mockito.verify(writer, Mockito.times(2)).write(ArgumentMatchers.anyString());
        Mockito.verify(writer).write(expectedLine1);
        Mockito.verify(writer).write(expectedLine2);
    }

    @Test
    public void testSaveData_startAfter10000() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        when(sample.getData()).thenReturn(Map.of(new Year(99991), "data1", new Year(99992), "data2"));
        when(sample.getCount()).thenReturn(Map.of(new Year(99991), 1, new Year(99992), 2));
        when(sample.getRange()).thenReturn(new Range(new Year(99991), new Year(99992)));

        BufferedWriter writer = Mockito.mock(BufferedWriter.class);

        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.saveData(sample, writer);

        String expectedLine1 = "9999100001  ";
        String expectedLine2 = "9999200001  ";

        Mockito.verify(writer, Mockito.times(2)).write(ArgumentMatchers.anyString());
        Mockito.verify(writer).write(expectedLine1);
        Mockito.verify(writer).write(expectedLine2);
    }

    @Test
    public void testSaveData_endBeforeStart() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        when(sample.getData()).thenReturn(Map.of(new Year(1991), "data1", new Year(1990), "data2"));
        when(sample.getCount()).thenReturn(Map.of(new Year(1991), 1, new Year(1990), 2));
        when(sample.getRange()).thenReturn(new Range(new Year(1991), new Year(1990)));

        BufferedWriter writer = Mockito.mock(BufferedWriter.class);

        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.saveData(sample, writer);

        String expectedLastLine = "199099999   ";

        Mockito.verify(writer, Mockito.times(1)).write(expectedLastLine);
    }

    @Test
    public void testSaveData_endAfterStart() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        when(sample.getData()).thenReturn(Map.of(new Year(1991), "data1", new Year(1992), "data2"));
        when(sample.getCount()).thenReturn(Map.of(new Year(1991), 1, new Year(1992), 2));
        when(sample.getRange()).thenReturn(new Range(new Year(1991), new Year(1993)));

        BufferedWriter writer = Mockito.mock(BufferedWriter.class);

        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.saveData(sample, writer);

        String expectedLastLine = "19929990   ";

        Mockito.verify(writer, Mockito.times(1)).write(expectedLastLine);
    }

    @Test
    public void testSaveData_endEqualToStart() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        when(sample.getData()).thenReturn(Map.of(new Year(1991), "data1", new Year(1991), "data2"));
        when(sample.getCount()).thenReturn(Map.of(new Year(1991), 1, new Year(1991), 2));
        when(sample.getRange()).thenReturn(new Range(new Year(1991), new Year(1991)));

        BufferedWriter writer = Mockito.mock(BufferedWriter.class);

        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.saveData(sample, writer);

        String expectedLastLine = "19919999   ";

        Mockito.verify(writer, Mockito.times(1)).write(expectedLastLine);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testSave() throws IOException {
        Sample sample = Mockito.mock(Sample.class);
        BufferedWriter writer = Mockito.mock(BufferedWriter.class);

        TucsonSimple tucsonSimple = new TucsonSimple();
        tucsonSimple.save(sample, writer);

        Mockito.verify(writer, Mockito.never()).write(ArgumentMatchers.anyString());
    }
}