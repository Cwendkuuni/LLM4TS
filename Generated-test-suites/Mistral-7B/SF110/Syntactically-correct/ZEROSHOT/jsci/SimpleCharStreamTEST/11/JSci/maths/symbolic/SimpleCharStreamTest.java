package jsit.SimpleCharStreamTest;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    @Test
    public void testInitialization() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());
        assertEquals(1, charStream.getBeginLine());
        assertEquals(1, charStream.getBeginColumn());
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1);
        char c = charStream.BeginToken();
        assertEquals('\t', c);
        assertEquals(1, charStream.getLine());
        assertEquals(2, charStream.getColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1);
        char c = charStream.BeginToken();
        assertEquals('\r', c);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('\n', c);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\tcolumn1\nline2\tcolumn2".getBytes()), 1, 1);

        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        char c = charStream.readChar();
        assertEquals('\r', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('\n', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('t', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('e', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('s', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(5, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('\n', c);
        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('l', c);
        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('i', c);
        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('n', c);
        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('\t', c);
        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(5, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('c', c);
        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(6, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('o', c);
        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(7, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('l', c);
        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(8, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('\n', c);
        charStream.UpdateLineColumn(c);
        assertEquals(3, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        assertNull(c);
        assertEquals(3, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        assertNull(c);
        assertEquals(3, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);

        for (int i = 0; i < 5000; i++) {
            charStream.readChar();
        }

        charStream.ExpandBuff(false);

        for (int i = 0; i < 5005; i++) {
            char c = charStream.readChar();
            assertEquals('\u0000', c);
        }

        for (int i = 0; i < 5000; i++) {
            char c = charStream.readChar();
            assertFalse(c == '\u0000');
        }
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        char c;

        for (int i = 0; i < 10; i++) {
            c = charStream.readChar();
            assertEquals('t', c);
        }

        for (int i = 0; i < 5; i++) {
            c = charStream.readChar();
            assertEquals('e', c);
        }

        for (int i = 0; i < 5; i++) {
            c = charStream.readChar();
            assertEquals('s', c);
        }

        for (int i = 0; i < 5; i++) {
            c = charStream.readChar();
            assertEquals('t', c);
        }

        charStream.FillBuff();

        for (int i = 0; i < 10; i++) {
            c = charStream.readChar();
            assertEquals('t', c);
        }

        for (int i = 0; i < 5; i++) {
            c = charStream.readChar();
            assertEquals('e', c);
        }

        for (int i = 0; i < 5; i++) {
            c = charStream.readChar();
            assertEquals('s', c);
        }

        for (int i = 0; i < 5; i++) {
            c = charStream.readChar();
            assertEquals('t', c);
        }
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);

        for (int i = 0; i < 10; i++) {
            charStream.readChar();
        }

        charStream.inputStream.close();

        charStream.readChar();
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\tcolumn1\nline2\tcolumn2".getBytes()), 1, 1);

        char c = charStream.readChar();
        assertEquals('\r', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('\n', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('t', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('e', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('s', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(5, charStream.getColumn());

        charStream.backup(3);

        assertEquals(1, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('s', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(5, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('e', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        charStream.backup(2);

        assertEquals(1, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('t', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        charStream.backup(1);

        assertEquals(1, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('\n', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        charStream.backup(1);

        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('\r', c);
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\tcolumn1\nline2\tcolumn2".getBytes()), 1, 1);

        String image = SimpleCharStream.GetImage();
        assertEquals("test\r\nline1	column1\nline2	column2", image);
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\tcolumn1\nline2\tcolumn2".getBytes()), 1, 1);

        char[] suffix = SimpleCharStream.GetSuffix(5);
        assertNull(suffix);

        suffix = SimpleCharStream.GetSuffix(10);
        assertEquals("line2	column2\r\n", new String(suffix));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\tcolumn1\nline2\tcolumn2".getBytes()), 1, 1);

        SimpleCharStream.Done();
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\tcolumn1\nline2\tcolumn2".getBytes()), 1, 1);

        charStream.adjustBeginLineColumn(2, 2);

        assertEquals(2, charStream.getBeginLine());
        assertEquals(2, charStream.getBeginColumn());
        assertEquals(2, charStream.getLine());
        assertEquals(2, charStream.getColumn());
    }

    @Test
    public void testStaticVariables() {
        assertEquals(true, SimpleCharStream.staticFlag);
        assertEquals(0, SimpleCharStream.available);
        assertEquals(0, SimpleCharStream.bufpos);
        assertEquals(0, SimpleCharStream.tokenBegin);
        assertNull(SimpleCharStream.inputStream);
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }
}