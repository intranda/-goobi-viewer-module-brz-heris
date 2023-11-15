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
package io.goobi.viewer.modules.heris;

import org.junit.Assert;
import org.junit.Test;

import io.goobi.viewer.AbstractModuleTest;
import io.goobi.viewer.controller.DataManager;
import io.goobi.viewer.modules.HerisModule;

public class ModuleConfigurationTest extends AbstractModuleTest {

    /**
     * @see ModuleConfiguration#isModuleEnabled()
     * @verifies return correct value
     */
    @Test
    public void isModuleEnabled_shouldReturnCorrectValue() throws Exception {
        Assert.assertTrue(((HerisModule) DataManager.getInstance().getModule(HerisModule.ID)).getConfiguration().isModuleEnabled());
    }

    /**
     * @see ModuleConfiguration#getIndexFieldForHost(String)
     * @verifies return correct value
     */
    @Test
    public void getIndexFieldForHost_shouldReturnCorrectValue() throws Exception {
        Assert.assertEquals("MD_FOO_ID",
                ((HerisModule) DataManager.getInstance().getModule(HerisModule.ID)).getConfiguration().getIndexFieldForHost("example.com"));
    }

    /**
     * @see ModuleConfiguration#getUrlPatternForHost(String)
     * @verifies return correct value
     */
    @Test
    public void getUrlPatternForHost_shouldReturnCorrectValue() throws Exception {
        Assert.assertEquals("{SCHEME}://{HOST}/{ID}",
                ((HerisModule) DataManager.getInstance().getModule(HerisModule.ID)).getConfiguration().getIndexFieldForHost("example.com"));
    }
}