package macaw.util.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import macaw.businessLayer.*;
import macaw.system.*;
import macaw.util.*;
import org.jmockit.MockObject;
import org.jmockit.integration.junit4.JUnit4Mockito;

public class DisplayableListPanelTest
{
    private DisplayableListPanel displayableListPanel;
    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private String panelTitleText;
    private boolean allowWriteAccess;
    private boolean allowRecordEditing;
    private UserInterfaceFactory userInterfaceFactory;
    private Log log;
    private ArrayList<Displayable> masterList;
    private DisplayableList displayableList;
    private JButton selectButton;
    private JButton addButton;
    private JButton editButton;
    private JButton viewButton;
    private JButton deleteButton;

    @Before
    public void setUp()
    {
        sessionProperties = mock(SessionProperties.class);
        parentForm = mock(DisplayableListParentForm.class);
        panelTitleText = "Test Panel Title";
        allowWriteAccess = true;
        allowRecordEditing = true;
        userInterfaceFactory = mock(UserInterfaceFactory.class);
        log = mock(Log.class);
        masterList = new ArrayList<>();
        displayableList = mock(DisplayableList.class);
        selectButton = mock(JButton.class, "selectButton");
        addButton = mock(JButton.class, "addButton");
        editButton = mock(JButton.class, "editButton");
        viewButton = mock(JButton.class, "viewButton");
        deleteButton = mock(JButton.class, "deleteButton");

        when(sessionProperties.getProperty("log")).thenReturn(log);
        when(sessionProperties.getUserInterfaceFactory()).thenReturn(userInterfaceFactory);

        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);

