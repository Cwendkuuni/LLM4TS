package org.apache.commons.lang3.builder;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CompareToBuilderTest {
    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }
}