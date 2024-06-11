import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_1");
        ValuedEnum javaVersion1_2 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_2");

        assertNotNull(javaVersion1_0);
        assertNotNull(javaVersion1_1);
        assertNotNull(javaVersion1_2);

        assertEquals("Java 1.0", javaVersion1_0.getName());
        assertEquals(100, javaVersion1_0.getValue());

        assertEquals("Java 1.1", javaVersion1_1.getName());
        assertEquals(110, javaVersion1_1.getValue());

        assertEquals("Java 1.2", javaVersion1_2.getName());
        assertEquals(120, javaVersion1_2.getValue());
    }

    @Test
    public void testGetEnumInvalidEnumName() {
        assertNull(ValuedEnum.getEnum("JavaVersionEnum", "INVALID_ENUM_NAME"));
    }

    @Test
    public void testGetEnumInvalidEnumClass() {
        try {
            ValuedEnum.getEnum(Object.class.getClassLoader().loadClass("org.apache.commons.lang.enums.ValuedEnumInvalidClass"), "JAVA1_0");
            fail("Expected IllegalArgumentException");
        } catch (ClassNotFoundException e) {
            // Expected
        } catch (IllegalArgumentException e) {
            fail("Unexpected IllegalArgumentException");
        }
    }

    @Test
    public void testGetEnumInvalidValue() {
        assertNull(ValuedEnum.getEnum("JavaVersionEnum", 1000));
    }

    @Test
    public void testGetEnumMap() {
        List<ValuedEnum> javaVersions = ValuedEnum.getEnumList(ValuedEnum.class);
        Map<String, ValuedEnum> javaVersionMap = ValuedEnum.getEnumMap(ValuedEnum.class);

        assertEquals(javaVersions.size(), javaVersionMap.size());
        for (ValuedEnum javaVersion : javaVersions) {
            assertEquals(javaVersion.getName(), javaVersionMap.get(javaVersion.getName()).getName());
            assertEquals(javaVersion.getValue(), javaVersionMap.get(javaVersion.getName()).getValue());
        }
    }

    @Test
    public void testGetEnumMapEmptyEnumClass() {
        List<ValuedEnum> emptyJavaVersionList = ValuedEnum.getEnumList(EmptyEnum.class);
        Map<String, ValuedEnum> emptyJavaVersionMap = ValuedEnum.getEnumMap(EmptyEnum.class);

        assertEquals(0, emptyJavaVersionList.size());
        assertEquals(0, emptyJavaVersionMap.size());
    }

    @Test(expected = NullPointerException.class)
    public void testGetEnumMapNullEnumClass() {
        ValuedEnum.getEnumMap(null);
    }

    private static class EmptyEnum extends ValuedEnum {
        private EmptyEnum(String name, int value) {
            super(name, value);
        }
    }
}