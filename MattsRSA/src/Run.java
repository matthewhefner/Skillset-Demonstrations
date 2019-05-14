
import java.util.Scanner;
import util.Decryption;
import util.Encryption;
import util.Generators;

public class Run
{
    /** Primary input */
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args)
    {
        char option = 'x';
        boolean success = false;
        do
        {
            option = prompt();
            switch(option)
            {
                case 'E':
                case 'e':
                    success = EncryptProcess();
                    break;
                case 'D':
                case 'd':
                    success = DecryptProcess();
                    break;
                case 'G':
                case 'g':
                    success = GenerateEAndD();
                    break;
                default:
                    success = true;
            }
            System.out.println("Process Success: " + success + ". \n");
        } while (option != 'x' && option != 'X');
        System.out.println("Thank you for using my utility - goodbye!");
    }

    public static boolean EncryptProcess()
    {
        if (Encryption.setupEncryption())
        {
            Encryption.encrypt();
            return true;
        }
        return false;
    }

    public static boolean DecryptProcess()
    {
        if (Decryption.setupDecryption())
        {
            Decryption.decrypt();
            return true;
        }
        return false;
    }

    public static boolean GenerateEAndD()
    {
        String in = "";
        System.out.println("This process will overwrite the existing MyM.txt, MyE.txt and MyD.txt."
            + "\nTHIS PROCESS MAY TAKE TIME."
            + "\nAre you sure you wish to continue? (Y/n)");
        while (in.length() == 0)
        {
            in = input.nextLine();
        }
        if (in.toCharArray()[0] == 'Y')
        {
            try
            {
                System.out.println("The nth positive integer less than and relatively prime to"
                    + "\nyour modulus will be selected for MyE.txt, and its inverse modulo your F will be"
                    + "\nMyD.txt.  Please select an n.  If there are less than n numbers, the largest of"
                    + "\nthese integers will be selected for MyE.txt.");
                Generators.generateEAndD(input.nextLong());
            }
            catch (Exception e)
            {
                e.printStackTrace();
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }

    public static char prompt()
    {
        String temp;
        System.out.println("Type E for Encryption, D for Decryption,"
            + "\nG to generate an M, E and a D, and X to exit.");
        temp = input.next();
        if (temp.length() == 1)
        {
            return temp.toCharArray()[0];
        }
        return 'a';
    }
}
