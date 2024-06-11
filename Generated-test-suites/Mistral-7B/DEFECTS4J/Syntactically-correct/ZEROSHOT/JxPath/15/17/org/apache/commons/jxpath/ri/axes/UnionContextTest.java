import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UnionContextTest {

    private UnionContext unionContext;
    private EvalContext context1;
    private EvalContext context2;
    private EvalContext[] contexts;

    public UnionContextTest(List<NodePointer> nodePointers1, List<NodePointer> nodePointers2) {
        context1 = new EvalContext(new BasicNodeSet(nodePointers1)) {
            @Override
            public boolean nextSet() {
                return true;
            }
        };
        context2 = new EvalContext(new BasicNodeSet(nodePointers2)) {
            @Override
            public boolean nextSet() {
                return true;
            }
        };
        unionContext = new UnionContext(null, new EvalContext[]{context1, context2});
        contexts = new EvalContext[]{context1, context2};
    }

    @Before
    public void setUp() {
        for (EvalContext ctx : contexts) {
            ctx.nextSet();
        }
    }

    @Test
    public void testGetDocumentOrder() {
        int documentOrder = unionContext.getDocumentOrder();
        assertEquals(1, documentOrder);
    }

    @Test
    public void testSetPositionWithinFirstContext() {
        int position = 0;
        boolean setPosition = unionContext.setPosition(position);
        assertTrue(setPosition);
        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        NodePointer expectedNodePointer = context1.getCurrentNodePointer();
        assertEquals(expectedNodePointer, nodePointer);
    }

    @Test
    public void testSetPositionWithinSecondContext() {
        int position = contexts[0].getSize();
        boolean setPosition = unionContext.setPosition(position);
        assertTrue(setPosition);
        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        NodePointer expectedNodePointer = context2.getCurrentNodePointer();
        assertEquals(expectedNodePointer, nodePointer);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionOutOfRange() {
        int position = unionContext.getSize() + 1;
        boolean setPosition = unionContext.setPosition(position);
        fail("Unexpectedly set position");
    }

    @Test
    public void testSize() {
        int size = unionContext.getSize();
        int totalSize = context1.getSize() + context2.getSize();
        assertEquals(totalSize, size);
    }

    @Test
    public void testCurrentNodePointerAfterLastNodeInFirstContext() {
        unionContext.nextSet();
        int position = context1.getSize();
        boolean setPosition = unionContext.setPosition(position);
        assertTrue(setPosition);
        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        NodePointer expectedNodePointer = context2.getCurrentNodePointer();
        assertEquals(expectedNodePointer, nodePointer);
    }

    @Test
    public void testCurrentNodePointerAfterLastNodeInSecondContext() {
        unionContext.nextSet();
        int position = context2.getSize();
        boolean setPosition = unionContext.setPosition(position);
        assertTrue(setPosition);
        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        NodePointer expectedNodePointer = context1.getCurrentNodePointer();
        assertEquals(expectedNodePointer, nodePointer);
    }

    @Parameterized.Parameters(name = "Context1: {0}, Context2: {1}")
    public static List<Object[]> data() {
        List<Object[]> data = new ArrayList<>();
        List<NodePointer> nodePointers1 = new ArrayList<>();
        nodePointers1.add(new NodePointer(1, 1, 1, 1, 1));
        List<NodePointer> nodePointers2 = new ArrayList<>();
        nodePointers2.add(new NodePointer(1, 1, 1, 1, 2));
        data.add(new Object[]{nodePointers1, nodePointers2});
        return data;
    }
}