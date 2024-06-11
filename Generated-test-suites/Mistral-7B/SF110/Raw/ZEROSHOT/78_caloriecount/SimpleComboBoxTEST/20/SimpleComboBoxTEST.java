package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxWithIntegerValues() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        model.addElement(3);

        SimpleComboBox cb = new SimpleComboBox(model);

        assertEquals(1, cb.getSelectedInt());
        cb.setSelectedIndex(1);
        assertEquals(2, cb.getSelectedInt());
        cb.setSelectedValue(3);
        assertEquals(3, cb.getSelectedInt());
    }

    @Test
    public void testSimpleComboBoxWithLongValues() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        model.addElement(3L);

        SimpleComboBox cb = new SimpleComboBox(model);

        assertEquals(1, cb.getSelectedLong());
        cb.setSelectedIndex(1);
        assertEquals(2, cb.getSelectedLong());
        cb.setSelectedValue(3);
        assertEquals(3, cb.getSelectedLong());
    }

    @Test
    public void testSimpleComboBoxWithNullModel() {
        SimpleComboBox cb = new SimpleComboBox(null);
        assertNull(cb.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithNonNumericType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox cb = new SimpleComboBox(model);
        cb.setSelectedValue("string");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedValueOrIntOrLongWhenModelIsEmpty() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox cb = new SimpleComboBox(model);
        cb.getSelectedValue();
        cb.getSelectedInt();
        cb.getSelectedLong();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntOrLongWhenModelContainsNonNumericElement() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("string");
        SimpleComboBox cb = new SimpleComboBox(model);
        cb.getSelectedInt();
        cb.getSelectedLong();
    }

    @Test
    public void testSetModelWithNullModel() {
        SimpleComboBox cb = new SimpleComboBox(null);
        assertNull(cb.getSelectedValue());
    }

    @Test
    public void testSetModelWithNonSimpleComboBoxModel() {
        JComboBoxModel model = new DefaultComboBoxModel();
        SimpleComboBox cb = new SimpleComboBox(model);
        assertNull(cb.getSelectedValue());
    }

    @Test
    public void testSetModelWithSimpleComboBoxModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox cb = new SimpleComboBox(model);
        assertEquals(model, cb.getModel());
    }
}