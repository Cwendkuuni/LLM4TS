package JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 2048);
        for (int i = 0; i < 2050; i++) {
            scs.readChar();
        }
        scs.ExpandBuff(false);
        assertEquals(4096, scs.bufsize);
        assertEquals(4096, scs.available);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 2048);
        assertNull(scs.readChar());
        scs.FillBuff();
        assertEquals('t', scs.readChar());
    }

    @Test(expected = IOException.class)
    public void testFillBuffWithIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 2048);
        scs.inputStream.close();
        scs.FillBuff();
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 2048);
        assertEquals('t', scs.BeginToken());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\rbar\n".getBytes()), 1, 1, 2048);
        assertEquals('t', scs.readChar());
        scs.UpdateLineColumn('t');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('f');
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.UpdateLineColumn('\t');
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.UpdateLineColumn('o');
        assertEquals(2, scs.getLine());
        assertEquals(4, scs.getColumn());

        scs.UpdateLineColumn('\r');
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('b');
        assertEquals(3, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.UpdateLineColumn('a');
        assertEquals(3, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(4, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testGetChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\rbar\n".getBytes()), 1, 1, 2048);
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals('t', scs.readChar());
        assertEquals('\r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals('f', scs.readChar());
        assertEquals('o', scs.readChar());
        assertEquals('o', scs.readChar());
        assertEquals('\r', scs.readChar());
        assertEquals('b', scs.readChar());
        assertEquals('a', scs.readChar());
        assertEquals('r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertNull(scs.readChar());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\rbar\n".getBytes()), 1, 1, 2048);
        assertEquals("test\r\nfoo\rbar\n", new String(scs.GetImage()));
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\rbar\n".getBytes()), 1, 1, 2048);
        assertArrayEquals("bar\n".toCharArray(), scs.GetSuffix(6));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\rbar\n".getBytes()), 1, 1, 2048);
        scs.Done();
        assertNull(scs.readChar());
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\rbar\n".getBytes()), 1, 1, 2048);
        scs.ReInit(new ByteArrayInputStream("test2\r\nfoo2\rbar2\n".getBytes()), 2, 2);
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals('t', scs.readChar());
        assertEquals('\r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals('f', scs.readChar());
        assertEquals('o', scs.readChar());
        assertEquals('o', scs.readChar());
        assertEquals('\r', scs.readChar());
        assertEquals('b', scs.readChar());
        assertEquals('a', scs.readChar());
        assertEquals('r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals('2', scs.getLine());
        assertEquals(1, scs.getColumn());
        assertNull(scs.readChar());
    }

    @Test
    public void testReInitWithIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\rbar\n".getBytes()), 1, 1, 2048);
        scs.ReInit(new ByteArrayInputStream(new byte[0]), 2, 2);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertNull(scs.readChar());
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\rbar\n".getBytes()), 1, 1, 2048);
        scs.adjustBeginLineColumn(3, 5);
        assertEquals(3, scs.getLine());
        assertEquals(5, scs.getColumn());
        assertEquals('f', scs.readChar());
        assertEquals('o', scs.readChar());
        assertEquals('o', scs.readChar());
        assertEquals('\r', scs.readChar());
        assertEquals('b', scs.readChar());
        assertEquals('a', scs.readChar());
        assertEquals('r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals(3, scs.getLine());
        assertEquals(12, scs.getColumn());
        assertNull(scs.readChar());
    }
}