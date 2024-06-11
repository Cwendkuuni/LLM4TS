import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.i18n.Internationalizable;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
    }

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        String encodedPassword = passwordEncoder.encode("password");
        assertNotNull(encodedPassword);
    }

    @Test
    public void testObjectMapper() {
        ObjectMapper mapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(mapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall httpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(httpFirewall);
        assertEquals(true, httpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);

        when(cloudMetadataClient.supportsCloudMetadata()).thenReturn(true);
        when(cloudMetadataClient.supportsTenantInstance()).thenReturn(true);
        when(cloudMetadataClient.needsOBTenantName()).thenReturn(true);
        when(cloudMetadataClient.needsSysTenantUser()).thenReturn(true);
        when(cloudMetadataClient.includeClusterNameForJdbcUsername()).thenReturn(true);
        when(cloudMetadataClient.oceanbaseAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);

        List<OBInstance> instances = Arrays.asList(new OBInstance(), new OBInstance());
        when(cloudMetadataClient.listInstances()).thenReturn(instances);

        List<OBTenant> tenants = Arrays.asList(new OBTenant(), new OBTenant());
        when(cloudMetadataClient.listTenants(anyString())).thenReturn(tenants);

        OBTenant tenant = new OBTenant();
        when(cloudMetadataClient.getTenant(anyString(), anyString())).thenReturn(tenant);

        List<OBDatabaseUser> databaseUsers = Arrays.asList(new OBDatabaseUser(), new OBDatabaseUser());
        when(cloudMetadataClient.listDatabaseUsers(anyString(), anyString())).thenReturn(databaseUsers);

        OBTenantEndpoint tenantEndpoint = new OBTenantEndpoint();
        when(cloudMetadataClient.getTenantEndpoint(anyString(), anyString())).thenReturn(tenantEndpoint);

        OBDatabaseUser sysTenantUser = new OBDatabaseUser();
        when(cloudMetadataClient.getSysTenantUser(anyString())).thenReturn(sysTenantUser);

        assertNotNull(cloudMetadataClient.supportsCloudMetadata());
        assertNotNull(cloudMetadataClient.supportsTenantInstance());
        assertNotNull(cloudMetadataClient.needsOBTenantName());
        assertNotNull(cloudMetadataClient.needsSysTenantUser());
        assertNotNull(cloudMetadataClient.includeClusterNameForJdbcUsername());
        assertNotNull(cloudMetadataClient.oceanbaseAccessMode());
        assertNotNull(cloudMetadataClient.listInstances());
        assertNotNull(cloudMetadataClient.listTenants(anyString()));
        assertNotNull(cloudMetadataClient.getTenant(anyString(), anyString()));
        assertNotNull(cloudMetadataClient.listDatabaseUsers(anyString(), anyString()));
        assertNotNull(cloudMetadataClient.getTenantEndpoint(anyString(), anyString()));
        assertNotNull(cloudMetadataClient.getSysTenantUser(anyString()));
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);

        assertFalse(nullCloudMetadataClient.supportsCloudMetadata());
        assertFalse(nullCloudMetadataClient.supportsTenantInstance());
        assertFalse(nullCloudMetadataClient.needsOBTenantName());
        assertFalse(nullCloudMetadataClient.needsSysTenantUser());
        assertTrue(nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listInstances());
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listTenants(anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenant(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.listDatabaseUsers(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getTenantEndpoint(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> nullCloudMetadataClient.getSysTenantUser(anyString()));
    }
}