package com.lts.swing.combobox.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

 private JFrame frame;
 private SimpleComboBoxSimpleComboBoxModel model;
 private SimpleComboBox comboBox;

 @Before
 public void setUp() {
 frame = new JFrame();
 model = new SimpleComboBoxModel();
 comboBox = new SimpleComboBox(model);
 frame.add(comboBox);
 frame.pack();
 frame.setVisible(true);
 SwingUtilities.invokeLater(() -> comboBox.setSelectedIndex(0));
 }

 @After
 public void tearDown() {
 frame.dispose();
 }

 @Test
 public void testSimpleComboBoxConstructor() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 assertNull(simpleComboBox.getSelectedValue());
 assertEquals(model, simpleComboBox.getModel());
 }

 @Test
 public void testSetModel() {
 ComboBoxModel anotherModel = new DefaultComboBoxModel();
 SimpleComboBox anotherSimpleComboBox = new SimpleComboBox(anotherModel);
 assertSame(anotherModel, anotherSimpleComboBox.getModel());

 SimpleComboBox newSimpleComboBox = new SimpleComboBox(model);
 assertSame(model, newSimpleComboBox.getModel());

 ComboBoxModel newSimpleModel = new SimpleComboBoxModel();
 newSimpleComboBox.setModel(newSimpleModel);
 assertSame(newSimpleModel, newSimpleComboBox.getModel());
 }

 @Test
 public void testGetSelectedValue() {
 assertEquals("First Item", comboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedInt() {
 assertEquals(0, comboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedLong() {
 assertEquals(0, comboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWithNonNumber() {
 comboBox.setSelectedValue("Non-number value");
 comboBox.getSelectedInt();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithNonNumber() {
 comboBox.setSelectedValue("Non-number value");
 comboBox.getSelectedLong();
 }

}