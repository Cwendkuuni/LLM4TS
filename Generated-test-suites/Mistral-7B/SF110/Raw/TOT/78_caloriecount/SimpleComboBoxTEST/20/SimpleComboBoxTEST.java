package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 @Test
 public void testSimpleComboBoxWithIntegerValues() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel(new Object[] {1, 2, 3, 4, 5}));
 cb.setSelectedIndex(2);
 assertEquals(3, cb.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithLongValues() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel(new Object[] {1L, 2L, 3L, 4L, 5L}));
 cb.setSelectedIndex(2);
 assertEquals(3L, cb.getSelectedLong());
 }

 @Test
 public void testSimpleComboBoxWithShortValues() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel(new Object[] {(short)1, (short)2, (short)3, (short)4, (short)5}));
 cb.setSelectedIndex(2);
 assertEquals(3, cb.getSelectedInt());
 }

 @Test
 public void testSimpleComboBoxWithByteValues() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel(new Object[] {(byte)1, (byte)2, (byte)3, (byte)4, (byte)5}));
 cb.setSelectedIndex(2);
 assertEquals(3, cb.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSimpleComboBoxWithNonNumericValue() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel(new Object[] {"One", "Two", "Three", "Four", "Five"}));
 cb.setSelectedIndex(2);
 }

 @Test
 public void testSimpleComboBoxWithNullModel() {
 SimpleComboBox cb = new SimpleComboBox(null);
 assertNull(cb.getSelectedValue());
 }

 @Test
 public void testSimpleComboBoxWithEmptyModel() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel(new Object[] {}));
 assertNull(cb.getSelectedValue());
 }

 @Test
 public void testSimpleComboBoxWithNoSelectedValue() {
 SimpleComboBox cb = new SimpleComboBox(new SimpleComboBoxModel(new Object[] {1, 2, 3, 4, 5}));
 assertNull(cb.getSelectedValue());
 }

}