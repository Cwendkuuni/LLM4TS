import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import wheel.components.StandaloneComponent;
import wheel.util.DynamicSelectModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private StandaloneComponent topLevelComponent;

 @Before
 public void setUp() {
 component = new Component();
 topLevelComponent = new StandaloneComponent();
 dynamicSelectModel = new DynamicSelectModel()
 .collection("colors")
 .label("color")
 .value("color")
 .translator("colorNameTranslator")
 .topLevelComponent(topLevelComponent);
 }

 @Test
 public void testGetOptionCount() {
 List<String> colors = new ArrayList<>();
 colors.add("red");
 colors.add("green");
 colors.add("blue");
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setObjects(colors);
 assertEquals(3, dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testGetLabel() {
 List<String> colors = new ArrayList<>();
 colors.add("red");
 colors.add("green");
 colors.add("blue");
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setObjects(colors);
 assertEquals("red", dynamicSelectModel.getLabel(0));
 }

 @Test
 public void testGetValue() {
 List<String> colors = new ArrayList<>();
 colors.add("red");
 colors.add("green");
 colors.add("blue");
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setObjects(colors);
 assertEquals("red", dynamicSelectModel.getValue(0));
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsWhenCollectionIsNull() {
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.collection = null;
 dynamicSelectModel.getObjects();
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsWhenCollectionIsEmpty() {
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.collection = new ElExpression("");
 dynamicSelectModel.getObjects();
 }

 @Test(expected = WheelException.class)
 public void testGetObjectsWhenCollectionIsNotAnInstanceOfCollectionOrObjectArray() {
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.collection = new ElExpression("1 + 1");
 dynamicSelectModel.getObjects();
 }

 @Test
 public void testTranslateValue() {
 List<String> colors = new ArrayList<>();
 colors.add("red");
 colors.add("green");
 colors.add("blue");
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setObjects(colors);
 dynamicSelectModel.setTranslator("colorNameTranslator");
 assertEquals("red", dynamicSelectModel.translateValue("red"));
 }

 @Test
 public void testTranslateValueWhenValueIsNull() {
 List<String> colors = new ArrayList<>();
 colors.add("red");
 colors.add("green");
 colors.add("blue");
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setObjects(colors);
 dynamicSelectModel.setTranslator("colorNameTranslator");
 assertNull(dynamicSelectModel.translateValue(null));
 }

 @Test(expected = WheelException.class)
 public void testTranslateValueWhenTranslatorIsEmpty() {
 List<String> colors = new ArrayList<>();
 colors.add("red");
 colors.add("green");
 colors.add("blue");
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setObjects(colors);
 dynamicSelectModel.setTranslator(new ElExpression(""));
 dynamicSelectModel.translateValue("red");
 }

 @Test
 public void testReset() {
 List<String> colors = new ArrayList<>();
 colors.add("red");
 colors.add("green");
 colors.add("blue");
 dynamicSelectModel.setComponent(component);
 dynamicSelectModel.setObjects(colors);
 dynamicSelectModel.reset();
 assertTrue(dynamicSelectModel.getObjects() == null);
 }

}