package spaceguts.util.resources;

import spaceguts.graphics.glsl.GLSLModel;
import spaceguts.graphics.glsl.GLSLModelLoader;
import spaceguts.util.model.Model;
import spaceguts.util.model.ModelLoader;

/**
 * All the possible models for the game. Each value in the enum also contains the path for the model, its scale, and which texture it uses.
 * @author TranquilMarmot
 *
 */
public enum Models {
	WING_X(Paths.MODEL_PATH.path() + "ships/wing_x.obj", 0.5f, Textures.SHIP1),
	LASERBULLET(Paths.MODEL_PATH.path() + "laserbullet.obj", 1.0f, Textures.LASERBULLET),
	SAUCER(Paths.MODEL_PATH.path() + "ships/saucer.obj", 1.0f, Textures.SAUCER),
	SKYBOX(Paths.MODEL_PATH.path() + "skybox.obj", 100000.0f, Textures.STARS),
	WESCOTT(Paths.MODEL_PATH.path() + "ships/wescott.obj", 1.0f, Textures.WESCOTT);
	
	/** the actual model object */
	private GLSLModel model;
	/** which file to load the model from */
	private String file;
	/** what scale the model is at */
	private float scale;
	/** which texture to use for the model */
	private Textures texture;
	
	private Models(String file, float scale, Textures texture){
		this.file = file;
		this.scale = scale;
		this.texture = texture;
	}
	
	public GLSLModel getModel(){
		if(!modelLoaded())
			initModel();
		return model; 
	}
	
	public boolean modelLoaded(){
		return model != null;
	}
	
	public void initModel(){
		//System.out.println("initializing model " + this);
		if(!modelLoaded()){
			model = GLSLModelLoader.loadObjFile(file, scale, texture);
		}
	}
}
