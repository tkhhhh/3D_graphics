import gmaths.*;

import com.jogamp.opengl.*;

public class Hatch_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private Camera camera;
    
  /* The constructor is not used to initialise anything */
  public Hatch_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,6f,15f));
    this.camera.setTarget(new Vec3(0f,5f,0f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);

    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();

    /* I declare this code is my own work */
    egg.dispose(gl);
    eggBase.dispose(gl);
    planeOfTable.dispose(gl);
    leg1.dispose(gl);
    leg2.dispose(gl);
    leg3.dispose(gl);
    leg4.dispose(gl);
    sky.dispose(gl);
    floor.dispose(gl);
    window.dispose(gl);
    wall1.dispose(gl);
    wall2.dispose(gl);
    light1.dispose(gl);
    light2.dispose(gl);
    t1.dispose(gl);
    t2.dispose(gl);
    leftLamp.dispose(gl);
    rightLamp.dispose(gl);
    /* Author Xienan Fang XFang24@sheffield.ac.uk */
  }

  /* I declare this code is my own work */
  private Model floor, window, wall1, wall2, leg1, leg2, leg3, leg4, planeOfTable, sky, egg, eggBase;
  private Light light1, light2, t1, t2;
  private Lamp leftLamp, rightLamp;
  private Material lightMaterial, nullMaterial;
  public boolean LIGHT1_ON = true;
  public boolean LIGHT2_ON = true;
  public boolean LEFT_LAMP_ON = true;
  public boolean RIGHT_LAMP_ON = true;
  private float eggY = 0, angleEgg = 0;
  private boolean EGG_UP = true;
  private RotateStyle leftStyle, rightStyle;
  /* Author Xienan Fang XFang24@sheffield.ac.uk */

  public void initialise(GL3 gl) {
    createRandomNumbers();
    /* I declare this code is my own work */
    leftStyle = new RotateStyle();
    rightStyle = new RotateStyleExtra();

    int[] textureWood = TextureLibrary.loadTexture(gl, "texture/container2.jpg");
    int[] textureWoodShadow = TextureLibrary.loadTexture(gl, "texture/container2_specular.jpg");
    int[] textureChequerboard = TextureLibrary.loadTexture(gl, "texture/chequerboard.jpg");
    int[] textureSky = TextureLibrary.loadTexture(gl, "texture/cloud.jpg");
    int[] textureEgg = TextureLibrary.loadTexture(gl, "texture/jade.jpg");
    int[] textureEggShadow = TextureLibrary.loadTexture(gl, "texture/jade_specular.jpg");
    int[] textureTiger = TextureLibrary.loadTexture(gl, "texture/tiger.jpg");
    int[] textureDog = TextureLibrary.loadTexture(gl, "texture/dog.jpg");
    int[] textureWall1 = TextureLibrary.loadTexture(gl, "texture/wall1.jpg");
    int[] textureWall2 = TextureLibrary.loadTexture(gl, "texture/wall2.jpg");
    int[] textureDogEye = TextureLibrary.loadTexture(gl, "texture/dog_eye.jpg");
    int[] textureTigerEye = TextureLibrary.loadTexture(gl, "texture/tiger_eye.jpg");

    lightMaterial = new Material(new Vec3(0.1f, 0.1f, 0.1f), new Vec3(0.7f, 0.7f, 0.7f), new Vec3(0.5f, 0.5f, 0.5f), 4f);
    nullMaterial = new Material(new Vec3(0,0,0), new Vec3(0,0,0), new Vec3(0,0,0), 0);
    Material materialFloor = new Material();
    Material materialWindow = new Material();
    Material materialGoods = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);

    light1 = new Light(gl, false, new Vec3(0,0,0));
    light1.setPosition(new Vec3(-5f, 5f, 4f));
    light1.setCamera(camera);

    light2 = new Light(gl, false, new Vec3(0,0,0));
    light2.setPosition(new Vec3(5f, 8f, -4f));
    light2.setCamera(camera);

    t1 = new Light(gl, true, new Vec3(1,0,0));
    t1.setPosition();
    t1.setDirection();
    t1.setCamera(camera);

    t2 = new Light(gl, true, new Vec3(-1,0,0));
    t2.setPosition();
    t2.setDirection();
    t2.setCamera(camera);

    Mesh meshForTwoTriangle = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Mesh meshForCube = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Mesh meshForSphere = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());

    Shader shaderTwoTriangleWithTexture = new Shader(gl, "shaderFile/vs_wall_floor.txt", "shaderFile/fs_wall_floor.txt");
    Shader shaderCubeForTable = new Shader(gl, "shaderFile/vs_cube.txt", "shaderFile/fs_cube.txt");
    Shader shaderSphereForEgg = new Shader(gl, "shaderFile/vs_sphere.txt", "shaderFile/fs_sphere.txt");
    Shader shaderSky = new Shader(gl, "shaderFile/vs_sky.txt", "shaderFile/fs_sky.txt");

    eggBase = new Model(gl, camera, light1, light2, t1, t2, shaderCubeForTable, materialGoods, new Mat4(1), meshForCube, textureWood, textureWoodShadow);
    egg = new Model(gl, camera, light1, light2, t1, t2, shaderSphereForEgg, materialGoods, new Mat4(1), meshForSphere, textureEgg, textureEggShadow);

    shaderTwoTriangleWithTexture.use(gl);
    shaderTwoTriangleWithTexture.setFloat(gl,"transparency", 1.0f);
    floor = new Model(gl, camera, light1, light2, t1, t2, shaderTwoTriangleWithTexture, materialFloor, new Mat4(1), meshForTwoTriangle, textureChequerboard);

    shaderTwoTriangleWithTexture.use(gl);
    shaderTwoTriangleWithTexture.setFloat(gl,"transparency", 0.0f);
    window = new Model(gl, camera, light1, light2, t1, t2, shaderTwoTriangleWithTexture, materialWindow, new Mat4(1), meshForTwoTriangle, textureWood);

    shaderTwoTriangleWithTexture.use(gl);
    shaderTwoTriangleWithTexture.setFloat(gl,"transparency", 1.0f);
    wall1 = new Model(gl, camera, light1, light2, t1, t2, shaderTwoTriangleWithTexture, materialFloor, new Mat4(1), meshForTwoTriangle, textureWall1);

    shaderTwoTriangleWithTexture.use(gl);
    shaderTwoTriangleWithTexture.setFloat(gl,"transparency", 1.0f);
    wall2 = new Model(gl, camera, light1, light2, t1, t2, shaderTwoTriangleWithTexture, materialFloor, new Mat4(1), meshForTwoTriangle, textureWall2);

    shaderSky.use(gl);
    shaderSky.setFloat(gl,"transparency", 1.0f);
    sky = new Model(gl, camera, light1, light2, t1, t2, shaderSky, materialFloor, new Mat4(1), meshForTwoTriangle, textureSky);

    planeOfTable = new Model(gl, camera, light1, light2, t1, t2, shaderCubeForTable, materialGoods, new Mat4(1), meshForCube, textureWood, textureWoodShadow);
    leg1 = new Model(gl, camera, light1, light2, t1, t2, shaderCubeForTable, materialGoods, new Mat4(1), meshForCube, textureWood, textureWoodShadow);
    leg2 = new Model(gl, camera, light1, light2, t1, t2, shaderCubeForTable, materialGoods, new Mat4(1), meshForCube, textureWood, textureWoodShadow);
    leg3 = new Model(gl, camera, light1, light2, t1, t2, shaderCubeForTable, materialGoods, new Mat4(1), meshForCube, textureWood, textureWoodShadow);
    leg4 = new Model(gl, camera, light1, light2, t1, t2, shaderCubeForTable, materialGoods, new Mat4(1), meshForCube, textureWood, textureWoodShadow);

    RotateStyle rotateStyle = new RotateStyle();
    leftLamp = new Lamp(gl, camera, light1, light2, t1, t2, textureDog, textureDog, textureDog, textureDog, textureDogEye, "leftLamp");
    leftLamp.generateFixComponent();
    leftLamp.generateDiffComponentBetweenLeftRight(-4f, 0.5f, 0f, 1, 1f);
    leftLamp.generateRotateComponent(rotateStyle);
    leftLamp.generateDecoration(1, true);
    leftLamp.buildLamp();
    leftLamp.update();

    rightLamp = new Lamp(gl, camera, light1, light2, t1, t2, textureTiger, textureTiger, textureTiger, textureTiger, textureTigerEye, "rightLamp");
    rightLamp.generateFixComponent();
    rightLamp.generateDiffComponentBetweenLeftRight(2.5f,0.5f,0f,1.5f, -1f);
    rightLamp.generateRotateComponent(rotateStyle);
    rightLamp.generateDecoration(1.5f, false);
    rightLamp.buildLamp();
    rightLamp.update();
    /* Author Xienan Fang XFang24@sheffield.ac.uk */
  }

  public void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    double elapsedTime = getSeconds() - startTime;
    double t = elapsedTime*0.1;

    /* I declare this code is my own work */
    if(LIGHT1_ON){
      light1.turnOn();
      light1.render(gl);
    } else light1.turnOff();
    if(LIGHT2_ON) {
      light2.turnOn();
      light2.render(gl);
    } else light2.turnOff();

    eggBase.setModelMatrix(getMforEggBase());
    eggBase.render(gl, t);
    egg.setModelMatrix(getMforEgg());
    egg.render(gl, t);

    planeOfTable.setModelMatrix(getMforPalneOfTable());
    planeOfTable.render(gl, t);
    leg1.setModelMatrix(getMforLeg(-3.0f,3.0f));
    leg1.render(gl, t);
    leg2.setModelMatrix(getMforLeg(-3.0f,-3.0f));
    leg2.render(gl, t);
    leg3.setModelMatrix(getMforLeg(3.0f,-3.0f));
    leg3.render(gl, t);
    leg4.setModelMatrix(getMforLeg(3.0f,3.0f));
    leg4.render(gl, t);

    sky.setModelMatrix(getMforSky());
    sky.render(gl, t);

    floor.setModelMatrix(getMforFloor());
    floor.render(gl, t);
    window.setModelMatrix(getMforWindow());
    window.render(gl, t);
    wall1.setModelMatrix(getMforWall1());
    wall1.render(gl, t);
    wall2.setModelMatrix(getMforWall2());
    wall2.render(gl, t);

    updateLamp();

    leftLamp.draw(gl);
    if(LEFT_LAMP_ON) {
      t1.turnOn();
      t1.setWorldMatrix(leftLamp.bulbName.worldTransform);
      t1.render(gl, leftLamp.bulbName.worldTransform);
    } else t1.turnOff();

    rightLamp.draw(gl);
    if(RIGHT_LAMP_ON) {
      t2.turnOn();
      t2.setWorldMatrix(rightLamp.bulbName.worldTransform);
      t2.render(gl, rightLamp.bulbName.worldTransform);
    } else t2.turnOff();
    /* Author Xienan Fang XFang24@sheffield.ac.uk */
  }

  /* I declare this code is my own work */
  private Mat4 getMforEgg() {
    angleEgg += 1f;
    Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(1,2,1), Mat4Transform.rotateAroundY(angleEgg));
    if(!EGG_UP) eggY -= 0.01f;
    else eggY += 0.01f;
    modelMatrix = Mat4.multiply(modelMatrix, Mat4Transform.translate(0,1.8f + eggY,0));
    if(eggY >= 2f) EGG_UP = false;
    if(eggY <= 0) EGG_UP = true;
    return modelMatrix;
  }

  private Mat4 getMforEggBase() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0f,2.1f,0f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(1.5f,1.0f,1.5f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforPalneOfTable() {
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,7.5f,0), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(2.4f,0.2f,2.4f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforLeg(float x, float z) {
    Mat4 modelMatrix = Mat4Transform.scale(0.2f,1.4f,0.2f);
    modelMatrix = Mat4.multiply(modelMatrix, Mat4Transform.translate(x,0.5f,z));
    return modelMatrix;
  }

  private Mat4 getMforSky() {
    float size = 64f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,0,-size*0.5f), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforWindow() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,0.5f), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size - 0.25f,-size*0.5f), modelMatrix);
    return modelMatrix;
  }
  /* Author Xienan Fang XFang24@sheffield.ac.uk */

  private Mat4 getMforFloor() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforWall1() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }

  private Mat4 getMforWall2() {
    float size = 16f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size*0.5f,size*0.5f,0), modelMatrix);
    return modelMatrix;
  }

  /* I declare this code is my own work */
  public void setLeftStyle(RotateStyle leftStyle) {
    this.leftStyle = leftStyle;
  }

  public void setRightStyle(RotateStyle rightStyle) {
    this.rightStyle = rightStyle;
  }

  private void updateLamp() {
    leftLamp.updateRotate(leftStyle);
    rightLamp.updateRotate(rightStyle);
  }
  /* Author Xienan Fang XFang24@sheffield.ac.uk */
  
    // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
}
