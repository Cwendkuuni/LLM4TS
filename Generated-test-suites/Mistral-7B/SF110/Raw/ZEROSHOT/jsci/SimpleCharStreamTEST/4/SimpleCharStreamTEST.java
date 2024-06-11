package jsci.maths.symbolic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testSimpleCharStream() throws IOException {
        final String testInput = "Hello\nWorld!";
        final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testInput.getBytes());

        SimpleCharStream charStream = new SimpleCharStream(testInputStream, 1, 1, 2048);

        assertNotNull(charStream.buffer);
        assertNotNull(charStream.bufline);
        assertNotNull(charStream.bufcolumn);
        assertEquals(1, charStream.line);
        assertEquals(1, charStream.column);
        assertEquals(1, charStream.getBeginLine());
        assertEquals(1, charStream.getBeginColumn());
        assertEquals(1, charStream.getColumn());
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getEndColumn());
        assertEquals(1, charStream.getEndLine());

        char c = charStream.BeginToken();
        assertEquals('\u0068', c); // H

        c = charStream.readChar();
        assertEquals('\u0068', c); // H
        assertEquals(1, charStream.getColumn());
        assertEquals(1, charStream.getLine());
        UpdateLineColumnAndColumn(charStream, c);

        c = charStream.readChar();
        assertEquals('\u0065', c); // E
        assertEquals(2, charStream.getColumn());
        assertEquals(1, charStream.getLine());
        UpdateLineColumnAndColumn(charStream, c);

        c = charStream.readChar();
        assertEquals('\n', c); // LF
        assertEquals(2, charStream.getColumn());
        assertEquals(2, charStream.getLine());
        UpdateLineColumnAndColumn(charStream, c);
        assertTrue(charStream.prevCharIsLF);
        assertFalse(charStream.prevCharIsCR);

        c = charStream.readChar();
        assertEquals('\u0077', c); // W
        assertEquals(3, charStream.getColumn());
        assertEquals(2, charStream.getLine());
        UpdateLineColumnAndColumn(charStream, c);

        c = charStream.readChar();
        assertEquals('\u006f', c); // O
        assertEquals(4, charStream.getColumn());
        assertEquals(2, charStream.getLine());
        UpdateLineColumnAndColumn(charStream, c);

        c = charStream.readChar();
        assertEquals('\u0021', c); // !
        assertEquals(5, charStream.getColumn());
        assertEquals(2, charStream.getLine());
        UpdateLineColumnAndColumn(charStream, c);

        c = charStream.BeginToken();
        assertNull(c);
        assertEquals(5, charStream.getColumn());
        assertEquals(2, charStream.getLine());

        c = charStream.readChar();
        assertEquals('\u0000', c); // EOF
        assertNull(charStream.getImage());
        assertEquals(5, charStream.getColumn());
        assertEquals(2, charStream.getLine());

        SimpleCharStream.Done();
    }

    @Test(expected = Error.class)
    public void testSecondCallToConstructor() throws IOException {
        final String testInput = "Hello\nWorld!";
        final ByteArrayInputStream testInputStream = new ByteArrayInputStream(testInput.getBytes());

        SimpleCharStream charStream = new SimpleCharStream(testInputStream, 1, 1, 2048);
        charStream.ReInit(testInputStream, 1, 1, 2048);
    }

    private void UpdateLineColumnAndColumn(SimpleCharStream charStream, char c) throws IOException {
        charStream.UpdateLineColumn(c);
    }
}