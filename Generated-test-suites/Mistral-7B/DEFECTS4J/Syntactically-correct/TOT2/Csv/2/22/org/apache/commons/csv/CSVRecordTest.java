import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CSVRecordTest {

    @Test
    public void testGetInt() {
        CSVRecord record = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
        record.values = new String[]{"John", "25", "New York"};

        assertEquals("Age", record.get(1));
    }
}