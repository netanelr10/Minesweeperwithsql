/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeper;

import java.util.Random;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.RowFilter;

/**
 *
 * @author אורח
 */
public class Minesweeper {

    private int bord[][];//the game bord basic
    private int displaybord[][]; //the actual bord that the player will see
    private int minesCount; //how many bombs.
    private int rowCount; //row of the game borad(array[][])
    private int columnsCount; //column of the game borad(array[][])
    private int mine = 9; //number of the mine himself 
    private boolean isBoom = false;// if we got mines in the out put
    private final int minRow = 2;//The minimum can be entered on the board   
    private final int minCol = 2;//The minimum can be entered on the board   
    private final int minCMines = 1; //The minimum can be entered on the board   
    private final int maxRow = 50; //The maximum can be entered on the board
    private final int maxCol = 50; // The maximum can be entered on the board
    private final int maxMines = 49; //The maximum can be entered on the board   
    private int row1;  //loction on the board for input mines on board 
    private int col1; // loction on the board for input mines on board
    private int row; //input of loction on the board
    private int col; //input of loction on the board
    private boolean gameAlive = true;
    private int openCellsCount = 0;//all the cell thay are not mines
    private String answer; //for restart game
    Random rand = new Random(); //for the random order of the mines on the bord[][]
    private static EntityManagerFactory emf;

    /**
     * *
     * i did constructor to reduce the set+get for the board and just do it for
     * me
     *
     * @param minesCount-how much mines(bombs) we have
     * @param rowCount - rows of the game board
     * @param columnsCount- columns of the game board
     * @param opencellsCount - the clear cells without mines
     */
    public Minesweeper(int minesCount, int rowCount, int columnsCount) {
        emf = Persistence.createEntityManagerFactory("MineSweeperPU");
        isValid(rowCount, columnsCount, minesCount); //Checks the information the player has entered if it is correct
        createGame(rowCount, columnsCount, minesCount);//method who really create the board 
        notOpen(rowCount, columnsCount); // for display ,what the player will see 

    }

    public int getRowCount() {
        return rowCount;
    }
    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
    public int getColumnsCount() {
        return columnsCount;
    }
    public void setColumnsCount(int columnsCount) {
        this.columnsCount = columnsCount;
    }

/***
 * to save the board in the database in 
 */
    public void saveG(int row,int col,int mines) {
        BoardGameJpaController bjc = new BoardGameJpaController(emf);
        BoardGame bg = new BoardGame();

        bg.setRows(rowCount);
        bg.setColumns(columnsCount);
        bg.setMines(minesCount);
        bjc.create(bg);
        boomboard();
        
    }
    
    public void boomboard(){
        for (int rowC = 0; row < bord.length; row++) {
            for (int colC = 0; col < bord[row].length; col++) {
                if (bord[rowC][colC] == 9) {
                    BoomBoardJpaController boomCon = new BoomBoardJpaController(emf);
                    BoomBoard boom = new BoomBoard();
                    boom.setRow(rowC);
                    boom.setCol(colC);
                    boomCon.create(boom);
                }
            }
        }
    }
    
    /***
     * save the steps  
     * @param row
     * @param col 
     */
    public void step(int row,int col) {
        StepsJpaController sjc = new StepsJpaController(emf);
        Steps step = new Steps();
        step.setRow(row);
        step.setCol(col);
        sjc.create(step);
    }

    /**
     * *
     * that method create the board behind
     *
     * @param rowCount-input of the player for the board according to rows
     * @param columnsCount -input of the player for the board according to
     * columns
     * @param minesCount- input of the player for the board according to mines
     *
     */
    private void createGame(int rowCount, int columnsCount, int minesCount) {
        this.rowCount = rowCount;
        this.columnsCount = columnsCount;
        this.minesCount = minesCount;
        this.isBoom = false;
        this.bord = new int[rowCount][columnsCount];
        this.displaybord = new int[rowCount][columnsCount];

        //now we enter the mines with the function random
        for (int temp9 = 0; temp9 < minesCount;) {
            row1 = rand.nextInt(rowCount);
            col1 = rand.nextInt(columnsCount);
            if (bord[row1][col1] == mine) {
                continue;
            }
            bord[row1][col1] = mine;
            saveG(row1,col1,minesCount);
            temp9++;
        }
       
    }

    /**
     * *
     * how the player will see the board
     *
     * @param temp1-temporary
     * @param temp2-temporary
     */
    public void notOpen(int rowCount, int columnsCount) {
        int temp1;
        int temp2;
        for (temp1 = 0; temp1 < rowCount; temp1++) {
            for (temp2 = 0; temp2 < columnsCount; temp2++) {

                displaybord[temp1][temp2] = -1;
            }
        }
    }

    public int getOpencellsCount() {
        openCellsCount = (rowCount * columnsCount) - minesCount;
        return openCellsCount;
    }

    /**
     * *
     * the variable that count all the cells without the mines
     *
     * @param opencellsCount
     */
    public void setOpencellsCount(int opencellsCount) {
        this.openCellsCount = opencellsCount;
    }

