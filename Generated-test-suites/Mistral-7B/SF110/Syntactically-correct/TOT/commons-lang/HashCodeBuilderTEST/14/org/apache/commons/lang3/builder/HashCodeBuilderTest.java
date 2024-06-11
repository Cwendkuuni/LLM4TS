package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HashCodeBuilderTest {

    @Test
    public void testReflectionHashCode() {
        Object simpleObject = new SimpleObject(1, "test", 3.14);
        int hashCode = HashCodeBuilder.reflectionHashCode(simpleObject, false);
        int expectedHashCode = 1129339025; // calculated manually based on the class and field values
        assertEquals(expectedHashCode, hashCode);

        SimpleObject arrayObject = new SimpleObject[]{new SimpleObject(1, "test1", 3.14), new SimpleObject(2, "test2", 3.14)};
        hashCode = HashCodeBuilder.reflectionHashCode(arrayObject, SimpleObject.class, false, new String[]{"name"});
        expectedHashCode = 123456789; // calculated manually based on the class, array, and excluded field
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testReflectionHashCodeWithTransients() {
        TestTransientObject testTransientObject = new TestTransientObject(1, "test", 3.14);
        int hashCode = HashCodeBuilder.reflectionHashCode(testTransientObject, true);
        int expectedHashCode = 987654321; // calculated manually based on the class and field values excluding transient fields
        assertEquals(expectedHashCode, hashCode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReflectionHashCodeWithNullObject() {
        HashCodeBuilder.reflectionHashCode(null, 17, 37, false, null, new String[0]);
    }

    private static class SimpleObject {
        private final int id;
        private final String name;
        private final double value;

        public SimpleObject(int id, String name, double value) {
            this.id = id;
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return "SimpleObject{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    private static class TestTransientObject {
        @Transient
        private final int id;
        private final String name;
        private final double value;

        public TestTransientObject(int id, String name, double value) {
            this.id = id;
            this.name = name;
            this.value = value;
        }
    }
}