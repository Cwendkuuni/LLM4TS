package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private SimpleComboBoxModel simpleComboBoxModel;

 @Test
 public void testSimpleComboBoxConstructionWithModel() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 assertNotNull(simpleComboBox);
 }

 @Test
 public void testSetModel() {
 simpleComboBox = new SimpleComboBox((ComboBoxModel)new DefaultComboBoxModel());
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(simpleComboBoxModel, simpleComboBox.getModel());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetModelWithInvalidModel() {
 simpleComboBox = new SimpleComboBox((ComboBoxModel)new JComboBoxModel());
 simpleComboBox.setModel(simpleComboBoxModel);
 }

 @Test
 public void testGetSelectedValue() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement("Element 1");
 simpleComboBoxModel.addElement("Element 2");
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(0);
 assertEquals("Element 1", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueWhenNothingIsSelected() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValue() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement("Element 1");
 simpleComboBoxModel.addElement("Element 2");
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setSelectedValue("Element 1");
 assertEquals("Element 1", simpleComboBox.getSelectedValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithInvalidValue() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement("Element 1");
 simpleComboBoxModel.addElement("Element 2");
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setSelectedValue("Invalid Value");
 }

 @Test
 public void testGetSelectedInt() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement(2);
 simpleComboBoxModel.addElement(3);
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWhenNothingIsSelected() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement(2);
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 assertNegative(simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedLong() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.addElement(2L);
 simpleComboBoxModel.addElement(3L);
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongWhenNothingIsSelected() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.addElement(2L);
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 assertNegative(simpleComboBox.getSelectedLong());
 }
}