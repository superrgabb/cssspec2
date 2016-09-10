import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public final class Main extends JFrame implements ActionListener{
    private static final long serialVersionUID = 1L;    
    private static ArrayList names = new ArrayList();
    private static JFrame frame;  
    private static JButton start = new JButton("Start");
    private static JLabel[] label;
    private static JLabel winLabel = new JLabel();  //winner
    
    public Main() throws FileNotFoundException{
      frame = new JFrame("ACM Raffle Draw");  
      frame.setSize(1027, 768);
      frame.setLayout(null);
      frame.setLocationRelativeTo(null);    //center
      start.setBounds(0, 0, 70, 20);
      start.setLayout(null);
      start.addActionListener(this);//for something to happen when start is clicked
      frame.add(start);
      frame.setResizable(true);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      Scanner in = new Scanner(new FileReader("input.txt"));//read from input.txt
      while(in.hasNext()){
          names.add(in.nextLine()); //add input.txt contents to ArrayList names
      }
      Collections.shuffle(names);   //shuffle names
      label = new JLabel[names.size()];
      frame.setVisible(true);   //show frame
    }//end Main

    public void actionPerformed (ActionEvent e) { 
        if(e.getSource()==start){   //if start button was clicked
            new Candidates().start();   //run Candidates thread
            start.setEnabled(false);    //disable start button (para di abuso :) )
        }
    }//end action performed()
         
    public static void main(String[] args) throws java.io.IOException{
            Main gui = new Main();
            gui.frame.setVisible(true);
    }
    
    public class Candidates extends Thread{
        public void run(){
            String win;
            int i=0;
            frame.remove(winLabel); //if there is a winner, remove it (refresh)
            winLabel=new JLabel(names.get(0).toString());   //get first name in shuffled ArrayList names
            int randomX=(int)(Math.random()*1024);
            int randomY=(int)(Math.random()*700);
            winLabel.setFont(new Font("Arial", Font.PLAIN, 25));
            winLabel.setBounds(randomX, randomY, 290, 20);  //set winLabel location
            winLabel.setLayout(null);
            frame.add(winLabel);
            
            for(i=0; i<names.size(); i++){  //add all the names in random locations
                label[i]=new JLabel(names.get(i).toString());   //put name into a JLabel
                label[i].setFont(new Font("Arial", Font.PLAIN, 25));
                randomX=(int)(Math.random()*1024);
                randomY=(int)(Math.random()*768);
                label[i].setBounds(randomX, randomY, 290, 20);
                label[i].setLayout(null);
                label[i].setVisible(false); //initially hide the JLabels
                frame.add(label[i]);
            }
            //make the JLabels visible with gradually increasing speed
            for(i=names.size()-1; i>=0; i--)
                try {Thread.sleep(i/20);    //dynamic delay, based on the number of names
                    label[i].setVisible(true);
                } catch (InterruptedException ex) {}
            //make the JLabels disappear with gradually decreasing speed (dramatic ;) )    
            for(i=0; i<names.size(); i++)
                try {Thread.sleep(i/2);
                    label[i].setVisible(false);
                } catch (InterruptedException ex) {}
            
            //horizontally center the winner
            if(winLabel.getX()>512)
                for(int x=winLabel.getX(); x>512; x--)
                    try {Thread.sleep(10);
                        winLabel.setLocation(x, winLabel.getY());
                    } catch (InterruptedException ex) {}
                    
            else
                for(int x=winLabel.getX(); x<512; x++)
                    try {Thread.sleep(10);
                        winLabel.setLocation(x, winLabel.getY());
                    } catch (InterruptedException ex) {}
            //vertically center the winner
            if(winLabel.getY()>384)
                for(int y=winLabel.getY(); y>384; y--)
                    try {Thread.sleep(10);
                        winLabel.setLocation(winLabel.getX(), y);
                    } catch (InterruptedException ex) {}
                    
            else
                for(int y=winLabel.getY(); y<384; y++)
                    try {Thread.sleep(10);
                        winLabel.setLocation(winLabel.getX(), y);
                    } catch (InterruptedException ex) {}
            start.setEnabled(true);
            names.remove(0);    //remove winner from ArrayList names so that he/she will not win again
        }
    }//end Candidates class
}
