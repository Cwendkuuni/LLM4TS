package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxWithIntegerValues() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleModel.addElement(1);
        simpleModel.addElement(2);
        simpleModel.addElement(3);
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);

        // Test setSelectedValue method with Integer value
        simpleComboBox.setSelectedValue(2);
        assertEquals(2, simpleComboBox.getSelectedInt());

        // Test getSelectedValue method with Integer value
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals(2, ((Integer) selectedValue).intValue());

        // Test setSelectedValue method with null value
        simpleComboBox.setSelectedValue(null);
        assertEquals(-1, simpleComboBox.getSelectedInt());

        // Test getSelectedValue method with null value
        selectedValue = simpleComboBox.getSelectedValue();
        assertNull(selectedValue);
    }

    @Test
    public void testSimpleComboBoxWithLongValues() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleModel.addElement(1L);
        simpleModel.addElement(2L);
        simpleModel.addElement(3L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);

        // Test setSelectedValue method with Long value
        simpleComboBox.setSelectedValue(2L);
        assertEquals(2, simpleComboBox.getSelectedInt()); // getSelectedInt should return the int value of the long value

        // Test getSelectedValue method with Long value
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals(2L, ((Long) selectedValue).longValue());

        // Test setSelectedValue method with null value
        simpleComboBox.setSelectedValue(null);
        assertEquals(-1, simpleComboBox.getSelectedInt());

        // Test getSelectedValue method with null value
        selectedValue = simpleComboBox.getSelectedValue();
        assertNull(selectedValue);
    }

    @Test
    public void testSimpleComboBoxWithShortValues() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleModel.addElement((short) 1);
        simpleModel.addElement((short) 2);
        simpleModel.addElement((short) 3);
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);

        // Test setSelectedValue method with Short value
        simpleComboBox.setSelectedValue(2);
        assertEquals(2, simpleComboBox.getSelectedInt());

        // Test getSelectedValue method with Short value
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals((short) 2, ((Short) selectedValue).shortValue());

        // Test setSelectedValue method with null value
        simpleComboBox.setSelectedValue(null);
        assertEquals(-1, simpleComboBox.getSelectedInt());

        // Test getSelectedValue method with null value
        selectedValue = simpleComboBox.getSelectedValue();
        assertNull(selectedValue);
    }

    @Test
    public void testSimpleComboBoxWithByteValues() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleModel.addElement((byte) 1);
        simpleModel.addElement((byte) 2);
        simpleModel.addElement((byte) 3);
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);

        // Test setSelectedValue method with Byte value
        simpleComboBox.setSelectedValue(2);
        assertEquals(2, simpleComboBox.getSelectedInt());

        // Test getSelectedValue method with Byte value
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals((byte) 2, ((Byte) selectedValue).byteValue());

        // Test setSelectedValue method with null value
        simpleComboBox.setSelectedValue(null);
        assertEquals(-1, simpleComboBox.getSelectedInt());

        // Test getSelectedValue method with null value
        selectedValue = simpleComboBox.getSelectedValue();
        assertNull(selectedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleComboBoxWithInvalidValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);

        // Test setSelectedValue method with String value
        simpleComboBox.setSelectedValue("test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleComboBoxWithInvalidValue2() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);

        // Test getSelectedValue method with String value
        simpleComboBox.setSelectedValue("test");
        Object selectedValue = simpleComboBox.getSelectedValue();
    }

    @Test
    public void testSetAndGetModel() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);

        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertEquals(newModel, simpleComboBox.getModel());
    }
}