    /**
     * *
     * method that check how much friends we have around the mines and count
     * them into the location that the player pressed temp1-temp temp2-temporary
     *
     * @param row-some index on the board
     * @param col-some index on the board
     * @param temp1- Temporary variable
     * @param temp2- Temporary variable
     * @return
     */
    //borad of the admin
    public int nighboorBomb(int row, int col) {
        int temp1;
        int temp2;
        int count = 0;
        if (bord[row][col] == 9) {
            return 9;
        }
        for (temp1 = -1; temp1 < 2; temp1++) {
            if (temp1 == -1 && row == 0) {
                continue;
            }
            if (row == bord.length - 1 && temp1 == 1) {
                continue;
            }

            for (temp2 = -1; temp2 < 2; temp2++) {

                if (temp2 == -1 && col == 0) {
                    continue;
                }
                if (col == bord[1].length - 1 && temp2 == 1) {
                    continue;
                }
                if (temp1 == 0 && temp2 == 0) {
                    continue;
                }
                if (bord[row + temp1][col + temp2] == 9) {
                    count = count + 1;
                }
            }
        }
        bord[row][col] = count;
        return count;
    }
    /**
     * *
     * this function check if the location is mine
     *
     * @param row- some index of row
     * @param col- some index of column
     * @return - false or true if this location a bomb
     */
    public boolean boom(int row, int col) {
        if (displaybord[row][col] == mine) {
            isBoom = true;
            return true;
        }
        return false;
    }

    /**
     * *
     * all the function/method in brief that what happened after the input of
     * the player
     *
     * @param row- location the player choose
     * @param col- location that the player take
     */
    public void click(int row, int col) {
        open(row, col);
        if (boom(row, col) == true) {
            openall(isBoom);
            return;
        }
        if (isWin() == true) {
            System.out.println("you win");
            gameAlive = false;
            return;
        }
        step(row, col); //just for chack if the database work
    }
    /**
     *
     * that method open the bored and discovered all the location
     *
     * @param row - location on board that the player choose
     * @param col - location on board that the player choose
     *
     */
    public void open(int row, int col) {
        if (displaybord[row][col] != -1 && bord[row][col] == mine && displaybord[row][col] == bord[row][col]) {
            return;
        }
        displaybord[row][col] = nighboorBomb(row, col);
        this.openCellsCount = openCellsCount - 1;

        if (displaybord[row][col] == 0) {
            openEmptyCells(row, col);
        }
    }

    /**
     * *
     * the method display the game board that we paly
     *
     * @temp1 - Temporary variable
     * @temp2 - Temporary variable
     */
    public void print() {
        int temp1;
        int temp2;
        for (temp1 = 0; temp1 < rowCount; temp1++) {
            for (temp2 = 0; temp2 < columnsCount; temp2++) {
                System.out.print(displaybord[temp1][temp2] + ",");
                //System.out.print(bord[temp1][temp2]); // that just for check the board
            }
            System.out.println();
        }
    }

    /**
     * *
     * that method open all the empty cells on the board if the player got
     * 0(empty cell)
     *
     * @param row- index that the player choose
     * @param col-index that the player choose
     */
    private void openEmptyCells(int row, int col) {
        for (int temp1 = -1; temp1 < 2; temp1++) {
            if (temp1 == -1 && row == 0) {
                continue;
            }
            if (row == bord.length - 1 && temp1 == 1) {
                continue;
            }

            for (int temp2 = -1; temp2 < 2; temp2++) {

                if (temp2 == -1 && col == 0) {
                    continue;
                }
                if (col == bord[1].length - 1 && temp2 == 1) {
                    continue;
                }
                if (temp1 == 0 && temp2 == 0) {
                    continue;
                }
                if (displaybord[row + temp1][col + temp2] != -1) {
                    continue;
                }
                open(row + temp1, col + temp2);

            }
            return;
        }
    }

    /**
     * *
     * get and set variable who tell us if the game is running/alive or over
     */
    public boolean getGameAlive() {
        return gameAlive;
    }

    public void setGameAlive(boolean gameAlive) {
        this.gameAlive = gameAlive;
    }

    /**
     * *
     * this method display the board with all te anwsers(numbers) when the
     * player lose
     *
     * @param temp1-temporary variable
     * @param temp2-temporary variable
     */
    private void openall(boolean isBoom) {
        for (int temp1 = 0; temp1 < rowCount; temp1++) {
            for (int temp2 = 0; temp2 < columnsCount; temp2++) {
                if (isBoom == true) {
                    displaybord[temp1][temp2] = nighboorBomb(temp1, temp2);
                }
            }
        }
        System.out.println("you lose fuck you");
        gameAlive = false;
    }

    /**
     * *
     * this function check if after that one step the player won
     *
     * @return true or false
     */
    private boolean isWin() {
        if (this.openCellsCount == 0) {
            return true;
        }
        return false;
    }

    /**
     * *
     * that method who check if the player enter legal numbers or not if he
     * enter illegal number he will get massage
     *
     * @param rowCount-input of the player for the count of the rows on board
     * @param columnsCount-input of the player for the count
     * @param minesCount-input of the player of the mines
     */
    private void isValid(int rowCount, int columnsCount, int minesCount) {
        boolean isvalid = true;
        String error = "";
        if (rowCount < minRow) {
            error = error + "\n" + "your size of rows is bigger than" + minRow;
            isvalid = false;
        }
        if (rowCount > maxRow) {
            error = error + "\n" + "your size of rows is bigger than" + maxRow;
            isvalid = false;
        }
        if (columnsCount < minCol) {
            error = error + "your size of columns is below then " + minCol;
            isvalid = false;
        }
        if (columnsCount > maxCol) {
            error = error + "\n" + " your size of columns is bigger than" + maxCol;
            isvalid = false;
        }
        if (minesCount < minCMines) {
            error = error + "\n" + "You have no bombs or you have entered a number Below" + minCMines;
            isvalid = false;
        }
        if (minesCount >= columnsCount * rowCount) {
            error = error + "you have too much mines its above the number of cells in the board";
            isvalid = false;
        }

        if (!isvalid) {
            throw new ArithmeticException(error);
        }
    }
}
