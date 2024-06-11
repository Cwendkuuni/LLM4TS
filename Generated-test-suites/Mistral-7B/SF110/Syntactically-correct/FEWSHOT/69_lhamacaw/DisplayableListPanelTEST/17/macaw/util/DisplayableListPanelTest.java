package macaw.util;

import static org.junit.Assert.*;
import org.junit.Test;
import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

    @Test
    public void testInit() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        assertNotNull(displayableListPanel);
        assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
        assertNull(displayableListPanel.getPanel());
        assertNull(displayableListPanel.getDisplayableList());
        assertNull(displayableListPanel.getSelect());
        assertNull(displayableListPanel.getAdd());
        assertNull(displayableListPanel.getEdit());
        assertNull(displayableListPanel.getView());
        assertNull(displayableListPanel.getDelete());
        assertNull(displayableListPanel.getListItemSelector());
        assertNull(displayableListPanel.getListItemAdder());
        assertNull(displayableListPanel.getListItemEditor());
        assertNull(displayableListPanel.getListItemViewer());
        assertNull(displayableListPanel.getListItemDeleter());
        assertNull(displayableListPanel.getListOwnerName());
        assertFalse(displayableListPanel.isAllowWriteAccess());
        assertFalse(displayableListPanel.isAllowRecordEditing());
        assertEquals(0, displayableListPanel.getMasterList().size());
        assertEquals(null, displayableListPanel.getPanel());
    }

    @Test
    public void testBuildUI() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        // Check if panel is not null
        assertNotNull(displayableListPanel.getPanel());

        // Check if title label is added to the panel
        JLabel titleLabel = (JLabel) displayableListPanel.getPanel().getComponent(0);
        assertEquals("Test Panel", titleLabel.getText());

        // Check if displayableList is added to the panel
        JScrollPane scrollPane = (JScrollPane) displayableListPanel.getPanel().getComponent(1);
        assertEquals(displayableListPanel.getDisplayableList(), scrollPane.getViewport().getView());

        // Check if control button panel is added to the panel
        JPanel controlButtonPanel = (JPanel) displayableListPanel.getPanel().getComponent(2);
        assertNotNull(controlButtonPanel);
    }

    @Test
    public void testSetNewRecord() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        // Set new record and check button states
        displayableListPanel.setNewRecord(true);
        assertFalse(displayableListPanel.getAdd().isEnabled());
        assertFalse(displayableListPanel.getDelete().isEnabled());
        assertFalse(displayableListPanel.getEdit().isEnabled());
        assertFalse(displayableListPanel.getView().isEnabled());
        assertFalse(displayableListPanel.getSelect().isEnabled());

        // Set new record to false and check button states
        displayableListPanel.setNewRecord(false);
        assertTrue(displayableListPanel.getAdd().isEnabled());
        assertTrue(displayableListPanel.getSelect().isEnabled());
        if (displayableListPanel.getAllowWriteAccess()) {
            assertTrue(displayableListPanel.getDelete().isEnabled());
            assertTrue(displayableListPanel.getEdit().isEnabled());
            assertTrue(displayableListPanel.getView().isEnabled());
        } else {
            assertFalse(displayableListPanel.getDelete().isEnabled());
            if (displayableListPanel.getAllowRecordEditing()) {
                assertTrue(displayableListPanel.getEdit().isEnabled());
            } else {
                assertFalse(displayableListPanel.getEdit().isEnabled());
                assertTrue(displayableListPanel.getView().isEnabled());
            }
        }
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);

        assertSame(listItemSelector, displayableListPanel.getListItemSelector());
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);

        assertSame(listItemAdder, displayableListPanel.getListItemAdder());
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);

        assertSame(listItemEditor, displayableListPanel.getListItemEditor());
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);

        assertSame(listItemViewer, displayableListPanel.getListItemViewer());
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);

        assertSame(listItemDeleter, displayableListPanel.getListItemDeleter());
    }

    @Test
    public void testAddListItem() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
        when(listItemAdder.addItems(anyString(), any(DisplayableList.class))).thenReturn(new ArrayList<>());
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);

        DisplayableList displayableList = mock(DisplayableList.class);
        when(displayableList.getAllItems()).thenReturn(new ArrayList<>());

        displayableListPanel.setDisplayableList(displayableList);
        displayableListPanel.addListItem();

        // Verify that commitChanges is called
        verify(displayableListPanel).commitChanges();

        // Verify that addItems is called
        verify(listItemAdder).addItems(eq("Test Owner"), displayableList);

        // Verify that updateButtonStates is called
        verify(displayableListPanel).updateButtonStates();
    }

    @Test
    public void testEditSelectedListItem() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
        when(listItemEditor.editListItem(anyString(), any(Displayable.class), any(DisplayableList.class))).thenReturn(new Displayable());

        DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
        when(listItemSelector.selectListItems(anyString(), any(DisplayableList.class))).thenReturn(new ArrayList<>());
        when(listItemSelector.getSelectedItems(any(DisplayableList.class))).thenReturn(new ArrayList<>());

        DisplayableList displayableList = mock(DisplayableList.class);
        when(displayableList.getSelectedItem()).thenReturn(new Displayable());
        when(displayableList.getAllItems()).thenReturn(new ArrayList<>());

        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);

        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        displayableListPanel.setDisplayableList(displayableList);
        displayableListPanel.setParentForm(parentForm);

        Displayable selectedListItem = new Displayable();
        displayableListPanel.editSelectedListItem();

        // Verify that commitChanges is called
        verify(displayableListPanel).commitChanges();

        // Verify that editListItem is called
        verify(listItemEditor).editListItem(eq("Test Owner"), eq(selectedListItem), displayableList);

        // Verify that updateButtonStates is called
        verify(displayableListPanel).updateButtonStates();
    }

    @Test
    public void testViewSelectedListItem() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
        when(listItemViewer.viewListItem(any(Displayable.class))).thenReturn(null);

        DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
        when(listItemSelector.selectListItems(anyString(), any(DisplayableList.class))).thenReturn(new ArrayList<>());
        when(listItemSelector.getSelectedItems(any(DisplayableList.class))).thenReturn(new ArrayList<>());

        DisplayableList displayableList = mock(DisplayableList.class);
        when(displayableList.getSelectedItem()).thenReturn(new Displayable());
        when(displayableList.getAllItems()).thenReturn(new ArrayList<>());

        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);

        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        displayableListPanel.setDisplayableList(displayableList);
        displayableListPanel.setParentForm(parentForm);

        Displayable selectedListItem = new Displayable();
        displayableListPanel.viewSelectedListItem();

        // Verify that commitChanges is called
        verify(displayableListPanel).commitChanges();

        // Verify that viewListItem is called
        verify(listItemViewer).viewListItem(eq(selectedListItem));
    }

    @Test
    public void testDeleteSelectedListItems() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
        when(listItemDeleter.deleteListItems(anyString(), any(DisplayableList.class))).thenReturn(new ArrayList<>());

        DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
        when(listItemSelector.selectListItems(anyString(), any(DisplayableList.class))).thenReturn(new ArrayList<>());
        when(listItemSelector.getSelectedItems(any(DisplayableList.class))).thenReturn(new ArrayList<>());

        DisplayableList displayableList = mock(DisplayableList.class);
        when(displayableList.getSelectedItems()).thenReturn(new ArrayList<>());

        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);

        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        displayableListPanel.setDisplayableList(displayableList);
        displayableListPanel.setParentForm(parentForm);

        JOptionPane optionPane = mock(JOptionPane.class);
        when(optionPane.showConfirmDialog(any(), anyString(), anyString(), anyInt())).thenReturn(0);

        displayableListPanel.deleteSelectedListItems();

        // Verify that commitChanges is called
        verify(displayableListPanel).commitChanges();

        // Verify that deleteListItems is called
        verify(listItemDeleter).deleteListItems(eq("Test Owner"), displayableList);

        // Verify that updateButtonStates is called
        verify(displayableListPanel).updateButtonStates();

        // Verify that showConfirmDialog is called
        verify(optionPane).showConfirmDialog(eq(null), anyString(), anyString(), eq(0));
    }

    @Test
    public void testSelectListItems() {
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(null, null, "Test Panel", false, false);
        displayableListPanel.init(null, null, "Test Panel", false, false);
        displayableListPanel.buildUI();

        DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
        when(listItemSelector.selectListItems(anyString(), any(DisplayableList.class))).thenReturn(new ArrayList<>());

        DisplayableList displayableList = mock(DisplayableList.class);
        when(displayableList.getAllItems()).thenReturn(new ArrayList<>());

        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        displayableListPanel.setDisplayableList(displayableList);
        displayableListPanel.selectListItems();

        // Verify that commitChanges is called
        verify(displayableListPanel).commitChanges();

        // Verify that selectListItems is called
        verify(listItemSelector).selectListItems(eq("Test Owner"), displayableList);

        // Verify that updateButtonStates is called
        verify(displayableListPanel).updateButtonStates();
    }
}