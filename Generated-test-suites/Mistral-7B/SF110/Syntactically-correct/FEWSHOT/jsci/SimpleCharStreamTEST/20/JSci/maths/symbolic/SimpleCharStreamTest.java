package JSci.maths.symbolic;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1, 8);
        scs.ExpandBuff(false);
        assertEquals(16, scs.bufsize);
        scs.ExpandBuff(true);
        assertEquals(3072, scs.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdefg"), 1, 1, 8);
        scs.FillBuff();
        assertEquals('a', scs.readChar());
        assertEquals('b', scs.readChar());
        assertEquals('c', scs.readChar());
        assertEquals('d', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('f', scs.readChar());
        assertEquals('g', scs.readChar());
        assertEquals(-1, scs.readChar());
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdefg"), 1, 1, 8);
        char c = scs.BeginToken();
        assertEquals(-1, c);
        c = scs.readChar();
        assertEquals('a', c);
        int beginLine = scs.getBeginLine();
        int beginColumn = scs.getBeginColumn();
        assertEquals(1, beginLine);
        assertEquals(1, beginColumn);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abc\r\ndefg\n\t\n"), 1, 1, 8);
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getLine());
        scs.UpdateLineColumn('a');
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getLine());
        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.getColumn());
        assertEquals(2, scs.getLine());
        scs.UpdateLineColumn('n');
        assertEquals(1, scs.getColumn());
        assertEquals(3, scs.getLine());
        scs.UpdateLineColumn('\t');
        assertEquals(9, scs.getColumn());
        assertEquals(3, scs.getLine());
        scs.UpdateLineColumn('\n');
        assertEquals(1, scs.getColumn());
        assertEquals(4, scs.getLine());
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abc\r\ndefg\n\t\n"), 1, 1, 8);
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getLine());
        scs.UpdateLineColumn('a');
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getLine());
        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.getColumn());
        assertEquals(2, scs.getLine());
        scs.UpdateLineColumn('n');
        assertEquals(1, scs.getColumn());
        assertEquals(3, scs.getLine());
        scs.UpdateLineColumn('\t');
        assertEquals(9, scs.getColumn());
        assertEquals(3, scs.getLine());
        scs.UpdateLineColumn('\n');
        assertEquals(1, scs.getColumn());
        assertEquals(4, scs.getLine());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abc\r\ndefg\n\t\n"), 1, 1, 8);
        scs.backup(1);
        assertEquals('a', scs.readChar());
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getLine());
        scs.backup(1);
        assertEquals('a', scs.readChar());
        assertEquals(0, scs.getColumn());
        assertEquals(1, scs.getLine());
        scs.backup(2);
        assertEquals(-1, scs.readChar());
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abc\r\ndefg\n\t\n"), 1, 1, 8);
        scs.ReInit(new StringReader("xyz"), 1, 1);
        assertEquals('x', scs.readChar());
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getLine());
        assertEquals('y', scs.readChar());
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getLine());
        assertEquals('z', scs.readChar());
        assertEquals(-1, scs.readChar());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abc\r\ndefg\n\t\n"), 1, 1, 8);
        String image = SimpleCharStream.GetImage();
        assertEquals("abc\r\ndefg\n\t\n", image);
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abc\r\ndefg\n\t\n"), 1, 1, 8);
        char[] suffix = SimpleCharStream.GetSuffix(5);
        assertEquals("defg\n\t\n", new String(suffix));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abc\r\ndefg\n\t\n"), 1, 1, 8);
        SimpleCharStream.Done();
    }

    @Test(expected = Error.class)
    public void testErrorIfSecondCallToConstructor() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1, 8);
        SimpleCharStream scs2 = new SimpleCharStream(new StringReader(""), 1, 1, 8);
    }
}