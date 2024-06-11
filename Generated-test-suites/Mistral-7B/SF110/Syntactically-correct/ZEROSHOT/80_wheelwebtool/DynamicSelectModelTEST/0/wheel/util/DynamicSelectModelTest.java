package wheel.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;;

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
 private Collection objects;

 @Before
 public void setUp() {
 dynamicSelectModel = new DynamicSelectModel();
 Mockito.when(collection.eval(topLevelComponent, component)).thenReturn(objects);
 dynamicSelectModel.collection(collection);
 dynamicSelectModel.label("label");
 dynamicSelectModel.value("value");
 }

 @After
 public void tearDown() {
 dynamicSelectModel = null;
 }

 @Test
 public void testGetOptionCount() {
 Mockito.when(objects.size()).thenReturn(3);
 assertEquals(3, dynamicSelectModel.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testGetOptionCountWhenObjectsIsNull() {
 dynamicSelectModel.objects = null;
 dynamicSelectModel.validate();
 }

 @Test
 public void testGetLabel() {
 Mockito.when(objects.toArray()[0].toString()).thenReturn("option1");
 assertEquals("option1", dynamicSelectModel.getLabel(0));
 }

 @Test(expected = WheelException.class)
 public void testGetLabelWhenObjectsArrayIsNull() {
 dynamicSelectModel.objects = null;
 dynamicSelectModel.getLabel(0);
 }

 @Test
 public void testGetValue() {
 Mockito.when(objects.toArray()[0].toString()).thenReturn("option1Value");
 assertEquals("option1Value", dynamicSelectModel.getValue(0));
 }

 @Test(expected = WheelException.class)
 public void testGetValueWhenObjectsArrayIsNull() {
 dynamicSelectModel.objects = null;
 dynamicSelectModel.getValue(0);
 }

 @Test
 public void testTranslateValue() {
 Mockito.when(objects.toArray()[0].toString()).thenReturn("option1Value");
 when(translator.eval(any(Object.class), eq(component))).thenReturn("translatedOption1Value");
 assertEquals("translatedOption1Value", dynamicSelectModel.translateValue("option1Value"));
 }

 @Test
 public void testTranslateValueWhenNullReturned() {
 when(objects.toArray()[0].toString()).thenReturn("option1Value");
 when(translator.eval(any(Object.class), eq(component))).thenReturn(null);
 assertNull(dynamicSelectModel.translateValue("option1Value"));
 }

 @Test(expected = WheelException.class)
 public void testTranslateValueWhenTranslatorIsNull() {
 dynamicSelectModel.translator = null;
 dynamicSelectModel.translateValue("optionValue");
 }

 @Test
 public void testGetObjects() {
 List<Object> list = new ArrayList<>();
 list.add("option1");
 list.add("option2");
 Mockito.when(objects.toArray()).thenReturn(list.toArray());
 assertEquals(list, dynamicSelectModel.getObjects());
 }

 @Test
 public void testGetComponent() {
 assertEquals(component, dynamicSelectModel.getComponent());
 }

 @Test
 public void testSetComponent() {
 dynamicSelectModel.setComponent(component);
 assertEquals(component, dynamicSelectModel.getComponent());
 }

 @Test
 public void testGetTopLevelComponent() {
 assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
 }

 @Test
 public void testSetTopLevelComponent() {
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 assertEquals(topLevelComponent, dynamicSelectModel.getTopLevelComponent());
 }

 @Test(expected = WheelException.class)
 public void testValidateWhenCollectionIsNull() {
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class) public void testValidateWhenLabelIsNull() { dynamicSelectModel.validate(); }

 @Test(expected = WheelException.class) public void testValidateWhenValueIsNull() { dynamicSelectModel.validate(); }
}