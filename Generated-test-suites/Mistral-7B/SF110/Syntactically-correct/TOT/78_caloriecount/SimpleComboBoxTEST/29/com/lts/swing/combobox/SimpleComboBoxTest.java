import org.junit.Test;
import static org.junit.Assert.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;
import java.util.Arrays;

public class SimpleComboBoxTest {

 @Test
 public void testConstructors() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 assertNotNull(simpleComboBox);
 }

 @Test
 public void testSetModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 SimpleComboBoxModel newModel = new SimpleComboBoxModel();
 simpleComboBox.setModel(newModel);
 assertSame(newModel, simpleComboBox.getModel());
 }

 @Test
 public void testGetSelectedValueAndSetSelectedValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("One");
 model.addElement("Two");
 model.addElement("Three");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);

 assertEquals("One", simpleComboBox.getSelectedValue());
 simpleComboBox.setSelectedValue("One");
 assertEquals("One", simpleComboBox.getSelectedValue());

 simpleComboBox.setSelectedIndex(1);
 assertEquals("Two", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedInt() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);

 assertEquals(-1, simpleComboBox.getSelectedInt());
 model.setSelectedIndex(1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedLong() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);

 assertEquals(-1L, simpleComboBox.getSelectedLong());
 model.setSelectedIndex(1);
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntAndLongWithInvalidType() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("String");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);

 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.getSelectedInt(); // Should not throw exception here
 simpleComboBox.getSelectedLong(); // Should throw exception here
 simpleComboBox.getSelectedValue(); // Should return "String"
 }

 @Test
 public void testSetSelectedValueWithInvalidType() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("String");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);

 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.getSelectedValue(); // Should return "String"
 simpleComboBox.setSelectedValue(2); // Should not throw exception
 simpleComboBox.setSelectedValue("Invalid"); // Should throw exception
 }
}