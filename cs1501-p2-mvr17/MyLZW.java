/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/
import java.util.*;
import java.lang.*;

public class MyLZW {
    private static final int R = 256;                       // number of input chars
    private static int W = 9;                               // codeword width
    private static int VW = W;
    private static int min_L = (int)Math.pow(2, W); 
    private static int L = (int)Math.pow(2, VW);            // number of codewords = 2^W


    private static final int min_codeWordSize = 9;
    private static final int max_codeWordSize = 16;

    private static double new_compression_ratio = 0;               //uncompressed data that has been proccessed/generated / compressed data proccessed/generated
    private static double old_compression_ratio = 0;

    public static void compress(char mode) { 
        BinaryStdOut.write(mode, 8);
        String input = BinaryStdIn.readString();    //the input file converted to a binary string
        TST<Integer> st = new TST<Integer>();       //the dictionary - stores the new codewords and its coresponfing num in the dictionary
        for (int i = 0; i < R; i++)                 //while less than 2^12 (the max length of the codeword)...increment codewordSize
            st.put("" + (char) i, i);               //adds codeword and ts correspnding number to the symbol table
        int code = R+1;  // R is codeword for EOF   //the current number new code word

        double unproccessed = 0;
        double proccessed = 0;

        while (input.length() > 0) {
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), VW);      // Print s's encoding.
            int t = s.length();

            unproccessed += t*8;
            proccessed +=VW;

            if (t < input.length() && code < L)    // Add s to symbol table.
            { 
                st.put(input.substring(0, t + 1), code++);
            }
            else if(code == L && VW < max_codeWordSize && t < input.length())   //do nothing mode
            {
                System.err.println("resizing");
                //incremment the codeWordSize till the max amount
                VW++;
                //resizes the max code word length
                L = (int)Math.pow(2, VW);
                st.put(input.substring(0, t + 1), code++);
            }
            else if(mode == 'r' && max_codeWordSize == (int)Math.pow(2, VW) && t < input.length())  //reset mode
            {
                st = new TST<Integer>();
                for (int i = 0; i < R; i++)   
                {
                    st.put("" + (char) i, i); 
                }
                code = R+1;
                VW = W;
                L = (int)Math.pow(2, VW);       
                         
            }
            else if(mode == 'm'  && t < input.length())    //monitor mode
            {
                if (old_compression_ratio == 0 && code == L )
                {
                    old_compression_ratio = unproccessed/proccessed;
                  
                    //new_compression_ratio = proccessed/unproccessed;
                }
                new_compression_ratio = unproccessed/proccessed; 

                //reset the codebook after the ratio reaches a threshold (x > 1.1)
                // proccessed += t;       //t = s.length =the length of the string from input
                // unproccessed += VW;
                //old_compression_ratio = unproccessed/proccessed;

               // System.err.println("proccessed data (t) is: " + proccessed);
                //System.err.println("unproccessed data (VW) is: " + unproccessed);
                //System.err.println("old_compression_ratio : " + old_compression_ratio);

                double ratioception = old_compression_ratio/new_compression_ratio;

                System.err.println(ratioception);

                if(ratioception > 1.1)
                {

                    st = new TST<Integer>();
                    for (int i = 0; i < R; i++)   
                    {
                        st.put("" + (char) i, i); 
                    }
                    code = R+1;
                    VW = W;
                    L = (int)Math.pow(2, VW);       
                    st.put(input.substring(0, t + 1), code++);
                    old_compression_ratio = 0;
                    System.err.println("resetting...");

                }
                //incremment the codeWordSize till the max amount
                // VW++;
                // //resizes the max code word length
                // L = (int)Math.pow(2, VW);
            }
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, VW);
        BinaryStdOut.close();
    } 




    public static void expand() {
        char mode = BinaryStdIn.readChar(8);
        String[] st = new String[L];
        int code = R+1; // next available codeword value
        int i;
        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(VW);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];

        double unproccessed = 0;
        double proccessed = 0;

        old_compression_ratio = 0;
        new_compression_ratio =0;
        while (true) {

            unproccessed += val.length() * 8;       //t = s.length =the length of the string from input
            proccessed += VW;
            if(code == L && VW < max_codeWordSize)
            {
                System.err.println("resizing");
                //decremment the codeWordSize till the max amount
                VW++;
                //resizes the max code word length
                L = (int)Math.pow(2, VW);
                st = Arrays.copyOf(st, L);
            }

            else if(mode == 'r' && max_codeWordSize == (int)Math.pow(2, VW))
            {
                st = new String[min_L];
                int j;
                for (j = 0; j < R; j++)   
                    st[j] = "" + (char) j;
                st[j++] = "";                        // (unused) lookahead for EOF

                 code = R+1;
                VW = W;
                L = min_L;       
                         
            }

             else if(mode == 'm' && code == L && VW == max_codeWordSize)    //monitor mode
            {
                //reset the codebook after the ratio reaches a threshold (x > 1.1)
                

                if(old_compression_ratio == 0)
                {

                    old_compression_ratio = unproccessed/proccessed;
                }
                new_compression_ratio = unproccessed/proccessed;

                double ratioception = old_compression_ratio/new_compression_ratio;


                if(ratioception > 1.1)
                {
                    System.err.println("reseting...");
                    st = new String[min_L];
                    int j;
                    for (j = 0; j < R; j++)   
                        st[j] = "" + (char) j;
                    st[j++] = "";                        // (unused) lookahead for EOF

                    code = R+1;
                    VW = W;
                    L = min_L;      
                    old_compression_ratio = 0;
                    
                }

                
                 
            }

            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(VW);
            if (codeword == R) break;
            String s = st[codeword];
            if (code == codeword) s = val + val.charAt(0);   // special case hack
            if (code < L) st[code++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        if (args[0].equals("-")) 
        {
            //compress();
            if(args[1].equals("n"))
            {   //doNothing_compress();   //Do nothing mode
                             compress('n');
            }
            else if(args[1].equals("r"))
            {
                compress('r');
            }
            else if(args[1].equals("m"))
                compress('m');

        }
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
