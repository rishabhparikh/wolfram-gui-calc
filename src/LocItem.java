import java.awt.Image;
import java.awt.image.BufferedImage;


public class LocItem {
	private BufferedImage img;
	private int x, y;
	private String text;
	//true = image, false = string
	private boolean type;
	
	public LocItem(BufferedImage i, int xc, int yc) {
		img = i;
		x = xc;
		y = yc;
		type = true;
	}
	public LocItem(String s, int xc, int yc) {
		text = s;
		x = xc;
		y = yc;
		type = false;
	}
	
	public boolean getType() {
		return type;
	}
	
	public String getText() {
		return text;
	}
	
	public Image getImage() {
		return img;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setText(String s) {
		text = s;
	}
	
	public void setImage(BufferedImage i) {
		img = i;
	}
	
	public void setX(int xc) {
		x = xc;
	}
	
	public void setY(int yc) {
		y = yc;
	}
}
