import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    private static final String testInput = "Hello\nWorld\nThis is a test.";

    private SimpleCharStream charStream;

    @Before
    public void setUp() {
        charStream = new SimpleCharStream(new ByteArrayInputStream(testInput.getBytes()), 1, 1);
    }

    @After
    public void tearDown() throws IOException {
        charStream.Done();
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        assertEquals('H', c);
        assertEquals(-1, charStream.getBeginLine());
        assertEquals(1, charStream.getBeginColumn());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char c = charStream.readChar();
        char nextC = charStream.readChar();
        charStream.UpdateLineColumn(c);
        charStream.UpdateLineColumn(nextC);
        assertEquals(2, charStream.getLine());
        assertEquals(3, charStream.getColumn());
        assertEquals('\n', c);
        assertEquals('W', nextC);
    }

    @Test
    public void testGetLineColumn() throws IOException {
        char c = charStream.readChar();
        char nextC = charStream.readChar();
        charStream.UpdateLineColumn(c);
        charStream.UpdateLineColumn(nextC);
        assertEquals(2, charStream.getLine());
        assertEquals(3, charStream.getColumn());
        assertEquals(2, charStream.getEndLine());
        assertEquals(7, charStream.getEndColumn());
    }

    @Test
    public void testBackup() throws IOException {
        char c = charStream.readChar();
        char nextC = charStream.readChar();
        charStream.UpdateLineColumn(c);
        charStream.UpdateLineColumn(nextC);
        charStream.backup(1);
        assertEquals(1, charStream.getLine());
        assertEquals(3, charStream.getColumn());
        charStream.backup(2);
        assertEquals(1, charStream.getLine());
        assertEquals(2, charStream.getColumn());
    }

    @Test(expected = IOException.class)
    public void testEmptyBuffer() throws IOException {
        charStream.readChar();
        charStream.readChar();
        charStream.readChar();
    }

    @Test
    public void testFillBuff() throws IOException {
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        charStream.FillBuff();
        char nextC = charStream.readChar();
        charStream.UpdateLineColumn(nextC);
        assertEquals(2, charStream.getLine());
        assertEquals(4, charStream.getColumn());
        assertEquals('\n', c);
        assertEquals('W', nextC);
    }

    @Test
    public void testExpandBuff() throws IOException {
        for (int i = 0; i < 10000; i++) {
            char c = charStream.readChar();
            charStream.UpdateLineColumn(c);
        }
        charStream.ExpandBuff(false);
        char nextC = charStream.readChar();
        charStream.UpdateLineColumn(nextC);
        assertEquals(10002, charStream.getLine());
        assertEquals(10004, charStream.getColumn());
    }
}