package org.apache.commons.jxpath.ri.compiler.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.nodes.SimpleNode;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThan;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.ExpressionFactory;
import org.apache.commons.jxpath.ri.compiler.ExpressionVisitor;

public class CoreOperationLessThanTest {

    @Test
    public void testCoreOperationLessThan() {
        // Test with valid numbers
        Expression arg1 = ExpressionFactory.createNumber(1.0);
        Expression arg2 = ExpressionFactory.createNumber(2.0);
        CoreOperationLessThan coreOpLessThan = new CoreOperationLessThan(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());
        Object result = coreOpLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createNumber(1.0);
        arg2 = ExpressionFactory.createNumber(1.0);
        coreOpLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOpLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);

        arg1 = ExpressionFactory.createNumber(1.0);
        arg2 = ExpressionFactory.createNumber(0.5);
        coreOpLessThan = new CoreOperationLessThan(arg1, arg2);
        result = coreOpLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);

        // Test with different types
        arg1 = ExpressionFactory.createString("1");
        arg2 = ExpressionFactory.createNumber(1.0);
        coreOpLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(new InfoSet());
        context.setCurrentNode(new SimpleNode(Path.EMPTY_PATH, new NodeInfo(1, "test"), null, null, null));
        result = coreOpLessThan.computeValue(context);
        assertNull(result);

        arg1 = ExpressionFactory.createNumber(1.0);
        arg2 = ExpressionFactory.createString("2");
        coreOpLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(new InfoSet());
        context.setCurrentNode(new SimpleNode(Path.EMPTY_PATH, new NodeInfo(1, "test"), null, null, null));
        result = coreOpLessThan.computeValue(context);
        assertNull(result);

        // Test with null value
        arg1 = ExpressionFactory.createNull();
        arg2 = ExpressionFactory.createNumber(1.0);
        coreOpLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOpLessThan.computeValue(context);
        assertNull(result);

        arg1 = ExpressionFactory.createNumber(1.0);
        arg2 = ExpressionFactory.createNull();
        coreOpLessThan = new CoreOperationLessThan(arg1, arg2);
        context = new EvalContext(new InfoSet());
        result = coreOpLessThan.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationLessThanVisitor() {
        Expression arg1 = ExpressionFactory.createNumber(1.0);
        Expression arg2 = ExpressionFactory.createNumber(2.0);
        CoreOperationLessThan coreOpLessThan = new CoreOperationLessThan(arg1, arg2);
        ExpressionVisitor visitor = new ExpressionVisitor();

        visitor.visit(coreOpLessThan);
        assertNotNull(visitor.getLeftOperand());
        assertEquals(arg1, visitor.getLeftOperand());

        assertNotNull(visitor.getRightOperand());
        assertEquals(arg2, visitor.getRightOperand());

        assertNull(visitor.getRightSide());
    }
}