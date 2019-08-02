package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
* @Title:蛇身体的坐标信息
* @Description:
* @author:woodwang
* @date  :2019年8月2日
 */
public class SnakeBody extends JPanel implements KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//记录方向
	private volatile int direction = south;

	//包含移动之前的头部的整个蛇身信息
	ArrayList<Point> tempBody = new ArrayList<Point>();
	
	//不包含头部的真正的蛇身坐标
	ArrayList<Point> body = new ArrayList<Point>();
	
	//食物
	private Point food = new Point(300,200);
	
	//蛇的头部
	private Point head = new Point(200,150);

	//得分
	int myScore = 0;
	
	//控制游戏状态，1 为运行中；-1 为 game over
	int gameState = 0;

	//四个方向
	public static final int south = -1;
	public static final int north = 1;
	public static final int east = 2;
	public static final int west = -2;
	
	//随机数
	Random random = new Random();
	
	//初始时缓存一个头部坐标
	public void initFirstBody() {
		tempBody.add(new Point(100,100));
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		BufferedImage bgImage = null;
		try {
			bgImage = ImageIO.read(new File("src/game/snakebg.png"));
			g.drawImage(bgImage, 0, 0, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		g.setColor(Color.LIGHT_GRAY);
		
		//右侧得分显示区域（600,0） 的位置，200*600
		g.fillRect(600, 0, 200, 600);
		
		Font font = new Font("微软雅黑", Font.BOLD, 18);
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString("得分："+myScore, 602, 100);
		
		//蛇头绘制
		g.setColor(Color.BLACK);
		g.fillRect(head.x, head.y, 10, 10);
		g.setColor(Color.YELLOW);
		g.fillRect(food.x, food.y, 10, 10);
		
		//根据有些操作状态，设置面板功能信息
		g.setColor(Color.red);
		g.drawString("按 A 键开始游戏", 602, 150);
		g.drawString("按 R 键重新开始游戏", 602, 180);
		g.drawString("按 Esc 键退出游戏", 602, 210);
		
		//重新 buffer body 的 i+1 处的坐标值
		for (int i = 0; i < body.size(); i++) {
			tempBody.get(i + 1).x = body.get(i).x;
			tempBody.get(i + 1).y = body.get(i).y;
		}
		
		//重新设置body 的坐标值
		for (int i = 0; i < body.size(); i++) {
			body.get(i).x = tempBody.get(i).x;
			body.get(i).y = tempBody.get(i).y;
		}
		
		//绘制蛇身体
		g.setColor(Color.green);
		for (int i = 0; i < body.size(); i++) {
			g.fillRect(body.get(i).x, body.get(i).y, 10, 10);
		}
		
		if (gameState == -1) {
			g.setColor(Color.red);
			g.drawString("Game Over !", 200, 200);
		}
	}

	//让蛇移动的线程
	public void move() {
		new Thread() {
			public void run() {
				while (true) {
					
					//O 是方向
					if (gameState == 1) {
						//记录此次移动前的头部坐标
						tempBody.get(0).x = head.x;
						tempBody.get(0).y = head.y;
						
						switch (direction) {
						case south:
							head.y = head.y + 10;
							break;
						case north:
							head.y = head.y - 10;
							break;
						case east:
							head.x = head.x + 10;
							break;
						case west:
							head.x = head.x - 10;
							break;
						}
						
						//重绘移动后的蛇身
						repaint();
						
						//吃到食物了
						if (head.x == food.x && head.y == food.y) {
							//身体长度加一个初始值为0的坐标
							tempBody.add(new Point(0,0));
							body.add(new Point(0,0));
							
							//得分加 10
							myScore = myScore + 10;
							System.out.println("Get the food ,new score is:"+myScore);
							
							//重新生成食物坐标
							food.x = random.nextInt(60) * 10;
							food.y = random.nextInt(56) * 10;
							
							//重绘
							repaint();
						}
						
						//碰到边缘
						if (head.x < 0 || head.y < 0 || head.x == 600 || head.y == 560) {
							gameState = -1;
							repaint();
							System.out.println("Game over because touch the edge.");
							break;
						}
						
						//碰到自己了
						for (int i = 0; i < body.size(); i++) {
							if (head.x == body.get(i).x && head.y == body.get(i).y) {
								gameState = -1;
								repaint();
								System.out.println("Game over because touch the body.");
								break;
							}
						}
					}
					
					try {
						Thread.sleep(140);
					} catch (InterruptedException e) {
						// TODO: handle exception
					}
				}
			}
		}.start();
	}

	@Override
	public void keyPressed(KeyEvent a) {
		// A 开始或者继续
		if (a.getKeyCode() == 65 && gameState == 0) {
			gameState = 1;
			repaint();
			move();
		}
		
		if (gameState == 1) {
			switch (a.getKeyCode()) {
			//键盘的方向，控制着 direction 方向，只能 90 度转向，不能 180 度转向
			case KeyEvent.VK_UP:
				if (direction == east || direction == west)
					direction = north;
				break;
			case KeyEvent.VK_RIGHT:
				if (direction == south || direction == north)
					direction = east;
				break;
			case KeyEvent.VK_DOWN:
				if (direction == east || direction == west)
					direction = south;
				break;
			case KeyEvent.VK_LEFT:
				if (direction == south || direction == north)
					direction = west;
				break;
			}
		}
		
		//82 是字母 R 
		if (a.getKeyCode() == 82 && gameState == -1) {
			myScore = 0;
			gameState = 0;
			direction = south;
			food.x = 300;
			food.y = 300;
			
			head.x = 100;
			head.y = 100;
			
			tempBody.clear();
			body.clear();
			
			initFirstBody();
			
			move();
			repaint();
		}
		
		if (a.getKeyCode() == 32 && gameState == 1) {
			gameState = 0;
			repaint();
		}
		
		if (a.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}