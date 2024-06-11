package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.table.*;

public class JTailPanelTest {

 private JTailModel model;
 private JTailPanel jTailPanel;

 @Before
 public void setUp() {
 model = new JTailModel();
 jTailPanel = new JTailPanel(model);
 }

 @After
 public void tearDown() {
 jTailPanel = null;
 model = null;
 }

 // Test constructor
 @Test
 public void testJTailPanelConstructor() {
 JTailLogger mockLogger = new JTailLogger() {
 }; // Mock logger for constructor
 assertNotNull(jTailPanel);
 }

 // Test getCharacterHeight method
 @Test
 public void testGetCharacterHeight() {
 assertEquals(jTailPanel.getCharacterHeight(), 16); // Assuming the font used in the class has a height of 16 pixels
 }

 // Test getVerticalScrollBar method
 @Test
 public void testGetVerticalScrollBar() {
 assertNotNull(jTailPanel.getVerticalScrollBar());
 }

 // Test update method
 @Test
 public void testUpdate() {
 JTailModelEvent event = new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 10, 5, 20);
 model.notifyObservers(event);
 // Simulate the update method being called, assuming it updates the scrollbar values
 jTailPanel.update(model, event);
 // Test that the scrollbar values have been updated correctly
 TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) jTailPanel.dataTable.getRowSorter();
 assertEquals(event.firstLine, sorter.getModel().getValueAt(0, 0).toString().parseInt());
 assertEquals(event.maxLine, sorter.getModel().getRowCount());
 }

 // Test updateDataTable method with empty line numbers and content
 @Test
 public void testUpdateDataTableEmpty() {
 String lineNumbers = "";
 String lines = "";
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(jTailPanel.dataTable.getRowCount(), 0);
 }

 // Test updateDataTable method with non-empty line numbers and content
 @Test
 public void testUpdateDataTable() {
 String lineNumbers = "1\n2\n3\n4\n5";
 String lines = "one\ntwo\nthree\nfour\nfive";
 jTailPanel.updateDataTable(lineNumbers, lines);
 assertEquals(jTailPanel.dataTable.getRowCount(), 5);
 for (int i = 0; i < 5; ++i) {
 String expectedLineNumber = Integer.toString(i + 1);
 String expectedLineContent = "one two three four five".split("\n")[i];
 String actualLineNumber = (String) jTailPanel.dataTable.getValueAt(i, 0);
 String actualLineContent = (String) jTailPanel.dataTable.getValueAt(i, 1);
 assertEquals(expectedLineNumber, actualLineNumber);
 assertEquals(expectedLineContent, actualLineContent);
 }
 }

 // Test exception when model is not an instance of JTailModel
 @Test(expected = IllegalArgumentException.class)
 public void testUpdateWithWrongModel() {
 JTailModel wrongModel = new Object();
 jTailPanel.update(wrongModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 10, 5, 20));
 }
}