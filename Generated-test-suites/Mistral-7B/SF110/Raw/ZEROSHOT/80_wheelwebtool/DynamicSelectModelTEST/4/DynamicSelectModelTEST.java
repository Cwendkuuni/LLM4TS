package wheel.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import wheel.components.*;
import wheel.*;

@RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private StandaloneComponent topLevelComponent;
 private ElExpression label;
 private ElExpression value;
 private ElExpression collection;
 private ElExpression translator;
 private Collection objects;

 @Before
 public void setUp() {
 component = mock(Component.class);
 topLevelComponent = mock(StandaloneComponent.class);
 label = mock(ElExpression.class);
 value = mock(ElExpression.class);
 collection = mock(ElExpression.class);
 translator = mock(ElExpression.class);
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 }

 @Test
 public void testConstructorWithCollectionLabelValue() {
 dynamicSelectModel = new DynamicSelectModel(collection, label, value);
 assertEquals(collection, dynamicSelectModel.getCollection());
 assertEquals(label, dynamicSelectModel.getLabel());
 assertEquals(value, dynamicSelectModel.getValue());
 }

 @Test
 public void testConstructorWithEmptyArguments() {
 dynamicSelectModel = new DynamicSelectModel();
 assertNull(dynamicSelectModel.getCollection());
 assertNull(dynamicSelectModel.getLabel());
 assertNull(dynamicSelectModel.getValue());
 }

 @Test
 public void testCollectionMethod() {
 dynamicSelectModel.collection("testCollection");
 assertEquals("testCollection", dynamicSelectModel.getCollection().getExpression());
 }

 @Test
 public void testLabelMethod() {
 dynamicSelectModel.label("testLabel");
 assertEquals("testLabel", dynamicSelectModel.getLabel().getExpression());
 }

 @Test
 public void testValueMethod() {
 dynamicSelectModel.value("testValue");
 assertEquals("testValue", dynamicSelectModel.getValue().getExpression());
 }

 @Test
 public void testEnumsMethod() {
 dynamicSelectModel.enumeration("MyEnum");
 assertEquals("MyEnum.values()", dynamicSelectModel.getCollection().getExpression());
 assertEquals("toString()", dynamicSelectModel.getLabel().getExpression());
 assertEquals("ordinal()", dynamicSelectModel.getValue().getExpression());
 }

 @Test
 public void testTranslatorMethod() {
 dynamicSelectModel.translator("MyTranslator");
 assertEquals("MyTranslator", dynamicSelectModel.getTranslator().getExpression());
 }

 @Test
 public void testEmptyMethod() {
 dynamicSelectModel.empty(new Object());
 assertNotNull(dynamicSelectModel.getObjects());
 assertEquals(2, dynamicSelectModel.getObjects().size());
 }

 @Test
 public void testGetOptionCount() {
 when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<Object>() {{ add("Option1"); add("Option2"); }});
 assertEquals(2, dynamicSelectModel.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsWithNullCollection() {
 dynamicSelectModel.setCollection(null);
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsWithInvalidCollectionType() {
 when(collection.eval(topLevelComponent, component)).thenReturn(1234);
 dynamicSelectModel.validate();
 }

 @Test
 public void testGetObjects() {
 when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<Object>() {{ add("Option1"); add("Option2"); }});
 assertEquals(2, dynamicSelectModel.getObjects().size());
 }

 @Test
 public void testGetLabel() {
 when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<Object>() {{ add("Option1"); add("Option2"); }});
 when(label.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("Label1");
 assertEquals("Label1", dynamicSelectModel.getLabel(0));
 }

 @Test
 public void testGetValue() {
 when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<Object>() {{ add("Option1"); add("Option2"); }});
 when(value.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("Value1");
 assertEquals("Value1", dynamicSelectModel.getValue(0));
 }

 @Test
 public void testTranslateValue() {
 when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList<Object>() {{ add("Option1"); add("Option2"); }});
 when(value.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("Value1");
 when(translator.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("TranslatedValue1");
 assertEquals("TranslatedValue1", dynamicSelectModel.translateValue("Value1"));
 }
}