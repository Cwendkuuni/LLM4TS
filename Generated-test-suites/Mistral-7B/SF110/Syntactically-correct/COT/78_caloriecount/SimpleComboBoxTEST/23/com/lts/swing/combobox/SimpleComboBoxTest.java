package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

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

 // Test constructor
 @Test
 public void testSimpleComboBoxConstructor() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 assertNotNull(simpleComboBox);
 }

 // Test setModel method
 @Test
 public void testSetModel() throws IllegalAccessException, InvocationTargetException {
 SimpleComboBoxModel newModel = new SimpleComboBoxModel();
 simpleComboBox.setModel(newModel);
 assertEquals(newModel, simpleComboBox.getModel());
 }

 // Test getSelectedValue method with null model
 @Test
 public void testGetSelectedValueWithNullModel() {
 assertNull(simpleComboBox.getSelectedValue());
 }

 // Test getSelectedValue method with integer value
 @Test
 public void testGetSelectedValueWithIntegerValue() {
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement(2);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setSelectedIndex(0);
 assertEquals(1, simpleComboBox.getSelectedValue());
 }

 // Test getSelectedValue method with long value
 @Test
 public void testGetSelectedValueWithLongValue() {
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.addElement(2L);
 simpleComboBoxModel.setSelectedIndex(1);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedValue());
 }

 // Test getSelectedValue method with short value
 @Test
 public void testGetSelectedValueWithShortValue() {
 simpleComboBoxModel.addElement((short)1);
 simpleComboBoxModel.addElement((short)2);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setSelectedIndex(0);
 assertEquals((short)1, simpleComboBox.getSelectedValue());
 }

 // Test getSelectedValue method with byte value
 @Test
 public void testGetSelectedValueWithByteValue() {
 simpleComboBoxModel.addElement((byte)1);
 simpleComboBoxModel.addElement((byte)2);
 simpleComboBoxModel.setSelectedIndex(1);
 simpleComboBox.setSelectedIndex(1);
 assertEquals((byte)2, simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with null value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithNullValue() {
 simpleComboBox.setSelectedValue(null);
 }

 // Test setSelectedValue method with integer value
 @Test
 public void testSetSelectedValueWithIntegerValue() {
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement(2);
 simpleComboBox.setSelectedValue(1);
 assertEquals(1, simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with long value
 @Test
 public void testSetSelectedValueWithLongValue() {
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.addElement(2L);
 simpleComboBox.setSelectedValue(2L);
 assertEquals(2L, simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with short value
 @Test
 public void testSetSelectedValueWithShortValue() {
 simpleComboBoxModel.addElement((short)1);
 simpleComboBoxModel.addElement((short)2);
 simpleComboBox.setSelectedValue((short)2);
 assertEquals((short)2, simpleComboBox.getSelectedValue());
 }

 // Test setSelectedValue method with byte value
 @Test
 public void testSetSelectedValueWithByteValue() {
 simpleComboBoxModel.addElement((byte)1);
 simpleComboBoxModel.addElement((byte)2);
 simpleComboBox.setSelectedValue((byte)2);
 assertEquals((byte)2, simpleComboBox.getSelectedValue());
 }

 // Test getSelectedInt method with null value
 @Test
 public void testGetSelectedIntWithNullValue() {
 assertFalse(simpleComboBox.getSelectedInt() == -1);
 }

 // Test getSelectedInt method with integer value
 @Test
 public void testGetSelectedIntWithIntegerValue() {
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement(2);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setSelectedIndex(0);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with long value
 @Test
 public void testGetSelectedIntWithLongValue() {
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.addElement(2L);
 simpleComboBoxModel.setSelectedIndex(1);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with short value
 @Test
 public void testGetSelectedIntWithShortValue() {
 simpleComboBoxModel.addElement((short)1);
 simpleComboBoxModel.addElement((short)2);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setSelectedIndex(0);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedInt method with byte value
 @Test
 public void testGetSelectedIntWithByteValue() {
 simpleComboBoxModel.addElement((byte)1);
 simpleComboBoxModel.addElement((byte)2);
 simpleComboBoxModel.setSelectedIndex(1);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 // Test getSelectedLong method with null value
 @Test
 public void testGetSelectedLongWithNullValue() {
 assertFalse(simpleComboBox.getSelectedLong() == -1L);
 }

 // Test getSelectedLong method with integer value
 @Test
 public void testGetSelectedLongWithIntegerValue() {
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement(2);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setSelectedIndex(0);
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with long value
 @Test
 public void testGetSelectedLongWithLongValue() {
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.addElement(2L);
 simpleComboBoxModel.setSelectedIndex(1);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with short value
 @Test
 public void testGetSelectedLongWithShortValue() {
 simpleComboBoxModel.addElement((short)1);
 simpleComboBoxModel.addElement((short)2);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setSelectedIndex(0);
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 // Test getSelectedLong method with byte value
 @Test
 public void testGetSelectedLongWithByteValue() {
 simpleComboBoxModel.addElement((byte)1);
 simpleComboBoxModel.addElement((byte)2);
 simpleComboBoxModel.setSelectedIndex(1);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 // Test setSelectedInt method with negative value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedIntWithNegativeValue() {
 simpleComboBox.setSelectedInt(-1);
 }

 // Test setSelectedInt method with valid integer value
 @Test
 public void testSetSelectedIntWithValidIntegerValue() {
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement(2);
 simpleComboBox.setSelectedInt(1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 // Test setSelectedLong method with negative value
 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedLongWithNegativeValue() {
 simpleComboBox.setSelectedLong(-1L);
 }

 // Test setSelectedLong method with valid long value
 @Test
 public void testSetSelectedLongWithValidLongValue() {
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.addElement(2L);
 simpleComboBox.setSelectedLong(2L);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 // Test setSelectedInt method with valid short value
 @Test
 public void testSetSelectedIntWithValidShortValue() {
 simpleComboBoxModel.addElement((short)1);
 simpleComboBoxModel.addElement((short)2);
 simpleComboBox.setSelectedInt((short)2);
 assertEquals((short)2, simpleComboBox.getSelectedInt());
 }

 // Test setSelectedInt method with valid byte value
 @Test
 public void testSetSelectedIntWithValidByteValue() {
 simpleComboBoxModel.addElement((byte)1);
 simpleComboBoxModel.addElement((byte)2);
 simpleComboBox.setSelectedInt((byte)2);
 assertEquals((byte)2, simpleComboBox.getSelectedInt());
 }

 // Test setSelectedLong method with valid short value
 @Test
 public void testSetSelectedLongWithValidShortValue() {
 simpleComboBoxModel.addElement((short)1);
 simpleComboBoxModel.addElement((short)2);
 simpleComboBox.setSelectedLong((short)2);
 assertEquals((short)2, simpleComboBox.getSelectedInt());
 }

 // Test setSelectedLong method with valid byte value
 @Test
 public void testSetSelectedLongWithValidByteValue() {
 simpleComboBoxModel.addElement((byte)1);
 simpleComboBoxModel.addElement((byte)2);
 simpleComboBox.setSelectedLong((byte)2);
 assertEquals((byte)2, simpleComboBox.getSelectedInt());
 }

 // Test addItem method with null object
 @Test(expected = NullPointerException.class)
 public void testAddItemWithNullObject() {
 simpleComboBox.addItem(null);
 }

 // Test addItem method with valid object
 @Test
 public void testAddItemWithValidObject() {
 simpleComboBoxModel.addElement("Item 1");
 simpleComboBoxModel.addElement("Item 2");
 simpleComboBox.addItem("Item 3");
 assertEquals(3, simpleComboBox.getItemCount());
 }

 // Test setSelectedIndex method with negative index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexWithNegativeIndex() {
 simpleComboBoxModel.addElement("Item 1");
 simpleComboBoxModel.addElement("Item 2");
 simpleComboBox.setSelectedIndex(-1);
 }

 // Test setSelectedIndex method with valid index
 @Test
 public void testSetSelectedIndexWithValidIndex() {
 simpleComboBoxModel.addElement("Item 1");
 simpleComboBoxModel.addElement("Item 2");
 simpleComboBoxModel.setSelectedIndex(1);
 simpleComboBox.setSelectedIndex(1);
 assertEquals("Item 2", simpleComboBox.getSelectedItem());
 }

 // Test fireItemStateChanged event
 @Test
 public void testFireItemStateChangedEvent() {
 simpleComboBoxModel.addElement("Item 1");
 simpleComboBoxModel.addElement("Item 2");
 simpleComboBoxModel.setSelectedIndex(1);
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.addItemListener(e -> {});
 simpleComboBoxModel.setSelectedIndex(0);
 assertEquals(ItemEvent.ITEM_STATE_CHANGED, simpleComboBox.getItemEvent().getID());
 }
}