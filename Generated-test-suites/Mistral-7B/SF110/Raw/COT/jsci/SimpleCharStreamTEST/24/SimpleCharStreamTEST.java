
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleCharStreamTest {
    private SimpleCharStream simpleCharStream;

    @Before
    public void setUp() {
        try {
            simpleCharStream = new SimpleCharStream(new StringReader("Test string"), 1, 1, 4096);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExpandBuff() {
        simpleCharStream.ExpandBuff(false);
        int initialSize = simpleCharStream.bufsize;
        simpleCharStream.ExpandBuff(true);
        int newSize = simpleCharStream.bufsize;

        assertTrue(initialSize < newSize);
    }

    @Test
    public void testFillBuff() throws IOException {
        simpleCharStream.FillBuff();
        char c = simpleCharStream.readChar();
        simpleCharStream.UpdateLineColumn(c);
        int line = simpleCharStream.getLine();
        int column = simpleCharStream.getColumn();

        assertEquals(1, line);
        assertEquals(1, column);

        simpleCharStream.FillBuff();
        c = simpleCharStream.readChar();
        simpleCharStream.UpdateLineColumn(c);

        assertEquals(1, line);
        assertEquals(2, column);
    }

    @Test
    public void testBeginToken() throws IOException {
        char c = simpleCharStream.BeginToken();
        int line = simpleCharStream.getLine();
        int column = simpleCharStream.getColumn();

        assertEquals(-1, line);
        assertEquals(0, column);
        assertEquals(c, simpleCharStream.readChar());

        line = simpleCharStream.getLine();
        column = simpleCharStream.getColumn();

        assertEquals(1, line);
        assertEquals(1, column);
    }

    @Test
    public void testUpdateLineColumn() throws IOException {
        for (char c = '\r'; c <= '\f'; c++) {
            simpleCharStream.UpdateLineColumn(c);
            int line = simpleCharStream.getLine();
            int column = simpleCharStream.getColumn();

            if (c == '\r') {
                assertEquals(1, line);
                assertEquals(1, column);
            } else {
                assertEquals(1, line);
                assertEquals(column + 1, column);
            }
        }

        simpleCharStream.UpdateLineColumn('\n');
        line = simpleCharStream.getLine();
        column = simpleCharStream.getColumn();

        assertEquals(1, line);
        assertEquals(1, column);

        simpleCharStream.UpdateLineColumn('\t');
        line = simpleCharStream.getLine();
        column = simpleCharStream.getColumn();

        assertEquals(1, line);
        assertEquals(9, column);

        simpleCharStream.UpdateLineColumn('\n');
        line = simpleCharStream.getLine();
        column = simpleCharStream.getColumn();

        assertEquals(2, line);
        assertEquals(1, column);
    }

    @Test(expected = IOException.class)
    public void testReadCharWhenEndOfStream() throws IOException {
        simpleCharStream.FillBuff();
        simpleCharStream.readChar();
        simpleCharStream.readChar();
        simpleCharStream.readChar();

        simpleCharStream.readChar();
    }

    @Test(expected = IOException.class)
    public void testFillBuffWhenIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Error".getBytes());
        SimpleCharStream simpleCharStream = new SimpleCharStream(inputStream, 1, 1, 4096);

        simpleCharStream.FillBuff();
    }
}