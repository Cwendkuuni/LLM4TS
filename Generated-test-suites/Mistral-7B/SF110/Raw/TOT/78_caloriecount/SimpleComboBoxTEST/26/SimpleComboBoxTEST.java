package com.lts.swing.combobox;

import static org.junit.Assert.*;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

	@Test
	public void testSimpleComboBoxWithIntegerValues() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, 2, 3, 4));
		SimpleComboBox comboBox = new SimpleComboBox(model);

		comboBox.setSelectedIndex(0);
		assertEquals(1, comboBox.getSelectedValue());
		assertEquals(1, comboBox.getSelectedInt());

		model.setSelectedItem(2);
		comboBox.setSelectedItem(2);
		assertEquals(2, comboBox.getSelectedValue());
		assertEquals(2, comboBox.getSelectedInt());

		comboBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.DESELECTED) {
				fail("Selection was not valid");
			}
		});

		comboBox.setSelectedValue("invalid");
		assertEquals(-1, comboBox.getSelectedInt());
	}

	@Test
	public void testSimpleComboBoxWithNullModel() {
		SimpleComboBox comboBox = new SimpleComboBox(null);
		assertNull(comboBox.getModel());
		assertNull(comboBox.getSelectedValue());
		assertNull(comboBox.getSelectedInt());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSimpleComboBoxWithInvalidSelectedValue() {
		SimpleComboBoxModel model = new SimpleComboBoxModel(Collections.singletonList("validValue"));;
		SimpleComboBox comboBox = new SimpleComboBox(model);

		comboBox.setModel(new ComboBoxModel() {});
		comboBox.setSelectedValue("invalidValue");
	}
}