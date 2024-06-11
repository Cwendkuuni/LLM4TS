package JSci.maths.symbolic; // Import the package name if needed

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    private static final int START_LINE = 1;
    private static final int START_COLUMN = 1;
    private static final int BUFFER_SIZE = 10;

    private SimpleCharStream charStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() throws IOException {
        charStream = new SimpleCharStream(new ByteArrayInputStream("Test String".getBytes()), START_LINE, START_COLUMN, BUFFER_SIZE);
        inputStream = new ByteArrayInputStream("Test String".getBytes());
        charStream.inputStream = inputStream;
    }

    

    // Test BeginToken()
    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        assertEquals('\u0000', c);
        c = charStream.BeginToken();
        assertEquals('T', c);
    }

    // Test UpdateLineColumn(char)
    @Test
    public void testUpdateLineColumn() throws IOException {
        charStream.UpdateLineColumn('T');
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        assertEquals(3, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    // Test readChar()
    @Test
    public void testReadChar() throws IOException {
        char c = charStream.readChar();
        assertEquals('T', c);

        c = charStream.readChar();
        assertEquals('e', c);

        c = charStream.readChar();
        assertEquals('s', c);

        c = charStream.readChar();
        assertEquals('t', c);

        c = charStream.readChar();
        assertEquals('\u0000', c);
    }

    // Test getColumn()
    @Test
    public void testGetColumn() throws IOException {
        assertEquals(1, charStream.getColumn());
        charStream.UpdateLineColumn('T');
        assertEquals(1, charStream.getColumn());
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        assertEquals(1, charStream.getColumn());
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        assertEquals(1, charStream.getColumn());
        charStream.UpdateLineColumn(' ');
        assertEquals(2, charStream.getColumn());
    }

    // Test getLine()
    @Test
    public void testGetLine() throws IOException {
        assertEquals(1, charStream.getLine());
        charStream.UpdateLineColumn('T');
        assertEquals(1, charStream.getLine());
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        assertEquals(2, charStream.getLine());
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        assertEquals(3, charStream.getLine());
    }

    // Test getBeginColumn()
    @Test
    public void testGetBeginColumn() throws IOException {
        assertEquals(1, charStream.getBeginColumn());
        charStream.UpdateLineColumn('T');
        assertEquals(1, charStream.getBeginColumn());
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        assertEquals(1, charStream.getBeginColumn());
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        assertEquals(1, charStream.getBeginColumn());
        charStream.UpdateLineColumn(' ');
        assertEquals(2, charStream.getBeginColumn());
    }

    // Test getBeginLine()
    @Test
    public void testGetBeginLine() throws IOException {
        assertEquals(1, charStream.getBeginLine());
        charStream.UpdateLineColumn('T');
        assertEquals(1, charStream.getBeginLine());
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        assertEquals(1, charStream.getBeginLine());
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        assertEquals(1, charStream.getBeginLine());
    }

    // Test backup(int amount)
    @Test
    public void testBackup() throws IOException {
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');

        charStream.backup(1);
        assertEquals('s', charStream.readChar());
        assertEquals('t', charStream.readChar());

        charStream.backup(2);
        assertEquals('e', charStream.readChar());
        assertEquals('s', charStream.readChar());

        charStream.backup(3);
        assertEquals('T', charStream.readChar());
        assertEquals('e', charStream.readChar());

        charStream.backup(4);
        assertEquals('\n', charStream.readChar());
        assertEquals('T', charStream.readChar());
    }

    // Test ReInit(Reader dstream, int startline, int startcolumn)
    @Test
    public void testReInit() throws IOException {
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');

        charStream.ReInit(inputStream, START_LINE, START_COLUMN);
        assertEquals('\u0000', charStream.readChar());
        assertEquals('T', charStream.readChar());
        assertEquals('e', charStream.readChar());
        assertEquals('s', charStream.readChar());
        assertEquals('t', charStream.readChar());
    }

    // Test ReInit(Reader dstream)
    @Test
    public void testReInitWithNullInputStream() {
        try {
            charStream.ReInit(null);
            fail("Expected IOException");
        } catch (IOException ignored) {
            // Expected
        }
    }

    // Test ReInit(Reader dstream, int startline, int startcolumn, int buffersize)
    @Test
    public void testReInitWithInvalidBuffersize() {
        try {
            charStream.ReInit(inputStream, START_LINE, START_COLUMN, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ignored) {
            // Expected
        }
    }

    // Test GetImage()
    @Test
    public void testGetImage() throws IOException {
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');

        String image = charStream.GetImage();
        assertEquals("Test", image);
    }

    // Test GetSuffix(int len)
    @Test
    public void testGetSuffix() throws IOException {
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');

        char[] suffix = charStream.GetSuffix(3);
        assertEquals("est", new String(suffix));
    }

    // Test Done()
    @Test
    public void testDone() throws IOException {
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');

        charStream.Done();
        try {
            charStream.readChar();
            fail("Expected IOException");
        } catch (IOException ignored) {
            // Expected
        }
    }

    // Test adjustBeginLineColumn(int newLine, int newCol)
    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('T');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');

        charStream.adjustBeginLineColumn(3, 1);

        assertEquals(3, charStream.getBeginLine());
        assertEquals(1, charStream.getBeginColumn());
    }

    
}