package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetAndGetSelectedValue() {
        final SimpleComboBoxModel mockModel = mock(SimpleComboBoxModel.class);
        final Object testValue = new Integer(1);

        when(mockModel.getSelectedValue()).thenReturn(testValue);

        final SimpleComboBox combobox = new SimpleComboBox(mockModel);
        assertEquals(testValue, combobox.getSelectedValue());

        combobox.setSelectedValue(new Long(2));
        assertNull(combobox.getSelectedValue());
        when(mockModel.getSelectedValue()).thenReturn(new Long(2));
        assertEquals(new Long(2), combobox.getSelectedValue());
    }

    @Test
    public void testSetAndGetSelectedInt() {
        final SimpleComboBoxModel mockModel = mock(SimpleComboBoxModel.class);
        final int testIntValue = 1;

        when(mockModel.getSelectedValue()).thenReturn(testIntValue);

        final SimpleComboBox combobox = new SimpleComboBox(mockModel);
        assertEquals(testIntValue, combobox.getSelectedInt());

        combobox.setSelectedValue(new Short((short) 2));
        assertEquals(testIntValue, combobox.getSelectedInt());

        when(mockModel.getSelectedValue()).thenReturn(new Short((short) 2));
        assertEquals(2, combobox.getSelectedInt());
    }

    @Test
    public void testSetAndGetSelectedLong() {
        final SimpleComboBoxModel mockModel = mock(SimpleComboBoxModel.class);
        final long testLongValue = 1;

        when(mockModel.getSelectedValue()).thenReturn(testLongValue);

        final SimpleComboBox combobox = new SimpleComboBox(mockModel);
        assertEquals(testLongValue, combobox.getSelectedLong());

        combobox.setSelectedValue(new Byte((byte) 2));
        assertEquals(testLongValue, combobox.getSelectedLong());

        when(mockModel.getSelectedValue()).thenReturn(new Byte((byte) 2));
        assertEquals(2, combobox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedValueWithNullModel() {
        final SimpleComboBox combobox = new SimpleComboBox(null);
        combobox.getSelectedValue();
    }

    @Test
    public void testSetModelWithNullModel() {
        final SimpleComboBox combobox = new SimpleComboBox(null);
        combobox.setModel(null);
    }

    @Test
    public void testSetModelWithDifferentModelType() {
        final DefaultComboBoxModel defaultModel = new DefaultComboBoxModel();
        final SimpleComboBox combobox = new SimpleComboBox(null);
        combobox.setModel(defaultModel);
    }

    @Test
    public void testSetModelWithSameModel() {
        final SimpleComboBoxModel mockModel = mock(SimpleComboBoxModel.class);
        final SimpleComboBox combobox = new SimpleComboBox(mockModel);
        combobox.setModel(mockModel);
        assertSame(mockModel, combobox.getModel());
    }
}