package JSci.maths.symbolic.test;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    @Test
    public void testSimpleCharStream() throws IOException {
        char[] input = {'t', 'e', 's', 't', '\r', '\n'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1);

        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('e', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('s', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(3, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('\r', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(4, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('\n', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(5, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals(-1, c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(5, charStream.getColumn());

        int beginLine = charStream.getBeginLine();
        int beginColumn = charStream.getBeginColumn();
        String image = SimpleCharStream.GetImage();
        char[] suffix = SimpleCharStream.GetSuffix('t', 'e', 's', 't');

        Assert.assertEquals(1, beginLine);
        Assert.assertEquals(5, beginColumn);
        Assert.assertEquals("test", image);
        Assert.assertArrayEquals(new char[]{'t', 'e', 's', 't'}, suffix);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        char[] input = {'t', 'e', 's', 't'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1);

        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream.ExpandBuff(false);

        c = charStream.readChar();
        Assert.assertEquals('e', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        charStream.ExpandBuff(false);

        charStream.inputStream.close();

        c = charStream.readChar();
        Assert.fail();
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        char[] input = {'t', 'e', 's', 't', '\r', '\n'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1);

        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('e');
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        charStream.UpdateLineColumn('s');
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(3, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('\n');
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        char[] input = {'t', 'e', 's', 't', '\r', '\n'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1);

        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream.backup(1);
        c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream.backup(2);
        c = charStream.readChar();
        Assert.assertEquals('e', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());

        charStream.backup(5);
        c = charStream.readChar();
        Assert.assertEquals('\r', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(4, charStream.getColumn());
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        char[] input = {'t', 'e', 's', 't', '\r', '\n', 'l', 'o', 'r', 'e', ' ', 'i', 's', ' ', 'a', ' ', 't', 'e', 's', 't', '\r', '\n'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1);

        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream.adjustBeginLineColumn(2, 5);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(5, charStream.getColumn());

        c = charStream.readChar();
        Assert.assertEquals('e', c);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(6, charStream.getColumn());

        charStream.adjustBeginLineColumn(1, 1);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(6, charStream.getColumn());

        charStream.adjustBeginLineColumn(1, 18);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(18, charStream.getColumn());

        charStream.adjustBeginLineColumn(1, 1);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testConstructors() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]));
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 4096);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 2048);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, 1);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream = new SimpleCharStream(new ByteArrayInputStream(new byte[0]), 1, 1, -1);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testReInit() throws IOException {
        char[] input = {'t', 'e', 's', 't', '\r', '\n'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1);

        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream.ReInit(inputStream, 2, 6);

        c = charStream.readChar();
        Assert.assertEquals('e', c);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(6, charStream.getColumn());

        charStream.ReInit(inputStream, 2);

        c = charStream.readChar();
        Assert.assertEquals('s', c);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream.ReInit(inputStream);

        c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        charStream.ReInit(new ByteArrayInputStream(new byte[0]), 2, 6);

        c = charStream.readChar();
        Assert.assertEquals(-1, c);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(6, charStream.getColumn());
    }

    @Test
    public void testGetImage() throws IOException {
        char[] input = {'t', 'e', 's', 't', '\r', '\n'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1);

        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        String image = SimpleCharStream.GetImage();
        Assert.assertEquals("test", image);
    }

    @Test
    public void testGetSuffix() throws IOException {
        char[] input = {'t', 'e', 's', 't', '\r', '\n'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1);

        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        char[] suffix = SimpleCharStream.GetSuffix('t', 'e', 's', 't');
        Assert.assertArrayEquals(new char[]{'t', 'e', 's', 't'}, suffix);
    }

    @Test
    public void testDone() throws IOException {
        char[] input = {'t', 'e', 's', 't', '\r', '\n'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        SimpleCharStream charStream = new SimpleCharStream(inputStream, 1, 1);

        char c = charStream.readChar();
        Assert.assertEquals('t', c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());

        SimpleCharStream.Done();
    }
}