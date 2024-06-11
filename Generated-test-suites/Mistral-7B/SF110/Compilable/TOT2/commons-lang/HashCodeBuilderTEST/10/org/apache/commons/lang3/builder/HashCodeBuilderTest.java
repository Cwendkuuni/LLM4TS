package org.apache.commons.lang3.builder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class HashCodeBuilderTest {

    @Test
    public void testAppendBoolean() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(true);
        int expectedHashCode = 32 * 37 + 1;
        int actualHashCode = builder.toHashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
}