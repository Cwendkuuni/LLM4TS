import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class UnionContextTest {

    private EvalContext parentContext;
    private EvalContext context1;
    private EvalContext context2;
    private UnionContext unionContext;

    @Parameterized.Parameters(name = "Context1: {0}, Context2: {1}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{new BasicNodeSetContext(new BasicNodeSet()), new BasicNodeSetContext(new BasicNodeSet()), new UnionContext(null, new EvalContext[0])},
                new Object[]{new BasicNodeSetContext(new BasicNodeSet()), new BasicNodeSetContext(new BasicNodeSet()), new UnionContext(new BasicNodeSetContext(new BasicNodeSet()), new EvalContext[0])},
                new Object[]{new BasicNodeSetContext(new BasicNodeSet()), new BasicNodeSetContext(new BasicNodeSet()), new UnionContext(new BasicNodeSetContext(new BasicNodeSet()), new BasicNodeSetContext(new BasicNodeSet()))}
        });
    }

    @Before
    public void setUp() {
        parentContext = new BasicNodeSetContext(new BasicNodeSet());
        context1 = new BasicNodeSetContext(new BasicNodeSet());
        context2 = new BasicNodeSetContext(new BasicNodeSet());
        unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
    }

    @Test
    public void testGetDocumentOrder() {
        assertTrue(unionContext.getDocumentOrder() > 0);
    }

    @Test
    public void testSetPosition_whenNoContextsPrepared_thenShouldBeFalse() {
        assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPosition_whenContextsPrepared_thenShouldBeTrue() {
        unionContext.contexts[0].nextSet();
        unionContext.contexts[0].nextNode();
        unionContext.contexts[1].nextSet();
        unionContext.contexts[1].nextNode();

        unionContext.setPosition(0);
        assertTrue(unionContext.setPosition(1));
        assertTrue(unionContext.getCurrentNodePointer().equals(unionContext.contexts[1].getCurrentNodePointer()));

        unionContext.setPosition(0);
        assertTrue(unionContext.setPosition(0));
        assertTrue(unionContext.getCurrentNodePointer().equals(unionContext.contexts[0].getCurrentNodePointer()));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPosition_whenPositionOutOfBounds_thenShouldThrowException() {
        unionContext.contexts[0].nextSet();
        unionContext.contexts[0].nextNode();
        unionContext.contexts[1].nextSet();
        unionContext.contexts[1].nextNode();

        unionContext.setPosition(unionContext.getSize() + 1);
    }
}