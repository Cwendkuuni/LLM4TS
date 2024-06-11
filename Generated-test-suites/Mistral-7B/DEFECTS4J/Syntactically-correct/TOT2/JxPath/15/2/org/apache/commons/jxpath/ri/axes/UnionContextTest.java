import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodeSetContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Assert;
import org.junit.Test;

public class UnionContextTest {
    @Test
    public void testUnionContextEmptyContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext contexts[] = new EvalContext[0];

        UnionContext unionContext = new UnionContext(parentContext, contexts);
        NodeSetContext nodeSetContext = (NodeSetContext) unionContext;
        BasicNodeSet nodeSet = nodeSetContext.getNodeSet();

        Assert.assertEquals(0, nodeSet.size());
    }

    @Test
    public void testUnionContextSingleContext() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context });
        NodeSetContext nodeSetContext = (NodeSetContext) unionContext;
        BasicNodeSet nodeSet = nodeSetContext.getNodeSet();

        context.addNode("test1", new NodePointer("test1"));
        context.nextSet();
        context.nextNode();

        unionContext.setPosition(0);
        Assert.assertEquals("test1", nodeSetContext.getCurrentNode().getName());
    }

    @Test
    public void testUnionContextMultipleContexts() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());

        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[] { context1, context2 });
        NodeSetContext nodeSetContext = (NodeSetContext) unionContext;
        BasicNodeSet nodeSet = nodeSetContext.getNodeSet();

        context1.addNode("test1", new NodePointer("test1"));
        context1.nextSet();
        context1.nextNode();

        context2.addNode("test2", new NodePointer("test2"));
        context2.nextSet();
        context2.nextNode();

        unionContext.setPosition(0);
        Assert.assertEquals("test1", nodeSetContext.getCurrentNode().getName());
        unionContext.nextNode();
        Assert.assertEquals("test2", nodeSetContext.getCurrentNode().getName());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testUnionContextInvalidPosition() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext contexts[] = new EvalContext[1];
        for (int i = 0; i < contexts.length; i++) {
            contexts[i] = new EvalContext(new BasicNodeSet());
            contexts[i].nextSet();
            contexts[i].nextNode();
        }

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        unionContext.setPosition(-1);
    }
}