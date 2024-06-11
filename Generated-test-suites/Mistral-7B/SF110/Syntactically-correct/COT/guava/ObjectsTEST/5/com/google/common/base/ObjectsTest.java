package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.ToStringHelper;

import java.util.Arrays;

@RunWith(JUnit4.class)
public class ObjectsTest {

    @Test
    public void testEqual() {
        assertTrue(Objects.equal(null, null));
        assertTrue(Objects.equal(new Object(), new Object()));
        assertFalse(Objects.equal(null, new Object()));
        assertFalse(Objects.equal("hello", 1));
        assertFalse(Objects.equal("hello", "world"));
    }

    @Test
    public void testHashCode() {
        Object[] array = new Object[0];
        int hashCode = Objects.hashCode(array);
        Objects[] anotherArray = new Objects[0];
        int anotherHashCode = Objects.hashCode(anotherArray);
        assertTrue(hashCode == anotherHashCode);

        Object[] arrayWithNull = new Object[] { null };
        int hashCodeWithNull = Objects.hashCode(arrayWithNull);
        Objects[] anotherArrayWithNull = new Objects[] { null };
        int anotherHashCodeWithNull = Objects.hashCode(anotherArrayWithNull);
        assertFalse(hashCodeWithNull == anotherHashCodeWithNull);

        Object[] arrayWithNullAndOtherObjects = new Object[] { null, new Object(), new Object() };
        int hashCodeWithNullAndOtherObjects = Objects.hashCode(arrayWithNullAndOtherObjects);
        Objects[] anotherArrayWithNullAndOtherObjects = new Objects[] { null, new Object(), new Object() };
        int anotherHashCodeWithNullAndOtherObjects = Objects.hashCode(anotherArrayWithNullAndOtherObjects);
        assertFalse(hashCodeWithNullAndOtherObjects == anotherHashCodeWithNullAndOtherObjects);
    }

    @Test
    public void testToStringHelper_omitNullValues_null() {
        ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .omitNullValues();
        assertEquals("ClassName{}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_omitNullValues_empty() {
        ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .omitNullValues();
        toStringHelper.add("name1", "value1");
        assertEquals("ClassName{name1=value1}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_omitNullValues_nullValue() {
        ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .omitNullValues();
        toStringHelper.add("name1", null);
        toStringHelper.add("name2", "value2");
        assertEquals("ClassName{name2=value2}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_omitNullValues_multipleNullValues() {
        ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .omitNullValues();
        toStringHelper.add("name1", null);
        toStringHelper.add("name2", null);
        toStringHelper.add("name3", "value3");
        assertEquals("ClassName{}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_omitNullValues_nullAndNonNullValues() {
        ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .omitNullValues();
        toStringHelper.add("name1", null);
        toStringHelper.add("name2", "value2");
        assertEquals("ClassName{name2=value2}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_multipleValues() {
        ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName");
        toStringHelper.add("name1", "value1");
       toStringHelper.add("name2", "value2");
        assertEquals("ClassName{name1=value1, name2=value2}", toStringHelper.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelper_nullClassName() {
        new Objects.ToStringHelper(null);
    }

    @Test
    public void testToStringHelper_addNullValue() {
        ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName");
        toStringHelper.add("name1", null);
        assertEquals("ClassName{name1=null}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_addNullClassName() {
        ToStringHelper toStringHelper = new Objects.ToStringHelper(null);
        toStringHelper.add("name1", "value1");
        assertEquals("ClassName{name1=value1}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_addNullName() {
        ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName");
        toStringHelper.add(null, "value1");
        assertEquals("ClassName{value1=}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_addNullNameAndValue() {
        ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName");
        toStringHelper.add(null, null);
        assertEquals("ClassName{}", toStringHelper.toString());
    }

    @Test
    public void testToStringHelper_addEmptyArray() {
        ToStringHelper toStringHelper = new Objects.ToStringHelper("ClassName")
                .add("name1", "");
        assertEquals("ClassName{name1=}", toStringHelper.toString());
    }

}