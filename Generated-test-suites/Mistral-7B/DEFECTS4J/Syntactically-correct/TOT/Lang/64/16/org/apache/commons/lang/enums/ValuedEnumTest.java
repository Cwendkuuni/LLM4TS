import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());

        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());
    }

    @Test
    public void testGetEnumInvalidName() {
        try {
            ValuedEnum unknown = ValuedEnum.getEnum("JavaVersionEnum", "UNKNOWN");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetEnumInvalidValue() {
        try {
            ValuedEnum unknown = ValuedEnum.getEnum("JavaVersionEnum", 150);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetEnumNullClass() {
        try {
            ValuedEnum unknown = ValuedEnum.getEnum(null, 100);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testGetEnumMap() {
        List javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);
        Map map = ValuedEnum.getEnumMap(ValuedEnum.class);

        assertEquals(javaVersions.size(), map.size());

        for (Object enumConstant : javaVersions) {
            ValuedEnum value = (ValuedEnum) enumConstant;
            assertEquals(value, map.get(value.getName()));
            assertEquals(value.getName(), map.getKeySet().iterator().next());
        }
    }

    @Test
    public void testGetEnumList() {
        List javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);
        Iterator iterator = javaVersions.iterator();

        assertEquals(4, javaVersions.size());
        assertEquals("JavaVersion[Java 1.0=100]", iterator.next().toString());
        assertEquals("JavaVersion[Java 1_1=110]", iterator.next().toString());
        // ...
    }
}