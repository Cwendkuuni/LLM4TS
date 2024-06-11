package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

/**
 * Test class for the {@link CSVParser} class
 */
public class CSVParserTest {

    /**
     * Test method for {@link CSVParser#parse(File, CSVFormat)}
     * @throws IOException
     */
    @Test
    public void testParseFile() throws IOException {
        // Given
        final File inputFile = new File("src/test/resources/test.csv");
        final CSVFormat format = new CSVFormat();
        // When
        final CSVParser parser = parse(inputFile, format);
        // Then
        assertNotNull(parser);
        // Use the parser
        final Iterator<CSVRecord> iterator = parser.iterator();
        CSVRecord record;
        int i = 0;
        while (iterator.hasNext()) {
            record = iterator.next();
            assertEquals("Record " + i++, 1, record.size());
            assertEquals("Column 0", "A1", record.get(0));
            assertEquals("Column 1", "B1", record.get(1));
        }
        // Clean up
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(String, CSVFormat)}
     */
    @Test
    public void testParseString() {
        // Given
        final String inputString = "A1,B1\nA2,B2";
        final CSVFormat format = new CSVFormat();
        // When
        final CSVParser parser = parse(new StringReader(inputString), format);
        // Then
        assertNotNull(parser);
        // Use the parser
        final Iterator<CSVRecord> iterator = parser.iterator();
        CSVRecord record;
        int i = 0;
        while (iterator.hasNext()) {
            record = iterator.next();
            assertEquals("Record " + i++, 1, record.size());
            assertEquals("Column 0", "A1", record.get(0));
            assertEquals("Column 1", "B1", record.get(1));
        }
        // Clean up
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(URL, Charset, CSVFormat)}
     * @throws IOException
     */
    @Test
    public void testParseURL() throws IOException {
        // Given
        final URL inputURL = getClass().getResource("/test.csv");
        final Charset charset = Charset.forName("UTF-8");
        final CSVFormat format = new CSVFormat();
        // When
        final CSVParser parser = parse(inputURL, charset, format);
        // Then
        assertNotNull(parser);
        // Use the parser
        final Iterator<CSVRecord> iterator = parser.iterator();
        CSVRecord record;
        int i = 0;
        while (iterator.hasNext()) {
            record = iterator.next();
            assertEquals("Record " + i++, 1, record.size());
            assertEquals("Column 0", "A1", record.get(0));
            assertEquals("Column 1", "B1", record.get(1));
        }
        // Clean up
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)}
     * @throws IOException
     */
    @Test
    public void testParseReader() throws IOException {
        // Given
        final String inputString = "A1,B1\nA2,B2";
        final Reader inputReader = new StringReader(inputString);
        final CSVFormat format = new CSVFormat();
        // When
        final CSVParser parser = new CSVParser(inputReader, format);
        // Then
        assertNotNull(parser);
        // Use the parser
        final Iterator<CSVRecord> iterator = parser.iterator();
        CSVRecord record;
        int i = 0;
        while (iterator.hasNext()) {
            record = iterator.next();
            assertEquals("Record " + i++, 1, record.size());
            assertEquals("Column 0", "A1", record.get(0));
            assertEquals("Column 1", "B1", record.get(1));
        }
        // Clean up
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(File, CSVFormat)} with empty file
     * @throws IOException
     */
    @Test
    public void testParseFileEmpty() throws IOException {
        // Given
        final File inputFile = new File("src/test/resources/empty.csv");
        final CSVFormat format = new CSVFormat();
        // When
        final CSVParser parser = parse(inputFile, format);
        // Then
        assertNotNull(parser);
        // Use the parser
        final Iterator<CSVRecord> iterator = parser.iterator();
        assertFalse(iterator.hasNext());
        // Clean up
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(String, CSVFormat)} with empty string
     */
    @Test
    public void testParseStringEmpty() {
        // Given
        final String inputString = "";
        final CSVFormat format = new CSVFormat();
        // When
        final CSVParser parser = parse(new StringReader(inputString), format);
        // Then
        assertNotNull(parser);
        // Use the parser
        final Iterator<CSVRecord> iterator = parser.iterator();
        assertFalse(iterator.hasNext());
        // Clean up
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(URL, Charset, CSVFormat)} with non-existing URL
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testParseURLNonExisting() throws IOException {
        // Given
        final URL inputURL = new URL("http://non-existent.com/test.csv");
        final Charset charset = Charset.forName("UTF-8");
        final CSVFormat format = new CSVFormat();
        // When
        final CSVParser parser = parse(inputURL, charset, format);
        // Then
        // The constructor should throw an IOException
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} with null reader
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseReaderNull() {
        // Given
        final CSVFormat format = new CSVFormat();
        // When
        // The constructor should throw an IllegalArgumentException
        try {
            new CSVParser(null, format);
        } catch (final IllegalArgumentException e) {
            // Then
            assertEquals("reader", e.getMessage());
        }
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} with empty reader
     */
    @Test
    public void testParseReaderEmpty() {
        // Given
        final Reader inputReader = new StringReader("");
        final CSVFormat format = new CSVFormat();
        // When
        final CSVParser parser = new CSVParser(inputReader, format);
        // Then
        assertNotNull(parser);
        // Use the parser
        final Iterator<CSVRecord> iterator = parser.iterator();
        assertFalse(iterator.hasNext());
        // Clean up
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} with null format
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseReaderNullFormat() throws IOException {
        // Given
        final Reader inputReader = new StringReader("A1,B1\nA2,B2");
        // When
        // The constructor should throw an IllegalArgumentException
        try {
            new CSVParser(inputReader, null);
        } catch (final IllegalArgumentException e) {
            // Then
            assertEquals("format", e.getMessage());
        }
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} with null reader and null format
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseReaderNullReaderNullFormat() {
        // Given
        // When
        // The constructor should throw an IllegalArgumentException
        try {
            new CSVParser(null, null);
        } catch (final IllegalArgumentException e) {
            // Then
            assertEquals("reader", e.getMessage());
        }
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} with empty reader and null format
     */
    @Test
    public void testParseReaderEmptyReaderNullFormat() {
        // Given
        final Reader inputReader = new StringReader("");
        final CSVFormat format = new CSVFormat();
        // When
        final CSVParser parser = new CSVParser(inputReader, format);
        // Then
        assertNotNull(parser);
        // Use the parser
        final Iterator<CSVRecord> iterator = parser.iterator();
        assertFalse(iterator.hasNext());
        // Clean up
        parser.close();
    }

    /**
     * Test method for {@link CSVParser#parse(Reader)} with invalid input
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testParseReaderInvalidInput() throws IOException {
        // Given
        final Reader inputReader = new StringReader("Invalid input");
        final CSVFormat format = new CSVFormat();
        // When
        final CSVParser parser = new CSVParser(inputReader, format);
        // Then
        // The constructor should throw an IOException
        parser.close();
    }

}