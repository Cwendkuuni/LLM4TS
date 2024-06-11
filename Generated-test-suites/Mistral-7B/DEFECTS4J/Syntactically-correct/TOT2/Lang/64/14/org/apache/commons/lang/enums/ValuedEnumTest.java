import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang.Enum;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        String name = "TestEnum";
        int value = 1;
        ValuedEnum enumInstance = new ValuedEnum(name, value);
        assertEquals(name, enumInstance.getName());
        assertEquals(value, enumInstance.getValue());
    }

    @Test
    public void testGetEnum() throws Exception {
        Class enumClass = ValuedEnum.class;
        Enum testEnum = ValuedEnum.getEnum(enumClass, 1);
        assertEquals(testEnum.getName(), "TestEnum");
        assertEquals(testEnum.getValue(), 1);
    }

    @Test
    public void testGetValue() {
        ValuedEnum testEnum = new ValuedEnum("TestEnum", 1);
        assertEquals(1, testEnum.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new ValuedEnum("Enum1", 1);
        ValuedEnum enum2 = new ValuedEnum("Enum2", 2);
        assertEquals(-1, enum1.compareTo(enum2));
        assertEquals(1, enum2.compareTo(enum1));
        assertEquals(0, enum1.compareTo(enum1));
    }

    @Test
    public void testToString() {
        ValuedEnum testEnum = new ValuedEnum("TestEnum", 1);
        String expected = "TestEnum[TestEnum=1]";
        String actual = testEnum.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        int value = 1;
        Class enumClass = null;
        Enum testEnum = ValuedEnum.getEnum(enumClass, value);
    }

    @Test
    public void testGetEnumWithNonExistentValue() {
        Class enumClass = ValuedEnum.class;
        Enum testEnum = ValuedEnum.getEnum(enumClass, 100);
        assertNull(testEnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullName() {
        String name = null;
        int value = 1;
        try {
            new ValuedEnum(name, value);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Name cannot be null"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeValue() {
        String name = "TestEnum";
        int value = -1;
        try {
            new ValuedEnum(name, value);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Value cannot be negative"));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithDuplicateName() {
        String name = "TestEnum";
        int value1 = 1;
        int value2 = 1;
        try {
            new ValuedEnum(name, value1);
            new ValuedEnum(name, value2);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Enum constant " + name + "=" + value1 + " already exists"));
        }
    }
}