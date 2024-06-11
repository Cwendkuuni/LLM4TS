import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testGT() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new Double(2.0)),
                new CoreConstantExpression(new Double(1.0))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertTrue(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testGTWithSameValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new Double(2.0)),
                new CoreConstantExpression(new Double(2.0))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertFalse(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testGTE() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new Double(2.0)),
                new CoreConstantExpression(new Double(2.0))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertTrue(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testLTE() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new Double(2.0)),
                new CoreConstantExpression(new Double(2.0))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertTrue(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testLT() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new Double(2.0)),
                new CoreConstantExpression(new Double(1.0))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertTrue(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testDoubleComparison() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new Double(2.1)),
                new CoreConstantExpression(new Double(1.5))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertTrue(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testDoubleComparisonWithSameValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new Double(2.1)),
                new CoreConstantExpression(new Double(2.1))
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertFalse(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testIteratorsComparison() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new InitialContext()),
                new CoreConstantExpression(new ArrayList<Object>() {{
                            add(new Double(2.0));
                            add(new Double(1.0));
                        }})
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertTrue(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testIteratorsComparisonWithSameValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new InitialContext()),
                new CoreConstantExpression(new ArrayList<Object>() {{
                            add(new Double(2.0));
                            add(new Double(2.0));
                        }})
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertFalse(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testIteratorsComparisonWithMatches() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new ArrayList<Object>() {{
                            add(new Double(2.0));
                            add(new Double(1.5));
                        }}),
                new CoreConstantExpression(new ArrayList<Object>() {{
                            add(new Double(1.5));
                            add(new Double(2.0));
                        }})
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertTrue(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testIteratorsComparisonWithNoMatches() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new ArrayList<Object>() {{
                            add(new Double(2.0));
                            add(new Double(1.0));
                        }}),
                new CoreConstantExpression(new ArrayList<Object>() {{
                            add(new Double(3.0));
                            add(new Double(2.0));
                        }})
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertFalse(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testIteratorsComparisonWithContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new ArrayList<Object>() {{
                            addAll(Collections.singletonList(new Double(2.0)));
                            addAll(Collections.singletonList(new Double(1.5)));
                        }}),
                new CoreConstantExpression(new ArrayList<Object>() {{
                            addAll(Collections.singletonList(new Double(1.5)));
                        }})
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertTrue(expression.computeValue(context) == Boolean.TRUE);
    }

    @Test
    public void testIteratorsComparisonWithNoContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreOperation[]{
                new CoreConstantExpression(new ArrayList<Object>() {{
                            addAll(Collections.singletonList(new Double(2.0)));
                            addAll(Collections.singletonList(new Double(1.0)));
                        }}),
                new CoreConstantExpression(new ArrayList<Object>() {{
                            addAll(Collections.singletonList(new Double(3.0)));
                        }})
        }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext(new Object[]{});
        assertFalse(expression.computeValue(context) == Boolean.TRUE);
    }
}