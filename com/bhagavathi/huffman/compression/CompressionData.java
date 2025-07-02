package com.bhagavathi.huffman.compression;
import java.util.BitSet;
import java.util.Map;
import java.util.PriorityQueue;
import com.bhagavathi.huffman.node.HuffmanNode;

public class CompressionData {
    int encodedBitLen;
    int fileNameLen;
    int paddedBits;
    String inputFilePath;
    String outputFilePath;
    String fileContent;
    Map<Character, Integer> freqMap;
    Map<Character, String> codes;
    HuffmanNode root;
    PriorityQueue<HuffmanNode> nodes;
    BitSet contentBits;


    public String getFileContent() {
        return fileContent;
    }
    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }
    public int getEncodedBitLen() {
        return encodedBitLen;
    }
    public void setEncodedBitLen(int encodedBitLen) {
        this.encodedBitLen = encodedBitLen;
    }
    public String getInputFilePath() {
        return inputFilePath;
    }
    public void setInputFilePath(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }
    public String getOutputFilePath() {
        return outputFilePath;
    }
    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }
    public int getFileNameLen() {
        return fileNameLen;
    }
    public void setFileNameLen(int fileNameLen) {
        this.fileNameLen = fileNameLen;
    }
    public int getPaddedBits() {
        return paddedBits;
    }
    public void setPaddedBits(int paddedBits) {
        this.paddedBits = paddedBits;
    }
    public Map<Character, Integer> getFreqMap() {
        return freqMap;
    }
    public void setFreqMap(Map<Character, Integer> freqMap) {
        this.freqMap = freqMap;
    }
    public BitSet getContentBits() {
        return contentBits;
    }
    public void setContentBits(BitSet contentBits) {
        this.contentBits = contentBits;
    }
    public Map<Character, String> getCodes() {
        return codes;
    }
    public void setCodes(Map<Character, String> codes) {
        this.codes = codes;
    }
    public HuffmanNode getRoot() {
        return root;
    }
    public void setRoot(HuffmanNode root) {
        this.root = root;
    }
    public PriorityQueue<HuffmanNode> getNodes() {
        return nodes;
    }
    public void setNodes(PriorityQueue<HuffmanNode> nodes) {
        this.nodes = nodes;
    }
    
    
    

}
