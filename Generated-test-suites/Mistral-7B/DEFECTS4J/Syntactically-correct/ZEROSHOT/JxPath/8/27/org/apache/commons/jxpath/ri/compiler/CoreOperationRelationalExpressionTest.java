import org.apache.commons.jxpath.ri.CompilerTestCase;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Axis;
import org.apache.commons.jxpath.ri.InitialContext;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.Path;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Variable;

public class CoreOperationRelationalExpressionTest extends CompilerTestCase {

    public CoreOperationRelationalExpressionTest(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGreaterThanExpression() throws Exception {
        CoreOperationRelationalExpression exp = (CoreOperationRelationalExpression) parser.compile("a > b");
        assertInstanceOf(CoreOperationRelationalExpression.class, exp);
        assertEquals(Axis.CHILD, exp.getAxis());
        assertEquals(2, exp.getOperands().length);

        EvalContext context = new EvalContext(node1, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        Object result = exp.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));

        context = new EvalContext(node2, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertFalse(Boolean.TRUE.equals(result));

        context = new EvalContext(node3, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));

        context = new EvalContext(node4, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertFalse(Boolean.TRUE.equals(result));
    }

    public void testGreaterThanOrEqualExpression() throws Exception {
        CoreOperationRelationalExpression exp = (CoreOperationRelationalExpression) parser.compile("a >= b");
        assertInstanceOf(CoreOperationRelationalExpression.class, exp);
        assertEquals(Axis.CHILD, exp.getAxis());
        assertEquals(2, exp.getOperands().length);

        EvalContext context = new EvalContext(node1, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        Object result = exp.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));

        context = new EvalContext(node2, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));

        context = new EvalContext(node3, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));

        context = new EvalContext(node4, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));
    }

    public void testLessThanExpression() throws Exception {
        CoreOperationRelationalExpression exp = (CoreOperationRelationalExpression) parser.compile("a < b");
        assertInstanceOf(CoreOperationRelationalExpression.class, exp);
        assertEquals(Axis.CHILD, exp.getAxis());
        assertEquals(2, exp.getOperands().length);

        EvalContext context = new EvalContext(node1, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        Object result = exp.computeValue(context);
        assertFalse(Boolean.TRUE.equals(result));

        context = new EvalContext(node2, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertFalse(Boolean.TRUE.equals(result));

        context = new EvalContext(node3, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertFalse(Boolean.TRUE.equals(result));

        context = new EvalContext(node4, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));
    }

    public void testLessThanOrEqualExpression() throws Exception {
        CoreOperationRelationalExpression exp = (CoreOperationRelationalExpression) parser.compile("a <= b");
        assertInstanceOf(CoreOperationRelationalExpression.class, exp);
        assertEquals(Axis.CHILD, exp.getAxis());
        assertEquals(2, exp.getOperands().length);

        EvalContext context = new EvalContext(node1, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        Object result = exp.computeValue(context);
        assertFalse(Boolean.TRUE. equals(result));

        context = new EvalContext(node2, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));

        context = new EvalContext(node3, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));

        context = new EvalContext(node4, new Axis(Axis.CHILD, new Path(".")), new InitialContext());
        result = exp.computeValue(context);
        assertTrue(Boolean.TRUE.equals(result));
    }

    private NodeInfo node1 = new NodeInfo("1", new Axis(Axis.CHILD, new Path(".")), null);
    private NodeInfo node2 = new NodeInfo("2", new Axis(Axis.CHILD, new Path(".")), null);
    private NodeInfo node3 = new NodeInfo("3", new Axis(Axis.CHILD, new Path(".")), null);
    private NodeInfo node4 = new NodeInfo("4", new Axis(Axis.CHILD, new Path(".")), null);

}