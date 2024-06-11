package JSci.maths.symbolic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

public class SimpleCharStreamTest {

    private SimpleCharStream simpleCharStream;
    private int expectedLine, expectedColumn;

    @Before
    public void setUp() {
        simpleCharStream = new SimpleCharStream(new StringReader("test\r\nline1\tcolumn1\nline2\tcolumn2"));
        expectedLine = 1;
        expectedColumn = 1;
    }

    @Test
    public void testBeginToken() throws IOException {
        assertEquals('t', simpleCharStream.BeginToken());
        assertEquals(expectedLine, simpleCharStream.getLine());
        assertEquals(expectedColumn, simpleCharStream.getColumn());

        simpleCharStream.backup(1);
        assertEquals('\r', simpleCharStream.BeginToken());
        assertEquals(expectedLine, simpleCharStream.getLine());
        assertEquals(expectedColumn, simpleCharStream.getColumn());

        simpleCharStream.backup(2);
        assertEquals('\n', simpleCharStream.BeginToken());
        assertEquals(expectedLine + 1, simpleCharStream.getLine());
        assertEquals(1, simpleCharStream.getColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        assertEquals('t', simpleCharStream.readChar());
        assertEquals(expectedLine, simpleCharStream.getLine());
        assertEquals(expectedColumn + 1, simpleCharStream.getColumn());

        assertEquals('\r', simpleCharStream.readChar());
        assertEquals(expectedLine, simpleCharStream.getLine());
        assertEquals(expectedColumn + 1, simpleCharStream.getColumn());

        assertEquals('\n', simpleCharStream.readChar());
        assertEquals(expectedLine + 1, simpleCharStream.getLine());
        assertEquals(1, simpleCharStream.getColumn());

        assertEquals('l', simpleCharStream.readChar());
        assertEquals(expectedLine + 1, simpleCharStream.getLine());
        assertEquals(expectedColumn + 1, simpleCharStream.getColumn());

        assertEquals('\t', simpleCharStream.readChar());
        assertEquals(expectedLine + 1, simpleCharStream.getLine());
        assertEquals(expectedColumn + 2, simpleCharStream.getColumn());

        assertEquals('c', simpleCharStream.readChar()); // Should throw an IOException since no more characters to read
        throw new IOException();
    }

    @Test(expected = IOException.class)
    public void testReadChar_IOException() throws IOException {
        SimpleCharStream simpleCharStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]));
        simpleCharStream.readChar();
    }

    @Test
    public void testGetLineColumn() {
        assertEquals(1, simpleCharStream.getLine());
        assertEquals(1, simpleCharStream.getColumn());

        assertEquals('\r', simpleCharStream.readChar());

        assertEquals(1, simpleCharStream.getLine());
        assertEquals(2, simpleCharStream.getColumn());

        assertEquals('\n', simpleCharStream.readChar());

        assertEquals(2, simpleCharStream.getLine());
        assertEquals(1, simpleCharStream.getColumn());

        assertEquals('l', simpleCharStream.readChar());

        assertEquals(2, simpleCharStream.getLine());
        assertEquals(2, simpleCharStream.getColumn());

        assertEquals('\t', simpleCharStream.readChar());

        assertEquals(2, simpleCharStream.getLine());
        assertEquals(3, simpleCharStream.getColumn());

        assertEquals('c', simpleCharStream.readChar()); // Should throw an IOException since no more characters to read

        assertEquals(2, simpleCharStream.getLine());
        assertEquals(3, simpleCharStream.getColumn());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char testChar;
        int testLine, testColumn;

        simpleCharStream = new SimpleCharStream(new StringReader(""));
        simpleCharStream.UpdateLineColumn('\r');
        assertEquals(1, simpleCharStream.getLine());
        assertEquals(1, simpleCharStream.getColumn());

        testChar = simpleCharStream.readChar();
        simpleCharStream.UpdateLineColumn(testChar);
        testLine = simpleCharStream.getLine();
        testColumn = simpleCharStream.getColumn();
        assertEquals(testLine, simpleCharStream.getLine());
        assertEquals(testColumn, simpleCharStream.getColumn());

        assertEquals('\n', testChar);
        simpleCharStream.UpdateLineColumn('\n');
        testLine = simpleCharStream.getLine();
        testColumn = simpleCharStream.getColumn();
        assertEquals(testLine + 1, simpleCharStream.getLine());
        assertEquals(1, simpleCharStream.getColumn());

        testChar = simpleCharStream.readChar();
        simpleCharStream.UpdateLineColumn(testChar);
        testLine = simpleCharStream.getLine();
        testColumn = simpleCharStream.getColumn();
        assertEquals(testLine, simpleCharStream.getLine());
        assertEquals(testColumn + 1, simpleCharStream.getColumn());

        simpleCharStream.UpdateLineColumn('\r');
        testLine = simpleCharStream.getLine();
        testColumn = simpleCharStream.getColumn();
        assertEquals(testLine + 1, simpleCharStream.getLine());
        assertEquals(1, simpleCharStream.getColumn());
    }

    @After
    public void tearDown() throws Exception {
        simpleCharStream.done();
    }
}