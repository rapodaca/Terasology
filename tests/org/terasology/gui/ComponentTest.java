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
	public static class HandleMouseEnter {
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
		public void dispatchesGivenInside() {			
			component.handleMouse(5, 5, MouseButton.NONE);
			verify(listener).mouseEntered(component);
		}
		@Test
		public void doesNotDispatchGivenOutside() {
			component.handleMouse(200, 200, MouseButton.NONE);
			verify(listener, times(0)).mouseEntered(component);
		}
		@Test
		public void doesNotDoubleDispatch() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(9, 9, MouseButton.NONE);
			verify(listener, times(1)).mouseEntered(component);
		}
		@Test
		public void dispatchesGivenEnterExitEnter() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(20, 20, MouseButton.NONE);
			component.handleMouse(5, 5, MouseButton.NONE);
			verify(listener, times(2)).mouseEntered(component);
		}
	}
	public static class HandleMouseExit {
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
		public void dispatchesGivenEntered() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(20, 20, MouseButton.NONE);
			verify(listener).mouseExited(component);
		}
		@Test
		public void doesNotDispatchGivenNotInside() {
			component.handleMouse(20, 20, MouseButton.NONE);
			verify(listener, times(0)).mouseExited(component);
		}
	}
	public static class HandleMouseButton {
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
		public void dispatchesLeftDownGivenInside() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(5, 5, MouseButton.LEFT);
			verify(listener).leftButtonDown(component);
		}
		@Test
		public void doesNotDispatchLeftDownGivenOutside() {
			component.handleMouse(100, 100, MouseButton.LEFT);
			verify(listener, times(0)).leftButtonDown(component);
		}
		@Test
		public void doesNotDoubleDispatchLeftDown() {
			component.handleMouse(5, 5, MouseButton.LEFT);
			component.handleMouse(5, 5, MouseButton.LEFT);
			verify(listener, times(1)).leftButtonDown(component);
		}
		@Test
		public void dispatchesRightDownGivenInside() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(5, 5, MouseButton.RIGHT);
			verify(listener).rightButtonDown(component);
		}
		@Test
		public void doesNotDoubleDispatchRightDown() {
			component.handleMouse(5, 5, MouseButton.RIGHT);
			component.handleMouse(5, 5, MouseButton.RIGHT);
			verify(listener, times(1)).rightButtonDown(component);
		}
		@Test
		public void dispatchesLeftUp() {
			component.handleMouse(5, 5, MouseButton.LEFT);
			component.handleMouse(5, 5, MouseButton.NONE);
			verify(listener).leftButtonUp(component);
		}
		@Test
		public void dispatchesRightUp() {
			component.handleMouse(5, 5, MouseButton.RIGHT);
			component.handleMouse(5, 5, MouseButton.NONE);
			verify(listener).rightButtonUp(component);
		}
	}
	public static class HandleMouseMove {
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
		public void doesNotDispatchGivenNotEntered() {
			component.handleMouse(5, 5, MouseButton.NONE);
			verify(listener, times(0)).mouseMoved(component);
		}
		@Test
		public void doesNotDispatchGivenNotInside() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(50, 50, MouseButton.NONE);
			verify(listener, times(0)).mouseMoved(component);
		}
		@Test
		public void dispatchesGivenEntered() {
			component.handleMouse(5, 5, MouseButton.NONE);
			component.handleMouse(10, 10, MouseButton.NONE);
			verify(listener).mouseMoved(component);
		}
	}
	public static class HandleMouseDrag {
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
		public void dispatchesGivenLeftMouseDown() {
			component.handleMouse(5, 5, MouseButton.LEFT);
			component.handleMouse(9, 9, MouseButton.LEFT);
			verify(listener).mouseDragged(component);
		}
		@Test
		public void doesNotDispatchGivenNoButtonDown() {
			component.handleMouse(5, 5, MouseButton.LEFT);
			component.handleMouse(9, 9, MouseButton.NONE);
		}
		@Test
		public void doesNotDispatchGivenRightMouseDown() {
			component.handleMouse(5, 5, MouseButton.RIGHT);
			component.handleMouse(9, 9, MouseButton.RIGHT);
			verify(listener, times(0)).mouseDragged(component);
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
		public void forwardsToChild() {
			Component child = new Component();
			ComponentListener childListener = mock(ComponentListener.class);
			
			child.setBounds(0, 0, 10, 10);
			child.addListener(childListener);
			component.addChild(child);
			
			component.handleMouse(5, 5, MouseButton.NONE);
			verify(childListener).mouseEntered(child);
		}
	}
}
