import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeGUI {
    JFrame frame = new JFrame("Tic Tac Toe AI");
    JButton[] buttons = new JButton[9];
    char[] board = {' ',' ',' ',' ',' ',' ',' ',' ',' '};

    int nodesMinimax = 0;
    int nodesAlpha = 0;

    public TicTacToeGUI() {
        frame.setLayout(new GridLayout(3,3));
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for(int i=0;i<9;i++){
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 40));
            int idx = i;
            buttons[i].addActionListener(e -> playerMove(idx));
            frame.add(buttons[i]);
        }

        frame.setVisible(true);
    }

    void playerMove(int i){
        if(board[i] == ' '){
            board[i] = 'X';
            buttons[i].setText("X");

            if(checkWinner('X')){
                JOptionPane.showMessageDialog(frame, "You Win!");
                reset();
                return;
            }

            aiMove();
        }
    }

    void aiMove(){
        long start1 = System.nanoTime();
        nodesMinimax = 0;
        int move1 = bestMoveMinimax();
        long end1 = System.nanoTime();

        long start2 = System.nanoTime();
        nodesAlpha = 0;
        int move2 = bestMoveAlphaBeta();
        long end2 = System.nanoTime();

        // Use Alpha-Beta move (better performance)
        board[move2] = 'O';
        buttons[move2].setText("O");

        System.out.println("Minimax Time: " + (end1-start1));
        System.out.println("Alpha-Beta Time: " + (end2-start2));
        System.out.println("Minimax Nodes: " + nodesMinimax);
        System.out.println("Alpha-Beta Nodes: " + nodesAlpha);
        System.out.println("--------------------------");

        if(checkWinner('O')){
            JOptionPane.showMessageDialog(frame, "AI Wins!");
            reset();
        }
    }

    int bestMoveMinimax(){
        int bestVal = -1000;
        int move = -1;

        for(int i=0;i<9;i++){
            if(board[i]==' '){
                board[i]='O';
                int val = minimax(false);
                board[i]=' ';
                if(val>bestVal){
                    bestVal = val;
                    move = i;
                }
            }
        }
        return move;
    }

    int minimax(boolean isMax){
        nodesMinimax++;

        if(checkWinner('O')) return 1;
        if(checkWinner('X')) return -1;
        if(isFull()) return 0;

        if(isMax){
            int best = -1000;
            for(int i=0;i<9;i++){
                if(board[i]==' '){
                    board[i]='O';
                    best = Math.max(best, minimax(false));
                    board[i]=' ';
                }
            }
            return best;
        } else {
            int best = 1000;
            for(int i=0;i<9;i++){
                if(board[i]==' '){
                    board[i]='X';
                    best = Math.min(best, minimax(true));
                    board[i]=' ';
                }
            }
            return best;
        }
    }

    int bestMoveAlphaBeta(){
        int bestVal = -1000;
        int move = -1;

        for(int i=0;i<9;i++){
            if(board[i]==' '){
                board[i]='O';
                int val = alphaBeta(false, -1000, 1000);
                board[i]=' ';
                if(val>bestVal){
                    bestVal = val;
                    move = i;
                }
            }
        }
        return move;
    }

    int alphaBeta(boolean isMax, int alpha, int beta){
        nodesAlpha++;

        if(checkWinner('O')) return 1;
        if(checkWinner('X')) return -1;
        if(isFull()) return 0;

        if(isMax){
            int best = -1000;
            for(int i=0;i<9;i++){
                if(board[i]==' '){
                    board[i]='O';
                    best = Math.max(best, alphaBeta(false, alpha, beta));
                    board[i]=' ';
                    alpha = Math.max(alpha, best);
                    if(beta <= alpha) break;
                }
            }
            return best;
        } else {
            int best = 1000;
            for(int i=0;i<9;i++){
                if(board[i]==' '){
                    board[i]='X';
                    best = Math.min(best, alphaBeta(true, alpha, beta));
                    board[i]=' ';
                    beta = Math.min(beta, best);
                    if(beta <= alpha) break;
                }
            }
            return best;
        }
    }

    boolean checkWinner(char p){
        int[][] win = {
                {0,1,2},{3,4,5},{6,7,8},
                {0,3,6},{1,4,7},{2,5,8},
                {0,4,8},{2,4,6}
        };
        for(int[] w:win){
            if(board[w[0]]==p && board[w[1]]==p && board[w[2]]==p)
                return true;
        }
        return false;
    }

    boolean isFull(){
        for(char c:board) if(c==' ') return false;
        return true;
    }

    void reset(){
        for(int i=0;i<9;i++){
            board[i]=' ';
            buttons[i].setText("");
        }
    }

    public static void main(String[] args) {
        new TicTacToeGUI();
    }
}
