package org.apache.commons.lang3.builder;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CompareToBuilderTest {

    @Test
    public void testAppend() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();

        compareToBuilder.append(lhs, rhs);

        assertEquals(0, compareToBuilder.toComparison());
    }
}