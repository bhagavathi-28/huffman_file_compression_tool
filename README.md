# Huffman File Compression Tool

A Java-based implementation of file compression and decompression using the Huffman Coding algorithm.

## Overview

This project provides a simple and efficient tool to compress and decompress files using Huffman Coding. Huffman Coding is a lossless data compression algorithm that assigns variable-length codes to input characters, with shorter codes assigned to more frequent characters. This tool can significantly reduce the size of text files and can be used for educational purposes or as a utility in larger projects.

## Features

- **Compress** any text file using Huffman coding.
- **Decompress** previously compressed files to their original content.
- Simple command-line interface for ease of use.
- Prints compression percentage after successful compression.
- Written entirely in Java.

## Getting Started

### Prerequisites

- [Java JDK 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) or higher installed.
- Basic knowledge of the command line.

### Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/bhagavathi-28/huffman_file_compression_tool.git
   ```
2. **Navigate to the project directory:**
   ```sh
   cd huffman_file_compression_tool
   ```
3. **Compile the Java files:**
   ```sh
   javac -d out com/bhagavathi/huffman/*.java
   ```

### Usage

#### Compress a File

```sh
java -cp out com.bhagavathi.huffman.FileCompressNExtract --compress <input_file> <output_file>
```

- `<input_file>`: Path to the file you want to compress.
- `<output_file>`: Path where the compressed file will be saved.

#### Decompress a File

```sh
java -cp out com.bhagavathi.huffman.FileCompressNExtract --extract <compressed_file> <output_file>
```

- `<compressed_file>`: Path to the compressed (.cmp) file.
- `<output_file>`: Path where the decompressed file will be saved.

#### Example

```sh
java -cp out com.bhagavathi.huffman.FileCompressNExtract --compress sample.txt sample.cmp
java -cp out com.bhagavathi.huffman.FileCompressNExtract --extract sample.cmp decompressed_sample.txt
```

## Project Structure

- com.bhagavathi.huffman
  - FileCompressNExtract.java
  - node
     - HuffmanNode.java
     - HuffmanNodeComparator.java
  - extraction
     - FileExtractor.java
     - ExtractionData.java
  - compression
     - FileCompressor.java
     - CompressionData.java

## Contributing

Contributions are welcome! Please fork this repository and submit a pull request for any improvements or bug fixes.

## License

This project is licensed under the [MIT License](LICENSE).

## Contact

For any questions or feedback, please contact [bhagavathi-28](https://github.com/bhagavathi-28).
