import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

    @Test
    public void Test_Constructor() {
        // Test default constructor
        ValuedEnum testEnum = new ValuedEnum("TestEnum", 1);
        // Test constructor with name and value
        ValuedEnum testEnum2 = new ValuedEnum("TestEnum2", 2);
    }

    @Test
    public void Test_getValue() {
        ValuedEnum testEnum = new ValuedEnum("TestEnum", 1);
        assertEquals(1, testEnum.getValue());

        // Test invalid input
        try {
            ValuedEnum invalidTestEnum = new ValuedEnum(null, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void Test_compareTo() {
        ValuedEnum testEnum1 = new ValuedEnum("TestEnum1", 1);
        ValuedEnum testEnum2 = new ValuedEnum("TestEnum2", 2);

        assertEquals(1, testEnum1.compareTo(testEnum1));
        assertEquals(-1, testEnum1.compareTo(testEnum2));
        assertEquals(1, testEnum2.compareTo(testEnum1));

        // Test exception handling
        try {
            ValuedEnum testEnum3 = new ValuedEnum(null, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void Test_getValue_EdgeCase() {
        ValuedEnum testEnum = new ValuedEnum("TestEnum", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, testEnum.getValue());
    }

    @Test
    public void Test_compareTo_EdgeCase() {
        ValuedEnum testEnum1 = new ValuedEnum("TestEnum1", Integer.MIN_VALUE);
        ValuedEnum testEnum2 = new ValuedEnum("TestEnum2", Integer.MIN_VALUE);

        assertEquals(-1, testEnum1.compareTo(testEnum2));
        assertEquals(0, testEnum1.compareTo(testEnum1));
    }

    @Test(expected = NullPointerException.class)
    public void Test_getEnum_NullParameter() {
        Class enumClass = ValuedEnum.class;
        int value = 1;
        ValuedEnum.getEnum(null, value);
    }
}