
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Apoorv
 */
public class Demo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        //Timer timer = new Timer(true);
        Timer timer = new Timer();
        TaskTest testTask = new TaskTest();
        timer.scheduleAtFixedRate(testTask, 10, 1000*15);
        //testTask.run();

        //timer.cancel();
        Calendar cal = Calendar.getInstance();

        Date date = new Date();
        System.out.println("Time is "+cal.getTime());
        System.out.println("Date is "+date);
        System.out.println(cal.get(Calendar.HOUR_OF_DAY) <= 12 && cal.get(Calendar.MINUTE) < 60);
        //System.out.println(Calendar.H);
        /*
        //Timer timer = new Timer("New Timer");
        //TaskTest taskTest = new TaskTest();
        //timer.scheduleAtFixedRate(taskTest, 1000, 1000);
        try {
        // get runtime environment and execute child process
        Runtime systemShell = Runtime.getRuntime();
        Process output = systemShell.exec("rasdial dataone apoorv1190 love643882");
        // open reader to get output from process
        BufferedReader br = new BufferedReader (new InputStreamReader(output.getInputStream()));
        String line = null;
        System.out.println("<OUTPUT/>");
        while((line = br.readLine()) != null )
         { System.out.println(line);  }          // display process output
     System.out.println("</OUTPUT>");
     int exitVal = output.waitFor();             // get process exit value
     System.out.println("Process Exit Value : "+ exitVal);
     output.destroy();

     }
   catch (IOException ioe){ System.err.println(ioe); }
   catch (Throwable t) { t.printStackTrace();}
   */
        
    }

}
