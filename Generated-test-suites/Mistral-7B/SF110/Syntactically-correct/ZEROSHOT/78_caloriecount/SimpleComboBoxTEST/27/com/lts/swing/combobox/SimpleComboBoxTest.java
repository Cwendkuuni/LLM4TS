package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstruction() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(simpleComboBox);
    }

    @Test
    public void testSetModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        ComboBoxModel newModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertFalse(simpleComboBox.mySimpleModel instanceof SimpleComboBoxModel);
        simpleComboBox.setModel(new SimpleComboBoxModel());
        assertTrue(simpleComboBox.mySimpleModel instanceof SimpleComboBoxModel);
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertNull(selectedValue);

        simpleComboBox.setSelectedIndex(0);
        Object newValue = new Integer(1);
        simpleComboBox.setSelectedValue(newValue);

        Object getValue = simpleComboBox.getSelectedValue();
        assertEquals(newValue, getValue);

        simpleComboBox.setSelectedValue(new Byte((byte)2));
        getValue = simpleComboBox.getSelectedValue();
        assertEquals(new Byte((byte)2), getValue);

        simpleComboBox.setSelectedValue(new Short((short)3));
        getValue = simpleComboBox.getSelectedValue();
        assertEquals(new Short((short)3), getValue);

        simpleComboBox.setSelectedValue(new Long(4L));
        getValue = simpleComboBox.getSelectedValue();
        assertEquals(new Long(4L), getValue);

        simpleComboBox.setSelectedValue("Five");
        getValue = simpleComboBox.getSelectedValue();
        fail("Expected an exception, but got " + getValue);
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());

        simpleComboBox.setSelectedValue(new Integer(1));
        assertEquals(new Integer(1), simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue(new Byte((byte)2));
        assertEquals(new Byte((byte)2), simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue(new Short((short)3));
        assertEquals(new Short((short)3), simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue(new Long(4L));
        assertEquals(new Long(4L), simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue("Five");
        fail("Expected an exception, but got " + simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());

        simpleComboBox.setSelectedValue(new Integer(1));
        assertEquals(1, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(new Byte((byte)2));
        assertEquals(2, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(new Short((short)3));
        assertEquals(3, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(new Long(4L));
        assertEquals(-1, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue("Five");
        fail("Expected an exception, but got " + simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());

        simpleComboBox.setSelectedValue(new Integer(1));
        assertEquals(1, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue(new Byte((byte)2));
        assertEquals(2, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue(new Short((short)3));
        assertEquals(3, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue(new Long(4L));
        assertEquals(4, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue("Five");
        fail("Expected an exception, but got " + simpleComboBox.getSelectedLong());
    }
}