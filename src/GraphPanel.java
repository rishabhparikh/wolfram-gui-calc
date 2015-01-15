import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


public class GraphPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	double[] xvals, yvals;
	private double xmod,ymod;
	private double xmin, xmax, ymin, ymax,xscl,yscl;
	
	public GraphPanel(double[] x, double[] y, double xmn, double xmx, double ymn, 
			double ymx,double xsc,double ysc, String s) {
		setLayout(null);
		this.setBackground(Color.white);
		xscl=xsc;
		yscl=ysc;
		xvals = x;
		yvals = y;
		xmin = xmn;
		ymin= ymn;
		xmax = xmx;
		ymax = ymx;
		this.setBounds(0,0,600,600);
		this.setVisible(true);
		xmod = this.getWidth()/(xmax-xmin);
		ymod = this.getHeight()/(ymax-ymin);
		
	}
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.pink);
		int xa = 0; 
		int ya = 0;
		int xdraw = 0;
		int ydraw = 0;
		
		 
			if(xmin<=0 && xmax>=0) {
				xa = (int)Math.abs(xmin);
				xdraw= (int)(xa*xmod);
				g.drawLine(xdraw, 0, xdraw, this.getHeight());
				System.out.println("X: "+ xa +", " + xdraw);		
			}
			
			if(ymin <=0 && ymax>=0) {
				ya = (int)Math.abs(ymin);
				ydraw= this.getHeight() - (int)(ya*ymod);
				g.drawLine(0, ydraw, this.getWidth(), ydraw);
				System.out.println("Y: "+ ya +", " + ydraw);
			}
		
		for(int i = xdraw; i < this.getWidth(); i+= xscl*xmod)
			g.drawLine(i, ydraw - 10, i, ydraw + 10);
		for(int i = xdraw; i >= 0; i-= xscl*xmod)
			g.drawLine(i, ydraw - 10, i, ydraw + 10);
		for(int i = ydraw; i < this.getWidth(); i+= yscl*ymod)
			g.drawLine(xdraw - 10, i, xdraw + 10,i);
		for(int i = ydraw; i >= 0; i-= yscl*ymod)
			g.drawLine(xdraw - 10, i, xdraw + 10,i);
		
		g.setColor(Color.BLACK);
		for(int i = 0; i < xvals.length - 1; i++) 
			g.drawLine((int)(xvals[i]*xmod) + xdraw,(int)(yvals[i]*ymod) + ydraw, (int)(xvals[i+1]*xmod) + xdraw, 
					(int)(yvals[i+1]*ymod) + ydraw);
		
		
	}

}
