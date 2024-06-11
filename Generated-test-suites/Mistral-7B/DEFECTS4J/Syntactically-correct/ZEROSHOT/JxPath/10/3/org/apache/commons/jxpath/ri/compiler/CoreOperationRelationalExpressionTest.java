import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.Iterator;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.util.Iterators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationRelationalExpressionTest {

    private CompilerContext compilerContext;
    private EvalContext evalContext;
    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        compilerContext = Mockito.mock(CompilerContext.class);
        evalContext = Mockito.mock(EvalContext.class);
        expression = new CoreOperationRelationalExpression(new CoreExpression[] { Mockito.mock(CoreExpression.class), Mockito.mock(CoreExpression.class) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
    }

    @Test
    public void testComputeValue() {
        NodeInfo leftNode = Mockito.mock(NodeInfo.class);
        NodeInfo rightNode = Mockito.mock(NodeInfo.class);
        InfoSet infoSet = Mockito.mock(InfoSet.class);

        Mockito.when(leftNode.getValue()).thenReturn(1.0);
        Mockito.when(rightNode.getValue()).thenReturn(2.0);
        Mockito.when(evalContext.getInfoSet()).thenReturn(infoSet);

        Mockito.when(compilerContext.getEvaluationContext(Mockito.anyArray(Class.class))).thenReturn(evalContext);
        Mockito.when(compilerContext.getInfoSet(Mockito.any())).thenReturn(infoSet);

        Object left = new InitialContext(compilerContext, new Object[] { leftNode });
        Object right = new InitialContext(compilerContext, new Object[] { rightNode });

        Mockito.when(left.computeValue(evalContext)).thenReturn(leftNode);
        Mockito.when(right.computeValue(evalContext)).thenReturn(rightNode);

        Object result = expression.computeValue(compilerContext);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWhenLeftIsCollection() {
        Iterator leftIterator = Iterators.emptyIterator();
        Iterator rightIterator = Iterators.emptyIterator();

        expression = new CoreOperationRelationalExpression(new CoreExpression[] { Mockito.mock(CoreExpression.class), Mockito.mock(CoreExpression.class) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Mockito.when(evalContext.getInfoSet()).thenReturn(Mockito.mock(InfoSet.class));

        Object left = new InitialContext(compilerContext, new Object[] { leftIterator });
        Object right = new InitialContext(compilerContext, new Object[] { rightIterator });

        assertTrue(expression.compute(left, right));
    }

    @Test
    public void testComputeValueWhenRightIsCollection() {
        Iterator leftIterator = Iterators.emptyIterator();
        Iterator rightIterator = Iterators.emptyIterator();

        expression = new CoreOperationRelationalExpression(new CoreExpression[] { Mockito.mock(CoreExpression.class), Mockito.mock(CoreExpression.class) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Mockito.when(evalContext.getInfoSet()).thenReturn(Mockito.mock(InfoSet.class));

        Object left = new InitialContext(compilerContext, new Object[] { leftIterator });
        Object right = new InitialContext(compilerContext, new Object[] { rightIterator });

        assertFalse(expression.compute(left, right));
    }

    @Test
    public void testComputeValueWhenBothAreCollections() {
        Iterator leftIterator = Iterators.emptyIterator();
        Iterator rightIterator = Iterators.emptyIterator();

        expression = new CoreOperationRelationalExpression(new CoreExpression[] { Mockito.mock(CoreExpression.class), Mockito.mock(CoreExpression.class) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Mockito.when(evalContext.getInfoSet()).thenReturn(Mockito.mock(InfoSet.class));

        Object left = new InitialContext(compilerContext, new Object[] { leftIterator });
        Object right = new InitialContext(compilerContext, new Object[] { rightIterator });

        assertFalse(expression.compute(left, right));
    }

    @Test
    public void testComputeValueWhenLeftIsDouble() {
        double left = 1.0;
        double right = 2.0;

        Object result = expression.computeValue(compilerContext);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWhenRightIsDouble() {
        double left = 1.0;
        double right = 2.0;

        expression = new CoreOperationRelationalExpression(new CoreExpression[] { Mockito.mock(CoreExpression.class), Mockito.mock(CoreExpression.class) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object result = expression.computeValue(compilerContext);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWhenLeftIsDoubleAndRightIsCollection() {
        double left = 1.0;
        Iterator rightIterator = Iterators.emptyIterator();

        expression = new CoreOperationRelationalExpression(new CoreExpression[] { Mockito.mock(CoreExpression.class), Mockito.mock(CoreExpression.class) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object result = expression.computeValue(compilerContext);

        assertNotNull(result);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWhenLeftIsCollectionAndRightIsDouble() {
        Iterator leftIterator = Iterators.emptyIterator();
        double right = 2.0;

        expression = new CoreOperationRelationalExpression(new CoreExpression[] { Mockito.mock(CoreExpression.class), Mockito.mock(CoreExpression.class) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object result = expression.computeValue(compilerContext);

        assertNotNull(result);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWhenBothAreDoublesAndReturnTrue() {
        double left = 1.0;
        double right = 2.0;

        expression = new CoreOperationRelationalExpression(new CoreExpression[] { Mockito.mock(CoreExpression.class), Mockito.mock(CoreExpression.class) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };

        Object result = expression.computeValue(compilerContext);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWhenBothAreDoublesAndReturnFalse() {
        double left = 1.0;
        double right = 2.0;

        expression = new CoreOperationRelationalExpression(new CoreExpression[] { Mockito.mock(CoreExpression.class), Mockito.mock(CoreExpression.class) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        Object result = expression.computeValue(compilerContext);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWhenBothAreDoublesAndReturnTrue() {
        double left = 1.0;
        double right = 1.0;

        expression = new CoreOperationRelationalExpression(new CoreExpression[] { Mockito.mock(CoreExpression.class), Mockito.mock(CoreExpression.class) }) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };

        Object result = expression.computeValue(compilerContext);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();

        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean isSymmetric = expression.isSymmetric();

        assertFalse(isSymmetric);
    }
}