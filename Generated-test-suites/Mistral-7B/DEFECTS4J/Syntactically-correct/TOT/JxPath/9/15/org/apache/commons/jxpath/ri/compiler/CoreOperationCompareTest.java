import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class CoreOperationCompareTest {

 private CoreOperationCompare operation;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 context = PowerMockito.mock(EvalContext.class);
 operation = new CoreOperationCompare(arg1, arg2);
 }

 @Test
 public void testEqual_sameValues() {
 Object value1 = "test";
 Object value2 = "test";
 PowerMockito.when(arg1.compute(context)).thenReturn(value1);
 PowerMockito.when(arg2.compute(context)).thenReturn(value2);
 assertTrue(operation.eval(context));
 }

 @Test
 public void testEqual_differentValues() {
 Object value1 = 1;
 Object value2 = 2;
 PowerMockito.when(arg1.compute(context)).thenReturn(value1);
 PowerMockito.when(arg2.compute(context)).thenReturn(value2);
 assertFalse(operation.eval(context));
 }

 @Test
 public void testEqual_nullAndNotNull() {
 PowerMockito.when(arg1.compute(context)).thenReturn(null);
 PowerMockito.when(arg2.compute(context)).thenReturn("test");
 assertFalse(operation.eval(context));

 PowerMockito.when(arg1.compute(context)).thenReturn("test");
 PowerMockito.when(arg2.compute(context)).thenReturn(null);
 assertFalse(operation.eval(context));
 }

 @Test
 public void testEqual_booleanValues() {
 PowerMockito.when(arg1.compute(context)).thenReturn(true);
 PowerMockito.when(arg2.compute(context)).thenReturn(true);
 assertTrue(operation.eval(context));

 PowerMockito.when(arg1.compute(context)).thenReturn(true);
 PowerMockito.when(arg2.compute(context)).thenReturn(false);
 assertFalse(operation.eval(context));
 }

 @Test
 public void testEqual_booleanAndString() {
 PowerMockito.when(arg1.compute(context)).thenReturn(true);
 PowerMockito.when(arg2.compute(context)).thenReturn("test");
 assertFalse(operation.eval(context));
 }

 @Test
 public void testEqual_differentDataTypes() {
 PowerMockito.when(arg1.compute(context)).thenReturn(1.0);
 PowerMockito.when(arg2.compute(context)).thenReturn("test");
 assertFalse(operation.eval(context));
 }

 @Test(expected = EvaluationException.class)
 public void testEqual_oneSideThrowsException() throws EvaluationException {
 PowerMockito.when(arg1.compute(context)).thenThrow(new EvaluationException());
 assertNull(operation.eval(context));
 }

 @Test
 public void testEqual_oneSideNull() {
 PowerMockito.when(arg1.compute(context)).thenReturn(null);
 PowerMockito.when(arg2.compute(context)).thenReturn("test");
 assertFalse(operation.eval(context));
 }

 @Test
 public void testEquals_Collections() {
 List<String> list1 = new ArrayList<>(Arrays.asList("test1", "test2"));
 List<String> list2 = new ArrayList<>(Arrays.asList("test1", "test3"));
 PowerMockito.when(arg1.compute(context)).thenReturn(list1.iterator());
 PowerMockito.when(arg2.compute(context)).thenReturn(list2.iterator());
 assertFalse(operation.eval(context));
 }

 @Test
 public void testEquals_CollectionsMatch() {
 List<String> list1 = new ArrayList<>(Arrays.asList("test1", "test2"));
 List<String> list2 = new ArrayList<>(Arrays.asList("test1", "test2"));PowerMockito.when(arg1.compute(context)).thenReturn(list1.iterator());
 PowerMockito.when(arg2.compute(context)).thenReturn(list2.iterator());
 assertTrue(operation.eval(context));
 }

 @Test
 public void testEquals_CollectionsWithDuplicates() {
 List<String> list1 = new ArrayList<>(Collections.singletonList("test"));List<String> list2 = new ArrayList<>(Arrays.asList("test1", "test", "test2"));
 PowerMockito.when(arg1.compute(context)).thenReturn(list1.iterator());
 PowerMockito.when(arg2.compute(context)).thenReturn(list2.iterator());
 assertFalse(operation.eval(context));
 }

 @Test
 public void testEquals_CollectionsWithNull() {
 List<String> list1 = new ArrayList<>(Arrays.asList("test1", null));
 List<String> list2 = new ArrayList<>(Arrays.asList("test1", "test2"));
 PowerMockito.when(arg1.compute(context)).thenReturn(list1.iterator());
 PowerMockito.when(arg2.compute(context)).thenReturn(list2.iterator());
 assertFalse(operation.eval(context));
 }

 @Test
 public void testEquals_EmptyCollections() {
 Iterator<Object> iterator1 = Collections.emptyIterator();
 Iterator<Object> iterator2 = Arrays.asList("test1", "test2").iterator();
 PowerMockito.when(arg1.compute(context)).thenReturn(iterator1);
 PowerMockito.when(arg2.compute(context)).thenReturn(iterator2);
 assertFalse(operation.eval(context));
 }

 @Test
 public void testEquals_EmptyCollectionsMatch() {
 Iterator<Object> iterator1 = Collections.emptyIterator();
 Iterator<Object> iterator2 = Collections.emptyIterator();
 PowerMockito.when(arg1.compute(context)).thenReturn(iterator1);
 PowerMockito.when(arg2.compute(context)).thenReturn(iterator2);
 assertTrue(operation.eval(context));
 }
}