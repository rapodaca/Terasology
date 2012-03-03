package org.terasology.gui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class ComponentTest {
	public static class AddChild {
		private Component component;
		
		@Before
		public void setUp() {
			component = new Component();
		}
		@Test
		public void setsParentOfChild() {
			Component child = new Component();
			
			component.addChild(child);
			assertEquals(component, child.getParent());
		}
	}
	public static class RemoveChild {
		private Component component, child;
		@Before
		public void setUp() {
			component = new Component();
			child = new Component();
			
			component.addChild(child);
		}
		@Test
		public void unsetsParentOfChild() {
			component.removeChild(child);
			assertNull(child.getParent());
		}
	}
	public static class HandleMouse {
		private Component component;
		private ComponentListener listener;
		
		@Before
		public void setUp() {
			component = new Component();
			listener = mock(ComponentListener.class);
			
			component.setBounds(0, 0, 10, 10);
			component.addListener(listener);
		}
		@Test
		public void dispatchesMouseEnteredGivenInside() {			
			component.handleMouse(5, 5, MouseButton.NONE);
			verify(listener).mouseEntered(component);
		}
		@Test
		public void doesNotDispatchMouseEnteredGivenOutside() {
			component.handleMouse(200, 200, MouseButton.NONE);
			verify(listener, times(0)).mouseEntered(component);
		}
		@Test
		public void doesNotDoubleDispatchMouseEntered() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(10, 10, MouseButton.NONE);
			verify(listener, times(1)).mouseEntered(component);
		}
		@Test
		public void doesNotDispatchMouseMovedGivenNotEntered() {
			component.handleMouse(5, 5, MouseButton.NONE);
			verify(listener, times(0)).mouseMoved(component);
		}
		@Test
		public void doesNotDispatchMouseMovedGivenNotInside() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(50, 50, MouseButton.NONE);
			verify(listener, times(0)).mouseMoved(component);
		}
		@Test
		public void dispatchesMouseMovedGivenEntered() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(10, 10, MouseButton.NONE);
			verify(listener).mouseMoved(component);
		}
		@Test
		public void dispatchesMouseExitedGivenEntered() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(20, 20, MouseButton.NONE);
			verify(listener).mouseExited(component);
		}
		@Test
		public void doesNotDispatchMouseExitedGivenNotInside() {
			component.handleMouse(20, 20, MouseButton.NONE);
			verify(listener, times(0)).mouseExited(component);
		}
		@Test
		public void forwardToChild() {
			Component child = new Component();
			ComponentListener childListener = mock(ComponentListener.class);
			
			child.setBounds(0, 0, 10, 10);
			child.addListener(childListener);
			component.addChild(child);
			
			component.handleMouse(5, 5, MouseButton.NONE);
			verify(childListener).mouseEntered(child);
		}
		@Test
		public void dispatchesLeftMouseDownGivenInside() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(5, 5, MouseButton.LEFT);
			verify(listener).leftButtonDown(component);
		}
		@Test
		public void doesNotDispatchLeftMouseDownGivenOutside() {
			component.handleMouse(100, 100, MouseButton.LEFT);
			verify(listener, times(0)).leftButtonDown(component);
		}
		@Test
		public void doesNotDoubleDispatchLeftMouseDown() {
			component.handleMouse(5, 5, MouseButton.LEFT);
			component.handleMouse(5, 5, MouseButton.LEFT);
			verify(listener, times(1)).leftButtonDown(component);
		}
		@Test
		public void dispatchesRightMouseDownGivenInside() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(5, 5, MouseButton.RIGHT);
			verify(listener).rightButtonDown(component);
		}
		@Test
		public void doesNotDoubleDispatchRightMouseDown() {
			component.handleMouse(5, 5, MouseButton.RIGHT);
			component.handleMouse(5, 5, MouseButton.RIGHT);
			verify(listener, times(1)).rightButtonDown(component);
		}
		@Test
		public void dispatchesLeftMouseUp() {
			component.handleMouse(5, 5, MouseButton.LEFT);
			component.handleMouse(5, 5, MouseButton.NONE);
			verify(listener).leftButtonUp(component);
		}
	}
}
