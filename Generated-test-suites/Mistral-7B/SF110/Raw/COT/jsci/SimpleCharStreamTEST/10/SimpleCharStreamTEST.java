package com.example.junit; // replace with your test package name

import org.junit.Assert;
import org.junit.Test;

import JSci.maths.symbolic.SimpleCharStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;



public class SimpleCharStreamTest {

    @Test
    public void testStaticVariables() {
        Assert.assertEquals(true, SimpleCharStream.staticFlag);
        Assert.assertNotSame(0, SimpleCharStream.bufsize);
        Assert.assertNotSame(0, SimpleCharStream.available);
        Assert.assertNotSame(0, SimpleCharStream.tokenBegin);
        Assert.assertNotSame(-1, SimpleCharStream.bufpos);
    }

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 10);
        charStream.ExpandBuff(false);
        Assert.assertTrue(charStream.bufsize > 10);
        charStream.ExpandBuff(true);
        Assert.assertTrue(charStream.bufsize > charStream.bufsize);
    }

    @Test(expected = IOException.class)
    public void testFillBuffWithIOException() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 10);
        charStream.FillBuff();
        charStream.FillBuff();
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\n".getBytes()), 1, 1, 10);
        char c = charStream.BeginToken();
        Assert.assertEquals('\n', c);
        c = charStream.BeginToken();
        Assert.assertEquals('t', c);
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 10);
        charStream.UpdateLineColumn('\r');
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());
        charStream.UpdateLineColumn('\n');
        Assert.assertEquals(2, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 10);
        Assert.assertEquals('t', charStream.readChar());
        Assert.assertEquals('e', charStream.readChar());
        Assert.assertEquals('s', charStream.readChar());
        Assert.assertEquals('\0', charStream.readChar()); // end of stream
    }

    @Test
    public void testGetLineColumn() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\r\nline2\r\n".getBytes()), 1, 1, 10);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(1, charStream.getColumn());
        Assert.assertEquals(1, charStream.getBeginLine());
        Assert.assertEquals(1, charStream.getBeginColumn());
        Assert.assertEquals(2, charStream.getEndLine());
        Assert.assertEquals(7, charStream.getEndColumn());
        charStream.Backup(3);
        Assert.assertEquals(1, charStream.getLine());
        Assert.assertEquals(5, charStream.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\r\nline2\r\n".getBytes()), 1, 1, 10);
        charStream.Backup(3);
        Assert.assertEquals('t', charStream.readChar());
        Assert.assertEquals('\r', charStream.readChar());
        Assert.assertEquals('\n', charStream.readChar());
        Assert.assertEquals('l', charStream.readChar());
        Assert.assertEquals('i', charStream.readChar());
        Assert.assertEquals('n', charStream.readChar());
        Assert.assertEquals('e', charStream.readChar());
        Assert.assertEquals('\r', charStream.readChar());
        Assert.assertEquals('\n', charStream.readChar());
        Assert.assertEquals('1', charStream.readChar());
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\r\nline2\r\n".getBytes()), 1, 1, 10);
        charStream.ReInit(new ByteArrayInputStream("new test".getBytes()), 1, 1, 10);
        Assert.assertEquals('n', charStream.readChar());
        Assert.assertEquals('e', charStream.readChar());
        Assert.assertEquals(' ', charStream.readChar());
        Assert.assertEquals('t', charStream.readChar());
    }

    @Test
    public void testGetImage() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 10);
        String image = SimpleCharStream.GetImage();
        Assert.assertEquals("test", image);
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("testtesttest".getBytes()), 1, 1, 10);
        char[] suffix = SimpleCharStream.GetSuffix(3);
        Assert.assertArrayEquals(new char[]{'t', 'e', 's'}, suffix);
    }

    @Test
    public void testDone() {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 10);
        SimpleCharStream.Done();
        Assert.assertNull(charStream.buffer);
        Assert.assertNull(charStream.bufline);
        Assert.assertNull(charStream.bufcolumn);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReInitWithNullInputStream() {
        SimpleCharStream charStream = new SimpleCharStream(null, 1, 1, 10);
        charStream.ReInit(new ByteArrayInputStream("test".getBytes()), 1, 1, 10);
    }
}

