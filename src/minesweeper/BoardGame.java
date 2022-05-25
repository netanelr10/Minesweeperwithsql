/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeper;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author אורח
 */
@Entity
@Table(name = "boardGame")
@NamedQueries({
    @NamedQuery(name = "BoardGame.findAll", query = "SELECT b FROM BoardGame b"),
    @NamedQuery(name = "BoardGame.findById", query = "SELECT b FROM BoardGame b WHERE b.id = :id"),
    @NamedQuery(name = "BoardGame.findByRows", query = "SELECT b FROM BoardGame b WHERE b.rows = :rows"),
    @NamedQuery(name = "BoardGame.findByColumns", query = "SELECT b FROM BoardGame b WHERE b.columns = :columns"),
    @NamedQuery(name = "BoardGame.findByMines", query = "SELECT b FROM BoardGame b WHERE b.mines = :mines")})
public class BoardGame implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "rows")
    private int rows;
    @Basic(optional = false)
    @Column(name = "columns")
    private int columns;
    @Basic(optional = false)
    @Column(name = "mines")
    private int mines;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idborad")
    private List<BoomBoard> boomBoardList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idboard")
    private List<Steps> stepsList;

    public BoardGame() {
    }

    public BoardGame(Integer id) {
        this.id = id;
    }

    public BoardGame(Integer id, int rows, int columns, int mines) {
        this.id = id;
        this.rows = rows;
        this.columns = columns;
        this.mines = mines;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public List<BoomBoard> getBoomBoardList() {
        return boomBoardList;
    }

    public void setBoomBoardList(List<BoomBoard> boomBoardList) {
        this.boomBoardList = boomBoardList;
    }

    public List<Steps> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoardGame)) {
            return false;
        }
        BoardGame other = (BoardGame) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "minesweeper.BoardGame[ id=" + id + " ]";
    }
    
}
