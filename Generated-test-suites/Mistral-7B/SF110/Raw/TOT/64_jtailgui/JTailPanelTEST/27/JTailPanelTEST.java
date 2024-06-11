package fr.pingtimeout.jtail.gui.view.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import fr.pingtimeout.jtail.gui.controller.*;
import fr.pingtimeout.jtail.gui.model.*;
import fr.pingtimeout.jtail.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.junit.*;
import org.mockito.*;

public class JTailPanelTest {

 private JTailModel mockModel;
 private JTailController mockController;
 private JTailPanel panelUnderTest;

 @Before
 public void setUp() {
 mockModel = mock(JTailModel.class);
 mockController = mock(JTailController.class);
 panelUnderTest = new JTailPanel(mockModel) {
 @Override
 public void update(Observable o, Object arg) {
 super.update(o, arg);
 mockController.scrollTable(panelUnderTest.getVerticalScrollBar(), panelUnderTest.getModel().getLineNumbers());
 }
 };
 panelUnderTest.setController(mockController);
 }

 @Test
 public void testInitialization() {
 assertEquals("Expected JTailPanel to have a JTable", true, panelUnderTest.contains(panelUnderTest.getDataTable()));
 assertEquals("Expected JTailPanel to have a JScrollPane", true, panelUnderTest.contains(panelUnderTest.getScrollPane()));
 assertEquals("Expected JTailPanel to have a JScrollBar", true, panelUnderTest.contains(panelUnderTest.getVerticalScrollBar()));
 }

 @Test
 public void testUpdateWithEmptyModel() {
 mockModel.setContent("");
 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 0, 0, 0));
 assertEquals("Expected table to have 0 rows after update with empty model", 0, panelUnderTest.getDataTable().getRowCount());
 }

 @Test
 public void testUpdateWithNonEmptyModel() {
 final String expectedLineNumbers = "1\n2\n3\n4\n5";
 final String expectedLines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5";
 mockModel.setContent(expectedLines);
 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 0, 0, 0, 0, 0));
 assertEquals("Expected table to have 5 rows after update with non empty model", 5, panelUnderTest.getDataTable().getRowCount());
 String[] lineNumbers = new String[5];
 for (int i = 0; i < lineNumbers.length; i++) {
 lineNumbers[i] = Integer.toString(i + 1);
 }
 assertArrayEquals("Expected line numbers to match model", lineNumbers, panelUnderTest.getModel().getLineNumbers().split("\n"));
 }

 @Test
 public void testUpdateWithRowsOutOfScreen() {
 final String expectedLineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 final String expectedLines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10";
 mockModel.setContent(expectedLines);
 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.RESIZED, 0, 2, 3, 5, 6, 7));
 String[] lineNumbers = new String[5];
 for (int i = 0; i < lineNumbers.length; i++) {
 lineNumbers[i] = Integer.toString(i + 1);
 }
 assertArrayEquals("Expected line numbers to match model (first 5 lines)", lineNumbers, panelUnderTest.getModel().getLineNumbers().split("\n")[0].split("\n"));
 assertEquals("Expected 3 lines to be displayed", 3, panelUnderTest.getDataTable().getRowCount());
 }

 @Test
 public void testScrolling() {
 final String expectedLineNumbers = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10";
 final String expectedLines = "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nLine 6\nLine 7\nLine 8\nLine 9\nLine 10";
 mockModel.setContent(expectedLines);
 mockController.when(mockController.scrollTable(any(JScrollBar.class), anyString())).thenReturn(true);
 panelUnderTest.update(mockModel, new JTailModelEvent(JTailModelEvent.Type.SCROLLED, 3, 2, 0, 0, 0, 0));
 String[] lineNumbers = new String[5];
 for (int i = 0; i < lineNumbers.length; i++) {
 lineNumbers[i] = Integer.toString(i + 1 + 3);
 }
 assertArrayEquals("Expected line numbers to match model (after scrolling 3 lines down)", lineNumbers, panelUnderTest.getModel().getLineNumbers().split("\n")[0].split("\n"));
 }
}