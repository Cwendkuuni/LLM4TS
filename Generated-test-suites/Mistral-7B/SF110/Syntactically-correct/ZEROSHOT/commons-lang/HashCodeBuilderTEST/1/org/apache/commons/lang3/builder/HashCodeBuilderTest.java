import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        // Test default constructor
        HashCodeBuilder hcb = new HashCodeBuilder();
        int hashCode = hcb.toHashCode();
        assertTrue(hashCode > 0);

        // Test constructor with odd initial value and odd multiplier
        HashCodeBuilder hcb1 = new HashCodeBuilder(13, 31);
        int hashCode1 = hcb1.toHashCode();
        assertTrue(hashCode1 > 0);

        // Test append methods for primitive types
        HashCodeBuilder hcb2 = new HashCodeBuilder();
        hcb2.append(true);
        hcb2.append((byte) 1);
        hcb2.append((short) 1);
        hcb2.append((char) 'a');
        hcb2.append(1);
        hcb2.append(1L);
        int hashCode2 = hcb2.toHashCode();
        // Hash codes cannot be compared directly, so just check that they are not zero
        assertNotEquals(0, hashCode2);

        // Test append methods for arrays of primitive types
        byte[] bytes = {1, 2, 3};
        short[] shorts = {1, 2, 3};
        char[] chars = {'a', 'b', 'c'};
        int[] ints = {1, 2, 3};
        long[] longs = {1, 2, 3};
        HashCodeBuilder hcb3 = new HashCodeBuilder();
        hcb3.append(bytes);
        hcb3.append(shorts);
        hcb3.append(chars);
        hcb3.append(ints);
        hcb3.append(longs);
        int hashCode3 = hcb3.toHashCode();
        // Hash codes cannot be compared directly, so just check that they are not zero
        assertNotEquals(0, hashCode3);

        // Test append methods for objects
        Object obj = new Object();
        HashCodeBuilder hcb4 = new HashCodeBuilder();
        hcb4.append(obj);
        int hashCode4 = hcb4.toHashCode();
        // Hash codes cannot be compared directly, so just check that they are not zero
        assertNotEquals(0, hashCode4);

        // Test reflectionHashCode methods
        Object reflectObject = new Object() {};
        Class<?> reflectClass = reflectObject.getClass();
        int hashCodeReflection = HashCodeBuilder.reflectionHashCode(reflectObject);
        int hashCodeReflectionWithTestTransients = HashCodeBuilder.reflectionHashCode(reflectObject, true);
        int hashCodeReflectionWithExcludeFields = HashCodeBuilder.reflectionHashCode(reflectObject, null, new String[]{"field1", "field2"});
        // Hash codes cannot be compared directly, so just check that they are not zero
        assertNotEquals(0, hashCodeReflection);
        assertNotEquals(0, hashCodeReflectionWithTestTransients);
        assertNotEquals(0, hashCodeReflectionWithExcludeFields);

        // Test register and unregister methods
        Set<HashCodeBuilder.IDKey> registry = HashCodeBuilder.getRegistry();
        Object registeredObj = new Object();
        HashCodeBuilder.register(registeredObj);
        assertTrue(registry.contains(new HashCodeBuilder.IDKey(registeredObj)));
        HashCodeBuilder.unregister(registeredObj);
        assertFalse(registry.contains(new HashCodeBuilder.IDKey(registeredObj)));

        // Test isRegistered method
        Object nonRegisteredObj = new Object();
        assertFalse(HashCodeBuilder.isRegistered(nonRegisteredObj));
        Object registeredObj2 = new Object();
        HashCodeBuilder.register(registeredObj2);
        assertTrue(HashCodeBuilder.isRegistered(registeredObj2));

        // Test getRegistry method
        Set<HashCodeBuilder.IDKey> registry2 = HashCodeBuilder.getRegistry();
        assertTrue(registry2 != null && registry2.isEmpty());
        HashCodeBuilder.register(new Object());
        HashCodeBuilder.register(new Object());
        registry2 = HashCodeBuilder.getRegistry();
        assertTrue(registry2.size() == 2);
    }

    private static class IDKey {
        private final Object object;

        public IDKey(Object object) {
            this.object = object;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            IDKey that = (IDKey) o;
            return object.equals(that.object);
        }

        @Override
        public int hashCode() {
            return object.hashCode();
        }
    }
}