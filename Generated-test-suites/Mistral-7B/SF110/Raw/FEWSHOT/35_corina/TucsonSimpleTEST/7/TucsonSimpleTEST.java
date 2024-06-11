package corina.formats.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import corina.formats.I18n;
import corina.formats.Sample;
import corina.formats.StringUtils;
import corina.formats.TucsonSimple;
import corina.formats.Year;
import corina.ui.UiFactory;

public class TucsonSimpleTest {

    @Test
    public void testToString() {
        TucsonSimple ut = new TucsonSimple();
        String result = ut.toString();
        assertEquals("format.tucsonsimple", result);
    }

    @Test
    public void testgetDefaultExtension() {
        TucsonSimple ut = new TucsonSimple();
        String result = ut.getDefaultExtension();
        assertEquals(".RWL", result);
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoad() throws IOException {
        TucsonSimple ut = new TucsonSimple();
        BufferedReader reader = mock(BufferedReader.class);
        ut.load(reader);
    }

    @Test
    public void testMake6digitCode() {
        Sample s = new Sample();
        s.meta = new HashMap<String, Object>();
        s.meta.put("id", "123456");
        String result = new TucsonSimple().make6digitCode(s);
        assertEquals("123456", result);

        s.meta.put("id", null);
        result = new TucsonSimple().make6digitCode(s);
        assertEquals("000000", result);

        s.meta.put("id", "12345");
        result = new TucsonSimple().make6digitCode(s);
        assertEquals("000123", result);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple ut = new TucsonSimple();
        Sample s = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        String code = "123456";
        int yearWidth = 6;
        Year start = new Year(1990);

        ut.saveRowHeader(writer, s, code, yearWidth, start);
        verify(writer).write(code + "1990");
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple ut = new TucsonSimple();
        Sample s = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        Year start = new Year(1990);
        Year end = new Year(1992);
        String code = "123456";
        boolean isSummed = false;
        boolean isProcessed = false;

        ut.saveData(s, writer);

        verify(writer, times(4)).write(StringUtils.leftPad("", isProcessed ? 4 : 6));
        verify(writer).write(code + "1990");
        verify(writer).write("9990   ");
        verify(writer).write("9990   ");
        verify(writer).write("9990   ");
        verify(writer).write("9990   ");
        verify(writer).write("9990   ");
        verify(writer).write("9990  ");
        verify(writer).write("999");
        verify(writer).newLine();
        verify(writer).newLine();

        ut.saveData(s, writer, isSummed, isProcessed);

        verify(writer, times(11)).write(StringUtils.leftPad("", isProcessed ? 4 : 6));
        verify(writer).write(code + "1990");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("999");
        verify(writer).newLine();
        verify(writer).newLine();

        Year y = new Year(1991);
        ut.saveData(s, writer, isSummed, isProcessed, y);

        verify(writer, times(13)).write(StringUtils.leftPad("", isProcessed ? 4 : 6));
        verify(writer).write(code + "1990");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("9990  ");
        verify(writer).write("1");
        verify(writer).newLine();
        verify(writer).newLine();
    }

    @Test
    public void testComputeRange() {
        Sample s = new Sample();
        s.range = new Range(new Year(1985), new Year(1987));

        Range result = new TucsonSimple().computeRange(s);
        assertEquals(s.range, result);

        s.range.setStart(new Year(1988));
        result = new TucsonSimple().computeRange(s);
        assertEquals(s.range.redateStartTo(new Year(1988)), result);

        s.range.setStart(new Year(1999));
        result = new TucsonSimple().computeRange(s);
        assertEquals(s.range.redateBy(8000), result);
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple ut = new TucsonSimple();
        Sample s = new Sample();
        BufferedWriter writer = mock(BufferedWriter.class);
        Year start = new Year(1990);
        Year end = new Year(1992);
        String code = "123456";

        ut.save(s, writer);

        verify(writer).write("1234561990");
        verify(writer).write("       ");
        verify(writer).write("9990   ");
        verify(writer).write("9990   ");
        verify(writer).write("9990   ");
        verify(writer).write("9990   ");
        verify(writer).write("9990  ");
        verify(writer).write("999");
        verify(writer).newLine();
        verify(writer).newLine();
    }
}