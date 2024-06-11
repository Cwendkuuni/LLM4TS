package com.example.junit; // replace with your package name

import org.junit.Assert;
import org.junit.Test;

import JSci.maths.symbolic.SimpleCharStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        char actual = charStream.BeginToken();
        char expected = 't';
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testBeginTokenWithIOException() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        charStream.inputStream.close();
        charStream.BeginToken();
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\tline2".getBytes()), 1, 1);
        char charToTest = 't';
        charStream.readChar();
        charStream.UpdateLineColumn(charToTest);
        int expectedLine = 1;
        int expectedColumn = 1;
        int actualLine = charStream.getLine();
        int actualColumn = charStream.getColumn();
        Assert.assertEquals(expectedLine, actualLine);
        Assert.assertEquals(expectedColumn, actualColumn);

        charStream.UpdateLineColumn('\n');
        expectedLine = 2;
        expectedColumn = 1;
        actualLine = charStream.getLine();
        actualColumn = charStream.getColumn();
        Assert.assertEquals(expectedLine, actualLine);
        Assert.assertEquals(expectedColumn, actualColumn);

        charStream.UpdateLineColumn('\t');
        expectedColumn = 9;
        actualColumn = charStream.getColumn();
        Assert.assertEquals(expectedColumn, actualColumn);

        charStream.UpdateLineColumn('\r');
        expectedLine = 3;
        expectedColumn = 1;
        actualLine = charStream.getLine();
        actualColumn = charStream.getColumn();
        Assert.assertEquals(expectedLine, actualLine);
        Assert.assertEquals(expectedColumn, actualColumn);

        charStream.UpdateLineColumn('\n');
        expectedLine = 4;
        expectedColumn = 1;
        actualLine = charStream.getLine();
        actualColumn = charStream.getColumn();
        Assert.assertEquals(expectedLine, actualLine);
        Assert.assertEquals(expectedColumn, actualColumn);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        char actual = charStream.readChar();
        char expected = 't';
        Assert.assertEquals(expected, actual);

        charStream.UpdateLineColumn(actual); // update line and column after reading a character
        int expectedLine = 1;
        int expectedColumn = 1;
        int actualLine = charStream.getLine();
        int actualColumn = charStream.getColumn();
        Assert.assertEquals(expectedLine, actualLine);
        Assert.assertEquals(expectedColumn, actualColumn);
    }

    @Test
    public void testGetLineAndColumn() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\tline2".getBytes()), 1, 1);
        charStream.UpdateLineColumn('t');
        int expectedLine1 = 1;
        int expectedColumn1 = 1;
        int actualLine1 = charStream.getLine();
        int actualColumn1 = charStream.getColumn();
        Assert.assertEquals(expectedLine1, actualLine1);
        Assert.assertEquals(expectedColumn1, actualColumn1);

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        int expectedLine2 = 2;
        int expectedColumn2 = 1;
        int actualLine2 = charStream.getLine();
        int actualColumn2 = charStream.getColumn();
        Assert.assertEquals(expectedLine2, actualLine2);
        Assert.assertEquals(expectedColumn2, actualColumn2);

        charStream.UpdateLineColumn('\t');
        int expectedColumn3 = 9;
        int actualColumn3 = charStream.getColumn();
        Assert.assertEquals(expectedColumn3, actualColumn3);

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        int expectedLine3 = 3;
        int expectedColumn3 = 1;
        int actualLine3 = charStream.getLine();
        int actualColumn3 = charStream.getColumn();
        Assert.assertEquals(expectedLine3, actualLine3);
        Assert.assertEquals(expectedColumn3, actualColumn3);

        charStream.UpdateLineColumn('\n');
        int expectedLine4 = 4;
        int expectedColumn4 = 1;
        int actualLine4 = charStream.getLine();
        int actualColumn4 = charStream.getColumn();
        Assert.assertEquals(expectedLine4, actualLine4);
        Assert.assertEquals(expectedColumn4, actualColumn4);
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        charStream.UpdateLineColumn('t');
        int lineBeforeBackup = charStream.getLine();
        int columnBeforeBackup = charStream.getColumn();
        charStream.backup(1);
        int lineAfterBackup = charStream.getLine();
        int columnAfterBackup = charStream.getColumn();
        Assert.assertEquals(lineBeforeBackup, lineAfterBackup);
        Assert.assertEquals(columnBeforeBackup + 1, columnAfterBackup);
    }

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test\r\nline1\tline2\r\nline3\tline4\r\nline5\r\nline6\r\nline7\r\nline8\r\nline9\r\nline10\r\nline11\r\nline12\r\nline13\r\nline14\r\nline15\r\nline16\r\nline17\r\nline18\r\nline19\r\nline20\r\nline21\r\nline22\r\nline23\r\nline24\r\nline25".getBytes()), 1, 1);
        int availableBeforeExpand = charStream.available;
        charStream.ExpandBuff(true);
        int availableAfterExpand = charStream.available;
        Assert.assertTrue(availableAfterExpand > availableBeforeExpand);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        char expected = 't';
        char actual = charStream.readChar();
        Assert.assertEquals(expected, actual);

        int availableBeforeFill = charStream.available;
        charStream.FillBuff();
        int availableAfterFill = charStream.available;
        Assert.assertTrue(availableAfterFill > availableBeforeFill);

        char actualAfterFill = charStream.readChar();
        Assert.assertEquals(expected, actualAfterFill);
    }

    @Test(expected = IOException.class)
    public void testFillBuffWithIOException() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new ByteArrayInputStream("test".getBytes()), 1, 1);
        charStream.inputStream.close();
        charStream.FillBuff();
    }

    
}