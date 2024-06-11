package macaw.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import macaw.businessLayer.Displayable;
import macaw.system.Log;
import macaw.system.MacawException;
import macaw.system.MacawMessages;
import macaw.system.SessionProperties;
import macaw.system.User;
import macaw.system.UserInterfaceFactory;
import macaw.util.DisplayableListPanel;
import macaw.util.DisplayableListItemAdder;
import macaw.util.DisplayableListItemDeleter;
import macaw.util.DisplayableListItemEditor;
import macaw.util.DisplayableListItemSelector;
import macaw.util.DisplayableListItemViewer;
import macaw.util.DisplayableListParentForm;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;
    private DisplayableListParentForm displayableListParentFormMock;
    private SessionProperties sessionPropertiesMock;
    private Log logMock;
    private UserInterfaceFactory userInterfaceFactoryMock;
    private DisplayableList displayableListMock;
    private User userMock;
    private DisplayableListItemSelector listItemSelectorMock;
    private DisplayableListItemAdder listItemAdderMock;
    private DisplayableListItemEditor listItemEditorMock;
    private DisplayableListItemViewer listItemViewerMock;
    private DisplayableListItemDeleter listItemDeleterMock;

    @Before
    public void setUp() {
        displayableListParentFormMock = Mockito.mock(DisplayableListParentForm.class);
        sessionPropertiesMock = Mockito.mock(SessionProperties.class);
        logMock = Mockito.mock(Log.class);
        userInterfaceFactoryMock = Mockito.mock(UserInterfaceFactory.class);
        displayableListMock = Mockito.mock(DisplayableList.class);
        userMock = Mockito.mock(User.class);
        listItemSelectorMock = Mockito.mock(DisplayableListItemSelector.class);
        listItemAdderMock = Mockito.mock(DisplayableListItemAdder.class);
        listItemEditorMock = Mockito.mock(DisplayableListItemEditor.class);
        listItemViewerMock = Mockito.mock(DisplayableListItemViewer.class);
        listItemDeleterMock = Mockito.mock(DisplayableListItemDeleter.class);

        Mockito.when(sessionPropertiesMock.getLog()).thenReturn(logMock);
        Mockito.when(sessionPropertiesMock.getProperty("currentUser")).thenReturn(userMock);
        Mockito.when(sessionPropertiesMock.getUserInterfaceFactory()).thenReturn(userInterfaceFactoryMock);
        Mockito.when(userInterfaceFactoryMock.createButton(Mockito.anyString())).thenReturn(Mockito.mock(JButton.class));
        Mockito.when(userInterfaceFactoryMock.createLabel(Mockito.anyString())).thenReturn(Mockito.mock(JLabel.class));
        Mockito.when(userInterfaceFactoryMock.createPanel()).thenReturn(Mockito.mock(JPanel.class));

        displayableListPanel = new DisplayableListPanel(sessionPropertiesMock, displayableListParentFormMock, "Test Panel Title", true, false);

        displayableListPanel.setDisplayableListItemSelector(listItemSelectorMock);
        displayableListPanel.setDisplayableListItemAdder(listItemAdderMock);
        displayableListPanel.setDisplayableListItemEditor(listItemEditorMock);
        displayableListPanel.setDisplayableListItemViewer(listItemViewerMock);
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleterMock);
        displayableListPanel.setDisplayableList(displayableListMock);
    }

    @Test
    public void testConstructorWithAllowWriteAccess() {
        assertNotNull(displayableListPanel);
    }

    @Test
    public void testConstructorWithAllowWriteAccessAndAllowRecordEditing() {
        assertNotNull(displayableListPanel);
    }

    @Test
    public void testInit() {
        displayableListPanel.init(sessionPropertiesMock, displayableListParentFormMock, "Test Panel Title", true, false);
        assertEquals("Test Panel Title", displayableListPanel.getPanelTitleText());
        assertTrue(displayableListPanel.isAllowWriteAccess());
        assertFalse(displayableListPanel.isAllowRecordEditing());

        displayableListPanel.init(sessionPropertiesMock, displayableListParentFormMock, "Test Panel Title", true, true);
        assertEquals("Test Panel Title", displayableListPanel.getPanelTitleText());
        assertTrue(displayableListPanel.isAllowWriteAccess());
        assertTrue(displayableListPanel.isAllowRecordEditing());
    }

    @Test
    public void testBuildUI() {
        displayableListPanel.buildUI();
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test(expected = MacawException.class)
    public void testAddListItemWithMissingAdder() {
        displayableListPanel.addListItem();
    }

    @Test
    public void testEditSelectedListItemWithMissingEditor() {
        Mockito.when(listItemEditorMock.editListItem(Mockito.anyString(), Mockito.any(Displayable.class), Mockito.any(DisplayableList.class)))
                .thenThrow(MacawException.class);

        displayableListPanel.editSelectedListItem();
    }

    @Test
    public void testViewSelectedListItemWithMissingViewer() {
        Mockito.when(listItemViewerMock.viewListItem(Mockito.any(Displayable.class))).thenThrow(MacawException.class);

        displayableListPanel.viewSelectedListItem();
    }

    @Test(expected = MacawException.class)
    public void testDeleteSelectedListItemsWithMissingDeleter() {
        displayableListPanel.deleteSelectedListItems();
    }

    @Test
    public void testSelectListItems() {
        Mockito.when(listItemSelectorMock.selectListItems(Mockito.anyString(), Mockito.any(DisplayableList.class))).thenReturn(true);

        displayableListPanel.selectListItems();
        assertTrue(displayableListPanel.isIsNewRecord());
    }

    @Test
    public void testUpdateButtonStates() {
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.isAddEnabled());
        assertFalse(displayableListPanel.isDeleteEnabled());
        assertFalse(displayableListPanel.isEditEnabled());
        assertFalse(displayableListPanel.isViewEnabled());
        assertFalse(displayableListPanel.isSelectEnabled());

        Mockito.when(displayableListMock.isEmpty()).thenReturn(false);
        displayableListPanel.updateButtonStates();
        assertTrue(displayableListPanel.isAddEnabled());
        assertTrue(displayableListPanel.isSelectEnabled());
        assertTrue(displayableListPanel.isDeleteEnabled());
        assertTrue(displayableListPanel.isEditEnabled());
        assertTrue(displayableListPanel.isViewEnabled());

        Mockito.when(displayableListMock.isEmpty()).thenReturn(true);
        displayableListPanel.updateButtonStates();
        assertFalse(displayableListPanel.isAddEnabled());
        assertFalse(displayableListPanel.isDeleteEnabled());
        assertFalse(displayableListPanel.isEditEnabled());
        assertFalse(displayableListPanel.isViewEnabled());
        assertFalse(displayableListPanel.isSelectEnabled());
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        Displayable displayableMock = Mockito.mock(Displayable.class);
        masterList.add(displayableMock);

        displayableListPanel.setDisplayableItems(masterList);
        assertEquals(1, displayableListPanel.getDisplayableList().size());
    }

    @Test
    public void testSave() {
        ArrayList<Displayable> displayableList = new ArrayList<>();
        Displayable displayableMock = Mockito.mock(Displayable.class);
        displayableList.add(displayableMock);

        displayableListPanel.save();
        assertEquals(displayableList, displayableListPanel.getMasterList());
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> displayableList = new ArrayList<>();
        Displayable displayableMock = Mockito.mock(Displayable.class);
        displayableList.add(displayableMock);

        displayableListPanel.setDisplayableList(displayableListMock);

        ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();
        assertEquals(displayableList, allListItems);
    }

    @Test
    public void testSetListOwnerName() {
        String listOwnerName = "Test Owner";

        displayableListPanel.setListOwnerName(listOwnerName);
        assertEquals(listOwnerName, displayableListPanel.getListOwnerName());
    }

    @Test
    public void testActionPerformedWithSelectButton() {
        ActionEvent actionEventMock = Mockito.mock(ActionEvent.class);

        displayableListPanel.selectListItems();
        displayableListPanel.actionPerformed(actionEventMock);
        Mockito.verify(listItemSelectorMock).selectListItems(Mockito.anyString(), Mockito.any(DisplayableList.class));
    }

    @Test
    public void testActionPerformedWithAddButton() {
        ActionEvent actionEventMock = Mockito.mock(ActionEvent.class);

        displayableListPanel.addListItem();
        displayableListPanel.actionPerformed(actionEventMock);
        Mockito.verify(listItemAdderMock).addItems(Mockito.anyString(), Mockito.any(DisplayableList.class));
    }

    @Test
    public void testActionPerformedWithEditButton() {
        Displayable displayableMock = Mockito.mock(Displayable.class);
        ActionEvent actionEventMock = Mockito.mock(ActionEvent.class);

        Mockito.when(displayableListMock.getSelectedItem()).thenReturn(displayableMock);

        displayableListPanel.editSelectedListItem();
        displayableListPanel.actionPerformed(actionEventMock);
        Mockito.verify(listItemEditorMock).editListItem(Mockito.anyString(), Mockito.eq(displayableMock), Mockito.any(DisplayableList.class));
    }

    @Test
    public void testActionPerformedWithViewButton() {
        Displayable displayableMock = Mockito.mock(Displayable.class);
        ActionEvent actionEventMock = Mockito.mock(ActionEvent.class);

        Mockito.when(displayableListMock.getSelectedItem()).thenReturn(displayableMock);

        displayableListPanel.viewSelectedListItem();
        displayableListPanel.actionPerformed(actionEventMock);
        Mockito.verify(listItemViewerMock).viewListItem(Mockito.eq(displayableMock));
    }

    @Test
    public void testActionPerformedWithDeleteButton() {
        ActionEvent actionEventMock = Mockito.mock(ActionEvent.class);

        displayableListPanel.deleteSelectedListItems();
        Mockito.verify(listItemDeleterMock).deleteListItems(Mockito.anyString(), Mockito.any(DisplayableList.class));
        displayableListPanel.actionPerformed(actionEventMock);
    }
}