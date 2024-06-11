package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstruction() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNotNull(simpleComboBox);
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        final SimpleComboBoxModel model1 = new SimpleComboBoxModel();
        final SimpleComboBox simpleComboBox1 = new SimpleComboBox(model1);
        final SimpleComboBoxModel model2 = new SimpleComboBoxModel();
        simpleComboBox1.setModel(model2);
        assertNotNull(simpleComboBox1);
        assertEquals(model2, simpleComboBox1.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidModelType() {
        final SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<>());
        simpleComboBox.setModel(new Object());
    }

    @Test
    public void testGetSelectedValue() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        model.setSelectedItem("Item 1");
        assertEquals("Item 1", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenNoSelection() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        model.addElement("Item 1");
        simpleComboBox.setSelectedIndex(0);
        simpleComboBox.setSelectedValue("Item 1");
        assertEquals("Item 1", simpleComboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidType() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(new Object());
    }

    @Test
    public void testGetSelectedInt() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        model.addElement("Item 1");
        model.addElement("Item 2");
        model.setSelectedItem("Item 1");
        assertEquals(0, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenNoSelection() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNegative(simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        model.addElement(1L);
        model.addElement(2L);
        model.setSelectedItem(1L);
        assertEquals(1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenNoSelection() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNegative(simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidType() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("Invalid Type");
    }
}