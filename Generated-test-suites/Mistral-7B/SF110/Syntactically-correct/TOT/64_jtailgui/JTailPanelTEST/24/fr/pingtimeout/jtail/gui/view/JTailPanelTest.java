package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.AdjustmentEvent;
import java.awt.event.ComponentEvent;
import java.util.Arrays;

import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pingtimeout.jtail.gui.controller.ScrollBarAdjustmentController;
import fr.pingtimeout.jtail.gui.controller.JTailSizeController;
import fr.pingtimeout.jtail.gui.model.JTailModel;
import fr.pingtimeout.jtail.gui.model.JTailModelEvent;
import fr.pingtimeout.jtail.gui.view.JTailPanel;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel jTailPanel;
 private JTable dataTable;
 private JScrollPane scrollPane;
 private JScrollBar verticalScrollBar;
 private DefaultTableModel tableModel;

 @Before
 public void setUp() {
 mockModel = Mockito.mock(JTailModel.class);
 jTailPanel = new JTailPanel(mockModel);
 dataTable = jTailPanel.getDataTable();
 scrollPane = jTailPanel.getScrollPane();
 verticalScrollBar = jTailPanel.getVerticalScrollBar();
 tableModel = jTailPanel.getTableModel();
 }

 @Test
 public void testJTailPanelConstruction() {
 // Test if JTailPanel is initialized correctly
 assertNotNull(jTailPanel.getModel());
 assertNotNull(jTailPanel.getDataTable());
 assertNotNull(jTailPanel.getScrollPane());
 assertNotNull(jTailPanel.getVerticalScrollBar());
 assertNotNull(jTailPanel.getTableModel());
 assertNotNull(jTailPanel.getCharacterHeight());
 assertNotNull(jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWhenModelIsResized() {
 // Given
 JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.RESIZED, 10, 20, 30, 40, 50);
 // When
 jTailPanel.update(mockModel, event);
 // Then
 verify(mockModel, times(1)).addObserver(jTailPanel);
 verify(verticalScrollBar, times(1)).addAdjustmentListener(any(ScrollBarAdjustmentController.class));
 verify(scrollPane, times(1)).addComponentListener(any(JTailSizeController.class));
 // Verify that scrollbar settings are updated correctly
 final int expectedMinLine = 10;
 final int expectedFirstLine = 20;
 final int expectedNbDisplayedLines = 30;
 final int expectedMaxLine = 50;
 final BoundedRangeModel rangeModel = verticalScrollBar.getModel();
 assertEquals(expectedMinLine, rangeModel.getMinimum());
 assertEquals(expectedFirstLine, rangeModel.getValue());
 assertEquals(expectedNbDisplayedLines, rangeModel.getExtent());
 assertEquals(expectedMaxLine, rangeModel.getMaximum());
 // Verify that dataTable is updated correctly
 final String[] lineNumbers = new String[] {"1", "2", "3", "4", "5", "6"};
 final String[] lines = new String[] {"Line1", "Line2", "Line3", "Line4", "Line5", "Line6"};
 jTailPanel.updateDataTable(Arrays.toString(lineNumbers), Arrays.toString(lines));
 assertEquals("1|Line1\n2|Line2\n3|Line3\n4|Line4\n5|Line5\n6|Line6", Arrays.toString(tableModel.getDataVector()));
 }

 @Test
 public void testUpdateWhenModelIsScrolled() {
 // Given
 JTailModelEvent event = new JTailModelEvent(mockModel, JTailModelEvent.Type.SCROLLED, 10, "Line10");
 // When
 jTailPanel.update(mockModel, event);
 // Then
 verify(mockModel, times(1)).addObserver(jTailPanel);
 // Verify that dataTable is updated correctly
 final String[] lineNumbers = new String[] {"1", "2", "3", "4", "5", "6", "10"};
 final String[] lines = new String[] {"Line1", "Line2", "Line3", "Line4", "Line5", "Line6", "Line10"};
 jTailPanel.updateDataTable(Arrays.toString(lineNumbers), Arrays.toString(lines));
 assertEquals("1|Line1\n2|Line2\n3|Line3\n4|Line4\n5|Line5\n6|Line6\n10|Line10", Arrays.toString(tableModel.getDataVector()));
 }

 @Test
 public void testScrollBarAdjustment() {
 // Given
 final int valueBefore = 10;
 final int newValue = 20;
 final AdjustmentEvent event = new AdjustmentEvent(verticalScrollBar, AdjustmentEvent.ADJUSTING, 0, valueBefore, newValue);
 // When
 verticalScrollBar.getAdjustmentModel().adjustmentValueChanged(event);
 // Then
 final BoundedRangeModel rangeModel = verticalScrollBar.getModel();
 assertEquals(newValue, rangeModel.getValue());
 }

 @Test
 public void testSizeController() {
 // Given
 final ComponentEvent event = new ComponentEvent(jTailPanel, ComponentEvent.RESIZED);
 // When
 jTailPanel.getComponents()[0].requestFocus();
 jTailPanel.getComponents()[0].dispatchEvent(event);
 // Then
 final JTailSizeController sizeController = jTailPanel.getComponents()[0].getComponents()[0];
 // Verify that scrollbar settings are updated correctly
 final int expectedMinLine = 0;
 final int expectedFirstLine = 0;
 final int expectedNbDisplayedLines = 1;
 final int expectedMaxLine = 1;
 final BoundedRangeModel rangeModel = verticalScrollBar.getModel();
 assertEquals(expectedMinLine, rangeModel.getMinimum());
 assertEquals(expectedFirstLine, rangeModel.getValue());
 assertEquals(expectedNbDisplayedLines, rangeModel.getExtent());
 assertEquals(expectedMaxLine, rangeModel.getMaximum());
 // Verify that dataTable is updated correctly
 final String[] lineNumbers = new String[] {"1"};
 final String[] lines = new String[] {"Line1"};
 jTailPanel.updateDataTable(Arrays.toString(lineNumbers), Arrays.toString(lines));
 assertEquals("1|Line1", Arrays.toString(tableModel.getDataVector()[0]));
 }
}