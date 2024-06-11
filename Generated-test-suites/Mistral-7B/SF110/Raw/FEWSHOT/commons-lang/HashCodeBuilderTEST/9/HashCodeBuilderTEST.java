package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.IDKey;
import org.apache.commons.lang3.reflect.ArrayUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        assertEquals(32, builder.toHashCode());
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean[] booleanArray = {true, false, true};
        builder.append(booleanArray);
        assertEquals(1900841985, builder.toHashCode());
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((byte) 0);
        assertEquals(33, builder.toHashCode());
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        byte[] byteArray = {3, 1, 6};
        builder.append(byteArray);
        assertEquals(1432821575, builder.toHashCode());
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append('A');
        assertEquals(35, builder.toHashCode());
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        char[] charArray = {'A', 'B', 'C'};
        builder.append(charArray);
        assertEquals(892255072, builder.toHashCode());
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1.23d);
        long doubleHashCode = Double.doubleToLongBits(1.23d);
        assertEquals(doubleHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        double[] doubleArray = {1.23, 2.46, 3.6};
        builder.append(doubleArray);
        long totalHashCode = 0;
        for (double value : doubleArray) {
            totalHashCode = 31 * (totalHashCode + Double.doubleToLongBits(value));
        }
        assertEquals(totalHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((float) 1.23f);
        long floatHashCode = Float.floatToIntBits((float) 1.23f);
        assertEquals(floatHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        float[] floatArray = {1.23f, 2.46f, 3.6f};
        builder.append(floatArray);
        long totalHashCode = 0;
        for (float value : floatArray) {
            totalHashCode = 31 * (totalHashCode + Float.floatToIntBits(value));
        }
        assertEquals(totalHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1);
        assertEquals(33, builder.toHashCode());
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        int[] intArray = {1, 2, 3};
        builder.append(intArray);
        long totalHashCode = 1;
        for (int value : intArray) {
            totalHashCode = 31 * (totalHashCode + value);
        }
        assertEquals(totalHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(1L);
        assertEquals(33, builder.toHashCode());
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        long[] longArray = {1, 2, 3};
        builder.append(longArray);
        long totalHashCode = 1;
        for (long value : longArray) {
            totalHashCode = 31 * (totalHashCode + value);
        }
        assertEquals(totalHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder builder = new HashCodeBuilder();
        String object = "Test";
        builder.append(object);
        assertEquals(object.hashCode(), builder.toHashCode());
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        String[] objectArray = {"Test1", "Test2", "Test3"};
        builder.append(objectArray);
        long totalHashCode = 0;
        for (String value : objectArray) {
            totalHashCode = 31 * (totalHashCode + value.hashCode());
        }
        assertEquals(totalHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append((short) 1);
        assertEquals(35, builder.toHashCode());
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder builder = new HashCodeBuilder();
        short[] shortArray = {1, 2, 3};
        builder.append(shortArray);
        long totalHashCode = 1;
        for (short value : shortArray) {
            totalHashCode = 31 * (totalHashCode + value);
        }
        assertEquals(totalHashCode, builder.toHashCode());
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder childBuilder = new HashCodeBuilder();
        childBuilder.append(42);
        HashCodeBuilder parentBuilder = new HashCodeBuilder();
        parentBuilder.appendSuper((int) childBuilder.toHashCode());
        assertEquals(42 * 31 + 17, parentBuilder.toHashCode());
    }

    @Test
    public void testReflectionHashCode_Object() {
        Object obj = new Object();
        assertEquals(obj.hashCode(), HashCodeBuilder.reflectionHashCode(obj));
    }

    @Test
    public void testReflectionHashCode_Object_Boolean() {
        Object obj = new Object();
        boolean testTransients = false;
        assertEquals(obj.hashCode(), HashCodeBuilder.reflectionHashCode(obj, testTransients));
    }

    @Test
    public void testReflectionHashCode_Object_Boolean_Class_StringArray() {
        Object obj = new Object() {
            private int id = 1;
            private String name;

            public int getId() {
                return id;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "Object{" +
                        "id=" + getId() +
                        ", name='" + name + '\'' +
                        '}';
            }
        };
        boolean testTransients = false;
        String[] excludeFields = {"name"};
        assertEquals(obj.hashCode(), HashCodeBuilder.reflectionHashCode(obj, testTransients, Object.class, excludeFields));
    }

    @Test
    public void testReflectionHashCode_Object_Boolean_Class_CollectionString() {
        Object obj = new Object() {
            private int id = 1;
            private String name;

            public int getId() {
                return id;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "Object{" +
                        "id=" + getId() +
                        ", name='" + name + '\'' +
                        '}';
            }
        };
        boolean testTransients = false;
        Collection<String> excludeFields = Arrays.asList("name");
        assertEquals(obj.hashCode(), HashCodeBuilder.reflectionHashCode(obj, testTransients, Object.class, excludeFields.toArray(new String[0])));
    }

    @Test
    public void testReflectionHashCode_Object_Boolean_Class_StringArray_StringArray() {
        Object obj = new Object() {
            private int id = 1;
            private String name;
            private String[] tags;

            public int getId() {
                return id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setTags(String[] tags) {
                this.tags = tags;
            }

            @Override
            public String toString() {
                return "Object{" +
                        "id=" + getId() +
                        ", name='" + name + '\'' +
                        ", tags=" + Arrays.toString(tags) +
                        '}';
            }
        };
        boolean testTransients = false;
        String[] excludeFields = {"name"};
        String[] excludeTags = {"tags"};
        assertEquals(obj.hashCode(), HashCodeBuilder.reflectionHashCode(obj, testTransients, Object.class, excludeFields, excludeTags));
    }

    @Test
    public void testIsRegistered() {
        Object obj = new Object();
        assertFalse(HashCodeBuilder.isRegistered(obj));
        HashCodeBuilder.reflectionHashCode(obj);
        assertTrue(HashCodeBuilder.isRegistered(obj));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstance_IllegalArgumentException() {
        new HashCodeBuilder((int) 0, (int) 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstance_IllegalArgumentException_ObjectArray() {
        new HashCodeBuilder((Object[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstance_IllegalArgumentException_CollectionString() {
        new HashCodeBuilder((Collection<String>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testRegister_NullValue() {
        HashCodeBuilder.register(null);
    }

    @Test(expected = NullPointerException.class)
    public void testUnregister_NullValue() {
        HashCodeBuilder.unregister(null);
    }
}