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
public class BoomBoardJpaController implements Serializable {

    public BoomBoardJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BoomBoard boomBoard) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoardGame idborad = boomBoard.getIdborad();
            if (idborad != null) {
                idborad = em.getReference(idborad.getClass(), idborad.getId());
                boomBoard.setIdborad(idborad);
            }
            em.persist(boomBoard);
            if (idborad != null) {
                idborad.getBoomBoardList().add(boomBoard);
                idborad = em.merge(idborad);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BoomBoard boomBoard) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoomBoard persistentBoomBoard = em.find(BoomBoard.class, boomBoard.getIdboom());
            BoardGame idboradOld = persistentBoomBoard.getIdborad();
            BoardGame idboradNew = boomBoard.getIdborad();
            if (idboradNew != null) {
                idboradNew = em.getReference(idboradNew.getClass(), idboradNew.getId());
                boomBoard.setIdborad(idboradNew);
            }
            boomBoard = em.merge(boomBoard);
            if (idboradOld != null && !idboradOld.equals(idboradNew)) {
                idboradOld.getBoomBoardList().remove(boomBoard);
                idboradOld = em.merge(idboradOld);
            }
            if (idboradNew != null && !idboradNew.equals(idboradOld)) {
                idboradNew.getBoomBoardList().add(boomBoard);
                idboradNew = em.merge(idboradNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = boomBoard.getIdboom();
                if (findBoomBoard(id) == null) {
                    throw new NonexistentEntityException("The boomBoard with id " + id + " no longer exists.");
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
            BoomBoard boomBoard;
            try {
                boomBoard = em.getReference(BoomBoard.class, id);
                boomBoard.getIdboom();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The boomBoard with id " + id + " no longer exists.", enfe);
            }
            BoardGame idborad = boomBoard.getIdborad();
            if (idborad != null) {
                idborad.getBoomBoardList().remove(boomBoard);
                idborad = em.merge(idborad);
            }
            em.remove(boomBoard);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BoomBoard> findBoomBoardEntities() {
        return findBoomBoardEntities(true, -1, -1);
    }

    public List<BoomBoard> findBoomBoardEntities(int maxResults, int firstResult) {
        return findBoomBoardEntities(false, maxResults, firstResult);
    }

    private List<BoomBoard> findBoomBoardEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BoomBoard.class));
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

    public BoomBoard findBoomBoard(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BoomBoard.class, id);
        } finally {
            em.close();
        }
    }

    public int getBoomBoardCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BoomBoard> rt = cq.from(BoomBoard.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
