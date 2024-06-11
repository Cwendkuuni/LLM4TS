package jsici.maths.symbolic.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    private static final String TEST_CHARS = "Hello\rWorld\nTest\t123\r\n456\n";
    private SimpleCharStream charStream;

    @Before
    public void setUp() throws IOException {
        charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_CHARS.getBytes()));
    }

    @After
    public void tearDown() {
        SimpleCharStream.Done();
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        Assert.assertEquals('H', c);
        Assert.assertEquals(-1, charStream.getBeginLine());
        Assert.assertEquals(-1, charStream.getBeginColumn());
        c = charStream.BeginToken();
        Assert.assertEquals('H', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(3, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(3, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(3, charStream.getLine());
        Assert.assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(3, charStream.getLine());
        Assert.assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(4, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(4, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(4, charStream.getLine());
        Assert.assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(4, charStream.getLine());
        Assert.assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(5, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(5, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(5, charStream.getLine());
        Assert.assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(5, charStream.getLine());
        Assert.assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(5, charStream.getLine());
        Assert.assertEquals(5, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(6, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(6, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(6, charStream.getLine());
        Assert.assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(6, charStream.getLine());
        Assert.assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(6, charStream.getLine());
        Assert.assertEquals(5, charStream.getColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        char c;
        for (int i = 0; i < TEST_CHARS.length(); i++) {
            c = charStream.readChar();
            Assert.assertEquals(TEST_CHARS.charAt(i), c);
        }
        Assert.assertEquals(-1, charStream.readChar());
    }

    @Test(expected = IOException.class)
    public void testReadCharIOException() throws IOException {
        charStream.inputStream.close();
        charStream.readChar();
    }

    @Test
    public void testGetLine() throws IOException {
        for (int i = 0; i < 7; i++) {
            char c = charStream.readChar();
            Assert.assertEquals(i + 1, charStream.getLine());
            if (i > 0 && (i % 3 == 0 || i % 3 == 1)) {
                Assert.assertEquals(1, charStream.getColumn());
            } else {
                Assert.assertEquals(i % 3 + 1, charStream.getColumn());
            }
        }
        Assert.assertEquals(-1, charStream.getLine());
        Assert.assertEquals(-1, charStream.getColumn());
    }

    @Test
    public void testGetColumn() throws IOException {
        for (int i = 0; i < TEST_CHARS.length(); i++) {
            char c = charStream.readChar();
            if (i > 0 && (i % 3 == 0 || i % 3 == 1)) {
                Assert.assertEquals(1, charStream.getColumn());
            } else {
                Assert.assertEquals(i % 3 + 1, charStream.getColumn());
            }
        }
        Assert.assertEquals(-1, charStream.getColumn());
    }

    @Test
    public void testGetEndLine() throws IOException {
        for (int i = 0; i < 7; i++) {
            char c = charStream.readChar();
            if (i > 0 && (i % 3 == 0 || i % 3 == 1)) {
                Assert.assertEquals(i / 3 + 1, charStream.getEndLine());
            } else {
                Assert.assertEquals(i / 3, charStream.getEndLine());
            }
        }
        Assert.assertEquals(-1, charStream.getEndLine());
        Assert.assertEquals(-1, charStream.getEndColumn());
    }

    @Test
    public void testGetEndColumn() throws IOException {
        for (int i = 0; i < TEST_CHARS.length(); i++) {
            char c = charStream.readChar();
            if ((i + 1) % 3 == 0) {
                Assert.assertEquals(i % 3 + 1, charStream.getEndColumn());
            } else {
                Assert.assertEquals(i % 3 + 1, charStream.getColumn());
            }
        }
        Assert.assertEquals(-1, charStream.getEndColumn());
        Assert.assertEquals(-1, charStream.getEndLine());
    }

    @Test
    public void testGetBeginLine() throws IOException {
        Assert.assertEquals(1, charStream.getBeginLine());
        Assert.assertEquals(1, charStream.getBeginColumn());
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        Assert.assertEquals(1, charStream.getBeginColumn());
    }

    @Test
    public void testBackup() throws IOException {
        char c = charStream.readChar();
        charStream.Backup(1);
        Assert.assertEquals(TEST_CHARS.charAt(0), c);
        c = charStream.readChar();
        charStream.Backup(3);
        Assert.assertEquals(TEST_CHARS.charAt(0), c);
        charStream.Backup(1);
        Assert.assertEquals(TEST_CHARS.charAt(1), c);
        charStream.Backup(-1);
        Assert.assertEquals(TEST_CHARS.charAt(1), c);
        charStream.Backup(-1);
        Assert.assertEquals(TEST_CHARS.charAt(0), c);
        charStream.Backup(TEST_CHARS.length());
        Assert.assertEquals(-1, charStream.readChar());
    }
}