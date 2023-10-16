import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main {

    private static boolean evenlyWeighted = true;
    private static boolean runningWindows = false;
    private static String[] corners = new String[]{
            "",
            "U R2 L2 U R2 L2 U",
            "U R2 F2 R2 F2 U",
            "U R2 L2 D L2 F2 U R2 U2 F2 U"
    };
    private static String[] edges = new String[]{
            "",
            "U R2 F2 R2 U",
            "U R2 L2 D",
            "U R2 F2 L2 F2 B2 U",
            "U R2 L2 F2 B2 U"
    };

    public static void main(String[] args) {
        if(System.getProperty("os.name").startsWith("Windows")) runningWindows = true;
        WeightedDie die;
        if(evenlyWeighted) {die = new WeightedDie(new double[]{1, 1, 2, 1, 1}, new int[]{0,1,2,3,4});}
        else {die = new WeightedDie(new double[]{1, 16, 36, 16, 1}, new int[]{0,1,2,3,4});}
        String scramble;
        while(true){
            scramble = "R' U' F "+nissy("twophase","R' U' F "+nissy("scramble","htr")+corners[ThreadLocalRandom.current().nextInt(corners.length)]+nissy("scramble","htr")+edges[die.roll()]+nissy("scramble","htr")+" R' U' F")+" R' U' F";
            System.out.println(scramble);
            try {
                // Read a single byte from the standard input
                System.in.read();
                // Clear the input buffer
                while (System.in.available() > 0) {
                    System.in.read();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(nissy("solve","htr",scramble));
            System.out.println(nissy("solve","drfin",scramble));
        }
    }

    private static String nissy(String... input){
        String[] commands;
        String directory;
        if(runningWindows){
            commands = new String[]{"cmd", "/c", "start", "/b", "/wait", "nissy-2.0.5.exe"};
            directory = "C:\\Users\\lolra\\Downloads";
        } else {
            commands = new String[]{"nissy"};
            directory = "/Users/michaelvogel/Documents/FMC/nissy-2.0.5";
        }
        String[] inputs = Arrays.copyOf (commands,commands.length+input.length);
        System.arraycopy (input, 0, inputs, commands.length, input.length);
        ProcessBuilder pb = new ProcessBuilder(inputs);
        pb.directory(new File(directory));
        Process p;
        try {
            p = pb.start();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        InputStream is = p.getInputStream();
        boolean finished;
        try {
            finished = p.waitFor(1000, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        if (finished) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder sb = new StringBuilder();
            while (true) {
                try {
                    if ((line = br.readLine()) == null) break;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                sb.append(line);
            }
            try {
                is.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                br.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return sb.toString();
        } else {
            return "took too long";
        }
    }
}

