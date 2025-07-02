package com.bhagavathi.huffman;
import com.bhagavathi.huffman.compression.CompressionData;
import com.bhagavathi.huffman.compression.FileCompressor;
import com.bhagavathi.huffman.extraction.ExtractionData;
import com.bhagavathi.huffman.extraction.FileExtractor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileCompressNExtract {

    public static void main(String[] args) {
        // read from the command line arguments 
        if (args.length < 1) {
            System.out.println("Usage:");
            System.out.println("  --compress <inputFile> <outputFile>");
            System.out.println("  --extract <inputFile> <outputFileDir>");
            System.out.println("  --help");
            return;
        }
        // --compress to compress the file , --extract to extract the compressed file 
        switch (args[0]) {
            case "--compress":
                if (args.length != 3) {
                    System.out.println("Usage: --compress <inputFile> <outputFile>");
                    return;
                }
                compressFile(args[1], args[2]);
                break;

            case "--extract":
                if (args.length != 3) {
                    System.out.println("Usage: --extract <inputFile> <outputFileDir>");
                    return;
                }
                decompressFile(args[1], args[2]);
                break;

            case "--help":
                System.out.println("Huffman Compression CLI");
                System.out.println("  --compress <inputFile> <outputFile>  Compresses the file.");
                System.out.println("  --extract <inputFile> <outputFileDir>   Extracts the compressed file to the directory.");
                break;

            default:
                System.out.println("Unknown command. Use --help for usage instructions.");
        }
    }
    

    private static void compressFile(String inputPath,String outputPath) {
        try {  
            if (!new File(inputPath).exists()) {
                throw new FileNotFoundException("Error: Input file not found: " + inputPath);
            }
            int lastSlash = inputPath.lastIndexOf('/');
            if (lastSlash == -1) lastSlash = inputPath.lastIndexOf('\\'); // handle Windows path
            String inputFileName = inputPath.substring(lastSlash + 1);
            int lastDot = outputPath.lastIndexOf('.');
            String outputFileExt = outputPath.substring(lastDot + 1);
            // files with these extension are not supported by compression using huffman encoding 
            String[] unsupportedExtensions = {
            ".zip", ".gz", ".rar", ".7z", ".xz", ".bz2",
            ".jpg", ".jpeg", ".png", ".gif",
            ".mp3", ".mp4", ".mkv", ".avi", ".webm",
            ".gpg", ".enc"
            };
            for (String ext : unsupportedExtensions) {
                //incase of unsupported extensions throw error 
                if (inputFileName.toLowerCase().endsWith(ext)) {
                    throw new RuntimeException("Huffman compression not supported for file type: " + ext);
                }
            }

            if (!outputFileExt.equalsIgnoreCase("cmp")) {
                // make sure you save the compressed content in a file with ext ".cmp" else throw error
                throw new RuntimeException("Output file type not matched: " + outputFileExt);
            }
            // create object to store the compression data 
            CompressionData cData = new CompressionData();
            // save input and output file paths for processing 
            cData.setInputFilePath(inputPath);
            cData.setOutputFilePath(outputPath);
            FileCompressor compressFile = new FileCompressor(cData);
            // save the file content as string 
            compressFile.convertFileToString();
            // add the filename to the content 
            compressFile.addFileName(inputFileName);
            // count character frequency 
            compressFile.countCharFreq();
            // create min heap object with char and their freq saved usinf huffmannode objects 
            compressFile.createMinheap();
            // build tree using huffman nodes for creating char encoding 
            compressFile.buildHuffmanTree();
            // generate codes for the characters 
            compressFile.generateCharCodes();
            // encode the content using generated codes 
            compressFile.encodeContent();
            // convert the encoded content to bytes and write to the output file 
            compressFile.encodeToBytes();

        }
        catch (FileNotFoundException e){
            System.err.println(e.getMessage());
            System.out.println("Program is exiting .....");
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.println("Program is exiting .....");
            System.exit(1);
        }
        catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.out.println("Program is exiting .....");
            System.exit(1);
        }
    }

    private static void decompressFile(String inputFilePath,String outputDir) {
        try {
            if (!new File(inputFilePath).exists()) {
                throw new FileNotFoundException("Error: Input file not found: " + inputFilePath);
            }
            // make sure you try to extract the file with ".cmp" extension
            if (!inputFilePath.endsWith(".cmp")) {
            System.out.println("File type isn't supported for inflation");
            System.exit(1);
            }
            // create eData to store the extraction data
            ExtractionData eData = new ExtractionData();
            eData.setInputFilePath(inputFilePath);
            eData.setOutputFileDir(outputDir);
            FileExtractor extractFile = new FileExtractor(eData);
            // read the data from the compressed file and retrieve the char freq
            extractFile.readCompressedFile();
            CompressionData cData = new CompressionData();
            FileCompressor extractFromCompress = new FileCompressor(cData);
            cData.setFreqMap(extractFile.geteData().getFreqMap());
            // create the min heap from the Huffman Nodes 
            extractFromCompress.createMinheap();
            eData.setNodes(cData.getNodes());
            // build huffman compression tree to generate codes 
            extractFromCompress.buildHuffmanTree();
            eData.setRoot(cData.getRoot());
            // use the codes to convert bit sequence to the encoded char sequence
            extractFile.decodeBitsToChars();
            // extract the original file name from the char sequence 
            extractFile.extractOriginalFileName();
            // write the content to the file with original name 
            extractFile.writeDecodedContentToFile();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.println("Program is exiting .....");
            System.exit(1);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.out.println("Program is exiting .....");
            System.exit(1);
        }
    }
}