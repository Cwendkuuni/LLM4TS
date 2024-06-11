package wheel.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import wheel.components.StandaloneComponent;

public class DynamicSelectModelTest {

 private DynamicSelectModel model;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setup() {
 topLevelComponent = new StandaloneComponent();
 model = new DynamicSelectModel();
 model.collection("colors").label("Color").value("color").translator("colorToString");
 }

 @Test
 public void testGetOptionCount() {
 model.getObjects().add("Red");
 model.getObjects().add("Green");
 model.getObjects().add("Blue");

 assertEquals(3, model.getOptionCount());
 }

 @Test
 public void testGetLabel() {
 model.getObjects().add("Red");
 model.getObjects().add("Green");
 model.getObjects().add("Blue");

 assertEquals("Red", model.getLabel(0));
 }

 @Test
 public void testGetValue() {
 model.getObjects().add("Red");
 model.getObjects().add("Green");
 model.getObjects().add("Blue");

 assertEquals("color[0]", model.getValue(0));
 }

 @Test(expected = WheelException.class)
 public void testEmptyCollection() {
 model.reset();
 model.getObjects();
 }

 @Test
 public void testTranslateValue() {
 model.getObjects().add("Red");
 model.getObjects().add("Green");
 model.getObjects().add("Blue");
 model.translator("colorToString");

 assertEquals("Red", model.translateValue("Red"));
 assertEquals("Green", model.translateValue("Green"));
 assertEquals("Blue", model.translateValue("Blue"));
 }

 @Test
 public void testEmptyModel() {
 assertEquals(0, model.getOptionCount());
 }

 @Test
 public void testInvalidCollection() {
 model.collection(null);
 try {
 model.getOptionCount();
 fail("Exception expected");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidLabel() {
 model.label(null);
 try {
 model.getLabel(0);
 fail("Exception expected");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidValue() {
 model.value(null);
 try {
 model.getValue(0);
 fail("Exception expected");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testInvalidTranslator() {
 model.translator(null);
 try {
 model.translateValue("Red");
 fail("Exception expected");
 } catch (WheelException e) {
 // Expected
 }
 }

 @Test
 public void testCollectionArray() {
 model.collection(new Object[] { "Red", "Green", "Blue" });
 assertEquals(3, model.getOptionCount());
 assertEquals("Red", model.getLabel(0));
 assertEquals("color[0]", model.getValue(0));
 }

 @Test
 public void testCollectionList() {
 model.collection(new LinkedList<String>(Arrays.asList("Red", "Green", "Blue")));
 assertEquals(3, model.getOptionCount());
 assertEquals("Red", model.getLabel(0));
 assertEquals("color[0]", model.getValue(0));
 }

 @Test
 public void testCollectionArrayList() {
 model.collection(new ArrayList<String>(Arrays.asList("Red", "Green", "Blue")));
 assertEquals(3, model.getOptionCount());
 assertEquals("Red", model.getLabel(0));
 assertEquals("color[0]", model.getValue(0));
 }

 @Test
 public void testEmptyCollectionWithEmpty() {
 model.empty(new Object());
 model.collection("colors").label("Color").value("color").translator("colorToString");
 model.getObjects().add("Red");
 model.getObjects().add("Green");
 model.getObjects().add("Blue");

 List<Object> objects = new ArrayList<Object>(model.getObjects());
 objects.add(model.getEmptyObject());
 assertEquals(4, model.getOptionCount());
 assertEquals("Red", model.getLabel(0));
 assertEquals("color[0]", model.getValue(0));
 assertEquals("", model.translateValue(""));
 assertEquals(model.getEmptyObject(), model.translateValue(""));
 }

}