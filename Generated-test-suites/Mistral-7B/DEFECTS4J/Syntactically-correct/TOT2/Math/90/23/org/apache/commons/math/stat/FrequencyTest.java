import org.apache.commons.math.stat.Frequency;
import org.junit.Test;

public class FrequencyTest {

    @Test
    public void testAddValueObject() {
        Frequency frequency = new Frequency();
        frequency.addValue("test");
        long expectedCount = 1;
        long actualCount = frequency.getCount("test");
        assertEquals(expectedCount, actualCount);
    }
}