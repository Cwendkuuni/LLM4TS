package JSci.maths.symbolic;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1, 1024);
        scs.ExpandBuff(false);
        assertEquals(2048, scs.bufsize);
        assertEquals(0, scs.tokenBegin);
        assertEquals(0, scs.bufpos);
        assertEquals(2048, scs.available);

        scs.ExpandBuff(true);
        assertEquals(4096, scs.bufsize);
        assertEquals(0, scs.tokenBegin);
        assertEquals(-2048, scs.bufpos);
        assertEquals(4096, scs.available);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdefg"), 1, 1, 1024);
        assertEquals(0, scs.bufpos);
        assertEquals(1, scs.available);
        scs.FillBuff();
        assertEquals('a', scs.readChar());
        assertEquals(1, scs.bufpos);
        assertEquals(1, scs.available);
        scs.FillBuff();
        assertEquals('b', scs.readChar());
        assertEquals(2, scs.bufpos);
        assertEquals(1, scs.available);
        scs.FillBuff();
        assertEquals('c', scs.readChar());
        assertEquals(3, scs.bufpos);
        assertEquals(1, scs.available);
        scs.FillBuff();
        assertEquals('d', scs.readChar());
        assertEquals(4, scs.bufpos);
        assertEquals(1, scs.available);
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abc"), 1, 1, 1024);
        assertEquals(-1, scs.tokenBegin);
        assertEquals('a', scs.BeginToken());
        assertEquals(0, scs.bufpos);
        assertEquals(1, scs.available);
        assertEquals('a', scs.readChar());
        assertEquals(1, scs.bufpos);
        assertEquals(1, scs.available);
        assertEquals('b', scs.BeginToken());
        assertEquals(2, scs.bufpos);
        assertEquals(1, scs.available);
        assertEquals('b', scs.readChar());
        assertEquals(3, scs.bufpos);
        assertEquals(1, scs.available);
        assertEquals('c', scs.BeginToken());
        assertEquals(4, scs.bufpos);
        assertEquals(1, scs.available);
        assertEquals('c', scs.readChar());
        assertEquals(5, scs.bufpos);
        assertEquals(1, scs.available);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("ab\ncdefg\n"), 1, 1, 1024);
        assertEquals(0, scs.column);
        assertEquals(1, scs.line);
        scs.readChar();
        scs.UpdateLineColumn((char) 0xA); // LF
        assertEquals(1, scs.column);
        assertEquals(1, scs.line);
        scs.readChar();
        scs.UpdateLineColumn('a');
        assertEquals(2, scs.column);
        assertEquals(1, scs.line);
        scs.readChar();
        scs.UpdateLineColumn((char) 0xD); // CR
        assertEquals(2, scs.column);
        assertEquals(2, scs.line);
        scs.readChar();
        scs.UpdateLineColumn('e');
        assertEquals(3, scs.column);
        assertEquals(2, scs.line);
        scs.readChar();
        scs.UpdateLineColumn('f');
        assertEquals(4, scs.column);
        assertEquals(2, scs.line);
        scs.readChar();
        scs.UpdateLineColumn('g');
        assertEquals(5, scs.column);
        assertEquals(2, scs.line);
        scs.readChar();
        scs.UpdateLineColumn((char) 0xA); // LF
        assertEquals(5, scs.column);
        assertEquals(3, scs.line);
        scs.readChar();
        assertEquals(6, scs.column);
        assertEquals(3, scs.line);
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("ab\ncdefg\n"), 1, 1, 1024);
        assertEquals(0, scs.getColumn());
        assertEquals(1, scs.getLine());
        scs.readChar();
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getLine());
        scs.readChar();
        scs.UpdateLineColumn((char) 0xA); // LF
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getLine());
        scs.readChar();
        assertEquals(2, scs.getColumn());
        assertEquals(1, scs.getLine());
        scs.readChar();
        scs.UpdateLineColumn('a');
        assertEquals(3, scs.getColumn());
        assertEquals(1, scs.getLine());
        scs.readChar();
        scs.UpdateLineColumn((char) 0xD); // CR
        assertEquals(3, scs.getColumn());
        assertEquals(2, scs.getLine());
        scs.readChar();
        scs.UpdateLineColumn('e');
        assertEquals(4, scs.getColumn());
        assertEquals(2, scs.getLine());
        scs.readChar();
        scs.UpdateLineColumn('f');
        assertEquals(5, scs.getColumn());
        assertEquals(2, scs.getLine());
        scs.readChar();
        scs.UpdateLineColumn('g');
        assertEquals(6, scs.getColumn());
        assertEquals(2, scs.getLine());
        scs.readChar();
        scs.UpdateLineColumn((char) 0xA); // LF
        assertEquals(6, scs.getColumn());
        assertEquals(3, scs.getLine());
        scs.readChar();
        assertEquals(7, scs.getColumn());
        assertEquals(3, scs.getLine());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abc"), 1, 1, 1024);
        scs.backup(1);
        assertEquals(9, scs.bufsize);
        assertEquals(-1, scs.bufpos);
        assertEquals(9, scs.available);
        assertEquals('b', scs.readChar());
        assertEquals(8, scs.bufsize);
        assertEquals(-1, scs.bufpos);
        assertEquals(8, scs.available);
        assertEquals('a', scs.readChar());
        assertEquals(7, scs.bufsize);
        assertEquals(-1, scs.bufpos);
        assertEquals(7, scs.available);
        assertEquals('a', scs.readChar());
        assertEquals(6, scs.bufsize);
        assertEquals(0, scs.bufpos);
        assertEquals(6, scs.available);
        assertEquals('a', scs.readChar());
        assertEquals(5, scs.bufsize);
        assertEquals(1, scs.bufpos);
        assertEquals(5, scs.available);
        assertEquals('a', scs.readChar());
        assertEquals(4, scs.bufsize);
        assertEquals(2, scs.bufpos);
        assertEquals(4, scs.available);
        assertEquals('c', scs.readChar());
        assertEquals(3, scs.bufsize);
        assertEquals(3, scs.bufpos);
        assertEquals(3, scs.available);
        assertEquals('c', scs.readChar());
        assertEquals(2, scs.bufsize);
        assertEquals(4, scs.bufpos);
        assertEquals(2, scs.available);
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);
        assertEquals("abcdef", SimpleCharStream.GetImage());

        scs.ExpandBuff(true);
        String image = new String(SimpleCharStream.buffer, 0, SimpleCharStream.bufpos + 1);
        assertEquals("abcdef", image.substring(0, 6));
        assertEquals("", image.substring(6));
        assertEquals("abcdef", SimpleCharStream.GetImage());

        scs.backup(1);
        image = new String(SimpleCharStream.buffer, 0, SimpleCharStream.bufpos + 1);
        assertEquals("abcde", image.substring(0, 5));
        assertEquals("f", image.substring(5));
        assertEquals("abcde", SimpleCharStream.GetImage());
        assertEquals("abcde", SimpleCharStream.GetSuffix(1));

        scs.backup(1);
        image = new String(SimpleCharStream.buffer, 0, SimpleCharStream.bufpos + 1);
        assertEquals("abcd", image.substring(0, 4));
        assertEquals("e", image.substring(4));
        assertEquals("abcd", SimpleCharStream.GetImage());
        assertEquals("abcd", SimpleCharStream.GetSuffix(3));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);
        assertEquals("abcdef", SimpleCharStream.GetImage());
        SimpleCharStream.Done();
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }
}