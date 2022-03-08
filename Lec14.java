import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Image;
import java.awt.image.*;
import java.time.Year;
import java.util.Calendar;

// What I add in the program
// 1.add backgroud and sling
// 2.birds rolling in the sky by using a function which can make the image Rotate 90 degress to make birds look like rolling in the sky
// 3.bird can't fly out of the Frame
// 4.you can only shoot the bird above grass 
// 5.added score board functionaliy, which uses quicksort for displaying the best performance. example: used 3 birds
// 6.add the enemy(pig)
// 7.add reset botton and exit botton
// 8.add score (if you defeat a pig, a star will appear)
// 9.if you win  "you win!!" will show on the screen. if you lose, "you lose" will show on the screen.
// 10.you have 5 birds in one game.
// 11.showing current date with name of its day of the week using Lec2's Zeller'scongruenc

//WARNING
//1.you can only reset and end it manually(press the button)
//2.when you hit the last pig this program need a few seconds to calculate the best performance




public class Lec14 extends JFrame{
	
	public Lec14(){
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
		new Lec14();
	}
	
	public class MyJPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener{
		Timer timer;
		Image image;
		Image init_image,sling_image,pig_image,score_image;
		Image win_image,lose_image,reset_image,exit_image,background_image,column_image;

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
		int player_no =0;
		JButton buttonReset;
		// List<Integer> rank = new ArrayList<Integer>();
		int rank[];
		
		Calendar c = Calendar.getInstance();
		int year=c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int date = c.get(Calendar.DATE);
		
		String dayofweek;

		public MyJPanel(){
			setBackground(Color.white);
			addMouseListener(this);
			addMouseMotionListener(this);
			
			ImageIcon icon = new ImageIcon("bird.jpg");
			ImageIcon sling = new ImageIcon("sling.png");
			ImageIcon pig = new ImageIcon("pig_failed.png");
			ImageIcon score = new ImageIcon("stars.png");
			ImageIcon lose = new ImageIcon("lose.png");
			ImageIcon win = new ImageIcon("win.png");
			ImageIcon reset = new ImageIcon("reset.png");
			ImageIcon exit = new ImageIcon("exit.png");
			ImageIcon background = new ImageIcon("background.png");
			ImageIcon column = new ImageIcon("column.png");

			score_image=score.getImage();
			pig_image=pig.getImage();
			sling_image = sling.getImage();
			init_image = icon.getImage();
			win_image=win.getImage();
			lose_image=lose.getImage();
			reset_image=reset.getImage();
			exit_image=exit.getImage();
			background_image=background.getImage();
			column_image=column.getImage();
			image=init_image;

			sling_height = sling_image.getWidth(this);
			my_width = image.getWidth(this);
			my_height = image.getHeight(this);
			

			pig_width = pig_image.getWidth(this);
			pig_height = pig_image.getHeight(this);
			enemy_alive = new int[3];
			enemy_x = new int[3];
			enemy_y = new int[3];
			star= new int[3];
			rank=new int[3];
			for (int i = 0; i < 3; i++) {
				rank[i]=5;
			}
			Day day=new Day();
			day.setyear(year);
			day.setmonth(month);
			day.setdate(date);
			dayofweek = day.dayOfWeek();
			init_game();
		}
		

		private void init_game(){
			for (int i = 0; i < 3; i++) {
				enemy_alive[i] = 1; //all alive
				enemy_x[i]=400+100*i;
				enemy_y[i]=290;
				star[i]=0;
			}	
			bird_alive = 5;
			init_x=100;
			init_y=265;
			i=0;
			count=5;
			score=0;
			t=0.0;
			v=100.0;
			grab_flag=0;
			my_x = init_x;
			my_y = init_y;
			image=init_image;

			
			
		}
		//add background
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			//background
			g.drawImage(background_image, 0, 0, getSize().width,
			  getSize().height, this);
			// login date
			g.drawString("login date:  "+ year + "/" + month +  "/" + date+ " is " + dayofweek ,550,25);
        System.out.println( );
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
					g.drawImage(column_image,enemy_x[i]+(my_width+15)/2,enemy_y[i]+40,10,50,this);
					
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
				Font f1 = new Font("Times New Roman",Font.PLAIN,18);
				g.setFont(f1);
				g.drawString("best performance: used "+rank[0]+" birds",300,50);
				
			}else if(check_if_sucess()==0){
				g.drawImage(lose_image,0,0,700,400,this);
				
			}

