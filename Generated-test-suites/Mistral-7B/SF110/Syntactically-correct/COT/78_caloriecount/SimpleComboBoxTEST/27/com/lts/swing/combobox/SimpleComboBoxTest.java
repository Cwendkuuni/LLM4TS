package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 // Test constructor
 @Test
 public void testSimpleComboBox() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 assertNotNull(simpleComboBox);
 }

 // Test setModel method with valid SimpleComboBoxModel
 @Test
 public void testSetModelValidSimpleComboBoxModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 ComboBoxModel model = new SimpleComboBoxModel();
 simpleComboBox.setModel(model);
 assertEquals(model, simpleComboBox.getModel());
 }

 // Test setModel method with invalid ComboBoxModel
 @Test(expected = IllegalArgumentException.class)
 public void testSetModelInvalidComboBoxModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setModel(new Object());
 }

 // Test getSelectedValue method with null model
 @Test
 public void testGetSelectedValueNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertNull(simpleComboBox.getSelectedValue());
 }

 // Test getSelectedValue method with valid model and selected value
 @Test
 public void testGetSelectedValueValidModelSelectedValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("Option 1");
 model.addElement("Option 2");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(0);
 assertEquals("Option 1", simpleComboBox.getSelectedValue());
 }

 // Test getSelectedValue method with valid model but no selected value
 @Test
 public void testGetSelectedValueValidModelNoSelectedValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 assertEquals(null, simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with null value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueNullValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setSelectedValue(null);
 }

 // Test setSelectedValue method with valid Integer value
 @Test
 public void testSetSelectedValueValidIntegerValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("Option 1");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedValue(new Integer(1));
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 // Test setSelectedValue method with valid Short value
 @Test
 public void testSetSelectedValueValidShortValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("Option 1");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedValue((short)1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 // Test setSelectedValue method with valid Byte value
 @Test
 public void testSetSelectedValueValidByteValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("Option 1");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedValue((byte)1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 // Test setSelectedValue method with valid Long value
 @Test
 public void testSetSelectedValueValidLongValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("Option 1");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedValue(new Long(1));
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedLong method with null model
 @Test
 public void testGetSelectedLongNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertNull(simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with valid model and selected Integer value
 @Test
 public void testGetSelectedLongValidIntegerValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("Option 1");
 model.addElement("Option 2");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(0);
 assertEquals(1, simpleComboBox.getSelectedInt());
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with valid model and selected Short value
 @Test
 public void testGetSelectedLongValidShortValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("Option 1");
 model.addElement("Option 2");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.setSelectedValue((short)1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with valid model and selected Byte value
 @Test
 public void testGetSelectedLongValidByteValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("Option 1");
 model.addElement("Option 2");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.setSelectedValue((byte)1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with valid model and selected Long value
 @Test
 public void testGetSelectedLongValidLongValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("Option 1");
 model.addElement("Option 2");
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.setSelectedValue(new Long(1));
 assertEquals(1, simpleComboBox.getSelectedInt());
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with valid model but no selected value
 @Test
 public void testGetSelectedLongValidModelNoSelectedValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

}