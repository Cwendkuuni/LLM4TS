package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 @Test
 public void testSimpleComboBoxConstruction() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 assertNotNull(simpleComboBox);
 }

 @Test
 public void testSetModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 ComboBoxModel model = new DefaultComboBoxModel();
 simpleComboBox.setModel(model);
 assertFalse(simpleComboBox.mySimpleModel instanceof SimpleComboBoxModel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetModelWithInvalidObject() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setModel(new Object());
 }

 @Test
 public void testSetModelWithValidSimpleComboBoxModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setModel(new SimpleComboBoxModel());
 assertTrue(simpleComboBox.mySimpleModel instanceof SimpleComboBoxModel);
 }

 @Test
 public void testGetSelectedValueNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueInteger() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(Integer.valueOf(2), simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueLong() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(Long.valueOf(2L), simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueShort() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(Short.valueOf((short)2), simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueByte() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(Byte.valueOf((byte)2), simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueNull() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setSelectedValue(null);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueInteger() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.setSelectedValue(2);
 assertEquals(Integer.valueOf(2), simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueLong() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.setSelectedValue(2L);
 assertEquals(Long.valueOf(2L), simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueShort() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.setSelectedValue(2);
 assertEquals(Short.valueOf((short)2), simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueByte() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.setSelectedValue((byte)2);
 assertEquals(Byte.valueOf((byte)2), simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedIntNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntInteger() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntLong() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntShort() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntByte() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWithInvalidType() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.setSelectedValue("Invalid Type");
 }

 @Test
 public void testGetSelectedLongNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongInteger() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongLong() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongShort() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongByte() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithInvalidType() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.setSelectedValue("Invalid Type");
 }

}