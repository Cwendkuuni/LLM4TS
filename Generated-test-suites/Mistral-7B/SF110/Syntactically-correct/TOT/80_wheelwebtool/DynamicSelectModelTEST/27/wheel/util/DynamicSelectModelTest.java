package wheel.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.*;

import wheel.*;
import wheel.components.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component component;
 private ElExpression label;
 private ElExpression value;
 private ElExpression collection;
 private Object[] collectionArray;
 private Collection collectionMock;

 @Before
 public void setup() {
 collection = mock(ElExpression.class);
 label = mock(ElExpression.class);
 value = mock(ElExpression.class);
 component = mock(Component.class);
 collectionMock = new ArrayList<>();
 collectionArray = new Object[] { new Object(), new Object() };
 when(collection.eval(component, component)).thenReturn(collectionArray);

 model = new DynamicSelectModel();
 model.collection("myCollection");
 model.label("myLabel");
 model.value("myValue");
 model.translator(null);
 model.setComponent(component);
 }

 @Test
 public void testGetOptionCount() {
 model.setCollection(new ArrayList<>());
 model.setObjects(model.getObjects());
 Assert.assertEquals(0, model.getOptionCount());

 model.setCollection("new ArrayList<Object>()");
 model.setObjects(model.getObjects());
 model.getObjects().add(new Object());
 model.getObjects().add(new Object());
 Assert.assertEquals(2, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsNullCollection() {
 model.setCollection(null);
 model.getObjects();
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsInvalidCollection() {
 model.setCollection(new ElExpression("invalidCollection"));
 model.getObjects();
 }

 @Test
 public void testGetLabelValidIndex() {
 model.setObjects(collectionMock);
 model.setLabel("toString()");
 when(label.eval(anyObject(), anyObject())).thenReturn("Label 1");
 model.getLabel(0);
 verify(label).eval(eq(collectionArray[0]), eq(component));
 }

 @Test(expected = WheelException.class)
 public void testGetLabelInvalidIndex() {
 model.getLabel(3);
 }

 @Test
 public void testGetValueValidIndex() {
 model.setObjects(collectionMock);
 model.setValue("ordinal()");
 when(value.eval(anyObject(), anyObject())).thenReturn("Value 1");
 model.getValue(0);
 verify(value).eval(eq(collectionArray[0]), eq(component));
 }

 @Test
 public void testGetValueInvalidIndex() {
 model.getValue(3);
 }

 @Test
 public void testTranslateValueValidValue() {
 model.setObjects(collectionMock);
 model.setTranslator("myTranslator");
 when(translator.eval(anyObject(), anyObject())).thenReturn("Translated value");
 model.translateValue("Value 1");
 verify(translator).eval(eq(collectionArray[0]), eq(component));
 }

 @Test
 public void testTranslateValueInvalidValue() {
 model.translateValue("Invalid value");
 }

 @Test
 public void testGetObjects() {
 model.setCollection("new ArrayList<Object>()");
 model.setObjects(model.getObjects());
 Assert.assertNotNull(model.getObjects());
 }

 @Test
 public void testSetTopLevelComponent() {
 model.setTopLevelComponent(new StandaloneComponent());
 }

}