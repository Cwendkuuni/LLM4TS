package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private SimpleComboBoxModel simpleComboBoxModel;

 @Test
 public void testSimpleComboBoxWithValidModel() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     assertNotNull(simpleComboBox);
     assertEquals(simpleComboBoxModel, simpleComboBox.getModel());
 }

 @Test
 public void testConstructorWithValidModel() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     assertEquals(simpleComboBoxModel, simpleComboBox.getModel());
 }

 @Test
 public void testSetModelWithValidModel() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     simpleComboBox.setModel(simpleComboBoxModel);
     assertEquals(simpleComboBoxModel, simpleComboBox.getModel());
 }

 @Test
 public void testSetModelWithInvalidModel() {
     SimpleComboBox simpleComboBox = new SimpleComboBox(new JComboBoxModel());
     assertNull(simpleComboBox.mySimpleModel);
 }

 @Test
 public void testGetSelectedValueWhenNullModel() {
     SimpleComboBox simpleComboBox = new SimpleComboBox(null);
     assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueWhenValidValue() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBoxModel.addElement("Test");
     simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     simpleComboBoxModel.setSelectedItem("Test");
     assertEquals("Test", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedIntWhenValidIntValue() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBoxModel.addElement(new Integer(1));
     simpleComboBoxModel.addElement(new Integer(2));
     simpleComboBoxModel.setSelectedItem(new Integer(1));
     SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWhenValidLongValue() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBoxModel.addElement(new Integer(1));
     simpleComboBoxModel.addElement(new Long(2));
     simpleComboBoxModel.setSelectedItem(new Long(2));
     SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWhenValidShortValue() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBoxModel.addElement(new Integer(1));
     simpleComboBoxModel.addElement(new Short((short)2));
     simpleComboBoxModel.setSelectedItem(new Short((short)2));
     SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWhenValidByteValue() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBoxModel.addElement(new Integer(1));
     simpleComboBoxModel.addElement(new Byte((byte)2));
     simpleComboBoxModel.setSelectedItem(new Byte((byte)2));
     SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWhenInvalidValue() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBoxModel.addElement("Test");
     simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     simpleComboBoxModel.setSelectedItem("Test");
     simpleComboBox.getSelectedInt();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWhenInvalidValue() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBoxModel.addElement("Test");
     simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     simpleComboBoxModel.setSelectedItem("Test");
     simpleComboBox.getSelectedLong();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWhenNullModel() {
     SimpleComboBox simpleComboBox = new SimpleComboBox(null);
     simpleComboBox.getSelectedInt();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithInvalidValue() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBoxModel.addElement("Test");
     simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     try {
         simpleComboBox.setSelectedValue("Test2");
     } catch (IllegalArgumentException e) {
         fail("Expected IllegalArgumentException, but got: " + e.getMessage());
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithNullValue() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     try {
         simpleComboBox.setSelectedValue(null);
     } catch (IllegalArgumentException e) {
         fail("Expected IllegalArgumentException, but got: " + e.getMessage());
     }
 }

 @Test(expected = ClassCastException.class)
 public void testGetSelectedIntWhenInvalidClass() {
     simpleComboBoxModel = new SimpleComboBoxModel();
     simpleComboBoxModel.addElement("Test");
     simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
     simpleComboBoxModel.setSelectedItem("Test");
     simpleComboBox.getSelectedInt();
 }

}