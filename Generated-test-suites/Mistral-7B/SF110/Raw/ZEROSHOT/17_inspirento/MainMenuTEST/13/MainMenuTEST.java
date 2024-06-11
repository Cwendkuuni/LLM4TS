package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.*;
import java.util.*;

public class MainMenuTest {

    private InspirentoMediator mediatorMock;
    private MainMenu mainMenu;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mediatorMock = mock(InspirentoMediator.class);
        mainMenu = new MainMenu(mediatorMock);
    }

    @Test
    public void testMainMenuCreation() {
        String[] menus = InspirentoUtilities.tokenize(UIResources.getString("menu"));
        assertEquals(menus.length, mainMenu.getMenuCount());

        for (String menu : menus) {
            JMenu menuComponent = mainMenu.getComponent(0);
            assertEquals(menu, menuComponent.getText());
            int numOfSubMenusAndItems = countMenuComponents(menuComponent);
            if (menu.contains("-")) {
                assertEquals(0, numOfSubMenusAndItems);
            } else {
                assertEquals(numOfSubMenusAndItems > 0, menuComponent.getComponentCount() > 0);
            }
        }
    }

    @Test
    public void testCreateMenu() {
        String menu = "File";
        JMenu expectedMenu = new JMenu(menu);
        expectedMenu.setMnemonic(KeyEvent.VK_F);

        JMenu menuComponent = mainMenu.createMenu(menu);
        assertEquals(expectedMenu.getText(), menuComponent.getText());
        assertEquals(expectedMenu.getMnemonic(), menuComponent.getMnemonic());

        String[] items = InspirentoUtilities.tokenize(UIResources.getString(menu));
        for (String item : items) {
            if (!item.equals("-")) {
                JMenuItem menuItem = null;
                if (item.startsWith("#")) {
                    String key = item.substring(1);
                    String expectedItemLabel = UIResources.getString(key + "Label");
                    JCheckBoxMenuItem jMenuItem = (JCheckBoxMenuItem) menuComponent.getComponent(0);
                    assertEquals(expectedItemLabel.substring(0, expectedItemLabel.length() - 2), jMenuItem.getText());
                    assertEquals(KeyEvent.VK_F, jMenuItem.getMnemonic());
                } else {
                    String expectedItemLabel = UIResources.getString(item + "Label");
                    JMenuItem menuItem = (JMenuItem) menuComponent.getComponent(0);
                    assertEquals(expectedItemLabel.substring(0, expectedItemLabel.length() - 2), menuItem.getText());
                    assertEquals(KeyEvent.VK_F, menuItem.getMnemonic());
                }
            }
        }
    }

    @Test
    public void testAddShortcutAndIcon() {
        String key = "File.Open";
        String shortcut = "Ctrl+O";
        String iconPath = "icon.png";

        JMenuItem menuItem = mock(JMenuItem.class);

        mainMenu.addShortcutAndIcon(menuItem, key);

        verify(menuItem).setAccelerator(eq(KeyStroke.getKeyStroke(shortcut)));

        verify(menuItem, never()).setIcon(any(Icon.class));

        try {
            URL iconUrl = mainMenu.getClass().getResource(iconPath);
            assertTrue(iconUrl != null);
        } catch (NullPointerException e) {
            fail("Icon file is missing.");
        }
    }

    private int countMenuComponents(Component component) {
        int count = 0;
        if (component instanceof JMenu) {
            JMenu menu = (JMenu) component;
            for (int i = 0; i < menu.getComponentCount(); i++) {
                Component subComponent = menu.getComponent(i);
                count += subComponent instanceof JMenuItem ? 1 : countMenuComponents(subComponent) + 1;
            }
        }
        return count;
    }
}