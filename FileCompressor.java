import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class FileCompressor {

    private CompressionData cData;

    public FileCompressor(CompressionData cData) {
    this.cData = cData;
    }
    
    public FileCompressor() {
    }

    public void convertFileToString() throws IOException {
        String content = Files.readString(Path.of(this.cData.getInputFilePath()));
        this.cData.setFileContent(content);
    }

    public void addFileName(String inputFileName){
        String fileContent = inputFileName + this.cData.getFileContent();
        this.cData.setFileContent(fileContent);
        int fileNameSize = inputFileName.length();
        this.cData.setFileNameLen(fileNameSize);
    } 

    public void countCharFreq(){
        String content = this.cData.getFileContent();
        Map<Character,Integer> freqMap = new HashMap<>();
        for(int i=0;i<content.length();i++){
            char curr = content.charAt(i);
            int freq = freqMap.getOrDefault(curr,0);
            freqMap.put(curr,freq+1);
        }
        this.cData.setFreqMap(freqMap);
    }

    public void createMinheap(){
        Map<Character,Integer> freqMap = this.cData.getFreqMap();
        PriorityQueue<HuffmanNode> nodes = new PriorityQueue<>(new HuffmanNodeComparator());
        for(Character key : freqMap.keySet()){
            int value = freqMap.get(key);
            nodes.add(new HuffmanNode(key,value));
        }
        this.cData.setNodes(nodes);
    }

    public void buildHuffmanTree(){
        PriorityQueue<HuffmanNode> nodes = this.cData.getNodes();
        while(nodes.size()>1){
          HuffmanNode n1 = nodes.poll();
          HuffmanNode n2 = nodes.poll();
          int totalFreq = n1.getFrequency() + n2.getFrequency();
          HuffmanNode newNode = new HuffmanNode(totalFreq);
          newNode.setLeft(n1);
          newNode.setRight(n2);
          nodes.add(newNode);
        }
        HuffmanNode root = nodes.poll();
        this.cData.setRoot(root);
    }

    public void traverseCompressionTree(HuffmanNode node , String code , Map<Character,String>codes){
        if(node.isLeaf()) {
            codes.put(node.getCharacter(), code);
            return;
        }
        traverseCompressionTree(node.getLeft(),code+"0",codes);
        traverseCompressionTree(node.getRight(),code+"1",codes);
    }


    public void generateCharCodes(){
        HuffmanNode root = this.cData.getRoot();
        Map<Character,String>codes = new HashMap<>();
        traverseCompressionTree(root,"",codes);
        this.cData.setCodes(codes);
    }
    
    public void encodeContent (){
        String content = this.cData.getFileContent();
        Map<Character,String>codes = this.cData.getCodes();
        BitSet contentBits = new BitSet();
        int bitIndex = 0;
        for(int i=0;i<content.length();i++){
            String code = codes.get(content.charAt(i));
            for (char bit : code.toCharArray()){
                if(bit == '1'){
                    contentBits.set(bitIndex);
                }
                bitIndex++;
            }
        }
        this.cData.setEncodedBitLen(bitIndex);
        this.cData.setContentBits(contentBits);
    }

    public void encodeToBytes() throws IOException {
        int len = this.cData.getEncodedBitLen();
        int bytesLen = (len + 7)/8;
        int paddedBits = (8 - (len % 8))%8;
        this.cData.setPaddedBits(paddedBits);
        byte contentInBytes[] = new byte[bytesLen];
        BitSet content = this.cData.getContentBits();
        for(int i=0;i<len;i++) {
            if(content.get(i)){
              contentInBytes[i/8] = (byte) (contentInBytes[i/8] | (1 << (7-(i%8)))) ;
            }
        }
       writeCompressedContentToFile(contentInBytes);
    }
    
    public void writeCompressedContentToFile(byte[] contentInBytes) throws IOException {
    int paddedBits = this.cData.getPaddedBits();
    Map<Character, Integer> freqMap = this.cData.getFreqMap();
    String outputPath = this.cData.getOutputFilePath();
    int fileNameLen = this.cData.getFileNameLen();
    try (DataOutputStream output = new DataOutputStream(new FileOutputStream(outputPath))) {
        // Write metadata
        output.writeInt(fileNameLen);
        output.writeInt(paddedBits);
        output.writeInt(freqMap.size());

        // Write frequency map (char + int)
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            output.writeChar(entry.getKey());     // 2 bytes
            output.writeInt(entry.getValue());    // 4 bytes
        }

        // Write the encoded compressed data
        output.write(contentInBytes); // variable size

        System.out.println("File successfully compressed.");
    }
}

    public CompressionData getcData() {
        return cData;
    }

    public void setcData(CompressionData cData) {
        this.cData = cData;
    }









}
