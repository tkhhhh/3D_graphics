import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import gmaths.*;

public class Light {

  /* I declare this code is my own work */
  private Material material, onMaterial, offMaterial = new Material(new Vec3(0,0,0), new Vec3(0,0,0), new Vec3(0,0,0), 0);
  private Vec3 originalPosition;
  private Vec3 originalDirection;
  private Vec3 currentPosition;
  private Vec3 currentDirection;
  private Mat4 worldTransform;
  private boolean IS_BULB;
  private float constant;
  private float linear;
  private float quadratic;
  private float cutOff;
  private float outerCutOff;
  /* Author Xienan Fang XFang24@sheffield.ac.uk */

  private Mat4 model;
  private Shader shader;
  private Camera camera;

  public Light(GL3 gl, boolean is_bulb, Vec3 originalDirection) {

    /* I declare this code is my own work */
    IS_BULB = is_bulb;
    onMaterial = new Material();
    onMaterial.setAmbient(0.1f, 0.1f, 0.1f);
    onMaterial.setDiffuse(0.7f, 0.7f, 0.7f);
    onMaterial.setSpecular(0.5f, 0.5f, 0.5f);
    onMaterial.setShininess(4f);
    material = onMaterial;
    originalPosition = new Vec3(0f,0f,0f);
    currentPosition = new Vec3(0f,0f,0f);
    this.originalDirection = originalDirection;
    currentDirection = originalDirection;
    worldTransform = Mat4Transform.translate(new Vec3(0.3f, -0.5f, 0));
    constant = 1.0f;
    linear = 0.09f;
    quadratic = 0.032f;
    cutOff = (float) Math.cos(Math.toRadians(12.5f));
    outerCutOff = (float) Math.cos(Math.toRadians(20f));
    /* Author Xienan Fang XFang24@sheffield.ac.uk */

    model = new Mat4(1);
    shader = new Shader(gl, "shaderFile/vs_light.txt", "shaderFile/fs_light.txt");
    fillBuffers(gl);
  }
  
  public void setPosition(Vec3 v) {
    currentPosition.x = v.x;
    currentPosition.y = v.y;
    currentPosition.z = v.z;
  }
  
  public void setPosition(float x, float y, float z) {
    currentPosition.x = x;
    currentPosition.y = y;
    currentPosition.z = z;
  }

  /* I declare this code is my own work */
  public Vec3 getPosition() {
    return currentPosition;
  }

  public Vec3 getDirection() {
    return currentDirection;
  }

  public void setWorldMatrix(Mat4 worldTransform) {
    this.worldTransform = worldTransform;
  }

  public void setMaterial(Material m) {
    material = m;
  }
  
  public Material getMaterial() {
    return material;
  }
  public float getConstant() {
    return constant;
  }

  public float getLinear() {
    return linear;
  }

  public float getQuadratic() {
    return quadratic;
  }

  public float getCutOff() {
    return cutOff;
  }
  public float getOuterCutOff() {
    return outerCutOff;
  }
  /* Author Xienan Fang XFang24@sheffield.ac.uk */
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }

  //fixed light
  public void render(GL3 gl) {
    Mat4 model = new Mat4(1);
    model = Mat4.multiply(Mat4Transform.scale(0.3f,0.3f,0.3f), model);
    model = Mat4.multiply(Mat4Transform.translate(currentPosition), model);
    
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), model));
    
    shader.use(gl);
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
  
    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    gl.glBindVertexArray(0);
  }

  //changing light
  public void render(GL3 gl, Mat4 worldTransform) {

    /* I declare this code is my own work */
    setPosition();
    setDirection();
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), worldTransform));
    /* Author Xienan Fang XFang24@sheffield.ac.uk */

    shader.use(gl);
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());

    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
    gl.glBindVertexArray(0);
  }

  public void dispose(GL3 gl) {
    gl.glDeleteBuffers(1, vertexBufferId, 0);
    gl.glDeleteVertexArrays(1, vertexArrayId, 0);
    gl.glDeleteBuffers(1, elementBufferId, 0);
  }

    // ***************************************************
  /* THE DATA
   */
  // anticlockwise/counterclockwise ordering
  
    private float[] vertices = new float[] {  // x,y,z
      -0.5f, -0.5f, -0.5f,  // 0
      -0.5f, -0.5f,  0.5f,  // 1
      -0.5f,  0.5f, -0.5f,  // 2
      -0.5f,  0.5f,  0.5f,  // 3
       0.5f, -0.5f, -0.5f,  // 4
       0.5f, -0.5f,  0.5f,  // 5
       0.5f,  0.5f, -0.5f,  // 6
       0.5f,  0.5f,  0.5f   // 7
     };
    
    private int[] indices =  new int[] {
      0,1,3, // x -ve 
      3,2,0, // x -ve
      4,6,7, // x +ve
      7,5,4, // x +ve
      1,5,7, // z +ve
      7,3,1, // z +ve
      6,4,0, // z -ve
      0,2,6, // z -ve
      0,4,5, // y -ve
      5,1,0, // y -ve
      2,3,7, // y +ve
      7,6,2  // y +ve
    };
    
  private int vertexStride = 3;
  private int vertexXYZFloats = 3;
  
  // ***************************************************
  /* THE LIGHT BUFFERS
   */

  private int[] vertexBufferId = new int[1];
  private int[] vertexArrayId = new int[1];
  private int[] elementBufferId = new int[1];
    
  private void fillBuffers(GL3 gl) {
    gl.glGenVertexArrays(1, vertexArrayId, 0);
    gl.glBindVertexArray(vertexArrayId[0]);
    gl.glGenBuffers(1, vertexBufferId, 0);
    gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferId[0]);

    if(IS_BULB) setBulbSize();
    FloatBuffer fb = Buffers.newDirectFloatBuffer(vertices);
    
    gl.glBufferData(GL.GL_ARRAY_BUFFER, Float.BYTES * vertices.length, fb, GL.GL_STATIC_DRAW);
    
    int stride = vertexStride;
    int numXYZFloats = vertexXYZFloats;
    int offset = 0;
    gl.glVertexAttribPointer(0, numXYZFloats, GL.GL_FLOAT, false, stride*Float.BYTES, offset);
    gl.glEnableVertexAttribArray(0);
     
    gl.glGenBuffers(1, elementBufferId, 0);
    IntBuffer ib = Buffers.newDirectIntBuffer(indices);
    gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, elementBufferId[0]);
    gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, Integer.BYTES * indices.length, ib, GL.GL_STATIC_DRAW);
    gl.glBindVertexArray(0);
  }

  /* I declare this code is my own work */
  // chang material to achieve effect "turn on" or "turn off"
  public void turnOn() {
    material = onMaterial;
  }

  public void turnOff() {
    material = offMaterial;
  }

  //use lamp node transform to translate position and direction of bulbLight
  public void setPosition() {
    Vec4 result = Vec4.multiplyMatrix(worldTransform, new Vec4(this.originalPosition, 1));
    this.currentPosition = result.toVec3();
  }

  public void setDirection() {
    Vec4 result = Vec4.multiplyMatrix(worldTransform, new Vec4(this.originalDirection, 0));
    this.currentDirection = result.toVec3();
  }

  //initialise bulbSize
  public void setBulbSize() {
    for (int i = 0;i < vertices.length;i++) {
        vertices[i] = vertices[i]*0.3f;
    }
  }
  /* Author Xienan Fang XFang24@sheffield.ac.uk */

}