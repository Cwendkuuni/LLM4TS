package com.lts.swing.combobox;

import static org.junit.Assert.*;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

	@Test
	public void testSimpleComboBoxConstruction() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, "One", 3, "Three", 2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		assertEquals(model, comboBox.getModel());
	}

	@Test
	public void testSetModel() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, "One", 3, "Three", 2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(null);
		comboBox.setModel(model);
		assertEquals(model, comboBox.getModel());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetModelWithInvalidModelType() {
		SimpleComboBoxModel model = new DefaultComboBoxModel();
		SimpleComboBox comboBox = new SimpleComboBox(null);
		comboBox.setModel(model);
	}

	@Test
	public void testGetSelectedValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, "One", 3, "Three", 2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedIndex(1);
		assertEquals("One", comboBox.getSelectedValue());
	}

	@Test
	public void testGetSelectedValueWithNullModel() {
		SimpleComboBox comboBox = new SimpleComboBox(null);
		assertEquals(null, comboBox.getSelectedValue());
	}

	@Test
	public void testGetSelectedInt() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, "One", 3, "Three", 2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedIndex(1);
		assertEquals(1, comboBox.getSelectedInt());
	}

	@Test
	public void testGetSelectedIntWithNullSelectedValue() {
		SimpleComboBox comboBox = new SimpleComboBox(null);
		assertEquals(-1, comboBox.getSelectedInt());
	}

	@Test
	public void testGetSelectedIntWithStringSelectedValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, "One", 3, "Three", 2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedItem("One");
		assertEquals(1, comboBox.getSelectedInt());
	}

	@Test
	public void testGetSelectedIntWithLongSelectedValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1L, "One", 3L, "Three", 2L, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedIndex(1);
		assertEquals(1, comboBox.getSelectedInt());
	}

	@Test
	public void testGetSelectedIntWithShortSelectedValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList((short)1, "One", 3, "Three", (short)2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedIndex(1);
		assertEquals(1, comboBox.getSelectedInt());
	}

	@Test
	public void testGetSelectedIntWithByteSelectedValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList((byte)1, "One", 3, "Three", (byte)2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedIndex(1);
		assertEquals(1, comboBox.getSelectedInt());
	}

	@Test
	public void testSetSelectedValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, "One", 3, "Three", 2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedValue(2);
		assertEquals(2, comboBox.getSelectedIndex());
		assertEquals("Two", comboBox.getSelectedItem());
		assertEquals(2, comboBox.getSelectedInt());
		assertEquals(2, comboBox.getSelectedLong());
	}

	@Test
	public void testSetSelectedValueWithNullValue() {
		SimpleComboBox comboBox = new SimpleComboBox(null);
		comboBox.setSelectedValue(null);
		assertEquals(-1, comboBox.getSelectedInt());
	}

	@Test
	public void testSetSelectedValueWithInvalidValueType() {		
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, "One", 3, "Three", 2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedValue("Four");
		assertEquals(-1, comboBox.getSelectedInt());
	}

	@Test
	public void testSetSelectedValueWithIntegerValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, "One", 3, "Three", 2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedValue(2);
		assertEquals("Two", comboBox.getSelectedItem());
		assertEquals(2, comboBox.getSelectedInt());
		assertEquals(2, comboBox.getSelectedLong());
	}

	@Test
	public void testSetSelectedValueWithLongValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1L, "One", 3L, "Three", 2L, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedValue(2L);
		assertEquals("Two", comboBox.getSelectedItem());
		assertEquals(2, comboBox.getSelectedInt());
		assertEquals(2, comboBox.getSelectedLong());
	}

	@Test
	public void testSetSelectedValueWithShortValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList((short)1, "One", 3, "Three", (short)2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedValue((short)2);
		assertEquals("Two", comboBox.getSelectedItem());
		assertEquals(2, comboBox.getSelectedInt());
		assertEquals(2, comboBox.getSelectedLong());
	}

	@Test
	public void testSetSelectedValueWithByteValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList((byte)1, "One", 3, "Three", (byte)2, "Two"));
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedValue((byte)2);
		assertEquals("Two", comboBox.getSelectedItem());
		assertEquals(2, comboBox.getSelectedInt());
		assertEquals(2, comboBox.getSelectedLong());
	}

	@Test
	public void testFireStateChangedEvent() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, "One", 3, "Three", 2, "Two")) {
			@Override
			public void setSelectedItem(Object anItem) {
				super.setSelectedItem(anItem);
				fireStateChanged();
			}
		};
		SimpleComboBox comboBox = new SimpleComboBox(model);
		assertEquals(0, comboBox.getSelectedIndex());
		assertEquals(null, comboBox.getSelectedItem());
		assertEquals(0, comboBox.getSelectedInt());
		assertEquals(0L, comboBox.getSelectedLong());
		comboBox.setSelectedValue(2);
		verify(comboBox).fireStateChanged();
	}

	@Test
	public void testFireStateChangedEventWithNullValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, "One", 3, "Three", 2, "Two")) {
			@Override
			public void setSelectedItem(Object anItem) {
				super.setSelectedItem(anItem);
				fireStateChanged();
			}
		};
		SimpleComboBox comboBox = new SimpleComboBox(model);
		comboBox.setSelectedValue(null);
		verifyNoMoreInteractions(comboBox);
	}

	private void verify(SimpleComboBox comboBox) {
		verify(comboBox, times(1)).fireStateChanged();
	}

	private void verifyNoMoreInteractions(SimpleComboBox comboBox) {
		verify(comboBox, never()).fireStateChanged();
	}
}