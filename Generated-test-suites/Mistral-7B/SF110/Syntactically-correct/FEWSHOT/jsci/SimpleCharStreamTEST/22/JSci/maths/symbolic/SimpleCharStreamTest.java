package JSci.maths.symbolic;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1);
        assertEquals('', scs.BeginToken());
        assertEquals(-1, scs.getLine());
        assertEquals(0, scs.getColumn());
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdefghijklmnopqrstuvwxyz"), 1, 1);
        assertEquals('a', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.FillBuff();
        assertEquals('b', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(2, scs.getColumn());
        scs.FillBuff();
        assertEquals('c', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(3, scs.getColumn());
        scs.FillBuff();
        assertEquals('d', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(4, scs.getColumn());
        scs.FillBuff();
        assertEquals('e', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(5, scs.getColumn());
        // Test the wrap around case
        scs.ExpandBuff(true);
        assertEquals('f', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(6, scs.getColumn());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1);
        assertEquals(1, scs.getLine());
        assertEquals(0, scs.getColumn());
        scs.UpdateLineColumn('a');
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('\r');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('\t');
        assertEquals(2, scs.getLine());
        assertEquals(9, scs.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdefg"), 1, 1);
        assertEquals('a', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.backup(1);
        assertEquals('a', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.backup(2);
        assertEquals('a', scs.readChar());
        assertEquals('a', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(0, scs.getColumn());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdefghijklmnopqrstuvwxyz"), 1, 1);
        assertEquals(new String(scs.buffer, 0, scs.bufpos + 1), scs.GetImage());
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdefghijklmnopqrstuvwxyz"), 1, 1);
        char[] suffix = scs.GetSuffix(10);
        assertEquals("ijklmno pqrstu", new String(suffix));
    }

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1);
        scs.ExpandBuff(false);
        assertEquals(4096, scs.bufsize);
        scs.ExpandBuff(true);
        assertEquals(6144, scs.bufsize);
        scs.ExpandBuff(false);
        assertEquals(8192, scs.bufsize);
    }

    @Test(expected = IOException.class)
    public void testFillBuffIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1);
        scs.inputStream.close();
        scs.FillBuff();
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdefghijklmnopqrstuvwxyz"), 1, 1);
        scs.ReInit(new StringReader("1234567890"), 2, 2);
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());
        assertEquals('1', scs.readChar());
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("abcdefghijklmnopqrstuvwxyz"), 1, 1);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('a');
        assertEquals(1, scs.getLine());
        assertEquals(2, scs.getColumn());
        scs.UpdateLineColumn('\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('\r');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.UpdateLineColumn('\t');
        assertEquals(2, scs.getLine());
        assertEquals(9, scs.getColumn());
    }

    @Test
    public void testDone() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1);
        scs.Done();
        assertNull(scs.buffer);
        assertNull(scs.bufline);
        assertNull(scs.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("1\n2\n3\n4\n5\n6\n7\n8\n9"), 1, 1);
        scs.adjustBeginLineColumn(3, 1);
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals("3", scs.GetImage());
    }
}