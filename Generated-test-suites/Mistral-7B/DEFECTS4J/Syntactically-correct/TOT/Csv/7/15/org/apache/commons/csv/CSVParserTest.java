import org.apache.commons.csv.*;
import org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for {@link CSVParser}.
 */
public class CSVParserTest {

    /**
     * Test parsing a simple CSV file.
     */
    @Test
    public void testParseSimpleCSV() throws IOException {
        String csvData = "Name,Age\nJohn,30\nMary,25\n";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        for (CSVRecord record : records) {
            String name = record.get(0);
            int age = record.get(1).getIntValue();
            if (name.equals("John")) {
                assertEquals(30, age);
            } else {
                assertEquals(25, age);
            }
        }
    }

    /**
     * Test parsing a CSV file with a custom format.
     */
    @Test
    public void testParseCustomCSV() throws IOException {
        String csvData = "Date,Amount\n2022-01-01,100.50\n2022-01-02,200.00";
        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeader("Date, Amount")
                .setParseLong("Date")
                .build();

        CSVParser parser = CSVParser.parse(new StringReader(csvData), customFormat);

        List<CSVRecord> records = parser.getRecords();
        assertEquals(2, records.size());

        for (CSVRecord record : records) {
            long date = record.get(0).getLongValue();
            double amount = record.get(1).getDoubleValue();
            if (date == 20220101L) {
                assertEquals(100.5, amount, 0.001);
            } else {
                assertEquals(200.0, amount, 0.001);
            }
        }
    }

    /**
     * Test handling a malformed CSV file.
     */
    @Test(expected = IOException.class)
    public void testMalformedCSV() throws IOException {
        String csvData = "Name,Age\nJohn,30\nMary,25\nName,25";
        CSVParser parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT);

        parser.getRecords();
    }
}