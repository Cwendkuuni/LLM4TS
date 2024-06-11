
import static org.junit.Assert.*;
import org.apache.commons.math.stat.Frequency;

public class FrequencyTest {

    @Test
    public void testAddValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(3, frequency.getCount(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNonComparableValue() {
        Frequency frequency = new Frequency();
        frequency.addValue("not a number");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(null);
    }

    @Test
    public void testAddDuplicateValue() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue(1);
        frequency.addValue(1);
        assertEquals(3, frequency.getCount(1));
    }

    @Test(expected = ClassCastException.class)
    public void testAddValuesOfDifferentTypes() {
        Frequency frequency = new Frequency();
        frequency.addValue(1);
        frequency.addValue('a');
    }
}