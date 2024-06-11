package wheel.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import wheel.*;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setUp() {
 topLevelComponent = Mockito.mock(StandaloneComponent.class);
 component = Mockito.mock(Component.class);
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 }

 @Test
 public void testGetOptionCount() {
 Collection collection = Arrays.asList("Option1", "Option2", "Option3");
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 dynamicSelectModel.setObjects(collection);

 assertEquals(3, dynamicSelectModel.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testGetOptionCountWhenObjectsIsNull() {
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 dynamicSelectModel.setObjects(null);
 }

 @Test
 public void testGetLabel() {
 Collection collection = Arrays.asList("Option1", "Option2", "Option3");
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 dynamicSelectModel.setObjects(collection);

 assertEquals("Option1", dynamicSelectModel.getLabel(0));
 assertEquals("Option2", dynamicSelectModel.getLabel(1));
 assertEquals("Option3", dynamicSelectModel.getLabel(2));
 }

 @Test(expected = WheelException.class)
 public void testGetLabelWhenIndexOutOfBounds() {
 Collection collection = Arrays.asList("Option1", "Option2", "Option3");
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 dynamicSelectModel.setObjects(collection);

 dynamicSelectModel.getLabel(3);
 }

 @Test
 public void testgetValue() {
 Collection collection = Arrays.asList(1, 2, 3);
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 dynamicSelectModel.setObjects(collection);

 assertEquals("1", dynamicSelectModel.getValue(0));
 assertEquals("2", dynamicSelectModel.getValue(1));
 assertEquals("3", dynamicSelectModel.getValue(2));
 }

 @Test(expected = WheelException.class)
 public void testgetValueWhenIndexOutOfBounds() {
 Collection collection = Arrays.asList(1, 2, 3);
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 dynamicSelectModel.setObjects(collection);

 dynamicSelectModel.getValue(3);
 }

 @Test
 public void testTranslateValue() {
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 dynamicSelectModel.translator("translator");
 dynamicSelectModel.setObjects(Arrays.asList(1, 2, 3));

 assertEquals(1, dynamicSelectModel.translateValue("1").hashCode());
 assertEquals(2, dynamicSelectModel.translateValue("2").hashCode());
 assertEquals(3, dynamicSelectModel.translateValue("3").hashCode());
 }

 @Test
 public void testTranslateValueWhenValueNotFound() {
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 dynamicSelectModel.translator("translator");
 dynamicSelectModel.setObjects(Arrays.asList(1, 2, 3));

 assertNull(dynamicSelectModel.translateValue("4"));
 }

 @Test
 public void testGetObjects() {
 Collection collection = Arrays.asList("Option1", "Option2", "Option3");
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);

 List<Object> objects = dynamicSelectModel.getObjects();
 assertEquals("Option1", objects.get(0));
 assertEquals("Option2", objects.get(1));
 assertEquals("Option3", objects.get(2));
 }

 @Test
 public void testSetObjects() {
 Collection collection = Arrays.asList("Option1", "Option2", "Option3");
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");

 dynamicSelectModel.setObjects(collection);
 List<Object> objects = dynamicSelectModel.getObjects();
 assertEquals("Option1", objects.get(0));
 assertEquals("Option2", objects.get(1));
 assertEquals("Option3", objects.get(2));
 }

 @Test
 public void testReset() {
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 dynamicSelectModel.setObjects(Arrays.asList("Option1", "Option2", "Option3"));
 dynamicSelectModel.reset();
 assertNull(dynamicSelectModel.getObjects());
 }

 @Test
 public void testValidate() {
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWhenCollectionIsNull() {
 dynamicSelectModel.collection(null);
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWhenLabelIsNull() {
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label(null);
 dynamicSelectModel.value("value");
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWhenValueIsNull() {
 dynamicSelectModel.collection("collection");
 dynamicSelectModel.label("label");
 dynamicSelectModel.value(null);
 dynamicSelectModel.validate();
 }
}