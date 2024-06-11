package org.apache.commons.csv;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVRecord.TYPE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Test class for {@link CSVParser}
 */
public class CSVParserTest {

    /**
     * Test the basic parsing functionalities of the CSVParser.
     *
     * @throws Exception
     *             for any error
     */
    @org.junit.Test
    public void testBasicParsing() throws Exception {
        String csvData = "name,age,fruit\nbob,24,apple\nbill,31,banana\nbob,15,orange";

        CSVParser parser = new CSVParser(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Test number of records
        assertTrue(records.size() == 3);

        // Test first record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record1.getHeader());
        assertArrayEquals(new String[]{"bob", "24", "apple"}, record1.getValues());

        // Test second record values
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record2.getHeader());
        assertArrayEquals(new String[]{"bill", "31", "banana"}, record2.getValues());

        // Test third record values
        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record3.getHeader());
        assertArrayEquals(new String[]{"bob", "15", "orange"}, record3.getValues());

        // Test empty record
        CSVRecord emptyRecord = records.get(3);
        assertNull(emptyRecord);
    }

    /**
     * Test the parsing of CSV records with missing values.
     *
     * @throws Exception
     *             for any error
     */
    @org.junit.Test
    public void testMissingValues() throws Exception {
        String csvData = "name,age,fruit\nbob,,apple\nbill,31,banana\nbob,,orange";

        CSVParser parser = new CSVParser(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Test number of records
        assertTrue(records.size() == 3);

        // Test first record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record1.getHeader());
        assertArrayEquals(new String[]{"bob", null, "apple"}, record1.getValues());

        // Test second record values
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record2.getHeader());
        assertArrayEquals(new String[]{"bill", "31", "banana"}, record2.getValues());

        // Test third record values
        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record3.getHeader());
        assertArrayEquals(new String[]{"bob", null, "orange"}, record3.getValues());
    }

    /**
     * Test the parsing of CSV records with empty lines and comments.
     *
     * @throws Exception
     *             for any error
     */
    @org.junit.Test
    public void testEmptyLinesAndComments() throws Exception {
        String csvData = "name,age,fruit\nbob,24,apple\n\n\n#comment\nbill,31,banana\nbob,,orange";

        CSVParser parser = new CSVParser(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Test number of records
        assertTrue(records.size() == 3);

        // Test first record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record1.getHeader());
        assertArrayEquals(new String[]{"bob", "24", "apple"}, record1.getValues());

        // Test second record values
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record2.getHeader());
        assertArrayEquals(new String[]{"bill", "31", "banana"}, record2.getValues());

        // Test third record values
        CSVRecord record3 = records.get(2);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record3.getHeader());
        assertArrayEquals(new String[]{"bob", null, "orange"}, record3.getValues());

        // Test comment value
        String comment = record1.getComment();
        assertNull(comment);

        comment = record2.getComment();
        assertTrue(comment.contains("#comment"));
    }

    /**
     * Test the parsing of CSV records with multiple lines values and escaped fields.
     *
     * @throws Exception
     *             for any error
     */
    @org.junit.Test
    public void testMultilineValuesAndEscapedFields() throws Exception {
        String csvData = "name,age,fruit\nbob,\"first name\\,last name\",apple\nbill,\"Bill Smith\"\n,31,banana";

        CSVParser parser = new CSVParser(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Test number of records
        assertTrue(records.size() == 2);

        // Test first record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record1.getHeader());
        assertArrayEquals(new String[]{"bob", "\"first name,last name\"", "apple"}, record1.getValues());

        // Test second record values
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record2.getHeader());
        assertArrayEquals(new String[]{"bill", "Bill Smith", "banana"}, record2.getValues());
    }

    /**
     * Test the parsing of CSV records with custom delimiters.
     *
     * @throws Exception
     *             for any error
     */
    @org.junit.Test
    public void testCustomDelimiters() throws Exception {
        String csvData = "name|age|fruit\nbob|24|apple\nbill|31|banana|";

        CSVFormat format = new CSVFormatBuilder().setDelimiter('|').build();
        CSVParser parser = new CSVParser(new StringReader(csvData), format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Test number of records
        assertTrue(records.size() == 2);

        // Test first record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record1.getHeader());
        assertArrayEquals(new String[]{"bob", "24", "apple"}, record1.getValues());

        // Test second record values
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record2.getHeader());
        assertArrayEquals(new String[]{"bill", "31", "banana"}, record2.getValues());
    }

    /**
     * Test the parsing of CSV records with custom header names.
     *
     * @throws Exception
     *             for any error
     */
    @org.junit.Test
    public void testCustomHeaderNames() throws Exception {
        String csvData = "name,age,fruit\nbob,24,apple\nbill,31,banana";

        CSVFormat format = new CSVFormatBuilder().setHeaderNames(Arrays.asList("title", "age", "color"))
                .build();
        CSVParser parser = new CSVParser(new StringReader(csvData), format);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Test number of records
        assertTrue(records.size() == 2);

        // Test first record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"title", "age", "color"}, record1.getHeader());
        assertArrayEquals(new String[]{"bob", "24", "apple"}, record1.getValues());

        // Test second record values
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"title", "age", "color"}, record2.getHeader());
        assertArrayEquals(new String[]{"bill", "31", "banana"}, record2.getValues());
    }

    /**
     * Test the parsing of CSV records with whitespace in values.
     *
     * @throws Exception
     *             for any error
     */
    @org.junit.Test
    public void testWhitespaceInValues() throws Exception {
        String csvData = "name,age,fruit\nbob, 24 , apple\nbill, 31 , banana";

        CSVParser parser = new CSVParser(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Test number of records
        assertTrue(records.size() == 2);

        // Test first record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record1.getHeader());
        assertArrayEquals(new String[]{"bob", "24", "apple"}, record1.getValues());

        // Test second record values
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record2.getHeader());
        assertArrayEquals(new String[]{"bill", "31", "banana"}, record2.getValues());
    }

    /**
     * Test the parsing of CSV records with empty lines.
     *
     * @throws Exception
     *             for any error
     */
    @org.junit.Test
    public void testEmptyLines() throws Exception {
        String csvData = "name,age,fruit\nbob, 24 , apple\n\n\nbill,31,banana";

        CSVParser parser = new CSVParser(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Test number of records
        assertTrue(records.size() == 2);

        // Test first record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record1.getHeader());
        assertArrayEquals(new String[]{"bob", "24", "apple"}, record1.getValues());

        // Test second record values
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record2.getHeader());
        assertArrayEquals(new String[]{"bill", "31", "banana"}, record2.getValues());
    }

    /**
     * Test the parsing of CSV records with comments.
     *
     * @throws Exception
     *             for any error
     */
    @org.junit.Test
    public void testComments() throws Exception {
        String csvData = "name,age,fruit\nbob, 24 , apple#This is a comment\nbill,31,banana";

        CSVParser parser = new CSVParser(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Test number of records
        assertTrue(records.size() == 2);

        // Test first record values
        CSVRecord record1 = records.get(0);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record1.getHeader());
        assertArrayEquals(new String[]{"bob", "24", "apple"}, record1.getValues());

        // Test second record values
        CSVRecord record2 = records.get(1);
        assertArrayEquals(new String[]{"name", "age", "fruit"}, record2.getHeader());
        assertArrayEquals(new String[]{"bill", "31", "banana"}, record2.getValues());

        // Test comment value
        String comment = record1.getComment();
        assertNull(comment);

        comment = record2.getComment();
        assertTrue(comment.contains("This is a comment"));
    }

    /**
     * Test the closing of the parser.
     *
     * @throws Exception
     *             for any error
     */
    @org.junit.Test
    public void testClosing() throws Exception {
        String csvData = "name,age,fruit\nbob, 24 , apple";

        CSVParser parser = new CSVParser(new StringReader(csvData), DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        parser.close();

        // Test getting records after closing
        List<CSVRecord> emptyRecords = parser.getRecords();
        assertFalse(emptyRecords.isEmpty());
    }
}