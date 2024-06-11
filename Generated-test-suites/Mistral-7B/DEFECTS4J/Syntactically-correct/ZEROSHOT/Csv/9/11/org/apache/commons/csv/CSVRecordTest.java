import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVRecordTest {

    @Test
    public void testCSVRecordConstruction() {
        String csvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25,Los Angeles";
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = null;
        try {
            parser = format.newParser(new StringReader(csvContent));
            List<CSVRecord> records = parser.getRecords();
            CSVRecord firstRecord = records.get(0);
            CSVRecord secondRecord = records.get(1);

            // Testing record with values
            Assert.assertArrayEquals(new String[]{"John Doe", "30", "New York"}, firstRecord.values());
            Assert.assertArrayEquals(new String[]{"Jane Doe", "25", "Los Angeles"}, secondRecord.values());

            // Testing record with comment
            CSVRecord commentRecord = records.get(0);
            Assert.assertNull(commentRecord.getComment());

            String commentContent = "Comment for John Doe record.";
            CSVRecord commentRecordWithComment = new CSVRecord(new String[]{"John Doe", "30", "New York"},
                    null, commentContent, 0);
            Assert.assertEquals(commentContent, commentRecordWithComment.getComment());

            // Testing record with mapping
            Map<String, Integer> mapping = new HashMap<>();
            mapping.put("Name", 0);
            mapping.put("Age", 1);
            mapping.put("City", 2);
            CSVRecord mappedRecord = new CSVRecord(new String[]{"John Doe", "30", "New York"}, mapping, null, 0);
            Assert.assertEquals("John Doe", mappedRecord.get("Name"));
            Assert.assertEquals("30", mappedRecord.get("Age"));
            Assert.assertEquals("New York", mappedRecord.get("City"));
            Assert.assertNull(mappedRecord.get("Unknown"));

            // Testing record consistency
            Assert.assertTrue(mappedRecord.isConsistent());
            String inconsistentCsvContent = "Name,Age,City\nJohn Doe,30,New York\nJane Doe,25";
            parser = format.newParser(new StringReader(inconsistentCsvContent));
            records = parser.getRecords();
            CSVRecord inconsistentRecord = records.get(1);
            Assert.assertFalse(inconsistentRecord.isConsistent());

            // Testing record size
            Assert.assertEquals(3, firstRecord.size());
            Assert.assertEquals(3, secondRecord.size());

            // Testing record iteration
            Iterator<String> recordIterator = firstRecord.iterator();
            List<String> recordValues = Arrays.asList(firstRecord.values());
            int i = 0;
            while (recordIterator.hasNext()) {
                Assert.assertEquals(recordValues.get(i), recordIterator.next());
                i++;
            }

            // Testing record toMap
            Map<String, String> map = firstRecord.toMap();
            Assert.assertEquals("New York", map.get("City"));
            Assert.assertEquals("30", map.get("Age"));
            Assert.assertEquals("John Doe", map.get("Name"));

            // Testing record toList
            List<String> recordList = firstRecord.toList();
            Assert.assertEquals(Arrays.asList("John Doe", "30", "New York"), recordList);

            // Testing record get method with Enum
            Assert.assertEquals("New York", firstRecord.get(CSVRecord.City.class));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (parser != null) {
                try {
                    parser.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}