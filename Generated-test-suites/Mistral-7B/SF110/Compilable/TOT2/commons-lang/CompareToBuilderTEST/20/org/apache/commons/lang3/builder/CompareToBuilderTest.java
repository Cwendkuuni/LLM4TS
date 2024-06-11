package org.apache.commons.lang3.builder;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;

public class CompareToBuilderTest {

    @Test
    public void testAppend() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        // Check the comparison result
        // ...
    }
}