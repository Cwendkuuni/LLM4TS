import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.ClassNotFoundException;
import java.lang.reflect.IllegalArgumentException;

public class ValuedEnumTest {

    @Test
    public void testConstructor() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        assertEquals("TestEnum1", enum1.getName());
        assertEquals(1, enum1.getValue());
    }

    @Test
    public void testGetEnum() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        ValuedEnum enum2 = ValuedEnum.getEnum(ValuedEnum.class, 1);
        assertEquals(enum1, enum2);
    }

    @Test
    public void testGetValue() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        assertEquals(1, enum1.getValue());
    }

    @Test
    public void testCompareTo() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        ValuedEnum enum2 = new ValuedEnum("TestEnum2", 2);
        assertEquals(-1, enum1.compareTo(enum2));
        assertEquals(1, enum2.compareTo(enum1));
        assertEquals(0, enum1.compareTo(enum1));
    }

    @Test
    public void testToString() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        assertEquals("TestEnum1[name=TestEnum1,value=1]", enum1.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullEnumClass() {
        ValuedEnum.getEnum(null, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumNullValue() {
        ValuedEnum.getEnum(ValuedEnum.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumInvalidEnumClass() {
        ValuedEnum.getEnum(String.class, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumInvalidValue() {
        ValuedEnum.getEnum(ValuedEnum.class, 10000);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToNullObject() {
        ValuedEnum enum1 = new ValuedEnum("TestEnum1", 1);
        assertNull(enum1.compareTo(null));
    }

    @Test(expected = ClassNotFoundException.class)
    public void testGetEnumUnknownClass() {
        Class unknownClass = ClassUtils.getClass("org.unknown.UnknownEnum");
        ValuedEnum.getEnum(unknownClass, 1);
    }
}