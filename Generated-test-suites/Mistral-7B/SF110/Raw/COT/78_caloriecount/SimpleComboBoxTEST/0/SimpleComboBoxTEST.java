package com.lts.swing.combobox.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
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

 @Test
 public void testSimpleComboBoxConstructor() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 assertEquals(simpleComboBoxModel, simpleComboBox.getModel());
 }

 @Test
 public void testSetModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new ComboBoxModel() {});
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(simpleComboBoxModel, simpleComboBox.getModel());
 }

 @Test
 public void testGetSelectedValueNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueEmptyModel() {
 simpleComboBoxModel.addElement("Empty");
 simpleComboBox.setModel(simpleComboBoxModel);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueValidValue() {
 simpleComboBoxModel.addElement("Valid Value");
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals("Valid Value", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedIntNullValue() {
 assertEqual(-1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntIntegerValue() {
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntLongValue() {
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntShortValue() {
 simpleComboBoxModel.addElement((short) 1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntByteValue() {
 simpleComboBoxModel.addElement((byte) 1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntInvalidValue() {
 simpleComboBoxModel.addElement("Invalid Value");
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 }

 @Test
 public void testGetSelectedLongNullValue() {
 assertEqual(-1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongIntegerValue() {
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongLongValue() {
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongShortValue() {
 simpleComboBoxModel.addElement((short) 1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongByteValue() {
 simpleComboBoxModel.addElement((byte) 1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongInvalidValue() {
 simpleComboBoxModel.addElement("Invalid Value");
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueNull() {
 simpleComboBox.setSelectedValue(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueInvalidType() {
 simpleComboBox.setSelectedValue("Invalid Value");
 }

 @Test
 public void testSetSelectedValueValidValue() {
 simpleComboBoxModel.addElement("Valid Value");
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedValue("Valid Value");
 assertEquals("Valid Value", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedIntValue() {
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedValue(1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testSetSelectedIntValueNegative() {
 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedValue(-1);
 assertEqual(-1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testSetSelectedLongValue() {
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedValue(1L);
 assertEquals(1, simpleComboBox.getSelectedInt());
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testSetSelectedLongValueNegative() {
 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedValue(-1L);
 assertEqual(-1, simpleComboBox.getSelectedInt());
 assertEqual(-1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testSetSelectedShortValue() {
 simpleComboBoxModel.addElement((short) 1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedValue((short) 1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 assertEquals((short) 1, simpleComboBox.getSelectedShort());
 }

 @Test
 public void testSetSelectedShortValueNegative() {
 simpleComboBoxModel.addElement((short) 1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedValue((short) -1);
 assertEqual(-1, simpleComboBox.getSelectedInt());
 assertEqual((short) -1, simpleComboBox.getSelectedShort());
 }

 @Test
 public void testSetSelectedByteValue() {
 simpleComboBoxModel.addElement((byte) 1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedValue((byte) 1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 assertEquals((byte) 1, simpleComboBox.getSelectedByte());
 }

 @Test
 public void testSetSelectedByteValueNegative() {
 simpleComboBoxModel.addElement((byte) 1);
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setSelectedValue((byte) -1);
 assertEqual(-1, simpleComboBox.getSelectedInt());
 assertEqual((byte) -1, simpleComboBox.getSelectedByte());
 }

 @Test
 public void testFireStateChangedEvent() {
 final SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 final Component listener = new JFrame();
 simpleComboBox.addActionListener(e -> {
 });
 simpleComboBox.setSelectedIndex(0);
 SwingUtilities.invokeLater(() -> simpleComboBox.setSelectedIndex(1));
 SwingUtilities.invokeLater(() -> simpleComboBox.fireStateChangedEvent());
 assertEquals(1, simpleComboBox.getSelectedIndex());
 assertEquals(1, ((EventObject) e).getSource().getSelectedIndex());
 }

 @Test
 public void testSetModelTwice() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(simpleComboBoxModel);
 simpleComboBox.setModel(new ComboBoxModel() {});
 assertEquals(new ComboBoxModel(), simpleComboBox.getModel());
 }

}