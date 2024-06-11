package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

 private SimpleComboBox cb;
 private SimpleComboBoxModel model;

 @Test
 public void testSimpleComboBox() {
 cb = new SimpleComboBox(new SimpleComboBoxModel());
 // empty model test
 assertEquals(0, cb.getItemCount());
 // add items to the model
 model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 cb = new SimpleComboBox(model);
 // check items in the combobox
 assertEquals(3, cb.getItemCount());
 assertEquals(1, cb.getSelectedIndex());
 // select an item
 cb.setSelectedIndex(1);
 assertEquals(2, cb.getSelectedValue());
 // set value manually
 cb.setSelectedValue(3);
 assertEquals(3, cb.getSelectedIndex());
 // test getSelectedInt and getSelectedLong
 cb.setSelectedValue(1);
 assertEquals(1, cb.getSelectedInt());
 cb.setSelectedValue(5L);
 assertEquals(5, cb.getSelectedInt());
 cb.setSelectedValue(10);
 assertEquals(-1, cb.getSelectedInt());
 cb.setSelectedValue("test");
 throw new IllegalArgumentException();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithInvalidType() {
 cb.setSelectedValue('a');
 }

 @Test
 public void testGetSelectedLong() {
 cb.setModel(new DefaultComboBoxModel<Integer>() {{
 addElement(1);
 addElement(2);
 addElement(3);
 addElement(Long.valueOf(4L));
 }});
 cb.setSelectedValue(3);
 assertEquals(3, cb.getSelectedInt());
 cb.setSelectedIndex(3);
 assertEquals(4L, cb.getSelectedLong());
 }

 @Test
 public void testSetAndGetSelectedValueWithNullModel() {
 cb = new SimpleComboBox(null);
 assertNull(cb.getSelectedValue());
 cb.setSelectedValue(new Integer(1));
 assertNull(cb.getSelectedValue());
 }

 @Test
 public void testSetModelWithInvalidModelType() {
 cb.setModel(new JComboBoxModel<Component>() {{
 addElement(new JButton());
 addElement(new JLabel());
 }});
 assertNull(cb.getSelectedValue());
 }
}