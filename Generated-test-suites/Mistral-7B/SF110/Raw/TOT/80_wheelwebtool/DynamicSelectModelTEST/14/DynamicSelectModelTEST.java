package wheel.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import wheel.*;

@RunWith(JUnit4.class)
public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setUp() {
 component = new Component();
 topLevelComponent = new StandaloneComponent();
 dynamicSelectModel = new DynamicSelectModel();
 dynamicSelectModel.collection("numbers");
 dynamicSelectModel.label("toString()");
 dynamicSelectModel.value("ordinal()");
 }

 @Test
 public void testGetOptionCount() {
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 topLevelComponent.setProperty("numbers", numbers);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 assertEquals(numbers.size(), dynamicSelectModel.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testGetOptionCountWithNullCollection() {
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.collection(null);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 dynamicSelectModel.setComponent(component);
 try {
 dynamicSelectModel.getOptionCount();
 fail("Expected WheelException");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test(expected = WheelException.class)
 public void testGetLabelWithEmptyIndex() {
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 try {
 dynamicSelectModel.getLabel(-1);
 fail("Expected WheelException");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testGetLabel() {
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 topLevelComponent.setProperty("numbers", numbers);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 assertEquals("1", dynamicSelectModel.getLabel(0));
 assertEquals("4", dynamicSelectModel.getLabel(3));
 }

 @Test(expected = WheelException.class)
 public void testGetLabelWithInvalidIndex() {
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 try {
 dynamicSelectModel.getLabel(5);
 fail("Expected WheelException");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testGetValue() {
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 topLevelComponent.setProperty("numbers", numbers);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 assertEquals("0", dynamicSelectModel.getValue(0));
 assertEquals("3", dynamicSelectModel.getValue(2));
 }

 @Test(expected = WheelException.class)
 public void testGetValueWithEmptyIndex() {
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 try {
 dynamicSelectModel.getValue(-1);
 fail("Expected WheelException");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testGetValue() {
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 topLevelComponent.setProperty("numbers", numbers);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 assertEquals("zero", dynamicSelectModel.translateValue("zero"));
 assertEquals("three", dynamicSelectModel.translateValue("three"));
 }

 @Test(expected = WheelException.class)
 public void testTranslateValueWithInvalidValue() {
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 try {
 dynamicSelectModel.translateValue("five");
 fail("Expected WheelException");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testGetObjects() {
 List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
 topLevelComponent.setProperty("numbers", numbers);
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 Collection objects = dynamicSelectModel.getObjects();
 assertEquals(numbers.size(), objects.size());
 assertTrue(objects.contains(1));
 assertTrue(objects.contains(2));
 assertTrue(objects.contains(3));
 assertTrue(objects.contains(4));
 assertTrue(objects.contains(5));
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsWithNullCollection() {
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setTopLevelComponent(topLevelComponent);
 try {
 dynamicSelectModel.getObjects();
 fail("Expected WheelException");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testReset() {
 dynamicSelectModel.reset();
 assertNull(dynamicSelectModel.getObjects());
 }
}