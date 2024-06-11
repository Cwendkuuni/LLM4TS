package wheel.util;

import static org.junit.Assert.*;

import org.junit.Test;

import wheel.components.*;
import wheel.*;
import java.util.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private ElExpression collectionExpression, labelExpression, valueExpression;

 @Test
 public void testDynamicSelectModelConstructorWithCollectionLabelValue() {
 dynamicSelectModel = new DynamicSelectModel(new ElExpression("['apple', 'banana', 'orange']"), new ElExpression("'label'"), new ElExpression("'value'"));
 assertNotNull(dynamicSelectModel);
 }

 @Test
 public void testDynamicSelectModelEmptyConstructor() {
 dynamicSelectModel = new DynamicSelectModel();
 assertNotNull(dynamicSelectModel);
 }

 @Test
 public void testDynamicSelectModelCollectionMethod() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("['apple', 'banana', 'orange']");
 assertEquals("['apple', 'banana', 'orange']", dynamicSelectModel.collection.getExpression());
 }

 @Test
 public void testDynamicSelectModelLabelMethod() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.label("'label'");
 assertEquals("'label'", dynamicSelectModel.label.getExpression());
 }

 @Test
 public void testDynamicSelectModelValueMethod() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.value("'value'");
 assertEquals("'value'", dynamicSelectModel.value.getExpression());
 }

 @Test
 public void testDynamicSelectModelEnumerateMethod() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("java.lang.Enum.class");
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("ordinal()");
 dynamicSelectModel.enumeration("MyEnumClass");
 assertEquals("MyEnumClass.values().toString()", dynamicSelectModel.collection.getExpression());
 assertEquals("toString()", dynamicSelectModel.label.getExpression());
 assertEquals("ordinal()", dynamicSelectModel.value.getExpression());
 }

 @Test
 public void testDynamicSelectModelTranslatorMethod() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.translator("'translator'");
 assertEquals("'translator'", dynamicSelectModel.translator.getExpression());
 }

 @Test
 public void testDynamicSelectModelEmptyMethod() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.empty(new Object());
 assertNotNull(dynamicSelectModel.getObjects());
 assertTrue(dynamicSelectModel.getObjects().contains(dynamicSelectModel.emptyObject));
 }

 @Test(expected = WheelException.class)
 public void testInvalidDynamicSelectModelCollection() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(new ElExpression("invalidCollection"));
 }

 @Test(expected = WheelException.class)
 public void testInvalidDynamicSelectModelLabel() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.label(new ElExpression("invalidLabel"));
 }

 @Test(expected = WheelException.class)
 public void testInvalidDynamicSelectModelValue() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.value(new ElExpression("invalidValue"));
 }

 @Test(expected = WheelException.class)
 public void testEmptyCollectionDynamicSelectModel() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(new ElExpression("[]"));
 assertEquals(0, dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testGetLabel() {
 component = new Component();
 dynamicSelectModel = new DynamicSelectModel(new ElExpression("['apple', 'banana']"), new ElExpression("'label'"), new ElExpression("'value'"));
 dynamicSelectModel.setComponent(component);
 labelExpression = new ElExpression("'index' + 1");
 assertEquals("label for index 0", dynamicSelectModel.getLabel(0));
 }

 @Test
 public void testGetValue() {
 component = new Component();
 dynamicSelectModel = new DynamicSelectModel(new ElExpression("['apple', 'banana']"), new ElExpression("'label'"), new ElExpression("'value'"));
 dynamicSelectModel.setComponent(component);
 valueExpression = new ElExpression("'index'");
 assertEquals("value for index 0", dynamicSelectModel.getValue(0));
 }

 @Test
 public void testTranslateValueSuccess() {
 component = new Component();
 dynamicSelectModel = new DynamicSelectModel(new ElExpression("['Apple', 'Banana']"), new ElExpression("'label'"), new ElExpression("'value'"));
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.translator(new ElExpression("'translator'"));
 assertEquals("translated value for 'Apple'", dynamicSelectModel.translateValue("Apple"));
 }

 @Test
 public void testTranslateValueFailure() {
 component = new Component();
 dynamicSelectModel = new DynamicSelectModel(new ElExpression("['Apple', 'Banana']"), new ElExpression("'label'"), new ElExpression("'value'"));
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.translator(new ElExpression("'translator'"));
 assertNull(dynamicSelectModel.translateValue("Orange"));
 }

 @Test
 public void testGetObjects() {
 component = new Component();
 dynamicSelectModel = new DynamicSelectModel(new ElExpression("['Apple', 'Banana']"), new ElExpression("'label'"), new ElExpression("'value'"));
 dynamicSelectModel.setComponent(component);
 assertEquals(2, dynamicSelectModel.getOptionCount());
 List<Object> list = new ArrayList<>();
 list.add("Apple");
 list.add("Banana");
 assertEquals(list, dynamicSelectModel.getObjects());
 }

 @Test
 public void testGetComponent() {
 component = new Component();
 dynamicSelectModel = new DynamicSelectModel(new ElExpression("['Apple', 'Banana']"), new ElExpression("'label'"), new ElExpression("'value'"));
 dynamicSelectModel.setComponent(component);
 assertEquals(component, dynamicSelectModel.getComponent());
 }

 @Test
 public void testSetComponent() {
 component = new Component();
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setComponent(component);
 assertEquals(component, dynamicSelectModel.getComponent());
 }

 @Test
 public void testGetTopLevelComponent() {
 topLevelComponent = new StandaloneComponent();
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testSetTopLevelComponent() {
 topLevelComponent = new StandaloneComponent();
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testValidate() {
 dynamicSelectModel = new DynamicSelectModel();
 try {
 dynamicSelectModel.validate();
 } catch (WheelException e) {
 fail("validate method should not throw an exception when all attributes are initialized");
 }
 }

 @Test(expected = WheelException.class)
 public void testValidateMissingCollection() {
 dynamicSelectModel = new DynamicSelectModel();
 try {
 dynamicSelectModel.validate();
 } catch (WheelException e) {
 assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
 }
 }

 @Test(expected = WheelException.class)
 public void testValidateMissingLabel() {
 dynamicSelectModel = new DynamicSelectModel();
 labelExpression = new ElExpression("'label'");
 dynamicSelectModel.label = labelExpression;
 try {
 dynamicSelectModel.validate();
 } catch (WheelException e) {
 assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
 }
 }

 @Test(expected = WheelException.class)
 public void testValidateMissingValue() {
 dynamicSelectModel = new DynamicSelectModel();
 valueExpression = new ElExpression("'value'");
 dynamicSelectModel.value = valueExpression;
 try {
 dynamicSelectModel.validate();
 } catch (WheelException e) {
 assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
 }
 }

 @Test
 public void testReset() {
 dynamicSelectModel = new DynamicSelectModel(new ElExpression("['Apple', 'Banana']"), new ElExpression("'label'"), new ElExpression("'value'"));
 dynamicSelectModel.setComponent(new Component());
 assertEquals(2, dynamicSelectModel.getOptionCount());
 dynamicSelectModel.reset();
 assertEquals(0, dynamicSelectModel.getOptionCount());
 }
}