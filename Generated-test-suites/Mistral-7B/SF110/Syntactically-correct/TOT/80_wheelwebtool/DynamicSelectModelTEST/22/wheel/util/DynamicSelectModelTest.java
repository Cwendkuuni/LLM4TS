package wheel.util; // or the package where DynamicSelectModel is located

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.*;

import static org.junit.Assert.*;

public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;

 @Before
 public void setup() {
 this.component = Mockito.mock(Component.class);
 this.dynamicSelectModel = new DynamicSelectModel()
 .collection("colors")
 .label("toString")
 .value("toString")
 .topLevelComponent(Mockito.mock(StandaloneComponent.class));
 }

 @Test
 public void testGetOptionCount() {
 final Collection colors = Arrays.asList("red", "green", "blue");
 dynamicSelectModel.objects = colors;
 assertEquals(colors.size(), dynamicSelectModel.getOptionCount());
 }

 @Test(expected = WheelException.class)
 public void testGetOptionCount_EmptyCollection() {
 dynamicSelectModel.objects = new ArrayList<>();
 dynamicSelectModel.validate();
 }

 @Test
 public void testGetLabel() {
 dynamicSelectModel.objects = Arrays.asList("apple", "banana", "cherry");
 assertNotNull(dynamicSelectModel.getLabel(0));
 assertEquals("apple", dynamicSelectModel.getLabel(0));
 }

 @Test(expected = WheelException.class)
 public void testGetLabel_InvalidIndex() {
 dynamicSelectModel.objects = Arrays.asList("apple", "banana", "cherry");
 dynamicSelectModel.getLabel(3);
 }

 @Test
 public void testGetValue() {
 dynamicSelectModel.objects = Arrays.asList(new Color("red"), new Color("green"), new Color("blue"));
 assertNotNull(dynamicSelectModel.getValue(0));
 assertEquals("red", dynamicSelectModel.getValue(0));
 }

 @Test(expected = WheelException.class)
 public void testGetValue_InvalidIndex() {
 dynamicSelectModel.objects = Arrays.asList(new Color("red"), new Color("green"), new Color("blue"));
 dynamicSelectModel.getValue(3);
 }

 @Test
 public void testTranslateValue() {
 dynamicSelectModel.objects = Arrays.asList(new Color("red"), new Color("green"), new Color("blue"));
 assertEquals("red", dynamicSelectModel.translateValue("red"));
 assertEquals("green", dynamicSelectModel.translateValue("green"));
 assertEquals("blue", dynamicSelectModel.translateValue("blue"));
 }

 @Test(expected = WheelException.class)
 public void testTranslateValue_InvalidValue() {
 dynamicSelectModel.translateValue("invalidValue");
 }

 @Test
 public void testGetObjects() {
 dynamicSelectModel.collection("colors.values()");
 dynamicSelectModel.topLevelComponent(Mockito.mock(StandaloneComponent.class));
 dynamicSelectModel.validate();
 final Collection colors = Arrays.asList("red", "green", "blue");
 dynamicSelectModel.objects = colors;
 assertEquals(colors, dynamicSelectModel.getObjects());
 }

 @Test(expected = WheelException.class)
 public void testGetObjects_InvalidCollection() {
 dynamicSelectModel.collection("invalidCollection");
 dynamicSelectModel.topLevelComponent(Mockito.mock(StandaloneComponent.class));
 dynamicSelectModel.validate();
 }

 @Test
 public void testGetObjects_EmptyCollection() {
 dynamicSelectModel.collection("colors.values()");
 dynamicSelectModel.topLevelComponent(Mockito.mock(StandaloneComponent.class));
 dynamicSelectModel.objects = new ArrayList<>();
 dynamicSelectModel.validate();
 dynamicSelectModel.reset();
 assertNotNull(dynamicSelectModel.getObjects());
 assertTrue(dynamicSelectModel.getObjects().isEmpty());
 }
}