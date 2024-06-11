package wheel.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 @Mock
 private Component component;
 @Mock
 private StandaloneComponent topLevelComponent;

 @Before
 public void setup() {
 dynamicSelectModel = new DynamicSelectModel();
 }

 @Test
 public void testEmptyConstructor() {
 // Test empty constructor
 dynamicSelectModel = new DynamicSelectModel();
 }

 @Test(expected = WheelException.class)
 public void testInvalidConstructor() {
 // Test constructor with null label, collection or value
 dynamicSelectModel = new DynamicSelectModel(null, null, null);
 }

 @Test
 public void testValidConstructor() {
 // Test valid constructor
 dynamicSelectModel = new DynamicSelectModel(mock(ElExpression.class), mock(ElExpression.class), mock(ElExpression.class));
 }

 @Test
 public void testCollectionMethod() {
 // Test collection method
 dynamicSelectModel.collection("testCollection");
 verify(component).errorMessage("Could not evaluate expression testCollection.values() to create a dynamic ISelectModel.");
 }

 @Test
 public void testLabelMethod() {
 // Test label method
 dynamicSelectModel.label("testLabel");
 verify(component).errorMessage("Could not evaluate expression testLabel to create a label for dynamic ISelectModel.");
 }

 @Test
 public void testValueMethod() {
 // Test value method
 dynamicSelectModel.value("testValue");
 verify(component).errorMessage("Could not evaluate expression testValue to create a value for dynamic ISelectModel.");
 }

 @Test
 public void testEnumMethod() {
 // Test enumeration method
 dynamicSelectModel.enumeration("testEnumeration");
 verify(component).errorMessage("Could not evaluate expression testEnumeration.values() to create a dynamic ISelectModel.");
 }

 @Test
 public void testTranslatorMethod() {
 // Test translator method
 dynamicSelectModel.translator("testTranslator");
 verify(component).errorMessage("Could not evaluate expression testTranslator to translate value ' ordinal()'");
 }

 @Test
 public void testEmptyMethod() {
 // Test empty method
 dynamicSelectModel.empty(new Object());
 }

 @Test
 public void testGetOptionCount() {
 // Test getOptionCount method
 List<Object> options = new ArrayList<>();
 options.add("Option 1");
 options.add("Option 2");
 when(collection.eval(topLevelComponent, component)).thenReturn(options.toArray());
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.collection("options");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 int optionCount = dynamicSelectModel.getOptionCount();
 verify(component, times(1)).errorMessage("Could not evaluate expression options.values() to create a dynamic ISelectModel.");
 verify(topLevelComponent, times(1)).eval(collection.getExpression(), component);
 assertEquals(optionCount, options.size());
 }

 @Test
 public void testGetLabel() {
 // Test getLabel method
 List<Object> options = new ArrayList<>();
 options.add("Option 1");
 options.add("Option 2");
 when(collection.eval(topLevelComponent, component)).thenReturn(options.toArray());
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.collection("options");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 String label = dynamicSelectModel.getLabel(0);
 verify(component, times(1)).errorMessage("Could not evaluate expression toString() to create a label for dynamic ISelectModel. Index was 0");
 verify(options.get(0), times(1)).toString();
 assertEquals(label, "Option 1");
 }

 @Test
 public void testGetValue() {
 // Test getValue method
 List<Object> options = new ArrayList<>();
 options.add("Option 1");
 options.add("Option 2");
 when(collection.eval(topLevelComponent, component)).thenReturn(options.toArray());
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.collection("options");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 Object value = dynamicSelectModel.getValue(0);
 verify(component, times(1)).errorMessage("Could not evaluate expression ordinal() to create a value for dynamic ISelectModel. Index was 0");
 verify(options.get(0), times(1)).ordinal();
 assertEquals(value.toString(), "0");
 }

 @Test
 public void testTranslateValue() {
 // Test translateValue method
 List<Object> options = new ArrayList<>();
 options.add("Option 1");
 options.add("Option 2");
 when(collection.eval(topLevelComponent, component)).thenReturn(options.toArray());
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.collection("options");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 dynamicSelectModel.translator("testTranslator");
 Object translatedValue = dynamicSelectModel.translateValue("0");
 verify(component, times(1)).errorMessage("Could not evaluate expression testTranslator to translate value ' 0'");
 verify(options.get(0), times(1)).ordinal();
 assertEquals(translatedValue, options.get(0));
 }

 @Test
 public void testGetObjects() {
 // Test getObjects method
 List<Object> options = new ArrayList<>();
 options.add("Option 1");
 options.add("Option 2");
 when(collection.eval(topLevelComponent, component)).thenReturn(options.toArray());
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.collection("options");
 dynamicSelectModel.label("toString");
 dynamicSelectModel.value("ordinal");
 Collection objects = dynamicSelectModel.getObjects();
 verify(component, times(1)).errorMessage("Could not evaluate expression options.values() to create a dynamic ISelectModel.");
 verify(topLevelComponent, times(1)).eval(collection.getExpression(), component);
 assertEquals(objects.size(), options.size() + 1);
 verify(objects).contains(dynamicSelectModel.emptyObject);
 verify(options, times(options.size())).toString();
 }

 @Test
 public void testReset() {
 // Test reset method
 dynamicSelectModel.reset();
 assertNull(dynamicSelectModel.getObjects());
 }
}