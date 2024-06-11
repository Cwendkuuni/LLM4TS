package JSci.maths.symbolic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() throws IOException {
        Reader mockInput = mock(Reader.class);
        SimpleCharStream scs = new SimpleCharStream(mockInput);
        char expected = 'A';
        when(mockInput.read(ByteArrayInputStream.class)).thenReturn(expected);

        char actual = scs.BeginToken();

        assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testBeginToken_IOException() throws IOException {
        Reader mockInput = mock(Reader.class);
        when(mockInput.read(ByteArrayInputStream.class)).thenThrow(IOException.class);

        SimpleCharStream scs = new SimpleCharStream(mockInput);

        scs.BeginToken();
    }

    @Test
    public void testFillBuff() throws IOException {
        Reader mockInput = mock(Reader.class);
        SimpleCharStream scs = new SimpleCharStream(mockInput);
        char[] expected = new char[]{'A', 'B', 'C'};
        when(mockInput.read(expected)).thenReturn(3);

        SimpleCharStream.FillBuff();

        char[] actual = SimpleCharStream.buffer;

        assertArrayEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testFillBuff_IOException() throws IOException {
        Reader mockInput = mock(Reader.class);
        when(mockInput.read(ByteArrayInputStream.class)).thenThrow(IOException.class);

        SimpleCharStream scs = new SimpleCharStream(mockInput);

        SimpleCharStream.FillBuff();
    }

    @Test
    public void testGetBeginColumn() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1);
        int expected = 0;

        int actual = scs.getBeginColumn();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetBeginLine() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1);
        int expected = 1;

        int actual = scs.getBeginLine();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetColumn() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1);
        int expected = 0;

        int actual = scs.getColumn();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetEndColumn() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1);
        int expected = 0;

        int actual = scs.getEndColumn();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetEndLine() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1);
        int expected = 1;

        int actual = scs.getEndLine();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetImage() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 5);
        char[] expected = new char[]{'A', 'B', 'C', 'D', 'E'};

        char[] actual = SimpleCharStream.GetImage();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetLine() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1);
        int expected = 1;

        int actual = scs.getLine();

        assertEquals(expected, actual);
    }

    @Test
    public void testReadChar() throws IOException {
        Reader mockInput = mock(Reader.class);
        when(mockInput.read(ByteArrayInputStream.class)).thenReturn('A');

        SimpleCharStream scs = new SimpleCharStream(mockInput);
        char expected = 'A';

        char actual = scs.readChar();

        assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testReadChar_IOException() throws IOException {
        Reader mockInput = mock(Reader.class);
        when(mockInput.read(ByteArrayInputStream.class)).thenThrow(IOException.class);

        SimpleCharStream scs = new SimpleCharStream(mockInput);

        scs.readChar();
    }

    @Test
    public void testReInit() throws IOException {
        Reader mockInput = mock(Reader.class);
        SimpleCharStream scs = new SimpleCharStream(mockInput, 1, 1, 5);
        char[] expected = new char[]{'A', 'B', 'C', 'D', 'E'};

        when(mockInput.read(expected)).thenReturn(5);

        scs.ReInit(mockInput, 1, 1, 5);

        char[] actual = SimpleCharStream.buffer;

        assertArrayEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testReInit_IOException() throws IOException {
        Reader mockInput = mock(Reader.class);
        SimpleCharStream scs = new SimpleCharStream(mockInput, 1, 1, 5);

        when(mockInput.read(ByteArrayInputStream.class)).thenThrow(IOException.class);

        scs.ReInit(mockInput, 1, 1, 5);
    }

    @Test
    public void testUpdateLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1);
        char c = 'A';

        scs.UpdateLineColumn(c);

        assertEquals(0, SimpleCharStream.column);
        assertEquals(1, SimpleCharStream.line);
        assertEquals(1, SimpleCharStream.column);
        assertEquals(1, SimpleCharStream.line);
    }

    @Test
    public void testBackup() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 5);
        int amount = 1;

        scs.backup(amount);

        assertEquals(4, SimpleCharStream.bufpos);
    }

    @Test
    public void testExpandBuff() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 5);

        scs.ExpandBuff(false);

        assertEquals(1025, SimpleCharStream.bufsize);
    }

    @Test
    public void testGetSuffix() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 10);
        int len = 5;

        char[] actual = SimpleCharStream.GetSuffix(len);

        assertArrayEquals(new char[0], actual);
    }

    @Test
    public void testDone() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 5);

        SimpleCharStream.Done();

        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() {
        SimpleCharStream scs = new SimpleCharStream(mock(Reader.class), 1, 1, 10);
        int newLine = 2;
        int newCol = 5;

        SimpleCharStream.adjustBeginLineColumn(newLine, newCol);

        assertEquals(2, SimpleCharStream.line);
        assertEquals(5, SimpleCharStream.column);
    }
}