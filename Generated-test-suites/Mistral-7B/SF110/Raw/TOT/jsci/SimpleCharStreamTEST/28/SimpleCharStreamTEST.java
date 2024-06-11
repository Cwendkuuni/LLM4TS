package JSci.maths.symbolic;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        char c = charStream.BeginToken();
        Assert.assertEquals('t', c);
        char column = charStream.getColumn();
        Assert.assertEquals(1, column);
        char line = charStream.getLine();
        Assert.assertEquals(1, line);
        char endColumn = charStream.getEndColumn();
        Assert.assertEquals(2, endColumn);
        char endLine = charStream.getEndLine();
        Assert.assertEquals(1, endLine);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        char c = charStream.BeginToken();
        charStream.UpdateLineColumn(c);
        char column = charStream.getColumn();
        Assert.assertEquals(2, column);
        char line = charStream.getLine();
        Assert.assertEquals(1, line);
        char c2 = charStream.readChar();
        charStream.UpdateLineColumn(c2);
        column = charStream.getColumn();
        Assert.assertEquals(3, column);
        line = charStream.getLine();
        Assert.assertEquals(1, line);

        c2 = '\n';
        charStream.UpdateLineColumn(c2);
        column = charStream.getColumn();
        Assert.assertEquals(1, column);
        line = charStream.getLine();
        Assert.assertEquals(2, line);

        c2 = '\t';
        charStream.UpdateLineColumn(c2);
        column = charStream.getColumn();
        Assert.assertEquals(9, column);
        line = charStream.getLine();
        Assert.assertEquals(2, line);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        char c = charStream.BeginToken();
        charStream.UpdateLineColumn(c);

        charStream.FillBuff();
        char column = charStream.getColumn();
        Assert.assertEquals(2, column);
        char line = charStream.getLine();
        Assert.assertEquals(1, line);
        char c2 = charStream.readChar();
        charStream.UpdateLineColumn(c2);
        column = charStream.getColumn();
        Assert.assertEquals(4, column);
        line = charStream.getLine();
        Assert.assertEquals(1, line);

        c2 = '\n';
        charStream.UpdateLineColumn(c2);
        column = charStream.getColumn();
        Assert.assertEquals(1, column);
        line = charStream.getLine();
        Assert.assertEquals(2, line);

        charStream.FillBuff();
        c2 = charStream.readChar();
        charStream.UpdateLineColumn(c2);
        column = charStream.getColumn();
        Assert.assertEquals(2, column);
        line = charStream.getLine();
        Assert.assertEquals(2, line);
    }

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        char c = charStream.BeginToken();
        charStream.UpdateLineColumn(c);

        charStream.FillBuff();
        char column = charStream.getColumn();
        Assert.assertEquals(2, column);
        char line = charStream.getLine();
        Assert.assertEquals(1, line);

        charStream.ExpandBuff(false);
        column = charStream.getColumn();
        Assert.assertEquals(2, column);
        line = charStream.getLine();
        Assert.assertEquals(1, line);

        char c2 = charStream.readChar();
        charStream.UpdateLineColumn(c2);
        column = charStream.getColumn();
        Assert.assertEquals(3, column);
        line = charStream.getLine();
        Assert.assertEquals(1, line);

        charStream.FillBuff();
        char c3 = charStream.readChar();
        charStream.UpdateLineColumn(c3);
        column = charStream.getColumn();
        Assert.assertEquals(5, column);
        line = charStream.getLine();
        Assert.assertEquals(1, line);

        charStream.ExpandBuff(true);
        column = charStream.getColumn();
        Assert.assertEquals(4, column);
        line = charStream.getLine();
        Assert.assertEquals(1, line);

        char c4 = charStream.readChar();
        charStream.UpdateLineColumn(c4);
        column = charStream.getColumn();
        Assert.assertEquals(5, column);
        line = charStream.getLine();
        Assert.assertEquals(1, line);

        charStream.FillBuff();
        c4 = charStream.readChar();
        charStream.UpdateLineColumn(c4);
        column = charStream.getColumn();
        Assert.assertEquals(6, column);
        line = charStream.getLine();
        Assert.assertEquals(1, line);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        char c = charStream.BeginToken();
        charStream.UpdateLineColumn(c);

        charStream.FillBuff();
        char column = charStream.getColumn();
        Assert.assertEquals(2, column);
        char line = charStream.getLine();
        Assert.assertEquals(1, line);

        charStream.inputStream.close();
        charStream.readChar();
    }

    @Test(expected = IOException.class)
    public void testIOException2() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        char c = charStream.BeginToken();
        charStream.UpdateLineColumn(c);

        charStream.FillBuff();
        char column = charStream.getColumn();
        Assert.assertEquals(2, column);
        char line = charStream.getLine();
        Assert.assertEquals(1, line);

        charStream.maxNextCharInd = 0;
        charStream.readChar();
    }

    @Test(expected = IOException.class)
    public void testIOException3() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        char c = charStream.BeginToken();
        charStream.UpdateLineColumn(c);

        charStream.FillBuff();
        char column = charStream.getColumn();
        Assert.assertEquals(2, column);
        char line = charStream.getLine();
        Assert.assertEquals(1, line);

        charStream.available = -1;
        charStream.readChar();
    }

    @Test
    public void testGetImage() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        String image = SimpleCharStream.GetImage();
        Assert.assertEquals("test\r\ntest\r\n", image);
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        char[] suffix = SimpleCharStream.GetSuffix(5);
        Assert.assertArrayEquals(new char[]{'t', 'e', 's', 't'}, suffix);

        suffix = SimpleCharStream.GetSuffix(15);
        Assert.assertArrayEquals(new char[]{'e', 's', 't', ' ', 't', 'e', 's', 't'}, suffix);
    }

    @Test
    public void testDone() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        SimpleCharStream.Done();
    }
}