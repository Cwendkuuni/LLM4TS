package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

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
        assertSame(simpleComboBoxModel, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        ComboBoxModel newModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertSame(newModel, simpleComboBox.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidType() {
        SimpleComboBox simpleComboBoxWithInvalidModel = new SimpleComboBox(new Object());
    }

    @Test
    public void testGetSelectedValue() {
        simpleComboBox.addItem(1);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedValue());

        simpleComboBox.addItem("test");
        simpleComboBox.setSelectedItem("test");
        assertEquals("test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenNoSelection() {
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithInteger() {
        simpleComboBox.addItem(1);
        simpleComboBox.setSelectedValue(1);
        assertEquals(1, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithLong() {
        simpleComboBox.addItem(1L);
        simpleComboBox.setSelectedValue(1L);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueWithShort() {
        simpleComboBox.addItem((short) 1);
        simpleComboBox.setSelectedValue((short) 1);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueWithByte() {
        simpleComboBox.addItem((byte) 1);
        simpleComboBox.setSelectedValue((byte) 1);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidType() {
        simpleComboBox.setSelectedValue("test");
    }

    @Test
    public void testGetSelectedIntWhenNoSelection() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongWhenNoSelection() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedIntWithInteger() {
        simpleComboBox.addItem(1);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongWithLong() {
        simpleComboBox.addItem(1L);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
        assertEquals(1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedIntWithShort() {
        simpleComboBox.addItem((short) 1);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
        assertEquals((short) 1, simpleComboBox.getSelectedShort());
    }

    @Test
    public void testGetSelectedIntWithByte() {
        simpleComboBox.addItem((byte) 1);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
        assertEquals((byte) 1, simpleComboBox.getSelectedByte());
    }

    @Test
    public void testSetSelectedValueAndGetSelectedValue() {
        simpleComboBox.addItem(1);
        simpleComboBox.addItem(2);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue(1);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testComboBoxWithNoItems() {
        SimpleComboBox simpleComboBoxNoItems = new SimpleComboBox(null);
        assertNull(simpleComboBoxNoItems.getSelectedValue());
        assertEquals(-1, simpleComboBoxNoItems.getSelectedInt());
        assertEquals(-1L, simpleComboBoxNoItems.getSelectedLong());
    }

    @Test
    public void testComboBoxWithItemsOfDifferentTypes() {
        simpleComboBox.addItem(1);
        simpleComboBox.addItem("test");
        simpleComboBox.addItem(new Long(2));
        simpleComboBox.addItem(new Short("3"));

        simpleComboBox.setSelectedIndex(1);
        assertEquals("test", simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedIndex(2);
        assertEquals(2, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedIndex(3);
        assertEquals((short) 3, simpleComboBox.getSelectedShort());
    }

    @Test
    public void testComboBoxWithCustomModel() {
        SimpleComboBoxModel customModel = new SimpleComboBoxModel() {
            @Override
            public Object getElementAt(int index) {
                return index;
            }

            @Override
            public int getSize() {
                return 5;
            }
        };
        SimpleComboBox simpleComboBoxWithCustomModel = new SimpleComboBox(customModel);

        for (int i = 0; i < 5; i++) {
            simpleComboBoxWithCustomModel.addItem(i);
        }

        simpleComboBoxWithCustomModel.setSelectedIndex(3);
        assertEquals(3, simpleComboBoxWithCustomModel.getSelectedInt());
    }

    @Test
    public void testComboBoxWithCustomModelAndDifferentTypes() {
        SimpleComboBoxModel customModel = new SimpleComboBoxModel() {
            @Override
            public Object getElementAt(int index) {
                if (index == 0) {
                    return 1;
                } else if (index == 1) {
                    return "test";
                } else if (index == 2) {
                    return new Long(2);
                } else if (index == 3) {
                    return new Short("3");
                }
                return null;
            }

            @Override
            public int getSize() {
                return 4;
            }
        };
        SimpleComboBox simpleComboBoxWithCustomModel = new SimpleComboBox(customModel);

        for (int i = 0; i < 4; i++) {
            simpleComboBoxWithCustomModel.addItem(simpleComboBoxWithCustomModel.getItemAt(i));
        }

        simpleComboBoxWithCustomModel.setSelectedIndex(1);
        assertEquals("test", simpleComboBoxWithCustomModel.getSelectedValue());

        simpleComboBoxWithCustomModel.setSelectedIndex(2);
        assertEquals(2, simpleComboBoxWithCustomModel.getSelectedInt());

        simpleComboBoxWithCustomModel.setSelectedIndex(3);
        assertEquals((short) 3, simpleComboBoxWithCustomModel.getSelectedShort());
    }

    @Test
    public void testComboBoxWithCustomModelAndDifferentTypesInDifferentOrder() {
        SimpleComboBoxModel customModel = new SimpleComboBoxModel() {
            @Override
            public Object getElementAt(int index) {
                if (index == 3) {
                    return 1;
                } else if (index == 0) {
                    return "test";
                } else if (index == 2) {
                    return new Long(2);
                } else if (index == 1) {
                    return new Short("3");
                }
                return null;
            }

            @Override
            public int getSize() {
                return 4;
            }
        };
        SimpleComboBox simpleComboBoxWithCustomModel = new SimpleComboBox(customModel);

        for (int i = 0; i < 4; i++) {
            simpleComboBoxWithCustomModel.addItem(simpleComboBoxWithCustomModel.getItemAt(i));
        }

        simpleComboBoxWithCustomModel.setSelectedIndex(2);
        assertEquals(2, simpleComboBoxWithCustomModel.getSelectedInt());

        simpleComboBoxWithCustomModel.setSelectedIndex(1);
        assertEquals((short) 3, simpleComboBoxWithCustomModel.getSelectedShort());

        simpleComboBoxWithCustomModel.setSelectedIndex(0);
        assertEquals("test", simpleComboBoxWithCustomModel.getSelectedValue());

        simpleComboBoxWithCustomModel.setSelectedIndex(3);
        assertEquals(1, simpleComboBoxWithCustomModel.getSelectedInt());
    }

    @Test
    public void testComboBoxWithCustomModelAndNullElement() {
        SimpleComboBoxModel customModel = new SimpleComboBoxModel() {
            @Override
            public Object getElementAt(int index) {
                if (index == 0) {
                    return null;
                }
                return "test";
            }

            @Override
            public int getSize() {
                return 2;
            }
        };
        SimpleComboBox simpleComboBoxWithCustomModel = new SimpleComboBox(customModel);

        for (int i = 0; i < 2; i++) {
            simpleComboBoxWithCustomModel.addItem(simpleComboBoxWithCustomModel.getItemAt(i));
        }

        try {
            simpleComboBoxWithCustomModel.setSelectedValue(null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            simpleComboBoxWithCustomModel.setSelectedIndex(0);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        simpleComboBoxWithCustomModel.setSelectedIndex(1);
        assertEquals("test", simpleComboBoxWithCustomModel.getSelectedValue());
    }

    @Test
    public void testComboBoxWithCustomModelAndMixedElements() {
        SimpleComboBoxModel customModel = new SimpleComboBoxModel() {
            @Override
            public Object getElementAt(int index) {
                if (index == 0) {
                    return 1;
                } else if (index == 1) {
                    return "test";
                } else if (index == 2) {
                    return new Long(2);
                } else if (index == 3) {
                    return new Short("3");
                }
                return null;
            }

            @Override
            public int getSize() {
                return 4;
            }
        };
        SimpleComboBox simpleComboBoxWithCustomModel = new SimpleComboBox(customModel);

        for (int i = 0; i < 4; i++) {
            simpleComboBoxWithCustomModel.addItem(simpleComboBoxWithCustomModel.getItemAt(i));
        }

        try {
            simpleComboBoxWithCustomModel.setSelectedValue("test2");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        simpleComboBoxWithCustomModel.setSelectedIndex(3);
        assertEquals((short) 3, simpleComboBoxWithCustomModel.getSelectedShort());

        simpleComboBoxWithCustomModel.setSelectedIndex(1);
        assertEquals("test", simpleComboBoxWithCustomModel.getSelectedValue());

        simpleComboBoxWithCustomModel.setSelectedIndex(2);
        assertEquals(2, simpleComboBoxWithCustomModel.getSelectedInt());

        simpleComboBoxWithCustomModel.setSelectedIndex(0);
        assertEquals(1, simpleComboBoxWithCustomModel.getSelectedInt());

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(frame, "Test");
            frame.dispose();
        });
    }
}