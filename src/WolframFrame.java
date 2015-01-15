import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class WolframFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private static String in;
    private JPanel contentPane;
    private JScrollPane scroll;
    
    public WolframFrame(String x) {
    	in = x;
    	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	this.setLayout(new FlowLayout());
		this.setResizable(true);
		this.setBackground(Color.white);
		contentPane = new WolframPanel(x);
		try{
		((WolframPanel)contentPane).generateComponents();
		}
		catch(NullPointerException e) {
			this.dispose();
		}
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		scroll = new JScrollPane(contentPane);
		scroll.setPreferredSize(new Dimension(600,600));
		scroll.setWheelScrollingEnabled(true);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.requestFocus();
		scroll.requestFocusInWindow();
		add(scroll);
		setBounds(100,100,600,600);
    }
    
    public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WolframFrame frame = new WolframFrame(in);
					frame.setVisible(true);
					frame.repaint();
					frame.scroll.repaint();
					frame.contentPane.repaint();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }

}
