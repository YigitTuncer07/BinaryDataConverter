import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

class Main {

    // big -> 0; little -> 1
    public static byte endianType;
    // int -> 0; unsigned -> 1; float -> 2
    public static byte dataType;
    public static byte size;

    public static void main(String[] args) throws FileNotFoundException {

        // Take file input
        Scanner scanner = new Scanner(System.in);
        System.out.println("File Name:");
        String fileName = scanner.nextLine();
        File file = new File(fileName);

        // Get File Length
        int fileLengthY = 0;
        try {
            fileLengthY = getFileLengthY(file);
        } catch (IOException ignore) {
        }

        // Takes hexadecimals from input and puts them into decArray
        short[][] decArray = new short[fileLengthY][12];
        short i = 0;
        String tempString;
        String[] tempStrings;
        try {
            Scanner scanner1 = new Scanner(file);
            while (scanner1.hasNext()) {
                tempString = scanner1.nextLine();
                tempStrings = tempString.split(" ");
                for (int j = 0; j < 12; j++) {
                    decArray[i][j] = convertHexToShort(tempStrings[j]);
                }
                i++;
            }
            scanner1.close();
        } catch (FileNotFoundException e) {
            System.out.println("FILENOTFOUND");
            scanner.close();
            return;
        }

        // Gets byte ordering
        System.out.println("Byte Ordering: ");
        tempString = scanner.nextLine();
        if (tempString.equals("b")) {
            endianType = 0;
        } else if (tempString.equals("l")) {
            endianType = 1;
        } else {
            System.out.println("Unkown Input");
            scanner.close();
            return;
        }

        // Gets data type
        System.out.println("Data Type: ");
        tempString = scanner.nextLine();
        if (tempString.equals("int")) {
            dataType = 0;
        } else if (tempString.equals("unsigned")) {
            dataType = 1;
        } else if (tempString.equals("float")) {
            dataType = 2;
        } else {
            System.out.println("Unkown Input");
            scanner.close();
            return;
        }

        // Gets Byte Size
        System.out.println("Byte Size: ");
        tempString = scanner.next();
        scanner.close();
        if (tempString.equals("1")) {
            size = 1;
        } else if (tempString.equals("2")) {
            size = 2;
        } else if (tempString.equals("3")) {
            size = 3;
        } else if (tempString.equals("4")) {
            size = 4;
        } else {
            System.out.println("Unkown Input");
            return;
        }

        String binaryNumbers[][] = new String[fileLengthY][12 / size];
        long results[][] = new long[fileLengthY][12 / size];

        switch (size) {

            case 1:
                for (i = 0; i < fileLengthY; i++) {
                    for (int j = 0; j < 12; j++) {
                        binaryNumbers[i][j] = convertShortToBinary(decArray[i][j]);
                    }
                }

                if (dataType == 1) {// For unsigned

                    for (i = 0; i < fileLengthY; i++) {
                        for (int j = 0; j < 12 / size; j++) {
                            results[i][j] = convertBinaryToUnsignedLong(binaryNumbers[i][j]);
                        }
                    }

                } else if (dataType == 0) {// For Signed
                    for (i = 0; i < fileLengthY; i++) {
                        for (int j = 0; j < 12 / size; j++) {
                            results[i][j] = convertBinaryToSignedLong(binaryNumbers[i][j]);
                        }
                    }
                } else {// For float
                    printBinaryToDoubleToFile(binaryNumbers, 4, fileLengthY);

                }
                printStringArray2D(binaryNumbers);

                break;

            case 2:
                for (i = 0; i < fileLengthY; i++) {
                    for (int j = 0; j < 12; j += 2) {
                        // big
                        if (endianType == 0) {
                            binaryNumbers[i][j / 2] = convertShortToBinary(decArray[i][j])
                                    + convertShortToBinary(decArray[i][j + 1]);
                        } else { // little
                            binaryNumbers[i][j / 2] = convertShortToBinary(decArray[i][j + 1])
                                    + convertShortToBinary(decArray[i][j]);
                        }
                    }
                }

                if (dataType == 1) {// If it is unsigned
                    for (i = 0; i < fileLengthY; i++) {
                        for (int j = 0; j < 12 / size; j++) {
                            results[i][j] = convertBinaryToUnsignedLong(binaryNumbers[i][j]);
                        }
                    }
                } else if (dataType == 0) {// For Signed
                    for (i = 0; i < fileLengthY; i++) {
                        for (int j = 0; j < 12 / size; j++) {
                            results[i][j] = convertBinaryToSignedLong(binaryNumbers[i][j]);
                        }
                    }
                } else {// For Float
                    printBinaryToDoubleToFile(binaryNumbers, 6, fileLengthY);
                }
                printStringArray2D(binaryNumbers);
                break;

            case 3:
                for (i = 0; i < fileLengthY; i++) {
                    for (int j = 0; j < 12; j += 3) {

                        if (endianType == 0) {
                            binaryNumbers[i][j / 3] = convertShortToBinary(decArray[i][j])
                                    + convertShortToBinary(decArray[i][j + 1])
                                    + convertShortToBinary(decArray[i][j + 2]);
                        } else {
                            binaryNumbers[i][j / 3] = convertShortToBinary(decArray[i][j + 2])
                                    + convertShortToBinary(decArray[i][j + 1]) + convertShortToBinary(decArray[i][j]);

                        }
                    }
                }

                if (dataType == 1) {// For unsigned
                    for (i = 0; i < fileLengthY; i++) {
                        for (int j = 0; j < 12 / size; j++) {
                            results[i][j] = convertBinaryToUnsignedLong(binaryNumbers[i][j]);
                        }
                    }

                } else if (dataType == 0) {// For Signed
                    for (i = 0; i < fileLengthY; i++) {
                        for (int j = 0; j < 12 / size; j++) {
                            results[i][j] = convertBinaryToSignedLong(binaryNumbers[i][j]);
                        }
                    }

                } else {// For float
                    printBinaryToDoubleToFile(binaryNumbers, 8, fileLengthY);
                }
                printStringArray2D(binaryNumbers);
                break;

            case 4:
                for (i = 0; i < fileLengthY; i++) {
                    for (int j = 0; j < 12; j += 4) {

                        if (endianType == 0) {
                            binaryNumbers[i][j / 4] = convertShortToBinary(decArray[i][j])
                                    + convertShortToBinary(decArray[i][j + 1])
                                    + convertShortToBinary(decArray[i][j + 2])
                                    + convertShortToBinary(decArray[i][j + 3]);
                        } else {
                            binaryNumbers[i][j / 4] = convertShortToBinary(decArray[i][j + 3])
                                    + convertShortToBinary(decArray[i][j + 2])
                                    + convertShortToBinary(decArray[i][j + 1])
                                    + convertShortToBinary(decArray[i][j]);

                        }
                    }
                }

                if (dataType == 1) {// For unsigned
                    for (i = 0; i < fileLengthY; i++) {
                        for (int j = 0; j < 12 / size; j++) {
                            results[i][j] = convertBinaryToUnsignedLong(binaryNumbers[i][j]);
                        }
                    }

                } else if (dataType == 0) {// For Signed
                    for (i = 0; i < fileLengthY; i++) {
                        for (int j = 0; j < 12 / size; j++) {
                            results[i][j] = convertBinaryToSignedLong(binaryNumbers[i][j]);
                        }
                    }

                } else {// For float
                    printBinaryToDoubleToFile(binaryNumbers, 10, fileLengthY);

                }
                printStringArray2D(binaryNumbers);
                break;

        }
        if (dataType != 2) {
            writeLongArrayToFile(results, "output.txt");

        }

    }

