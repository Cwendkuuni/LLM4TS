package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

    private JFrame frame;
    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() throws Exception {
        frame = new JFrame("SimpleComboBoxTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SimpleComboBoxModel model = new SimpleComboBoxModel(new Object[]{"One", "Two", "Three"});
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setBounds(10, 10, 200, 20);
        frame.getContentPane().add(simpleComboBox);

        frame.pack();
        frame.setVisible(true);

        SwingUtilities.invokeAndWait(() -> {
            simpleComboBox.setSelectedIndex(1);
        });
    }

    @After
    public void tearDown() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame.dispose();
        });
    }

    @Test
    public void testConstructor() {
        // Test default constructor
        new SimpleComboBox();

        // Test constructor with model
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Object[]{"One", "Two", "Three"});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        // Test setModel with null model
        try {
            simpleComboBox.setModel(null);
            fail("Expected HeadlessException");
        } catch (HeadlessException e) {
            // Expected
        }

        // Test setModel with valid model
        SimpleComboBoxModel newModel = new SimpleComboBoxModel(new Object[]{"Four", "Five", "Six"});
        simpleComboBox.setModel(newModel);
        assertEquals(newModel, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        assertEquals("Two", simpleComboBox.getSelectedValue());
        simpleComboBox.setSelectedIndex(0);
        assertEquals("One", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        simpleComboBox.setSelectedValue("Four");
        assertEquals("Four", simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue(new Integer(5));
        assertEquals(5, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(new Short((short)6));
        assertEquals(6, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(new Byte((byte)7));
        assertEquals(7, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(new Long(8));
        assertEquals(8, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue("Five");
        assertEquals("Five", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
        simpleComboBox.setSelectedIndex(2);
        assertEquals("Three", simpleComboBox.getSelectedValue());
        assertEquals(2, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
        simpleComboBox.setSelectedIndex(1);
        assertEquals("Two", simpleComboBox.getSelectedValue());
        assertEquals(2, simpleComboBox.getSelectedInt());
        assertEquals(2L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testFireItemEvent() {
        // Test that item event is fired when selection changes
        final List<Object> eventList = Arrays.asList(new Object[3]);
        simpleComboBox.addItemListener(e -> eventList.add(e.getItem()));

        simpleComboBox.setSelectedIndex(0);
        assertEquals("One", simpleComboBox.getSelectedValue());
        assertEquals("One", eventList.get(0));

        simpleComboBox.setSelectedIndex(2);
        assertEquals("Three", simpleComboBox.getSelectedValue());
        assertEquals("Three", eventList.get(1));

        simpleComboBox.setSelectedValue("Four");
        assertEquals("Four", simpleComboBox.getSelectedValue());
        assertEquals("Four", eventList.get(2));
    }
}