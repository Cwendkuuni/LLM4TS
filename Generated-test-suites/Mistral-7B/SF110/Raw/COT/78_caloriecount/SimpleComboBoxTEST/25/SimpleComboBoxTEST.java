package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.util.EventObject;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private SimpleComboBox simpleComboBox;
 private JFrame testFrame;

 @Before
 public void setup() {
 testFrame = new JFrame();
 testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 testFrame.setSize(300, 200);
 testFrame.setVisible(true);

 simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 testFrame.add(simpleComboBox, 5, 5);
 }

 @Test
 public void testConstructorWithModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 assertNotNull(simpleComboBox);
 }

 @Test
 public void testSetModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 ComboBoxModel model = new DefaultComboBoxModel();
 simpleComboBox.setModel(model);
 assertNotSame(simpleComboBox.mySimpleModel, simpleComboBox.mySimpleModel);
 assertSame(simpleComboBox.mySimpleModel, model);
 }

 @Test
 public void testGetSelectedValueNullModel() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(null);
 assertEquals(null, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueIntegerValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueLongValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueShortValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement((short) 1);
 model.addElement((short) 2);
 model.addElement((short) 3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals((short) 2, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testGetSelectedValueByteValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement((byte) 1);
 model.addElement((byte) 2);
 model.addElement((byte) 3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals((byte) 2, simpleComboBox.getSelectedValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedValueInvalidType() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("test");
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(0);
 }

 @Test
 public void testGetSelectedIntNullValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 assertEquals(-1, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntIntegerValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntLongValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntShortValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement((short) 1);
 model.addElement((short) 2);
 model.addElement((short) 3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntByteValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement((byte) 1);
 model.addElement((byte) 2);
 model.addElement((byte) 3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntInvalidType() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("test");
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(0);
 }

 @Test
 public void testGetSelectedLongNullValue() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 assertEquals(-1L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongIntegerValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongLongValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongShortValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement((short) 1);
 model.addElement((short) 2);
 model.addElement((short) 3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongByteValue() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement((byte) 1);
 model.addElement((byte) 2);
 model.addElement((byte) 3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(1);
 assertEquals(2L, simpleComboBox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongInvalidType() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("test");
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedIndex(0);
 }

 @Test
 public void testSetSelectedValueNull() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
 simpleComboBox.setSelectedValue(null);
 assertEquals(null, simpleComboBox.getSelectedValue());
 }

 @Test
 public void testSetSelectedValueInteger() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue(2);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testSetSelectedValueLong() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1L);
 model.addElement(2L);
 model.addElement(3L);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue(2L);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testSetSelectedValueShort() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement((short) 1);
 model.addElement((short) 2);
 model.addElement((short) 3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue((short) 2);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test
 public void testSetSelectedValueByte() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement((byte) 1);
 model.addElement((byte) 2);
 model.addElement((byte) 3);
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue((byte) 2);
 assertEquals(2, simpleComboBox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueInvalidType() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement("test");
 simpleComboBox.setModel(model);
 simpleComboBox.setSelectedValue("test");
 }

 @Test
 public void testItemStateChanged() {
 SimpleComboBoxModel model = new SimpleComboBoxModel();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 simpleComboBox.setModel(model);
 final int initialSize = simpleComboBox.getSize();
 simpleComboBox.addActionListener(e -> {
 final Container source = (Container)e.getSource();
 final Component component = (Component)source.getComponentAt(0, 0);
 final JComboBox jComboBox = (JComboBox)component;
 final int selectedIndex = jComboBox.getSelectedIndex();
 assertEquals(selectedIndex, simpleComboBox.getSelectedInt());
 });
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.doClick();
 simpleComboBox.dispatchEvent(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, 1, ItemEvent.DESELECTED, -1, null) {
 @Override
 public Component getSource() {
 return simpleComboBox;
 }

 @Override
 public Object getItem() {
 return simpleComboBox.getItemAt(selectedIndex);
 }
 });
 simpleComboBox.setSelectedIndex(2);
 simpleComboBox.doClick();
 simpleComboBox.dispatchEvent(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, 2, ItemEvent.SELECTED, -1, null) {
 @Override
 public Component getSource() {
 return simpleComboBox;
 }

 @Override
 public Object getItem() {
 return simpleComboBox.getItemAt(selectedIndex);
 }
 });
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.doClick();
 simpleComboBox.dispatchEvent(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, 1, ItemEvent.SELECTED, -1, null) {
 @Override
 public Component getSource() {
 return simpleComboBox;
 }

 @Override
 public Object getItem() {
 return simpleComboBox.getItemAt(selectedIndex);
 }
 });
 simpleComboBox.setSelectedIndex(0);
 simpleComboBox.doClick();
 simpleComboBox.dispatchEvent(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, 0, ItemEvent.DESELECTED, -1, null) {
 @Override
 public Component getSource() {
 return simpleComboBox;
 }

 @Override
 public Object getItem() {
 return simpleComboBox.getItemAt(selectedIndex);
 }
 });
 simpleComboBox.setSelectedIndex(1);
 simpleComboBox.doClick();
 simpleComboBox.dispatchEvent(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, 1, ItemEvent.SELECTED, -1, null) {
 @Override
 public Component getSource() {
 return simpleComboBox;
 }

 @Override
 public Object getItem() {
 return simpleComboBox.getItemAt(selectedIndex);
 }
 });
 SwingUtilities.invokeLater(() -> {
 final int finalSize = simpleComboBox.getSize();
 assertEquals(initialSize, finalSize);
 });
 }

}