package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private ElExpression collectionExpression;
 private ElExpression labelExpression;
 private ElExpression valueExpression;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setUp() {
 collectionExpression = Mockito.mock(ElExpression.class);
 labelExpression = Mockito.mock(ElExpression.class);
 valueExpression = Mockito.mock(ElExpression.class);
 component = Mockito.mock(Component.class);
 topLevelComponent = Mockito.mock(StandaloneComponent.class);
 dynamicSelectModel = new DynamicSelectModel();
 }

 // Basic tests for constructors
 @Test
 public void testDefaultConstructor() {
 dynamicSelectModel = new DynamicSelectModel();
 assertNull(dynamicSelectModel.getCollection());
 assertNull(dynamicSelectModel.getLabel());
 assertNull(dynamicSelectModel.getValue());
 assertNull(dynamicSelectModel.getObjects());
 }

 @Test
 public void testConstructorWithCollectionLabelValue() {
 dynamicSelectModel = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 assertEquals(collectionExpression, dynamicSelectModel.getCollection());
 assertEquals(labelExpression, dynamicSelectModel.getLabel());
 assertEquals(valueExpression, dynamicSelectModel.getValue());
 assertNull(dynamicSelectModel.getObjects());
 }

 // Test methods collection, label, value, translator, empty
 @Test
 public void testCollection() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("testCollection");
 assertEquals("testCollection", dynamicSelectModel.getCollection().getExpression());
 }

 @Test
 public void testLabel() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.label("testLabel");
 assertEquals("testLabel", dynamicSelectModel.getLabel().getExpression());
 }

 @Test
 public void testValue() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.value("testValue");
 assertEquals("testValue", dynamicSelectModel.getValue().getExpression());
 }

 @Test
 public void testTranslator() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.translator("testTranslator");
 assertEquals("testTranslator", dynamicSelectModel.getTranslator().getExpression());
 }

 @Test
 public void testEmpty() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.empty(Mockito.mock(Object.class));
 assertNotNull(dynamicSelectModel.getEmptyObject());
 }

 // Test method getObjects
 @Test
 public void testGetObjects_Collection() {
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 Collection testCollection = new ArrayList();
 testCollection.add(Mockito.mock(Object.class));
 testCollection.add(Mockito.mock(Object.class));
 Mockito.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(testCollection);
 List<Object> result = dynamicSelectModel.getObjects();
 assertEquals(2, result.size());
 }

 @Test
 public void testGetObjects_ObjectArray() {
 dynamicSelectModel.collection("testCollection.values()");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 Object[] testCollection = new Object[2];
 testCollection[0] = Mockito.mock(Object.class);
 testCollection[1] = Mockito.mock(Object.class);
 Mockito.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(testCollection);
 List<Object> result = dynamicSelectModel.getObjects();
 assertEquals(2, result.size());
 }

 @Test(expected = WheelException.class)
 public void testGetObjects_NullCollection() {
 dynamicSelectModel.collection(null);
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 Mockito.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(null);
 dynamicSelectModel.getObjects();
 }

 @Test(expected = WheelException.class)
 public void testGetObjects_NonCollectionOrObjectArray() {
 dynamicSelectModel.collection("testCollection.toString()");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 Mockito.when(collectionExpression.eval(topLevelComponent, component)).thenReturn("testString");
 dynamicSelectModel.getObjects();
 }

 // Test method getLabel and getValue
 @Test
 public void testGetLabelAndgetValue_ValidIndex() {
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 Collection testCollection = new ArrayList();
 testCollection.add(Mockito.mock(Object.class));
 testCollection.add(Mockito.mock(Object.class));
 Mockito.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(testCollection);
 String label = "testLabel";
 String value = "testValue";
 Object obj1 = Mockito.mock(Object.class);
 Object obj2 = Mockito.mock(Object.class);
 testCollection.set(0, obj1);
 testCollection.set(1, obj2);
 String label1 = dynamicSelectModel.getLabel(0);
 assertEquals(label, label1);
 String value1 = dynamicSelectModel.getValue(0);
 assertEquals(value, value1);
 }

 @Test
 public void testGetLabelAndgetValue_InvalidIndex() {
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 Collection testCollection = new ArrayList();
 testCollection.add(Mockito.mock(Object.class));
 testCollection.add(Mockito.mock(Object.class));
 Mockito.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(testCollection);
 dynamicSelectModel.getLabel(-1);
 dynamicSelectModel.getValue(-1);
 }

 // Test method translateValue
 @Test
 public void testTranslateValue_ValidValue() {
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 dynamicSelectModel.translator("testTranslator");
 Collection testCollection = new ArrayList();
 testCollection.add(Mockito.mock(Object.class, "Object1"));
 testCollection.add(Mockito.mock(Object.class, "Object2"));
 Object obj1 = Mockito.mock(Object.class, "ObjectTranslated1");
 Object obj2 = Mockito.mock(Object.class, "ObjectTranslated2");
 testCollection.set(0, obj1);
 testCollection.set(1, obj2);
 Mockito.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(testCollection);
 Object result = dynamicSelectModel.translateValue("ObjectTranslated1.toString()");
 assertEquals(obj1, result);
 }

 @Test
 public void testTranslateValue_InvalidValue() {
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 dynamicSelectModel.translator("testTranslator");
 Collection testCollection = new ArrayList();
 testCollection.add(Mockito.mock(Object.class, "Object1"));
 testCollection.add(Mockito.mock(Object.class, "Object2"));
 Object obj1 = Mockito.mock(Object.class, "Object1");
 Object obj2 = Mockito.mock(Object.class, "Object2");
 testCollection.set(0, obj1);
 testCollection.set(1, obj2);
 Mockito.when(collectionExpression.eval(topLevelComponent, component)).thenReturn(testCollection);
 dynamicSelectModel.translateValue("Object3.toString()");
 }

 // Test method reset
 @Test
 public void testReset() {
 dynamicSelectModel.reset();
 assertNull(dynamicSelectModel.getObjects());
 }

 // Test method validate
 @Test(expected = WheelException.class)
 public void testValidate_NullCollection() {
 dynamicSelectModel.collection(null);
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidate_NullLabel() {
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label(null);
 dynamicSelectModel.value("ordinal");
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidate_NullValue() {
 dynamicSelectModel.collection("testCollection");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value(null);
 dynamicSelectModel.validate();
 }
}