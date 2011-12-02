package util.debug.console;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.lwjgl.util.vector.Vector3f;

import util.Runner;
import util.debug.Debug;
import entities.Entities;
import entities.Entity;

/**
 * Commands that the {@link Console} carries out. See console_commands.txt.
 * 
 * @author TranquilMarmot
 * @see Console
 * 
 */
public class ConsoleCommands {
	private static Console console = Debug.console;

	/**
	 * Issues a command
	 * 
	 * @param comm
	 *            The command to issue
	 */
	public static void issueCommand(String comm) {
		// make sure the command isn't empty
		if (comm.length() > 1) {
			// split the command at the spaces
			StringTokenizer toker = new StringTokenizer(comm, " ");

			// grab the actual command and lop off the / at the beginning
			String command = toker.nextToken();
			command = command.substring(1, command.length());

			try {
				// player position
				if (command.equals("xyz") || command.equals("pos")
						|| command.equals("position")) {
					positionCommand(toker);
				}

				// clear console
				else if (command.equals("clear")) {
					console.text.clear();
				}

				// player speed
				else if (command.equals("speed")) {
					speedCommand(toker);
				}

				// list entities
				else if (command.equals("listentities")) {
					console.print("Listing entities...");
					for (entities.Entity ent : Entities.entities)
						console.print(ent.type);
				}

				// print number of entities
				else if (command.equals("numentities")) {
					console.print("Number of entities: "
							+ Entities.entities.size());
				}

				// 99 bottles of beer on the wall
				else if (command.equals("beer")) {
					for (int i = 99; i > 0; i--) {
						console.print(i + " bottles of beer on the wall, " + i
								+ " bottles of beer");
						console.print("Take one down, pass it around, "
								+ (i - 1) + " bottles of beer on the wall");
					}
				}

				// camera command
				else if (command.equals("camera")) {
					cameraCommand(toker);
				}

				// quit
				else if (command.equals("quit") || command.equals("exit")
						|| command.equals("q")) {
					Runner.done = true;
				}

				// warp command
				else if (command.equals("warp")) {
					warpCommand(toker);
				}

				// invalid
				else {
					console.print("Invalid command! (" + command + ")");
				}
			} catch (NoSuchElementException e) {
				console.print("Not enough vairbales for command '" + command
						+ "'!");
			} catch (NumberFormatException e) {
				console.print("Incorrect number format "
						+ e.getLocalizedMessage().toLowerCase());
			}
		}
	}

	/**
	 * Carry out a command to change the player's position
	 * 
	 * @param toker
	 *            StringTokenizer containing the command
	 */
	private static void positionCommand(StringTokenizer toker) {
		float x = Float.parseFloat(toker.nextToken());
		float y = Float.parseFloat(toker.nextToken());
		float z = Float.parseFloat(toker.nextToken());

		float oldX = Entities.player.location.x;
		float oldY = Entities.player.location.y;
		float oldZ = Entities.player.location.z;

		console.print("Moving player to x: " + x + " y: " + y + " z: " + z
				+ " from x: " + oldX + " y: " + oldY + " z: " + oldZ);

		Entities.player.location.x = x;
		Entities.player.location.y = y;
		Entities.player.location.z = z;
	}

	/**
	 * Command to warp to an entity
	 * 
	 * @param toker
	 *            StringTokenizer containing the command
	 */
	private static void warpCommand(StringTokenizer toker) {
		boolean hasWarped = false;
		String warp = toker.nextToken();
		for (Entity ent : Entities.entities) {
			if (ent.type.toLowerCase().equals(warp.toLowerCase())) {
				console.print("Warping Player to " + ent.type + " ("
						+ ent.location.x + "," + ent.location.y + ","
						+ ent.location.z + ")...");
				Entities.player.location = new Vector3f(ent.location.x,
						ent.location.y, ent.location.z);
				hasWarped = true;
				break;
			}
		}
		
		if(!hasWarped){
			console.print("Couldn't find entity " + warp);
		}
	}

