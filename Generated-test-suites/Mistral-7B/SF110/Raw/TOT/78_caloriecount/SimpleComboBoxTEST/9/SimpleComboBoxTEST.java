package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 @Test
 public void testSimpleComboBoxWithIntegerValues() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel<Integer>(new Integer[] {1, 2, 3, 4, 5}));
 cb.setSelectedIndex(2);
 assertEquals(3, cb.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithNullModel() {
 SimpleComboBox cb = new SimpleComboBox(null);
 assertNull(cb.getSelectedValue());
 }

 @Test
 public void testSimpleComboBoxWithLongValues() {
 SimpleComboBoxModel<Long> model = new SimpleComboBoxModel<Long>(new Long[] {1L, 2L, 3L, 4L, 5L});
 SimpleComboBox cb = new SimpleComboBox(model);
 cb.setSelectedIndex(2);
 assertEquals(3L, cb.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleComboBoxWithNonNumberValue() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel<String>(new String[] {"one", "two", "three", "four", "five"}));
 cb.setSelectedIndex(2);
 }

 @Test
 public void testSimpleComboBoxWithEmptyModel() {
 SimpleComboBoxModel<Integer> model = new SimpleComboBoxModel<Integer>();
 SimpleComboBox cb = new SimpleComboBox(model);
 assertNull(cb.getSelectedValue());
 }

}