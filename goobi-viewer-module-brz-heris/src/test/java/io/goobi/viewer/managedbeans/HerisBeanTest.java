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

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.goobi.viewer.AbstractModuleTest;

public class HerisBeanTest extends AbstractModuleTest {

    HerisBean bean;

    @BeforeClass
    public static void setUpClass() throws Exception {
        AbstractModuleTest.setUpClass();
    }

    @Before
    public void setUp() throws Exception {
        bean = new HerisBean();
    }

    /**
     * @see HerisBean#isModuleEnabled()
     * @verifies return correct value
     */
    @Test
    public void isModuleEnabled_shouldReturnCorrectValue() throws Exception {
        Assert.assertTrue(bean.isModuleEnabled());
    }
}