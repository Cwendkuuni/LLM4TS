package junit.test.jsci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 256);
        assertEquals(256, scs.bufsize);
        for (int i = 0; i < 257; ++i) {
            scs.readChar();
        }
        scs.ExpandBuff(false);
        assertEquals(512, scs.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nline\r\n".getBytes()), 1, 1, 256);
        assertFalse(scs.available > 0);
        scs.FillBuff();
        assertTrue(scs.available > 0);
        assertEquals('\r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals('l', scs.readChar());
        assertEquals('i', scs.readChar());
        assertEquals('n', scs.readChar());
        assertEquals('\r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertFalse(scs.available > 0);
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nline\r\n".getBytes()), 1, 1, 256);
        assertEquals('\r', scs.BeginToken());
        assertEquals('\n', scs.BeginToken());
        assertEquals('t', scs.BeginToken());
        assertEquals('e', scs.BeginToken());
        assertEquals('s', scs.BeginToken());
        assertEquals(' ', scs.BeginToken());
        assertNull(scs.BeginToken());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nline\r\n".getBytes()), 1, 1, 256);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('l');
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.UpdateLineColumn('i');
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.UpdateLineColumn('n');
        assertEquals(2, scs.getLine());
        assertEquals(4, scs.getColumn());

        scs.UpdateLineColumn('\r');
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nline\r\n".getBytes()), 1, 1, 256);
        assertEquals('\r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals(' ', scs.readChar());
        assertNull(scs.readChar());
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nline\r\n".getBytes()), 1, 1, 256);

        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('l');
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.UpdateLineColumn('i');
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.UpdateLineColumn('n');
        assertEquals(2, scs.getLine());
        assertEquals(4, scs.getColumn());

        scs.UpdateLineColumn('\r');
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nline\r\n".getBytes()), 1, 1, 256);
        assertEquals(3, scs.bufpos);
        scs.backup(1);
        assertEquals(2, scs.bufpos);

        scs.UpdateLineColumn('\r');
        assertEquals(3, scs.getLine());
        scs.backup(1);
        assertEquals(2, scs.getLine());

        scs.UpdateLineColumn('\n');
        assertEquals(4, scs.getColumn());
        scs.backup(3);
        assertEquals(1, scs.getColumn());
        assertEquals(2, scs.getLine());

        scs.UpdateLineColumn('l');
        assertEquals(3, scs.getLine());
        scs.backup(1);
        assertEquals(3, scs.getLine());

        scs.UpdateLineColumn('i');
        assertEquals(3, scs.getLine());
        scs.backup(1);
        assertEquals(3, scs.getLine());

        scs.UpdateLineColumn('n');
        assertEquals(3, scs.getLine());
        scs.backup(1);
        assertEquals(3, scs.getLine());

        scs.UpdateLineColumn('\r');
        assertEquals(4, scs.getLine());
        scs.backup(1);
        assertEquals(4, scs.getLine());

        scs.UpdateLineColumn('\n');
        assertEquals(1, scs.getLine());
        scs.backup(1);
        assertEquals(1, scs.getLine());
    }

    @Test
    public void testConstructor() {
        SimpleCharStream scs1 = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 256);
        SimpleCharStream scs2 = new SimpleCharStream(scs1.inputStream, 1, 1);
        assertNotNull(scs1.inputStream);
        assertNotNull(scs2.inputStream);
        assertSame(scs1.inputStream, scs2.inputStream);
        SimpleCharStream.Done();
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs1 = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 256);
        scs1.ReInit(new ByteArrayInputStream("reinit".getBytes()), 4, 5);
        assertEquals(5, scs1.getBeginLine());
        assertEquals(4, scs1.getBeginColumn());
        assertEquals("reinit", SimpleCharStream.GetImage());

        SimpleCharStream scs2 = new SimpleCharStream(scs1);
        scs2.ReInit(new ByteArrayInputStream("reinit2".getBytes()), 4, 5);
        assertEquals(5, scs2.getBeginLine());
        assertEquals(4, scs2.getBeginColumn());
        assertEquals("reinit2", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nline\r\n".getBytes()), 1, 1, 256);
        assertEquals("test\r\nline\r\n", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 256);
        assertEquals("", SimpleCharStream.GetSuffix(0));
        assertEquals("t", SimpleCharStream.GetSuffix(1));
        assertEquals("st", SimpleCharStream.GetSuffix(2));
        assertEquals("est", SimpleCharStream.GetSuffix(3));
        assertEquals("est", SimpleCharStream.GetSuffix(4));
        assertEquals("est", SimpleCharStream.GetSuffix(5));
        assertEquals("est", SimpleCharStream.GetSuffix(6));
        assertEquals("est", SimpleCharStream.GetSuffix(7));
        assertEquals("est", SimpleCharStream.GetSuffix(8));
        assertEquals("est", SimpleCharStream.GetSuffix(9));
        assertEquals("est", SimpleCharStream.GetSuffix(10));
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nline\r\n".getBytes()), 1, 1, 256);
        SimpleCharStream.adjustBeginLineColumn(3, 5);
        assertEquals(3, scs.getBeginLine());
        assertEquals(5, scs.getBeginColumn());
    }

    @Test(expected = Error.class)
    public void testError() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 256);
        scs.ExpandBuff(false);
        scs.ExpandBuff(false);
    }
}