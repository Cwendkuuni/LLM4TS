import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnum.getValueOf(ValuedEnum.class, "JavaVersionEnum", "JAVA1_0");
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());

        ValuedEnum java1_1 = ValuedEnum.getValueOf(ValuedEnum.class, "JavaVersionEnum", "JAVA1_1");
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());
    }

    @Test
    public void testGetEnumValueByInt() {
        ValuedEnum java1_0 = ValuedEnum.getEnum(ValuedEnum.class, 100);
        assertEquals("Java 1.0", java1_0.getName());

        ValuedEnum java1_1 = ValuedEnum.getEnum(ValuedEnum.class, 110);
        assertEquals("Java 1.1", java1_1.getName());

        assertNull(ValuedEnum.getEnum(ValuedEnum.class, 120));
    }

    @Test
    public void testGetEnumMap() {
        List<ValuedEnum> javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);
        Map<String, ValuedEnum> map = new HashMap<>();
        for (ValuedEnum javaVersion : javaVersions) {
            map.put(javaVersion.getName(), javaVersion);
        }
        assertEquals(javaVersions.size(), map.size());

        ValuedEnum java1_0 = map.get("Java 1.0");
        assertEquals(100, java1_0.getValue());

        assertNull(map.get("Unknown"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullName() {
        try {
            ValuedEnum.getValueOf(ValuedEnum.class, null, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullValue() {
        try {
            ValuedEnum.getValueOf(ValuedEnum.class, "JavaVersionEnum", null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithUnknownName() {
        try {
            ValuedEnum.getValueOf(ValuedEnum.class, "JavaVersionEnum", "Unknown");
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithUnknownValue() {
        try {
            ValuedEnum.getEnum(ValuedEnum.class, 120);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumWithNullClass() {
        try {
            ValuedEnum.getValueOf(null, "JavaVersionEnum", "JAVA1_0");
            fail();
        } catch (NullPointerException e) {
            // expected
        }
    }
}