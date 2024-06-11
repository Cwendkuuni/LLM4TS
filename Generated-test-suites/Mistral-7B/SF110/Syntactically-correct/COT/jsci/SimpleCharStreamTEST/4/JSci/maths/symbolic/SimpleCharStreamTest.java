package com.example.tests; // Replace this package name with the actual package name of your test class

import org.junit.Test;
import static org.junit.Assert.*;

import JSci.maths.symbolic.SimpleCharStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        char actualChar = charStream.BeginToken();
        char expectedChar = 't';
        assertCharEquals(expectedChar, actualChar);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\ntest\r\n".getBytes()), 1, 1, 4096);
        char c = 't';
        charStream.UpdateLineColumn(c);
        int expectedLine = 2;
        int expectedColumn = 1;
        assertLineColumn(expectedLine, expectedColumn, charStream.getLine(), charStream.getColumn());

        c = '\r';
        charStream.UpdateLineColumn(c);
        expectedLine = 2;
        expectedColumn = 1;
        assertLineColumn(expectedLine, expectedColumn, charStream.getLine(), charStream.getColumn());

        c = '\n';
        charStream.UpdateLineColumn(c);
        expectedLine = 3;
        expectedColumn = 1;
        assertLineColumn(expectedLine, expectedColumn, charStream.getLine(), charStream.getColumn());

        c = 'e';
        charStream.UpdateLineColumn(c);
        expectedLine = 3;
        expectedColumn = 2;
        assertLineColumn(expectedLine, expectedColumn, charStream.getLine(), charStream.getColumn());
    }

    @Test
    public void testGetImage() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test123test123".getBytes()), 1, 1, 4096);
        String expectedImage = "test123test123";
        String actualImage = SimpleCharStream.GetImage();
        assertEquals(expectedImage, actualImage);
    }

    @Test(expected = IOException.class)
    public void testFillBuffIOException() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1, 4096);
        char[] buffer = new char[10];
        int maxNextCharInd = 5;
        charStream.maxNextCharInd = maxNextCharInd;
        charStream.available = 1;
        charStream.FillBuff();
        charStream.inputStream.read(buffer, 0, 10);
        fail("IOException expected but not thrown");
    }

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test123test123".getBytes()), 1, 1, 4096);
        charStream.ExpandBuff(false);
        int expectedBufsize = 8192;
        assertEquals(expectedBufsize, charStream.bufsize);

        charStream.ExpandBuff(true);
        expectedBufsize = 16384;
        assertEquals(expectedBufsize, charStream.bufsize);
    }
}