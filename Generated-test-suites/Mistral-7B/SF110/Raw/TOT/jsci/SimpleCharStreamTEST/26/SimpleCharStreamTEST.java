package JSci.maths.symbolic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimpleCharStreamTest {

    private char[] buffer;
    private int bufpos;
    private int line;
    private int column;

    @Before
    public void setUp() {
        buffer = new char[1024];
        SimpleCharStream.bufsize = buffer.length;
        SimpleCharStream.available = buffer.length;
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(buffer), 1, 1);
        charStream.line = 1;
        charStream.column = 0;
        this.bufpos = -1;
        this.line = charStream.getLine();
        this.column = charStream.getColumn();
    }

    @After
    public void tearDown() {
        SimpleCharStream.ReInit(null);
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(buffer), 1, 1);
        char c = charStream.BeginToken();
        assertEquals(-1, charStream.getBeginLine());
        assertEquals(-1, charStream.getBeginColumn());
        assertEquals(c, charStream.readChar());
        assertEquals(charStream.getLine(), 1);
        assertEquals(charStream.getColumn(), 1);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(buffer), 1, 1);
        for (int i = 0; i < 1021; i++) {
            charStream.readChar();
        }

        charStream.FillBuff();

        for (int i = 0; i < 512; i++) {
            assertEquals('\u0000', charStream.readChar());
        }

        charStream.updateLineColumn('\n');
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(buffer), 1, 1);
        charStream.FillBuff();
        charStream.inputStream.close();
        charStream.readChar();
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(buffer), 1, 1);

        for (int i = 0; i < 10; i++) {
            charStream.UpdateLineColumn((char) ('a' + i));
        }

        for (int i = 0; i < 10; i++) {
            assertEquals((char) ('a' + i), charStream.readChar());
            assertEquals(i + 2, charStream.getLine());
            assertEquals(i + 2, charStream.getColumn());
            charStream.UpdateLineColumn('\n');
        }

        assertFalse(charStream.isAtBeginning());
        assertEquals(13, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testGetLineColumn() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(buffer), 1, 1);

        charStream.UpdateLineColumn('a');
        charStream.UpdateLineColumn('b');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('c');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('d');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('e');

        for (int i = 0; i < 9; i++) {
            assertEquals(i + 2, charStream.getLine());
            assertEquals(i + 3, charStream.getColumn());
            charStream.UpdateLineColumn('\n');
        }

        assertEquals(15, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testBackup() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(buffer), 1, 1);

        for (int i = 0; i < 10; i++) {
            char c = (char) ('a' + i);
            charStream.UpdateLineColumn(c);
            charStream.backup(1);
            assertEquals(i, charStream.getColumn());
        }

        charStream.backup(3);
        assertEquals(7, charStream.getColumn());
        assertEquals(1, charStream.getLine());
    }

    @Test
    public void testIsAtBeginning() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(buffer), 1, 1);
        assertFalse(charStream.isAtBeginning());
        charStream.backup(2);
        assertTrue(charStream.isAtBeginning());
    }
}