package game;

import javax.swing.*;

public class SnakeGame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		//定义一个窗体
		JFrame gameFrame = new JFrame();
		gameFrame.setSize(800, 600);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setDefaultCloseOperation(3);
		gameFrame.setTitle("我的贪吃蛇游戏");
		
		//蛇初始化后 ，初值 bufferBody 为 100，100 的坐标值
		SnakeBody snake = new SnakeBody();
		snake.initFirstBody();
		
		//将蛇添加到当前 Frame
		gameFrame.add(snake);
		gameFrame.addKeyListener(snake);
		gameFrame.setVisible(true);
	}
}
