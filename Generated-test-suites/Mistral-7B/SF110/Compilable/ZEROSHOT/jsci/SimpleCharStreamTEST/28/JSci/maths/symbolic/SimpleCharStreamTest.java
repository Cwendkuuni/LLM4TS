package jsunit;

import org.junit.Assert;
import org.junit.Test;
import JSci.maths.symbolic.SimpleCharStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 256);
        Assert.assertEquals(256, SimpleCharStream.bufsize);
        for (int i = 0; i < 255; i++) {
            scs.readChar();
        }
        scs.ExpandBuff(false);
        Assert.assertEquals(512, SimpleCharStream.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 256);
        Assert.assertEquals(256, SimpleCharStream.bufsize);
        for (int i = 0; i < 255; i++) {
            scs.readChar();
        }
        scs.FillBuff();
        Assert.assertEquals('t', scs.readChar());
        Assert.assertEquals('e', scs.readChar());
        Assert.assertEquals('s', scs.readChar());
        Assert.assertEquals('t', scs.readChar());
        Assert.assertEquals('\u0000', scs.readChar());
        Assert.assertEquals(-1, scs.readChar());
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 256);
        Assert.assertEquals('t', scs.BeginToken());
        Assert.assertEquals('\r', scs.readChar());
        Assert.assertEquals('\n', scs.readChar());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 256);
        scs.BeginToken();
        scs.UpdateLineColumn('t');
        Assert.assertEquals(1, SimpleCharStream.bufline[SimpleCharStream.bufpos]);
        Assert.assertEquals(1, SimpleCharStream.bufcolumn[SimpleCharStream.bufpos]);
        scs.UpdateLineColumn('\r');
        Assert.assertEquals(1, SimpleCharStream.bufline[SimpleCharStream.bufpos]);
        Assert.assertEquals(2, SimpleCharStream.bufcolumn[SimpleCharStream.bufpos]);
        scs.UpdateLineColumn('\n');
        Assert.assertEquals(2, SimpleCharStream.bufline[SimpleCharStream.bufpos]);
        Assert.assertEquals(1, SimpleCharStream.bufcolumn[SimpleCharStream.bufpos]);
    }

    @Test
    public void testGetters() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 256);
        Assert.assertEquals(1, SimpleCharStream.bufline[SimpleCharStream.bufpos]);
        Assert.assertEquals(1, SimpleCharStream.bufcolumn[SimpleCharStream.bufpos]);
        Assert.assertEquals(1, SimpleCharStream.getLine());
        Assert.assertEquals(1, SimpleCharStream.getColumn());
        Assert.assertEquals(1, SimpleCharStream.getBeginLine());
        Assert.assertEquals(1, SimpleCharStream.getBeginColumn());
        Assert.assertEquals(2, SimpleCharStream.getEndLine());
        Assert.assertEquals(2, SimpleCharStream.getEndColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 256);
        Assert.assertEquals('t', scs.readChar());
        Assert.assertEquals('e', scs.readChar());
        scs.backup(1);
        Assert.assertEquals('t', scs.readChar());
        Assert.assertEquals('e', scs.readChar());
        Assert.assertEquals('t', scs.readChar());
        scs.backup(3);
        Assert.assertEquals('t', scs.readChar());
        Assert.assertEquals('t', scs.readChar());
        Assert.assertEquals('t', scs.readChar());
        Assert.assertEquals('\r', scs.readChar());
        Assert.assertEquals('\n', scs.readChar());
        Assert.assertEquals(-1, scs.readChar());
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 256);
        Assert.assertEquals('t', scs.readChar());
        Assert.assertEquals('e', scs.readChar());
        scs.ReInit(new ByteArrayInputStream("test2\r\n".getBytes()), 1, 1, 256);
        Assert.assertEquals('t', scs.readChar());
        Assert.assertEquals('e', scs.readChar());
        Assert.assertEquals('2', scs.readChar());
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 256);
        scs.FillBuff();
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 256);
        Assert.assertEquals("test\r\n", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n hello world\r\n".getBytes()), 1, 1, 256);
        Assert.assertEquals("ld\r\n", SimpleCharStream.GetSuffix(3));
        Assert.assertEquals("ld\r\nhello world", SimpleCharStream.GetImage());
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 256);
        SimpleCharStream.Done();
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1:\r\nline2:test\r\nline3:\r\nline4:test\r\n".getBytes()), 1, 1, 256);
        SimpleCharStream.adjustBeginLineColumn(3, 1);
        Assert.assertEquals(3, SimpleCharStream.bufline[SimpleCharStream.bufpos]);
        Assert.assertEquals(1, SimpleCharStream.bufcolumn[SimpleCharStream.bufpos]);
        Assert.assertEquals(4, SimpleCharStream.getBeginLine());
        Assert.assertEquals(1, SimpleCharStream.getBeginColumn());
    }
}