package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private SimpleComboBoxModel simpleComboBoxModel;

 @Before
 public void setup() {
 simpleComboBoxModel = new SimpleComboBoxModel();
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 }

 // Test constructor with valid SimpleComboBoxModel
 @Test
 public void testSimpleComboBoxWithValidModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 assertNotNull(simpleComboBox);
 }

 // Test setModel method with valid SimpleComboBoxModel
 @Test
 public void testSetModelWithValidModel() {
 simpleComboBox.setModel(simpleComboBoxModel);
 assertSame(simpleComboBoxModel, simpleComboBox.getModel());
 }

 // Test getSelectedValue method with null model
 @Test(expected = NullPointerException.class)
 public void testGetSelectedValueWithNullModel() {
 simpleComboBox.setModel(null);
 simpleComboBox.getSelectedValue();
 }

 // Test getSelectedValue method with null selection
 @Test
 public void testGetSelectedValueWithNullSelection() {
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.getSelectedValue();
 assertNull(simpleComboBox.getSelectedValue());
 }

 // Test getSelectedValue method with valid selection
 @Test
 public void testGetSelectedValueWithValidSelection() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals("Option 1", simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with null selection
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithNullSelection() {
 simpleComboBox.setSelectedValue(null);
 }

 // Test setSelectedValue method with int value
 @Test
 public void testSetSelectedValueWithIntValue() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBox.setSelectedValue(0);
 assertEquals("Option 1", simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with long value
 @Test
 public void testSetSelectedValueWithLongValue() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBox.setSelectedValue(1L);
 assertEquals("Option 1", simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with short value
 @Test
 public void testSetSelectedValueWithShortValue() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBox.setSelectedValue((short) 1);
 assertEquals("Option 1", simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with byte value
 @Test
 public void testSetSelectedValueWithByteValue() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBox.setSelectedValue((byte) 1);
 assertEquals("Option 1", simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with invalid value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithInvalidValue() {
 simpleComboBox.setSelectedValue("Invalid value");
 }

 // Test getSelectedInt method with null selection
 @Test
 public void testGetSelectedIntWithNullSelection() {
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.getSelectedInt();
 assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with int selection
 @Test
 public void testGetSelectedIntWithIntSelection() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(0, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with long selection
 @Test
 public void testGetSelectedIntWithLongSelection() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBoxModel.setSelectedIndex(1);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with short selection
 @Test
 public void testGetSelectedIntWithShortSelection() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBoxModel.setSelectedIndex(2);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with byte selection
 @Test
 public void testGetSelectedIntWithByteSelection() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBoxModel.setSelectedIndex(3);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(3, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedLong method with null selection
 @Test
 public void testGetSelectedLongWithNullSelection() {
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.getSelectedLong();
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with int selection
 @Test
 public void testGetSelectedLongWithIntSelection() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(0L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with long selection
 @Test
 public void testGetSelectedLongWithLongSelection() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBoxModel.setSelectedIndex(1);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with short selection
 @Test
 public void testGetSelectedLongWithShortSelection() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBoxModel.setSelectedIndex(2);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with byte selection
 @Test
 public void testGetSelectedLongWithByteSelection() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBoxModel.setSelectedIndex(3);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(3L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with invalid selection
 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithInvalidSelection() {
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBoxModel.setSelectedIndex(-1);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.getSelectedLong();
 }

 // Test itemStateChanged method
 @Test
 public void testItemStateChanged() {
 simpleComboBoxModel.addElement("Option 1");
 simpleComboBoxModel.addElement("Option 2");
 simpleComboBoxModel.addChangeListener(e -> System.out.println("Item state changed: " + e.getStateChange()));
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.setFocusable(false);
 simpleComboBox.setEditable(false);
 simpleComboBox.itemStateChanged(new ItemEvent(simpleComboBox, ItemEvent.SELECTED, 0, false));
 assertTrue(simpleComboBoxModel.isSelected(0));
 }

 // Test setModel method with invalid ComboBoxModel
 @Test(expected = IllegalArgumentException.class)
 public void testSetModelWithInvalidModel() {
 simpleComboBox.setModel(new JComboBoxModel() {});
 }
}