	/**
	 * Command to change the player's speed variables
	 * 
	 * @param toker
	 *            StringTokenizer containing the command
	 */
	private static void speedCommand(StringTokenizer toker) {
		String speedCommand = toker.nextToken().toLowerCase();
		Float speedChange = Float.parseFloat(toker.nextToken());

		// max x speed
		if (speedCommand.equals("maxx") || speedCommand.equals("xmax")) {
			console.print("Changing player max X speed from "
					+ Entities.player.maxXSpeed + " to " + speedChange);
			Entities.player.maxXSpeed = speedChange;
		}

		// max y speed
		else if (speedCommand.equals("maxy") || speedCommand.equals("ymax")) {
			console.print("Changing player max Y speed from "
					+ Entities.player.maxYSpeed + " to " + speedChange);
			Entities.player.maxYSpeed = speedChange;
		}

		// max z speed
		else if (speedCommand.equals("maxz") || speedCommand.equals("zmax")) {
			console.print("Changing player max Z speed from "
					+ Entities.player.maxZSpeed + " to " + speedChange);
			Entities.player.maxZSpeed = speedChange;
		}

		// x acceleration
		else if (speedCommand.equals("accelx") || speedCommand.equals("xaccel")) {
			console.print("Changing player X acceleration from "
					+ Entities.player.xAccel + " to " + speedChange);
			Entities.player.xAccel = speedChange;
		}

		// y acceleration
		else if (speedCommand.equals("accely") || speedCommand.equals("yaccel")) {
			console.print("Changing player Y acceleration from "
					+ Entities.player.yAccel + " to " + speedChange);
			Entities.player.yAccel = speedChange;
		}

		// z acceleration
		else if (speedCommand.equals("accelz") || speedCommand.equals("zaccel")) {
			console.print("Changing player Z acceleration from "
					+ Entities.player.zAccel + " to " + speedChange);
			Entities.player.zAccel = speedChange;
		}

		// x deceleration
		else if (speedCommand.equals("decelx") || speedCommand.equals("xdecel")) {
			console.print("Changing player X deceleration from "
					+ Entities.player.xDecel + " to " + speedChange);
			Entities.player.xDecel = speedChange;
		}

		// y deceleration
		else if (speedCommand.equals("decely") || speedCommand.equals("ydecel")) {
			console.print("Changing player Y deceleration from "
					+ Entities.player.yDecel + " to " + speedChange);
			Entities.player.yDecel = speedChange;
		}

		// z deceleration
		else if (speedCommand.equals("decelz") || speedCommand.equals("zdecel")) {
			console.print("Changing player Z deceleration from "
					+ Entities.player.zDecel + " to " + speedChange);
			Entities.player.zDecel = speedChange;
		}

		// invalid
		else {
			console.print("Not a valid speed command!");
		}
	}

	/**
	 * Command to change how the camera behaves
	 * 
	 * @param toker
	 *            StringTokenizer containing the command
	 */
	private static void cameraCommand(StringTokenizer toker) {
		String cameraCommand = toker.nextToken().toLowerCase();

		// zoom
		if (cameraCommand.equals("zoom")) {
			float zoom = Float.parseFloat(toker.nextToken());
			console.print("Changing camera zoom from " + Entities.camera.zoom
					+ " to " + zoom);
			Entities.camera.zoom = zoom;
		}

		// y offset
		else if (cameraCommand.equals("yoffset")) {
			float yOffset = Float.parseFloat(toker.nextToken());
			console.print("Changing camera yOffset from "
					+ Entities.camera.yOffset + " to " + yOffset);
			Entities.camera.yOffset = yOffset;
		}

		// x offset
		else if (cameraCommand.equals("xoffset")) {
			float xOffset = Float.parseFloat(toker.nextToken());
			console.print("Changing camera xOffset from "
					+ Entities.camera.xOffset + " to " + xOffset);
			Entities.camera.xOffset = xOffset;
		}

		// following
		else if (cameraCommand.equals("follow")) {
			boolean changed = false;
			String toFollow = toker.nextToken();

			if (toFollow.toLowerCase().equals("player")) {
				console.print("Camera now following " + toFollow);
				Entities.camera.following = Entities.player;
				changed = true;
			} else {
				for (Entity ent : Entities.entities) {
					if (ent.type.toLowerCase().equals(toFollow.toLowerCase())) {
						console.print("Camera now following " + toFollow);
						Entities.camera.following = ent;
						changed = true;
						break;
					}
				}
			}
			
			if (!changed) {
				console.print("Couldn't find entity " + toFollow);
			}
		}

		// invalid
		else {
			console.print("Invalid camera command! (" + cameraCommand + ")");
		}
	}
}
