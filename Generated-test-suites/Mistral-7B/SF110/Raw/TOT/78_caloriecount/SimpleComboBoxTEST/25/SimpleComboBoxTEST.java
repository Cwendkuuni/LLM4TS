import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;
import com.lts.swing.combobox.SimpleComboBoxModelItem;

import java.util.Arrays;
import java.util.List;

public class SimpleComboBoxTest {

 private SimpleComboBox<Integer> comboBox;
 private SimpleComboBoxModel<Integer> model;

 @Before
 public void setUp() {
 model = new SimpleComboBoxModel<>();
 model.addElement(new SimpleComboBoxModelItem<>(1));
 model.addElement(new SimpleComboBoxModelItem<>(2));
 model.addElement(new SimpleComboBoxModelItem<>(3));
 comboBox = new SimpleComboBox<>(model);
 }

 @Test
 public void testGetSelectedValueWhenSelectedItemIsInteger() {
 final Integer selectedValue = (Integer) comboBox.getSelectedValue();
 assertEquals(2, selectedValue.intValue());
 }

 @Test
 public void testGetSelectedValueWhenNoSelection() {
 assertEquals(null, comboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueWhenSelectedItemIsLong() {
 final SimpleComboBoxModelItem<Long> longItem = new SimpleComboBoxModelItem<>(4L);
 model.addElement(longItem);
 comboBox.setModel(model);
 comboBox.setSelectedIndex(3);
 final Long selectedValue = (Long) comboBox.getSelectedValue();
 assertEquals(4L, selectedValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedValueWhenSelectedItemIsNotNumber() {
 model.addElement("StringValue");
 comboBox.setModel(model);
 comboBox.setSelectedIndex(3);
 comboBox.getSelectedValue();
 }

 @Test
 public void testSetSelectedValueWhenValueIsInteger() {
 comboBox.setSelectedValue(2);
 final Integer selectedValue = (Integer) comboBox.getSelectedValue();
 assertEquals(2, selectedValue.intValue());
 }

 @Test
 public void testSetSelectedValueWhenValueIsLong() {
 final SimpleComboBoxModelItem<Long> longItem = new SimpleComboBoxModelItem<>(5L);
 model.addElement(longItem);
 comboBox.setSelectedValue(longItem.getValue());
 final Long selectedValue = (Long) comboBox.getSelectedValue();
 assertEquals(5L, selectedValue);
 }

 @Test
 public void testSetSelectedValueWhenValueIsNull() {
 comboBox.setSelectedValue(null);
 assertEquals(null, comboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueWhenValueIsNotNumber() {
 model.addElement("StringValue");
 comboBox.setSelectedValue("StringValue");
 assertEquals("StringValue", comboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedIntWhenSelectedItemIsInteger() {
 final int selectedInt = comboBox.getSelectedInt();
 assertEquals(2, selectedInt);
 }

 @Test
 public void testGetSelectedIntWhenNoSelection() {
 assertEquals(-1, comboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWhenSelectedItemIsLong() {
 final SimpleComboBoxModelItem<Long> longItem = new SimpleComboBoxModelItem<>(7L);
 model.addElement(longItem);
 comboBox.setModel(model);
 comboBox.setSelectedIndex(3);
 final int selectedInt = comboBox.getSelectedInt();
 assertEquals(7, selectedInt);
 }

 @Test
 public void testGetSelectedIntWhenSelectedItemIsShort() {
 final SimpleComboBoxModelItem<Short> shortItem = new SimpleComboBoxModelItem((short) 5);
 model.addElement(shortItem);
 comboBox.setModel(model);
 comboBox.setSelectedIndex(3);
 final int selectedInt = comboBox.getSelectedInt();
 assertEquals(5, selectedInt);
 }

 @Test
 public void testGetSelectedIntWhenSelectedItemIsByte() {
 final SimpleComboBoxModelItem<Byte> byteItem = new SimpleComboBoxModelItem((byte) 4);
 model.addElement(byteItem);
 comboBox.setModel(model);
 comboBox.setSelectedIndex(3);
 final int selectedInt = comboBox.getSelectedInt();
 assertEquals(4, selectedInt);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWhenSelectedItemIsNotNumber() {
 model.addElement("StringValue");
 comboBox.setSelectedIndex(3);
 comboBox.getSelectedInt();
 }

 @Test
 public void testSetSelectedIntWhenValueIsInteger() {
 comboBox.setSelectedInt(2);
 final Integer selectedValue = (Integer) comboBox.getSelectedValue();
 assertEquals(2, selectedValue.intValue());
 }

 @Test
 public void testSetSelectedIntWhenValueIsLong() {
 final SimpleComboBoxModelItem<Long> longItem = new SimpleComboBoxModelItem<>(6L);
 model.addElement(longItem);
 final int intValue = 6;
 comboBox.setSelectedInt(intValue);
 final Long selectedValue = (Long) comboBox.getSelectedValue();
 assertEquals(intValue, selectedValue.intValue());
 }

 @Test
 public void testSetSelectedIntWhenValueIsShort() {
 final SimpleComboBoxModelItem<Short> shortItem = new SimpleComboBoxModelItem((short) 5);
 model.addElement(shortItem);
 final int intValue = 5;
 comboBox.setSelectedInt(intValue);
 final Short selectedValue = (Short) comboBox.getSelectedValue();
 assertEquals(intValue, selectedValue.shortValue());
 }

 @Test
 public void testSetSelectedIntWhenValueIsByte() {
 final SimpleComboBoxModelItem<Byte> byteItem = new SimpleComboBoxModelItem((byte) 4);
 model.addElement(byteItem);
 final int intValue = 4;
 comboBox.setSelectedInt(intValue);
 final Byte selectedValue = (Byte) comboBox.getSelectedValue();
 assertEquals(intValue, selectedValue.intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIntWhenValueIsNotNumber() {
 model.addElement("StringValue");
 comboBox.setSelectedInt(4);
 }

 @Test
 public void testGetSelectedLongWhenSelectedItemIsLong() {
 final long selectedLong = comboBox.getSelectedLong();
 assertEquals(3L, selectedLong);
 }

 @Test
 public void testGetSelectedLongWhenNoSelection() {
 assertEquals(-1L, comboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongWhenSelectedItemIsInteger() {
 final SimpleComboBoxModelItem<Integer> intItem = new SimpleComboBoxModelItem<>(4);
 model.addElement(intItem);
 comboBox.setModel(model);
 comboBox.setSelectedIndex(3);
 final long selectedLong = comboBox.getSelectedLong();
 assertEquals(4L, selectedLong);
 }

 @Test
 public void testGetSelectedLongWhenSelectedItemIsShort() {
 final SimpleComboBoxModelItem<Short> shortItem = new SimpleComboBoxModelItem((short) 5);
 model.addElement(shortItem);
 comboBox.setModel(model);
 comboBox.setSelectedIndex(3);
 final long selectedLong = comboBox.getSelectedLong();
 assertEquals(5L, selectedLong);
 }

 @Test
 public void testGetSelectedLongWhenSelectedItemIsByte() {
 final SimpleComboBoxModelItem<Byte> byteItem = new SimpleComboBoxModelItem((byte) 4);
 model.addElement(byteItem);
 comboBox.setModel(model);
 comboBox.setSelectedIndex(3);
 final long selectedLong = comboBox.getSelectedLong();
 assertEquals(4L, selectedLong);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWhenSelectedItemIsNotNumber() {
 model.addElement("StringValue");
 comboBox.setSelectedIndex(3);
 comboBox.getSelectedLong();
 }

 @Test
 public void testSetSelectedLongWhenValueIsLong() {
 comboBox.setSelectedLong(3);
 final Long selectedValue = (Long) comboBox.getSelectedValue();
 assertEquals(3L, selectedValue);
 }

 @Test
 public void testSetSelectedLongWhenValueIsInteger() {
 final SimpleComboBoxModelItem<Integer> intItem = new SimpleComboBoxModelItem<>(3);
 model.addElement(intItem);
 comboBox.setModel(model);
 final long longValue = 3;
 comboBox.setSelectedLong(longValue);
 final Integer selectedValue = (Integer) comboBox.getSelectedValue();
 assertEquals(longValue, selectedValue.intValue());
 }

 @Test
 public void testSetSelectedLongWhenValueIsShort() {
 final SimpleComboBoxModelItem<Short> shortItem = new SimpleComboBoxModelItem((short) 3);
 model.addElement(shortItem);
 final long longValue = 3;
 comboBox.setSelectedLong(longValue);
 final Short selectedValue = (Short) comboBox.getSelectedValue();
 assertEquals(longValue, selectedValue.shortValue());
 }

 @Test
 public void testSetSelectedLongWhenValueIsByte() {
 final SimpleComboBoxModelItem<Byte> byteItem = new SimpleComboBoxModelItem((byte) 3);
 model.addElement(byteItem);
 final long longValue = 3;
 comboBox.setSelectedLong(longValue);
 final Byte selectedValue = (Byte) comboBox.getSelectedValue();
 assertEquals(longValue, selectedValue.intValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedLongWhenValueIsNotNumber() {
 model.addElement("StringValue");
 comboBox.setSelectedLong(3);
 }
}