import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUIPanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private double[] xVals, yVals;
	private double xmin = -10, xmax = 10, ymin = -10, ymax = 10, xscl = 1, yscl = 1;
	private JTextField textField;
	private ArrayList<String> functions;
	private ArrayList<Integer> chars;
	private ArrayList<String> ops;
	private boolean displaying = false;
	private static final int LEFT_ASSOC = 0, RIGHT_ASSOC = 1; 
	private String ans = "";
	private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
	private JCheckBox radians;
	static {
		OPERATORS.put("+", new int[] { 0, LEFT_ASSOC });
		OPERATORS.put("-", new int[] { 0, LEFT_ASSOC });
		OPERATORS.put("*", new int[] { 5, LEFT_ASSOC });
		OPERATORS.put("/", new int[] { 5, LEFT_ASSOC });
		OPERATORS.put("%", new int[] { 5, LEFT_ASSOC });
		OPERATORS.put("^", new int[] { 10, RIGHT_ASSOC });
		OPERATORS.put("sin", new int[] {15, RIGHT_ASSOC});
		OPERATORS.put("cos", new int[] {15, RIGHT_ASSOC});
		OPERATORS.put("tan", new int[] {15, RIGHT_ASSOC});
		OPERATORS.put("log", new int[] {15, RIGHT_ASSOC});
		OPERATORS.put("ln", new int[] {15, RIGHT_ASSOC});
		OPERATORS.put(String.valueOf('\u221A'), new int[] {15, RIGHT_ASSOC});
		OPERATORS.put("arcsin", new int[] {15, RIGHT_ASSOC});
		OPERATORS.put("arccos", new int[] {15, RIGHT_ASSOC});
		OPERATORS.put("arctan", new int[] {15, RIGHT_ASSOC});
	}
	
	public GUIPanel() {
		xmax = 10;
		xmin = -10;
		ymax = 10;
		ymin = -10;
		xscl = 1;
		yscl = 1;
		String[] s = {"+","-","/","*","%","^","(",")","sin", "cos", "tan", "log", "ln", String.valueOf('\u221A'), "arcsin", "arccos", "arctan"};
		functions = new ArrayList<String>();
		for(String x : s)
			functions.add(x);
		ActionListener eqAct = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!textField.getText().isEmpty() && displaying == false) {
					String[] input = textField.getText().split(" ");
					String[] output = infixToRPN(input);
					ans = "" + evalRPN(output);
					textField.setText(ans);
					displaying = true;
					ops.clear();
				}
				reqFoq();
			}
		};
		
		ActionListener numAct = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(displaying && functions.contains(((JButton)e.getSource()).getText())) {
					textField.setText(ans + " " + ((JButton)e.getSource()).getText() + " ");
					ops.add(String.valueOf(((JButton)e.getSource()).getText()));
					displaying = false;
				}
				else {
					if(displaying) {
						displaying = false;
						textField.setText("");
					}
					if(functions.contains(((JButton)e.getSource()).getText())) {
						if(ops.size()> 0 && functions.contains(ops.get(ops.size()-1)) && ((JButton)e.getSource()).getText().equals("-")) {
							textField.setText(textField.getText() + ((JButton)e.getSource()).getText());
							ops.clear();
						}	
						else {
							ops.add(String.valueOf(((JButton)e.getSource()).getText()));
							textField.setText(textField.getText() + " " + ((JButton)e.getSource()).getText() + " ");
						}
					}
					else {
						textField.setText(textField.getText() + ((JButton)e.getSource()).getText());
						ops.clear();
					}	
					reqFoq();
				}
			}
		};
		
		ops = new ArrayList<String>();
		
		chars = new ArrayList<Integer>();
		for(int i = 48; i < 58; i++) {
			chars.add(i);
		}
		
		int[] c = {32,37,40,41,42,43,45,46,47,94};
		for(int j: c)
			chars.add(j);
		chars.add((int)'x');
		chars.add((int)'X');
		chars.add((int)'e');
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{66, 56, 56, 50, 56, 56, 56, 81, 147, 0, 117, 0};
		gridBagLayout.rowHeights = new int[]{62, 29, 16, 0, 2, 0, 0, 29, 2, 15, 44, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JButton btnNewButton = new JButton("1");
		btnNewButton.addActionListener(numAct);
		
		textField = new JTextField("");
		textField.setFont(new Font("monospaced",Font.BOLD,18));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridheight = 2;
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridwidth = 8;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		add(textField, gbc_textField);
		textField.setFocusable(false);
		
		JLabel lblNewLabel = new JLabel("Note: You must use a multiplication sign(*) between numbers and functions. Implied multipication does not work.");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 9;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 3;
		add(lblNewLabel, gbc_lblNewLabel);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 4;
		add(btnNewButton, gbc_btnNewButton);
		
		JButton btnNewButton_1 = new JButton("2");
		btnNewButton_1.addActionListener(numAct);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 2;
		gbc_btnNewButton_1.gridy = 4;
		add(btnNewButton_1, gbc_btnNewButton_1);
			
		JButton button = new JButton("3");
		button.addActionListener(numAct);
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.anchor = GridBagConstraints.NORTH;
		gbc_button.fill = GridBagConstraints.HORIZONTAL;
		gbc_button.insets = new Insets(0, 0, 5, 5);
		gbc_button.gridx = 3;
		gbc_button.gridy = 4;
		add(button, gbc_button);
		
		JButton button_8 = new JButton("+");
		button_8.addActionListener(numAct);
		GridBagConstraints gbc_button_8 = new GridBagConstraints();
		gbc_button_8.anchor = GridBagConstraints.NORTH;
		gbc_button_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_8.insets = new Insets(0, 0, 5, 5);
		gbc_button_8.gridx = 4;
		gbc_button_8.gridy = 4;
		add(button_8, gbc_button_8);
		
		JButton button_9 = new JButton("-");
		button_9.addActionListener(numAct);
		GridBagConstraints gbc_button_9 = new GridBagConstraints();
		gbc_button_9.anchor = GridBagConstraints.NORTH;
		gbc_button_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_9.insets = new Insets(0, 0, 5, 5);
		gbc_button_9.gridx = 5;
		gbc_button_9.gridy = 4;
		add(button_9, gbc_button_9);
		
		JButton log = new JButton("log");
		log.addActionListener(numAct);
		GridBagConstraints gbc_log = new GridBagConstraints();
		gbc_log.anchor = GridBagConstraints.NORTH;
		gbc_log.fill = GridBagConstraints.HORIZONTAL;
		gbc_log.insets = new Insets(0, 0, 5, 5);
		gbc_log.gridx = 6;
		gbc_log.gridy = 4;
		add(log, gbc_log);

		JButton button_1 = new JButton("4");
		button_1.addActionListener(numAct);
		
		JButton btnNewButton_2 = new JButton("Graph");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!textField.getText().isEmpty()) {
					String in = textField.getText();
						
					xVals = new double[(int)(xmax-xmin) * 400];
					yVals = new double[(int)(xmax-xmin) * 400];
					for(int i = 0; i < xVals.length; i++) {
						xVals[i] = xmin + ((double)i/400);
					}
							
					for(int i = 0; i < yVals.length; i++) {
						String insert = String.valueOf(xVals[i]);
						String[] input = in.split(" ");
						for(int j = 0; j < input.length; j++) {
							if(input[j].equalsIgnoreCase("x"))
								input[j] = insert;
							else if(input[j].equalsIgnoreCase("-x"))
								input[j] = String.valueOf(-Double.parseDouble(insert));
							}
							yVals[i] = -evalRPN(infixToRPN(input));
						}		
						
						new GraphFrame(xVals,yVals,xmin,xmax,ymin,ymax,xscl,yscl, "Y= " + textField.getText()).setVisible(true);
						}
						reqFoq();
					}
				});
		
		radians = new JCheckBox("Radians");
		GridBagConstraints gbc_chckbxRadians = new GridBagConstraints();
		gbc_chckbxRadians.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxRadians.gridx = 7;
		gbc_chckbxRadians.gridy = 4;
		add(radians, gbc_chckbxRadians);
		
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.gridwidth = 2;
		gbc_btnNewButton_2.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridheight = 2;
		gbc_btnNewButton_2.gridx = 8;
		gbc_btnNewButton_2.gridy = 4;
		add(btnNewButton_2, gbc_btnNewButton_2);
		GridBagConstraints gbc_button_1 = new GridBagConstraints();
		gbc_button_1.anchor = GridBagConstraints.NORTH;
		gbc_button_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_1.insets = new Insets(0, 0, 5, 5);
		gbc_button_1.gridx = 1;
		gbc_button_1.gridy = 5;
		add(button_1, gbc_button_1);
		
		JButton button_2 = new JButton("5");
		button_2.addActionListener(numAct);
		GridBagConstraints gbc_button_2 = new GridBagConstraints();
		gbc_button_2.anchor = GridBagConstraints.NORTH;
		gbc_button_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_2.insets = new Insets(0, 0, 5, 5);
		gbc_button_2.gridx = 2;
		gbc_button_2.gridy = 5;
		add(button_2, gbc_button_2);
		
		JButton button_3 = new JButton("6");
		button_3.addActionListener(numAct);
		GridBagConstraints gbc_button_3 = new GridBagConstraints();
		gbc_button_3.anchor = GridBagConstraints.NORTH;
		gbc_button_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_3.insets = new Insets(0, 0, 5, 5);
		gbc_button_3.gridx = 3;
		gbc_button_3.gridy = 5;
		add(button_3, gbc_button_3);
		
		JButton button_10 = new JButton("*");
		button_10.addActionListener(numAct);
		GridBagConstraints gbc_button_10 = new GridBagConstraints();
		gbc_button_10.anchor = GridBagConstraints.NORTH;
		gbc_button_10.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_10.insets = new Insets(0, 0, 5, 5);
		gbc_button_10.gridx = 4;
		gbc_button_10.gridy = 5;
		add(button_10, gbc_button_10);
		
		JButton button_11 = new JButton("/");
		button_11.addActionListener(numAct);
		GridBagConstraints gbc_button_11 = new GridBagConstraints();
		gbc_button_11.anchor = GridBagConstraints.NORTH;
		gbc_button_11.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_11.insets = new Insets(0, 0, 5, 5);
		gbc_button_11.gridx = 5;
		gbc_button_11.gridy = 5;
		add(button_11, gbc_button_11);
		
		JButton btnNewButton_8 = new JButton("ln");
		btnNewButton_8.addActionListener(numAct);
		GridBagConstraints gbc_btnNewButton_8 = new GridBagConstraints();
		gbc_btnNewButton_8.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_8.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_8.gridx = 6;
		gbc_btnNewButton_8.gridy = 5;
		add(btnNewButton_8, gbc_btnNewButton_8);

		JButton button_4 = new JButton("7");
		button_4.addActionListener(numAct);
		
		JButton btnArcsin = new JButton("arcsin");
		btnArcsin.addActionListener(numAct);
		
		GridBagConstraints gbc_btnArcsin = new GridBagConstraints();
		gbc_btnArcsin.anchor = GridBagConstraints.NORTH;
		gbc_btnArcsin.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnArcsin.insets = new Insets(0, 0, 5, 5);
		gbc_btnArcsin.gridx = 7;
		gbc_btnArcsin.gridy = 5;
		add(btnArcsin, gbc_btnArcsin);
		
		GridBagConstraints gbc_button_4 = new GridBagConstraints();
		gbc_button_4.anchor = GridBagConstraints.NORTH;
		gbc_button_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_4.insets = new Insets(0, 0, 5, 5);
		gbc_button_4.gridx = 1;
		gbc_button_4.gridy = 6;
		add(button_4, gbc_button_4);

		JButton button_5 = new JButton("8");
		button_5.addActionListener(numAct);
		GridBagConstraints gbc_button_5 = new GridBagConstraints();
		gbc_button_5.anchor = GridBagConstraints.NORTH;
		gbc_button_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_5.insets = new Insets(0, 0, 5, 5);
		gbc_button_5.gridx = 2;
		gbc_button_5.gridy = 6;
		add(button_5, gbc_button_5);
		
		JButton button_6 = new JButton("9");
		button_6.addActionListener(numAct);
		GridBagConstraints gbc_button_6 = new GridBagConstraints();
		gbc_button_6.anchor = GridBagConstraints.NORTH;
		gbc_button_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_6.insets = new Insets(0, 0, 5, 5);
		gbc_button_6.gridx = 3;
		gbc_button_6.gridy = 6;
		add(button_6, gbc_button_6);
		
		JButton button_12 = new JButton("^");
		button_12.addActionListener(numAct);
		GridBagConstraints gbc_button_12 = new GridBagConstraints();
		gbc_button_12.anchor = GridBagConstraints.NORTH;
		gbc_button_12.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_12.insets = new Insets(0, 0, 5, 5);
		gbc_button_12.gridx = 4;
		gbc_button_12.gridy = 6;
		add(button_12, gbc_button_12);
		
		JButton button_13 = new JButton("%");
		button_13.addActionListener(numAct);
		GridBagConstraints gbc_button_13 = new GridBagConstraints();
		gbc_button_13.anchor = GridBagConstraints.NORTH;
		gbc_button_13.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_13.insets = new Insets(0, 0, 5, 5);
		gbc_button_13.gridx = 5;
		gbc_button_13.gridy = 6;
		add(button_13, gbc_button_13);
		
		JButton btnNewButton_7 = new JButton("Clear");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displaying = false;
				textField.setText("");
				reqFoq();
			}
		});
		
		JButton button_14 = new JButton("(");
		button_14.addActionListener(numAct);
		
		JButton btnNewButton_9 = new JButton("e");
		btnNewButton_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText(textField.getText() + Math.E);
				reqFoq();
			}
		});
		
		GridBagConstraints gbc_btnNewButton_9 = new GridBagConstraints();
		gbc_btnNewButton_9.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_9.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_9.gridx = 6;
		gbc_btnNewButton_9.gridy = 6;
		add(btnNewButton_9, gbc_btnNewButton_9);
		
		JButton btnArccos = new JButton("arccos");
		btnArccos.addActionListener(numAct);
		GridBagConstraints gbc_btnArccos = new GridBagConstraints();
		gbc_btnArccos.anchor = GridBagConstraints.NORTH;
		gbc_btnArccos.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnArccos.insets = new Insets(0, 0, 5, 5);
		gbc_btnArccos.gridx = 7;
		gbc_btnArccos.gridy = 6;
		add(btnArccos, gbc_btnArccos);
		
		JButton btnNewButton_4 = new JButton("Window");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					xmin = Double.parseDouble(JOptionPane.showInputDialog("xMin?"));
					xmax = Double.parseDouble(JOptionPane.showInputDialog("xMax?"));
					ymin = Double.parseDouble(JOptionPane.showInputDialog("yMin?"));
					ymax = Double.parseDouble(JOptionPane.showInputDialog("yMax?"));
					xscl = Double.parseDouble(JOptionPane.showInputDialog("xScl?"));
					yscl = Double.parseDouble(JOptionPane.showInputDialog("yScl?"));
					
				}
				catch(Exception x) {
					
				}
				reqFoq();
			}
		});
		
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.gridwidth = 2;
		gbc_btnNewButton_4.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_4.gridheight = 2;
		gbc_btnNewButton_4.gridx = 8;
		gbc_btnNewButton_4.gridy = 6;
		add(btnNewButton_4, gbc_btnNewButton_4);
		
		GridBagConstraints gbc_button_14 = new GridBagConstraints();
		gbc_button_14.anchor = GridBagConstraints.NORTH;
		gbc_button_14.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_14.insets = new Insets(0, 0, 5, 5);
		gbc_button_14.gridx = 1;
		gbc_button_14.gridy = 7;
		add(button_14, gbc_button_14);
		
		JButton button_7 = new JButton("0");
		button_7.addActionListener(numAct);
		GridBagConstraints gbc_button_7 = new GridBagConstraints();
		gbc_button_7.anchor = GridBagConstraints.NORTH;
		gbc_button_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_7.insets = new Insets(0, 0, 5, 5);
		gbc_button_7.gridx = 2;
		gbc_button_7.gridy = 7;
		add(button_7, gbc_button_7);
		
		JButton button_15 = new JButton(")");
		button_15.addActionListener(numAct);
		GridBagConstraints gbc_button_15 = new GridBagConstraints();
		gbc_button_15.anchor = GridBagConstraints.NORTH;
		gbc_button_15.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_15.insets = new Insets(0, 0, 5, 5);
		gbc_button_15.gridx = 3;
		gbc_button_15.gridy = 7;
		add(button_15, gbc_button_15);
		
		JButton button_17 = new JButton("sin");
		button_17.addActionListener(numAct);
		GridBagConstraints gbc_button_17 = new GridBagConstraints();
		gbc_button_17.anchor = GridBagConstraints.NORTH;
		gbc_button_17.fill = GridBagConstraints.HORIZONTAL;
		gbc_button_17.insets = new Insets(0, 0, 5, 5);
		gbc_button_17.gridx = 4;
		gbc_button_17.gridy = 7;
		add(button_17, gbc_button_17);
		
		JButton btnNewButton_3 = new JButton("cos");
		btnNewButton_3.addActionListener(numAct);
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_3.gridx = 5;
		gbc_btnNewButton_3.gridy = 7;
		add(btnNewButton_3, gbc_btnNewButton_3);
		
		JButton btnNewButton_10 = new JButton(String.valueOf('\u03C0'));
		btnNewButton_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textField.setText(textField.getText() + Math.PI);
				reqFoq();
			}
		});
		
		GridBagConstraints gbc_btnNewButton_10 = new GridBagConstraints();
		gbc_btnNewButton_10.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_10.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_10.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_10.gridx = 6;
		gbc_btnNewButton_10.gridy = 7;
		add(btnNewButton_10, gbc_btnNewButton_10);
		
		JButton btnArctan = new JButton("arctan");
		btnArctan.addActionListener(numAct);
		GridBagConstraints gbc_btnArctan = new GridBagConstraints();
		gbc_btnArctan.anchor = GridBagConstraints.NORTH;
		gbc_btnArctan.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnArctan.insets = new Insets(0, 0, 5, 5);
		gbc_btnArctan.gridx = 7;
		gbc_btnArctan.gridy = 7;
		add(btnArctan, gbc_btnArctan);
		GridBagConstraints gbc_btnNewButton_7 = new GridBagConstraints();
		gbc_btnNewButton_7.anchor = GridBagConstraints.SOUTH;
		gbc_btnNewButton_7.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_7.gridx = 1;
		gbc_btnNewButton_7.gridy = 8;
		add(btnNewButton_7, gbc_btnNewButton_7);
		
		JButton button_16 = new JButton("=");
		button_16.addActionListener(eqAct);
		GridBagConstraints gbc_button_16 = new GridBagConstraints();
		gbc_button_16.insets = new Insets(0, 0, 5, 5);
		gbc_button_16.gridx = 2;
		gbc_button_16.gridy = 8;
		add(button_16, gbc_button_16);
		
		JButton btnNewButton_5 = new JButton("tan");
		btnNewButton_5.addActionListener(numAct);
		
		JButton btnAns = new JButton("Ans");
		btnAns.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textField.setText(textField.getText() + ans);
				reqFoq();
			}
		});
		
		GridBagConstraints gbc_btnAns = new GridBagConstraints();
		gbc_btnAns.insets = new Insets(0, 0, 5, 5);
		gbc_btnAns.gridx = 3;
		gbc_btnAns.gridy = 8;
		add(btnAns, gbc_btnAns);
		
		GridBagConstraints gbc_btnNewButton_5 = new GridBagConstraints();
		gbc_btnNewButton_5.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_5.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_5.gridx = 4;
		gbc_btnNewButton_5.gridy = 8;
		add(btnNewButton_5, gbc_btnNewButton_5);
		JButton btnX = new JButton("x");
		btnX.addActionListener(numAct);
		
		GridBagConstraints gbc_btnX = new GridBagConstraints();
		gbc_btnX.anchor = GridBagConstraints.NORTH;
		gbc_btnX.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnX.insets = new Insets(0, 0, 5, 5);
		gbc_btnX.gridx = 5;
		gbc_btnX.gridy = 8;
		add(btnX, gbc_btnX);
		
		JButton btnNewButton_11 = new JButton(String.valueOf('\u221A'));
		btnNewButton_11.addActionListener(numAct);
		GridBagConstraints gbc_btnNewButton_11 = new GridBagConstraints();
		gbc_btnNewButton_11.anchor = GridBagConstraints.NORTH;
		gbc_btnNewButton_11.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_11.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_11.gridx = 6;
		gbc_btnNewButton_11.gridy = 8;
		add(btnNewButton_11, gbc_btnNewButton_11);
		
		JButton btnNewButton_6 = new JButton("Wolfram Alpha");
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!textField.getText().isEmpty())
					new WolframFrame(textField.getText()).setVisible(true);
				else {
					String s = JOptionPane.showInputDialog("Search WolframAlpha:");
					if(s!= null && !s.isEmpty())
						new WolframFrame(s).setVisible(true);
				}
			}	
		});
		
		GridBagConstraints gbc_btnNewButton_6 = new GridBagConstraints();
		gbc_btnNewButton_6.gridwidth = 2;
		gbc_btnNewButton_6.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_6.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_6.gridheight = 2;
		gbc_btnNewButton_6.gridx = 8;
		gbc_btnNewButton_6.gridy = 8;
		add(btnNewButton_6, gbc_btnNewButton_6);
	}
	
	private static boolean isOperator(String token) {
		return OPERATORS.containsKey(token);
	}
	
	private static boolean isAssociative(String token, int type) {
		if (!isOperator(token)) {
			throw new IllegalArgumentException("Invalid token: " + token);
		}
		if (OPERATORS.get(token)[1] == type) {
			return true;
		}
		return false;
	}
	
	private static final int cmpPrecedence(String token1, String token2) {
		if (!isOperator(token1) || !isOperator(token2)) {
			throw new IllegalArgumentException("Invalied tokens: " + token1
					+ " " + token2);
		}
		return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
	}
	
	public static String[] infixToRPN(String[] inputTokens) {
			ArrayList<String> out = new ArrayList<String>();
			Stack<String> stack = new Stack<String>();
			for (String token : inputTokens) {
				if (isOperator(token)) {
					while (!stack.empty() && isOperator(stack.peek())) {
						if ((isAssociative(token, LEFT_ASSOC) && cmpPrecedence(
								token, stack.peek()) <= 0)
								|| (isAssociative(token, RIGHT_ASSOC) && cmpPrecedence(
										token, stack.peek()) < 0)) {
							out.add(stack.pop());
							continue;
						}
						break;
					}
					
					stack.push(token);
				} else if (token.equals("(")) {
					stack.push(token);
				} else if (token.equals(")")) {
					while (!stack.empty() && !stack.peek().equals("(")) {
						out.add(stack.pop());
					}
					stack.pop();
				} else {
					out.add(token);
				}
			}
			while (!stack.empty()) {
				out.add(stack.pop());
			}
			String[] output = new String[out.size()];
			return out.toArray(output);
		}
	
	public void reqFoq() {
		this.requestFocus();
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {}
	
	@Override
	public void keyReleased(KeyEvent arg0) {}
	
	public double evalRPN(String[] expr){
		Stack<Double> stack = new Stack<Double>();
		try {
			for(String token:expr){
				
				Double tokenNum = null;
				try{
					tokenNum = Double.parseDouble(token);
				}catch(NumberFormatException e){}
				if(tokenNum != null){
					stack.push(Double.parseDouble(token+""));
				}else if(token.equals("*")){
					double secondOperand = stack.pop();
					double firstOperand = stack.pop();
					stack.push(firstOperand * secondOperand);
				}else if(token.equals("/")){			
					double secondOperand = stack.pop();
					double firstOperand = stack.pop();
					stack.push(firstOperand / secondOperand);
				}else if(token.equals("-")){				
					double secondOperand = stack.pop();
					double firstOperand = stack.pop();
					stack.push(firstOperand - secondOperand);
				}else if(token.equals("+")){				
					double secondOperand = stack.pop();
					double firstOperand = stack.pop();
					stack.push(firstOperand + secondOperand);
				}else if(token.equals("^")){				
					double secondOperand = stack.pop();
					double firstOperand = stack.pop();
					stack.push(Math.pow(firstOperand, secondOperand));
				}else if(token.equals("%")){			
					double secondOperand = stack.pop();
					double firstOperand = stack.pop();
					stack.push(firstOperand % secondOperand);	
				}else if(token.equals("sin")){
					double firstOperand = stack.pop();
					if(radians.isSelected())
						stack.push(Math.sin(firstOperand));
					else
						stack.push(Math.sin(firstOperand * (Math.PI /180)));
				}else if(token.equals("cos")){
					double firstOperand = stack.pop();
					if(radians.isSelected())
						stack.push(Math.cos(firstOperand));
					else
						stack.push(Math.cos(firstOperand * (Math.PI /180)));
				}else if(token.equals("tan")){
					double firstOperand = stack.pop();
					if(radians.isSelected())
						stack.push(Math.tan(firstOperand));
					else
						stack.push(Math.tan(firstOperand * (Math.PI /180)));	
				}else if(token.equals("arcsin")){
					double firstOperand = stack.pop();
					if(radians.isSelected())
						stack.push(Math.asin(firstOperand));
					else
						stack.push(Math.asin(firstOperand) * 180 / Math.PI);
				}else if(token.equals("arccos")){
					double firstOperand = stack.pop();
					if(radians.isSelected())
						stack.push(Math.acos(firstOperand));
					else
						stack.push(Math.acos(firstOperand) * 180 / Math.PI);
				}else if(token.equals("arctan")){
					double firstOperand = stack.pop();
					if(radians.isSelected())
						stack.push(Math.atan(firstOperand));
					else
						stack.push(Math.atan(firstOperand)* 180 / Math.PI);	
				}else if(token.equals("log")) {
					double firstOperand = stack.pop();
					stack.push(Math.log10(firstOperand));
				}else if(token.equals("ln")) {
					double firstOperand = stack.pop();
					stack.push(Math.log(firstOperand));
				}
				else if(token.equals(String.valueOf('\u221A'))) {
					double firstOperand = stack.pop();
					stack.push(Math.sqrt(firstOperand));
				}
				else {}
			}
			return stack.pop();
		}
		catch(EmptyStackException e) {
			//JOptionPane.showMessageDialog((Component)this,"Invalid Format");
			return 0.0;
		}
	}	
	
	@Override
	public void keyTyped(KeyEvent e) {
		
		if(e.getKeyChar() == KeyEvent.VK_ENTER && displaying == false) {
			
			ops.clear();
			String[] input = textField.getText().split(" ");
			String[] output = infixToRPN(input);
			ans = "" + evalRPN(output);
			textField.setText(ans);
			displaying = true;
		}
		else {
			
			if(e.getKeyChar() == KeyEvent.VK_BACK_SPACE && textField.getText().length() > 0) {
				while(textField.getText().length() >= 2 && textField.getText().substring(textField.getText().length() - 2, textField.getText().length() - 1).equals(" ")) {
					textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
				}
				textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
			}
			if(displaying && functions.contains(String.valueOf(e.getKeyChar()))) {
				textField.setText(ans + " " + e.getKeyChar() + " ");
				ops.add(String.valueOf(e.getKeyChar()));
				displaying = false;
			}
			else {
				if(displaying) {
					displaying = false;
					textField.setText("");
				}
				boolean typable = chars.contains((int)e.getKeyChar());
				boolean isFunction = functions.contains(String.valueOf(e.getKeyChar()));
				if(typable && !isFunction) {
					ops.clear();
					if(e.getKeyChar() != 'e')
						textField.setText(textField.getText() + e.getKeyChar());
					else
						textField.setText(textField.getText() + Math.E);
				}
				else if(isFunction) {
					if(ops.size() > 0 && functions.contains(ops.get(ops.size()-1)) && e.getKeyChar() == KeyEvent.VK_MINUS) {
						textField.setText(textField.getText() + e.getKeyChar());
						
					}	
					else {
						ops.add(String.valueOf(e.getKeyChar()));
						textField.setText(textField.getText() + " " + e.getKeyChar() + " ");
					}	
				}
			}
		}
	}
}
