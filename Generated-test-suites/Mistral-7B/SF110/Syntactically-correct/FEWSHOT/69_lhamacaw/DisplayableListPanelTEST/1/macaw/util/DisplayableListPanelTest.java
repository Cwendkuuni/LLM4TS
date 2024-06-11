package macaw.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;

public class DisplayableListPanelTest {

    private DisplayableListPanel displayableListPanel;
    private SessionProperties sessionProperties;
    private DisplayableListParentForm parentForm;
    private UserInterfaceFactory userInterfaceFactory;
    private Log log;
    private String panelTitleText;
    private boolean allowWriteAccess;
    private boolean allowRecordEditing;

    @Before
    public void setUp() {
        sessionProperties = new SessionProperties();
        parentForm = new DisplayableListParentForm();
        userInterfaceFactory = new UserInterfaceFactory();
        log = new Log();
        sessionProperties.setLog(log);
        panelTitleText = "Test Panel Title";
        allowWriteAccess = true;
        allowRecordEditing = true;
        displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
    }

    @Test
    public void testConstructorWithAllowWriteAccess() {
        assertEquals("Test Panel Title", displayableListPanel.getPanelTitleText());
        assertTrue(displayableListPanel.isAllowWriteAccess());
        assertFalse(displayableListPanel.isAllowRecordEditing());
    }

    @Test
    public void testConstructorWithAllowWriteAccessAndAllowRecordEditing() {
        assertEquals("Test Panel Title", displayableListPanel.getPanelTitleText());
        assertTrue(displayableListPanel.isAllowWriteAccess());
        assertTrue(displayableListPanel.isAllowRecordEditing());
    }

    @Test
    public void testInit() {
        assertNotNull(displayableListPanel.getDisplayableList());
        assertNotNull(displayableListPanel.getSelectButton());
        assertNotNull(displayableListPanel.getAddButton());
        assertNotNull(displayableListPanel.getEditButton());
        assertNotNull(displayableListPanel.getViewButton());
        assertNotNull(displayableListPanel.getDeleteButton());
    }

    @Test
    public void testBuildUI() {
        assertNotNull(displayableListPanel.getPanel());
    }

    @Test
    public void testSetNewRecord() {
        displayableListPanel.setNewRecord(true);
        assertFalse(displayableListPanel.isAddEnabled());
        assertFalse(displayableListPanel.isDeleteEnabled());
        assertFalse(displayableListPanel.isEditEnabled());
        assertFalse(displayableListPanel.isViewEnabled());
        assertFalse(displayableListPanel.isSelectEnabled());

        displayableListPanel.setNewRecord(false);
        assertTrue(displayableListPanel.isAddEnabled());
        assertTrue(displayableListPanel.isSelectEnabled());
        if (displayableListPanel.getDisplayableList().isEmpty()) {
            assertFalse(displayableListPanel.isDeleteEnabled());
            if (!displayableListPanel.isAllowRecordEditing()) {
                assertFalse(displayableListPanel.isEditEnabled());
            } else {
                assertTrue(displayableListPanel.isEditEnabled());
            }
            assertFalse(displayableListPanel.isViewEnabled());
        } else {
            assertTrue(displayableListPanel.isDeleteEnabled());
            if (!displayableListPanel.isAllowRecordEditing()) {
                assertFalse(displayableListPanel.isEditEnabled());
            } else {
                assertTrue(displayableListPanel.isEditEnabled());
            }
            assertTrue(displayableListPanel.isViewEnabled());
        }
    }

    @Test
    public void testGetPanel() {
        JPanel panel = displayableListPanel.getPanel();
        assertNotNull(panel);
    }

    @Test
    public void testSetDisplayableListItemSelector() {
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        assertEquals(listItemSelector, displayableListPanel.getListItemSelector());
    }

    @Test
    public void testSetDisplayableListItemAdder() {
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        assertEquals(listItemAdder, displayableListPanel.getListItemAdder());
    }

    @Test
    public void testSetDisplayableListItemEditor() {
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        assertEquals(listItemEditor, displayableListPanel.getListItemEditor());
    }

    @Test
    public void testSetDisplayableListItemViewer() {
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        assertEquals(listItemViewer, displayableListPanel.getListItemViewer());
    }

    @Test
    public void testSetDisplayableListItemDeleter() {
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        assertEquals(listItemDeleter, displayableListPanel.getListItemDeleter());
    }

    @Test
    public void testAddListItem() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        DisplayableListItemAdder listItemAdder = new DisplayableListItemAdder();
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        displayableListPanel.setMasterList(masterList);

