import org.apache.commons.lang.enums.ValuedEnum;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class ValuedEnumTest {

    @Test
    public void testGetEnum() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        Assert.assertNotNull(java1_0);
        Assert.assertEquals("Java 1.0", java1_0.getName());
        Assert.assertEquals(100, java1_0.getValue());

        ValuedEnum nullEnum = ValuedEnum.getEnum("JavaVersionEnum", "null");
        Assert.assertNull(nullEnum);

        ValuedEnum unknownEnum = ValuedEnum.getEnum("JavaVersionEnum", "UNKNOWN");
        Assert.assertNull(unknownEnum);
    }

    @Test
    public void testGetEnumInt() {
        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", 100);
        Assert.assertNotNull(java1_0);
        Assert.assertEquals("Java 1.0", java1_0.getName());
        Assert.assertEquals(100, java1_0.getValue());

        ValuedEnum wrongEnumType = ValuedEnum.getEnum(String.class, 100);
        Assert.assertNull(wrongEnumType);

        ValuedEnum negativeValue = ValuedEnum.getEnum("JavaVersionEnum", -1);
        Assert.assertNull(negativeValue);
    }

    @Test
    public void testGetEnumMap() {
        List<ValuedEnum> enumList = ValuedEnum.getEnumList(ValuedEnum.class);
        Map<String, ValuedEnum> enumMap = new HashMap<>();
        for (ValuedEnum enumItem : enumList) {
            enumMap.put(enumItem.getName(), enumItem);
        }

        ValuedEnum java1_0 = ValuedEnum.getEnum("JavaVersionEnum", "JAVA1_0");
        Assert.assertEquals(java1_0, enumMap.get("Java 1.0"));

        ValuedEnum nullEnum = ValuedEnum.getEnum("JavaVersionEnum", null);
        Assert.assertNull(enumMap.get(null));

        ValuedEnum unknownEnum = ValuedEnum.getEnum("JavaVersionEnum", "UNKNOWN");
        Assert.assertNull(unknownEnum);
    }

    @Test
    public void testCompareTo() {
        ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum", 100) {
            @Override
            public int compareTo(Object other) {
                if (other instanceof ValuedEnum) {
                    return super.compareTo(other);
                } else {
                    throw new IllegalArgumentException("Other object is not an Enum.");
                }
            }
        };

        ValuedEnum java1_1 = new ValuedEnum("JavaVersionEnum", 110) {
            @Override
            public int compareTo(Object other) {
                if (other instanceof ValuedEnum) {
                    return super.compareTo(other);
                } else {
                    throw new IllegalArgumentException("Other object is not an Enum.");
                }
            }
        };

        ValuedEnum java1_2 = new ValuedEnum("JavaVersionEnum", 120) {
            @Override
            public int compareTo(Object other) {
                if (other instanceof ValuedEnum) {
                    return super.compareTo(other);
                } else {
                    throw new IllegalArgumentException("Other object is not an Enum.");
                }
            }
        };

        int comparisonResult = java1_0.compareTo(java1_1);
        Assert.assertThat(comparisonResult, is(-10));

        comparisonResult = java1_1.compareTo(java1_0);
        Assert.assertThat(comparisonResult, is(10));

        comparisonResult = java1_0.compareTo(java1_2);
        Assert.assertThat(comparisonResult, is(-10));
    }

    @Test
    public void testToString() {
        ValuedEnum java1_0 = new ValuedEnum("JavaVersionEnum", 100) {
            @Override
            public String toString() {
                return "CustomJavaVersionEnum{" + super.toString() + "}";
            }
        };

        String java1_0String = java1_0.toString();
        Assert.assertEquals("CustomJavaVersionEnum[Java 1.0=100]", java1_0String);
    }
}