package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.system.MacawException;
import macaw.system.MacawMessages;
import macaw.system.SessionProperties;
import macaw.system.User;
import macaw.system.UserInterfaceFactory;

import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest
{
    private DisplayableListPanel displayableListPanel;
    private UserInterfaceFactory userInterfaceFactoryMock;
    private SessionProperties sessionPropertiesMock;
    private DisplayableListParentForm parentFormMock;
    private Log logMock;
    private DisplayableList displayableListMock;
    private DisplayableListItemSelector listItemSelectorMock;
    private DisplayableListItemAdder listItemAdderMock;
    private DisplayableListItemEditor listItemEditorMock;
    private DisplayableListItemViewer listItemViewerMock;
    private DisplayableListItemDeleter listItemDeleterMock;

    @Before
    public void setUp()
    {
        userInterfaceFactoryMock = mock(UserInterfaceFactory.class);
        sessionPropertiesMock = mock(SessionProperties.class);
        parentFormMock = mock(DisplayableListParentForm.class);
        logMock = mock(Log.class);
        displayableListMock = mock(DisplayableList.class);
        listItemSelectorMock = mock(DisplayableListItemSelector.class);
        listItemAdderMock = mock(DisplayableListItemAdder.class);
        listItemEditorMock = mock(DisplayableListItemEditor.class);
        listItemViewerMock = mock(DisplayableListItemViewer.class);
        listItemDeleterMock = mock(DisplayableListItemDeleter.class);

        when(userInterfaceFactoryMock.createButton(anyString())).thenReturn(mock(JButton.class));
        when(userInterfaceFactoryMock.createLabel(anyString())).thenReturn(mock(JLabel.class));
        when(userInterfaceFactoryMock.createPanel()).thenReturn(mock(JPanel.class));

        when(sessionPropertiesMock.getProperty("currentUser")).thenReturn(new User("testUserID", "testUserName"));
        when(sessionPropertiesMock.getLog()).thenReturn(logMock);
        when(sessionPropertiesMock.getUserInterfaceFactory()).thenReturn(userInterfaceFactoryMock);

        displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, parentFormMock, "Test Title", true, true);
        displayableListPanel.userInterfaceFactory = userInterfaceFactoryMock;
        displayableListPanel.sessionProperties = sessionPropertiesMock;
        displayableListPanel.parentForm = parentFormMock;
        displayableListPanel.displayableList = displayableListMock;
        displayableListPanel.listItemSelector = listItemSelectorMock;
        displayableListPanel.listItemAdder = listItemAdderMock;
        displayableListPanel.listItemEditor = listItemEditorMock;
        displayableListPanel.listItemViewer = listItemViewerMock;
        displayableListPanel.listItemDeleter = listItemDeleterMock;
    }

    @Test
    public void testInit()
    {
        verify(userInterfaceFactoryMock, times(6)).createButton(anyString());
        verify(userInterfaceFactoryMock, times(1)).createPanel();
        verify(userInterfaceFactoryMock, times(1)).createLabel(displayableListPanel.panelTitleText);
        verify(sessionPropertiesMock, times(1)).getLog();
        verify(sessionPropertiesMock, times(1)).getUserInterfaceFactory();
        verify(parentFormMock, times(0)).commitChanges();
    }

    @Test
    public void testAddListItem() throws MacawException
    {
        when(listItemAdderMock.addItems(anyString(), eq(displayableListMock))).thenReturn(new ArrayList<Displayable>());

        displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
        displayableListPanel.addListItem();

        verify(parentFormMock, times(1)).commitChanges();
        verify(listItemAdderMock, times(1)).addItems(displayableListPanel.listOwnerName, displayableListMock);
    }

    @Test(expected = MacawException.class)
    public void testAddListItemMissingAdder()
    {
        displayableListPanel.setDisplayableListItemAdder(null);
        displayableListPanel.addListItem();

        fail("Should throw MacawException for missing DisplayableListItemAdder");
    }

    @Test
    public void testEditSelectedListItem() throws MacawException
    {
        Displayable testDisplayable = new Displayable("Test Displayable Name");
        when(displayableListMock.getSelectedItem()).thenReturn(testDisplayable);
        when(listItemEditorMock.editListItem(anyString(), eq(testDisplayable), eq(displayableListMock))).thenReturn(testDisplayable);

        displayableListPanel.setDisplayableListItemEditor(listItemEditorMock);
        displayableListPanel.editSelectedListItem();

        verify(parentFormMock, times(1)).commitChanges();
        verify(displayableListMock, times(1)).getSelectedItem();
        verify(listItemEditorMock, times(1)).editListItem(displayableListPanel.listOwnerName, testDisplayable, displayableListMock);
    }

    @Test(expected = MacawException.class)
    public void testEditSelectedListItemMissingEditor()
    {
        when(displayableListMock.itemsAreSelected()).thenReturn(true);
        when(displayableListMock.getSelectedItem()).thenReturn(null);

        displayableListPanel.setDisplayableListItemEditor(null);
        displayableListPanel.editSelectedListItem();

        fail("Should throw MacawException for missing DisplayableListItemEditor");
    }

    @Test
    public void testViewSelectedListItem() throws MacawException
    {
        Displayable testDisplayable = new Displayable("Test Displayable Name");
        when(displayableListMock.getSelectedItem()).thenReturn(testDisplayable);

        displayableListPanel.setDisplayableListItemViewer(listItemViewerMock);
        displayableListPanel.viewSelectedListItem();

        verify(listItemViewerMock, times(1)).viewListItem(testDisplayable);
    }

    @Test(expected = MacawException.class)
    public void testViewSelectedListItemMissingViewer()
    {
        when(displayableListMock.itemsAreSelected()).thenReturn(true);
        when(displayableListMock.getSelectedItem()).thenReturn(null);

        displayableListPanel.setDisplayableListItemViewer(null);
        displayableListPanel.viewSelectedListItem();

        fail("Should throw MacawException for missing DisplayableListItemViewer");
    }

    @Test
    public void testDeleteSelectedListItems() throws MacawException
    {
        when(listItemDeleterMock.deleteListItems(anyString(), eq(displayableListMock))).thenReturn(new ArrayList<Displayable>());
        when(JOptionPane.showConfirmDialog(any(JFrame.class), anyString(), anyString(), 0, 2)).thenReturn(JOptionPane.YES_OPTION);
        when(displayableListMock.itemsAreSelected()).thenReturn(true);

        displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
        displayableListPanel.deleteSelectedListItems();

        verify(parentFormMock, times(1)).commitChanges();
        verify(JOptionPane, times(1)).showConfirmDialog(eq(null), anyString(), anyString(), 0, 2);
        verify(listItemDeleterMock, times(1)).deleteListItems(displayableListPanel.listOwnerName, displayableListMock);
    }

    @Test(expected = MacawException.class)
    public void testDeleteSelectedListItemsNoItemsSelected()
    {
        when(displayableListMock.itemsAreSelected()).thenReturn(false);

        displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
        displayableListPanel.deleteSelectedListItems();

        fail("Should throw MacawException for unselected items");
    }

    @Test
    public void testSelectListItems() throws MacawException
    {
        when(listItemSelectorMock.selectListItems(anyString(), eq(displayableListMock))).thenReturn(new ArrayList<Displayable>());

        displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
        displayableListPanel.selectListItems();

        verify(parentFormMock, times(1)).commitChanges();
        verify(listItemSelectorMock, times(1)).selectListItems(displayableListPanel.listOwnerName, displayableListMock);
    }

    @Test(expected = MacawException.class)
    public void testSelectListItemsNoItemsToSelect()
    {
        when(displayableListMock.isEmpty()).thenReturn(true);

        displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
        displayableListPanel.selectListItems();

        fail("Should throw MacawException for empty displayable list");
    }

    @Test
    public void testUpdateButtonStates()
    {
        displayableListPanel.isNewRecord = false;
        displayableListPanel.allowWriteAccess = true;
        displayableListPanel.allowRecordEditing = true;
        displayableListPanel.displayableList.setEmpty(false);

        displayableListPanel.updateButtonStates();

        verify(selectListItemsMock, times(1)).isEnabled();
        verify(addListItemMock, times(1)).isEnabled();
        verify(editListItemMock, times(1)).isEnabled();
        verify(viewListItemMock, times(1)).isEnabled();
        verify(deleteListItemsMock, times(1)).isEnabled();
    }

    @Test
    public void testUpdateButtonStatesNewRecord()
    {
        displayableListPanel.isNewRecord = true;
        displayableListPanel.allowWriteAccess = true;

        displayableListPanel.updateButtonStates();

        verify(selectListItemsMock, times(0)).isEnabled();
        verify(addListItemMock, times(0)).isEnabled();
        verify(editListItemMock, times(0)).isEnabled();
        verify(viewListItemMock, times(0)).isEnabled();
        verify(deleteListItemsMock, times(0)).isEnabled();
    }

    @Test
    public void testUpdateButtonStatesNoWriteAccess()
    {
        displayableListPanel.isNewRecord = false;
        displayableListPanel.allowWriteAccess = false;

        displayableListPanel.updateButtonStates();

        verify(selectListItemsMock, times(1)).isEnabled();
        verify(addListItemMock, times(0)).isEnabled();
        verify(editListItemMock, times(0)).isEnabled();
        verify(viewListItemMock, times(1)).isEnabled();
        verify(deleteListItemsMock, times(0)).isEnabled();
    }

    @Test
    public void testUpdateButtonStatesEmptyDisplayableList()
    {
        displayableListPanel.isNewRecord = false;
        displayableListPanel.allowWriteAccess = true;

        displayableListPanel.updateButtonStates();

        verify(selectListItemsMock, times(0)).isEnabled();
        verify(addListItemMock, times(0)).isEnabled();
        verify(editListItemMock, times(0)).isEnabled();
        verify(viewListItemMock, times(0)).isEnabled();
        verify(deleteListItemsMock, times(0)).isEnabled();
    }

    @Test
    public void testSave()
    {
        when(displayableListMock.getAllItems()).thenReturn(new ArrayList<Displayable>());

        displayableListPanel.save();

        verify(masterList, times(1)).clear();
        verify(masterList, times(1)).addAll(displayableListMock.getAllItems());
    }
}