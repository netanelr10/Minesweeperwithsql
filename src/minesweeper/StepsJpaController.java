/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeper;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import minesweeper.exceptions.NonexistentEntityException;

/**
 *
 * @author אורח
 */
public class StepsJpaController implements Serializable {

    public StepsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Steps steps) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoardGame idboard = steps.getIdboard();
            if (idboard != null) {
                idboard = em.getReference(idboard.getClass(), idboard.getId());
                steps.setIdboard(idboard);
            }
            em.persist(steps);
            if (idboard != null) {
                idboard.getStepsList().add(steps);
                idboard = em.merge(idboard);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Steps steps) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Steps persistentSteps = em.find(Steps.class, steps.getId());
            BoardGame idboardOld = persistentSteps.getIdboard();
            BoardGame idboardNew = steps.getIdboard();
            if (idboardNew != null) {
                idboardNew = em.getReference(idboardNew.getClass(), idboardNew.getId());
                steps.setIdboard(idboardNew);
            }
            steps = em.merge(steps);
            if (idboardOld != null && !idboardOld.equals(idboardNew)) {
                idboardOld.getStepsList().remove(steps);
                idboardOld = em.merge(idboardOld);
            }
            if (idboardNew != null && !idboardNew.equals(idboardOld)) {
                idboardNew.getStepsList().add(steps);
                idboardNew = em.merge(idboardNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = steps.getId();
                if (findSteps(id) == null) {
                    throw new NonexistentEntityException("The steps with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Steps steps;
            try {
                steps = em.getReference(Steps.class, id);
                steps.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The steps with id " + id + " no longer exists.", enfe);
            }
            BoardGame idboard = steps.getIdboard();
            if (idboard != null) {
                idboard.getStepsList().remove(steps);
                idboard = em.merge(idboard);
            }
            em.remove(steps);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Steps> findStepsEntities() {
        return findStepsEntities(true, -1, -1);
    }

    public List<Steps> findStepsEntities(int maxResults, int firstResult) {
        return findStepsEntities(false, maxResults, firstResult);
    }

    private List<Steps> findStepsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Steps.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Steps findSteps(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Steps.class, id);
        } finally {
            em.close();
        }
    }

    public int getStepsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Steps> rt = cq.from(Steps.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
