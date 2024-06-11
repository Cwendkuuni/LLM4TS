import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
        ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_2");

        assertNotNull(java1_0);
        assertNotNull(java1_1);
        assertNotNull(java1_2);

        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());
        assertEquals("Java 1.2", java1_2.getName());
        assertEquals(120, java1_2.getValue());

        // Test invalid enum
        assertNull(ValuedEnum.getEnum("JavaVersionEnum", "INVALID"));
    }

    @Test
    public void testGetEnumWithValue() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", 100);
        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", 110);
        ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersionEnum", 120);

        assertNotNull(java1_0);
        assertNotNull(java1_1);
        assertNotNull(java1_2);

        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());
        assertEquals("Java 1.2", java1_2.getName());
        assertEquals(120, java1_2.getValue());

        // Test invalid value
        assertNull(ValuedEnum.getEnum("JavaVersionEnum", 200));
    }

    @Test
    public void testComparator() {
        ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum", "Java 1.0", 100);
        ValuedEnum java1_1 = new ValuedEnum("JavaVersionEnum", "Java 1.1", 110);
        ValuedEnum java1_2 = new ValuedEnum("JavaVersionEnum", "Java 1.2", 120);

        assertEquals(java1_0.compareTo(java1_1), -10);
        assertEquals(java1_1.compareTo(java1_2), -10);
        assertEquals(java1_0.compareTo(java1_2), -20);

        // Test self comparison
        assertEquals(java1_0.compareTo(java1_0), 0);
        assertEquals(java1_1.compareTo(java1_1), 0);
        assertEquals(java1_2.compareTo(java1_2), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEnumClass() {
        ValuedEnum.getEnum(null, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullName() {
        ValuedEnum.getEnum("JavaVersionEnum", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        ValuedEnum.getEnum("JavaVersionEnum", 0);
    }
}