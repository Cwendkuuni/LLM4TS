package JSci.maths.symbolic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class SimpleCharStreamTest {

    private SimpleCharStream simpleCharStream;
    private int startline = 1;
    private int startcolumn = 1;
    private int buffersize = 4096;
    private Reader inputStream;

    @Before
    public void setUp() throws IOException {
        inputStream = new ByteArrayInputStream("Hello World!".getBytes());
        simpleCharStream = new SimpleCharStream(inputStream, startline, startcolumn, buffersize);
    }

    @After
    public void tearDown() {
        if (simpleCharStream != null) {
            simpleCharStream.Done();
        }
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = simpleCharStream.BeginToken();
        assertEquals('H', c);
        c = simpleCharStream.BeginToken();
        assertEquals('H', c);
        c = simpleCharStream.BeginToken();
        assertEquals('e', c);
        c = simpleCharStream.BeginToken();
        assertEquals('l', c);
        // Test error scenario
        try {
            simpleCharStream.BeginToken();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("End of input reached!"));
        }
    }

    @Test
    public void testReadChar() throws IOException {
        char c = simpleCharStream.readChar();
        assertEquals('H', c);
        c = simpleCharStream.readChar();
        assertEquals('e', c);
        c = simpleCharStream.readChar();
        assertEquals('l', c);
        // Test edge cases
        simpleCharStream.backup(2);
        c = simpleCharStream.readChar();
        assertEquals('l', c);
        // Test error scenario
        try {
            simpleCharStream.readChar();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("End of input reached!"));
        }
    }

    @Test
    public void testGetColumn() throws IOException {
        int column = simpleCharStream.getColumn();
        assertEquals(1, column);
        simpleCharStream.backup(1);
        column = simpleCharStream.getColumn();
        assertEquals(2, column);
        simpleCharStream.readChar();
        simpleCharStream.backup(1);
        column = simpleCharStream.getColumn();
        assertEquals(3, column);
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        column = simpleCharStream.getColumn();
        assertEquals(7, column);
        simpleCharStream.backup(6);
        column = simpleCharStream.getColumn();
        assertEquals(1, column);
        simpleCharStream.readChar();
        column = simpleCharStream.getColumn();
        assertEquals(2, column);
        // Test error scenario
        try {
            simpleCharStream.getColumn();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("End of input reached!"));
        }
    }

    @Test
    public void testGetLine() throws IOException {
        int line = simpleCharStream.getLine();
        assertEquals(1, line);
        simpleCharStream.backup(8);
        line = simpleCharStream.getLine();
        assertEquals(1, line);
        simpleCharStream.readChar();
        line = simpleCharStream.getLine();
        assertEquals(1, line);
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        line = simpleCharStream.getLine();
        assertEquals(2, line);
        // Test error scenario
        try {
            simpleCharStream.getLine();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("End of input reached!"));
        }
    }

    @Test
    public void testGetEndLine() throws IOException {
        int line = simpleCharStream.getEndLine();
        assertEquals(1, line);
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        line = simpleCharStream.getEndLine();
        assertEquals(2, line);
        // Test error scenario
        try {
            simpleCharStream.getEndLine();
        } catch (IOException e) {
            assertTrue(e.getMessage().contain("End of input reached!"));
        }
    }

    @Test
    public void testGetBeginLine() throws IOException {
        int line = simpleCharStream.getBeginLine();
        assertEquals(1, line);
        simpleCharStream.backup(11);
        line = simpleCharStream.getBeginLine();
        assertEquals(1, line);
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        line = simpleCharStream.getBeginLine();
        assertEquals(1, line);
        // Test error scenario
        try {
            simpleCharStream.getBeginLine();
        } catch (IOException e) {
            assertTrue(e.getMessage().contain("End of input reached!"));
        }
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        int column = simpleCharStream.getBeginColumn();
        assertEquals(1, column);
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        column = simpleCharStream.getBeginColumn();
        assertEquals(1, column);
        simpleCharStream.readChar();
        column = simpleCharStream.getBeginColumn();
        assertEquals(8, column);
        // Test error scenario
        try {
            simpleCharStream.getBeginColumn();
        } catch (IOException e) {
            assertTrue(e.getMessage().contain("End of input reached!"));
        }
    }

    @Test
    public void testBackup() throws IOException {
        simpleCharStream.backup(2);
        int column = simpleCharStream.getColumn();
        assertEquals(3, column);
        int line = simpleCharStream.getLine();
        assertEquals(1, line);
        simpleCharStream.readChar();
        column = simpleCharStream.getColumn();
        assertEquals(4, column);
        line = simpleCharStream.getLine();
        assertEquals(1, line);
        simpleCharStream.backup(1);
        column = simpleCharStream.getColumn();
        assertEquals(3, column);
        line = simpleCharStream.getLine();
        assertEquals(1, line);
        simpleCharStream.backup(6);
        column = simpleCharStream.getColumn();
        assertEquals(1, column);
        line = simpleCharStream.getLine();
        assertEquals(1, line);
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        column = simpleCharStream.getColumn();
        assertEquals(17, column);
        line = simpleCharStream.getLine();
        assertEquals(2, line);
        simpleCharStream.backup(17);
        column = simpleCharStream.getColumn();
        assertEquals(1, column);
        line = simpleCharStream.getLine();
        assertEquals(1, line);
        // Test error scenario
        try {
            simpleCharStream.backup(100);
        } catch (IOException e) {
            assertTrue(e.getMessage().contain("End of input reached!"));
        }
    }

    @Test
    public void testReInit() throws IOException {
        simpleCharStream.ReInit(inputStream, startline, startcolumn, buffersize);
        char c = simpleCharStream.readChar();
        assertEquals('H', c);
        c = simpleCharStream.readChar();
        assertEquals('e', c);
        // Test edge case
        simpleCharStream.backup(13);
        simpleCharStream.readChar();
        simpleCharStream.backup(2);
        simpleCharStream.readChar();
        simpleCharStream.backup(13);
        simpleCharStream.readChar();
        simpleCharStream.backup(1);
        simpleCharStream.readChar();
        int line = simpleCharStream.getLine();
        assertEquals(2, line);
        int column = simpleCharStream.getColumn();
        assertEquals(2, column);
        // Test error scenario
        try {
            simpleCharStream.reInit(null, 1, 1);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Second call to the constructor of a static SimpleCharStream"));
        }
    }
}