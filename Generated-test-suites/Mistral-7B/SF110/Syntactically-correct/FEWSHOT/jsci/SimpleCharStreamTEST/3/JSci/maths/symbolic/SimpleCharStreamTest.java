package JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.ExpandBuff(false);
        assertEquals(4096, scs.bufsize);
        assertEquals(0, scs.tokenBegin);
        assertEquals(0, scs.bufpos);
        assertEquals(4096, scs.available);

        scs.ExpandBuff(true);
        assertEquals(10240, scs.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        when(mock(InputStream.class).read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenReturn(1);

        scs.FillBuff();
        assertEquals('a', scs.buffer[scs.bufpos]);
        assertEquals(1, scs.maxNextCharInd);
        assertEquals(0, scs.available);
        assertEquals(1, scs.bufpos);
    }

    @Test(expected = IOException.class)
    public void testFillBuffIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        when(mock(InputStream.class).read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenThrow(new IOException("Test Exception"));

        scs.FillBuff();
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.line);
        assertEquals(0, scs.column);
        scs.UpdateLineColumn('\n');
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);
        scs.UpdateLineColumn('\t');
        assertEquals(1, scs.line);
        assertEquals(9, scs.column);
        scs.UpdateLineColumn(' ');
        assertEquals(1, scs.line);
        assertEquals(10, scs.column);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        when(mock(InputStream.class).read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenReturn(1);

        assertEquals('a', scs.readChar());
        assertEquals(1, scs.maxNextCharInd);
        assertEquals(0, scs.available);
        assertEquals(1, scs.bufpos);

        scs.FillBuff();
        assertEquals('\u0000', scs.readChar());
        assertEquals(0, scs.available);
        assertEquals(0, scs.bufpos);
    }

    @Test
    public void testGetColumn() {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        assertEquals(3, scs.getColumn());
    }

    @Test
    public void testGetLine() {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        assertEquals(1, scs.getLine());
    }

    @Test
    public void testGetEndColumn() {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        assertEquals(3, scs.getEndColumn());
    }

    @Test
    public void testGetEndLine() {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        assertEquals(1, scs.getEndLine());
    }

    @Test
    public void testGetBeginColumn() {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        assertEquals(0, scs.getBeginColumn());
    }

    @Test
    public void testGetBeginLine() {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        assertEquals(1, scs.getBeginLine());
    }

    @Test
    public void testBackup() {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.backup(1);
        assertEquals(0, scs.bufpos);
        scs.UpdateLineColumn('a');
        scs.backup(1);
        assertEquals(-1, scs.bufpos);
    }

    @Test
    public void testGetImage() {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        assertEquals("abc", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        scs.UpdateLineColumn('d');
        scs.UpdateLineColumn('e');
        assertEquals("de", SimpleCharStream.GetSuffix(3));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new InputStreamReader(mock(InputStream.class).mockConstructions()), 1, 1, 2048);
        scs.Close();
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }
}