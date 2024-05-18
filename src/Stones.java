import java.util.List;
import java.util.ArrayList;

public class Stones {
    private final List<Integer> stones = new ArrayList<>();
    private final int numPlayers;

    public Stones(int numPlayers) {

        this.numPlayers = numPlayers;
        this.newGame();
    }

    private void newGame() {
        int numStacks = (int) (Math.random() * (this.numPlayers - 1)) + 2;

        for (int i = 0; i < numStacks; i++) {
            int numStones = (int) (Math.random() * 5) + 2;
            this.stones.add(numStones);
        }
    }
    
    public String toString() {
        int maxStackHeight = -1;

        for (int stack : this.stones) {
            if (stack > maxStackHeight) {
                maxStackHeight = stack;
            }
        }

        int[][] stacks = new int[this.stones.size()][maxStackHeight];

        for (int i = 0; i < this.stones.size(); i++) {
            for (int j = 0; j < this.stones.get(i); j++) {
                stacks[i][j] = 1;
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append("STONES\n\n");
        
        for (int i = maxStackHeight - 1; i >= 0; i--) {
            for (int j = 0; j < this.stones.size(); j++) {
                if (stacks[j][i] == 1) {
                    sb.append("O ");
                } else {
                    sb.append("  ");
                }
            }

            sb.append("\n");
        }
        
        sb.append("\n");

        for (int i = 0; i < this.stones.size(); i++) {
            sb.append((i + 1) + " ");
        }

        return sb.append("\n").toString();  
    }

    public boolean removeStones(int stack, int numStones) {
        if (stack < 0 || stack >= this.stones.size()) {
            return false;
        }

        if (numStones < 1 || numStones > this.stones.get(stack)) {
            return false;
        }

        this.stones.set(stack, this.stones.get(stack) - numStones);

        return true;
    }

    public boolean isGameOver() {
        for (int stack : this.stones) {
            if (stack > 0) {
                return false;
            }
        }

        return true;
    }
}
