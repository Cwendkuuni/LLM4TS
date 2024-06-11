package JSci.maths.symbolic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 1024);

        for (int i = 0; i < 11; i++) {
            scs.readChar();
        }

        scs.ExpandBuff(false);

        for (int i = 0; i < 11; i++) {
            assertEquals((char) ('t' + i), scs.readChar());
        }

        assertEquals(12, scs.available);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 1024);

        for (int i = 0; i < 10; i++) {
            scs.readChar();
        }

        scs.FillBuff();

        for (int i = 0; i < 11; i++) {
            assertEquals((char) ('t' + i), scs.readChar());
        }

        assertEquals(12, scs.available);
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 1024);

        assertEquals('\r', scs.BeginToken());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        assertEquals('\n', scs.BeginToken());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());

        for (int i = 0; i < 5; i++) {
            assertEquals((char) ('t' + i), scs.readChar());
        }

        assertEquals(5, scs.getColumn());
        assertEquals(1, scs.getLine());

        assertEquals('\r', scs.BeginToken());
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        assertEquals('\n', scs.BeginToken());
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        for (int i = 0; i < 5; i++) {
            assertEquals((char) ('t' + i), scs.readChar());
        }

        assertEquals(6, scs.getColumn());
        assertEquals(2, scs.getLine());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\r\nbar".getBytes()), 1, 1, 1024);

        for (int i = 0; i < 4; i++) {
            assertEquals((char) ('t' + i), scs.readChar());
        }

        assertEquals(4, scs.getColumn());
        assertEquals(1, scs.getLine());

        assertEquals('\r', scs.readChar());

        assertEquals(5, scs.getColumn());
        assertEquals(2, scs.getLine());

        assertEquals('\n', scs.readChar());

        assertEquals(1, scs.getColumn());
        assertEquals(2, scs.getLine());

        for (int i = 0; i < 3; i++) {
            assertEquals((char) ('f' + i), scs.readChar());
        }

        assertEquals(3, scs.getColumn());
        assertEquals(2, scs.getLine());

        assertEquals('\r', scs.readChar());

        assertEquals(4, scs.getColumn());
        assertEquals(3, scs.getLine());

        assertEquals('\n', scs.readChar());

        assertEquals(1, scs.getColumn());
        assertEquals(3, scs.getLine());

        for (int i = 0; i < 3; i++) {
            assertEquals((char) ('b' + i), scs.readChar());
        }

        assertEquals(4, scs.getColumn());
        assertEquals(3, scs.getLine());

        assertEquals('\r', scs.readChar());

        assertEquals(5, scs.getColumn());
        assertEquals(4, scs.getLine());

        assertEquals('\n', scs.readChar());

        assertEquals(1, scs.getColumn());
        assertEquals(4, scs.getLine());

        for (int i = 0; i < 3; i++) {
            assertEquals((char) ('b' + i), scs.readChar());
        }

        assertEquals(5, scs.getColumn());
        assertEquals(4, scs.getLine());

        assertNull(scs.readChar());
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\nfoo\r\nbar".getBytes()), 1, 1, 1024);

        for (int i = 0; i < 7; i++) {
            scs.readChar();
        }

        scs.ReInit(new ByteArrayInputStream("test2".getBytes()), 1, 1);

        for (int i = 0; i < 4; i++) {
            assertEquals((char) ('t' + i), scs.readChar());
        }

        assertEquals(4, scs.getColumn());
        assertEquals(1, scs.getLine());

        scs.ReInit(new ByteArrayInputStream("test3\r\n".getBytes()), 2, 1);

        assertEquals('\r', scs.readChar());

        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());

        assertEquals('\n', scs.readChar());

        assertEquals(1, scs.getColumn());
        assertEquals(2, scs.getLine());

        for (int i = 0; i < 3; i++) {
            assertEquals((char) ('t' + i), scs.readChar());
        }

        assertEquals(3, scs.getColumn());
        assertEquals(2, scs.getLine());
    }
}