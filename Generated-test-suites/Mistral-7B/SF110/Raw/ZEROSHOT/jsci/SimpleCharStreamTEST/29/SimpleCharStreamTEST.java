package jsic.test.maths.symbolic;

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

public class SimpleCharStreamTest {

    private static final String TEST_DATA = "test\nline 1\ncolumn 1\r\ftab column 2\nline 2\n";
    private static final int TEST_START_LINE = 1;
    private static final int TEST_START_COLUMN = 1;
    private static final int TEST_BUFFER_SIZE = 1024;

    private SimpleCharStream charStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream(TEST_DATA.getBytes());
        charStream = new SimpleCharStream(inputStream, TEST_START_LINE, TEST_START_COLUMN, TEST_BUFFER_SIZE);
    }

    @After
    public void tearDown() {
        if (charStream != null) {
            charStream.Done();
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(charStream);
        assertEquals(TEST_START_LINE, charStream.getBeginLine());
        assertEquals(TEST_START_COLUMN, charStream.getBeginColumn());
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        assertEquals('\n', c);
        assertEquals(0, charStream.getBeginLine());
        assertEquals(1, charStream.getBeginColumn());
        assertFalse(charStream.prevCharIsCR);
        assertFalse(charStream.prevCharIsLF);
    }

    @Test
    public void testReadChar() throws IOException {
        char c;
        for (int i = 0; i < TEST_DATA.length(); ++i) {
            c = charStream.readChar();
            UpdateCharStreamState(charStream, c);
            assertEquals(TEST_DATA.charAt(i), c);
        }
        assertEquals(-1, charStream.readChar());
    }

    @Test
    public void testGetLineColumn() {
        assertEquals(TEST_START_LINE, charStream.getLine());
        assertEquals(TEST_START_COLUMN, charStream.getColumn());
        for (int i = 0; i < TEST_DATA.length(); ++i) {
            char c = TEST_DATA.charAt(i);
            UpdateCharStreamState(charStream, c);
            assertEquals(TEST_START_LINE, charStream.getLine());
            assertEquals(i + 1, charStream.getColumn());
        }
        assertEquals(TEST_START_LINE + 1, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testGetEndLineColumn() {
        assertEquals(TEST_START_LINE, charStream.getLine());
        assertEquals(TEST_START_COLUMN, charStream.getColumn());
        for (int i = 0; i < TEST_DATA.length(); ++i) {
            char c = TEST_DATA.charAt(i);
            UpdateCharStreamState(charStream, c);
        }
        assertEquals(TEST_START_LINE + 1, charStream.getLine());
        assertEquals(TEST_DATA.length() + TEST_START_COLUMN, charStream.getEndColumn());
        assertEquals(TEST_START_LINE + 1, charStream.getEndLine());
    }

    @Test
    public void testBackup() throws IOException {
        char c;
        for (int i = 0; i < TEST_DATA.length(); ++i) {
            c = charStream.readChar();
            UpdateCharStreamState(charStream, c);
        }
        char[] backup = new char[3];
        charStream.backup(2);
        assertEquals(TEST_DATA.charAt(TEST_DATA.length() - 3), charStream.readChar());
        assertEquals(TEST_DATA.charAt(TEST_DATA.length() - 2), charStream.readChar());
        assertEquals(TEST_DATA.charAt(TEST_DATA.length() - 1), charStream.readChar());
        charStream.backup(backup.length);
        assertArrayEquals(backup, charStream.buffer, 0, backup.length);
    }

    @Test
    public void testReInit() throws IOException {
        char c;
        for (int i = 0; i < TEST_DATA.length(); ++i) {
            c = charStream.readChar();
            UpdateCharStreamState(charStream, c);
        }
        charStream.ReInit(inputStream, TEST_START_LINE, TEST_START_COLUMN, TEST_BUFFER_SIZE);
        c = charStream.readChar();
        UpdateCharStreamState(charStream, c);
        assertEquals(TEST_DATA.charAt(0), charStream.readChar());
    }

    @Test
    public void testFillBuff() throws IOException {
        char c = '\0';
        SimpleCharStream charStream2 = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, TEST_BUFFER_SIZE);
        charStream2.fillBuff();
        assertEquals(0, charStream2.available);
        assertEquals(0, charStream2.maxNextCharInd);

        charStream = new SimpleCharStream(inputStream, TEST_START_LINE, TEST_START_COLUMN, TEST_BUFFER_SIZE);
        charStream.fillBuff();
        assertEquals(TEST_DATA.length(), charStream.available);

        charStream.fillBuff();
        assertEquals(TEST_DATA.length(), charStream.available);

        charStream.fillBuff();
        assertEquals(TEST_DATA.length(), charStream.available);

        charStream.ExpandBuff(false);
        charStream.fillBuff();
        assertEquals(TEST_DATA.length() * 2, charStream.available);
    }

    @Test
    public void testGetImage() {
        char[] image = SimpleCharStream.GetImage();
        assertNotNull(image);
        assertEquals(TEST_DATA.length(), image.length);
        assertArrayEquals(TEST_DATA.getBytes(), image);
    }

    @Test
    public void testGetSuffix() {
        char[] suffix = SimpleCharStream.GetSuffix(TEST_DATA.length());
        assertNotNull(suffix);
        assertEquals(TEST_DATA.length(), suffix.length);
        assertArrayEquals(TEST_DATA.getBytes(), suffix);

        suffix = SimpleCharStream.GetSuffix(5);
        assertNotNull(suffix);
        assertEquals(5, suffix.length);
        assertArrayEquals(" \r\ftab".toCharArray(), suffix);
    }

    @Test
    public void testDone() {
        char stream = TEST_DATA.charAt(0);
        charStream.Done();
        assertNull(charStream.buffer);
        assertNull(charStream.bufline);
        assertNull(charStream.bufcolumn);
        assertNull(charStream.inputStream);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        char[] expected = "test\nline 2\ncolumn 2\n".toCharArray();
        SimpleCharStream charStream3 = new SimpleCharStream(inputStream, TEST_START_LINE, TEST_START_COLUMN, TEST_BUFFER_SIZE);
        charStream3.fillBuff();
        charStream3.adjustBeginLineColumn(TEST_START_LINE + 1, TEST_START_COLUMN + 1);
        char[] actual = SimpleCharStream.GetImage();
        assertArrayEquals(expected, actual);
    }

    private void UpdateCharStreamState(SimpleCharStream charStream, char c) {
        final int line = charStream.getLine();
        final int column = charStream.getColumn();
        charStream.UpdateLineColumn(c);
        charStream.prevCharIsCR = c == '\r';
        charStream.prevCharIsLF = c == '\n';
    }
}