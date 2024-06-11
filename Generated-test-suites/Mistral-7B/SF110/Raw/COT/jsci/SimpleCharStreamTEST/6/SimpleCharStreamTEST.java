package com.example.junit.simplecharstream; // Replace with your test package name

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testExpandBuff() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test".getBytes()), 1, 1, 1024);
        scs.ExpandBuff(false);
        assertEquals(2048, scs.bufsize);
    }

    @Test(expected = Error.class)
    public void testExpandBuffWithThrowable() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test".getBytes()), 1, 1, 1024);
        scs.ExpandBuff(true); // Intentionally throw an exception
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nLine1\r\nTest\r\nLine2\r\nTest".getBytes()), 1, 1, 1024);
        char c;
        for (int i = 0; i < 12; i++) {
            c = scs.readChar();
            assertEquals(i < 9 ? (char) (i < 3 ? '\r' : '\n') : 'T', c);
        }
        scs.FillBuff(); // Test when maxNextCharInd == available
        c = scs.readChar();
        assertEquals('T', c);
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nLine1\r\nTest".getBytes()), 1, 1, 1024);
        char c = 'T';
        for (int line = 1, col = 1; col <= 11; col++) {
            scs.UpdateLineColumn(c);
            assertEquals(line, scs.getLine());
            assertEquals(col, scs.getColumn());
            c = col < 10 ? 'T' : '\n';
        }
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nLine1\r\nTest\r\nLine2\r\nTest".getBytes()), 1, 1, 1024);
        for (int i = 0; i < 13; i++) {
            assertEquals(i < 12 ? (char) (i < 9 ? '\r' : '\n') : 'T', scs.readChar());
        }
        assertEquals(-1, scs.readChar()); // Test when no more characters to read
    }

    @Test
    public void testGetLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nLine1\r\nTest\r\nLine2\r\nTest".getBytes()), 1, 1, 1024);
        for (int line = 1, col = 1; col <= 11; col++) {
            char c = col < 10 ? 'T' : '\n';
            scs.UpdateLineColumn(c);
            assertEquals(line, scs.getLine());
            assertEquals(col, scs.getColumn());
            assertEquals(col, scs.getEndColumn());
            assertEquals(line, scs.getEndLine());
        }
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nLine1\r\nTest\r\nLine2\r\nTest".getBytes()), 1, 1, 1024);
        char c;
        for (int i = 0; i < 13; i++) {
            c = scs.readChar();
            if (c != '\r' && c != '\n') {
                scs.backup(1);
            }
            assertEquals(i < 12 ? (char) (i < 11 ? 'T' : '\r') : '\n', c);
        }
    }

    @Test
    public void testConstructors() {
        SimpleCharStream scs1 = new SimpleCharStream(new ByteArrayInputStream("Test".getBytes()), 1, 1, 1024);
        SimpleCharStream scs2 = new SimpleCharStream(new ByteArrayInputStream("Test".getBytes()), 1, 1);
        SimpleCharStream scs3 = new SimpleCharStream(new ByteArrayInputStream("Test".getBytes()));
        SimpleCharStream scs4 = new SimpleCharStream(new FileReader("Test.txt"));
        SimpleCharStream scs5 = new SimpleCharStream(new FileReader("Test.txt"), 1, 1);
        SimpleCharStream scs6 = new SimpleCharStream(new FileReader("Test.txt"), 1, 1, 1024);
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nLine1\r\nTest\r\nLine2\r\nTest".getBytes()), 1, 1, 1024);
        scs.ReInit(new ByteArrayInputStream("Test2\r\nLine1\r\nTest2\r\nLine2\r\nTest2".getBytes()), 2, 2);
        char c;
        for (int i = 0; i < 13; i++) {
            c = scs.readChar();
            if (i < 12) {
                assertEquals(i < 9 ? '\r' : '\n', c);
            } else {
                assertEquals('2', c);
            }
        }
    }

    @Test
    public void testGetImage() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nLine1\r\nTest\r\nLine2\r\nTest".getBytes()), 1, 1, 1024);
        String image = SimpleCharStream.GetImage();
        assertEquals("Test\r\nLine1\r\nTest\r\nLine2\r\nTest", image);
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nLine1\r\nTest\r\nLine2\r\nTestTest".getBytes()), 1, 1, 1024);
        char[] suffix = SimpleCharStream.GetSuffix(5);
        assertEquals("Test", new String(suffix));
    }

    @Test
    public void testDone() {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream("Test\r\nLine1\r\nTest\r\nLine2\r\nTest".getBytes()), 1, 1, 1024);
        SimpleCharStream.Done();
        assertEquals(null, scs.buffer);
        assertEquals(null, scs.bufline);
        assertEquals(null, scs.bufcolumn);
    }

}