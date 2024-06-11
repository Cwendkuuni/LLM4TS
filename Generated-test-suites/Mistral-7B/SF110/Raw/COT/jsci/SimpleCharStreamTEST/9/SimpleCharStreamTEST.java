package JSci.maths.symbolic; // Assuming the package name is correct

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 512);
        scs.ExpandBuff(false);
        Assert.assertEquals(1024, scs.bufsize);
        verify(scs, times(1)).Systemarraycopy(anyArray(), anyInt(), anyArray(), anyInt(), anyInt());
        verify(scs, times(1)).Systemarraycopy(anyArray(), anyInt(), anyArray(), anyInt(), anyInt());
        verify(scs, times(1)).Systemarraycopy(anyArray(), anyInt(), anyArray(), anyInt(), anyInt());
        verify(scs, times(1)).bufline = anyArray();
        verify(scs, times(1)).bufcolumn = anyArray();
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 512);
        when(scs.inputStream.read(anyCharArray(), anyInt(), anyInt())).thenReturn(1);

        scs.FillBuff();
        Assert.assertEquals(1, scs.maxNextCharInd);
        Assert.assertEquals(1, scs.available);
        verify(scs.inputStream, times(1)).read(anyCharArray(), anyInt(), anyInt());
    }

    @Test(expected = IOException.class)
    public void testFillBuffThrowsIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 512);
        when(scs.inputStream.read(anyCharArray(), anyInt(), anyInt())).thenThrow(new IOException());

        scs.FillBuff();
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 512);
        when(scs.inputStream.read(anyCharArray(), anyInt(), anyInt())).thenReturn('A');

        char c = scs.BeginToken();
        Assert.assertEquals('A', c);
        Assert.assertEquals(-1, scs.tokenBegin);

        c = scs.BeginToken();
        Assert.assertEquals('A', c);
        Assert.assertEquals(0, scs.bufpos);
        Assert.assertEquals(1, scs.tokenBegin);
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(new byte[0], 1, 1, 512);
        scs.UpdateLineColumn(' ');
        Assert.assertEquals(1, scs.line);
        Assert.assertEquals(1, scs.column);

        scs.UpdateLineColumn('\n');
        Assert.assertEquals(1, scs.line);
        Assert.assertEquals(1, scs.column);

        scs.UpdateLineColumn('\r');
        Assert.assertEquals(1, scs.line);
        Assert.assertEquals(1, scs.column);

        scs.UpdateLineColumn('\t');
        Assert.assertEquals(1, scs.line);
        Assert.assertEquals(9, scs.column);

        scs.UpdateLineColumn('\r');
        scs.UpdateLineColumn('\n');
        Assert.assertEquals(2, scs.line);
        Assert.assertEquals(1, scs.column);

        scs.UpdateLineColumn('A');
        Assert.assertEquals(2, scs.line);
        Assert.assertEquals(2, scs.column);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[]{'A'}), 1, 1, 512);
        char c = scs.readChar();
        Assert.assertEquals('A', c);
        Assert.assertEquals(0, scs.bufpos);
        Assert.assertEquals(1, scs.maxNextCharInd);
    }

    @Test
    public void testGetColumn() {
        SimpleCharStream scs = new SimpleCharStream(new byte[0], 1, 1, 512);
        Assert.assertEquals(0, scs.getColumn());

        scs.UpdateLineColumn(' ');
        Assert.assertEquals(1, scs.getColumn());
    }

    @Test
    public void testGetLine() {
        SimpleCharStream scs = new SimpleCharStream(new byte[0], 1, 1, 512);
        Assert.assertEquals(1, scs.getLine());

        scs.UpdateLineColumn('\n');
        Assert.assertEquals(2, scs.getLine());
    }

    @Test
    public void testGetEndColumn() {
        SimpleCharStream scs = new SimpleCharStream(new byte[0], 1, 1, 512);
        Assert.assertEquals(0, scs.getEndColumn());

        scs.UpdateLineColumn(' ');
        scs.UpdateLineColumn(' ');
        Assert.assertEquals(2, scs.getEndColumn());
    }

    @Test
    public void testGetEndLine() {
        SimpleCharStream scs = new SimpleCharStream(new byte[0], 1, 1, 512);
        Assert.assertEquals(1, scs.getEndLine());

        scs.UpdateLineColumn('\n');
        Assert.assertEquals(2, scs.getEndLine());
    }

    @Test
    public void testGetBeginColumn() {
        SimpleCharStream scs = new SimpleCharStream(new byte[0], 1, 1, 512);
        Assert.assertEquals(0, scs.getBeginColumn());

        scs.UpdateLineColumn(' ');
        scs.UpdateLineColumn(' ');
        Assert.assertEquals(0, scs.getBeginColumn());
    }

    @Test
    public void testGetBeginLine() {
        SimpleCharStream scs = new SimpleCharStream(new byte[0], 1, 1, 512);
        Assert.assertEquals(1, scs.getBeginLine());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 512);
        scs.backup(1);
        Assert.assertEquals(-1, scs.bufpos);
        Assert.assertEquals(0, scs.inBuf);

        scs.UpdateLineColumn(' ');
        scs.backup(1);
        Assert.assertEquals(0, scs.bufpos);
        Assert.assertEquals(1, scs.inBuf);
    }

    @Test
    public void testConstructor() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 512);
        Assert.assertNotNull(scs.buffer);
        Assert.assertNotNull(scs.bufline);
        Assert.assertNotNull(scs.bufcolumn);
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 512);
        scs.ReInit(new ByteArrayInputStream(new byte[0]), 1, 1, 512);
        Assert.assertNull(scs.buffer);
        Assert.assertNull(scs.bufline);
        Assert.assertNull(scs.bufcolumn);
    }

    @Test
    public void testGetImage() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[]{'A', 'B', 'C'}), 1, 1, 512);
        Assert.assertEquals("ABC", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[]{'A', 'B', 'C'}), 1, 1, 512);
        Assert.assertEquals("BC", SimpleCharStream.GetSuffix(2));
    }

    @Test
    public void testDone() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 512);
        SimpleCharStream.Done();
        Assert.assertNull(SimpleCharStream.buffer);
        Assert.assertNull(SimpleCharStream.bufline);
        Assert.assertNull(SimpleCharStream.bufcolumn);
    }
}