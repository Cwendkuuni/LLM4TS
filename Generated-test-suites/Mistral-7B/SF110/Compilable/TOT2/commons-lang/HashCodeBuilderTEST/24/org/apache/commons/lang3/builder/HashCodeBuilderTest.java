package org.apache.commons.lang3.builder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class HashCodeBuilderTest {
    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        boolean value = true;
        builder.append(value);
        int expectedHashCode = 37; // the initial value of iTotal in HashCodeBuilder
        int actualHashCode = builder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}