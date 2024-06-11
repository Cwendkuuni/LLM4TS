package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxCreation() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(simpleComboBox.getSelectedValue());
        assertTrue(simpleComboBox.getModel() instanceof SimpleComboBoxModel);
    }

    @Test
    public void testSetModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertSame(model, simpleComboBox.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        simpleComboBox.setModel(new Object());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(new Integer(1));
        assertEquals(new Integer(1), simpleComboBox.getSelectedValue());
        simpleComboBox.setSelectedValue(new Long(2));
        assertEquals(new Long(2), simpleComboBox.getSelectedValue());
        simpleComboBox.setSelectedValue(new Short((short)3));
        assertEquals(new Short((short)3), simpleComboBox.getSelectedValue());
        simpleComboBox.setSelectedValue(new Byte((byte)4));
        assertEquals(new Byte((byte)4), simpleComboBox.getSelectedValue());
        simpleComboBox.setSelectedValue(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(new Integer(1));
        assertEquals(new Integer(1), model.getSelectedValue());
        simpleComboBox.setSelectedValue(new Long(2));
        assertEquals(new Long(2), model.getSelectedValue());
        simpleComboBox.setSelectedValue(new Short((short)3));
        assertEquals(new Short((short)3), model.getSelectedValue());
        simpleComboBox.setSelectedValue(new Byte((byte)4));
        assertEquals(new Byte((byte)4), model.getSelectedValue());
        simpleComboBox.setSelectedValue(null);
        assertNull(model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(new Integer(1));
        assertEquals(1, simpleComboBox.getSelectedInt());
        simpleComboBox.setSelectedValue(new Long(2));
        assertEquals(-1, simpleComboBox.getSelectedInt());
        simpleComboBox.setSelectedValue(new Short((short)3));
        assertEquals(3, simpleComboBox.getSelectedInt());
        simpleComboBox.setSelectedValue(new Byte((byte)4));
        assertEquals(4, simpleComboBox.getSelectedInt());
        simpleComboBox.setSelectedValue(null);
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(new Integer(1));
        assertEquals(1, simpleComboBox.getSelectedLong());
        simpleComboBox.setSelectedValue(new Long(2));
        assertEquals(2, simpleComboBox.getSelectedLong());
        simpleComboBox.setSelectedValue(new Short((short)3));
        assertEquals(-1, simpleComboBox.getSelectedLong());
        simpleComboBox.setSelectedValue(new Byte((byte)4));
        assertEquals(-1, simpleComboBox.getSelectedLong());
        simpleComboBox.setSelectedValue(null);
        assertEquals(-1, simpleComboBox.getSelectedLong());
    }
}