package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import wheel.components.Component;
import wheel.components.MockComponent;
import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setup() {
 component = Mockito.mock(Component.class);
 topLevelComponent = Mockito.mock(StandaloneComponent.class);
 model = new DynamicSelectModel()
 .collection("java.util.Arrays.asList(1, 2, 3, 4, 5)")
 .label("toString()")
 .value("toString()")
 .getComponent()
 .setComponent(component)
 .setTopLevelComponent(topLevelComponent);
 }

 @Test
 public void testGetOptionCount() {
 List<Object> objects = Collections.singletonList(new Integer(1));
 when(component.getObjects()).thenReturn(objects);
 when(topLevelComponent.getObjects()).thenReturn(Collections.emptyList());
 assertEquals(1, model.getOptionCount());

 objects = Arrays.asList(new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5));
 when(component.getObjects()).thenReturn(objects);
 when(topLevelComponent.getObjects()).thenReturn(Arrays.asList(new Integer(6), new Integer(7), new Integer(8)));
 assertEquals(11, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testInvalidCollectionExpression() {
 model = new DynamicSelectModel()
 .collection(null)
 .label("toString()")
 .value("toString()");
 model.getOptionCount();
 }

 @Test
 public void testInvalidLabelExpression() {
 model = new DynamicSelectModel()
 .collection("java.util.Arrays.asList(1, 2, 3, 4, 5)")
 .label(null)
 .value("toString()");
 model.getLabel(0);
 }

 @Test
 public void testInvalidValueExpression() {
 model = new DynamicSelectModel()
 .collection("java.util.Arrays.asList(1, 2, 3, 4, 5)")
 .label("toString()")
 .value(null);
 model.getValue(0);
 }

 @Test
 public void testTranslateValueSuccess() {
 model = new DynamicSelectModel()
 .collection("java.util.Arrays.asList(new String(\"Apple\"), new String(\"Banana\"), new String(\"Cherry\"))")
 .label("toString()")
 .value("toString()")
 .translator("s -> s.toUpperCase()");
 assertEquals("APPLE", model.translateValue("Apple"));
 }

 @Test(expected = WheelException.class)
 public void testInvalidTranslatorExpression() {
 model = new DynamicSelectModel()
 .collection("java.util.Arrays.asList(new String(\"Apple\"), new String(\"Banana\"), new String(\"Cherry\"))")
 .label("toString()")
 .value("toString()")
 .translator(null);
 model.translateValue("Apple");
 }

 @Test
 public void testEmptyModel() {
 model = new DynamicSelectModel()
 .collection("java.util.Collections.emptyList()")
 .label("toString()")
 .value("toString()");
 assertNull(model.translateValue(""));
 }

 @Test
 public void testEmptyOption() {
 model = new DynamicSelectModel()
 .collection("java.util.Arrays.asList(new String(\"Apple\"), new String(\"Banana\"), new String(\"Cherry\"))")
 .label("toString()")
 .value("toString()")
 .empty(new String(""))
 .getObjects();
 assertEquals(6, model.getOptionCount());
 assertEquals("", model.getLabel(0));
 assertNull(model.getValue(0));
 }
}