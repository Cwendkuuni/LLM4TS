package jscc.test.SimpleCharStream;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        for (int i = 0; i < 100; i++) {
            scs.readChar();
        }
        scs.ExpandBuff(false);
        assertEquals(4096 + 2048, scs.bufsize);
        assertTrue(scs.available > 4096);
        assertEquals(0, scs.tokenBegin);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        scs.FillBuff();
        assertEquals('t', scs.readChar());
        assertEquals('\r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('st', scs.read(2));
        assertEquals(-1, scs.read());
    }

    @Test
    public void testBeginTokenAndUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        char c = scs.BeginToken();
        assertEquals(c, '\r');
        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('t');
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        assertEquals('\r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals(-1, scs.read());
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\ntest".getBytes()), 1, 1, 4096);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());
        assertEquals(2, scs.getLine());
        assertEquals(7, scs.getColumn());
        assertEquals(3, scs.getLine());
        assertEquals(2, scs.getColumn());
        assertEquals(-1, scs.getColumn());
        assertEquals(-1, scs.getLine());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        assertEquals(-1, scs.getBeginLine());
        assertEquals(-1, scs.getBeginColumn());
        scs.Backup(2);
        assertEquals(1, scs.getBeginLine());
        assertEquals(3, scs.getBeginColumn());
        scs.Backup(10);
        assertEquals(1, scs.getBeginLine());
        assertEquals(2, scs.getBeginColumn());
        scs.Backup(100);
        assertEquals(1, scs.getBeginLine());
        assertEquals(1, scs.getBeginColumn());
    }

    @Test
    public void testGetImageAndGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        String image = SimpleCharStream.GetImage();
        char[] suffix = SimpleCharStream.GetSuffix(3);
        assertEquals("test\r\ntest", image);
        assertEquals("est", new String(suffix));
    }

    @Test(expected = IOException.class)
    public void testIOExceptionWhenClosingInputStream() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        scs.inputStream.close();
        scs.readChar();
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        SimpleCharStream.Done();
        assertEquals(null, SimpleCharStream.buffer);
        assertEquals(null, SimpleCharStream.bufline);
        assertEquals(null, SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\ntest".getBytes()), 1, 1, 4096);
        SimpleCharStream.adjustBeginLineColumn(1, 10);
        assertEquals(1, scs.getBeginLine());
        assertEquals(10, scs.getBeginColumn());
        SimpleCharStream.adjustBeginLineColumn(2, 15);
        assertEquals(2, scs.getBeginLine());
        assertEquals(15, scs.getBeginColumn());
    }
}