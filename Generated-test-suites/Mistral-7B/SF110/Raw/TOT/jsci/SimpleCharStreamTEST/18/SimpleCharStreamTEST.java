package JSci.maths.symbolic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SimpleCharStreamTest {

    private SimpleCharStream simpleCharStream;
    private Reader readerMock;

    @Before
    public void setUp() {
        readerMock = Mockito.mock(Reader.class);
        simpleCharStream = new SimpleCharStream(readerMock);
    }

    @Test
    public void testExpandBuff() throws IOException {
        simpleCharStream.ExpandBuff(false);
        assertEquals(4096, simpleCharStream.bufsize);

        simpleCharStream.ExpandBuff(true);
        assertEquals(6144, simpleCharStream.bufsize);

        simpleCharStream.ExpandBuff(false);
        assertEquals(8192, simpleCharStream.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        Mockito.when(readerMock.read(Mockito.anyCharArray(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(1);

        simpleCharStream.FillBuff();
        assertEquals(1, simpleCharStream.maxNextCharInd);

        simpleCharStream.FillBuff();
        assertEquals(2, simpleCharStream.maxNextCharInd);

        simpleCharStream.FillBuff();
        assertEquals(3, simpleCharStream.maxNextCharInd);

        Mockito.when(readerMock.read(Mockito.anyCharArray(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(-1);

        simpleCharStream.FillBuff();
        assertEquals(3, simpleCharStream.available);
        assertEquals(3, simpleCharStream.bufpos);
    }

    @Test(expected = IOException.class)
    public void testBeginTokenWithIOException() throws IOException {
        simpleCharStream.tokenBegin = -1;
        Mockito.when(readerMock.read(Mockito.anyCharArray(), Mockito.anyInt(), Mockito.anyInt())).thenThrow(new IOException());
        simpleCharStream.BeginToken();
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        simpleCharStream.UpdateLineColumn('a');
        assertEquals(1, simpleCharStream.column);

        simpleCharStream.UpdateLineColumn('\r');
        assertEquals(1, simpleCharStream.line);
        assertEquals(1, simpleCharStream.column);

        simpleCharStream.UpdateLineColumn('\n');
        assertEquals(2, simpleCharStream.line);
        assertEquals(1, simpleCharStream.column);

        simpleCharStream.UpdateLineColumn('\t');
        assertEquals(2, simpleCharStream.line);
        assertEquals(9, simpleCharStream.column);

        simpleCharStream.UpdateLineColumn('\r');
        assertEquals(3, simpleCharStream.line);
        assertEquals(1, simpleCharStream.column);
    }

    @Test
    public void testReadChar() throws IOException {
        Mockito.when(readerMock.read(Mockito.anyCharArray(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn('a').thenReturn('b').thenReturn('c').thenReturn(-1);

        ArgumentCaptor<char[]> charArrayArgumentCaptor = ArgumentCaptor.forClass(char[].class);
        Mockito.verify(readerMock).read(charArrayArgumentCaptor.capture(), Mockito.eq(0), Mockito.eq(1));
        assertEquals('a', charArrayArgumentCaptor.getValue()[0]);

        simpleCharStream.FillBuff();
        assertEquals('a', simpleCharStream.readChar());
        assertEquals(1, simpleCharStream.bufpos);

        simpleCharStream.UpdateLineColumn('a');
        assertEquals(1, simpleCharStream.line);
        assertEquals(1, simpleCharStream.column);

        simpleCharStream.readChar();
        assertEquals('b', simpleCharStream.buffer[1]);
        assertEquals(2, simpleCharStream.bufpos);
        assertEquals(1, simpleCharStream.line);
        assertEquals(2, simpleCharStream.column);

        simpleCharStream.readChar();
        assertEquals('c', simpleCharStream.buffer[2]);
        assertEquals(3, simpleCharStream.bufpos);
        assertEquals(1, simpleCharStream.line);
        assertEquals(3, simpleCharStream.column);

        simpleCharStream.readChar();
        assertEquals(-1, simpleCharStream.bufpos);
        assertEquals(-1, simpleCharStream.available);
        assertEquals(1, simpleCharStream.line);
        assertEquals(3, simpleCharStream.column);
    }

    @Test
    public void testGetColumn() throws IOException {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');

        assertEquals(1, simpleCharStream.getColumn());
        assertEquals(1, simpleCharStream.getBeginColumn());

        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');

        assertEquals(2, simpleCharStream.getColumn());
        assertEquals(9, simpleCharStream.getEndColumn());
    }

    @Test
    public void testGetLine() throws IOException {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');

        assertEquals(1, simpleCharStream.getLine());
        assertEquals(1, simpleCharStream.getBeginLine());

        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');

        assertEquals(2, simpleCharStream.getLine());
        assertEquals(2, simpleCharStream.getBeginLine());
    }

    @Test
    public void testGetEndColumn() throws IOException {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');

        assertEquals(1, simpleCharStream.getEndColumn());

        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');

        assertEquals(9, simpleCharStream.getEndColumn());
    }

    @Test
    public void testGetEndLine() throws IOException {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');

        assertEquals(1, simpleCharStream.getEndLine());

        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');

        assertEquals(2, simpleCharStream.getEndLine());
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');

        assertEquals(1, simpleCharStream.getBeginColumn());

        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');

        assertEquals(1, simpleCharStream.getBeginColumn());
    }

    @Test
    public void testGetBeginLine() throws IOException {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');

        assertEquals(1, simpleCharStream.getBeginLine());

        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');

        assertEquals(1, simpleCharStream.getBeginLine());
    }

    @Test
    public void testBackup() throws IOException {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');

        simpleCharStream.backup(1);
        assertEquals(2, simpleCharStream.bufpos);
        assertEquals(2, simpleCharStream.available);
        assertEquals(2, simpleCharStream.line);
        assertEquals(2, simpleCharStream.column);

        simpleCharStream.backup(1);
        assertEquals(3, simpleCharStream.bufpos);
        assertEquals(3, simpleCharStream.available);
        assertEquals(2, simpleCharStream.line);
        assertEquals(3, simpleCharStream.column);

        simpleCharStream.backup(1);
        assertEquals(4, simpleCharStream.bufpos);
        assertEquals(4, simpleCharStream.available);
        assertEquals(2, simpleCharStream.line);
        assertEquals(4, simpleCharStream.column);

        simpleCharStream.backup(1);
        assertEquals(5, simpleCharStream.bufpos);
        assertEquals(5, simpleCharStream.available);
        assertEquals(2, simpleCharStream.line);
        assertEquals(5, simpleCharStream.column);

        simpleCharStream.backup(5);
        assertEquals(0, simpleCharStream.bufpos);
        assertEquals(5, simpleCharStream.available);
        assertEquals(1, simpleCharStream.line);
        assertEquals(1, simpleCharStream.column);

        simpleCharStream.backup(10);
        assertEquals(-5, simpleCharStream.bufpos);
        assertEquals(5, simpleCharStream.available);
        assertEquals(1, simpleCharStream.line);
        assertEquals(1, simpleCharStream.column);
    }

    @Test
    public void testGetImage() throws IOException {
        String expected = "ab\rc\nde";
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');
        simpleCharStream.UpdateLineColumn('\r');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');

        assertEquals(expected, SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() throws IOException {
        String expected = "de";
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');
        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');

        assertEquals(expected, SimpleCharStream.GetSuffix(2));
    }

    @Test
    public void testDone() throws IOException {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');

        SimpleCharStream.Done();

        assertEquals(false, SimpleCharStream.staticFlag);
        assertFalse(simpleCharStream.buffer == null);
        assertFalse(simpleCharStream.bufline == null);
        assertFalse(simpleCharStream.bufcolumn == null);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');
        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');
        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('f');
        simpleCharStream.UpdateLineColumn('g');
        simpleCharStream.UpdateLineColumn('h');

        SimpleCharStream.adjustBeginLineColumn(3, 5);

        assertEquals(3, simpleCharStream.line);
        assertEquals(6, simpleCharStream.column);
    }
}