package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component component;
 private ElExpression label;
 private ElExpression value;
 private ElExpression collection;
 private ElExpression translator;
 private Collection objects;

 @Test
 public void testConstructorWithCollectionLabelValue() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel(collection, label, value);
     assertEquals(collection, model.getCollection());
     assertEquals(label, model.getLabel());
     assertEquals(value, model.getValue());
     assertNull(model.getObjects());
 }

 @Test
 public void testConstructorEmpty() {
     component = Mockito.mock(Component.class);
     model = new DynamicSelectModel();
     assertNull(model.getCollection());
     assertNull(model.getLabel());
     assertNull(model.getValue());
     assertNull(model.getObjects());
 }

 @Test
 public void testCollectionMethod() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.collection("testCollection");
     assertEquals("testCollection", model.getCollection().getExpression());
 }

 @Test
 public void testLabelMethod() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.label("testLabel");
     assertEquals("testLabel", model.getLabel().getExpression());
 }

 @Test
 public void testValueMethod() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.value("testValue");
     assertEquals("testValue", model.getValue().getExpression());
 }

 @Test
 public void testEnumerationMethod() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     translator = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.enumeration("testEnum");
     assertEquals("testEnum.values()", model.getCollection().getExpression());
     assertEquals("toString()", model.getLabel().getExpression());
     assertEquals("ordinal()", model.getValue().getExpression());
     assertNull(model.getTranslator());
 }

 @Test
 public void testTranslatorMethod() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     translator = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.translator("testTranslator");
     assertEquals("testTranslator", model.getTranslator().getExpression());
 }

 @Test
 public void testEmptyMethod() {
     component = Mockito.mock(Component.class);
     model = new DynamicSelectModel();
     model.empty(new Object());
     assertNotNull(model.getObjects());
     assertTrue(model.getObjects().contains(new Object()));
 }

 @Test(expected = WheelException.class)
 public void testInvalidCollectionAttribute() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.setComponent(component);
     model.collection(null);
     model.validate();
 }

 @Test(expected = WheelException.class)
 public void testInvalidLabelAttribute() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.setComponent(component);
     model.label(null);
     model.validate();
 }

 @Test(expected = WheelException.class)
 public void testInvalidValueAttribute() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.setComponent(component);
     model.value(null);
     model.validate();
 }

 @Test(expected = WheelException.class)
 public void testInvalidCollectionEvaluation() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.setComponent(component);
     model.collection(new ElExpression("invalidExpression"));
     model.validate();
 }

 @Test(expected = WheelException.class)
 public void testNullCollectionReturned() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.setComponent(component);
     Mockito.when(collection.eval(component, Mockito.any())).thenReturn(null);
     model.collection("testCollection");
     model.validate();
 }

 @Test
 public void testGetObjectsNullInitialization() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.setComponent(component);
     assertNull(model.getObjects());
 }

 @Test
 public void testGetObjectsValidInitialization() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     Object[] array = new Object[3];
     for (int i = 0; i < array.length; i++) {
         array[i] = new Object();
     }
     Mockito.when(collection.eval(component, Mockito.any())).thenReturn(array);
     model = new DynamicSelectModel();
     model.setComponent(component);
     model.collection("testCollection");
     Collection expected = new ArrayList();
     for (int i = 0; i < array.length; i++) {
         expected.add(array[i]);
     }
     Collection actual = model.getObjects();
     assertEquals(expected, actual);
 }

 @Test
 public void testGetObjectsEmptyInitialization() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     Object emptyObject = new Object();
     model = new DynamicSelectModel();
     model.setComponent(component);
     model.collection("testCollection");
     model.empty(emptyObject);
     Collection expected = new LinkedList();
     expected.add(emptyObject);
     Collection actual = model.getObjects();
     assertEquals(expected, actual);
 }

 @Test
 public void testGetLabelIndexOutOfBounds() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     Object[] array = new Object[3];
     for (int i = 0; i < array.length; i++) {
         array[i] = new Object();
     }
     model = new DynamicSelectModel();
     model.setComponent(component);
     model.collection(new ElExpression("testCollection"));
     model.objects = new ArrayList();
     model.objects.addAll(Arrays.asList(array));
     String expectedLabel = "null";
     try {
         String actualLabel = model.getLabel(-1);
         fail("Expected an error but got a label");
     } catch (IllegalArgumentException e) {
         assertEquals("Index was -1", e.getMessage());
         assertEquals(expectedLabel, model.getLabel(0).toString());
     }
 }

 @Test
 public void testGetValueIndexOutOfBounds() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     Object[] array = new Object[3];
     for (int i = 0; i < array.length; i++) {
         array[i] = new Object();
     }
     model = new DynamicSelectModel();
     model.setComponent(component);
     model.collection(new ElExpression("testCollection"));
     model.objects = new ArrayList();
     model.objects.addAll(Arrays.asList(array));
     Object expectedValue = array[0];
     try {
         Object actualValue = model.getValue(-1);
         fail("Expected an error but got a value");
     } catch (IllegalArgumentException e) {
         assertEquals("Index was -1", e.getMessage());
         assertEquals(expectedValue.toString(), model.getValue(0).toString());
     }
 }

 @Test
 public void testTranslateValueNullValue() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     translator = Mockito.mock(ElExpression.class);
     Object[] array = new Object[3];
     for (int i = 0; i < array.length; i++) {
         array[i] = new Object();
     }
     model = new DynamicSelectModel();
     model.setComponent(component);
     model.collection(new ElExpression("testCollection"));
     model.objects = new ArrayList();
     model.objects.addAll(Arrays.asList(array));
     model.translator(new ElExpression("testTranslator"));
     Object expectedTranslatedValue = array[0];
     String actualTranslatedValue = model.translateValue("null");
     assertEquals(expectedTranslatedValue.toString(), actualTranslatedValue);
 }

 @Test(expected = WheelException.class)
 public void testTranslateValueInvalidValue() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     translator = Mockito.mock(ElExpression.class);
     Object[] array = new Object[3];
     for (int i = 0; i < array.length; i++) {
         array[i] = new Object();
     }
     model = new DynamicSelectModel();
     model.setComponent(component);
     model.collection(new ElExpression("testCollection"));
     model.objects = new ArrayList();
     model.objects.addAll(Arrays.asList(array));
     model.translator(new ElExpression("testTranslator"));
     model.validate();
     String invalidValue = "invalidValue";
     model.translateValue(invalidValue);
 }

 @Test
 public void testReset() {
     component = Mockito.mock(Component.class);
     label = Mockito.mock(ElExpression.class);
     value = Mockito.mock(ElExpression.class);
     collection = Mockito.mock(ElExpression.class);
     model = new DynamicSelectModel();
     model.setComponent(component);
     model.collection(new ElExpression("testCollection"));
     model.objects = new ArrayList();
     model.objects.addAll(Arrays.asList(new Object[3]));
     model.reset();
     assertNull(model.getObjects());
 }
}