package com.bitwaffle.spaceout.entities.dynamic;

import java.util.Random;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

import com.bitwaffle.spaceguts.entities.DynamicEntity;
import com.bitwaffle.spaceguts.entities.Entities;
import com.bitwaffle.spaceguts.entities.Entity;
import com.bitwaffle.spaceguts.graphics.render.Render3D;
import com.bitwaffle.spaceguts.physics.CollisionTypes;
import com.bitwaffle.spaceguts.physics.Physics;
import com.bitwaffle.spaceguts.util.QuaternionHelper;
import com.bitwaffle.spaceout.interfaces.Health;
import com.bitwaffle.spaceout.interfaces.Projectile;
import com.bitwaffle.spaceout.resources.Models;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.Transform;

/**
 * A la Atari's 1979 classic, Asteroids.
 * @author TranquilMarmot
 */
public class Asteroid extends DynamicEntity implements Health, Projectile{
	final static short COL_GROUP = CollisionTypes.PLANET;
	final static short COL_WITH = (short)(CollisionTypes.SHIP | CollisionTypes.WALL | CollisionTypes.PLANET | CollisionTypes.PICKUP | CollisionTypes.PROJECTILE);
	
	final static int DAMAGE = 10;
	
	final static float ASTEROID_RESTITUTION = 0.5f; 
	
	/** Size at which the asteroid will drop loot instead of splitting into smaller asteroids*/
	final static float LOOT_SIZE = 10.0f;
	
	/** How many items an asteroid drops when it's destroyed and is smaller than LOOT_SIZE */
	final static int LOOT_AMOUNT = 25;
	
	/** How many asteroids are created when the asteroid is destroyed */
	final static int NUMBER_OF_DIVISIONS = 3;
	
	/** How heavy an asteroid is based on it's size (mass = size * MASS_FACTOR) */
	final static float MASS_FACTOR = 10;
	
	int health = 100;
	
	private float size;
	
	public Asteroid(Vector3f location, Quaternion rotation, float size) {
		super(location, rotation, new SphereShape(size), size * MASS_FACTOR, ASTEROID_RESTITUTION, COL_GROUP, COL_WITH);
		
		rigidBody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
		rigidBody.setAngularVelocity(new javax.vecmath.Vector3f(0.0f, 0.015f, 0.0f));
		
		this.type = "Asteroid (size " + size + " health " + health + ")";
		this.model = Models.ASTEROID.getModel();
		this.size = size;
	}
	
	@Override
	public void draw(){
		Matrix4f oldModelView = new Matrix4f();
		oldModelView.load(Render3D.modelview);
		Render3D.modelview.scale(new org.lwjgl.util.vector.Vector3f(size, size, size));
		Render3D.program.setUniform("ModelViewMatrix", Render3D.modelview);
		super.draw();
		Render3D.modelview.load(oldModelView);
	}
	
	@Override
	/**
	 * Draws the physics debug info for this entity. Should be called before
	 * rotations are applied.
	 */
	public void drawPhysicsDebug() {
		Transform worldTransform = new Transform();
		rigidBody.getWorldTransform(worldTransform);

		Physics.dynamicsWorld.debugDrawObject(worldTransform, Models.ASTEROID.getModel().getCollisionShape(),
				new javax.vecmath.Vector3f(0.0f, 0.0f, 0.0f));
	}
	
	@Override
	public void update(float timeStep){
		super.update(timeStep);
	}

	@Override
	public int getCurrentHealth() {
		return health;
	}

	@Override
	public void hurt(int amount) {
		health -= amount;
		this.type = "Asteroid (size " + size + " health " + health + ")";
		if(health <= 0){
			explode();
		}
		
	}
	
	/**
	 * Cause the asteroid to explode
	 */
	private void explode(){
		removeFlag = true;
		
		if(size <= LOOT_SIZE){
			for(int i = 0; i < 25; i++){
				addRandomDiamond();
			}
		} else{
			for(int i = 0; i < NUMBER_OF_DIVISIONS; i++){
				addRandomAsteroid(this.size / NUMBER_OF_DIVISIONS);
			}
		}
	}
	
	private void addRandomAsteroid(float newSize){
		Random randy = new Random();

		float asteroidX = randy.nextFloat() * 10.0f;
		float asteroidY = randy.nextFloat() * 10.0f;
		float asteroidZ = randy.nextFloat() * 10.0f;
		
		asteroidX = randy.nextBoolean() ? -asteroidX - size : asteroidX + size;
		asteroidY = randy.nextBoolean() ? -asteroidY - size : asteroidY + size;
		asteroidZ = randy.nextBoolean() ? -asteroidZ - size : asteroidZ + size;
		
		Vector3f asteroidLocation = new Vector3f();

		// put the asteroid right in front of the camera
		Vector3f downInFront = QuaternionHelper.rotateVectorByQuaternion(
				new Vector3f(asteroidX, asteroidY, asteroidZ), this.rotation);

		Vector3f.add(this.location, downInFront, asteroidLocation);

		Quaternion asteroidRotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		
		float xRot = randy.nextFloat() * 100.0f;
		float yRot = randy.nextFloat() * 100.0f;
		float zRot = randy.nextFloat() * 100.0f;
		
		asteroidRotation = QuaternionHelper.rotate(asteroidRotation, new Vector3f(xRot,yRot, zRot));
		
		Asteroid a = new Asteroid(asteroidLocation, asteroidRotation, newSize);

		Entities.addDynamicEntity(a);
	}
	
	// TODO find a better way to do loot drops
	private void addRandomDiamond() {
		Random randy = new Random();

		float diamondX = randy.nextFloat() * 10.0f;
		float diamondY = randy.nextFloat() * 10.0f;
		float diamondZ = randy.nextFloat() * 10.0f;
		
		if(randy.nextBoolean()) diamondX = -diamondX;
		if(randy.nextBoolean()) diamondY = -diamondY;
		if(randy.nextBoolean()) diamondZ = -diamondZ;
		
		Vector3f diamondLocation = new Vector3f();

		Vector3f downInFront = QuaternionHelper.rotateVectorByQuaternion(
				new Vector3f(diamondX, diamondY, diamondZ), this.rotation);

		Vector3f.add(this.location, downInFront, diamondLocation);

		Quaternion diamondRotation = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		
		float xRot = randy.nextFloat() * 100.0f;
		float yRot = randy.nextFloat() * 100.0f;
		float zRot = randy.nextFloat() * 100.0f;
		
		diamondRotation = QuaternionHelper.rotate(diamondRotation, new Vector3f(xRot,yRot, zRot));
		
		float diamondStopSpeed = 0.3f;
		
		Diamond d = new Diamond(diamondLocation, diamondRotation, diamondStopSpeed);

		Entities.addDynamicEntity(d);
	}

	@Override
	public void heal(int amount) {
		health += amount;
	}

	@Override
	public int getDamage() {
		return DAMAGE;
	}

	@Override
	public Entity getOwner() {
		return this;
	}
}
