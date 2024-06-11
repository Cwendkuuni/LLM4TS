import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.jxpath.ri.compiler.CoreExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.junit.Test;
import org.mockito.Mockito;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testEmptyConstructor() { CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[0]); assertNull(expression.getLeftExpression()); assertNull(expression.getRightExpression()); }

    @Test
    public void testConstructorWithArguments() { CoreExpression leftExp = Mockito.mock(CoreExpression.class); CoreExpression rightExp = Mockito.mock(CoreExpression.class); CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp }); assertSame(leftExp, expression.getLeftExpression()); assertSame(rightExp, expression.getRightExpression()); }

    @Test
    public void testPrecedence() { CoreExpression leftExp = Mockito.mock(CoreExpression.class); CoreExpression rightExp = Mockito.mock(CoreExpression.class); CoreOperationRelationalExpression expression1 = new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp }) { protected String getOperatorSymbol() { return ">"; } }; CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp }) { protected String getOperatorSymbol() { return ">="; } }; CoreOperationRelationalExpression expression3 = new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp }) { protected String getOperatorSymbol() { return "<"; } }; CoreOperationRelationalExpression expression4 = new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp }) { protected String getOperatorSymbol() { return "<="; } }; assertEquals(3, expression1.getPrecedence()); assertEquals(3, expression2.getPrecedence()); assertEquals(3, expression3.getPrecedence()); assertEquals(3, expression4.getPrecedence()); }

    @Test
    public void testIsSymmetric() { CoreOperationRelationalExpression expression = Mockito.mock(CoreOperationRelationalExpression.class); when(expression.isSymmetric()).thenReturn(false); assertFalse(expression.isSymmetric()); }

    @Test
    public void testDifferentExpressionTypes() { CoreExpression leftExp = Mockito.mock(CoreNumberExpression.class); CoreExpression rightExp = Mockito.mock(CoreVariableExpression.class); CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp }); assertFalse(expression.isSymmetric()); }

    @Test
    public void testPrecedenceWithHigherLeftExpression() { CoreExpression leftExp = Mockito.mock(CoreExpression.class); leftExp.precedence = 5; CoreExpression rightExp = Mockito.mock(CoreExpression.class); CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp }); assertEquals(5, expression.getPrecedence()); }

    @Test
    public void testNullLeftExpression() { CoreExpression leftExp = null; CoreExpression rightExp = Mockito.mock(CoreExpression.class); assertThrows(NullPointerException.class, () -> new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp })); }

    @Test
    public void testDifferentOperators() { CoreExpression leftExp = Mockito.mock(CoreExpression.class); CoreExpression rightExp = Mockito.mock(CoreExpression.class); CoreOperationRelationalExpression expression1 = new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp }) { protected String getOperatorSymbol() { return ">"; } }; CoreOperationRelationalExpression expression2 = new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp }) { protected String getOperatorSymbol() { return ">="; } }; CoreOperationRelationalExpression expression3 = new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp }) { protected String getOperatorSymbol() { return "<"; } }; CoreOperationRelationalExpression expression4 = new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp }) { protected String getOperatorSymbol() { return "<="; } }; assertEquals(3, expression1.getPrecedence()); assertEquals(3, expression2.getPrecedence()); assertEquals(3, expression3.getPrecedence()); assertEquals(3, expression4.getPrecedence()); }

    @Test
    public void testIllegalArgumentExceptionWithInvalidNumberOfArguments() { CoreExpression[] args = new CoreExpression[3]; CoreOperationRelationalExpression expression = assertThrows(IllegalArgumentException.class, () -> new CoreOperationRelationalExpression(args)); }

    @Test
    public void testIllegalArgumentExceptionWithInvalidLeftExpressionType() { CoreExpression leftExp = Mockito.mock(String.class); CoreExpression rightExp = Mockito.mock(CoreExpression.class); assertThrows(IllegalArgumentException.class, () -> new CoreOperationRelationalExpression(new CoreExpression[] { leftExp, rightExp })); }
}