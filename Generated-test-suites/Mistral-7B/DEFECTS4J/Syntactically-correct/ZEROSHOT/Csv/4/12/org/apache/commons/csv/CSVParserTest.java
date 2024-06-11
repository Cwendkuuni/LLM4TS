import org.apache.commons.csv.*;
import org.junit.Assert.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CSVParserTest {

    @org.junit.Test
    public void testParseSimpleCSV() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }
        parser.close();

        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        CSVRecord secondRecord = records.get(1);

        assertEquals("Name", firstRecord.get(0));
        assertEquals("Age", firstRecord.get(1));
        assertEquals("City", firstRecord.get(2));
        assertEquals("John", firstRecord.get(0));
        assertEquals(30, firstRecord.get(1).getInt(0));
        assertEquals("New York", firstRecord.get(2));

        assertEquals("Name", secondRecord.get(0));
        assertEquals("Age", secondRecord.get(1));
        assertEquals("City", secondRecord.get(2));
        assertEquals("Mike", secondRecord.get(0));
        assertEquals(25, secondRecord.get(1).getInt(0));
        assertEquals("Chicago", secondRecord.get(2));
    }

    @org.junit.Test
    public void testEmptyCSV() throws Exception {
        String csvContent = "";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }
        parser.close();

        assertEquals(0, records.size());
    }

    @org.junit.Test
    public void testInvalidCSV() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\nName,Age\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT);

        try {
            List<CSVRecord> records = new ArrayList<>();
            for (CSVRecord record : parser) {
                records.add(record);
            }
            assertTrue(false); // Should not reach this point
        } catch (CSVException e) {
            // Expected result
        } finally {
            parser.close();
        }
    }

    @org.junit.Test
    public void testParseCSVWithHeaders() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), CSVFormat.DEFAULT.withHeader());
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }
        parser.close();

        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        CSVRecord secondRecord = records.get(1);

        assertEquals("Name", firstRecord.get(CSVHeader.NAME.name()));
        assertEquals("Age", firstRecord.get(CSVHeader.AGE.name()));
        assertEquals("City", firstRecord.get(CSVHeader.CITY.name()));
        assertEquals("John", firstRecord.get(CSVHeader.NAME.name()));
        assertEquals(30, firstRecord.get(CSVHeader.AGE.name()).getInt(0));
        assertEquals("New York", firstRecord.get(CSVHeader.CITY.name()));

        assertEquals("Name", secondRecord.get(CSVHeader.NAME.name()));
        assertEquals("Age", secondRecord.get(CSVHeader.AGE.name()));
        assertEquals("City", secondRecord.get(CSVHeader.CITY.name()));
        assertEquals("Mike", secondRecord.get(CSVHeader.NAME.name()));
        assertEquals(25, secondRecord.get(CSVHeader.AGE.name()).getInt(0));
        assertEquals("Chicago", secondRecord.get(CSVHeader.CITY.name()));
    }

    @org.junit.Test
    public void testParseCSVWithCustomFormat() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\n";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeaderNames(Arrays.asList("Name1", "Age1", "City1"))
                .build();

        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat);
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }
        parser.close();

        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        CSVRecord secondRecord = records.get(1);

        assertEquals("Name1", firstRecord.get(0));
        assertEquals("Age1", firstRecord.get(1));
        assertEquals("City1", firstRecord.get(2));
        assertEquals("John", firstRecord.get(0));
        assertEquals(30, firstRecord.get(1).getInt(0));
        assertEquals("New York", firstRecord.get(2));

        assertEquals("Name1", secondRecord.get(0));
        assertEquals("Age1", secondRecord.get(1));
        assertEquals("City1", secondRecord.get(2));
        assertEquals("Mike", secondRecord.get(0));
        assertEquals(25, secondRecord.get(1).getInt(0));
        assertEquals("Chicago", secondRecord.get(2));
    }

    @org.junit.Test
    public void testParseCSVWithHeaderAndCustomFormat() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\n";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeaderNames(Arrays.asList("Name1", "Age1", "City1"))
                .build();

        CSVParser parser = CSVParser.parse(new StringReader(csvContent), customFormat.withHeader());
        List<CSVRecord> records = new ArrayList<>();
        for (CSVRecord record : parser) {
            records.add(record);
        }
        parser.close();

        assertEquals(2, records.size());
        CSVRecord firstRecord = records.get(0);
        CSVRecord secondRecord = records.get(1);

        assertEquals("Name1", firstRecord.get(CSVHeader.NAME.name()));
        assertEquals("Age1", firstRecord.get(CSVHeader.AGE.name()));
        assertEquals("City1", firstRecord.get(CSVHeader.CITY.name()));
        assertEquals("John", firstRecord.get(CSVHeader.NAME.name()));
        assertEquals(30, firstRecord.get(CSVHeader.AGE.name()).getInt(0));
        assertEquals("New York", firstRecord.get(CSVHeader.CITY.name()));

        assertEquals("Name1", secondRecord.get(CSVHeader.NAME.name()));
        assertEquals("Age1", secondRecord.get(CSVHeader.AGE.name()));
        assertEquals("City1", secondRecord.get(CSVHeader.CITY.name()));
        assertEquals("Mike", secondRecord.get(CSVHeader.NAME.name()));
        assertEquals(25, secondRecord.get(CSVHeader.AGE.name()).getInt(0));
        assertEquals("Chicago", secondRecord.get(CSVHeader.CITY.name()));
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNullCSVFormat() throws Exception {
        String csvContent = "Name,Age,City\nJohn,30,New York\nMike,25,Chicago\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvContent), null);

        try {
            parser.nextRecord();
        } catch (IOException e) {
            throw new IllegalArgumentException("Unexpected IOException", e);
        } finally {
            parser.close();
        }

        fail("Should not reach this point");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testParseNullCSVData() throws Exception {
        CSVParser parser = CSVParser.parse(null, CSVFormat.DEFAULT);

        try {
            parser.nextRecord();
        } catch (IOException e) {
            throw new IllegalArgumentException("Unexpected IOException", e);
        } finally {
            parser.close();
        }

        fail("Should not reach this point");
    }

}