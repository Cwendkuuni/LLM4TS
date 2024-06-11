package corina.formats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TucsonSimpleTest {

    @Test
    public void testMake6digitCode() {
        Sample s = mock(Sample.class);
        when(s.meta.get("id")).thenReturn("12345");

        TucsonSimple tucsonSimple = new TucsonSimple();
        String code = tucsonSimple.make6digitCode(s);

        assertEquals("123456", code);

        when(s.meta.get("id")).thenReturn(null);

        code = tucsonSimple.make6digitCode(s);

        assertEquals("000000", code);

        s.meta.put("id", "001234");

        code = tucsonSimple.make6digitCode(s);

        assertEquals("001234", code);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);

        Sample s = mock(Sample.class);
        when(s.range.getStart()).thenReturn(new Year(2000));

        String code = "ABCDEF";
        int yearWidth = 6;

        tucsonSimple.saveRowHeader(writer, s, code, yearWidth, null);

        String expected = "ABCDEF2000";
        String actual = "";
        try (StringBuilder sb = new StringBuilder()) {
            while ((actual = writer.write(expected.charAt(0))) > 0) {
                sb.append((char) actual);
            }
            actual = sb.toString();
        }

        assertEquals(expected, actual);

        Year start = new Year(1999);

        tucsonSimple.saveRowHeader(writer, s, code, yearWidth, start);

        expected = "ABCDEF1999";
        actual = "";
        try (StringBuilder sb = new StringBuilder()) {
            while ((actual = writer.write(expected.charAt(0))) > 0) {
                sb.append((char) actual);
            }
            actual = sb.toString();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testSaveData() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);

        Sample s = mock(Sample.class);
        Range range = new Range(new Year(2000), new Year(2002));
        when(s.range).thenReturn(range);
        when(s.data.get(0)).thenReturn(new Double(1.2));
        when(s.count.get(0)).thenReturn(new Integer(3));

        String code = "ABCDEF";
        boolean isSummed = true;
        boolean isProcessed = true;
        int yearWidth = 4;

        tucsonSimple.saveData(s, writer);

        String expected = "ABCDEF2000   1.200\n" +
                "ABCDEF2001   1.200\n" +
                "ABCDEF2002   1.200\n" +
                "9990  3\n";
        String actual = "";
        try (StringBuilder sb = new StringBuilder()) {
            while ((actual = writer.write(expected.charAt(0))) > 0) {
                sb.append((char) actual);
            }
            actual = sb.toString();
        }

        assertEquals(expected, actual);

        when(s.data.get(0)).thenReturn(null);
        when(s.count.get(0)).thenReturn(null);

        tucsonSimple.saveData(s, writer);

        expected = "ABCDEF2000   999\n" +
                "ABCDEF2001   9990   \n" +
                "ABCDEF2002   9990   \n" +
                "9990  0\n";
        actual = "";
        try (StringBuilder sb = new StringBuilder()) {
            while ((actual = writer.write(expected.charAt(0))) > 0) {
                sb.append((char) actual);
            }
            actual = sb.toString();
        }

        assertEquals(expected, actual);
    }

    @Test
    public void testComputeRange() {
        Sample s = mock(Sample.class);
        when(s.range.getStart()).thenReturn(new Year(1998));

        TucsonSimple tucsonSimple = new TucsonSimple();
        Range range = tucsonSimple.computeRange(s);

        assertEquals(new Range(new Year(1998), new Year(1998)), range);

        when(s.range.getStart()).thenReturn(new Year(9000));

        range = tucsonSimple.computeRange(s);

        assertEquals(new Range(new Year(9000), new Year(9000)), range);

        when(s.range.getStart()).thenReturn(new Year(1000));

        range = tucsonSimple.computeRange(s);

        assertEquals(new Range(new Year(1001), new Year(1001)), range);
    }

    @Test
    public void testSave() throws IOException {
        TucsonSimple tucsonSimple = new TucsonSimple();
        BufferedWriter writer = mock(BufferedWriter.class);

        Sample s = mock(Sample.class);
        Range range = new Range(new Year(2000), new Year(2002));
        when(s.range).thenReturn(range);
        when(s.data).thenReturn(new HashMap<>());
        when(s.count).thenReturn(new HashMap<>());

        tucsonSimple.save(s, writer);

        assertNull(writer.write);
    }
}