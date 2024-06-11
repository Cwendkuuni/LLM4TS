package JSci.maths.symbolic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    private SimpleCharStream charStream;

    @Before
    public void setUp() {
        charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r12345".getBytes()), 1, 1, 4096);
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        Assert.assertEquals('t', c);
        int line = charStream.getLine();
        int column = charStream.getColumn();
        Assert.assertEquals(1, line);
        Assert.assertEquals(2, column);
    }

    @Test
    public void testReadChar() throws IOException {
        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        int line = charStream.getLine();
        int column = charStream.getColumn();
        Assert.assertEquals(1, line);
        Assert.assertEquals(3, column);

        c = charStream.readChar();
        Assert.assertEquals('\r', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(4, column);

        c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(5, column);

        c = charStream.readChar();
        Assert.assertEquals('e', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(6, column);

        c = charStream.readChar();
        Assert.assertEquals('s', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(7, column);

        c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(8, column);

        c = charStream.readChar();
        Assert.assertEquals(' ', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(9, column);

        c = charStream.readChar();
        Assert.assertEquals('1', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(10, column);

        c = charStream.readChar();
        Assert.assertEquals('2', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(11, column);

        c = charStream.readChar();
        Assert.assertEquals('3', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(12, column);

        c = charStream.readChar();
        Assert.assertEquals('4', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(13, column);

        c = charStream.readChar();
        Assert.assertEquals('5', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(14, column);

        c = charStream.readChar();
        Assert.assertEquals('\n', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(15, column);

        c = charStream.readChar();
        Assert.assertEquals('\r', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(16, column);

        c = charStream.readChar();
        Assert.assertEquals('\n', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(17, column);

        try {
            charStream.readChar();
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testGetLineAndColumn() throws IOException {
        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('\r', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(5, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('e', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(6, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('s', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(7, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(8, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals(' ', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(9, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('1', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(10, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('2', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(11, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('3', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(12, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('4', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(13, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('5', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(14, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('\n', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(15, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('\r', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(16, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('\n', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(17, charStream.getColumn());

        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(18, charStream.getColumn());

        try {
            charStream.readChar();
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        charStream.UpdateLineColumn('e');
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(3, charStream.getColumn());

        charStream.UpdateLineColumn('s');
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(4, charStream.getColumn());

        charStream.UpdateLineColumn('t');
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(5, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(6, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        char c2 = charStream.readChar();
        Assert.assertEquals('1', c2);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        charStream.UpdateLineColumn('2');
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(3, charStream.getColumn());

        charStream.UpdateLineColumn('3');
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(4, charStream.getColumn());

        charStream.UpdateLineColumn('4');
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(5, charStream.getColumn());

        charStream.UpdateLineColumn('5');
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(6, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        Assert.assertEquals(3, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        Assert.assertEquals(3, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        char c3 = charStream.readChar();
        Assert.assertEquals('\r', c3);
        Assert.assertEquals(3, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        Assert.assertEquals(3, charStream.getLine());
        Assert.assertEquals(3, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        Assert.assertEquals(4, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        Assert.assertEquals(4, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        char c4 = charStream.readChar();
        Assert.assertEquals('\0', c4);
        Assert.assertEquals(4, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        try {
            charStream.readChar();
            Assert.fail();
        } catch (IOException e) {
            // expected
        }
    }
}