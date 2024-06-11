package wheel.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import wheel.Component;
import wheel.StandaloneComponent;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setUp() {
 component = mock(Component.class);
 topLevelComponent = mock(StandaloneComponent.class);
 model = new DynamicSelectModel();
 model.setComponent(component);
 model.setTopLevelComponent(topLevelComponent);
 }

 @Test
 public void testConstructorWithCollectionLabelValue() {
 model.collection("collection");
 model.label("label");
 model.value("value");
 model = model.build(); // assume there's a build method here

 assertEquals(3, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testInvalidConstructor() {
 model.collection(null);
 model.label(null);
 model.value(null);
 model = model.build();
 }

 @Test
 public void testCollectionMethod() {
 model.collection("collection");
 assertEquals("collection", model.getCollection().getExpression());
 }

 @Test
 public void testLabelMethod() {
 model.label("label");
 assertEquals("label", model.getLabel().getExpression());
 }

 @Test
 public void testValueMethod() {
 model.value("value");
 assertEquals("value", model.getValue().getExpression());
 }

 @Test
 public void testEnumsMethod() {
 model.enumeration("MyEnum");
 assertEquals("MyEnum.values()", model.getCollection().getExpression());
 assertEquals("toString()", model.getLabel().getExpression());
 assertEquals("ordinal()", model.getValue().getExpression());
 }

 @Test
 public void testTranslatorMethod() {
 model.translator("translator");
 assertEquals("translator", model.getTranslator().getExpression());
 }

 @Test
 public void testEmptyMethod() {
 model.empty("empty");
 verify(component).errorMessage("Could not evaluate expression toString() to create a label for dynamic ISelectModel. Index was -1");
 }

 @Test
 public void testGetObjects() {
 model.collection("collection");
 model.label("label");
 model.value("value");
 model = model.build();

 Collection objects = model.getObjects();
 assertEquals(3, objects.size());
 }

 @Test
 public void testGetLabel() {
 model.collection("collection");
 model.label("label");
 model.value("value");
 model = model.build();

 String label1 = model.getLabel(0);
 assertEquals("label for first option", label1);
 }

 @Test
 public void testGetValue() {
 model.collection("collection");
 model.label("label");
 model.value("value");
 model = model.build();

 String value1 = model.getValue(0);
 assertEquals("value for first option", value1);
 }

 @Test
 public void testTranslateValue() {
 model.collection("collection");
 model.label("label");
 model.value("value");
 model.translator("translator");
 model = model.build();

 Object translatedValue = model.translateValue("valueForOption");
 assertEquals("translated value for option", translatedValue);
 }

 @Test
 public void testGetObjectsWhenCollectionIsEmpty() {
 model.collection("emptyCollection");
 model.label("label");
 model.value("value");
 model = model.build();

 Collection objects = model.getObjects();
 assertTrue(objects.isEmpty());
 }

 @Test
 public void testGetObjectsWhenCollectionIsEmptyAndEmptyOptionIsSet() {
 model.collection(new ArrayList<>());
 model.label("label");
 model.value("value");
 model.empty("empty");
 model = model.build();

 Collection objects = model.getObjects();
 assertTrue(objects.contains("empty"));
 }
}