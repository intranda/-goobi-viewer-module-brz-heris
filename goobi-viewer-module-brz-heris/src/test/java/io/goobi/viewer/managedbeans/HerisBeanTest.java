/**
 * This file is part of the Goobi viewer - a content presentation and management application for digitized objects.
 *
 * Visit these websites for more information.
 *          - http://www.intranda.com
 *          - http://digiverso.com
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.goobi.viewer.managedbeans;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.goobi.viewer.AbstractModuleSolrEnabledTest;
import io.goobi.viewer.model.security.user.User;
import io.goobi.viewer.model.viewer.StringPair;

public class HerisBeanTest extends AbstractModuleSolrEnabledTest {

    HerisBean bean;

    @BeforeClass
    public static void setUpClass() throws Exception {
        AbstractModuleSolrEnabledTest.setUpClass();
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        bean = new HerisBean();

        FacesContext facesContext = ContextMocker.mockFacesContext();
        ExternalContext externalContext = Mockito.mock(ExternalContext.class);
        Mockito.when(facesContext.getExternalContext()).thenReturn(externalContext);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(externalContext.getRequest()).thenReturn(request);
    }

    /**
     * @see HerisBean#isModuleEnabled()
     * @verifies return correct value
     */
    @Test
    public void isModuleEnabled_shouldReturnCorrectValue() throws Exception {
        assertTrue(bean.isModuleEnabled());
    }

    /**
     * @see HerisBean#getExternalLinks()
     * @verifies return empty list if user not logged in
     */
    @Test
    public void getExternalLinks_shouldReturnEmptyListIfUserNotLoggedIn() throws Exception {
        bean.userBean = new UserBean();
        assertFalse(bean.userBean.isLoggedIn());

        List<StringPair> ret = bean.getExternalLinks();
        assertTrue(ret.isEmpty());
    }

    /**
     * @see HerisBean#getExternalLinks()
     * @verifies return empty list if no record loaded
     */
    @Test
    public void getExternalLinks_shouldReturnEmptyListIfNoRecordLoaded() throws Exception {
        bean.userBean = new UserBean();
        bean.userBean.setUser(new User());
        bean.userBean.getUser().setActive(true);
        assertTrue(bean.userBean.isLoggedIn());

        bean.activeDocumentBean = new ActiveDocumentBean();
        assertFalse(bean.activeDocumentBean.isRecordLoaded());

        List<StringPair> ret = bean.getExternalLinks();
        assertTrue(ret.isEmpty());
    }

    /**
     * @see HerisBean#getExternalLinks()
     * @verifies return empty list if scheme param missing
     */
    @Test
    public void getExternalLinks_shouldReturnEmptyListIfSchemeParamMissing() throws Exception {
        bean.userBean = new UserBean();
        bean.userBean.setUser(new User());
        bean.userBean.getUser().setActive(true);
        assertTrue(bean.userBean.isLoggedIn());

        bean.activeDocumentBean = new ActiveDocumentBean();
        bean.activeDocumentBean.setPersistentIdentifier(PI_KLEIUNIV);
        bean.activeDocumentBean.update();
        assertTrue(bean.activeDocumentBean.isRecordLoaded());

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Mockito.when(request.getHeader("PORTAL-SCHEME")).thenReturn(null);
        Mockito.when(request.getHeader("PORTAL-AUTHORITY")).thenReturn(null);

        List<StringPair> ret = bean.getExternalLinks();
        assertTrue(ret.isEmpty());
    }

    /**
     * @see HerisBean#getExternalLinks()
     * @verifies return empty list if authority param missing
     */
    @Test
    public void getExternalLinks_shouldReturnEmptyListIfAuthorityParamMissing() throws Exception {
        bean.userBean = new UserBean();
        bean.userBean.setUser(new User());
        bean.userBean.getUser().setActive(true);
        assertTrue(bean.userBean.isLoggedIn());

        bean.activeDocumentBean = new ActiveDocumentBean();
        bean.activeDocumentBean.setPersistentIdentifier(PI_KLEIUNIV);
        bean.activeDocumentBean.update();
        assertTrue(bean.activeDocumentBean.isRecordLoaded());

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Mockito.when(request.getHeader("PORTAL-SCHEME")).thenReturn("https");
        Assert.assertEquals("https", request.getHeader("PORTAL-SCHEME"));
        Mockito.when(request.getHeader("PORTAL-AUTHORITY")).thenReturn(null);

        List<StringPair> ret = bean.getExternalLinks();
        assertTrue(ret.isEmpty());
    }

    /**
     * @see HerisBean#getExternalLinks()
     * @verifies return urls correctly
     */
    @Test
    public void getExternalLinks_shouldReturnUrlsCorrectly() throws Exception {
        bean.userBean = new UserBean();
        bean.userBean.setUser(new User());
        bean.userBean.getUser().setActive(true);
        assertTrue(bean.userBean.isLoggedIn());

        bean.activeDocumentBean = new ActiveDocumentBean();
        bean.activeDocumentBean.setPersistentIdentifier(PI_KLEIUNIV);
        bean.activeDocumentBean.update();
        assertTrue(bean.activeDocumentBean.isRecordLoaded());

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Mockito.when(request.getHeader("PORTAL-SCHEME-TEST")).thenReturn("https");
        Assert.assertEquals("https", request.getHeader("PORTAL-SCHEME-TEST"));
        Mockito.when(request.getHeader("PORTAL-AUTHORITY-TEST")).thenReturn("foo.example.com");

        List<StringPair> ret = bean.getExternalLinks();
        assertFalse(ret.isEmpty());
        Assert.assertEquals("https://bar.example.com/" + PI_KLEIUNIV, ret.get(0).getOne());
        Assert.assertEquals("https://bar.example.com/" + PI_KLEIUNIV, ret.get(0).getTwo());
    }
}