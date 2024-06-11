package jsci.maths.symbolic.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleCharStreamTest {

    private static final String TEST_CHARS = "Hello\nWorld\r\f!@#$%^&*()_+={[}]:;\"'<>,./?`~";
    private static final String TEST_STRING = "Line 1: Hello\nLine 2: World\nLine 3:\rLine 4: \fLine 5: !@#$%^&*()_+={[}]:;\"'<>,./?`~";
    private static final int TEST_LINE = 1;
    private static final int TEST_COLUMN = 1;

    private SimpleCharStream charStream;
    private char[] buffer;
    private int[] bufline;
    private int[] bufcolumn;

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(TEST_CHARS.getBytes());
        charStream = new SimpleCharStream(inputStream, TEST_LINE, TEST_COLUMN, 4096);
        buffer = new char[TEST_CHARS.length()];
        System.arraycopy(TEST_CHARS.toCharArray(), 0, buffer, 0, TEST_CHARS.length());
        bufline = new int[4096];
        bufcolumn = new int[4096];
    }

    @After
    public void tearDown() {
        SimpleCharStream.Done();
    }

    @Test
    public void testExpandBuff() throws IOException {
        charStream.ExpandBuff(false);
        assertEquals(SimpleCharStream.bufsize, SimpleCharStream.available);
        assertEquals(SimpleCharStream.bufsize - SimpleCharStream.tokenBegin, SimpleCharStream.maxNextCharInd);
        assertEquals(SimpleCharStream.bufsize - SimpleCharStream.tokenBegin, SimpleCharStream.bufpos + 1);

        charStream.ExpandBuff(true);
        assertEquals(2 * SimpleCharStream.bufsize, SimpleCharStream.available);
        assertEquals(SimpleCharStream.bufsize - SimpleCharStream.tokenBegin, SimpleCharStream.maxNextCharInd);
        assertEquals(SimpleCharStream.bufsize * 2 - SimpleCharStream.tokenBegin, SimpleCharStream.bufpos + 1);
    }

    @Test
    public void testFillBuff() throws IOException {
        char c;
        for (int i = 0; i < TEST_CHARS.length(); ++i) {
            c = charStream.readChar();
            assertEquals(TEST_CHARS.charAt(i), c);
            UpdateLineColumn(c);
        }
        assertEquals(TEST_STRING.length(), SimpleCharStream.maxNextCharInd);
        assertArrayEquals(TEST_STRING.getBytes(), SimpleCharStream.buffer, 0, TEST_STRING.length());

        charStream.FillBuff();
        assertEquals(TEST_STRING.length(), SimpleCharStream.maxNextCharInd);
        assertArrayEquals(TEST_STRING.getBytes(), SimpleCharStream.buffer, 0, TEST_STRING.length());
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        assertFalse(charStream.tokenBegin == -1);
        assertEquals(TEST_CHARS.charAt(0), c);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char c = TEST_CHARS.charAt(0);
        charStream.UpdateLineColumn(c);
        assertEquals(TEST_LINE, SimpleCharStream.line);
        assertEquals(TEST_COLUMN, SimpleCharStream.column);

        c = TEST_CHARS.charAt(1);
        charStream.UpdateLineColumn(c);
        assertEquals(TEST_LINE, SimpleCharStream.line);
        assertEquals(TEST_COLUMN + 1, SimpleCharStream.column);

        c = '\n';
        charStream.UpdateLineColumn(c);
        assertEquals(TEST_LINE + 1, SimpleCharStream.line);
        assertEquals(1, SimpleCharStream.column);

        c = '\r';
        charStream.UpdateLineColumn(c);
        assertEquals(TEST_LINE + 1, SimpleCharStream.line);
        assertEquals(0, SimpleCharStream.column);

        c = '\f';
        charStream.UpdateLineColumn(c);
        assertEquals(TEST_LINE + 1, SimpleCharStream.line);
        assertEquals(0, SimpleCharStream.column);

        c = '\t';
        charStream.UpdateLineColumn(c);
        assertEquals(TEST_LINE + 1, SimpleCharStream.line);
        assertEquals(8, SimpleCharStream.column);
    }

    @Test
    public void testGetters() throws IOException {
        char c = TEST_CHARS.charAt(0);
        charStream.UpdateLineColumn(c);

        assertEquals(TEST_LINE, SimpleCharStream.getLine());
        assertEquals(TEST_COLUMN, SimpleCharStream.getColumn());
        assertEquals(TEST_COLUMN, SimpleCharStream.getBeginColumn());
        assertEquals(TEST_LINE, SimpleCharStream.getBeginLine());

        c = TEST_CHARS.charAt(1);
        charStream.UpdateLineColumn(c);

        assertEquals(TEST_LINE + 1, SimpleCharStream.getLine());
        assertEquals(TEST_COLUMN + 1, SimpleCharStream.getColumn());
        assertEquals(TEST_COLUMN, SimpleCharStream.getBeginColumn());
        assertEquals(TEST_LINE, SimpleCharStream.getBeginLine());

        c = '\n';
        charStream.UpdateLineColumn(c);

        assertEquals(TEST_LINE + 1, SimpleCharStream.getLine());
        assertEquals(1, SimpleCharStream.getColumn());
        assertEquals(TEST_COLUMN + 1, SimpleCharStream.getBeginColumn());
        assertEquals(TEST_LINE + 1, SimpleCharStream.getBeginLine());

        c = '\r';
        charStream.UpdateLineColumn(c);

        assertEquals(TEST_LINE + 1, SimpleCharStream.getLine());
        assertEquals(0, SimpleCharStream.getColumn());
        assertEquals(TEST_COLUMN + 1, SimpleCharStream.getBeginColumn());
        assertEquals(TEST_LINE + 1, SimpleCharStream.getBeginLine());

        c = '\f';
        charStream.UpdateLineColumn(c);

        assertEquals(TEST_LINE + 1, SimpleCharStream.getLine());
        assertEquals(0, SimpleCharStream.getColumn());
        assertEquals(TEST_COLUMN + 1, SimpleCharStream.getBeginColumn());
        assertEquals(TEST_LINE + 1, SimpleCharStream.getBeginLine());
    }

    @Test
    public void testBackup() throws IOException {
        char c = TEST_CHARS.charAt(0);
        charStream.UpdateLineColumn(c);

        charStream.backup(1);
        assertEquals(TEST_LINE, SimpleCharStream.line);
        assertEquals(TEST_COLUMN, SimpleCharStream.column);

        charStream.backup(TEST_CHARS.length());
        assertEquals(TEST_LINE, SimpleCharStream.line);
        assertEquals(TEST_COLUMN - TEST_CHARS.length() + 1, SimpleCharStream.column);

        charStream.backup(-TEST_CHARS.length());
        assertEquals(TEST_LINE, SimpleCharStream.line);
        assertEquals(TEST_COLUMN, SimpleCharStream.column);
    }

    @Test
    public void testReInit() throws IOException {
        charStream.ReInit(new ByteArrayInputStream(TEST_CHARS.getBytes()), TEST_LINE, TEST_COLUMN, 4096);
        assertEquals(TEST_STRING.length(), SimpleCharStream.maxNextCharInd);
        assertArrayEquals(TEST_STRING.getBytes(), SimpleCharStream.buffer, 0, TEST_STRING.length());
        assertEquals(TEST_LINE, SimpleCharStream.line);
        assertEquals(TEST_COLUMN, SimpleCharStream.column);
    }

    @Test(expected = Error.class)
    public void testSecondConstructorCall() {
        SimpleCharStream charStream1 = new SimpleCharStream(new ByteArrayInputStream(TEST_CHARS.getBytes()), TEST_LINE, TEST_COLUMN, 4096);
        SimpleCharStream charStream2 = new SimpleCharStream(new ByteArrayInputStream(TEST_CHARS.getBytes()), TEST_LINE, TEST_COLUMN, 4096);
    }

    @Test
    public void testGetImage() {
        charStream.UpdateLineColumn(TEST_CHARS.charAt(0));
        for (int i = 0; i < TEST_CHARS.length(); ++i) {
            charStream.UpdateLineColumn(TEST_CHARS.charAt(i + 1));
        }
        String image = SimpleCharStream.GetImage();
        assertEquals(TEST_STRING, image);
    }

    @Test
    public void testGetSuffix() {
        charStream.UpdateLineColumn(TEST_CHARS.charAt(TEST_CHARS.length() - 1));
        String suffix = SimpleCharStream.GetSuffix(TEST_CHARS.length());
        assertEquals(TEST_CHARS, suffix);
    }

    @Test
    public void testDone() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_CHARS.getBytes()), TEST_LINE, TEST_COLUMN, 4096);
        SimpleCharStream.Done();
        assertNull(charStream.buffer);
        assertNull(charStream.bufline);
        assertNull(charStream.bufcolumn);
    }

    private void UpdateLineColumn(char c) {
        charStream.UpdateLineColumn(c);
    }
}