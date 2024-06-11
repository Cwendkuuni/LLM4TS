import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.apache.commons.jxpath.ri.compiler.CoreOperationCompare.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression left = ExpressionFactory.createLiteral(1);
        Expression right = ExpressionFactory.createLiteral(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertTrue(coreOperationCompare.compare(new EvalContext(), null));
    }

    @Test
    public void testUnequalNullLeft() {
        Expression left = ExpressionFactory.createNull();
        Expression right = ExpressionFactory.createLiteral(1);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertFalse(coreOperationCompare.compare(new EvalContext(), null));
    }

    @Test
    public void testUnequalNullRight() {
        Expression left = ExpressionFactory.createLiteral(1);
        Expression right = ExpressionFactory.createNull();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertFalse(coreOperationCompare.compare(new EvalContext(), null));
    }

    @Test
    public void testUnequalDifferentTypes() {
        Expression left = ExpressionFactory.createLiteral(1);
        Expression right = ExpressionFactory.createLiteral("1");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertFalse(coreOperationCompare.compare(new EvalContext(), null));
    }

    @Test
    public void testUnequalDifferentBooleanValues() {
        Expression left = ExpressionFactory.createLiteral(true);
        Expression right = ExpressionFactory.createLiteral(false);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertFalse(coreOperationCompare.compare(new EvalContext(), null));
    }

    @Test
    public void testUnequalDifferentNumberValues() {
        Expression left = ExpressionFactory.createLiteral(1.0);
        Expression right = ExpressionFactory.createLiteral(2.0);
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertFalse(coreOperationCompare.compare(new EvalContext(), null));
    }

    @Test
    public void testUnequalDifferentStringValues() {
        Expression left = ExpressionFactory.createLiteral("1");
        Expression right = ExpressionFactory.createLiteral("2");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertFalse(coreOperationCompare.compare(new EvalContext(), null));
    }

    @Test
    public void testUnequalDifferentObjects() {
        Expression left = ExpressionFactory.createLiteral(new Object());
        Expression right = ExpressionFactory.createLiteral(new Object());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertFalse(coreOperationCompare.compare(new EvalContext(), null));
    }

    @Test
    public void testEqualSameObjects() {
        Expression left = ExpressionFactory.createLiteral(new Object());
        Expression right = ExpressionFactory.createLiteral(new Object());
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertTrue(coreOperationCompare.compare(new EvalContext(), null));
    }

    @Test
    public void testEqualCollectionSameElements() {
        Expression left = ExpressionFactory.createVariable("list");
        Expression right = ExpressionFactory.createVariable("list");
        EvalContext context = new EvalContext();
        context.setVariable("list", new ArrayList<>());
        context.setVariable("list", context.getVariable("list"));

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertTrue(coreOperationCompare.compare(context, null));
    }

    @Test
    public void testUnequalCollectionDifferentElements() {
        Expression left = ExpressionFactory.createVariable("list1");
        Expression right = ExpressionFactory.createVariable("list2");
        EvalContext context = new EvalContext();
        context.setVariable("list1", new ArrayList<>());
        context.setVariable("list1", context.getVariable("list1"));
        context.setVariable("list2", new ArrayList<>());
        context.setVariable("list2", context.getVariable("list2"));
        context.getVariable("list1").add(ExpressionFactory.createLiteral(1));

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertFalse(coreOperationCompare.compare(context, null));
    }

    @Test
    public void testEqualIteratorsSameElements() {
        Expression left = ExpressionFactory.createVariable("iterator1");
        Expression right = ExpressionFactory.createVariable("iterator2");
        EvalContext context = new EvalContext();
        context.setVariable("iterator1", new ArrayList<>().iterator());
        context.setVariable("iterator2", new ArrayList<>().iterator());

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertTrue(coreOperationCompare.compare(context, null));
    }

    @Test
    public void testUnequalIteratorsDifferentElements() {
        Expression left = ExpressionFactory.createVariable("iterator1");
        Expression right = ExpressionFactory.createVariable("iterator2");
        EvalContext context = new EvalContext();
        context.setVariable("iterator1", new ArrayList<>().iterator());
        context.setVariable("iterator2", new ArrayList<>().iterator());
        context.getVariable("iterator1").next();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertFalse(coreOperationCompare.compare(context, null));
    }

    @Test
    public void testEqualInitialContexts() {
        Expression left = ExpressionFactory.createVariable("initialContext1");
        Expression right = ExpressionFactory.createVariable("initialContext2");
        EvalContext context = new EvalContext();
        InitialContext initialContext1 = new InitialContext(context);
        InitialContext initialContext2 = new InitialContext(context);

        context.setVariable("initialContext1", initialContext1);
        context.setVariable("initialContext2", initialContext2);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertTrue(coreOperationCompare.compare(context, null));
    }

    @Test
    public void testUnequalSelfContexts() {
        Expression left = ExpressionFactory.createVariable("selfContext1");
        Expression right = ExpressionFactory.createVariable("selfContext2");
        EvalContext context = new EvalContext();
        SelfContext selfContext1 = new SelfContext(context, Pointer.createRoot("/"));
        SelfContext selfContext2 = new SelfContext(context, Pointer.createRoot("/"));

        context.setVariable("selfContext1", selfContext1);
        context.setVariable("selfContext2", selfContext2);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertTrue(coreOperationCompare.compare(context, null));
    }

    @Test
    public void testUnequalPointerValues() {
        Expression left = ExpressionFactory.createVariable("pointer1");
        Expression right = ExpressionFactory.createVariable("pointer2");
        EvalContext context = new EvalContext();
        Pointer pointer1 = Pointer.createRoot("/a/b/c");
        Pointer pointer2 = Pointer.createRoot("/a/b/d");

        context.setVariable("pointer1", pointer1);
        context.setVariable("pointer2", pointer2);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertFalse(coreOperationCompare.compare(context, null));
    }

    @Test
    public void testEqualPointerValues() {
        Expression left = ExpressionFactory.createVariable("pointer1");
        Expression right = ExpressionFactory.createVariable("pointer2");
        EvalContext context = new EvalContext();
        Pointer pointer1 = Pointer.createRoot("/a/b/c");
        Pointer pointer2 = Pointer.createRoot("/a/b/c");

        context.setVariable("pointer1", pointer1);
        context.setVariable("pointer2", pointer2);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);
        Assert.assertTrue(coreOperationCompare.compare(context, null));
    }
}