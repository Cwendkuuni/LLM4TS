import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationCompareTest {

 private Expression arg1;
 private Expression arg2;
 private CoreOperationCompare coreOperationCompare;
 private EvalContext context;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 context = Mockito.spy(EvalContext.class);
 coreOperationCompare = new CoreOperationCompare(arg1, arg2);
 }

 @Test
 public void testGetPrecedence() {
 assertTrue(coreOperationCompare.getPrecedence() == 2);
 }

 @Test
 public void testIsSymmetric() {
 assertTrue(coreOperationCompare.isSymmetric());
 }

 @Test
 public void testEqualWithInitialContext() {
 Mockito.when(arg1.compute(context)).thenReturn(new InitialContext());
 Mockito.when(arg2.compute(context)).thenReturn(new InitialContext());
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithSelfContext() {
 Mockito.when(arg1.compute(context)).thenReturn(new SelfContext(context, new Pointer("foo")));
 Mockito.when(arg2.compute(context)).thenReturn(new SelfContext(context, new Pointer("foo")));
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqualWithCollection() {
 Mockito.when(arg1.compute(context)).thenReturn(new HashSet<>());
 Mockito.when(arg2.compute(context)).thenReturn(new HashSet<>());
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));

 Mockito.when(arg1.compute(context)).thenReturn(new HashSet<>().add("a"));
 Mockito.when(arg2.compute(context)).thenReturn(new HashSet<>().add("a"));
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));

 Mockito.when(arg1.compute(context)).thenReturn(new HashSet<>().add("a"));
 Mockito.when(arg2.compute(context)).thenReturn(new HashSet<>().add("b"));
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testContains() {
 Iterator<Object> it = Mockito.mock(Iterator.class);
 Mockito.when(arg1.compute(context)).thenReturn(it);
 Mockito.when(it.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
 Mockito.when(it.next()).thenReturn("a");

 Mockito.when(arg2.compute(context)).thenReturn("a");
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));

 Mockito.when(arg2.compute(context)).thenReturn("b");
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testFindMatch() {
 Iterator<Object> it1 = Mockito.mock(Iterator.class);
 Iterator<Object> it2 = Mockito.mock(Iterator.class);
 HashSet<Object> set = new HashSet<>();

 Mockito.when(arg1.compute(context)).thenReturn(it1);
 Mockito.when(arg2.compute(context)).thenReturn(it2);

 Mockito.when(it1.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
 Mockito.when(it1.next()).thenReturn("a");
 Mockito.when(it2.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
 Mockito.when(it2.next()).thenReturn("a");

 Mockito.when(it1.iterator()).thenReturn(it1);
 Mockito.when(it2.iterator()).thenReturn(it2);

 Mockito.when(contains(it1, "a")).thenReturn(true);
 Mockito.when(contains(it1, "b")).thenReturn(false);
 Mockito.when(contains(it2, "a")).thenReturn(true);
 Mockito.when(contains(it2, "b")).thenReturn(false);

 Mockito.when(coreOperationCompare.findMatch(it1, it2)).thenCallRealMethod();
 assertTrue(coreOperationCompare.findMatch(it1, it2));
 }

 private boolean contains(Iterator<?> it, Object value) {
 // Delegate to the real method
 return coreOperationCompare.contains(it, value);
 }

}