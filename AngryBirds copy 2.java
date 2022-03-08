import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.*;
import java.lang.Thread;
//what I add in the program
//add backgroud an sling
//birds rolling in the sky by using a function which can make the image Rotate 90 degress to make birds look like rolling in the sky
//bird can't fly out of the Frame
//you can only shoot the bird above grass 

public class AngryBirds extends JFrame{
	
	public AngryBirds(){
		setSize(800,500);
		setTitle("Angry Birds...");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		MyJPanel myJPanel= new MyJPanel();
		Container c = getContentPane();
		c.add(myJPanel);
		setVisible(true);
		setResizable(false);
	}
	
	public static void main(String[] args){
		new AngryBirds();
	}
	
	public class MyJPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
		Timer timer;
		Image image;
		Image init_image,sling_image,pig_image,score_image;
		Image win_image,lose_image;
		int my_x, my_y;
		int mouse_x, mouse_y;
		int start_x, start_y;
		int init_x=100, init_y=265;
		int i=0,count=5;
		int score =0;
		double t=0.0, v=100.0;
		double v_x, v_y;
		int my_width, my_height,sling_height;
		int pig_width,pig_height;
		int grab_flag=0;
		int enemy_alive[];
		int enemy_x[];
		int enemy_y[];
		int star[];
		int bird_alive = 5;
		JButton buttonReset;

		
		public MyJPanel(){
			setBackground(Color.white);
			addMouseListener(this);
			addMouseMotionListener(this);
			
			ImageIcon icon = new ImageIcon("bird.png");
			ImageIcon sling = new ImageIcon("sling.png");
			ImageIcon pig = new ImageIcon("pig_failed.png");
			ImageIcon score = new ImageIcon("stars-edited-1.png");
			ImageIcon lose = new ImageIcon("lose.png");
			ImageIcon win = new ImageIcon("win.png");
			score_image=score.getImage();
			pig_image=pig.getImage();
			sling_image = sling.getImage();
			init_image = icon.getImage();
			win_image=win.getImage();
			lose_image=lose.getImage();


			image=init_image;

			sling_height = sling_image.getWidth(this);
			my_width = image.getWidth(this);
			my_height = image.getHeight(this);
			my_x = init_x;
			my_y = init_y;

			pig_width = pig_image.getWidth(this);
			pig_height = pig_image.getHeight(this);
			enemy_alive = new int[3];
			enemy_x = new int[3];
			enemy_y = new int[3];
			star= new int[3];

			init_game();
			
		}
		private void init_game(){
			for (int i = 0; i < 3; i++) {
				enemy_alive[i] = 1; //all alive
				enemy_x[i]=400+100*i;
				enemy_y[i]=290;
				star[i]=0;
			}	
			// bird_alive = 5;
			// init_x=100;
			// init_y=265;
			// i=0;
			// count=5;
			// score =0;
			// t=0.0;
			// v=100.0;
			// grab_flag=0;
	
		}
		//add background
		public void paintComponent(Graphics g){
			ImageIcon background_icon = new ImageIcon("background.png");
			
			super.paintComponent(g);
			//background
			g.drawImage(background_icon.getImage(), 0, 0, getSize().width,
			  getSize().height, this);

			//line
			if(grab_flag==1){
				g.drawLine(95+my_width/2-1,290,mouse_x,mouse_y);
				g.drawLine(95+my_width/2+19,290,mouse_x,mouse_y);
			}

			//sling
			g.drawImage(sling_image,95,270,50,100,this);

			//bird
			for (int i = 0; i < bird_alive; i++) {
				if(i==0){
					g.drawImage(image,my_x,my_y,my_width,my_height,this);
				}else{
					g.drawImage(init_image,100-i*20,340,my_width,my_height,this);
				}

			}
			//enemy
			for (int i = 0; i < 3; i++) {
				if (enemy_alive[i] == 1) {
					g.drawImage(pig_image,enemy_x[i],enemy_y[i], my_width+25,my_height+25,this);
				}
			}	
			//score
			for (int i = 0; i < score; i++) {
				if (star[i] == 1) {
					g.drawImage(score_image,600+i*50,50,50,50,this);
				}
			}

			if(check_if_sucess()==1){
				g.drawImage(win_image,0,0,700,400,this);
				
			}else if(check_if_sucess()==0){
				g.drawImage(lose_image,0,0,700,400,this);
				
			}
			
		}
		
