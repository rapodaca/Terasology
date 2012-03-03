package org.terasology.gui;

public class ComponentEvent {
	private Component _target;
	private EventType _type;
	
	public ComponentEvent(Component target) {
		_target = target;
	}
	
	public Component getTarget() {
		return _target;
	}
	
	public EventType getType() {
		return _type;
	}
}
