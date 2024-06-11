package com.lts.swing.combobox;

import static org.junit.Assert.*;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

 private SimpleComboBox<Integer> combobox;
 private SimpleComboBoxModel<Integer> model;

 @Before
 public void setUp() {
 model = new SimpleComboBoxModel<>();
 model.addElement(1);
 model.addElement(2);
 model.addElement(3);
 combobox = new SimpleComboBox<>(model);
 }

 @Test
 public void testSetAndGetSelectedValue() {
 combobox.setSelectedValue(1);
 assertEquals(1, combobox.getSelectedValue());
 }

 @Test
 public void testSetAndGetSelectedValueWithNullModel() {
 combobox.setModel(null);
 assertNull(combobox.getSelectedValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSelectedValueWithNonSupportedType() {
 combobox.setSelectedValue("not a number");
 }

 @Test
 public void testGetSelectedInt() {
 combobox.setSelectedValue(1);
 assertEquals(1, combobox.getSelectedInt());
 }

 @Test
 public void testGetSelectedIntWithNullValue() {
 assertEqual(-1, combobox.getSelectedInt());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedIntWithNonSupportedType() {
 combobox.setSelectedValue("not a number");
 combobox.setSelectedInt(0);
 }

 @Test
 public void testGetSelectedLong() {
 combobox.setSelectedValue(3L);
 assertEquals(3L, combobox.getSelectedLong());
 }

 @Test
 public void testGetSelectedLongWithNullValue() {
 assertEqual(-1L, combobox.getSelectedLong());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetSelectedLongWithNonSupportedType() {
 combobox.setSelectedValue("not a number");
 combobox.setSelectedLong(0);
 }

 @Test
 public void testEventNotifications() {
 final int initialSelection = combobox.getSelectedIndex();
 final int newSelection = 1;
 combobox.setSelectedIndex(newSelection);

 final List<Integer> expectedItems = Arrays.asList(1, 2, 3);
 final List<Integer> actualItems = Arrays.asList(1, 2, 3);
 final Object changeEventSource = combobox;

 model.addChangeListener((ItemEvent e) -> {
 assertEquals(changeEventSource, e.getSource());
 assertEquals(ItemEvent.ITEM_STATE_CHANGED, e.getID());
 assertEquals(newSelection, e.getItemSelectable());
 assertEquals(expectedItems.get(newSelection), e.getItem());
 });

 assertEquals(initialSelection, combobox.getSelectedIndex());
 combobox.setSelectedIndex(newSelection);
}
}