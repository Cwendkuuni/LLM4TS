import org.apache.commons.lang.enums.ValuedEnum;
import org.apache.commons.lang.enums.ValuedEnumTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum("JavaVersion", "Java 1.0");
        assertEquals(javaVersion1_0.getValue(), ValuedEnum.JAVA1_0_VALUE);

        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum("JavaVersion", "Java 1.1");
        assertEquals(javaVersion1_1.getValue(), ValuedEnum.JAVA1_1_VALUE);

        ValuedEnum nullEnum = ValuedEnum.getEnum("JavaVersion", null);
        assertNull(nullEnum);

        ValuedEnum unknownEnum = ValuedEnum.getEnum("UnknownEnum", "Unknown Value");
        assertNull(unknownEnum);
    }

    @Test
    public void testGetEnumWithValue() {
        ValuedEnum javaVersion1_0 = ValuedEnum.getEnum(ValuedEnum.class, ValuedEnum.JAVA1_0_VALUE);
        assertEquals(javaVersion1_0.getName(), "Java 1.0");

        ValuedEnum javaVersion1_1 = ValuedEnum.getEnum(ValuedEnum.class, ValuedEnum.JAVA1_1_VALUE);
        assertEquals(javaVersion1_1.getName(), "Java 1.1");

        ValuedEnum nullEnum = ValuedEnum.getEnum(ValuedEnum.class, 0);
        assertNull(nullEnum);

        ValuedEnum unknownEnum = ValuedEnum.getEnum(ValuedEnum.class, 1000);
        assertNull(unknownEnum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullEnumClass() {
        ValuedEnum.getEnum(null, "Java 1.0");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNullName() {
        ValuedEnum.getEnum(ValuedEnum.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithInvalidName() {
        ValuedEnum.getEnum(ValuedEnum.class, "InvalidName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumWithNegativeValue() {
        ValuedEnum.getEnum(ValuedEnum.class, -1);
    }
}