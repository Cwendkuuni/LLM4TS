import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        assertNotNull(java1_0);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());

        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
        assertNotNull(java1_1);
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullClass() {
        ValuedEnum.getEnum(null, "JAVA1_0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullName() {
        Class<? extends ValuedEnum> javaVersionEnumClass = ValuedVersionEnum.class;
        ValuedEnum.getEnum(javaVersionEnumClass, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumInvalidName() {
        Class<? extends ValuedEnum> javaVersionEnumClass = ValuedVersionEnum.class;
        ValuedEnum.getEnum(javaVersionEnumClass, "InvalidEnumName");
    }

    @Test
    public void testGetEnumByValue() {
        ValuedEnum java1_0 = ValuedEnum.getEnum(JavaVersionEnum.class, 100);
        assertNotNull(java1_0);
        assertEquals("Java 1.0", java1_0.getName());
        assertEquals(100, java1_0.getValue());

        ValuedEnum java1_1 = ValuedEnum.getEnum(JavaVersionEnum.class, 110);
        assertNotNull(java1_1);
        assertEquals("Java 1.1", java1_1.getName());
        assertEquals(110, java1_1.getValue());

        ValuedEnum java1_2 = ValuedEnum.getEnum(JavaVersionEnum.class, 120);
        assertNull(java1_2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNegativeValue() {
        ValuedEnum.getEnum(JavaVersionEnum.class, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNonExistentValue() {
        ValuedEnum.getEnum(JavaVersionEnum.class, 150);
    }
}