        JUnit4Mockito.initMocks(this);
    }

    @Test
    public void testInit()
    {
        displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);

        verify(sessionProperties, times(1)).getProperty("currentUser");
        assertEquals(panelTitleText, displayableListPanel.getPanelTitleText());
        assertTrue(displayableListPanel.isAllowWriteAccess());
        assertTrue(displayableListPanel.isAllowRecordEditing());
    }

    @Test
    public void testBuildUI()
    {
        displayableListPanel.buildUI();

        verify(userInterfaceFactory, times(1)).createLabel(panelTitleText);
        verify(userInterfaceFactory, times(1)).createPanel();
        verify(displayableList, times(1)).getListScrollPane();
        verify(userInterfaceFactory, times(1)).createGridBagConstraints();
        verify(panel, times(1)).add(any(JComponent.class), any(GridBagConstraints.class));
        verify(userInterfaceFactory, times(1)).createButton(MacawMessages.getMessage("general.buttons.select"));
        verify(userInterfaceFactory, times(1)).createButton(MacawMessages.getMessage("general.buttons.add"));
        verify(userInterfaceFactory, times(1)).createButton(MacawMessages.getMessage("general.buttons.edit"));
        verify(userInterfaceFactory, times(1)).createButton(MacawMessages.getMessage("general.buttons.view"));
        verify(userInterfaceFactory, times(1)).createButton(MacawMessages.getMessage("general.buttons.delete"));
    }

    @Test
    public void testSetDisplayableListItemSelector()
    {
        DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
        displayableListPanel.setDisplayableListItemSelector(mockSelector);

        verify(displayableListPanel, times(1)).listItemSelector = mockSelector;
    }

    @Test
    public void testSetDisplayableListItemAdder()
    {
        DisplayableListItemAdder mockAdder = mock(DisplayableListItemAdder.class);
        displayableListPanel.setDisplayableListItemAdder(mockAdder);

        verify(displayableListPanel, times(1)).listItemAdder = mockAdder;
    }

    @Test
    public void testSetDisplayableListItemEditor()
    {
        DisplayableListItemEditor mockEditor = mock(DisplayableListItemEditor.class);
        displayableListPanel.setDisplayableListItemEditor(mockEditor);

        verify(displayableListPanel, times(1)).listItemEditor = mockEditor;
    }

    @Test
    public void testSetDisplayableListItemViewer()
    {
        DisplayableListItemViewer mockViewer = mock(DisplayableListItemViewer.class);
        displayableListPanel.setDisplayableListItemViewer(mockViewer);

        verify(displayableListPanel, times(1)).listItemViewer = mockViewer;
    }

    @Test
    public void testSetDisplayableListItemDeleter()
    {
        DisplayableListItemDeleter mockDeleter = mock(DisplayableListItemDeleter.class);
        displayableListPanel.setDisplayableListItemDeleter(mockDeleter);

        verify(displayableListPanel, times(1)).listItemDeleter = mockDeleter;
    }

    @Test
    public void testAddListItem()
    {
        when(listItemAdder.addItems(eq(listOwnerName), eq(displayableList))).thenReturn(true);

        displayableListPanel.addListItem();

        verify(parentForm, times(1)).commitChanges();
        verify(listItemAdder, times(1)).addItems(eq(listOwnerName), eq(displayableList));
        assertTrue(displayableListPanel.isNewRecord());
        assertFalse(displayableList.itemsAreSelected());
    }

    @Test(expected = MacawException.class)
    public void testAddListItemMissingAdder()
    {
        when(listItemAdder = null).thenReturn(true);

        displayableListPanel.addListItem();

        fail("Expected MacawException to be thrown");
    }

    @Test
    public void testEditSelectedListItem()
    {
        Displayable mockDisplayable = mock(Displayable.class);
        Displayable selectedListItem = mock(Displayable.class);

        when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
        when(listItemEditor.editListItem(eq(listOwnerName), eq(selectedListItem), eq(displayableList))).thenReturn(true);

        displayableListPanel.editSelectedListItem();

        verify(parentForm, times(1)).commitChanges();
        verify(listItemEditor, times(1)).editListItem(eq(listOwnerName), eq(selectedListItem), eq(displayableList));
        assertFalse(displayableList.itemsAreSelected());
    }

    @Test(expected = MacawException.class)
    public void testEditSelectedListItemMissingEditor()
    {
        when(listItemEditor = null).thenReturn(true);

        displayableListPanel.editSelectedListItem();

        fail("Expected MacawException to be thrown");
    }

    @Test
    public void testViewSelectedListItem()
    {
        Displayable selectedListItem = mock(Displayable.class);

        when(displayableList.getSelectedItem()).thenReturn(selectedListItem);

        displayableListPanel.viewSelectedListItem();

        verify(listItemViewer, times(1)).viewListItem(eq(selectedListItem));
        assertFalse(displayableList.itemsAreSelected());
    }

    @Test(expected = MacawException.class)
    public void testViewSelectedListItemMissingViewer()
    {
        when(listItemViewer = null).thenReturn(true);

        displayableListPanel.viewSelectedListItem();

        fail("Expected MacawException to be thrown");
    }

    @Test
    public void testDeleteSelectedListItems()
    {
        when(listItemDeleter.deleteListItems(eq(listOwnerName), eq(displayableList))).thenReturn(true);
        when(JOptionPane.showConfirmDialog(eq(null), anyString(), anyString(), anyInt())).thenReturn(JOptionPane.YES_OPTION);

        displayableListPanel.deleteSelectedListItems();

        verify(parentForm, times(1)).commitChanges();
        verify(listItemDeleter, times(1)).deleteListItems(eq(listOwnerName), eq(displayableList));
        assertFalse(displayableList.itemsAreSelected());
    }

    @Test(expected = MacawException.class)
    public void testDeleteSelectedListItemsMissingDeleter()
    {
        when(listItemDeleter = null).thenReturn(true);

        displayableListPanel.deleteSelectedListItems();

        fail("Expected MacawException to be thrown");
    }

    @Test
    public void testSelectListItems()
    {
        when(listItemSelector.selectListItems(eq(listOwnerName), eq(displayableList))).thenReturn(true);

        displayableListPanel.selectListItems();

        verify(listItemSelector, times(1)).selectListItems(eq(listOwnerName), eq(displayableList));
        assertTrue(displayableList.itemsAreSelected());
    }

    @Test(expected = MacawException.class)
    public void testSelectListItemsMissingSelector()
    {
        when(listItemSelector = null).thenReturn(true);

        displayableListPanel.selectListItems();

        fail("Expected MacawException to be thrown");
    }

    @Test
    public void testUpdateButtonStates()
    {
        when(displayableList.isEmpty()).thenReturn(false);
        when(displayableList.isSelectedItem()).thenReturn(true);

        displayableListPanel.updateButtonStates();

        assertFalse(displayableListPanel.isNewRecord());
        assertTrue(displayableListPanel.isAllowWriteAccess());
        assertTrue(displayableListPanel.isAllowRecordEditing());
        assertTrue(addButton.isEnabled());
        assertTrue(selectButton.isEnabled());
        assertTrue(editButton.isEnabled());
        assertTrue(viewButton.isEnabled());
        assertTrue(deleteButton.isEnabled());
    }

    @Test
    public void testUpdateButtonStatesEmptyList()
    {
        when(displayableList.isEmpty()).thenReturn(true);
        when(displayableList.isSelectedItem()).thenReturn(false);

        displayableListPanel.updateButtonStates();

        assertFalse(displayableListPanel.isNewRecord());
        assertTrue(displayableListPanel.isAllowWriteAccess());
        assertFalse(displayableListPanel.isAllowRecordEditing());
        assertFalse(addButton.isEnabled());
        assertFalse(selectButton.isEnabled());
        assertFalse(editButton.isEnabled());
        assertFalse(viewButton.isEnabled());
        assertFalse(deleteButton.isEnabled());
    }

    @Test
    public void testUpdateButtonStatesNewRecord()
    {
        when(displayableList.isEmpty()).thenReturn(false);
        when(displayableList.isSelectedItem()).thenReturn(false);
        when(displayableList.isNewRecord()).thenReturn(true);

        displayableListPanel.updateButtonStates();

        assertFalse(displayableListPanel.isNewRecord());
        assertTrue(displayableListPanel.isAllowWriteAccess());
        assertTrue(displayableListPanel.isAllowRecordEditing());
        assertFalse(addButton.isEnabled());
        assertFalse(selectButton.isEnabled());
        assertFalse(editButton.isEnabled());
        assertFalse(viewButton.isEnabled());
        assertFalse(deleteButton.isEnabled());
    }

    @Test
    public void testSetDisplayableItems()
    {
        List<Displayable> mockDisplayables = new ArrayList<>();
        Displayable mockDisplayable = mock(Displayable.class);
        mockDisplayables.add(mockDisplayable);

        displayableListPanel.setDisplayableItems(mockDisplayables);

        verify(displayableList, times(1)).setDisplayItems(eq(mockDisplayables));
        assertEquals(mockDisplayables, displayableListPanel.getAllListItems());
    }

    @Test
    public void testSave()
    {
        Displayable mockDisplayable1 = mock(Displayable.class);
        Displayable mockDisplayable2 = mock(Displayable.class);
        List<Displayable> allItems = new ArrayList<>();
        allItems.add(mockDisplayable1);
        allItems.add(mockDisplayable2);

        when(displayableList.getAllItems()).thenReturn(allItems);

        displayableListPanel.save();

        verify(masterList, times(1)).clear();
        verify(masterList, times(1)).addAll(eq(allItems));
    }

    @Test
    public void testGetAllListItems()
    {
        List<Displayable> allItems = new ArrayList<>();
        allItems.add(mock(Displayable.class));

        when(displayableList.getAllItems()).thenReturn(allItems);

        assertEquals(allItems, displayableListPanel.getAllListItems());
    }
}