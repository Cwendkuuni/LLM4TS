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
        charStream = new SimpleCharStream(new ByteArrayInputStream("Test String".getBytes()), 1, 1);
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = charStream.BeginToken();
        Assert.assertEquals('T', c);
        int line = charStream.getLine();
        int column = charStream.getColumn();
        Assert.assertEquals(1, line);
        Assert.assertEquals(1, column);
    }

    @Test(expected = IOException.class)
    public void testBeginTokenWithEmptyBuffer() throws IOException {
        char c = charStream.BeginToken();
        Assert.fail();
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char c = charStream.readChar();
        char nextC = charStream.readChar();
        charStream.UpdateLineColumn(c);
        charStream.UpdateLineColumn(nextC);
        int line = charStream.getLine();
        int column = charStream.getColumn();
        Assert.assertEquals(1, line);
        Assert.assertEquals(2, column);
    }

    @Test
    public void testReadChar() throws IOException {
        char c = charStream.readChar();
        Assert.assertEquals('T', c);
        int line = charStream.getLine();
        int column = charStream.getColumn();
        Assert.assertEquals(1, line);
        Assert.assertEquals(2, column);
        c = charStream.readChar();
        Assert.assertEquals('e', c);
        Assert.assertEquals(1, line);
        Assert.assertEquals(3, column);
    }

    @Test(expected = IOException.class)
    public void testReadCharWithEmptyBuffer() throws IOException {
        char c = charStream.readChar();
        Assert.fail();
    }

    @Test
    public void testGetColumn() throws IOException {
        char c = charStream.readChar();
        Assert.assertEquals(2, charStream.getColumn());
        c = charStream.readChar();
        Assert.assertEquals(3, charStream.getColumn());
    }

    @Test
    public void testGetLine() throws IOException {
        char c = charStream.readChar();
        Assert.assertEquals(1, charStream.getLine());
        char c2 = charStream.readChar();
        Assert.assertEquals(1, charStream.getLine());
    }

    @Test
    public void testGetEndColumn() throws IOException {
        char c = charStream.readChar();
        Assert.assertEquals(2, charStream.getEndColumn());
        char c2 = charStream.readChar();
        Assert.assertEquals(3, charStream.getEndColumn());
    }

    @Test
    public void testGetEndLine() throws IOException {
        char c = charStream.readChar();
        Assert.assertEquals(1, charStream.getEndLine());
        char c2 = charStream.readChar();
        Assert.assertEquals(1, charStream.getEndLine());
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        Assert.assertEquals(1, charStream.getBeginColumn());
        Assert.assertEquals(1, charStream.getBeginLine());
    }

    @Test
    public void testBackup() throws IOException {
        char c = charStream.readChar();
        char c2 = charStream.readChar();
        char c3 = charStream.readChar();
        charStream.backup(2);
        char nextC = charStream.readChar();
        Assert.assertEquals(c3, nextC);
        int line = charStream.getLine();
        int column = charStream.getColumn();
        Assert.assertEquals(1, line);
        Assert.assertEquals(2, column);
    }

    @Test(expected = IOException.class)
    public void testBackupWithEmptyBuffer() throws IOException {
        charStream.backup(1);
        Assert.fail();
    }
}