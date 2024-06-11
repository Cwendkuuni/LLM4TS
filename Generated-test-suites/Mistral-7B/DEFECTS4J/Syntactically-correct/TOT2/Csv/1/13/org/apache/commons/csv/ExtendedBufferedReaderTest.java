import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() throws IOException {
        Reader reader = new StringReader("");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        // Add assertions here if necessary
    }

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("Hello, World!");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        int result = readerDecorator.read();
        int expected = 'H';
        assertEquals(expected, result);
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("Hello, World!");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        int result = readerDecorator.read();
        int expected = 'H';
        assertEquals(expected, readerDecorator.readAgain());
    }

    @Test
    public void testReadCharArray() throws IOException {
        Reader reader = new StringReader("Hello, World!");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        char[] buffer = new char[5];
        int result = readerDecorator.read(buffer, 0, buffer.length);
        String expected = "Hello";
        assertEquals(expected, new String(buffer, 0, result));
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("Hello\nWorld!");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        String result = readerDecorator.readLine();
        String expected = "Hello";
        assertEquals(expected, result);
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("Hello, World!");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        int result = readerDecorator.lookAhead();
        int expected = 'H';
        assertEquals(expected, result);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("Hello\nWorld!");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        String line1 = readerDecorator.readLine();
        String line2 = readerDecorator.readLine();
        int result = readerDecorator.getLineNumber();
        int expected = 2;
        assertEquals(expected, result);
    }

    @Test
    public void testEmptyReader() throws IOException {
        Reader reader = new StringReader("");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        // Add assertions here
    }

    @Test
    public void testReadPastEOF() throws IOException {
        Reader reader = new StringReader("Hello, World!");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        readerDecorator.read();
        readerDecorator.read();
        readerDecorator.read(); // This should throw an IOException
    }

    @Test
    public void testReadComment() throws IOException {
        Reader reader = new StringReader("// This is a comment!");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        String result = readerDecorator.readLine();
        String expected = "";
        assertEquals(expected, result);
    }

    @Test
    public void testReadMalformedLine() throws IOException {
        Reader reader = new StringReader("Hello\rWorld!");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        readerDecorator.readLine(); // This should throw an IllegalArgumentException
    }

    @Test
    public void testReadInvalidCharacter() throws IOException {
        Reader reader = new StringReader("\u0000");
        ExtendedBufferedReader readerDecorator = new ExtendedBufferedReader(reader);
        readerDecorator.read(); // This should throw an IOException
    }
}