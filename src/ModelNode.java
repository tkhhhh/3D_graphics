import com.jogamp.opengl.*;

public class ModelNode extends SGNode {

    protected Model model;
    private double time;

    public ModelNode(String name, Model m, double time) {
        super(name);
        model = m;
    }

    public void draw(GL3 gl) {
        model.render(gl, worldTransform, time);
        for (int i=0; i<children.size(); i++) {
            children.get(i).draw(gl);
        }
    }

}