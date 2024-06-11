package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private SimpleComboBoxModel simpleComboBoxModel;

 @Before
 public void setUp() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 }

 // Test constructor
 @Test
 public void testSimpleComboBox() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 assertNotNull(simpleComboBox);
 }

 // Test setModel method
 @Test
 public void testSetModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 assertNotNull(simpleComboBox.mySimpleModel);
 simpleComboBox.setModel(null);
 assertNull(simpleComboBox.mySimpleModel);
 }

 // Test getSelectedValue method
 @Test
 public void testGetSelectedValue() {
 simpleComboBox.setSelectedIndex(0);
 assertEquals("Expected value", simpleComboBoxModel.getSelectedItem(), simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueWithNoSelection() {
 assertEquals(null, simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method
 @Test
 public void testSetSelectedValue() {
 simpleComboBox.setModel(new DefaultComboBoxModel());
 simpleComboBox.addItem("Item 1");
 simpleComboBox.addItem("Item 2");
 simpleComboBox.setSelectedValue("Item 1");
 assertEquals("Expected value", "Item 1", simpleComboBox.getSelectedItem());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithInvalidType() {
 simpleComboBox.setModel(new DefaultComboBoxModel());
 simpleComboBox.addItem("Item 1");
 simpleComboBox.addItem("Item 2");
 simpleComboBox.setSelectedValue(new Object());
 }

 // Test getSelectedInt method
 @Test
 public void testGetSelectedInt() {
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement(2);
 simpleComboBoxModel.addElement(3);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWithNoSelection() {
 assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 @Test
 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWithInvalidType() {
 simpleComboBoxModel.addElement("Item 1");
 simpleComboBoxModel.addElement("Item 2");
 simpleComboBoxModel.addElement("Item 3");
 simpleComboBox.setSelectedIndex(1);
 assertEquals(-1, simpleComboBox.getSelectedInt());
 simpleComboBox.setSelectedValue("Item 1");
 assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedLong method
 @Test
 public void testGetSelectedLong() {
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.addElement(2L);
 simpleComboBoxModel.addElement(3L);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongWithNoSelection() {
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 @Test
 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithInvalidType() {
 simpleComboBoxModel.addElement("Item 1");
 simpleComboBoxModel.addElement("Item 2");
 simpleComboBoxModel.addElement("Item 3");
 simpleComboBox.setSelectedIndex(1);
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 simpleComboBox.setSelectedValue("Item 1");
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

}