package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

@Test
public void testConstructor() {
Expression arg1 = new Expression() {};
Expression arg2 = new Expression() {};
CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
assertNotNull(coreOperationLessThan);
}

@Test
public void testComputeValue() {
EvalContext context = new EvalContext();
Expression arg1 = new Expression() {
@Override
public Object computeValue(EvalContext context) {
return 3.5;
}
};
Expression arg2 = new Expression() {
@Override
public Object computeValue(EvalContext context) {
return 5.5;
}
};
CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
double result = coreOperationLessThan.computeValue(context).booleanValue();
assertEquals(false, result);

arg1 = new Expression() {
@Override
public Object computeValue(EvalContext context) {
return 2.5;
}
};
arg2 = new Expression() {
@Override
public Object computeValue(EvalContext context) {
return 3.5;
}
};
coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
result = coreOperationLessThan.computeValue(context).booleanValue();
assertEquals(true, result);
}

@Test
public void testGetSymbol() {
CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(new Expression() {}, new Expression() {});
assertEquals("<", coreOperationLessThan.getSymbol());
}

@Test
public void testNaNInputs() {
EvalContext context = new EvalContext();
Expression arg1 = new Expression() {
@Override
public Object computeValue(EvalContext context) {
return Double.NaN;
}
};
Expression arg2 = new Expression() {
@Override
public Object computeValue(EvalContext context) {
return 5.5;
}
};
CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
assertNull(coreOperationLessThan.computeValue(context));
}

@Test
public void testInfiniteInputs() {
EvalContext context = new EvalContext();
Expression arg1 = new Expression() {
@Override
public Object computeValue(EvalContext context) {
return Double.POSITIVE_INFINITY;
}
};
Expression arg2 = new Expression() {
@Override
public Object computeValue(EvalContext context) {
return 3.5;
}
};
CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
assertFalse(coreOperationLessThan.computeValue(context).booleanValue());

arg1 = new Expression() {
@Override
public Object computeValue(EvalContext context) {
return Double.NEGATIVE_INFINITY;
}
};
arg2 = new Expression() {
@Override
public Object computeValue(EvalContext context) {
return Double.NEGATIVE_INFINITY;
}
};
coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
assertFalse(coreOperationLessThan.computeValue(context).booleanValue());
}

@Test
public void testNullInputs() {
EvalContext context = new EvalContext();
Expression arg1 = new Expression() {};
Expression arg2 = null;
CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
assertNull(coreOperationLessThan.computeValue(context));

arg1 = null;
coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);
assertNull(coreOperationLessThan.computeValue(context));
}
}