package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private SimpleComboBoxModel simpleComboBoxModel;

 @Before
 public void setUp() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 }

 @Test
 public void testSetAndGetSelectedValue() {
 // Test setting and getting integer value
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement(2);
 simpleComboBoxModel.setSelectedIndex(0);
 assertEquals(1, simpleComboBox.getSelectedValue());
 simpleComboBox.setSelectedValue(1);

 // Test setting and getting long value
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.addElement(2L);
 simpleComboBoxModel.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedValue());
 simpleComboBox.setSelectedValue(2L);

 // Test setting and getting short value
 simpleComboBoxModel.addElement((short)1);
 simpleComboBoxModel.addElement((short)2);
 simpleComboBoxModel.setSelectedIndex(0);
 assertEquals((short)1, simpleComboBox.getSelectedValue());
 simpleComboBox.setSelectedValue((short)1);

 // Test setting and getting byte value
 simpleComboBoxModel.addElement((byte)1);
 simpleComboBoxModel.addElement((byte)2);
 simpleComboBoxModel.setSelectedIndex(1);
 assertEquals((byte)2, simpleComboBox.getSelectedValue());
 simpleComboBox.setSelectedValue((byte)2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetAndGetInvalidValue() {
 simpleComboBoxModel.addElement("Invalid value");
 assertEquals(null, simpleComboBox.getSelectedValue());
 simpleComboBox.setSelectedValue("Invalid value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntAndLongWithNullModel() {
 simpleComboBox.setModel(null);
 assertEquals(-1, simpleComboBox.getSelectedInt());
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntAndLongWithInvalidValue() {
 simpleComboBoxModel.addElement("Invalid value");
 simpleComboBoxModel.setSelectedValue("Invalid value");
 assertEquals(-1, simpleComboBox.getSelectedInt());
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }
}