package org.apache.commons.lang3.builder;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;

public class CompareToBuilderTest {
    @Test
    public void testAppend() {
        Object lhs = new Object();
        Object rhs = new Object();
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.append(lhs, rhs);
        int comparison = compareToBuilder.toComparison();
        // Add assertion here based on expected behavior
    }
}