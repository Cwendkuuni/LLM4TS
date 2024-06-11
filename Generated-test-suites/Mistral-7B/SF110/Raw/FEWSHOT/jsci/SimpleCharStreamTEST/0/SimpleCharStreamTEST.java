package JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

public class SimpleCharStreamTest {

    private SimpleCharStream charStream;
    private Reader reader;

    @Before
    public void setUp() {
        reader = mock(Reader.class);
        charStream = new SimpleCharStream(reader);
    }

    @Test
    public void testExpandBuff() throws IOException {
        when(reader.read(charStream.buffer, 0, charStream.bufsize)).thenReturn(charStream.bufsize);

        charStream.ExpandBuff(false);

        assertEquals(65536, charStream.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        when(reader.read(charStream.buffer, 0, charStream.bufsize)).thenReturn(charStream.bufsize);

        charStream.FillBuff();

        assertEquals(charStream.buffer[0], SimpleCharStream.readChar());
        assertEquals(charStream.buffer[1], SimpleCharStream.readChar());
        assertEquals(charStream.buffer[2], SimpleCharStream.readChar());
        assertEquals(charStream.buffer[3], SimpleCharStream.readChar());
        assertEquals(charStream.buffer[4], SimpleCharStream.readChar());
    }

    @Test
    public void testBackup() throws IOException {
        charStream.FillBuff();
        charStream.backup(2);
        assertEquals(SimpleCharStream.readChar(), charStream.buffer[charStream.bufpos]);
        assertEquals(SimpleCharStream.readChar(), charStream.buffer[charStream.bufpos - 1]);
        assertEquals(SimpleCharStream.readChar(), charStream.buffer[charStream.bufpos - 2]);
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = SimpleCharStream.BeginToken();
        assertEquals(c, SimpleCharStream.readChar());
    }

    @Test
    public void testReadChar() throws IOException {
        when(reader.read(charStream.buffer, 0, charStream.bufsize)).thenReturn(charStream.bufsize);

        char c = SimpleCharStream.readChar();
        assertEquals(c, charStream.buffer[SimpleCharStream.bufpos]);
        assertEquals(++SimpleCharStream.bufpos, SimpleCharStream.maxNextCharInd);

        c = SimpleCharStream.readChar();
        assertEquals(c, charStream.buffer[SimpleCharStream.bufpos]);
        assertEquals(++SimpleCharStream.bufpos, SimpleCharStream.maxNextCharInd);

        c = SimpleCharStream.readChar();
        assertNull(c);
    }

    @Test
    public void testGetColumn() {
        charStream.UpdateLineColumn('A');
        charStream.UpdateLineColumn('B');
        charStream.UpdateLineColumn('C');

        assertEquals(1, SimpleCharStream.getColumn());
        assertEquals(2, SimpleCharStream.getColumn());
        assertEquals(3, SimpleCharStream.getColumn());

        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('\t');
        charStream.UpdateLineColumn('D');

        assertEquals(1, SimpleCharStream.getColumn());
        assertEquals(9, SimpleCharStream.getColumn());
        assertEquals(10, SimpleCharStream.getColumn());
    }

    @Test
    public void testGetLine() {
        charStream.UpdateLineColumn('A');
        charStream.UpdateLineColumn('B');
        charStream.UpdateLineColumn('C');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('D');

        assertEquals(1, SimpleCharStream.getLine());
        assertEquals(2, SimpleCharStream.getLine());

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('E');

        assertEquals(2, SimpleCharStream.getLine());
        assertEquals(3, SimpleCharStream.getLine());
    }

    @Test
    public void testGetEndColumn() {
        charStream.UpdateLineColumn('A');
        charStream.UpdateLineColumn('B');
        charStream.UpdateLineColumn('C');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('D');

        assertEquals(3, SimpleCharStream.getEndColumn());
        assertEquals(4, SimpleCharStream.getEndColumn());

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('E');

        assertEquals(3, SimpleCharStream.getEndColumn());
        assertEquals(5, SimpleCharStream.getEndColumn());
    }

    @Test
    public void testGetEndLine() {
        charStream.UpdateLineColumn('A');
        charStream.UpdateLineColumn('B');
        charStream.UpdateLineColumn('C');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('D');

        assertEquals(2, SimpleCharStream.getEndLine());
        assertEquals(3, SimpleCharStream.getEndLine());

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('E');

        assertEquals(3, SimpleCharStream.getEndLine());
        assertEquals(4, SimpleCharStream.getEndLine());
    }

    @Test
    public void testGetBeginColumn() {
        charStream.UpdateLineColumn('A');
        charStream.UpdateLineColumn('B');
        charStream.UpdateLineColumn('C');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('D');

        assertEquals(1, SimpleCharStream.getBeginColumn());
        assertEquals(1, SimpleCharStream.getBeginColumn());

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('E');

        assertEquals(2, SimpleCharStream.getBeginColumn());
        assertEquals(2, SimpleCharStream.getBeginColumn());
    }

    @Test
    public void testGetBeginLine() {
        charStream.UpdateLineColumn('A');
        charStream.UpdateLineColumn('B');
        charStream.UpdateLineColumn('C');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('D');

        assertEquals(1, SimpleCharStream.getBeginLine());
        assertEquals(1, SimpleCharStream.getBeginLine());

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('E');

        assertEquals(2, SimpleCharStream.getBeginLine());
        assertEquals(2, SimpleCharStream.getBeginLine());
    }

    @Test
    public void testGetImage() {
        charStream.UpdateLineColumn('A');
        charStream.UpdateLineColumn('B');
        charStream.UpdateLineColumn('C');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('D');
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('E');

        String image = SimpleCharStream.GetImage();

        assertArrayEquals("ABC\nD\r\nE".toCharArray(), image.toCharArray());
    }

    @Test
    public void testGetSuffix() {
        charStream.UpdateLineColumn('A');
        charStream.UpdateLineColumn('B');
        charStream.UpdateLineColumn('C');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('D');
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('E');
        charStream.UpdateLineColumn('F');
        charStream.UpdateLineColumn('G');

        char[] suffix = SimpleCharStream.GetSuffix(6);

        assertArrayEquals("EFG".toCharArray(), suffix);
    }

    @Test
    public void testDone() throws IOException {
        charStream.FillBuff();

        SimpleCharStream.Done();

        assertEquals(null, SimpleCharStream.buffer);
        assertEquals(null, SimpleCharStream.bufline);
        assertEquals(null, SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        charStream.UpdateLineColumn('A');
        charStream.UpdateLineColumn('B');
        charStream.UpdateLineColumn('C');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('D');
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('E');
        charStream.UpdateLineColumn('F');
        charStream.UpdateLineColumn('G');

        SimpleCharStream.adjustBeginLineColumn(3, 10);

        assertEquals(3, SimpleCharStream.getBeginLine());
        assertEquals(13, SimpleCharStream.getBeginColumn());
    }
}