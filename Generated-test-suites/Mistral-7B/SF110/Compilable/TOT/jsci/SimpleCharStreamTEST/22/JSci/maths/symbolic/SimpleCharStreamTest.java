package JSci.maths.symbolic;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

public class SimpleCharStreamTest {

    private SimpleCharStream charStream;

    @Before
    public void setUp() {
        charStream = new SimpleCharStream(new InputStreamReader(new ByteArrayInputStream("test\ntest1\rtest2".getBytes())));
    }

    @After
    public void tearDown() {
        SimpleCharStream.Done();
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        assertEquals('\t', c);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.BeginToken();
        assertEquals('\n', c);
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.BeginToken();
        assertEquals('\r', c);
        assertEquals(2, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        c = charStream.BeginToken();
        assertEquals('t', c);
        assertEquals(2, charStream.getLine());
        assertEquals(3, charStream.getColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        char c;

        c = charStream.readChar();
        assertEquals('\t', c);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('\n', c);
        assertEquals(1, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('\r', c);
        assertEquals(1, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('t', c);
        assertEquals(1, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('e', c);
        assertEquals(1, charStream.getLine());
        assertEquals(5, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('s', c);
        assertEquals(1, charStream.getLine());
        assertEquals(6, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('t', c);
        assertEquals(1, charStream.getLine());
        assertEquals(7, charStream.getColumn());

        c = charStream.readChar();
        assertEquals('\n', c);
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        assertThrows(IOException.class, () -> charStream.readChar());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char c = '\n';

        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        assertEquals(3, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\t');
        assertEquals(3, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        charStream.UpdateLineColumn(' ');
        assertEquals(3, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        assertEquals(4, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        assertEquals(5, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('a');
        assertEquals(5, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        assertEquals(6, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testGetLineColumn() {
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        assertEquals(3, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\t');
        assertEquals(3, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        charStream.UpdateLineColumn(' ');
        assertEquals(3, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        assertEquals(4, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        assertEquals(5, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('a');
        assertEquals(5, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        assertEquals(6, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        char c = '\n';

        charStream.UpdateLineColumn(c);
        charStream.UpdateLineColumn(c);
        charStream.UpdateLineColumn(c);
        charStream.UpdateLineColumn(c);
        charStream.UpdateLineColumn(c);
        charStream.UpdateLineColumn(c);
        charStream.UpdateLineColumn(c);
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('\t');
        charStream.UpdateLineColumn(' ');
        charStream.UpdateLineColumn('a');
        charStream.UpdateLineColumn('\n');

        charStream.backup(13);
        assertEquals(5, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        charStream.backup(4);
        assertEquals(4, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        charStream.backup(16);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.backup(20);
        assertThrows(IndexOutOfBoundsException.class, () -> charStream.readChar());
    }

    @Test
    public void testReInit() throws IOException {
        char c;

        charStream = new SimpleCharStream(new InputStreamReader(new ByteArrayInputStream("test1\rtest2\ntest3".getBytes())));
        charStream.UpdateLineColumn('\n');
        charStream.UpdateLineColumn('t');

        char c1 = charStream.readChar();
        assertEquals('\n', c1);
        assertEquals(2, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.ReInit(new InputStreamReader(new ByteArrayInputStream("test1\rtest2\ntest3".getBytes())));
        char c2 = charStream.readChar();
        assertEquals('t', c2);
        assertEquals(2, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        char c3 = charStream.readChar();
        assertEquals('e', c3);
        assertEquals(2, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        char c4 = charStream.readChar();
        assertEquals('s', c4);
        assertEquals(2, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        char c5 = charStream.readChar();
        assertEquals('t', c5);
        assertEquals(2, charStream.getLine());
        assertEquals(5, charStream.getColumn());

        char c6 = charStream.readChar();
        assertEquals('\r', c6);
        assertEquals(3, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        char c7 = charStream.readChar();
        assertEquals('\n', c7);
        assertEquals(3, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        char c8 = charStream.readChar();
        assertEquals('t', c8);
        assertEquals(3, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        char c9 = charStream.readChar();
        assertEquals('e', c9);
        assertEquals(3, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        char c10 = charStream.readChar();
        assertEquals('s', c10);
        assertEquals(3, charStream.getLine());
        assertEquals(6, charStream.getColumn());

        char c11 = charStream.readChar();
        assertEquals('t', c11);
        assertEquals(3, charStream.getLine());
        assertEquals(7, charStream.getColumn());

        char c12 = charStream.readChar();
        assertEquals('\n', c12);
        assertEquals(4, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        assertThrows(IOException.class, () -> charStream.readChar());

        charStream.ReInit(new InputStreamReader(new ByteArrayInputStream("test1\rtest2\ntest3".getBytes())));
        char c13 = charStream.readChar();
        assertEquals('t', c13);
        assertEquals(2, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        char c14 = charStream.readChar();
        assertEquals('e', c14);
        assertEquals(2, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        char c15 = charStream.readChar();
        assertEquals('s', c15);
        assertEquals(2, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        char c16 = charStream.readChar();
        assertEquals('t', c16);
        assertEquals(2, charStream.getLine());
        assertEquals(5, charStream.getColumn());

        char c17 = charStream.readChar();
        assertEquals('\r', c17);
        assertEquals(3, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        char c18 = charStream.readChar();
        assertEquals('\n', c18);
        assertEquals(3, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        char c19 = charStream.readChar();
        assertEquals('t', c19);
        assertEquals(3, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        char c20 = charStream.readChar();
        assertEquals('e', c20);
        assertEquals(3, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        char c21 = charStream.readChar();
        assertEquals('s', c21);
        assertEquals(3, charStream.getLine());
        assertEquals(6, charStream.getColumn());

        char c22 = charStream.readChar();
        assertEquals('t', c22);
        assertEquals(3, charStream.getLine());
        assertEquals(7, charStream.getColumn());

        char c23 = charStream.readChar();
        assertEquals('\n', c23);
        assertEquals(4, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        assertThrows(IOException.class, () -> charStream.readChar());

        charStream.ReInit(new InputStreamReader(new ByteArrayInputStream("test1\rtest2\ntest3".getBytes())));
        char c24 = charStream.readChar();
        assertEquals('t', c24);
        assertEquals(2, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        char c25 = charStream.readChar();
        assertEquals('e', c25);
        assertEquals(2, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        char c26 = charStream.readChar();
        assertEquals('s', c26);
        assertEquals(2, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        char c27 = charStream.readChar();
        assertEquals('t', c27);
        assertEquals(2, charStream.getLine());
        assertEquals(5, charStream.getColumn());

        char c28 = charStream.readChar();
        assertEquals('\r', c28);
        assertEquals(3, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        char c29 = charStream.readChar();
        assertEquals('\n', c29);
        assertEquals(3, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        char c30 = charStream.readChar();
        assertEquals('t', c30);
        assertEquals(3, charStream.getLine());
        assertEquals(3, charStream.getColumn());

        char c31 = charStream.readChar();
        assertEquals('e', c31);
        assertEquals(3, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        char c32 = charStream.readChar();
        assertEquals('s', c32);
        assertEquals(3, charStream.getLine());
        assertEquals(6, charStream.getColumn());

        char c33 = charStream.readChar();
        assertEquals('t', c33);
        assertEquals(3, charStream.getLine());
        assertEquals(7, charStream.getColumn());

        char c34 = charStream.readChar();
        assertEquals('\n', c34);
        assertEquals(4, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        assertThrows(IOException.class, () -> charStream.readChar());
    }
}