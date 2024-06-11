package test.JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        scs.ExpandBuff(false);
        assertEquals(8192, scs.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        scs.FillBuff();
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals(0, scs.readChar());
        scs.ExpandBuff(false);
        assertEquals('t', scs.readChar());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nhello\r\nworld".getBytes()), 1, 1);
        scs.UpdateLineColumn('t');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('h');
        assertEquals(1, scs.getLine());
        assertEquals(2, scs.getColumn());
        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('w');
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());
        scs.UpdateLineColumn('\r');
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());
        scs.UpdateLineColumn('o');
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());
        scs.UpdateLineColumn('ld');
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nhello\r\nworld".getBytes()), 1, 1);
        scs.UpdateLineColumn('t');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.backup(1);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testStaticVariables() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        SimpleCharStream scs2 = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        assertTrue(scs.staticFlag);
        assertTrue(scs2.staticFlag);
        assertEquals(8192, SimpleCharStream.bufsize);
        SimpleCharStream.bufsize = 1024;
        assertEquals(1024, SimpleCharStream.bufsize);
        assertEquals(0, SimpleCharStream.available);
        SimpleCharStream.available = 1024;
        assertEquals(1024, SimpleCharStream.available);
        SimpleCharStream.tokenBegin = 0;
        assertEquals(0, SimpleCharStream.tokenBegin);
    }

    @Test
    public void testConstructor() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        SimpleCharStream scs2 = new SimpleCharStream(scs.inputStream, 1, 1);
        SimpleCharStream scs3 = new SimpleCharStream(scs.inputStream);
        SimpleCharStream scs4 = new SimpleCharStream(scs.inputStream, 1, 1, 2048);
        SimpleCharStream scs5 = new SimpleCharStream(scs.inputStream, 1, 1, 4096);
        SimpleCharStream scs6 = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 2048);
        SimpleCharStream scs7 = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        SimpleCharStream scs8 = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 8192);
        SimpleCharStream scs9 = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 16384);

        assertEquals(scs.inputStream, scs2.inputStream);
        assertEquals(scs.inputStream, scs3.inputStream);
        assertEquals(scs.inputStream, scs4.inputStream);
        assertEquals(scs.inputStream, scs5.inputStream);
        assertEquals(scs.inputStream, scs6.inputStream);
        assertEquals(scs.inputStream, scs7.inputStream);
        assertEquals(scs.inputStream, scs8.inputStream);
        assertEquals(scs.inputStream, scs9.inputStream);
        assertEquals(scs.bufsize, scs2.bufsize);
        assertEquals(scs.bufsize, scs3.bufsize);
        assertEquals(scs.bufsize, scs4.bufsize);
        assertEquals(scs.bufsize, scs5.bufsize);
        assertEquals(scs.bufsize, scs6.bufsize);
        assertEquals(scs.bufsize, scs7.bufsize);
        assertEquals(scs.bufsize, scs8.bufsize);
        assertEquals(scs.bufsize, scs9.bufsize);
        assertEquals(scs.available, scs2.available);
        assertEquals(scs.available, scs3.available);
        assertEquals(scs.available, scs4.available);
        assertEquals(scs.available, scs5.available);
        assertEquals(scs.available, scs6.available);
        assertEquals(scs.available, scs7.available);
        assertEquals(scs.available, scs8.available);
        assertEquals(scs.available, scs9.available);
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        scs.FillBuff();
        scs.ReInit(scs.inputStream);
        assertEquals(0, scs.bufpos);
        assertEquals(0, scs.tokenBegin);
        assertEquals(0, scs.available);
        assertEquals(0, scs.maxNextCharInd);
        assertEquals(0, scs.inBuf);
        assertEquals('t', scs.readChar());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        scs.FillBuff();
        assertEquals("test", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("testtesttest".getBytes()), 1, 1);
        scs.FillBuff();
        scs.backup(5);
        assertEquals("testtest", SimpleCharStream.GetSuffix(3));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        scs.FillBuff();
        SimpleCharStream.Done();
        assertEquals(false, SimpleCharStream.bufline != null);
        assertEquals(false, SimpleCharStream.bufcolumn != null);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nhello\r\nworld".getBytes()), 1, 1);
        scs.UpdateLineColumn('t');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.adjustBeginLineColumn(3, 5);
        assertEquals(3, scs.getLine());
        assertEquals(5, scs.getColumn());
    }

}