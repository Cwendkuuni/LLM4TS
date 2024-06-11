package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private SimpleComboBoxModel simpleComboBoxModel;

 @Test
 public void testSimpleComboBoxWithValidModel() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel = new SimpleComboBoxModel());
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(0);

 Object selectedValue = simpleComboBox.getSelectedValue();
 assertEquals("Expected selected value to be null initially", null, selectedValue);

 simpleComboBoxModel.setSelectedValue("Test Value");
 simpleComboBox.setSelectedIndex(1);
 selectedValue = simpleComboBox.getSelectedValue();
 assertEquals("Expected selected value to match the selected value in the model", "Test Value", selectedValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithInvalidType() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(0);

 simpleComboBox.setSelectedValue("Invalid Value");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWithNullSelectedValue() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(0);

 int selectedInt = simpleComboBox.getSelectedInt();
 assertEquals("Expected int value to be -1 when null value is selected", -1, selectedInt);
 }

 @Test
 public void testGetSelectedIntWithValidIntegerValue() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBoxModel.setSelectedValue(1);
 simpleComboBox.setSelectedIndex(0);

 int selectedInt = simpleComboBox.getSelectedInt();
 assertEquals("Expected int value to match the selected integer value", 1, selectedInt);
 }

 @Test
 public void testGetSelectedIntWithValidShortValue() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBoxModel.setSelectedValue((short)1);
 simpleComboBox.setSelectedIndex(0);

 int selectedInt = simpleComboBox.getSelectedInt();
 assertEquals("Expected int value to match the selected short value", 1, selectedInt);
 }

 @Test
 public void testGetSelectedIntWithValidByteValue() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBoxModel.setSelectedValue((byte)1);
 simpleComboBox.setSelectedIndex(0);

 int selectedInt = simpleComboBox.getSelectedInt();
 assertEquals("Expected int value to match the selected byte value", 1, selectedInt);
 }

 @Test
 public void testGetSelectedLongWithValidIntegerValue() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBoxModel.setSelectedValue(1);
 simpleComboBox.setSelectedIndex(0);

 long selectedLong = simpleComboBox.getSelectedLong();
 assertEquals("Expected long value to match the selected integer value", 1L, selectedLong);
 }

 @Test
 public void testGetSelectedLongWithValidLongValue() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBoxModel.setSelectedValue(1L);
 simpleComboBox.setSelectedIndex(0);

 long selectedLong = simpleComboBox.getSelectedLong();
 assertEquals("Expected long value to match the selected long value", 1L, selectedLong);
 }

 @Test
 public void testGetSelectedLongWithValidShortValue() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBoxModel.setSelectedValue((short)1);
 simpleComboBox.setSelectedIndex(0);

 long selectedLong = simpleComboBox.getSelectedLong();
 assertEquals("Expected long value to match the selected short value", 1L, selectedLong);
 }

 @Test
 public void testGetSelectedLongWithValidByteValue() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBoxModel.setSelectedValue((byte)1);
 simpleComboBox.setSelectedIndex(0);

 long selectedLong = simpleComboBox.getSelectedLong();
 assertEquals("Expected long value to match the selected byte value", 1L, selectedLong);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithNullSelectedValue() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(0);

 long selectedLong = simpleComboBox.getSelectedLong();
 assertEquals("Expected IllegalArgumentException when null value is selected", IllegalArgumentException.class, selectedLong.getClass());
 }

 @Test
 public void testSetModelWithNullModel() {
 simpleComboBox = new SimpleComboBox(null);
 assertNull("Expected model to be null when null model is set", simpleComboBox.getModel());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetModelWithInvalidModelType() {
 simpleComboBox = new SimpleComboBox(new JComboBoxModel());
 }

 @Test
 public void testSetModelWithValidModel() {
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBoxModel.setSelectedValue("Test Value");
 simpleComboBox.setSelectedIndex(1);
 Object selectedValue = simpleComboBox.getSelectedValue();
 assertEquals("Expected selected value to match the selected value in the model", "Test Value", selectedValue);
 }
}