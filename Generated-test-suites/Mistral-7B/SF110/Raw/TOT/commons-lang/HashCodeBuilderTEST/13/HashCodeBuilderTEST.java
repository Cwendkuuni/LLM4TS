import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode_Object() {
        Object object = new Object();
        int actualHashCode = HashCodeBuilder.reflectionHashCode(object, false);
        int expectedHashCode = calculateHashCode(object);
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testReflectionHashCode_ObjectWithExcludeFields() {
        Object object = new ObjectWithExcludedFields();
        int actualHashCode = HashCodeBuilder.reflectionHashCode(object, false, Object.class, new String[]{"excludedField"});
        int expectedHashCode = calculateHashCode(object);
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testReflectionHashCode_ObjectWithSuperclass() {
        Object object = new ObjectWithSuperclass();
        int actualHashCode = HashCodeBuilder.reflectionHashCode(object, false, Object.class, null, Object[].class);
        int expectedHashCode = calculateHashCode(object);
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testReflectionHashCode_ObjectWithTransients() {
        Object object = new ObjectWithTransients();
        int actualHashCode = HashCodeBuilder.reflectionHashCode(object, true);
        int expectedHashCode = calculateHashCode(object);
        Assert.assertEquals(expectedHashCode, actualHashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCode_NullObject() {
        HashCodeBuilder.reflectionHashCode(null, 17, 37, false, null, new String[0]);
    }

    private int calculateHashCode(Object object) {
        try {
            Set<IDKey> registry = HashCodeBuilder.getRegistry();
            registry.clear();
            int hashCode = System.identityHashCode(object);
            FieldUtils.writeFieldValue(HashCodeBuilder.class, "REGISTRY", new HashSet<IDKey>(), true);
            int calculatedHashCode = System.identityHashCode(new HashCodeBuilder().append(object).build());
            FieldUtils.writeFieldValue(HashCodeBuilder.class, "REGISTRY", registry, true);
            return calculatedHashCode;
        } catch (IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static class Object {
        public int excludedField;
    }

    static class ObjectWithExcludedFields extends Object {
        public int includedField = 42;
    }

    static class ObjectWithSuperclass extends Object {
        public long superField = 1L;
    }
}