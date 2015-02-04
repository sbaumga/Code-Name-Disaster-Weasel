package gui;

/*
 * GridBagLayoutDemo.java requires no other files.
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import org.lsmr.vendingmachine.simulator.*;

public class GUI implements ActionListener, DisplaySimulatorListener,
		IndicatorLightSimulatorListener {
	final static boolean shouldFill = true;
	final static boolean shouldWeightX = true;
	final static boolean RIGHT_TO_LEFT = false;

	JButton b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, br;// all pop buttons and
														// return button (br)
	JButton b20, b21, b22, b23, b24, b25, b26; // all money buttons
	JFrame frame, f2; // frame is Vending Machine Frame, f2 is for wallet
	JTextField text; // display area
	JTextField OutofOrderLight, ExactChangeLight;
	JTextField DeliveryChute, ReturnChange;

	final int[] validValues = { 5, 10, 25, 100, 200 };
	final int[] popPrices = { 200, 200, 200, 200, 200, 200, 200, 200, 200, 200 };
	final String[] popNames = { "Water", "Coke", "Diet Coke", "Coke Zero",
			"7-Up", "Monster", "Red Bull", "Dr. Pepper", "Crush", "Gatorade" };
	// private int SelectedPop = 10;

	private HardwareSimulator machine = new HardwareSimulator(validValues,
			popPrices, popNames);

	public void addComponentsToPane(Container pane) {
		if (RIGHT_TO_LEFT) {
			pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}

		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
			// natural height, maximum width
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

		text = new JTextField("");
		text.setEditable(false);
		text.setBackground(Color.WHITE);
		text.setFont(new Font("Arial Black", Font.BOLD, 18));
		text.setForeground(Color.BLACK);
		text.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40; // make this component tall
		c.weightx = 0.0;
		c.gridwidth = 5;
		c.gridx = 2;
		c.gridy = 1;
		pane.add(text, c);

		// A field to represent the out of order light
		OutofOrderLight = new JTextField("OutOfOrderLight");
		OutofOrderLight.setEditable(false);
		OutofOrderLight.setBackground(Color.WHITE);
		OutofOrderLight.setText("");
		OutofOrderLight.setForeground(Color.BLACK);
		OutofOrderLight.setBorder(BorderFactory
				.createLineBorder(Color.BLACK, 2));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10; // make this component tall
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 1;
		pane.add(OutofOrderLight, c);

		// A field to represent the exact change light/mode
		ExactChangeLight = new JTextField("ExactChangeLight");
		ExactChangeLight.setEditable(true);
		ExactChangeLight.setBackground(Color.WHITE);
		ExactChangeLight.setText("Exact Change Mode");
		ExactChangeLight.setForeground(Color.BLACK);
		ExactChangeLight.setBorder(BorderFactory.createLineBorder(Color.BLACK,
				2));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10; // make this component tall
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		pane.add(ExactChangeLight, c);

		// Unused Part
		// DeliveryChute = new JTextField("");
		// DeliveryChute.setEditable(true);
		// DeliveryChute.setBackground(Color.WHITE);
		// DeliveryChute.createVolatileImage(1,2);
		// DeliveryChute.setForeground(Color.BLACK);
		// DeliveryChute.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.ipady = 10;
		// c.weightx = 0.0;
		// c.gridwidth = 1;
		// c.gridx = 4;
		// c.gridy = 2;
		// pane.add(DeliveryChute, c);
		//
		// ReturnChange = new JTextField("");
		// ReturnChange.setEditable(true);
		// ReturnChange.setBackground(Color.WHITE);
		// ReturnChange.setForeground(Color.BLACK);
		// ReturnChange.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
		// c.fill = GridBagConstraints.HORIZONTAL;
		// c.ipady = 10;
		// c.weightx = 0.0;
		// c.gridwidth = 1;
		// c.gridx = 6;
		// c.gridy = 2;
		// pane.add(ReturnChange, c);

		DeliveryChute = new JTextField("DeliveryChute");
		DeliveryChute.setEditable(true);
		DeliveryChute.setBackground(Color.WHITE);
		DeliveryChute.createVolatileImage(1,2);
		DeliveryChute.setForeground(Color.BLACK);
		DeliveryChute.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 10;      
		c.weightx = 0.0;
		c.gridwidth = 5;
		c.gridx = 2;
		c.gridy = 2;
		pane.add(DeliveryChute, c);	
//		
//		ReturnChange = new JTextField("");
//		ReturnChange.setEditable(true);
//		ReturnChange.setBackground(Color.WHITE);
//		ReturnChange.setForeground(Color.BLACK);
//		ReturnChange.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.ipady = 10;      
//		c.weightx = 0.0;
//		c.gridwidth = 1;
//		c.gridx = 6;
//		c.gridy = 2;
//		pane.add(ReturnChange, c);	
		
		
		br = new JButton("Return");
		br.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0; // reset to default
		c.weighty = 0.5; // request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; // bottom of space
		c.insets = new Insets(10, 0, 0, 0); // top padding
		c.gridx = 8;
		// c.gridwidth = 1; //2 columns wide
		c.gridy = 1;
		pane.add(br, c);

		// additional setup
		// registers the gui to listen to the display
		machine.getDisplay().register(this);
		// registers the gui to listen to the out of order light and exact
		// change light
		machine.getOutOfOrderLight().register(this);
		machine.getExactChangeLight().register(this);

		// fill coin racks
		while (machine.getCoinRack(0).hasSpace()) {
			try {
				machine.getCoinRack(0).acceptCoin(new Coin(5));
			} catch (CapacityExceededException | DisabledException e) {
				// should never happen
			}
		}
		while (machine.getCoinRack(1).hasSpace()) {
			try {
				machine.getCoinRack(1).acceptCoin(new Coin(10));
			} catch (CapacityExceededException | DisabledException e) {
				// should never happen
			}
		}
		while (machine.getCoinRack(2).hasSpace()) {
			try {
				machine.getCoinRack(2).acceptCoin(new Coin(25));
			} catch (CapacityExceededException | DisabledException e) {
				// should never happen
			}
		}
		while (machine.getCoinRack(3).hasSpace()) {
			try {
				machine.getCoinRack(3).acceptCoin(new Coin(100));
			} catch (CapacityExceededException | DisabledException e) {
				// should never happen
			}
		}
		while (machine.getCoinRack(4).hasSpace()) {
			try {
				machine.getCoinRack(4).acceptCoin(new Coin(200));
			} catch (CapacityExceededException | DisabledException e) {
				// should never happen
			}
		}
		// adds pop to the racks
		for (int i = 0; i < popPrices.length; i++) {
			PopCanRackSimulator rack = machine.getPopCanRack(i);
			for (int j = 0; j < 2; j++) {
				try {
					rack.addPop(new PopCan());
				} catch (CapacityExceededException | DisabledException e) {
					// should never happen
				}
			}
		}
	}

	public void addComponentsToPane2(Container pane) {
		if (RIGHT_TO_LEFT) {
			pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		}

		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
			// natural height, maximum width
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

		if (e.getSource() == b1) {
			machine.getSelectionButton(0).press();
			// SelectedPop = 0;
		} else if (e.getSource() == b2) {
			machine.getSelectionButton(1).press();
			// SelectedPop = 1;
		} else if (e.getSource() == b3) {
			machine.getSelectionButton(2).press();
			// SelectedPop = 2;
		} else if (e.getSource() == b4) {
			machine.getSelectionButton(3).press();
			// SelectedPop = 3;
		} else if (e.getSource() == b5) {
			machine.getSelectionButton(4).press();
			// SelectedPop = 4;
		} else if (e.getSource() == b6) {
			machine.getSelectionButton(5).press();
			// SelectedPop = 5;
		} else if (e.getSource() == b7) {
			machine.getSelectionButton(6).press();
			// SelectedPop = 6;
		} else if (e.getSource() == b8) {
			machine.getSelectionButton(7).press();
			// SelectedPop = 7;
		} else if (e.getSource() == b9) {
			machine.getSelectionButton(8).press();
			// SelectedPop = 8;
		} else if (e.getSource() == b10) {
			machine.getSelectionButton(9).press();
			// SelectedPop = 9;
		} else if (e.getSource() == br) {
			machine.getReturnButton().press();
		} else if (e.getSource() == b20) {
			try {
				machine.getCoinSlot().addCoin(new Coin(5));
			} catch (DisabledException e1) {
				// should never happen
			}
		} else if (e.getSource() == b22) {
			try {
				machine.getCoinSlot().addCoin(new Coin(10));
			} catch (DisabledException e1) {
				// should never happen
			}
		} else if (e.getSource() == b23) {
			try {
				machine.getCoinSlot().addCoin(new Coin(25));
			} catch (DisabledException e1) {
				// should never happen
			}
		} else if (e.getSource() == b24) {
			try {
				machine.getCoinSlot().addCoin(new Coin(100));
			} catch (DisabledException e1) {
				// should never happen
			}
		} else if (e.getSource() == b25) {
			try {
				machine.getCoinSlot().addCoin(new Coin(200));
			} catch (DisabledException e1) {
				// should never happen
			}
		} else if (e.getSource() == b26) {
			try {
				// invalid coin
				machine.getCoinSlot().addCoin(new Coin(50));
			} catch (DisabledException e1) {
				// should never happen
			}
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */

	public void createAndShowGUI() {
		// Create and set up the window.
		frame = new JFrame("Vending Machine");
		f2 = new JFrame("Wallet!!!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Set up the content pane.
		addComponentsToPane(frame.getContentPane());
		addComponentsToPane2(f2.getContentPane());

		// Display the window.
		frame.pack();
		f2.pack();
		frame.setVisible(true);
		f2.setVisible(true);
		// f2.setSize(100, 30);
		f2.setLocation(600, 100);

	}

	public void TurnOnoutOfOrderLight() {
		OutofOrderLight.setBackground(Color.RED);
		OutofOrderLight.setText("Out of Order");
	}

	public void TurnONexactChangeLight() {
		ExactChangeLight.setBackground(Color.GREEN);
	}

	public void TurnOffexactChangeLight() {
		ExactChangeLight.setBackground(Color.WHITE);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		// javax.swing.SwingUtilities.invokeLater(new Runnable() {
		// public void run() {
		// this.createAndShowGUI();
		// }
		// });
		GUI gui = new GUI();
		gui.createAndShowGUI();
		// v.outOfOrderLight();
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

	@Override
	public void activated(IndicatorLightSimulator light) {
		// set equivalent light on the GUI
		if (light == machine.getOutOfOrderLight()) {
			TurnOnoutOfOrderLight();
		} else if (light == machine.getExactChangeLight()) {
			TurnONexactChangeLight();
		}

	}

	@Override
	public void deactivated(IndicatorLightSimulator light) {
		// when turn off the exact change light when there is enough changes in
		// the coin racks
		if (light == machine.getExactChangeLight()) {
			TurnOffexactChangeLight();
		}

	}

	/*
	 * Will empty delivery chute and return the contents of the chute in a human
	 * readable format
	 */
	public String getDeliveryChuteContents() {
		String contents = " ";

		Object[] items = machine.getDeliveryChute().removeItems();

		if (items.length < 1) {
			contents = "Delivery chute empty";
		} else {
			int i = 1;
			// if the last item is a pop can
			// a pop can only be the last item due to the way we have pop being
			// purchased
			if (items[items.length - 1] instanceof PopCan) {
				contents = "Pop delivered. ";
				// only pop can is delivered
				// i.e. exact change
				if (items.length == 1) {
					// coin loop will not run
					i = 0;
				} else {
					// last item will not be counted as a coin
					i = items.length - 1;
				}
			} else {
				// all items are coins
				i = items.length;
			}
			int coinTotal = 0;
			// loop through all coins and add their values together
			for (int j = 0; j < i; j++) {
				coinTotal = +((Coin) (items[j])).getValue();
			}
			if (coinTotal == 0) {
				contents += "No coins returned.";
			} else {
				contents += coinTotal + " cents worth of coins returned.";
			}
		}

		return contents;
	}

}