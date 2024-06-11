package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

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
    public void testConstructor() {
        assertSame(simpleComboBoxModel, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel newModel = mock(SimpleComboBoxModel.class);
        simpleComboBox.setModel(newModel);
        assertSame(newModel, simpleComboBox.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidModel() {
        simpleComboBox.setModel(new Object() {});
    }

    @Test
    public void testGetSelectedValue() {
        Object selectedValue = new Integer(1);
        when(simpleComboBoxModel.getSelectedValue()).thenReturn(selectedValue);
        assertEquals(selectedValue, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenNoSelection() {
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        Integer selectedInt = new Integer(1);
        when(simpleComboBoxModel.getSelectedValue()).thenReturn(selectedInt);
        assertEquals(selectedInt.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidSelectedValue() {
        when(simpleComboBoxModel.getSelectedValue()).thenReturn(new Object() {});
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong() {
        Long selectedLong = new Long(1);
        when(simpleComboBoxModel.getSelectedValue()).thenReturn(selectedLong);
        assertEquals(selectedLong.longValue(), simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidSelectedValue() {
        when(simpleComboBoxModel.getSelectedValue()).thenReturn(new Object() {});
        simpleComboBox.getSelectedLong();
    }
}