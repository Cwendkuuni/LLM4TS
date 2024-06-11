import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        assertInstanceOf(ValuedEnum.class, java1_0);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());

        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
        assertInstanceOf(ValuedEnum.class, java1_1);
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());

        ValuedEnum unknown = ValuedEnum.getEnum("JavaVersionEnum", "UNKNOWN");
        assertNull(unknown);
    }

    @Test
    public void testGetEnumValueByNumber() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", 100);
        assertInstanceOf(ValuedEnum.class, java1_0);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());

        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", 110);
        assertInstanceOf(ValuedEnum.class, java1_1);
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());

        ValuedEnum unknown = ValuedEnum.getEnum("JavaVersionEnum", 120);
        assertNull(unknown);
    }

    @Test
    public void testGetEnumMap() {
        ValuedEnum[] values = ValuedEnum.values();
        Map<String, ValuedEnum> map = ValuedEnum.getEnumMap(values.getClass());
        ValuedEnum java1_0 = map.get("Java 1.0");
        assertInstanceOf(ValuedEnum.class, java1_0);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
    }

    @Test
    public void testGetEnumList() {
        ValuedEnum[] values = ValuedEnum.values();
        List<ValuedEnum> list = ValuedEnum.getEnumList(values.getClass());
        ValuedEnum java1_0 = list.get(0);
        assertInstanceOf(ValuedEnum.class, java1_0);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
    }

    @Test
    public void testIterator() {
        ValuedEnum[] values = ValuedEnum.values();
        Iterator<ValuedEnum> iterator = ValuedEnum.iterator(values.getClass());
        ValuedEnum java1_0 = iterator.next();
        assertInstanceOf(ValuedEnum.class, java1_0);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());

        ValuedEnum java1_1 = iterator.next();
        assertInstanceOf(ValuedEnum.class, java1_1);
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());

        assertFalse(iterator.hasNext());
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnumClass() {
        ValuedEnum.iterator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEnumValue() {
        try {
            ValuedEnum.getEnum("JavaVersionEnum", "INVALID");
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The Enum Class must not be null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValue() {
        try {
            ValuedEnum.getEnum("JavaVersionEnum", -1);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("The Enum value must be a positive integer.", e.getMessage());
        }
    }
}