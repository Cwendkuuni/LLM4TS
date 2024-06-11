import org.junit.Test;
import static org.junit.Assert.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;
import java.awt.event.ItemEvent;

public class SimpleComboBoxTest {

 @Test
 public void testSimpleComboBoxWithIntegerValues() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Integer[] { 1, 2, 3, 4, 5 }));

 // Test selecting an integer value
 simpleComboBox.setSelectedIndex(2);
 assertEquals(3, simpleComboBox.getSelectedInt());

 // Test setting an integer value
 simpleComboBox.setSelectedValue(4);
 assertEquals(4, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertNull(simpleComboBox.getModel());
 assertNull(simpleComboBox.mySimpleModel);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSimpleComboBoxWithNonIntegerValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Object[] { 1, "Two", 3, 4.0, 5L }));
 simpleComboBox.setSelectedIndex(1);
 assertNull(simpleComboBox.getSelectedInt());
 assertNotNull(simpleComboBox.getSelectedValue());
 assertEquals("Two", simpleComboBox.getSelectedValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleComboBoxWithInvalidValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Object[] { 1, 2, 3, 4, 5 }));
 simpleComboBox.setSelectedValue('A');
 }

 @Test
 public void testSimpleComboBoxWithEmptyModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Object[0]));
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSimpleComboBoxWithChangedModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Integer[] { 1, 2, 3, 4, 5 }));
 simpleComboBox.setModel(new SimpleComboBoxModel(new Integer[] { 2, 3, 4, 5, 6 }));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(4, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithItemEvent() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new Integer[] { 1, 2, 3, 4, 5 }));
 simpleComboBox.addItemListener(e -> {
 if (e.getStateChange() == ItemEvent.SELECTED) {
 assertEquals(simpleComboBox.getSelectedIndex(), e.getItemPosition());
 }
 });
 simpleComboBox.setSelectedIndex(2);
 simpleComboBox.fireItemStateChanged(new ItemEvent(simpleComboBox, ItemEvent.SELECTED, 2, -1));
 }
}