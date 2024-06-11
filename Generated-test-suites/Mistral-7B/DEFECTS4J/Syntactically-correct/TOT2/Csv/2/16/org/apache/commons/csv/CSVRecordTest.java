import static org.junit.Assert.*;
import org.apache.commons.csv.CSVRecord;

public class CSVRecordTest {

    @Test
    public void testGetWithIntegerIndex() {
        String[] values = new String[] {"John", "Doe", "30", "New York"};
        CSVRecord record = new CSVRecord(values, null, null, 1);
        String expected = "Doe";
        String actual = record.get(1);
        assertEquals(expected, actual);
    }
}