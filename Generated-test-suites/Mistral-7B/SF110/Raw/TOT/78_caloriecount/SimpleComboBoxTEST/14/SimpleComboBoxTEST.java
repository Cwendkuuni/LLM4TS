import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

import org.junit.Test;

public class SimpleComboBoxTest {

 @Test
 public void testSimpleComboBoxWithIntegerValues() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel(new Integer[] { 1, 2, 3, 4 }));
 cb.setSelectedIndex(2);
 assertEquals(3, cb.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithNullModel() {
 SimpleComboBox cb = new SimpleComboBox(null);
 assertNull(cb.getSelectedValue());
 }

 @Test
 public void testSimpleComboBoxWithNonSupportedType() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel(new String[] { "A", "B", "C" }));
 cb.setSelectedValue("B");
 assertThrows(IllegalArgumentException.class, () -> cb.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithLongValues() {
 SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
 when(model.getSelectedValue()).thenReturn(new Long(10));
 SimpleComboBox cb = new SimpleComboBox(model);
 assertEquals(10, cb.getSelectedLong());
 }

 @Test
 public void testSimpleComboBoxWithShortValues() {
 SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
 when(model.getSelectedValue()).thenReturn((short) 5);
 SimpleComboBox cb = new SimpleComboBox(model);
 assertEquals(5, cb.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithByteValues() {
 SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
 when(model.getSelectedValue()).thenReturn((byte) 3);
 SimpleComboBox cb = new SimpleComboBox(model);
 assertEquals(3, cb.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithEmptyModel() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel(new Object[0]));
 assertNull(cb.getSelectedValue());
 }
}