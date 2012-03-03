package org.terasology.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Mouse;

public class Component {
	private List<ComponentListener> _listeners;
	private List<Component> _children;
	private Component _parent;
	private double _x;
	private double _y;
	private double _width;
	private double _height;
	
	public Component() {
		_listeners = new ArrayList<ComponentListener>();
		_children = new ArrayList<Component>();
		_parent = null;
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
	
	public void handleMouse() {
		if (containsPoint(Mouse.getX(), Mouse.getY())) {
			for (Component child : _children) {
				child.handleMouse();
			}			
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
	
	protected void dispatchEvent(EventType type) {		
		for (ComponentListener listener : _listeners) {
			switch(type) {
			case MOUSE_DOWN:
				listener.mouseDown(this);
				break;
			case MOUSE_UP:
				listener.mouseUp(this);
				break;
			case MOUSE_MOVED:
				listener.mouseMoved(this);
				break;
			case MOUSE_DRAGGED:
				listener.mouseDragged(this);
				break;
			}
		}
	}
}
