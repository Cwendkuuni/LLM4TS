package JSci.maths.symbolic;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1, 4096);
        scs.ExpandBuff(true);
        assertEquals(SimpleCharStream.bufsize * 2, scs.available);
        assertEquals(SimpleCharStream.bufsize * 2, scs.maxNextCharInd);
        SimpleCharStream.bufsize = SimpleCharStream.bufsize; // Reset the size
        scs.ExpandBuff(false);
        assertEquals(SimpleCharStream.bufsize, scs.available);
        assertEquals(SimpleCharStream.bufsize, scs.maxNextCharInd);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1, 4096);
        scs.FillBuff();
        assertEquals(' ', scs.readChar());
        assertEquals(' ', scs.readChar());
        assertEquals(' ', scs.readChar());
        assertEquals(-1, scs.readChar());
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("Hello World"), 1, 1, 4096);
        assertEquals('H', scs.BeginToken());
        assertEquals('H', scs.readChar());
        assertEquals(-1, scs.readChar());
    }

    @Test(expected = IOException.class)
    public void testBeginTokenWithIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader(""), 1, 1, 4096);
        scs.inputStream.close();
        scs.BeginToken();
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("Hello\nWorld\n"), 1, 1, 4096);
        assertEquals('H', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('\n', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('W', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());
    }

    @Test
    public void testGetLineAndColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("Hello\nWorld\n"), 1, 1, 4096);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('H', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(2, scs.getColumn());
        assertEquals('\n', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('W', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());
        assertEquals('\n', scs.readChar());
        assertEquals(-1, scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(3, scs.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("Hello\nWorld\n"), 1, 1, 4096);
        scs.backup(1);
        assertEquals('H', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(2, scs.getColumn());
        scs.backup(10);
        assertEquals('H', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        scs.backup(100);
        assertEquals(-1, scs.readChar());
    }

    @Test
    public void testConstructor() {
        SimpleCharStream scs1 = new SimpleCharStream(new StringReader(""), 1, 1, 4096);
        SimpleCharStream scs2 = new SimpleCharStream(new StringReader(""), 1, 1, 4096);
        assertNotSame(scs1, scs2);
    }

    @Test
    public void testReInit() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("Hello\nWorld\n"), 1, 1, 4096);
        scs.ReInit(new StringReader(""), 1, 1, 4096);
        assertEquals(-1, scs.readChar());
    }

    @Test
    public void testGetImage() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("Hello\nWorld\n"), 1, 1, 4096);
        assertEquals("Hello\nWorld\n", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("HelloWorld"), 1, 1, 4096);
        assertEquals("ld", SimpleCharStream.GetSuffix(2));
    }

    @Test
    public void testDone() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("Hello\nWorld\n"), 1, 1, 4096);
        SimpleCharStream.Done();
        assertEquals(-1, scs.readChar());
    }

    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(new StringReader("1\n2\n3\n4\n5"), 1, 1, 4096);
        SimpleCharStream.adjustBeginLineColumn(2, 1);
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('2', scs.readChar());
        assertEquals('2', scs.readChar());
        assertEquals('3', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(4, scs.getColumn());
        SimpleCharStream.adjustBeginLineColumn(3, 1);
        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('4', scs.readChar());
        assertEquals(-1, scs.readChar());
    }
}