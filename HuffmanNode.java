public class HuffmanNode {
     private char character;
     private HuffmanNode left;
     private HuffmanNode right;
     private int frequency;

    public HuffmanNode() {
    }

    public HuffmanNode(int frequency) {
        this.frequency = frequency;
    }

    public HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public HuffmanNode getLeft() {
        return left;
    }

    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    public HuffmanNode getRight() {
        return right;
    }

    public void setRight(HuffmanNode right) {
        this.right = right;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public boolean isLeaf() {
        return (left == null && right == null);
    }

    @Override
    public String toString() {
        return "HuffmanNode{" +
                "character=" + character +
                ", left=" + left +
                ", right=" + right +
                ", frequency=" + frequency +
                '}';
    }

}
