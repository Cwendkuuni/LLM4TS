package JSci.maths.symbolic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        final SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        when(scs.inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenReturn(-1);

        scs.ExpandBuff(false);
        assertEquals(2048, scs.bufsize);
        assertEquals(0, scs.tokenBegin);
        assertEquals(2048, scs.available);

        scs.ExpandBuff(true);
        assertEquals(4096, scs.bufsize);
        assertEquals(0, scs.tokenBegin);
        assertEquals(4096, scs.available);
    }

    @Test
    public void testFillBuff() throws IOException {
        final SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);

        when(scs.inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd))
                .thenReturn(3).thenReturn(-1);

        scs.FillBuff();
        assertEquals(3, scs.maxNextCharInd);
        assertEquals('a', scs.buffer[scs.maxNextCharInd - 1]);

        scs.FillBuff();
        assertEquals(-1, scs.maxNextCharInd);
    }

    @Test(expected = IOException.class)
    public void testFillBuff_IOException() throws IOException {
        final SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);

        when(scs.inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenThrow(new IOException());

        scs.FillBuff();
    }

    @Test
    public void testBeginToken() throws IOException {
        final SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);

        when(scs.inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd)).thenReturn('a');

        assertEquals('a', scs.BeginToken());
        assertEquals(0, scs.tokenBegin);
        assertEquals(1, scs.bufpos);
    }

    @Test
    public void testUpdateLineColumn() {
        final SimpleCharStream scs = new SimpleCharStream(null, 1, 1, 1);

        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);
        assertEquals(1, scs.bufline[0]);
        assertEquals(1, scs.bufcolumn[0]);

        scs.UpdateLineColumn('\n');
        assertEquals(1, scs.line);
        assertEquals(1, scs.column);
        assertEquals(1, scs.bufline[0]);
        assertEquals(1, scs.bufcolumn[0]);

        scs.UpdateLineColumn('\t');
        assertEquals(1, scs.line);
        assertEquals(9, scs.column);
        assertEquals(1, scs.bufline[0]);
        assertEquals(9, scs.bufcolumn[0]);

        scs.UpdateLineColumn('x');
        assertEquals(1, scs.line);
        assertEquals(10, scs.column);
        assertEquals(1, scs.bufline[0]);
        assertEquals(10, scs.bufcolumn[0]);
    }

    @Test
    public void testReadChar() throws IOException {
        final SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);

        when(scs.inputStream.read(scs.buffer, scs.maxNextCharInd, scs.available - scs.maxNextCharInd))
                .thenReturn('a').thenReturn('b').thenReturn(3).thenReturn(-1);

        assertEquals('a', scs.readChar());
        assertEquals('a', scs.buffer[0]);
        assertEquals(0, scs.bufpos);
        assertEquals(1, scs.maxNextCharInd);

        assertEquals('b', scs.readChar());
        assertEquals('b', scs.buffer[0]);
        assertEquals(1, scs.bufpos);
        assertEquals(2, scs.maxNextCharInd);

        assertEquals('a', scs.readChar());
        assertEquals('a', scs.buffer[1]);
        assertEquals(1, scs.bufpos);
        assertEquals(2, scs.maxNextCharInd);

        assertEquals(-1, scs.readChar());
        assertEquals(-1, scs.maxNextCharInd);
        assertEquals(1, scs.inBuf);
        assertEquals(0, scs.bufpos);
    }

    @Test
    public void testGetLineColumn() {
        final SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);

        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getBeginLine());
        assertEquals(1, scs.getBeginColumn());
        assertEquals(1, scs.getEndLine());
        assertEquals(1, scs.getEndColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals(2, scs.getBeginLine());
        assertEquals(1, scs.getBeginColumn());
        assertEquals(2, scs.getEndLine());
        assertEquals(1, scs.getEndColumn());

        scs.UpdateLineColumn('\r');
        scs.UpdateLineColumn('\n');
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals(3, scs.getBeginLine());
        assertEquals(1, scs.getBeginColumn());
        assertEquals(3, scs.getEndLine());
        assertEquals(1, scs.getEndColumn());
    }

    @Test
    public void testBackup() {
        final SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);

        scs.bufpos = 2;
        scs.backup(1);
        assertEquals(1, scs.bufpos);
        assertEquals(2, scs.tokenBegin);
        assertEquals(3, scs.maxNextCharInd);
        assertEquals('b', scs.buffer[0]);
        assertEquals('c', scs.buffer[1]);
        assertEquals(null, scs.inputStream);

        scs.bufpos = 0;
        scs.backup(3);
        assertEquals(-1, scs.bufpos);
        assertEquals(3, scs.tokenBegin);
        assertEquals(3, scs.maxNextCharInd);
        assertEquals(null, scs.buffer);
        assertEquals(null, scs.inputStream);
    }

    @Test
    public void testGetImage() {
        final SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);

        when(scs.inputStream.read(scs.buffer, 0, scs.bufsize)).thenReturn(5);
        scs.inputStream.read(scs.buffer, 0, scs.bufsize);

        assertEquals("abcde", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() {
        final SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);

        assertEquals("de", SimpleCharStream.GetSuffix(3));
        assertEquals("abc", SimpleCharStream.GetSuffix(3));
        assertEquals("", SimpleCharStream.GetSuffix(0));
    }

    @Test
    public void testDone() {
        final SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        SimpleCharStream.Done();
        assertEquals(null, SimpleCharStream.buffer);
        assertEquals(null, SimpleCharStream.bufline);
        assertEquals(null, SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        final SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);

        when(scs.inputStream.read(scs.buffer, 0, scs.bufsize)).thenReturn(10);
        scs.inputStream.read(scs.buffer, 0, scs.bufsize);

        scs.adjustBeginLineColumn(2, 2);
        assertEquals(2, scs.line);
        assertEquals(2, scs.column);
        assertEquals(2, scs.bufline[0]);
        assertEquals(2, scs.bufcolumn[0]);
        assertEquals(2, scs.bufline[1]);
        assertEquals(4, scs.bufcolumn[1]);
        assertEquals(2, scs.bufline[2]);
        assertEquals(6, scs.bufcolumn[2]);
        assertEquals(2, scs.bufline[3]);
        assertEquals(8, scs.bufcolumn[3]);
        assertEquals(2, scs.bufline[4]);
        assertEquals(10, scs.bufcolumn[4]);
        assertEquals(2, scs.bufline[5]);
        assertEquals(12, scs.bufcolumn[5]);
        assertEquals(2, scs.bufline[6]);
        assertEquals(14, scs.bufcolumn[6]);
        assertEquals(2, scs.bufline[7]);
        assertEquals(16, scs.bufcolumn[7]);
        assertEquals(2, scs.bufline[8]);
        assertEquals(18, scs.bufcolumn[8]);
        assertEquals(2, scs.bufline[9]);
        assertEquals(20, scs.bufcolumn[9]);
        assertEquals(2, scs.bufline[10]);
        assertEquals(22, scs.bufcolumn[10]);

        scs.adjustBeginLineColumn(3, 3);
        assertEquals(3, scs.line);
        assertEquals(3, scs.column);
        assertEquals(3, scs.bufline[0]);
        assertEquals(3, scs.bufcolumn[0]);
        assertEquals(3, scs.bufline[1]);
        assertEquals(6, scs.bufcolumn[1]);
        assertEquals(3, scs.bufline[2]);
        assertEquals(9, scs.bufcolumn[2]);
        assertEquals(3, scs.bufline[3]);
        assertEquals(12, scs.bufcolumn[3]);
        assertEquals(3, scs.bufline[4]);
        assertEquals(15, scs.bufcolumn[4]);
        assertEquals(3, scs.bufline[5]);
        assertEquals(18, scs.bufcolumn[5]);
        assertEquals(3, scs.bufline[6]);
        assertEquals(21, scs.bufcolumn[6]);
        assertEquals(3, scs.bufline[7]);
        assertEquals(24, scs.bufcolumn[7]);
        assertEquals(3, scs.bufline[8]);
        assertEquals(27, scs.bufcolumn[8]);
        assertEquals(3, scs.bufline[9]);
        assertEquals(30, scs.bufcolumn[9]);
        assertEquals(3, scs.bufline[10]);
        assertEquals(33, scs.bufcolumn[10]);
    }
}