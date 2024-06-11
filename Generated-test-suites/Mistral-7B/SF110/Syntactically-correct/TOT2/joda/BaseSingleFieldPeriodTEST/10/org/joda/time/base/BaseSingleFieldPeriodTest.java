import org.joda.time.base.BaseSingleFieldPeriod;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSingleFieldPeriodTest {
    @Test
    public void testGetValue() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(10);
        int actual = period.getValue();
        int expected = 10;
        assertEquals(expected, actual);

        period = new MySingleFieldPeriod(0);
        actual = period.getValue();
        expected = 0;
        assertEquals(expected, actual);

        period = new MySingleFieldPeriod(-5);
        try {
            actual = period.getValue();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetFieldType() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(10);
        DurationFieldType actual = period.getFieldType();
        DurationFieldType expected = MyFieldType.INSTANCE;
        assertEquals(expected, actual);

        period = new AnotherSingleFieldPeriod(10);
        try {
            actual = period.getFieldType();
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testSize() {
        BaseSingleFieldPeriod period = new MySingleFieldPeriod(10);
        int actual = period.size();
        int expected = 1;
        assertEquals(expected, actual);

        try {
            period = new MultipleFieldsPeriod(10, 5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}

enum MyFieldType implements DurationFieldType {
    INSTANCE;
}

class MySingleFieldPeriod extends BaseSingleFieldPeriod {
    MySingleFieldPeriod(int iPeriod) {
        super(iPeriod);
    }
}

class AnotherSingleFieldPeriod extends BaseSingleFieldPeriod {
    AnotherSingleFieldPeriod(int iPeriod) {
        super(iPeriod);
    }

    @Override
    public DurationFieldType getFieldType() {
        return new AnotherFieldType();
    }
}

enum AnotherFieldType implements DurationFieldType {
    INSTANCE;
}

class MultipleFieldsPeriod extends BaseSingleFieldPeriod {
    MultipleFieldsPeriod(int period1, int period2) {
        super(period1);
        super.setValue(period2);
    }
}