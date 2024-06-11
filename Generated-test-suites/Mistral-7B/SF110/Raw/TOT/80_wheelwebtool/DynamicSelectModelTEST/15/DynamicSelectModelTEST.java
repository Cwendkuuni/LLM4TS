import static org.junit.Assert.*;
import static wheel.util.DynamicSelectModel.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.*;

@RunWith(PowerMockRunner.class)
public class DynamicSelectModelTest {

 private DynamicSelectModel dynamicSelectModel;
 private Component component;
 private ElExpression label;
 private ElExpression value;
 private ElExpression collection;
 private Collection objects;

 @Before
 public void setUp() {
 this.component = Mockito.mock(Component.class);
 this.label = new ElExpression("label");
 this.value = new ElExpression("value");
 this.collection = new ElExpression("collection");
 this.dynamicSelectModel = new DynamicSelectModel();
 this.dynamicSelectModel.collection(this.collection).label(this.label).value(this.value);
 }

 @Test
 public void testGetOptionCount_whenCollectionIsEmpty_thenReturnZero() {
 PowerMockito.when(this.collection.eval(this.component, this.component)).thenReturn(new ArrayList<>());
 PowerMockito.when(this.label.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("Label");
 PowerMockito.when(this.value.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("Value");
 assertEquals(0, this.dynamicSelectModel.getOptionCount());
 }

 @Test
 public void testGetLabel_whenIndexIsValid_thenReturnCorrectLabel() {
 PowerMockito.when(this.collection.eval(this.component, this.component)).thenReturn(Arrays.asList("Option1", "Option2", "Option3"));
 PowerMockito.when(this.label.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn(arg -> arg.toString());
 assertEquals("Option1", this.dynamicSelectModel.getLabel(0));
 }

 @Test(expected = WheelException.class)
 public void testGetLabel_whenIndexIsOutOfBounds_thenThrowException() {
 PowerMockito.when(this.collection.eval(this.component, this.component)).thenReturn(Arrays.asList("Option1", "Option2"));
 PowerMockito.when(this.label.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn(arg -> arg.toString());
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setTopLevelComponent(Mockito.mock(StandaloneComponent.class));
 this.dynamicSelectModel.setObjects(Arrays.asList("Option1", "Option2"));
 try {
 this.dynamicSelectModel.getLabel(2);
 } catch (WheelException e) {
 assertEquals("Index 2 is out of bounds. Size of the collection is 2.", e.getMessage());
 }
 }

 @Test
 public void testGetValue_whenIndexIsValid_thenReturnCorrectValue() {
 PowerMockito.when(this.collection.eval(this.component, this.component)).thenReturn(Arrays.asList(new MyObject("Option1"), new MyObject("Option2"), new MyObject("Option3")));
 PowerMockito.when(this.value.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn(arg -> arg.toString());
 assertEquals("Option1", this.dynamicSelectModel.getValue(0));
 }

 @Test(expected = WheelException.class)
 public void testGetValue_whenIndexIsOutOfBounds_thenThrowException() {
 PowerMockito.when(this.collection.eval(this.component, this.component)).thenReturn(Arrays.asList(new MyObject("Option1"), new MyObject("Option2")));
 PowerMockito.when(this.value.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn(arg -> arg.toString());
 this.dynamicSelectModel.setComponent(this.component);
 this.dynamicSelectModel.setTopLevelComponent(Mockito.mock(StandaloneComponent.class));
 this.dynamicSelectModel.setObjects(Arrays.asList(new MyObject("Option1"), new MyObject("Option2")));
 try {
 this.dynamicSelectModel.getValue(2);
 } catch (WheelException e) {
 assertEquals("Index 2 is out of bounds. Size of the collection is 2.", e.getMessage());
 }
 }

 @Test
 public void testTranslateValue_whenValueIsValid_thenReturnCorrespondingObject() {
 PowerMockito.when(this.collection.eval(this.component, this.component)).thenReturn(Arrays.asList(new MyObject("Option1"), new MyObject("Option2"), new MyObject("Option3")));
 PowerMockito.when(this.value.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("Option2");
 PowerMockito.when(this.translator.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn(arg -> arg.toString());
 assertEquals(new MyObject("Option2"), this.dynamicSelectModel.translateValue("Option2"));
 }

 @Test
 public void testTranslateValue_whenValueIsNotPresent_thenReturnNull() {
 PowerMockito.when(this.collection.eval(this.component, this.component)).thenReturn(Arrays.asList(new MyObject("Option1"), new MyObject("Option2")));
 PowerMockito.when(this.value.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn("Option4");
 PowerMockito.when(this.translator.eval(Mockito.anyObject(), Mockito.anyObject())).thenReturn(arg -> arg.toString());
 assertNull(this.dynamicSelectModel.translateValue("Option4"));
 }

 @After
 public void tearDown() {
 PowerMockito.verifyNoMoreInteractions(this.component);
 PowerMockito.verifyNoMoreInteractions(this.collection);
 PowerMockito.verifyNoMoreInteractions(this.label);
 PowerMockito.verifyNoMoreInteractions(this.value);
 PowerMockito.verifyNoMoreInteractions(this.translator);
 }

 class MyObject {
 public MyObject(String label) {
 this.label = label;
 }

 String label;
 }
}