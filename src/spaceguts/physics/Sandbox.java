package spaceguts.physics;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import spaceguts.entities.Camera;
import spaceguts.entities.Entities;
import spaceguts.entities.Entity;
import spaceout.entities.dynamic.Planet;
import spaceout.entities.dynamic.Player;
import spaceout.entities.passive.Skybox;
import spaceout.entities.passive.Sun;
import spaceout.entities.passive.particles.Debris;
import spaceout.resources.Models;
import spaceout.resources.Textures;
import spaceout.ship.Ship;

/**
 * Sandbox class for physics testing and fun
 * @author TranquilMarmot
 *
 */
public class Sandbox extends Entity{
	public void createSandboxWorld() {
		/* BEGIN SUN */
		Vector3f sunLocation = new Vector3f(1500.0f, 1500.0f, -2.0f);
		float sunSize = 150.0f;
		Vector3f sunIntensity = new Vector3f(0.9f, 0.9f, 0.9f);
		Sun sun = new Sun(sunLocation, sunSize, sunIntensity);
		Entities.addLight(sun);
		/* END SUN */
		
		/* BEGIN BOX
		Vector3f box0Location = new Vector3f(0.0f, -200.0f, 0.0f);
		Quaternion box0Rotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		Vector3f box0Size = new Vector3f(500.0f, 1.00f, 500.0f);
		float box0Mass = 0.0f;
		float box0Restitution = 0.0f;
		//Box box0 = new Box(box0Location, box0Rotation, box0Size, box0Mass, box0Restitution);
		//Entities.addDynamicEntity(box0);
		/* END BOX */
		
		/* BEGIN BOX
		Vector3f box1Location = new Vector3f(0.0f, 200.0f, 0.0f);
		Quaternion box1Rotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		Vector3f box1Size = new Vector3f(500.0f, 1.00f, 500.0f);
		float box1Mass = 0.0f;
		float box1Restitution = 0.0f;
		//Box box1 = new Box(box1Location, box1Rotation, box1Size, box1Mass, box1Restitution);
		//Entities.addDynamicEntity(box1);
		/* END BOX */
		
		/* BEGIN PLAYER */
		Vector3f playerLocation = new Vector3f(50.0f, 50.0f, 1000.0f);
		Quaternion playerRotation = new Quaternion(-0.0058384943f, 0.9989326f, -0.045716275f, -0.0030980944f);
		float playerMass = 50.0f;
		float playerRestitution = 0.01f;
		
		/* TEMP SHIP INFO TODO make this load from XML */
		String shipName = "WingX";
		Models shipModel = Models.WESCOTT;
		int shipHealth = 100;
		float shipMass = 50.0f;
		float shipRestitution = 0.01f;
		Vector3f shipAcceleration = new Vector3f(1000.0f, 1000.0f, 1000.0f);
		float shipTopSpeed = 200.0f;
		float shipStabilizationSpeed = 0.5f;
		float shipStopSpeed = 1.0f;
		float shipRollSpeed = 0.5f;
		float shipXTurnSpeed = 0.0025f;
		float shipYTurnSpeed = 0.0025f;
		
		Ship ship = new Ship(shipName, shipModel, shipHealth, shipMass, shipRestitution, shipAcceleration, shipTopSpeed, shipStabilizationSpeed, shipStopSpeed, shipRollSpeed, shipXTurnSpeed, shipYTurnSpeed);
		
		Player player = new Player(playerLocation, playerRotation, ship, playerMass, playerRestitution);
		player.type = "dynamicPlayer";
		Entities.player = player;
		
		Camera.createCamera();
		Entities.camera.following = player;
		
		/* END PLAYER */
		
		/* BEGIN SKYBOX  */
		Skybox skybox = new Skybox(Entities.camera);
		Entities.skybox = skybox;
		/* END SKYBOX */
		
		
		/* BEGIN DEBRIS */
		Debris debris = new Debris(Entities.camera, 2500, 25000.0f, 420133742L);
		debris.update();
		Entities.addPassiveEntity(debris);
		/* END DEBRIS */
	}
	
	private static void addRandomSphere(){
		Random randy = new Random();
		float sphereSize = randy.nextInt(200) / 10.0f;
		
		Textures sphereTexture;
		String tex;
		
		switch(randy.nextInt(4)){
		case 0:
			sphereTexture = Textures.EARTH;
			tex = "Earth";
			break;
		case 1:
			sphereTexture = Textures.MERCURY;
			tex = "Mercury";
			break;
		case 2:
			sphereTexture = Textures.VENUS;
			tex = "Venus";
			break;
		case 3:
			sphereTexture = Textures.MARS;
			tex = "Mars";
			break;
		default:
			sphereTexture = Textures.EARTH;
			tex = "Earth";
		}
		
		float sphereX = 0.0f + (randy.nextFloat() * 100.0f);
		float sphereY = 0.0f + (randy.nextFloat() * 100.0f);
		float sphereZ = 0.0f + (randy.nextFloat() * 100.0f);
		Vector3f sphereLocation = new Vector3f(sphereX, sphereY, sphereZ);
		Quaternion sphereRotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		
		//float sphereMass = randy.nextFloat() * 10.0f;
		float sphereMass = sphereSize * 2.0f;
		
		Planet p = new Planet(sphereLocation, sphereRotation, sphereSize, sphereMass, 0.0f, sphereTexture);
		p.type = tex;
		
		Entities.addDynamicEntity(p);
	}

	@Override
	public void update() {
		if(Keyboard.isKeyDown(Keyboard.KEY_P))
			addRandomSphere();
	}

	@Override
	public void draw() {
	}

	@Override
	public void cleanup() {
	}
}
