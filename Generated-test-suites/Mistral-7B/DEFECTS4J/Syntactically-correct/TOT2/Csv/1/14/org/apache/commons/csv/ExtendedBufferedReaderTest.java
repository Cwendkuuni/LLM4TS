import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testReadNormalCharacter() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        int actual = reader.read();
        int expected = 'H';
        assertEquals(expected, actual);
    }

    @Test
    public void testReadEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        reader.read();
        reader.read();
        int actual = reader.read();
        int expected = ExtendedBufferedReader.END_OF_STREAM;
        assertEquals(expected, actual);
    }

    @Test
    public void testReadArrayCharacter() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        char[] buffer = new char[2];
        int actual = reader.read(buffer, 0, buffer.length);
        int expected = 2;
        assertEquals(expected, actual);
        assertEquals('H', buffer[0]);
        assertEquals('e', buffer[1]);
    }

    @Test
    public void testReadLineNormal() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\n"));
        String actual = reader.readLine();
        String expected = "Hello World";
        assertEquals(expected, actual);
    }

    @Test
    public void testReadLineEmpty() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        String actual = reader.readLine();
        String expected = null;
        assertEquals(expected, actual);
    }

    @Test
    public void testLookAheadCharacter() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        int actual = reader.lookAhead();
        int expected = 'H';
        assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testLookAheadEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World"));
        reader.read();
        reader.read();
        int actual = reader.lookAhead();
        //No character should be returned at end of stream
    }

    @Test
    public void testGetLineNumber() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello World\nHello Again\n"));
        int actual = reader.getLineNumber();
        int expected = 2;
        assertEquals(expected, actual);
    }
}