    public static boolean isZero(String bin) {
        for (int i = 0; i < bin.length(); i++) {
            if (bin.charAt(i) != '0') {
                return false;
            }
        }
        return true;
    }

    public static boolean isOne(String bin) {
        for (int i = 0; i < bin.length(); i++) {
            if (bin.charAt(i) != '1') {
                return false;
            }
        }
        return true;
    }

    public static void printBinaryToDoubleToFile(String[][] binaryNumbers, int exp, int fileLengthY) {

        try {
            PrintWriter writer = new PrintWriter(new File("output.txt"));
            int bias, exponent = 0;
            double mantisa = 0;
            double result = 0;

            for (int i = 0; i < fileLengthY; i++) {
                for (int j = 0; j < 12 / size; j++) {
                    String IEEE = binaryNumbers[i][j];
                    String[] doubleParts = new String[3];

                    // Split the string into parts
                    doubleParts[0] = IEEE.substring(0, 1);// Sign
                    doubleParts[1] = IEEE.substring(1, 1 + exp);// Exponent
                    doubleParts[2] = IEEE.substring(1 + exp, IEEE.length());// Mantissa

                    // System.out.println(doubleParts[0] + " " + doubleParts[1] + " " +
                    // doubleParts[2]);

                    // Set Bias
                    bias = (int) Math.pow(2, exp - 1) - 1;

                    // System.out.println(bias);

                    if (doubleParts[2].length() > 13) {
                        // doubleParts[2] = roundToEven(doubleParts[2]);
                    }

                    if (isZero(doubleParts[1])) {// Checks if it is a denormalized number

                        // System.out.println(doubleParts[1] + " IS ZERO");
                        // Set Exponent
                        exponent = 1 - bias;

                        mantisa = convertBinaryToDouble(doubleParts[2]);

                        if (doubleParts[0].equals("1")) {
                            mantisa *= -1;
                        }

                        // System.out.println(doubleParts[1] + " exponent = " + exponent + "| " +
                        // doubleParts[2] + " mantisa = " + mantisa);
                        if ((!(mantisa > 0) && !(mantisa < 0))) {
                            writer.print(0);
                        } else if (mantisa == -0.0) {
                            writer.print(-0);
                        } else {
                            result = mantisa * Math.pow(2, exponent);
                            writer.printf("%.5e", result);
                        }

                    } else if (isOne(doubleParts[1])) {// Checks if it is a special number

                        // System.out.println(doubleParts[1] + " IS ONE");

                        if (isZero(doubleParts[2])) {// This means infinity
                            writer.print("INFINITY");
                        } else {
                            writer.print("NaN");
                        }

                    } else {

                        // System.out.println(doubleParts[1] + " IS NORMAL");

                        exponent = convertBinaryToInteger(doubleParts[1]) - bias;

                        mantisa = convertBinaryToDouble(doubleParts[2]) + 1;

                        // Set Sign
                        if (doubleParts[0].equals("1")) {
                            mantisa *= -1;
                        }

                        // System.out.println(doubleParts[1] + " exponent = " + exponent + "| " +
                        // doubleParts[2] + " mantisa = " + mantisa);

                        // Combine them

                        if ((!(mantisa > 0) && !(mantisa < 0))) {
                            if (isNegative(mantisa)) {
                                writer.print("-0");
                            } else {
                                writer.print("0");
                            }
                        } else {
                            result = mantisa * Math.pow(2, exponent);
                            writer.printf("%.5e", result);
                        }

                    }

                    if (j != 11) {
                        writer.print(" ");
                    }
                }
                if (i != fileLengthY - 1){
                    writer.println();
                }
            }
            writer.close();

        } catch (Exception e) {
            System.out.println("FILE NOT FOUND");
            return;
        }

    }

