package util;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigInteger;

public class Encryption
{
    private static BigInteger theirM;
    private static BigInteger theirE;
    private static BigInteger[] myBigIntegers;
    private static File theirMTxt;
    private static File theirETxt;
    private static File myMessage;
    private static int[] myAsciiArray;
    private static PrintWriter myCiphertextFile;
    private static long timeStamp;

    public static boolean setupEncryption()
    {
        timeStamp = System.currentTimeMillis();
        //Load BigIntegers from theirM.txt and theirE.txt
        try
        {
            System.out.println("Loading data from files...");
            //Load files from paths
            theirMTxt = new File("data/TheirM.txt");
            theirETxt = new File("data/TheirE.txt");
            myMessage = new File("data/MyMessage.txt");
            //Load E and M from files
            theirM = Tools.fileToBigIntegers(theirMTxt)[0];
            theirE = Tools.fileToBigIntegers(theirETxt)[0];
            //Load ascii array from files
            myAsciiArray = Tools.fileToAsciiArray(myMessage);
            myBigIntegers = Tools.concatenateAscii(myAsciiArray, theirM);
            //initialize ciphertext file
            myCiphertextFile = new PrintWriter("data/MyCiphertext.txt");
            System.out.println("Encryption data ready.");
            return true;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void encrypt()
    {
        int totalCount = myBigIntegers.length;
        int count = 1;
        Tools.initiateProgress("Encrypting");
        for (BigInteger i : myBigIntegers)
        {
            BigInteger tempInt = i.modPow(theirE, theirM);
            myCiphertextFile.println(tempInt);
            count++;
            Tools.reportProgress(count, totalCount);
        }
        System.out.println("Ciphertext integers successfully saved to data/MyCiphertext.txt.");
        myCiphertextFile.close();
        double time = (System.currentTimeMillis() - timeStamp) / 1000.0;
        System.out.println("Total process time: " + time + " seconds.");
    }
}
