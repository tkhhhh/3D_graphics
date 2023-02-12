import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Hatch extends JFrame implements ActionListener {
  
  private static final int WIDTH = 1500;
  private static final int HEIGHT = 1024;
  private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
  private GLCanvas canvas;
  private Hatch_GLEventListener glEventListener;
  private final FPSAnimator animator;
  private Camera camera;

  public static void main(String[] args) {
    Hatch b1 = new Hatch("Hatch");
    b1.getContentPane().setPreferredSize(dimension);
    b1.pack();
    b1.setVisible(true);
    b1.canvas.requestFocusInWindow();
  }

  public Hatch(String textForTitleBar) {
    super(textForTitleBar);
    GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
    canvas = new GLCanvas(glcapabilities);
    camera = new Camera(Camera.DEFAULT_POSITION, Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
    glEventListener = new Hatch_GLEventListener(camera);
    canvas.addGLEventListener(glEventListener);
    canvas.addMouseMotionListener(new MyMouseInput(camera));
    canvas.addKeyListener(new MyKeyboardInput(camera));
    getContentPane().add(canvas, BorderLayout.CENTER);

    JMenuBar menuBar=new JMenuBar();
    this.setJMenuBar(menuBar);
    JMenu fileMenu = new JMenu("File");
    JMenuItem quitItem = new JMenuItem("Quit");
    quitItem.addActionListener(this);
    fileMenu.add(quitItem);
    menuBar.add(fileMenu);

    /* I declare this code is my own work */
    JToolBar p = new JToolBar();
    JButton b = new JButton("power of light1");
    b.addActionListener(this);
    p.add(b);
    p.addSeparator(new Dimension(15,3));
    b = new JButton("power of light2");
    b.addActionListener(this);
    p.add(b);
    p.addSeparator(new Dimension(15,3));
    b = new JButton("power of leftLamp");
    b.addActionListener(this);
    p.add(b);
    p.addSeparator(new Dimension(15,3));
    b = new JButton("power of rightLamp");
    b.addActionListener(this);
    p.add(b);
    p.addSeparator(new Dimension(15,3));
    b = new JButton("leftLamp style A");
    b.addActionListener(this);
    p.add(b);
    p.addSeparator(new Dimension(15,3));
    b = new JButton("leftLamp style B");
    b.addActionListener(this);
    p.add(b);
    p.addSeparator(new Dimension(15,3));
    b = new JButton("leftLamp style C");
    b.addActionListener(this);
    p.add(b);
    p.addSeparator(new Dimension(15,3));
    b = new JButton("rightLamp style A");
    b.addActionListener(this);
    p.add(b);
    p.addSeparator(new Dimension(15,3));
    b = new JButton("rightLamp style B");
    b.addActionListener(this);
    p.add(b);
    p.addSeparator(new Dimension(15,3));
    b = new JButton("rightLamp style C");
    b.addActionListener(this);
    p.add(b);

    this.add(p, BorderLayout.SOUTH);
    /* Author Xienan Fang XFang24@sheffield.ac.uk */

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        animator.stop();
        remove(canvas);
        dispose();
        System.exit(0);
      }
    });
    animator = new FPSAnimator(canvas, 60);
    animator.start();
  }

  public void actionPerformed(ActionEvent e) {

    /* I declare this code is my own work */
    if (e.getActionCommand().equalsIgnoreCase("power of light1")) {
      glEventListener.LIGHT1_ON = !glEventListener.LIGHT1_ON;
    }
    else if (e.getActionCommand().equalsIgnoreCase("power of light2")) {
      glEventListener.LIGHT2_ON = !glEventListener.LIGHT2_ON;
    }
    else if (e.getActionCommand().equalsIgnoreCase("power of leftLamp")) {
      glEventListener.LEFT_LAMP_ON = !glEventListener.LEFT_LAMP_ON;
    }
    else if (e.getActionCommand().equalsIgnoreCase("power of rightLamp")) {
      glEventListener.RIGHT_LAMP_ON = !glEventListener.RIGHT_LAMP_ON;
    }
    else if (e.getActionCommand().equalsIgnoreCase("leftLamp style A")) {
      glEventListener.setLeftStyle(new RotateStyle());
    }
    else if (e.getActionCommand().equalsIgnoreCase("leftLamp style B")) {
      glEventListener.setLeftStyle(new RotateStyleB());
    }
    else if (e.getActionCommand().equalsIgnoreCase("leftLamp style C")) {
      glEventListener.setLeftStyle(new RotateStyleC());
    }
    else if (e.getActionCommand().equalsIgnoreCase("rightLamp style A")) {
      glEventListener.setRightStyle(new RotateStyleExtra());
    }
    else if (e.getActionCommand().equalsIgnoreCase("rightLamp style B")) {
      glEventListener.setRightStyle(new RotateStyleBExtra());
    }
    else if (e.getActionCommand().equalsIgnoreCase("rightLamp style C")) {
      glEventListener.setRightStyle(new RotateStyleCExtra());
    }
    else if (e.getActionCommand().equalsIgnoreCase("power in leftLamp")) {
      glEventListener.LEFT_LAMP_ON = !glEventListener.LEFT_LAMP_ON;
    }
    else if (e.getActionCommand().equalsIgnoreCase("power in rightLamp")) {
      glEventListener.RIGHT_LAMP_ON = !glEventListener.RIGHT_LAMP_ON;
    }
    /* Author Xienan Fang XFang24@sheffield.ac.uk */

    else if(e.getActionCommand().equalsIgnoreCase("quit"))
      System.exit(0);
  }
  
}

class MyKeyboardInput extends KeyAdapter  {
  private Camera camera;
  
  public MyKeyboardInput(Camera camera) {
    this.camera = camera;
  }
  
  public void keyPressed(KeyEvent e) {
    Camera.Movement m = Camera.Movement.NO_MOVEMENT;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
      case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
      case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
      case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
      case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
      case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
    }
    camera.keyboardInput(m);
  }
}

class MyMouseInput extends MouseMotionAdapter {
  private Point lastpoint;
  private Camera camera;
  
  public MyMouseInput(Camera camera) {
    this.camera = camera;
  }
  
    /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */    
  public void mouseDragged(MouseEvent e) {
    Point ms = e.getPoint();
    float sensitivity = 0.001f;
    float dx=(float) (ms.x-lastpoint.x)*sensitivity;
    float dy=(float) (ms.y-lastpoint.y)*sensitivity;
    //System.out.println("dy,dy: "+dx+","+dy);
    if (e.getModifiers()==MouseEvent.BUTTON1_MASK)
      camera.updateYawPitch(dx, -dy);
    lastpoint = ms;
  }

  /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */  
  public void mouseMoved(MouseEvent e) {   
    lastpoint = e.getPoint(); 
  }
}