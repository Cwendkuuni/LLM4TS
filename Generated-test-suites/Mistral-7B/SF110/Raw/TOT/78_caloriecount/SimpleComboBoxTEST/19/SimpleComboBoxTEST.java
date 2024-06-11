package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.event.ItemEvent;
import java.util.Arrays;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private SimpleComboBox<Integer> simpleComboBox;
 private JComboBox<Integer> jComboBox;
 private SimpleComboBoxModel<Integer> simpleModel;

 @Before
 public void setUp() {
 simpleModel = new SimpleComboBoxModel<>();
 simpleModel.addElement(1);
 simpleModel.addElement(2);
 simpleModel.addElement(3);
 simpleComboBox = new SimpleComboBox<>(simpleModel);
 jComboBox = new JComboBox<>();
 jComboBox.setModel(simpleModel);
 }

 @Test
 public void testConstructor() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(simpleModel);
 assertNotNull(simpleComboBox);
 }

 @Test
 public void testSetModel() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(simpleModel);
 ComboBoxModel<Integer> wrongModel = new DefaultComboBoxModel<>();
 simpleComboBox.setModel(wrongModel);
 simpleComboBox.setModel(simpleModel);
 assertEquals(simpleModel, simpleComboBox.getModel());
 }

 @Test
 public void testGetSelectedValue() {
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedValue());
 jComboBox.setSelectedIndex(1);
 assertEquals(2, jComboBox.getSelectedValue());
 simpleComboBox.setSelectedValue(3);
 assertEquals(3, simpleComboBox.getSelectedValue());
 jComboBox.setSelectedValue(3);
 assertEquals(3, jComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValue() {
 simpleComboBox.setSelectedValue(1);
 assertEquals(1, simpleComboBox.getSelectedValue());
 simpleComboBox.setSelectedValue(3);
 assertEquals(3, simpleComboBox.getSelectedValue());
 simpleComboBox.setSelectedValue(null);
 assertNull(simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedInt() {
 simpleComboBox.setSelectedValue(1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 simpleComboBox.setSelectedValue(3);
 assertEquals(3, simpleComboBox.getSelectedInt());
 simpleComboBox.setSelectedValue(null);
 assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedLong() {
 simpleComboBox.setSelectedValue(1);
 assertEquals(1, simpleComboBox.getSelectedLong());
 simpleComboBox.setSelectedValue(3);
 assertEquals(3, simpleComboBox.getSelectedLong());
 simpleComboBox.setSelectedValue(null);
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testSetSelectedValueWithNull() {
 simpleComboBox.setSelectedValue(null);
 simpleComboBox.setSelectedValue(1);
 assertEquals(1, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithWrongType() {
 simpleComboBox.setSelectedValue("test");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedValueWithNullModel() {
 SimpleComboBox<Integer> simpleComboBox = new SimpleComboBox<>(null);
 assertNull(simpleComboBox.getSelectedValue());
 simpleComboBox.setSelectedValue(1);
 }

 @Test
 public void testEventNotifications() {
 final Object[] expected = {2, 3};
 int selectedInt = -1;
 simpleComboBox.addActionListener(e -> selectedInt = simpleComboBox.getSelectedInt());
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.dispatchEvent(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, 1, ItemEvent.DESELECTED, false));
 simpleComboBox.setSelectedIndex(2);
 simpleComboBox.dispatchEvent(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, 2, ItemEvent.SELECTED, false));
 assertArrayEquals(expected, new Object[] {2, 3});
 }

 @Test
 public void testSwingThread() {
 SwingUtilities.invokeLater(() -> {
 simpleComboBox.setSelectedValue(3);
 });
 SwingUtilities.invokeLater(() -> {
 assertEquals(3, simpleComboBox.getSelectedInt());
 });
 }

 @Test
 public void testCustomModel() {
 SimpleComboBoxModel<String> customModel = new SimpleComboBoxModel<>();
 customModel.addElement("test1");
 customModel.addElement("test2");
 SimpleComboBox<String> simpleComboBox = new SimpleComboBox<>(customModel);
 simpleComboBox.setSelectedValue("test2");
 assertEquals("test2", simpleComboBox.getSelectedValue());
 }

}