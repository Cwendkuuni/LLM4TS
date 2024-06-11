import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.common.json.JsonUtils;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.session.ConnectionSessionFactory;
import com.oceanbase.odc.core.shared.PreConditions;
import com.oceanbase.odc.metadb.connection.DatabaseEntity;
import com.oceanbase.odc.metadb.connection.DatabaseRepository;
import com.oceanbase.odc.service.onlineschemachange.model.OscLockDatabaseUserInfo;
import com.oceanbase.odc.service.onlineschemachange.OscService;

@RunWith(MockitoJUnitRunner.class)
public class OscServiceTest {

    @InjectMocks
    private OscService oscService;

    @Mock
    private DatabaseRepository databaseRepository;

    @Mock
    private ConnectionService connectionService;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(oscService, "skipAuthorize", new SkipAuthorize() {});
    }

    @Test
    public void testGetOscDatabaseInfo_whenDatabaseDoesNotExist_returnsEmptyOscLockDatabaseUserInfo() {
        Long id = 1L;
        Mockito.when(databaseRepository.findById(id)).thenReturn(Optional.empty());

        OscLockDatabaseUserInfo oscDatabase = oscService.getOscDatabaseInfo(id);

        assertNull(oscDatabase.getDatabaseId());
        assertFalse(oscDatabase.isLockDatabaseUserRequired());
    }

    // ... more test cases
}