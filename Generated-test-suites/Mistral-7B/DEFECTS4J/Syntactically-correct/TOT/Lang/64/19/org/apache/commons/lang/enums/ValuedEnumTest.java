import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.lang.enums.ValuedEnum;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersion", "Java 1.0");
        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersion", "Java 1.1");
        ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersion", "Java 1.2");

        assertEquals("JavaVersion[Java 1.0=100]", java1_0.toString());
        assertEquals("JavaVersion[Java 1.1=110]", java1_1.toString());
        assertEquals("JavaVersion[Java 1.2=120]", java1_2.toString());

        ValuedEnum unknown = ValuedEnum.getEnum("JavaVersion", "Java 1.3");
        assertNull(unknown);
    }

    @Test
    public void testGetEnumByValue() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersion", 100);
        ValuedEnum java1_1 = ValuedEnum.getEnum("JavaVersion", 110);
        ValuedEnum java1_2 = ValuedEnum.getEnum("JavaVersion", 120);

        assertEquals("JavaVersion[Java 1.0=100]", java1_0.toString());
        assertEquals("JavaVersion[Java 1.1=110]", java1_1.toString());
        assertEquals("JavaVersion[Java 1.2=120]", java1_2.toString());

        ValuedEnum unknown = ValuedEnum.getEnum("JavaVersion", 130);
        assertNull(unknown);
    }

    @Test
    public void testCompareTo() {
        ValuedEnum java1_0 = new ValuedEnum("JavaVersion1", 100);
        ValuedEnum java1_1 = new ValuedEnum("JavaVersion2", 110);
        ValuedEnum java1_2 = new ValuedEnum("JavaVersion3", 120);

        int result = java1_0.compareTo(java1_1);
        int expected = -10;
        assertEquals("java1_0 less than java1_1", expected, result);

        result = java1_1.compareTo(java1_0);
        expected = 10;
        assertEquals("java1_1 greater than java1_0", expected, result);

        result = java1_0.compareTo(java1_2);
        expected = -20;
        assertEquals("java1_0 less than java1_2", expected, result);

        result = java1_2.compareTo(java1_0);
        expected = 20;
        assertEquals("java1_2 greater than java1_0", expected, result);

        result = java1_1.compareTo(java1_1);
        expected = 0;
        assertEquals("java1_1 equals java1_1", expected, result);
    }
}