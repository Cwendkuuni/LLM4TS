package com.lts.swing.combobox;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class SimpleComboBoxTest {
    @Test
    public void testSimpleComboBoxConstructor() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(simpleComboBox);
    }

    @Test
    public void testSetModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        ComboBoxModel mockModel = mock(SimpleComboBoxModel.class);
        simpleComboBox.setModel(mockModel);
        verify(mockModel).setSimpleComboBox(simpleComboBox);
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Test Value");
        assertEquals("Test Value", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Test Value");
        assertEquals("Test Value", simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue(123);
        assertEquals(123, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(123L);
        assertEquals(123, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue((short)123);
        assertEquals(123, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue((byte)123);
        assertEquals(123, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(123);
        assertEquals(123, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue("Test Value");
        assertEquals(-1, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(123L);
        assertEquals(123, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue((short)123);
        assertEquals(123, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue((byte)123);
        assertEquals(123, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(123);
        assertEquals(123L, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue("Test Value");
        assertEquals(-1L, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue(123L);
        assertEquals(123L, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue((short)123);
        assertEquals((long)123, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue((byte)123);
        assertEquals((long)123, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidDataType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Test Value");
        simpleComboBox.setSelectedValue(new Object());
    }

    @Test
    public void testNullSimpleComboBoxModel() {
        assertThrows(NullPointerException.class, () -> new SimpleComboBox(null));
    }

    @Test
    public void testNullComboBoxModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertThrows(NullPointerException.class, () -> simpleComboBox.setModel(null));
    }
}