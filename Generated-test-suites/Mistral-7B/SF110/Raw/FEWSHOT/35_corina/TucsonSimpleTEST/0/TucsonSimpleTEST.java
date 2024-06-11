package corina.formats;

import static org.junit.Assert. assertArrayEquals;
import static org.junit.Assert. assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import corina.formats.TucsonSimple;
import corina.ui.I18n;
import corina.util.StringUtils;
import corina.util.Year;
import corina.util.WrongFiletypeException;

public class TucsonSimpleTest {

    private BufferedWriter writer;
    private File file;

    @Before
    public void setUp() throws IOException {
        file = File.createTempFile("TucsonSimpleTest", null);
        writer = new BufferedWriter(new FileWriter(file));
    }

    @After
    public void tearDown() throws Exception {
        writer.close();
        file.delete();
    }

    @Test
    public void testMake6digitCode() {
        Sample s = new Sample(new HashMap<String, Object>(), "sample data", new Range(new Year(1), new Year(1)));
        String code = new TucsonSimple().make6digitCode(s);
        assertEquals("000000", code);
        
        s.meta.put("id", 12345);
        code = new TucsonSimple().make6digitCode(s);
        assertEquals("12345", code);
        
        s.meta.put("id", "12345");
        code = new TucsonSimple().make6digitCode(s);
        assertEquals("12345", code);
    }

    @Test
    public void testSaveRowHeader() throws IOException {
        Sample s = new Sample(new HashMap<String, Object>(), "sample data", new Range(new Year(1), new Year(1)));
        String code = "123456";
        int yearWidth = 6;
        Year y = new Year(1);

        new TucsonSimple().saveRowHeader(writer, s, code, yearWidth, y);
        assertEquals("123456 1", writer.getBuffer().toString());

        y = new Year(1999);
        new TucsonSimple().saveRowHeader(writer, s, code, yearWidth, y);
        assertEquals("123456 1999", writer.getBuffer().toString());
    }

    @Test
    public void testSaveData() throws IOException {
        Sample s = new Sample(new HashMap<String, Object>(), "sample data", new Range(new Year(1), new Year(1)));
        Year start = new Year(1);
        Year end = new Year(10);
        String code = "123456";
        boolean isSummed = false;
        boolean isProcessed = false;

        new TucsonSimple().saveData(s, writer);
        assertEquals("", writer.getBuffer().toString());

        writer.write("123456 1\n");
        new TucsonSimple().saveData(s, writer);
        assertEquals("123456 1\n123456 2\n", writer.getBuffer().toString());

        writer.write("123456 1\n123456 2\n9990 3\n");
        new TucsonSimple().saveData(s, writer, isSummed, isProcessed);
        assertEquals("123456 1\n123456 2\n9990 3\n999\n", writer.getBuffer().toString());

        s.data = new double[] { 1.1, 2.2, 3.3 };
        writer.write("123456 1\n123456 2\n");
        new TucsonSimple().saveData(s, writer);
        assertEquals("123456 1\n123456 2\n1.1\n1.1\n1.1\n", writer.getBuffer().toString());

        writer.write("123456 1\n123456 2\n");
        new TucsonSimple().saveData(s, writer, isSummed, isProcessed, start, end);
        assertEquals("123456 1\n123456 2\n1.1\n1.1\n1.1\n9990 0\n", writer.getBuffer().toString());

        writer.write("123456 1\n123456 2\n9990 0\n");
        new TucsonSimple().saveData(s, writer, isSummed, isProcessed, start, end);
        assertEquals("123456 1\n123456 2\n9990 0\n999\n", writer.getBuffer().toString());

        writer.write("123456 1\n123456 2\n9990 0\n");
        new TucsonSimple().saveData(s, writer, isSummed, isProcessed, start.add(3000), end);
        assertEquals("123456 1\n123456 2\n9990 0\n9990   \n", writer.getBuffer().toString());
    }

    @Test
    public void testComputeRange() {
        Sample s = new Sample(new HashMap<String, Object>(), "sample data", new Range(new Year(1), new Year(1)));
        Year start = new Year(1);
        Year end = new Year(10);

        Range range = new TucsonSimple().computeRange(s);
        assertEquals(start, range.getStart());
        assertEquals(end, range.getEnd());

        s.meta.put("start", "1999-01-01");
        range = new TucsonSimple().computeRange(s);
        assertEquals(new Year(1999), range.getStart());
        assertEquals(end, range.getEnd());

        s.meta.put("start", "1999-12-31");
        range = new TucsonSimple().computeRange(s);
        assertEquals(new Year(2000), range.getStart());
        assertEquals(end, range.getEnd());

        s.meta.put("start", "1999-12-31");
        range = new TucsonSimple().computeRange(s);
        assertEquals(new Year(2000), range.getStart());
        assertEquals(new Year(2000), range.getEnd());

        s.meta.put("start", "1999-01-01");
        range = new TucsonSimple().computeRange(s);
        assertEquals(start, range.getStart());
        assertEquals(new Year(1001), range.getEnd());
    }

    @Test
    public void testSave() throws IOException {
        Sample s = new Sample(new HashMap<String, Object>(), "sample data", new Range(new Year(1), new Year(1)));
        String data = "1\n2\n3\n";
        String count = "1\n1\n1\n";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        PrintWriter writer = new PrintWriter(new StringWriter());
        StringWriter stringWriter = new StringWriter();
        PrintWriter out = new PrintWriter(stringWriter);

        when(reader.readLine()).thenReturn(data);
        when(reader.readLine()).thenReturn(count);
        when(reader.readLine()).thenReturn(null);

        new TucsonSimple().save(s, new BufferedWriter(new FileWriter(file)));
        new TucsonSimple().save(s, writer);

        assertArrayEquals(data.split("\n"), writer.getBuffer().toString().split("\n"));

        writer.flush();
        String fileContent = new String(outputStream.toByteArray(), "UTF-8");
        assertEquals(data, fileContent);

        reader.close();
        writer.close();
        out.close();
    }

    @Test(expected = WrongFiletypeException.class)
    public void testSaveWrongFiletype() throws IOException {
        Sample s = new Sample(new HashMap<String, Object>(), "sample data", new Range(new Year(1), new Year(1)));
        new TucsonSimple().save(s, writer);
    }
}