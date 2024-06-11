package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
        assertFalse(comboBox.mySimpleModel == model1);
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        model.setSelectedItem("One");

        assertEquals("One", comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        model.addElement("Two");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedValue("One");
        assertEquals("One", comboBox.getSelectedValue());

        comboBox.setSelectedValue("Two");
        assertEquals("Two", comboBox.getSelectedValue());

        comboBox.setSelectedValue(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWrongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedValue(new Object());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        model.addElement("Two");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        model.setSelectedItem("One");

        assertEquals(0, comboBox.getSelectedInt());

        model.setSelectedItem("Two");

        assertEquals(1, comboBox.getSelectedInt());

        model.setSelectedItem(null);

        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        model.setSelectedItem(1);

        assertEquals(1, comboBox.getSelectedLong());

        model.setSelectedItem(2);

        assertEquals(2, comboBox.getSelectedLong());

        model.setSelectedItem(null);

        assertEquals(-1, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWrongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedInt(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWrongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedLong(2.0);
    }
}