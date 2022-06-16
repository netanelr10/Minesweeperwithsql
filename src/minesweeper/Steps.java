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
@Table(name = "steps")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Steps.findAll", query = "SELECT s FROM Steps s"),
    @NamedQuery(name = "Steps.findById", query = "SELECT s FROM Steps s WHERE s.id = :id"),
    @NamedQuery(name = "Steps.findByRow", query = "SELECT s FROM Steps s WHERE s.row = :row"),
    @NamedQuery(name = "Steps.findByCol", query = "SELECT s FROM Steps s WHERE s.col = :col")})
public class Steps implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "row")
    private int row;
    @Basic(optional = false)
    @NotNull
    @Column(name = "col")
    private int col;
    @JoinColumn(name = "idboard", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private BoardGame idboard;

    public Steps() {
    }

    public Steps(Integer id) {
        this.id = id;
    }

    public Steps(Integer id, int row, int col) {
        this.id = id;
        this.row = row;
        this.col = col;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BoardGame getIdboard() {
        return idboard;
    }

    public void setIdboard(BoardGame idboard) {
        this.idboard = idboard;
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
        if (!(object instanceof Steps)) {
            return false;
        }
        Steps other = (Steps) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "minesweeper.Steps[ id=" + id + " ]";
    }
    
}
