import com.jogamp.opengl.GL3;
import gmaths.Mat4;
import gmaths.Mat4Transform;
import gmaths.Vec3;

/* I declare this code is my own work */
/* Author Xienan Fang XFang24@sheffield.ac.uk */

/* class lamp for constructing and updating lamp, at first I constructed lamp in Hatch_GLEventListener.java
and the initialise function looked so complicated, later I extracted lamp as a single class
 */
public class Lamp extends SGNode{
    private Model base, connectionBetweenBaseAndLower, lower, connectionBetweenLowerAndUpper, upper,
            connectionBetweenUpperAndHead, head, bulb, tail, leftEar, rightEar, leftEye, rightEye;
    private NameNode baseName, connectionBetweenBaseAndLowerName, lowerName, connectionBetweenLowerAndUpperName,
            upperName, connectionBetweenUpperAndHeadName, headName, tailName, leftEarName, rightEarName, leftEyeName, rightEyeName;

    public NameNode bulbName;
    private TransformNode baseScale, baseTranslate, baseToConnection,
            connectionBetweenBaseAndLowerScale, connectionBetweenBaseAndLowerTranslate, connectionToLower,
            lowerScale, lowerTranslate, lowerToConnection,
            connectionBetweenLowerAndUpperScale, connectionBetweenLowerAndUpperTranslate, connectionToUpper,
            upperScale, upperTranslate, upperToConnection,
            connectionBetweenUpperAndHeadScale, connectionBetweenUpperAndHeadTranslate, connectionToHead,
            headScale, headTranslate, headToBulb,
            bulbScale, bulbTranslate,
            rotateLower, rotateUpper, rotateHead,
            connectionToTail, tailScale, tailTranslate,
            headToLeftEar, headToRightEar, rotateLeftEar, rotateRightEar, leftEarScale, rightEarScale, leftEarTranslate, rightEarTranslate,
            headToLeftEye, headToRightEye, leftEyeScale, rightEyeScale, leftEyeTranslate, rightEyeTranslate;

    private ModelNode baseNode, connectionBetweenBaseAndLowerNode,
            lowerNode, upperNode, connectionBetweenLowerAndUpperNode, connectionBetweenUpperAndHeadNode,
            headNode, bulbNode, tailNode, leftEarNode, rightEarNode, leftEyeNode, rightEyeNode;

    private GL3 gl;
    private Camera camera;
    private Light light1, light2, light3, light4;

    private int[] textureBase, textureConnection, textureArm, textureHead, textureEye;
    private Mesh cubeMesh, sphereMesh;
    private Material material;
    private Shader shader;
    public RotateStyle currentRotateStyle;

    public Lamp(
            GL3 gl, Camera camera, Light light1, Light light2, Light light3, Light light4, int[] textureBase,
            int[] textureConnection, int[] textureArm, int[] textureHead, int[] textureEye,
    String name) {
        super(name);
        this.gl = gl;
        this.camera = camera;
        this.light1 = light1;
        this.light2 = light2;
        this.light3 = light3;
        this.light4 = light4;
        this.textureBase = textureBase;
        this.textureConnection = textureConnection;
        this.textureArm = textureArm;
        this.textureHead = textureHead;
        this.textureEye = textureEye;
        cubeMesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
        sphereMesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        material = new Material(
                new Vec3(0.6f, 0.5f, 0.4f),
                new Vec3(0.6f, 0.5f, 0.4f),
                new Vec3(0.1f, 0.1f, 0.1f), 4.0f
        );
        shader = new Shader(gl, "shaderFile/vs_lamp.txt", "shaderFile/fs_lamp.txt");
    }

    public void generateFixComponent(
    ) {
        connectionBetweenBaseAndLower = new Model(
                gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), sphereMesh, textureConnection
        );
        connectionBetweenBaseAndLowerName = new NameNode("connectionBetweenBaseAndLower");
        connectionBetweenBaseAndLowerNode = new ModelNode("Sphere(0)", connectionBetweenBaseAndLower, 0);

