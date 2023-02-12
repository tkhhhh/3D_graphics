/* I declare this code is my own work */
/* Author Xienan Fang XFang24@sheffield.ac.uk */
public class RotateStyle {
    public float DEFAULT_ROTATE_X_LOWER = 0;
    public float DEFAULT_ROTATE_Z_LOWER = 30;
    public float DEFAULT_ROTATE_X_UPPER = 10;
    public float DEFAULT_ROTATE_Z_UPPER = -60;
    public float DEFAULT_ROTATE_Z_HEAD = 10;
    public float DEFAULT_ROTATE_Y_HEAD = 10;
    public RotateStyle clone() {
        RotateStyle rotateStyle = new RotateStyle();
        rotateStyle.DEFAULT_ROTATE_X_LOWER = DEFAULT_ROTATE_X_LOWER;
        rotateStyle.DEFAULT_ROTATE_Z_LOWER = DEFAULT_ROTATE_Z_LOWER;
        rotateStyle.DEFAULT_ROTATE_X_UPPER = DEFAULT_ROTATE_X_UPPER;
        rotateStyle.DEFAULT_ROTATE_Z_UPPER = DEFAULT_ROTATE_Z_UPPER;
        rotateStyle.DEFAULT_ROTATE_Z_HEAD = DEFAULT_ROTATE_Z_HEAD;
        rotateStyle.DEFAULT_ROTATE_Y_HEAD = DEFAULT_ROTATE_Y_HEAD;
        return rotateStyle;
    }
}
