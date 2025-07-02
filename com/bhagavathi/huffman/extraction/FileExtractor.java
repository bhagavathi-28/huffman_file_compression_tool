package com.bhagavathi.huffman.extraction;
import com.bhagavathi.huffman.node.HuffmanNode;
import java.io.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class FileExtractor {
    
    private ExtractionData eData;

    public FileExtractor(ExtractionData eData) {
        this.eData = eData;
    }
    
    public FileExtractor() {
    }

    public void readCompressedFile() throws IOException {    
        String filePath = this.eData.getInputFilePath();
        BitSet content = new BitSet();
        Map<Character,Integer> freqMap = new HashMap<>();
        File compressedFile = new File(filePath);
        FileInputStream readInput = new FileInputStream(compressedFile);
        try (DataInputStream data = new DataInputStream(readInput)) {
            this.eData.setFileNameSize(data.readInt());
            this.eData.setPaddedBits(data.readInt());
            int lenOfMap = data.readInt();
            // read the encoded chars and their frequencies 
            for(int i=1;i<=lenOfMap;i++){
               char key = data.readChar();
               int freq = data.readInt();
               freqMap.put(key,freq);
            }
            // read the encoded content of the file and convert to bitset
            this.eData.setEncodedBitLen(0);
            while(data.available()>0){
                appendByteToBitSet(data.readByte(),content);
            }
            //remove padded bits from the bitset 
            int originalLen = this.eData.getEncodedBitLen() - this.eData.getPaddedBits();
            this.eData.setEncodedBitLen(originalLen);
        }
        // store the freq map and bitset to the ExtractionData object 
        this.eData.setFreqMap(freqMap);
        this.eData.setContent(content);
    }
    public void appendByteToBitSet(byte b , BitSet content){
        // create each bit from the byte and store it in the bitset 
        for(int i=7;i>=0;i--){
            int val = (b>>i)&1;
            int currBitIndex = this.eData.getEncodedBitLen();
            if(val==1){
                content.set(currBitIndex);
            }
            this.eData.setEncodedBitLen(currBitIndex+1);
        }
    }
    public void decodeBitsToChars(){
        BitSet content = this.eData.getContent();
        HuffmanNode root = this.eData.getRoot();
        StringBuilder original = new StringBuilder();
        // this is the total length of bit encoding it is different from bitset size which ignores the trailing zeroes
        int bitStreamLen = this.eData.getEncodedBitLen();
        int ind = 0;
        HuffmanNode curr;
        // traverse each bit till you find leaf node and once you reach append that character to the decoded content
        while(ind<bitStreamLen){
            curr = root;
            while(ind < bitStreamLen && !curr.isLeaf()){
                // if that bit is set traverse to the right
                if(content.get(ind)){
                   curr = curr.getRight();
                }
                // else traverse to the left 
                else{
                   curr = curr.getLeft();
                }
                ind++;
            }
            original.append(curr.getCharacter());
        }
        // save the decoded string to the object 
        this.eData.setFileContent(original.toString());
    }

    public void extractOriginalFileName(){
        // get the original file name from the content string using file name length property 
        int fileNameSize = this.eData.getFileNameSize();
        String originalContent = this.eData.getFileContent();
        String fileName = originalContent.substring(0, fileNameSize);
        originalContent = originalContent.substring(fileNameSize);
        this.eData.setFileContent(originalContent);
        this.eData.setOriginalFileName(fileName);
    }

  public void writeDecodedContentToFile() throws IOException {
        String content = this.eData.getFileContent();
        String fileDir = this.eData.getOutputFileDir();
        // get the file path by using output dir and original file name
        String outputFile = fileDir + this.eData.getOriginalFileName();
        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(content);
        }
        System.out.println("Successfully written to the file.");
   } 

    public ExtractionData geteData() {
        return eData;
    }

    public void seteData(ExtractionData eData) {
        this.eData = eData;
    }



}
