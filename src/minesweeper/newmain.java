/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package minesweeper;

import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

import java.util.Scanner;

/**
 *
 * @author אורח
 */
public class newmain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String userName;
        int password;
        int minesCount;
        int rowCount;
        int columnsCount;
        int row;
        int col;

        System.out.println("welcame to minesweeper game;");
        System.out.println("you have user: ");
        String answer = sc.next();
        if (answer.equalsIgnoreCase("no")) {
            System.out.println("enter username:");
            userName = sc.next();
            System.out.println("enter password(numbers)");
            password = sc.nextInt();
        } else if (answer.equalsIgnoreCase("yes")) {
            System.out.println("enter username:");
            userName = sc.next();
        }
        System.out.println("enter number of rows:");
        rowCount = sc.nextInt();
        System.out.println("enter number of columns:");
        columnsCount = sc.nextInt();
        System.out.println("enter number of mines:");
        minesCount = sc.nextInt();
        try {
            Minesweeper g1 = new Minesweeper(minesCount, rowCount, columnsCount);
          //  gui g2 = new gui();
            g1.print();
            while (g1.getGameAlive() == true) {

                System.out.println("enter a index:");
                row = sc.nextInt();
                col = sc.nextInt();
                g1.click(row, col);
                g1.print();
            }
        } catch (Exception e) {
            System.out.println(e);

        }

    }
}
