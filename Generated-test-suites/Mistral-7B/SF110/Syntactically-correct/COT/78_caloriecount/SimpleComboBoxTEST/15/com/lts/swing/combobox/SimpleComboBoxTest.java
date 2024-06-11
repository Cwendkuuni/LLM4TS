package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.util.EventObject;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private SimpleComboBoxModel simpleComboBoxModel;

 @Before
 public void setUp() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 }

 // Test for constructor
 @Test
 public void testSimpleComboBox() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 assertNotNull(simpleComboBox);
 }

 // Test for setModel method
 @Test
 public void testSetModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel());
 simpleComboBox.setModel(simpleComboBoxModel);
 assertSame(simpleComboBoxModel, simpleComboBox.getModel());
 }

 // Test for getSelectedValue method with null mySimpleModel
 @Test(expected = NullPointerException.class)
 public void testGetSelectedValueWithNullMySimpleModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 simpleComboBox.getSelectedValue();
 }

 // Test for getSelectedValue method with valid value
 @Test
 public void testGetSelectedValue() {
 simpleComboBoxModel.addElement(42);
 simpleComboBoxModel.setSelectedIndex(0);
 Object selectedValue = simpleComboBox.getSelectedValue();
 assertEquals(42, selectedValue);
 }

 // Test for setSelectedValue method with null value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithNullValue() {
 simpleComboBox.setSelectedValue(null);
 }

 // Test for setSelectedValue method with valid value
 @Test
 public void testSetSelectedValue() {
 simpleComboBoxModel.addElement(42);
 simpleComboBoxModel.addElement("Forty Two");
 simpleComboBox.setSelectedValue("Forty Two");
 assertEquals("Forty Two", simpleComboBox.getSelectedItem());
 }

 // Test for getSelectedInt method with null selected value
 @Test(expected = NullPointerException.class)
 public void testGetSelectedIntWithNullSelectedValue() {
 simpleComboBox.getSelectedInt();
 }

 // Test for getSelectedInt method with valid integer value
 @Test
 public void testGetSelectedInt() {
 simpleComboBoxModel.addElement(42);
 simpleComboBoxModel.setSelectedIndex(0);
 int selectedInt = simpleComboBox.getSelectedInt();
 assertEquals(42, selectedInt);
 }

 // Test for getSelectedInt method with valid byte value
 @Test
 public void testGetSelectedIntWithValidByteValue() {
 simpleComboBoxModel.addElement((byte)42);
 simpleComboBoxModel.setSelectedIndex(0);
 int selectedInt = simpleComboBox.getSelectedInt();
 assertEquals(42, selectedInt);
 }

 // Test for getSelectedInt method with valid short value
 @Test
 public void testGetSelectedIntWithValidShortValue() {
 simpleComboBoxModel.addElement((short)42);
 simpleComboBoxModel.setSelectedIndex(0);
 int selectedInt = simpleComboBox.getSelectedInt();
 assertEquals(42, selectedInt);
 }

 // Test for getSelectedInt method with valid long value
 @Test
 public void testGetSelectedIntWithValidLongValue() {
 simpleComboBoxModel.addElement(42L);
 simpleComboBoxModel.setSelectedIndex(0);
 int selectedInt = simpleComboBox.getSelectedInt();
 assertEquals(42, selectedInt);
 }

 // Test for getSelectedLong method with null selected value
 @Test(expected = NullPointerException.class)
 public void testGetSelectedLongWithNullSelectedValue() {
 simpleComboBox.getSelectedLong();
 }

 // Test for getSelectedLong method with valid integer value
 @Test
 public void testGetSelectedLong() {
 simpleComboBoxModel.addElement(42L);
 simpleComboBoxModel.setSelectedIndex(0);
 long selectedLong = simpleComboBox.getSelectedLong();
 assertEquals(42L, selectedLong);
 }

 // Test for getSelectedLong method with valid byte value
 @Test
 public void testGetSelectedLongWithValidByteValue() {
 simpleComboBoxModel.addElement((byte)42);
 simpleComboBoxModel.setSelectedIndex(0);
 long selectedLong = simpleComboBox.getSelectedLong();
 assertEquals(42L, selectedLong);
 }

 // Test for getSelectedLong method with valid short value
 @Test
 public void testGetSelectedLongWithValidShortValue() {
 simpleComboBoxModel.addElement((short)42);
 simpleComboBoxModel.setSelectedIndex(0);
 long selectedLong = simpleComboBox.getSelectedLong();
 assertEquals(42L, selectedLong);
 }

 // Test for getSelectedLong method with valid long value
 @Test
 public void testGetSelectedLongWithValidLongValue() {
 simpleComboBoxModel.addElement(42L);
 simpleComboBoxModel.setSelectedIndex(0);
 long selectedLong = simpleComboBox.getSelectedLong();
 assertEquals(42L, selectedLong);
 }

 // Test for getSelectedLong method with invalid value
 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithInvalidValue() {
 simpleComboBoxModel.addElement("Forty Two");
 simpleComboBoxModel.setSelectedIndex(0);
 long selectedLong = simpleComboBox.getSelectedLong();
 fail();
 }

}