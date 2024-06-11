import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SimpleCharStreamTest {

    private SimpleCharStream simpleCharStream;
    private InputStream inputStreamMock;

    @Before
    public void setUp() {
        inputStreamMock = mock(InputStream.class);
        simpleCharStream = new SimpleCharStream(inputStreamMock);
    }

    @Test
    public void testBeginToken() throws IOException {
        when(inputStreamMock.read()).thenReturn('a').thenReturn('b').thenReturn('c').thenReturn(-1);

        assertEquals('a', simpleCharStream.BeginToken());
        assertEquals('a', simpleCharStream.readChar());

        verify(inputStreamMock).read();
    }

    @Test(expected = IOException.class)
    public void testBeginTokenWithIOException() throws IOException {
        when(inputStreamMock.read()).thenThrow(new IOException("Test IOException"));

        simpleCharStream.BeginToken();
    }

    @Test
    public void testExpandBuff() {
        simpleCharStream.ExpandBuff(true);
        assertEquals(SimpleCharStream.bufsize + 2048, simpleCharStream.bufsize);
        simpleCharStream.ExpandBuff(false);
        assertEquals(SimpleCharStream.bufsize - 2048, simpleCharStream.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        when(inputStreamMock.read()).thenReturn(-1);

        simpleCharStream.FillBuff();
        verify(inputStreamMock).close();
        verify(inputStreamMock).read();

        ByteArrayInputStream inputStreamByteArray = new ByteArrayInputStream("Test".getBytes());
        simpleCharStream = new SimpleCharStream(inputStreamByteArray);
        simpleCharStream.FillBuff();
        assertEquals('T', simpleCharStream.readChar());
        assertEquals('e', simpleCharStream.readChar());
        assertEquals('s', simpleCharStream.readChar());
    }

    @Test
    public void testGetBeginColumn() {
        simpleCharStream.backup(1);
        simpleCharStream.UpdateLineColumn('a');
        assertEquals(0, simpleCharStream.getBeginColumn());

        simpleCharStream.backup(3);
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');
        assertEquals(1, simpleCharStream.getBeginColumn());
    }

    @Test
    public void testGetBeginLine() {
        assertEquals(1, simpleCharStream.getBeginLine());

        simpleCharStream.backup(1);
        simpleCharStream.UpdateLineColumn('a');
        assertEquals(1, simpleCharStream.getBeginLine());

        simpleCharStream.backup(2);
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');
        assertEquals(2, simpleCharStream.getBeginLine());
    }

    @Test
    public void testGetColumn() {
        simpleCharStream.backup(1);
        simpleCharStream.UpdateLineColumn('a');
        assertEquals(1, simpleCharStream.getColumn());

        simpleCharStream.UpdateLineColumn('b');
        assertEquals(2, simpleCharStream.getColumn());
    }

    @Test
    public void testGetEndColumn() {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');
        assertEquals(3, simpleCharStream.getEndColumn());
    }

    @Test
    public void testGetEndLine() {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');
        assertEquals(2, simpleCharStream.getEndLine());
    }

    @Test
    public void testGetLine() {
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');
        assertEquals(2, simpleCharStream.getLine());
    }

    @Test
    public void testReadChar() throws IOException {
        when(inputStreamMock.read()).thenReturn('a').thenReturn('b').thenReturn('c').thenReturn(-1);

        assertEquals('a', simpleCharStream.readChar());
        assertEquals('a', simpleCharStream.readChar());
        assertEquals('b', simpleCharStream.readChar());

        verify(inputStreamMock, times(3)).read();
    }

    @Test
    public void testUpdateLineColumn() {
        simpleCharStream.UpdateLineColumn('r');
        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('a');
        assertEquals(2, simpleCharStream.line);
        assertEquals(1, simpleCharStream.column);

        simpleCharStream.UpdateLineColumn('\t');
        assertEquals(2, simpleCharStream.line);
        assertEquals(9, simpleCharStream.column);
    }

    @Test
    public void testBackup() {
        simpleCharStream.UpdateLineColumn('a').UpdateLineColumn('b').UpdateLineColumn('c');
        simpleCharStream.backup(2);
        assertEquals(2, simpleCharStream.line);
        assertEquals(2, simpleCharStream.column);

        simpleCharStream.backup(3);
        assertEquals(2, simpleCharStream.line);
        assertEquals(1, simpleCharStream.column);
    }

    @Test
    public void testReInit() {
        InputStream inputStreamByteArray = new ByteArrayInputStream("Test".getBytes());
        simpleCharStream = new SimpleCharStream(inputStreamByteArray);
        simpleCharStream.ReInit(inputStreamMock, 3, 4);
        assertEquals(inputStreamMock, simpleCharStream.inputStream);
        assertEquals(3, simpleCharStream.line);
        assertEquals(4, simpleCharStream.column);
    }

    @Test
    public void testReInitWithNullInputStream() {
        simpleCharStream = new SimpleCharStream();
        simpleCharStream.ReInit(null, 3, 4);
        assertNull(simpleCharStream.inputStream);
    }

    @Test
    public void testDone() {
        simpleCharStream.Done();
        assertNull(simpleCharStream.buffer);
        assertNull(simpleCharStream.bufline);
        assertNull(simpleCharStream.bufcolumn);
    }

    @Test
    public void testGetImage() {
        ByteArrayInputStream inputStreamByteArray = new ByteArrayInputStream("Test\nTest2".getBytes());
        simpleCharStream = new SimpleCharStream(inputStreamByteArray);
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');
        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');
        assertEquals("abcd\ne", simpleCharStream.GetImage());
    }

    @Test
    public void testGetSuffix() {
        ByteArrayInputStream inputStreamByteArray = new ByteArrayInputStream("Test\nTest2".getBytes());
        simpleCharStream = new SimpleCharStream(inputStreamByteArray);
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');
        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');
        simpleCharStream.backup(5);
        char[] suffix = simpleCharStream.GetSuffix(3);
        assertEquals("st\nTest", new String(suffix));
    }

    @Test
    public void testAdjustBeginLineColumn() {
        InputStream inputStreamByteArray = new ByteArrayInputStream("Test\nTest\nTest\nTest\nTest".getBytes());
        simpleCharStream = new SimpleCharStream(inputStreamByteArray);
        simpleCharStream.UpdateLineColumn('a');
        simpleCharStream.UpdateLineColumn('b');
        simpleCharStream.UpdateLineColumn('c');
        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('d');
        simpleCharStream.UpdateLineColumn('e');
        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('f');
        simpleCharStream.UpdateLineColumn('g');
        simpleCharStream.UpdateLineColumn('h');
        simpleCharStream.UpdateLineColumn('\n');
        simpleCharStream.UpdateLineColumn('i');
        simpleCharStream.UpdateLineColumn('j');
        simpleCharStream.UpdateLineColumn('k');

        simpleCharStream.adjustBeginLineColumn(3, 3);
        assertEquals(3, simpleCharStream.line);
        assertEquals(3, simpleCharStream.column);

        simpleCharStream.backup(11);
        simpleCharStream.adjustBeginLineColumn(5, 5);
        assertEquals(5, simpleCharStream.line);
        assertEquals(5, simpleCharStream.column);
    }
}