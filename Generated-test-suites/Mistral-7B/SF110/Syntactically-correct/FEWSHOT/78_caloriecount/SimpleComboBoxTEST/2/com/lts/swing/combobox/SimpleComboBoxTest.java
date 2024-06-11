package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class SimpleComboBoxTest {
    @Test
    public void testSimpleComboBox() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertSame(model, comboBox.getModel());
    }

    @Test
    public void testSetModel() {
        final SimpleComboBoxModel model1 = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(new DefaultComboBoxModel());
        comboBox.setModel(model1);
        assertSame(model1, comboBox.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidModelType() {
        final JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setModel(new Object()[0]);
    }

    @Test
    public void testGetSelectedValue() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        model.addElement("Two");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        assertSame("Two", comboBox.getSelectedValue());
        assertEquals("Two", model.getSelectedItem());
    }

    @Test
    public void testGetSelectedValue_NullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        model.addElement("Two");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("Two");
        assertSame("Two", comboBox.getSelectedValue());
        assertEquals("Two", model.getSelectedItem());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValue_InvalidType() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(new Object());
    }

    @Test
    public void testGetSelectedInt() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        assertEquals(2, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_NullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_InvalidType() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        assertEquals(2L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_NullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_InvalidType() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(-1L, comboBox.getSelectedLong());
    }
}