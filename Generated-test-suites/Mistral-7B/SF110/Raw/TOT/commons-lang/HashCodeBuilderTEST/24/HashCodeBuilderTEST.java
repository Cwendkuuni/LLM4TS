package org.apache.commons.lang3.builder;

import static org.junit.Assert.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder.IDKey;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCodeWithObject() {
        Object object = new Object();
        int hashCode = HashCodeBuilder.reflectionHashCode(object);
        System.out.println("Reflection Hash Code for Object: " + hashCode);

        Object anotherObject = new Object();
        int hashCodeAnotherObject = HashCodeBuilder.reflectionHashCode(anotherObject);
        System.out.println("Reflection Hash Code for another Object: " + hashCodeAnotherObject);

        assertEquals(hashCode, hashCodeAnotherObject);
    }

    @Test
    public void testReflectionHashCodeWithCollectionExcludeFields() {
        String[] excludeFields = {"excludedField"};
        List<String> list = Arrays.asList("field1", "field2", "excludedField", "field3");
        int hashCode = HashCodeBuilder.reflectionHashCode(list, excludeFields);
        System.out.println("Reflection Hash Code for List with excludeFields: " + hashCode);

        List<String> anotherList = Arrays.asList("field1", "field2", "field3", "field4");
        int hashCodeAnotherList = HashCodeBuilder.reflectionHashCode(anotherList, excludeFields);
        System.out.println("Reflection Hash Code for another List with excludeFields: " + hashCodeAnotherList);

        assertEquals(hashCode, hashCodeAnotherList);
    }

    @Test
    public void testReflectionHashCodeWithTransients() {
        class TestClass {
            private int transientField;
            private transient int transientField2;

            public TestClass() {
                this.transientField = 1;
            }
        }

        TestClass testClass = new TestClass();
        int hashCode = HashCodeBuilder.reflectionHashCode(testClass, true);
        System.out.println("Reflection Hash Code for TestClass with transients: " + hashCode);

        TestClass anotherTestClass = new TestClass();
        int hashCodeAnotherTestClass = HashCodeBuilder.reflectionHashCode(anotherTestClass, true);
        System.out.println("Reflection Hash Code for another TestClass with transients: " + hashCodeAnotherTestClass);

        assertEquals(hashCode, hashCodeAnotherTestClass);
    }

    @Test
    public void testReflectionHashCodeWithRegisteredObject() {
        Object registeredObject = new Object();
        HashCodeBuilder.register(registeredObject);
        int hashCode = HashCodeBuilder.reflectionHashCode(registeredObject);
        System.out.println("Reflection Hash Code for Registered Object: " + hashCode);

        HashCodeBuilder.unregister(registeredObject);
        int hashCodeUnregisteredObject = HashCodeBuilder.reflectionHashCode(registeredObject);
        System.out.println("Reflection Hash Code for Unregistered Object: " + hashCodeUnregisteredObject);

        assertNotEquals(hashCode, hashCodeUnregisteredObject);
    }

    @Test
    public void testReflectionHashCodeWithExcludedFields() throws NoSuchFieldException, IllegalAccessException {
        Object testObject = new Object() {
            private int excludedField = 1;
        };

        int hashCode = HashCodeBuilder.reflectionHashCode(testObject, "excludedField");
        System.out.println("Reflection Hash Code for Object with excludedField: " + hashCode);

        int excludedFieldValue = FieldUtils.readField(testObject, "excludedField", true);
        testObject.excludedField = 2;

        int hashCodeAnotherObject = HashCodeBuilder.reflectionHashCode(testObject, "excludedField");
        System.out.println("Reflection Hash Code for Object with another excludedField value: " + hashCodeAnotherObject);

        assertEquals(hashCode, hashCodeAnotherObject);
    }

    @Test
    public void testReflectionHashCodeWithSuperClass() {
        class SubClass extends Object {
            @Override
            public int hashCode() {
                return 123;
            }
        }

        int superHashCode = HashCodeBuilder.class.hashCode();
        int hashCode = HashCodeBuilder.reflectionHashCode(new SubClass(), superHashCode);
        System.out.println("Reflection Hash Code for SubClass with superHashCode: " + hashCode);

        int hashCodeAnotherSubClass = HashCodeBuilder.reflectionHashCode(new SubClass(), superHashCode);
        System.out.println("Reflection Hash Code for another SubClass with superHashCode: " + hashCodeAnotherSubClass);

        assertEquals(hashCode, hashCodeAnotherSubClass);
    }
}