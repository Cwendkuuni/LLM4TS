import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CSVParserTest {

    private static final String TEST_CSV = "name,age,city\nJohn,30,New York\nMary,25,London\n,35,Paris\n";

    private CSVParser parser;

    @Before
    public void setUp() throws IOException {
        parser = CSVParser.parse(new ByteArrayInputStream(TEST_CSV.getBytes(StandardCharsets.UTF_8)), CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        parser.close();
    }

    @Test
    public void testParseRecords() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(3, records.size());

        for (int i = 0; i < records.size(); i++) {
            CSVRecord record = records.get(i);
            Assert.assertEquals(i + 1, record.getRecordNumber());

            Object[] values = record.values();
            switch (i) {
                case 0:
                    Assert.assertEquals("John", values[0]);
                    Assert.assertEquals(30, values[1]);
                    Assert.assertEquals("New York", values[2]);
                    break;
                case 1:
                    Assert.assertEquals("Mary", values[0]);
                    Assert.assertEquals(25, values[1]);
                    Assert.assertEquals("London", values[2]);
                    break;
                case 2:
                    Assert.assertNull(values[0]);
                    Assert.assertEquals("Paris", values[1]);
                    break;
            }
        }
    }

    @Test
    public void testParseEmptyRecord() throws IOException {
        List<CSVRecord> records = parser.getRecords();
        Assert.assertEquals(3, records.size());

        CSVRecord lastRecord = records.get(records.size() - 1);
        Object[] emptyRecordValues = lastRecord.values();
        Assert.assertEquals(0, emptyRecordValues.length);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoMoreRecords() throws IOException {
        parser.getRecords();
        parser.nextRecord();
    }

    @Test(expected = EOFException.class)
    public void testEndOfStream() throws IOException {
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();
        parser.nextRecord();
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidCSVFormat() throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream("name,age,city\nJohn,30,New York\nMary,25,London\nname,35,Paris\n".getBytes(
                StandardCharsets.UTF_8));
        CSVParser invalidParser = new CSVParser(stream, CSVFormat.DEFAULT);
        invalidParser.nextRecord();
    }
}