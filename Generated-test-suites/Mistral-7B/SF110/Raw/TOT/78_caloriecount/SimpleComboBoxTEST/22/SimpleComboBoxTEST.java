import static org.junit.Assert.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

import org.junit.Test;

public class SimpleComboBoxTest {

 @Test
 public void testEmptySimpleComboBox() {
 SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
 // Empty combo box, no selection
 assertEquals(null, comboBox.getSelectedValue());
 assertEquals(-1, comboBox.getSelectedInt());
 assertEquals(-1L, comboBox.getSelectedLong());
 }

 @Test
 public void testSimpleComboBoxWithIntegerValues() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox comboBox = new SimpleComboBox(model);

 // Selection of valid integer value
 comboBox.setSelectedIndex(1);
 assertEquals(2, comboBox.getSelectedInt());

 // Selection of invalid index
 comboBox.setSelectedIndex(-1);
 assertEquals(null, comboBox.getSelectedValue());
 }

 @Test
 public void testSimpleComboBoxWithStringValues() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("One");
 model.addElement("Two");
 model.addElement("Three");
 SimpleComboBox comboBox = new SimpleComboBox(model);

 // Selection of valid string value
 comboBox.setSelectedIndex(1);
 assertEquals("Two", comboBox.getSelectedValue().toString());

 // Selection of invalid index
 comboBox.setSelectedIndex(-1);
 assertEquals(null, comboBox.getSelectedValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleComboBoxWithNullModel() {
 SimpleComboBox comboBox = new SimpleComboBox(null);
 }

 @Test
 public void testSimpleComboBoxWithNullValue() {
 SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
 comboBox.setSelectedValue(null);
 assertEquals(null, comboBox.getSelectedValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleComboBoxWithInvalidValueType() {
 SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
 comboBox.setSelectedValue(new Object());
 }

 @Test
 public void testSimpleComboBoxWithIntegerValueAndChangeModel() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox comboBox = new SimpleComboBox(model);
 model.setSelectedIndex(1);
 assertEquals(2, comboBox.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithLongValueAndChangeModel() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 SimpleComboBox comboBox = new SimpleComboBox(model);
 model.setSelectedIndex(1);
 assertEquals(2, comboBox.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithShortValueAndChangeModel() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement((short)1);
 model.addElement((short)2);
 model.addElement((short)3);
 SimpleComboBox comboBox = new SimpleComboBox(model);
 model.setSelectedIndex(1);
 assertEquals(1, comboBox.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithByteValueAndChangeModel() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement((byte)1);
 model.addElement((byte)2);
 model.addElement((byte)3);
 SimpleComboBox comboBox = new SimpleComboBox(model);
 model.setSelectedIndex(1);
 assertEquals(1, comboBox.getSelectedInt());
 }
}