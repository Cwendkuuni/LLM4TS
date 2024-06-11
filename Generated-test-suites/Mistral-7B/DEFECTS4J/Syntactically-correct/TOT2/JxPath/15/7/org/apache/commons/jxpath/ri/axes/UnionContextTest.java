import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {
    @Test
    public void testUnionContextEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext contexts[] = new EvalContext[0];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertFalse(unionContext.nextSet());
        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testUnionContextOneContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());
        context.nextSet();
        context.nextNode();
        EvalContext contexts[] = new EvalContext[]{context};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.nextSet());
        assertTrue(unionContext.nextNode());
        assertSame(context.getCurrentNodePointer(), unionContext.getCurrentNodePointer());
    }

    @Test
    public void testUnionContextMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        context1.nextSet();
        context1.nextNode();
        context2.nextSet();
        context2.nextNode();
        EvalContext contexts[] = new EvalContext[]{context1, context2};
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        assertTrue(unionContext.nextSet());
        assertTrue(unionContext.nextNode()); // Node from context1
        assertTrue(unionContext.nextSet());
        assertTrue(unionContext.nextNode()); // Node from context2
    }

    @Test(expected = IllegalStateException.class)
    public void testUnionContextNullContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext contexts[] = null;
        UnionContext unionContext = new UnionContext(parentContext, contexts);
    }
}