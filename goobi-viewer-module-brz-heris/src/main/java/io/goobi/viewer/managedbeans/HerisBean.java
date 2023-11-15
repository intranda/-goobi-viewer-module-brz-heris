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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.goobi.viewer.controller.DataManager;
import io.goobi.viewer.exceptions.ModuleMissingException;
import io.goobi.viewer.managedbeans.utils.BeanUtils;
import io.goobi.viewer.model.viewer.StringPair;
import io.goobi.viewer.modules.HerisModule;
import io.goobi.viewer.modules.heris.ModuleConfiguration;

@Named
public class HerisBean implements Serializable {

    private static final long serialVersionUID = 3963436132111065936L;

    private static final Logger logger = LogManager.getLogger(HerisBean.class);

    @Inject
    private ActiveDocumentBean activeDocumentBean;

    @Inject
    private UserBean userBean;

    /** Empty constructor. */
    public HerisBean() {
        // Empty
    }

    @PostConstruct
    public void init() {
    }

    public boolean isModuleEnabled() throws ModuleMissingException {
        return HerisModule.getInstance().getConfiguration().isModuleEnabled();
    }

    public List<StringPair> getExternalLinks() {
        if (!userBean.isLoggedIn() || !activeDocumentBean.isRecordLoaded()) {
            return Collections.emptyList();
        }

        // Get PORTAL-SCHEME + PORTAL-AUTHORITY from HTTP header
        String scheme = BeanUtils.getRequest().getHeader("PORTAL-SCHEME");
        String host = BeanUtils.getRequest().getHeader("PORTAL-AUTHORITY");
        logger.trace("PORTAL-SCHEME: {}", scheme);
        logger.trace("PORTAL-AUTHORITY: {}", host);

        // Use appropriate identifier in URL from configured Solr 
        List<StringPair> ret = new ArrayList<>();
        if (StringUtils.isNotEmpty(scheme) && StringUtils.isNotEmpty(host)) {
            try {
                ModuleConfiguration config = (ModuleConfiguration) DataManager.getInstance().getModule(HerisModule.ID).getConfiguration();
                String field = config.getIndexFieldForHost(host);
                String pattern = config.getUrlPatternForHost(host);
                logger.trace("field: {}", field);
                logger.trace("pattern: {}", pattern);
                if (StringUtils.isNotEmpty(field) && StringUtils.isNotEmpty(pattern)) {
                    String identifier = activeDocumentBean.getViewManager().getTopStructElement().getMetadataValue(field);
                    if (StringUtils.isNotEmpty(identifier)) {
                        String url = pattern.replace("{SCHEME}", scheme).replace("{HOST}", host).replace("{ID}", identifier);
                        ret.add(new StringPair(url, url));
                        logger.trace("added {}", url);
                    }
                } else {
                    logger.warn("HERIS configuration incomplete for host '{}'. Make sure pattern/text() and pattern/@field are not empty.", host);
                }

            } catch (ModuleMissingException e) {
                logger.error(e.getMessage());
            }
        }

        return ret;
    }
}
