package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxWithSimpleComboBoxModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        Object value1 = new Integer(1);
        Object value2 = new Long(2);
        Object value3 = new Short("3");
        Object value4 = new Byte((byte)4);

        model.addElement(value1);
        model.addElement(value2);
        model.addElement(value3);
        model.addElement(value4);

        assertEquals(value1, combobox.getSelectedValue());
        combobox.setSelectedValue(value2);
        assertEquals(value2, combobox.getSelectedValue());

        combobox.setSelectedValue(value3);
        assertEquals(value3, combobox.getSelectedValue());

        combobox.setSelectedValue(value4);
        assertEquals(value4, combobox.getSelectedValue());

        combobox.setSelectedValue(null);
        assertNull(combobox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        Object value1 = new Integer(1);
        Object value2 = new Integer(2);
        Object value3 = new Short("3");
        Object value4 = new Byte((byte)4);

        model.addElement(value1);
        model.addElement(value2);
        model.addElement(value3);
        model.addElement(value4);

        combobox.setSelectedIndex(0);
        assertEquals(1, combobox.getSelectedInt());

        combobox.setSelectedIndex(1);
        assertEquals(2, combobox.getSelectedInt());

        combobox.setSelectedIndex(2);
        assertEquals((short)3, combobox.getSelectedInt());

        combobox.setSelectedIndex(3);
        assertEquals(4, combobox.getSelectedInt());

        combobox.setSelectedIndex(-1);
        assertEquals(-1, combobox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithNullSelectedValue() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        combobox.setSelectedValue(null);
        combobox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithNonNumericValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        String value1 = "One";
        model.addElement(value1);

        combobox.setSelectedValue(value1);
        combobox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        Object value1 = new Integer(1);
        Object value2 = new Long(2);
        Object value3 = new Short("3");
        Object value4 = new Byte((byte)4);
        Object value5 = new Long(5);

        model.addElement(value1);
        model.addElement(value2);
        model.addElement(value3);
        model.addElement(value4);
        model.addElement(value5);

        combobox.setSelectedIndex(0);
        assertEquals(1, combobox.getSelectedInt());
        assertEquals(1, combobox.getSelectedLong());

        combobox.setSelectedIndex(1);
        assertEquals(2, combobox.getSelectedInt());
        assertEquals(2, combobox.getSelectedLong());

        combobox.setSelectedIndex(2);
        assertEquals((short)3, combobox.getSelectedInt());
        assertEquals((short)3, combobox.getSelectedLong());

        combobox.setSelectedIndex(3);
        assertEquals(4, combobox.getSelectedInt());
        assertEquals(4L, combobox.getSelectedLong());

        combobox.setSelectedIndex(4);
        assertEquals(5, combobox.getSelectedInt());
        assertEquals(5L, combobox.getSelectedLong());

        combobox.setSelectedIndex(-1);
        assertEquals(-1, combobox.getSelectedInt());
        assertNull(combobox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithNullSelectedValue() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        combobox.setSelectedValue(null);
        combobox.getSelectedLong();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithNonNumericValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        String value1 = "One";
        model.addElement(value1);

        combobox.setSelectedValue(value1);
        combobox.getSelectedLong();
    }
}