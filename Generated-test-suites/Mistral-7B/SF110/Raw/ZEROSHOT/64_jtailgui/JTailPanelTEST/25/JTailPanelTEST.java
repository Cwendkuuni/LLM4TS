package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.JTailLogger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 mockModel = Mockito.spy(new JTailModel());
 jTailPanel = new JTailPanel(mockModel);
 }

 @Test
 public void testConstructor() {
 JTailLogger.reset();
 JTailPanel jTailPanel = new JTailPanel(mockModel);
 assertEquals(mockModel, jTailPanel.model);
 assertNotNull(jTailPanel.scrollPane);
 assertNotNull(jTailPanel.verticalScrollBar);
 assertNotNull(jTailPanel.dataTable);
 assertNotNull(jTailPanel.tableModel);
 assertEquals(2, jTailPanel.tableModel.getColumnCount());
 assertFalse(jTailPanel.isVisible());
 }

 @Test
 public void testGetCharacterHeight() {
 JTailLogger.reset();
 int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals(characterHeight, jTailPanel.characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JTailLogger.reset();
 JScrollBar verticalScrollBar = jTailPanel.getVerticalScrollBar();
 assertSame(verticalScrollBar, jTailPanel.verticalScrollBar);
 }

 @Test
 public void testUpdate() {
 JTailLogger.reset();
 JTailModelEvent mockEvent = Mockito.mock(JTailModelEvent.class);
 mockModel.addObserver(jTailPanel);
 Mockito.when(mockEvent.type).thenReturn(JTailModelEvent.Type.RESIZED);
 mockModel.setContent("Line 1\nLine 2");
 mockModel.setLineNumbers("1\n2");
 mockModel.notifyObservers(mockEvent);
 assertEquals(1, jTailPanel.tableModel.getRowCount());
 assertNull(jTailPanel.verticalScrollBar.getValueIsAdjusting());
 assertFalse(jTailPanel.verticalScrollBar.isEnabled());
 Mockito.verify(jTailPanel).updateDataTable(Arrays.asList("1", "2"), Arrays.asList("Line 1", "Line 2"));
 }

 @Test
 public void testUpdateDataTable() {
 JTailLogger.reset();
 String[] lineNumbersArray = {"1", "2", "3", "4", "5"};
 String[] linesArray = {"Line 1", "Line 2", "Line 3", "Line 4", "Line 5"};
 jTailPanel.updateDataTable(Arrays.asList(Arrays.toString(lineNumbersArray), Arrays.toString(linesArray)).get(0), Arrays.asList(Arrays.toString(linesArray)).get(0));
 assertEquals(5, jTailPanel.tableModel.getRowCount());
 for (int i = 0; i < 5; i++) {
 String lineNumber = (String) jTailPanel.tableModel.getValueAt(i, 0);
 String line = (String) jTailPanel.tableModel.getValueAt(i, 1);
 assertEquals(lineNumber, String.valueOf(i + 1));
 assertEquals(line, Arrays.asList(linesArray).get(i));
 }
 }

 @Test
 public void testSetVisible() {
 JTailLogger.reset();
 jTailPanel.setVisible(true);
 assertTrue(jTailPanel.isVisible());
 }
}