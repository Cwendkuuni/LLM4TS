package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

    private JFrame frame;
    private Container contentPane;

    @Before
    public void setUp() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        contentPane = frame.getContentPane();
    }

    @After
    public void tearDown() {
        if (frame != null) {
            frame.dispose();
        }
    }

    @Test
    public void testSimpleComboBoxConstructor() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3"}));
        assertSame("Expected SimpleComboBoxModel as model", new SimpleComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3"}), simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel<String>());
        SimpleComboBoxModel model = new SimpleComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3"});
        simpleComboBox.setModel(model);
        assertSame("Expected SimpleComboBoxModel as model", model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull("Expected null as selected value", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue() {
        Object expectedValue = "Item 1";
        SimpleComboBoxModel model = new SimpleComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3"});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals("Expected 'Item 1' as selected value", expectedValue, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWrongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(1, 2, 3));
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertInstanceOf("Expected IllegalArgumentException", IllegalArgumentException.class, assertThrows(IllegalArgumentException.class, () -> simpleComboBox.getSelectedValue()));
    }

    @Test
    public void testGetSelectedIntNullValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3"}));
        simpleComboBox.setSelectedIndex(-1);
        assertEquals("Expected -1 as selected int", -1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Integer[]{1, 2, 3});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals("Expected 1 as selected int", 1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Long[]{1L, 2L, 3L});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals("Expected 1 as selected int", 1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Short[]{(short)1, (short)2, (short)3});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals("Expected 1 as selected int", 1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Byte[]{(byte)1, (byte)2, (byte)3});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals("Expected 1 as selected int", 1, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWrongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList("Item 1", "Item 2", "Item 3"));
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
    }

    @Test
    public void testGetSelectedLongNullValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3"}));
        simpleComboBox.setSelectedIndex(-1);
        assertEquals("Expected -1 as selected long", -1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Integer[]{1, 2, 3});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals("Expected 1 as selected long", 1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Long[]{1L, 2L, 3L});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals("Expected 1 as selected long", 1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Short[]{(short)1, (short)2, (short)3});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals("Expected 1 as selected long", 1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Byte[]{(byte)1, (byte)2, (byte)3});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals("Expected 1 as selected long", 1L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWrongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList("Item 1", "Item 2", "Item 3"));
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
    }

    @Test
    public void testSetSelectedValueNullValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3"}));
        simpleComboBox.setSelectedIndex(-1);
        simpleComboBox.setSelectedValue(null);
        assertEquals("Expected null as selected value after setting index to -1", null, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueStringValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3"});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.setSelectedValue("Item 2");
        assertEquals("Expected 'Item 2' as selected value", "Item 2", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Integer[]{1, 2, 3});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.setSelectedValue(2);
        assertEquals("Expected 2 as selected value", 2, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Long[]{1L, 2L, 3L});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.setSelectedValue(2L);
        assertEquals("Expected 2 as selected value", 2, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Short[]{(short)1, (short)2, (short)3});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.setSelectedValue(2);
        assertEquals("Expected 2 as selected value", 2, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(new Byte[]{(byte)1, (byte)2, (byte)3});
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.setSelectedValue((byte)2);
        assertEquals("Expected 2 as selected value", 2, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWrongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList("Item 1", "Item 2", "Item 3"));
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("Item 2");
        simpleComboBox.setSelectedIndex(-1);
    }

    @Test
    public void testComboBoxItemEvent() {
        String[] items = {"Item 1", "Item 2", "Item 3"};
        SimpleComboBoxModel model = new SimpleComboBoxModel(items);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        int initialSelectedIndex = simpleComboBox.getSelectedIndex();
        simpleComboBox.addActionListener(e -> {
            SimpleComboBox simpleComboBox1 = (SimpleComboBox) e.getSource();
            int newSelectedIndex = simpleComboBox1.getSelectedIndex();
            assertEquals("Expected new selected index to be different from initial", newSelectedIndex, initialSelectedIndex + 1);
        });
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.doClick();
    }

    @Test
    public void testComboBoxEditable() {
        String[] items = {"Item 1", "Item 2", "Item 3"};
        SimpleComboBoxModel model = new SimpleComboBoxModel(items);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        JComboBox jComboBox = (JComboBox)simpleComboBox;
        simpleComboBox.setEditable(true);
        assertTrue("Expected JComboBox to be editable", jComboBox.isEditable());
        jComboBox.setEditable(false);
        assertFalse("Expected JComboBox to be not editable", jComboBox.isEditable());
    }
}