package org.apache.commons.lang3.builder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HashCodeBuilderTest {

    private Object testObject;
    private boolean testTransients;

    public HashCodeBuilderTest(Object testObject, boolean testTransients) {
        this.testObject = testObject;
        this.testTransients = testTransients;
    }

    @Parameters
    public static Object[][] data() {
        return new Object[][] {
            {new Integer(1), false},
            {new Integer[10], false},
            {new String[]{"test", "string"}, true},
            {new String[]{"test", null}, true},
            {new MyClass(), false},
            {new MyClass(), true}
        };
    }

    @Test
    public void testReflectionHashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int expectedHashCode = reflectionHashCode(hashCodeBuilder, this.testObject, this.testTransients);
        int actualHashCode = hashCodeBuilder.toHashCode();
        assert (expectedHashCode == actualHashCode) : "Hash code not equal to the expected value";
    }

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        boolean value = true;
        hashCodeBuilder.append(value);
        int expectedHashCode = 37; // initial odd number
        int actualHashCode = hashCodeBuilder.toHashCode();
        assert (expectedHashCode == actualHashCode) : "Hash code not equal to the expected value";
    }

    @Test
    public void testAppendInt() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        int value = 10;
        hashCodeBuilder.append(value);
        int expectedHashCode = 526771225; // initial odd number * 37 + value
        int actualHashCode = hashCodeBuilder.toHashCode();
        assert (expectedHashCode == actualHashCode) : "Hash code not equal to the expected value";
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAppendNull() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        hashCodeBuilder.append(null);
    }

    private static int reflectionHashCode(HashCodeBuilder hashCodeBuilder, Object testObject, boolean testTransients) {
        return reflectionHashCode(17, 37, testObject, testTransients, null, new String[0]);
    }
}

class MyClass{}