import org.apache.commons.csv.*;
import org.junit.Assert;
import org.junit.Test;

public class CSVParserTest {

    @Test
    public void testParseFileWithHeader() throws IOException {
        File file = new File("testData/CSVParserTest_ParseFileWithHeader.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals(4, firstRecord.size());
        Assert.assertEquals("Column1", firstRecord.get(0));
        Assert.assertEquals("Column2", firstRecord.get(1));
        Assert.assertEquals("Column3", firstRecord.get(2));
        Assert.assertEquals("Column4", firstRecord.get(3));
    }

    @Test
    public void testParseFileWithoutHeader() throws IOException {
        File file = new File("testData/CSVParserTest_ParseFileWithoutHeader.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT.withHeaderExcluded());

        List<CSVRecord> records = parser.getRecords();

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals(4, firstRecord.size());
        Assert.assertEquals("Value1", firstRecord.get(0));
        Assert.assertEquals("Value2", firstRecord.get(1));
        Assert.assertEquals("Value3", firstRecord.get(2));
        Assert.assertEquals("Value4", firstRecord.get(3));
    }

    @Test
    public void testParseString() throws IOException {
        String csvData = "Column1,Column2,Column3,Column4\nValue1,Value2,Value3,Value4";
        CSVParser parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);

        List<CSVRecord> records = parser.getRecords();

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals(4, firstRecord.size());
        Assert.assertEquals("Value1", firstRecord.get(0));
        Assert.assertEquals("Value2", firstRecord.get(1));
        Assert.assertEquals("Value3", firstRecord.get(2));
        Assert.assertEquals("Value4", firstRecord.get(3));
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFile() throws IOException {
        File invalidFile = new File("testData/CSVParserTest_ParseInvalidFile.csv");
        CSVParser parser = CSVParser.parse(invalidFile, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseNullFile() {
        File nullFile = null;
        try {
            CSVParser parser = CSVParser.parse(nullFile, CSVFormat.DEFAULT);
        } catch (IOException e) {
            // Ignore
        }
        CSVParser parser = new CSVParser(new StringReader(""), new CSVFormat());
        parser.parseNextRecord(); // Parse to initialize
        parser.parseNextRecord(new CSVRecord("Value1,Value2,Value3".split(","))); // Test parsing a record

        Assert.assertNotNull(parser.getHeaderMap());
        Assert.assertEquals(3, parser.getHeaderMap().size());
        Assert.assertEquals("Value1", parser.getHeaderMap().get("Column1"));
        Assert.assertEquals("Value2", parser.getHeaderMap().get("Column2"));
        Assert.assertEquals("Value3", parser.getHeaderMap().get("Column3"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyFile() throws IOException {
        File emptyFile = new File("testData/CSVParserTest_ParseEmptyFile.csv");
        CSVParser parser = CSVParser.parse(emptyFile, CSVFormat.DEFAULT);
        parser.getRecords();
    }

    @Test
    public void testParseFileWithCustomFormat() throws IOException {
        File customFormatFile = new File("testData/CSVParserTest_ParseFileWithCustomFormat.csv");
        CSVParser parser = CSVParser.parse(customFormatFile, new CSVFormatBuilder()
                .setDelimiter(';')
                .setHeaderNames(new String[]{"Header1", "Header2"})
                .build());

        List<CSVRecord> records = parser.getRecords();

        CSVRecord firstRecord = records.get(0);
        Assert.assertEquals(2, firstRecord.size());
        Assert.assertEquals("Value1", firstRecord.get(0));
        Assert.assertEquals("Value2", firstRecord.get(1));
    }

    @Test(expected = IOException.class)
    public void testParseFileWithInvalidCustomFormat() throws IOException {
        File invalidCustomFormatFile = new File("testData/CSVParserTest_ParseFileWithInvalidCustomFormat.csv");
        CSVParser parser = CSVParser.parse(invalidCustomFormatFile, new CSVFormatBuilder()
                .setDelimiter('X') // Invalid delimiter
                .setHeaderNames(new String[]{"Header1", "Header2"})
                .build());
    }

    @Test
    public void testCloseParser() throws IOException {
        File file = new File("testData/CSVParserTest_ParseFileWithHeader.csv");
        CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
        parser.close();

        try {
            parser.parseNextRecord();
            Assert.fail("Expected IOException after closing parser");
        } catch (IOException e) {
            // Expected
        }
    }
}