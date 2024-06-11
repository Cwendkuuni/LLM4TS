package JSci.maths.symbolic;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader(""), 1, 1, 1024);
        for (int i = 0; i < 10000; i++) {
            stream.readChar();
        }

        stream.ExpandBuff(false);
        assertEquals(1152, stream.bufsize);
        assertEquals(1152, stream.available);
        assertEquals(0, stream.tokenBegin);
        assertEquals(10000, stream.bufpos);

        stream.ExpandBuff(true);
        assertEquals(2256, stream.bufsize);
        assertEquals(2256, stream.available);
        assertEquals(10000, stream.bufpos);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader(""), 1, 1, 1024);

        for (int i = 0; i < 1000; i++) {
            stream.readChar();
        }

        stream.FillBuff();
        assertEquals(1024, stream.available);
        assertEquals(1024, stream.bufsize);
        assertEquals(1024, stream.maxNextCharInd);
        assertEquals(' ', stream.buffer[0]);
        assertEquals('\n', stream.buffer[1023]);

        stream.FillBuff();
        assertEquals(2048, stream.available);
        assertEquals(2048, stream.bufsize);
        assertEquals(2048, stream.maxNextCharInd);
        assertEquals('\0', stream.buffer[0]);
        assertEquals('\0', stream.buffer[2047]);

        stream.FillBuff();
        assertEquals(4096, stream.available);
        assertEquals(4096, stream.bufsize);
        assertEquals(4096, stream.maxNextCharInd);
        assertEquals('\0', stream.buffer[0]);
        assertEquals('\0', stream.buffer[4095]);
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);

        assertEquals('a', stream.BeginToken());
        assertEquals(1, stream.getLine());
        assertEquals(1, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(1, stream.getBeginColumn());

        assertEquals('b', stream.BeginToken());
        assertEquals(1, stream.getLine());
        assertEquals(2, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(2, stream.getBeginColumn());

        assertEquals('c', stream.BeginToken());
        assertEquals(1, stream.getLine());
        assertEquals(3, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(3, stream.getBeginColumn());

        assertEquals('d', stream.BeginToken());
        assertEquals(1, stream.getLine());
        assertEquals(4, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(4, stream.getBeginColumn());

        assertEquals('e', stream.BeginToken());
        assertEquals(1, stream.getLine());
        assertEquals(5, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(5, stream.getBeginColumn());

        assertEquals('f', stream.BeginToken());
        assertEquals(1, stream.getLine());
        assertEquals(6, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(6, stream.getBeginColumn());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader(""), 1, 1, 1024);

        stream.UpdateLineColumn('a');
        assertEquals(1, stream.line);
        assertEquals(1, stream.column);

        stream.UpdateLineColumn('\r');
        assertEquals(1, stream.line);
        assertEquals(0, stream.column);

        stream.UpdateLineColumn('\n');
        assertEquals(2, stream.line);
        assertEquals(1, stream.column);

        stream.UpdateLineColumn('\t');
        assertEquals(2, stream.line);
        assertEquals(3, stream.column);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);

        assertEquals('a', stream.readChar());
        assertEquals(1, stream.getLine());
        assertEquals(1, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(1, stream.getBeginColumn());

        assertEquals('b', stream.readChar());
        assertEquals(1, stream.getLine());
        assertEquals(2, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(2, stream.getBeginColumn());

        assertEquals('c', stream.readChar());
        assertEquals(1, stream.getLine());
        assertEquals(3, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(3, stream.getBeginColumn());

        assertEquals('d', stream.readChar());
        assertEquals(1, stream.getLine());
        assertEquals(4, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(4, stream.getBeginColumn());

        assertEquals('e', stream.readChar());
        assertEquals(1, stream.getLine());
        assertEquals(5, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(5, stream.getBeginColumn());

        assertEquals('f', stream.readChar());
        assertEquals(1, stream.getLine());
        assertEquals(6, stream.getColumn());
        assertEquals(1, stream.getBeginLine());
        assertEquals(6, stream.getBeginColumn());

        assertEquals(-1, stream.readChar());
    }

    @Test
    public void testGetColumn() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);

        assertEquals(1, stream.getColumn());

        stream.UpdateLineColumn('a');
        assertEquals(1, stream.getLine());
        assertEquals(1, stream.column);

        stream.UpdateLineColumn('\r');
        assertEquals(1, stream.getLine());
        assertEquals(0, stream.column);

        stream.UpdateLineColumn('\n');
        assertEquals(2, stream.getLine());
        assertEquals(1, stream.column);

        stream.UpdateLineColumn('\t');
        assertEquals(2, stream.getLine());
        assertEquals(3, stream.column);

        stream.UpdateLineColumn('b');
        assertEquals(2, stream.getLine());
        assertEquals(4, stream.column);
    }

    @Test
    public void testGetLine() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);

        assertEquals(1, stream.getLine());

        stream.UpdateLineColumn('a');
        assertEquals(1, stream.getLine());

        stream.UpdateLineColumn('\r');
        assertEquals(1, stream.getLine());

        stream.UpdateLineColumn('\n');
        assertEquals(2, stream.getLine());

        stream.UpdateLineColumn('\t');
        assertEquals(2, stream.getLine());

        stream.UpdateLineColumn('b');
        assertEquals(2, stream.getLine());
    }

    @Test
    public void testGetBeginLine() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);

        assertEquals(1, stream.getBeginLine());

        stream.UpdateLineColumn('a');
        assertEquals(1, stream.getBeginLine());

        stream.UpdateLineColumn('\r');
        assertEquals(1, stream.getBeginLine());

        stream.UpdateLineColumn('\n');
        assertEquals(1, stream.getBeginLine());

        stream.UpdateLineColumn('\t');
        assertEquals(1, stream.getBeginLine());

        stream.UpdateLineColumn('b');
        assertEquals(1, stream.getBeginLine());
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);

        assertEquals(1, stream.getBeginColumn());

        stream.UpdateLineColumn('a');
        assertEquals(1, stream.getBeginColumn());

        stream.UpdateLineColumn('\r');
        assertEquals(1, stream.getBeginColumn());

        stream.UpdateLineColumn('\n');
        assertEquals(1, stream.getBeginColumn());

        stream.UpdateLineColumn('\t');
        assertEquals(1, stream.getBeginColumn());

        stream.UpdateLineColumn('b');
        assertEquals(1, stream.getBeginColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);

        stream.UpdateLineColumn('a');
        stream.UpdateLineColumn('b');
        stream.UpdateLineColumn('c');

        stream.backup(1);
        assertEquals(1, stream.getLine());
        assertEquals(3, stream.getColumn());
        assertEquals(3, stream.getBeginColumn());
        assertEquals(3, stream.getBeginLine());

        stream.backup(2);
        assertEquals(1, stream.getLine());
        assertEquals(2, stream.getColumn());
        assertEquals(2, stream.getBeginColumn());
        assertEquals(1, stream.getBeginLine());

        stream.backup(3);
        assertEquals(1, stream.getLine());
        assertEquals(1, stream.getColumn());
        assertEquals(1, stream.getBeginColumn());
        assertEquals(1, stream.getBeginLine());

        stream.backup(10);
        assertEquals(1, stream.getLine());
        assertEquals(1, stream.getColumn());
        assertEquals(1, stream.getBeginColumn());
        assertEquals(1, stream.getBeginLine());
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);

        stream.UpdateLineColumn('a');
        stream.UpdateLineColumn('b');
        stream.UpdateLineColumn('c');

        stream.ReInit(new StringReader("hijklmn"), 2, 2, 1024);
        assertEquals(2, stream.getLine());
        assertEquals(1, stream.getColumn());
        assertEquals(2, stream.getBeginColumn());
        assertEquals(2, stream.getBeginLine());

        stream.ReInit(new StringReader("hijklmn"), 2, 2);
        assertEquals(2, stream.getLine());
        assertEquals(1, stream.getColumn());
        assertEquals(2, stream.getBeginColumn());
        assertEquals(2, stream.getBeginLine());

        stream.ReInit(new StringReader("hijklmn"));
        assertEquals(1, stream.getLine());
        assertEquals(1, stream.getColumn());
        assertEquals(1, stream.getBeginColumn());
        assertEquals(1, stream.getBeginLine());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);

        stream.UpdateLineColumn('a');
        stream.UpdateLineColumn('b');
        stream.UpdateLineColumn('c');

        assertEquals("ab\nc", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);

        stream.UpdateLineColumn('a');
        stream.UpdateLineColumn('b');
        stream.UpdateLineColumn('c');

        char[] suffix = SimpleCharStream.GetSuffix(5);
        assertArrayEquals(new char[]{'e', 'f', 'd', 'c', 'b'}, suffix);
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdef"), 1, 1, 1024);

        stream.UpdateLineColumn('a');
        stream.UpdateLineColumn('b');
        stream.UpdateLineColumn('c');

        SimpleCharStream.Done();
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("abcdefghijklmnopqrst\nabcdefghijkl"), 1, 1, 1024);

        stream.UpdateLineColumn('a');
        stream.UpdateLineColumn('b');
        stream.UpdateLineColumn('c');
        stream.UpdateLineColumn('d');
        stream.UpdateLineColumn('e');
        stream.UpdateLineColumn('f');
        stream.UpdateLineColumn('g');
        stream.UpdateLineColumn('h');
        stream.UpdateLineColumn('i');
        stream.UpdateLineColumn('j');
        stream.UpdateLineColumn('k');
        stream.UpdateLineColumn('l');
        stream.UpdateLineColumn('m');
        stream.UpdateLineColumn('n');
        stream.UpdateLineColumn('o');
        stream.UpdateLineColumn('p');
        stream.UpdateLineColumn('q');
        stream.UpdateLineColumn('r');
        stream.UpdateLineColumn('s');
        stream.UpdateLineColumn('t');

        SimpleCharStream.adjustBeginLineColumn(3, 1);
        assertEquals(3, stream.getBeginLine());
        assertEquals(1, stream.getBeginColumn());

        SimpleCharStream.adjustBeginLineColumn(3, 10);
        assertEquals(3, stream.getBeginLine());
        assertEquals(11, stream.getBeginColumn());

        SimpleCharStream.adjustBeginLineColumn(3, 30);
        assertEquals(3, stream.getBeginLine());
        assertEquals(31, stream.getBeginColumn());
    }
}