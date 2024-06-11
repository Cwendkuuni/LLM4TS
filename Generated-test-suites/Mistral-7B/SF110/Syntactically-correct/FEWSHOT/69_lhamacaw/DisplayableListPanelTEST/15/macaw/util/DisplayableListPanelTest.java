package macaw.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class DisplayableListPanelTest {

    @Mock
    private SessionProperties sessionProperties;
    @Mock
    private DisplayableListParentForm parentForm;
    @Mock
    private UserInterfaceFactory userInterfaceFactory;
    @Mock
    private Log log;
    @Mock
    private DisplayableListItemSelector listItemSelector;
    @Mock
    private DisplayableListItemAdder listItemAdder;
    @Mock
    private DisplayableListItemEditor listItemEditor;
    @Mock
    private DisplayableListItemViewer listItemViewer;
    @Mock
    private DisplayableListItemDeleter listItemDeleter;

    @InjectMocks
    private DisplayableListPanel displayableListPanel;

    @Test
    public void testInit() {
        displayableListPanel.init(sessionProperties, parentForm, "Displayable List Panel", true, true);

        Mockito.verify(parentForm, Mockito.times(1)).commitChanges();
        Mockito.verify(sessionProperties, Mockito.times(1)).getLog();
        Mockito.verify(userInterfaceFactory, Mockito.times(5)).createButton(Mockito.anyString());
        Mockito.verify(userInterfaceFactory, Mockito.times(1)).createPanel();
    }

    @Test
    public void testInitWithoutRecordEditing() {
        displayableListPanel.init(sessionProperties, parentForm, "Displayable List Panel", true, false);

        Mockito.verify(parentForm, Mockito.times(1)).commitChanges();
        Mockito.verify(sessionProperties, Mockito.times(1)).getLog();
        Mockito.verify(userInterfaceFactory, Mockito.times(5)).createButton(Mockito.anyString());
        Mockito.verify(userInterfaceFactory, Mockito.times(1)).createPanel();
        Mockito.verify(listItemViewer, Mockito.never()).viewListItem(Mockito.any());
    }

    @Test
    public void testBuildUI() {
        displayableListPanel.buildUI();

        Mockito.verify(userInterfaceFactory, Mockito.times(1)).createLabel(Mockito.anyString());
        Mockito.verify(userInterfaceFactory, Mockito.times(1)).createPanel();
        Mockito.verify(userInterfaceFactory, Mockito.times(1)).createGridBagConstraints();
        Mockito.verify(panel, Mockito.times(1)).add(Mockito.anyComponent(), Mockito.any());
        Mockito.verify(panel, Mockito.times(2)).add(Mockito.anyComponent(), Mockito.any());
        Mockito.verify(userInterfaceFactory, Mockito.times(1)).getListScrollPane();
        Mockito.verify(panel, Mockito.times(1)).add(Mockito.anyComponent(), Mockito.any());
        Mockito.verify(userInterfaceFactory, Mockito.times(1)).createGridBagConstraints();
    }

    @Test
    public void testCreateControlButtonPanel() {
        displayableListPanel.createControlButtonPanel();

        Mockito.verify(userInterfaceFactory, Mockito.times(1)).createPanel();
        Mockito.verify(userInterfaceFactory, Mockito.times(1)).createGridBagConstraints();
        Mockito.verify(panel, Mockito.times(7)).add(Mockito.anyComponent(), Mockito.any());
    }

    @Test
    public void testActionPerformedSelect() {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        displayableListPanel.setDisplayableListItemSelector(listItemSelector);
        displayableListPanel.actionPerformed(actionEvent);

        Mockito.verify(listItemSelector, Mockito.times(1)).selectListItems(Mockito.eq("Displayable List Panel"), Mockito.any());
    }

    @Test
    public void testActionPerformedAdd() {
        ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
        displayableListPanel.addListItem();

        Mockito.verify(parentForm, Mockito.times(1)).commitChanges();
        Mockito.verify(listItemAdder, Mockito.times(1)).addItems(Mockito.eq("Displayable List Panel"), Mockito.any());
    }

    @Test(expected = MacawException.class)
    public void testAddListItemMissingAdder() {
        displayableListPanel.addListItem();

        Mockito.verify(parentForm, Mockito.times(1)).commitChanges();
        Mockito.verify(listItemAdder, Mockito.never()).addItems(Mockito.anyString(), Mockito.any());
    }
}