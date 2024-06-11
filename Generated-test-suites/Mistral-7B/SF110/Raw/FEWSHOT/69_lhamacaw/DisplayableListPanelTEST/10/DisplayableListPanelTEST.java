package macaw.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import macaw.businessLayer.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;
 private DisplayableList displayableList;
 private JButton select;
 private JButton add;
 private JButton edit;
 private JButton view;
 private JButton delete;
 private ArrayList<Displayable> masterList;
 private String panelTitleText;
 private String currentUserID;
 private boolean allowWriteAccess;
 private boolean allowRecordEditing;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 log = Mockito.mock(Log.class);
 displayableList = Mockito.mock(DisplayableList.class);
 select = Mockito.mock(JButton.class);
 add = Mockito.mock(JButton.class);
 edit = Mockito.mock(JButton.class);
 view = Mockito.mock(JButton.class);
 delete = Mockito.mock(JButton.class);
 masterList = new ArrayList<>();
 panelTitleText = "Test Panel Title";
 currentUserID = "TestUserID";
 allowWriteAccess = true;
 allowRecordEditing = true;

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);

 assertEquals(sessionProperties, displayableListPanel.sessionProperties);
 assertEquals(parentForm, displayableListPanel.parentForm);
 assertEquals(panelTitleText, displayableListPanel.panelTitleText);
 assertEquals(allowWriteAccess, displayableListPanel.allowWriteAccess);
 assertEquals(allowRecordEditing, displayableListPanel.allowRecordEditing);
 assertEquals(userInterfaceFactory, displayableListPanel.userInterfaceFactory);
 assertEquals(currentUserID, displayableListPanel.currentUserID);
 }

 @Test
 public void testGetPanel() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.buildUI();

 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.setNewRecord(true);

 assertTrue(displayableListPanel.isNewRecord);
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.displayableList = Mockito.spy(displayableList);
 Mockito.doReturn(true).when(displayableList).itemsAreSelected();
 displayableListPanel.listItemSelector = Mockito.mock(DisplayableListItemSelector.class);

 displayableListPanel.selectListItems();

 assertTrue(displayableListPanel.isNewRecord);
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.displayableList = Mockito.spy(displayableList);
 displayableListPanel.listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 Mockito.doThrow(new MacawException(MacawErrorType.MISSING_DISPLAY_LIST_ADDER, "Test Error")).when(displayableListPanel.listItemAdder).addItems(displayableListPanel.listOwnerName, displayableListPanel.displayableList);
 displayableListPanel.parentForm = Mockito.mock(DisplayableListParentForm.class);

 displayableListPanel.addListItem();

 assertTrue(displayableListPanel.isNewRecord);
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testEditSelectedListItem() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.displayableList = Mockito.spy(displayableList);
 displayableListPanel.listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 Mockito.doReturn(true).when(displayableList).itemsAreSelected();
 Mockito.doReturn(Mockito.mock(Displayable.class)).when(displayableList).getSelectedItem();
 displayableListPanel.parentForm = Mockito.mock(DisplayableListParentForm.class);

 displayableListPanel.editSelectedListItem();

 assertFalse(displayableListPanel.isNewRecord);
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testViewSelectedListItem() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.displayableList = Mockito.spy(displayableList);
 displayableListPanel.listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 Mockito.doReturn(true).when(displayableList).itemsAreSelected();
 Mockito.doReturn(Mockito.mock(Displayable.class)).when(displayableList).getSelectedItem();
 displayableListPanel.parentForm = Mockito.mock(DisplayableListParentForm.class);

 displayableListPanel.viewSelectedListItem();

 assertFalse(displayableListPanel.isNewRecord);
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.displayableList = Mockito.spy(displayableList);
 displayableListPanel.listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 Mockito.doReturn(true).when(displayableList).itemsAreSelected();
 displayableListPanel.parentForm = Mockito.mock(DisplayableListParentForm.class);
 Mockito.doReturn(JOptionPane.YES_OPTION).when(Mockito.mock(JOptionPane.class)).showConfirmDialog(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());

 displayableListPanel.deleteSelectedListItems();

 assertFalse(displayableListPanel.isNewRecord);
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.displayableList = Mockito.spy(displayableList);
 displayableListPanel.masterList = masterList;
 displayableListPanel.displayableList.setDisplayItems(masterList);
 displayableListPanel.updateButtonStates();

 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.displayableList = Mockito.spy(displayableList);
 displayableListPanel.masterList = masterList;
 displayableListPanel.save();

 assertEquals(displayableListPanel.displayableList.getAllItems(), masterList);
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.init(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.displayableList = Mockito.spy(displayableList);
 displayableListPanel.isNewRecord = true;
 displayableListPanel.add = Mockito.mock(JButton.class);
 displayableListPanel.delete = Mockito.mock(JButton.class);
 displayableListPanel.edit = Mockito.mock(JButton.class);
 displayableListPanel.view = Mockito.mock(JButton.class);
 displayableListPanel.select = Mockito.mock(JButton.class);
 Mockito.doReturn(true).when(displayableList).isEmpty();
 Mockito.doReturn(true).when(displayableList).itemsAreSelected();

 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.isNewRecord = false;
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.displayableList.clear();
 displayableListPanel.updateButtonStates();

 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }
}