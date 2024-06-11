package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.*;
import org.junit.Test;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder builder = new HashCodeBuilder();
        assertEquals(37, builder.hashCode());

        HashCodeBuilder builderWithInitialValue = new HashCodeBuilder(11, 31);
        assertEquals(11 * 31 + 37, builderWithInitialValue.hashCode());

        HashCodeBuilder builderWithInitialValueAndSuperHashCode = new HashCodeBuilder(11, 31, 5);
        assertEquals(11 * 31 + 37 + 5, builderWithInitialValueAndSuperHashCode.hashCode());
    }

    @Test
    public void testReflectionHashCode() {
        String testClassName = "org.apache.commons.lang3.builder.HashCodeBuilderTest";

        Object testObject = new Object() {
            int id;
            String name;
            long[] array;

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public long[] getArray() {
                return array;
            }
        };

        int hashCode = HashCodeBuilder.reflectionHashCode(testObject, false);
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(testObject.getClass().getField("id").get(testObject)).append(testObject.getClass().getField("name").get(testObject)).append(testObject.getClass().getField("array").get(testObject));
        assertEquals(hashCode, builder.toHashCode());

        Object[] testObjectArray = new Object[] { testObject, new String("test"), new Integer(123), new Long(456) };
        hashCode = HashCodeBuilder.reflectionHashCode(testObjectArray, true, Object[].class, new String[]{"name", "class"});
        builder = new HashCodeBuilder();
        builder.append(testObject).append(new String("test")).append(new Integer(123)).append(new Long(456));
        assertEquals(hashCode, builder.toHashCode());
    }

    @Test
    public void testReflectionHashCodeWithExcludedFields() {
        String testClassName = "org.apache.commons.lang3.builder.HashCodeBuilderTest";

        Object testObject = new Object() {
            int id;
            String name;
            String excludedField;
            long[] array;

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public String getExcludedField() {
                return excludedField;
            }

            public long[] getArray() {
                return array;
            }
        };

        int hashCode = HashCodeBuilder.reflectionHashCode(testObject, false);
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(testObject.getClass().getField("id").get(testObject)).append(testObject.getClass().getField("name").get(testObject)).append(testObject.getClass().getField("array").get(testObject));
        assertEquals(hashCode, builder.toHashCode());

        HashCodeBuilder builderWithExcludedFields = new HashCodeBuilder();
        builderWithExcludedFields.append(testObject.getClass().getField("id").get(testObject)).append(testObject.getClass().getField("name").get(testObject));
        assertEquals(hashCode, builderWithExcludedFields.toHashCode());

        Object[] testObjectArray = new Object[] { testObject, new String("test"), new Integer(123), new Long(456) };
        hashCode = HashCodeBuilder.reflectionHashCode(testObjectArray, true, Object[].class, new String[]{"name", "class"}, new String[]{"excludedField", "class"});
        builder = new HashCodeBuilder();
        builder.append(testObject).append(new String("test")).append(new Integer(123)).append(new Long(456));
        assertEquals(hashCode, builder.toHashCode());

        builderWithExcludedFields = new HashCodeBuilder();
        builderWithExcludedFields.append(testObject).append(new String("test")).append(new Integer(123)).append(new Long(456));
        assertEquals(hashCode, builderWithExcludedFields.toHashCode());
    }

    @Test
    public void testReflectionHashCodeWithTransients() {
        String testClassName = "org.apache.commons.lang3.builder.HashCodeBuilderTest";

        class TestClass {
            int id;
            transient String name;
            long array[] = {1, 2, 3};

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public long[] getArray() {
                return array;
            }
        }

        Object testObject = new TestClass();
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject, true);
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(testObject.getClass().getField("id").get(testObject));
        assertEquals(hashCode, builder.toHashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNegativeInitialValue() {
        HashCodeBuilder.reflectionHashCode(-17, 37, new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithEvenMultiplier() {
        HashCodeBuilder.reflectionHashCode(17, 34, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testReflectionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(17, 37, null);
    }
}