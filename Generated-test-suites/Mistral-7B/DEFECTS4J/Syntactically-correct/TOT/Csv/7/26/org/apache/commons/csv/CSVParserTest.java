package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

/**
 * Test for {@link CSVParser} class.
 */
public class CSVParserTest {

    private static final String TEST_CSV = "name,age;Alice,25;Bob,30;Charlie,35;";

    private CSVParser parser;

    @Before
    public void setUp() throws IOException {
        parser = new CSVParser(new StringReader(TEST_CSV), CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        parser.close();
    }

    /**
     * Test parsing records from CSV file.
     */
    @Test
    public void testParseRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(3, records.size());
        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            String[] values = record.getValues();
            switch (i) {
                case 0:
                    Assert.assertEquals("Alice", values[0]);
                    Assert.assertEquals(25, Integer.parseInt(values[1]));
                    break;
                case 1:
                    Assert.assertEquals("Bob", values[0]);
                    Assert.assertEquals(30, Integer.parseInt(values[1]));
                    break;
                case 2:
                    Assert.assertEquals("Charlie", values[0]);
                    Assert.assertEquals(35, Integer.parseInt(values[1]));
                    break;
            }
        }
    }

    /**
     * Test handling header and no header.
     */
    @Test
    public void testHandlingHeader() throws IOException {
        parser = new CSVParser(new StringReader(TEST_CSV), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = parser.getRecords();

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals(2, firstRecord.size());
        Assert.assertEquals("name", firstRecord.get(0).getName());
        Assert.assertEquals("age", firstRecord.get(1).getName());

        CSVRecord secondRecord = records.get(1);
        Assert.assertEquals(2, secondRecord.size());
        Assert.assertEquals("Alice", secondRecord.get(0).getName());
        Assert.assertEquals("age", secondRecord.get(1).getName());
        Assert.assertEquals(25, secondRecord.get(1).getValueByIndex(0).getValue());

        CSVRecord thirdRecord = records.get(2);
        Assert.assertEquals(2, thirdRecord.size());
        Assert.assertEquals("Bob", thirdRecord.get(0).getName());
        Assert.assertEquals("age", thirdRecord.get(1).getName());
        Assert.assertEquals(30, thirdRecord.get(1).getValueByIndex(0).getValue());

        CSVRecord fourthRecord = records.get(3);
        Assert.assertEquals(1, fourthRecord.size());
        Assert.assertEquals("Charlie", fourthRecord.get(0).getName());
        Assert.assertNull(fourthRecord.get(1));
    }

    /**
     * Test parsing empty CSV file.
     */
    @Test
    public void testParseEmptyCsvFile() throws IOException {
        parser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(0, records.size());
    }

    /**
     * Test parsing CSV with invalid format.
     */
    @Test(expected = IOException.class)
    public void testParseInvalidFormat() throws IOException {
        parser = new CSVParser(new StringReader("name,ageAlice,25;Bob,30;Charlie,35;"), CSVFormat.DEFAULT);
        parser.getRecords();
    }

}