/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeper;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import minesweeper.exceptions.IllegalOrphanException;
import minesweeper.exceptions.NonexistentEntityException;

/**
 *
 * @author אורח
 */
public class BoardGameJpaController implements Serializable {

    public BoardGameJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BoardGame boardGame) {
        if (boardGame.getBoomBoardList() == null) {
            boardGame.setBoomBoardList(new ArrayList<BoomBoard>());
        }
        if (boardGame.getStepsList() == null) {
            boardGame.setStepsList(new ArrayList<Steps>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<BoomBoard> attachedBoomBoardList = new ArrayList<BoomBoard>();
            for (BoomBoard boomBoardListBoomBoardToAttach : boardGame.getBoomBoardList()) {
                boomBoardListBoomBoardToAttach = em.getReference(boomBoardListBoomBoardToAttach.getClass(), boomBoardListBoomBoardToAttach.getIdboom());
                attachedBoomBoardList.add(boomBoardListBoomBoardToAttach);
            }
            boardGame.setBoomBoardList(attachedBoomBoardList);
            List<Steps> attachedStepsList = new ArrayList<Steps>();
            for (Steps stepsListStepsToAttach : boardGame.getStepsList()) {
                stepsListStepsToAttach = em.getReference(stepsListStepsToAttach.getClass(), stepsListStepsToAttach.getId());
                attachedStepsList.add(stepsListStepsToAttach);
            }
            boardGame.setStepsList(attachedStepsList);
            em.persist(boardGame);
            for (BoomBoard boomBoardListBoomBoard : boardGame.getBoomBoardList()) {
                BoardGame oldIdboradOfBoomBoardListBoomBoard = boomBoardListBoomBoard.getIdborad();
                boomBoardListBoomBoard.setIdborad(boardGame);
                boomBoardListBoomBoard = em.merge(boomBoardListBoomBoard);
                if (oldIdboradOfBoomBoardListBoomBoard != null) {
                    oldIdboradOfBoomBoardListBoomBoard.getBoomBoardList().remove(boomBoardListBoomBoard);
                    oldIdboradOfBoomBoardListBoomBoard = em.merge(oldIdboradOfBoomBoardListBoomBoard);
                }
            }
            for (Steps stepsListSteps : boardGame.getStepsList()) {
                BoardGame oldIdboardOfStepsListSteps = stepsListSteps.getIdboard();
                stepsListSteps.setIdboard(boardGame);
                stepsListSteps = em.merge(stepsListSteps);
                if (oldIdboardOfStepsListSteps != null) {
                    oldIdboardOfStepsListSteps.getStepsList().remove(stepsListSteps);
                    oldIdboardOfStepsListSteps = em.merge(oldIdboardOfStepsListSteps);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BoardGame boardGame) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoardGame persistentBoardGame = em.find(BoardGame.class, boardGame.getId());
            List<BoomBoard> boomBoardListOld = persistentBoardGame.getBoomBoardList();
            List<BoomBoard> boomBoardListNew = boardGame.getBoomBoardList();
            List<Steps> stepsListOld = persistentBoardGame.getStepsList();
            List<Steps> stepsListNew = boardGame.getStepsList();
            List<String> illegalOrphanMessages = null;
            for (BoomBoard boomBoardListOldBoomBoard : boomBoardListOld) {
                if (!boomBoardListNew.contains(boomBoardListOldBoomBoard)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BoomBoard " + boomBoardListOldBoomBoard + " since its idborad field is not nullable.");
                }
            }
            for (Steps stepsListOldSteps : stepsListOld) {
                if (!stepsListNew.contains(stepsListOldSteps)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Steps " + stepsListOldSteps + " since its idboard field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<BoomBoard> attachedBoomBoardListNew = new ArrayList<BoomBoard>();
            for (BoomBoard boomBoardListNewBoomBoardToAttach : boomBoardListNew) {
                boomBoardListNewBoomBoardToAttach = em.getReference(boomBoardListNewBoomBoardToAttach.getClass(), boomBoardListNewBoomBoardToAttach.getIdboom());
                attachedBoomBoardListNew.add(boomBoardListNewBoomBoardToAttach);
            }
            boomBoardListNew = attachedBoomBoardListNew;
            boardGame.setBoomBoardList(boomBoardListNew);
            List<Steps> attachedStepsListNew = new ArrayList<Steps>();
            for (Steps stepsListNewStepsToAttach : stepsListNew) {
                stepsListNewStepsToAttach = em.getReference(stepsListNewStepsToAttach.getClass(), stepsListNewStepsToAttach.getId());
                attachedStepsListNew.add(stepsListNewStepsToAttach);
            }
            stepsListNew = attachedStepsListNew;
            boardGame.setStepsList(stepsListNew);
            boardGame = em.merge(boardGame);
            for (BoomBoard boomBoardListNewBoomBoard : boomBoardListNew) {
                if (!boomBoardListOld.contains(boomBoardListNewBoomBoard)) {
                    BoardGame oldIdboradOfBoomBoardListNewBoomBoard = boomBoardListNewBoomBoard.getIdborad();
                    boomBoardListNewBoomBoard.setIdborad(boardGame);
                    boomBoardListNewBoomBoard = em.merge(boomBoardListNewBoomBoard);
                    if (oldIdboradOfBoomBoardListNewBoomBoard != null && !oldIdboradOfBoomBoardListNewBoomBoard.equals(boardGame)) {
                        oldIdboradOfBoomBoardListNewBoomBoard.getBoomBoardList().remove(boomBoardListNewBoomBoard);
                        oldIdboradOfBoomBoardListNewBoomBoard = em.merge(oldIdboradOfBoomBoardListNewBoomBoard);
                    }
                }
            }
            for (Steps stepsListNewSteps : stepsListNew) {
                if (!stepsListOld.contains(stepsListNewSteps)) {
                    BoardGame oldIdboardOfStepsListNewSteps = stepsListNewSteps.getIdboard();
                    stepsListNewSteps.setIdboard(boardGame);
                    stepsListNewSteps = em.merge(stepsListNewSteps);
                    if (oldIdboardOfStepsListNewSteps != null && !oldIdboardOfStepsListNewSteps.equals(boardGame)) {
                        oldIdboardOfStepsListNewSteps.getStepsList().remove(stepsListNewSteps);
                        oldIdboardOfStepsListNewSteps = em.merge(oldIdboardOfStepsListNewSteps);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = boardGame.getId();
                if (findBoardGame(id) == null) {
                    throw new NonexistentEntityException("The boardGame with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoardGame boardGame;
            try {
                boardGame = em.getReference(BoardGame.class, id);
                boardGame.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The boardGame with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<BoomBoard> boomBoardListOrphanCheck = boardGame.getBoomBoardList();
            for (BoomBoard boomBoardListOrphanCheckBoomBoard : boomBoardListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BoardGame (" + boardGame + ") cannot be destroyed since the BoomBoard " + boomBoardListOrphanCheckBoomBoard + " in its boomBoardList field has a non-nullable idborad field.");
            }
            List<Steps> stepsListOrphanCheck = boardGame.getStepsList();
            for (Steps stepsListOrphanCheckSteps : stepsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BoardGame (" + boardGame + ") cannot be destroyed since the Steps " + stepsListOrphanCheckSteps + " in its stepsList field has a non-nullable idboard field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(boardGame);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BoardGame> findBoardGameEntities() {
        return findBoardGameEntities(true, -1, -1);
    }

    public List<BoardGame> findBoardGameEntities(int maxResults, int firstResult) {
        return findBoardGameEntities(false, maxResults, firstResult);
    }

    private List<BoardGame> findBoardGameEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BoardGame.class));
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

    public BoardGame findBoardGame(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BoardGame.class, id);
        } finally {
            em.close();
        }
    }

    public int getBoardGameCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BoardGame> rt = cq.from(BoardGame.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
