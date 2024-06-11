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

 private DynamicSelectModel dynamicSelectModel;
 private ElExpression labelExpression;
 private ElExpression valueExpression;
 private ElExpression collectionExpression;
 private ElExpression translatorExpression;
 private Collection objects;

 @Test
 public void testDynamicSelectModelConstructionWithCollectionLabelAndValue() {
     collectionExpression = Mockito.mock(ElExpression.class);
     labelExpression = Mockito.mock(ElExpression.class);
     valueExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);
     assertNull(dynamicSelectModel.getObjects());
     assertEquals(collectionExpression, dynamicSelectModel.getCollection());
     assertEquals(labelExpression, dynamicSelectModel.getLabel());
     assertEquals(valueExpression, dynamicSelectModel.getValue());
 }

 @Test
 public void testDynamicSelectModelWithEmptyConstructor() {
     dynamicSelectModel = new DynamicSelectModel();
     assertNull(dynamicSelectModel.getObjects());
     assertNull(dynamicSelectModel.getCollection());
     assertNull(dynamicSelectModel.getLabel());
     assertNull(dynamicSelectModel.getValue());
 }

 @Test
 public void testDynamicSelectModelCollectionMethod() {
     collectionExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection("testCollection");
     assertEquals(collectionExpression, dynamicSelectModel.getCollection());
 }

 @Test
 public void testDynamicSelectModelLabelMethod() {
     labelExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.label("testLabel");
     assertEquals(labelExpression, dynamicSelectModel.getLabel());
 }

 @Test
 public void testDynamicSelectModelValueMethod() {
     valueExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.value("testValue");
     assertEquals(valueExpression, dynamicSelectModel.getValue());
 }

 @Test
 public void testDynamicSelectModelEnumerationMethod() {
     collectionExpression = Mockito.mock(ElExpression.class);
     labelExpression = Mockito.mock(ElExpression.class);
     valueExpression = Mockito.mock(ElExpression.class);
     translatorExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection("testCollection");
     dynamicSelectModel.label("toString");
     dynamicSelectModel.value("ordinal");
     dynamicSelectModel.translator("testTranslator");
     when(collectionExpression.eval(Mockito.any(Component.class), Mockito.any(Component.class))).thenReturn(new ArrayList<>());
     when(translatorExpression.eval(Mockito.any(Object.class), Mockito.any(Component.class))).thenReturn("testTranslation");

     Collection objects = dynamicSelectModel.getObjects();
     assertEquals(2, objects.size());
     assertEquals("testTranslation", objects.get(0));
     assertEquals("testTranslation", objects.get(1));
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelValidationException() {
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(null);
     dynamicSelectModel.label(null);
     dynamicSelectModel.value(null);
 }

 @Test
 public void testDynamicSelectModelGetObjectsMethodWithEmptyCollection() {
     collectionExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(new ElExpression("new ArrayList<>()"));
     when(collectionExpression.eval(Mockito.any(Component.class), Mockito.any(Component.class))).thenReturn(new ArrayList<>());
     Collection objects = dynamicSelectModel.getObjects();
     assertFalse(objects.isEmpty());
 }

 @Test
 public void testDynamicSelectModelGetObjectsMethodWithNullCollection() {
     collectionExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(new ElExpression("null"));
     when(collectionExpression.eval(Mockito.any(Component.class), Mockito.any(Component.class))).thenReturn(null);
     try {
         dynamicSelectModel.getObjects();
         fail("Expected WheelException to be thrown");
     } catch (WheelException e) {
         // Expected
     }
 }

 @Test
 public void testDynamicSelectModelGetObjectsMethodWithInvalidCollection() {
     collectionExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(new ElExpression("new Object[10]()"));
     when(collectionExpression.eval(Mockito.any(Component.class), Mockito.any(Component.class))).thenReturn(new Object[10]);
     try {
         dynamicSelectModel.getObjects();
         fail("Expected WheelException to be thrown");
     } catch (WheelException e) {
         // Expected
     }
 }

 @Test
 public void testDynamicSelectModelGetObjectsMethodWithValidCollection() {
     collectionExpression = Mockito.mock(ElExpression.class);
     labelExpression = Mockito.mock(ElExpression.class);
     valueExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(new ElExpression("new ArrayList<>()"));
     when(collectionExpression.eval(Mockito.any(Component.class), Mockito.any(Component.class))).thenReturn(new ArrayList<>());
     Collection objects = dynamicSelectModel.getObjects();
     assertFalse(objects.isEmpty());
 }

 @Test
 public void testDynamicSelectModelGetLabelMethodWithValidIndex() {
     labelExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(new ElExpression("new ArrayList<>()"));
     dynamicSelectModel.label(new ElExpression("toString"));
     dynamicSelectModel.value(new ElExpression("toString"));
     objects = new ArrayList<>();
     objects.add(Mockito.mock(Object.class));
     dynamicSelectModel.setObjects(objects);
     when(labelExpression.eval(Mockito.any(Object.class), Mockito.any(Component.class))).thenReturn("testLabel");
     String label = dynamicSelectModel.getLabel(0);
     assertEquals("testLabel", label);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelGetLabelMethodWithInvalidIndex() {
     labelExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(new ElExpression("new ArrayList<>()"));
     dynamicSelectModel.label(new ElExpression("toString"));
     dynamicSelectModel.value(new ElExpression("toString"));
     objects = new ArrayList<>();
     objects.add(Mockito.mock(Object.class));
     dynamicSelectModel.setObjects(objects);
     when(labelExpression.eval(Mockito.any(Object.class), Mockito.any(Component.class))).thenThrow(new WheelException("testException"));
     try {
         dynamicSelectModel.getLabel(-1);
         fail("Expected WheelException to be thrown");
     } catch (WheelException e) {
         // Expected
     }
 }

 @Test
 public void testDynamicSelectModelGetValueMethodWithValidIndex() {
     valueExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(new ElExpression("new ArrayList<>()"));
     dynamicSelectModel.label(new ElExpression("toString"));
     dynamicSelectModel.value(new ElExpression("toString"));
     dynamicSelectModel.setObjects(new ArrayList<>());
     when(valueExpression.eval(Mockito.any(Object.class), Mockito.any(Component.class))).thenReturn("testValue");
     String value = dynamicSelectModel.getValue(0);
     assertEquals("testValue", value);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelGetValueMethodWithInvalidIndex() {
     valueExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(new ElExpression("new ArrayList<>()"));
     dynamicSelectModel.label(new ElExpression("toString"));
     dynamicSelectModel.value(new ElExpression("toString"));
     dynamicSelectModel.setObjects(new ArrayList<>());
     when(valueExpression.eval(Mockito.any(Object.class), Mockito.any(Component.class))).thenThrow(new WheelException("testException"));
     try {
         dynamicSelectModel.getValue(-1);
         fail("Expected WheelException to be thrown");
     } catch (WheelException e) {
         // Expected
     }
 }

 @Test
 public void testDynamicSelectModelTranslateValueMethodWithValidValue() {
     translatorExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(new ElExpression("new ArrayList<>()"));
     dynamicSelectModel.label(new ElExpression("toString"));
     dynamicSelectModel.value(new ElExpression("toString"));
     dynamicSelectModel.translator(new ElExpression("testTranslator"));
     Collection objects = new ArrayList<>();
     objects.add(Mockito.mock(Object.class));
     objects.add(Mockito.mock(Object.class));
     dynamicSelectModel.setObjects(objects);
     when(translatorExpression.eval(Mockito.any(Object.class), Mockito.any(Component.class))).thenReturn("testTranslation");
     Object translatedValue = dynamicSelectModel.translateValue("testValue");
     assertEquals("testTranslation", translatedValue);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelTranslateValueMethodWithInvalidValue() {
     translatorExpression = Mockito.mock(ElExpression.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.collection(new ElExpression("new ArrayList<>()"));
     dynamicSelectModel.label(new ElExpression("toString"));
     dynamicSelectModel.value(new ElExpression("toString"));
     dynamicSelectModel.translator(new ElExpression("testTranslator"));
     Collection objects = new ArrayList<>();
     objects.add(Mockito.mock(Object.class));
     objects.add(Mockito.mock(Object.class));
     dynamicSelectModel.setObjects(objects);
     when(translatorExpression.eval(Mockito.any(Object.class), Mockito.any(Component.class))).thenThrow(new WheelException("testException"));
     try {
         dynamicSelectModel.translateValue("testInvalidValue");
         fail("Expected WheelException to be thrown");
     } catch (WheelException e) {
         // Expected
     }
 }

 @Test
 public void testDynamicSelectModelGetComponentMethod() {
     Component component = Mockito.mock(Component.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.setComponent(component);
     assertEquals(component, dynamicSelectModel.getComponent());
 }

 @Test
 public void testDynamicSelectModelSetComponentMethod() {
     Component component = Mockito.mock(Component.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.setComponent(component);
     assertEquals(component, dynamicSelectModel.getComponent());
 }

 @Test
 public void testDynamicSelectModelGetTopLevelComponentMethod() {
     StandaloneComponent topLevelComponent = Mockito.mock(StandaloneComponent.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.setTopLevelComponent(topLevelComponent);
     assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testDynamicSelectModelSetTopLevelComponentMethod() {
     StandaloneComponent topLevelComponent = Mockito.mock(StandaloneComponent.class);
     dynamicSelectModel = new DynamicSelectModel();
     dynamicSelectModel.setTopLevelComponent(topLevelComponent);
     assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
 }

}