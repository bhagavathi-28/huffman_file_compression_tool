import java.util.BitSet;
import java.util.Map;
import java.util.PriorityQueue;

public class ExtractionData {
    int fileNameSize;
    int paddedBits;
    int encodedBitLen;
    String outputFileDir;
    String originalFileName;
    String fileContent;
    String inputFilePath;
    BitSet content;
    Map<Character,Integer> freqMap;
    PriorityQueue<HuffmanNode> nodes;
    HuffmanNode root;
    
    public String getInputFilePath() {
        return inputFilePath;
    }
    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }
    public BitSet getContent() {
        return content;
    }
    
    public void setContent(BitSet content) {
        this.content = content;
    }

    public Map<Character, Integer> getFreqMap() {
        return freqMap;
    }
    public void setFreqMap(Map<Character, Integer> freqMap) {
        this.freqMap = freqMap;
    }
    
    public int getEncodedBitLen() {
        return encodedBitLen;
    }
    public void setEncodedBitLen(int encodedBitLen) {
        this.encodedBitLen = encodedBitLen;
    }
    public int getFileNameSize() {
        return fileNameSize;
    }
    public void setFileNameSize(int fileNameSize) {
        this.fileNameSize = fileNameSize;
    }
    public int getPaddedBits() {
        return paddedBits;
    }
    public void setPaddedBits(int paddedBits) {
        this.paddedBits = paddedBits;
    }
    public String getOutputFileDir() {
        return outputFileDir;
    }
    public void setOutputFileDir(String outputFileDir) {
        this.outputFileDir = outputFileDir;
    }

    public PriorityQueue<HuffmanNode> getNodes() {
        return nodes;
    }

    public void setNodes(PriorityQueue<HuffmanNode> nodes) {
        this.nodes = nodes;
    }

    public HuffmanNode getRoot() {
        return root;
    }

    public void setRoot(HuffmanNode root) {
        this.root = root;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
}
