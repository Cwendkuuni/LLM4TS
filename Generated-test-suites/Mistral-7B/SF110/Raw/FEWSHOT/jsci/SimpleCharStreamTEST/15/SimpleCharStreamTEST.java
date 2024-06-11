package JSci.maths.symbolic.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 10);
        scs.ExpandBuff(false);
        assertEquals(11, scs.bufsize);
        assertEquals(1, scs.bufpos);
        assertEquals(1, scs.tokenBegin);
        assertEquals(11, scs.available);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 10);
        scs.FillBuff();
        assertEquals('t', scs.readChar());
        assertEquals(2, scs.bufpos);
        assertEquals(1, scs.tokenBegin);
        assertEquals(10, scs.available);
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 10);
        assertEquals('\n', scs.BeginToken());
        assertEquals(-1, scs.bufpos);
        assertEquals(0, scs.tokenBegin);
        assertEquals(1, scs.available);
    }

    @Test(expected = IOException.class)
    public void testFillBuff_IOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 10);
        scs.FillBuff();
        scs.inputStream.close();
        scs.FillBuff();
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\rbar".getBytes()), 1, 1, 10);
        assertEquals('t', scs.readChar());
        scs.UpdateLineColumn('t');
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);
        assertEquals('\r', scs.readChar());
        scs.UpdateLineColumn('\r');
        assertEquals(2, scs.line);
        assertEquals(1, scs.column);
        assertEquals('f', scs.readChar());
        scs.UpdateLineColumn('f');
        assertEquals(2, scs.line);
        assertEquals(3, scs.column);
        assertEquals('o', scs.readChar());
        scs.UpdateLineColumn('o');
        assertEquals(2, scs.line);
        assertEquals(4, scs.column);
        assertEquals('\n', scs.readChar());
        scs.UpdateLineColumn('\n');
        assertEquals(3, scs.line);
        assertEquals(1, scs.column);
        assertEquals('f', scs.readChar());
        scs.UpdateLineColumn('f');
        assertEquals(3, scs.line);
        assertEquals(2, scs.column);
        assertEquals('o', scs.readChar());
        scs.UpdateLineColumn('o');
        assertEquals(3, scs.line);
        assertEquals(3, scs.column);
        assertEquals('r', scs.readChar());
        scs.UpdateLineColumn('r');
        assertEquals(3, scs.line);
        assertEquals(4, scs.column);
        assertEquals('b', scs.readChar());
        scs.UpdateLineColumn('b');
        assertEquals(3, scs.line);
        assertEquals(5, scs.column);
        assertEquals('\r', scs.readChar());
        scs.UpdateLineColumn('\r');
        assertEquals(4, scs.line);
        assertEquals(1, scs.column);
        assertEquals('b', scs.readChar());
        scs.UpdateLineColumn('b');
        assertEquals(4, scs.line);
        assertEquals(2, scs.column);
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\rbar".getBytes()), 1, 1, 10);
        assertEquals("test\r\nfoo\rbar", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("testfoo\rbar".getBytes()), 1, 1, 10);
        assertEquals("foo\rbar", SimpleCharStream.GetSuffix(7));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 10);
        SimpleCharStream.Done();
        assertEquals(null, SimpleCharStream.buffer);
        assertEquals(null, SimpleCharStream.bufline);
        assertEquals(null, SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\nfoo\nbar\nqux".getBytes()), 1, 1, 10);
        scs.adjustBeginLineColumn(2, 2);
        assertEquals(2, scs.line);
        assertEquals(2, scs.column);
        assertEquals('t', scs.readChar());
        scs.UpdateLineColumn('t');
        assertEquals(2, scs.line);
        assertEquals(3, scs.column);
        assertEquals('e', scs.readChar());
        scs.UpdateLineColumn('e');
        assertEquals(2, scs.line);
        assertEquals(4, scs.column);
        assertEquals('s', scs.readChar());
        scs.UpdateLineColumn('s');
        assertEquals(2, scs.line);
        assertEquals(5, scs.column);
        assertEquals('t', scs.readChar());
        scs.UpdateLineColumn('t');
        assertEquals(2, scs.line);
        assertEquals(6, scs.column);
        assertEquals('\n', scs.readChar());
        scs.UpdateLineColumn('\n');
        assertEquals(3, scs.line);
        assertEquals(1, scs.column);
        assertEquals('f', scs.readChar());
        scs.UpdateLineColumn('f');
        assertEquals(3, scs.line);
        assertEquals(2, scs.column);
        assertEquals('o', scs.readChar());
        scs.UpdateLineColumn('o');
        assertEquals(3, scs.line);
        assertEquals(3, scs.column);
        assertEquals('\n', scs.readChar());
        scs.UpdateLineColumn('\n');
        assertEquals(4, scs.line);
        assertEquals(1, scs.column);
        assertEquals('b', scs.readChar());
        scs.UpdateLineColumn('b');
        assertEquals(4, scs.line);
        assertEquals(2, scs.column);
        assertEquals('a', scs.readChar());
        scs.UpdateLineColumn('a');
        assertEquals(4, scs.line);
        assertEquals(3, scs.column);
        assertEquals('\n', scs.readChar());
        scs.UpdateLineColumn('\n');
        assertEquals(5, scs.line);
        assertEquals(1, scs.column);
        assertEquals('q', scs.readChar());
        scs.UpdateLineColumn('q');
        assertEquals(5, scs.line);
        assertEquals(2, scs.column);
        assertEquals('u', scs.readChar());
        scs.UpdateLineColumn('u');
        assertEquals(5, scs.line);
        assertEquals(3, scs.column);
        assertEquals('x', scs.readChar());
        scs.UpdateLineColumn('x');
        assertEquals(5, scs.line);
        assertEquals(4, scs.column);
    }
}