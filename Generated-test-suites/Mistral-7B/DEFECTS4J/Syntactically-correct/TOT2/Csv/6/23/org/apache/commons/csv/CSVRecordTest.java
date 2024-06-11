
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;;

@RunWith(JUnit4.class)
public class CSVRecordTest {

    @Test
    public void testGetMethodWithValidEnum() {
        String[] values = new String[]{"apple", "orange", "banana"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Fruit", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Enum testEnum = Enum.valueOf(CSVRecord.class, "Fruit");
        String expectedValue = "apple";

        String actualValue = record.get(testEnum);

        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodWithInvalidEnum() {
        String[] values = new String[]{"apple", "orange", "banana"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Fruit", 0);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        Enum testEnum = Enum.valueOf(CSVRecord.class, "InvalidEnum");
        record.get(testEnum);
    }

    @Test
    public void testGetMethodWithValidIndex() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        int expectedIndex = 1;
        String expectedValue = "Doe";

        String actualValue = record.get(expectedIndex);

        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetMethodWithOutOfBoundsIndex() {
        String[] values = new String[]{"John", "Doe", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        int expectedIndex = 3;
        record.get(expectedIndex);
    }

    @Test
    public void testGetMethodWithValidName() {
        String[] values = new String[]{"Name", "John", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String expectedName = "Name";
        String expectedValue = "John";

        String actualValue = record.get(expectedName);

        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMethodWithInvalidName() {
        String[] values = new String[]{"Name", "John", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 0);
        mapping.put("Age", 2);
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String expectedName = "InvalidName";
        record.get(expectedName);
    }

    @Test
    public void testGetMethodWithInconsistentRecord() {
        String[] values = new String[]{"Name", "John", "Age", "30"};
        Map<String, Integer> mapping = new HashMap<>();
        mapping.put("Name", 1); // Mapping is incorrect
        CSVRecord record = new CSVRecord(values, mapping, null, 1);

        String expectedName = "Name";
        String expectedValue = "John";

        try {
            record.get(expectedName);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}