			g.drawImage(reset_image,10,10,50,50,this);
			g.drawImage(exit_image,70,10,50,50,this);
			
		}
		
		public void actionPerformed(ActionEvent e){
			Dimension d;
			d=getSize();

//add the range for the bird to move and flipped when it is moving
//-------------------------------------------------------------------
			my_x = (int)(v*v_x*t+start_x);
			my_y = (int)(9.8*t*t/2 - v*v_y*t+start_y);
			
			if(my_x<d.width-35){
				t+=0.2;
				image = genImageRotate90(image);
			}else{
				my_y = my_y+10*i;
				i+=1;
			}
//-------------------------------------------------------------------
			if(((my_x<=0)&&t>0.2)||(my_x>=d.width)||(my_y>=d.height-130)||(my_y<=0)){     
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
			if(score==3&&bird_alive>=0){
				
			 }
			checkHitToEnemy();
			repaint();
		}

		private int check_if_sucess(){
			if(score==3&&bird_alive>=0){
				rank[2]=5-(bird_alive-1);
				Quick quick = new Quick();
				quick.sort(rank);
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
			if(me.getX()>=10&&
			   me.getX()<=60&&
			   me.getY()>=10&&
			   me.getY()<=70
			){
				
			timer.stop();
			System.out.println(rank);
		
			init_game();
			repaint();
			}else if(me.getX()>=70&&
			   me.getX()<=120&&
			   me.getY()>=10&&
			   me.getY()<=70
			){
			System.out.println("===== Game Over =====");
			System.exit(0);
			}
		}
		
		public void mousePressed(MouseEvent me){
			mouse_x = me.getX();
			mouse_y = me.getY();
			if((grab_flag==0)&&(my_x<mouse_x)&&(mouse_x<my_x+my_width)&&(my_y<mouse_y)&&(mouse_y<my_y+my_height)){
				grab_flag = 1;
				start_x = mouse_x;
				start_y = mouse_y;
			}
		}
		public void mouseReleased(MouseEvent me){
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
		public void mouseExited(MouseEvent me){
		}
		public void mouseEntered(MouseEvent me){
		}
		public void mouseMoved(MouseEvent me){
		}
		public void mouseDragged(MouseEvent me){
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
//date
class Day{
    private int year;
    private int month;
    private int date;
    private String dayOfWeek ;
    public String dayOfWeek(){
        int h;
        if(month == 1 || month == 2){
            year =year-1;
            month = month+12;
        }
        h = (year + year / 4 - year / 100 + year / 400 + (13 * month + 8)
        / 5 + date) % 7;
        dayOfWeek = switch(h){
            case 0 -> "Sunday";
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            default -> "error";
        };
       return dayOfWeek;
    }
    public void setyear(int year){
        if(year <=1583){
            System.err.println("year should be >=1583"+year);
            System.exit(1);
        }
        this.year=year;
    }
    public void setmonth(int month){
        if(month >12 &&month<1){
            System.err.println("Please input correct month");
            System.exit(1);
        }
        this.month=month;
    }
    public void setdate(int date){
        this.date = date;
    }

}

//quickSort
class Quick{
    public void swap(int[] A, int i, int j){
        int tmp = A[i];
        A[i] = A[j];
        A[j] = tmp;
    }
    public boolean compareAndSwap(int[] A, int i, int j){
        if (A[i] > A[j]) {
            swap(A, i, j);
            return true;
        }
        return false;
    }

    public void quickSort(int[]A,int left,int right){
        int pl=left;
        int pr=right;
        int pivot = (pl+pr)/2;
        compareAndSwap(A, pl, pivot);
        compareAndSwap(A, pivot,pr);
        compareAndSwap(A, pl, pivot);
        int x = A[pivot];
        swap(A,pivot,right-1);
        pl++;
        pr-=2;
        do {
            while (A[pl] < x) pl ++;
            while (A[pr] > x) pr --;
            if (pl <= pr)
                swap (A,pl++,pr--);
                
        } while (pl <= pr);
            if (left < pr) quickSort(A, left, pr);
            if (pl < right) quickSort(A, pl, right);
    }
    
    public void sort(int[] A) {
        //implement here
        quickSort(A,0,A.length-1);
    }
    
}
