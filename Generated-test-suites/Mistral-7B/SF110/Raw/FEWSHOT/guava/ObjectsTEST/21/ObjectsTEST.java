package com.google.common.base;

import static org.junit.Assert.*;

import com.google.common.annotations.GoogleInternal;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.junit.Test;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class ObjectsTest {

    @Test
    public void testEqual() {
        assertFalse(Objects.equal(null, "foo"));
        assertFalse(Objects.equal("foo", null));
        assertFalse(Objects.equal("foo", "bar"));
        assertTrue(Objects.equal("foo", "foo"));
        assertTrue(Objects.equal(1, 1));
        assertTrue(Objects.equal(1, (Object) 1));
        assertTrue(Objects.equal(new Object(), new Object()));
    }

    @Test
    public void testHashCode() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        Object[] array1 = new Object[0];
        Object[] array2 = new Object[0];

        int hashCode1 = Objects.hashCode(obj1);
        int hashCode2 = Objects.hashCode(obj2);
        int hashCode3 = Objects.hashCode(array1);
        int hashCode4 = Objects.hashCode(array2);

        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode2, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
        assertNotEquals(hashCode2, hashCode4);
        assertNotEquals(hashCode3, hashCode4);

        Object[] arrayWithElement = new Object[1];
        arrayWithElement[0] = new Object();
        int hashCode5 = Objects.hashCode(arrayWithElement);

        Object[] arrayWithTwoElements = new Object[2];
        arrayWithTwoElements[0] = new Object();
        arrayWithTwoElements[1] = new Object();
        int hashCode6 = Objects.hashCode(arrayWithTwoElements);

        assertEquals(hashCode1, Objects.hashCode(obj1));
        assertEquals(hashCode2, Objects.hashCode(obj2));
        assertEquals(hashCode3, Objects.hashCode(array1));
        assertEquals(hashCode4, Objects.hashCode(array2));
        assertEquals(hashCode5, Objects.hashCode(arrayWithElement));
        assertEquals(hashCode6, Objects.hashCode(arrayWithTwoElements));
    }

    @Test
    public void testToStringHelper_omitNullValues() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
                .omitNullValues()
                .add("name1", "value1")
                .add("name2", null)
                .add("name3", "value3");

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name3=value3" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_addValue() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.addValue(1);
        helper.addValue(true);
        helper.addValue(3.14);
        helper.addValue('a');

        String toString = helper.toString();
        String expected = "ClassName{" +
                "=1," +
                "=true," +
                "=3.14," +
                "=a" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "value1");
        helper.add("name2", "value2");
        helper.add("name3", null);

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name2=value2" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_nullName() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add(null, "value1");

        String toString = helper.toString();
        String expected = "ClassName{}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_emptyArray() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "value1")
                .add("name2", null)
                .add("name3", new Object[0]);

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=value1" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_multipleValues() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "value1")
                .add("name2", "value2")
                .add("name3", "value3")
                .add("name4", null)
                .add("name5", new Object[0])
                .add("name6", new Object[0])
                .add("name7", new Object[0])
                .add("name8", new Object[0]);

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name2=value2," +
                "name3=value3" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_nullValue() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "value1")
                .add("name2", null)
                .add("name3", "value3");

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name3=value3" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_emptyValue() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "value1")
                .add("name2", new Object[0])
                .add("name3", "value3");

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name3=value3" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_nullValues() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
                .add("name1", "value1")
                .add("name2", null)
                .add("name3", null)
                .add("name4", null)
                .add("name5", null);

        String toString = helper.toString();
        String expected = "ClassName{}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_multipleNullValues() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
                .add("name1", null)
                .add("name2", null)
                .add("name3", null)
                .add("name4", null)
                .add("name5", null);

        String toString = helper.toString();
        String expected = "ClassName{}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_emptyString() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "")
                .add("name2", "value2")
                .add("name3", "value3");

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=," +
                "name2=value2," +
                "name3=value3" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_emptyString_multipleValues() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "")
                .add("name2", "value2")
                .add("name3", "value3")
                .add("name4", "")
                .add("name5", "")
                .add("name6", "value6")
                .add("name7", "value7");

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=," +
                "name2=value2," +
                "name3=value3," +
                "name4=," +
                "name5=," +
                "name6=value6," +
                "name7=value7" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_emptyString_multipleNullValues() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName")
                .add("name1", null)
                .add("name2", null)
                .add("name3", null)
                .add("name4", "")
                .add("name5", "")
                .add("name6", null)
                .add("name7", null);

        String toString = helper.toString();
        String expected = "ClassName{}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_emptyArray() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "value1")
                .add("name2", new Object[0])
                .add("name3", "value3");

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name3=value3" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_emptyArray_multipleValues() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "value1")
                .add("name2", new Object[0])
                .add("name3", "value3")
                .add("name4", new Object[0])
                .add("name5", new Object[0])
                .add("name6", new Object[0])
                .add("name7", new Object[0]);

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name3=value3," +
                "name4=[], " +
                "name5=[]," +
                "name6=[]," +
                "name7=[]" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_anonymousClass() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "value1")
                .add("name2", new Object() {});

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name2=java.lang.Object@5852e6a2" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_anonymousClass_multipleValues() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "value1")
                .add("name2", new Object() {})
                .add("name3", "value3")
                .add("name4", new Object() {})
                .add("name5", "value5")
                .add("name6", new Object() {});

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name2=java.lang.Object@6bba725f," +
                "name3=value3," +
                "name4=java.lang.Object@67e641b1," +
                "name5=value5," +
                "name6=java.lang.Object@2e1b3f4" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_innerClass() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "value1")
                .add("name2", new InnerClass());

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name2=InnerClass@67f5a75a" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_add_innerClass_multipleValues() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.add("name1", "value1")
                .add("name2", new InnerClass())
                .add("name3", "value3")
                .add("name4", new InnerClass())
                .add("name5", "value5")
                .add("name6", new InnerClass());

        String toString = helper.toString();
        String expected = "ClassName{" +
                "name1=value1," +
                "name2=InnerClass@52942b1d," +
                "name3=value3," +
                "name4=InnerClass@5b9561c5," +
                "name5=value5," +
                "name6=InnerClass@6d1c2978" +
                "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_addValue_GoogleInternal() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.addValue(new GoogleInternal());

        String toString = helper.toString();
        String expected = "ClassName{@type=class com.google.common.base.GoogleInternal}";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringHelper_addValue_GoogleInternal_multipleValues() {
        Objects.ToStringHelper helper = Objects.newToStringHelper("ClassName");

        helper.addValue(new GoogleInternal())
                .addValue(new GoogleInternal())
                .addValue(new GoogleInternal());

        String toString = helper.toString();
        String expected = "ClassName{" +
                "@type=class com.google.common.base.GoogleInternal," +
                "@type=class com.google.common.base.GoogleInternal," +
                "@type=class com.google.common.base.GoogleInternal" +
                "}";
        assertEquals(expected, toString);
    }
}