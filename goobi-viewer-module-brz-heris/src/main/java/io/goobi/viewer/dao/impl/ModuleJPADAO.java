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
package io.goobi.viewer.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.goobi.viewer.controller.DataManager;
import io.goobi.viewer.dao.IModuleDAO;
import io.goobi.viewer.exceptions.DAOException;
import io.goobi.viewer.model.Skeleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public class ModuleJPADAO implements IModuleDAO {

    /** Logger for this class. */
    private static final Logger logger = LogManager.getLogger(ModuleJPADAO.class);

    /** Core DAO. */
    private final JPADAO dao;

    public ModuleJPADAO() throws DAOException {
        logger.trace("ModuleJPADAO()");
        dao = (JPADAO) DataManager.getInstance().getDao();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Skeleton> getAllSkeletons() throws DAOException {
        preQuery();
        Query q = dao.getEntityManager().createQuery("SELECT o FROM Skeleton o");
        // q.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return q.getResultList();
    }

    @Override
    public boolean addSkeleton(Skeleton skeleton) throws DAOException {
        preQuery();
        EntityManager em = dao.getEntityManager();
        try {
            dao.startTransaction(em);
            em.persist(skeleton);
            dao.commitTransaction(em);
        } finally {
            dao.close(em);
        }

        return true;
    }

    @Override
    public boolean updateSkeleton(Skeleton skeleton) throws DAOException {
        preQuery();
        EntityManager em = dao.getEntityManager();
        try {
            dao.startTransaction(em);
            em.merge(skeleton);
            dao.commitTransaction(em);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            dao.close(em);
        }

        return true;
    }

    @Override
    public boolean deleteSkeleton(Skeleton skeleton) throws DAOException {
        preQuery();
        EntityManager em = dao.getEntityManager();
        try {
            dao.startTransaction(em);
            Skeleton o = em.getReference(Skeleton.class, skeleton.getId());
            em.remove(o);
            dao.commitTransaction(em);
            return true;
        } finally {
            dao.close(em);
        }
    }

    private void preQuery() throws DAOException {
        dao.preQuery();
    }
}
