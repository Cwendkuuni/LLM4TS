package org.apache.commons.lang3.builder;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.CompareToBuilderTest;
import org.junit.Test;
import org.junit.Assert;

public class CompareToBuilderTest {

    @Test
    public void testReflectionCompare_sameClass() {
        Object lhs = new Object();
        Object rhs = new Object();
        Assert.assertEquals(0, CompareToBuilder.reflectionCompare(lhs, rhs));
    }

    @Test
    public void testReflectionCompare_differentClasses_compatible() {
        int lhsInt = 1;
        long rhsLong = 1L;
        Assert.assertEquals(0, CompareToBuilder.reflectionCompare(lhsInt, rhsLong, false, null, new String[0]));
    }

    @Test(expected = ClassCastException.class)
    public void testReflectionCompare_differentClasses_notCompatible() {
        int lhsInt = 1;
        String rhsString = "hello";
        CompareToBuilder.reflectionCompare(lhsInt, rhsString, false, null, new String[0]);
    }

    @Test
    public void testReflectionCompare_excludeFields_allFields() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
        };
        Object rhs = new Object() {
            int a = 1;
            int b = 3;
        };
        Assert.assertEquals(0, CompareToBuilder.reflectionCompare(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(new String[0])));
    }

    @Test
    public void testReflectionCompare_excludeFields_nonexistent() {
        Object lhs = new Object() {
            int a = 1;
            int b = 2;
        };
        Object rhs = new Object() {
            int a = 1;
            int c = 2;
        };
        Assert.assertEquals(0, CompareToBuilder.reflectionCompare(lhs, rhs, ReflectionToStringBuilder.toNoNullStringArray(new String[]{"c"})));
    }

    @Test
    public void testReflectionCompare_excludeTransientFields() {
        class TestClass {
            private int a;
            private transient int b;

            public TestClass(int a, int b) {
                this.a = a;
                this.b = b;
            }
        }

        TestClass lhs = new TestClass(1, 2);
        TestClass rhs = new TestClass(1, 3);
        Assert.assertEquals(0, CompareToBuilder.reflectionCompare(lhs, rhs, false, null, new String[0]));
    }

    @Test
    public void testAppendSuper_negative() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        compareToBuilder.appendSuper(-1);
        Assert.assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_nullLhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = new Object();
        compareToBuilder.append(lhs, rhs);
        Assert.assertEquals(-1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_nullRhs() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = null;
        compareToBuilder.append(lhs, rhs);
        Assert.assertEquals(1, compareToBuilder.toComparison());
    }

    @Test
    public void testAppend_sameClassEqualValues() {
        CompareToBuilder compareToBuilder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        compareToBuilder.append(lhs, rhs);
        Assert.assertEquals(0, compareToBuilder.toComparison());
    }

    // Other test cases for the append method as described in the explanation above.

}