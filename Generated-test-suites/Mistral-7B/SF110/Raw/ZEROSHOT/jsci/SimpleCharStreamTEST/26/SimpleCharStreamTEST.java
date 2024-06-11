import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.*;

public class SimpleCharStreamTest {

    private static final String TEST_FILE = "SimpleCharStreamTest.java";
    private static final String TEST_INPUT = "SimpleCharStreamTestInput.txt";

    private Reader inputStream;
    private SimpleCharStream charStream;

    @Before
    public void setUp() throws IOException {
        inputStream = new FileReader(TEST_INPUT);
        charStream = new SimpleCharStream(inputStream, 1, 1, 4096);
    }

    @After
    public void tearDown() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
        charStream.Done();
    }

    @org.junit.Test
    public void testExpandBuff() {
        charStream.ExpandBuff(false);
        assertEquals(4096 + 2048, charStream.bufsize);
    }

    @org.junit.Test
    public void testFillBuff() throws IOException {
        charStream.FillBuff();
        char c = charStream.readChar();
        assertEquals('\u0000', c); // EOF character

        charStream.ExpandBuff(false);
        charStream.FillBuff();
        c = charStream.readChar();
        assertNotEquals('\u0000', c);
    }

    @org.junit.Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        assertNotEquals('\u0000', c);
    }

    @org.junit.Test
    public void testUpdateLineColumn() throws IOException {
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        int line = charStream.getLine();
        int column = charStream.getColumn();
        assertEquals(1, line);
        assertEquals(1, column);

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        line = charStream.getLine();
        column = charStream.getColumn();
        assertEquals(1, line);
        assertEquals(2, column);

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        line = charStream.getLine();
        column = charStream.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);

        c = '\r';
        charStream.UpdateLineColumn(c);
        line = charStream.getLine();
        column = charStream.getColumn();
        assertEquals(2, line);
        assertEquals(1, column);

        c = '\n';
        charStream.UpdateLineColumn(c);
        line = charStream.getLine();
        column = charStream.getColumn();
        assertEquals(2, line);
        assertEquals(1, column);
    }

    @org.junit.Test
    public void testReadChar() throws IOException {
        char c = charStream.readChar();
        assertNotEquals('\u0000', c);

        c = charStream.readChar();
        assertNotEquals(c, charStream.readChar());
    }

    @org.junit.Test
    public void testGetLineColumn() throws IOException {
        int line = charStream.getLine();
        int column = charStream.getColumn();
        assertEquals(1, line);
        assertEquals(1, column);

        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        line = charStream.getLine();
        column = charStream.getColumn();
        assertEquals(1, line);
        assertEquals(2, column);

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        line = charStream.getLine();
        column = charStream.getColumn();
        assertEquals(1, line);
        assertEquals(3, column);

        c = '\r';
        charStream.UpdateLineColumn(c);
        line = charStream.getLine();
        column = charStream.getColumn();
        assertEquals(2, line);
        assertEquals(1, column);

        c = '\n';
        charStream.UpdateLineColumn(c);
        line = charStream.getLine();
        column = charStream.getColumn();
        assertEquals(2, line);
        assertEquals(1, column);
    }

    @org.junit.Test
    public void testBackup() throws IOException {
        char c = charStream.readChar();
        charStream.backup(1);
        c = charStream.readChar();
        assertEquals(c, charStream.readChar());

        charStream.backup(10);
        assertEquals(c, charStream.readChar());

        charStream.backup(100);
        assertEquals(c, charStream.readChar());
    }

    @org.junit.Test
    public void testGetImage() throws IOException {
        String image = charStream.GetImage();
        assertFalse(image.isEmpty());
    }

    @org.junit.Test
    public void testGetSuffix() throws IOException {
        String suffix = charStream.GetSuffix(10);
        assertFalse(suffix.isEmpty());
    }

    @org.junit.Test
    public void testAdjustBeginLineColumn() throws IOException {
        charStream.adjustBeginLineColumn(2, 2);
        int line = charStream.getBeginLine();
        int column = charStream.getBeginColumn();
        assertEquals(2, line);
        assertEquals(2, column);
    }
}