package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.util.EventObject;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

/**
 * Test class for SimpleComboBox.
 */
public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;
    private JFrame frame;
    private SimpleComboBoxModel model;

    @Before
    public void setup() {
        model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new JLabel("Selected Value:"), SwingConstants.RIGHT);
        frame.add(comboBox, 10, 10);
        frame.pack();
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    }

    @Test
    public void testConstructor() {
        // Test constructor with valid model
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(comboBox);
    }

    @Test
    public void testSetModel() {
        // Test setting model to null
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getModel());

        // Test setting model to valid SimpleComboBoxModel
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        comboBox.setModel(model);
        assertEquals(model, comboBox.getModel());

        // Test setting model to non-SimpleComboBoxModel
        ComboBoxModel nonSimpleModel = new DefaultComboBoxModel();
        try {
            comboBox.setModel(nonSimpleModel);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetSelectedValue() {
        // Test getting selected value from an empty combo box
        assertEquals(null, comboBox.getSelectedValue());

        // Test getting selected value from a populated combo box
        model.addElement("One");
        model.addElement("Two");
        model.addElement("Three");
        comboBox.setSelectedIndex(1);
        assertEquals("Two", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        // Test setting selected value to null
        comboBox.setSelectedValue(null);
        assertEquals(null, comboBox.getSelectedValue());

        // Test setting selected value to integer
        model.addElement(1);
        comboBox.setSelectedValue(1);
        assertEquals(1, comboBox.getSelectedInt());

        // Test setting selected value to long
        model.addElement(1L);
        comboBox.setSelectedValue(1L);
        assertEquals(1, comboBox.getSelectedInt());

        // Test setting selected value to short
        model.addElement((short) 1);
        comboBox.setSelectedValue((short) 1);
        assertEquals(1, comboBox.getSelectedInt());

        // Test setting selected value to byte
        model.addElement((byte) 1);
        comboBox.setSelectedValue((byte) 1);
        assertEquals(1, comboBox.getSelectedInt());

        // Test setting selected value to non-numeric object
        model.addElement("Four");
        try {
            comboBox.setSelectedValue("Four");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetSelectedInt() {
        // Test getting selected int from an empty combo box
        assertEquals(-1, comboBox.getSelectedInt());

        // Test getting selected int from a populated combo box with integer value
        model.addElement(1);
        comboBox.setSelectedIndex(0);
        assertEquals(1, comboBox.getSelectedInt());

        // Test getting selected int from a populated combo box with long value
        model.addElement(1L);
        comboBox.setSelectedIndex(1);
        assertEquals(1, comboBox.getSelectedInt());

        // Test getting selected int from a populated combo box with short value
        model.addElement((short) 1);
        comboBox.setSelectedIndex(2);
        assertEquals(1, comboBox.getSelectedInt());

        // Test getting selected int from a populated combo box with byte value
        model.addElement((byte) 1);
        comboBox.setSelectedIndex(3);
        assertEquals(1, comboBox.getSelectedInt());

        // Test getting selected int from a populated combo box with non-numeric value
        model.addElement("Five");
        try {
            comboBox.getSelectedInt();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetSelectedLong() {
        // Test getting selected long from an empty combo box
        assertEquals(-1L, comboBox.getSelectedLong());

        // Test getting selected long from a populated combo box with integer value
        model.addElement(1);
        comboBox.setSelectedIndex(0);
        assertEquals(1L, comboBox.getSelectedLong());

        // Test getting selected long from a populated combo box with long value
        model.addElement(1L);
        comboBox.setSelectedIndex(1);
        assertEquals(1L, comboBox.getSelectedLong());

        // Test getting selected long from a populated combo box with short value
        model.addElement((short) 1);
        comboBox.setSelectedIndex(2);
        assertEquals(1L, comboBox.getSelectedLong());

        // Test getting selected long from a populated combo box with byte value
        model.addElement((byte) 1);
        comboBox.setSelectedIndex(3);
        assertEquals(1L, comboBox.getSelectedLong());

        // Test getting selected long from a populated combo box with non-numeric value
        model.addElement("Six");
        try {
            comboBox.getSelectedLong();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testItemEvent() {
        // Test handlingItemStateChanged with valid event
        final Object oldValue = model.getElementAt(0);
        model.addElement("Seven");
        comboBox.setSelectedIndex(1);
        final ItemEvent itemEvent = new ItemEvent(comboBox, 0, "Seven");
        comboBox.handleItemStateChanged(itemEvent);
        assertEquals("Seven", comboBox.getSelectedValue());

        // Test handlingItemStateChanged with null event
        comboBox.handleItemStateChanged(null);
        assertEquals(oldValue, comboBox.getSelectedValue());

        // Test handlingItemStateChanged with unsupported event type
        final EventObject otherEvent = new Object();
        comboBox.handleItemStateChanged(otherEvent);
        assertEquals(oldValue, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedIndexWithInvalidIndex() {
        // Test setting selected index to negative value
        try {
            comboBox.setSelectedIndex(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // Test setting selected index to index greater than size of model
        model.addElement("Eight");
        try {
            comboBox.setSelectedIndex(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testSetSelectedIndexWithNullModel() {
        // Test setting selected index to null model
        comboBox.setModel(null);
        comboBox.setSelectedIndex(0);
        assertEquals(null, comboBox.getSelectedValue());
    }
}