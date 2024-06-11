package JSci.maths.symbolic.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import JSci.maths.symbolic.SimpleCharStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    private static final String TEST_INPUT = "Hello\nWorld\n!\t";
    private static final int TEST_START_LINE = 1;
    private static final int TEST_START_COLUMN = 1;
    private static final int TEST_BUFFER_SIZE = 10;

    private SimpleCharStream charStream;

    @Before
    public void setUp() {
        try {
            charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_INPUT.getBytes()), TEST_START_LINE, TEST_START_COLUMN, TEST_BUFFER_SIZE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() {
        SimpleCharStream.Done();
    }

    @Test
    public void testBeginToken() throws IOException {
        assertEquals('\u0068', charStream.BeginToken()); // H character code
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testExpandBuff() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_INPUT.getBytes()), TEST_START_LINE, TEST_START_COLUMN, TEST_BUFFER_SIZE);
        char c;
        for (int i = 0; i < TEST_INPUT.length(); ++i) {
            c = charStream.readChar();
            UpdateTestStream(charStream, c);
            assertEquals(TEST_INPUT.charAt(i), c);
        }
        assertThrows(IOException.class, () -> charStream.readChar()); // Test reading past the end of the stream

        SimpleCharStream.ExpandBuff(false);
        c = charStream.readChar();
        UpdateTestStream(charStream, c);
        assertEquals('\n', c); // Test reading the newline character after expanding the buffer
    }

    @Test(expected = IOException.class)
    public void testExpandBuffWithIOException() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_INPUT.getBytes()), TEST_START_LINE, TEST_START_COLUMN, TEST_BUFFER_SIZE);
        SimpleCharStream.ExpandBuff(true); // Intentionally throw an IOException
        char c = charStream.readChar();
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_INPUT.getBytes()), TEST_START_LINE, TEST_START_COLUMN, TEST_BUFFER_SIZE);
        char c;
        for (int i = 0; i < TEST_INPUT.length(); ++i) {
            c = charStream.readChar();
            UpdateTestStream(charStream, c);
            assertEquals(TEST_INPUT.charAt(i), c);
        }
        assertThrows(IOException.class, () -> charStream.readChar()); // Test reading past the end of the stream

        charStream.FillBuff();
        c = charStream.readChar();
        UpdateTestStream(charStream, c);
        assertEquals('\n', c); // Test reading the newline character after filling the buffer
    }

    @Test
    public void testGetColumn() throws IOException {
        assertEquals(1, charStream.getColumn());
        char c = charStream.readChar();
        UpdateTestStream(charStream, c);
        assertEquals(2, charStream.getColumn());
        charStream.backup(1);
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testGetEndColumn() throws IOException {
        assertEquals(1, charStream.getEndColumn());
        char c = charStream.readChar();
        UpdateTestStream(charStream, c);
        assertEquals(2, charStream.getEndColumn());
        charStream.backup(1);
        assertEquals(1, charStream.getEndColumn());
    }

    @Test
    public void testGetEndLine() throws IOException {
        assertEquals(1, charStream.getEndLine());
        char c = charStream.readChar();
        UpdateTestStream(charStream, c);
        assertEquals(2, charStream.getEndLine());
        charStream.backup(1);
        assertEquals(1, charStream.getEndLine());
    }

    @Test
    public void testGetLine() throws IOException {
        assertEquals(1, charStream.getLine());
        char c = charStream.readChar();
        UpdateTestStream(charStream, c);
        assertEquals(2, charStream.getLine());
    }

    @Test
    public void testGetSuffix() throws IOException {
        assertEquals(0, SimpleCharStream.getSuffix(0).length);
        char c = charStream.readChar();
        UpdateTestStream(charStream, c);
        char[] suffix = SimpleCharStream.GetSuffix(3);
        assertEquals(3, suffix.length);
        assertEquals('r', suffix[0]);
        assertEquals('o', suffix[1]);
        assertEquals('l', suffix[2]);
    }

    @Test
    public void testReadChar() throws IOException {
        char c;
        for (int i = 0; i < TEST_INPUT.length() - 1; ++i) {
            c = charStream.readChar();
            UpdateTestStream(charStream, c);
        }
        assertEquals('\n', charStream.readChar());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char c = charStream.readChar();
        UpdateTestStream(charStream, c);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        UpdateTestStream(charStream, c);
        assertEquals(1, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        char cr = '\r';
        char lf = '\n';
        SimpleCharStream.UpdateLineColumn(cr);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        SimpleCharStream.UpdateLineColumn(lf);
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    private void UpdateTestStream(SimpleCharStream charStream, char c) throws IOException {
        charStream.UpdateLineColumn(c);
        charStream.bufline[charStream.bufpos] = charStream.line;
        charStream.bufcolumn[charStream.bufpos] = charStream.column;
    }
}