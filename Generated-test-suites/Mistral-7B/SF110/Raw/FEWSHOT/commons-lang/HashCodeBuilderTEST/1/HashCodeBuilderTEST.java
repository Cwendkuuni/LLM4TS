package org.apache.commons.lang3.builder;

import org.apache.commons.lang3.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testHashCodeBuilder() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        assertEquals(37, hcb.hashCode());

        HashCodeBuilder hcbWithInitialValue = new HashCodeBuilder(53, 31);
        assertEquals(53, hcbWithInitialValue.hashCode());
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(true);
        hcb.append(false);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb = new HashCodeBuilder();
        hcb.append(false);
        hcb.append(true);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendByte() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((byte) 1);
        hcb.append((byte) -128);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb = new HashCodeBuilder();
        hcb.append((byte) -128);
        hcb.append((byte) 1);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendChar() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append('a');
        hcb.append('ö');

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb = new HashCodeBuilder();
        hcb.append('ö');
        hcb.append('a');

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendDouble() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.5);
        hcb.append(Double.longBitsToDouble(0x7ff0000000000001L));

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb = new HashCodeBuilder();
        hcb.append(Double.longBitsToDouble(0x7ff0000000000001L));
        hcb.append(1.5);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendFloat() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1.5f);
        hcb.append(Float.intBitsToFloat(0x7f800001));

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb = new HashCodeBuilder();
        hcb.append(Float.intBitsToFloat(0x7f800001));
        hcb.append(1.5f);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1);
        hcb.append(-128);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb = new HashCodeBuilder();
        hcb.append(-128);
        hcb.append(1);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendLong() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(1L);
        hcb.append(-128L);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb = new HashCodeBuilder();
        hcb.append(-128L);
        hcb.append(1L);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendObject() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append("Hello World!");
        hcb.append(new StringBuilder("Hello World!"));

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb = new HashCodeBuilder();
        hcb.append(new StringBuilder("Hello World!"));
        hcb.append("Hello World!");

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendShort() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append((short) 1);
        hcb.append((short) -128);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb = new HashCodeBuilder();
        hcb.append((short) -128);
        hcb.append((short) 1);

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testAppendSuper() {
        class TestClass {
            int i = 1;
            int j = 2;
            int k = 3;
        }

        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.appendSuper(new TestClass().hashCode());
        hcb.append(1);

        HashSet<Integer> set = new HashSet<>();
        set.add(hcb.toHashCode());

        hcb = new HashCodeBuilder();
        hcb.append(1);
        hcb.appendSuper(new TestClass().hashCode());

        assertFalse(set.contains(hcb.toHashCode()));
    }

    @Test
    public void testReflectionHashCode() {
        TestClass testClass = new TestClass();

        int hashCode = HashCodeBuilder.reflectionHashCode(testClass, true);
        assertEquals(1351420783, hashCode);

        hashCode = HashCodeBuilder.reflectionHashCode(testClass, false, String[].class, "i", "j");
        assertEquals(1729609747, hashCode);
    }
}

class TestClass {
    int i = 1;
    int j = 2;
    int k = 3;
}