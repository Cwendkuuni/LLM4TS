import org.junit.Test;
import org.apache.commons.lang.enums.ValuedEnum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        assertEquals("JavaVersionEnum_JAVA1_0", java1_0.toString());
        assertEquals(100, java1_0.getValue());

        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
        assertEquals("JavaVersionEnum_JAVA1_1", java1_1.toString());
        assertEquals(110, java1_1.getValue());

        ValuedEnum nullEnum = ValuedEnum.getEnum("JavaVersionEnum", "NONE");
        assertNull(nullEnum);
    }

    @Test
    public void testGetEnumWithValue() {
        ValuedEnum java1_0 = ValuedEnum.getEnum(ValuedEnum.class, 100);
        assertEquals("JavaVersionEnum_JAVA1_0", java1_0.toString());

        ValuedEnum java1_1 = ValuedEnum.getEnum(ValuedEnum.class, 110);
        assertEquals("JavaVersionEnum_JAVA1_1", java1_1.toString());

        ValuedEnum nullEnum = ValuedEnum.getEnum(ValuedEnum.class, 120);
        assertNull(nullEnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithInvalidClass() {
        ValuedEnum.getEnum(String.class.getClassLoader().loadClass("org.apache.commons.lang.StringUtils"), "100");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithInvalidName() {
        ValuedEnum.getEnum(ValuedEnum.class, 123);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullName() {
        ValuedEnum.getEnum(ValuedEnum.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullClass() {
        ValuedEnum.getEnum(null, "JAVA1_0");
    }

    @Test
    public void testCompareTo() {
        ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum_JAVA1_0", 100);
        ValuedEnum java1_1 = new ValuedEnum("JavaVersionEnum_JAVA1_1", 110);
        ValuedEnum java1_2 = new ValuedEnum("JavaVersionEnum_JAVA1_2", 120);

        assertEquals(java1_0.compareTo(java1_0), 0);
        assertEquals(java1_0.compareTo(java1_1), -1);
        assertEquals(java1_1.compareTo(java1_0), 1);
        assertEquals(java1_1.compareTo(java1_1), 0);
        assertEquals(java1_1.compareTo(java1_2), -1);
        assertEquals(java1_2.compareTo(java1_1), 1);
    }

    @Test
    public void testToString() {
        ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum_JAVA1_0", 100);
        assertEquals("JavaVersionEnum_JAVA1_0", java1_0.toString());
    }
}