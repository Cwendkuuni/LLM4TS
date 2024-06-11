package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        String testText = "Line 1\rLine 2\rLine 3";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testText.getBytes());
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(inputStream));

        // Test reading individual characters
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('\r', reader.read()); // Line break
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('\r', reader.read()); // Line break
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('L', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals(' ', reader.read());
        Assert.assertEquals('\r', reader.read()); // Line break
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        // Test reading multiple characters into a char array
        char[] buffer = new char[3];
        Assert.assertEquals(3, reader.read(buffer, 0, buffer.length));
        Assert.assertEquals('L', buffer[0]);
        Assert.assertEquals('i', buffer[1]);
        Assert.assertEquals('n', buffer[2]);

        // Test reading until EOF
        ArrayList<Character> readChars = new ArrayList<>();
        int read;
        while ((read = reader.read()) != ExtendedBufferedReader.END_OF_STREAM) {
            readChars.add((char) read);
        }
        Assert.assertEquals(testText.length(), readChars.size());
        for (int i = 0; i < testText.length(); i++) {
            Assert.assertEquals(testText.charAt(i), readChars.get(i));
        }

        // Test reading past EOF
        try {
            reader.read();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }

        // Test reading line by line
        String line;
        while ((line = reader.readLine()) != null) {
            Assert.assertEquals("Line 1", line);
            Assert.assertEquals("Line 2", reader.readLine());
            Assert.assertEquals("Line 3", reader.readLine());
            Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
        }

        // Test lookahead
        Assert.assertEquals('L', reader.lookAhead());
        Assert.assertEquals('L', reader.lookAhead());
        Assert.assertEquals('\r', reader.lookAhead()); // Line break
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());

        // Test getLineNumber
        Assert.assertEquals(3, reader.getLineNumber());
    }
}