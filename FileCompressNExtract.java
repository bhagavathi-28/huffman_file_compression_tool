import java.io.IOException;
import java.util.Scanner;

public class FileCompressNExtract {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Type {cmp} for compressing the file and {ext} for extraction : ");
            String function = scanner.nextLine().trim().toLowerCase();
            
            switch (function) {
                case "cmp" -> handleCompression(scanner);
                case "ext" -> handleExtraction(scanner);
                default -> {
                    System.err.println("Command not found");
                    System.out.println("Program is exiting .....");
                    System.exit(1);
                }
            }
        }
    }

    private static void handleCompression(Scanner scanner) {
        System.out.print("Enter file path: ");
        String inputPath = scanner.nextLine().trim();

        System.out.print("Enter the compressed file save path: ");
        String outputPath = scanner.nextLine().trim();

        int lastSlash = inputPath.lastIndexOf('/');
        if (lastSlash == -1) lastSlash = inputPath.lastIndexOf('\\'); // handle Windows path
        String inputFileName = inputPath.substring(lastSlash + 1);

        int lastDot = outputPath.lastIndexOf('.');
        String outputFileExt = outputPath.substring(lastDot + 1);

        String[] unsupportedExtensions = {
            ".zip", ".gz", ".rar", ".7z", ".xz", ".bz2",
            ".jpg", ".jpeg", ".png", ".gif",
            ".mp3", ".mp4", ".mkv", ".avi", ".webm",
            ".gpg", ".enc"
        };

        try {
            for (String ext : unsupportedExtensions) {
                if (inputFileName.toLowerCase().endsWith(ext)) {
                    throw new RuntimeException("Huffman compression not supported for file type: " + ext);
                }
            }

            if (!outputFileExt.equalsIgnoreCase("cmp")) {
                throw new RuntimeException("Output file type not matched: " + outputFileExt);
            }
            CompressionData cData = new CompressionData();
            cData.setInputFilePath(inputPath);
            cData.setOutputFilePath(outputPath);
            FileCompressor compressFile = new FileCompressor(cData);
            compressFile.convertFileToString();
            compressFile.addFileName(inputFileName);
            compressFile.countCharFreq();
            compressFile.createMinheap();
            compressFile.buildHuffmanTree();
            compressFile.generateCharCodes();
            compressFile.encodeContent();
            compressFile.encodeToBytes();

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

    private static void handleExtraction(Scanner scanner) {
        System.out.print("Enter file to be inflated: ");
        String inputFilePath = scanner.nextLine().trim();

        if (!inputFilePath.endsWith(".cmp")) {
            System.out.println("File type isn't supported for inflation");
            System.exit(1);
        }

        System.out.print("Enter output directory: ");
        String outputDir = scanner.nextLine().trim();

        try {
            ExtractionData eData = new ExtractionData();
            eData.setInputFilePath(inputFilePath);
            eData.setOutputFileDir(outputDir);
            FileExtractor extractFile = new FileExtractor(eData);
            extractFile.readCompressedFile();
            CompressionData cData = new CompressionData();
            FileCompressor extractFromCompress = new FileCompressor(cData);
            cData.setFreqMap(extractFile.geteData().getFreqMap());
            extractFromCompress.createMinheap();
            eData.setNodes(cData.getNodes());
            extractFromCompress.buildHuffmanTree();
            eData.setRoot(cData.getRoot());
            extractFile.decodeBitsToChars();
            extractFile.extractOriginalFileName();
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