package com.lts.swing.combobox.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

/**
 * Tests for SimpleComboBox class
 */
public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstructor() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(comboBox.mySimpleModel);
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setModel(model);
        assertEquals(model, comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(Integer.valueOf(1), comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(Long.valueOf(1L), comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(Short.valueOf((short) 1), comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(Byte.valueOf((byte) 1), comboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedValueInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("InvalidType");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.getSelectedValue();
    }

    @Test
    public void testSetSelectedValueNullValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertNull(comboBox.getSelectedValue());
        comboBox.setSelectedValue(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(Integer.valueOf(1));
        assertEquals(Integer.valueOf(1), comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(Long.valueOf(1L));
        assertEquals(Long.valueOf(1L), comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(Short.valueOf((short) 1));
        assertEquals(Short.valueOf((short) 1), comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(Byte.valueOf((byte) 1));
        assertEquals(Byte.valueOf((byte) 1), comboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("InvalidType");
    }

    @Test
    public void testGetSelectedIntNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testConstructorWithNullModel() {
        assertThrows(NullPointerException.class, () -> new SimpleComboBox(null));
    }
}