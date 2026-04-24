import javax.swing.*;
import java.awt.*;

public class SudokuGUI {

    JFrame frame = new JFrame("Sudoku Solver");
    JTextField[][] cells = new JTextField[9][9];

    public SudokuGUI() {
        frame.setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(9,9));

        int[][] puzzle = {
                {5,3,0,0,7,0,0,0,0},
                {6,0,0,1,9,5,0,0,0},
                {0,9,8,0,0,0,0,6,0},
                {8,0,0,0,6,0,0,0,3},
                {4,0,0,8,0,3,0,0,1},
                {7,0,0,0,2,0,0,0,6},
                {0,6,0,0,0,0,2,8,0},
                {0,0,0,4,1,9,0,0,5},
                {0,0,0,0,8,0,0,7,9}
        };

        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                cells[i][j] = new JTextField();
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(new Font("Arial", Font.BOLD, 18));

                if(puzzle[i][j] != 0){
                    cells[i][j].setText(String.valueOf(puzzle[i][j]));
                    cells[i][j].setEditable(false);
                }

                grid.add(cells[i][j]);
            }
        }

        JPanel buttons = new JPanel();

        JButton solveBtn = new JButton("Solve");
        JButton checkBtn = new JButton("Check");

        solveBtn.addActionListener(e -> solveSudoku());
        checkBtn.addActionListener(e -> checkSolution());

        buttons.add(solveBtn);
        buttons.add(checkBtn);

        frame.add(grid, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);

        frame.setSize(400,450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    void solveSudoku(){
        int[][] board = getBoard();

        if(solve(board)){
            setBoard(board);
        } else {
            JOptionPane.showMessageDialog(frame, "No solution!");
        }
    }

    void checkSolution(){
        int[][] board = getBoard();

        if(isValidBoard(board)){
            JOptionPane.showMessageDialog(frame, "You Won!");
        } else {
            JOptionPane.showMessageDialog(frame, "Try Again!");
        }
    }

    int[][] getBoard(){
        int[][] board = new int[9][9];
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                String text = cells[i][j].getText();
                board[i][j] = text.isEmpty() ? 0 : Integer.parseInt(text);
            }
        }
        return board;
    }

    void setBoard(int[][] board){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                cells[i][j].setText(String.valueOf(board[i][j]));
            }
        }
    }

    boolean solve(int[][] board){
        for(int row=0;row<9;row++){
            for(int col=0;col<9;col++){
                if(board[row][col]==0){
                    for(int num=1;num<=9;num++){
                        if(isSafe(board,row,col,num)){
                            board[row][col]=num;
                            if(solve(board)) return true;
                            board[row][col]=0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    boolean isSafe(int[][] board,int row,int col,int num){
        for(int i=0;i<9;i++){
            if(board[row][i]==num || board[i][col]==num)
                return false;
        }

        int sr=row-row%3, sc=col-col%3;

        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++)
                if(board[sr+i][sc+j]==num)
                    return false;

        return true;
    }

    boolean isValidBoard(int[][] board){
        for(int i=0;i<9;i++){
            boolean[] row = new boolean[10];
            boolean[] col = new boolean[10];

            for(int j=0;j<9;j++){
                int r = board[i][j];
                int c = board[j][i];

                if(r<1 || r>9 || row[r]) return false;
                if(c<1 || c>9 || col[c]) return false;

                row[r] = col[c] = true;
            }
        }

        for(int i=0;i<9;i+=3){
            for(int j=0;j<9;j+=3){
                boolean[] box = new boolean[10];
                for(int r=0;r<3;r++){
                    for(int c=0;c<3;c++){
                        int val = board[i+r][j+c];
                        if(val<1 || val>9 || box[val]) return false;
                        box[val] = true;
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        new SudokuGUI();
    }
}