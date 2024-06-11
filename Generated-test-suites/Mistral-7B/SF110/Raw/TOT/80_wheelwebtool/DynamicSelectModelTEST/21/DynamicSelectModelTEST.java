import org.junit.Test;
import static org.junit.Assert.*;
import wheel.components.StandaloneComponent;
import wheel.util.DynamicSelectModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class DynamicSelectModelTest {

 @Test
 public void testGetOptionCount() {
 DynamicSelectModel model = new DynamicSelectModel().collection("colors").label("color").value("color");
 Collection colors = Arrays.asList("red", "green", "blue", "yellow");
 model.setComponent(new StandaloneComponent());
 model.setTopLevelComponent(new StandaloneComponent());
 model.setObjects(colors);
 assertEquals(4, model.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testEmptyCollection() {
 DynamicSelectModel model = new DynamicSelectModel().collection("").label("color").value("color");
 model.setComponent(new StandaloneComponent());
 model.setTopLevelComponent(new StandaloneComponent());
 model.setObjects(new ArrayList<>());
 model.getObjects();
 }

 @Test
 public void testLabel() {
 DynamicSelectModel model = new DynamicSelectModel().collection("colors").label("color").value("color");
 model.setComponent(new StandaloneComponent());
 model.setTopLevelComponent(new StandaloneComponent());
 model.setObjects(Arrays.asList("red", "green", "blue", "yellow"));
 assertEquals("red", model.getLabel(0));
 assertEquals("green", model.getLabel(1));
 assertEquals("blue", model.getLabel(2));
 }

 @Test(expected = WheelException.class)
 public void testInvalidLabelExpression() {
 DynamicSelectModel model = new DynamicSelectModel().collection("colors").label("invalidExpression").value("color");
 model.setComponent(new StandaloneComponent());
 model.setTopLevelComponent(new StandaloneComponent());
 model.setObjects(Arrays.asList("red", "green", "blue", "yellow"));
 model.getLabel(0);
 }

 @Test
 public void testValue() {
 DynamicSelectModel model = new DynamicSelectModel().collection("colors").label("color").value("color");
 model.setComponent(new StandaloneComponent());
 model.setTopLevelComponent(new StandaloneComponent());
 model.setObjects(Arrays.asList("red", "green", "blue", "yellow"));
 assertEquals("red", model.getValue(0));
 assertEquals("green", model.getValue(1));
 assertEquals("blue", model.getValue(2));
 }

 @Test(expected = WheelException.class)
 public void testInvalidValueExpression() {
 DynamicSelectModel model = new DynamicSelectModel().collection("colors").label("color").value("invalidExpression");
 model.setComponent(new StandaloneComponent());
 model.setTopLevelComponent(new StandaloneComponent());
 model.setObjects(Arrays.asList("red", "green", "blue", "yellow"));
 model.getValue(0);
 }

 @Test
 public void testTranslateValue() {
 DynamicSelectModel model = new DynamicSelectModel().collection("colors").label("color").value("color").translator("colorToRgb");
 model.setComponent(new StandaloneComponent());
 model.setTopLevelComponent(new StandaloneComponent());
 model.setObjects(Arrays.asList("red", "green", "blue", "yellow"));
 assertEquals("#FF0000", model.translateValue("red"));
 assertEquals("#00FF00", model.translateValue("green"));
 assertEquals("#0000FF", model.translateValue("blue"));
 }

 @Test(expected = WheelException.class)
 public void testInvalidTranslatorExpression() {
 DynamicSelectModel model = new DynamicSelectModel().collection("colors").label("color").value("color").translator("invalidExpression");
 model.setComponent(new StandaloneComponent());
 model.setTopLevelComponent(new StandaloneComponent());
 model.setObjects(Arrays.asList("red", "green", "blue", "yellow"));
 model.translateValue("red");
 }

 @Test
 public void testEmptyOption() {
 DynamicSelectModel model = new DynamicSelectModel().collection("colors").label("color").value("color").empty("emptyOption");
 model.setComponent(new StandaloneComponent());
 model.setTopLevelComponent(new StandaloneComponent());
 model.setObjects(Arrays.asList("red", "green", "blue", "yellow"));
 assertEquals("emptyOption", model.getLabel(3));
 assertEquals("emptyOption", model.getValue(3));
 }
}