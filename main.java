import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Main{

    public static byte endianType;
    public static byte dataType;
    public static byte size;

    public static void main(String[] args){

        //Take file input
        File file = new File("./input.txt");

        //Takes hexadecimals from input and puts them into hexArray
        short[][] hexArray = new short[3][12];
        short i = 0;
        String tempString;
        String[] tempStrings;
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()){
                tempString = scanner.nextLine();
                tempStrings = tempString.split(" ");
                for (int j = 0; j < 12; j++){
                    hexArray[i][j] = getDecimalFromHex(tempStrings[j]);
                }
                i++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("FILENOTFOUND");
            return;
        }

        //Gets other inputs

        //Gets byte ordering
        Scanner scanner = new Scanner(System.in);
        System.out.println("Byte Ordering: ");
        tempString = scanner.nextLine();
        if (tempString.equals("Big Endian")){
            endianType = 0;
        } else if (tempString.equals("Little Endian")){
            endianType = 1;
        } else {
            System.out.println("Unkown Input");
            return;
        }


        //Gets data type
        System.out.println("Data Type: ");
        tempString = scanner.nextLine();
        if (tempString.equals("Signed Integer")) {
            dataType = 0;
        } else if (tempString.equals("Unsigned Integer")){
            dataType = 1;
        } else if (tempString.equals("Floating Point")){
            dataType = 2;
        } else {
            System.out.println("Unkown Input");
            return;
        }


        //Gets Byte Size
        System.out.println("Byte Size: ");
        tempString = scanner.next();
        if (tempString.equals("1")){
            size = 1;
        } else if (tempString.equals("2")){
            size = 2;
        } else if (tempString.equals("3")){
            size = 3;
        } else if (tempString.equals("4")){
            size = 4;
        } else {
            System.out.println("Unkown Input");
            return;
        }        
    }


    //Convers a 2 byte hexadecimal into a decimal
    //For example 1a into 170
    public static short getDecimalFromHex(String hex){

        //Converts first number
        char temp = hex.charAt(1);
        int x = 0;
        short sum = 0;
        if (temp >= '0' && temp <= '9'){
            x = temp - '0';
        } else {
            x = temp - 'a' + 10;
        }
        sum+=x;

        //Converts second number
        x = 0;
        temp = hex.charAt(0);
        if (temp >= '0' && temp <= '9'){
            x = temp - '0';
        } else {
            x = temp - 'a' + 10;
        }

        //Adds them up and returns
        sum +=x*16;
        return sum;

    }

    //Temp func for testing delete.
    public static void printShortArray2D(short[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
    }
    
}