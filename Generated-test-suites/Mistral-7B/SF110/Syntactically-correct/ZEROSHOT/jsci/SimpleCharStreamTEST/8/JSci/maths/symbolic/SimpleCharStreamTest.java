package jsic.test.maths.symbolic;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
        char c = scs.BeginToken();
        assertEquals('t', c);
        int line = scs.getLine();
        int column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(2, column);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
        char c = scs.BeginToken();
        scs.UpdateLineColumn(c);
        c = scs.readChar();
        scs.UpdateLineColumn(c);
        int line2 = scs.getLine();
        int column2 = scs.getColumn();
        assertEquals(1, line2);
        assertEquals(3, column2);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
        char c = scs.BeginToken();
        assertEquals('t', c);
        c = scs.readChar();
        assertEquals('e', c);
        int line = scs.getLine();
        int column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
        char c = scs.BeginToken();
        assertEquals('t', c);
        int line = scs.getLine();
        int column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(1, column);
        c = scs.readChar();
        assertEquals('e', c);
        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
        char c = scs.BeginToken();
        assertEquals('t', c);
        int line = scs.getLine();
        int column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(1, column);
        c = scs.readChar();
        assertEquals('e', c);
        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);
        scs.backup(1);
        c = scs.readChar();
        assertEquals('t', c);
        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
        char c = scs.BeginToken();
        assertEquals('t', c);
        int line = scs.getLine();
        int column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(1, column);
        c = scs.readChar();
        assertEquals('e', c);
        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);
        scs.ReInit(new ByteArrayInputStream("test2\r\ntest2".getBytes()), 1, 1);
        c = scs.readChar();
        assertEquals('t', c);
        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(1, column);
    }

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
        char c = scs.BeginToken();
        assertEquals('t', c);
        scs.ExpandBuff(true);
        c = scs.readChar();
        assertEquals('e', c);
        int line = scs.getLine();
        int column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);
        scs.ExpandBuff(false);
        c = scs.readChar();
        assertEquals('t', c);
        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);
    }

    @Test(expected = IOException.class)
    public void testFillBuffException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
        char c = scs.BeginToken();
        assertEquals('t', c);
        scs.available = 0;
        scs.FillBuff();
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
        String image = scs.GetImage();
        assertEquals("test\r\ntest", image);
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
        char[] suffix = scs.GetSuffix(5);
        assertEquals("test", new String(suffix));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1);
        scs.Done();
    }

    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest1\r\ntest2\r\ntest3".getBytes()), 1, 1);
        scs.adjustBeginLineColumn(2, 1);
        int line = scs.getLine();
        int column = scs.getColumn();
        int line2 = scs.getBeginLine();
        int column2 = scs.getBeginColumn();
        assertEquals(3, line);
        assertEquals(1, column);
        assertEquals(3, line2);
        assertEquals(1, column2);
    }
}