package JSci.maths.symbolic; // package name should be changed according to the actual project structure

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimpleCharStreamTest {

    // Basic test cases for each method

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        char c = charStream.BeginToken();
        assertEquals('t', c);
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.getLine());
        assertEquals(2, charStream.getColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        assertEquals('t', c);
    }

    @Test
    public void testGetColumn() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testGetLine() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getLine());
    }

    @Test
    public void testGetEndColumn() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getEndColumn());
    }

    @Test
    public void testGetEndLine() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        assertEquals(2, charStream.getEndLine());
    }

    @Test
    public void testGetBeginColumn() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getBeginColumn());
    }

    @Test
    public void testGetBeginLine() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        assertEquals(1, charStream.getBeginLine());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        charStream.backup(1);
        char expectedChar = charStream.readChar();
        assertEquals(c, expectedChar);
    }

    @Test
    public void testConstructor() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        assertEquals(1, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testConstructorWithStartColumn() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 2);
        assertEquals(1, charStream.getLine());
        assertEquals(2, charStream.getColumn());
    }

    @Test
    public void testConstructorWithInputReader() {
        SimpleCharStream charStream = new SimpleCharStream(new InputStreamReader(System.in), 1, 1);
        char c = charStream.readChar();
        assertEquals(System.in.read(), (int) c);
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        charStream.ReInit(new ByteArrayInputStream("test2".getBytes()), 1, 1, 4096);
        char expectedChar = charStream.readChar();
        assertEquals('t', expectedChar);
    }

    @Test
    public void testGetImage() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        String expectedImage = new String(new char[]{'t'});
        String image = charStream.GetImage();
        assertEquals(expectedImage, image);
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("testtest".getBytes()), 1, 1, 4096);
        char[] expectedSuffix = new char[]{ 't', 'e', 's', 't' };
        char[] suffix = charStream.GetSuffix(4);
        assertArrayEquals(expectedSuffix, suffix);
    }

    @Test
    public void testDone() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        char[] expectedBuffer = new char[]{'t'};
        char[] buffer = charStream.GetImage().toCharArray();
        assertArrayEquals(expectedBuffer, buffer);
        charStream.Done();
        buffer = charStream.GetImage().toCharArray();
        assertNull(buffer);
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeBuffersize() {
        new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReInitWithNegativeBuffersize() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        charStream.ReInit(new ByteArrayInputStream("test2".getBytes()), 1, 1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullInputReader() {
        new SimpleCharStream(null, 1, 1, 4096);
    }

    @Test(expected = IOException.class)
    public void testFillBuffWithIOException() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        charStream.FillBuff();
        charStream.inputStream.close();
        charStream.FillBuff();
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testUpdateLineColumnWithInvalidCharIndex() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        charStream.UpdateLineColumn((char) 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBackupWithInvalidAmount() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        char c = charStream.readChar();
        charStream.backup(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testGetImageWithNullCharArray() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        char[] expectedBuffer = new char[]{ 't' };
        char[] buffer = null;
        charStream.GetImage().toCharArray();
        assertNull(buffer);
    }
}