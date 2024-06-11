package jscc.test.SimpleCharStream;

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

    private SimpleCharStream simpleCharStream;
    private static final String TEST_CHARS = "Hello\rWorld\nTest\r\n";

    @Before
    public void setUp() throws Exception {
        simpleCharStream = new SimpleCharStream(new ByteArrayInputStream(TEST_CHARS.getBytes()));
    }

    @After
    public void tearDown() throws Exception {
        simpleCharStream.Done();
    }

    @Test
    public void testConstructor() {
        assertNotNull(simpleCharStream);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullStream() {
        new SimpleCharStream(null);
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = simpleCharStream.BeginToken();
        assertEquals('H', c);
    }

    @Test
    public void testReadChar() throws IOException {
        char c;
        for (int i = 0; i < TEST_CHARS.length(); ++i) {
            c = simpleCharStream.readChar();
            assertEquals(TEST_CHARS.charAt(i), c);
        }
        assertFalse(simpleCharStream.hasNextChar());
    }

    @Test
    public void testGetLineColumn() throws IOException {
        int line, column;
        for (int i = 0; i < TEST_CHARS.length() - 1; ++i) {
            c = simpleCharStream.readChar();
            line = simpleCharStream.getLine();
            column = simpleCharStream.getColumn();
            assertEquals(1, line);
            assertEquals(i + 1, column);
        }
        line = simpleCharStream.getLine();
        column = simpleCharStream.getColumn();
        assertEquals(1, line);
        assertEquals(TEST_CHARS.length(), column);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.UpdateLineColumn('\r');
        simpleCharStream.UpdateLineColumn('\n');
        assertEquals(2, simpleCharStream.getLine());
        assertEquals(1, simpleCharStream.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.backup(2);
        assertEquals('H', simpleCharStream.readChar());
        assertEquals('H', simpleCharStream.readChar());
    }

    @Test
    public void testGetEndLineColumn() throws IOException {
        int endLine, endColumn;
        for (int i = 0; i < TEST_CHARS.length() - 1; ++i) {
            c = simpleCharStream.readChar();
            line = simpleCharStream.getLine();
            column = simpleCharStream.getColumn();
            endLine = simpleCharStream.getEndLine();
            endColumn = simpleCharStream.getEndColumn();
            assertEquals(1, line);
            assertEquals(i + 1, column);
            assertEquals(i + 2, endColumn);
            assertEquals(i + 1, endLine);
        }
        line = simpleCharStream.getEndLine();
        column = simpleCharStream.getEndColumn();
        endLine = simpleCharStream.getEndLine();
        endColumn = simpleCharStream.getEndColumn();
        assertEquals(1, line);
        assertEquals(TEST_CHARS.length(), column);
        assertEquals(TEST_CHARS.length(), endColumn);
        assertEquals(TEST_CHARS.length(), endLine);
    }

    @Test
    public void testReInit() throws IOException {
        int lineOld = simpleCharStream.getLine();
        int columnOld = simpleCharStream.getColumn();
        simpleCharStream.ReInit(simpleCharStream.inputStream, 1, 1);
        assertEquals(lineOld, simpleCharStream.getLine());
        assertEquals(columnOld, simpleCharStream.getColumn());
    }

    @Test
    public void testHasNextChar() throws IOException {
        assertTrue(simpleCharStream.hasNextChar());
        simpleCharStream.readChar();
        assertFalse(simpleCharStream.hasNextChar());
    }

    @Test
    public void testGetImage() throws IOException {
        String image = simpleCharStream.GetImage();
        assertEquals(TEST_CHARS, image);
    }

    @Test
    public void testGetSuffix() throws IOException {
        char[] suffix = simpleCharStream.GetSuffix(5);
        assertEquals("Test\r\n", new String(suffix));
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        int newLine = 2, newCol = 1;
        simpleCharStream.adjustBeginLineColumn(newLine, newCol);
        assertEquals(2, simpleCharStream.getBeginLine());
        assertEquals(1, simpleCharStream.getBeginColumn());
    }
}