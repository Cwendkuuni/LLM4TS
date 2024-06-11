package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;
 private Observer mockObserver;
 private ScrollBarAdapter mockScrollBarController;
 private JTailSizeController mockSizeController;

 @Before
 public void setUp() {
 JTailLogger.reset();
 model = new JTailModel();
 mockObserver = mock(Observer.class);
 mockScrollBarController = mock(ScrollBarAdjustmentController.class);
 mockSizeController = mock(JTailSizeController.class);

 jTailPanel = new JTailPanel(model);
 jTailPanel.addObserver(mockObserver);
 jTailPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
 jTailPanel.addComponentListener(mockSizeController);
 jTailPanel.verticalScrollBar.addAdjustmentListener(mockScrollBarController);
 }

 @Test
 public void testJTailPanelConstruction() {
 assertEquals("Expected JTailPanel to have a JTailModel", model, jTailPanel.model);
 assertNotNull("Expected JTailPanel to have a JScrollPane", jTailPanel.scrollPane);
 assertNotNull("Expected JTailPanel to have a JScrollBar", jTailPanel.verticalScrollBar);
 assertNotNull("Expected JTailPanel to have a JTable", jTailPanel.dataTable);
 assertNotNull("Expected JTailPanel to have a DefaultTableModel", jTailPanel.tableModel);
 assertFalse("Expected JTailPanel vertical scrollbar to be enabled by default", jTailPanel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testGetCharacterHeight() {
 final int expectedCharacterHeight = 15; // Assuming a font with a line-height of 15 pixels
 assertEquals("Expected character height to be " + expectedCharacterHeight, expectedCharacterHeight, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 assertEquals("Expected JTailPanel to return its vertical scrollbar", jTailPanel.verticalScrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdate() {
 final int firstLine = 10;
 final int maxLine = 20;
 final int nbDisplayedLines = 3;
 final JTailModelEvent modelEvent = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, firstLine, maxLine, nbDisplayedLines);

 // Given
 jTailPanel.model.addObserver(mockObserver);

 // When
 jTailPanel.update(model, modelEvent);

 // Then
 verify(mockObserver, times(1)).update(model, argThat(event -> event.getType() == JTailModelEvent.Type.RESIZED));
 JTailLogger.assertDebugContains("The window has beed resized");
 JScrollBar scrollBar = jTailPanel.verticalScrollBar;
 assertEquals("Expected scrollbar minimum to be firstLine", firstLine, scrollBar.getMinimum());
 assertEquals("Expected scrollbar value to be firstLine", firstLine, scrollBar.getValue());
 assertEquals("Expected scrollbar extent to be nbDisplayedLines", nbDisplayedLines, scrollBar.getExtent());
 assertEquals("Expected scrollbar maximum to be maxLine", maxLine, scrollBar.getMaximum());
 }

 @Test
 public void testUpdateWhenDisabled() {
 final int firstLine = 10;
 final int maxLine = 20;
 final int nbDisplayedLines = 3;
 final JTailModelEvent modelEvent = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, firstLine, maxLine, nbDisplayedLines);

 // Given
 jTailPanel.verticalScrollBar.setEnabled(false);
 jTailPanel.model.addObserver(mockObserver);

 // When
 jTailPanel.update(model, modelEvent);

 // Then
 verify(mockObserver, never()).update(model, argThat(event -> event.getType() == JTailModelEvent.Type.RESIZED));
 }

 @Test
 public void testUpdateWhenScrolling() {
 final int firstLine = 10;
 final int maxLine = 20;
 final int lineScrolled = 5;
 final JTailModelEvent modelEvent = new JTailModelEvent(model, JTailModelEvent.Type.SCROLLED, lineScrolled);

 // Given
 jTailPanel.model.addObserver(mockObserver);

 // When
 jTailPanel.update(model, modelEvent);

 // Then
 verify(mockObserver, times(1)).update(model, argThat(event -> event.getType() == JTailModelEvent.Type.SCROLLED));
 }

 @Test
 public void testUpdateDataTable() {
 final String lineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 final String lines = "Line 1 content\nLine 2 content\nLine 3 content\nLine 4 content\nLine 5 content\nLine 6 content\nLine 7 content\nLine 8 content\nLine 9 content\nLine 10 content";

 // Given
 jTailPanel.updateDataTable(lineNumbers, lines);

 // Then
 assertEquals("Expected table to have 10 rows", 10, jTailPanel.tableModel.getRowCount());
 for (int i = 0; i < 10; i++) {
 String expectedLineNumber = Integer.toString(i + 1);
 String expectedLineContent = lines.split("\n")[i];
 TableModel model = jTailPanel.tableModel;
 assertEquals("Expected cell content at position [" + i + ", 0]", expectedLineNumber, model.getValueAt(i, 0).toString());
 assertEquals("Expected cell content at position [" + i + ", 1]", expectedLineContent, model.getValueAt(i, 1).toString());
 }
 }
}