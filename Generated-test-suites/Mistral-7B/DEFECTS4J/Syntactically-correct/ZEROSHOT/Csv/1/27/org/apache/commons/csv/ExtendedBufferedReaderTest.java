package org.apache.commons.csv.test;

import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;
    private static final String TEST_CHARS = "test\r\rtest123\n";
    private static final String TEST_LINE = "test\r\rtest123";

    @Before
    public void setUp() throws IOException {
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(TEST_CHARS.getBytes())));
    }

    @After
    public void tearDown() throws Exception {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }

    @Test
    public void testRead() throws IOException {
        for (int i = 0; i < TEST_CHARS.length(); i++) {
            int charRead = reader.read();
            assertEquals(TEST_CHARS.charAt(i), (char) charRead);
            if (TEST_CHARS.charAt(i) == '\n') {
                assertTrue(reader.readLine() != null);
            }
        }
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        for (int i = 0; i < TEST_CHARS.length(); i++) {
            int charRead = reader.read();
            int charReadAgain = reader.readAgain();
            assertEquals(charRead, charReadAgain);
            if (TEST_CHARS.charAt(i) == '\n') {
                assertTrue(reader.readLine() != null);
            }
        }
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        String line = reader.readLine();
        assertEquals(TEST_LINE, line);
        assertTrue(reader.readLine() == null);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testLookAhead() throws IOException {
        int lookAheadChar = reader.lookAhead();
        assertEquals(TEST_CHARS.charAt(0), (char) lookAheadChar);
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        for (int i = 0; i < 10; i++) {
            reader.readLine();
        }
        assertEquals(10, reader.getLineNumber());
    }

    @Test
    public void testEmptyLine() throws IOException {
        reader.readLine();
        reader.readLine();
        int charRead = reader.read();
        assertEquals('\r', (char) charRead);
        reader.readLine();
        int charReadAgain = reader.readAgain();
        assertEquals('\r', (char) charReadAgain);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadEmptyFile() throws IOException {
        reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0])));
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
        assertEquals(0, reader.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testNegativeLookAhead() throws IOException {
        reader.lookAhead();
    }

}