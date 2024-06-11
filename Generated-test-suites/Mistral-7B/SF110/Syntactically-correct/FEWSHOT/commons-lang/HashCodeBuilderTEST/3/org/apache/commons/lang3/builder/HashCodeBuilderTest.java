package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(13, 17);

        assertEquals(17, hcb.toHashCode());
        assertEquals(13, hcbWithInitialValue.toHashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        hcb.append(false);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb.append(true);
        hcb.append(false);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 1);
        hcb.append((byte) 2);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb.append((byte) 1);
        hcb.append((byte) 2);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        hcb.append('b');

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb.append('a');
        hcb.append('b');

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1);
        hcb.append(2);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb.append(1);
        hcb.append(2);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        hcb.append(2L);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb.append(1L);
        hcb.append(2L);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendObject() {
        String str1 = "test";
        String str2 = "test";

        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(str1);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb.append(str2);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendObjectArray() {
        String[] strArray1 = {"test", "test"};
        String[] strArray2 = {"test", "test"};

        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(strArray1);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb.append(strArray2);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendBooleanArray() {
        boolean[] boolArray1 = {true, false};
        boolean[] boolArray2 = {true, false};

        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(boolArray1);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb.append(boolArray2);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testReflectionHashCode() {
        Object obj = new Object() {
            int i = 1;
            String s = "test";
        };

        int hashCode = HashCodeBuilder.reflectionHashCode(obj, true);

        assertEquals(61593003, hashCode);
    }
}