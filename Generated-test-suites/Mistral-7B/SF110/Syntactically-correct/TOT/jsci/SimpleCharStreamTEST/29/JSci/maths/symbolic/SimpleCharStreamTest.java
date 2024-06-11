package JSci.maths.symbolic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimpleCharStreamTest {

    private static final String TEST_FILE_CONTENTS = "Test1\n" +
            "Line 1: Column 1\tTab1\r\n" +
            "Test2\r\n" +
            "Line 2: Column 1\tTab2\n" +
            "Test3\n";
    private static final int TEST_FILE_LENGTH = TEST_FILE_CONTENTS.length();

    private SimpleCharStream charStream;
    private int beginLine;
    private int beginColumn;

    @Before
    public void setUp() throws IOException {
        charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_FILE_CONTENTS.getBytes()));
        beginLine = charStream.getBeginLine();
        beginColumn = charStream.getBeginColumn();
    }

    @After
    public void tearDown() {
        SimpleCharStream.Done();
    }

    @Test
    public void testBeginToken() throws IOException {
        char testChar = charStream.BeginToken();
        int line = charStream.getLine();
        int column = charStream.getColumn();

        assertEquals('T', testChar);
        assertEquals(1, line);
        assertEquals(1, column);
    }

    @Test
    public void testReadChar() throws IOException {
        char testChar;

        // Read valid characters
        testChar = charStream.readChar();
        assertEquals('T', testChar);

        testChar = charStream.readChar();
        assertEquals('\n', testChar);

        testChar = charStream.readChar();
        assertEquals('L', testChar);

        // Read invalid characters
        testChar = charStream.readChar();
        assertNull(testChar);

        // Test reading past EOF
        testChar = charStream.readChar();
        assertNull(testChar);
    }

    @Test
    public void testGetLineColumn() throws IOException {
        int line, column;

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(1, line);
        assertEquals(1, column);

        testChar = charStream.readChar(); // Read 'T'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(1, line);
        assertEquals(2, column);

        testChar = charStream.readChar(); // Read '\n'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(2, line);
        assertEquals(1, column);

        testChar = charStream.readChar(); // Read 'T'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(2, line);
        assertEquals(2, column);

        testChar = charStream.readChar(); // Read '\r'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(2, line);
        assertEquals(3, column);

        testChar = charStream.readChar(); // Read '\n'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(1, column);

        testChar = charStream.readChar(); // Read 'T'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(2, column);

        testChar = charStream.readChar(); // Read 'e'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(3, column);

        testChar = charStream.readChar(); // Read 's'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(4, column);

        testChar = charStream.readChar(); // Read 't'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(5, column);

        testChar = charStream.readChar(); // Read '2'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(6, column);

        testChar = charStream.readChar(); // Read ':'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(7, column);

        testChar = charStream.readChar(); // Read 'C'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(8, column);

        testChar = charStream.readChar(); // Read 'o'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(9, column);

        testChar = charStream.readChar(); // Read 'l'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(10, column);

        testChar = charStream.readChar(); // Read 'u'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(4, line);
        assertEquals(1, column);

        testChar = charStream.readChar(); // Read '\n'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(4, line);
        assertEquals(1, column);

        // Test reading past EOF
        testChar = charStream.readChar();
        assertNull(testChar);
    }

    @Test
    public void testGetBeginLineColumn() throws IOException {
        assertEquals(1, charStream.getBeginLine());
        assertEquals(1, charStream.getBeginColumn());
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        // Test adjusting beginning of file
        charStream.adjustBeginLineColumn(2, 2);

        assertEquals(2, charStream.getBeginLine());
        assertEquals(2, charStream.getBeginColumn());

        int line = charStream.getLine();
        int column = charStream.getColumn();

        assertEquals(2, line);
        assertEquals(1, column);

        char testChar = charStream.readChar();

        assertEquals('T', testChar);

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(2, line);
        assertEquals(2, column);

        testChar = charStream.readChar(); // Read '\n'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(2, line);
        assertEquals(1, column);

        testChar = charStream.readChar(); // Read 'T'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(2, line);
        assertEquals(2, column);

        testChar = charStream.readChar(); // Read '\r'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(2, line);
        assertEquals(3, column);

        testChar = charStream.readChar(); // Read '\n'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(1, column);

        testChar = charStream.readChar(); // Read 'T'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(2, column);

        testChar = charStream.readChar(); // Read 'e'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(3, column);

        testChar = charStream.readChar(); // Read 's'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(4, column);

        testChar = charStream.readChar(); // Read 't'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(5, column);

        testChar = charStream.readChar(); // Read '2'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(6, column);

        testChar = charStream.readChar(); // Read ':'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(7, column);

        testChar = charStream.readChar(); // Read 'C'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(8, column);

        testChar = charStream.readChar(); // Read 'o'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(9, column);

        testChar = charStream.readChar(); // Read 'l'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(10, column);

        testChar = charStream.readChar(); // Read 'u'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(4, line);
        assertEquals(1, column);

        testChar = charStream.readChar(); // Read '\n'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(4, line);
        assertEquals(1, column);

        // Test adjusting middle of file
        charStream.adjustBeginLineColumn(4, 5);

        assertEquals(4, charStream.getBeginLine());
        assertEquals(5, charStream.getBeginColumn());

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(3, line);
        assertEquals(6, column);

        testChar = charStream.readChar(); // Read ':'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(4, line);
        assertEquals(5, column);

        testChar = charStream.readChar(); // Read 'C'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(4, line);
        assertEquals(6, column);

        testChar = charStream.readChar(); // Read 'o'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(4, line);
        assertEquals(7, column);

        testChar = charStream.readChar(); // Read 'l'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(4, line);
        assertEquals(8, column);

        testChar = charStream.readChar(); // Read 'u'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(4, line);
        assertEquals(9, column);

        testChar = charStream.readChar(); // Read ':'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(4, line);
        assertEquals(10, column);

        testChar = charStream.readChar(); // Read 'F'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(5, line);
        assertEquals(1, column);

        testChar = charStream.readChar(); // Read '\n'

        line = charStream.getLine();
        column = charStream.getColumn();

        assertEquals(5, line);
        assertEquals(1, column);

        // Test adjusting end of file
        charStream.adjustBeginLineColumn(6, 12);

        assertEquals(6, charStream.getBeginLine());
        assertEquals(12, charStream.getBeginColumn());

        testChar = charStream.readChar();
        assertNull(testChar);
    }
}