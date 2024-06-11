package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import wheel.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component component;
 private ElExpression collection;
 private ElExpression label;
 private ElExpression value;
 private Collection objects;

 @Before
 public void setUp() {
 collection = mock(ElExpression.class);
 label = mock(ElExpression.class);
 value = mock(ElExpression.class);
 component = mock(Component.class);
 model = new DynamicSelectModel();
 model.collection(collection);
 model.label(label);
 model.value(value);
 }

 @Test
 public void testGetOptionCount_whenCollectionIsEmpty_returnsZero() {
 when(collection.eval(component, component)).thenReturn(new ArrayList<>());
 assertEquals(0, model.getOptionCount());
 }

 @Test
 public void testGetOptionCount_whenCollectionIsNotEmpty_returnsSizeOfCollection() {
 when(collection.eval(component, component)).thenAnswer(new Answer<Collection>() {
 @Override
 public Collection answer(InvocationOnMock invocation) throws Throwable {
 List list = new ArrayList();
 list.add("Option 1");
 list.add("Option 2");
 return list;
 }
 });
 assertEquals(2, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testGetObjects_whenCollectionEvaluationFails_throwsException() {
 when(collection.eval(component, component)).thenThrow(new RuntimeException());
 model.getObjects();
 }

 @Test
 public void testGetObjects_whenCollectionIsAnInstanceOfCollection_returnsCollection() {
 when(collection.eval(component, component)).thenReturn(Arrays.asList("Option 1", "Option 2"));
 assertEquals(Arrays.asList("Option 1", "Option 2"), model.getObjects());
 }

 @Test
 public void testGetObjects_whenCollectionIsAnArray_convertsItToArrayList() {
 when(collection.eval(component, component)).thenReturn(new Object[]{"Option 1", "Option 2"});
 assertEquals(Arrays.asList("Option 1", "Option 2"), model.getObjects());
 }

 @Test
 public void testGetLabel_whenLabelEvaluationSucceeds_returnsLabelString() {
 when(label.eval(anyObject(), component)).thenReturn("Label for Option 1");
 assertEquals("Label for Option 1", model.getLabel(0));
 }

 @Test
 public void testGetValue_whenValueEvaluationSucceeds_returnsValueString() {
 when(value.eval(anyObject(), component)).thenReturn("Value for Option 1");
 assertEquals("Value for Option 1", model.getValue(0));
 }

 @Test
 public void testTranslateValue_whenValueIsFound_returnsTranslatedValue() {
 Object translatedValue = "Translated Value";
 when(translator.eval(anyObject(), component)).thenReturn(translatedValue);
 model.translator("translator");
 String valueToFind = "Value for Option 1";
 when(value.eval(anyObject(), component)).thenReturn(valueToFind);
 when(label.eval(anyObject(), component)).thenReturn("Label for Option 1");
 Object result = model.translateValue(valueToFind);
 assertEquals(translatedValue, result);
 }

 @Test
 public void testTranslateValue_whenValueIsNotFound_returnsNull() {
 when(value.eval(anyObject(), component)).thenReturn("Value for Option 1");
 when(label.eval(anyObject(), component)).thenReturn("Label for Option 1");
 assertNull(model.translateValue("Value for Option 2"));
 }

 @Test
 public void testReset_resetsObjectsListToNull() {
 model.reset();
 assertNull(model.getObjects());
 }

 @Test
 public void testValidate_throwsExceptionWhenCollectionIsNull() {
 model.validate();
 verify(component, times(1)).errorMessage("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.");
 }

 @Test
 public void testValidate_throwsExceptionWhenLabelIsNull() {
 model.label = null;
 model.validate();
 verify(component, times(1)).errorMessage("Could not create DynamicSelection model. Label attribute was null or not an el-expression.");
 }

 @Test
 public void testValidate_throwsExceptionWhenValueIsNull() {
 model.value = null;
 model.validate();
 verify(component, times(1)).errorMessage("Could not create DynamicSelection model. Value attribute was null or not an el-expression.");
 }
}