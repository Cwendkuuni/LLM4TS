package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private JFrame frame;
 private SimpleComboBox<String> simpleComboBox;
 private SimpleComboBoxModel<String> model;

 @Before
 public void setUp() {
 initGui();
 model = new SimpleComboBoxModel<>(Arrays.asList("Option 1", "Option 2", "Option 3"));
 simpleComboBox = new SimpleComboBox<>(model);
 }

 @After
 public void tearDown() {
 SwingUtilities.invokeLater(frame::dispose);
 }

 @Test
 public void testSimpleComboBoxConstruction() {
 SimpleComboBox<String> comboBox = new SimpleComboBox<>(new SimpleComboBoxModel<String>(Collections.emptyList()));
 assertNotNull(comboBox);
 }

 @Test
 public void testSetModel() {
 SimpleComboBox<String> comboBox = new SimpleComboBox<>(new JComboBoxModel() {});
 comboBox.setModel(model);
 assertSame(model, comboBox.getModel());
 }

 @Test
 public void testGetSelectedValue_WhenNoSelection_ReturnsNull() {
 assertEquals(null, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValue_WhenValueIsInteger() {
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.setModel(model);
 assertEquals("Option 2", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValue_WhenValueIsLong() {
 model.setSelectedItem(2);
 simpleComboBox.setModel(model);
 assertEquals("Option 3", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValue_WhenValueIsShort() {
 model.setSelectedItem(1);
 simpleComboBox.setModel(model);
 assertEquals("Option 1", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValue_WhenValueIsByte() {
 model.setSelectedItem(0);
 simpleComboBox.setModel(model);
 assertEquals("Option 1", simpleComboBox.getSelectedValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedValue_WhenValueIsNotNumber() {
 model.setSelectedItem("Invalid value");
 simpleComboBox.setModel(model);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testGetSelectedInt() {
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.setModel(model);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedLong() {
 model.setSelectedIndex(2);
 simpleComboBox.setModel(model);
 assertEquals(2, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testSetSelectedValue() {
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue("Option 1");
 assertEquals("Option 1", simpleComboBox.getSelectedValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValue_WhenValueIsNotNumber() {
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue("Invalid value");
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testSetSelectedInt() {
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.setSelectedValue(1);
 assertEquals("Option 1", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedLong() {
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(2);
 simpleComboBox.setSelectedValue(2);
 assertEquals("Option 3", simpleComboBox.getSelectedValue());
 }

 @Test
 public void testFireSelectedItemEvent() {
 simpleComboBox.addActionListener(e -> System.out.println("Selected item: " + simpleComboBox.getSelectedValue()));
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue("Option 1");
 simpleComboBox.setSelectedIndex(2);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue("Option 3");
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue("Option 1");
 simpleComboBox.fireSelectionValueChanged(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, 0, ItemEvent.DESELECTED, null, "Option 1"));
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue(1);
 simpleComboBox.fireSelectionValueChanged(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, 1, ItemEvent.SELECTED, null, "Option 2"));
 simpleComboBox.setSelectedIndex(2);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue(2);
 simpleComboBox.fireSelectionValueChanged(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, 2, ItemEvent.SELECTED, null, "Option 3"));
 }
}