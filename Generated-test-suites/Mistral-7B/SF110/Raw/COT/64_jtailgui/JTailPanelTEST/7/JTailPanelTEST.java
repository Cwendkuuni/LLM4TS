package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnit4.class)
public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;
 private JTable dataTable;
 private JScrollBar verticalScrollBar;
 private JScrollPane scrollPane;

 @Before
 public void setUp() {
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 dataTable = jTailPanel.dataTable;
 verticalScrollBar = jTailPanel.verticalScrollBar;
 scrollPane = jTailPanel.scrollPane;
 }

 @Test
 public void testJTailPanelConstructor() {
 // Check if the constructor is called
 assertNotNull(jTailPanel);
 // Check if the model is initialized
 assertEquals(model, jTailPanel.model);
 // Check if the scrollPane is initialized
 assertNotNull(scrollPane);
 // Check if the dataTable is initialized
 assertNotNull(dataTable);
 // Check if the verticalScrollBar is initialized
 assertNotNull(verticalScrollBar);
 }

 @Test
 public void testGetCharacterHeight() {
 // Test with a valid character height
 int characterHeight = jTailPanel.getCharacterHeight();
 assertEquals(characterHeight, jTailPanel.characterHeight);
 // Test with a negative character height
 int negativeCharacterHeight = -1;
 assertNotSame(negativeCharacterHeight, jTailPanel.getCharacterHeight());
 }

 @Test
 public void testGetVerticalScrollBar() {
 // Test with a null verticalScrollBar
 assertNull(jTailPanel.getVerticalScrollBar());
 // Test after initialization
 assertSame(verticalScrollBar, jTailPanel.getVerticalScrollBar());
 }

 @Test
 public void testUpdate() {
 // Test with an observation of a different object
 Observable differentObservable = new Object();
 jTailPanel.update(differentObservable, new Object());
 // Test with no argument
 jTailPanel.update(model, null);
 // Test with an unused type
 JTailModelEvent unusedEvent = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, -1, null, null, 0);
 jTailPanel.update(model, unusedEvent);
 // Test with a valid JTailModelEvent
 JTailModelEvent validEvent = new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, null, null, 1, null, -1, 2);
 jTailPanel.update(model, validEvent);
 }

 @Test
 public void testUpdateDataTable() {
 // Test with an empty lineNumbers and content
 String emptyLineNumbers = "";
 String emptyLines = "";
 jTailPanel.updateDataTable(emptyLineNumbers, emptyLines);
 assertTrue(dataTable.getRowCount() == 0);
 // Test with a valid lineNumbers and content
 String lineNumbers = "1\n2\n3";
 String lines = "one\ntwo\nthree";
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(3, dataTable.getRowCount());
 for (int i = 0; i < dataTable.getRowCount(); i++) {
 String lineNumber = (String)dataTable.getValueAt(i, 0);
 String line = (String)dataTable.getValueAt(i, 1);
 assertEquals(lineNumber, String.valueOf(i + 1));
 assertEquals(line, String.valueOf(i + 1) + ": " + (char)('a' + i));
 }
 }

 @Test
 public void testResizeColumns() {
 // Test with a width less than the total width of the columns
 int width = 100;
 jTailPanel.updateDataTable("1\n2\n3", "one\ntwo\nthree");
 jTailPanel.dataTable.setWidth(width);
 assertFalse(dataTable.isAutoResizeModeSet(3));
 // Test with a width greater than the total width of the columns
 int width2 = 500;
 jTailPanel.updateDataTable("1\n2\n3", "one\ntwo\nthree");
 jTailPanel.dataTable.setWidth(width2);
 assertTrue(dataTable.isAutoResizeModeSet(3));
 }

 @Test
 public void testScrollToLine() {
 // Test scrolling to a valid line
 int lineToScrollTo = 2;
 jTailPanel.updateDataTable("1\n2\n3", "one\ntwo\nthree");
 jTailPanel.scrollToLine(lineToScrollTo);
 assertEquals(lineToScrollTo, verticalScrollBar.getValue());
 // Test scrolling to a line out of range
 int lineOutOfRange = 10;
 jTailPanel.scrollToLine(lineOutOfRange);
 assertEquals(1, verticalScrollBar.getValue());
 }

 @Test
 public void testDisableScrolling() {
 // Test scrolling when disabled
 int linesDisplayed = 3;
 jTailPanel.updateDataTable("1\n2\n3", "one\ntwo\nthree");
 jTailPanel.update(model, new JTailModelEvent(model, JTailModelEvent.Type.RESIZED, 0, null, null, linesDisplayed, null, 0, linesDisplayed));
 jTailPanel.scrollToLine(2);
 assertFalse(verticalScrollBar.isEnabled());
 // Test scrolling when enabled
 jTailPanel.scrollToLine(2);
 assertTrue(verticalScrollBar.isEnabled());
 }

}