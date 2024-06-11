package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.time.Year;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import corina.Sample;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;

@RunWith(MockitoJUnitRunner.class)
public class TucsonSimpleTest {

    @Test
    public void testMake6digitCode() {
        Sample s = new Sample();
        I18n i18n = mock(I18n.class);
        when(i18n.getText(Mockito.eq("format.tucsonsimple"))).thenReturn("TucsonSimple Format");
        s.meta = new Hashtable<String, String>() {{
            put("id", "123456");
        }};

        String code = new TucsonSimple().make6digitCode(s);
        assertEquals("123456", code);

        s = new Sample();
        code = new TucsonSimple().make6digitCode(s);
        assertEquals("000000", code);

        s = new Sample();
        s.meta.put("id", "1234567");
        code = new TucsonSimple().make6digitCode(s);
        assertEquals("123456", code);

        s = new Sample();
        code = new TucsonSimple().make6digitCode(s);
        assertEquals("000001", code);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        Sample s = new Sample();
        I18n i18n = mock(I18n.class);
        Year start = new Year(2010);
        Year end = new Year(2015);
        BufferedWriter writer = mock(BufferedWriter.class);
        StringWriter stringWriter = new StringWriter();
        BufferedWriter w = Mockito.spy(stringWriter);

        when(i18n.getText(Mockito.eq("format.tucsonsimple"))).thenReturn("TucsonSimple Format");

        new TucsonSimple().saveRowHeader(w, s, "000001", 6, start);
        assertEquals("0000012010", w.getBuffer().toString());

        new TucsonSimple().saveRowHeader(w, s, "000001", 4, start);
        assertEquals("000001", w.getBuffer().toString());

        new TucsonSimple().saveRowHeader(w, s, "000001", 6, end);
        assertEquals("0000012015", w.getBuffer().toString());

        new TucsonSimple().saveRowHeader(w, s, "000001", 4, end);
        assertEquals("000001", w.getBuffer().toString());
    }

    @Test
    public void testSaveData() throws IOException {
        Sample s = new Sample();
        I18n i18n = mock(I18n.class);
        Year start = new Year(2010);
        Year end = new Year(2015);
        BufferedWriter writer = mock(BufferedWriter.class);
        StringWriter stringWriter = new StringWriter();
        BufferedWriter w = Mockito.spy(stringWriter);

        when(i18n.getText(Mockito.eq("format.tucsonsimple"))).thenReturn("TucsonSimple Format");

        new TucsonSimple().saveData(s, w);
        assertEquals("", w.getBuffer().toString());

        s.data = new Hashtable<Year, Double>() {{
            put(start, 1.0);
            put(end, 2.0);
        }};

        new TucsonSimple().saveData(s, w);
        String expectedOutput = "0000012010  1.0\n" +
                "0000012015  2.0\n";
        assertEquals(expectedOutput, w.getBuffer().toString());

        s.count = new Hashtable<Year, Integer>() {{
            put(start, 1);
            put(end, 2);
        }};
        new TucsonSimple().saveData(s, w, true);
        expectedOutput = "0000012010  1.001\n" +
                "0000012015  2.002\n";
        assertEquals(expectedOutput, w.getBuffer().toString());

        s.processed = true;
        new TucsonSimple().saveData(s, w);
        expectedOutput = "000001   999\n" +
                "9990   ";
        assertEquals(expectedOutput, w.getBuffer().toString());
    }

    @Test
    public void testComputeRange() {
        Sample s = new Sample();
        Year start = new Year(1990);
        Year end = new Year(2000);

        Range range = new TucsonSimple().computeRange(s);
        assertEquals(start, range.getStart());
        assertEquals(end, range.getEnd());

        s.range = new Range(new Year(1950), new Year(1955));
        range = new TucsonSimple().computeRange(s);
        assertEquals(new Year(1950), range.getStart());
        assertEquals(new Year(1955), range.getEnd());

        s.range = new Range(new Year(1980), new Year(1985));
        range = new TucsonSimple().computeRange(s);
        assertEquals(new Year(1980), range.getStart());
        assertEquals(new Year(1985), range.getEnd());

        s.range = new Range(new Year(1995), new Year(2000));
        range = new TucsonSimple().computeRange(s);
        assertEquals(new Year(1995), range.getStart());
        assertEquals(new Year(2000), range.getEnd());

        s.range = new Range(new Year(2000), new Year(2005));
        range = new TucsonSimple().computeRange(s);
        assertEquals(new Year(2001), range.getStart());
        assertEquals(new Year(2005), range.getEnd());
    }

    @Test
    public void testSave() throws IOException {
        Sample s = new Sample();
        I18n i18n = mock(I18n.class);
        when(i18n.getText(Mockito.eq("format.tucsonsimple"))).thenReturn("TucsonSimple Format");

        BufferedWriter writer = mock(BufferedWriter.class);

        new TucsonSimple().save(s, writer);
        assertEquals("", writer.getBuffer().toString());

        s.data = new Hashtable<Year, Double>() {{
            put(new Year(2010), 1.0);
            put(new Year(2015), 2.0);
        }};

        new TucsonSimple().save(s, writer);
        String expectedOutput = "0000012010  1.0\n" +
                "0000012015  2.0\n";
        assertEquals(expectedOutput, writer.getBuffer().toString());

        s.count = new Hashtable<Year, Integer>() {{
            put(new Year(2010), 1);
            put(new Year(2015), 2);
        }};
        new TucsonSimple().save(s, writer, true);
        expectedOutput = "0000012010  1.001\n" +
                "0000012015  2.002\n";
        assertEquals(expectedOutput, writer.getBuffer().toString());

        new TucsonSimple().save(s, writer);
        assertEquals("000001   999\n" +
                "9990   ", writer.getBuffer().toString());
    }
}