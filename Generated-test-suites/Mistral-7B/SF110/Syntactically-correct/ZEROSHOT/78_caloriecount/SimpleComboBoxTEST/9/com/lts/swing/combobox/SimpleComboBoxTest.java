package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private SimpleComboBoxModel simpleComboBoxModel;

 @Test
 public void testSimpleComboBox() {
 simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 assertNotNull(simpleComboBox);
 }

 @Test
 public void testSetModel() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertSame(simpleComboBoxModel, simpleComboBox.getModel());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetModelWithNullModel() {
 simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setModel(null);
 }

 @Test
 public void testSetModelWithDifferentTypeModel() {
 simpleComboBoxModel = new DefaultComboBoxModel();
 simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setModel(defaultComboBoxModel);
 assertNotSame(defaultComboBoxModel, simpleComboBox.getModel());
 }

 @Test
 public void testGetSelectedValue() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement("Two");
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(0);
 assertEquals(1, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueWithString() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement("Two");
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(1);
 assertEquals("Two", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedInt() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement("Two");
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(0);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWithNullValue() {
 simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setSelectedIndex(-1);
 simpleComboBox.getSelectedInt();
 }

 @Test
 public void testGetSelectedLong() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.addElement("Two");
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(0);
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithStringValue() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement("Two");
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.getSelectedLong();
 }
}