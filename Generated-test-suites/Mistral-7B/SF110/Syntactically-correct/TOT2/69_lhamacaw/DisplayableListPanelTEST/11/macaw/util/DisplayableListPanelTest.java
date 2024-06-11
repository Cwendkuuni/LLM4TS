package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import org.junit.*;
import org.mockito.*;

public class DisplayableListPanelTest
{
    private DisplayableListPanel displayableListPanel;
    private DisplayableListParentForm parentForm;
    private SessionProperties sessionProperties;
    private Log log;
    private UserInterfaceFactory userInterfaceFactory;
    private JButton selectButton;
    private JButton addButton;
    private JButton editButton;
    private JButton viewButton;
    private JButton deleteButton;
    private ArrayList<Displayable> masterList;
    private ArrayList<Displayable> masterSourceVariableCollection;
    private DisplayableList displayableList;

    @Before
    public void setUp()
    {
        sessionProperties = mock(SessionProperties.class);
        parentForm = mock(DisplayableListParentForm.class);
        log = mock(Log.class);
        userInterfaceFactory = mock(UserInterfaceFactory.class);

        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "DisplayableListPanelTest", true, true);

        selectButton = displayableListPanel.select;
        addButton = displayableListPanel.add;
        editButton = displayableListPanel.edit;
        viewButton = displayableListPanel.view;
        deleteButton = displayableListPanel.delete;
        masterList = new ArrayList<>();
        masterSourceVariableCollection = new ArrayList<>();
        displayableList = new DisplayableList(userInterfaceFactory);
    }

    @Test
    public void testDisplayableListPanelConstructor()
    {
        verify(sessionProperties, times(1)).getLog();
        verify(parentForm, times(1)).commitChanges();
        verifyNoMoreInteractions(sessionProperties, parentForm);
    }

    @Test
    public void testGetPanel()
    {
        displayableListPanel.buildUI();
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testSetDisplayableListItemSelector()
    {
        DisplayableListItemSelector mockDisplayableListItemSelector = mock(DisplayableListItemSelector.class);
        displayableListPanel.setDisplayableListItemSelector(mockDisplayableListItemSelector);
        verify(displayableListPanel, times(1)).listItemSelector = mockDisplayableListItemSelector;
    }

    @Test
    public void testSetDisplayableListItemAdder()
    {
        DisplayableListItemAdder mockDisplayableListItemAdder = mock(DisplayableListItemAdder.class);
        displayableListPanel.setDisplayableListItemAdder(mockDisplayableListItemAdder);
        verify(displayableListPanel, times(1)).listItemAdder = mockDisplayableListItemAdder;
    }

    @Test
    public void testSetDisplayableListItemEditor()
    {
        DisplayableListItemEditor mockDisplayableListItemEditor = mock(DisplayableListItemEditor.class);
        displayableListPanel.setDisplayableListItemEditor(mockDisplayableListItemEditor);
        verify(displayableListPanel, times(1)).listItemEditor = mockDisplayableListItemEditor;
    }

    @Test
    public void testSetDisplayableListItemViewer()
    {
        DisplayableListItemViewer mockDisplayableListItemViewer = mock(DisplayableListItemViewer.class);
        displayableListPanel.setDisplayableListItemViewer(mockDisplayableListItemViewer);
        verify(displayableListPanel, times(1)).listItemViewer = mockDisplayableListItemViewer;
    }

    @Test
    public void testSetDisplayableListItemDeleter()
    {
        DisplayableListItemDeleter mockDisplayableListItemDeleter = mock(DisplayableListItemDeleter.class);
        displayableListPanel.setDisplayableListItemDeleter(mockDisplayableListItemDeleter);
        verify(displayableListPanel, times(1)).listItemDeleter = mockDisplayableListItemDeleter;
    }

    @Test
    public void testAddListItem()
    {
        doReturn(true).when(parentForm).commitChanges();
        doCallRealMethod().when(displayableListPanel).addListItem();

        verify(parentForm, times(1)).commitChanges();
        verify(displayableListPanel.listItemAdder, times(1)).addItems(eq(displayableListPanel.listOwnerName), eq(displayableList));
    }

    @Test(expected = MacawException.class)
    public void testAddListItemMissingAdder()
    {
        doReturn(true).when(parentForm).commitChanges();
        doThrow(new MacawException(MacawErrorType.MISSING_DISPLAY_LIST_ADDER, "Missing DisplayListAdder")).when(displayableListPanel).addListItem();

        verify(parentForm, times(1)).commitChanges();
        verify(displayableListPanel.log, times(1)).displayErrorDialog(any(MacawException.class));
    }

    @Test
    public void testEditSelectedListItem()
    {
        Displayable mockDisplayable = mock(Displayable.class);
        doReturn(mockDisplayable).when(displayableList).getSelectedItem();
        doReturn(true).when(parentForm).commitChanges();
        doCallRealMethod().when(displayableListPanel).editSelectedListItem();

        verify(parentForm, times(1)).commitChanges();
        verify(displayableListPanel.listItemEditor, times(1)).editListItem(eq(displayableListPanel.listOwnerName), eq(mockDisplayable), eq(displayableList));
    }

    @Test(expected = MacawException.class)
    public void testEditSelectedListItemMissingEditor()
    {
        Displayable mockDisplayable = mock(Displayable.class);
        doReturn(mockDisplayable).when(displayableList).getSelectedItem();
        doReturn(true).when(parentForm).commitChanges();
        doThrow(new MacawException(MacawErrorType.MISSING_DISPLAY_LIST_EDITOR, "Missing DisplayListEditor")).when(displayableListPanel).editSelectedListItem();

        verify(parentForm, times(1)).commitChanges();
        verify(displayableListPanel.log, times(1)).displayErrorDialog(any(MacawException.class));
    }

    @Test
    public void testViewSelectedListItem()
    {
        Displayable mockDisplayable = mock(Displayable.class);
        doReturn(mockDisplayable).when(displayableList).getSelectedItem();
        doCallRealMethod().when(displayableListPanel).viewSelectedListItem();

        verify(displayableListPanel.listItemViewer).viewListItem(eq(mockDisplayable));
    }

    @Test(expected = MacawException.class)
    public void testViewSelectedListItemMissingViewer()
    {
        Displayable mockDisplayable = mock(Displayable.class);
        doReturn(mockDisplayable).when(displayableList).getSelectedItem();
        doCallRealMethod().when(displayableListPanel).viewSelectedListItem();

        verify(displayableListPanel.log, times(1)).displayErrorDialog(any(MacawException.class));
    }

    @Test
    public void testDeleteSelectedListItems()
    {
        doReturn(true).when(parentForm).commitChanges();
        doCallRealMethod().when(displayableListPanel).deleteSelectedListItems();

        verify(parentForm, times(1)).commitChanges();
        verify(displayableListPanel.listItemDeleter, times(1)).deleteListItems(eq(displayableListPanel.listOwnerName), eq(displayableList));

        // Display warning dialog
        doReturn(JOptionPane.YES_OPTION).when(JOptionPane).showConfirmDialog(eq(null), anyString(), anyString(), eq(0), eq(2));
        doCallRealMethod().when(displayableListPanel).deleteSelectedListItems();

        verify(displayableListPanel.displayableList, times(1)).deleteListItems(eq(displayableListPanel.listOwnerName), eq(displayableList));
    }

    @Test
    public void testSelectListItems()
    {
        doCallRealMethod().when(displayableListPanel).selectListItems();

        verify(displayableListPanel.listItemSelector, times(1)).selectListItems(eq(displayableListPanel.listOwnerName), eq(displayableList));
    }

    @Test
    public void testUpdateButtonStates()
    {
        doReturn(false).when(displayableList).isEmpty();
        displayableListPanel.isNewRecord = true;
        displayableListPanel.updateButtonStates();

        verify(addButton, times(1)).setEnabled(false);
        verify(deleteButton, times(1)).setEnabled(false);
        verify(editButton, times(1)).setEnabled(false);
        verify(viewButton, times(1)).setEnabled(false);
        verify(selectButton, times(1)).setEnabled(false);

        displayableListPanel.isNewRecord = false;
        displayableListPanel.updateButtonStates();

        verify(addButton, times(1)).setEnabled(true);
        verify(selectButton, times(1)).setEnabled(true);
        verify(deleteButton, times(1)).setEnabled(true);
        verify(editButton, times(1)).setEnabled(true);
        verify(viewButton, times(1)).setEnabled(true);

        doReturn(true).when(displayableList).isEmpty();
        displayableListPanel.updateButtonStates();

        verify(addButton, times(1)).setEnabled(false);
        verify(deleteButton, times(1)).setEnabled(false);
        if (!displayableListPanel.allowRecordEditing) {
            verify(viewButton, times(1)).setEnabled(false);
        }
        else {
            verify(viewButton, times(1)).setEnabled(true);
        }
        verify(selectButton, times(1)).setEnabled(true);
    }

    @Test
    public void testSetDisplayableItems()
    {
        doCallRealMethod().when(displayableListPanel).setDisplayableItems(masterList);

        verify(displayableList, times(1)).setDisplayItems(masterList);
        verify(displayableListPanel, times(1)).updateButtonStates();
    }

    @Test
    public void testSave()
    {
        doCallRealMethod().when(displayableListPanel).save();

        verify(masterList, times(1)).clear();
        verify(displayableList).getAllItems().size() > 0;
        masterList.addAll(displayableList.getAllItems());
        verify(masterList, times(1)).size();
    }

    @Test
    public void testGetAllListItems()
    {
        ArrayList<Displayable> allListItems = new ArrayList<>();
        doReturn(allListItems).when(displayableList).getAllItems();
        ArrayList<Displayable> allListItemsReturned = displayableListPanel.getAllListItems();

        verify(displayableList.getAllItems(), times(1)).size();
        verify(allListItemsReturned, times(1)).size();
        verify(allListItemsReturned).equals(allListItems);
    }

    @Test
    public void testSetListOwnerName()
    {
        String listOwnerName = "TestListOwnerName";
        displayableListPanel.setListOwnerName(listOwnerName);
        assertEquals(listOwnerName, displayableListPanel.listOwnerName);
    }

    @Test
    public void testActionPerformed()
    {
        doCallRealMethod().when(displayableListPanel).actionPerformed(any(ActionEvent.class));

        ActionEvent selectEvent = mock(ActionEvent.class);
        when(selectEvent.getSource()).thenReturn(selectButton);
        displayableListPanel.actionPerformed(selectEvent);

        verify(displayableListPanel).selectListItems();

        ActionEvent addEvent = mock(ActionEvent.class);
        when(addEvent.getSource()).thenReturn(addButton);
        displayableListPanel.actionPerformed(addEvent);

        verify(displayableListPanel).addListItem();

        ActionEvent editEvent = mock(ActionEvent.class);
        when(editEvent.getSource()).thenReturn(editButton);
        displayableListPanel.actionPerformed(editEvent);

        verify(displayableListPanel).editSelectedListItem();

        ActionEvent viewEvent = mock(ActionEvent.class);
        when(viewEvent.getSource()).thenReturn(viewButton);
        displayableListPanel.actionPerformed(viewEvent);

        verify(displayableListPanel).viewSelectedListItem();

        ActionEvent deleteEvent = mock(ActionEvent.class);
        when(deleteEvent.getSource()).thenReturn(deleteButton);
        displayableListPanel.actionPerformed(deleteEvent);

        verify(displayableListPanel).deleteSelectedListItems();
    }
}