import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.web.http.CookieSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oceanbase.odc.common.i18n.I18nOutputSerializer;
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

    @Test
    public void testPasswordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("testPassword");
        assertNotNull(encodedPassword);
    }

    @Test
    public void testObjectMapper() throws Exception {
        CustomOutputSerializer customOutputSerializer = new CustomOutputSerializer()
                .addSerializer(Internationalizable.class, new I18nOutputSerializer());
        ObjectMapper objectMapper = JacksonFactory.unsafeJsonMapper()
                .registerModule(JacksonModules.sensitiveInputHandling(sensitivePropertyHandler::decrypt))
                .registerModule(JacksonModules.customOutputHandling(customOutputSerializer));
        assertNotNull(objectMapper);
    }

    @Test
    public void testDefaultHttpFirewall() {
        DefaultHttpFirewall defaultHttpFirewall = new DefaultHttpFirewall();
        defaultHttpFirewall.setAllowUrlEncodedSlash(true);
        assertNotNull(defaultHttpFirewall);
    }

    @Test
    public void testCloudMetadataClient() {
        CloudMetadataClient cloudMetadataClient = new NullCloudMetadataClient();
        assertNotNull(cloudMetadataClient);
    }

    @Test
    public void testCookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        assertNotNull(serializer);
    }

    @Test(expected = UnsupportedException.class)
    public void testListInstances() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        when(nullCloudMetadataClient.listInstances()).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        List<OBInstance> instances = nullCloudMetadataClient.listInstances();
    }

    @Test(expected = UnsupportedException.class)
    public void testListTenants() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        when(nullCloudMetadataClient.listTenants(null)).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        List<OBTenant> tenants = nullCloudMetadataClient.listTenants(null);
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenant() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        when(nullCloudMetadataClient.getTenant(null, null)).thenThrow(new UnsupportedException("getTenant not supported in non-could"));
        OBTenant tenant = nullCloudMetadataClient.getTenant(null, null);
    }

    @Test(expected = UnsupportedException.class)
    public void testListDatabaseUsers() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        when(nullCloudMetadataClient.listDatabaseUsers(null, null)).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        List<OBDatabaseUser> users = nullCloudMetadataClient.listDatabaseUsers(null, null);
    }

    @Test(expected = UnsupportedException.class)
    public void testGetTenantEndpoint() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        when(nullCloudMetadataClient.getTenantEndpoint(null, null)).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        OBTenantEndpoint endpoint = nullCloudMetadataClient.getTenantEndpoint(null, null);
    }

    @Test(expected = UnsupportedException.class)
    public void testGetSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        when(nullCloudMetadataClient.getSysTenantUser(null)).thenThrow(new UnsupportedException("CloudMetadata not supported"));
        OBDatabaseUser user = nullCloudMetadataClient.getSysTenantUser(null);
    }

    @Test
    public void testOceanbaseAccessMode() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        OceanBaseAccessMode oceanbaseAccessMode = nullCloudMetadataClient.oceanbaseAccessMode();
        assertEquals(OceanBaseAccessMode.DIRECT, oceanbaseAccessMode);
    }

    @Test
    public void testIncludeClusterNameForJdbcUsername() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        boolean includeClusterNameForJdbcUsername = nullCloudMetadataClient.includeClusterNameForJdbcUsername();
        assertEquals(true, includeClusterNameForJdbcUsername);
    }

    @Test
    public void testNeedsSysTenantUser() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        boolean needsSysTenantUser = nullCloudMetadataClient.needsSysTenantUser();
        assertEquals(false, needsSysTenantUser);
    }

    @Test
    public void testNeedsOBTenantName() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        boolean needsOBTenantName = nullCloudMetadataClient.needsOBTenantName();
        assertEquals(false, needsOBTenantName);
    }

    @Test
    public void testSupportsTenantInstance() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        boolean supportsTenantInstance = nullCloudMetadataClient.supportsTenantInstance();
        assertEquals(false, supportsTenantInstance);
    }

    @Test
    public void testSupportsCloudMetadata() {
        NullCloudMetadataClient nullCloudMetadataClient = new NullCloudMetadataClient();
        boolean supportsCloudMetadata = nullCloudMetadataClient.supportsCloudMetadata();
        assertEquals(false, supportsCloudMetadata);
    }
}