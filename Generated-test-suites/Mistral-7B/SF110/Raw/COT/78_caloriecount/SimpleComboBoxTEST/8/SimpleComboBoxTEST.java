package com.lts.swing.combobox.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

	@Test
	public void testSimpleComboBoxConstructor() {
		SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
		assertNotNull(simpleComboBox);
	}

	@Test
	public void testSetModel() {
		SimpleComboBox simpleComboBox = new SimpleComboBox(new JComboBoxModel());
		assertNull(simpleComboBox.mySimpleModel);

		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBox.setModel(simpleComboBoxModel);
		assertEquals(simpleComboBoxModel, simpleComboBox.mySimpleModel);
	}

	@Test
	public void testGetSelectedValueWithNullModel() {
		SimpleComboBox simpleComboBox = new SimpleComboBox(null);
		assertNull(simpleComboBox.getSelectedValue());
	}

	@Test
	public void testGetSelectedValueWithIntegerValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement(1);
		simpleComboBoxModel.addElement(2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals(1, simpleComboBox.getSelectedValue());
	}

	@Test
	public void testGetSelectedValueWithLongValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement(1L);
		simpleComboBoxModel.addElement(2L);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals(1L, simpleComboBox.getSelectedValue());
	}

	@Test
	public void testGetSelectedValueWithShortValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement((short) 1);
		simpleComboBoxModel.addElement((short) 2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals((short) 1, simpleComboBox.getSelectedValue());
	}

	@Test
	public void testGetSelectedValueWithByteValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement((byte) 1);
		simpleComboBoxModel.addElement((byte) 2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals((byte) 1, simpleComboBox.getSelectedValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetSelectedValueWithInvalidType() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement("Invalid Type");
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);
	}

	@Test
	public void testSetSelectedValueWithNullValue() {
		SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
		simpleComboBox.setSelectedValue(null);
		assertNull(simpleComboBox.getSelectedValue());
	}

	@Test
	public void testSetSelectedValueWithIntegerValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement(1);
		simpleComboBoxModel.addElement(2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);

		simpleComboBox.setSelectedValue(1);
		assertEquals(1, simpleComboBox.getSelectedValue());
	}

	@Test
	public void testSetSelectedValueWithLongValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement(1L);
		simpleComboBoxModel.addElement(2L);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);

		simpleComboBox.setSelectedValue(1L);
		assertEquals(1L, simpleComboBox.getSelectedValue());
	}

	@Test
	public void testSetSelectedValueWithShortValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement((short) 1);
		simpleComboBoxModel.addElement((short) 2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);

		simpleComboBox.setSelectedValue((short) 1);
		assertEquals((short) 1, simpleComboBox.getSelectedValue());
	}

	@Test
	public void testSetSelectedValueWithByteValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement((byte) 1);
		simpleComboBoxModel.addElement((byte) 2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);

		simpleComboBox.setSelectedValue((byte) 1);
		assertEquals((byte) 1, simpleComboBox.getSelectedValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSetSelectedValueWithInvalidType() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedValue("Invalid Type");
	}

	@Test
	public void testGetSelectedIntWithNullSelectedValue() {
		SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
		assertEquals(-1, simpleComboBox.getSelectedInt());
	}

	@Test
	public void testGetSelectedIntWithIntegerValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement(1);
		simpleComboBoxModel.addElement(2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals(1, simpleComboBox.getSelectedInt());
	}

	@Test
	public void testGetSelectedIntWithLongValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement(1L);
		simpleComboBoxModel.addElement(2L);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals(1, simpleComboBox.getSelectedInt());
	}

	@Test
	public void testGetSelectedIntWithShortValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement((short) 1);
		simpleComboBoxModel.addElement((short) 2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals(1, simpleComboBox.getSelectedInt());
	}

	@Test
	public void testGetSelectedIntWithByteValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement((byte) 1);
		simpleComboBoxModel.addElement((byte) 2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals(1, simpleComboBox.getSelectedInt());
	}

	@Test
	public void testGetSelectedLongWithNullSelectedValue() {
		SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
		assertEquals(-1L, simpleComboBox.getSelectedLong());
	}

	@Test
	public void testGetSelectedLongWithIntegerValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement(1);
		simpleComboBoxModel.addElement(2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals(1L, simpleComboBox.getSelectedLong());
	}

	@Test
	public void testGetSelectedLongWithLongValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement(1L);
		simpleComboBoxModel.addElement(2L);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals(1L, simpleComboBox.getSelectedLong());
	}

	@Test
	public void testGetSelectedLongWithShortValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement((short) 1);
		simpleComboBoxModel.addElement((short) 2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals(1L, simpleComboBox.getSelectedLong());
	}

	@Test
	public void testGetSelectedLongWithByteValue() {
		SimpleComboBoxModel simpleComboBoxModel = new SimpleComboBoxModel();
		simpleComboBoxModel.addElement((byte) 1);
		simpleComboBoxModel.addElement((byte) 2);
		SimpleComboBox simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
		simpleComboBox.setSelectedIndex(0);

		assertEquals(1L, simpleComboBox.getSelectedLong());
	}
}