import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.apache.commons.jxpath.ri.Expression;
import org.apache.commons.jxpath.ri.ITERATOR_SEQUENCE;
import org.apache.commons.jxpath.ri.iter.ArrayIter;
import org.apache.commons.jxpath.ri.iter.SingletonIter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CoreOperationCompareTest {

    private Expression left;
    private Expression right;
    private CoreOperationCompare operation;

    public CoreOperationCompareTest(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Parameterized.Parameters(name = "{index}: compare({0}, {1})")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new Const(1), new Const(1)},
                {new Const(1), new Const(2)},
                {new Const("str1"), new Const("str1")},
                {new Const("str1"), new Const("str2")},
                {new Variable("$x"), new Const("true")},
                {new Variable("$x"), new Const("false")},
                {new Variable("$x"), new Variable("$y")},
                {new ArrayIter(new SingletonIter(new Const(1))), new ArrayIter(new SingletonIter(new Const(1)))},
                {new ArrayIter(new SingletonIter(new Const(1))), new ArrayIter(new SingletonIter(new Const(2)))},
                {new ArrayIter(new SingletonIter(new Const("str1"))), new ArrayIter(new SingletonIter(new Const("str1")))},
                {new ArrayIter(new SingletonIter(new Const("str1"))), new ArrayIter(new SingletonIter(new Const("str2")))},
                {new IteratorNumber("1"), new IteratorNumber("1")},
                {new IteratorNumber("1"), new IteratorNumber("2")},
                {new IteratorString("str1"), new IteratorString("str1")},
                {new IteratorString("str1"), new IteratorString("str2")},
                {new VarRef("$x"), new VarRef("$x")},
                {new VarRef("$x"), new VarRef("$y")},
                {new Add(new Const(1), new Const(1)), new Add(new Const(1), new Const(1))},
                {new Add(new Const(1), new Const(1)), new Add(new Const(1), new Const(2))},
                {new Multiply(new Const(1), new Const(1)), new Multiply(new Const(1), new Const(1))},
                {new Multiply(new Const(1), new Const(1)), new Multiply(new Const(1), new Const(2))}
        });
    }

    @Test
    public void testEquals() throws EvaluationException {
        operation = new OperationEquals(left, right);
        JXPathContext context = new JXPathContext();
        assertTrue(operation.compute(context));
    }

    @Test
    public void testNotEquals() throws EvaluationException {
        operation = new OperationNotEquals(left, right);
        JXPathContext context = new JXPathContext();
        assertFalse(operation.compute(context));
    }

}

class Const extends Constant {
    Const(Object value) {
        super(value);
    }
}

class Variable extends VariableRef {
    Variable(String name) {
        super(name);
    }
}

class IteratorNumber extends Iterator {
    IteratorNumber(String index) {
        super(new Const(Integer.parseInt(index)));
    }
}

class IteratorString extends Iterator {
    IteratorString(String value) {
        super(new Const(value));
    }
}

class Add extends Arithmetic {
    Add(Expression left, Expression right) {
        super(left, right);
    }
}

class Multiply extends Arithmetic {
    Multiply(Expression left, Expression right) {
        super(left, right);
    }
}

class OperationEquals extends CoreOperationCompare {
    OperationEquals(Expression left, Expression right) {
        super(left, right);
    }
}

class OperationNotEquals extends CoreOperationCompare {
    OperationNotEquals(Expression left, Expression right) {
        super(left, right);
    }
}