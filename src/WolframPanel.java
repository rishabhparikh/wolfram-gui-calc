import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAImage;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;


public class WolframPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private static String appid = "AXAEU6-XUKRXW3L2R";
	private ArrayList<LocItem> items;
	private static String in;

	public WolframPanel(String s) {
	    this.setLayout(new FlowLayout());
		this.setVisible(true);
		this.setBackground(Color.white);
		in = s;
		items = new ArrayList<LocItem>();
	}
	
	public void generateComponents() {
		int x = 5, y = 20;
		String input = in;
	       
        WAEngine engine = new WAEngine();
       
        engine.setAppID(appid);
        engine.addFormat("plaintext");
        engine.addFormat("image");

        WAQuery query = engine.createQuery();
       
        query.setInput(input);
        
        try {
           
            WAQueryResult queryResult = engine.performQuery(query);
            
            if (queryResult.isError()) {
                System.out.println("Query error");
                System.out.println("  error code: " + queryResult.getErrorCode());
                System.out.println("  error message: " + queryResult.getErrorMessage());
            } else if (!queryResult.isSuccess()) {
                System.out.println("Query was not understood; no results available.");
                JOptionPane.showMessageDialog((Component)this, "Query not understood");
                this.getParent().setVisible(false);
            } else {
               
                System.out.println("Successful query");
                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                    	items.add(new LocItem(pod.getTitle()+":",x,y));
                        y += 10;
                       
                        for (WASubpod subpod : pod.getSubpods()) {
                        	
                            for (Object element : subpod.getContents()) {
                            	
                                if (element instanceof WAPlainText) {
                                    
                                }
                                else if(element instanceof WAImage) {
                                	
                                	URL url = null;
                                	try {
										url = new URL(((WAImage)element).getURL());
										
									} catch (MalformedURLException e) {
										
										e.printStackTrace();
									}
                                	try {
                                		
                                		items.add(new LocItem(ImageIO.read(url),x,y));
                                		
                                		y += items.get(items.size() - 1).getImage().getHeight(this) + 15;
                                		
										
									} catch (IOException e) {
										
										e.printStackTrace();
									}
                                }
                            }
                        }
                        System.out.println("");
                    }
                }

            }
        } catch (WAException e) {
            e.printStackTrace();
        }
        this.setPreferredSize(new Dimension(600,y));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		
		for(LocItem l : items) {
			if(l.getType() == true) {
				g.drawImage(l.getImage(), l.getX(), l.getY(), this);
			}
			else {
				g.drawString(l.getText(), l.getX(), l.getY());
			}
		}
	}
}
