package org.apache.commons.lang3.builder;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Assert;
import org.junit.Test;

public class CompareToBuilderTest {
    @Test
    public void testAppend() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        compareToBuilder.append(lhs, rhs);
        Assert.assertEquals(0, compareToBuilder.toComparison());
    }
}