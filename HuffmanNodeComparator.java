import java.util.Comparator;

public class HuffmanNodeComparator implements Comparator<HuffmanNode> {
    @Override
    public int compare(HuffmanNode n1, HuffmanNode n2) {
        // Compare based on frequency (ascending order)
        return Integer.compare(n1.getFrequency(), n2.getFrequency());
    }
}
