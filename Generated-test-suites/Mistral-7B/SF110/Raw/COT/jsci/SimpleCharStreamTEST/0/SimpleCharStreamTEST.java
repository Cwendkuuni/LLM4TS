package com.example.junitTests; // replace with your package name

import org.junit.Test;
import org.junit.Assert.*;
import java.io.StringReader;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        stream.ExpandBuff(false);
        int oldSize = stream.bufsize;
        stream.ExpandBuff(false);
        int newSize = stream.bufsize;
        assertEquals("Buffer should have doubled in size after expanding", oldSize * 2, newSize);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        for (int i = 0; i < 1024; i++) {
            stream.FillBuff();
            stream.UpdateLineColumn((char) i);
        }
        assertEquals("Buffer should have been filled with 1024 characters", 1024, stream.available);
    }

    @Test(expected = IOException.class)
    public void testFillBuffWithIOException() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        stream.inputStream.close();
        stream.FillBuff();
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        char c = stream.BeginToken();
        assertEquals("First character read should be the first character in the input stream", 't', c);
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test\r\ntest"), 1, 1, 1024);
        for (int i = 0; i < 6; i++) {
            stream.UpdateLineColumn((char) i);
        }
        assertEquals("Line should be 2 after reading the first newline character", 2, stream.getLine());
        assertEquals("Column should be 1 after reading the first character", 1, stream.getColumn());
        assertEquals("Column should be 6 after reading the fifth character", 6, stream.getColumn());
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        char c;
        for (int i = 0; i < 6; i++) {
            c = stream.readChar();
            assertEquals("Character read should match the expected character", (char) (i + 't'), c);
        }
        assertEquals("Last character read should be the last character in the input stream", '\n', stream.readChar());
    }

    @Test
    public void testGetLine() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test\r\ntest"), 1, 1, 1024);
        assertEquals("Line should be 1 for the first line", 1, stream.getLine());
        assertEquals("Line should be 3 for the second line", 3, stream.getLine());
    }

    @Test
    public void testGetColumn() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test\r\ntest"), 1, 1, 1024);
        assertEquals("Column should be 1 for the first character", 1, stream.getColumn());
        assertEquals("Column should be 6 for the sixth character", 6, stream.getColumn());
    }

    @Test
    public void testBackup() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        stream.UpdateLineColumn((char) 0);
        stream.backup(1);
        assertEquals("Character position should be 0 after backing up by one character", 0, stream.bufpos);
        assertEquals("Line should be 1 and column should be 0 after backing up", 1, stream.getLine());
        assertEquals("Column should be 0 after backing up", 0, stream.getColumn());
    }

    @Test
    public void testConstructor() {
        SimpleCharStream stream1 = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        SimpleCharStream stream2 = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        assertNotSame("Two instances of SimpleCharStream should not be the same object", stream1, stream2);
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        stream.ReInit(new StringReader("test2"), 2, 2);
        assertEquals("Line should be 2 after reinitializing the stream", 2, stream.getLine());
        assertEquals("Column should be 2 after reinitializing the stream", 2, stream.getColumn());
    }

    @Test
    public void testGetImage() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        String image = SimpleCharStream.GetImage();
        assertEquals("Image should match the contents of the input stream", "test", image);
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("testtesttest"), 1, 1, 1024);
        char[] suffix = SimpleCharStream.GetSuffix(3);
        assertEquals("Suffix should match the last three characters in the input stream", "test", new String(suffix));
    }

    @Test
    public void testDone() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test"), 1, 1, 1024);
        SimpleCharStream.Done();
        assertNull("Buffer and associated arrays should be null after calling Done", stream.buffer);
        assertNull("Buffer line array should be null after calling Done", stream.bufline);
        assertNull("Buffer column array should be null after calling Done", stream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream stream = new SimpleCharStream(new StringReader("test\r\ntest\r\ntest"), 1, 1, 1024);
        SimpleCharStream.adjustBeginLineColumn(2, 3);
        assertEquals("Line should be 3 after adjusting the begin line and column", 3, stream.getLine());
        assertEquals("Column should be 3 after adjusting the begin line and column", 3, stream.getColumn());
    }
}