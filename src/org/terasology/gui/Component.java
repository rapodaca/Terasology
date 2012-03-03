package org.terasology.gui;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2d;

/**
 * Top-level 2D GUI component.
 */
public class Component {
	private List<ComponentListener> _listeners;
	private List<Component> _children;
	private Component _parent;
	private double _x;
	private double _y;
	private double _width;
	private double _height;
	private Vector2d _lastCursor;
	private MouseButton _lastButton;
	
	public Component() {
		_listeners = new ArrayList<ComponentListener>();
		_children = new ArrayList<Component>();
		_parent = null;
		_lastCursor = null;
		_lastButton = MouseButton.NONE;
	}
	
	public void addListener(ComponentListener listener) {
		_listeners.add(listener);
	}
	
	public void removeListener(ComponentListener listener) {
		_listeners.remove(listener);
	}
	
	public void addChild(Component child) {
		_children.add(child);
		child.setParent(this);
	}
	
	public void removeChild(Component child) {
		_children.remove(child);
		child.setParent(null);
	}
	
	public void render() {
		for (Component child : _children) {
			child.render();
		}
	}
	
	public Component getParent() {
		return _parent;
	}
	
	public void handleMouse(double x, double y, MouseButton button) {
		forwardMouse(x, y, button);
		
		if (!containsPoint(x, y)) {
			if (_lastCursor != null) {
				dispatch(EventType.MOUSE_EXITED);
				_lastCursor = null;
			}
			return;
		}
		
		switch (button) {
		case NONE:
			handleMouseNoButton(x, y);
			break;
		case LEFT:
			handleMouseLeft(x, y);
			break;
		case RIGHT:
			handleMouseRight(x, y);
			break;
		}
	}
	
	public void setBounds(double x, double y, double width, double height) {
		_x = x;
		_y = y;
		_width = width;
		_height = height;
	}
	
	public boolean containsPoint(double x, double y) {
		if (x > _x + _width || x < _x) {
			return false;
		}
		if (y < _y || y > _y + _height) {
			return false;
		}
		
		return true;
	}
	
	protected void setParent(Component parent) {
		_parent = parent;
	}
	
	protected void dispatch(EventType type) {		
		for (ComponentListener listener : _listeners) {
			switch(type) {
			case LEFT_MOUSE_DOWN:
				listener.leftButtonDown(this);
				break;
			case LEFT_MOUSE_UP:
				listener.leftButtonUp(this);
				break;
			case RIGHT_MOUSE_DOWN:
				listener.rightButtonDown(this);
				break;
			case RIGHT_MOUSE_UP:
				listener.rightButtonUp(this);
				break;
			case MOUSE_MOVED:
				listener.mouseMoved(this);
				break;
			case MOUSE_DRAGGED:
				listener.mouseDragged(this);
				break;
			case MOUSE_ENTERED:
				listener.mouseEntered(this);
				break;
			case MOUSE_EXITED:
				listener.mouseExited(this);
				break;
			}
		}
	}
	
	private void forwardMouse(double x, double y, MouseButton button) {
		for (Component child : _children) {
			child.handleMouse(x, y, button);
		}
	}
	
	private void recordCursor(double x, double y) {
		_lastCursor = new Vector2d(x, y);
	}
	
	private void handleMouseNoButton(double x, double y) {
		if (_lastCursor == null) {
			dispatch(EventType.MOUSE_ENTERED);		
		} else {
			dispatch(EventType.MOUSE_MOVED);			
		}
		
		switch (_lastButton) {
		case LEFT:
			dispatch(EventType.LEFT_MOUSE_UP);
			break;
		case RIGHT:
			dispatch(EventType.RIGHT_MOUSE_UP);
			break;
		}
		
		recordCursor(x, y);
	}
	
	private void handleMouseLeft(double x, double y) {
		switch (_lastButton) {
		case NONE:
			dispatch(EventType.LEFT_MOUSE_DOWN);			
			break;
		case LEFT:
			dispatch(EventType.MOUSE_DRAGGED);
			break;
		case RIGHT:
			break;
		}
		
		_lastButton = MouseButton.LEFT;
	}
	
	private void handleMouseRight(double x, double y) {
		if (_lastButton != MouseButton.RIGHT) {
			dispatch(EventType.RIGHT_MOUSE_DOWN);
		}
		
		_lastButton = MouseButton.RIGHT;
	}
}
