package org.apache.commons.jxpath.ri.compiler.test;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.ExpressionType;
import org.apache.commons.jxpath.ri.compiler.CoreOperationTest;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory.Operator;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory.OperatorType;
import org.junit.Test;

/**
 * Test case for CoreOperationCompare class
 */
public class CoreOperationCompareTest extends CoreOperationTest {

    @Test
    public void testEqual() throws Exception {
        Expression left = createNumberExpression(1);
        Expression right = createNumberExpression(1);

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNotEqual() throws Exception {
        Expression left = createNumberExpression(1);
        Expression right = createNumberExpression(2);

        CoreOperationCompare compare = new CoreOperationCompare(
            ExpressionFactory.create(Operator.EQ, left, right),
            ExpressionFactory.create(Operator.NE, left, right));
        EvalContext context = createEvalContext();
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualCollection() throws Exception {
        Expression left = createCollectionExpression(new int[] { 1, 2, 3 });
        Expression right = createCollectionExpression(new int[] { 1, 2, 3 });

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNotEqualCollection() throws Exception {
        Expression left = createCollectionExpression(new int[] { 1, 2, 3 });
        Expression right = createCollectionExpression(new int[] { 1, 2, 4 });

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualIterator() throws Exception {
        Expression left = createIteratorExpression(new int[] { 1, 2, 3 }.iterator());
        Expression right = createIteratorExpression(new int[] { 1, 2, 3 }.iterator());

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNotEqualIterator() throws Exception {
        Expression left = createIteratorExpression(new int[] { 1, 2, 3 }.iterator());
        Expression right = createIteratorExpression(new int[] { 1, 2, 4 }.iterator());

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualPointer() throws Exception {
        Expression left = createPointerExpression(createVariableWithValue(new Integer(1)));
        Expression right = createPointerExpression(createVariableWithValue(new Integer(1)));

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNotEqualPointer() throws Exception {
        Expression left = createPointerExpression(createVariableWithValue(new Integer(1)));
        Expression right = createPointerExpression(createVariableWithValue(new Integer(2)));

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualNull() throws Exception {
        Expression left = createNullExpression();
        Expression right = createNullExpression();

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNotEqualNull() throws Exception {
        Expression left = createNullExpression();
        Expression right = createNumberExpression(1);

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualBoolean() throws Exception {
        Expression left = createBooleanExpression(true);
        Expression right = createBooleanExpression(true);

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNotEqualBoolean() throws Exception {
        Expression left = createBooleanExpression(true);
        Expression right = createBooleanExpression(false);

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualString() throws Exception {
        Expression left = createStringExpression("abc");
        Expression right = createStringExpression("abc");

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNotEqualString() throws Exception {
        Expression left = createStringExpression("abc");
        Expression right = createStringExpression("def");

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testEqualStringCaseSensitive() throws Exception {
        Expression left = createStringExpression("abc");
        Expression right = createStringExpression("ABC");

        CoreOperationCompare compare = new CoreOperationCompare(left, right);
        EvalContext context = createEvalContext();
        assertFalse(compare.evaluate(context));
    }

    @Test
    public void testNotEqualStringCaseInsensitive() throws Exception {
        Expression left = createStringExpression("abc");
        Expression right = createStringExpression("ABC");

        CoreOperationCompare compare = new CoreOperationCompare(left, right, ExpressionType.STRING_CASE_INSENSITIVE);
        EvalContext context = createEvalContext();
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testEqualStringTrim() throws Exception {
        Expression left = createStringExpression("abc");
        Expression right = createStringExpression(" abc ");

        CoreOperationCompare compare = new CoreOperationCompare(left, right, ExpressionType.STRING_TRIM);
        EvalContext context = createEvalContext();
        assertTrue(compare.evaluate(context));
    }

    @Test
    public void testNotEqualStringTrim() throws Exception {
        Expression left = createStringExpression("abc");
        Expression right = createStringExpression(" def ");

        CoreOperationCompare compare = new CoreOperationCompare(left, right, ExpressionType.STRING_TRIM);
        EvalContext context = createEvalContext();
        assertFalse(compare.evaluate(context));
    }

    private Expression createCollectionExpression(Iterator iterator) {
        Expression left = ExpressionFactory.create(ExpressionType.COLLECTION, iterator);
        return ExpressionFactory.create(ExpressionType.VARIABLE, "iterator")
            .setExpression(ExpressionFactory.create(Operator.ASSIGN, createVariable("iterator"), left));
    }

    private Expression createIteratorExpression(Iterator iterator) {
        return ExpressionFactory.create(ExpressionType.ITERATOR, iterator);
    }

    private Expression createPointerExpression(Object value) {
        Expression left = ExpressionFactory.create(ExpressionType.VARIABLE, "value");
        return ExpressionFactory.create(ExpressionType.POINTER, left)
            .setExpression(ExpressionFactory.create(Operator.ASSIGN, left, createVariableWithValue(value)));
    }

}