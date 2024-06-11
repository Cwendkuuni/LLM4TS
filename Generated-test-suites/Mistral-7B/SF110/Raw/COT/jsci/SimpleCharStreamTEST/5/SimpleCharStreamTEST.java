import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static JSci.maths.symbolic.SimpleCharStream.*;

public class SimpleCharStreamTest {

    // Basic Test Cases

    @Test
    public void testBeginToken() throws IOException {
        Reader inputStream = new ByteArrayInputStream("testString".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        char c = charStream.BeginToken();
        Assert.assertEquals('t', c);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        Reader inputStream = new ByteArrayInputStream("test\r\nstring".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        char c = 't';
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(2, charStream.getColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        Reader inputStream = new ByteArrayInputStream("testString".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        char c = charStream.readChar();
        Assert.assertEquals('t', c);
    }

    @Test
    public void testGetColumn() throws IOException {
        Reader inputStream = new ByteArrayInputStream("test\r\nstring".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        char c = 't';
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(2, charStream.getColumn());
    }

    @Test
    public void testGetLine() throws IOException {
        Reader inputStream = new ByteArrayInputStream("test\r\nstring".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        char c = 't';
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(1, charStream.getLine());
    }

    @Test
    public void testGetEndColumn() throws IOException {
        Reader inputStream = new ByteArrayInputStream("test\r\nstring".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        char c = 'e';
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(4, charStream.getEndColumn());
    }

    @Test
    public void testGetEndLine() throws IOException {
        Reader inputStream = new ByteArrayInputStream("test\r\nstring".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        char c = '\n';
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(2, charStream.getEndLine());
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        Reader inputStream = new ByteArrayInputStream("test\r\nstring".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        char c = 't';
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(1, charStream.getBeginColumn());
    }

    @Test
    public void testGetBeginLine() throws IOException {
        Reader inputStream = new ByteArrayInputStream("test\r\nstring".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        char c = 't';
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(1, charStream.getBeginLine());
    }

    @Test
    public void testBackup() throws IOException {
        Reader inputStream = new ByteArrayInputStream("testString".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        char c = 't';
        charStream.UpdateLineColumn(c);
        charStream.backup(1);
        Assert.assertEquals(0, charStream.getColumn());
        Assert.assertEquals(1, charStream.getLine());
    }

    @Test
    public void testExpandBuff() throws IOException {
        Reader inputStream = new ByteArrayInputStream("testString".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        charStream.ExpandBuff(true);
        char c = 't';
        charStream.UpdateLineColumn(c);
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testFillBuff() throws IOException {
        Reader inputStream = new ByteArrayInputStream("testString".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        char c = charStream.readChar();
        charStream.FillBuff();
        Assert.assertEquals('t', c);
    }

    // Edge Cases and Exception Handling Test Cases

    @Test(expected = IOException.class)
    public void testUnsupportedOperationExceptionOnNegativeBufferSize() {
        new SimpleCharStream(null, 1, 1, -1);
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnInputStreamClosure() throws IOException {
        Reader inputStream = new ByteArrayInputStream("testString".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        charStream.close();
        charStream.BeginToken();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsExceptionOnInvalidStartColumn() {
        Reader inputStream = new ByteArrayInputStream("testString".getBytes());
        new SimpleCharStream(inputStream, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsExceptionOnInvalidStartLine() {
        Reader inputStream = new ByteArrayInputStream("testString".getBytes());
        new SimpleCharStream(inputStream, 0, 1);
    }

    @Test
    public void testGetImage() throws IOException {
        Reader inputStream = new ByteArrayInputStream("testString".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        charStream.BeginToken();
        charStream.UpdateLineColumn('t');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');
        char c = charStream.readChar();
        String image = charStream.GetImage();
        Assert.assertEquals("testString", image);
    }

    @Test
    public void testGetSuffix() throws IOException {
        Reader inputStream = new ByteArrayInputStream("testString".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        charStream.BeginToken();
        charStream.UpdateLineColumn('t');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');
        char c = charStream.readChar();
        char[] suffix = charStream.GetSuffix(5);
        Assert.assertEquals("String", new String(suffix));
    }

    @Test
    public void testDone() throws IOException {
        Reader inputStream = new ByteArrayInputStream("testString".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        charStream.BeginToken();
        charStream.UpdateLineColumn('t');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');
        char c = charStream.readChar();
        charStream.Done();
        Assert.assertNull(charStream.buffer);
        Assert.assertNull(charStream.bufline);
        Assert.assertNull(charStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        Reader inputStream = new ByteArrayInputStream("test\r\nstring\r\nnewline".getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream);
        charStream.BeginToken();
        charStream.UpdateLineColumn('t');
        charStream.UpdateLineColumn('e');
        charStream.UpdateLineColumn('s');
        charStream.UpdateLineColumn('t');
        char c = charStream.readChar();
        charStream.adjustBeginLineColumn(2, 3);
        Assert.assertEquals(2, charStream.getBeginLine());
        Assert.assertEquals(3, charStream.getBeginColumn());
    }
}