import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.mockito.Mockito;

public class FinalMockCandidateFilterTest {

  @Test
  public void testFilterCandidate_OneMock_Success() {
    FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
    Object mock = Mockito.mock(Object.class);
    Object fieldInstance = new Object();
    Field field = Mockito.mock(Field.class);
    Collection<Object> mocks = Arrays.asList(mock);

    OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
    assertTrue(injecter.thenInject());

    verify(mock).equals(fieldInstance);
  }
}