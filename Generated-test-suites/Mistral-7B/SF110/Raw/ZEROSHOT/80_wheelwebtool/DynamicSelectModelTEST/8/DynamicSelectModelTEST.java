package wheel.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import wheel.components.Component;
import wheel.components.StandaloneComponent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 @Mock
 private Component component;
 @Mock
 private StandaloneComponent topLevelComponent;
 @Mock
 private ElExpression collection;
 @Mock
 private ElExpression label;
 @Mock
 private ElExpression value;
 @Mock
 private ElExpression translator;
 @Mock
 private Object emptyObject;

 @Before
 public void setUp() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 }

 @Test
 public void testDynamicSelectModelWithCollectionLabelAndValue() {
 when(collection.getExpression()).thenReturn("testCollection");
 when(label.getExpression()).thenReturn("testLabel");
 when(value.getExpression()).thenReturn("testValue");

 dynamicSelectModel.collection(collection);
 dynamicSelectModel.label(label);
 dynamicSelectModel.value(value);

 assertEquals(0, dynamicSelectModel.getOptionCount());

 dynamicSelectModel.validate();

 verify(collection, times(1)).getExpression();
 verify(label, times(1)).getExpression();
 verify(value, times(1)).getExpression();
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithNullCollection() {
 dynamicSelectModel.collection(null);

 dynamicSelectModel.validate();

 verify(component, times(1)).error("Could not create DynamicSelection model. Collection attribute was null or not an el-expression.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithNullLabel() {
 dynamicSelectModel.label(null);

 dynamicSelectModel.validate();

 verify(component, times(1)).error("Could not create DynamicSelection model. Label attribute was null or not an el-expression.");
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithNullValue() {
 dynamicSelectModel.value(null);

 dynamicSelectModel.validate();

 verify(component, times(1)).error("Could not create DynamicSelection model. Value attribute was null or not an el-expression.");
 }

 @Test
 public void testDynamicSelectModelWithCollectionEnumerationLabelAndValue() {
 when(collection.getExpression()).thenReturn("testEnumerable.values()");
 when(label.getExpression()).thenReturn("toString()");
 when(value.getExpression()).thenReturn("ordinal()");

 dynamicSelectModel.collection(collection);
 dynamicSelectModel.label(label);
 dynamicSelectModel.value(value);

 assertEquals(5, dynamicSelectModel.getOptionCount());

 dynamicSelectModel.validate();

 verify(collection, times(1)).getExpression();
 verify(label, times(1)).getExpression();
 verify(value, times(1)).getExpression();
 }

 @Test(expected = WheelException.class)
 public void testDynamicSelectModelWithNullTranslator() {
 dynamicSelectModel.translator(null);

 dynamicSelectModel.validate();

 verify(component, times(1)).error("Could not create DynamicSelection model. Translator attribute was null or not an el-expression.");
 }

 @Test
 public void testDynamicSelectModelWithEmptyCollection() {
 when(collection.getExpression()).thenReturn("new ArrayList()");

 dynamicSelectModel.collection(collection);
 dynamicSelectModel.validate();

 assertEquals(0, dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testDynamicSelectModelWithCollectionAndObjects() {
 when(collection.getExpression()).thenReturn("testCollection");
 when(label.getExpression()).thenReturn("toString()");
 when(value.getExpression()).thenReturn("value");
 when(component.eval(Mockito.anyObject(), Mockito.anyObject())).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList() {{
 add("Option1");
 add("Option2");
 add("Option3");
}});

 dynamicSelectModel.collection(collection);
 dynamicSelectModel.label(label);
 dynamicSelectModel.value(value);

 assertEquals("Option1", dynamicSelectModel.getLabel(0));
 assertEquals("Option1", dynamicSelectModel.getValue(0));
 assertEquals("Option2", dynamicSelectModel.getLabel(1));
 assertEquals("Option2", dynamicSelectModel.getValue(1));
 assertEquals("Option3", dynamicSelectModel.getLabel(2));
 assertEquals("Option3", dynamicSelectModel.getValue(2));
 }

 @Test
 public void testDynamicSelectModelWithCollectionAndEmptyObject() {
 when(collection.getExpression()).thenReturn("testCollection");
 when(label.getExpression()).thenReturn("toString()");
 when(value.getExpression()).thenReturn("value");
 when(component.eval(Mockito.anyObject(), Mockito.anyObject())).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList() {{
 add("Option1");
 add("Option2");
 add("Option3");
 add(emptyObject);
}});

 dynamicSelectModel.collection(collection);
 dynamicSelectModel.label(label);
 dynamicSelectModel.value(value);
 dynamicSelectModel.empty(emptyObject);

 assertEquals("Option1", dynamicSelectModel.getLabel(0));
 assertEquals("Option1", dynamicSelectModel.getValue(0));
 assertEquals("Option2", dynamicSelectModel.getLabel(1));
 assertEquals("Option2", dynamicSelectModel.getValue(1));
 assertEquals("Option3", dynamicSelectModel.getLabel(2));
 assertEquals("Option3", dynamicSelectModel.getValue(2));
 assertEquals(emptyObject.toString(), dynamicSelectModel.getLabel(3));
 assertEquals(emptyObject, dynamicSelectModel.getValue(3));
 }

 @Test
 public void testTranslateValue() {
 when(collection.getExpression()).thenReturn("testCollection");
 when(label.getExpression()).thenReturn("toString()");
 when(value.getExpression()).thenReturn("value");
 when(translator.getExpression()).thenReturn("translateValue");
 when(component.eval(Mockito.anyObject(), Mockito.anyObject())).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList() {{
 add(Mockito.mock(Object.class));
 add(Mockito.mock(Object.class));
 add(Mockito.mock(Object.class));
 add(Mockito.mock(Object.class));
}});

 dynamicSelectModel.collection(collection);
 dynamicSelectModel.label(label);
 dynamicSelectModel.value(value);
 dynamicSelectModel.translator(translator);

 Object option1 = Mockito.mock(Object.class);
 Object option2 = Mockito.mock(Object.class);
 Object option3 = Mockito.mock(Object.class);
 Object option4 = Mockito.mock(Object.class);

 when(value.eval(option1, component)).thenReturn("Option1Value");
 when(value.eval(option2, component)).thenReturn("Option2Value");
 when(value.eval(option3, component)).thenReturn("Option3Value");
 when(value.eval(option4, component)).thenReturn("Option4Value");

 assertEquals("Option1Value", dynamicSelectModel.translateValue("Option1Value"));
 assertEquals(option1, dynamicSelectModel.translateValue("Option1Value"));
 assertEquals("Option2Value", dynamicSelectModel.translateValue("Option2Value"));
 assertEquals(option2, dynamicSelectModel.translateValue("Option2Value"));
 assertEquals("Option3Value", dynamicSelectModel.translateValue("Option3Value"));
 assertEquals(option3, dynamicSelectModel.translateValue("Option3Value"));
 assertEquals(null, dynamicSelectModel.translateValue("nonExistingValue"));
 }
}