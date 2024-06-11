package macaw.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import macaw.util.DisplayableListPanel;

public class DisplayableListPanelTest
{
    private DisplayableListPanel displayableListPanel;
    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private Log log;
    private User currentUser;
    private DisplayableList displayableList;
    private DisplayableListItemSelector listItemSelector;
    private DisplayableListItemAdder listItemAdder;
    private DisplayableListItemEditor listItemEditor;
    private DisplayableListItemViewer listItemViewer;
    private DisplayableListItemDeleter listItemDeleter;

    @Before
    public void setUp()
    {
        sessionProperties = mock(SessionProperties.class);
        parentForm = mock(DisplayableListParentForm.class);
        log = mock(Log.class);
        currentUser = mock(User.class);
        when(sessionProperties.getProperty("currentUser")).thenReturn(currentUser);
        displayableList = mock(DisplayableList.class);

        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
        displayableListPanel.init(sessionProperties, parentForm, "Test Panel Title", true, true);
    }

    @Test
    public void testDisplayableListPanelConstructor()
    {
        assertNotNull(displayableListPanel);
    }

    @Test
    public void testAddButtons()
    {
        assertNotNull(displayableListPanel.select);
        assertNotNull(displayableListPanel.add);
        assertNotNull(displayableListPanel.edit);
        assertNotNull(displayableListPanel.view);
        assertNotNull(displayableListPanel.delete);

        assertFalse(displayableListPanel.isNewRecord);
    }

    @Test
    public void testSetNewRecordTrue()
    {
        displayableListPanel.setNewRecord(true);
        assertTrue(displayableListPanel.isNewRecord);
    }

    @Test
    public void testSetNewRecordFalse()
    {
        displayableListPanel.setNewRecord(false);
        assertFalse(displayableListPanel.isNewRecord);
    }

    @Test
    public void testGetPanel()
    {
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test(expected = MacawException.class)
    public void testAddListItemMissingAdder()
    {
        displayableListPanel.listItemAdder = null;
        displayableListPanel.addListItem();
    }

    @Test
    public void testAddListItem()
    {
        displayableListPanel.listItemAdder = mock(DisplayableListItemAdder.class);
        when(parentForm.commitChanges()).thenReturn(true);
        displayableListPanel.addListItem();
        assertTrue(displayableListPanel.isNewRecord);
    }

    @Test(expected = MacawException.class)
    public void testEditSelectedListItemMissingEditor()
    {
        displayableListPanel.listItemEditor = null;
        displayableListPanel.editSelectedListItem();
    }

    @Test(expected = MacawException.class)
    public void testViewSelectedListItemMissingViewer()
    {
        displayableListPanel.listItemViewer = null;
        displayableListPanel.viewSelectedListItem();
    }

    @Test(expected = MacawException.class)
    public void testDeleteSelectedListItemsMissingDeleter()
    {
        displayableListPanel.listItemDeleter = null;
        displayableListPanel.deleteSelectedListItems();
    }

    @Test(expected = MacawException.class)
    public void testDeleteSelectedListItemsUserCancel()
    {
        when(parentForm.commitChanges()).thenReturn(true);
        displayableListPanel.listItemDeleter = mock(DisplayableListItemDeleter.class);
        when(JOptionPane.showConfirmDialog(displayableListPanel.getPanel(), "Warning: Deletion is permanent.\nDo you want to continue?", "Warning", 0, 0)).thenReturn(JOptionPane.NO_OPTION);
        displayableListPanel.deleteSelectedListItems();
    }

    @Test
    public void testDeleteSelectedListItems()
    {
        when(parentForm.commitChanges()).thenReturn(true);
        displayableListPanel.listItemDeleter = mock(DisplayableListItemDeleter.class);
        when(JOptionPane.showConfirmDialog(displayableListPanel.getPanel(), "Warning: Deletion is permanent.\nDo you want to continue?", "Warning", 0, 0)).thenReturn(JOptionPane.YES_OPTION);
        when(displayableListPanel.displayableList.itemsAreSelected()).thenReturn(true);
        displayableListPanel.deleteSelectedListItems();

        assertTrue(displayableListPanel.isNewRecord);
    }

    @Test
    public void testSelectListItems()
    {
        when(displayableListPanel.displayableList.itemsAreSelected()).thenReturn(true);
        displayableListPanel.selectListItems();
        assertTrue(displayableListPanel.isNewRecord);
    }

    @Test
    public void testUpdateButtonStates()
    {
        displayableListPanel.allowWriteAccess = false;
        displayableListPanel.isNewRecord = false;
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());

        displayableListPanel.allowWriteAccess = true;
        displayableListPanel.isNewRecord = false;
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertTrue(displayableListPanel.view.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());

        when(displayableListPanel.displayableList.isEmpty()).thenReturn(false);
        displayableListPanel.allowWriteAccess = true;
        displayableListPanel.isNewRecord = false;
        assertFalse(displayableListPanel.add.isEnabled());
        assertTrue(displayableListPanel.delete.isEnabled());
        assertTrue(displayableListPanel.edit.isEnabled());
        assertTrue(displayableListPanel.view.isEnabled());
        assertTrue(displayableListPanel.select.isEnabled());

        when(displayableListPanel.displayableList.isEmpty()).thenReturn(true);
        displayableListPanel.allowWriteAccess = true;
        displayableListPanel.isNewRecord = false;
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());

        displayableListPanel.isNewRecord = true;
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.add.isEnabled());
        assertFalse(displayableListPanel.delete.isEnabled());
        assertFalse(displayableListPanel.edit.isEnabled());
        assertFalse(displayableListPanel.view.isEnabled());
        assertFalse(displayableListPanel.select.isEnabled());
    }
}