package wheel.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

import wheel.components.Component;
import wheel.components.StandaloneComponent;
import wheel.exceptions.WheelException;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 topLevelComponent = Mockito.mock(StandaloneComponent.class);
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.setComponent(component = Mockito.mock(Component.class));
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 }

 @After
 public void tearDown() {
 dynamicSelectModel = null;
 component = null;
 topLevelComponent = null;
 }

 @Test
 public void testGetOptionCountWithEmptyCollection() {
 dynamicSelectModel.collection(new ElExpression(""));
 assertFalse(dynamicSelectModel.getObjects() != null);
 assertFalse(dynamicSelectModel.getObjects().isEmpty());
 assertFalse(dynamicSelectModel.getOptionCount() > 0);
 }

 @Test
 public void testGetOptionCountWithCollection() {
 dynamicSelectModel.collection(new ElExpression("[1, 2, 3]"));
 assertFalse(dynamicSelectModel.getObjects() == null);
 assertTrue(dynamicSelectModel.getObjects().size() > 0);
 assertTrue(dynamicSelectModel.getOptionCount() == 3);
 }

 @Test(expected = WheelException.class)
 public void testGetOptionCountWithNullCollection() {
 dynamicSelectModel.collection(null);
 }

 @Test
 public void testGetLabelWithValidIndex() {
 dynamicSelectModel.collection(new ElExpression("[\"apple\", \"banana\", \"orange\"]"));
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("toString()");
 String expectedLabel = "apple";
 String actualLabel = dynamicSelectModel.getLabel(0);
 assertEquals(expectedLabel, actualLabel);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetLabelWithInvalidIndex() {
 dynamicSelectModel.collection(new ElExpression("[\"apple\", \"banana\", \"orange\"]"));
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("toString()");
 dynamicSelectModel.getLabel(2);
 }

 @Test
 public void testGetValueWithValidIndex() {
 dynamicSelectModel.collection(new ElExpression("[1, 2, 3]"));
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("toString()");
 Integer expectedValue = 1;
 Integer actualValue = Integer.parseInt(dynamicSelectModel.getValue(0));
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithInvalidIndex() {
 dynamicSelectModel.collection(new ElExpression("[1, 2, 3]"));
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("toString()");
 dynamicSelectModel.getValue(2);
 }

 @Test
 public void testTranslateValueWithValidValue() {
 dynamicSelectModel.collection(new ElExpression("[{\"label\": \"Option 1\"}, {\"label\": \"Option 2\"}]"));
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("toString()");
 dynamicSelectModel.translator("label");
 String expectedTranslatedValue = "Option 1";
 Object actualTranslatedValue = dynamicSelectModel.translateValue("Option 1");
 assertEquals(expectedTranslatedValue, actualTranslatedValue);
 }

 @Test
 public void testTranslateValueWithInvalidValue() {
 dynamicSelectModel.collection(new ElExpression("[{\"label\": \"Option 1\"}, {\"label\": \"Option 2\"}]"));
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("toString()");
 dynamicSelectModel.translator("label");
 assertNull(dynamicSelectModel.translateValue("Option 3"));
 }

 @Test
 public void testGetObjectsWithValidCollection() {
 dynamicSelectModel.collection(new ElExpression("[\"Option 1\", \"Option 2\"]"));
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("toString()");
 Collection expectedObjects = Arrays.asList("Option 1", "Option 2");
 Collection actualObjects = dynamicSelectModel.getObjects();
 assertEquals(expectedObjects, actualObjects);
 }

 @Test
 public void testGetObjectsWithEmptyCollection() {
 dynamicSelectModel.collection(new ElExpression(""));
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("toString()");
 Collection expectedObjects = new ArrayList<>();
 Collection actualObjects = dynamicSelectModel.getObjects();
 assertEquals(expectedObjects, actualObjects);
 }

 @Test
 public void testGetObjectsWithNullCollection() {
 dynamicSelectModel.collection(null);
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("toString()");
 assertNull(dynamicSelectModel.getObjects());
 }
}