package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTest;
import org.junit.Test;

/**
 * Test class for CoreOperationRelationalExpression
 */
public class CoreOperationRelationalExpressionTest extends CoreOperationTest {

    // Test constructor
    @Test
    public void testConstructor() {
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("2");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationBinary[2]) {
            {
                args[0] = arg1;
                args[1] = arg2;
            }
        };
        assertEquals("Expected number of arguments: 2", 2, expression.getNumberOfArguments());
        assertEquals("Argument 1 should be: " + arg1, arg1, expression.getArgument(0));
        assertEquals("Argument 2 should be: " + arg2, arg2, expression.getArgument(1));
    }

    // Test getPrecedence
    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationBinary[2]) {
            {
                args[0] = createExpression("1");
                args[1] = createExpression("2");
            }
        };
        assertEquals("Precedence should be: 3", 3, expression.getPrecedence());
    }

    // Test isSymmetric
    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationBinary[2]) {
            {
                args[0] = createExpression("1");
                args[1] = createExpression("2");
            }
        };
        assertFalse("isSymmetric should return: false", expression.isSymmetric());
    }

    // Test equals
    @Test
    public void testEquals() {
        Expression arg1 = createExpression("1");
        Expression arg2 = createExpression("2");
        CoreOperationRelationalExpression expression1 = new CoreOperationRelationalExpression(new CoreOperationBinary[2]) {
            {
                args[0] = arg1;
                args[1] = arg2;
            }
        };
        CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(new CoreOperationBinary[2]) {
            {
                args[0] = arg1;
                args[1] = arg2;
            }
        };
        assertTrue("Expected expressions to be equal", expression1.equals(expression2));

        Expression arg3 = createExpression("3");
        CoreOperationRelationalExpression expression3 = new CoreOperationRelationalExpression(new CoreOperationBinary[2]) {
            {
                args[0] = arg1;
                args[1] = arg3;
            }
        };
        assertFalse("Expected expressions to be not equal", expression1.equals(expression3));
    }

    // Test evaluate
    @Test
    public void testEvaluate() {
        Expression leftExpression = createExpression("5");
        Expression rightExpression = createExpression("3");
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperationBinary[2]) {
            {
                args[0] = leftExpression;
                args[1] = rightExpression;
            }
        };

        Object leftValue = leftExpression.evaluate(context);
        Object rightValue = rightExpression.evaluate(context);

        // Test >
        assertEquals("Expected > to return: false", false, expression.evaluate(context));
        leftValue = 4;
        rightValue = 5;
        assertEquals("Expected > to return: true", true, expression.evaluate(context));

        // Test >=
        leftValue = 4;
        rightValue = 4;
        assertEquals("Expected >= to return: true", true, expression.evaluate(new CoreOperationGreaterOrEqualExpression(new CoreOperationBinary[2]) {
            {
                args[0] = leftExpression;
                args[1] = rightExpression;
            }
        }.evaluate(context)));
        leftValue = 4;
        rightValue = 5;
        assertEquals("Expected >= to return: false", false, expression.evaluate(new CoreOperationGreaterOrEqualExpression(new CoreOperationBinary[2]) {
            {
                args[0] = leftExpression;
                args[1] = rightExpression;
            }
        }.evaluate(context)));
    }

}