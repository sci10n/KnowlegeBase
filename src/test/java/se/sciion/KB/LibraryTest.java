package se.sciion.KB;
import org.junit.Test;

import se.sciion.KB.Action;
import se.sciion.KB.KnowlegeBase;

import static org.junit.Assert.*;

public class LibraryTest {
    
    @Test public void registerTest() {
    	KnowlegeBase base = new KnowlegeBase();    	
    	Action openDoor = new Action(
    			new String[] {"closed(door1)"},
    			new String[] {"open(door1)"},
    			new String[] {"closed(door1)"});
    	
    	base.add("closed(door1)");
    	assertTrue(openDoor.perform(base));
    	
    	assertFalse(openDoor.perform(base));
    }
    
    @Test public void interactionTest() {
    	KnowlegeBase base = new KnowlegeBase();    	
    	
    	Action agentInteract = new Action(
    			new String[] {},
    			new String[] {"interact(door1)"},
    			new String[] {});
    	
    	Action openDoor = new Action(
    			new String[] {"closed(door1)", "interact(door1)"},
    			new String[] {"open(door1)"},
    			new String[] {"closed(door1)", "interact(door1)"});
    	
    	Action closeDoor = new Action(
    			new String[] {"open(door1)", "interact(door1)"},
    			new String[] {"closed(door1)"},
    			new String[] {"open(door1)", "interact(door1)"});
    	
    	base.add("closed(door1)");
    	
    	openDoor.perform(base);
    	System.out.println(base);
    	/*
    	 * Prints:
    	 * closed(door1)
    	 */
    	
    	agentInteract.perform(base);
    	openDoor.perform(base);
    	
    	System.out.println(base);
    	/*
    	 * Prints:
    	 * open(door1)
    	 */
    	
    	agentInteract.perform(base);
    	closeDoor.perform(base);
    	System.out.println(base);
    	/*
    	 * Prints:
    	 * closed(door1)
    	 */
    }
    
    @Test public void negationTest() {
    	KnowlegeBase base = new KnowlegeBase();    	
    	
    	base.add("fact(true)");
    	
    	assertTrue(base.lookup("fact(true)"));
    	assertFalse(base.lookup("!fact(true)"));
    	assertTrue(base.lookup("!fact(false)"));
    }
}
