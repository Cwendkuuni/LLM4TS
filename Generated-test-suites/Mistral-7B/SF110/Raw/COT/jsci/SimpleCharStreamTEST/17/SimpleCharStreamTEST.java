package JSci.maths.symbolic;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {

    

    // Basic tests for constructor methods
    @Test
    public void testSimpleCharStreamConstructorWithReaderAndParameters() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 4096);
        assertNotNull(simpleCharStream);
    }

    @Test
    public void testSimpleCharStreamConstructorWithReader() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"));
        assertNotNull(simpleCharStream);
    }

    @Test
    public void testSimpleCharStreamConstructorWithInputStreamAndParameters() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.InputStreamReader(new java.io.ByteArrayInputStream("Test String".getBytes())), 1, 1, 4096);
        assertNotNull(simpleCharStream);
    }

    @Test
    public void testSimpleCharStreamConstructorWithInputStream() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.InputStreamReader(new java.io.ByteArrayInputStream("Test String".getBytes())));
        assertNotNull(simpleCharStream);
    }

    @Test
    public void testSimpleCharStreamConstructorWithReaderAndStartLineAndStartColumn() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1);
        assertNotNull(simpleCharStream);
        assertEquals(1, simpleCharStream.getBeginLine());
        assertEquals(1, simpleCharStream.getBeginColumn());
    }

    @Test
    public void testSimpleCharStreamConstructorWithReaderAndStartLine() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1);
        assertNotNull(simpleCharStream);
        assertEquals(1, simpleCharStream.getBeginLine());
        assertEquals(0, simpleCharStream.getBeginColumn());
    }

    @Test
    public void testSimpleCharStreamConstructorWithReader() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"));
        assertNotNull(simpleCharStream);
        assertEquals(1, simpleCharStream.getBeginLine());
        assertEquals(0, simpleCharStream.getBeginColumn());
    }

    // Basic tests for methods
    @Test
    public void testBeginToken() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 4096);
        char c = simpleCharStream.BeginToken();
        assertNotNull(c);
    }

    @Test
    public void testReadChar() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 4096);
        char c = simpleCharStream.readChar();
        assertNotNull(c);
    }

    @Test
    public void testGetColumn() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 4096);
        int column = simpleCharStream.getColumn();
        assertEquals(1, column);
    }

    @Test
    public void testGetLine() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 4096);
        int line = simpleCharStream.getLine();
        assertEquals(1, line);
    }

    @Test
    public void testGetEndColumn() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test\nString"), 1, 1, 4096);
        int column = simpleCharStream.getEndColumn();
        assertEquals(7, column);
    }

    @Test
    public void testGetEndLine() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test\nString"), 1, 1, 4096);
        int line = simpleCharStream.getEndLine();
        assertEquals(2, line);
    }

    @Test
    public void testGetBeginColumn() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test\nString"), 1, 1, 4096);
        int column = simpleCharStream.getBeginColumn();
        assertEquals(1, column);
    }

    @Test
    public void testGetBeginLine() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test\nString"), 1, 1, 4096);
        int line = simpleCharStream.getBeginLine();
        assertEquals(1, line);
    }

    @Test
    public void testBackup() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 4096);
        simpleCharStream.backup(2);
        assertEquals(SimpleCharStream.bufpos - 2, simpleCharStream.bufpos);
    }

    // Edge cases and exception handling tests
    @Test(expected = Error.class)
    public void testSimpleCharStreamConstructorWithNullReader() {
        new SimpleCharStream(null, 1, 1, 4096);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleCharStreamConstructorWithNegativeBufferSize() {
        new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, -4096);
    }

    @Test(expected = IOException.class)
    public void testBeginTokenThrowsIOException() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test"), 1, 1, 4096);
        simpleCharStream.inputStream.close();
        simpleCharStream.BeginToken();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testExpandBuffArrayIndexOutOfBoundsException() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 2);
        simpleCharStream.ExpandBuff(true);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testFillBuffArrayIndexOutOfBoundsException() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 2);
        simpleCharStream.FillBuff();
    }

    @Test(expected = IOException.class)
    public void testFillBuffIOException() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 2) {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        simpleCharStream.FillBuff();
    }

    @Test(expected = NullPointerException.class)
    public void testUpdateLineColumnNullCharacter() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 4096);
        simpleCharStream.UpdateLineColumn(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateLineColumnOutOfRangeCharacter() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String\r\nTest"), 1, 1, 4096);
        simpleCharStream.UpdateLineColumn('\u0000');
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetImageArrayIndexOutOfBoundsException() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 4096);
        simpleCharStream.bufpos += 100;
        simpleCharStream.GetImage();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetSuffixArrayIndexOutOfBoundsException() throws Exception {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new java.io.StringReader("Test String"), 1, 1, 4096);
        simpleCharStream.bufpos += 100;
        simpleCharStream.GetSuffix(15);
    }

    
}