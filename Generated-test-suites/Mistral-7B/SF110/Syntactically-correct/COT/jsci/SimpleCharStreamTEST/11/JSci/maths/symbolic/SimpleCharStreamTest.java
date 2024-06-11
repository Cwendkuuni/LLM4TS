package com.example.junit; // Replace with your test package name

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

public class SimpleCharStreamTest {
    private static final int BUFFER_SIZE = 10;
    private static final int START_LINE = 1;
    private static final int START_COLUMN = 1;
    private static final String TEST_INPUT = "HelloWorld!";

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream(TEST_INPUT.getBytes()), START_LINE, START_COLUMN, BUFFER_SIZE);

        char expected[] = TEST_INPUT.toCharArray();
        char[] actual = new char[expected.length * 2];

        SimpleCharStream.ExpandBuff(false);
        for (int i = 0; i < expected.length; i++) {
            char c = charStream.readChar();
            actual[i] = c;
        }

        assertArrayEquals("ExpandBuff - Expanded buffer content", expected, actual);

        charStream.ReInit(new ByteArrayInputStream(TEST_INPUT.getBytes()), START_LINE, START_COLUMN, BUFFER_SIZE);

        SimpleCharStream.ExpandBuff(true);
        for (int i = 0; i < expected.length; i++) {
            char c = charStream.readChar();
            actual[i + expected.length] = c;
        }

        assertArrayEquals("ExpandBuff - Expanded buffer content (wrap around)", expected, new char[expected.length * 2]);
    }

    @Test
    public void testFillBuff() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_INPUT.getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream, START_LINE, START_COLUMN, BUFFER_SIZE);

        SimpleCharStream.FillBuff();
        char c = charStream.readChar();
        assertEquals("FillBuff - First character read", TEST_INPUT.charAt(0), c);

        char expected[] = TEST_INPUT.toCharArray();
        char actual[] = new char[expected.length];
        for (int i = 0; i < expected.length; i++) {
            c = charStream.readChar();
            actual[i] = c;
        }

        assertArrayEquals("FillBuff - Buffer content", expected, actual);
    }

    @Test
    public void testBeginToken() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_INPUT.getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream, START_LINE, START_COLUMN, BUFFER_SIZE);

        char c = charStream.BeginToken();
        assertEquals("BeginToken - First character read", TEST_INPUT.charAt(0), c);

        char actual = charStream.readChar();
        char expected = TEST_INPUT.charAt(1);
        assertEquals("BeginToken - Second character read", expected, actual);

        int beginLine = charStream.getBeginLine();
        int beginColumn = charStream.getBeginColumn();
        assertEquals("BeginToken - Begin line and column", START_LINE, beginLine);
        assertEquals("BeginToken - Begin line and column", START_COLUMN, beginColumn);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_INPUT.getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream, START_LINE, START_COLUMN, BUFFER_SIZE);

        for (int i = 0; i < TEST_INPUT.length(); i++) {
            char c = charStream.readChar();
            UpdateLineColumn(c);
        }

        int line = charStream.getLine();
        int column = charStream.getColumn();
        int endLine = charStream.getEndLine();
        int endColumn = charStream.getEndColumn();
        int beginLine = charStream.getBeginLine();
        int beginColumn = charStream.getBeginColumn();

        assertEquals("UpdateLineColumn - Current line and column", line, TEST_INPUT.length());
        assertEquals("UpdateLineColumn - Current line and column", column, TEST_INPUT.length());
        assertEquals("UpdateLineColumn - End line and column", line, endLine);
        assertEquals("UpdateLineColumn - End line and column", column + TEST_INPUT.length(), endColumn);
        assertEquals("UpdateLineColumn - Begin line and column", START_LINE, beginLine);
        assertEquals("UpdateLineColumn - Begin line and column", START_COLUMN, beginColumn);
    }

    @Test(expected = IOException.class)
    public void testFillBuffWithIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(TEST_INPUT.getBytes());
        SimpleCharStream charStream = new SimpleCharStream(inputStream, START_LINE, START_COLUMN, BUFFER_SIZE);

        SimpleCharStream.FillBuff();

        try {
            char c = charStream.readChar();
        } catch (IOException e) {
            // Expected IOException
        }

        fail("FillBuffWithIOException - No IOException thrown");
    }
}