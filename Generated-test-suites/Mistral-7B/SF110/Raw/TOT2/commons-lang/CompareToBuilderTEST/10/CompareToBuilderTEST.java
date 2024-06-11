package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.reflect.ReflectionToStringBuilder;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class CompareToBuilderTest {

    @Test
    public void testCompareToBuilder_DefaultConstructor() {
        CompareToBuilder builder = new CompareToBuilder();
        assertEquals(0, builder.build());
    }

    @Test
    public void testCompareToBuilder_StaticMethods_EqualObjects() {
        Object obj1 = new Object();
        Object obj2 = obj1;
        assertEquals(0, CompareToBuilder.reflectionCompare(obj1, obj2));
    }

    @Test
    public void testCompareToBuilder_StaticMethods_NullObjects() {
        assertNull(CompareToBuilder.reflectionCompare(null, null));
        assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(null, new Object()));
        assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(new Object(), null));
    }

    @Test
    public void testCompareToBuilder_StaticMethods_DifferentClasses() {
        Object obj1 = new Object();
        String strObj = "Test";
        assertNotEquals(0, CompareToBuilder.reflectionCompare(obj1, strObj));
    }

    @Test
    public void testCompareToBuilder_StaticMethods_ExcludeFields() {
        class TestClass {
            int field1;
            String field2;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                TestClass testClass = (TestClass) o;

                return field1 == testClass.field1 && field2.equals(testClass.field2);
            }

            @Override
            public String toString() {
                return ReflectionToStringBuilder.toString(this);
            }
        }

        TestClass obj1 = new TestClass();
        TestClass obj2 = new TestClass();
        obj1.field1 = 1;
        obj2.field1 = 2;

        assertEquals(1, CompareToBuilder.reflectionCompare(obj1, obj2, ReflectionToStringBuilder.class));
    }

    @Test
    public void testCompareToBuilder_InstanceMethods_AppendPrimitiveTypes() {
        CompareToBuilder builder = new CompareToBuilder();
        builder.append(1, 2);
        assertEquals(-1, builder.build());

        builder = new CompareToBuilder();
        builder.append(1, 1);
        assertEquals(0, builder.build());

        builder = new CompareToBuilder();
        builder.append(1, -1);
        assertEquals(1, builder.build());

        builder = new CompareToBuilder();
        builder.append(2, 1);
        assertEquals(1, builder.build());
    }

    @Test
    public void testCompareToBuilder_InstanceMethods_AppendObjectTypes() {
        CompareToBuilder builder = new CompareToBuilder();

        Object obj1 = new Object();
        Object obj2 = new Object();
        builder.append(obj1, obj2);
        assertEquals(-1, builder.build());

        builder = new CompareToBuilder();
        builder.append(obj1, obj1);
        assertEquals(0, builder.build());

        builder = new CompareToBuilder();
        builder.append(obj2, obj1);
        assertEquals(1, builder.build());
    }

    @Test
    public void testCompareToBuilder_InstanceMethods_AppendArrays() {
        CompareToBuilder builder = new CompareToBuilder();

        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};
        builder.append(arr1, arr2);
        assertEquals(0, builder.build());

        int[] arr3 = {1, 2, 3};
        int[] arr4 = {1, 2, 4};
        builder = new CompareToBuilder();
        builder.append(arr3, arr4);
        assertEquals(-1, builder.build());

        int[] arr5 = null;
        int[] arr6 = {1, 2, 3};
        builder = new CompareToBuilder();
        builder.append(arr5, arr6);
        assertEquals(-1, builder.build());

        int[] arr7 = {1, 2, 3};
        int[] arr8 = null;
        builder = new CompareToBuilder();
        builder.append(arr7, arr8);
        assertEquals(1, builder.build());
    }

    @Test
    public void testCompareToBuilder_InstanceMethods_AppendDifferentDataTypes() {
        CompareToBuilder builder = new CompareToBuilder();

        int intValue = 1;
        String strValue = "Test";
        builder.append(intValue, strValue);
        assertEquals(-1, builder.build());
    }

    @Test
    public void testCompareToBuilder_InstanceMethods_AppendTransientFields() throws NoSuchFieldException, IllegalAccessException {
        class TestClass {
            private int field1;
            private transient int field2;

            public TestClass(int field1, int field2) {
                this.field1 = field1;
                this.field2 = field2;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                TestClass testClass = (TestClass) o;

                return field1 == testClass.field1;
            }

            @Override
            public String toString() {
                return ReflectionToStringBuilder.toString(this);
            }
        }

        TestClass obj1 = new TestClass(1, 1);
        TestClass obj2 = new TestClass(1, 2);

        CompareToBuilder builder = new CompareToBuilder();
        builder.append(obj1, obj2);
        assertEquals(-1, builder.build());
    }

    @Test
    public void testCompareToBuilder_InstanceMethods_AppendSuper() {
        class Parent {
            int field = 1;
        }

        class Child extends Parent {
        }

        CompareToBuilder builder = new CompareToBuilder();
        builder.appendSuper(1);
        Parent parent = new Parent();
        Child child = new Child();
        builder.append(parent, child);
        assertEquals(-1, builder.build());
    }
}