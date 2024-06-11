import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for UnionContext
 */
public class UnionContextTest {

    private NodePointer nodePointer1;
    private NodePointer nodePointer2;
    private NodePointer nodePointer3;
    private NodePointer nodePointer4;
    private NodePointer nodePointer5;
    private EvalContext contextA;
    private EvalContext contextB;
    private EvalContext unionContext;

    @Before
    public void setUp() {
        Document document = XMLUtil.createDocument();
        Element root = document.createElement("root");
        document.appendChild(root);

        Element a = document.createElement("a");
        root.appendChild(a);

        Element b = document.createElement("b");
        root.appendChild(b);

        Element c = document.createElement("c");
        root.appendChild(c);

        nodePointer1 = new NodePointer(document, 0, 0);
        nodePointer2 = new NodePointer(document, 1, 0);
        nodePointer3 = new NodePointer(document, 2, 0);
        nodePointer4 = new NodePointer(document, 1, 1);
        nodePointer5 = new NodePointer(document, 2, 1);

        contextA = new EvalContext(null, new BasicNodeSet(Arrays.asList(nodePointer1, nodePointer2)));
        contextB = new EvalContext(null, new BasicNodeSet(Arrays.asList(nodePointer3, nodePointer4, nodePointer5)));
    }

    @Test
    public void testEmptyUnionContext() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
        assertEquals(0, unionContext.size());
        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContextWithEmptyContexts() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
        assertEquals(0, unionContext.size());
        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContextWithOneContext() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA});
        assertEquals(2, unionContext.size());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer1, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer2, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContextWithTwoContexts() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
        assertEquals(5, unionContext.size());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer1, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer2, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer3, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer4, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer5, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContextWithDuplicateNodes() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB});
        assertEquals(5, unionContext.size());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer1, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer2, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer3, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer4, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer5, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContextWithNoDuplicateNodes() {
        EvalContext contextC = new EvalContext(null, new BasicNodeSet(Arrays.asList(nodePointer3, nodePointer5)));
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, contextB, contextC});
        assertEquals(5, unionContext.size());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer1, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer2, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer3, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer4, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer5, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContextWithMultipleSetsInEachContext() {
        EvalContext contextCA = new EvalContext(null, new BasicNodeSet(Arrays.asList(nodePointer1, nodePointer2)));
        EvalContext contextCB = new EvalContext(null, new BasicNodeSet(Arrays.asList(nodePointer3, nodePointer4, nodePointer5)));
        EvalContext contextCC = new EvalContext(null, new BasicNodeSet(Arrays.asList(nodePointer1, nodePointer2, nodePointer3)));
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextCA, contextCB, contextCC});
        assertEquals(6, unionContext.size());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer1, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer2, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer3, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer4, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer5, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertFalse(unionContext.nextSet());
        assertTrue(unionContext.nextSet());
        assertNull(unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContextWithEmptyContext() {
        EvalContext emptyContext = new EvalContext(null, new BasicNodeSet());
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, emptyContext, contextB});
        assertEquals(5, unionContext.size());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer1, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer2, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer3, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer4, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer5, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertFalse(unionContext.nextSet());
    }

    @Test
    public void testUnionContextWithNullContext() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{contextA, null, contextB});
        assertEquals(2, unionContext.size());
        assertTrue(unionContext.nextSet());
        assertEquals(nodePointer1, unionContext.getCurrentNodePointer());
        assertTrue(unionContext.nextNode());
        assertEquals(nodePointer2, unionContext.getCurrentNodePointer());
        assertFalse(unionContext.nextNode());
        assertFalse(unionContext.nextSet());
    }
}