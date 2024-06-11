package JSci.maths.symbolic;

import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleCharStreamTest {
    @Test
    public void testExpandBuff() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1, 256);
        for (int i = 0; i < 256; i++) {
            scs.readChar();
        }

        scs.ExpandBuff(false);

        for (int i = 0; i < 512; i++) {
            char c = scs.readChar();
            assertEquals(-1, c);
        }

        scs.ExpandBuff(true);

        for (int i = 0; i < 512; i++) {
            char c = scs.readChar();
            assertNotEquals(-1, c);
        }
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1, 256);

        for (int i = 0; i < 256; i++) {
            scs.readChar();
        }

        scs.FillBuff();

        for (int i = 0; i < 256; i++) {
            char c = scs.readChar();
            assertNotEquals(-1, c);
        }
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("foo\nbar"), 1, 1, 256);

        char c = scs.BeginToken();
        assertEquals('f', c);

        int line = scs.getLine();
        int column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(1, column);

        c = scs.readChar();
        assertEquals('\n', c);

        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(2, column);

        c = scs.BeginToken();
        assertEquals('b', c);

        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("foo\r\nbar"), 1, 1, 256);

        for (int i = 0; i < 3; i++) {
            char c = scs.readChar();
            UpdateLineColumn(c);
        }

        int line = scs.getLine();
        int column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);

        c = scs.readChar();
        assertEquals('\n', c);

        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(4, column);

        UpdateLineColumn('\r');

        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(1, line);
        assertEquals(5, column);

        for (int i = 0; i < 2; i++) {
            c = scs.readChar();
            UpdateLineColumn(c);
        }

        line = scs.getLine();
        column = scs.getColumn();
        assertEquals(2, line);
        assertEquals(3, column);
    }

    @Test(expected = IOException.class)
    public void testFillBuffIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1, 256);

        for (int i = 0; i < 256; i++) {
            scs.readChar();
        }

        scs.FillBuff();

        scs.inputStream.close();

        scs.FillBuff();
    }

    @Test
    public void testGetImage() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("foo\nbar"), 1, 1, 256);

        String image = SimpleCharStream.GetImage();
        assertEquals("foo\nbar", image);
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("foo\nbar"), 1, 1, 256);

        char[] suffix = SimpleCharStream.GetSuffix(5);
        assertArrayEquals(new char[]{'r', 'a', 'r', ' ', 'b'}, suffix);
    }
}