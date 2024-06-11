package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import com.google.common.base.Objects;

public class ObjectsTest
{
    @Test
    public void testEqualNullNull()
    {
        assertTrue(Objects.equal(null, null));
    }

    @Test
    public void testEqualNullObject()
    {
        assertFalse(Objects.equal(null, new Object()));
    }

    @Test
    public void testEqualNullString()
    {
        assertFalse(Objects.equal(null, ""));
    }

    @Test
    public void testEqualNullList()
    {
        List<Object> list = new ArrayList<>();
        assertFalse(Objects.equal(null, list.toArray()));
    }

    @Test
    public void testEqualSameObjects()
    {
        Object a = new Object();
        Object b = a;
        assertTrue(Objects.equal(a, b));
    }

    @Test
    public void testEqualSameStrings()
    {
        String a = "hello";
        String b = "hello";
        assertTrue(Objects.equal(a, b));
    }

    @Test
    public void testEqualSameLists()
    {
        List<Integer> listA = Arrays.asList(1, 2, 3);
        List<Integer> listB = Arrays.asList(1, 2, 3);
        assertTrue(Objects.equal(listA, listB));
    }

    @Test(expected = NullPointerException.class)
    public void testEqualNullObjectWithNull()
    {
        Object a = null;
        Object b = new Object();
        Objects.equal(a, b);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualNullStringWithNull()
    {
        Object a = null;
        String b = "";
        Objects.equal(a, b);
    }

    @Test(expected = NullPointerException.class)
    public void testEqualNullListWithNull()
    {
        Object a = null;
        Object[] b = new Object[0];
        Objects.equal(a, b);
    }

    @Test
    public void testHashCodeNull()
    {
        int result = Objects.hashCode(new Object[0]);
        int expected = 0;
        assertEquals(expected, result);
    }

    @Test
    public void testHashCodeObjects()
    {
        int result = Objects.hashCode(new Object(), new Object());
        int expected = 0; // different objects will have different hash codes, so an arbitrary value is used here for the test
        assertNotEquals(expected, result);
    }

    @Test
    public void testToStringHelperEmpty()
    {
        Objects.ToStringHelper helper = Objects.newToStringHelper(Objects.class);
        String result = helper.toString();
        String expected = "Objects{}";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringHelperSingleValue()
    {
        Objects.ToStringHelper helper = Objects.newToStringHelper(Objects.class).add("name", new Object());
        String result = helper.toString();
        String expected = "Objects{name=}";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringHelperMultipleValues()
    {
        Objects.ToStringHelper helper = Objects.newToStringHelper(Objects.class)
                .add("name1", new Object())
                .add("name2", new Object())
                .addValue(new Object());
        String result = helper.toString();
        String expected = "Objects{name1=, name2=}";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringHelperNullValuesOmit()
    {
        Objects.ToStringHelper helper = Objects.newToStringHelper(Objects.class)
                .add("name1", null)
                .add("name2", new Object())
                .addValue(null)
                .omitNullValues();
        String result = helper.toString();
        String expected = "Objects{name2=}";
        assertEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelperNullName()
    {
        Objects.ToStringHelper helper = Objects.newToStringHelper(Objects.class)
                .add(null, new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringHelperNullValue()
    {
        Objects.ToStringHelper helper = Objects.newToStringHelper(Objects.class)
                .add("name", null);
    }
}