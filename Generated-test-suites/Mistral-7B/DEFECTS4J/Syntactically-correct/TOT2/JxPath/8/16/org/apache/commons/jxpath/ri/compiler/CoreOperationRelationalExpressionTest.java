package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreOperation[] { new CoreOperation(1), new CoreOperation(2) });
        Object left = Boolean.TRUE;
        Object right = Boolean.FALSE;
        EvalContext context = new EvalContext();

        boolean result = expr.computeValue(context).equals(Boolean.FALSE);
        assert result;

        left = Boolean.FALSE;
        right = Boolean.TRUE;
        result = expr.computeValue(context).equals(Boolean.FALSE);
        assert result;

        left = Boolean.TRUE;
        right = Boolean.TRUE;
        result = expr.computeValue(context).equals(Boolean.TRUE);
        assert result;
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreOperation[] {});
        int precedence = expr.getPrecedence();
        assert precedence == 3;
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression(new CoreOperation[] {});
        boolean result = expr.isSymmetric();
        assert result == false;
    }

    @Test
    public void testEvaluateCompareInt() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        boolean result = expr.evaluateCompare(2);
        assert result;

        result = expr.evaluateCompare(0);
        assert result == false;

        result = expr.evaluateCompare(-1);
        assert result;
    }

    @Test(expected = NullPointerException.class)
    public void testEvaluateCompareNullLeft() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(Object left, Object right) {
                return left != null && left instanceof Number && right != null && right instanceof Number ? ((Number) left).doubleValue() > ((Number) right).doubleValue() : false;
            }
        };

        expr.evaluateCompare(null, new Integer(1));
    }

    @Test(expected = NullPointerException.class)
    public void testEvaluateCompareNullRight() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(Object left, Object right) {
                return left != null && left instanceof Number && right != null && right instanceof Number ? ((Number) left).doubleValue() > ((Number) right).doubleValue() : false;
            }
        };

        expr.evaluateCompare(new Integer(1), null);
    }

    @Test
    public void testEvaluateCompareEmptyIterators() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(Object left, Object right) {
                return left instanceof Iterator && right instanceof Iterator ? ((Iterator) left).hasNext() != ((Iterator) right).hasNext() : false;
            }
        };

        boolean result = expr.evaluateCompare(new HashSet<Object>().iterator(), new HashSet<Object>().iterator());
        assert result;
    }

    @Test
    public void testEvaluateCompareSameElementsDifferentOrder() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(Object left, Object right) {
                return left instanceof Iterator && right instanceof Iterator && containsMatch((Iterator) left, (Iterator) right);
            }
        };

        boolean result = expr.evaluateCompare(new HashSet<Object>() {{add(1); add(2);}}, new HashSet<Object>() {{add(2); add(1);}});
        assert result;
    }

    @Test
    public void testEvaluateCompareDifferentTypes() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(Object left, Object right) {
                return left instanceof Number && right instanceof Number && ((Number) left).doubleValue() > ((Number) right).doubleValue();
            }
        };

        boolean result = expr.evaluateCompare(new Integer(1), new String("2"));
        assert result == false;
    }

    @Test
    public void testEvaluateCompareInitialContexts() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(Object left, Object right) {
                if (left instanceof InitialContext && right instanceof InitialContext) {
                    return ((InitialContext) left).getSize() > ((InitialContext) right).getSize();
                }
                return false;
            }
        };

        boolean result = expr.evaluateCompare(new InitialContext(), new InitialContext());
        assert result;
    }

    @Test
    public void testEvaluateCompareCollections() {
        CoreOperationRelationalExpression expr = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(Object left, Object right) {
                if (left instanceof Collection && right instanceof Collection) {
                    return ((Collection) left).size() > ((Collection) right).size();
                }
                return false;
            }
        };

        boolean result = expr.evaluateCompare(new HashSet<Object>(), new ArrayList<Object>());
        assert result;
    }

    private boolean containsMatch(Iterator<Object> it1, Iterator<Object> it2) {
        while (it1.hasNext()) {
            Object element1 = it1.next();
            while (it2.hasNext()) {
                Object element2 = it2.next();
                if (compute(element1, element2)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean compute(Object left, Object right) {
        left = reduce(left);
        right = reduce(right);

        if (left instanceof InitialContext) {
            ((InitialContext) left).reset();
        }
        if (right instanceof InitialContext) {
            ((InitialContext) right).reset();
        }
        if (left instanceof Iterator && right instanceof Iterator) {
            return findMatch((Iterator<Object>) left, (Iterator<Object>) right);
        }
        if (left instanceof Iterator) {
            return containsMatch((Iterator<Object>) left, right);
        }
        if (right instanceof Iterator) {
            return containsMatch((Iterator<Object>) right, left);
        }
        double ld = InfoSetUtil.doubleValue(left);
        double rd = InfoSetUtil.doubleValue(right);
        return evaluateCompare(ld == rd ? 0 : ld < rd ? -1 : 1);
    }

    private Object reduce(Object o) {
        if (o instanceof SelfContext) {
            o = ((EvalContext) o).getSingleNodePointer();
        }
        if (o instanceof Collection) {
            o = ((Collection) o).iterator();
        }
        return o;
    }

    private boolean findMatch(Iterator<Object> lit, Iterator<Object> rit) {
        HashSet<Object> left = new HashSet<>();
        while (lit.hasNext()) {
            left.add(lit.next());
        }
        while (rit.hasNext()) {
            if (containsMatch(left.iterator(), rit.next())) {
                return true;
            }
        }
        return false;
    }

    private boolean containsMatch(Iterator<Object> it, Object value) {
        while (it.hasNext()) {
            Object element = it.next();
            if (compute(element, value)) {
                return true;
            }
        }
        return false;
    }
}