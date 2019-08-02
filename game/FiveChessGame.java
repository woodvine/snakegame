package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FiveChessGame extends JFrame implements MouseListener, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String args[]) {
		FiveChessGame ff = new FiveChessGame();
		ff.setVisible(true);
	}

	int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	
	BufferedImage bgImage = null;// 缓存图片
	int x = 0;
	int y = 0;

	int allChess[][] = new int[19][19];// 设置棋盘大小
	boolean isBlack = true;
	boolean canPlay = true;

	String message = "黑方先行";
	int maxTime = 0;
	Thread t = new Thread(this);// 创建一个线程
	int blackTime = 0;
	int whiteTime = 0;
	String blackMessage = "无限制";
	String whiteMessage = "无限制";

	public FiveChessGame() {
		this.setTitle("五子棋");
		this.setSize(600, 600);// 设置窗体大小
		this.setResizable(true);// 窗体是否可改变大小
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 单击窗口的关闭按钮
		this.setLocation((width - 500) / 2, (height - 500) / 2);// 离显示屏上下，左右像素
		this.addMouseListener(this);// 处理鼠标事件
		this.setVisible(true); // 窗体可视

		t.start();// 开始线程
		this.repaint();

		try {
			bgImage = ImageIO.read(new File("E:/image/bgImage.jpg"));
		} catch (IOException e) {
			e.printStackTrace();// 运行时自动将io流异常初始化，并打印出程序的异常信息
		}
	}

	public void paint(Graphics g) {
		BufferedImage bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
		Graphics g2 = bi.createGraphics();

		g2.drawImage(bgImage, 0, 0, this);// 信息参数
		g2.setColor(Color.black);
		g2.setFont(new Font("黑体", Font.BOLD, 25));
		g2.drawString("游戏信息: " + message, 20, 60);
		g2.setFont(new Font("宋体", Font.BOLD, 20));
		g2.setColor(Color.white);
		g2.fillRect(15, 460, 185, 25);
		g2.fillRect(255, 460, 185, 25);
		g2.setColor(Color.black);
		g2.drawString("黑方时间:" + blackMessage, 20, 480);
		g2.drawString("白方时间:" + whiteMessage, 260, 480);

		g2.setColor(Color.yellow);
		g2.fill3DRect(390, 90, 90, 30, true);// x坐标，y坐标，长，宽，false凹 true凸
		g2.fill3DRect(390, 140, 90, 30, true);
		g2.fill3DRect(390, 190, 90, 30, true);
		g2.fill3DRect(390, 240, 90, 30, true);
		g2.fill3DRect(390, 290, 90, 30, true);
		g2.fill3DRect(390, 340, 90, 30, true);
		g2.fill3DRect(390, 395, 90, 30, true);

		g2.setColor(Color.red);
		g2.drawString("开始游戏", 394, 113);// 框体中的内容，x坐标。y坐标
		g2.drawString("游戏设置", 394, 163);
		g2.drawString("游戏说明", 394, 213);
		g2.drawString("暂停", 412, 263);
		g2.drawString("继续", 412, 313);
		g2.drawString("认输", 412, 363);
		g2.drawString("退出", 412, 418);

		g2.setColor(Color.black);
		
		//绘制棋盘，19 条横线和 19 条纵线
		for (int i = 0; i < 19; i++) {
			g2.drawLine(10, 70 + 20 * i, 370, 70 + 20 * i);
			g2.drawLine(10 + 20 * i, 70, 10 + 20 * i, 430);
		}

		//绘制棋盘上 8 个黑点
		g2.fillOval(66, 126, 8, 8);
		g2.fillOval(306, 126, 8, 8);
		g2.fillOval(306, 366, 8, 8);
		g2.fillOval(66, 366, 8, 8);
		g2.fillOval(306, 246, 8, 8);
		g2.fillOval(186, 126, 8, 8);
		g2.fillOval(66, 246, 8, 8);
		g2.fillOval(186, 366, 8, 8);
		g2.fillOval(186, 246, 8, 8);// 设置棋盘上的九个星

		for (int i = 0; i < 19; i++) {
			for (int j = 0; j < 19; j++) {
				if (allChess[i][j] == 1) {
					int tempX = i * 20 + 10;
					int tempY = j * 20 + 70;
					g2.fillOval(tempX - 8, tempY - 8, 16, 16);
				}
				if (allChess[i][j] == 2) {
					int tempX = i * 20 + 10;
					int tempY = j * 20 + 70;
					g2.setColor(Color.white);
					g2.fillOval(tempX - 8, tempY - 8, 16, 16);
					g2.setColor(Color.black);
					g2.drawOval(tempX - 8, tempY - 8, 16, 16);
				}
			}
		}
		g.drawImage(bi, 0, 0, this);
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * 鼠标按下时记录点击的位置，减掉边距后除以 20 的四舍五入的整数 ，就对应二维数组的位置
	 */
	public void mousePressed(MouseEvent e) {
	    System.out.println("X坐标："+e.getX()+" "+"Y坐标"+e.getY());
	    int clickPointX = e.getX();
	    int clickPointY = e.getY();
	    
	    x = clickPointX;
	    y = clickPointY;
		if (canPlay == true) {
			if (clickPointX >= 10 && clickPointX <= 370 && clickPointY >= 70 && clickPointY <= 430) {
				//根据当前点击的位置与 20 的倍数四舍五入，找打对应数组的值
				long multipX = Math.round((clickPointX - 10) / 20.0);
				long multipY = Math.round((clickPointY - 70) / 20.0);
				x = Integer.parseInt(String.valueOf(multipX));
				y = Integer.parseInt(String.valueOf(multipY));
				
				if (allChess[x][y] == 0) {
					if (isBlack == true) {
						allChess[x][y] = 1;
						isBlack = false;
						message = "轮到白方";
					} else {
						allChess[x][y] = 2;
						isBlack = true;
						message = "轮到黑方";
					}
					boolean winFlag = this.checkWin();
					if (winFlag == true) {
						JOptionPane.showMessageDialog(this,
								"游戏结束！" + " " + (allChess[x][y] == 1 ? "黑方" : "白方") + "获胜！");
						canPlay = false;
					}
				} else {
					JOptionPane.showMessageDialog(this, "当前位置已经有棋子，请重新落子！");
				}
				this.repaint();
			}
		}
		if (clickPointX >= 390 && e.getY() >= 70 && clickPointX <= 480 && e.getY() <= 100) {
			int result = JOptionPane.showConfirmDialog(this, "是否重新开始游戏？");
			if (result == 0) {
				for (int i = 0; i < 19; i++) {
					for (int j = 0; j < 19; j++) {
						allChess[i][j] = 0;
						canPlay = true;
					}
				}
				// allChess = new int[19][19];
				message = "黑方先行";
				isBlack = true;
				blackTime = maxTime;
				whiteTime = maxTime;
				if (maxTime > 0) {
					blackMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":"
							+ (maxTime - maxTime / 60 * 60);
					whiteMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":"
							+ (maxTime - maxTime / 60 * 60);
					t.resume();
				} else {
					blackMessage = "无限制";
					whiteMessage = "无限制";
				}
				this.repaint();
			}
		}
		if (clickPointX >= 390 && clickPointX <= 480 && clickPointY >= 120 && clickPointY <= 150) {
			String input = JOptionPane.showInputDialog("请输入游戏的最大时间(分钟),如果输入0，表示没有时间限制：");
			try {
				maxTime = Integer.parseInt(input) * 60;
				if (maxTime < 0) {
					JOptionPane.showMessageDialog(this, "请输入正确信息，不允许输入负数！");
				}
				if (maxTime == 0) {
					int result = JOptionPane.showConfirmDialog(this, "设置完成,是否重新开始游戏？");
					if (result == 0) {
						for (int i = 0; i < 19; i++) {
							for (int j = 0; j < 19; j++) {
								allChess[i][j] = 0;
							}
						}
						// allChess = new int[19][19];
						message = "黑方先行";
						isBlack = true;
						blackTime = maxTime;
						whiteTime = maxTime;
						blackMessage = "无限制";
						whiteMessage = "无限制";
						this.repaint();
					}
				}
				if (maxTime > 0) {
					int result = JOptionPane.showConfirmDialog(this, "设置完成,是否重新开始游戏？");
					if (result == 0) {
						for (int i = 0; i < 19; i++) {
							for (int j = 0; j < 19; j++) {
								allChess[i][j] = 0;
							}
						}
						// allChess = new int[19][19];
						message = "黑方先行";
						isBlack = true;
						blackTime = maxTime;
						whiteTime = maxTime;
						blackMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":"
								+ (maxTime - maxTime / 60 * 60);
						whiteMessage = maxTime / 3600 + ":" + (maxTime / 60 - maxTime / 3600 * 60) + ":"
								+ (maxTime - maxTime / 60 * 60);
						t.resume();
						this.repaint();
					}
				}
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this, "请输入正确信息！");
			}
		}
		if (clickPointX >= 390 && clickPointX <= 480 && clickPointY >= 170 && clickPointY <= 200) {
			JOptionPane.showMessageDialog(this, "这是一个五子棋游戏程序，黑白双方轮流下棋，当某一方连到五子时游戏结束！");
		}
		if (clickPointX >= 390 && clickPointX <= 480 && clickPointY >= 220 && clickPointY <= 250) {
			JOptionPane.showMessageDialog(this, "游戏已暂停");
			canPlay = false;
		}
		if (clickPointX >= 390 && clickPointX <= 480 && clickPointY >= 270 && clickPointY <= 300) {
			JOptionPane.showMessageDialog(this, "游戏继续进行");
			canPlay = true;
		}
		if (clickPointX >= 390 && clickPointX <= 480 && clickPointY >= 320 && clickPointY <= 350) {
			int result = JOptionPane.showConfirmDialog(this, "是否确认认输？");
			System.out.println(result);
			if (result == 0) {
				if (isBlack == true) {
					JOptionPane.showMessageDialog(this, "黑方已经认输，游戏结束！");
				} else {
					JOptionPane.showMessageDialog(this, "白方已经认输，游戏结束！");
				}
				canPlay = false;
			}
		}

		if (clickPointX >= 390 && clickPointX <= 480 && clickPointY >= 420 && clickPointY <= 450) {
			JOptionPane.showMessageDialog(this, "游戏结束");
			System.exit(0);
		}
	}

	private boolean checkWin() {
		boolean flag = false;
		int count = 1;
		int color = allChess[x][y];
		/*
		 * int i = 1; while (color == allChess[x+i][y]){ count ++; i++; } i = 1; while
		 * (color == allChess[x-i][y]){ count ++; i++; } if(count >= 5){ flag = true; }
		 * 
		 * int i2 = 1; int count2 = 1; while (color == allChess[x][y+i2]){ count2 ++;
		 * i2++; } i2 = 1; while (color == allChess[x][y-i2]){ count2 ++; i2++; }
		 * if(count2 >= 5){ flag = true; }
		 * 
		 * int i3 =1; int count3 = 1; while (color == allChess[x+i3][y-i3]){ count3 ++;
		 * i3++; } i3 = 1; while (color == allChess[x-i3][y+i3]){ count3 ++; i3++; }
		 * if(count3 >= 5){ flag = true; }
		 * 
		 * int i4 =1; int count4 = 1; while (color == allChess[x+i4][y+i4]){ count4 ++;
		 * i4++; } i4 = 1; while (color == allChess[x-i4][y-i4]){ count4 ++; i4++; }
		 * if(count4 >= 5){ flag = true; }
		 */
		count = this.checkCount(1, 0, color);
		if (count >= 5) {
			flag = true;
		} else {
			count = this.checkCount(0, 1, color);
			if (count >= 5) {
				flag = true;
			} else {
				count = this.checkCount(1, -1, color);
				if (count >= 5) {
					flag = true;
				} else {
					count = this.checkCount(1, 1, color);
					if (count >= 5) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	private int checkCount(int xChange, int yChange, int color) {
		int count = 1;
		int tempX = xChange;
		int tempY = yChange;
		while (x + xChange >= 0 && x + xChange <= 18 && y + yChange >= 0 && y + yChange <= 18
				&& color == allChess[x + xChange][y + yChange]) {
			count++;
			if (xChange != 0)
				xChange++;
			if (yChange != 0) {
				if (yChange > 0)
					yChange++;
				else {
					yChange--;
				}
			}
		}
		xChange = tempX;
		yChange = tempY;
		while (x - xChange >= 0 && x - xChange <= 18 && y - yChange >= 0 && y - yChange <= 18
				&& color == allChess[x - xChange][y - yChange]) {
			count++;
			if (xChange != 0)
				xChange++;
			if (yChange != 0) {
				if (yChange > 0)
					yChange++;
				else {
					yChange--;
				}
			}
		}
		return count;
	}

	public void run() {
		if (maxTime > 0) {
			while (true) {
				if (isBlack) {
					blackTime--;
					if (blackTime == 0) {
						JOptionPane.showMessageDialog(this, "黑方超时，游戏结束！");
					}
				} else {
					whiteTime--;
					if (whiteTime == 0) {
						JOptionPane.showMessageDialog(this, "白方超时，游戏结束！");
					}
				}
				blackMessage = blackTime / 3600 + ":" + (blackTime / 60 - blackTime / 3600 * 60) + ":"
						+ (blackTime - blackTime / 60 * 60);
				whiteMessage = whiteTime / 3600 + ":" + (whiteTime / 60 - whiteTime / 3600 * 60) + ":"
						+ (whiteTime - whiteTime / 60 * 60);
				this.repaint();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
