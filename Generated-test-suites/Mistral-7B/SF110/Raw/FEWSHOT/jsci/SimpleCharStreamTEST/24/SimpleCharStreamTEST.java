package JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class SimpleCharStreamTest {
    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        scs.ExpandBuff(true);
        assertEquals(2048, scs.bufsize);
        assertEquals(0, scs.tokenBegin);
        assertEquals(0, scs.bufpos);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 2048);
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenReturn(10);
        when(inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenReturn(-1);

        scs.inputStream = inputStream;
        scs.FillBuff();
        assertEquals(2068, scs.available);
    }

    @Test(expected = IOException.class)
    public void testFillBuffThrowsIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 2048);
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenThrow(new IOException());

        scs.inputStream = inputStream;
        scs.FillBuff();
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        assertEquals('\u0000', scs.BeginToken());
        assertEquals(0, scs.tokenBegin);

        scs.buffer[1] = 'A';
        scs.bufpos = 1;
        assertEquals('A', scs.BeginToken());
        assertEquals(1, scs.tokenBegin);
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(new byte[0], 1, 1, 1024);
        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.line);
        assertEquals(1, scs.column);

        scs.UpdateLineColumn('\r');
        assertEquals(2, scs.line);
        assertEquals(1, scs.column);

        scs.UpdateLineColumn('\t');
        assertEquals(2, scs.line);
        assertEquals(9, scs.column);

        scs.UpdateLineColumn('A');
        assertEquals(2, scs.line);
        assertEquals(10, scs.column);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[]{'A'}), 1, 1, 1024);
        assertEquals('A', scs.readChar());
        assertEquals(0, scs.bufpos);
        assertEquals(1, scs.maxNextCharInd);

        scs.bufpos = 1;
        assertEquals('A', scs.readChar());
        assertEquals(2, scs.maxNextCharInd);

        scs.FillBuff();
        assertEquals('B', scs.readChar());
        assertEquals(3, scs.maxNextCharInd);
    }

    @Test
    public void testGetLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(new byte[0], 1, 1, 1024);
        assertEquals(1, scs.getLine());
        assertEquals(0, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\r');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\t');
        assertEquals(2, scs.getLine());
        assertEquals(9, scs.getColumn());

        scs.UpdateLineColumn('A');
        assertEquals(2, scs.getLine());
        assertEquals(10, scs.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[]{'A'}), 1, 1, 1024);
        scs.backup(1);
        assertEquals('A', scs.buffer[scs.bufpos]);
        assertEquals(0, scs.inBuf);
        assertEquals(0, scs.bufpos);

        scs.bufpos = 1;
        scs.backup(3);
        assertEquals('A', scs.buffer[scs.bufpos]);
        assertEquals(0, scs.inBuf);
        assertEquals(-2, scs.bufpos);

        scs.backup(10);
        assertEquals('A', scs.buffer[scs.bufpos]);
        assertEquals(0, scs.inBuf);
        assertEquals(10, scs.bufpos);
    }

    @Test
    public void testGetImage() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[]{'A', 'B', 'C'}), 1, 1, 1024);
        assertEquals("ABC", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[]{'A', 'B', 'C'}), 1, 1, 1024);
        assertEquals(new char[]{'C'}, SimpleCharStream.GetSuffix(1));
        assertEquals(new char[]{'B', 'C'}, SimpleCharStream.GetSuffix(2));
        assertEquals(new char[0], SimpleCharStream.GetSuffix(0));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[]{'A'}), 1, 1, 1024);
        SimpleCharStream.Done();
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[]{'A', 'B', 'C', 'D', 'E', 'F'}), 1, 1, 1024);
        SimpleCharStream.adjustBeginLineColumn(5, 10);
        assertEquals(5, SimpleCharStream.bufline[0]);
        assertEquals(10, SimpleCharStream.bufcolumn[0]);
        assertEquals(5, SimpleCharStream.bufline[1]);
        assertEquals(17, SimpleCharStream.bufcolumn[1]);
        assertEquals(5, SimpleCharStream.bufline[2]);
        assertEquals(24, SimpleCharStream.bufcolumn[2]);
        assertEquals(5, SimpleCharStream.bufline[3]);
        assertEquals(31, SimpleCharStream.bufcolumn[3]);
        assertEquals(5, SimpleCharStream.bufline[4]);
        assertEquals(38, SimpleCharStream.bufcolumn[4]);
        assertEquals(5, SimpleCharStream.bufline[5]);
        assertEquals(45, SimpleCharStream.bufcolumn[5]);
        assertEquals(5, SimpleCharStream.bufline[6]);
        assertEquals(52, SimpleCharStream.bufcolumn[6]);
        assertEquals(5, SimpleCharStream.bufline[7]);
        assertEquals(55, SimpleCharStream.bufcolumn[7]);
    }
}