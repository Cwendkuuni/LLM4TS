package org.apache.commons.lang3.builder;
import org.junit.Test;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean value = true;
        builder.append(value);
        int expectedHashCode = 37; // based on the implementation of append(boolean value)
        int actualHashCode = builder.toHashCode();
        assert(expectedHashCode == actualHashCode);
    }
}