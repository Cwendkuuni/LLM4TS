import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import org.springframework.session.web.http.CookieSerializer;

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

        assertEquals(JacksonFactory.unsafeJsonMapper().getClass(), objectMapper.getClass());
        assertEquals(
                JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt).getClass(),
                objectMapper.getSerializationConfig().getModule(JacksonModules.SensitiveInputHandling.class)
                        .getClass());
        assertEquals(customOutputSerializer.getClass(),
                objectMapper.getSerializationConfig().getModule(CustomOutputSerializer.class).getClass());
    }

    @Test
    public void testDefaultHttpFirewall() {
        HttpFirewall defaultHttpFirewall = beanConfiguration.defaultHttpFirewall();
        assertNotNull(defaultHttpFirewall);
        assertEquals(DefaultHttpFirewall.class, defaultHttpFirewall.getClass());
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = beanConfiguration.cloudMetadataClient();
        assertNotNull(cloudMetadataClient);
        assertEquals(NullCloudMetadataClient.class, cloudMetadataClient.getClass());
    }

    @Test
    public void testCookieSerializer() {
        CookieSerializer cookieSerializer = beanConfiguration.cookieSerializer();
        assertNotNull(cookieSerializer);
        assertEquals(DefaultCookieSerializer.class, cookieSerializer.getClass());
    }

    @Test
    public void testNullCloudMetadataClient() {
        NullCloudMetadataClient nullCloudMetadataClient = beanConfiguration.new NullCloudMetadataClient();
        assertNotNull(nullCloudMetadataClient);

        when(sensitivePropertyHandler.decrypt("test")).thenReturn("decryptedTest");

        assertEquals(false, nullCloudMetadataClient.supportsCloudMetadata());
        assertEquals(false, nullCloudMetadataClient.supportsTenantInstance());
        assertEquals(false, nullCloudMetadataClient.needsOBTenantName());
        assertEquals(false, nullCloudMetadataClient.needsSysTenantUser());
        assertEquals(true, nullCloudMetadataClient.includeClusterNameForJdbcUsername());
        assertEquals(OceanBaseAccessMode.DIRECT, nullCloudMetadataClient.oceanbaseAccessMode());

        List<OBInstance> obInstances = new ArrayList<>();
        OBInstance obInstance = new OBInstance();
        obInstance.setId("testId");
        obInstances.add(obInstance);
        when(nullCloudMetadataClient.listInstances()).thenReturn(obInstances);

        List<OBTenant> obTenants = new ArrayList<>();
        OBTenant obTenant = new OBTenant();
        obTenant.setId("testTenantId");
        obTenants.add(obTenant);
        when(nullCloudMetadataClient.listTenants("testInstanceId")).thenReturn(obTenants);

        when(nullCloudMetadataClient.getTenant("testInstanceId", "testTenantId"))
                .thenReturn(obTenant);

        List<OBDatabaseUser> obDatabaseUsers = new ArrayList<>();
        OBDatabaseUser obDatabaseUser = new OBDatabaseUser();
        obDatabaseUser.setId("testDbUserId");
        obDatabaseUsers.add(obDatabaseUser);
        when(nullCloudMetadataClient.listDatabaseUsers("testInstanceId", "testTenantId"))
                .thenReturn(obDatabaseUsers);

        OBTenantEndpoint obTenantEndpoint = new OBTenantEndpoint();
        obTenantEndpoint.setAccessMode(OceanBaseAccessMode.DIRECT);
        when(nullCloudMetadataClient.getTenantEndpoint("testInstanceId", "testTenantId"))
                .thenReturn(obTenantEndpoint);

        when(nullCloudMetadataClient.getSysTenantUser("testInstanceId")).thenReturn(obDatabaseUser);

        assertEquals(obInstances, nullCloudMetadataClient.listInstances());
        assertEquals(obTenants, nullCloudMetadataClient.listTenants("testInstanceId"));
        assertEquals(obTenant, nullCloudMetadataClient.getTenant("testInstanceId", "testTenantId"));
        assertEquals(obDatabaseUsers,
                nullCloudMetadataClient.listDatabaseUsers("testInstanceId", "testTenantId"));
        assertEquals(obTenantEndpoint,
                nullCloudMetadataClient.getTenantEndpoint("testInstanceId", "testTenantId"));
        assertEquals(obDatabaseUser, nullCloudMetadataClient.getSysTenantUser("testInstanceId"));
    }

}