		public void actionPerformed(ActionEvent e){
			Dimension d;
			d=getSize();

//add the range for the bird to move and flipped when it is moving
//-------------------------------------------------------------------
			my_x = (int)(v*v_x*t+start_x);
			my_y = (int)(9.8*t*t/2 - v*v_y*t+start_y);
			
			if(my_x>0&&my_x<d.width-30){
				t+=0.2;
				image = genImageRotate90(image);
			}else{
				my_y = my_y+10*i;
				i+=1;
			}
//-------------------------------------------------------------------
			if((my_x==0)||(my_x==d.width)||(my_y>=d.height-130)||(my_y<=0)){
				timer.stop();
				my_x=init_x;
				my_y=init_y;
				image = init_image;
				t=0.0;
				i=0;	
				count=5;
				bird_alive--;
			}
			grab_flag=0;

			checkHitToEnemy();
			
			
			repaint();
		}

		private int check_if_sucess(){
			if(score==3&&bird_alive>=0){
				return 1;
			}else if(score<=3&&bird_alive==0){
			
			return 0;
			}
			return 2;
		}

		private void checkHitToEnemy(){
			for (int i = 0; i < 3; i++) {
				if(enemy_alive[i]==1){
				if(
					my_x<enemy_x[i]+my_width+25&&
					my_x>enemy_x[i]&&
					my_y<enemy_y[i]+my_height+25&&
					my_y>enemy_y[i]
				){
					enemy_alive[i] = 0;
					star[score]=1;
					score++;	
					}
				}
			}		
		}
		
		public void mouseClicked(MouseEvent me)
		{
		}
		
		public void mousePressed(MouseEvent me)
		{
			mouse_x = me.getX();
			mouse_y = me.getY();
			if((grab_flag==0)&&(my_x<mouse_x)&&(mouse_x<my_x+my_width)&&(my_y<mouse_y)&&(mouse_y<my_y+my_height)){
				grab_flag = 1;
				start_x = mouse_x;
				start_y = mouse_y;
			}
		}
		
		public void mouseReleased(MouseEvent me)
		{
			if(grab_flag==1){
				timer = new Timer(100, this);
				timer.start();
//-------------------------------------------------------------------
				v_x = (double)(start_x-mouse_x)/100;
				v_y = -(double)(start_y-mouse_y)/100;
//-------------------------------------------------------------------
				start_x = my_x;
				start_y = my_y;
			}
		}
		
		public void mouseExited(MouseEvent me)
		{
		}
		
		public void mouseEntered(MouseEvent me)
		{
		}
		
		public void mouseMoved(MouseEvent me)
		{
		}
		
		public void mouseDragged(MouseEvent me)
		{
			if(grab_flag==1 && mouse_y>170){
				mouse_x = me.getX();
				mouse_y = me.getY();
				my_x = init_x - (start_x-mouse_x);
				my_y = init_y - (start_y-mouse_y);
				repaint();
			}
		}

		//make the bird roll in the air，used the get image rotate 90 function which learned in lecture 8
		public Image genImageRotate90(Image ima){
            //implement here
            int width,height;
            int[] data;
            int[] newdata;
			Image image_90;
            width=ima.getWidth(this);
            height=ima.getHeight(this);
            data = new int[width*height];
            newdata = new int[width*height];
            try {
                PixelGrabber pg = new PixelGrabber(ima, 0, 0, width, height, data, 0, width);
                pg.grabPixels();
                }
                catch (Exception e) {
                System.out.println( "error" );
                    
                }
                for( int i=0; i<width; i++ ){
                    for( int j=0; j<height; j++ ){
                        newdata[j*width+i]=data[j+(width-1-i)*width];
                    }
                }
                image_90=createImage( new MemoryImageSource(width, height, newdata, 0, width));
                return image_90;
        }
		
	}
}
