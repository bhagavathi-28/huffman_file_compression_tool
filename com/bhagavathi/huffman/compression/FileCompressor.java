package com.bhagavathi.huffman.compression;
import com.bhagavathi.huffman.node.HuffmanNode;
import com.bhagavathi.huffman.node.HuffmanNodeComparator;
import java.io.DataOutputStream;
import java.io.File;
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
        // read the file content as a string 
        String content = Files.readString(Path.of(this.cData.getInputFilePath()));
        if(content.isEmpty()){
            throw new IOException("Error: Input file is empty. Aborting compression.");
        }
        this.cData.setFileContent(content);
    }

    public void addFileName(String inputFileName){
        // read the file name and add it to the original file content 
        String fileContent = inputFileName + this.cData.getFileContent();
        this.cData.setFileContent(fileContent);
        int fileNameSize = inputFileName.length();
        this.cData.setFileNameLen(fileNameSize);
    } 

    public void countCharFreq(){
        // count the char frequency from the content string 
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
        // create Huffman nodes using chars and their frequencies and add them to minHeap 
        Map<Character,Integer> freqMap = this.cData.getFreqMap();
        // heap is sorted using custom comparator that uses frequency for sorting 
        PriorityQueue<HuffmanNode> nodes = new PriorityQueue<>(new HuffmanNodeComparator());
        for(Character key : freqMap.keySet()){
            int value = freqMap.get(key);
            nodes.add(new HuffmanNode(key,value));
        }
        this.cData.setNodes(nodes);
    }

    public void buildHuffmanTree(){
        PriorityQueue<HuffmanNode> nodes = this.cData.getNodes();
        // build the huffman compression tree using huffman algorithm 
        while(nodes.size()>1){
          // find two nodes with min frequencies in the heap and remove from the heap 
          HuffmanNode n1 = nodes.poll();
          HuffmanNode n2 = nodes.poll();
          int totalFreq = n1.getFrequency() + n2.getFrequency();
          // create new huffman node with the sum of their frequencies
          HuffmanNode newNode = new HuffmanNode(totalFreq);
          // set the left and right pointers of the new sum node to the removed nodes 
          newNode.setLeft(n1);
          newNode.setRight(n2);
          // add this new node to the heap 
          nodes.add(newNode);
        }
        HuffmanNode root = nodes.poll();
        // save the root node of the tree 
        this.cData.setRoot(root);
    }

    public void traverseCompressionTree(HuffmanNode node , String code , Map<Character,String>codes){
        // add to the map once you reach the root
        if(node.isLeaf()) {
            codes.put(node.getCharacter(), code);
            return;
        }
        // if you traverse left add '0' to the code and '1' if you traverse right 
        traverseCompressionTree(node.getLeft(),code+"0",codes);
        traverseCompressionTree(node.getRight(),code+"1",codes);
    }


    public void generateCharCodes(){
        //generate the codes for each character and add to the map codes 
        HuffmanNode root = this.cData.getRoot();
        Map<Character,String>codes = new HashMap<>();
        traverseCompressionTree(root,"",codes);
        this.cData.setCodes(codes);
    }
    
    public void encodeContent (){
        String content = this.cData.getFileContent();
        Map<Character,String>codes = this.cData.getCodes();
        BitSet contentBits = new BitSet();
        // record the total bit length as the trailing zeroes would be ignored in bitset
        // so manually track the bit length
        int bitIndex = 0;
        // traverse each character of the content 
        for(int i=0;i<content.length();i++){
            // find the code of each character 
            String code = codes.get(content.charAt(i));
            // save the code to bitset by reading each bit from char code
            for (char bit : code.toCharArray()){
                if(bit == '1'){
                    contentBits.set(bitIndex);
                }
                bitIndex++;
            }
        }
        // save the total bit length and bitset 
        this.cData.setEncodedBitLen(bitIndex);
        this.cData.setContentBits(contentBits);
    }

    public void encodeToBytes() throws IOException {
    // Get the total number of encoded bits in the compressed data
    int len = this.cData.getEncodedBitLen();

    // Calculate the number of bytes required to store these bits
    int bytesLen = (len + 7) / 8; // Add 7 to round up for partial bytes

    // Calculate how many padding bits are needed to make the last byte full
    int paddedBits = (8 - (len % 8)) % 8;

    // Store the number of padded bits in cData for later use during extraction
    this.cData.setPaddedBits(paddedBits);

    // Create a byte array to hold the final byte-encoded compressed data
    byte contentInBytes[] = new byte[bytesLen];

    // Retrieve the BitSet containing the encoded content bits
    BitSet content = this.cData.getContentBits();

    // Iterate through each bit in the BitSet
    for (int i = 0; i < len; i++) {
        // If the bit at position i is set, update the corresponding bit in the byte array
        if (content.get(i)) {
            // Calculate the correct byte and bit position within that byte, and set the bit using bitwise OR
            contentInBytes[i / 8] = (byte) (contentInBytes[i / 8] | (1 << (7 - (i % 8))));
        }
    }

    // Write the compressed byte array to the output file
    writeCompressedContentToFile(contentInBytes);
   }
    public int calculateCompressionRatio(){
        File inputFile = new File(this.cData.getInputFilePath());
        File outputFile = new File(this.cData.getOutputFilePath());
        long inputFileSize = inputFile.length();
        long compressedFileSize = outputFile.length();
        double ratio = (double)compressedFileSize/inputFileSize;
        double percentage = (1-ratio)*100;
        return (int)percentage;
    }
    public void writeCompressedContentToFile(byte[] contentInBytes) throws IOException {
    int paddedBits = this.cData.getPaddedBits();
    Map<Character, Integer> freqMap = this.cData.getFreqMap();
    String outputPath = this.cData.getOutputFilePath();
    int fileNameLen = this.cData.getFileNameLen();
    // open the output file and write the compressed byte data 
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
        int compressionPercent = this.calculateCompressionRatio();
        System.out.println("File successfully compressed (deflated "+compressionPercent+"%)");
    }
}

    public CompressionData getcData() {
        return cData;
    }

    public void setcData(CompressionData cData) {
        this.cData = cData;
    }









}
