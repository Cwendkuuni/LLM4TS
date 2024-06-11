package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private SimpleComboBoxModel simpleComboBoxModel;

 @Before
 public void setUp() {
 simpleComboBoxModel = mock(SimpleComboBoxModel.class);
 simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 }

 @Test
 public void testSimpleComboBoxConstruction() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 assertNull(simpleComboBox.getSelectedValue());
 assertNull(simpleComboBox.mySimpleModel);
 }

 @Test
 public void testSetModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 simpleComboBox.setModel(simpleComboBoxModel);
 assertSame(simpleComboBoxModel, simpleComboBox.mySimpleModel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetModelWithInvalidModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(new JComboBoxModel() {});
 }

 @Test
 public void testGetSelectedValue() {
 simpleComboBox.setSelectedValue("Test Value");
 assertEquals("Test Value", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueWhenNoSelection() {
 assertEquals(null, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValue() {
 simpleComboBox.setSelectedValue("Test Value");
 assertEquals("Test Value", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedInt() {
 simpleComboBox.setSelectedValue(1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWhenNoSelection() {
 assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWithInvalidSelectedValue() {
 simpleComboBox.setSelectedValue("Test Value");
 simpleComboBox.getSelectedInt();
 }

 @Test
 public void testGetSelectedLong() {
 simpleComboBox.setSelectedValue(1L);
 assertEquals(1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongWhenNoSelection() {
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithInvalidSelectedValue() {
 simpleComboBox.setSelectedValue("Test Value");
 simpleComboBox.getSelectedLong();
 }
}