        lower = new Model(
                gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), sphereMesh, textureArm
        );
        lowerName = new NameNode("lower");
        lowerNode = new ModelNode("Sphere(1)", lower, 0);

        connectionBetweenLowerAndUpper = new Model(
                gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), sphereMesh, textureConnection
        );
        connectionBetweenLowerAndUpperName = new NameNode("connectionBetweenLowerAndUpper");
        connectionBetweenLowerAndUpperNode = new ModelNode("Sphere(2)", connectionBetweenLowerAndUpper, 0);

        upper = new Model(
                gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), sphereMesh, textureArm
        );
        upperName = new NameNode("upper");
        upperNode = new ModelNode("Sphere(3)", upper, 0);

        connectionBetweenUpperAndHead = new Model(
                gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), sphereMesh, textureConnection
        );
        connectionBetweenUpperAndHeadName = new NameNode("connectionBetweenUpperAndHead");
        connectionBetweenUpperAndHeadNode = new ModelNode("Sphere(4)", connectionBetweenUpperAndHead, 0);

        head = new Model(
                gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), cubeMesh, textureHead
        );
        headName = new NameNode("head");
        headNode = new ModelNode("Cube(1)", head, 0);

        tail = new Model(gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), sphereMesh, textureBase);
        tailName = new NameNode("tail");
        tailNode = new ModelNode("Sphere(5)", tail, 0);

        bulb = new Model(gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), cubeMesh, textureBase);
        bulbName = new NameNode("bulb");
        bulbNode = new ModelNode("Cube(2)", bulb, 0);

        leftEar = new Model(gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), sphereMesh, textureBase);
        leftEarName = new NameNode("leftEar");
        leftEarNode = new ModelNode("Sphere(6)", leftEar, 0);
        rightEar = new Model(gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), sphereMesh, textureBase);
        rightEarName = new NameNode("rightEar");
        rightEarNode = new ModelNode("Sphere(7)", rightEar, 0);

        leftEye = new Model(gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), sphereMesh, textureEye);
        leftEyeName = new NameNode("leftEyeName");
        leftEyeNode = new ModelNode("Sphere(8)", leftEye, 0);
        rightEye = new Model(gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), sphereMesh, textureEye);
        rightEyeName = new NameNode("rightEyeName");
        rightEyeNode = new ModelNode("Sphere(9)", rightEye, 0);
    }

    public void generateDecoration(float scaleSize, boolean isDog) {
        if(isDog) {
            connectionToTail = new TransformNode("connectionToTail",Mat4Transform.translate(-0.1f*scaleSize*0.5f,0.1f*scaleSize*0.5f,0));
            tailScale = new TransformNode("tailScale", Mat4Transform.scale(0.4f*scaleSize,0.3f*scaleSize, 0.3f*scaleSize));
            tailTranslate = new TransformNode("tailTranslate", Mat4Transform.translate(-0.5f, 0, 0));

            headToLeftEar = new TransformNode("headToLeftEar", Mat4Transform.translate(-0.5f*scaleSize*0.5f,0.25f*scaleSize*0.5f,0.5f*scaleSize*0.5f));
            headToRightEar = new TransformNode("headToRightEar", Mat4Transform.translate(-0.5f*scaleSize*0.5f,0.25f*scaleSize*0.5f,-0.5f*scaleSize*0.5f));
            leftEarScale = new TransformNode("leftEarScale", Mat4Transform.scale(0.2f*scaleSize,0.6f*scaleSize, 0.2f*scaleSize));
            rightEarScale = new TransformNode("rightEarScale", Mat4Transform.scale(0.2f*scaleSize,0.6f*scaleSize, 0.2f*scaleSize));
            leftEarTranslate = new TransformNode("leftEarTranslate", Mat4Transform.translate(0,0.5f,0));
            rightEarTranslate = new TransformNode("rightEarTranslate", Mat4Transform.translate(0,0.5f,0));
            Mat4 rotate = Mat4Transform.rotateAroundZ(45);
            rotate = Mat4.multiply(rotate, Mat4Transform.rotateAroundX(45));
            rotateLeftEar = new TransformNode("rotateLeftEar", rotate);
            rotate = Mat4Transform.rotateAroundZ(45);
            rotate = Mat4.multiply(rotate, Mat4Transform.rotateAroundX(-45));
            rotateRightEar = new TransformNode("rotateRightEar", rotate);

            headToLeftEye = new TransformNode("headToLeftEye", Mat4Transform.translate(0.5f*scaleSize*0.5f,0.25f*scaleSize*0.5f,0.5f*scaleSize*0.5f));
            headToRightEye = new TransformNode("headToRightEye", Mat4Transform.translate(0.5f*scaleSize*0.5f,0.25f*scaleSize*0.5f,-0.5f*scaleSize*0.5f));
            leftEyeScale = new TransformNode("leftEyeScale", Mat4Transform.scale(0.2f*scaleSize,0.2f*scaleSize, 0.2f*scaleSize));
            rightEyeScale = new TransformNode("rightEyeScale", Mat4Transform.scale(0.2f*scaleSize,0.2f*scaleSize, 0.2f*scaleSize));
            leftEyeTranslate = new TransformNode("leftEyeTranslate", Mat4Transform.translate(0,0.3f,0.3f));
            rightEyeTranslate = new TransformNode("rightEyeTranslate", Mat4Transform.translate(0,0.3f,-0.3f));

        } else {
            connectionToTail = new TransformNode("connectionToTail",Mat4Transform.translate(0.1f*scaleSize*0.5f,0.1f*scaleSize*0.5f,0));
            tailScale = new TransformNode("tailScale", Mat4Transform.scale(0.8f*scaleSize,0.2f*scaleSize, 0.2f*scaleSize));
            tailTranslate = new TransformNode("tailTranslate", Mat4Transform.translate(0.5f, 0, 0));

            headToLeftEar = new TransformNode("headToLeftEar", Mat4Transform.translate(0.5f*scaleSize*0.5f,0.25f*scaleSize*0.5f,0.5f*scaleSize*0.5f));
            headToRightEar = new TransformNode("headToRightEar", Mat4Transform.translate(0.5f*scaleSize*0.5f,0.25f*scaleSize*0.5f,-0.5f*scaleSize*0.5f));
            leftEarScale = new TransformNode("leftEarScale", Mat4Transform.scale(0.2f*scaleSize,0.3f*scaleSize, 0.2f*scaleSize));
            rightEarScale = new TransformNode("rightEarScale", Mat4Transform.scale(0.2f*scaleSize,0.3f*scaleSize, 0.2f*scaleSize));
            leftEarTranslate = new TransformNode("earTranslate", Mat4Transform.translate(0,0.5f,0));
            rightEarTranslate = new TransformNode("rightEarTranslate", Mat4Transform.translate(0,0.5f,0));
            Mat4 rotate = Mat4Transform.rotateAroundZ(45);
            rotate = Mat4.multiply(rotate, Mat4Transform.rotateAroundX(45));
            rotateLeftEar = new TransformNode("rotateLeftEar", rotate);
            rotate = Mat4Transform.rotateAroundZ(45);
            rotate = Mat4.multiply(rotate, Mat4Transform.rotateAroundX(-45));
            rotateRightEar = new TransformNode("rotateRightEar", rotate);

            headToLeftEye = new TransformNode("headToLeftEye", Mat4Transform.translate(-0.5f*scaleSize*0.5f,0.25f*scaleSize*0.5f,-0.5f*scaleSize*0.5f));
            headToRightEye = new TransformNode("headToRightEye", Mat4Transform.translate(-0.5f*scaleSize*0.5f,0.25f*scaleSize*0.5f,0.5f*scaleSize*0.5f));
            leftEyeScale = new TransformNode("leftEyeScale", Mat4Transform.scale(0.2f*scaleSize,0.2f*scaleSize, 0.2f*scaleSize));
            rightEyeScale = new TransformNode("rightEyeScale", Mat4Transform.scale(0.2f*scaleSize,0.2f*scaleSize, 0.2f*scaleSize));
            leftEyeTranslate = new TransformNode("leftEyeTranslate", Mat4Transform.translate(0,0.3f,-0.3f));
            rightEyeTranslate = new TransformNode("rightEyeTranslate", Mat4Transform.translate(0,0.3f,0.3f));
        }
    }

    public void generateRotateComponent(RotateStyle rotateStyle) {
        this.currentRotateStyle = rotateStyle.clone();

        Mat4 rotate = Mat4Transform.rotateAroundZ(rotateStyle.DEFAULT_ROTATE_Z_LOWER);
        rotate = Mat4.multiply(rotate, Mat4Transform.rotateAroundX(rotateStyle.DEFAULT_ROTATE_X_LOWER));
        rotateLower = new TransformNode("rotateAroundX; rotateAroundZ", rotate);

        rotate = Mat4Transform.rotateAroundZ(rotateStyle.DEFAULT_ROTATE_Z_UPPER);
        rotate = Mat4.multiply(rotate, Mat4Transform.rotateAroundX(rotateStyle.DEFAULT_ROTATE_X_UPPER));
        rotateUpper = new TransformNode("rotateAroundX; rotateAroundZ", rotate);

        rotate = Mat4Transform.rotateAroundY(rotateStyle.DEFAULT_ROTATE_Y_HEAD);
        rotate = Mat4.multiply(rotate, Mat4Transform.rotateAroundZ(rotateStyle.DEFAULT_ROTATE_Z_HEAD));
        rotateHead = new TransformNode("rotateAroundX; rotateAroundZ", rotate);
    }


    public void generateDiffComponentBetweenLeftRight(float baseX, float baseY, float baseZ, float scaleSize, float bulbSide
    ) {
        base = new Model(
                gl, camera, light1, light2, light3, light4, shader, material, new Mat4(1), cubeMesh, textureBase
        );
        baseScale = new TransformNode("baseScale", Mat4Transform.scale(scaleSize,0.25f*scaleSize, scaleSize));
        baseTranslate = new TransformNode("baseTranslate", Mat4Transform.translate(baseX,0.5f,baseZ));
        baseName = new NameNode("base");
        baseNode = new ModelNode("Cube(0)", base, 0);
        baseToConnection = new TransformNode("baseToConnection", Mat4Transform.translate(baseX*scaleSize,0.25f*scaleSize,baseZ));

        connectionBetweenBaseAndLowerScale = new TransformNode("connectionBetweenBaseAndLowerScale", Mat4Transform.scale(0.1f*scaleSize,0.1f*scaleSize,0.1f*scaleSize));
        connectionBetweenBaseAndLowerTranslate = new TransformNode("connectionBetweenBaseAndLowerTranslate", Mat4Transform.translate(0, 0.5f, 0));
        connectionToLower = new TransformNode("connectionToLower", Mat4Transform.translate(0,0.1f*scaleSize,0));

        lowerScale = new TransformNode("lowerScale", Mat4Transform.scale(0.2f*scaleSize,2f*scaleSize,0.2f*scaleSize));
        lowerTranslate = new TransformNode("lowerTranslate", Mat4Transform.translate(0,0.5f,0));
        lowerToConnection = new TransformNode("lowerToConnection", Mat4Transform.translate(0,2f*scaleSize,0));

        connectionBetweenLowerAndUpperScale = new TransformNode("connectionBetweenLowerAndUpperScale", Mat4Transform.scale(0.1f*scaleSize,0.1f*scaleSize,0.1f*scaleSize));
        connectionBetweenLowerAndUpperTranslate = new TransformNode("connectionBetweenLowerAndUpperTranslate", Mat4Transform.translate(0, 0.5f, 0));
        connectionToUpper = new TransformNode("connectionToUpper", Mat4Transform.translate(0,0.1f*scaleSize,0));

        upperScale = new TransformNode("upperScale", Mat4Transform.scale(0.2f*scaleSize,2f*scaleSize,0.2f*scaleSize));
        upperTranslate = new TransformNode("upperTranslate", Mat4Transform.translate(0,0.5f,0));
        upperToConnection = new TransformNode("upperToConnection", Mat4Transform.translate(0,2f*scaleSize,0));

        connectionBetweenUpperAndHeadScale = new TransformNode("connectionBetweenUpperAndHeadScale", Mat4Transform.scale(0.1f*scaleSize,0.1f*scaleSize,0.1f*scaleSize));
        connectionBetweenUpperAndHeadTranslate = new TransformNode("connectionBetweenUpperAndHeadTranslate", Mat4Transform.translate(0, 0.5f, 0));
        connectionToHead = new TransformNode("connectionToHead", Mat4Transform.translate(0,0.1f*scaleSize,0));

        headScale = new TransformNode("headScale", Mat4Transform.scale(0.5f*scaleSize,0.25f*scaleSize,0.5f*scaleSize));
        headTranslate = new TransformNode("headTranslate", Mat4Transform.translate(0,0.5f,0));
        headToBulb = new TransformNode("headToBulb", Mat4Transform.translate(0.5f*scaleSize*0.5f*bulbSide,0.25f*scaleSize*0.5f,0));

        bulbScale = new TransformNode("bulbScale", Mat4Transform.scale(0.1f*scaleSize, 0.1f*scaleSize,0.1f*scaleSize));
        bulbTranslate = new TransformNode("bulbTranslate", Mat4Transform.translate(0.5f*bulbSide,0f,0));
    }

    void buildLamp() {
        addChild(baseName);
        baseName.addChild(baseScale);
        baseScale.addChild(baseTranslate);
        baseTranslate.addChild(baseNode);
        baseName.addChild(baseToConnection);
        baseToConnection.addChild(connectionBetweenBaseAndLowerName);

        connectionBetweenBaseAndLowerName.addChild(connectionBetweenBaseAndLowerScale);
        connectionBetweenBaseAndLowerScale.addChild(connectionBetweenBaseAndLowerTranslate);
        connectionBetweenBaseAndLowerTranslate.addChild(connectionBetweenBaseAndLowerNode);
        connectionBetweenBaseAndLowerName.addChild(connectionToLower);

        connectionToLower.addChild(rotateLower);
        rotateLower.addChild(lowerName);
        lowerName.addChild(lowerScale);
        lowerScale.addChild(lowerTranslate);
        lowerTranslate.addChild(lowerNode);
        lowerName.addChild(lowerToConnection);
        lowerToConnection.addChild(connectionBetweenLowerAndUpperName);

        connectionBetweenLowerAndUpperName.addChild(connectionBetweenLowerAndUpperScale);
        connectionBetweenLowerAndUpperScale.addChild(connectionBetweenLowerAndUpperTranslate);
        connectionBetweenLowerAndUpperTranslate.addChild(connectionBetweenLowerAndUpperNode);
        connectionBetweenLowerAndUpperName.addChild(connectionToUpper);
        connectionBetweenLowerAndUpperName.addChild(connectionToTail);

            connectionToTail.addChild(tailName);
            tailName.addChild(tailScale);
            tailScale.addChild(tailTranslate);
            tailTranslate.addChild(tailNode);

        connectionToUpper.addChild(rotateUpper);
        rotateUpper.addChild(upperName);
        upperName.addChild(upperScale);
        upperScale.addChild(upperTranslate);
        upperTranslate.addChild(upperNode);
        upperName.addChild(upperToConnection);
        upperToConnection.addChild(connectionBetweenUpperAndHeadName);

        connectionBetweenUpperAndHeadName.addChild(connectionBetweenUpperAndHeadScale);
        connectionBetweenUpperAndHeadScale.addChild(connectionBetweenUpperAndHeadTranslate);
        connectionBetweenUpperAndHeadTranslate.addChild(connectionBetweenUpperAndHeadNode);
        connectionBetweenUpperAndHeadName.addChild(connectionToHead);
        connectionToHead.addChild(rotateHead);

        rotateHead.addChild(headName);
        headName.addChild(headScale);
        headScale.addChild(headTranslate);
        headTranslate.addChild(headNode);
        headName.addChild(headToBulb);
        headName.addChild(headToLeftEar);
        headName.addChild(headToRightEar);
        headName.addChild(headToLeftEye);
        headName.addChild(headToRightEye);

            headToLeftEar.addChild(rotateLeftEar);
            rotateLeftEar.addChild(leftEarName);
            leftEarName.addChild(leftEarScale);
            leftEarScale.addChild(leftEarTranslate);
            leftEarTranslate.addChild(leftEarNode);

            headToRightEar.addChild(rotateRightEar);
            rotateRightEar.addChild(rightEarName);
            rightEarName.addChild(rightEarScale);
            rightEarScale.addChild(rightEarTranslate);
            rightEarTranslate.addChild(rightEarNode);

            headToLeftEye.addChild(leftEyeName);
            leftEyeName.addChild(leftEyeScale);
            leftEyeScale.addChild(leftEyeTranslate);
            leftEyeTranslate.addChild(leftEyeNode);

            headToRightEye.addChild(rightEyeName);
            rightEyeName.addChild(rightEyeScale);
            rightEyeScale.addChild(rightEyeTranslate);
            rightEyeTranslate.addChild(rightEyeNode);

        headToBulb.addChild(bulbName);
        bulbName.addChild(bulbScale);
        bulbScale.addChild(bulbTranslate);
        bulbTranslate.addChild(bulbNode);

        update();
    }

    public void updateRotate(RotateStyle rotateStyle) {
        if(currentRotateStyle.DEFAULT_ROTATE_X_LOWER < rotateStyle.DEFAULT_ROTATE_X_LOWER) {
            currentRotateStyle.DEFAULT_ROTATE_X_LOWER += 1;
        }
        if(currentRotateStyle.DEFAULT_ROTATE_X_LOWER > rotateStyle.DEFAULT_ROTATE_X_LOWER) {
            currentRotateStyle.DEFAULT_ROTATE_X_LOWER -= 1;
        }
        if(currentRotateStyle.DEFAULT_ROTATE_Z_LOWER < rotateStyle.DEFAULT_ROTATE_Z_LOWER) {
            currentRotateStyle.DEFAULT_ROTATE_Z_LOWER += 1;
        }
        if(currentRotateStyle.DEFAULT_ROTATE_Z_LOWER > rotateStyle.DEFAULT_ROTATE_Z_LOWER) {
            currentRotateStyle.DEFAULT_ROTATE_Z_LOWER -= 1;
        }
        if(currentRotateStyle.DEFAULT_ROTATE_X_UPPER < rotateStyle.DEFAULT_ROTATE_X_UPPER) {
            currentRotateStyle.DEFAULT_ROTATE_X_UPPER += 1;
        }
        if(currentRotateStyle.DEFAULT_ROTATE_X_UPPER > rotateStyle.DEFAULT_ROTATE_X_UPPER) {
            currentRotateStyle.DEFAULT_ROTATE_X_UPPER -= 1;
        }
        if(currentRotateStyle.DEFAULT_ROTATE_Z_UPPER < rotateStyle.DEFAULT_ROTATE_Z_UPPER) {
            currentRotateStyle.DEFAULT_ROTATE_Z_UPPER += 1;
        }
        if(currentRotateStyle.DEFAULT_ROTATE_Z_UPPER > rotateStyle.DEFAULT_ROTATE_Z_UPPER) {
            currentRotateStyle.DEFAULT_ROTATE_Z_UPPER -= 1;
        }
        if(currentRotateStyle.DEFAULT_ROTATE_Y_HEAD < rotateStyle.DEFAULT_ROTATE_Y_HEAD) {
            currentRotateStyle.DEFAULT_ROTATE_Y_HEAD += 1;
        }
        if(currentRotateStyle.DEFAULT_ROTATE_Y_HEAD > rotateStyle.DEFAULT_ROTATE_Y_HEAD) {
            currentRotateStyle.DEFAULT_ROTATE_Y_HEAD -= 1;
        }
        if(currentRotateStyle.DEFAULT_ROTATE_Z_HEAD < rotateStyle.DEFAULT_ROTATE_Z_HEAD) {
            currentRotateStyle.DEFAULT_ROTATE_Z_HEAD += 1;
        }
        if(currentRotateStyle.DEFAULT_ROTATE_Z_HEAD > rotateStyle.DEFAULT_ROTATE_Z_HEAD) {
            currentRotateStyle.DEFAULT_ROTATE_Z_HEAD -= 1;
        }
        Mat4 rotate = Mat4Transform.rotateAroundZ(currentRotateStyle.DEFAULT_ROTATE_Z_LOWER);
        rotate = Mat4.multiply(rotate, Mat4Transform.rotateAroundX(currentRotateStyle.DEFAULT_ROTATE_X_LOWER));
        rotateLower.setTransform(rotate);
        rotate = Mat4Transform.rotateAroundZ(currentRotateStyle.DEFAULT_ROTATE_Z_UPPER);
        rotate = Mat4.multiply(rotate, Mat4Transform.rotateAroundX(currentRotateStyle.DEFAULT_ROTATE_X_UPPER));
        rotateUpper.setTransform(rotate);
        rotate = Mat4Transform.rotateAroundZ(currentRotateStyle.DEFAULT_ROTATE_Y_HEAD);
        rotate = Mat4.multiply(rotate, Mat4Transform.rotateAroundX(currentRotateStyle.DEFAULT_ROTATE_Z_HEAD));
        rotateHead.setTransform(rotate);
        update();
    }

    public void dispose(GL3 gl) {
        base.dispose(gl);
        connectionBetweenBaseAndLower.dispose(gl);
        lower.dispose(gl);
        connectionBetweenLowerAndUpper.dispose(gl);
        upper.dispose(gl);
        connectionBetweenUpperAndHead.dispose(gl);
        head.dispose(gl);
        bulb.dispose(gl);
        tail.dispose(gl);
        leftEar.dispose(gl);
        rightEar.dispose(gl);
        leftEye.dispose(gl);
        rightEye.dispose(gl);
    }

}
