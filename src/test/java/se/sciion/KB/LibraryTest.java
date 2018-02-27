package se.sciion.KB;
import org.junit.Test;

import se.sciion.KB.Action;
import se.sciion.KB.KnowlegeBase;
import se.sciion.KB.resolution.ResolutionTree;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

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
    	//System.out.println(base);
    	/*
    	 * Prints:
    	 * closed(door1)
    	 */
    	
    	agentInteract.perform(base);
    	openDoor.perform(base);
    	
    	//System.out.println(base);
    	/*
    	 * Prints:
    	 * open(door1)
    	 */
    	
    	agentInteract.perform(base);
    	closeDoor.perform(base);
    	//System.out.println(base);
    	/*
    	 * Prints:
    	 * closed(door1)
    	 */
    }
    
    @Test public void fetchTest() {
    	
    	KnowlegeBase base = new KnowlegeBase();
    	assertTrue(base.add("exists(entity1)"));
    	base.add("exists(entity1,entity2)");
    	base.add("exists(entity1,entity3)");
    	
    	assertTrue(base.fetch("exists(_)").size() == 1);
    	assertTrue(base.fetch("exists(entity1,_)").size() == 2);
    }

    @Test public void resolutionTest() {
    	KnowlegeBase base = new KnowlegeBase();
    	
    	base.add("node(node1)");
    	base.add("node(node2)");
    	base.add("node(node3)");
    	base.add("navigation-node(player1,node1)");
    	base.add("navigation-node(player1,node2)");
    	base.add("navigation-node(player2,node2)");
    	base.add("navigation-node(player2,node4)");
    	
    	ResolutionTree tree = new ResolutionTree();
    	ArrayList<Clause> clauses = tree.resolv("node(node2):-navigation-node(X,node2)", base);
    	assertTrue(clauses.size() == 2);
    }
    
    @Test public void negationTest() {
    	KnowlegeBase base = new KnowlegeBase();    	
    	
    	base.add("fact(true)");
    	
    	assertTrue(base.lookup("fact(true)"));
    	assertFalse(base.lookup("!fact(true)"));
    	assertTrue(base.lookup("!fact(false)"));
    }
    
    @Test public void lookupLifted() {
    	KnowlegeBase base = new KnowlegeBase();
    	base.add("fact(f1)");
    	base.add("fact(f2)");
    	base.add("fact2(f1)");
    	base.add("fact2(f4)");
    	
    	assertTrue(base.lookup("fact2(X)"));
    	assertTrue(base.lookup("fact2(f1)"));
    	assertFalse(base.lookup("fact3(X)"));


    	
    }
    @Test public void clauseTest() {
    	
    	KnowlegeBase base = new KnowlegeBase();
    	base.add("test(x1-1)");
    	base.add("test(x1,x-1,x2)");
    	base.add("test(x-1,x-1,x3)");
    	base.add("test(x3,x-2,x3)");

    	{
    		String clause = "test(x1-1)";
    		String[] params = Utils.parse(clause);
    		Clause c = new Clause(params);
    		assertTrue(Utils.matchGround(params[1]));
    		assertFalse(Utils.matchVariable(params[1]));
    		
    		System.out.println("Match: " + c.getMatchRegex());
    	}
    	{
    		String clause = "test(x1,x2)";
    		Clause c = new Clause(Utils.parse(clause));
    		assertFalse(c.containsVariables());
    		assertTrue(c.getNumParameters() == 2);
    		System.out.println("Match: " + c.getMatchRegex());

    	}
    	{
    		String clause = "test(x1,x2,x3)";
    		Utils.parse(clause);
    	}
    	{
    		String clause = "test(x1,x2,x3,x4)";
    		Utils.parse(clause);
    	}
    	{
    		String clause = "test(_,x-1,_)";
    		String[] params = Utils.parse(clause);
    		Clause c = new Clause(params);
    		assertFalse(Utils.matchGround(params[1]));
    		assertTrue(Utils.matchVariable(params[1]));
    		
    		assertTrue(Utils.matchGround(params[2]));
    		assertFalse(Utils.matchVariable(params[2]));
    		
    		assertFalse(Utils.matchGround(params[3]));
    		assertTrue(Utils.matchVariable(params[3]));
    		
    		System.out.println("Match: " + c.getMatchRegex());

    	}
    }
}
