package com.lts.swing.combobox.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleComboBoxModel;

    @Before
    public void setUp() {
        simpleComboBoxModel = mock(SimpleComboBoxModel.class);
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }

    @Test
    public void testSimpleComboBoxWithValidModel() {
        simpleComboBox.setModel(simpleComboBoxModel);
        when(simpleComboBoxModel.getSelectedValue()).thenReturn("Test Value");

        assertEquals("Test Value", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetAndGetSelectedValueWithValidValue() {
        when(simpleComboBoxModel.getSelectedValue()).thenReturn(1);

        simpleComboBox.setSelectedValue(1);
        assertEquals(1, simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue("Test String");
        assertEquals(1, simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue((short) 1);
        assertEquals(1, simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue((byte) 1);
        assertEquals(1, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueAndSetSelectedValueWithNullModel() {
        simpleComboBox.setModel(null);

        assertNull(simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue(1);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntAndGetSelectedLongWithInvalidSelectedValue() {
        when(simpleComboBoxModel.getSelectedValue()).thenReturn("Test String");

        simpleComboBox.getSelectedInt();
        simpleComboBox.getSelectedLong();
    }

    @Test
    public void testGetSelectedIntWithValidIntegerValue() {
        when(simpleComboBoxModel.getSelectedValue()).thenReturn(1);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithValidShortValue() {
        when(simpleComboBoxModel.getSelectedValue()).thenReturn((short) 1);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithValidByteValue() {
        when(simpleComboBoxModel.getSelectedValue()).thenReturn((byte) 1);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithValidByteValue() {
        when(simpleComboBoxModel.getSelectedValue()).thenReturn((byte) 1);

        simpleComboBox.getSelectedLong();
    }

    @Test
    public void testGetSelectedLongWithValidShortValue() {
        when(simpleComboBoxModel.getSelectedValue()).thenReturn((short) 1);

        assertEquals(1, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithValidIntegerValue() {
        when(simpleComboBoxModel.getSelectedValue()).thenReturn(1);

        assertEquals(1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithValidLongValue() {
        when(simpleComboBoxModel.getSelectedValue()).thenReturn(1L);

        assertEquals(1L, simpleComboBox.getSelectedLong());
    }
}