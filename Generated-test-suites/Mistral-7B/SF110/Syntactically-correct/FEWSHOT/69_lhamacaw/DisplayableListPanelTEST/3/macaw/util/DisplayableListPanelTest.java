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
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testInitWithAllowRecordEditing() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
        assertNotNull(displayableListPanel.getPanel());
        assertTrue(displayableListPanel.isAllowRecordEditing());
    }

    @Test
    public void testAddListener() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        ActionEvent actionEvent = mock(ActionEvent.class);
        ActionListener actionListener = mock(ActionListener.class);

        JButton selectButton = displayableListPanel.getSelect();
        selectButton.addActionListener(actionListener);
        selectButton.actionPerformed(actionEvent);
        verify(actionListener).actionPerformed(actionEvent);
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        assertSame(listItemSelector, displayableListPanel.listItemSelector);
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        assertSame(listItemAdder, displayableListPanel.listItemAdder);
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
        DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        assertSame(listItemEditor, displayableListPanel.listItemEditor);
        assertTrue(displayableListPanel.isAllowRecordEditing());
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        assertSame(listItemViewer, displayableListPanel.listItemViewer);
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        assertSame(listItemDeleter, displayableListPanel.listItemDeleter);
    }

    @Test
    public void testAddListItem() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
        DisplayableList displayableList = mock(DisplayableList.class);
        when(displayableList.getListScrollPane()).thenReturn(mock(JScrollPane.class));
        displayableListPanel.displayableList = displayableList;
        doNothing().when(parentForm).commitChanges();
        doThrow(new MacawException("Test Exception")).when(listItemAdder).addItems(displayableListPanel.listOwnerName, displayableList);

        try {
            displayableListPanel.addListItem();
            fail("Should have thrown MacawException");
        } catch (MacawException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testEditSelectedListItem() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
        DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
        Displayable selectedListItem = mock(Displayable.class);
        DisplayableList displayableList = mock(DisplayableList.class);
        when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
        doNothing().when(parentForm).commitChanges();
        doThrow(new MacawException("Test Exception")).when(listItemEditor).editListItem(displayableListPanel.listOwnerName, selectedListItem, displayableList);

        try {
            displayableListPanel.editSelectedListItem();
            fail("Should have thrown MacawException");
        } catch (MacawException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testViewSelectedListItem() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
        Displayable selectedListItem = mock(Displayable.class);
        when(displayableListPanel.displayableList.getSelectedItem()).thenReturn(selectedListItem);
        doThrow(new MacawException("Test Exception")).when(listItemViewer).viewListItem(selectedListItem);

        try {
            displayableListPanel.viewSelectedListItem();
            fail("Should have thrown MacawException");
        } catch (MacawException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testDeleteSelectedListItems() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
        doThrow(new MacawException("Test Exception")).when(listItemDeleter).deleteListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);

        try {
            displayableListPanel.deleteSelectedListItems();
            fail("Should have thrown MacawException");
        } catch (MacawException e) {
            assertEquals("Test Exception", e.getMessage());
        }
    }

    @Test
    public void testSelectListItems() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
        doNothing().when(listItemSelector).selectListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
        displayableListPanel.listItemSelector = listItemSelector;

        displayableListPanel.selectListItems();
        verify(listItemSelector).selectListItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
    }

    @Test
    public void testUpdateButtonStates() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        boolean allowRecordEditing = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
        displayableListPanel.isNewRecord = true;
        displayableListPanel.displayableList = new DisplayableList();
        displayableListPanel.updateButtonStates();

        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());

        displayableListPanel.isNewRecord = false;
        displayableListPanel.displayableList.isEmpty();
        displayableListPanel.updateButtonStates();

        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());

        displayableListPanel.displayableList.setSize(1);
        displayableListPanel.updateButtonStates();

        assertTrue(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());

        displayableListPanel.allowWriteAccess = false;
        displayableListPanel.updateButtonStates();

        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertTrue(displayableListPanel.view.isEnabled());
    }

    @Test
    public void testSetDisplayableItems() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        ArrayList<Displayable> masterList = new ArrayList<>();
        displayableListPanel.setDisplayableItems(masterList);

        assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
    }

    @Test
    public void testSave() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        ArrayList<Displayable> masterList = new ArrayList<>();
        DisplayableList displayableList = mock(DisplayableList.class);
        when(displayableList.getAllItems()).thenReturn(masterList);
        displayableListPanel.displayableList = displayableList;

        displayableListPanel.save();
        assertEquals(masterList, displayableListPanel.masterList);
    }

    @Test
    public void testGetAllListItems() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        ArrayList<Displayable> allItems = new ArrayList<>();
        when(displayableListPanel.displayableList.getAllItems()).thenReturn(allItems);

        ArrayList<Displayable> result = displayableListPanel.getAllListItems();
        assertEquals(allItems, result);
    }

    @Test
    public void testSetListOwnerName() {
        SessionProperties sessionProperties = new SessionProperties();
        DisplayableListParentForm parentForm = mock(DisplayableListParentForm.class);
        String panelTitleText = "Displayable List Panel";
        boolean allowWriteAccess = true;
        DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
        String listOwnerName = "Test List Owner Name";
        displayableListPanel.setListOwnerName(listOwnerName);
        assertEquals(listOwnerName, displayableListPanel.listOwnerName);
    }
}