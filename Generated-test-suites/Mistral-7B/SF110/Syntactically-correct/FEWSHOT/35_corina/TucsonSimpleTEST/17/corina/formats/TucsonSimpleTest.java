package corina.formats.test;

import static org.junit.Assert.*;
import static corina.util.StringUtils.leftPad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.Year;
import corina.util.Sample;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

    @Test
    public void testMake6digitCode() {
        Sample s = new Sample();
        s.meta.put("id", "123456");
        String code = new TucsonSimple().make6digitCode(s);
        assertEquals("123456", code);

        s = new Sample();
        code = new TucsonSimple().make6digitCode(s);
        assertEquals("000000", code);

        s.meta.put("id", "000001");
        code = new TucsonSimple().make6digitCode(s);
        assertEquals("000001", code);

        s.meta.put("id", "abcdef");
        code = new TucsonSimple().make6digitCode(s);
        assertEquals("00000a", code);

        s.meta.put("id", "1234567");
        code = new TucsonSimple().make6digitCode(s);
        assertEquals("123456", code);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        BufferedWriter w = new BufferedWriter(new FileWriter("testFile.rwl"));
        Sample s = new Sample();
        String code = "123456";
        int yearWidth = 6;
        Year start = new Year(2000);
        new TucsonSimple().saveRowHeader(w, s, code, yearWidth, start);
        assertEquals("1234562000", w.toString());

        Year end = new Year(2001);
        new TucsonSimple().saveRowHeader(w, s, code, yearWidth, end);
        assertEquals("1234562000\n1234562001", w.toString());
    }

    @Test
    public void testSaveData() throws IOException {
        Sample s = new Sample();
        s.data = new Integer[10];
        s.count = new Integer[10];
        BufferedWriter w = new BufferedWriter(new FileWriter("testFile.rwl"));
        BufferedReader r = new BufferedReader(new StringReader("1 2 3 4 5 6 7 8 9 10"));
        new TucsonSimple().load(r, s);

        new TucsonSimple().saveData(s, w);
        assertEquals("1234562000\n1 2\n1234562001\n3 4\n1234562002\n5 5\n1234562003\n6 6\n1234562004\n7 7\n1234562005\n8 8\n1234562006\n9 9\n1234562007\n10 10\n1234562008\n   999\n1234562009\n9990   ", w.toString());

        w.close();
    }

    @Test
    public void testLoad() throws IOException, WrongFiletypeException {
        Sample s = new Sample();
        BufferedReader r = new BufferedReader(new FileReader("testFile.rwl"));
        new TucsonSimple().load(r, s);

        assertEquals(10, s.data.length);
        assertEquals(10, s.count.length);
        assertEquals(new Integer(1), s.data[0]);
        assertEquals(new Integer(1), s.count[0]);
        assertEquals(new Integer(2), s.data[1]);
        assertEquals(new Integer(1), s.count[1]);
        assertEquals(new Integer(3), s.data[2]);
        assertEquals(new Integer(1), s.count[2]);
        assertEquals(new Integer(4), s.data[3]);
        assertEquals(new Integer(1), s.count[3]);
        assertEquals(new Integer(5), s.data[4]);
        assertEquals(new Integer(1), s.count[4]);
        assertEquals(new Integer(6), s.data[5]);
        assertEquals(new Integer(1), s.count[5]);
        assertEquals(new Integer(7), s.data[6]);
        assertEquals(new Integer(1), s.count[6]);
        assertEquals(new Integer(8), s.data[7]);
        assertEquals(new Integer(1), s.count[7]);
        assertEquals(new Integer(9), s.data[8]);
        assertEquals(new Integer(1), s.count[8]);
        assertEquals(new Integer(10), s.data[9]);
        assertEquals(new Integer(1), s.count[9]);
        assertEquals(new Integer(999), s.data[10]);

        r.close();
        new File("testFile.rwl").delete();
    }

    @Test
    public void testComputeRange() {
        Sample s = new Sample();
        s.range = new YearRange(new Year(2000), new Year(2001));
        Range r = new TucsonSimple().computeRange(s);
        assertEquals(s.range, r);

        s.range = new YearRange(new Year(1999), new Year(2000));
        r = new TucsonSimple().computeRange(s);
        assertEquals(new YearRange(new Year(1001), new Year(1001)), r);

        s.range = new YearRange(new Year(1800), new Year(1900));
        r = new TucsonSimple().computeRange(s);
        assertEquals(new YearRange(new Year(1001), new Year(1001)).redateStartTo(new Year(8001)), r);

        s.range = new YearRange(new Year(-1000), new Year(1));
        r = new TucsonSimple().computeRange(s);
        assertEquals(new YearRange(new Year(1), new Year(1)), r);
    }

    @Test
    public void testToString() {
        assertEquals("format.tucsonsimple", new TucsonSimple().toString());
    }

    @Test(expected = WrongFiletypeException.class)
    public void testLoadException() throws IOException, WrongFiletypeException {
        Sample s = new Sample();
        BufferedReader r = new BufferedReader(new StringReader("wrongFileType"));
        new TucsonSimple().load(r, s);
    }

    @Test
    public void testSaveException() throws IOException {
        Sample s = new Sample();
        BufferedWriter w = new BufferedWriter(new FileWriter("testFile.rwl"));
        new TucsonSimple().save(s, w);
        w.close();
        File file = new File("testFile.rwl");
        file.delete();
    }
}