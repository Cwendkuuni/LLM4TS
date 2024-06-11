package com.example.junit4test; // Replace with the appropriate test package

import org.junit.Assert;
import org.junit.Test;

import JSci.maths.symbolic.SimpleCharStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test class for SimpleCharStream methods
 */
public class SimpleCharStreamTest {

    private static final String TEST_STRING = "Hello\nWorld\nThis\tis\ta\ttest\r\r123";

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(TEST_STRING.getBytes()), 1, 1, 4096);
        Assert.assertEquals('H', scs.BeginToken());
        Assert.assertEquals(-1, scs.getBeginLine());
        Assert.assertEquals(-1, scs.getBeginColumn());
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(TEST_STRING.getBytes()), 1, 1, 4096);
        for (int i = 0; i < TEST_STRING.length(); ++i) {
            char c = scs.readChar();
            scs.UpdateLineColumn(c);
        }

        int expectedLine[] = {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3};
        int expectedColumn[] = {1, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27};

        for (int i = 0; i < TEST_STRING.length(); ++i) {
            char c = TEST_STRING.charAt(i);
            Assert.assertEquals(expectedLine[i], scs.getLine());
            Assert.assertEquals(expectedColumn[i], scs.getColumn());
            scs.readChar();
        }

        Assert.assertEquals(3, scs.getEndLine());
        Assert.assertEquals(28, scs.getEndColumn());
    }

    @Test
    public void testGetLineColumn() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(TEST_STRING.getBytes()), 1, 1, 4096);
        for (int i = 0; i < TEST_STRING.length(); ++i) {
            char c = scs.readChar();
            scs.UpdateLineColumn(c);
        }

        for (int i = 0; i < TEST_STRING.length(); ++i) {
            char c = TEST_STRING.charAt(i);
            Assert.assertEquals(i / 2 + 1, scs.getLine());
            Assert.assertEquals(i % 2 == 0 ? i + 1 : i, scs.getColumn());
            scs.readChar();
        }

        Assert.assertEquals(3, scs.getEndLine());
        Assert.assertEquals(28, scs.getEndColumn());
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(TEST_STRING.getBytes()), 1, 1, 4096);
        byte[] buffer = new byte[4096];
        int available = 0;
        for (int i = 0; i < TEST_STRING.length(); ++i) {
            char c = TEST_STRING.charAt(i);
            scs.UpdateLineColumn(c);
            if (i < 2047) {
                buffer[available++] = (byte) c;
            } else if (i == 2048 || i == 2049) {
                scs.FillBuff();
                for (int j = 0; j < 2048; ++j) {
                    Assert.assertEquals(buffer[j], SimpleCharStream.buffer[j]);
                }
                available = 0;
            }
        }

        if (available > 0) {
            scs.FillBuff();
            for (int i = 0; i < available; ++i) {
                Assert.assertEquals(buffer[i], SimpleCharStream.buffer[i + SimpleCharStream.tokenBegin]);
            }
        }
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnFillBuff() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(TEST_STRING.getBytes()), 1, 1, 4096);
        for (int i = 0; i < TEST_STRING.length() - 1; ++i) {
            char c = TEST_STRING.charAt(i);
            scs.UpdateLineColumn(c);
        }
        scs.FillBuff();
        scs.readChar();
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(TEST_STRING.getBytes()), 1, 1, 4096);
        for (int i = 0; i < TEST_STRING.length(); ++i) {
            char c = TEST_STRING.charAt(i);
            Assert.assertEquals(c, scs.readChar());
            scs.UpdateLineColumn(c);
        }

        Assert.assertEquals(-1, scs.readChar());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream scs = new SimpleCharStream(new ByteArrayInputStream(TEST_STRING.getBytes()), 1, 1, 4096);
        for (int i = 0; i < TEST_STRING.length(); ++i) {
            char c = TEST_STRING.charAt(i);
            Assert.assertEquals(c, scs.readChar());
            scs.UpdateLineColumn(c);
        }

        Assert.assertEquals(-1, scs.bufpos);
        Assert.assertEquals(4096, scs.available);
        Assert.assertEquals(4096, SimpleCharStream.bufsize);
        scs.backup(2048);
        Assert.assertEquals(2046, scs.bufpos);
        Assert.assertEquals(2048, scs.available);
        scs.backup(4096);
        Assert.assertEquals(-1, scs.bufpos);
        Assert.assertEquals(4096, scs.available);

        for (int i = 0; i < TEST_STRING.length(); ++i) {
            char c = TEST_STRING.charAt(i);
            Assert.assertEquals(c, scs.readChar());
            scs.UpdateLineColumn(c);
        }

        Assert.assertEquals(-1, scs.bufpos);
        Assert.assertEquals(4096, scs.available);
        scs.backup(4096);
        Assert.assertEquals(-1, scs.bufpos);
        Assert.assertEquals(4096, scs.available);
    }
}

