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

 private Component component;
 private DynamicSelectModel dynamicSelectModel;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setUp() {
 this.component = Mockito.mock(Component.class);
 this.topLevelComponent = Mockito.mock(StandaloneComponent.class);
 this.dynamicSelectModel = new DynamicSelectModel();
 }

 @Test
 public void testConstructorWithCollectionLabelValue() {
 // Given
 ElExpression collectionExpression = Mockito.mock(ElExpression.class);
 ElExpression labelExpression = Mockito.mock(ElExpression.class);
 ElExpression valueExpression = Mockito.mock(ElExpression.class);

 when(collectionExpression.getExpression()).thenReturn("collection");
 when(labelExpression.getExpression()).thenReturn("label");
 when(valueExpression.getExpression()).thenReturn("value");

 this.dynamicSelectModel = new DynamicSelectModel(collectionExpression, labelExpression, valueExpression);

 // Then
 assertEquals("collection", this.dynamicSelectModel.getCollection().getExpression());
 assertEquals("label", this.dynamicSelectModel.getLabel().getExpression());
 assertEquals("value", this.dynamicSelectModel.getValue().getExpression());
 }

 @Test
 public void testDefaultConstructor() {
 // When
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();

 // Then
 assertNull(dynamicSelectModel.getCollection());
 assertNull(dynamicSelectModel.getLabel());
 assertNull(dynamicSelectModel.getValue());
 }

 @Test
 public void testCollectionMethod() {
 // Given
 ElExpression collectionExpression = Mockito.mock(ElExpression.class);

 when(collectionExpression.getExpression()).thenReturn("collection");

 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.collection("collection");

 // Then
 assertEquals("collection", this.dynamicSelectModel.getCollection().getExpression());
 }

 @Test
 public void testLabelMethod() {
 // Given
 ElExpression labelExpression = Mockito.mock(ElExpression.class);

 when(labelExpression.getExpression()).thenReturn("label");

 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.label("label");

 // Then
 assertEquals("label", this.dynamicSelectModel.getLabel().getExpression());
 }

 @Test
 public void testValueMethod() {
 // Given
 ElExpression valueExpression = Mockito.mock(ElExpression.class);

 when(valueExpression.getExpression()).thenReturn("value");

 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.value("value");

 // Then
 assertEquals("value", this.dynamicSelectModel.getValue().getExpression());
 }

 @Test
 public void testEnumsMethod() {
 // Given
 ElExpression collectionExpression = Mockito.mock(ElExpression.class);
 ElExpression labelExpression = Mockito.mock(ElExpression.class);
 ElExpression valueExpression = Mockito.mock(ElExpression.class);
 ElExpression translatorExpression = Mockito.mock(ElExpression.class);

 when(collectionExpression.getExpression()).thenReturn("enumValues");
 when(labelExpression.getExpression()).thenReturn("toString()");
 when(valueExpression.getExpression()).thenReturn("ordinal()");
 when(translatorExpression.getExpression()).thenReturn("translator");

 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.enumeration("enum");
 this.dynamicSelectModel.label("toString()");
 this.dynamicSelectModel.value("ordinal()");
 this.dynamicSelectModel.translator("translator");

 // Then
 assertEquals("enumValues.values()", this.dynamicSelectModel.getCollection().getExpression());
 assertEquals("toString()", this.dynamicSelectModel.getLabel().getExpression());
 assertEquals("ordinal()", this.dynamicSelectModel.getValue().getExpression());
 assertEquals("translator", this.dynamicSelectModel.getTranslator().getExpression());
 }

 @Test
 public void testGetOptionCountWithEmptyCollection() {
 // Given
 this.dynamicSelectModel = new DynamicSelectModel();

 // Then
 assertEquals(0, this.dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testGetOptionCountWithCollection() {
 // Given
 Collection collection = new ArrayList();
 collection.add(new Object());
 collection.add(new Object());
 ElExpression collectionExpression = Mockito.mock(ElExpression.class);
 when(collectionExpression.getExpression()).thenReturn(collection.toString());

 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.collection("collection");
 this.dynamicSelectModel.setComponent(this.component);

 // When
 this.dynamicSelectModel.setObjects(collection);

 // Then
 assertEquals(2, this.dynamicSelectModel.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsWithNullCollection() {
 // Given
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.collection("collection");
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setTopLevelComponent(this.topLevelComponent);

 // When
 this.dynamicSelectModel.setObjects(null);

 // Then (expected exception)
 fail("Exception not thrown.");
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsWithInvalidCollectionType() {
 // Given
 Object[] objects = new Object[0];
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.collection("collection");
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setTopLevelComponent(this.topLevelComponent);

 // When
 this.dynamicSelectModel.setObjects(objects);

 // Then (expected exception)
 fail("Exception not thrown.");
 }

 @Test
 public void testGetObjectsWithEmptyCollectionAndEmptyObject() {
 // Given
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.collection("collection");
 this.dynamicSelectModel.empty(new Object());
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setTopLevelComponent(this.topLevelComponent);

 // When
 this.dynamicSelectModel.setObjects(new ArrayList());

 // Then
 List objects = (List)this.dynamicSelectModel.getObjects();
 assertTrue(objects.contains(this.dynamicSelectModel.getEmptyObject()));
 assertTrue(objects.isEmpty());
 }

 @Test
 public void testGetLabelWithIndexOutOfBounds() {
 // Given
 Collection objects = new ArrayList();
 objects.add(new Object());
 String label = "label";
 ElExpression labelExpression = new ElExpression(label);
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.label(labelExpression);
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setObjects(objects);

 // When and Then
 try {
 this.dynamicSelectModel.getLabel(-1);
 fail("Exception not thrown.");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testGetLabelWithValidIndex() {
 // Given
 Collection objects = new ArrayList();
 objects.add(new Object());
 objects.add(new Object());
 String label = "label";
 ElExpression labelExpression = new ElExpression(label);
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.label(labelExpression);
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setObjects(objects);

 // When
 String result = this.dynamicSelectModel.getLabel(0);

 // Then
 assertEquals(label, result);
 }

 @Test
 public void testGetValueWithIndexOutOfBounds() {
 // Given
 Collection objects = new ArrayList();
 objects.add(new Object());
 objects.add(new Object());
 String value = "value";
 ElExpression valueExpression = new ElExpression(value);
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.value(valueExpression);
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setObjects(objects);

 // When and Then
 try {
 this.dynamicSelectModel.getValue(-1);
 fail("Exception not thrown.");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testGetValueWithValidIndex() {
 // Given
 Collection objects = new ArrayList();
 objects.add(new Object());
 objects.add(new Object());
 String value = "value";
 ElExpression valueExpression = new ElExpression(value);
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.value(valueExpression);
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setObjects(objects);

 // When
 String result = this.dynamicSelectModel.getValue(0).toString();

 // Then
 assertEquals(value, result);
 }

 @Test
 public void testTranslateValueWithValidValue() {
 // Given
 Collection objects = new ArrayList();
 objects.add(new Object1());
 objects.add(new Object2());
 String value = "value";
 ElExpression labelExpression = new ElExpression("label");
 ElExpression valueExpression = new ElExpression(value);
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.label(labelExpression);
 this.dynamicSelectModel.value(valueExpression);
 this.dynamicSelectModel.translator(new ElExpression("translator"));
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setObjects(objects);

 // When
 Object result = this.dynamicSelectModel.translateValue(value);

 // Then
 assertEquals(objects.get(0), result);
 }

 @Test(expected = WheelException.class)
 public void testTranslateValueWithInvalidValue() {
 // Given
 Collection objects = new ArrayList();
 objects.add(new Object1());
 objects.add(new Object2());
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.label(Mockito.mock(ElExpression.class));
 this.dynamicSelectModel.value(Mockito.mock(ElExpression.class));
 this.dynamicSelectModel.translator(new ElExpression("translator"));
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setObjects(objects);

 // When and Then (expected exception)
 try {
 this.dynamicSelectModel.translateValue("invalidValue");
 fail("Exception not thrown.");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testSetComponent() {
 // Given
 Component component = Mockito.mock(Component.class);

 // When
 this.dynamicSelectModel.setComponent(component);

 // Then
 assertEquals(component, this.dynamicSelectModel.getComponent());
 }

 @Test
 public void testSetTopLevelComponent() {
 // Given
 StandaloneComponent topLevelComponent = Mockito.mock(StandaloneComponent.class);

 // When
 this.dynamicSelectModel.setTopLevelComponent(topLevelComponent);

 // Then
 assertEquals(topLevelComponent, this.dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testValidate() {
 // Given
 ElExpression collectionExpression = Mockito.mock(ElExpression.class);
 ElExpression labelExpression = Mockito.mock(ElExpression.class);
 ElExpression valueExpression = Mockito.mock(ElExpression.class);

 when(collectionExpression.getExpression()).thenReturn(null);
 when(labelExpression.getExpression()).thenReturn(null);
 when(valueExpression.getExpression()).thenReturn(null);

 // When
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.collection(collectionExpression);
 this.dynamicSelectModel.label(labelExpression);
 this.dynamicSelectModel.value(valueExpression);

 // Then (expected exception)
 try {
 this.dynamicSelectModel.validate();
 fail("Exception not thrown.");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testValidateWithValidModel() {
 // Given
 ElExpression collectionExpression = Mockito.mock(ElExpression.class);
 ElExpression labelExpression = Mockito.mock(ElExpression.class);
 ElExpression valueExpression = Mockito.mock(ElExpression.class);

 when(collectionExpression.getExpression()).thenReturn("collection");
 when(labelExpression.getExpression()).thenReturn("label");
 when(valueExpression.getExpression()).thenReturn("value");

 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.collection(collectionExpression);
 this.dynamicSelectModel.label(labelExpression);
 this.dynamicSelectModel.value(valueExpression);

 // When
 this.dynamicSelectModel.validate();

 // Then
 // No exception
 }

 @Test
 public void testReset() {
 // Given
 Collection objects = new ArrayList();
 objects.add(new Object());
 objects.add(new Object());
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.collection("collection");
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setObjects(objects);

 // When
 this.dynamicSelectModel.reset();

 // Then
 assertNull(this.dynamicSelectModel.getObjects());
 }
}