package fr.pingtimeout.jtail.gui.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JTailPanelTest {

    private JTailPanel panel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
    private JScrollBar verticalScrollBar;

    @Before
    public void setUp() {
        tableModel = new DefaultTableModel(new Object[][]{}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        verticalScrollBar = new JScrollBar();

        panel = new JTailPanel(new JTailModelStub()) {
            @Override
            public JScrollBar getVerticalScrollBar() {
                return verticalScrollBar;
            }

            @Override
            public void update(Observable observable, Object arg) {
                // do nothing
            }
        };
        panel.setPreferredSize(new Dimension(200, 200));
        panel.setLayout(new BorderLayout());
        panel.add(scrollPane, "Center");
        panel.add(verticalScrollBar, "East");
    }

    @Test
    public void testConstructor() {
        assertEquals("JTailPanel", panel.getClass().getSimpleName());
        assertEquals(new JTailModelStub(), panel.getModel());
        assertNull(panel.getVerticalScrollBar());
        assertNull(panel.getDataTable());
        assertNull(panel.getTableModel());
        assertFalse(panel.isEnabled());
    }

    @Test
    public void testSetEnabled() {
        panel.setEnabled(true);
        assertTrue(panel.isEnabled());
    }

    @Test
    public void testGetCharacterHeight() {
        final FontMetrics metrics = new JLabel("").getFontMetrics(panel.getFont());
        assertEquals(metrics.getHeight() - 1, panel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        assertEquals("JScrollBar", verticalScrollBar.getClass().getSimpleName());
        assertSame(panel, verticalScrollBar.getParent());
        assertFalse(verticalScrollBar.isEnabled());
        assertEquals(0, verticalScrollBar.getValue());
    }

    @Test
    public void testUpdate() {
        final int maxLine = 10;
        final int nbDisplayedLines = 5;
        final int firstLine = 3;
        final int minLine = 2;

        panel.update(new Observable() {}, new JTailModelEvent(JTailModelEvent.Type.RESIZED, maxLine, nbDisplayedLines, firstLine, minLine));

        final BoundedRangeModel rangeModel = verticalScrollBar.getModel();
        assertEquals(true, rangeModel.getValueIsAdjusting());
        assertEquals(minLine, rangeModel.getMinimum());
        assertEquals(firstLine, rangeModel.getValue());
        assertEquals(nbDisplayedLines, rangeModel.getExtent());
        assertEquals(maxLine, rangeModel.getMaximum());
        assertEquals(false, rangeModel.getValueIsAdjusting());

        if (minLine == firstLine && maxLine == nbDisplayedLines) {
            assertTrue(verticalScrollBar.isEnabled());
        } else {
            assertTrue(verticalScrollBar.isEnabled());
        }
    }

    @Test
    public void testUpdateDataTable() {
        final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
        final String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10";

        panel.updateDataTable(lineNumbers, lines);

        assertEquals(10, tableModel.getRowCount());
        for (int i = 0; i < 10; i++) {
            final int row = i;
            assertEquals(String.valueOf(i + 1), tableModel.getValueAt(row, 0).toString());
            assertEquals(lines.split("\n")[i], tableModel.getValueAt(row, 1).toString());
        }

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setPreferredScrollableViewportSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        table.doLayout();
        table.validate();

        final TableColumn numbersColumn = table.getColumnModel().getColumn(0);
        final TableColumn linesColumn = table.getColumnModel().getColumn(1);

        final int numbersColumnWidth = (lineNumbers.split("\n")[lineNumbers.split("\n").length - 1].length()) * panel.getMaxCharacterWidth();
        final int linesColumnWidth = table.getWidth() - numbersColumnWidth;

        assertEquals(numbersColumnWidth, numbersColumn.getPreferredWidth());
        assertEquals(linesColumnWidth, linesColumn.getPreferredWidth());
    }

    @Test
    public void testSizeObserver() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.add(panel, 0);
        frame.setVisible(true);

        final Dimension size = new Dimension(300, 300);
        frame.setSize(size);

        SwingUtilities.invokeLater(() -> {
            final int maxLine = 10;
            final int nbDisplayedLines = 5;
            final int firstLine = 3;
            final int minLine = 2;

            panel.update(new Observable() {}, new JTailModelEvent(JTailModelEvent.Type.RESIZED, maxLine, nbDisplayedLines, firstLine, minLine));

            final BoundedRangeModel rangeModel = verticalScrollBar.getModel();
            assertEquals(true, rangeModel.getValueIsAdjusting());
            assertEquals(minLine, rangeModel.getMinimum());
            assertEquals(firstLine, rangeModel.getValue());
            assertEquals(nbDisplayedLines, rangeModel.getExtent());
            assertEquals(maxLine, rangeModel.getMaximum());
            assertEquals(false, rangeModel.getValueIsAdjusting());

            if (minLine == firstLine && maxLine == nbDisplayedLines) {
                assertTrue(verticalScrollBar.isEnabled());
            } else {
                assertTrue(verticalScrollBar.isEnabled());
            }
        });

        frame.dispose();
    }

    @Test
    public void testScrollBarListener() {
        final AdjustmentListener scrollBarListener = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (e.getValueIsAdjusting()) {
                    return;
                }

                final JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, null);
                panel.update(new Observable() {}, event);
            }
        };

        verticalScrollBar.addAdjustmentListener(scrollBarListener);

        SwingUtilities.invokeLater(() -> {
            final int maxLine = 10;
            final int nbDisplayedLines = 5;
            final int firstLine = 3;
            final int minLine = 2;

            final JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, maxLine, nbDisplayedLines, firstLine, minLine);
            panel.update(new Observable() {}, event);

            final int value = verticalScrollBar.getValue();
            verticalScrollBar.setValue(value + 1);

            SwingUtilities.invokeLater(() -> {
                assertEquals(value + 1, verticalScrollBar.getValue());
            });
        });

        verticalScrollBar.removeAdjustmentListener(scrollBarListener);
    }

    @Test
    public void testComponentListener() {
        final ComponentAdapter componentAdapter = new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (e.getSource() == panel) {
                    final JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, null);
                    panel.update(new Observable() {}, event);
                }
            }
        };

        panel.addComponentListener(componentAdapter);

        SwingUtilities.invokeLater(() -> {
            final int width = 300;
            final int height = 300;
            panel.setSize(new Dimension(width, height));
            panel.validate();

            SwingUtilities.invokeLater(() -> {
                assertEquals(width, panel.getWidth());
                assertEquals(height, panel.getHeight());
            });
        });

        panel.removeComponentListener(componentAdapter);
    }

    @Test
    public void testKeyListener() {
        final KeyEvent keyEvent = new KeyEvent(new JTextField(), KeyEvent.KEY_PRESSED, 0, ' ');

        SwingUtilities.invokeLater(() -> {
            panel.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == keyEvent.getKeyCode()) {
                        final JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, null);
                        panel.update(new Observable() {}, event);
                    }
                }
            });
        });

        SwingUtilities.invokeLater(() -> {
            final int maxLine = 10;
            final int nbDisplayedLines = 5;
            final int firstLine = 3;
            final int minLine = 2;

            final JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, maxLine, nbDisplayedLines, firstLine, minLine);
            panel.update(new Observable() {}, event);

            panel.dispatchEvent(keyEvent);

            SwingUtilities.invokeLater(() -> {
                final int value = verticalScrollBar.getValue();
                verticalScrollBar.setValue(value + 1);

                assertEquals(value + 1, verticalScrollBar.getValue());
            });
        });
    }

    @Test
    public void testMouseListener() {
        final MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getSource() == panel) {
                    final JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.SCROLLED, null);
                    panel.update(new Observable() {}, event);
                }
            }
        };

        panel.addMouseListener(mouseAdapter);

        SwingUtilities.invokeLater(() -> {
            final int maxLine = 10;
            final int nbDisplayedLines = 5;
            final int firstLine = 3;
            final int minLine = 2;

            final JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, maxLine, nbDisplayedLines, firstLine, minLine);
            panel.update(new Observable() {}, event);

            panel.dispatchEvent(new MouseEvent(panel, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), 0, 0, 0, 0, false));

            SwingUtilities.invokeLater(() -> {
                final int value = verticalScrollBar.getValue();
                verticalScrollBar.setValue(value + 1);

                assertEquals(value + 1, verticalScrollBar.getValue());
            });
        });

        panel.removeMouseListener(mouseAdapter);
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> {
            if (panel != null) {
                panel.dispose();
            }
        });
    }
}