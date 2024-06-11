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
    public void testExpandBuff() throws IOException {
        SimpleCharStream sut = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        when(sut.inputStream.read(sut.buffer, sut.maxNextCharInd, sut.available - sut.maxNextCharInd)).thenReturn(10);
        sut.ExpandBuff(false);
        assertEquals(2048, sut.bufsize);
        assertEquals(1024, sut.tokenBegin);
        assertEquals(2048, sut.available);
        assertEquals(0, sut.bufpos);
        assertEquals(sut.buffer, sut.bufline);
        assertEquals(sut.buffer, sut.bufcolumn);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream sut = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        when(sut.inputStream.read(sut.buffer, sut.maxNextCharInd, sut.available - sut.maxNextCharInd)).thenReturn(-1);
        sut.FillBuff();
        assertEquals(1024, sut.available);
        assertEquals(sut.bufpos, SimpleCharStream.tokenBegin);
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream sut = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        when(sut.inputStream.read(sut.buffer, sut.maxNextCharInd, sut.available - sut.maxNextCharInd)).thenReturn('A');
        char result = sut.BeginToken();
        assertEquals('A', result);
        assertEquals(-1, sut.tokenBegin);
    }

    @Test(expected = IOException.class)
    public void testBeginTokenIOException() throws IOException {
        SimpleCharStream sut = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        when(sut.inputStream.read(sut.buffer, sut.maxNextCharInd, sut.available - sut.maxNextCharInd)).thenThrow(new IOException());
        sut.BeginToken();
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream sut = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        sut.UpdateLineColumn('A');
        assertEquals(1, sut.column);
        assertEquals(1, sut.line);
        sut.UpdateLineColumn('\n');
        sut.UpdateLineColumn('B');
        assertEquals(1, sut.line);
        assertEquals(2, sut.column);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream sut = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        when(sut.inputStream.read(sut.buffer, sut.maxNextCharInd, sut.available - sut.maxNextCharInd)).thenReturn('A');
        char result = sut.readChar();
        assertEquals('A', result);
        assertEquals(-1, sut.tokenBegin);
        assertEquals(sut.bufpos, 0);
    }

    @Test
    public void testGetColumn() {
        SimpleCharStream sut = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        sut.UpdateLineColumn('A');
        sut.UpdateLineColumn('\t');
        sut.UpdateLineColumn('\n');
        sut.UpdateLineColumn('B');
        sut.UpdateLineColumn(' ');
        sut.UpdateLineColumn('C');
        assertEquals(1, sut.getColumn());
        assertEquals(1, sut.getLine());
        assertEquals(2, sut.getEndColumn());
        assertEquals(2, sut.getEndLine());
        assertEquals(7, sut.getBeginColumn());
        assertEquals(1, sut.getBeginLine());
    }

    @Test
    public void testBackup() {
        SimpleCharStream sut = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        sut.UpdateLineColumn('A');
        sut.UpdateLineColumn('\t');
        sut.UpdateLineColumn('\n');
        sut.UpdateLineColumn('B');
        sut.UpdateLineColumn(' ');
        sut.UpdateLineColumn('C');
        int backupAmount = 2;
        sut.backup(backupAmount);
        assertEquals(5, sut.bufpos);
        assertEquals(1, sut.column);
        assertEquals(1, sut.line);
        assertEquals(8, sut.getBeginColumn());
        assertEquals(1, sut.getBeginLine());
        assertEquals(6, sut.getEndColumn());
        assertEquals(1, sut.getEndLine());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBackupNegativeAmount() {
        SimpleCharStream sut = new SimpleCharStream(mock(Reader.class), 1, 1, 1024);
        sut.backup(-1);
    }

}