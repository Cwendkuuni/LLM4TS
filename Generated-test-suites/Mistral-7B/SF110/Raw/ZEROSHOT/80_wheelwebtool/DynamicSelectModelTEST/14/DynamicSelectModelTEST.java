package wheel.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockITOA;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DynamicSelectModel.class})
public class DynamicSelectModelTest {

 private static final ElExpression COLLECTION_EXPRESSION = mockITOA("collectionExpression");
 private static final ElExpression LABEL_EXPRESSION = mockITOA("labelExpression");
 private static final ElExpression VALUE_EXPRESSION = mockITOA("valueExpression");
 private static final ElExpression ENUMERATION_EXPRESSION = mockITOA("enumerationExpression");
 private static final ElExpression TRANSLATOR_EXPRESSION = mockITOA("translatorExpression");
 private static final Object EMPTY_OBJECT = mockITOA("emptyObject");
 private static final Component COMPONENT = mockITOA(Component.class);
 private static final StandaloneComponent TOP_LEVEL_COMPONENT = mockITOA(StandaloneComponent.class);
 private static final Collection OBJECTS = new ArrayList();

 @Test
 public void testDynamicSelectModel_ValidConstruction() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(COLLECTION_EXPRESSION, LABEL_EXPRESSION, VALUE_EXPRESSION);
 assertNull(dynamicSelectModel.getObjects());
 assertSame(COLLECTION_EXPRESSION, dynamicSelectModel.getCollection());
 assertSame(LABEL_EXPRESSION, dynamicSelectModel.getLabel());
 assertSame(VALUE_EXPRESSION, dynamicSelectModel.getValue());
 assertNull(dynamicSelectModel.getTopLevelComponent());
 assertNull(dynamicSelectModel.getComponent());
 }

 @Test
 public void testDynamicSelectModel_EmptyConstruction() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 assertNull(dynamicSelectModel.getObjects());
 assertNull(dynamicSelectModel.getCollection());
 assertNull(dynamicSelectModel.getLabel());
 assertNull(dynamicSelectModel.getValue());
 assertNull(dynamicSelectModel.getTopLevelComponent());
 assertNull(dynamicSelectModel.getComponent());
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModel_NullCollection() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(null, LABEL_EXPRESSION, VALUE_EXPRESSION);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModel_NullLabel() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(COLLECTION_EXPRESSION, null, VALUE_EXPRESSION);
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModel_NullValue() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel(COLLECTION_EXPRESSION, LABEL_EXPRESSION, null);
 }

 @Test
 public void testDynamicSelectModel_CollectionMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("collection");
 assertSame("collection", dynamicSelectModel.getCollection());
 }

 @Test
 public void testDynamicSelectModel_LabelMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.label("label");
 assertSame("label", dynamicSelectModel.getLabel());
 }

 @Test
 public void testDynamicSelectModel_ValueMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.value("value");
 assertSame("value", dynamicSelectModel.getValue());
 }

 @Test
 public void testDynamicSelectModel_EnumerateMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.enumeration("enumeration");
 assertSame("enumeration.values()", dynamicSelectModel.getCollection());
 assertSame("toString()", dynamicSelectModel.getLabel());
 assertSame("ordinal()", dynamicSelectModel.getValue());
 }

 @Test
 public void testDynamicSelectModel_TranslatorMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.translator("translator");
 assertSame("translator", dynamicSelectModel.getTranslator());
 }

 @Test
 public void testDynamicSelectModel_EmptyMethod() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.empty(EMPTY_OBJECT);
 assertNotNull(dynamicSelectModel.getObjects());
 assertFalse(dynamicSelectModel.getObjects().isEmpty());
 assertSame(EMPTY_OBJECT, dynamicSelectModel.getObjects().get(0));
 }

 @Test
 public void testDynamicSelectModel_GetOptionCount() {
 given(OBJECTS.size()).willReturn(5);
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setObjects(OBJECTS);
 assertEquals(5, dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testDynamicSelectModel_GetLabel() {
 given(LABEL_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("label1");
 given(LABEL_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("label2");
 given(LABEL_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("label3");
 given(LABEL_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("label4");
 given(LABEL_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("label5");
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setObjects(OBJECTS);
 dynamicSelectModel.setComponent(COMPONENT);
 List<String> labels = new ArrayList<>();
 for (int i = 0; i < 5; i++) {
 labels.add(dynamicSelectModel.getLabel(i));
 }
 assertEquals(5, labels.size());
 assertEquals("label1", labels.get(0));
 assertEquals("label2", labels.get(1));
 assertEquals("label3", labels.get(2));
 assertEquals("label4", labels.get(3));
 assertEquals("label5", labels.get(4));
 }

 @Test
 public void testDynamicSelectModel_GetValue() {
 given(VALUE_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("value1");
 given(VALUE_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("value2");
 given(VALUE_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("value3");
 given(VALUE_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("value4");
 given(VALUE_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("value5");
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setObjects(OBJECTS);
 dynamicSelectModel.setComponent(COMPONENT);
 List<String> values = new ArrayList<>();
 for (int i = 0; i < 5; i++) {
 values.add(dynamicSelectModel.getValue(i));
 }
 assertEquals(5, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 assertEquals("value3", values.get(2));
 assertEquals("value4", values.get(3));
 assertEquals("value5", values.get(4));
 }

 @Test
 public void testDynamicSelectModel_TranslateValue() {
 given(TRANSLATOR_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("translatedValue");
 given(TRANSLATOR_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("translatedValue2");
 given(TRANSLATOR_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("translatedValue3");
 given(TRANSLATOR_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("translatedValue4");
 given(TRANSLATOR_EXPRESSION.eval(anyObject(), eq(COMPONENT))).willReturn("translatedValue5");
 given(OBJECTS.toArray()[0].toString()).willReturn("object1");
 given(OBJECTS.toArray()[1].toString()).willReturn("object2");
 given(OBJECTS.toArray()[2].toString()).willReturn("object3");
 given(OBJECTS.toArray()[3].toString()).willReturn("object4");
 given(OBJECTS.toArray()[4].toString()).willReturn("object5");
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setObjects(OBJECTS);
 dynamicSelectModel.setComponent(COMPONENT);
 dynamicSelectModel.setTranslator(TRANSLATOR_EXPRESSION);
 List<Object> translatedValues = new ArrayList<>();
 for (int i = 0; i < 5; i++) {
 translatedValues.add(dynamicSelectModel.translateValue("value" + i));
 }
 assertEquals(5, translatedValues.size());
 assertEquals("translatedValue1", translatedValues.get(0).toString());
 assertEquals("translatedValue2", translatedValues.get(1).toString());
 assertEquals("translatedValue3", translatedValues.get(2).toString());
 assertEquals("translatedValue4", translatedValues.get(3).toString());
 assertEquals("translatedValue5", translatedValues.get(4).toString());
 }

 @Test
 public void testDynamicSelectModel_SetTopLevelComponent() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setTopLevelComponent(TOP_LEVEL_COMPONENT);
 assertSame(TOP_LEVEL_COMPONENT, dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testDynamicSelectModel_SetComponent() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setComponent(COMPONENT);
 assertSame(COMPONENT, dynamicSelectModel.getComponent());
 }

 @Test
 public void testDynamicSelectModel_Validate() {
 PowerMockito.spy(DynamicSelectModel.class);
 DynamicSelectModel spyDynamicSelectModel = PowerMockito.spy(new DynamicSelectModel());
 spyDynamicSelectModel.validate();
 PowerMockito.verifyNewCapture(ElExpression.class).called();
 PowerMockito.verifyNewCapture(ElExpression.class).called();
 PowerMockito.verifyNewCapture(ElExpression.class).called();
 }

 @Test
 public void testDynamicSelectModel_Reset() {
 DynamicSelectModel dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setObjects(OBJECTS);
 dynamicSelectModel.reset();
 assertNull(dynamicSelectModel.getObjects());
 }
}