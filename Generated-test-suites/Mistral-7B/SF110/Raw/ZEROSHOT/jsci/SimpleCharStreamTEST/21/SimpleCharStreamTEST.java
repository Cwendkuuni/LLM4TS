package jsci.maths.symbolic.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    private SimpleCharStream charStream;
    private static final String TEST_CHARS = "abcd\r\nefgh\nijkl\nmno\r\nopqr\n";
    private static final String TEST_SUFFIX = "pqr";
    private static final int TEST_START_LINE = 1;
    private static final int TEST_START_COLUMN = 1;
    private static final int TEST_BUFFER_SIZE = 1024;

    @Before
    public void setUp() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_CHARS.getBytes());
        charStream = new SimpleCharStream(inputStream, TEST_START_LINE, TEST_START_COLUMN, TEST_BUFFER_SIZE);
    }

    @After
    public void tearDown() {
        SimpleCharStream.Done();
        charStream = null;
    }

    @Test
    public void testExpandBuff() {
        charStream.ExpandBuff(false);
        assertEquals(SimpleCharStream.bufsize + 2048, SimpleCharStream.bufsize);
        assertEquals(SimpleCharStream.available, SimpleCharStream.bufsize);
        assertEquals(SimpleCharStream.tokenBegin, 0);

        charStream.ExpandBuff(true);
        assertEquals(SimpleCharStream.bufsize + 2048, SimpleCharStream.bufsize * 2);
        assertEquals(SimpleCharStream.available, SimpleCharStream.bufsize * 2);
        assertEquals(SimpleCharStream.tokenBegin, 0);
    }

    @Test
    public void testFillBuff() throws IOException {
        charStream.FillBuff();
        assertEquals(SimpleCharStream.bufpos, 0);
        assertEquals(SimpleCharStream.maxNextCharInd, SimpleCharStream.available - 1);
        assertEquals(SimpleCharStream.available, TEST_BUFFER_SIZE);

        char c = charStream.readChar();
        assertEquals(c, 'a');
        assertEquals(SimpleCharStream.bufpos, 1);
        assertEquals(SimpleCharStream.maxNextCharInd, TEST_BUFFER_SIZE - 1);

        for (int i = 0; i < TEST_CHARS.length(); ++i) {
            c = charStream.readChar();
            assertEquals(c, TEST_CHARS.charAt(i));
            ++i;
        }

        assertFalse(charStream.hasNextChar());
        assertFalse(charStream.hasNextLine());
        assertEquals(SimpleCharStream.bufpos, TEST_CHARS.length() + 1);
    }

    @Test(expected = IOException.class)
    public void testFillBuffIOException() throws IOException {
        MockInputStream inputStream = new MockInputStream();
        charStream = new SimpleCharStream(inputStream, TEST_START_LINE, TEST_START_COLUMN, TEST_BUFFER_SIZE);
        charStream.FillBuff();
        charStream.readChar();

        inputStream.throwIOException();
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        assertEquals(c, 'a');

        c = charStream.BeginToken();
        assertEquals(c, 'b');

        charStream.backup(1);
        c = charStream.BeginToken();
        assertEquals(c, 'a');
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char c = charStream.BeginToken();
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE);
        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN);

        c = charStream.readChar();
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE);
        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN + 1);

        c = '\r';
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);
        assertEquals(SimpleCharStream.getColumn(), 1);

        c = '\n';
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);
        assertEquals(SimpleCharStream.getColumn(), 1);

        c = '\t';
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getColumn(), 2);
    }

    @Test
    public void testReadChar() throws IOException {
        char c = charStream.BeginToken();
        assertEquals(c, 'a');

        c = charStream.readChar();
        assertEquals(c, 'a');
        assertEquals(SimpleCharStream.getColumn(), 2);

        c = charStream.readChar();
        assertEquals(c, 'b');
        assertEquals(SimpleCharStream.getColumn(), 3);

        c = charStream.readChar();
        assertEquals(c, 'c');
        assertEquals(SimpleCharStream.getColumn(), 4);

        c = charStream.readChar();
        assertEquals(c, '\r');
        assertEquals(SimpleCharStream.getColumn(), 5);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);

        c = charStream.readChar();
        assertEquals(c, '\n');
        assertEquals(SimpleCharStream.getColumn(), 1);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);

        c = charStream.readChar();
        assertEquals(c, 'e');
        assertEquals(SimpleCharStream.getColumn(), 2);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);
    }

    @Test
    public void testGetLineColumn() throws IOException {
        char c = charStream.BeginToken();
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE);
        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN);

        c = charStream.readChar();
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE);
        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN + 1);

        c = '\r';
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);
        assertEquals(SimpleCharStream.getColumn(), 1);

        c = '\n';
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);
        assertEquals(SimpleCharStream.getColumn(), 1);

        c = '\t';
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getColumn(), 2);

        c = charStream.readChar();
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);
        assertEquals(SimpleCharStream.getColumn(), 3);

        c = charStream.readChar();
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);
        assertEquals(SimpleCharStream.getColumn(), 4);

        c = '\r';
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 2);
        assertEquals(SimpleCharStream.getColumn(), 1);

        c = '\n';
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 2);
        assertEquals(SimpleCharStream.getColumn(), 1);

        c = charStream.readChar();
        UpdateLineColumn(c);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 2);
        assertEquals(SimpleCharStream.getColumn(), 2);
    }

    @Test
    public void testGetEndLineColumn() throws IOException {
        char c = charStream.BeginToken();
        UpdateLineColumn(c);
        UpdateLineColumn('\r');
        UpdateLineColumn('\n');

        c = charStream.readChar();
        UpdateLineColumn(c);

        c = charStream.readChar();
        UpdateLineColumn(c);

        c = charStream.readChar();
        UpdateLineColumn(c);

        c = '\r';
        UpdateLineColumn(c);

        c = '\n';
        UpdateLineColumn(c);

        c = charStream.readChar();
        UpdateLineColumn(c);

        c = charStream.readChar();
        UpdateLineColumn(c);

        c = charStream.readChar();
        UpdateLineColumn(c);

        c = '\r';
        UpdateLineColumn(c);

        c = '\n';
        UpdateLineColumn(c);

        c = charStream.readChar();
        UpdateLineColumn(c);

        c = charStream.readChar();
        UpdateLineColumn(c);

        c = charStream.readChar();
        UpdateLineColumn(c);

        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 3);
        assertEquals(SimpleCharStream.getEndLine(), TEST_START_LINE + 4);
        assertEquals(SimpleCharStream.getBeginColumn(), TEST_START_COLUMN);
        assertEquals(SimpleCharStream.getEndColumn(), TEST_START_COLUMN + 11);
    }

    @Test
    public void testBackup() throws IOException {
        char c = charStream.BeginToken();
        UpdateLineColumn(c);
        UpdateLineColumn('\r');
        UpdateLineColumn('\n');

        c = charStream.readChar();
        UpdateLineColumn(c);

        char prevChar = charStream.readChar();
        UpdateLineColumn(prevChar);

        charStream.backup(1);

        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN + 1);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);

        assertEquals(prevChar, charStream.readChar());
        UpdateLineColumn(prevChar);

        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN + 2);
        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);
    }

    @Test
    public void testGetImage() throws IOException {
        char c = charStream.BeginToken();
        UpdateLineColumn(c);
        UpdateLineColumn('\r');
        UpdateLineColumn('\n');

        c = charStream.readChar();
        UpdateLineColumn(c);

        char prevChar = charStream.readChar();
        UpdateLineColumn(prevChar);

        charStream.backup(1);

        assertEquals(SimpleCharStream.GetImage(), "ab");

        c = charStream.readChar();
        UpdateLineColumn(c);

        assertEquals(SimpleCharStream.GetImage(), "abc");

        charStream.backup(2);

        assertEquals(SimpleCharStream.GetImage(), "a");
    }

    @Test
    public void testGetSuffix() throws IOException {
        char c = charStream.BeginToken();
        UpdateLineColumn(c);
        UpdateLineColumn('\r');
        UpdateLineColumn('\n');

        c = charStream.readChar();
        UpdateLineColumn(c);

        char prevChar = charStream.readChar();
        UpdateLineColumn(prevChar);

        charStream.backup(1);

        assertEquals(new String(SimpleCharStream.GetSuffix(3)), "pqr");

        c = charStream.readChar();
        UpdateLineColumn(c);

        assertEquals(new String(SimpleCharStream.GetSuffix(3)), "opq");

        charStream.backup(2);

        assertEquals(new String(SimpleCharStream.GetSuffix(3)), "no\r");
    }

    @Test
    public void testDone() throws IOException {
        char c = charStream.BeginToken();
        UpdateLineColumn(c);
        UpdateLineColumn('\r');
        UpdateLineColumn('\n');

        c = charStream.readChar();
        UpdateLineColumn(c);

        char prevChar = charStream.readChar();
        UpdateLineColumn(prevChar);

        charStream.backup(1);

        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 1);
        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN + 1);

        SimpleCharStream.Done();

        assertNull(charStream.inputStream);
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        char c = charStream.BeginToken();
        UpdateLineColumn(c);
        UpdateLineColumn('\r');
        UpdateLineColumn('\n');

        c = charStream.readChar();
        UpdateLineColumn(c);

        char prevChar = charStream.readChar();
        UpdateLineColumn(prevChar);

        charStream.backup(1);

        SimpleCharStream.adjustBeginLineColumn(TEST_START_LINE + 2, TEST_START_COLUMN + 2);

        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 2);
        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN + 4);

        charStream.backup(1);

        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 2);
        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN + 4);

        c = charStream.readChar();
        UpdateLineColumn(c);

        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 2);
        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN + 5);

        charStream.backup(1);

        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 2);
        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN + 5);

        charStream.backup(TEST_CHARS.length());

        assertEquals(SimpleCharStream.getLine(), TEST_START_LINE + 2);
        assertEquals(SimpleCharStream.getColumn(), TEST_START_COLUMN);
    }
}

class MockInputStream extends InputStream {
    private boolean throwIOException = false;
    private int read = 0;

    @Override
    public int read() throws IOException {
        if (throwIOException) {
            throw new IOException();
        }
        return read == -1 ? -1 : read++;
    }

    public void throwIOException() {
        throwIOException = true;
    }
}