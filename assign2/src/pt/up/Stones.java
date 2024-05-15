package pt.up;

import java.util.List;
import java.util.ArrayList;

/**
 * A class to represent a game of Stones.
 * The game starts with a random number of stacks and stones.
 * Players take turns removing a number of stones from a stack.
 * The game ends when all the stones have been removed.
 * The player who removes the last stone(s) wins.
 */
public class Stones {

    private final List<Integer> stones = new ArrayList<>();
    private final int numPlayers;
    private int currentPlayer = 0;

    /**
     * Constructor for the Stones class.
     *
     * @param numPlayers The number of players in the game.
     */
    public Stones(int numPlayers) {

        this.numPlayers = numPlayers;
        this.newGame();
    }

    /**
     * Initializes a new game of Stones.
     * Randomly generates a number of stacks and stones.
     */
    private void newGame() {

        // Generate a number of stacks 2-numPlayers
        int numStacks = (int) (Math.random() * (this.numPlayers - 1)) + 2;

        for (int i = 0; i < numStacks; i++) {
            // Generate a number of stones 1-5
            int numStones = (int) (Math.random() * 5) + 1;
            this.stones.add(numStones);
        }
    }

    /**
     * Checks if the game is over.
     * The game is over when all the stacks are empty.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {

      for (int stack : this.stones) {
          if (stack > 0) {
              return false;
          }
      }

      return true;
    }

    /**
     * Gets the current player.
     *
     * @return The current player.
     */
    public int getCurrentPlayer() {
        
        return this.currentPlayer;
    }

    /**
     * Advances to the next player.
     */
    public void nextPlayer() {
        
        this.currentPlayer = (this.currentPlayer + 1) % this.numPlayers;
    }

    /**
     * Checks if a move is valid.
     * A move is valid if the stack exists and has enough stones.
     * 
     * @param stack The stack to remove stones from.
     * @param numStones The number of stones to remove.
     * @return True if the move is valid, false otherwise.
     */
    public boolean isValidMove(int stack, int numStones) {
        
        return stack >= 0 && stack < this.stones.size() && numStones > 0 && numStones <= this.stones.get(stack);
    }

    /**
     * Removes a number of stones from a given stack.
     *
     * @param stack The stack to remove stones from.
     * @param numStones The number of stones to remove.
     */
    public void removeStones(int stack, int numStones) {
        
        this.stones.set(stack, this.stones.get(stack) - numStones);
    }

    /**
     * Prints the current state of the game.
     */
    public void printStacks() {
        
        // Find the maximum stack height
        int maxStackHeight = 0;
        for (int stack : this.stones) {
            if (stack > maxStackHeight) {
                maxStackHeight = stack;
            }
        }

        // Create matrix of stacks and stones
        int[][] stacks = new int[this.stones.size()][maxStackHeight];

        // Fill the matrix with stones (1 = stone, 0 = empty)
        for (int i = 0; i < this.stones.size(); i++) {
            for (int j = 0; j < this.stones.get(i); j++) {
                stacks[i][j] = 1;
            }
        }

        // Print the stacks
        for (int i = maxStackHeight - 1; i >= 0; i--) {
            for (int j = 0; j < this.stones.size(); j++) {
                if (stacks[j][i] == 1) {
                    System.out.print("o  ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }

        // Print the stack numbers
        for (int i = 0; i < this.stones.size(); i++) {
            System.out.print((i + 1) + "  ");
        }

        System.out.println();
    }  
}
