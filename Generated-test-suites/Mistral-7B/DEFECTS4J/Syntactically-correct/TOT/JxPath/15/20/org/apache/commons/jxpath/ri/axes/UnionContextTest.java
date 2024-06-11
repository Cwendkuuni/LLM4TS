import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

    private static final NodePointer FIRST_NODE = new NodePointer(0, 0);
    private static final NodePointer SECOND_NODE = new NodePointer(1, 1);
    private static final NodePointer THIRD_NODE = new NodePointer(2, 2);
    private static final NodePointer FOURTH_NODE = new NodePointer(3, 3);

    private EvalContext contextA;
    private EvalContext contextB;
    private UnionContext unionContext;

    @Before
    public void setUp() {
        List<NodePointer> nodeSetA = new ArrayList<>();
        nodeSetA.add(FIRST_NODE);
        contextA = new EvalContext(new BasicNodeSet(nodeSetA), null);

        List<NodePointer> nodeSetB = new ArrayList<>();
        nodeSetB.add(SECOND_NODE);
        nodeSetB.add(THIRD_NODE);
        contextB = new EvalContext(new BasicNodeSet(nodeSetB), null);

        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition_SingleContext() {
        unionContext.setPosition(0);
        NodePointer node = unionContext.getCurrentNodePointer();
        assertSame(FIRST_NODE, node);
    }

    @Test
    public void testSetPosition_FirstContextNode() {
        unionContext.setPosition(0);
        unionContext.nextNode();
        NodePointer node = unionContext.getCurrentNodePointer();
        assertSame(SECOND_NODE, node);
    }

    @Test
    public void testSetPosition_SecondContextNode() {
        contextB.nextNode();
        unionContext.setPosition(0);
        unionContext.nextNode();
        NodePointer node = unionContext.getCurrentNodePointer();
        assertSame(THIRD_NODE, node);
    }

    @Test
    public void testSetPosition_EmptyFirstContext() {
        contextA.setEmpty();
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
        unionContext.setPosition(0);
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition_EmptySecondContext() {
        contextB.setEmpty();
        unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
        unionContext.setPosition(0);
        assertSame(FIRST_NODE, unionContext.getCurrentNodePointer());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPosition_BeyondTheEnd() {
        unionContext.setPosition(4);
    }

    @Test
    public void testNextSet_TwoSets() {
        assertFalse(unionContext.nextSet());
        contextA.nextSet();
        assertTrue(unionContext.nextSet());
        assertSame(FIRST_NODE, unionContext.getCurrentNodePointer());
        contextB.nextSet();
        assertTrue(unionContext.nextSet());
        assertSame(SECOND_NODE, unionContext.getCurrentNodePointer());
        contextA.nextSet();
        assertTrue(unionContext.nextSet());
        assertSame(THIRD_NODE, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testNextSet_OneSet() {
        contextB.setEmpty();
        assertFalse(unionContext.nextSet());
        contextA.nextSet();
        assertTrue(unionContext.nextSet());
        assertSame(FIRST_NODE, unionContext.getCurrentNodePointer());
    }

    @Test
    public void testNextSet_BothSetsEmpty() {
        contextA.setEmpty();
        contextB.setEmpty();
        assertFalse(unionContext.nextSet());
        assertNull(unionContext.getCurrentNodePointer());
    }
}