package org.apache.commons.csv;

import static org.apache.commons.csv.CSVParser.parse;
import static org.apache.commons.csv.Token.Type.TOKEN;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVParserTest {

    @org.junit.Test
    public void testParseSimpleCSV() throws IOException {
        String csvData = "a,b\nc,d";

        CSVFormat format = new CSVFormat();
        CSVParser parser = parse(new ByteArrayInputStream(csvData.getBytes()), format);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(2, record1.size());
        assertEquals("a", record1.get(0));
        assertEquals("b", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals(2, record2.size());
        assertEquals("c", record2.get(0));
        assertEquals("d", record2.get(1));

        parser.close();
    }

    @org.junit.Test
    public void testParseExcelCSV() throws IOException {
        String csvData = "\"Name\",\"Age\"\n\"John Doe\",25\n\"Jane Doe\",30";

        CSVFormat format = CSVFormat.EXCEL;
        CSVParser parser = parse(new ByteArrayInputStream(csvData.getBytes()), format);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(2, record1.size());
        assertEquals("Name", record1.get(0));
        assertEquals("Age", record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals(2, record2.size());
        assertEquals("John Doe", record2.get(0));
        assertEquals(Integer.class, record2.get(1).getClass());
        assertEquals(25, record2.get(1));

        parser.close();
    }

    @org.junit.Test
    public void testParseCSVWithCustomFormat() throws IOException {
        String csvData = "Name,Age\nJohn Doe,25\nJane Doe,30";

        CSVFormat format = new CSVFormat(';', '\'\'') // semicolon as delimiter and double quotes as quote character
                .setHeader("Name", "Age")
                .setNullValue("NA");

        CSVParser parser = parse(new ByteArrayInputStream(csvData.getBytes()), format);
        List<CSVRecord> records = parser.getRecords();

        assertEquals(2, records.size());

        CSVRecord record1 = records.get(0);
        assertEquals(2, record1.size());
        assertEquals("Name", record1.getHeaderByName("Name"));
        assertEquals("Age", record1.getHeaderByName("Age"));
        assertEquals("John Doe", record1.get(0));
        assertEquals(Integer.class, record1.get(1).getClass());
        assertEquals(25, record1.get(1));

        CSVRecord record2 = records.get(1);
        assertEquals(2, record2.size());
        assertEquals("Name", record2.getHeaderByName("Name"));
        assertEquals("Age", record2.getHeaderByName("Age"));
        assertEquals("Jane Doe", record2.get(0));
        assertEquals(Integer.class, record2.get(1).getClass());
        assertEquals(30, record2.get(1));

        parser.close();
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() throws IOException {
        parse(null, CSVFormat.DEFAULT);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNullFormat() throws IOException {
        File csvFile = new File("test.csv");
        parse(csvFile, (CSVFormat) null);
    }

    @org.junit.Test(expected = IOException.class)
    public void testParseNullCSVData() throws IOException {
        CSVFormat format = new CSVFormat();
        CSVParser parser = parse(new ByteArrayInputStream(null), format);
    }

}