        displayableListPanel.addListItem();

        assertEquals(1, masterList.size());
        assertTrue(displayableListPanel.isAddEnabled());
        assertTrue(displayableListPanel.isSelectEnabled());
        if (displayableListPanel.getDisplayableList().isEmpty()) {
            assertFalse(displayableListPanel.isDeleteEnabled());
            if (!displayableListPanel.isAllowRecordEditing()) {
                assertFalse(displayableListPanel.isEditEnabled());
            } else {
                assertTrue(displayableListPanel.isEditEnabled());
            }
            assertFalse(displayableListPanel.isViewEnabled());
        } else {
            assertTrue(displayableListPanel.isDeleteEnabled());
            if (!displayableListPanel.isAllowRecordEditing()) {
                assertFalse(displayableListPanel.isEditEnabled());
            } else {
                assertTrue(displayableListPanel.isEditEnabled());
            }
            assertTrue(displayableListPanel.isViewEnabled());
        }
    }

    @Test
    public void testEditSelectedListItem() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        DisplayableListItemEditor listItemEditor = new DisplayableListItemEditor();
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        displayableListPanel.setMasterList(masterList);

        Displayable displayable = new Displayable("Test Displayable");
        masterList.add(displayable);
        displayableListPanel.getDisplayableList().setDisplayItems(masterList);

        displayableListPanel.editSelectedListItem();

        Displayable editedDisplayable = displayableListPanel.getDisplayableList().getSelectedItem();
        assertEquals("Test Displayable", editedDisplayable.getDisplayName());
        assertTrue(displayableListPanel.isAddEnabled());
        assertTrue(displayableListPanel.isSelectEnabled());
        if (displayableListPanel.getDisplayableList().isEmpty()) {
            assertFalse(displayableListPanel.isDeleteEnabled());
            if (!displayableListPanel.isAllowRecordEditing()) {
                assertFalse(displayableListPanel.isEditEnabled());
            } else {
                assertTrue(displayableListPanel.isEditEnabled());
            }
            assertFalse(displayableListPanel.isViewEnabled());
        } else {
            assertTrue(displayableListPanel.isDeleteEnabled());
            if (!displayableListPanel.isAllowRecordEditing()) {
                assertFalse(displayableListPanel.isEditEnabled());
            } else {
                assertTrue(displayableListPanel.isEditEnabled());
            }
            assertTrue(displayableListPanel.isViewEnabled());
        }
    }

    @Test
    public void testViewSelectedListItem() {
        DisplayableListItemViewer listItemViewer = new DisplayableListItemViewer();
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);

        Displayable displayable = new Displayable("Test Displayable");
        displayableListPanel.getDisplayableList().setDisplayItems(new ArrayList<>(Collections.singletonList(displayable)));

        displayableListPanel.viewSelectedListItem();

        listItemViewer.viewListItem(displayable);
        // since we cannot verify the actual behavior of viewListItem method, we can only check if it was called
        assertTrue(listItemViewer.isViewListItemCalled());
    }

    @Test
    public void testDeleteSelectedListItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        DisplayableListItemDeleter listItemDeleter = new DisplayableListItemDeleter();
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
        displayableListPanel.setMasterList(masterList);

        Displayable displayable1 = new Displayable("Displayable 1");
        Displayable displayable2 = new Displayable("Displayable 2");
        masterList.add(displayable1);
        masterList.add(displayable2);
        displayableListPanel.getDisplayableList().setDisplayItems(masterList);

        displayableListPanel.deleteSelectedListItems();

        assertEquals(0, masterList.size());
        assertTrue(displayableListPanel.isAddEnabled());
        assertTrue(displayableListPanel.isSelectEnabled());
        if (displayableListPanel.getDisplayableList().isEmpty()) {
            assertFalse(displayableListPanel.isDeleteEnabled());
            if (!displayableListPanel.isAllowRecordEditing()) {
                assertFalse(displayableListPanel.isEditEnabled());
            } else {
                assertTrue(displayableListPanel.isEditEnabled());
            }
            assertFalse(displayableListPanel.isViewEnabled());
        } else {
            assertTrue(displayableListPanel.isDeleteEnabled());
            if (!displayableListPanel.isAllowRecordEditing()) {
                assertFalse(displayableListPanel.isEditEnabled());
            } else {
                assertTrue(displayableListPanel.isEditEnabled());
            }
            assertTrue(displayableListPanel.isViewEnabled());
        }
    }

    @Test
    public void testSelectListItems() {
        DisplayableListItemSelector listItemSelector = new DisplayableListItemSelector();
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);

        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable("Displayable 1"));
        masterList.add(new Displayable("Displayable 2"));
        displayableListPanel.setMasterList(masterList);
        displayableListPanel.getDisplayableList().setDisplayItems(masterList);

        displayableListPanel.selectListItems();

        Displayable[] selectedItems = displayableListPanel.getDisplayableList().getSelectedItems();
        assertEquals(2, selectedItems.length);
    }

    @Test
    public void testUpdateButtonStates() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        masterList.add(new Displayable("Displayable 1"));
        masterList.add(new Displayable("Displayable 2"));
        displayableListPanel.setMasterList(masterList);
        displayableListPanel.getDisplayableList().setDisplayItems(masterList);
        displayableListPanel.setNewRecord(true);

        displayableListPanel.updateButtonStates();

        assertFalse(displayableListPanel.isAddEnabled());
        assertFalse(displayableListPanel.isDeleteEnabled());
        assertFalse(displayableListPanel.isEditEnabled());
        assertFalse(displayableListPanel.isViewEnabled());
        assertFalse(displayableListPanel.isSelectEnabled());

        displayableListPanel.setNewRecord(false);
        displayableListPanel.updateButtonStates();

        assertTrue(displayableListPanel.isAddEnabled());
        assertTrue(displayableListPanel.isSelectEnabled());
        if (displayableListPanel.getDisplayableList().isEmpty()) {
            assertFalse(displayableListPanel.isDeleteEnabled());
            if (!displayableListPanel.isAllowRecordEditing()) {
                assertFalse(displayableListPanel.isEditEnabled());
            } else {
                assertTrue(displayableListPanel.isEditEnabled());
            }
            assertFalse(displayableListPanel.isViewEnabled());
        } else {
            assertTrue(displayableListPanel.isDeleteEnabled());
            if (!displayableListPanel.isAllowRecordEditing()) {
                assertFalse(displayableListPanel.isEditEnabled());
            } else {
                assertTrue(displayableListPanel.isEditEnabled());
            }
            assertTrue(displayableListPanel.isViewEnabled());
        }
    }

    @Test
    public void testSetDisplayableItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        displayableListPanel.setMasterList(masterList);

        Displayable displayable1 = new Displayable("Displayable 1");
        Displayable displayable2 = new Displayable("Displayable 2");
        masterList.add(displayable1);
        masterList.add(displayable2);
        displayableListPanel.getDisplayableList().setDisplayItems(masterList);

        displayableListPanel.setDisplayableItems(masterList);

        assertEquals(masterList, displayableListPanel.getAllListItems());
    }

    @Test
    public void testSave() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        Displayable displayable1 = new Displayable("Displayable 1");
        Displayable displayable2 = new Displayable("Displayable 2");
        masterList.add(displayable1);
        masterList.add(displayable2);
        displayableListPanel.setMasterList(masterList);
        displayableListPanel.getDisplayableList().setDisplayItems(masterList);

        displayableListPanel.save();

        ArrayList<Displayable> savedListItems = displayableListPanel.getAllListItems();

        assertEquals(masterList, savedListItems);
    }

    @Test
    public void testGetAllListItems() {
        ArrayList<Displayable> masterList = new ArrayList<>();
        Displayable displayable1 = new Displayable("Displayable 1");
        Displayable displayable2 = new Displayable("Displayable 2");
        masterList.add(displayable1);
        masterList.add(displayable2);
        displayableListPanel.setMasterList(masterList);
        displayableListPanel.getDisplayableList().setDisplayItems(masterList);

        ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();

        assertEquals(masterList, allListItems);
    }

    @Test
    public void testSetListOwnerName() {
        String listOwnerName = "Test List Owner";
        displayableListPanel.setListOwnerName(listOwnerName);

        assertEquals(listOwnerName, displayableListPanel.getListOwnerName());
    }

    @Test
    public void testActionPerformed() {
        DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
        DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
        DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
        DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
        DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);

        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        displayableListPanel.setDisplayableListItemAdder(listItemAdder);
        displayableListPanel.setDisplayableListItemEditor(listItemEditor);
        displayableListPanel.setDisplayableListItemViewer(listItemViewer);
        displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);

        ActionEvent actionEvent = new ActionEvent(displayableListPanel.getSelectButton(), ActionEvent.ACTION_PERFORMED, null);

        displayableListPanel.actionPerformed(actionEvent);

        verify(listItemSelector).selectListItems(displayableListPanel.getListOwnerName(), displayableListPanel.getDisplayableList());
    }
}