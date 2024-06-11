import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;

public class NameBasedCandidateFilterTest {

  @Test
  public void testNameBasedCandidateFilterConstructor() {
    MockCandidateFilter mockCandidateFilter = new MockCandidateFilter() {
    };
    NameBasedCandidateFilter filter = new NameBasedCandidateFilter(mockCandidateFilter);
  }
}