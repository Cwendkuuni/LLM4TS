package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ByteBuddyMockMakerTest {
    @Test
    public void testCreateMock() {
        // given
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings<Object>() {};

        // when
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(settings, handler);

        // then
        assertNotNull(mock);
    }
}