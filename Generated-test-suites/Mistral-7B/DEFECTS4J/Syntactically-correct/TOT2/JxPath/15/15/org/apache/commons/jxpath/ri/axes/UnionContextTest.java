import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class UnionContextTest {

    private UnionContext unionContext;
    private EvalContext parentContext;
    private EvalContext[] contexts;

    public UnionContextTest(EvalContext parentContext, EvalContext[] contexts) {
        this.parentContext = parentContext;
        this.contexts = contexts;
        this.unionContext = new UnionContext(parentContext, contexts);
    }

    @Parameterized.Parameters(name = "{index}: UnionContext({0}, {1})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new EvalContext(new BasicNodeSet()), new EvalContext[]{}},
                {new EvalContext(new BasicNodeSet()), new EvalContext[]{new EvalContext(new BasicNodeSet())}}
        });
    }

    @Test
    public void testGetDocumentOrder() {
        Assert.assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_ValidPosition() {
        Assert.assertTrue(unionContext.setPosition(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_InvalidPosition() {
        Assert.assertFalse(unionContext.setPosition(-1));
    }
}