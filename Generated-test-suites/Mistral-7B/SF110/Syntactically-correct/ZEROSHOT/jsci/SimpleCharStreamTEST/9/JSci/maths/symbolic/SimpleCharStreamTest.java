package jsici.maths.symbolic.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    private static final String TEST_DATA = "this is a test\r\nfor SimpleCharStream\r\nwith tab and return characters\r\n\t\r\nand some more text.";
    private static final int[] TEST_LINE_COLUMN = {1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};

    private SimpleCharStream simpleCharStream;
    private ByteArrayInputStream testInputStream;

    @Before
    public void setUp() throws IOException {
        testInputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
        simpleCharStream = new SimpleCharStream(testInputStream);
        simpleCharStream.ReInit(testInputStream, 1, 1);
    }

    @After
    public void tearDown() {
        SimpleCharStream.Done();
        testInputStream.close();
        simpleCharStream = null;
        testInputStream = null;
    }

    @Test
    public void testBeginToken() throws IOException {
        char expected = TEST_DATA.charAt(0);
        char actual = simpleCharStream.BeginToken();
        assertEquals(expected, actual);
        assertEquals(-1, simpleCharStream.getBeginLine());
        assertEquals(1, simpleCharStream.getBeginColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        char expected;
        char actual;
        for (int i = 0; i < TEST_DATA.length(); ++i) {
            expected = TEST_DATA.charAt(i);
            actual = simpleCharStream.readChar();
            assertEquals(expected, actual);
            UpdateLineColumn(expected);
        }
        assertEquals('\u0000', simpleCharStream.readChar());
        assertEquals(-1, simpleCharStream.getLine());
        assertEquals(-1, simpleCharStream.getColumn());
    }

    @Test(expected = IOException.class)
    public void testReadCharIOException() throws IOException {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 4096);
        simpleCharStream.BeginToken();
        simpleCharStream.readChar();
    }

    @Test
    public void testGetLine() throws IOException {
        for (int i = 0; i < TEST_DATA.length(); ++i) {
            char c = simpleCharStream.readChar();
            int line = simpleCharStream.getLine();
            int column = simpleCharStream.getColumn();
            assertEquals(TEST_LINE_COLUMN[i], line * 10 + column);
        }
    }

    @Test
    public void testGetColumn() throws IOException {
        for (int i = 0; i < TEST_DATA.length(); ++i) {
            char c = simpleCharStream.readChar();
            int column = simpleCharStream.getColumn();
            assertEquals(TEST_LINE_COLUMN[i] % 10, column);
        }
    }

    @Test
    public void testGetEndLine() throws IOException {
        for (int i = 0; i < TEST_DATA.length(); ++i) {
            char c = simpleCharStream.readChar();
            int line = simpleCharStream.getEndLine();
            int column = simpleCharStream.getEndColumn();
            assertEquals(TEST_LINE_COLUMN[i] / 10, line);
            assertEquals(TEST_LINE_COLUMN[i] % 10, column);
        }
    }

    @Test
    public void testGetEndColumn() throws IOException {
        for (int i = 0; i < TEST_DATA.length(); ++i) {
            char c = simpleCharStream.readChar();
            int column = simpleCharStream.getEndColumn();
            assertEquals(TEST_LINE_COLUMN[i] % 10, column);
        }
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        char c = simpleCharStream.BeginToken();
        int column = simpleCharStream.getBeginColumn();
        assertEquals(1, column);
    }

    @Test
    public void testGetBeginLine() throws IOException {
        char c = simpleCharStream.BeginToken();
        int line = simpleCharStream.getBeginLine();
        assertEquals(1, line);
    }

    @Test
    public void testBackup() throws IOException {
        char c1 = simpleCharStream.readChar();
        char c2 = simpleCharStream.readChar();
        simpleCharStream.backup(1);
        char prevChar = simpleCharStream.readChar();
        assertEquals(c2, prevChar);
        UpdateLineColumn(c1);
        int line = simpleCharStream.getLine();
        int column = simpleCharStream.getColumn();
        assertEquals(TEST_LINE_COLUMN[0], line * 10 + column);
    }

    @Test
    public void testExpandBuff() throws IOException {
        for (int i = 0; i < TEST_DATA.length(); ++i) {
            char c = simpleCharStream.readChar();
            UpdateLineColumn(c);
            if (i % 2048 == 0) {
                simpleCharStream.ExpandBuff(false);
            }
        }
        assertEquals(SimpleCharStream.bufsize, TEST_DATA.length());
    }

    @Test
    public void testFillBuff() throws IOException {
        simpleCharStream.ExpandBuff(false);
        simpleCharStream.FillBuff();
        char c = simpleCharStream.readChar();
        UpdateLineColumn(c);
        int line = simpleCharStream.getLine();
        int column = simpleCharStream.getColumn();
        assertEquals(TEST_LINE_COLUMN[TEST_DATA.length() - 1] / 10, line);
        assertEquals(TEST_LINE_COLUMN[TEST_DATA.length() - 1] % 10, column);
    }

    @Test
    public void testFillBuffWrapAround() throws IOException {
        simpleCharStream.ExpandBuff(true);
        simpleCharStream.FillBuff();
        char c = simpleCharStream.readChar();
        UpdateLineColumn(c);
        int line = simpleCharStream.getLine();
        int column = simpleCharStream.getColumn();
        assertEquals(TEST_LINE_COLUMN[0] / 10, line);
        assertEquals(TEST_LINE_COLUMN[0] % 10, column);
    }

    private void UpdateLineColumn(char c) {
        SimpleCharStream.UpdateLineColumn(c);
    }
}