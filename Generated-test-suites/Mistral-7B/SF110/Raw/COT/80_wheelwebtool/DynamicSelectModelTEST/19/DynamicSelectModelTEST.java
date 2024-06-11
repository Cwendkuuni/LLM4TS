package wheel.util.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.util.DynamicSelectModel;

public class DynamicSelectModelTest {

 private Component component;
 private StandaloneComponent topLevelComponent;

 @Test
 public void testConstructorWithCollectionLabelValue() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
 assertNotNull(dynamicSelectModel);
 }

 @Test
 public void testConstructor() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 assertNotNull(dynamicSelectModel);
 }

 @Test
 public void testCollection() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("collection");
 assertSame(dynamicSelectModel.getCollection(), new ElExpression("collection"));
 }

 @Test
 public void testLabel() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.label("label");
 assertSame(dynamicSelectModel.getLabel(), new ElExpression("label"));
 }

 @Test
 public void testValue() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.value("value");
 assertSame(dynamicSelectModel.getValue(), new ElExpression("value"));
 }

 @Test
 public void testEnumeration() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("enumeration.values()");
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("ordinal()");
 assertNotNull(dynamicSelectModel.getLabel());
 assertNotNull(dynamicSelectModel.getValue());
 }

 @Test
 public void testTranslator() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.translator("translator");
 assertSame(dynamicSelectModel.getTranslator(), new ElExpression("translator"));
 }

 @Test
 public void testEmpty() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.empty(new Object());
 assertNotNull(dynamicSelectModel.getEmptyObject());
 }

 @Test(expected = WheelException.class)
 public void testInvalidCollection() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(null);
 }

 @Test(expected = WheelException.class)
 public void testInvalidLabel() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.label(null);
 }

 @Test(expected = WheelException.class)
 public void testInvalidValue() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.value(null);
 }

 @Test(expected = WheelException.class)
 public void testInvalidEnumeration() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("enumeration");
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("ordinal()");
 }

 @Test(expected = WheelException.class)
 public void testInvalidTranslator() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.translator(null);
 }

 @Test
 public void testGetOptionCount() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(new ElExpression("new ArrayList<String>()"));
 Collection objects = dynamicSelectModel.getObjects();
 assertEquals(0, dynamicSelectModel.getOptionCount());
 objects.add("Option1");
 assertEquals(1, dynamicSelectModel.getOptionCount());
 objects.add("Option2");
 assertEquals(2, dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testGetLabel() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(new ElExpression("new ArrayList<String>()"));
 Collection objects = dynamicSelectModel.getObjects();
 objects.add(new ElExpression("Label1"));
 assertEquals("Label1", dynamicSelectModel.getLabel(0));
 }

 @Test
 public void testGetValue() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(new ElExpression("new ArrayList<String>()"));
 Collection objects = dynamicSelectModel.getObjects();
 objects.add(new ElExpression("Value1"));
 assertEquals("Value1", dynamicSelectModel.getValue(0));
 }

 @Test
 public void testTranslateValue() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(new ElExpression("new ArrayList<String>()"));
 dynamicSelectModel.collection.eval(new Object[]{"Value1", "Value2", "Value3"}, component);
 dynamicSelectModel.translateValue("Value1");
 assertSame(dynamicSelectModel.getObjects().get(0), dynamicSelectModel.translateValue("Value1"));
 }

 @Test
 public void testTranslateValueNull() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(new ElExpression("new ArrayList<String>()"));
 dynamicSelectModel.translateValue(null);
 assertNull(dynamicSelectModel.translateValue(null));
 }

 @Test
 public void testGetObjects() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(new ElExpression("new ArrayList<String>()"));
 dynamicSelectModel.collection.eval(new Object[]{"Object1", "Object2"}, topLevelComponent);
 Collection objects = dynamicSelectModel.getObjects();
 assertEquals(2, objects.size());
 }

 @Test
 public void testGetComponent() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 Component component = new Component();
 dynamicSelectModel.setComponent(component);
 assertSame(dynamicSelectModel.getComponent(), component);
 }

 @Test
 public void testSetComponent() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 Component component = new Component();
 dynamicSelectModel.setComponent(component);
 }

 @Test
 public void testGetTopLevelComponent() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 StandaloneComponent topLevelComponent = new StandaloneComponent();
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 assertSame(dynamicSelectModel.getTopLevelComponent(), topLevelComponent);
 }

 @Test
 public void testSetTopLevelComponent() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 StandaloneComponent topLevelComponent = new StandaloneComponent();
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 }

 @Test
 public void testValidate() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 try {
 dynamicSelectModel.validate();
 } catch (WheelException e) {
 fail("Validate should not throw exception when all attributes are set.");
 }
 }

 @Test(expected = WheelException.class)
 public void testValidateNullCollection() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 try {
 dynamicSelectModel.validate();
 } catch (WheelException e) {
 assertEquals("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.", e.getMessage());
 }
 }

 @Test(expected = WheelException.class)
 public void testValidateNullLabel() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 try {
 dynamicSelectModel.validate();
 } catch (WheelException e) {
 assertEquals("Could not create DynamicSelection model. Label attribute was null or not an el-expression.", e.getMessage());
 }
 }

 @Test(expected = WheelException.class)
 public void testValidateNullValue() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 try {
 dynamicSelectModel.validate();
 } catch (WheelException e) {
 assertEquals("Could not create DynamicSelection model. Value attribute was null or not an el-expression.", e.getMessage());
 }
 }

 @Test
 public void testReset() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection(new ElExpression("new ArrayList<String>()"));
 Collection objects = dynamicSelectModel.getObjects();
 assertEquals(0, dynamicSelectModel.getOptionCount());
 dynamicSelectModel.reset();
 assertEquals(0, dynamicSelectModel.getOptionCount());
 }
}