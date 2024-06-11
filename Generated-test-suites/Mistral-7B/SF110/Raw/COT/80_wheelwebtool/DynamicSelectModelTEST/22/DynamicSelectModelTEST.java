package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import wheel.components.Component;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

 private static final String COLLECTION_EXPRESSION = "new ArrayList() {add('A'); add('B'); add('C');}";
 private static final String LABEL_EXPRESSION = "toString()";
 private static final String VALUE_EXPRESSION = "ordinal()";
 private static final String ENUMERATION_EXPRESSION = "java.lang.Enum.class.getEnumConstants()[0].getClass().getSuperclass().getDeclaredMethod(\"values\").invoke(null)";
 private static final String TRANSLATOR_EXPRESSION = "toString()";
 private static final String EMPTY_OBJECT = "new Object()";

 @Test
 public void testConstructorWithCollectionLabelValue() {
 DynamicSelectModel model = new DynamicSelectModel(new ElExpression(COLLECTION_EXPRESSION), new ElExpression(LABEL_EXPRESSION), new ElExpression(VALUE_EXPRESSION));
 assertEquals(3, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testInvalidConstructor() {
 new DynamicSelectModel();
 }

 @Test
 public void testCollectionMethod() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.collection("testCollection");
 assertEquals("testCollection", model.getCollection().getExpression());
 }

 @Test
 public void testLabelMethod() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.label("testLabel");
 assertEquals("testLabel", model.getLabel().getExpression());
 }

 @Test
 public void testValueMethod() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.value("testValue");
 assertEquals("testValue", model.getValue().getExpression());
 }

 @Test
 public void testEnumerationMethod() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.enumeration("java.lang.Enum.class");
 assertEquals("java.lang.Enum.class.getEnumConstants()[0].getClass().getSuperclass().getDeclaredMethod(\"values\").invoke(null)", model.getCollection().getExpression());
 assertEquals("toString()", model.getLabel().getExpression());
 assertEquals("ordinal()", model.getValue().getExpression());
 }

 @Test
 public void testTranslatorMethod() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.translator("testTranslator");
 assertEquals("testTranslator", model.getTranslator().getExpression());
 }

 @Test
 public void testEmptyMethod() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.empty(new ElExpression(EMPTY_OBJECT));
 assertEquals(2, model.getOptionCount());
 assertEquals(model.getEmptyObject(), model.getValue(0));
 }

 @Test
 public void testGetObjectsMethodWithCollection() {
 DynamicSelectModel model = new DynamicSelectModel(new ElExpression(COLLECTION_EXPRESSION), new ElExpression(LABEL_EXPRESSION), new ElExpression(VALUE_EXPRESSION));
 Collection objects = model.getObjects();
 assertNotNull(objects);
 assertEquals(3, objects.size());
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsMethodWithNullCollection() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.collection(null);
 model.getObjects();
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsMethodWithInvalidCollection() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.collection(new ElExpression("invalidCollectionExpression"));
 model.getObjects();
 }

 @Test
 public void testGetObjectsMethodWithObjectArray() {
 DynamicSelectModel model = new DynamicSelectModel(new ElExpression(COLLECTION_EXPRESSION), new ElExpression(LABEL_EXPRESSION), new ElExpression(VALUE_EXPRESSION));
 Object[] array = model.getObjects().toArray();
 assertNotNull(array);
 assertEquals(3, array.length);
 }

 @Test
 public void testGetObjectsMethodWithLinkedList() {
 DynamicSelectModel model = new DynamicSelectModel(new ElExpression(COLLECTION_EXPRESSION), new ElExpression(LABEL_EXPRESSION), new ElExpression(VALUE_EXPRESSION));
 List<Object> list = (List<Object>)model.getObjects();
 assertNotNull(list);
 assertEquals(3, list.size());
 }

 @Test
 public void testGetLabelMethod() {
 DynamicSelectModel model = new DynamicSelectModel(new ElExpression(COLLECTION_EXPRESSION), new ElExpression(LABEL_EXPRESSION), new ElExpression(VALUE_EXPRESSION));
 assertEquals("A", model.getLabel(0));
 assertEquals("B", model.getLabel(1));
 assertEquals("C", model.getLabel(2));
 }

 @Test(expected = WheelException.class)
 public void testGetLabelMethodWithInvalidIndex() {
 DynamicSelectModel model = new DynamicSelectModel(new ElExpression(COLLECTION_EXPRESSION), new ElExpression(LABEL_EXPRESSION), new ElExpression(VALUE_EXPRESSION));
 model.getLabel(3);
 }

 @Test
 public void testGetValueMethod() {
 DynamicSelectModel model = new DynamicSelectModel(new ElExpression(COLLECTION_EXPRESSION), new ElExpression(LABEL_EXPRESSION), new ElExpression(VALUE_EXPRESSION));
 assertEquals("A", model.getValue(0));
 assertEquals("B", model.getValue(1));
 assertEquals("C", model.getValue(2));
 }

 @Test(expected = WheelException.class)
 public void testGetValueMethodWithInvalidIndex() {
 DynamicSelectModel model = new DynamicSelectModel(new ElExpression(COLLECTION_EXPRESSION), new ElExpression(LABEL_EXPRESSION), new ElExpression(VALUE_EXPRESSION));
 model.getValue(3);
 }

 @Test
 public void testTranslateValueMethodWithValidValue() {
 DynamicSelectModel model = new DynamicSelectModel(new ElExpression(COLLECTION_EXPRESSION), new ElExpression(LABEL_EXPRESSION), new ElExpression(VALUE_EXPRESSION));
 assertEquals("A", model.translateValue("A"));
 assertEquals("B", model.translateValue("B"));
 assertEquals("C", model.translateValue("C"));
 }

 @Test(expected = WheelException.class)
 public void testTranslateValueMethodWithInvalidValue() {
 DynamicSelectModel model = new DynamicSelectModel(new ElExpression(COLLECTION_EXPRESSION), new ElExpression(LABEL_EXPRESSION), new ElExpression(VALUE_EXPRESSION));
 model.translateValue("D");
 }

 @Test
 public void testValidateMethod() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateMethodWithNullCollection() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.collection(null);
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateMethodWithNullLabel() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.label(null);
 model.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateMethodWithNullValue() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.value(null);
 model.validate();
 }

 @Test
 public void testGetComponentMethod() {
 Component component = new Component();
 DynamicSelectModel model = new DynamicSelectModel();
 model.setComponent(component);
 assertEquals(component, model.getComponent());
 }

 @Test
 public void testSetComponentMethod() {
 Component component = new Component();
 DynamicSelectModel model = new DynamicSelectModel();
 model.setComponent(component);
 }

 @Test
 public void testGetTopLevelComponentMethod() {
 StandaloneComponent topLevelComponent = new StandaloneComponent();
 DynamicSelectModel model = new DynamicSelectModel();
 model.setTopLevelComponent(topLevelComponent);
 assertEquals(topLevelComponent, model.getTopLevelComponent());
 }

 @Test
 public void testSetTopLevelComponentMethod() {
 StandaloneComponent topLevelComponent = new StandaloneComponent();
 DynamicSelectModel model = new DynamicSelectModel();
 model.setTopLevelComponent(topLevelComponent);
 }

 @Test
 public void testResetMethod() {
 DynamicSelectModel model = new DynamicSelectModel();
 model.reset();
 assertNull(model.getObjects());
 }
}