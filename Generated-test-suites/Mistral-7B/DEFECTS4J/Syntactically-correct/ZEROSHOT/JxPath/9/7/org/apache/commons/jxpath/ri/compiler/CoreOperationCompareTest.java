import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class CoreOperationCompareTest {

 private CoreOperationCompare coreOperationCompare;
 private Expression arg1;
 private Expression arg2;
 private EvalContext context;

 @Before
 public void setup() {
 arg1 = Mockito.mock(Expression.class);
 arg2 = Mockito.mock(Expression.class);
 context = PowerMockito.mock(EvalContext.class);
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
 public void testEqual_SameObjects() {
 Mockito.when(arg1.compute(context)).thenReturn(new Object());
 Mockito.when(arg2.compute(context)).thenReturn(new Object());
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_DifferentObjectsButEqualValues() {
 Mockito.when(arg1.compute(context)).thenReturn("testString");
 Mockito.when(arg2.compute(context)).thenReturn("testString");
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_DifferentObjectsButNotEqualValues() {
 Mockito.when(arg1.compute(context)).thenReturn("testString1");
 Mockito.when(arg2.compute(context)).thenReturn("testString2");
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_DifferentObjectsButBothNull() {
 Mockito.when(arg1.compute(context)).thenReturn(null);
 Mockito.when(arg2.compute(context)).thenReturn(null);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_OneObjectIsPointer() {
 Pointer pointer1 = Mockito.mock(Pointer.class);
 Mockito.when(arg1.compute(context)).thenReturn(pointer1);
 Mockito.when(arg2.compute(context)).thenReturn(new Object());
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_BothObjectsArePointer() {
 Pointer pointer1 = Mockito.mock(Pointer.class);
 Pointer pointer2 = Mockito.mock(Pointer.class);
 Mockito.when(arg1.compute(context)).thenReturn(pointer1);
 Mockito.when(arg2.compute(context)).thenReturn(pointer2);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_OneObjectIsCollection() {
 List list1 = Arrays.asList("testString1", "testString2");
 Mockito.when(arg1.compute(context)).thenReturn(list1.iterator());
 Mockito.when(arg2.compute(context)).thenReturn(new Object());
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testEqual_BothObjectsAreCollections() {
 List list1 = Arrays.asList("testString1", "testString2");
 List list2 = Arrays.asList("testString1", "testString2");
 Mockito.when(arg1.compute(context)).thenReturn(list1.iterator());
 Mockito.when(arg2.compute(context)).thenReturn(list2.iterator());
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testContains_IteratorContainsValue() {
 Iterator iterator = Arrays.asList("testString1", "testString2").iterator();
 Mockito.when(arg1.compute(context)).thenReturn(iterator);
 Mockito.when(arg2.compute(context)).thenReturn("testString1");
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testContains_IteratorDoesNotContainValue() {
 Iterator iterator = Arrays.asList("testString1", "testString2").iterator();
 Mockito.when(arg1.compute(context)).thenReturn(iterator);
 Mockito.when(arg2.compute(context)).thenReturn("testString3");
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testFindMatch_OneIteratorContainsAllValuesOfAnotherIterator() {
 Iterator iterator1 = Arrays.asList("testString1", "testString2").iterator();
 Iterator iterator2 = Arrays.asList("testString1", "testString2").iterator();
 HashSet set = new HashSet<>();
 while (iterator1.hasNext()) {
 set.add(iterator1.next());
 }
 Mockito.when(arg1.compute(context)).thenReturn(iterator1);
 Mockito.when(arg2.compute(context)).thenReturn(iterator2);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testFindMatch_OneIteratorDoesNotContainAllValuesOfAnotherIterator() {
 Iterator iterator1 = Arrays.asList("testString1", "testString2").iterator();
 Iterator iterator2 = Arrays.asList("testString1", "testString2", "testString3").iterator();
 HashSet set = new HashSet<>();
 while (iterator1.hasNext()) {
 set.add(iterator1.next());
 }
 Mockito.when(arg1.compute(context)).thenReturn(iterator1);
 Mockito.when(arg2.compute(context)).thenReturn(iterator2);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testFindMatch_BothIteratorsAreEmpty() {
 Iterator iterator1 = Collections.emptyIterator();
 Iterator iterator2 = Collections.emptyIterator();
 Mockito.when(arg1.compute(context)).thenReturn(iterator1);
 Mockito.when(arg2.compute(context)).thenReturn(iterator2);
 assertTrue(coreOperationCompare.equal(context, arg1, arg2));
 }

 @Test
 public void testFindMatch_OneIteratorIsEmpty() {
 Iterator iterator1 = Collections.emptyIterator();
 Iterator iterator2 = Arrays.asList("testString1", "testString2").iterator();
 Mockito.when(arg1.compute(context)).thenReturn(iterator1);
 Mockito.when(arg2.compute(context)).thenReturn(iterator2);
 assertFalse(coreOperationCompare.equal(context, arg1, arg2));
 }

}