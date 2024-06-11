package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstruction() {
        SimpleComboBox model = new SimpleComboBox(new SimpleComboBoxModel());
        assertEquals("SimpleComboBox[model=SimpleComboBoxModel]", model.toString());
    }

    @Test
    public void testSetModel() {
        SimpleComboBox cb = new SimpleComboBox(null);
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        cb.setModel(model);
        assertSame(model, cb.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWrongType() {
        SimpleComboBox cb = new SimpleComboBox(null);
        cb.setModel(new Object());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox cb = new SimpleComboBox(null);
        assertNull(cb.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox cb = new SimpleComboBox(model);

        model.addElement("One");
        model.addElement("Two");
        model.setSelectedIndex(1);

        assertEquals("Two", cb.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueSelectedIndexOutOfBounds() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox cb = new SimpleComboBox(model);

        assertEquals(null, cb.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox cb = new SimpleComboBox(model);

        model.addElement(1);
        model.addElement(2);
        model.setSelectedIndex(1);

        assertEquals(1, cb.getSelectedInt());

        model.addElement('c');
        model.setSelectedIndex(2);

        assertEquals(2, cb.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWrongType() {
        SimpleComboBox cb = new SimpleComboBox(null);
        cb.setSelectedValue("x");
        cb.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox cb = new SimpleComboBox(model);

        model.addElement(1);
        model.addElement(2L);
        model.setSelectedIndex(1);

        assertEquals(2L, cb.getSelectedLong());

        model.addElement('c');
        model.setSelectedIndex(2);

        assertEquals(2, cb.getSelectedInt());
        assertEquals(2, cb.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWrongType() {
        SimpleComboBox cb = new SimpleComboBox(null);
        cb.setSelectedValue("x");
        cb.getSelectedLong();
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox cb = new SimpleComboBox(model);

        model.addElement("One");
        model.addElement("Two");

        cb.setSelectedValue("One");
        assertEquals("One", cb.getSelectedValue());

        cb.setSelectedValue("Two");
        assertEquals("Two", cb.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWrongType() {
        SimpleComboBox cb = new SimpleComboBox(null);
        cb.setSelectedValue(123);
    }

    @Test
    public void testCreateAndDisplayFrame() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Option 1");
        model.addElement("Option 2");

        final SimpleComboBox cb = new SimpleComboBox(model);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel();
        label.setLabelFor(cb);
        frame.getContentPane().add(label);
        frame.getContentPane().add(cb, 0, 1);

        frame.pack();
        frame.setSize(new Dimension(300, 200));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });

        assertEquals("Option 1", cb.getSelectedItem());
        assertTrue(frame.isVisible());
    }

}