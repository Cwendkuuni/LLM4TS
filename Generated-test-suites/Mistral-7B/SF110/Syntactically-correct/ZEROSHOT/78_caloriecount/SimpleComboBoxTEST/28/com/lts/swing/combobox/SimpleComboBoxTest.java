package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

/**
 * SimpleComboBoxTest class file for JUnit 4 tests
 */
public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleComboBoxModel;

    @Before
    public void setUp() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }

    @Test
    public void testConstructor() {
        assertNotNull(simpleComboBox);
        assertEquals(simpleComboBoxModel, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertEquals(newModel, simpleComboBox.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidModel() {
        simpleComboBox.setModel(new Object());
    }

    @Test
    public void testGetSelectedValue() {
        simpleComboBoxModel.addElement("Item 1");
        simpleComboBoxModel.addElement("Item 2");
        simpleComboBox.setSelectedIndex(1);

        assertEquals("Item 2", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenEmpty() {
        assertEquals(null, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        simpleComboBoxModel.addElement("Item 1");
        simpleComboBoxModel.addElement("Item 2");

        simpleComboBox.setSelectedValue("Item 1");
        assertEquals("Item 1", simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue("Item 3");
        assertEquals("Item 3", simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue(1);
        assertEquals("Item 1", simpleComboBox.getSelectedValue()); // since index 1 corresponds to "Item 1"

        simpleComboBox.setSelectedValue(2);
        assertEquals("Item 2", simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue(null);
        assertEquals(null, simpleComboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidValue() {
        simpleComboBox.setSelectedValue(new Object());
    }

    @Test
    public void testGetSelectedInt() {
        simpleComboBoxModel.addElement("Item 1");
        simpleComboBoxModel.addElement("Item 2");
        simpleComboBoxModel.addElement("Item 3");

        simpleComboBox.setSelectedIndex(1);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWhenNullSelectedValue() {
        simpleComboBox.setSelectedValue(null);
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedIntWithInteger() {
        simpleComboBoxModel.addElement("Item 1");
        simpleComboBoxModel.addElement(1);
        simpleComboBoxModel.addElement("Item 3");

        simpleComboBox.setSelectedValue(1);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLong() {
        simpleComboBoxModel.addElement("Item 1");
        simpleComboBoxModel.addElement(1L);
        simpleComboBoxModel.addElement("Item 3");

        simpleComboBox.setSelectedValue(1L);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShort() {
        simpleComboBoxModel.addElement("Item 1");
        simpleComboBoxModel.addElement((short)1);
        simpleComboBoxModel.addElement("Item 3");

        simpleComboBox.setSelectedValue((short)1);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByte() {
        simpleComboBoxModel.addElement("Item 1");
        simpleComboBoxModel.addElement((byte)1);
        simpleComboBoxModel.addElement("Item 3");

        simpleComboBox.setSelectedValue((byte)1);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }
}