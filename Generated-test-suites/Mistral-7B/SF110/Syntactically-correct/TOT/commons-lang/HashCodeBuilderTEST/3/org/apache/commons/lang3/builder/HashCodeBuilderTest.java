package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.HashCodeBuilder;
import org.apache.commons.lang3.builder.IDKey;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class HashCodeBuilderTest {

    private HashCodeBuilder builder;
    private Object object;

    @Parameters
    public static Collection<Object[]> data() {
        List<Object> list = Arrays.asList(
                new Object[]{"Simple object", new SimpleObject()},
                new Object[]{"Nested object", new NestedObject()}
        );
        return list.stream().map(o -> new Object[]{o[0], o[1]}).collect(Collectors.toList());
    }

    public HashCodeBuilderTest(String name, Object obj) {
        this.object = obj;
    }

    @Before
    public void setUp() {
        builder = new HashCodeBuilder();
    }

    @Test
    public void testSimpleHashCode() {
        builder.append(object);
        int initialHashCode = builder.toHashCode();
        builder.appendSuper(object.hashCode());
        int finalHashCode = builder.toHashCode();
        assertEquals(initialHashCode, finalHashCode);
    }

    @Test
    public void testReflectionsHashCode() {
        builder.appendSuper(object.hashCode());
        builder.append(object);
        int initialHashCode = builder.toHashCode();
        reflectionHashCodeTest(builder, object, true, null, null);
        int finalHashCode = builder.toHashCode();
        assertEquals(initialHashCode, finalHashCode);
    }

    @Test
    public void testReflectionsHashCodeWithExcludedFields() {
        String[] excludeFields = {"field1", "field2"};
        builder.appendSuper(object.hashCode());
        builder.append(object);
        int initialHashCode = builder.toHashCode();
        reflectionHashCodeTest(builder, object, true, null, excludeFields);
        int finalHashCode = builder.toHashCode();
        assertEquals(initialHashCode, finalHashCode);
    }

    @Test
    public void testReflectionsHashCodeWithTestTransients() {
        builder.appendSuper(object.hashCode());
        builder.append(object);
        int initialHashCode = builder.toHashCode();
        reflectionHashCodeTest(builder, object, false, null, null);
        int finalHashCode = builder.toHashCode();
        assertEquals(initialHashCode, finalHashCode);

        // Test with transient fields
        Class<?> clazz = object.getClass();
        Field transientField = null;
        try {
            transientField = FieldUtils.getField(clazz, "transientField", true);
        } catch (NoSuchFieldException e) {
            // Field not found, test will still pass
        }
        if (transientField != null) {
            reflectionHashCodeTest(builder, object, true, transientField, null);
            reflectionHashCodeTest(builder, object, false, transientField, null);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitialOddNumberIsEven() {
        new HashCodeBuilder(2, 37);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiplierOddNumberIsEven() {
        new HashCodeBuilder(17, 2);
    }

    @Test
    public void testRegistry() {
        builder.appendSuper(object.hashCode());
        final Object object1 = new Object();
        builder.append(object1);
        int hashCode1 = builder.toHashCode();

        builder.reset();
        builder.appendSuper(object.hashCode());
        final Object object2 = new Object();
        builder.append(object2);
        int hashCode2 = builder.toHashCode();

        assertEquals(hashCode1, hashCode2);

        // Object 1 should be registered
        HashCodeBuilder.register(object1);

        builder.reset();
        builder.appendSuper(object.hashCode());
        builder.append(object1);
        int hashCode3 = builder.toHashCode();

        // Object 1 should not be registered anymore
        HashCodeBuilder.unregister(object1);

        builder.reset();
        builder.appendSuper(object.hashCode());
        builder.append(object1);
        int hashCode4 = builder.toHashCode();

        assertEquals(hashCode1, hashCode3);
        assertEquals(hashCode3, hashCode4);

        // Object 2 should be registered
        HashCodeBuilder.register(object2);

        builder.reset();
        builder.appendSuper(object.hashCode());
        builder.append(object1);
        int hashCode5 = builder.toHashCode();

        assertEquals(hashCode3, hashCode5);

        builder.reset();
        builder.appendSuper(object.hashCode());
        builder.append(object2);
        int hashCode6 = builder.toHashCode();

        assertEquals(hashCode4, hashCode6);

        // Verify that the registry is empty
        HashCodeBuilder.unregister(object1);
        HashCodeBuilder.unregister(object2);
        Set<IDKey> registry = HashCodeBuilder.getRegistry();
        assertEquals(0, registry.size());
        HashCodeBuilder.REGISTRY.remove();
        registry = HashCodeBuilder.getRegistry();
        assertEquals(null, registry);
    }

    private static void reflectionHashCodeTest(HashCodeBuilder builder, Object object, boolean testTransients, Field transientField, String[] excludeFields) {
        reflectionAppend(builder, object.getClass(), builder, testTransients, excludeFields);
    }

    private static void reflectionAppend(HashCodeBuilder builder, Class<?> clazz, HashCodeBuilder builderToAppendTo, boolean testTransients, String[] excludeFields) {
        try {
            if (isRegistered(builderToAppendTo, object)) {
                return;
            }
            register(builderToAppendTo, object);

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!isShouldAppendField(field, excludeFields)) {
                    continue;
                }

                Object fieldValue = field.get(object);
                builderToAppendTo.append(fieldValue);
            }
        } catch (IllegalAccessException e) {
            throw new InternalError("Unexpected InternalError");
        } finally {
            unregister(builderToAppendTo, object);
        }
    }

    private static boolean isRegistered(HashCodeBuilder builderToAppendTo, Object object) {
        return HashCodeBuilder.isRegistered(object);
    }

    private static Object register(HashCodeBuilder builderToAppendTo, Object object) {
        register(object);
        return object;
    }

    private static void unregister(HashCodeBuilder builderToUnregisterFrom, Object object) {
        unregister(object);
    }

    private static void unregister(Object object) {
        HashCodeBuilder.unregister(object);
    }

    private static boolean isShouldAppendField(Field field, String[] excludeFields) {
        if (excludeFields != null && Arrays.asList(excludeFields).contains(field.getName())) {
            return false;
        }

        if (field.getName().indexOf(36) != -1) {
            return false;
        }

        return true;
    }
}