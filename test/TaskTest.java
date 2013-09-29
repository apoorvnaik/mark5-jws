
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.TimerTask;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Apoorv
 */
public class TaskTest extends TimerTask{

    Calendar calendar;
    int minute;
    int hour;

    public TaskTest() {
    calendar = Calendar.getInstance();
    minute = calendar.get(Calendar.MINUTE);
    hour = calendar.get(Calendar.HOUR_OF_DAY);

    }

    
    
    @Override
    public void run() {
        calendar = Calendar.getInstance();
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println("Current Minute is : "+minute);
        System.out.println("Current Hour is : "+hour);
        System.out.println(hour>12);
        System.out.println(minute<5);
        if( hour > 12 && minute < 05 )
        {
            System.out.println("RUNNING............." + Calendar.getInstance().getTime());
            minute = calendar.get(Calendar.MINUTE);
        }
        else
        {
            System.exit(0);
            System.out.print("Cancelling @ "+Calendar.getInstance().getTime());
            cancel();
        }
    }


}
