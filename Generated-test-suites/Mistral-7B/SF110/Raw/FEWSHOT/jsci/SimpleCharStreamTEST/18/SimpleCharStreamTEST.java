package JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleCharStreamTest {

    private static final String TEST_DATA = "Line 1\n" +
                                           "foo bar --qux\n" +
                                           "Line 2\n" +
                                           "baz\tquux\r\n" +
                                           "Line 3\n";

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_DATA.getBytes()), 1, 1, 4096);
        char expected = 'L';
        char actual;
        int line = 1;
        int column = 1;

        actual = charStream.BeginToken();
        assertEquals(expected, actual);
        assertEquals(line, charStream.getLine());
        assertEquals(column, charStream.getColumn());

        // Read next character
        expected = 'f';
        actual = charStream.readChar();
        assertEquals(expected, actual);
        assertEquals(line, charStream.getLine());
        assertEquals(column + 1, charStream.getColumn());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_DATA.getBytes()), 1, 1, 4096);
        char c;
        int line = 1;
        int column = 1;

        c = charStream.readChar();
        UpdateLineColumn(c);
        assertEquals(line, charStream.getLine());
        assertEquals(column + 1, charStream.getColumn());

        c = '\n';
        UpdateLineColumn(c);
        assertEquals(line + 1, charStream.getLine());
        assertEquals(column = 1, charStream.getColumn());

        c = ' ';
        UpdateLineColumn(c);
        assertEquals(line, charStream.getLine());
        assertEquals(column + 1, charStream.getColumn());

        c = '\t';
        UpdateLineColumn(c);
        assertEquals(line, charStream.getLine());
        assertEquals(column += 8 - (column & 0x7), charStream.getColumn());

        c = '\r';
        UpdateLineColumn(c);
        assertTrue(charStream.prevCharIsCR);
        assertEquals(line, charStream.getLine());
        assertEquals(column, charStream.getColumn());
        assertFalse(charStream.prevCharIsLF);

        c = '\n';
        UpdateLineColumn(c);
        assertFalse(charStream.prevCharIsCR);
        assertTrue(charStream.prevCharIsLF);
        assertEquals(line + 1, charStream.getLine());
        assertEquals(column = 1, charStream.getColumn());
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_DATA.getBytes()), 1, 1, 4096);
        char[] expected = TEST_DATA.toCharArray();
        char[] actual = new char[expected.length];
        int available = 0;
        int maxNextCharInd = 0;

        for (int i = 0; i < expected.length; ++i) {
            char c = charStream.BeginToken();
            assertEquals(expected[i], c);
            actual[i] = c;
            ++available;

            if (available >= 2048) {
                charStream.FillBuff();
                assertEquals(available, charStream.available);
                assertArrayEquals(expected, actual, 0, available);
                available -= available;
                maxNextCharInd = charStream.maxNextCharInd;
            }
        }

        assertEquals(expected.length, charStream.available);
        assertArrayEquals(expected, actual, 0, expected.length);

        charStream.FillBuff();
        char[] nextExpected = new char[TEST_DATA.length() - expected.length];
        System.arraycopy(TEST_DATA.toCharArray(), expected.length, nextExpected, 0, nextExpected.length);
        assertArrayEquals(nextExpected, actual, expected.length, nextExpected.length);
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_DATA.getBytes()), 1, 1, 4096);

        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        char c = charStream.readChar();
        UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        c = '\n';
        UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = ' ';
        UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        c = '\t';
        UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(11, charStream.getColumn());

        c = '\r';
        UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(11, charStream.getColumn());
        assertTrue(charStream.prevCharIsCR);

        c = '\n';
        UpdateLineColumn(c);
        assertEquals(3, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.ReInit(new ByteArrayInputStream(TEST_DATA.getBytes()), 3, 1);
        assertEquals(3, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = 'b';
        UpdateLineColumn(c);
        assertEquals(3, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        charStream.ReInit(new ByteArrayInputStream(TEST_DATA.getBytes()), 3, 2);
        assertEquals(3, charStream.getLine());
        assertEquals(3, charStream.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_DATA.getBytes()), 1, 1, 4096);

        char[] expected = TEST_DATA.toCharArray();
        char[] actual = new char[expected.length];

        for (int i = 0; i < expected.length; ++i) {
            char c = charStream.BeginToken();
            assertEquals(expected[i], c);
            actual[i] = c;

            if (i > 0) {
                charStream.backup(1);
                assertEquals(expected[i - 1], charStream.readChar());
            }
        }

        charStream.FillBuff();
        char[] nextExpected = new char[TEST_DATA.length() - expected.length];
        System.arraycopy(TEST_DATA.toCharArray(), expected.length, nextExpected, 0, nextExpected.length);
        assertArrayEquals(nextExpected, actual, expected.length, nextExpected.length);
    }

    @Test(expected = IOException.class)
    public void testExceptionCase() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_DATA.getBytes()), 1, 1, 4096);
        char[] expected = TEST_DATA.toCharArray();
        char[] actual = new char[expected.length];

        char c = charStream.BeginToken();
        assertEquals(expected[0], c);
        actual[0] = c;

        charStream.FillBuff();
        assertArrayEquals(expected, actual, 0, expected.length);

        charStream.inputStream.close();
        c = charStream.BeginToken();
        throw new IOException();
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_DATA.getBytes()), 1, 1, 4096);
        int line = 1;
        int column = 1;

        char[] expected = TEST_DATA.toCharArray();
        char[] actual = new char[expected.length];

        for (int i = 0; i < expected.length; ++i) {
            char c = charStream.BeginToken();
            assertEquals(expected[i], c);
            actual[i] = c;
            UpdateLineColumn(c);
            assertEquals(line, charStream.getLine());
            assertEquals(column, charStream.getColumn());

            if (i > 0) {
                charStream.adjustBeginLineColumn(line + 1, 1);
                assertEquals(line + 1, charStream.getLine());
                assertEquals(column, charStream.getColumn());
            }
        }
    }

    private static void UpdateLineColumn(char c) {
        SimpleCharStream.UpdateLineColumn(c);
    }
}