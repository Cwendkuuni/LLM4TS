package com.yourpackagename.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 4096);
        assertEquals('\r', scs.BeginToken());
        assertEquals('\n', scs.BeginToken());
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 4096);
        assertEquals('\r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals('t', scs.readChar());
    }

    @Test
    public void testGetColumn() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 4096);
        assertEquals(1, scs.getColumn());
        assertEquals(2, scs.readChar());
        assertEquals(3, scs.getColumn());
    }

    @Test
    public void testGetLine() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 4096);
        assertEquals(1, scs.getLine());
        assertEquals('\r', scs.readChar());
        assertEquals('\n', scs.readChar());
        assertEquals(1, scs.getLine());
    }

    @Test
    public void testExpandBuffWithWrapAround() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 4096);
        scs.ExpandBuff(true);
        assertEquals(6145, scs.bufsize);
    }

    @Test
    public void testExpandBuffWithoutWrapAround() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 4096);
        scs.ExpandBuff(false);
        assertEquals(8192, scs.bufsize);
    }

    @Test(expected = IOException.class)
    public void testFillBuffWithIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 4096);
        scs.FillBuff();
        scs.FillBuff(); // This should throw an IOException
    }

    @Test(expected = IOException.class)
    public void testReadCharWithIOException() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 4096);
        scs.FillBuff();
        scs.inputStream = new ByteArrayInputStream(new byte[]{-1});
        assertThrows(IOException.class, () -> scs.readChar());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('\r', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('\n', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(2, scs.getColumn());
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.getLine());
        assertEquals(3, scs.getColumn());
        assertEquals('e', scs.readChar());
        assertEquals('\r', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(1, scs.getColumn());
        assertEquals('t', scs.readChar());
        assertEquals(2, scs.getLine());
        assertEquals(2, scs.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 1, 4096);
        assertEquals(0, scs.bufpos);
        assertEquals('t', scs.readChar());
        assertEquals(1, scs.bufpos);
        scs.backup(1);
        assertEquals('t', scs.readChar());
        assertEquals(0, scs.bufpos);
    }

    @Test
    public void testConstructorWithInvalidStartcolumn() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 1, 0, 4096));
    }

    @Test
    public void testConstructorWithInvalidStartline() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleCharStream(new ByteArrayInputStream("test\r\n".getBytes()), 0, 1, 4096));
    }
}