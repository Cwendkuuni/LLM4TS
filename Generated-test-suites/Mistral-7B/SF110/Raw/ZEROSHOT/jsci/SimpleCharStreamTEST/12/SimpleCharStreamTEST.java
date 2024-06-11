package org.junit.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputstream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testStaticVariables() {
        assertEquals(true, SimpleCharStream.staticFlag);
        assertNull(SimpleCharStream.bufsize);
        assertNull(SimpleCharStream.available);
        assertNull(SimpleCharStream.tokenBegin);
        assertNull(SimpleCharStream.bufpos);
        assertNotNull(SimpleCharStream.bufline);
        assertNotNull(SimpleCharStream.bufcolumn);
        assertNull(SimpleCharStream.column);
        assertNull(SimpleCharStream.line);
        assertFalse(SimpleCharStream.prevCharIsCR);
        assertFalse(SimpleCharStream.prevCharIsLF);
        assertNull(SimpleCharStream.inputStream);
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.maxNextCharInd);
        assertNull(SimpleCharStream.inBuf);
    }

    @Test
    public void testConstructor() {
        SimpleCharStream charStream = new SimpleCharStream(new InputStreamReader(new ByteArrayInputstream(new byte[] { 'A' })));
        assertSame(1, charStream.getLine());
        assertSame(1, charStream.getBeginLine());
        assertSame(0, charStream.getColumn());
        assertSame(0, charStream.getBeginColumn());
        assertEquals('A', charStream.readChar());
        assertEquals('A', charStream.BeginToken());
        charStream.backup(1);
        assertEquals('A', charStream.readChar());

        SimpleCharStream charStream2 = new SimpleCharStream(new InputStreamReader(new ByteArrayInputstream(new byte[] { 'A', '\r', '\n', 'B' })));
        assertSame(1, charStream2.getLine());
        assertSame(1, charStream2.getBeginLine());
        assertSame(0, charStream2.getColumn());
        assertSame(0, charStream2.getBeginColumn());
        assertEquals('A', charStream2.readChar());
        assertEquals('\r', charStream2.readChar());
        assertEquals('\n', charStream2.readChar());
        assertEquals('B', charStream2.readChar());
        assertEquals('B', charStream2.BeginToken());
        charStream2.backup(3);
        assertEquals('A', charStream2.readChar());
        assertEquals('\r', charStream2.readChar());
        assertEquals('\n', charStream2.readChar());
    }

    @Test
    public void testMethods() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new InputStreamReader(new ByteArrayInputstream(new byte[] { 'A', '\r', '\n', 'B', '\r', '\n', 'C' })));
        assertEquals(2, charStream.getLine());
        assertEquals(2, charStream.getEndLine());
        assertEquals(7, charStream.getColumn());
        assertEquals(0, charStream.getBeginColumn());
        assertEquals('A', charStream.readChar());
        assertEquals('\r', charStream.readChar());
        assertEquals('\n', charStream.readChar());
        assertEquals('B', charStream.readChar());
        assertEquals('\r', charStream.readChar());
        assertEquals('\n', charStream.readChar());
        assertEquals('C', charStream.readChar());
        assertEquals('C', charStream.BeginToken());
        charStream.backup(6);
        assertEquals('B', charStream.readChar());
        assertEquals('\r', charStream.readChar());
        assertEquals('\n', charStream.readChar());
        assertEquals('A', charStream.readChar());
        assertEquals('\r', charStream.readChar());
        assertEquals('\n', charStream.readChar());

        charStream.UpdateLineColumn('\r');
        assertEquals(2, charStream.getLine());
        charStream.UpdateLineColumn('\n');
        assertEquals(3, charStream.getLine());

        charStream.backup(1);
        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        assertEquals(3, charStream.getLine());

        charStream.UpdateLineColumn('\t');
        assertEquals(3, charStream.getLine());
        assertEquals(12, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        assertEquals(4, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn(' ');
        assertEquals(4, charStream.getLine());
        assertEquals(2, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        assertEquals(5, charStream.getLine());
        assertEquals(1, charStream.getColumn());

        charStream.UpdateLineColumn('A');
        charStream.UpdateLineColumn('B');
        charStream.UpdateLineColumn('C');
        assertEquals(5, charStream.getLine());
        assertEquals(4, charStream.getColumn());

        charStream.UpdateLineColumn('\r');
        charStream.UpdateLineColumn('\n');
        assertEquals(6, charStream.getLine());
        assertEquals(1, charStream.getColumn());
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new InputStreamReader(new ByteArrayInputstream(new byte[] { 'A' })));
        charStream.FillBuff();
        charStream.inputStream.close();
        charStream.readChar();
    }

    @Test
    public void testExpandBuff() throws IOException {
        SimpleCharStream charStream = new SimpleCharStream(new InputStreamReader(new ByteArrayInputstream(new byte[] { 'A', '\r', '\n', 'B', '\r', '\n', 'C' })));
        charStream.ExpandBuff(true);
        assertEquals(4064, charStream.bufsize);
        assertEquals(17, charStream.maxNextCharInd);
        assertEquals(15, charStream.available);
        assertEquals(5, charStream.getLine());
        assertEquals(1, charStream.getColumn());
        assertEquals('B', charStream.readChar());
        assertEquals('\r', charStream.readChar());
        assertEquals('\n', charStream.readChar());
        assertEquals('C', charStream.readChar());
        assertEquals('C', charStream.BeginToken());

        charStream.ExpandBuff(false);
        assertEquals(2048, charStream.bufsize);

        SimpleCharStream charStream2 = new SimpleCharStream(new InputStreamReader(new ByteArrayInputstream(new byte[] { 'A', '\r', '\n', 'B', '\r', '\n', 'C' })));
        charStream2.ExpandBuff(false);
        assertEquals(2048, charStream2.bufsize);
        assertEquals(21, charStream2.available);
        assertEquals(5, charStream2.getLine());
        assertEquals(1, charStream2.getColumn());
        assertEquals('B', charStream2.readChar());
        assertEquals('\r', charStream2.readChar());
        assertEquals('\n', charStream2.readChar());
        assertEquals('C', charStream2.readChar());
        assertEquals('C', charStream2.BeginToken());
    }

    @Test
    public void testGetImage() {
        SimpleCharStream charStream = new SimpleCharStream(new InputStreamReader(new ByteArrayInputstream(new byte[] { 'A', '\r', '\n', 'B', '\r', '\n', 'C' })));
        assertEquals("AB\r\nC", SimpleCharStream.GetImage());

        charStream.backup(3);
        charStream.backup(1);
        assertEquals("B\r\nAB\r\nC", SimpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream charStream = new SimpleCharStream(new InputStreamReader(new ByteArrayInputstream(new byte[] { 'A', '\r', '\n', 'B', '\r', '\n', 'C' })));
        assertEquals("C", SimpleCharStream.GetSuffix(3));

        charStream.backup(3);
        charStream.backup(1);
        assertEquals("AB", SimpleCharStream.GetSuffix(2));
    }

    @Test
    public void testDone() {
        SimpleCharStream charStream = new SimpleCharStream(new InputStreamReader(new ByteArrayInputstream(new byte[] { 'A', '\r', '\n', 'B', '\r', '\n', 'C' })));
        SimpleCharStream.Done();
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream charStream = new SimpleCharStream(new InputStreamReader(new ByteArrayInputstream(new byte[] { 'A', '\r', '\n', 'B', '\r', '\n', 'C' })));
        SimpleCharStream.adjustBeginLineColumn(3, 5);
        assertEquals(3, charStream.getLine());
        assertEquals(5, charStream.getColumn());
    }
}