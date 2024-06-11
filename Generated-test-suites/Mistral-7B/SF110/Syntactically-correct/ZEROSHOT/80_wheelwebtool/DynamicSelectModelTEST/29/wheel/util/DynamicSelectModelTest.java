package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 @Mock
 private Component component;
 @Mock
 private StandaloneComponent topLevelComponent;
 @Mock
 private ElExpression label;
 @Mock
 private ElExpression value;
 @Mock
 private ElExpression collection;
 @Mock
 private ElExpression translator;
 @Mock
 private Collection objects;

 @Before
 public void setUp() {
 dynamicSelectModel = new DynamicSelectModel();
 Mockito.when(label.getExpression()).thenReturn("labelExpression");
 Mockito.when(value.getExpression()).thenReturn("valueExpression");
 Mockito.when(collection.getExpression()).thenReturn("collectionExpression");
 dynamicSelectModel.collection("collectionExpression");
 dynamicSelectModel.label("labelExpression");
 dynamicSelectModel.value("valueExpression");
 }

 @Test
 public void testGetOptionCount() {
 Mockito.when(objects.size()).thenReturn(5);
 assertEquals(5, dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testGetLabel() {
 Mockito.when(objects.toArray()[0]).thenReturn(new Object() {
 @Override
 public String toString() {
 return "Option 1";
 }
 });
 Mockito.when(label.eval(Mockito.anyObject(), Mockito.eq(component))).thenReturn("Label 1");
 assertEquals("Label 1", dynamicSelectModel.getLabel(0));
 }

 @Test(expected = WheelException.class)
 public void testGetLabelWithNullObject() {
 Mockito.when(objects.toArray()[0]).thenReturn(null);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.getLabel(0);
 }

 @Test
 public void testGetValue() {
 Mockito.when(objects.toArray()[0]).thenReturn(new Object() {
 @Override
 public String toString() {
 return "Option 1";
 }
 });
 Mockito.when(value.eval(Mockito.anyObject(), Mockito.eq(component))).thenReturn("Value 1");
 assertEquals("Value 1", dynamicSelectModel.getValue(0));
 }

 @Test(expected = WheelException.class)
 public void testGetValueWithNullObject() {
 Mockito.when(objects.toArray()[0]).thenReturn(null);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.getValue(0);
 }

 @Test
 public void testTranslateValue() {
 Mockito.when(objects.toArray()[0]).thenReturn(new Object() {
 @Override
 public String toString() {
 return "Option 1";
 }
 });
 Mockito.when(translator.eval(Mockito.anyObject(), Mockito.eq(component))).thenReturn("Translated Value 1");
 assertEquals("Translated Value 1", dynamicSelectModel.translateValue("Value 1"));
 }

 @Test
 public void testTranslateValueWithNullValue() {
 dynamicSelectModel.translator("translatorExpression");
 assertNull(dynamicSelectModel.translateValue(null));
 }

 @Test
 public void testGetObjects() {
 Mockito.when(collection.eval(topLevelComponent, component)).thenReturn(objects);
 Collection expected = new ArrayList();
 expected.add(new Object() {
 @Override
 public String toString() {
 return "Option 1";
 }
 });
 expected.add(new Object() {
 @Override
 public String toString() {
 return "Option 2";
 }
 });
 Mockito.when(objects.toArray()).thenReturn(expected.toArray());
 assertEquals(expected, dynamicSelectModel.getObjects());
 }

 @Test
 public void testGetObjectsWithEmptyCollection() {
 Mockito.when(collection.eval(topLevelComponent, component)).thenReturn(new ArrayList());
 Collection expected = new ArrayList();
 expected.add(dynamicSelectModel.emptyObject);
 Mockito.when(dynamicSelectModel.emptyObject.toString()).thenReturn("Empty Option");
 assertEquals(expected, dynamicSelectModel.getObjects());
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

 @Test
 public void testValidate() {
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithNullCollection() {
 dynamicSelectModel.collection(null);
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithNullLabel() {
 dynamicSelectModel.label(null);
 dynamicSelectModel.validate();
 }

 @Test(expected = WheelException.class)
 public void testValidateWithNullValue() {
 dynamicSelectModel.value(null);
 dynamicSelectModel.validate();
 }

 @Test
 public void testReset() {
 dynamicSelectModel.objects = new LinkedList<>();
 dynamicSelectModel.reset();
 assertTrue(dynamicSelectModel.objects.isEmpty());
 }
}