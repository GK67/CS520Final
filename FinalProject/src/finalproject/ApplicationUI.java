/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import javax.swing.JComboBox;
 
// An AWT program inherits from the top-level container java.awt.Frame
public class ApplicationUI extends Frame implements ActionListener {
   private Label lblStart;  // Declare a Label component 
   private Label lblEnd;  // Declare a Label component 
   private Label type;
   private Label Start; 
   private TextField tfCount; // Declare a TextField component 
   private Button btnCount;   // Declare a Button component
   private int count = 0;     // Counter's value
   private TextField tfDisplay;
   private JComboBox StartList;
   private JComboBox EndList;
   private JComboBox TypeList;
   private TextField TypeDisplay;
   
   private Button Calculate;
  // private TextField tfDisplay;
       
   // Constructor to setup GUI components and event handlers
   public ApplicationUI () {
      setLayout(new FlowLayout());
         // "super" Frame, which is a Container, sets its layout to FlowLayout to arrange
         // the components from left-to-right, and flow to next row from top-to-bottom.
      Panel panelButtons = new Panel(new GridLayout(2, 4));
      lblStart = new Label("Start-Point");  // construct the Label component
                    
       String[] PlaceStrings = { "Place 1", "Place 2", "Place 3", "Place 4", "Place 5" };

       //Create the combo box, select the item at index 4.
        //Indices start at 0, so 4 specifies the pig.
       StartList = new JComboBox(PlaceStrings);
       StartList.setSelectedIndex(4);
       StartList.addActionListener(this);
      
      lblEnd = new Label("End-Point");  // construct the Label component
      EndList = new JComboBox(PlaceStrings);
      EndList.setSelectedIndex(4);
      EndList.addActionListener(this);
      
       String[] TypeStrings = { "A*", "Dj" };
      type = new Label("Type");
      TypeList = new JComboBox(TypeStrings);
      TypeList.setSelectedIndex(1);
      TypeList.addActionListener(this);
      
      Start =  new Label("Let's start"); // construct the TextField component with initial text
             // set to read-only
                          
 
      Calculate = new Button("Calculate");   // construct the Button component
                       
 
      Calculate.addActionListener(this);
      panelButtons.add(lblStart);
      panelButtons.add(StartList);
      panelButtons.add(type);
      panelButtons.add(TypeList);
      panelButtons.add(lblEnd);
      panelButtons.add(EndList);
      panelButtons.add(Start);
      panelButtons.add(Calculate);
      
      
      Panel panelDisplay = new Panel(new FlowLayout());
      tfDisplay = new TextField();
      tfDisplay.setPreferredSize(new Dimension( 780, 100 ));
      panelDisplay.add(tfDisplay);
      
      
      setLayout(new BorderLayout());  // "super" Frame sets to BorderLayout
      add(panelDisplay, BorderLayout.NORTH);
      add(panelButtons, BorderLayout.CENTER);
         // "btnCount" is the source object that fires an ActionEvent when clicked.
         // The source add "this" instance as an ActionEvent listener, which provides
         //   an ActionEvent handler called actionPerformed().
         // Clicking "btnCount" invokes actionPerformed().
 
      setTitle("AWT Counter");  // "super" Frame sets its title
      setSize(800, 300);        // "super" Frame sets its initial window size
 
      // For inspecting the Container/Components objects
      // System.out.println(this);
      // System.out.println(lblCount);
      // System.out.println(tfCount);
      // System.out.println(btnCount);
 
      setVisible(true);         // "super" Frame shows
 
      // System.out.println(this);
      // System.out.println(lblCount);
      // System.out.println(tfCount);
      // System.out.println(btnCount);
   }
 
   // The entry main() method
   public static void main(String[] args) {
      // Invoke the constructor to setup the GUI, by allocating an instance
      ApplicationUI app = new ApplicationUI();
         // or simply "new AWTCounter();" for an anonymous instance
   }
 
   // ActionEvent handler - Called back upon button-click.
   @Override
   public void actionPerformed(ActionEvent evt) {
    
   }
}