    public static String roundToEven(String bin) {
        String partToRound = bin.substring(12,bin.length());

        return " ";
    }

    public static boolean isNegative(double d) {
        return Double.compare(d, 0.0) < 0;
    }

    public static String convertShortToBinary(short input) {
        String binary = "";
        for (int i = 7; i >= 0; i--) {
            binary += (input >> i) & 1;
        }
        return binary;
    }

    public static double convertBinaryToDouble(String binary) {
        double result = 0;
        for (int i = 0; i < binary.length(); i++) {
            char bit = binary.charAt(i);
            if (bit == '1') {
                result += Math.pow(0.5, i + 1);
            }
        }
        // Truncates to 5 digits after point
        return Math.floor(result * 100000) / 100000;
    }

    public static long convertBinaryToUnsignedLong(String binaryString) {
        long decimalValue = 0;
        int power = 0;
        for (int i = binaryString.length() - 1; i >= 0; i--) {
            char binaryDigit = binaryString.charAt(i);
            if (binaryDigit == '1') {
                decimalValue += Math.pow(2, power);
            }
            power++;
        }
        return decimalValue;
    }

    public static long convertBinaryToSignedLong(String binary) {
        int length = binary.length();
        if (binary.charAt(0) == '1') { // if the number is negative
            // invert all the bits
            String inverted = "";
            for (int i = 0; i < length; i++) {
                inverted += (binary.charAt(i) == '0') ? '1' : '0';
            }
            // add 1 to the inverted number
            long value = convertBinaryToUnsignedLong(inverted) + 1;
            // negate the value to get the 2's complement
            return -value;
        } else { // if the number is non-negative
            return convertBinaryToUnsignedLong(binary);
        }
    }

    public static int convertBinaryToInteger(String binaryString) {
        int decimalValue = 0;
        int power = 0;
        for (int i = binaryString.length() - 1; i >= 0; i--) {
            char binaryDigit = binaryString.charAt(i);
            if (binaryDigit == '1') {
                decimalValue += Math.pow(2, power);
            }
            power++;
        }
        return decimalValue;
    }

    // Convers a 2 byte hexadecimal into a decimal
    // For example 1a into 170
    public static short convertHexToShort(String hex) {

        // Converts first number
        char temp = hex.charAt(1);
        int x = 0;
        short sum = 0;
        if (temp >= '0' && temp <= '9') {
            x = temp - '0';
        } else {
            x = temp - 'a' + 10;
        }
        sum += x;

        // Converts second number
        x = 0;
        temp = hex.charAt(0);
        if (temp >= '0' && temp <= '9') {
            x = temp - '0';
        } else {
            x = temp - 'a' + 10;
        }

        // Adds them up and returns
        sum += x * 16;
        return sum;

    }

    // Temp func for testing delete.
    public static void printShortArray2D(short[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void printStringArray2D(String[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void printLongArray2D(long[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void writeLongArrayToFile(long[][] array, String filePath) {
        try {
            PrintWriter writer = new PrintWriter(new File(filePath));
            int i = 0;
            int j = 0;
            for (i = 0; i < array.length; i++) {
                for (j = 0; j < array[i].length - 1; j++) {
                    writer.print(array[i][j] + " ");
                }
                writer.print(array[i][j++]);
                j = 0;
                writer.println();
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: OUTPUT FILE NOT FOUND");
            return;
        }
    }

    public static int getFileLengthY(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            int lineCount = 0;
            while (br.readLine() != null) {
                lineCount++;
            }
            return lineCount;
        }
    }

}