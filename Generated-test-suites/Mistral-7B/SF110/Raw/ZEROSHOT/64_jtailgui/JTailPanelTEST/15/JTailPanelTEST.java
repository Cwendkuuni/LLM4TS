package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.TailSizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;

public class JTailPanelTest {

    private JTailPanel jTailPanel;
    private JTailModel mockModel;

    @Before
    public void setUp() {
        mockModel = Mockito.mock(JTailModel.class);
        jTailPanel = new JTailPanel(mockModel);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(jTailPanel);
        frame.pack();
    }

    @After
    public void tearDown() {
        jTailPanel = null;
        mockModel = null;
    }

    @Test
    public void testConstructor() {
        assertNotNull(jTailPanel);
        assertEquals(mockModel, jTailPanel.model);
        assertNotNull(jTailPanel.scrollPane);
        assertNotNull(jTailPanel.verticalScrollBar);
        assertNotNull(jTailPanel.dataTable);
        assertNotNull(jTailPanel.tableModel);
        assertEquals(2, jTailPanel.tableModel.getColumnCount());
        assertTrue(jTailPanel.dataTable.getAutoCreateColumnsFromModel());
        assertEquals(0, jTailPanel.tableModel.getRowCount());
        assertEquals(new Dimension(160, 16), jTailPanel.getPreferredSize());
    }

    @Test
    public void testGetCharacterHeight() {
        final int expectedCharacterHeight = 15;
        Mockito.when(mockModel.getCharacterHeight()).thenReturn(expectedCharacterHeight);
        assertEquals(expectedCharacterHeight, jTailPanel.getCharacterHeight());
    }

    @Test
    public void testGetVerticalScrollBar() {
        assertSame(jTailPanel.verticalScrollBar, jTailPanel.getVerticalScrollBar());
    }

    @Test
    public void testUpdate() {
        final int maxLine = 10;
        final int firstLine = 5;
        final int nbDisplayedLines = 5;
        final int minLine = 0;

        Mockito.when(mockModel.getMinLine()).thenReturn(minLine);
        Mockito.when(mockModel.getMaxLine()).thenReturn(maxLine);
        Mockito.when(mockModel.getFirstLine()).thenReturn(firstLine);
        Mockito.when(mockModel.getNbDisplayedLines()).thenReturn(nbDisplayedLines);

        JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED);
        jTailPanel.update(mockModel, event);

        final JScrollBar scrollBar = jTailPanel.verticalScrollBar;
        final BoundedRangeModel scrollBarModel = scrollBar.getModel();

        assertEquals(minLine, scrollBarModel.getMinimum());
        assertEquals(firstLine, scrollBarModel.getValue());
        assertEquals(nbDisplayedLines, scrollBarModel.getExtent());
        assertEquals(maxLine, scrollBarModel.getMaximum());
        assertFalse(scrollBarModel.isAdjusting());

        if (minLine == firstLine && maxLine == nbDisplayedLines) {
            assertFalse(scrollBar.isEnabled());
        } else {
            assertTrue(scrollBar.isEnabled());
        }
    }

    @Test
    public void testUpdateDataTable() {
        String lineNumbers = "1\n2\n3\n4\n5";
        String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";

        jTailPanel.updateDataTable(lineNumbers, lines);

        DefaultTableModel model = (DefaultTableModel) jTailPanel.tableModel;
        int rowCount = model.getRowCount();

        String[] lineNumbersArray = lineNumbers.split("\n");
        String[] linesArray = lines.split("\n");

        for (int i = 0; i < lineNumbersArray.length; i++) {
            String lineNumberTmp = lineNumbersArray[i];
            String lineTmp = (i < linesArray.length) ? linesArray[i] : "";
            String[] rowData = new String[] { lineNumberTmp, lineTmp };
            assertArrayEquals(rowData, model.getDataVector().getElementAt(i));
        }

        assertEquals(rowCount, model.getRowCount());
    }

    @Test
    public void testScrollBarAdjustmentController() {
        JScrollBar scrollBar = Mockito.mock(JScrollBar.class);
        ScrollBarAdjustmentController scrollBarController = new ScrollBarAdjustmentController(jTailPanel, mockModel);

        AdjustmentEvent adjustmentEvent = new AdjustmentEvent(scrollBar, AdjustmentEvent.ADJUSTING, 1, 1, 1);

        scrollBarController.valueChanged(adjustmentEvent);

        Mockito.verify(mockModel).scroll(1);
    }

    @Test
    public void testTailSizeController() {
        JFrame frame = Mockito.mock(JFrame.class);

        TailSizeController tailSizeController = new TailSizeController(jTailPanel, mockModel);

        tailSizeController.componentResized(frame);

        Mockito.verify(mockModel).resize();
    }
}