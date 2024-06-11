package com.example.junit; // Replace this with your test package name

import org.junit.Assert;
import org.junit.Test;

import JSci.maths.symbolic.SimpleCharStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("some text".getBytes()), 1, 1);
        char c = charStream.BeginToken();
        Assert.assertEquals((char) 0, c);
        char nextChar = charStream.readChar();
        Assert.assertEquals(c, nextChar);
    }

    @Test(expected = IOException.class)
    public void testBeginTokenWithIOException() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("some text".getBytes()), 1, 1);
        charStream.inputStream.close(); // Simulate IOException by closing input stream
        char c = charStream.BeginToken();
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("some text\nnew line".getBytes()), 1, 1);
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);
        int line = charStream.getLine();
        int column = charStream.getColumn();
        Assert.assertEquals(1, line);
        Assert.assertEquals(2, column);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("some text".getBytes()), 1, 1);
        char c = charStream.readChar();
        Assert.assertEquals('s', c);

        char nextChar = charStream.readChar();
        Assert.assertEquals('o', nextChar);

        char lastChar = charStream.readChar();
        Assert.assertEquals('t', lastChar);

        char endChar = charStream.readChar();
        Assert.assertEquals((char) -1, endChar); // End of stream

        char unreadChar = charStream.readChar();
        Assert.assertEquals((char) -1, unreadChar); // Unread character is not available
    }

    @Test
    public void testGetLineAndColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("some text\nnew line".getBytes()), 1, 1);
        char c = charStream.readChar();
        charStream.UpdateLineColumn(c);

        int line = charStream.getLine();
        int column = charStream.getColumn();
        Assert.assertEquals(1, line);
        Assert.assertEquals(1, column);

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);

        line = charStream.getLine();
        column = charStream.getColumn();
        Assert.assertEquals(1, line);
        Assert.assertEquals(2, column);

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);

        line = charStream.getLine();
        column = charStream.getColumn();
        Assert.assertEquals(1, line);
        Assert.assertEquals(3, column);

        c = charStream.readChar();
        charStream.UpdateLineColumn(c);

        line = charStream.getLine();
        column = charStream.getColumn();
        Assert.assertEquals(2, line);
        Assert.assertEquals(1, column);
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("some text".getBytes()), 1, 1);
        char c = charStream.readChar();
        charStream.backup(1);
        char previousChar = charStream.readChar();
        Assert.assertEquals(c, previousChar);
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("some text".getBytes()), 1, 1);
        char c = charStream.readChar();
        charStream.ReInit(new ByteArrayInputStream("another text".getBytes()), 1, 1);
        char nextChar = charStream.readChar();
        Assert.assertEquals((char) 'a', nextChar);
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("some text".getBytes()), 1, 1);
        String image = SimpleCharStream.GetImage();
        Assert.assertEquals("some text", image);
    }

    @Test
    public void testGetSuffix() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("some textsome text".getBytes()), 1, 1);
        char[] suffix = SimpleCharStream.GetSuffix(6);
        Assert.assertEquals("texttext", new String(suffix));
    }

    @Test
    public void testDone() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("some text".getBytes()), 1, 1);
        SimpleCharStream.Done();
        char c = charStream.readChar();
        Assert.assertEquals((char) -1, c);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("123\n456\n789".getBytes()), 1, 1);
        charStream.adjustBeginLineColumn(2, 1);
        int beginLine = charStream.getBeginLine();
        int beginColumn = charStream.getBeginColumn();
        Assert.assertEquals(2, beginLine);
        Assert.assertEquals(1, beginColumn);
    }

    
}