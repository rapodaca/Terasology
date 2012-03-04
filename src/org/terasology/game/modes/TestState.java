package org.terasology.game.modes;

import java.util.ArrayList;

import javax.vecmath.Vector2f;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.terasology.game.Terasology;
import org.terasology.rendering.gui.components.UICrosshair;
import org.terasology.rendering.gui.components.UIImageOverlay;
import org.terasology.rendering.gui.framework.UIDisplayElement;

public class TestState implements IGameState {
    /* GUI */
    private UIImageOverlay _overlay = null;
    
    public TestState() {
        _overlay = new UIImageOverlay("menuBackground");
        _overlay.setVisible(true);

    }
    
	@Override
	public void init() {

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void activate() {
		Mouse.setGrabbed(false);
	}

	@Override
	public void deactivate() {

	}

	@Override
	public void update(double delta) {

	}

	@Override
	public void render() {
		_overlay.render();
	}

	@Override
	public void processKeyboardInput() {

	}

	@Override
	public void processMouseInput() {

	}
}
