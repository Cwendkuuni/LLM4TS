package JSci.maths.symbolic;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1, 1024);
        scs.ExpandBuff(false);
        assertEquals(2048, scs.bufsize);
        assertEquals(SimpleCharStream.bufsize, scs.available);
        assertEquals(0, scs.tokenBegin);
        assertNull(scs.buffer);

        char[] buffer = new char[SimpleCharStream.bufsize];
        int[] bufline = new int[SimpleCharStream.bufsize];
        int[] bufcolumn = new int[SimpleCharStream.bufsize];

        System.arraycopy(scs.buffer, 0, buffer, 0, SimpleCharStream.bufsize - scs.tokenBegin);
        System.arraycopy(scs.bufline, 0, bufline, 0, SimpleCharStream.bufsize - scs.tokenBegin);
        System.arraycopy(scs.bufcolumn, 0, bufcolumn, 0, SimpleCharStream.bufsize - scs.tokenBegin);

        scs.buffer = buffer;
        scs.bufline = bufline;
        scs.bufcolumn = bufcolumn;

        scs.ExpandBuff(true);
        assertEquals(4096, scs.bufsize);
        assertEquals(SimpleCharStream.bufsize, scs.available);
        assertEquals(0, scs.tokenBegin);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);
        scs.FillBuff();
        assertEquals('a', scs.readChar());
        assertEquals('b', scs.readChar());
        assertEquals('c', scs.readChar());
        assertEquals('d', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('f', scs.readChar());
        assertEquals(-1, scs.readChar());
    }

    @Test(expected = IOException.class)
    public void testFillBuffWithIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1, 1024);
        scs.FillBuff();
        scs.inputStream.close();
        scs.readChar();
    }

    @Test
    public void testBackup() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);
        scs.backup(3);
        assertEquals('d', scs.readChar());
        assertEquals('c', scs.readChar());
        assertEquals('b', scs.readChar());
        assertEquals('a', scs.readChar());
        assertEquals(-1, scs.readChar());
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abc\ndef\r\rghi\njklm\nopqrst\n"), 1, 1, 1024);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getBeginLine());
        assertEquals(1, scs.getBeginColumn());
        assertEquals(1, scs.getEndLine());
        assertEquals(4, scs.getEndColumn());

        scs.UpdateLineColumn('a');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        scs.UpdateLineColumn('\n');
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('d');
        scs.UpdateLineColumn('e');
        scs.UpdateLineColumn('f');
        scs.UpdateLineColumn('\r');
        scs.UpdateLineColumn('\r');
        scs.UpdateLineColumn('g');
        scs.UpdateLineColumn('h');
        scs.UpdateLineColumn('i');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('j');
        scs.UpdateLineColumn('k');
        scs.UpdateLineColumn('l');
        scs.UpdateLineColumn('m');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('o');
        scs.UpdateLineColumn('p');
        scs.UpdateLineColumn('q');
        scs.UpdateLineColumn('r');
        scs.UpdateLineColumn('s');
        scs.UpdateLineColumn('t');
        scs.UpdateLineColumn('\n');

        assertEquals(14, scs.getLine());
        assertEquals(18, scs.getColumn());
    }
}