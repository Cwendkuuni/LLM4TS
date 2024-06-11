import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 // Test for constructor
 @Test
 public void testSimpleComboBoxConstructor() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4, 5)));
 assertEquals(5, simpleComboBox.getItemCount());
 }

 // Test for setModel method
 @Test
 public void testSetModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4, 5)));
 SimpleComboBoxModel newModel = new SimpleComboBoxModel(Collections.singletonList(6));
 simpleComboBox.setModel(newModel);
 assertEquals(1, simpleComboBox.getItemCount());
 assertEquals(6, simpleComboBox.getSelectedValue());
 }

 // Test for getSelectedValue method with null model
 @Test(expected = NullPointerException.class)
 public void testGetSelectedValueNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 simpleComboBox.getSelectedValue();
 }

 // Test for getSelectedValue method with integer value
 @Test
 public void testGetSelectedValueInteger() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4, 5)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(3, simpleComboBox.getSelectedValue());
 }

 // Test for getSelectedValue method with long value
 @Test
 public void testGetSelectedValueLong() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1L, 2L, 3L, 4L, 5L)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(3L, simpleComboBox.getSelectedValue());
 }

 // Test for getSelectedValue method with short value
 @Test
 public void testGetSelectedValueShort() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((short)1, (short)2, (short)3, (short)4, (short)5)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals((short)3, simpleComboBox.getSelectedValue());
 }

 // Test for getSelectedValue method with byte value
 @Test
 public void testGetSelectedValueByte() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((byte)1, (byte)2, (byte)3, (byte)4, (byte)5)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals((byte)3, simpleComboBox.getSelectedValue());
 }

 // Test for setSelectedValue method with null value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueNullValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4, 5)));
 simpleComboBox.setSelectedValue(null);
 }

 // Test for setSelectedValue method with integer value
 @Test
 public void testSetSelectedValueInteger() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4, 5)));
 simpleComboBox.setSelectedValue(3);
 assertEquals(3, simpleComboBox.getSelectedIndex());
 assertEquals(3, simpleComboBox.getSelectedValue());
 }

 // Test for setSelectedValue method with long value
 @Test
 public void testSetSelectedValueLong() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1L, 2L, 3L, 4L, 5L)));
 simpleComboBox.setSelectedValue(3L);
 assertEquals(3, simpleComboBox.getSelectedIndex());
 assertEquals(3L, simpleComboBox.getSelectedValue());
 }

 // Test for setSelectedValue method with short value
 @Test
 public void testSetSelectedValueShort() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((short)1, (short)2, (short)3, (short)4, (short)5)));
 simpleComboBox.setSelectedValue((short)3);
 assertEquals(3, simpleComboBox.getSelectedIndex());
 assertEquals((short)3, simpleComboBox.getSelectedValue());
 }

 // Test for setSelectedValue method with byte value
 @Test
 public void testSetSelectedValueByte() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((byte)1, (byte)2, (byte)3, (byte)4, (byte)5)));
 simpleComboBox.setSelectedValue((byte)3);
 assertEquals(3, simpleComboBox.getSelectedIndex());
 assertEquals((byte)3, simpleComboBox.getSelectedValue());
 }

 // Test for getSelectedInt method with null value
 @Test(expected = NullPointerException.class)
 public void testGetSelectedIntNullValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 simpleComboBox.getSelectedInt();
 }

 // Test for getSelectedInt method with integer value
 @Test
 public void testGetSelectedIntInteger() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4, 5)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(3, simpleComboBox.getSelectedInt());
 }

 // Test for getSelectedInt method with long value
 @Test
 public void testGetSelectedIntLong() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1L, 2L, 3L, 4L, 5L)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(3, simpleComboBox.getSelectedInt());
 }

 // Test for getSelectedInt method with short value
 @Test
 public void testGetSelectedIntShort() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((short)1, (short)2, (short)3, (short)4, (short)5)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(3, simpleComboBox.getSelectedInt());
 }

 // Test for getSelectedInt method with byte value
 @Test
 public void testGetSelectedIntByte() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((byte)1, (byte)2, (byte)3, (byte)4, (byte)5)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(3, simpleComboBox.getSelectedInt());
 }

 // Test for getSelectedLong method with null value
 @Test(expected = NullPointerException.class)
 public void testGetSelectedLongNullValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 simpleComboBox.getSelectedLong();
 }

 // Test for getSelectedLong method with integer value
 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongInteger() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4, 5)));
 simpleComboBox.setSelectedIndex(2);
 simpleComboBox.getSelectedLong();
 }

 // Test for getSelectedLong method with long value
 @Test
 public void testGetSelectedLongLong() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1L, 2L, 3L, 4L, 5L)));
 simpleComboBox.setSelectedIndex(2);
 assertEquals(3L, simpleComboBox.getSelectedLong());
 }

 // Test for getSelectedLong method with short value
 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongShort() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((short)1, (short)2, (short)3, (short)4, (short)5)));
 simpleComboBox.setSelectedIndex(2);
 simpleComboBox.getSelectedLong();
 }

 // Test for getSelectedLong method with byte value
 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongByte() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((byte)1, (byte)2, (byte)3, (byte)4, (byte)5)));
 simpleComboBox.setSelectedIndex(2);
 simpleComboBox.getSelectedLong();
 }

 // Test for setSelectedInt method with negative value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIntNegativeValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4, 5)));
 simpleComboBox.setSelectedInt(-1);
 }

 // Test for setSelectedInt method with valid index
 @Test
 public void testSetSelectedIntValidIndex() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4, 5)));
 simpleComboBox.setSelectedInt(2);
 assertEquals(3, simpleComboBox.getSelectedInt());
 }

 // Test for setSelectedLong method with negative value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedLongNegativeValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1L, 2L, 3L, 4L, 5L)));
 simpleComboBox.setSelectedLong(-1L);
 }

 // Test for setSelectedLong method with valid index
 @Test
 public void testSetSelectedLongValidIndex() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1L, 2L, 3L, 4L, 5L)));
 simpleComboBox.setSelectedLong(3L);
 assertEquals(3, simpleComboBox.getSelectedInt());
 assertEquals(3L, simpleComboBox.getSelectedLong());
 }

 // Test for setSelectedShort method with negative value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedShortNegativeValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((short)1, (short)2, (short)3, (short)4, (short)5)));
 simpleComboBox.setSelectedShort(-1);
 }

 // Test for setSelectedShort method with valid index
 @Test
 public void testSetSelectedShortValidIndex() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((short)1, (short)2, (short)3, (short)4, (short)5)));
 simpleComboBox.setSelectedShort((short)3);
 assertEquals(3, simpleComboBox.getSelectedInt());
 assertEquals((short)3, simpleComboBox.getSelectedShort());
 }

 // Test for setSelectedByte method with negative value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedByteNegativeValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((byte)1, (byte)2, (byte)3, (byte)4, (byte)5)));
 simpleComboBox.setSelectedByte(-1);
 }

 // Test for setSelectedByte method with valid index
 @Test
 public void testSetSelectedByteValidIndex() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList((byte)1, (byte)2, (byte)3, (byte)4, (byte)5)));
 simpleComboBox.setSelectedByte((byte)3);
 assertEquals(3, simpleComboBox.getSelectedInt());
 assertEquals((byte)3, simpleComboBox.getSelectedByte());
 }

 // Test for fireItemStateChanged event
 @Test
 public void testFireItemStateChanged() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4, 5)));
 int initialSize = simpleComboBox.getItemCount();
 simpleComboBox.addItemListener(e -> {
 if (e.getStateChange() == ItemEvent.SELECTED) {
 simpleComboBox.setSelectedIndex(2);
 }
 });
 simpleComboBox.setSelectedIndex(3);
 assertEquals(5, simpleComboBox.getItemCount());
 assertTrue(simpleComboBox.hasFocus());
 simpleComboBox.doClick();
 assertEquals(4, simpleComboBox.getItemCount());
 }
}