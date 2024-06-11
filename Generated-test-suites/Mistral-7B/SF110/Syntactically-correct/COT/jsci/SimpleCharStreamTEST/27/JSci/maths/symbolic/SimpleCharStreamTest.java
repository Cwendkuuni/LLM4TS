package JSci.maths.symbolic;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimpleCharStreamTest {

    static SimpleCharStream createStream(Reader inputStream, int startline, int startcolumn, int buffersize) {
        return new SimpleCharStream(inputStream, startline, startcolumn, buffersize);
    }

    static SimpleCharStream createStream(Reader inputStream, int startline, int startcolumn) {
        return new SimpleCharStream(inputStream, startline, startcolumn, 4096);
    }

    static SimpleCharStream createStream(Reader inputStream) {
        return new SimpleCharStream(inputStream, 1, 1, 4096);
    }

    static SimpleCharStream createStream(InputStream inputStream, int startline, int startcolumn, int buffersize) {
        return new SimpleCharStream(new InputStreamReader(inputStream), startline, startcolumn, 4096);
    }

    static SimpleCharStream createStream(InputStream inputStream, int startline, int startcolumn) {
        return new SimpleCharStream(new InputStreamReader(inputStream), startline, startcolumn, 4096);
    }

    static SimpleCharStream createStream(InputStream inputStream) {
        return new SimpleCharStream(new InputStreamReader(inputStream), 1, 1, 4096);
    }

    @Test
    public void testStaticFlag() {
        assertTrue(SimpleCharStream.staticFlag);
    }

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test"), 1, 1, 1024);
        stream.ExpandBuff(false);
        assertEquals(2048, stream.bufsize);
        assertEquals(2048, stream.available);
        assertEquals(0, stream.tokenBegin);
        assertEquals(0, stream.bufpos);
        assertArrayEquals(new int[2048], stream.bufline);
        assertArrayEquals(new int[2048], stream.bufcolumn);
        stream.ExpandBuff(true);
        assertEquals(4096, stream.bufsize);
        assertEquals(4096, stream.available);
        assertEquals(0, stream.tokenBegin);
        assertEquals(0, stream.bufpos);
        assertArrayEquals(new int[4096], stream.bufline);
        assertArrayEquals(new int[4096], stream.bufcolumn);
    }

    @Test(expected = Throwable.class)
    public void testExpandBuffThrowable() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test"), 1, 1, 1024);
        stream.ExpandBuff(false);
        stream.ExpandBuff(false);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test"), 1, 1, 1024);
        assertFalse(stream.available > 0);
        assertFalse(stream.maxNextCharInd > 0);
        stream.FillBuff();
        assertTrue(stream.available > 0);
        assertTrue(stream.maxNextCharInd > 0);
        assertEquals('t', stream.readChar());
        assertEquals('e', stream.readChar());
        assertEquals('s', stream.readChar());
        assertEquals('t', stream.readChar());
        assertEquals('\n', stream.readChar());
        assertEquals(-1, stream.readChar());
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test\ntest2\n"), 1, 1, 1024);
        assertEquals('\n', stream.BeginToken());
        assertEquals('t', stream.readChar());
        assertEquals('e', stream.readChar());
        assertEquals('s', stream.readChar());
        assertEquals('\n', stream.readChar());
        assertEquals('t', stream.readChar());
        assertEquals('e', stream.readChar());
        assertEquals('s', stream.readChar());
        assertEquals('\n', stream.readChar());
        assertEquals(-1, stream.readChar());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test\r\ntest2\n"), 1, 1, 1024);
        stream.UpdateLineColumn('\r');
        stream.UpdateLineColumn('\n');
        stream.UpdateLineColumn('t');
        stream.UpdateLineColumn('e');
        stream.UpdateLineColumn('s');
        assertEquals(2, stream.line);
        assertEquals(7, stream.column);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test"), 1, 1, 1024);
        assertEquals('t', stream.readChar());
        assertEquals('e', stream.readChar());
        assertEquals('s', stream.readChar());
        assertEquals('\n', stream.readChar());
        assertEquals(-1, stream.readChar());
    }

    @Test
    public void testGetColumn() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test\r\ntest2\n"), 1, 1, 1024);
        assertEquals(1, stream.getColumn());
        assertEquals(8, stream.getColumn());
        assertEquals(14, stream.getColumn());
        assertEquals(15, stream.getColumn());
    }

    @Test
    public void testGetLine() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test\r\ntest2\n"), 1, 1, 1024);
        assertEquals(1, stream.getLine());
        assertEquals(2, stream.getLine());
    }

    @Test
    public void testGetEndColumn() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test\r\ntest2\n"), 1, 1, 1024);
        assertEquals(8, stream.getEndColumn());
        assertEquals(15, stream.getEndColumn());
    }

    @Test
    public void testGetEndLine() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test\r\ntest2\n"), 1, 1, 1024);
        assertEquals(2, stream.getEndLine());
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test\r\ntest2\n"), 1, 1, 1024);
        assertEquals(1, stream.getBeginColumn());
        assertEquals(1, stream.getBeginColumn());
    }

    @Test
    public void testGetBeginLine() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test\r\ntest2\n"), 1, 1, 1024);
        assertEquals(1, stream.getBeginLine());
        assertEquals(1, stream.getBeginLine());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test"), 1, 1, 1024);
        assertEquals('t', stream.readChar());
        assertEquals('e', stream.readChar());
        assertEquals('s', stream.readChar());
        assertEquals('\n', stream.readChar());
        stream.backup(3);
        assertEquals('s', stream.readChar());
        assertEquals('t', stream.readChar());
        assertEquals('e', stream.readChar());
        stream.backup(4);
        assertEquals('t', stream.readChar());
        assertEquals('\n', stream.readChar());
        assertEquals(-1, stream.readChar());
    }

    @Test
    public void testConstructor() {
        Reader inputStream = new StringReader("test");
        SimpleCharStream stream = createStream(inputStream, 1, 1, 1024);
        assertNotNull(stream.inputStream);
        assertNotNull(stream.buffer);
        assertNotNull(stream.bufline);
        assertNotNull(stream.bufcolumn);
        assertEquals(1, stream.line);
        assertEquals(1, stream.column);
        assertEquals(1024, stream.bufsize);
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test"), 1, 1, 1024);
        stream.ReInit(new StringReader("test2"), 2, 2, 1024);
        assertEquals(2, stream.line);
        assertEquals(2, stream.column);
        assertEquals(1024, stream.bufsize);
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test"), 1, 1, 1024);
        assertEquals("test", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("testtest"), 1, 1, 1024);
        char[] suffix = SimpleCharStream.GetSuffix(5);
        assertArrayEquals(new char[]{'t', 'e', 's', 't'}, suffix);
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test"), 1, 1, 1024);
        SimpleCharStream.Done();
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream stream = createStream(new StringReader("test1\r\ntest2\ntest3\r\ntest4"), 1, 1, 1024);
        SimpleCharStream.adjustBeginLineColumn(2, 5);
        assertEquals(2, stream.line);
        assertEquals(5, stream.column);
    }
}