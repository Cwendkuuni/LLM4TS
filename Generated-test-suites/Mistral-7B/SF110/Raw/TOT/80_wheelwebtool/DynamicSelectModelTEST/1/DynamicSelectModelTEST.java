package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.exceptions.WheelException;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setup() {
 component = new Component();
 topLevelComponent = new StandaloneComponent();
}

 @Test
 public void testGetOptionCount_ValidCollection() {
 // Given: A DynamicSelectModel with a valid collection
 model = new DynamicSelectModel()
 .collection("new ArrayList<String>()")
 .label("toString()")
 .value("toString()")
 .getComponent(component)
 .setTopLevelComponent(topLevelComponent);
 List<String> objects = Collections.singletonList("Option 1");
 model.setObjects(objects);

 // When: Getting the option count
 int optionCount = model.getOptionCount();

 // Then: The option count should be equal to the size of the objects collection
 assertEquals(1, optionCount);
 }

 @Test
 public void testGetLabel_ValidIndex() {
 // Given: A DynamicSelectModel with valid label expression and index
 model = new DynamicSelectModel()
 .collection("new ArrayList<String>()")
 .label("toString()")
 .value("toString()")
 .getComponent(component)
 .setTopLevelComponent(topLevelComponent);
 List<String> objects = Collections.singletonList("Option 1");
 model.setObjects(objects);

 // When: Getting the label for a valid index
 String label = model.getLabel(0);

 // Then: The label should be the string representation of the object at the index
 assertEquals("Option 1", label);
 }

 @Test(expected = WheelException.class)
 public void testGetLabel_InvalidIndex() {
 // Given: A DynamicSelectModel with valid label expression and an invalid index
 model = new DynamicSelectModel()
 .collection("new ArrayList<String>()")
 .label("toString()")
 .value("toString()")
 .getComponent(component)
 .setTopLevelComponent(topLevelComponent);
 List<String> objects = Collections.singletonList("Option 1");
 model.setObjects(objects);

 // When: Getting the label for an invalid index
 model.getLabel(-1);

 // Then: A WheelException should be thrown
}

 @Test
 public void testgetValue_ValidIndex() {
 // Given: A DynamicSelectModel with valid value expression and index
 model = new DynamicSelectModel()
 .collection("new ArrayList<String>()")
 .label("toString()")
 .value("toString()")
 .getComponent(component)
 .setTopLevelComponent(topLevelComponent);
 List<String> objects = Collections.singletonList("Option 1");
 model.setObjects(objects);

 // When: Getting the value for a valid index
 String value = model.getValue(0);

 // Then: The value should be the string representation of the object at the index
 assertEquals("Option 1", value);
 }

 @Test(expected = WheelException.class)
 public void testgetValue_InvalidIndex() {
 // Given: A DynamicSelectModel with valid value expression and an invalid index
 model = new DynamicSelectModel()
 .collection("new ArrayList<String>()")
 .label("toString()")
 .value("toString()")
 .getComponent(component)
 .setTopLevelComponent(topLevelComponent);
 List<String> objects = Collections.singletonList("Option 1");
 model.setObjects(objects);

 // When: Getting the value for an invalid index
 model.getValue(-1);

 // Then: A WheelException should be thrown
 }

 @Test
 public void testTranslateValue_ValidValue() {
 // Given: A DynamicSelectModel with a valid translator expression and value
 model = new DynamicSelectModel()
 .collection("new ArrayList<String>()")
 .label("toString()")
 .value("toString()")
 .translator("new String() { public String toString() { return 'Translated ' + value; } }")
 .getComponent(component)
 .setTopLevelComponent(topLevelComponent);
 List<String> objects = Collections.singletonList("Option 1");
 model.setObjects(objects);

 // When: Translating a valid value
 String translatedValue = model.translateValue("Option 1");

 // Then: The translated value should be "Translated Option 1"
 assertEquals("Translated Option 1", translatedValue);
 }

 @Test
 public void testTranslateValue_InvalidValue() {
 // Given: A DynamicSelectModel with a valid translator expression and an invalid value
 model = new DynamicSelectModel()
 .collection("new ArrayList<String>()")
 .label("toString()")
 .value("toString()")
 .translator("new String() { public String toString() { return 'Translated ' + value; } }")
 .getComponent(component)
 .setTopLevelComponent(topLevelComponent);
 List<String> objects = Collections.singletonList("Option 1");
 model.setObjects(objects);

 // When: Translating an invalid value
 model.translateValue("Invalid Value");

 // Then: Nothing should be returned
 assertNull(model.translateValue("Invalid Value"));
 }

 @Test
 public void testGetObjects_ValidCollection() {
 // Given: A DynamicSelectModel with a valid collection
 model = new DynamicSelectModel()
 .collection("new ArrayList<String>()")
 .label("toString()")
 .value("toString()")
 .getComponent(component)
 .setTopLevelComponent(topLevelComponent);
 List<String> objects = Collections.singletonList("Option 1");
 model.setObjects(objects);

 // When: Getting the objects
 List<Object> actualObjects = model.getObjects();

 // Then: The objects should be the same as the original collection
 assertEquals(objects, actualObjects);
 }

 @Test
 public void testGetObjects_EmptyCollection() {
 // Given: A DynamicSelectModel with an empty collection
 model = new DynamicSelectModel()
 .collection("new ArrayList<String>()")
 .label("toString()")
 .value("toString()")
 .getComponent(component)
 .setTopLevelComponent(topLevelComponent);
 List<String> objects = Collections.emptyList();
 model.setObjects(objects);

 // When: Getting the objects
 List<Object> actualObjects = model.getObjects();

 // Then: The objects should be empty
 assertTrue(actualObjects.isEmpty());
 }

 @Test
 public void testGetObjects_MixedCollection() {
 // Given: A DynamicSelectModel with a mixed collection
 model = new DynamicSelectModel()
 .collection("new ArrayList<Object>()")
 .label("toString()")
 .value("toString()")
 .getComponent(component)
 .setTopLevelComponent(topLevelComponent);
 List<Object> objects = new ArrayList<>();
 objects.add("Option 1");
 objects.add(1);
 model.setObjects(objects);

 // When: Getting the objects
 List<Object> actualObjects = model.getObjects();

 // Then: The objects should be the same as the original collection
 assertEquals(objects, actualObjects);
 }

 @Test
 public void testGetObjects_InvalidCollection() {
 // Given: A DynamicSelectModel with an invalid collection
 model = new DynamicSelectModel()
 .collection("new ArrayList<String>()")
 .label("toString()")
 .value("toString()")
 .getComponent(component)
 .setTopLevelComponent(topLevelComponent);
 List<Object> objects = new ArrayList<>();
 objects.add(new Object());
 model.setObjects(objects);

 // When: Getting the objects
 List<Object> actualObjects = model.getObjects();

 // Then: An exception should be thrown
 try {
 assertEquals(objects, actualObjects);
 } catch (AssertionError e) {
 fail("Unexpected assertion failure: " + e.getMessage());
 }

 throw new AssertionError("Expected an exception");
 }
}