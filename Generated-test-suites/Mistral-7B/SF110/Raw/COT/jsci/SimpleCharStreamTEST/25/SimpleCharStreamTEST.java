package com.example; // Please replace this package name with the actual one

import org.junit.Assert;
import org.junit.Test;

import JSci.maths.symbolic.SimpleCharStream;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Test class for SimpleCharStream class.
 */
public class SimpleCharStreamTest {

    @Test
    public void testStaticVariables() {
        Assert.assertTrue(SimpleCharStream.staticFlag);
        Assert.assertEquals(4096, SimpleCharStream.bufsize);
    }

    @Test
    public void testExpandBuff() {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        simpleCharStream.ExpandBuff(false);
        Assert.assertEquals(6144, simpleCharStream.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        ByteArrayInputStream inputStream = new ByteArrayInputStream("TestString".getBytes());
        simpleCharStream.inputStream = inputStream;
        char c = simpleCharStream.readChar();
        simpleCharStream.FillBuff();
        c = simpleCharStream.readChar();
        Assert.assertEquals('T', c);
    }

    @Test(expected = IOException.class)
    public void testFillBuffIOException() throws IOException {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        ByteArrayInputStream inputStream = new ByteArrayInputStream("TestString".getBytes());
        simpleCharStream.inputStream = inputStream;
        simpleCharStream.FillBuff();
        simpleCharStream.inputStream = Mockito.mock(IOException.class);
        simpleCharStream.FillBuff();
    }

    @Test
    public void testBeginToken() throws IOException {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        Assert.assertEquals(-1, simpleCharStream.tokenBegin);
        char c = simpleCharStream.BeginToken();
        Assert.assertEquals(c, simpleCharStream.buffer[0]);
        Assert.assertEquals(0, simpleCharStream.tokenBegin);
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        simpleCharStream.UpdateLineColumn('a');
        Assert.assertEquals(1, simpleCharStream.column);
        simpleCharStream.UpdateLineColumn('\n');
        Assert.assertEquals(1, simpleCharStream.line);
        Assert.assertEquals(1, simpleCharStream.column);
        simpleCharStream.UpdateLineColumn('\r');
        Assert.assertEquals(1, simpleCharStream.line);
        Assert.assertEquals(1, simpleCharStream.column);
        simpleCharStream.UpdateLineColumn('\t');
        Assert.assertEquals(1, simpleCharStream.line);
        Assert.assertEquals(9, simpleCharStream.column);
    }

    @Test
    public void testReadChar() throws IOException {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        ByteArrayInputStream inputStream = new ByteArrayInputStream("TestString".getBytes());
        simpleCharStream.inputStream = inputStream;
        char c = simpleCharStream.readChar();
        Assert.assertEquals('T', c);
        c = simpleCharStream.readChar();
        Assert.assertEquals('e', c);
        c = simpleCharStream.readChar();
        Assert.assertEquals('s', c);
        c = simpleCharStream.readChar();
        Assert.assertEquals('t', c);
        c = simpleCharStream.readChar();
        Assert.assertEquals('S', c);
        c = simpleCharStream.readChar();
        Assert.assertEquals('t', c);
        c = simpleCharStream.readChar();
        Assert.assertEquals('r', c);
        Assert.assertEquals(-1, simpleCharStream.readChar());
    }

    @Test
    public void testGetLineAndColumn() {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('\r');
        simpleCharStream.UpdateLineColumn('c');
        Assert.assertEquals(2, simpleCharStream.getLine());
        Assert.assertEquals(5, simpleCharStream.getColumn());
    }

    @Test
    public void testBackup() throws IOException {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        ByteArrayInputStream inputStream = new ByteArrayInputStream("TestString".getBytes());
        simpleCharStream.inputStream = inputStream;
        char c = simpleCharStream.readChar();
        simpleCharStream.backup(1);
        c = simpleCharStream.readChar();
        Assert.assertEquals('T', c);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBackupNegativeAmount() throws IOException {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        simpleCharStream.backup(-1);
    }

    @Test
    public void testReInit() throws IOException {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        ByteArrayInputStream inputStream = new ByteArrayInputStream("TestString1".getBytes());
        simpleCharStream.inputStream = inputStream;
        simpleCharStream.ReInit(Mockito.mock(Reader.class), 1, 1, 4096);
        char c = simpleCharStream.readChar();
        Assert.assertEquals('T', c);
        ByteArrayInputStream inputStream2 = new ByteArrayInputStream("TestString2".getBytes());
        simpleCharStream.inputStream = inputStream2;
        simpleCharStream.ReInit(inputStream2, 2, 2, 4096);
        c = simpleCharStream.readChar();
        Assert.assertEquals('T', c);
    }

    @Test
    public void testGetImage() {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        ByteArrayInputStream inputStream = new ByteArrayInputStream("TestString".getBytes());
        simpleCharStream.inputStream = inputStream;
        String image = SimpleCharStream.GetImage();
        Assert.assertEquals("TestString", image);
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        ByteArrayInputStream inputStream = new ByteArrayInputStream("TestStringTestString".getBytes());
        simpleCharStream.inputStream = inputStream;
        char[] suffix = SimpleCharStream.GetSuffix(6);
        Assert.assertArrayEquals(new char[]{'T', 'e', 's', 't', 'S', 't'}, suffix);
    }

    @Test
    public void testDone() {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        SimpleCharStream.Done();
        Assert.assertNull(SimpleCharStream.buffer);
        Assert.assertNull(SimpleCharStream.bufline);
        Assert.assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream simpleCharStream = new SimpleCharStream(Mockito.mock(Reader.class));
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Line1:Column1 TestString\nLine2:Column1 TestString\nLine3:Column1 TestString\n".getBytes());
        simpleCharStream.inputStream = inputStream;
        simpleCharStream.AdjustBeginLineColumn(3, 4);
        Assert.assertEquals(3, simpleCharStream.getBeginLine());
        Assert.assertEquals(4, simpleCharStream.getBeginColumn());
    }
}