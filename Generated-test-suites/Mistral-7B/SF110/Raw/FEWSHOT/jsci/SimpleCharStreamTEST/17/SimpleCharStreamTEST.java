package JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws Exception {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\n"), 1, 1);
        assertEquals('\n', scs.BeginToken());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testFillBuff() throws Exception {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\n"), 1, 1);
        scs.FillBuff();
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
        assertEquals('\n', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(4, scs.getColumn());
    }

    @Test
    public void testUpdateLineColumn() throws Exception {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\n"), 1, 1);
        scs.UpdateLineColumn('\r');
        assertEquals(1, scs.getLine());
        assertEquals(0, scs.getColumn());
        scs.UpdateLineColumn('\n');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('t');
        assertEquals(1, scs.getLine());
        assertEquals(2, scs.getColumn());
        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testGetLineColumn() throws Exception {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\n"), 1, 1);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(2, scs.getColumn());
        assertEquals('\r', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(3, scs.getColumn());
        assertEquals('\n', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
    }

    @Test
    public void testBackup() throws Exception {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\n"), 1, 1);
        assertEquals('t', scs.readChar());
        assertEquals('t', scs.readChar());
        scs.backup(2);
        assertEquals('t', scs.readChar());
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(3, scs.getColumn());
    }

    @Test
    public void testExpandBuff() throws Exception {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\n"), 1, 1);
        for (int i = 0; i < 2052; i++) {
            assertEquals((char) i, scs.readChar());
        }
        scs.ExpandBuff(false);
        assertEquals((char) 0, scs.readChar());
        assertEquals((char) 0, scs.readChar());
        assertEquals((char) 1, scs.readChar());
        assertEquals((char) 2, scs.readChar());
        assertEquals(3, scs.getLine());
        assertEquals(3, scs.getColumn());
    }

    @Test
    public void testStaticFields() {
        assertEquals(true, SimpleCharStream.staticFlag);
        assertEquals(4096, SimpleCharStream.bufsize);
        assertNull(SimpleCharStream.inputStream);
        assertArrayEquals(new int[4096], SimpleCharStream.bufline);
        assertArrayEquals(new int[4096], SimpleCharStream.bufcolumn);
    }

    @Test
    public void testReInit() throws Exception {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\n"), 1, 1);
        scs.ReInit(new StringReader("test2\r\n"), 2, 2);
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('t', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());
        assertEquals('\r', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());
        assertEquals('\n', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(4, scs.getColumn());
    }

    @Test
    public void testGetImage() throws Exception {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\n"), 1, 1);
        assertEquals("test\r\n", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() throws Exception {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\n"), 1, 1);
        assertEquals("tn", SimpleCharStream.GetSuffix(2));
        assertEquals("rn", SimpleCharStream.GetSuffix(3));
        assertEquals("\n", SimpleCharStream.GetSuffix(0));
    }

    @Test
    public void testDone() throws Exception {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\n"), 1, 1);
        SimpleCharStream.Done();
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws Exception {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("test\r\n--test2\r\n--test3\r\n--test4\r\n--test5\r\n--test6\r\n--test7\r\n--test8\r\n--test9\r\n--test10\r\n"), 1, 1);
        SimpleCharStream.adjustBeginLineColumn(3, 5);
        assertEquals(3, scs.getLine());
        assertEquals(5, scs.getColumn());
    }
}