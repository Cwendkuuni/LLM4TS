import org.junit.Test;
import static org.junit.Assert.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;
import java.awt.event.ItemEvent;
import java.util.Arrays;

public class SimpleComboBoxTest {

 @Test
 public void testConstructor() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 assertNotNull(simpleComboBox);
 }

 @Test
 public void testSetModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 SimpleComboBoxModel newModel = new SimpleComboBoxModel();
 simpleComboBox.setModel(newModel);
 assertEquals(newModel, simpleComboBox.getModel());
 }

 @Test
 public void testGetSelectedValueNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueStringValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("One");
 model.addElement("Two");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(0);
 assertEquals("One", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueIntegerValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedValueLongValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedValueInvalidType() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("One");
 model.addElement(1);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.getSelectedValue(); //should throw IllegalArgumentException
 }

 @Test
 public void testSetSelectedValueStringValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("One");
 model.addElement("Two");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.setSelectedValue("One");
 assertEquals("One", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueNullValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("One");
 model.addElement("Two");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.setSelectedValue(null);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueIntegerValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("One");
 model.addElement("Two");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.setSelectedValue(1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testSetSelectedValueLongValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("One");
 model.addElement("Two");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.setSelectedValue(1L);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testSetSelectedValueInvalidType() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("One");
 model.addElement("Two");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.setSelectedValue("One");
 simpleComboBox.getSelectedValue(); //should be same as before
 simpleComboBox.setSelectedValue(2); //should throw no exception
 simpleComboBox.setSelectedValue('A'); //should throw IllegalArgumentException
 }

 @Test
 public void testAddItem() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 model.addElement("One");
 model.addElement("Two");
 simpleComboBox.setSelectedIndex(0);
 assertEquals("One", simpleComboBox.getSelectedValue());
 model.addElement("Three");
 assertEquals("One", simpleComboBox.getSelectedValue());
 assertEquals(3, simpleComboBox.getItemCount());
 }

 @Test
 public void testRemoveItem() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 model.addElement("One");
 model.addElement("Two");
 simpleComboBox.setSelectedIndex(1);
 assertEquals("Two", simpleComboBox.getSelectedValue());
 model.removeElement("Two");
 assertEquals("One", simpleComboBox.getSelectedValue());
 assertEquals(1, simpleComboBox.getItemCount());
 }

 @Test
 public void testFireStateChangedEvent() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 SimpleComboBox simpleComboBox2 = new SimpleComboBox(model);
 simpleComboBox.addActionListener(e -> simpleComboBox2.setSelectedIndex(simpleComboBox.getSelectedIndex()));
 simpleComboBox.setSelectedIndex(1);
 assertEquals(1, simpleComboBox2.getSelectedInt());
 simpleComboBox.setSelectedIndex(0);
 assertEquals(0, simpleComboBox2.getSelectedInt());
 }
}