package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.lang.IllegalArgumentException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.*;
import javax.swing.event.ListDataEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
 assertNotNull(simpleComboBox);
 }

 @Test
 public void testSetModel() throws HeadlessException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IllegalArgumentException {
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 SimpleComboBoxModel simpleComboBoxModelSpy = mock(SimpleComboBoxModel.class);
 simpleComboBox.setModel(simpleComboBoxModelSpy);
 verify(simpleComboBoxModelSpy).setComboBox(simpleComboBox);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetModelWithInvalidModelType() {
 SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
 simpleComboBox.setModel(new JListModel() {});
 }

 @Test
 public void testGetSelectedValue() {
 Object selectedValue = simpleComboBox.getSelectedValue();
 assertNull(selectedValue);

 simpleComboBoxModel.addElement("TestValue");
 simpleComboBox.setSelectedIndex(0);
 Object expectedValue = simpleComboBoxModel.getElementAt(0);
 Object actualValue = simpleComboBox.getSelectedValue();
 assertEquals(expectedValue, actualValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithNullValue() {
 simpleComboBox.setSelectedValue(null);
 }

 @Test
 public void testSetSelectedValueWithValidValue() {
 Object value = "TestValue";
 simpleComboBox.setSelectedValue(value);
 Object expectedValue = value;
 Object actualValue = simpleComboBox.getSelectedValue();
 assertEquals(expectedValue, actualValue);
 }

 @Test
 public void testGetSelectedInt() {
 int selectedInt = simpleComboBox.getSelectedInt();
 assertEquals(-1, selectedInt);

 simpleComboBoxModel.addElement(1);
 simpleComboBoxModel.addElement("TestValue");
 simpleComboBoxModel.setSelectedIndex(0);
 int expectedInt = 1;
 int actualInt = simpleComboBox.getSelectedInt();
 assertEquals(expectedInt, actualInt);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWithNonNumericValue() {
 simpleComboBoxModel.addElement("TestValue");
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.getSelectedInt();
 }

 @Test
 public void testGetSelectedLong() {
 long selectedLong = simpleComboBox.getSelectedLong();
 assertEquals(-1L, selectedLong);

 simpleComboBoxModel.addElement(1L);
 simpleComboBoxModel.addElement("TestValue");
 simpleComboBoxModel.setSelectedIndex(1);
 long expectedLong = 1L;
 long actualLong = simpleComboBox.getSelectedLong();
 assertEquals(expectedLong, actualLong);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithNonNumericValue() {
 simpleComboBoxModel.addElement("TestValue");
 simpleComboBoxModel.setSelectedIndex(0);
 simpleComboBox.getSelectedLong();
 }

 @Test
 public void testComboBoxModelChangeListener() {
 final int initialSize = simpleComboBoxModel.getSize();
 simpleComboBoxModel.addListDataListener(new ListDataListener() {
 @Override
 public void contentsChanged(ListDataEvent e) {
 assertEquals(initialSize + 1, simpleComboBoxModel.getSize());
 }

 @Override
 public void intervalAdded(ListDataEvent e, int index) {}

 @Override
 public void intervalRemoved(ListDataEvent e, int index) {}
 });
 simpleComboBoxModel.addElement("TestValue");
 }

 @After
 public void tearDown() {
 simpleComboBox = null;
 simpleComboBoxModel = null;
 }

}