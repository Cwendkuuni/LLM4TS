package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    private static Set<IDKey> previousRegistry;

    @BeforeClass
    public static void setup() {
        previousRegistry = HashCodeBuilder.getRegistry();
        HashCodeBuilder.REGISTRY.set(new HashSet<IDKey>());
    }

    @Test
    public void testReflectionHashCodeWithObject() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false);
        assertNotEquals(0, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithEmptyArray() {
        int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, new Object[0], false, Object[].class, new String[0]);
        assertEquals(17, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithNull() {
        Object object = null;
        try {
            int hashCode = HashCodeBuilder.reflectionHashCode(17, 37, object, false, Object.class, new String[0]);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testReflectionHashCodeWithBoolean() {
        boolean booleanValue = true;
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(booleanValue);
        int hashCode = hashCodeBuilder.toHashCode();
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(booleanValue, false);
        assertEquals(hashCode, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeWithBooleanArray() {
        boolean[] booleanArray = {true, false};
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(booleanArray);
        int hashCode = hashCodeBuilder.toHashCode();
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(booleanArray, false, boolean[].class, new String[0]);
        assertEquals(hashCode, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeWithRegisteredObject() {
        Object object = new Object();
        HashCodeBuilder.register(object);
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false);
        HashCodeBuilder.unregister(object);
        assertFalse(HashCodeBuilder.isRegistered(object));
        int previousHashCode = HashCodeBuilder.reflectionHashCode(object, false);
        assertNotEquals(hashCode, previousHashCode);
    }

    @Test
    public void testReflectionHashCodeWithSuperClass() {
        Object superClassObject = new SuperClass();
        Object subClassObject = new SubClass();
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.appendSuper(superClassObject.hashCode());
        hashCodeBuilder.append(subClassObject);
        int hashCode = hashCodeBuilder.toHashCode();
        int reflectionHashCode = HashCodeBuilder.reflectionHashCode(subClassObject, false, SubClass.class, new String[]{"superHashCode"}, new Class[]{SuperClass.class});
        assertEquals(hashCode, reflectionHashCode);
    }

    @Test
    public void testReflectionHashCodeWithExcludedFields() {
        Object object = new ObjectWithExcludedFields();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, true, ObjectWithExcludedFields.class, new String[]{"excludedField"}, new String[0]);
        assertNotEquals(0, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithTransients() {
        Object object = new ObjectWithTransients();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, true, ObjectWithTransients.class, new String[0], ObjectWithTransients.class);
        assertNotEquals(0, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithExcludedStaticFields() {
        Object object = new ObjectWithExcludedStaticFields();
        int hashCode = HashCodeBuilder.reflectionHashCode(object, false, ObjectWithExcludedStaticFields.class, new String[0]);
        assertNotEquals(0, hashCode);
    }

    @Test(expected = InternalError.class)
    public void testReflectionHashCodeWithIllegalAccessException() {
        Object object = new ObjectWithIllegalAccess();
        HashCodeBuilder.reflectionHashCode(object, false, ObjectWithIllegalAccess.class, new String[0]);
    }

    @Test
    public void testGetRegistryAndIsRegistered() {
        Object object = new Object();
        HashCodeBuilder.register(object);
        assertTrue(HashCodeBuilder.isRegistered(object));
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertTrue(registry.contains(new IDKey(object)));
        HashCodeBuilder.unregister(object);
        assertFalse(HashCodeBuilder.isRegistered(object));
        registry = HashCodeBuilder.getRegistry();
        assertFalse(registry.contains(new IDKey(object)));
    }

    @AfterClass
    public static void teardown() {
        HashCodeBuilder.REGIRegistry.set(previousRegistry);
    }

    private static class IDKey {
        private final Object value;

        public IDKey(Object value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof IDKey && obj.hashCode() == this.hashCode() && this.value.equals(((IDKey)obj).value);
        }
    }

    private static class SuperClass {
    }

    private static class SubClass extends SuperClass {
    }

    private static class ObjectWithExcludedFields {
        private int excludedField;

        @Override
        public String toString() {
            return "ObjectWithExcludedFields{" +
                    "excludedField=" + excludedField +
                    '}';
        }
    }

    private static class ObjectWithTransients {
        private transient int transientField;

        @Override
        public String toString() {
            return "ObjectWithTransients{" +
                    "transientField=" + transientField +
                    '}';
        }
    }

    private static class ObjectWithIllegalAccess {
        private static int illegalAccessField;

        @Override
        public String toString() {
            return "ObjectWithIllegalAccess{" +
                    "illegalAccessField=" + illegalAccessField +
                    '}';
        }

        private static void setIllegalAccessField(ObjectWithIllegalAccess objectWithIllegalAccess, int value) throws IllegalAccessException {
            Field field = ObjectWithIllegalAccess.class.getDeclaredField("illegalAccessField");
            AccessibleObject.setAccessible(field, true);
            field.set(objectWithIllegalAccess, value);
        }

        @Test
        public void testIllegalAccess() throws NoSuchFieldException, IllegalAccessException {
            ObjectWithIllegalAccess objectWithIllegalAccess = new ObjectWithIllegalAccess();
            int initialHashCode = objectWithIllegalAccess.hashCode();
            setIllegalAccessField(objectWithIllegalAccess, 42);
            int finalHashCode = objectWithIllegalAccess.hashCode();
            assertNotEquals(initialHashCode, finalHashCode);
        }
    }
}