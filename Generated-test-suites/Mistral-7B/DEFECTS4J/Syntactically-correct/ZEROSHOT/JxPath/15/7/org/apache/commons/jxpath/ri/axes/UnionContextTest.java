import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.NodeSetContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UnionContextTest {

    private EvalContext parentContext;
    private EvalContext contextA;
    private EvalContext contextB;
    private UnionContext unionContext;
    private int documentOrder;

    @Parameters(name = "{0}, {1}")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new BasicNodeSet(), new BasicNodeSet(), 1},
                {new BasicNodeSet(), new BasicNodeSet(), 2},
                {new BasicNodeSet(), new BasicNodeSet(), 3}
        });
    }

    public UnionContextTest(BasicNodeSet nodeSetA, BasicNodeSet nodeSetB, int documentOrder) {
        this.parentContext = new EvalContext(new BasicNodeSet(), null);
        this.contextA = new EvalContext(nodeSetA, parentContext);
        this.contextB = new EvalContext(nodeSetB, parentContext);
        this.documentOrder = documentOrder;
    }

    @Before
    public void setUp() {
        List<EvalContext> contexts = new ArrayList<>();
        contexts.add(contextA);
        contexts.add(contextB);
        unionContext = new UnionContext(parentContext, contexts.toArray(new EvalContext[0]));
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(documentOrder, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPositionWithSingleContext() {
        assertFalse(unionContext.setPosition(0));
    }

    @Test
    public void testSetPositionWithOneSetPositionLessThanTotalNodes() {
        contextA.nextSet();
        contextB.nextSet();

        for (int i = 0; i < contextA.size(); i++) {
            contextA.nextNode();
            contextB.nextNode();

            assertTrue(unionContext.setPosition(i + 1));
            assertEquals(contextA.getCurrentNodePointer(), unionContext.getCurrentNodePointer());
        }

        contextB.nextSet();
        for (int i = 0; i < contextB.size(); i++) {
            contextB.nextNode();

            assertTrue(unionContext.setPosition(contextA.size() + i + 1));
            assertEquals(contextB.getCurrentNodePointer(), unionContext.getCurrentNodePointer());
        }

        assertFalse(unionContext.setPosition(unionContext.size()));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionWithOneSetPositionGreaterThanTotalNodes() {
        contextA.nextSet();
        contextB.nextSet();

        for (int i = 0; i < contextA.size(); i++) {
            contextA.nextNode();
            contextB.nextNode();
        }

        unionContext.setPosition(unionContext.size() + 1);
    }

    @Test
    public void testSetPositionWithBothSetsAtEnd() {
        contextA.nextSet();
        contextB.nextSet();

        for (int i = 0; i < contextA.size(); i++) {
            contextA.nextNode();
            contextB.nextNode();
        }

        contextA.nextSet();
        for (int i = 0; i < contextB.size(); i++) {
            contextB.nextNode();
        }

        contextB.nextSet();
        for (int i = 0; i < contextA.size(); i++) {
            contextA.nextNode();
        }

        contextA.nextSet();
        for (int i = 0; i < contextB.size(); i++) {
            contextB.nextNode();
        }

        assertTrue(unionContext.setPosition(unionContext.size()));
        assertNull(unionContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithBothSetsMixed() {
        contextA.nextSet();
        contextB.nextSet();

        for (int i = 0; i < contextA.size(); i++) {
            contextA.nextNode();
            contextB.nextNode();

            assertTrue(unionContext.setPosition(i + 1));
            NodePointer nodeA = contextA.getCurrentNodePointer();
            NodePointer nodeB = contextB.getCurrentNodePointer();
            assertEquals(nodeA, unionContext.getCurrentNodePointer());
            assertNotEquals(nodeB, unionContext.getCurrentNodePointer());
        }

        contextB.nextSet();
        for (int i = 0; i < contextB.size(); i++) {
            contextB.nextNode();

            assertTrue(unionContext.setPosition(contextA.size() + i + 1));
            NodePointer nodeB = contextB.getCurrentNodePointer();
            assertEquals(nodeB, unionContext.getCurrentNodePointer());
            assertNotEquals(nodeA, unionContext.getCurrentNodePointer());
        }

        assertFalse(unionContext.setPosition(unionContext.size()));
    }
}