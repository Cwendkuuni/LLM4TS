import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

import java.util.Arrays;

import org.junit.Test;

public class SimpleComboBoxTest {

 @Test
 public void testSimpleComboBoxWithIntegerValues() {
 SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4, 5)));
 assertEquals(1, comboBox.getSelectedInt());
 comboBox.setSelectedValue(3);
 assertEquals(3, comboBox.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithLongValues() {
 SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1L, 2L, 3L, 4L, 5L)));
 assertEquals(1, comboBox.getSelectedInt());
 comboBox.setSelectedValue(3L);
 assertEquals(3, comboBox.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithShortValues() {
 SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((short)1, (short)2, (short)3, (short)4, (short)5)));
 assertEquals(1, comboBox.getSelectedInt());
 comboBox.setSelectedValue(3);
 assertEquals(3, comboBox.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithByteValues() {
 SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((byte)1, (byte)2, (byte)3, (byte)4, (byte)5)));
 assertEquals(1, comboBox.getSelectedInt());
 comboBox.setSelectedValue(3);
 assertEquals(3, comboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleComboBoxWithStringValue() {
 SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList("1", "2", "3", "4", "5")));
 comboBox.setSelectedValue("StringValue");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleComboBoxWithDoubleValue() {
 SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0)));
 comboBox.setSelectedValue(3.5);
 }

 @Test
 public void testSimpleComboBoxWithNullModel() {
 SimpleComboBox comboBox = new SimpleComboBox(null);
 assertNull(comboBox.getSelectedValue());
 }

 @Test
 public void testSimpleComboBoxWithEmptyModel() {
 SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
 assertNull(comboBox.getSelectedValue());
 }

 @Test
 public void testSimpleComboBoxWithCustomModel() {
 SimpleComboBoxModel customModel = mock(SimpleComboBoxModel.class);
 when(customModel.getSelectedValue()).thenReturn(5L);
 SimpleComboBox comboBox = new SimpleComboBox(customModel);
 assertEquals(5, comboBox.getSelectedLong());
 }
}