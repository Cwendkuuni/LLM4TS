import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {
    @Test
    public void testGetMethodUsingIndex() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
        String expectedName = "Name";
        String actualName = record.get(0);
        assertEquals(expectedName, actualName);
    }
}