package fr.pingtimeout.jtail.gui.view;

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
 panelUnderTest = new JTailPanel(mockModel);
 panelUnderTest.setJTailController(mockController);
 }

 @Test
 public void testConstructor() {
 // Test that a new JTailPanel instance is created
 // No specific assertions here as the constructor does not throw any exceptions
 }

 @Test
 public void testGetCharacterHeight() {
 final FontMetrics metrics = Mockito.spy(panelUnderTest.dataTable.getFontMetrics(panelUnderTest.dataTable.getFont()));
 assertEquals("characterHeight should be correct", panelUnderTest.characterHeight, metrics.getHeight() - 1);
 }

 @Test
 public void testGetVerticalScrollBar() {
 // Test that verticalScrollBar is initialized
 assertNotNull("Vertical scroll bar should not be null", panelUnderTest.getVerticalScrollBar());
 }

 @Test
 public void testUpdateWhenModelIsResized() {
 // Set up the model to simulate a resize event
 final int maxLine = 10;
 when(mockModel.getMaxLine()).thenReturn(maxLine);
 when(mockModel.getNbDisplayedLines()).thenReturn(5);
 when(mockModel.getMinLine()).thenReturn(3);
 when(mockModel.getFirstLine()).thenReturn(1);

 // Set up the JTailPanel to listen to the model
 panelUnderTest.addObserver(panelUnderTest);

 // Simulate the model resizing event
 mockModel.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.RESIZED));

 // Test that the scrollbar is updated correctly
 final BoundedRangeModel rangeModel = panelUnderTest.verticalScrollBar.getModel();
 assertEquals("minLine should be correct", 3, rangeModel.getMinimum());
 assertEquals("firstLine should be correct", 1, rangeModel.getValue());
 assertEquals("nbDisplayedLines should be correct", 5, rangeModel.getExtent());
 assertEquals("maxLine should be correct", 10, rangeModel.getMaximum());
 }

 @Test
 public void testUpdateWhenModelIsScrolled() {
 // Set up the model to simulate a scroll event
 final int maxLine = 10;
 final int firstLine = 5;
 when(mockModel.getMaxLine()).thenReturn(maxLine);
 when(mockModel.getFirstLine()).thenReturn(firstLine);

 // Set up the JTailPanel to listen to the model
 panelUnderTest.addObserver(panelUnderTest);

 // Simulate the model scroll event
 mockModel.notifyObservers(new JTailModelEvent(JTailModelEvent.Type.SCROLLED));

 // Test that the data table is updated correctly
 final TableModel tableModel = panelUnderTest.dataTable.getModel();
 assertEquals("number of rows should be correct", 11, tableModel.getRowCount());
 }

 @After
 public void tearDown() {
 panelUnderTest.removeObserver(panelUnderTest);
 }
}