import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

public class Main extends JFrame {

  public Main() {
    this.setLayout(new FlowLayout());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JButton processButton = new JButton("Start");
    JButton helloButton = new JButton("Hello");

    processButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        MyTask process = new MyTask();
        try {
          process.execute();
          } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    helloButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "Hello There");
      }
    });
    this.getContentPane().add(processButton);
    this.getContentPane().add(helloButton);

    this.pack();
    setVisible(true);

  }

  public static void main(String[] args) {
    new Main();
  }

}

class MyTask extends SwingWorker {
  protected Object doInBackground() throws Exception {
    Integer result = new Integer(0);
    for (int i = 0; i < 10; i++) {
      result += i * 10;
      try {
        Thread.sleep(500);
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println(result);
    }
    
    return result;
  }
}