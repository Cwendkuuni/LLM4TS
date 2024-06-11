package JSci.maths.symbolic;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test".getBytes()), 1, 1, 8);
        assertEquals(8, SimpleCharStream.bufsize);

        for (int i = 0; i < 15; i++) {
            scs.readChar();
        }

        scs.ExpandBuff(false);
        assertEquals(128, SimpleCharStream.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test".getBytes()), 1, 1, 8);

        for (int i = 0; i < 15; i++) {
            scs.readChar();
        }

        scs.FillBuff();
        assertEquals('T', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());
        assertEquals('\u0000', scs.readChar()); // End of Stream
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\n".getBytes()), 1, 1, 8);

        assertNull(scs.BeginToken());
        assertEquals('T', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals('e', scs.readChar());
        assertEquals('s', scs.readChar());

        char token = scs.BeginToken();
        assertEquals('T', token);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals(1, scs.getBeginLine());
        assertEquals(1, scs.getBeginColumn());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nTest\r\nTest".getBytes()), 1, 1, 8);

        for (int i = 0; i < 15; i++) {
            scs.readChar();
        }

        assertEquals('\r', scs.readChar());
        assertEquals('\n', scs.readChar());

        UpdateLineColumn(scs, '\t');
        assertEquals(1, scs.getLine());
        assertEquals(9, scs.getColumn());

        UpdateLineColumn(scs, '\r');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        UpdateLineColumn(scs, '\n');
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        UpdateLineColumn(scs, 'T');
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nTest\r\nTest".getBytes()), 1, 1, 8);

        for (int i = 0; i < 15; i++) {
            scs.readChar();
        }

        assertEquals("Test\r\nTest\r\nTest", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nTest\r\nTestTest".getBytes()), 1, 1, 8);

        for (int i = 0; i < 15; i++) {
            scs.readChar();
        }

        assertEquals("estTest", SimpleCharStream.GetSuffix(6));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nTest\r\nTestTest".getBytes()), 1, 1, 8);

        for (int i = 0; i < 15; i++) {
            scs.readChar();
        }

        SimpleCharStream.Done();

        try {
            scs.readChar();
            fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\rTest\nTest".getBytes()), 1, 1, 8);

        for (int i = 0; i < 36; i++) {
            scs.readChar();
        }

        SimpleCharStream.adjustBeginLineColumn(3, 1);

        assertEquals(3, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals(3, scs.getBeginLine());
        assertEquals(1, scs.getBeginColumn());

        SimpleCharStream.adjustBeginLineColumn(5, 2);

        assertEquals(5, scs.getLine());
        assertEquals(2, scs.getColumn());
        assertEquals(5, scs.getBeginLine());
        assertEquals(2, scs.getBeginColumn());
    }

    private static void UpdateLineColumn(SimpleCharStream scs, char c) throws IOException {
        char token = scs.BeginToken();

        scs.UpdateLineColumn(c);

        assertEquals(token, scs.readChar());
    }
}