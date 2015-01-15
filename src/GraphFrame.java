import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


public class GraphFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static double[] xvals, yvals;
	private static double xmin, xmax, ymin, ymax;
	private static double xscl, yscl;
	private static String name;
	public GraphFrame(double[] x, double[] y, double xmn, double xmx, double ymn, double ymx,double xsc, double ysc, String s) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle(name);
		this.setResizable(true);
		this.setBackground(Color.white);
		s = name;
		xscl=xsc;
		yscl=ysc;
		xvals = x;
		yvals = y;
		xmin = xmn;
		ymin= ymn;
		xmax = xmx;
		ymax = ymx;
		
		
		setBounds(100, 100, 600, 600);
		contentPane = new GraphPanel(x,y,xmn,xmx,ymn,ymx,xscl,yscl, s);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GraphFrame frame = new GraphFrame(xvals,yvals,xmin,xmax,ymin,ymax,xscl,yscl,name);
					frame.setVisible(true);
					frame.repaint();
					frame.contentPane.repaint();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
