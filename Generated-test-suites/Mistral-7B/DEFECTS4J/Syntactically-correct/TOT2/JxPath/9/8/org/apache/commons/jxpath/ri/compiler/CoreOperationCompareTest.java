package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        Expression arg1 = Mockito.mock(Expression.class);
        Expression arg2 = Mockito.mock(Expression.class);
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
    }

    @Test
    public void testCoreOperationCompareConstructor() {
        // Test code here
    }

    @Test
    public void testGetPrecedence() {
        // Test code here
    }

    @Test
    public void testIsSymmetric() {
        // Test code here
    }

    @Test
    public void testEqual() {
        // Test code here
    }

    @Test
    public void testContains() {
        // Test code here
    }

    @Test
    public void testFindMatch() {
        // Test code here
    }

    @Test
    public void testEqualObjects() {
        // Test code here
    }

    @Test
    public void testEqualDifferentObjects() {
        // Test code here
    }

    @Test
    public void testEqualNull() {
        // Test code here
    }

    @Test
    public void testEqualNull2() {
        // Test code here
    }

    @Test
    public void testEqualBooleanTrue() {
        // Test code here
    }

    @Test
    public void testEqualBooleanFalse() {
        // Test code here
    }

    @Test
    public void testEqualBooleanFalseAndTrue() {
        // Test code here
    }

    @Test
    public void testEqualNumber() {
        // Test code here
    }

    @Test
    public void testEqualNumberDifferent() {
        // Test code here
    }

    @Test
    public void testEqualString() {
        // Test code here
    }

    @Test
    public void testEqualStringDifferent() {
        // Test code here
    }

    @Test
    public void testEqualStringNull() {
        // Test code here
    }

    @Test
    public void testEqualStringNull2() {
        // Test code here
    }
}