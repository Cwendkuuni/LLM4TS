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

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class UnionContextTest {

    private BasicNodeSet nodeSet;
    private UnionContext unionContext;
    private EvalContext context1;
    private EvalContext context2;
    private int position;

    public UnionContextTest(EvalContext context1, EvalContext context2, int position) {
        this.context1 = context1;
        this.context2 = context2;
        this.position = position;
    }

    @Parameterized.Parameters(name = "{index}: position={0}")
    public static List<Object[]> data() {
        BasicNodeSet nodeSet1 = new BasicNodeSet();
        BasicNodeSet nodeSet2 = new BasicNodeSet();
        EvalContext context1 = new EvalContext(null, nodeSet1);
        EvalContext context2 = new EvalContext(null, nodeSet2);

        nodeSet1.add(new NodePointer(1, "root", 1));
        nodeSet1.add(new NodePointer(2, "root", 2));

        nodeSet2.add(new NodePointer(3, "root", 1));
        nodeSet2.add(new NodePointer(2, "root", 2));
        nodeSet2.add(new NodePointer(4, "root", 3));

        return Arrays.asList(new Object[][]{
                {context1, context2, 0},
                {context1, context2, 1},
                {context1, context2, 2},
                {context1, context2, 3},
                {context1, context2, 4},
                {context1, context2, 5}
        });
    }

    @Before
    public void setUp() {
        this.nodeSet = new BasicNodeSet();
        this.unionContext = new UnionContext(this.context1, new EvalContext[]{this.context1, this.context2});
    }

    @Test
    public void testGetDocumentOrder() {
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test
    public void testSetPosition() {
        for (int i = 0; i < position; i++) {
            unionContext.nextSet();
            unionContext.nextNode();
        }
        unionContext.nextNode();

        NodePointer nodePointer = unionContext.getCurrentNodePointer();
        int expectedNodeId = position == 0 ? 1 : (position - 1) % (context1.size() + context2.size()) + 1;
        assertEquals("Unexpected node: " + nodePointer, expectedNodeId, nodePointer.getNodeId());
    }
}