package JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1);
        assertEquals('t', scs.BeginToken());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.ReInit(new StringReader("test"));
        assertEquals('t', scs.BeginToken());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.FillBuff();
        assertEquals('e', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.FillBuff();
        assertEquals('s', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.FillBuff();
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(4, scs.getColumn());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\nnewline"), 1, 1);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('e');
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\nnewline"), 1, 1);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('e');
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());

        scs.UpdateLineColumn('s');
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());

        scs.UpdateLineColumn('\n');
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.UpdateLineColumn('t');
        assertEquals(3, scs.getLine());
        assertEquals(2, scs.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.backup(1);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.backup(3);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.backup(5);
        assertEquals(null, scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        scs.ReInit(new StringReader("test"));
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        SimpleCharStream.Done();
        assertFalse(SimpleCharStream.inputStream instanceof Reader);
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1);
        assertEquals("test", SimpleCharStream.GetImage());

        scs.backup(3);
        assertEquals("est", SimpleCharStream.GetImage());

        scs.FillBuff();
        assertEquals("esttest", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test"), 1, 1);
        assertArrayEquals(new char[0], SimpleCharStream.GetSuffix(0));

        scs.backup(3);
        assertArrayEquals(new char[]{'e', 's', 't'}, SimpleCharStream.GetSuffix(3));

        scs.FillBuff();
        assertArrayEquals(new char[]{'t', 'e', 's', 't'}, SimpleCharStream.GetSuffix(4));
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\nnewline1\r\nnewline2\r\nnewline3"), 1, 1);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        SimpleCharStream.adjustBeginLineColumn(2, 1);
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        SimpleCharStream.adjustBeginLineColumn(3, 1);
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());

        SimpleCharStream.adjustBeginLineColumn(3, 2);
        assertEquals(3, scs.getLine());
        assertEquals(2, scs.getColumn());

        SimpleCharStream.adjustBeginLineColumn(3, 4);
        assertEquals(3, scs.getLine());
        assertEquals(4, scs.getColumn());

        SimpleCharStream.adjustBeginLineColumn(3, 5);
        assertEquals(3, scs.getLine());
        assertEquals(5, scs.getColumn());

        SimpleCharStream.adjustBeginLineColumn(4, 1);
        assertEquals(4, scs.getLine());
        assertEquals(1, scs.getColumn());

        SimpleCharStream.adjustBeginLineColumn(4, 2);
        assertEquals(4, scs.getLine());
        assertEquals(2, scs.getColumn());

        SimpleCharStream.adjustBeginLineColumn(4, 5);
        assertEquals(4, scs.getLine());
        assertEquals(5, scs.getColumn());
    }
}