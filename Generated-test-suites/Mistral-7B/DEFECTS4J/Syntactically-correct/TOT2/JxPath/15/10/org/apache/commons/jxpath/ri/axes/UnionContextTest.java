import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

    @Test
    public void testUnionContextConstructor() {
        EvalContext parentContext = new EvalContext(new BasicNodeSet());
        EvalContext context1 = new EvalContext(new BasicNodeSet());
        EvalContext context2 = new EvalContext(new BasicNodeSet());
        UnionContext unionContext = new UnionContext(parentContext, new EvalContext[]{context1, context2});
        assertEquals(3, unionContext.getContexts().length);
    }

    @Test
    public void testGetDocumentOrder() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[]{new EvalContext(new BasicNodeSet())});
        int documentOrder = unionContext.getDocumentOrder();
        assertEquals(1, documentOrder);
    }

    @Test
    public void testSetPosition() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[]{new EvalContext(new BasicNodeSet())});
        boolean positionSet = unionContext.setPosition(0);
        assertEquals(true, positionSet);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextConstructorWithNullParentContext() {
        UnionContext unionContext = new UnionContext(null, new EvalContext[]{new EvalContext(new BasicNodeSet())});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnionContextConstructorWithNullContexts() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), null);
    }

    @Test
    public void testGetDocumentOrderWhenNoContexts() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[0]);
        int documentOrder = unionContext.getDocumentOrder();
        assertEquals(super.getDocumentOrder(), documentOrder);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionOutOfBounds() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[]{new EvalContext(new BasicNodeSet())});
        boolean positionSet = unionContext.setPosition(Integer.MAX_VALUE);
        assertEquals(false, positionSet);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionNegative() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[]{new EvalContext(new BasicNodeSet())});
        boolean positionSet = unionContext.setPosition(-1);
        assertEquals(false, positionSet);
    }

    @Test
    public void testGetDocumentOrderWhenMultipleContexts() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[]{new EvalContext(new BasicNodeSet()), new EvalContext(new BasicNodeSet())});
        int documentOrder = unionContext.getDocumentOrder();
        assertEquals(1, documentOrder);
    }

    @Test
    public void testSetPositionWithFirstNode() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[]{new EvalContext(new BasicNodeSet()), new EvalContext(new BasicNodeSet())});
        boolean positionSet = unionContext.setPosition(0);
        assertEquals(true, positionSet);
    }

    @Test
    public void testSetPositionWithLastNode() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[]{new EvalContext(new BasicNodeSet()), new EvalContext(new BasicNodeSet())});
        boolean positionSet = unionContext.setPosition(unionContext.size() - 1);
        assertEquals(true, positionSet);
    }

    @Test
    public void testSetPositionWithOutOfBoundsPosition() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[]{new EvalContext(new BasicNodeSet()), new EvalContext(new BasicNodeSet())});
        boolean positionSet = unionContext.setPosition(unionContext.size() + 1);
        assertEquals(false, positionSet);
    }

    @Test
    public void testSetPositionWithNegativePosition() {
        UnionContext unionContext = new UnionContext(new EvalContext(new BasicNodeSet()), new EvalContext[]{new EvalContext(new BasicNodeSet()), new EvalContext(new BasicNodeSet())});
        boolean positionSet = unionContext.setPosition(-1);
        assertEquals(false, positionSet);
    }
}