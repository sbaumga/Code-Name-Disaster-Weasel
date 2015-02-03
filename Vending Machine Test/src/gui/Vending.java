package gui;

/*
 * GridBagLayoutDemo.java requires no other files.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.lsmr.vendingmachine.simulator.*;

public class Vending  implements  ActionListener, DisplaySimulatorListener {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;    

     JButton b1,b2,b3,b4,b5,b6,b7,b8,b9,b10,br;// all pop buttons and return button (br)
     JButton b20,b21,b22,b23,b24,b25,b26; // all money buttons
     JFrame frame, f2; // frame is Vending Machine Frame, f2 is for wallet
     JTextField text;  // display area
     
     final int[] validValues = {5, 10, 25, 100, 200};
     final int[] popPrices = {200, 200, 200, 200, 200, 200, 200, 200, 200, 200};
     final String[] popNames = {"Water", "Coke", "Diet Coke", "Coke Zero", "7-Up", "Monster", "Red Bull", 
    		 "Dr. Pepper", "Crush", "Gatorade"};
     
     private HardwareSimulator machine = new HardwareSimulator(validValues , popPrices, popNames);

     
     
    public  void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	if (shouldFill) {
	//natural height, maximum width
	c.fill = GridBagConstraints.HORIZONTAL;
	}

	b1 = new JButton(popNames[0]);
	b1.addActionListener(this);
	if (shouldWeightX) {
	c.weightx = 0.5;
	}
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(b1, c);

	b2 = new JButton(popNames[1]);
	b2.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 1;
	c.gridy = 0;
	pane.add(b2, c);

	b3 = new JButton(popNames[2]);
	b3.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 2;
	c.gridy = 0;
	pane.add(b3, c);

	b4 = new JButton(popNames[3]);
	b4.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 3;
	c.gridy = 0;
	pane.add(b4, c);

	b5 = new JButton(popNames[4]);
	b5.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 4;
	c.gridy = 0;
	pane.add(b5, c);

	b6 = new JButton(popNames[5]);
	b6.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 5;
	c.gridy = 0;
	pane.add(b6, c);

	b7 = new JButton(popNames[6]);
	b7.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 6;
	c.gridy = 0;
	pane.add(b7, c);

	b8 = new JButton(popNames[7]);
	b8.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 7;
	c.gridy = 0;
	pane.add(b8, c);
	
	b9 = new JButton(popNames[8]);
	b9.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 8;
	c.gridy = 0;
	pane.add(b9, c);

	b10 = new JButton(popNames[9]);
	b10.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 9;
	c.gridy = 0;
	pane.add(b10, c);
	
	
	text = new JTextField("display");
	text.setEditable(false);
	text.setBackground(Color.GREEN);
	text.setText("hello");
	text.setForeground(Color.BLACK);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.ipady = 40;      //make this component tall
	c.weightx = 0.0;
	c.gridwidth = 3;
	c.gridx = 1;
	c.gridy = 1;
	pane.add(text, c);

	br = new JButton("Return");
	br.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.ipady = 0;       //reset to default
	c.weighty = 0.5;   //request any extra vertical space
	c.anchor = GridBagConstraints.PAGE_END; //bottom of space
	c.insets = new Insets(10,0,0,0);  //top padding
	c.gridx = 4;       //aligned with button 2
	//c.gridwidth = 1;   //2 columns wide
	c.gridy = 2;       //third row
	pane.add(br, c);
	
	//additional setup
	//registers the gui to listen to the display
	machine.getDisplay().register(this);
	//adds pop to the racks
	for (int i = 0; i < popPrices.length; i++){
		PopCanRackSimulator rack = machine.getPopCanRack(i);
		for (int j = 0; j < 5; j++) {
			try {
				rack.addPop(new PopCan());
			} catch (CapacityExceededException | DisabledException e) {
				//should never happen
			}
		}
	}
    }

    
    public  void addComponentsToPane2(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        
	pane.setLayout(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	if (shouldFill) {
	//natural height, maximum width
	c.fill = GridBagConstraints.HORIZONTAL;
	}

	b20 = new JButton("$0.05");
	b20.addActionListener(this);
	if (shouldWeightX) {
	c.weightx = 0.5;
	}
	c.fill = GridBagConstraints.HORIZONTAL;
	c.gridx = 0;
	c.gridy = 0;
	pane.add(b20, c);

	b22 = new JButton("$0.10");
	b22.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 1;
	c.gridy = 0;
	pane.add(b22, c);

	b23 = new JButton("$0.25");
	b23.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 2;
	c.gridy = 0;
	pane.add(b23, c);

	b24 = new JButton("$1");
	b24.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 3;
	c.gridy = 0;
	pane.add(b24, c);

	b25 = new JButton("$2");
	b25.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 4;
	c.gridy = 0;
	pane.add(b25, c);

	b26 = new JButton("other");
	b26.addActionListener(this);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 0.5;
	c.gridx = 5;
	c.gridy = 0;
	pane.add(b26, c);

    }
    
    
    

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		
		if(e.getSource() == b1)
        {
			text.setText("pop1 pressed");
			machine.getSelectionButton(0).press();
        }
		else if(e.getSource() == b2)
        {
			text.setText("pop2 pressed");
			machine.getSelectionButton(1).press();
        }
		else if(e.getSource() == b3)
        {
			text.setText("pop3 pressed");
			machine.getSelectionButton(2).press();
        }
		else if(e.getSource() == b4)
        {
			text.setText("pop4 pressed");
			machine.getSelectionButton(3).press();
        }
		else if(e.getSource() == b5)
        {
			text.setText("pop5 pressed");
			machine.getSelectionButton(4).press();
        }
		else if(e.getSource() == b6)
        {
			text.setText("pop6 pressed");
			machine.getSelectionButton(5).press();
        }
		else if(e.getSource() == b7)
        {
			text.setText("pop7 pressed");
			machine.getSelectionButton(6).press();
        }
		else if(e.getSource() == b8)
        {
			text.setText("pop8 pressed");
			machine.getSelectionButton(7).press();
        }
		else if(e.getSource() == b9)
        {
			text.setText("pop9 pressed");
			machine.getSelectionButton(8).press();
        }
		else if(e.getSource() == b10)
        {
			text.setText("pop10 pressed");
			machine.getSelectionButton(9).press();
        }
		else if(e.getSource() == br)
        {
			text.setText("return pressed");
			machine.getReturnButton().press();
        }
		else if(e.getSource() == b20)
        {
			try {
				machine.getCoinSlot().addCoin(new Coin(5));
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
		else if(e.getSource() == b22)
        {
			try {
				machine.getCoinSlot().addCoin(new Coin(10));
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
		else if(e.getSource() == b23)
        {
			try {
				machine.getCoinSlot().addCoin(new Coin(25));
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
		else if(e.getSource() == b24)
        {
			try {
				machine.getCoinSlot().addCoin(new Coin(100));
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
		else if(e.getSource() == b25)
        {
			try {
				machine.getCoinSlot().addCoin(new Coin(200));
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
		else if(e.getSource() == b26)
        {
			try {
				machine.getCoinSlot().addCoin(new Coin(50));
			} catch (DisabledException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
	}
    
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */

    
    
    public void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("Vending Machine");
        f2 = new JFrame("Wallet!!!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
        addComponentsToPane2(f2.getContentPane());

        //Display the window.
        frame.pack();
        f2.pack();
        frame.setVisible(true);
        f2.setVisible(true);
//        f2.setSize(100, 30);
        f2.setLocation(600, 100);
       
    }

    
    public void outOfOrderLight(){
    	text.setBackground(Color.RED);
    	text.setText("Out of Order!!!");
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                this.createAndShowGUI();
//            }
//        });
    	Vending v = new Vending();
    	v.createAndShowGUI();
    	//v.outOfOrderLight();
    }


	@Override
	public void enabled(AbstractHardware<AbstractHardwareListener> hardware) {
		// Does not need to do anything on enabled
		
	}


	@Override
	public void disabled(AbstractHardware<AbstractHardwareListener> hardware) {
		// Does not need to do anything on disabled
		
	}


	@Override
	public void messageChange(DisplaySimulator display, String oldMsg,
			String newMsg) {
		// update text display
		text.setText(newMsg);		
	}


}