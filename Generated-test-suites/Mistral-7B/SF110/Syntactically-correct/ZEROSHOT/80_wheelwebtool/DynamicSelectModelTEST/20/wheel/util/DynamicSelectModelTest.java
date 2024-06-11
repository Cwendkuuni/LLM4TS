package wheel.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DynamicSelectModelTest {

 @Before
 public void setup() {
 component = mock(Component.class);
 topLevelComponent = mock(StandaloneComponent.class);
 }

 private Component component;
 private StandaloneComponent topLevelComponent;
 private DynamicSelectModel dynamicSelectModel;

 @Test
 public void testDefaultConstructor() {
 dynamicSelectModel = new DynamicSelectModel();
 assertNull(dynamicSelectModel.getLabel(0));
 assertNull(dynamicSelectModel.getValue(0));
 assertNull(dynamicSelectModel.getObjects());
 }

 @Test
 public void testConstructorWithCollectionLabelValue() {
 dynamicSelectModel = new DynamicSelectModel(new ElExpression("collection"), new ElExpression("label"), new ElExpression("value"));
 Collection objects = Arrays.asList("Option1", "Option2", "Option3").asList();
 when(component.eval(Mockito.anyObject(), Mockito.eq(component))).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(component.eval(Mockito.anyObject(), Mockito.eq(component))).thenAnswer(invocation -> invocation.getArguments()[1]);
 when(topLevelComponent.eval(Mockito.anyObject(), Mockito.eq(component))).thenReturn(objects);

 assertEquals("Option1", dynamicSelectModel.getLabel(0));
 assertEquals("Option1", dynamicSelectModel.getValue(0));
 assertEquals(objects, dynamicSelectModel.getObjects());
 }

 @Test(expected = WheelException.class)
 public void testInvalidConstructor() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection = null;
 when(component.eval(Mockito.anyObject(), Mockito.eq(component))).thenAnswer(invocation -> invocation.getArguments()[0]);
 dynamicSelectModel.validate();
 }

 @Test
 public void testCollection() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("collection");
 Collection objects = Arrays.asList("Option1", "Option2", "Option3").asList();
 when(component.eval(Mockito.anyObject(), Mockito.eq(component))).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(topLevelComponent.eval(Mockito.anyObject(), Mockito.eq(component))).thenReturn(objects);

 assertEquals(dynamicSelectModel.collection, new ElExpression("collection"));
 }

 @Test
 public void testLabel() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.label("label");
 assertEquals(dynamicSelectModel.label, new ElExpression("label"));
 }

 @Test
 public void testValue() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.value("value");
 assertEquals(dynamicSelectModel.value, new ElExpression("value"));
 }

 @Test
 public void testEnums() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("MyEnum.values()");
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("ordinal()");
 Collection objects = Arrays.asList(MyEnum.VALUE1, MyEnum.VALUE2, MyEnum.VALUE3).asList();
 when(component.eval(Mockito.anyObject(), Mockito.eq(component))).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(topLevelComponent.eval(Mockito.anyObject(), Mockito.eq(component))).thenReturn(objects);

 assertEquals("VALUE1", dynamicSelectModel.getLabel(0));
 assertEquals("1", dynamicSelectModel.getValue(0));
 assertEquals(objects, dynamicSelectModel.getObjects());
 }

 @Test
 public void testTranslator() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("ordinal()");
 dynamicSelectModel.translator("translator");
 Collection objects = Arrays.asList("Option1", "Option2", "Option3").asList();
 when(component.eval(Mockito.anyObject(), Mockito.eq(component))).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(topLevelComponent.eval(Mockito.anyObject(), Mockito.eq(component))).thenReturn(objects);
 when(component.eval(Mockito.anyObject(), Mockito.eq(component))).thenAnswer(invocation -> invocation.getArguments()[1]);

 assertEquals("translatedOption1", dynamicSelectModel.translateValue("Option1"));
 }

 @Test
 public void testEmpty() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("ordinal()");
 dynamicSelectModel.empty("emptyOption");
 Collection objects = new ArrayList<>();
 objects.add("Option1");
 objects.add("Option2");
 objects.add("emptyOption");
 when(component.eval(Mockito.anyObject(), Mockito.eq(component))).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(topLevelComponent.eval(Mockito.anyObject(), Mockito.eq(component))).thenReturn(objects.toArray());

 assertEquals("emptyOption", dynamicSelectModel.getLabel(2));
 assertEquals("2", dynamicSelectModel.getValue(2));
 }

 @Test
 public void testGetObjects() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("ordinal()");
 Collection objects = Arrays.asList("Option1", "Option2", "Option3").asList();
 when(component.eval(Mockito.anyObject(), Mockito.eq(component))).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(topLevelComponent.eval(Mockito.anyObject(), Mockito.eq(component))).thenReturn(objects.toArray());

 assertEquals(objects, dynamicSelectModel.getObjects());
 }

 @Test
 public void testGetOptionCount() {
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("ordinal()");
 Collection objects = Arrays.asList("Option1", "Option2", "Option3").asList();
 when(component.eval(Mockito.anyObject(), Mockito.eq(component))).thenAnswer(invocation -> invocation.getArguments()[0]);
 when(topLevelComponent.eval(Mockito.anyObject(), Mockito.eq(component))).thenReturn(objects.toArray());

 assertEquals(3, dynamicSelectModel.getOptionCount());
 }
}