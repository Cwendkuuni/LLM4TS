import org.apache.commons.lang3.*;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);

        hashCodeBuilder = new HashCodeBuilder(17, 37);
        hashCode = hashCodeBuilder.toHashCode();
        Assert.assertEquals(17, hashCode);
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(true);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendBooleanArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        boolean[] booleanArray = new boolean[3];
        booleanArray[0] = true;
        booleanArray[1] = false;
        booleanArray[2] = true;
        hashCodeBuilder.append(booleanArray);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((byte) 1);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendByteArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        byte[] byteArray = new byte[3];
        byteArray[0] = 1;
        byteArray[1] = 2;
        byteArray[2] = 3;
        hashCodeBuilder.append(byteArray);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append('a');
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendCharArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        char[] charArray = new char[3];
        charArray[0] = 'a';
        charArray[1] = 'b';
        charArray[2] = 'c';
        hashCodeBuilder.append(charArray);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        double doubleValue = 3.14;
        hashCodeBuilder.append(doubleValue);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendDoubleArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        double[] doubleArray = new double[3];
        doubleArray[0] = 1.1;
        doubleArray[1] = 2.2;
        doubleArray[2] = 3.3;
        hashCodeBuilder.append(doubleArray);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        float floatValue = 3.14f;
        hashCodeBuilder.append(floatValue);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendFloatArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        float[] floatArray = new float[3];
        floatArray[0] = 1.1f;
        floatArray[1] = 2.2f;
        floatArray[2] = 3.3f;
        hashCodeBuilder.append(floatArray);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendIntArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int[] intArray = new int[3];
        intArray[0] = 1;
        intArray[1] = 2;
        intArray[2] = 3;
        hashCodeBuilder.append(intArray);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(1L);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendLongArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        long[] longArray = new long[3];
        longArray[0] = 1;
        longArray[1] = 2;
        longArray[2] = 3;
        hashCodeBuilder.append(longArray);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        String object = "test";
        hashCodeBuilder.append(object);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendObjectArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        String[] stringArray = new String[3];
        stringArray[0] = "test1";
        stringArray[1] = "test2";
        stringArray[2] = "test3";
        hashCodeBuilder.append(stringArray);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append((short) 1);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendShortArray() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        short[] shortArray = new short[3];
        shortArray[0] = 1;
        shortArray[1] = 2;
        shortArray[2] = 3;
        hashCodeBuilder.append(shortArray);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testAppendSuperHashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int superHashCode = 1234;
        hashCodeBuilder.appendSuper(superHashCode);
        int hashCode = hashCodeBuilder.toHashCode();
        Assert.assertEquals(1234, hashCode);
    }

    @Test
    public void testReflectionHashCode() {
        Object testObject = new TestClass();
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject);
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testReflectionHashCodeExcludeFields() {
        Object testObject = new TestClass();
        String[] excludeFields = new String[]{"field1", "field2"};
        int hashCode = HashCodeBuilder.reflectionHashCode(testObject, excludeFields);
        Assert.assertNotEquals(-1, hashCode);
    }

    @Test
    public void testIsRegistered() {
        Object testObject = new TestClass();
        Assert.assertFalse(HashCodeBuilder.isRegistered(testObject));

        HashCodeBuilder.reflectionAppend(testObject, TestClass.class, new HashCodeBuilder(), false, null);
        Assert.assertTrue(HashCodeBuilder.isRegistered(testObject));

        HashCodeBuilder.unregister(testObject);
        Assert.assertFalse(HashCodeBuilder.isRegistered(testObject));
    }

    @Test
    public void testGetRegistry() {
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        Assert.assertNull(registry);

        Object testObject1 = new TestClass();
        Object testObject2 = new TestClass();
        HashCodeBuilder.reflectionAppend(testObject1, TestClass.class, new HashCodeBuilder(), false, null);
        HashCodeBuilder.reflectionAppend(testObject2, TestClass.class, new HashCodeBuilder(), false, null);

        registry = HashCodeBuilder.getRegistry();
        Assert.assertNotNull(registry);
        Assert.assertEquals(2, registry.size());
    }

    private static class TestClass {
        private int field1 = 1;
        private String field2 = "test";
    }
}