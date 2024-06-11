import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

 private EvalContext context;
 private CoreOperationCompare coreOperationCompare;
 private Expression arg1;
 private Expression arg2;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 context = mock(EvalContext.class);
 arg1 = mock(Expression.class);
 arg2 = mock(Expression.class);
 coreOperationCompare = new CoreOperationCompare(arg1, arg2);
 }

 @Test
 public void testEqual() {
 Object l = new Object();
 Object r = l;
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);

 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithDifferentObjects() {
 Object l = new Object();
 Object r = new Object();
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);

 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithNull() {
 Object l = null;
 Object r = null;
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);

 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithIterators() {
 Object[] l = { 1, 2, 3 };
 Object[] r = { 1, 2, 3 };
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);

 Iterator it = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return null;
 }
 };
 when(arg1.iterator()).thenReturn(it);
 when(arg2.iterator()).thenReturn(it);

 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testContains() {
 Object[] l = { 1, 2, 3 };
 Object value = 1;
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(value);

 Iterator it = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return null;
 }
 };
 when(arg1.iterator()).thenReturn(it);

 assertTrue(coreOperationCompare.contains(it, value));
 }

 @Test
 public void testFindMatch() {
 Object[] l = { 1, 2, 3 };
 Object[] r = { 1, 2, 3 };
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);

 Iterator lit = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return null;
 }
 };
 when(arg1.iterator()).thenReturn(lit);

 Iterator rit = new Iterator() {
 @Override
 public boolean hasNext() {
 return true;
 }

 @Override
 public Object next() {
 return null;
 }
 };
 when(arg2.iterator()).thenReturn(rit);

 HashSet left = new HashSet();
 left.add(1);
 left.add(2);
 left.add(3);

 assertTrue(coreOperationCompare.findMatch(lit, rit));
 }

 @Test
 public void testEqualWithPointer() {
 Pointer l = new Pointer("1");
 Pointer r = new Pointer("1");
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);

 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithDifferentPointer() {
 Pointer l = new Pointer("1");
 Pointer r = new Pointer("2");
 when(arg1.compute(context)).thenReturn(l);
 when(arg2.compute(context)).thenReturn(r);

 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }
}