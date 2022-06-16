/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeper;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author אורח
 */
@Entity
@Table(name = "boomBoard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BoomBoard.findAll", query = "SELECT b FROM BoomBoard b"),
    @NamedQuery(name = "BoomBoard.findByIdboom", query = "SELECT b FROM BoomBoard b WHERE b.idboom = :idboom"),
    @NamedQuery(name = "BoomBoard.findByRow", query = "SELECT b FROM BoomBoard b WHERE b.row = :row"),
    @NamedQuery(name = "BoomBoard.findByCol", query = "SELECT b FROM BoomBoard b WHERE b.col = :col")})
public class BoomBoard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idboom")
    private Integer idboom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "row")
    private int row;
    @Basic(optional = false)
    @NotNull
    @Column(name = "col")
    private int col;
    @JoinColumn(name = "idborad", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BoardGame idborad;

    public BoomBoard() {
    }

    public BoomBoard(Integer idboom) {
        this.idboom = idboom;
    }

    public BoomBoard(Integer idboom, int row, int col) {
        this.idboom = idboom;
        this.row = row;
        this.col = col;
    }

    public Integer getIdboom() {
        return idboom;
    }

    public void setIdboom(Integer idboom) {
        this.idboom = idboom;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public BoardGame getIdborad() {
        return idborad;
    }

    public void setIdborad(BoardGame idborad) {
        this.idborad = idborad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idboom != null ? idboom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoomBoard)) {
            return false;
        }
        BoomBoard other = (BoomBoard) object;
        if ((this.idboom == null && other.idboom != null) || (this.idboom != null && !this.idboom.equals(other.idboom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "minesweeper.BoomBoard[ idboom=" + idboom + " ]";
    }
    
}
