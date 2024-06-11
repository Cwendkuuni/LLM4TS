import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleCharStreamTest {

    private static final int START_LINE = 1;
    private static final int START_COLUMN = 1;
    private static final int BUFFER_SIZE = 1024;

    private SimpleCharStream charStream;
    private Reader inputStreamMock;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() throws IOException {
        inputStreamMock = mock(Reader.class);
        when(inputStreamMock.read(eq(0), anyInt(), anyInt())).thenReturn(-1);
        byteArrayInputStream = new ByteArrayInputStream("Test String".getBytes());
        charStream = new SimpleCharStream(byteArrayInputStream, START_LINE, START_COLUMN, BUFFER_SIZE);
    }

    @Test
    public void testStaticFlag() {
        assertTrue(SimpleCharStream.staticFlag);
    }

    @Test
    public void testConstructor_Reader_int_int() throws IOException {
        charStream = new SimpleCharStream(inputStreamMock, START_LINE, START_COLUMN, BUFFER_SIZE);
        assertEquals(START_LINE, charStream.getBeginLine());
        assertEquals(START_COLUMN, charStream.getBeginColumn());
        assertEquals(BUFFER_SIZE, charStream.bufsize);
    }

    @Test
    public void testConstructor_Reader() throws IOException {
        charStream = new SimpleCharStream(inputStreamMock);
        assertEquals(1, charStream.getBeginLine());
        assertEquals(1, charStream.getBeginColumn());
        assertEquals(4096, charStream.bufsize);
    }

    @Test
    public void testReInit_Reader_int_int() throws IOException {
        charStream.ReInit(inputStreamMock, START_LINE, START_COLUMN, BUFFER_SIZE);
        assertEquals(START_LINE, charStream.getBeginLine());
        assertEquals(START_COLUMN, charStream.getBeginColumn());
        assertEquals(BUFFER_SIZE, charStream.bufsize);
    }

    @Test
    public void testReInit_Reader() throws IOException {
        charStream.ReInit(inputStreamMock);
        assertEquals(1, charStream.getBeginLine());
        assertEquals(1, charStream.getBeginColumn());
        assertEquals(4096, charStream.bufsize);
    }

    @Test
    public void testReInit_InputStream_int_int() throws IOException {
        charStream.ReInit(new InputStreamReader(byteArrayInputStream), START_LINE, START_COLUMN, BUFFER_SIZE);
        assertEquals("Test String".charAt(0), charStream.readChar());
        assertEquals(START_LINE, charStream.getBeginLine());
        assertEquals(START_COLUMN, charStream.getBeginColumn());
        assertEquals(BUFFER_SIZE, charStream.bufsize);
    }

    @Test
    public void testReInit_InputStream() throws IOException {
        charStream.ReInit(new InputStreamReader(byteArrayInputStream));
        assertEquals("Test String".charAt(0), charStream.readChar());
        assertEquals(1, charStream.getBeginLine());
        assertEquals(1, charStream.getBeginColumn());
        assertEquals(4096, charStream.bufsize);
    }

    @Test
    public void testFillBuff() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        char c = '\0';
        while (c != '\n') {
            c = charStream.readChar();
        }
        verify(inputStreamMock, times(5)).read(anyInt(), anyInt(), anyInt());
    }

    @Test(expected = IOException.class)
    public void testBeginToken_IOException() throws IOException {
        when(inputStreamMock.read(eq(0), anyInt(), anyInt())).thenThrow(new IOException());
        charStream = new SimpleCharStream(inputStreamMock, START_LINE, START_COLUMN, BUFFER_SIZE);
        charStream.BeginToken();
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        char c = 'a';
        for (int i = 0; i < "Test String".length(); ++i) {
            c = charStream.readChar();
            UpdateLineColumn(c);
        }
        assertEquals(7, charStream.getLine());
        assertEquals(18, charStream.getColumn());
    }

    @Test
    public void testGetLine() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        for (int i = 0; i < 5; ++i) {
            charStream.readChar();
        }
        assertEquals(1, charStream.getLine());
        charStream.readChar();
        assertEquals(2, charStream.getLine());
    }

    @Test
    public void testGetColumn() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        for (int i = 0; i < 5; ++i) {
            charStream.readChar();
        }
        assertEquals(1, charStream.getColumn());
        charStream.readChar();
        assertEquals(2, charStream.getColumn());
    }

    @Test
    public void testGetEndLine() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        for (int i = 0; i < "Test String".length(); ++i) {
            charStream.readChar();
        }
        assertEquals(7, charStream.getLine());
    }

    @Test
    public void testGetEndColumn() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        for (int i = 0; i < "Test String".length(); ++i) {
            charStream.readChar();
        }
        assertEquals(18, charStream.getColumn());
    }

    @Test
    public void testGetBeginLine() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        for (int i = 0; i < 5; ++i) {
            charStream.readChar();
        }
        assertEquals(1, charStream.getBeginLine());
    }

    @Test
    public void testGetBeginColumn() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        for (int i = 0; i < 5; ++i) {
            charStream.readChar();
        }
        assertEquals(1, charStream.getBeginColumn());
    }

    @Test
    public void testBackup() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        char c = '\0';
        char backupChar = '\0';
        for (int i = 0; i < 6; ++i) {
            c = charStream.readChar();
            backupChar = charStream.readChar();
            charStream.backup(1);
            assertEquals(c, backupChar);
        }
    }

    @Test
    public void testExpandBuff_WrapAround() throws IOException {
        charStream.bufsize = 10;
        charStream.ExpandBuff(true);
        assertEquals(12064, charStream.bufsize);
    }

    @Test
    public void testExpandBuff_DontWrapAround() throws IOException {
        charStream.bufsize = 10;
        charStream.ExpandBuff(false);
        assertEquals(1024, charStream.bufsize);
    }

    @Test
    public void testGetImage() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        String image = SimpleCharStream.GetImage();
        assertEquals("Test String", image);
    }

    @Test
    public void testGetSuffix() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        char[] suffix = SimpleCharStream.GetSuffix(5);
        assertArrayEquals("String".toCharArray(), suffix);
    }

    @Test
    public void testDone() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        SimpleCharStream.Done();
        assertNull(SimpleCharStream.buffer);
        assertNull(SimpleCharStream.bufline);
        assertNull(SimpleCharStream.bufcolumn);
    }

    @Test
    public void testAdjustBeginLineColumn() throws IOException {
        charStream.ReInit(byteArrayInputStream);
        SimpleCharStream.adjustBeginLineColumn(2, 4);
        assertEquals(2, charStream.getBeginLine());
        assertEquals(4, charStream.getBeginColumn());
    }
}