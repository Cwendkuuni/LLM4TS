import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for CSVParser.
 */
public class CSVParserTest {

    /**
     * Test parse method with valid file.
     *
     * @throws IOException if file reading fails
     */
    @Test
    public void testParseValidFile() throws IOException {
        File csvFile = new File("test.csv");
        CSVParser parser = CSVParser.parse(csvFile, CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Check size of records
        assertEquals(5, records.size());

        // Check values of records
        for (CSVRecord record : records) {
            String[] expectedValues = new String[]{"A1", "B1", "C1", "D1", ""};
            Assert.assertArrayEquals(expectedValues, record.toArray());
        }
    }

    /**
     * Test parse method with invalid file format.
     *
     * @throws IOException if file reading fails
     */
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidFileFormat() throws IOException {
        File csvFile = new File("invalid_format.csv");
        CSVParser.parse(csvFile, CSVFormat.RFC4180);
    }

    /**
     * Test parse method with empty string.
     *
     * @throws IOException if parsing fails
     */
    @Test
    public void testParseEmptyString() throws IOException {
        String csvString = "";
        CSVParser parser = new CSVParser(new StringReader(csvString), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Check size of records
        assertEquals(0, records.size());
    }

    /**
     * Test parse method with valid string.
     *
     * @throws IOException if parsing fails
     */
    @Test
    public void testParseValidString() throws IOException {
        String csvString = "A1,B1,C1,D1\nA2,B2,C2,D2\n";
        CSVParser parser = new CSVParser(new StringReader(csvString), CSVFormat.RFC4180);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Check size of records
        assertEquals(2, records.size());

        // Check values of records
        for (CSVRecord record : records) {
            String[] expectedValues = new String[]{"A1", "B1", "C1", "D1"};
            if (record.getRecordNumber() == 1) {
                expectedValues = new String[]{"A2", "B2", "C2", "D2"};
            }
            Assert.assertArrayEquals(expectedValues, record.toArray());
        }
    }

    /**
     * Test parse method with custom format.
     *
     * @throws IOException if parsing fails
     */
    @Test
    public void testParseCustomFormat() throws IOException {
        String csvString = "A1,B1\nA2,B2,C2\n";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeaderNames(new String[]{"A", "B", "C"})
                .build();

        CSVParser parser = new CSVParser(new StringReader(csvString), customFormat);
        List<CSVRecord> records = parser.getRecords();
        parser.close();

        // Check size of records
        assertEquals(2, records.size());

        // Check values of records
        for (CSVRecord record : records) {
            String[] expectedValues = new String[3];
            if (record.getRecordNumber() == 1) {
                expectedValues[2] = "C2";
            }
            Assert.assertArrayEquals(expectedValues, record.toArray());
            for (int i = 0; i < record.size(); i++) {
                String actualValue = record.get(i);
                String expectedValue = record.getHeaderName(i);
                assertEquals(expectedValue, actualValue);
            }
        }
    }
}