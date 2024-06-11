package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetAndGetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        assertNull(combobox.getModel());
        combobox.setModel(model);
        assertEquals(model, combobox.getModel());

        model = new SimpleComboBoxModel(new Object[]{"Option 1", "Option 2", "Option 3"});
        combobox = new SimpleComboBox(model);

        assertSame(model, combobox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Object[]{"Option 1", "Option 2", "Option 3"});
        SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedIndex(1);

        assertEquals("Option 2", combobox.getSelectedValue());

        combobox.setSelectedValue("Option 4");
        assertEquals("Option 4", combobox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Object[]{"Option 1", "Option 2", "Option 3"});
        SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedValue(new Double(123.5));
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Object[]{1, 2, 3, 4, 5});
        SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedIndex(2);
        assertEquals(3, combobox.getSelectedInt());

        combobox.setSelectedValue(new Byte((byte)6));
        assertEquals(6, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Object[]{1L, 2L, 3L, 4L, 5L});
        SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedIndex(3);
        assertEquals(4L, combobox.getSelectedLong());

        combobox.setSelectedValue(new Short((short)7));
        assertEquals((long)7, combobox.getSelectedLong());
    }
}