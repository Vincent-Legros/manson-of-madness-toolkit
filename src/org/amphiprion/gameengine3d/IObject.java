package org.amphiprion.gameengine3d;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import org.amphiprion.gameengine3d.util.Velocity;

public interface IObject extends IPosition {
	public void draw(GL10 gl);

	public void setParent(IObject parent);

	IObject getParent();

	public void update(float sElapsed, List<IObject> collidableMesh);

	public boolean isMoving();

	public Velocity getVelocity();

	public void setVelocity(float vx, float vy, float vz, float sElapsed);

	// void unloadTexture();
}
