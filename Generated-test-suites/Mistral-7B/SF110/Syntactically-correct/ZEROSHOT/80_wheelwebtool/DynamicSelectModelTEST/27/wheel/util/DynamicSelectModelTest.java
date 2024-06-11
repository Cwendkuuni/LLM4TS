package wheel.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import wheel.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setUp() {
 topLevelComponent = Mockito.mock(StandaloneComponent.class);
 component = Mockito.mock(Component.class);
 model = new DynamicSelectModel();
 model.setComponent(component);
 model.setTopLevelComponent(topLevelComponent);
 }

 @Test
 public void testConstructorWithCollectionLabelValue() {
 model.collection("testCollection");
 model.label("testLabel");
 model.value("testValue");
 List<Object> testCollection = Arrays.asList("Option1", "Option2", "Option3");
 model.setObjects(testCollection);
 assertEquals(3, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testValidateThrowsExceptionWhenCollectionIsNull() {
 model.validate();
 model.collection(null);
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateThrowsExceptionWhenLabelIsNull() {
 model.validate();
 model.label(null);
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateThrowsExceptionWhenValueIsNull() {
 model.validate();
 model.value(null);
 model.validate();
 }

 @Test
 public void testGetLabel() {
 model.collection("testCollection");
 model.label("toString()");
 model.value("ordinal()");
 model.setObjects(Arrays.asList("Option1", "Option2", "Option3"));
 assertEquals("Option1", model.getLabel(0));
 }

 @Test(expected = RuntimeException.class)
 public void testGetLabelThrowsRuntimeExceptionWhenExpressionEvaluationFails() {
 model.collection("testCollection");
 model.label("throw new RuntimeException()");
 model.value("ordinal()");
 model.setObjects(Arrays.asList("Option1", "Option2", "Option3"));
 }

 @Test
 public void testGetValue() {
 model.collection("testCollection");
 model.label("toString()");
 model.value("ordinal()");
 model.setObjects(Arrays.asList("Option1", "Option2", "Option3"));
 assertEquals("Option1", model.getValue(0));
 }

 @Test(expected = RuntimeException.class)
 public void testGetValueThrowsRuntimeExceptionWhenExpressionEvaluationFails() {
 model.collection("testCollection");
 model.label("throw new RuntimeException()");
 model.value("ordinal()");
 model.setObjects(Arrays.asList("Option1", "Option2", "Option3"));
 }

 @Test
 public void testTranslateValue() {
 model.collection("testCollection");
 model.label("toString()");
 model.value("ordinal()");
 model.translator("testTranslator");
 model.setObjects(Arrays.asList("Option1", "Option2", "Option3"));
 assertEquals("Option1", model.translateValue("0"));
 }

 @Test(expected = RuntimeException.class)
 public void testTranslateValueThrowsRuntimeExceptionWhenExpressionEvaluationFails() {
 model.collection("testCollection");
 model.label("toString()");
 model.value("ordinal()");
 model.translator("throw new RuntimeException()");
 model.setObjects(Arrays.asList("Option1", "Option2", "Option3"));
 }

 @Test
 public void testGetObjects() {
 model.collection("testCollection");
 model.label("toString()");
 model.value("ordinal()");
 List<Object> testCollection = Arrays.asList("Option1", "Option2", "Option3");
 model.setObjects(testCollection);
 assertEquals(testCollection, model.getObjects());
 }

 @Test
 public void testGetObjectsWhenCollectionIsAnArray() {
 model.collection(new String[]{"Option1", "Option2", "Option3"});
 model.label("toString()");
 model.value("ordinal()");
 List<Object> testCollection = Arrays.asList("Option1", "Option2", "Option3");
 model.setObjects(testCollection);
 assertEquals(testCollection, model.getObjects());
 }

 @Test
 public void testGetObjectsWhenCollectionIsEmpty() {
 model.collection(new ArrayList<>());
 model.label("toString()");
 model.value("ordinal()");
 List<Object> testCollection = Arrays.asList();
 model.setObjects(testCollection);
 assertEquals(testCollection, model.getObjects());
 }

 @Test
 public void testReset() {
 model.collection("testCollection");
 model.label("toString()");
 model.value("ordinal()");
 model.setObjects(Arrays.asList("Option1", "Option2", "Option3"));
 model.reset();
 assertNull(model.getObjects());
 }
}