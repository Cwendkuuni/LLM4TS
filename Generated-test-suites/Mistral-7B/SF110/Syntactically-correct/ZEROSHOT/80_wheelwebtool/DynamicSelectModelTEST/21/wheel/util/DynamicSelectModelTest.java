package wheel.util;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Component;
import org.w3c.dom.ElExpression;
import org.w3c.dom.StandaloneComponent;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private StandaloneComponent topLevelComponent;
 private ElExpression collection;
 private ElExpression label;
 private ElExpression value;
 private ElExpression translator;
 private Collection objects;

 @Test
 public void testDynamicSelectModelConstructionWithCollectionLabelValue() {
 // Given
 collection = mock(ElExpression.class);
 label = mock(ElExpression.class);
 value = mock(ElExpression.class);
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);

 // Then
 assertEquals("collection", collection, dynamicSelectModel.getCollection());
 assertEquals("label", label, dynamicSelectModel.getLabel());
 assertEquals("value", value, dynamicSelectModel.getValue());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelConstructionWithoutCollection() {
 // Given
 label = mock(ElExpression.class);
 value = mock(ElExpression.class);
 dynamicSelectModel = new DynamicSelectModel();

 // When
 dynamicSelectModel.validate();

 // Then
 fail("Validation should have thrown WheelException");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelConstructionWithoutLabel() {
 // Given
 collection = mock(ElExpression.class);
 value = mock(ElExpression.class);
 dynamicSelectModel = new DynamicSelectModel();

 // When
 dynamicSelectModel.validate();

 // Then
 fail("Validation should have thrown WheelException");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelConstructionWithoutValue() {
 // Given
 collection = mock(ElExpression.class);
 label = mock(ElExpression.class);
 dynamicSelectModel = new DynamicSelectModel();

 // When
 dynamicSelectModel.validate();

 // Then
 fail("Validation should have thrown WheelException");
 }

 @Test
 public void testDynamicSelectModelCollectionMethod() {
 // Given
 dynamicSelectModel = new DynamicSelectModel();

 // When
 dynamicSelectModel = dynamicSelectModel.collection("collection");

 // Then
 assertEquals("collection", dynamicSelectModel.getCollection());
 }

 @Test
 public void testDynamicSelectModelLabelMethod() {
 // Given
 dynamicSelectModel = new DynamicSelectModel();

 // When
 dynamicSelectModel = dynamicSelectModel.label("label");

 // Then
 assertEquals("label", dynamicSelectModel.getLabel());
 }

 @Test
 public void testDynamicSelectModelValueMethod() {
 // Given
 dynamicSelectModel = new DynamicSelectModel();

 // When
 dynamicSelectModel = dynamicSelectModel.value("value");

 // Then
 assertEquals("value", dynamicSelectModel.getValue());
 }

 @Test
 public void testDynamicSelectModelEnumerationMethod() {
 // Given
 dynamicSelectModel = new DynamicSelectModel();

 // When
 dynamicSelectModel = dynamicSelectModel.enumeration("enumeration");

 // Then
 assertEquals("enumeration.values()", dynamicSelectModel.getCollection().getExpression());
 assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
 assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
 }

 @Test
 public void testDynamicSelectModelTranslatorMethod() {
 // Given
 dynamicSelectModel = new DynamicSelectModel();

 // When
 dynamicSelectModel = dynamicSelectModel.translator("translator");

 // Then
 assertEquals("translator", dynamicSelectModel.getTranslator());
 }

 @Test
 public void testDynamicSelectModelEmptyMethod() {
 // Given
 dynamicSelectModel = new DynamicSelectModel();

 // When
 dynamicSelectModel = dynamicSelectModel.empty("emptyObject");

 // Then
 assertNotNull("emptyObject should not be null", dynamicSelectModel.getEmptyObject());
 }

 @Test
 public void testGetOptionCount() {
 // Given
 when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<Object>() {{ add("Option 1"); add("Option 2"); }});
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);

 // Then
 assertEquals(2, dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testGetLabel() {
 // Given
 when(label.eval(any(Object.class), any(Component.class))).thenReturn("Label 1");
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.setObjects(new ArrayList<Object>() {{ add(new Object() {{}}); add(new Object() {{}}); }});

 // When
 String label1 = dynamicSelectModel.getLabel(0);
 String label2 = dynamicSelectModel.getLabel(1);

 // Then
 assertEquals("Label 1", label1);
 assertEquals("Label 1", label2); // Same label for all options, since label is just a toString() call
 }

 @Test
 public void testGetValue() {
 // Given
 when(value.eval(any(Object.class), any(Component.class))).thenReturn("Value 1");
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.setObjects(new ArrayList<Object>() {{ add(new Object() {{}}); add(new Object() {{}}); }});

 // When
 String value1 = dynamicSelectModel.getValue(0);
 String value2 = dynamicSelectModel.getValue(1);

 // Then
 assertEquals("Value 1", value1);
 assertEquals("Value 1", value2); // Same value for all options, since value is just a toString() call
 }

 @Test
 public void testTranslateValue() {
 // Given
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.setObjects(new ArrayList<Object>() {{ add(new Object() {{}}); add(new Object() {{}}); add(new Object() {{}}); }});
 when(translator.eval(any(Object.class), any(Component.class))).thenReturn("Translated Value");

 // When
 Object translatedValue = dynamicSelectModel.translateValue("Value 1");

 // Then
 assertEquals("Translated Value", translatedValue);
 }

 @Test
 public void testGetObjects() {
 // Given
 when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<Object>() {{ add(new Object() {{}}); add(new Object() {{}}); }});
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);

 // When
 List<Object> objects = dynamicSelectModel.getObjects();

 // Then
 assertEquals(2, objects.size());
 }

 @Test
 public void testGetObjectsWithEmptyOption() {
 // Given
 when(collection.eval(topLevelComponent, component)).thenReturn(new LinkedList<Object>() {{ add(new Object() {{}}); add("Empty Option"); add(new Object() {{}}); }});
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.setEmptyObject(new Object() {{}});

 // When
 List<Object> objects = dynamicSelectModel.getObjects();

 // Then
 assertEquals(3, objects.size());
 }

 @Test
 public void testGetComponent() {
 // Given
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 dynamicSelectModel.setComponent(component);

 // When
 Component result = dynamicSelectModel.getComponent();

 // Then
 assertEquals(component, result);
 }

 @Test
 public void testSetComponent() {
 // Given
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 Component componentToSet = new Component();
 dynamicSelectModel.setComponent(componentToSet);

 // When
 Component result = dynamicSelectModel.getComponent();

 // Then
 assertEquals(componentToSet, result);
 }

 @Test
 public void testGetTopLevelComponent() {
 // Given
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 StandaloneComponent topLevelComponentToSet = new StandaloneComponent();
 dynamicSelectModel.setTopLevelComponent(topLevelComponentToSet);

 // When
 StandaloneComponent result = dynamicSelectModel.getTopLevelComponent();

 // Then
 assertEquals(topLevelComponentToSet, result);
 }

 @Test
 public void testSetTopLevelComponent() {
 // Given
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 StandaloneComponent topLevelComponentToSet = new StandaloneComponent();
 dynamicSelectModel.setTopLevelComponent(topLevelComponentToSet);

 // When
 StandaloneComponent result = dynamicSelectModel.getTopLevelComponent();

 // Then
 assertEquals(topLevelComponentToSet, result);
 }

 @Test
 public void testValidate() {
 // Given
 dynamicSelectModel = new DynamicSelectModel();

 // When
 try {
 dynamicSelectModel.validate();
 fail("Validation should have thrown WheelException");
 } catch (WheelException e) {
 // Then
 assertTrue(e.getMessage().contains("Could not create DynamicSelection model. Collection attribute was null or not an el-expression."));
 }

 // Reset state
 dynamicSelectModel = new DynamicSelectModel();

 // When
 try {
 dynamicSelectModel.validate();
 fail("Validation should have thrown WheelException");
 } catch (WheelException e) {
 // Then
 assertTrue(e.getMessage().contains("Could not create DynamicSelection model. Label attribute was null or not an el-expression."));
 }

 // Reset state
 dynamicSelectModel = new DynamicSelectModel();

 // When
 try {
 dynamicSelectModel.validate();
 fail("Validation should have thrown WheelException");
 } catch (WheelException e) {
 // Then
 assertTrue(e.getMessage().contains("Could not create DynamicSelection model. Value attribute was null or not an el-expression."));
 }
 }

 @Test
 public void testReset() {
 // Given
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setObjects(new ArrayList<Object>() {{ add(new Object() {{}}); add(new Object() {{}}); }});

 // When
 dynamicSelectModel.reset();

 // Then
 assertNull(dynamicSelectModel.getObjects());
 }
}