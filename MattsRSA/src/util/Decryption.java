package util;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.ArrayList;

public class Decryption
{
    private static File theirCiphertexttxt;
    private static File myDTxt;
    private static BigInteger myM;
    private static BigInteger myD;
    private static BigInteger[] theirBigIntegers;
    private static int[] theirAsciiArray;
    private static PrintWriter theirMessage;
    private static long timeStamp;
    
    public static boolean setupDecryption()
    {
        timeStamp = System.currentTimeMillis();
        //Load BigIntegers from theirM.txt and theirE.txt
        try
        {
            System.out.println("Loading data from files...");
            //Load files from paths
            myDTxt = new File("data/MyD.txt");
            theirCiphertexttxt = new File("data/TheirCiphertext.txt");
            //Load D and M from files
            myM = Generators.generateM();
            myD = Tools.fileToBigIntegers(myDTxt)[0];
            //Load bigints array from file
            theirBigIntegers = Tools.fileToBigIntegers(theirCiphertexttxt);
            //can't do anything with ascii array here like as in encryption
            //we have to decrypt to get the ascii values!
            //initialize ciphertext file the same
            theirMessage = new PrintWriter("data/TheirMessage.txt");
            System.out.println("Decryption data ready.");
            return true;
        } catch(Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void decrypt()
    {
        ArrayList<BigInteger> decipheredInts = new ArrayList<BigInteger>();
        int totalProgressCount = theirBigIntegers.length;
        int progressCount = 1;
        Tools.initiateProgress("Decrypting Integers");
        
        for (BigInteger bigInt : theirBigIntegers)
        {
            BigInteger value = bigInt.modPow(myD, myM);
            decipheredInts.add(value);
            progressCount++;
            Tools.reportProgress(progressCount, totalProgressCount);
        }
        BigInteger[] frank = new BigInteger[decipheredInts.size()];
        int count = 0;
        for (@SuppressWarnings("unused") BigInteger i : decipheredInts)
        {
            frank[count] = decipheredInts.get(count);
            count++;
        }
        theirAsciiArray = Tools.partitionAscii(frank);
        System.out.println("Decrypted.  Now writing to file...");
        for (int i : theirAsciiArray)
        {
            theirMessage.print((char) i);
        }
        System.out.println("Message successfully saved to data/TheirMessage.txt.");
        theirMessage.close();
        double time = (System.currentTimeMillis() - timeStamp) / 1000.0;
        System.out.println("Total process time: " + time + " seconds.");
    }
}
