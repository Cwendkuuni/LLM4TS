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
 private StandaloneComponent topLevelComponent;
 private Collection collectionMock;
 private ElExpression collectionExpression;
 private ElExpression labelExpression;
 private ElExpression valueExpression;
 private ElExpression translatorExpression;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 component = mock(Component.class);
 topLevelComponent = mock(StandaloneComponent.class);
 collectionMock = mock(Collection.class);
 collectionExpression = new ElExpression("collectionExpression");
 labelExpression = new ElExpression("labelExpression");
 valueExpression = new ElExpression("valueExpression");
 translatorExpression = new ElExpression("translatorExpression");
 model = new DynamicSelectModel();
 model.setComponent(component);
 model.setTopLevelComponent(topLevelComponent);
 model.collection(collectionExpression);
 model.label(labelExpression);
 model.value(valueExpression);
 }

 @Test
 public void testEmptyConstructor() {
 DynamicSelectModel emptyModel = new DynamicSelectModel();
 assertNull(emptyModel.getCollection());
 assertNull(emptyModel.getLabel());
 assertNull(emptyModel.getValue());
 }

 @Test
 public void testConstructorWithExpressions() {
 DynamicSelectModel model = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
 assertEquals(collectionExpression, model.getCollection());
 assertEquals(labelExpression, model.getLabel());
 assertEquals(valueExpression, model.getValue());
 }

 @Test
 public void testAddMethods() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.collection("testCollection");
 model.label("testLabel");
 model.value("testValue");
 model.translator("testTranslator");
 model.empty(new Object());
 assertEquals("testCollection", model.getCollection().getExpression());
 assertEquals("testLabel", model.getLabel().getExpression());
 assertEquals("testValue", model.getValue().getExpression());
 assertEquals("testTranslator", model.getTranslator().getExpression());
 assertEquals(new Object(), model.getEmptyObject());
 }

 @Test
 public void testGetObjectsWithCollectionAsInstanceOfCollection() {
 when(collectionExpression.eval(topLevelComponent, component)).thenReturn(collectionMock);
 model.setCollection(collectionExpression);
 model.setLabel(labelExpression);
 model.setValue(valueExpression);
 Collection objects = model.getObjects();
 assertEquals(collectionMock, objects);
 }

 @Test
 public void testGetObjectsWithCollectionAsInstanceOfArray() {
 when(collectionExpression.eval(topLevelComponent, component)).thenReturn(new Object[]{ "one", "two", "three" });
 model.setCollection(collectionExpression);
 model.setLabel(labelExpression);
 model.setValue(valueExpression);
 Collection objects = model.getObjects();
 assertEquals(3, objects.size());
 assertEquals("one", objects.get(0).toString());
 assertEquals("two", objects.get(1).toString());
 assertEquals("three", objects.get(2).toString());
 }

 @Test
 public void testGetObjectsWithEmptyObject() {
 model.setCollection(collectionExpression);
 model.setLabel(labelExpression);
 model.setValue(valueExpression);
 model.empty(new Object());
 model.setObjects(new ArrayList<Object>());
 assertEquals(2, model.getObjects().size());
 assertEquals(new Object(), model.getObjects().get(0));
 assertEquals("", model.getObjects().get(1).toString());
 }

 @Test
 public void testGetObjectsWithEmptyCollection() {
 model.setCollection(collectionExpression);
 model.setLabel(labelExpression);
 model.setValue(valueExpression);
 model.setObjects(Collections.emptyList());
 assertNull(model.getObjects());
 }

 @Test
 public void testTranslateValue() {
 when(collectionExpression.eval(topLevelComponent, component)).thenReturn(collectionMock);
 when(valueExpression.eval(anyObject(), anyObject())).thenReturn("testValue");
 when(translatorExpression.eval(anyObject(), anyObject())).thenReturn("translatedValue");
 model.setCollection(collectionExpression);
 model.setLabel(labelExpression);
 model.setValue(valueExpression);
 model.setTranslator(translatorExpression);
 assertEquals("translatedValue", model.translateValue("testValue"));
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsWithNullCollection() {
 model.setCollection(null);
 model.setLabel(labelExpression);
 model.setValue(valueExpression);
 model.setObjects(new ArrayList<Object>());
 model.getObjects();
 fail("Should have thrown WheelException.");
 }

 @Test
 public void testGetObjectsWithNullLabel() {
 model.setCollection(collectionExpression);
 model.setLabel(null);
 model.setValue(valueExpression);
 model.setObjects(new ArrayList<Object>());
 model.getObjects();
 fail("Should have thrown WheelException.");
 }

 @Test
 public void testGetObjectsWithNullValue() {
 model.setCollection(collectionExpression);
 model.setLabel(labelExpression);
 model.setValue(null);
 model.setObjects(new ArrayList<Object>());
 model.getObjects();
 fail("Should have thrown WheelException.");
 }

 @Test
 public void testGetObjectsWithNullComponent() {
 model.setCollection(collectionExpression);
 model.setLabel(labelExpression);
 model.setValue(valueExpression);
 model.setObjects(new ArrayList<Object>());
 model.setComponent(null);
 model.getObjects();
 fail("Should have thrown WheelException.");
 }

 @Test
 public void testGetObjectsWithNullTopLevelComponent() {
 model.setCollection(collectionExpression);
 model.setLabel(labelExpression);
 model.setValue(valueExpression);
 model.setObjects(new ArrayList<Object>());
 model.setTopLevelComponent(null);
 model.getObjects();
 fail("Should have thrown WheelException.");
 }

 @Test
 public void testValidate() {
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithNullCollection() {
 model.setCollection(null);
 model.validate();
 fail("Should have thrown WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testValidateWithNullLabel() {
 model.setLabel(null);
 model.validate();
 fail("Should have thrown WheelException.");
 }

 @Test(expected = WheelException.class)
 public void testValidateWithNullValue() {
 model.setValue(null);
 model.validate();
 fail("Should have thrown WheelException.");
 }
}