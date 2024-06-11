package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBox() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model1 = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model1);
        SimpleComboBoxModel model2 = new SimpleComboBoxModel();

        comboBox.setModel(model2);
        assertSame(model2, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue() {
        Object value1 = new Integer(1);
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(value1);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedIndex(0);
        assertEquals(value1, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Object value1 = new Integer(1);

        comboBox.setSelectedValue(value1);
        assertEquals(value1, comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntNullValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertThrows(IllegalArgumentException.class, () -> comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Long(1));
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Short("1"));
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Byte("1"));
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongNullValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertThrows(IllegalArgumentException.class, () -> comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        assertEquals(1, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Long(1));
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        assertEquals(1, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Short("1"));
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        assertEquals(1, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Byte("1"));
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        assertEquals(1, comboBox.getSelectedLong());
    }
}