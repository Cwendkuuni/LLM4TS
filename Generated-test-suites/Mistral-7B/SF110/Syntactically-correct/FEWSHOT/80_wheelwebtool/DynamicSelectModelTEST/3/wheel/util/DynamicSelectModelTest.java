package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import wheel.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component component;
 private ElExpression collectionEl;
 private ElExpression labelEl;
 private ElExpression valueEl;
 private Collection objects;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 component = mock(Component.class);
 collectionEl = mock(ElExpression.class);
 labelEl = mock(ElExpression.class);
 valueEl = mock(ElExpression.class);
 model = new DynamicSelectModel()
 .collection(collectionEl)
 .label(labelEl)
 .value(valueEl);
 }

 @Test
 public void testNewInstance() {
 DynamicSelectModel instance = new DynamicSelectModel();
 assertNull(instance.collection);
 assertNull(instance.label);
 assertNull(instance.value);
 }

 @Test
 public void testDynamicSelectModelWithCollectionLabelValue() {
 model = new DynamicSelectModel()
 .collection(collectionEl)
 .label(labelEl)
 .value(valueEl);

 verify(collectionEl, times(1)).eval(component, component);
 verifyNoMoreInteractions(collectionEl);
 verifyNoMoreInteractions(labelEl);
 verifyNoMoreInteractions(valueEl);

 assertEquals(0, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testCollectionIsNull() {
 DynamicSelectModel model = new DynamicSelectModel(null, labelEl, valueEl);
 }

 @Test(expected = WheelException.class)
 public void testLabelIsNull() {
 DynamicSelectModel model = new DynamicSelectModel(collectionEl, null, valueEl);
 }

 @Test(expected = WheelException.class)
 public void testValueIsNull() {
 DynamicSelectModel model = new DynamicSelectModel(collectionEl, labelEl, null);
 }

 @Test
 public void testCollectionExpressionEvaluatedToNull() {
 doReturn(null).when(collectionEl).eval(component, component);

 try {
 model.getObjects();
 fail("Expected WheelException");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testCollectionExpressionEvaluatedToArray() {
 Object[] array = new Object[]{"A", "B", "C"};
 when(collectionEl.eval(component, component)).thenReturn(array);

 objects = model.getObjects();
 assertEquals(3, objects.size());
 assertEquals("A", objects.toArray()[0].toString());
 assertEquals("B", objects.toArray()[1].toString());
 assertEquals("C", objects.toArray()[2].toString());
 }

 @Test
 public void testCollectionExpressionEvaluatedToList() {
 List<Object> list = Arrays.asList("A", "B", "C");
 when(collectionEl.eval(component, component)).thenReturn(list);

 objects = model.getObjects();
 assertEquals(3, objects.size());
 assertEquals("A", objects.toArray()[0].toString());
 assertEquals("B", objects.toArray()[1].toString());
 assertEquals("C", objects.toArray()[2].toString());
 }

 @Test
 public void testGetLabel() {
 String label = "TestLabel";
 when(labelEl.eval(anyObject(), anyObject())).thenReturn(label);

 assertEquals(label, model.getLabel(0));
 }

 @Test
 public void testGetValue() {
 String value = "TestValue";
 when(valueEl.eval(anyObject(), anyObject())).thenReturn(value);

 assertEquals(value, model.getValue(0));
 }

 @Test
 public void testTranslateValue() {
 Object translatedValue = new Object(){};
 when(translatorEl.eval(anyObject(), anyObject())).thenReturn(translatedValue);

 Object translated = model.translateValue("TestValue");
 assertEquals(translatedValue, translated);
 }

 @Test
 public void testTranslateValueNull() {
 assertNull(model.translateValue(null));
 }

 @Test
 public void testTranslateValueUnknownValue() {
 assertNull(model.translateValue("UnknownValue"));
 }
}