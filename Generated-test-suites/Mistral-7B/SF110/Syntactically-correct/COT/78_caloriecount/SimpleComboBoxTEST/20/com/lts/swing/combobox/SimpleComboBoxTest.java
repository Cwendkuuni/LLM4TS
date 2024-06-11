package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private Container frame;

 @Before
 public void setUp() {
 // Create a new frame for testing
 frame = new JFrame();
 frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

 // Create a new SimpleComboBox using a SimpleComboBoxModel
 SimpleComboBoxModel model = new SimpleComboBoxModel(new String[] {"One", "Two", "Three"});
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);

 // Add the SimpleComboBox to the frame
 frame.add(simpleComboBox, 10, 10);
 frame.pack();
 frame.setVisible(true);

 // Get a reference to the SimpleComboBox for testing
 this.simpleComboBox = simpleComboBox;
 }

 @After
 public void tearDown() {
 // Dispose the frame to cleanup resources
 frame.dispose();
 }

 @Test
 public void testConstructorWithModel() {
 SimpleComboBoxModel model = new SimpleComboBoxModel(new String[] {"One", "Two", "Three"});
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 assertEquals("Expected SimpleComboBox to have the correct model", model, simpleComboBox.getModel());
 }

 @Test
 public void testSetModel() {
 SimpleComboBoxModel oldModel = simpleComboBox.getModel();
 SimpleComboBoxModel newModel = new SimpleComboBoxModel(new String[] {"Four", "Five", "Six"});
 simpleComboBox.setModel(newModel);
 assertNotEquals("Expected SimpleComboBox to have a different model", oldModel, simpleComboBox.getModel());
 assertEquals("Expected SimpleComboBox to have the new model", newModel, simpleComboBox.getModel());
 }

 @Test
 public void testGetSelectedValueNullModel() {
 simpleComboBox.setModel(null);
 assertNull("Expected SimpleComboBox to return null when model is null", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueIntegerValue() {
 simpleComboBox.setSelectedIndex(0);
 assertEquals("Expected SimpleComboBox to return the selected integer value", 0, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedValueLongValue() {
 simpleComboBox.setSelectedIndex(1);
 assertEquals("Expected SimpleComboBox to return the selected long value", 1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedValueShortValue() {
 simpleComboBox.setSelectedIndex(2);
 assertEquals("Expected SimpleComboBox to return the selected short value", (short) 2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedValueByteValue() {
 simpleComboBox.setSelectedIndex(0);
 assertEquals("Expected SimpleComboBox to return the selected byte value", (byte) 0, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedValueInvalidValue() {
 simpleComboBox.setModel(new DefaultComboBoxModel(new String[] {"One", "Two"}));
 simpleComboBox.setSelectedValue("Three");
 }

 @Test
 public void testSetSelectedValueNullValue() {
 simpleComboBox.setModel(new DefaultComboBoxModel(new String[] {"One", "Two", "Three"}));
 simpleComboBox.setSelectedValue(null);
 assertEquals("Expected SimpleComboBox to have no selected value", -1, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueInvalidValue() {
 simpleComboBox.setModel(new DefaultComboBoxModel(new String[] {"One", "Two", "Three"}));
 simpleComboBox.setSelectedValue("Four");
 }

 @Test
 public void testGetSelectedValueWithCustomRenderer() {
 SimpleComboBoxModel model = new SimpleComboBoxModel(new String[] {"One", "Two", "Three"});
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setRenderer(new JLabel());
 simpleComboBox.setSelectedIndex(1);
 JLabel label = (JLabel) simpleComboBox.getSelectedValue();
 assertEquals("Expected SimpleComboBox to return the selected label", "Two", label.getText());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetSelectedValueWithEditableFalse() {
 SimpleComboBoxModel model = new DefaultComboBoxModel();
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setEditable(false);
 simpleComboBox.setSelectedValue("Test");
 }

 @Test
 public void testGetSelectedValueWithEditableTrue() {
 SimpleComboBoxModel model = new DefaultComboBoxModel();
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setEditable(true);
 simpleComboBox.setSelectedValue("Test");
 assertEquals("Expected SimpleComboBox to return the selected value", "Test", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueWithComboBoxEditor() {
 SimpleComboBoxModel model = new DefaultComboBoxModel();
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 JComboBoxEditor editor = new JComboBoxEditor();
 simpleComboBox.setEditor(editor);
 simpleComboBox.setSelectedValue("Test");
 assertEquals("Expected SimpleComboBox to return the selected value", "Test", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueWithCustomEditor() {
 SimpleComboBoxModel model = new DefaultComboBoxModel();
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 JSpinner.NumberEditor editor = new JSpinner.NumberEditor(simpleComboBox, "##");
 simpleComboBox.setEditor(editor);
 simpleComboBox.setSelectedValue(new Integer(42));
 assertEquals("Expected SimpleComboBox to return the selected value", new Integer(42), simpleComboBox.getSelectedValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithNullValue() {
 SimpleComboBoxModel model = new DefaultComboBoxModel(new String[] {"One", "Two", "Three"});
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedValue(null);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetSelectedIntOutOfBounds() {
 SimpleComboBoxModel model = new DefaultComboBoxModel(new String[] {"One", "Two", "Three"});
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetSelectedIntOutOfBoundsMax() {
 SimpleComboBoxModel model = new DefaultComboBoxModel(new String[] {"One", "Two", "Three"});
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(2 + 1);
 }

 @Test(expected = ClassCastException.class)
 public void testGetSelectedValueClassCastException() {
 SimpleComboBoxModel model = new DefaultComboBoxModel(new String[] {"One", "Two", "Three", "Four"});
 SimpleComboBox simpleComboBox = new SimpleComboBox(model);
 simpleComboBox.setSelectedIndex(3);
 }
}