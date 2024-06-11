package corina.formats;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.Year;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import corina.Sample;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

public class TucsonSimpleTest {

    private BufferedReader mockReader;
    private BufferedWriter mockWriter;
    private I18n i18n;
    private StringUtils stringUtils;
    private TucsonSimple tucsonSimple;

    @Before
    public void setup() {
        mockReader = mock(BufferedReader.class);
        mockWriter = mock(BufferedWriter.class);
        i18n = Mockito.mock(I18n.class);
        stringUtils = Mockito.mock(StringUtils.class);
        tucsonSimple = new TucsonSimple() {
            {
                this.i18n = i18n;
                this.stringUtils = stringUtils;
            }
        };
    }

    @Test
    public void testComputeRange() {
        Year start = new Year(1995);
        Sample sample = new Sample();
        sample.setRange(new Range(start, start.add(1)));

        Range expectedRange = new Range(start, start.add(1));
        Range actualRange = tucsonSimple.computeRange(sample);

        assertEquals(expectedRange, actualRange);

        start = new Year(-5000);
        sample.setRange(new Range(start, start.add(1)));

        Range expectedRedatedRange = new Range(new Year(3001), new Year(3002));
        Range actualRedatedRange = tucsonSimple.computeRange(sample);

        assertEquals(expectedRedatedRange, actualRedatedRange);

        start = new Year(1);
        sample.setRange(new Range(start, start.add(8001)));

        Range expectedRedatedBy8000Range = new Range(new Year(8001), new Year(8002));
        Range actualRedatedBy8000Range = tucsonSimple.computeRange(sample);

        assertEquals(expectedRedatedBy8000Range, actualRedatedBy8000Range);
    }

    @Test
    public void testSaveData() throws IOException {
        Year start = new Year(1995);
        Year end = new Year(1996);
        String code = "123456";
        Sample sample = new Sample();
        sample.setRange(new Range(start, end));
        sample.setData(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0});
        sample.setCount(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        BufferedWriter writer = mockWriter;

        tucsonSimple.saveData(sample, writer);

        verify(writer, times(11)).write(anyString());
        verify(writer, times(8)).newLine();
    }

    @Test
    public void testMake6digitCode() {
        Sample sample = new Sample();
        sample.setMeta(Map.of("id", "123"));

        String expectedCode = "123";
        String actualCode = tucsonSimple.make6digitCode(sample);

        assertEquals(expectedCode, actualCode);

        sample.setMeta(null);

        expectedCode = "000001";
        actualCode = tucsonSimple.make6digitCode(sample);

        assertEquals(expectedCode, actualCode);
    }
}