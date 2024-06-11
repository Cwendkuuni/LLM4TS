import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NodeSet;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

    @Test
    public void testUnionContextWithEmptyContexts() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        EvalContext contexts[] = new EvalContext[0];

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        NodeSet nodeSet = unionContext.getNodeSet();
        assertTrue(nodeSet.isEmpty());
    }

    @Test
    public void testUnionContextWithSingleContext() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        EvalContext context = new EvalContext(new BasicNodeSet().add(createNode("root1", 1)));
        EvalContext contexts[] = new EvalContext[]{context};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        NodeSet nodeSet = unionContext.getNodeSet();
        assertEquals(1, nodeSet.size());
        assertEquals(createNode("root1", 1), nodeSet.iterator().next().getNode());
    }

    @Test
    public void testUnionContextWithTwoDifferentContexts() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        EvalContext context1 = new EvalContext(new BasicNodeSet().add(createNode("root1_1", 1)));
        EvalContext context2 = new EvalContext(new BasicNodeSet().add(createNode("root1_2", 2)));
        EvalContext contexts[] = new EvalContext[]{context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        NodeSet nodeSet = unionContext.getNodeSet();
        assertEquals(2, nodeSet.size());
        List<Node> nodes = new ArrayList<>(nodeSet);
        Assert.assertTrue(nodes.contains(createNode("root1_1", 1)));
        Assert.assertTrue(nodes.contains(createNode("root1_2", 2)));
    }

    @Test
    public void testUnionContextWithTwoIdenticalContexts() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        EvalContext context = new EvalContext(new BasicNodeSet().add(createNode("root1", 1)));
        EvalContext contexts[] = new EvalContext[]{context, context};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        NodeSet nodeSet = unionContext.getNodeSet();
        assertEquals(1, nodeSet.size());
        assertEquals(createNode("root1", 1), nodeSet.iterator().next().getNode());
    }

    @Test
    public void testUnionContextWithTwoContextsWithSameNode() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        Node node = createNode("root1", 1);
        EvalContext context1 = new EvalContext(new BasicNodeSet().add(node));
        EvalContext context2 = new EvalContext(new BasicNodeSet().add(node));
        EvalContext contexts[] = new EvalContext[]{context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        NodeSet nodeSet = unionContext.getNodeSet();
        assertEquals(1, nodeSet.size());
        assertEquals(node, nodeSet.iterator().next().getNode());
    }

    @Test
    public void testUnionContextWithContextsWithDifferentDocumentOrder() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        EvalContext context1 = new EvalContext(new BasicNodeSet().add(createNode("root1_1", 1)));
        EvalContext context2 = new EvalContext(new BasicNodeSet().add(createNode("root2_1", 2)).setDocumentOrder(1));
        EvalContext contexts[] = new EvalContext[]{context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        NodeSet nodeSet = unionContext.getNodeSet();
        assertEquals(2, nodeSet.size());
        List<Node> nodes = new ArrayList<>(nodeSet);
        Assert.assertTrue(nodes.contains(createNode("root1_1", 1)));
        Assert.assertTrue(nodes.contains(createNode("root2_1", 2)));
        assertEquals(1, unionContext.getDocumentOrder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextWithNullContexts() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        EvalContext contexts[] = null;

        UnionContext unionContext = new UnionContext(parentContext, contexts);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextWithNullContext() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        EvalContext contexts[] = new EvalContext[]{null};

        UnionContext unionContext = new UnionContext(parentContext, contexts);
    }

    @Test
    public void testUnionContextPositioning() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        Node node1 = createNode("root1_1", 1);
        Node node2 = createNode("root1_2", 2);
        EvalContext context1 = new EvalContext(new BasicNodeSet().add(node1));
        EvalContext context2 = new EvalContext(new BasicNodeSet().add(node2));
        EvalContext contexts[] = new EvalContext[]{context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);

        assertEquals(2, unionContext.getSize());
        assertFalse(unionContext.nextSet());

        unionContext.nextSet();
        assertEquals(2, unionContext.getSize());
        unionContext.nextNode();
        assertEquals(node1, unionContext.getCurrentNode().getNode());
        assertFalse(unionContext.nextNode());

        unionContext.nextNode();
        assertEquals(node2, unionContext.getCurrentNode().getNode());
        assertFalse(unionContext.nextNode());

        unionContext.nextSet();
        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testUnionContextPositioningAfterPreparation() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        Node node1 = createNode("root1_1", 1);
        Node node2 = createNode("root1_2", 2);
        EvalContext context1 = new EvalContext(new BasicNodeSet().add(node1));
        EvalContext context2 = new EvalContext(new BasicNodeSet().add(node2));
        EvalContext contexts[] = new EvalContext[]{context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.nextSet();

        assertEquals(2, unionContext.getSize());
        assertFalse(unionContext.nextNode());

        unionContext.nextNode();
        assertEquals(node1, unionContext.getCurrentNode().getNode());
        assertFalse(unionContext.nextNode());

        unionContext.nextNode();
        assertEquals(node2, unionContext.getCurrentNode().getNode());
        assertFalse(unionContext.nextNode());

        unionContext.nextSet();
        assertFalse(unionContext.nextNode());
    }

    @Test
    public void testUnionContextPositioningAfterPreparationAndMoveBack() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        Node node1 = createNode("root1_1", 1);
        Node node2 = createNode("root1_2", 2);
        EvalContext context1 = new EvalContext(new BasicNodeSet().add(node1));
        EvalContext context2 = new EvalContext(new BasicNodeSet().add(node2));
        EvalContext contexts[] = new EvalContext[]{context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.nextSet();
        unionContext.nextNode();

        assertEquals(node1, unionContext.getCurrentNode().getNode());

        unionContext.previousNode();
        assertEquals(node2, unionContext.getCurrentNode().getNode());
    }

    @Test
    public void testUnionContextPositioningAfterPreparationAndMoveToEnd() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        Node node1 = createNode("root1_1", 1);
        Node node2 = createNode("root1_2", 2);
        EvalContext context1 = new EvalContext(new BasicNodeSet().add(node1));
        EvalContext context2 = new EvalContext(new BasicNodeSet().add(node2));
        EvalContext contexts[] = new EvalContext[]{context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.nextSet();
        unionContext.nextNode();
        unionContext.nextNode();

        assertNull(unionContext.getCurrentNode());
    }

    @Test
    public void testUnionContextPositioningAfterPreparationAndMoveToBeginning() {
        EvalContext parentContext = new EvalContext(new EvalContext(new BasicNodeSet()));
        Node node1 = createNode("root1_1", 1);
        Node node2 = createNode("root1_2", 2);
        EvalContext context1 = new EvalContext(new BasicNodeSet().add(node1));
        EvalContext context2 = new EvalContext(new BasicNodeSet().add(node2));
        EvalContext contexts[] = new EvalContext[]{context1, context2};

        UnionContext unionContext = new UnionContext(parentContext, contexts);
        unionContext.nextSet();
        unionContext.previousSet();

        assertNull(unionContext.getCurrentNode());
    }

    private Node createNode(String name, int index) {
        Node node = new JXPathContext("dummy:document()").setDocumentOrder(index).setNodeValue(name);
        return node;
    }
}