package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigInteger;

public class Generators
{
    private static long timeStamp;
    
    public static BigInteger generateM() throws FileNotFoundException
    {
        BigInteger P = Tools.fileToBigIntegers(new File("data/MyP.txt"))[0];
        BigInteger Q = Tools.fileToBigIntegers(new File("data/MyQ.txt"))[0];
        BigInteger M = P.multiply(Q);
        PrintWriter myMtxt = new PrintWriter("data/MyM.txt");
        myMtxt.print(M.toString());
        myMtxt.close();
        return M;
    }

    public static void generateEAndD(long index) throws FileNotFoundException
    {
        timeStamp = System.currentTimeMillis();
        generateM();
        BigInteger myP = Tools.fileToBigIntegers(new File("data/MyP.txt"))[0];
        BigInteger myQ = Tools.fileToBigIntegers(new File("data/MyQ.txt"))[0];
        BigInteger f = myP.subtract(BigInteger.ONE).multiply(myQ.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.ONE;
        long count = 0;
        for (BigInteger i = new BigInteger("2"); i.compareTo(f) == -1; i = i.add(BigInteger.ONE))
        {
            if (f.gcd(i).compareTo(BigInteger.ONE) == 0)
            {
                count++;
                e = i;
            }
            if (count == index || i.add(BigInteger.ONE).compareTo(f) == 0)
            {
                PrintWriter myEtxt = new PrintWriter("data/MyE.txt");
                PrintWriter myDtxt = new PrintWriter("data/MyD.txt");
                myEtxt.print(e);
                myDtxt.print(e.modInverse(f));
                myEtxt.close();
                myDtxt.close();
                double time = (System.currentTimeMillis() - timeStamp) / 1000.0;
                System.out.println("Total process time: " + time + " seconds.");
                return;
            }
        }
    }
}
