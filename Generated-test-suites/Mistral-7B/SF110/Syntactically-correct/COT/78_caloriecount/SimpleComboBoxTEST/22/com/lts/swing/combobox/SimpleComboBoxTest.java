package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private JFrame frame;

 @Before
 public void setUp() {
 SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
 simpleModel.addElement("Element 1");
 simpleModel.addElement("Element 2");
 simpleModel.addElement("Element 3");
 simpleComboBox = new SimpleComboBox(simpleModel);
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 frame.add(simpleComboBox, 10, 10);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> simpleComboBox.requestFocusInWindow());
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testSimpleComboBoxConstructor() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertNull(simpleComboBox.mySimpleModel);
 }

 @Test
 public void testSetModel() {
 SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
 assertNull(simpleComboBox.mySimpleModel);
 simpleComboBox.setModel(simpleModel);
 assertSame(simpleModel, simpleComboBox.mySimpleModel);
 }

 @Test
 public void testGetSelectedValueNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValue() {
 Object selectedValue = simpleComboBox.getSelectedValue();
 assertEquals("Element 1", selectedValue);
 simpleComboBox.setSelectedIndex(1);
 selectedValue = simpleComboBox.getSelectedValue();
 assertEquals("Element 2", selectedValue);
 }

 @Test
 public void testSetSelectedValueNullObject() {
 assertDoesNotThrow(() -> simpleComboBox.setSelectedValue(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueNotSupportedType() {
 simpleComboBox.setSelectedValue("String");
 }

 @Test
 public void testSetSelectedValue() {
 simpleComboBox.setSelectedValue(3);
 assertEquals("Element 3", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedIntNullValue() {
 assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntIntegerValue() {
 simpleComboBox.setSelectedValue(1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntLongValue() {
 simpleComboBox.setSelectedValue(3L);
 assertEquals(3, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntShortValue() {
 simpleComboBox.setSelectedValue((short)3);
 assertEquals(3, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntByteValue() {
 simpleComboBox.setSelectedValue((byte)3);
 assertEquals(3, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntNotFoundType() {
 simpleComboBox.setSelectedValue("String");
 simpleComboBox.getSelectedInt();
 }

 @Test
 public void testGetSelectedLongNullValue() {
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongIntegerValue() {
 simpleComboBox.setSelectedValue(1);
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongLongValue() {
 simpleComboBox.setSelectedValue(3L);
 assertEquals(3L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongShortValue() {
 simpleComboBox.setSelectedValue((short)3);
 assertEquals(3L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongByteValue() {
 simpleComboBox.setSelectedValue((byte)3);
 assertEquals(3L, simpleComboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongNotFoundType() {
 simpleComboBox.setSelectedValue("String");
 simpleComboBox.getSelectedLong();
 }

 @Test
 public void testComboBoxEvent() {
 simpleComboBox.addItemListener(e -> {
 if (e.getStateChange() == ItemEvent.SELECTED) {
 Equals(3, simpleComboBox.getSelectedInt());
 }
 });
 simpleComboBox.setSelectedValue(3);
 }

 @Test
 public void testComboBoxModelChangeListener() {
 SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
 simpleModel.addChangeListener(e -> {
 if (e.getStateChange() == ComboBoxModelEvent.STATE_CHANGED) {
 assertEquals(3, simpleComboBox.getSelectedInt());
 }
 });
 simpleModel.setSelectedItem("Element 3");
 }

 @Test
 public void testRemoveAllElements() {
 SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
 simpleModel.addElement("Element 1");
 simpleModel.addElement("Element 2");
 simpleModel.addElement("Element 3");
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
 simpleComboBox.setSelectedValue(1);
 simpleModel.removeElement("Element 1");
 simpleComboBox.getItemCount(); // should return 2
 simpleComboBox.setSelectedIndex(0); // should throw an IndexOutOfBoundsException
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetSelectedIndexOutOfBounds() {
 simpleComboBox.setSelectedIndex(20);
 }

 @Test
 public void testAddElementNullElement() {
 SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
 simpleModel.addElement(null);
 }

 @Test(expected = NullPointerException.class)
 public void testAddElementNullModel() {
 SimpleComboBoxModel simpleModel = null;
 simpleComboBox.setModel(simpleModel);
 simpleModel.addElement("Element 1");
 }
}