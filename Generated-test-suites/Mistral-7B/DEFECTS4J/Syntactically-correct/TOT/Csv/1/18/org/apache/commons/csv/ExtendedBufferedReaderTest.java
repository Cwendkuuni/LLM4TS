package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test class for ExtendedBufferedReader
 */
public class ExtendedBufferedReaderTest {

    /**
     * Test default constructor
     */
    @Test
    public void testDefaultConstructor() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("line1\nline2\nline3".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
         int expectedLineCounter = 3;
         int expectedLastChar = 3; // last character of "line3"

         // Test read method
         Assert.assertEquals(expectedLastChar, extendedBufferedReader.read());
         Assert.assertEquals(expectedLineCounter, extendedBufferedReader.getLineNumber());

         // Test readAgain method
         Assert.assertEquals(expectedLastChar, extendedBufferedReader.readAgain());

         // Test readLine method
         String line = extendedBufferedReader.readLine();
         Assert.assertEquals("line1", line);
         Assert.assertEquals(expectedLineCounter + 1, extendedBufferedReader.getLineNumber());
         Assert.assertEquals(-1, extendedBufferedReader.read()); // EOF

         // Test lookAhead method
         int lookAheadChar = extendedBufferedReader.lookAhead();
         Assert.assertEquals(-1, lookAheadChar); // EOF

         // Test read method with char array
         char[] charArray = new char[3];
         int readLength = extendedBufferedReader.read(charArray);
         Assert.assertEquals(3, readLength);
         Assert.assertEquals("line1".toCharArray(), charArray, false);

         // Test readLine method with empty line
         line = extendedBufferedReader.readLine();
         Assert.assertNull(line);
         Assert.assertEquals(expectedLineCounter + 1, extendedBufferedReader.getLineNumber());

         // Test readLine method with EOF
         line = extendedBufferedReader.readLine();
         Assert.assertNull(line);
         Assert.assertEquals(expectedLineCounter + 1, extendedBufferedReader.getLineNumber());

         // Clean up
         extendedBufferedReader.close();
    }

    /**
     * Test custom constructor
     */
    @Test
    public void testCustomConstructor() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("line1\r\nline2\r\nline3".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
         int expectedLineCounter = 3;
         int expectedLastChar = 3; // last character of "line3"

         // Test read method
         Assert.assertEquals(expectedLastChar, extendedBufferedReader.read());
         Assert.assertEquals(expectedLineCounter, extendedBufferedReader.getLineNumber());

         // Test readAgain method
         Assert.assertEquals(expectedLastChar, extendedBufferedReader.readAgain());

         // Test readLine method
         String line = extendedBufferedReader.readLine();
         Assert.assertEquals("line1", line);
         Assert.assertEquals(expectedLineCounter + 1, extendedBufferedReader.getLineNumber());

         // Test lookAhead method
         int lookAheadChar = extendedBufferedReader.lookAhead();
         Assert.assertEquals(-1, lookAheadChar); // EOF

         // Test read method with char array
         char[] charArray = new char[3];
         int readLength = extendedBufferedReader.read(charArray);
         Assert.assertEquals(3, readLength);
         Assert.assertEquals("line1".toCharArray(), charArray, false);

         // Test readLine method with empty line
         line = extendedBufferedReader.readLine();
         Assert.assertNull(line);
         Assert.assertEquals(expectedLineCounter + 1, extendedBufferedReader.getLineNumber());

         // Test readLine method with EOF
         line = extendedBufferedReader.readLine();
         Assert.assertNull(line);
         Assert.assertEquals(expectedLineCounter + 1, extendedBufferedReader.getLineNumber());

         // Clean up
         extendedBufferedReader.close();
    }

    /**
     * Test negative number of characters to read
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfCharactersToRead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("line1\nline2\nline3".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        extendedBufferedReader.read(-1);
    }

    /**
     * Test null char array in read method
     */
    @Test(expected = NullPointerException.class)
    public void testNullCharArrayInReadMethod() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("line1\nline2\nline3".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        extendedBufferedReader.read(null);
    }

    /**
     * Test null reader in constructor
     */
    @Test(expected = NullPointerException.class)
    public void testNullReaderInConstructor() throws IOException {
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(null);
    }
}