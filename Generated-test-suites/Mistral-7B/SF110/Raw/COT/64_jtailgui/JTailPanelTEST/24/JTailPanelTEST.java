package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel panel;

 @Before
 public void setUp() {
 model = new JTailModel();
 panel = new JTailPanel(model);
 }

 @Test
 public void testJTailPanelConstructor() {
 JTailLogger.debug("Testing JTailPanel constructor");
 assertNotNull(panel);
 assertEquals(model, panel.model);
 assertNotNull(panel.scrollPane);
 assertNotNull(panel.verticalScrollBar);
 assertNotNull(panel.dataTable);
 assertNotNull(panel.tableModel);
 assertFalse(panel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testGetCharacterHeight() {
 JTailLogger.debug("Testing getCharacterHeight method");
 int characterHeight = panel.getCharacterHeight();
 assertNotNull(characterHeight);
 }

 @Test
 public void testGetVerticalScrollBar() {
 JTailLogger.debug("Testing getVerticalScrollBar method");
 JScrollBar verticalScrollBar = panel.getVerticalScrollBar();
 assertNotNull(verticalScrollBar);
 }

 @Test
 public void testUpdateMethodWithResizeEvent() {
 JTailLogger.debug("Testing update method with RESIZED event");
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.RESIZED, 1, 1, 1, 1, 1, ""));
 assertEquals(1, panel.verticalScrollBar.getModel().getValue());
 }

 @Test
 public void testUpdateMethodWithScrolledEvent() {
 JTailLogger.debug("Testing update method with SCROLLED event");
 model.fireNotification(new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 1, 1, 1, 1, 1, ""));
 assertFalse(panel.verticalScrollBar.isEnabled());
 }

 @Test
 public void testUpdateDataTableMethod() {
 JTailLogger.debug("Testing updateDataTable method");
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
 panel.updateDataTable(lineNumbers, lines);
 int numberOfRows = panel.dataTable.getRowCount();
 for (int i = 0; i < numberOfRows; ++i) {
 String lineNumber = (String)panel.dataTable.getValueAt(i, 0);
 String line = (String)panel.dataTable.getValueAt(i, 1);
 assertEquals(lineNumber, String.valueOf(i + 1));
 assertEquals(line, "Line " + (i + 1));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableMethodWithNullLines() {
 JTailLogger.debug("Testing updateDataTable method with null lines");
 String lineNumbers = "1\n2\n3\n4\n5";
 panel.updateDataTable(lineNumbers, null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateDataTableMethodWithNullLineNumbers() {
 JTailLogger.debug("Testing updateDataTable method with null line numbers");
 panel.updateDataTable(null, "1\n2\n3\n4\n5");
 fail("Should throw an IllegalArgumentException");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testUpdateDataTableMethodWithNotEnoughLines() {
 JTailLogger.debug("Testing updateDataTable method with not enough lines");
 String lineNumbers = "1\n2\n";
 String lines = "Line 1\nLine 2\n";
 panel.updateDataTable(lineNumbers, lines);
 int numberOfRows = panel.dataTable.getRowCount();
 assertEquals(2, numberOfRows);
 }
}