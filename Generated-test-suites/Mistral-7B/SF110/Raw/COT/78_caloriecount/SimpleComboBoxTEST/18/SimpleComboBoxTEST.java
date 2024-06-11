package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ItemEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

/**
 * Test class for SimpleComboBox.
 */
public class SimpleComboBoxTest {

    private SimpleComboBox comboBox;
    private SimpleComboBoxModel model;
    private JFrame frame;

    @Before
    public void setUp() {
        model = new SimpleComboBoxModel();
        comboBox = new SimpleComboBox(model);
        frame = new JFrame("SimpleComboBox Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new JLabel("Selected Value:"), SwingConstants.LEFT);
        frame.add(comboBox, SwingConstants.LEFT);
        frame.pack();
        frame.setVisible(true);
        EventQueue.invokeLater(() -> comboBox.requestFocus());
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> frame.dispose());
    }

    @Test
    public void testSimpleComboBoxConstructor() {
        assertInstanceOf(SimpleComboBox.class, comboBox);
    }

    @Test
    public void testSetModel() {
        final ComboBoxModel wrongModel = new DefaultComboBoxModel();
        comboBox.setModel(wrongModel);
        assertNull(comboBox.mySimpleModel);

        comboBox.setModel(model);
        assertSame(model, comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        model.addElement("One");
        model.addElement("Two");
        model.setSelectedIndex(1);
        assertEquals("Two", comboBox.getSelectedValue());

        model.setSelectedIndex(0);
        assertEquals("One", comboBox.getSelectedValue());

        model.setSelectedItem("Three");
        assertEquals("Three", comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        model.addElement("One");
        model.addElement("Two");

        comboBox.setSelectedValue("One");
        assertEquals("One", comboBox.getSelectedValue());

        try {
            comboBox.setSelectedValue("Three");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        model.addElement("Three");
        comboBox.setSelectedValue("Three");
        assertEquals("Three", comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        model.addElement("One");
        model.addElement("Two");
        model.addElement(1);
        model.addElement(2);
        model.setSelectedIndex(2);
        assertEquals(2, comboBox.getSelectedInt());

        model.setSelectedIndex(1);
        assertEquals(1, comboBox.getSelectedInt());

        model.setSelectedValue("Three");
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        model.addElement("One");
        model.addElement("Two");
        model.addElement(1L);
        model.addElement(2L);
        model.setSelectedIndex(3);
        assertEquals(2L, comboBox.getSelectedLong());

        model.setSelectedIndex(1);
        assertEquals(1L, comboBox.getSelectedLong());

        model.setSelectedValue("Three");
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testIllegalArgumentException() {
        model.addElement("One");
        model.addElement("Two");

        try {
            comboBox.setSelectedValue(new Object());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            comboBox.getSelectedInt();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            comboBox.getSelectedLong();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetModelWithNullModel() {
        comboBox.setModel(null);
        assertNull(comboBox.mySimpleModel);
    }

    @Test
    public void testSetSelectedValueWithNullValue() {
        model.addElement("One");
        model.addElement("Two");
        model.setSelectedIndex(1);

        comboBox.setSelectedValue(null);
        assertEquals("Two", comboBox.getSelectedValue());

        try {
            comboBox.setSelectedValue(null);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetSelectedValueWithNullModel() {
        model = null;
        assertEquals(null, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedIntWithInvalidValue() {
        model.addElement("One");
        model.addElement("Two");
        model.setSelectedIndex(1);

        try {
            comboBox.setSelectedInt(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            comboBox.setSelectedInt(model.getSize());
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testSetSelectedLongWithInvalidValue() {
        model.addElement("One");
        model.addElement("Two");
        model.addElement(1L);
        model.addElement(2L);
        model.setSelectedIndex(1);

        try {
            comboBox.setSelectedLong(-1L);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            comboBox.setSelectedLong(model.getSize() + 1L);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testSetSelectedIntWithNegativeIndex() {
        model.addElement("One");
        model.addElement("Two");
        model.setSelectedIndex(1);

        try {
            comboBox.setSelectedInt(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testSetSelectedLongWithNegativeIndex() {
        model.addElement("One");
        model.addElement("Two");
        model.addElement(1L);
        model.addElement(2L);
        model.setSelectedIndex(1);

        try {
            comboBox.setSelectedLong(-1L);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testSetSelectedIntWithWrongTypeValue() {
        model.addElement("One");
        model.addElement("Two");
        model.setSelectedIndex(1);

        try {
            comboBox.setSelectedInt(3.14F);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            comboBox.setSelectedInt('A');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetSelectedLongWithWrongTypeValue() {
        model.addElement("One");
        model.addElement("Two");
        model.addElement(1L);
        model.addElement(2L);
        model.setSelectedIndex(1);

        try {
            comboBox.setSelectedLong("Three");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            comboBox.setSelectedLong('B');
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFireStateChangedEvent() {
        final Object[] expectedValues = {"One", "Two", "Three"};
        final Object[] receivedValues = new Object[expectedValues.length];

        comboBox.addItemListener(e -> receivedValues[e.getIndex()] = e.getItem());

        model.setSelectedItem("One");
        assertEquals(expectedValues[0], comboBox.getSelectedValue());
        assertArrayEquals(expectedValues, receivedValues);

        model.setSelectedItem("Two");
        assertEquals(expectedValues[1], comboBox.getSelectedValue());
        assertArrayEquals(expectedValues, receivedValues);

        model.setSelectedItem("Three");
        assertEquals(expectedValues[2], comboBox.getSelectedValue());
        assertArrayEquals(expectedValues, receivedValues);
    }

    @Test
    public void testFireStateChangedEventWithNullModel() {
        comboBox.addItemListener(e -> fail("Should not receive ItemEvent"));
        model = null;
        comboBox.setModel(model);
        comboBox.setSelectedIndex(0);

        SwingUtilities.invokeLater(() -> SwingUtilities.getWindowAncestor(comboBox).dispose());
    }

    @Test
    public void testFireStateChangedEventWithNullListener() {
        comboBox.setModel(model);
        comboBox.addItemListener(null);
        model.setSelectedItem("One");

        SwingUtilities.invokeLater(() -> SwingUtilities.getWindowAncestor(comboBox).dispose());
    }

    @Test
    public void testSetModelWithWrongTypeModel() {
        final DefaultComboBoxModel wrongModel = new DefaultComboBoxModel();
        try {
            comboBox.setModel(wrongModel);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetModelWithEmptyModel() {
        final DefaultComboBoxModel emptyModel = new DefaultComboBoxModel();
        comboBox.setModel(emptyModel);
        assertTrue(comboBox.isEmpty());
    }
}