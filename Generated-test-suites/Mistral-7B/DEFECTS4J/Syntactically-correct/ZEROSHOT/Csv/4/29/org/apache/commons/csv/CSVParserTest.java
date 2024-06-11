import org.apache.commons.csv.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVParserTest {

    private CSVParser parser;
    private String csvData = "col1,col2\n1,2\n3,4";

    @Before
    public void setup() throws IOException {
        CSVFormat format = new BasicCSVFormat();
        parser = new CSVParser(new StringReader(csvData), format);
    }

    @Test
    public void testParseEmptyCSV() throws IOException {
        assertNull(parser.nextRecord());
        parser.close();
    }

    @Test
    public void testParseSingleRecordCSV() throws IOException {
        CSVRecord record = parser.nextRecord();
        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("1", record.get(0));
        assertEquals("2", record.get(1));
        parser.nextRecord();
        assertNull(parser.nextRecord());
        parser.close();
    }

    @Test
    public void testParseMultipleRecordsCSV() throws IOException {
        CSVRecord record1 = parser.nextRecord();
        assertNotNull(record1);
        assertEquals(2, record1.size());
        assertEquals("1", record1.get(0));
        assertEquals("2", record1.get(1));

        CSVRecord record2 = parser.nextRecord();
        assertNotNull(record2);
        assertEquals(2, record2.size());
        assertEquals("3", record2.get(0));
        assertEquals("4", record2.get(1));

        CSVRecord record3 = parser.nextRecord();
        assertNull(record3);

        parser.close();
    }

    @Test(expected = NoSuchElementException.class)
    public void testParseEmptyIterator() throws IOException {
        Iterator<CSVRecord> iterator = parser.iterator();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        parser.close();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidCSV() throws IOException {
        String csvDataInvalid = "col1,col2\n1,2\nabcd\n3,4";
        CSVParser parserInvalid = new CSVParser(new StringReader(csvDataInvalid), new BasicCSVFormat());
        parserInvalid.nextRecord();
        parserInvalid.nextRecord();
        parserInvalid.nextRecord();
        parserInvalid.close();
    }

}