import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Assert;
import org.junit.Test;

public class UnionContextTest {

    @Test
    public void testUnionContextConstruction() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext[] contexts = new EvalContext[2];
        UnionContext unionContext = new UnionContext(parentContext, contexts);
        Assert.assertNotNull(unionContext);
    }

    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
        Assert.assertEquals(0, unionContext.getDocumentOrder());

        UnionContext unionContextWithContexts = new UnionContext(null, new EvalContext[1]);
        Assert.assertEquals(1, unionContextWithContexts.getDocumentOrder());

        UnionContext unionContextWithMultipleContexts = new UnionContext(null, new EvalContext[2]);
        Assert.assertEquals(1, unionContextWithMultipleContexts.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
        Assert.assertFalse(unionContext.setPosition(0));

        UnionContext unionContextWithContexts = new UnionContext(null, new EvalContext[1]);
        unionContextWithContexts.setPosition(0);
        Assert.assertTrue(unionContextWithContexts.setPosition(0));

        UnionContext unionContextWithMultipleContexts = new UnionContext(null, new EvalContext[2]);
        unionContextWithMultipleContexts.setPosition(0);
        Assert.assertTrue(unionContextWithMultipleContexts.setPosition(1));
    }
}