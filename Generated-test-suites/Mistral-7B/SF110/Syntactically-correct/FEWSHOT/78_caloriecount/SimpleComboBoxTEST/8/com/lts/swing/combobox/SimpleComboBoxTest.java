package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.event.ItemEvent;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setModel(model);
        assertTrue(combobox.mySimpleModel == model);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithNonSimpleComboBoxModel() {
        final ComboBoxModel nonSimpleModel = new DefaultComboBoxModel();
        final SimpleComboBox combobox = new SimpleComboBox(null);

        combobox.setModel(nonSimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");

        final SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedIndex(0);
        assertEquals("Item 1", combobox.getSelectedValue());

        combobox.setSelectedIndex(1);
        assertEquals("Item 2", combobox.getSelectedValue());

        combobox.setSelectedIndex(-1);
        assertNull(combobox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");

        final SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedValue("Item 1");
        assertEquals("Item 1", combobox.getSelectedValue());

        combobox.setSelectedValue("Item 2");
        assertEquals("Item 2", combobox.getSelectedValue());

        combobox.setSelectedValue(null);
        assertNull(combobox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");

        final SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedIndex(0);
        assertEquals(0, combobox.getSelectedInt());

        combobox.setSelectedIndex(1);
        assertEquals(1, combobox.getSelectedInt());

        combobox.setSelectedIndex(-1);
        assertEquals(-1, combobox.getSelectedInt());

        combobox.setSelectedValue(new Integer(42));
        assertEquals(42, combobox.getSelectedInt());

        combobox.setSelectedValue(new Short((short)42));
        assertEquals(42, combobox.getSelectedInt());

        combobox.setSelectedValue(new Byte((byte)42));
        assertEquals(42, combobox.getSelectedInt());

        combobox.setSelectedValue(new Long(42L));
        assertEquals(42, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Item 1");
        model.addElement("Item 2");

        final SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedIndex(0);
        assertEquals(0L, combobox.getSelectedLong());

        combobox.setSelectedIndex(1);
        assertEquals(1L, combobox.getSelectedLong());

        combobox.setSelectedIndex(-1);
        assertEquals(-1L, combobox.getSelectedLong());

        combobox.setSelectedValue(new Integer(42));
        assertEquals(42L, combobox.getSelectedLong());

        combobox.setSelectedValue(new Short((short)42));
        assertEquals(42L, combobox.getSelectedLong());

        combobox.setSelectedValue(new Byte((byte)42));
        assertEquals(42L, combobox.getSelectedLong());

        combobox.setSelectedValue(new Long(42L));
        assertEquals(42L, combobox.getSelectedLong());
    }

    @Test
    public void testFireValueChangedEvent() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel() {
            @Override
            public void setSelectedValue(Object value) {
                super.setSelectedValue(value);
                fireValueChanged(new ItemEvent(this, 0, getSelectedIndex(), getSelectedIndex(), getSelectedValue()));
            }
        };

        final SimpleComboBox combobox = new SimpleComboBox(model);

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(combobox);
        frame.pack();
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            combobox.setSelectedIndex(0);
        });

        int i = 0;
        SwingUtilities.invokeLater(() -> {
            final Runnable listener = () -> {
                i++;
            };
            combobox.addActionListener(listener);
        });

        SwingUtilities.invokeLater(() -> {
            combobox.setSelectedIndex(1);
        });

        assertTrue(i > 0);

        SwingUtilities.invokeLater(() -> combobox.removeActionListener(listener));
        frame.dispose();
    }
}