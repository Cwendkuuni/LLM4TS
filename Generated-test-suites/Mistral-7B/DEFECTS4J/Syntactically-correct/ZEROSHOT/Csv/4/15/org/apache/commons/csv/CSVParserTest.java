import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFile() throws IOException {
        File file = new File("test.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertNotNull(records);
        Assert.assertEquals(5, records.size());

        for (CSVRecord record : records) {
            Assert.assertNotNull(record.getValues());
            Assert.assertEquals(5, record.size());
            Assert.assertEquals("Header1", record.getHeaderMap().get("Header1"));
            Assert.assertEquals("Value1", record.get(0));
            Assert.assertEquals("Value2", record.get(1));
            Assert.assertEquals("Value3", record.get(2));
            Assert.assertEquals("Value4", record.get(3));
            Assert.assertEquals("Value5", record.get(4));
        }

        parser.close();
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Header1,Value1,Value2,Value3,Value4\n" +
                "Header1,Value11,Value21,Value31,Value41\n" +
                "Header1,Value12,Value22,Value32,Value42\n" +
                "Header1,Value13,Value23,Value33,Value43\n" +
                "Header1,Value14,Value24,Value34,Value44";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertNotNull(records);
        Assert.assertEquals(5, records.size());

        for (CSVRecord record : records) {
            Assert.assertNotNull(record.getValues());
            Assert.assertEquals(5, record.size());
            Assert.assertEquals("Header1", record.getHeaderMap().get("Header1"));
            Assert.assertEquals("Value1", record.get(0));
            Assert.assertEquals("Value11", record.get(1));
            Assert.assertEquals("Value12", record.get(2));
            Assert.assertEquals("Value13", record.get(3));
            Assert.assertEquals("Value14", record.get(4));
        }

        parser.close();
    }

    @Test
    public void testParseUrl() throws IOException {
        String urlString = "http://example.com/test.csv";
        URL url = new URL(urlString);
        CSVParser parser = CSVParser.parse(url, StandardCharsets.UTF_8, CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertNotNull(records);
        Assert.assertEquals(5, records.size());

        for (CSVRecord record : records) {
            Assert.assertNotNull(record.getValues());
            Assert.assertEquals(5, record.size());
            Assert.assertEquals("Header1", record.getHeaderMap().get("Header1"));
            Assert.assertEquals("Value1", record.get(0));
            Assert.assertEquals("Value2", record.get(1));
            Assert.assertEquals("Value3", record.get(2));
            Assert.assertEquals("Value4", record.get(3));
            Assert.assertEquals("Value5", record.get(4));
        }

        parser.close();
    }

    @Test
    public void testParseCustomFormat() throws IOException {
        String csvData = "Header1,Value1,Value2\n" +
                "Header1,Value11,Value21\n" +
                "Header2,Value3,Value4\n" +
                "Header2,Value31,Value41";

        CSVFormat customFormat = new CSVFormatBuilder()
                .setHeaderMap(Map.of("Header1", 0, "Header2", 1))
                .build();

        CSVParser parser = new CSVParser(new StringReader(csvData), customFormat);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertNotNull(records);
        Assert.assertEquals(4, records.size());

        for (CSVRecord record : records) {
            Assert.assertNotNull(record.getValues());
            Assert.assertEquals(3, record.size());
            Assert.assertEquals("Header1", record.getHeaderMap().get("Header1"));
            Assert.assertEquals("Value1", record.get(0));
            Assert.assertEquals("Value11", record.get(1));
            Assert.assertEquals("Header2", record.getHeaderMap().get("Header2"));
            Assert.assertEquals("Value3", record.get(1));
            Assert.assertEquals("Value31", record.get(2));
        }

        parser.close();
    }

    @Test
    public void testGetCurrentLineNumber() throws IOException {
        String csvData = "Header1,Value1\n" +
                "Header1,Value2";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);

        Assert.assertEquals(1, parser.getCurrentLineNumber());
        parser.nextRecord();
        Assert.assertEquals(2, parser.getCurrentLineNumber());
        parser.nextRecord();
        Assert.assertEquals(3, parser.getCurrentLineNumber());

        parser.close();
    }

    @Test(expected = IOException.class)
    public void testInvalidInput() throws IOException {
        String invalidCsvData = "Header1,Value1\n" +
                "Header1,Value1\n" +
                "Header1,Value1,\n";

        CSVParser parser = new CSVParser(new StringReader(invalidCsvData), CSVFormat.DEFAULT);
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord(); // This should throw an IOException

        parser.close();
    }

    @Test
    public void testClose() throws IOException {
        String csvData = "Header1,Value1\n" +
                "Header1,Value2\n" +
                "Header1,Value3";

        CSVParser parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT);
        List<CSVRecord> records = parser.getRecords();

        Assert.assertNotNull(records);
        Assert.assertEquals(3, records.size());

        parser.close();

        try {
            parser.nextRecord();
            Assert.fail();
        } catch (NoSuchElementException e) {
            // Expected
        }
    }
}