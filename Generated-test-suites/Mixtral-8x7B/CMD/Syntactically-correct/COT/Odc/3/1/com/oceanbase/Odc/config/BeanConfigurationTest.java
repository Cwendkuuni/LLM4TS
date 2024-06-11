import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
import com.oceanbase.odc.common.json.JacksonFactory;
import com.oceanbase.odc.common.json.JacksonModules;
import com.oceanbase.odc.common.json.JacksonModules.CustomOutputSerializer;
import com.oceanbase.odc.core.shared.exception.UnsupportedException;
import com.oceanbase.odc.service.connection.CloudMetadataClient;
import com.oceanbase.odc.service.connection.model.OBDatabaseUser;
import com.oceanbase.odc.service.connection.model.OBInstance;
import com.oceanbase.odc.service.connection.model.OBTenant;
import com.oceanbase.odc.service.connection.model.OBTenantEndpoint;
import com.oceanbase.odc.service.connection.model.OceanBaseAccessMode;
import com.oceanbase.odc.service.encryption.SensitivePropertyHandler;

@RunWith(MockitoJUnitRunner.class)
public class BeanConfigurationTest {

    @Mock
    private SensitivePropertyHandler sensitivePropertyHandler;

    @InjectMocks
    private BeanConfiguration beanConfiguration;

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = beanConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder);
        assertEquals(BCryptPasswordEncoder.class, passwordEncoder.getClass());
    }

    @Test
    public void testObjectMapper() throws Exception {
        ObjectMapper objectMapper = beanConfiguration.objectMapper(sensitivePropertyHandler);
        assertNotNull(objectMapper);

        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());

        CustomOutputSerializer actualSerializer = JacksonFactory.unsafeJsonMapper()
                .readerFor(CustomOutputSerializer.class).readValue(objectMapper.writeValueAsString(customOutputSerializer));

        assertEquals(customOutputSerializer.getSerializers().size(), actualSerializer.getSerializers().size());
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertEquals(DefaultHttpFirewall.class, defaultHttpFirewall.getClass());
        assertEquals(true, defaultHttpFirewall.getAllowUrlEncodedSlash());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());

        when(sensitivePropertyHandler.decrypt(anyString())).thenReturn("decrypted");
        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(new CustomOutputSerializer()));
        when(objectMapper.writeValueAsString(any(CustomOutputSerializer.class))).thenReturn("serialized");

        NullCloudMetadataClient nullCloudMetadataClient = (NullCloudMetadataClient) cloudMetadataClient;
        assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
        assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
        assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
        assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
        assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        List<OBInstance> instances = new ArrayList<>();
        instances.add(mock(OBInstance.class));
        when(instances.get(0).getId()).thenReturn("instanceId");
        when(instances.get(0).getName()).thenReturn("instanceName");

        when(mock(OBInstance.class).getId()).thenReturn("instanceId");
        when(mock(OBInstance.class).getName()).thenReturn("instanceName");

        when(cloudMetadataClient.listInstances()).thenReturn(instances);

        List<OBTenant> tenants = new ArrayList<>();
        OBTenant tenant = mock(OBTenant.class);
        when(tenant.getId()).thenReturn("tenantId");
        when(tenant.getName()).thenReturn("tenantName");
        tenants.add(tenant);

        when(tenant.getId()).thenReturn("tenantId");
        when(tenant.getName()).thenReturn("tenantName");

        when(cloudMetadataClient.listTenants(anyString())).thenReturn(tenants);

        when(cloudMetadataClient.getTenant(anyString(), anyString())).thenReturn(tenant);

        List<OBDatabaseUser> databaseUsers = new ArrayList<>();
        OBDatabaseUser databaseUser = mock(OBDatabaseUser.class);
        when(databaseUser.getId()).thenReturn("databaseUserId");
        when(databaseUser.getName()).thenReturn("databaseUserName");
        databaseUsers.add(databaseUser);

        when(databaseUser.getId()).thenReturn("databaseUserId");
        when(databaseUser.getName()).thenReturn("databaseUserName");

        when(cloudMetadataClient.listDatabaseUsers(anyString(), anyString())).thenReturn(databaseUsers);

        OBTenantEndpoint endpoint = mock(OBTenantEndpoint.class);
        when(endpoint.getAccessMode()).thenReturn(OceanBaseAccessMode.DIRECT);
        when(cloudMetadataClient.getTenantEndpoint(anyString(), anyString())).thenReturn(endpoint);

        OBDatabaseUser sysTenantUser = mock(OBDatabaseUser.class);
        when(sysTenantUser.getId()).thenReturn("sysTenantUserId");
        when(sysTenantUser.getName()).thenReturn("sysTenantUserName");
        when(cloudMetadataClient.getSysTenantUser(anyString())).thenReturn(sysTenantUser);

        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listInstances());
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listTenants(anyString()));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenant(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.listDatabaseUsers(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getTenantEndpoint(anyString(), anyString()));
        assertThrows(UnsupportedException.class, () -> cloudMetadataClient.getSysTenantUser(anyString()));
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals(DefaultCookieSerializer.class, cookieSerializer.getClass());
        assertEquals("JSESSIONID", cookieSerializer.getCookieName());
    }
}