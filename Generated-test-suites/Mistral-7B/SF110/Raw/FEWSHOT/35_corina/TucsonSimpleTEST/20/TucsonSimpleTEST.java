package corina.formats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import corina.formats.Filetype;
import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private BufferedWriter mockWriter;

    @Before
    public void setup() {
        tucsonSimple = new TucsonSimple();
        mockWriter = mock(BufferedWriter.class);
    }

    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", tucsonSimple.toString());
    }

    @Test
    public void testGetDefaultExtension() {
        assertEquals(".RWL", tucsonSimple.getDefaultExtension());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadNullReader() throws IOException {
        tucsonSimple.load(null);
    }

    @Test
    public void testLoadWithNullData() throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn(null);
        assertThrows(WrongFiletypeException.class, () -> tucsonSimple.load(reader));
    }

    @Test
    public void testLoadSample() throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        when(reader.readLine()).thenReturn("000001\t2002\tData1\tData2\tData3\tData4\tData5\tData6\tData7");
        Sample sample = new Sample();
        sample.data = new String[7];
        sample.data[0] = "Data1";
        sample.data[1] = "Data2";
        sample.data[2] = "Data3";
        sample.data[3] = "Data4";
        sample.data[4] = "Data5";
        sample.data[5] = "Data6";
        sample.data[6] = "Data7";
        when(reader.readLine()).thenReturn("000002\t2003\tData8\tData9\tData10\tData11\tData12\tData13\tData14");
        sample.count = new int[7];
        sample.count[0] = 1;
        sample.count[1] = 1;
        when(reader.readLine()).thenReturn(null);

        Sample loadedSample = tucsonSimple.load(reader);
        assertEquals(sample.getData()[0], loadedSample.getData()[0]);
        assertEquals(sample.getData()[1], loadedSample.getData()[1]);
        assertEquals(sample.getData()[2], loadedSample.getData()[2]);
        assertEquals(sample.getData()[3], loadedSample.getData()[3]);
        assertEquals(sample.getData()[4], loadedSample.getData()[4]);
        assertEquals(sample.getData()[5], loadedSample.getData()[5]);
        assertEquals(sample.getData()[6], loadedSample.getData()[6]);
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.meta = new HashMap<>();
        sample.meta.put("id", "sampleId");
        assertEquals("sampleId", tucsonSimple.make6digitCode(sample));

        sample = new Sample();
        assertEquals("000000", tucsonSimple.make6digitCode(sample));
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        Sample sample = new Sample();
        Year startYear = new Year(2002);
        Year endYear = new Year(2003);
        String code = "000001";

        tucsonSimple.saveRowHeader(mockWriter, sample, code, 6, startYear);
        verify(mockWriter).write("0000012002");
        verifyNoMoreInteractions(mockWriter);

        startYear = startYear.add(1);
        tucsonSimple.saveRowHeader(mockWriter, sample, code, 6, startYear);
        verify(mockWriter).write("0000012003");
        verifyNoMoreInteractions(mockWriter);

        startYear = startYear.add(1);
        tucsonSimple.saveRowHeader(mockWriter, sample, code, 4, startYear);
        verify(mockWriter).write("0000012004");
        verifyNoMoreInteractions(mockWriter);
    }

    @Test
    public void testSaveData() throws IOException {
        Sample sample = new Sample();
        sample.data = Arrays.asList("Data1", "Data2", "Data3", "Data4", "Data5", "Data6", "Data7").toArray(new String[7]);
        sample.count = new int[7];
        Year startYear = new Year(2002);
        Year endYear = new Year(2003);
        String code = "000001";

        tucsonSimple.saveData(sample, mockWriter);

        verify(mockWriter).write("Data1");
        verify(mockWriter).write("Data2");
        verify(mockWriter).write("Data3");
        verify(mockWriter).write("Data4");
        verify(mockWriter).write("Data5");
        verify(mockWriter).write("Data6");
        verify(mockWriter).write("Data7");
        verify(mockWriter).write("0");
        verifyNoMoreInteractions(mockWriter);

        startYear = startYear.add(1);
        tucsonSimple.saveData(sample, mockWriter);

        verify(mockWriter).write("Data1");
        verify(mockWriter).write("Data2");
        verify(mockWriter).write("Data3");
        verify(mockWriter).write("Data4");
        verify(mockWriter).write("Data5");
        verify(mockWriter).write("Data6");
        verify(mockWriter).write("Data7");
        verify(mockWriter).write("1");
        verifyNoMoreInteractions(mockWriter);

        startYear = startYear.add(1);
        tucsonSimple.saveData(sample, mockWriter);

        verify(mockWriter).write("Data1");
        verify(mockWriter).write("Data2");
        verify(mockWriter).write("Data3");
        verify(mockWriter).write("Data4");
        verify(mockWriter).write("Data5");
        verify(mockWriter).write("Data6");
        verify(mockWriter).write("Data7");
        verify(mockWriter).write(" ");
        verifyNoMoreInteractions(mockWriter);

        tucsonSimple.saveData(sample, mockWriter);

        verify(mockWriter).write("999");
        verifyNoMoreInteractions(mockWriter);
    }

    @Test
    public void testSaveDataWithSummed() throws IOException {
        Sample sample = new Sample();
        sample.data = Arrays.asList("Data1", "Data2", "Data3", "Data4", "Data5", "Data6", "Data7").toArray(new String[7]);
        sample.count = new int[7];
        sample.count[0] = 2;
        sample.count[1] = 3;
        sample.count[2] = 4;
        sample.count[3] = 1;
        sample.count[4] = 5;
        sample.count[5] = 6;
        sample.count[6] = 7;
        Year startYear = new Year(2002);
        Year endYear = new Year(2003);
        String code = "000001";
        boolean isProcessed = true;

        tucsonSimple.saveData(sample, mockWriter);

        verify(mockWriter).write("Data1");
        verify(mockWriter).write("Data2");
        verify(mockWriter).write("Data3");
        verify(mockWriter).write("Data4");
        verify(mockWriter).write("Data5");
        verify(mockWriter).write("Data6");
        verify(mockWriter).write("Data7");
        verify(mockWriter).write("2");
        verifyNoMoreInteractions(mockWriter);

        startYear = startYear.add(1);
        tucsonSimple.saveData(sample, mockWriter);

        verify(mockWriter).write("Data1");
        verify(mockWriter).write("Data2");
        verify(mockWriter).write("Data3");
        verify(mockWriter).write("Data4");
        verify(mockWriter).write("Data5");
        verify(mockWriter).write("Data6");
        verify(mockWriter).write("Data7");
        verify(mockWriter).write("3");
        verifyNoMoreInteractions(mockWriter);

        startYear = startYear.add(1);
        tucsonSimple.saveData(sample, mockWriter);

        verify(mockWriter).write("Data1");
        verify(mockWriter).write("Data2");
        verify(mockWriter).write("Data3");
        verify(mockWriter).write("Data4");
        verify(mockWriter).write("Data5");
        verify(mockWriter).write("Data6");
        verify(mockWriter).write("Data7");
        verify(mockWriter).write("4");
        verifyNoMoreInteractions(mockWriter);

        startYear = startYear.add(1);
        tucsonSimple.saveData(sample, mockWriter);

        verify(mockWriter).write("Data1");
        verify(mockWriter).write("Data2");
        verify(mockWriter).write("Data3");
        verify(mockWriter).write("Data4");
        verify(mockWriter).write("Data5");
        verify(mockWriter).write("Data6");
        verify(mockWriter).write("Data7");
        verify(mockWriter).write(" ");
        verifyNoMoreInteractions(mockWriter);

        tucsonSimple.saveData(sample, mockWriter);

        verify(mockWriter).write("9990  0");
        verifyNoMoreInteractions(mockWriter);
    }

    @Test
    public void testComputeRange() {
        Sample sample = new Sample();
        Year start = new Year(1);
        Year end = new Year(1001);
        Range expectedRange = new Range(start, end);

        Range actualRange = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange.getStart(), actualRange.getStart());
        assertEquals(expectedRange.getEnd(), actualRange.getEnd());
    }

    @Test
    public void testComputeRangeWithStartBeforeOne() {
        Sample sample = new Sample();
        Year start = new Year(0);
        Year end = new Year(1001);
        Range expectedRange = new Range(new Year(1001), end);

        Range actualRange = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange.getStart(), actualRange.getStart());
        assertEquals(expectedRange.getEnd(), actualRange.getEnd());
    }

    @Test
    public void testComputeRangeWithStartAfterEightThousand() {
        Sample sample = new Sample();
        Year start = new Year(8001);
        Year end = new Year(1001);
        Range expectedRange = new Range(start, end.redateBy(8000));

        Range actualRange = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange.getStart(), actualRange.getStart());
        assertEquals(expectedRange.getEnd(), actualRange.getEnd());
    }
}