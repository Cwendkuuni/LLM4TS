package com.lts.swing.combobox.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

/**
 * Test class for SimpleComboBox.
 */
public class SimpleComboBoxTest {

    /**
     * Test constructor with valid SimpleComboBoxModel.
     */
    @Test
    public void testSimpleComboBoxWithValidModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertNull(comboBox.getSelectedValue());
    }

    /**
     * Test constructor with null SimpleComboBoxModel.
     */
    @Test(expected = NullPointerException.class)
    public void testSimpleComboBoxWithNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
    }

    /**
     * Test setModel method with valid SimpleComboBoxModel.
     */
    @Test
    public void testSetModelWithValidModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setModel(model);
        assertSame(model, comboBox.getModel());
    }

    /**
     * Test setModel method with null ComboBoxModel.
     */
    @Test(expected = NullPointerException.class)
    public void testSetModelWithNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        comboBox.setModel(null);
    }

    /**
     * Test setModel method with non-SimpleComboBoxModel.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithNonSimpleComboBoxModel() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        comboBox.setModel(new JComboBoxModel());
    }

    /**
     * Test getSelectedValue method with null model.
     */
    @Test
    public void testGetSelectedValueWithNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }

    /**
     * Test getSelectedValue method with valid integer selected value.
     */
    @Test
    public void testGetSelectedValueWithInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedValue());
    }

    /**
     * Test getSelectedValue method with valid long selected value.
     */
    @Test
    public void testGetSelectedValueWithLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(1);
        assertEquals(2L, comboBox.getSelectedValue());
    }

    /**
     * Test getSelectedValue method with valid short selected value.
     */
    @Test
    public void testGetSelectedValueWithShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals((short) 1, comboBox.getSelectedValue());
    }

    /**
     * Test getSelectedValue method with valid byte selected value.
     */
    @Test
    public void testGetSelectedValueWithByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals((byte) 1, comboBox.getSelectedValue());
    }

    /**
     * Test getSelectedValue method with null selected value.
     */
    @Test
    public void testGetSelectedValueWithNullValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        comboBox.setSelectedIndex(-1);
        assertNull(comboBox.getSelectedValue());
    }

    /**
     * Test setSelectedValue method with null value.
     */
    @Test(expected = NullPointerException.class)
    public void testSetSelectedValueWithNullValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        comboBox.setSelectedValue(null);
    }

    /**
     * Test setSelectedValue method with valid integer value.
     */
    @Test
    public void testSetSelectedValueWithInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(1);
        assertEquals(1, comboBox.getSelectedIndex());
    }

    /**
     * Test setSelectedValue method with valid long value.
     */
    @Test
    public void testSetSelectedValueWithLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(1L);
        assertEquals(0, comboBox.getSelectedIndex());
        assertEquals(1L, comboBox.getSelectedValue());
    }

    /**
     * Test setSelectedValue method with valid short value.
     */
    @Test
    public void testSetSelectedValueWithShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue((short) 1);
        assertEquals(0, comboBox.getSelectedIndex());
        assertEquals((short) 1, comboBox.getSelectedValue());
    }

    /**
     * Test setSelectedValue method with valid byte value.
     */
    @Test
    public void testSetSelectedValueWithByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue((byte) 1);
        assertEquals(0, comboBox.getSelectedIndex());
        assertEquals((byte) 1, comboBox.getSelectedValue());
    }

    /**
     * Test getSelectedInt method with null selected value.
     */
    @Test
    public void testGetSelectedIntWithNullValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEqual(-1, comboBox.getSelectedInt());
    }

    /**
     * Test getSelectedInt method with valid integer selected value.
     */
    @Test
    public void testGetSelectedIntWithInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    /**
     * Test getSelectedInt method with valid long selected value.
     */
    @Test
    public void testGetSelectedIntWithLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    /**
     * Test getSelectedInt method with valid short selected value.
     */
    @Test
    public void testGetSelectedIntWithShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    /**
     * Test getSelectedInt method with valid byte selected value.
     */
    @Test
    public void testGetSelectedIntWithByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());
    }

    /**
     * Test getSelectedLong method with null selected value.
     */
    @Test
    public void testGetSelectedLongWithNullValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEqual(-1L, comboBox.getSelectedLong());
    }

    /**
     * Test getSelectedLong method with valid integer selected value.
     */
    @Test
    public void testGetSelectedLongWithInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    /**
     * Test getSelectedLong method with valid long selected value.
     */
    @Test
    public void testGetSelectedLongWithLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    /**
     * Test getSelectedLong method with valid short selected value.
     */
    @Test
    public void testGetSelectedLongWithShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    /**
     * Test getSelectedLong method with valid byte selected value.
     */
    @Test
    public void testGetSelectedLongWithByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    /**
     * Test getSelectedLong method with invalid selected value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        comboBox.setSelectedIndex(1);
        comboBox.getSelectedLong();
    }
}