package JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 4096);
        assertEquals('\0', scs.BeginToken());

        scs.FillBuff();
        assertEquals('t', scs.BeginToken());

        scs.ExpandBuff(false);
        assertEquals('t', scs.BeginToken());

        scs.inputStream.read(); // consume a character
        assertEquals('e', scs.BeginToken());

        scs.inputStream.close();
        assertNull(scs.BeginToken());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\nline1\nline2"), 1, 1, 4096);

        for (int i = 0; i < 5; ++i) {
            scs.readChar();
        }
        assertEquals(1, scs.getLine());
        assertEquals(6, scs.getColumn());

        scs.readChar();
        assertEquals('\n', scs.getLastChar());
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.readChar();
        assertEquals('l', scs.getLastChar());
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.readChar();
        assertEquals('i', scs.getLastChar());
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.readChar();
        assertEquals('n', scs.getLastChar());
        assertEquals(2, scs.getLine());
        assertEquals(4, scs.getColumn());

        scs.readChar();
        assertEquals('e', scs.getLastChar());
        assertEquals(2, scs.getLine());
        assertEquals(5, scs.getColumn());
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 4096);

        for (int i = 0; i < 5; ++i) {
            scs.readChar();
        }

        scs.FillBuff();

        assertEquals('t', scs.BeginToken());
        assertEquals('e', scs.BeginToken());
        assertEquals('s', scs.BeginToken());
        assertEquals('\n', scs.BeginToken());
        assertEquals('t', scs.BeginToken());
    }

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 2048);

        for (int i = 0; i < 5; ++i) {
            scs.readChar();
        }
        scs.ExpandBuff(false);

        assertEquals('t', scs.BeginToken());
        assertEquals('e', scs.BeginToken());
        assertEquals('s', scs.BeginToken());
        assertEquals('\n', scs.BeginToken());
        assertEquals('t', scs.BeginToken());

        scs.FillBuff();

        assertEquals('t', scs.BeginToken());
        assertEquals('e', scs.BeginToken());
        assertEquals('s', scs.BeginToken());
        assertEquals('\n', scs.BeginToken());
        assertEquals('t', scs.BeginToken());
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\nline1\nline2"), 1, 1, 4096);

        for (int i = 0; i < 5; ++i) {
            scs.readChar();
        }
        assertEquals(2, scs.getLine());
        assertEquals(6, scs.getColumn());

        scs.readChar();
        assertEquals('\n', scs.getLastChar());
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.readChar();
        assertEquals('l', scs.getLastChar());
        assertEquals(3, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.readChar();
        assertEquals('i', scs.getLastChar());
        assertEquals(3, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.readChar();
        assertEquals('n', scs.getLastChar());
        assertEquals(3, scs.getLine());
        assertEquals(4, scs.getColumn());

        scs.readChar();
        assertEquals('e', scs.getLastChar());
        assertEquals(3, scs.getLine());
        assertEquals(5, scs.getColumn());
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 4096);

        for (int i = 0; i < 5; ++i) {
            scs.readChar();
        }

        scs.ReInit(new StringReader("test2"), 1, 1);

        assertEquals('t', scs.BeginToken());
        assertEquals('e', scs.BeginToken());
        assertEquals('s', scs.BeginToken());
        assertEquals('t', scs.BeginToken());
        assertEquals('2', scs.BeginToken());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 4096);

        for (int i = 0; i < 5; ++i) {
            scs.readChar();
        }

        String image = SimpleCharStream.GetImage();
        assertEquals("test", image);

        scs.inputStream.close();
        image = SimpleCharStream.GetImage();
        assertNull(image);
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("testprefix"), 1, 1, 4096);

        for (int i = 0; i < 5; ++i) {
            scs.readChar();
        }

        char[] suffix = SimpleCharStream.GetSuffix(5);
        assertEquals("prefix", new String(suffix));

        scs.inputStream.close();
        suffix = SimpleCharStream.GetSuffix(5);
        assertNull(suffix);
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1, 4096);

        for (int i = 0; i < 5; ++i) {
            scs.readChar();
        }

        SimpleCharStream.Done();

        assertEquals(-1, scs.bufpos);
        assertNull(scs.buffer);
        assertNull(scs.bufline);
        assertNull(scs.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\nline1\nline2\nline3\nline4\nline5"), 1, 1, 4096);

        scs.adjustBeginLineColumn(1, 5);

        assertEquals(2, scs.getLine());
        assertEquals(6, scs.getColumn());

        scs.readChar();
        assertEquals('\n', scs.getLastChar());
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.readChar();
        assertEquals('l', scs.getLastChar());
        assertEquals(3, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.readChar();
        assertEquals('i', scs.getLastChar());
        assertEquals(3, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.readChar();
        assertEquals('n', scs.getLastChar());
        assertEquals(3, scs.getLine());
        assertEquals(4, scs.getColumn());

        scs.readChar();
        assertEquals('e', scs.getLastChar());
        assertEquals(3, scs.getLine());
        assertEquals(5, scs.getColumn());

        scs.readChar();
        assertEquals(' ', scs.getLastChar());
        assertEquals(4, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.readChar();
        assertEquals('l', scs.getLastChar());
        assertEquals(4, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.readChar();
        assertEquals('i', scs.getLastChar());
        assertEquals(4, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.readChar();
        assertEquals('n', scs.getLastChar());
        assertEquals(4, scs.getLine());
        assertEquals(4, scs.getColumn());

        scs.readChar();
        assertEquals('e', scs.getLastChar());
        assertEquals(4, scs.getLine());
        assertEquals(5, scs.getColumn());

        scs.readChar();
        assertEquals('\n', scs.getLastChar());
        assertEquals(5, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.readChar();
        assertEquals('t', scs.getLastChar());
        assertEquals(5, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.readChar();
        assertEquals('e', scs.getLastChar());
        assertEquals(5, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.readChar();
        assertEquals('s', scs.getLastChar());
        assertEquals(5, scs.getLine());
        assertEquals(4, scs.getColumn());

        scs.readChar();
        assertEquals('\n', scs.getLastChar());
        assertEquals(6, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.readChar();
        assertEquals('l', scs.getLastChar());
        assertEquals(6, scs.getLine());
        assertEquals(2, scs.getColumn());
    }
}