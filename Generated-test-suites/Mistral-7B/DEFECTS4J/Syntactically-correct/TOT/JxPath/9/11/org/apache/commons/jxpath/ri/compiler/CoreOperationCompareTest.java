import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.Variable;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testEqualBoolean() {
        Expression left = new Variable("left");
        Expression right = new Constant(true);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Constant(true));
        context.setVariable("left", new Constant(true));

        boolean result = compare.evaluate(context);
        assertTrue(result);

        context.setVariable("left", new Constant(false));
        result = compare.evaluate(context);
        assertFalse(result);
    }

    @Test
    public void testEqualNumber() {
        Expression left = new Variable("left");
        Expression right = new Constant(1.0);
        CoreOperationCompare compare = new CoreOperationCompare(left, right);

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Constant(1.0));
        context.setVariable("left", new Variable(0));

        context.setVariable("left", new Constant(1.0));

        boolean result = compare.evaluate(context);
        assertTrue(result);

        context.setVariable("left", new Constant(2.0));
        result = compare.evaluate(context);
        assertFalse(result);
    }

    @Test
    public void testEqualString() {
        Expression left = new Variable("left");
        Expression right = new Constant("test");
        CoreOperationCompare compare = new CoreOperationCompare(left, right);

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Constant("test"));
        context.setVariable("left", new Variable("test"));

        boolean result = compare.evaluate(context);
        assertTrue(result);

        context.setVariable("left", new Variable("anotherString"));
        result = compare.evaluate(context);
        assertFalse(result);
    }

    @Test
    public void testEqualCollection1() {
        Expression left = new Variable("left");
        Expression right = new Constant(Collections.singletonList(1));
        CoreOperationCompare compare = new CoreOperationCompare(left, right);

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Variable("list"));
        context.setVariable("left", new Variable("list"));

        List list = Collections.singletonList(1);
        context.setVariable("list", list);

        boolean result = compare.evaluate(context);
        assertTrue(result);

        context.setVariable("list", Collections.singletonList(2));
        result = compare.evaluate(context);
        assertFalse(result);
    }

    @Test
    public void testEqualCollection2() {
        Expression left = new Variable("left");
        Expression right = new Variable("right");

        CoreOperationCompare compare = new CoreOperationCompare(left, right);

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Variable("list1"));
        context.setVariable("left", new Variable("list1"));
        context.setVariable("right", new Variable("list2"));

        List list1 = Collections.singletonList(1);
        List list2 = Collections.singletonList(1);
        context.setVariable("list1", list1);
        context.setVariable("list2", list2);

        boolean result = compare.evaluate(context);
        assertTrue(result);

        list1 = Collections.singletonList(2);
        list2 = Collections.singletonList(1);
        context.setVariable("list1", list1);
        context.setVariable("list2", list2);
        result = compare.evaluate(context);
        assertFalse(result);
    }

    @Test
    public void testEqualCollection3() {
        Expression left = new Variable("left");
        Expression right = new Variable("right");

        CoreOperationCompare compare = new CoreOperationCompare(left, right);

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Variable("list1"));
        context.setVariable("left", new Variable("list1"));
        context.setVariable("right", new Variable("list2"));

        List list1 = Collections.singletonList(1);
        List list2 = Collections.singletonList(1);
        context.setVariable("list1", list1);
        context.setVariable("list2", list2);

        boolean result = compare.evaluate(context);
        assertTrue(result);

        list1 = Collections.singletonList(1, 2);
        list2 = Collections.singletonList(1);
        context.setVariable("list1", list1);
        context.setVariable("list2", list2);
        result = compare.evaluate(context);
        assertFalse(result);
    }

    @Test
    public void testNotEqualBoolean() {
        Expression left = new Variable("left");
        Expression right = new Constant(true);
        CoreOperationCompare notEqual = new CoreOperationCompare(left, right);
        notEqual = new CoreOperationCompare(notEqual, new Constant(false));

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Constant(true));
        context.setVariable("left", new Constant(true));

        boolean result = notEqual.evaluate(context);
        assertFalse(result);

        context.setVariable("left", new Constant(false));
        result = notEqual.evaluate(context);
        assertTrue(result);
    }

    @Test
    public void testNotEqualNumber() {
        Expression left = new Variable("left");
        Expression right = new Constant(1.0);
        CoreOperationCompare notEqual = new CoreOperationCompare(left, right);

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Constant(2.0));
        context.setVariable("left", new Variable(0));

        boolean result = notEqual.evaluate(context);
        assertTrue(result);

        context.setVariable("left", new Constant(1.0));
        result = notEqual.evaluate(context);
        assertFalse(result);
    }

    @Test
    public void testNotEqualString() {
        Expression left = new Variable("left");
        Expression right = new Constant("test");
        CoreOperationCompare notEqual = new CoreOperationCompare(left, right);

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Variable("test2"));
        context.setVariable("left", new Variable("test"));

        boolean result = notEqual.evaluate(context);
        assertTrue(result);

        context.setVariable("left", new Variable("test"));
        result = notEqual.evaluate(context);
        assertFalse(result);
    }

    @Test
    public void testNotEqualCollection1() {
        Expression left = new Variable("left");
        Expression right = new Constant(Collections.singletonList(1));
        CoreOperationCompare notEqual = new CoreOperationCompare(left, right);

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Variable("list"));
        context.setVariable("left", new Variable("list"));

        List list = Collections.singletonList(2);
        context.setVariable("list", list);

        boolean result = notEqual.evaluate(context);
        assertTrue(result);

        list = Collections.singletonList(1);
        context.setVariable("list", list);
        result = notEqual.evaluate(context);
        assertFalse(result);
    }

    @Test
    public void testNotEqualCollection2() {
        Expression left = new Variable("left");
        Expression right = new Variable("right");

        CoreOperationCompare notEqual = new CoreOperationCompare(left, right);
        notEqual = new CoreOperationCompare(notEqual, new Constant(false));

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Variable("list1"));
        context.setVariable("left", new Variable("list1"));
        context.setVariable("right", new Variable("list2"));

        List list1 = Collections.singletonList(1);
        List list2 = Collections.singletonList(1);
        context.setVariable("list1", list1);
        context.setVariable("list2", list2);

        boolean result = notEqual.evaluate(context);
        assertFalse(result);

        list1 = Collections.singletonList(1, 2);
        list2 = Collections.singletonList(1);
        context.setVariable("list1", list1);
        context.setVariable("list2", list2);
        result = notEqual.evaluate(context);
        assertTrue(result);
    }

    @Test
    public void testNotEqualCollection3() {
        Expression left = new Variable("left");
        Expression right = new Variable("right");

        CoreOperationCompare notEqual = new CoreOperationCompare(left, right);
        notEqual = new CoreOperationCompare(notEqual, new Constant(false));

        EvaluationContext context = new JXPathContext();
        context.setContextItem(new Variable("list1"));
        context.setVariable("left", new Variable("list1"));
        context.setVariable("right", new Variable("list2"));

        List list1 = Collections.singletonList(1);
        List list2 = Collections.singletonList(1);
        context.setVariable("list1", list1);
        context.setVariable("list2", list2);

        boolean result = notEqual.evaluate(context);
        assertFalse(result);

        list1 = Collections.singletonList(1, 2);
        list2 = Collections.singletonList(1);
        context.setVariable("list1", list1);
        context.setVariable("list2", list2);
        result = notEqual.evaluate(context);
        assertTrue(result);

        list1 = Collections.singletonList(1);
        list2 = Collections.emptyList();
        context.setVariable("list1", list1);
        context.setVariable("list2", list2);
        result = notEqual.evaluate(context);
        assertTrue(result);
    }
}