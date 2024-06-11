package JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        SimpleCharStream scs = new SimpleCharStream(mock(InputStream.class), 1, 1, 4096);
        scs.ExpandBuff(false);
        assertEquals(8192, scs.bufsize);
        scs.ExpandBuff(true);
        assertEquals(16384, scs.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(InputStream.class), 1, 1, 4096);
        when(scs.inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenReturn(10);

        scs.FillBuff();
        assertEquals(10, scs.maxNextCharInd);

        scs.FillBuff();
        assertEquals(4096, scs.available);

        when(scs.inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenThrow(new IOException());

        scs.FillBuff();
        assertNull(scs.inputStream);
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(InputStream.class), 1, 1, 4096);
        when(scs.inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenReturn(1);

        char c = scs.BeginToken();
        assertEquals(1, scs.tokenBegin);
        assertEquals(c, scs.buffer[scs.tokenBegin]);
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 4096);

        scs.UpdateLineColumn('r');
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);
        assertEquals(false, scs.prevCharIsCR);
        assertEquals(false, scs.prevCharIsLF);

        scs.UpdateLineColumn('\n');
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);
        assertEquals(true, scs.prevCharIsLF);
        assertEquals(false, scs.prevCharIsCR);

        scs.UpdateLineColumn('\t');
        assertEquals(1, scs.line);
        assertEquals(11, scs.column);
        assertEquals(false, scs.prevCharIsCR);
        assertEquals(false, scs.prevCharIsLF);

        scs.UpdateLineColumn('r');
        assertEquals(1, scs.line);
        assertEquals(11, scs.column);
        assertEquals(true, scs.prevCharIsCR);
        assertEquals(false, scs.prevCharIsLF);

        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.line);
        assertEquals(1, scs.column);
        assertEquals(false, scs.prevCharIsCR);
        assertEquals(false, scs.prevCharIsLF);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(InputStream.class), 1, 1, 4096);

        when(scs.inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenReturn(1);

        char c = scs.readChar();
        assertEquals(0, scs.inBuf);
        assertEquals(1, scs.bufpos);
        assertEquals(1, scs.tokenBegin);
        assertEquals(c, scs.buffer[scs.tokenBegin]);

        c = scs.readChar();
        assertEquals(1, scs.inBuf);
        assertEquals(2, scs.bufpos);
        assertEquals(1, scs.tokenBegin);
        assertEquals(c, scs.buffer[scs.tokenBegin + 1]);

        when(scs.inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenThrow(new IOException());

        c = scs.readChar();
        assertNull(scs.inputStream);
    }

    @Test
    public void testGetColumn() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 4096);

        scs.UpdateLineColumn('r');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('\t');
        scs.UpdateLineColumn('a');

        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.bufcolumn[scs.bufpos]);
        assertEquals(1, scs.bufcolumn[scs.tokenBegin]);
    }

    @Test
    public void testGetLine() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 4096);

        scs.UpdateLineColumn('r');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('a');

        assertEquals(2, scs.getLine());
        assertEquals(2, scs.bufline[scs.bufpos]);
        assertEquals(2, scs.bufline[scs.tokenBegin]);
    }

    @Test
    public void testGetEndColumn() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 4096);

        scs.UpdateLineColumn('r');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('\t');
        scs.UpdateLineColumn('a');

        assertEquals(12, scs.getEndColumn());
        assertEquals(12, scs.bufcolumn[scs.bufpos + 1]);
    }

    @Test
    public void testGetEndLine() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 4096);

        scs.UpdateLineColumn('r');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('a');

        assertEquals(3, scs.getEndLine());
        assertEquals(3, scs.bufline[scs.tokenBegin + 1]);
    }

    @Test
    public void testGetBeginColumn() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 4096);

        scs.UpdateLineColumn('r');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('\t');
        scs.UpdateLineColumn('a');

        assertEquals(1, scs.getBeginColumn());
        assertEquals(1, scs.bufcolumn[scs.tokenBegin]);
    }

    @Test
    public void testGetBeginLine() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 4096);

        scs.UpdateLineColumn('r');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('a');

        assertEquals(1, scs.getBeginLine());
        assertEquals(1, scs.bufline[scs.tokenBegin]);
    }

    @Test
    public void testBackup() {
        SimpleCharStream scs = new SimpleCharStream(mock(InputStream.class), 1, 1, 4096);

        scs.UpdateLineColumn('r');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('\t');
        scs.UpdateLineColumn('a');
        scs.backup(2);

        assertEquals(11, scs.column);
        assertEquals(1, scs.line);
        assertEquals(13, scs.bufpos);
        assertEquals(1, scs.tokenBegin);
    }

    @Test
    public void testGetImage() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 4096);

        scs.UpdateLineColumn('r');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('\t');
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');

        String image = SimpleCharStream.GetImage();
        assertEquals("r\n\t\tab", image);
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 4096);

        scs.UpdateLineColumn('r');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('\t');
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');

        char[] suffix = SimpleCharStream.GetSuffix(3);
        assertArrayEquals(new char[] {'b', 'c'}, suffix);
    }

    @Test
    public void testDone() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 4096);
        SimpleCharStream.Done();
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 4096);

        scs.UpdateLineColumn('r');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('\t');
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        SimpleCharStream.adjustBeginLineColumn(2, 2);

        assertEquals(3, scs.line);
        assertEquals(1, scs.column);
        assertEquals(16, scs.bufpos);
        assertEquals(1, scs.tokenBegin);
    }
}