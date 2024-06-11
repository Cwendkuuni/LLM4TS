import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testValuedEnumConstructor() {
        ValuedEnum enumInstance = new ValuedEnum("MyEnum", 10);
        assertEquals("MyEnum", enumInstance.getName());
        assertEquals(10, enumInstance.getValue());
    }

    @Test
    public void testGetEnumMethod() {
        List<ValuedEnum> enumList = Arrays.asList(ValuesEnum.JAVA1_0, ValuedEnum.JAVA1_1, ValuedEnum.JAVA1_2);
        ValuedEnum enumInstance = ValuedEnum.getEnum(ValuesEnum.class, ValuesEnum.JAVA1_1.getValue());
        assertEquals(ValuesEnum.JAVA1_1, enumInstance);
    }

    @Test
    public void testGetValueMethod() {
        ValuedEnum enumInstance = ValuedEnum.JAVA1_1;
        assertEquals(110, enumInstance.getValue());
    }

    @Test
    public void testCompareToMethod() {
        ValuedEnum enumInstance1 = ValuedEnum.JAVA1_0;
        ValuedEnum enumInstance2 = ValuedEnum.JAVA1_1;
        assertEquals(-1, enumInstance1.compareTo(enumInstance2));
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorArgument() {
        new ValuedEnum(null, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeValueConstructorArgument() {
        new ValuedEnum("MyEnum", -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEnumClassArgument() {
        ValuedEnum.getEnum(null, 10);
    }

    @Test
    public void testNonexistentEnumValue() {
        List<ValuedEnum> enumList = Arrays.asList(ValuesEnum.JAVA1_0, ValuedEnum.JAVA1_1, ValuedEnum.JAVA1_2);
        assertEquals(null, ValuedEnum.getEnum(ValuesEnum.class, Integer.MAX_VALUE));
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentInGetEnumMethod() {
        ValuedEnum.getEnum(null, 10);
    }

    @Test(expected = NullPointerException.class)
    public void testNullOtherArgumentInCompareToMethod() {
        ValuedEnum enumInstance = ValuedEnum.JAVA1_0;
        enumInstance.compareTo(null);
    }
}