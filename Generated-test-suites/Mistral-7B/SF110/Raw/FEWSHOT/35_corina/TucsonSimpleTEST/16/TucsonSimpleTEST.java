package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import corina.Sample;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    private TucsonSimple tucsonSimple;
    private BufferedWriter writerMock;
    private Sample sampleMock;
    private BufferedReader readerMock;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        tucsonSimple = new TucsonSimple();
        writerMock = mock(BufferedWriter.class);
        sampleMock = mock(Sample.class);
        readerMock = mock(BufferedReader.class);
    }

    @Test
    public void testComputeRange() {
        Year start = new Year(1990);
        Range expectedRange = new Range(start, start.add(1));
        when(sampleMock.getRange()).thenReturn(new Range(start, start.add(1)));
        Range actualRange = tucsonSimple.computeRange(sampleMock);
        assertEquals(expectedRange, actualRange);

        start = new Year(1990).add(8001);
        expectedRange = new Range(start.redateBy(8000), start);
        when(sampleMock.getRange()).thenReturn(new Range(start, start));
        actualRange = tucsonSimple.computeRange(sampleMock);
        assertEquals(expectedRange, actualRange);

        start = new Year(1899);
        expectedRange = new Range(new Year(1001), start.add(1));
        when(sampleMock.getRange()).thenReturn(new Range(start, start.add(1)));
        actualRange = tucsonSimple.computeRange(sampleMock);
        assertEquals(expectedRange, actualRange);
    }

    @Test
    public void testSaveData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedWriter writer = new BufferedWriter(outputStream);
        writerMock = writer;

        when(sampleMock.getData()).thenReturn(new double[]{1.0, 2.0, 3.0});
        when(sampleMock.getCount()).thenReturn(new int[]{1, 2, 3});
        when(sampleMock.getRange()).thenReturn(new Range(new Year(1990), new Year(1990).add(1)));
        when(sampleMock.getMeta()).thenReturn(new HashMap<String, Object>() {{
            put("id", "sampleId");
        }});

        tucsonSimple.saveData(sampleMock, writerMock);

        verify(writerMock).write("1.000000 1");
        verify(writerMock).write("2.000000 2");
        verify(writerMock).write("3.000000 3");
        verify(writerMock).write("---");
        verify(writerMock).write("sampleId");
        verify(writerMock).write("999");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("999");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("999");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("999");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("999");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("999");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("999");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("9990   ");
        verify(writerMock).write("999");

        verifyNoMoreInteractions(writerMock);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testSave() throws IOException {
        when(sampleMock.getData()).thenReturn(new double[]{1.0, 2.0, 3.0});
        when(sampleMock.getCount()).thenReturn(new int[]{1, 2, 3});
        when(sampleMock.getRange()).thenReturn(new Range(new Year(1990), new Year(1990).add(1)));
        when(sampleMock.getMeta()).thenReturn(new HashMap<String, Object>() {{
            put("id", "sampleId");
        }});

        tucsonSimple.save(sampleMock, writerMock);
    }
}