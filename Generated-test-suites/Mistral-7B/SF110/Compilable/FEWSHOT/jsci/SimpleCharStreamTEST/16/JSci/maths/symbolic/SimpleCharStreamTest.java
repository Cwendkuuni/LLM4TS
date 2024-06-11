package JSci.maths.symbolic;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 1024);
        scs.ExpandBuff(false);
        assertEquals(2048, scs.bufsize);

        scs.ExpandBuff(true);
        assertEquals(4096, scs.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 1024);
        scs.FillBuff();
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals('t', scs.readChar());

        scs.FillBuff();
        assertEquals('\0', scs.readChar());
        assertEquals('\0', scs.readChar());
        assertEquals('\0', scs.readChar());
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 1024);
        assertEquals('\t', scs.BeginToken());
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals('\n', scs.readChar());

        scs.backup(1);
        assertEquals('\n', scs.readChar());
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 1024);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('t');
        assertEquals(1, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\t');
        assertEquals(2, scs.getLine());
        assertEquals(9, scs.getColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 1024);

        assertEquals('\t', scs.BeginToken());
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals('\n', scs.readChar());

        assertEquals('\n', scs.readChar());
        assertNull(scs.readChar());
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 1024);

        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('t');
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 1024);

        assertEquals('\t', scs.BeginToken());
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals('\n', scs.readChar());

        scs.backup(1);
        assertEquals('s', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('t', scs.readChar());
        assertEquals('t', scs.readChar());
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 1024);

        assertEquals('\t', scs.BeginToken());
        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals('\n', scs.readChar());

        scs.ReInit(new ByteArrayInputStream("test2\n".getBytes()), 1, 1);

        assertEquals('t', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals('t', scs.readChar());
        assertEquals('\n', scs.readChar());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 1024);

        assertEquals("test\n", new String(SimpleCharStream.GetImage()));
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("testtest\n".getBytes()), 1, 1, 1024);

        assertEquals("st", SimpleCharStream.GetSuffix(2));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 1024);

        SimpleCharStream.Done();

        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 1024);

        SimpleCharStream.adjustBeginLineColumn(2, 2);

        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());
    }
}