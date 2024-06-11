import org.apache.commons.lang.Enum;
import org.apache.commons.lang.ValuedEnum;
import org.junit.Assert;
import org.junit.Test;

public class ValuedEnumTest {

    /* Existing test cases */

    @Test
    public void testValidEnumCreation() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        assertNotNull(java1_0);
    }

    @Test
    public void testGetEnumByValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum(JavaVersionEnum.class, JavaVersionEnum.JAVA1_0_VALUE);
        assertEquals(JavaVersionEnum.JAVA1_0, java1_0);
    }

    @Test
    public void testGetValue() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals(JavaVersionEnum.JAVA1_0_VALUE, java1_0.getValue());
    }

    @Test
    public void testCompareTo() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        JavaVersionEnum java1_1 = JavaVersionEnum.getEnum("Java 1.1");
        JavaVersionEnum java1_2 = JavaVersionEnum.getEnum("Java 1.2");

        assertEquals(0, java1_0.compareTo(java1_0));
        assertEquals(-1, java1_0.compareTo(java1_1));
        assertEquals(1, java1_1.compareTo(java1_0));
        assertEquals(-1, java1_0.compareTo(java1_2));
        assertEquals(1, java1_2.compareTo(java1_0));
    }

    @Test
    public void testToString() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        assertEquals("JavaVersion[Java 1.0=100]", java1_0.toString());
    }

    /* New test cases */

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByNullClassName() {
        JavaVersionEnum.getEnum(null, JavaVersionEnum.JAVA1_0_VALUE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumByNegativeValue() {
        JavaVersionEnum.getEnum(JavaVersionEnum.class, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareToNullObject() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        assertNotNull(java1_0);
        assertNull(java1_0.compareTo(null));
    }

    @Test
    public void testCompareToDifferentClasses() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        OtherEnum otherEnum = new OtherEnum("OtherEnum", 100);

        assertEquals(0, java1_0.compareTo(java1_0));
        assertEquals(-1, java1_0.compareTo(otherEnum));
    }

    @Test
    public void testCompareToEqualObjects() {
        JavaVersionEnum java1_0 = JavaVersionEnum.getEnum("Java 1.0");
        JavaVersionEnum java1_0_copy = new JavaVersionEnum("Java 1.0", JavaVersionEnum.JAVA1_0_VALUE);

        assertEquals(0, java1_0.compareTo(java1_0_copy));
    }

}

class OtherEnum extends ValuedEnum {
    OtherEnum(String name, int value) {
        super(name, value);
    }
}