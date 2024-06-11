package osa.ora.server.admin.ui;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;

public class UpdateUserPanelTest {
    private AdminApp adminApp;
    private ControlPanel controlPanel;
    private UpdateUserPanel updateUserPanel;
    private Group mockGroup;
    private User mockUser, mockUser2, mockUser3;

    @Before
    public void setUp() {
        adminApp = mock(AdminApp.class);
        controlPanel = mock(ControlPanel.class);
        mockGroup = mock(Group.class);
        mockUser = mock(User.class);
        mockUser2 = mock(User.class);
        mockUser3 = mock(User.class);

        when(adminApp.getGroups()).thenReturn(new ArrayList<>(Arrays.asList(mockGroup)));
        when(mockGroup.getUsers()).thenReturn(new Vector<>(Arrays.asList(mockUser, mockUser2, mockUser3)));
        when(mockUser.getId()).thenReturn(1);
        when(mockUser.getName()).thenReturn("John Doe");
        when(mockUser.getEmail()).thenReturn("johndoe@example.com");
        when(mockUser.getDirectPhone()).thenReturn("555-555-5555");
        when(mockUser.getJobTitle()).thenReturn("Software Engineer");
        when(mockUser.isShowMyStatus()).thenReturn(true);
        when(mockUser.isOnlyStartChat()).thenReturn(false);
        when(mockUser.isCanJoinChatRoom()).thenReturn(true);
        when(mockUser2.getId()).thenReturn(2);
        when(mockUser2.getName()).thenReturn("Jane Doe");
        when(mockUser2.getEmail()).thenReturn("janedoe@example.com");
        when(mockUser2.getDirectPhone()).thenReturn("555-555-5556");
        when(mockUser2.getJobTitle()).thenReturn("Product Manager");
        when(mockUser3.getId()).thenReturn(3);
        when(mockUser3.getName()).thenReturn("Bob Smith");
        when(mockUser3.getEmail()).thenReturn("bobsmith@example.com");
        when(mockUser3.getDirectPhone()).thenReturn("555-555-5557");
        when(mockUser3.getJobTitle()).thenReturn("Designer");

        updateUserPanel = new UpdateUserPanel(adminApp, controlPanel);
    }

    // Test cases go here
}