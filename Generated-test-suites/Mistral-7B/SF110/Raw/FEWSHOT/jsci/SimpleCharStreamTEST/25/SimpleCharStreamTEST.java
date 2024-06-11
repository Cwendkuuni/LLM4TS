package JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        SimpleCharStream scs = new SimpleCharStream(mock(InputStream.class), 1, 1, 2048);
        scs.ExpandBuff(false);
        assertEquals(4096, scs.bufsize);
        assertEquals(4096, scs.available);
        assertArrayEquals(new int[] {0, 0, 0}, scs.bufline);
        assertArrayEquals(new int[] {0, 0, 0}, scs.bufcolumn);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(InputStream.class), 1, 1, 2048);
        when(scs.inputStream.read(anyIntArray(), anyInt(), anyInt())).thenReturn(100);

        scs.FillBuff();
        assertEquals(103, scs.maxNextCharInd);
        assertEquals('a', scs.buffer[scs.bufpos]);

        scs.FillBuff();
        assertEquals(2048, scs.available);
        assertEquals('b', scs.buffer[scs.bufpos]);
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(InputStream.class), 1, 1, 2048);
        when(scs.inputStream.read(anyIntArray(), anyInt(), anyInt())).thenReturn('a');

        char c = scs.BeginToken();
        assertEquals('a', c);
        assertEquals(0, scs.tokenBegin);
        assertEquals(0, scs.bufpos);
    }

    @Test(expected = IOException.class)
    public void testBeginTokenWithIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(InputStream.class), 1, 1, 2048);
        when(scs.inputStream.read(anyIntArray(), anyInt(), anyInt())).thenThrow(new IOException("Test IOException"));

        scs.BeginToken();
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        scs.UpdateLineColumn('a');
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);

        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);

        scs.UpdateLineColumn('\n');
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);

        scs.UpdateLineColumn('\t');
        assertEquals(1, scs.line);
        assertEquals(9, scs.column);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(InputStream.class), 1, 1, 2048);
        when(scs.inputStream.read(anyIntArray(), anyInt(), anyInt())).thenReturn('a');

        char c = scs.readChar();
        assertEquals('a', c);
        assertEquals(0, scs.inBuf);
        assertEquals(0, scs.bufpos);

        c = scs.readChar();
        assertEquals('a', c);
        assertEquals(1, scs.inBuf);
        assertEquals(1, scs.bufpos);
    }

    @Test(expected = IOException.class)
    public void testReadCharWithIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(InputStream.class), 1, 1, 2048);
        when(scs.inputStream.read(anyIntArray(), anyInt(), anyInt())).thenThrow(new IOException("Test IOException"));

        scs.readChar();
    }

    @Test
    public void testGetColumn() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn
 ('c');
assertEquals(1, scs.getLine());
assertEquals(3, scs.getColumn());
 
    }

    @Test
    public void testGetLine() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testGetEndColumn() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        assertEquals(3, scs.getEndColumn());
    }

    @Test
    public void testGetEndLine() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('\n');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('\n');
        assertEquals(3, scs.getEndLine());
    }

    @Test
    public void testGetBeginColumn() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        scs.UpdateLineColumn('a');
        assertEquals(1, scs.getBeginColumn());
    }

    @Test
    public void testGetBeginLine() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        assertEquals(1, scs.getBeginLine());
    }

    @Test
    public void testBackup() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        scs.backup(1);
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.backup(11);
        assertEquals(1, scs.getLine());
        assertEquals(12, scs.getColumn());
    }

    @Test
    public void testSimpleCharStreamConstruction() {
        Reader reader = mock(Reader.class);
        SimpleCharStream scs1 = new SimpleCharStream(reader, 1, 1, 2048);
        SimpleCharStream scs2 = new SimpleCharStream(reader, 1, 1);
        SimpleCharStream scs3 = new SimpleCharStream(reader);

        assertEquals(reader, scs1.inputStream);
        assertEquals(reader, scs2.inputStream);
        assertEquals(reader, scs3.inputStream);

        assertFalse(scs1.inputStream.equals(scs2.inputStream));
        assertFalse(scs1.inputStream.equals(scs3.inputStream));
        assertFalse(scs2.inputStream.equals(scs3.inputStream));
    }

    @Test
    public void testSimpleCharStreamReInit() {
        InputStream inputStream = mock(InputStream.class);
        SimpleCharStream scs = new SimpleCharStream(inputStream, 1, 1, 2048);
        scs.ReInit(inputStream, 2, 2);
        assertEquals(2, scs.line);
        assertEquals(2, scs.column);
    }

    @Test
    public void testGetImage() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        assertEquals("abcc", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        scs.UpdateLineColumn('d');
        assertArrayEquals("dcdc".toCharArray(), SimpleCharStream.GetSuffix(4));
    }

    @Test
    public void testDone() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        SimpleCharStream.Done();
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 2048);
        scs.UpdateLineColumn('a');
        scs.UpdateLineColumn('b');
        scs.UpdateLineColumn('c');
        scs.UpdateLineColumn('d');
        SimpleCharStream.adjustBeginLineColumn(2, 3);
        assertEquals(2, scs.getBeginLine());
        assertEquals(3, scs.getBeginColumn());
    }
}