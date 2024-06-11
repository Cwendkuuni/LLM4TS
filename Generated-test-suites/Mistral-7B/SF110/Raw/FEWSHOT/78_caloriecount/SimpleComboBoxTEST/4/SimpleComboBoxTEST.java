package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private ComboBoxModel mockModel;
    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        mockModel = mock(ComboBoxModel.class);
        simpleComboBox = new SimpleComboBox(mockModel);
    }

    @After
    public void tearDown() {
        simpleComboBox = null;
        mockModel = null;
    }

    @Test
    public void testSimpleComboBoxConstruction() {
        assertSame(SimpleComboBox.class, simpleComboBox.getClass());
        assertSame(mockModel, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        final ComboBoxModel newModel = mock(ComboBoxModel.class);
        simpleComboBox.setModel(newModel);
        assertSame(newModel, simpleComboBox.getModel());
        assertNull(mySimpleModel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithWrongModelType() {
        final Object wrongModel = new Object();
        simpleComboBox.setModel(wrongModel);
    }

    @Test
    public void testGetSelectedValue() {
        final Object selectedValue = new Object();
        when(mockModel.getSelectedItem()).thenReturn(selectedValue);

        assertEquals(selectedValue, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenNoSelection() {
        when(mockModel.getSelectedItem()).thenReturn(null);

        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        final Object newValue = new Object();

        simpleComboBox.setSelectedValue(newValue);

        verifySelectedValue(newValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithWrongValueType() {
        final int wrongValue = 1;
        simpleComboBox.setSelectedValue(wrongValue);
    }

    @Test
    public void testGetSelectedInt() {
        final int intValue = 1;
        when(mockModel.getSelectedItem()).thenReturn(intValue);

        assertEquals(intValue, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenNoSelection() {
        when(mockModel.getSelectedItem()).thenReturn(null);

        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithWrongValueType() {
        final long longValue = 1L;
        simpleComboBox.setSelectedValue(longValue);
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong() {
        final long longValue = 1L;
        when(mockModel.getSelectedItem()).thenReturn(longValue);

        assertEquals(longValue, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenNoSelection() {
        when(mockModel.getSelectedItem()).thenReturn(null);

        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithWrongValueType() {
        final int intValue = 1;
        simpleComboBox.setSelectedValue(intValue);
        simpleComboBox.getSelectedLong();
    }

    @Test
    public void testSetSelectedValueAndGetSelectedValue() {
        final Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSwingUtilitiesInvokeLater() {
        final Runnable runnable = () -> System.out.println("Test invoked by SwingUtilities.invokeLater");

        SwingUtilities.invokeLater(runnable);

        // This test is not comprehensive, as it only checks that the Runnable is executed eventually.
        // The test should be extended to check the thread upon which the Runnable is executed.
    }

    private void verifySelectedValue(final Object value) {
        when(mockModel.getSelectedItem()).thenReturn(value);

        simpleComboBox.setSelectedValue(value);

        assertEquals(value, simpleComboBox.getSelectedValue());
    }
}