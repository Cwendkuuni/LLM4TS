package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstruction() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertNotNull(comboBox);
        assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        ComboBoxModel differentModel = new DefaultComboBoxModel();
        comboBox.setModel(differentModel);
        assertNotSame(model, comboBox.getModel());
        assertEquals(differentModel, comboBox.getModel());

        comboBox.setModel(model);
        assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValueWithNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithIntegerSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithLongSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithShortSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals((short) 1, comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithByteSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals((byte) 1, comboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedValueWithWrongTypeSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("one");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        comboBox.getSelectedValue();
    }

    @Test
    public void testSetSelectedValueWithInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(1);
        assertEquals(1, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(1L);
        assertEquals(1L, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue((short) 1);
        assertEquals((short) 1, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue((byte) 1);
        assertEquals((byte) 1, comboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithWrongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("one");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("two");
    }

    @Test
    public void testGetSelectedIntWithNullSelectedValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithIntegerSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithWrongTypeSelected() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        comboBox.setSelectedValue("one");
        comboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLongWithNullSelectedValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithIntegerSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithLongSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShortSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByteSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithWrongTypeSelected() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        comboBox.setSelectedValue("one");
        comboBox.getSelectedLong();
    }
}