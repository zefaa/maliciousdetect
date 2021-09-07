/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

import core.Settings;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author WINDOWS_X
 */
public class EventQueueHandlerMal {
    /** Event queue settings main namespace ({@value})*/
	public static final String SETTINGS_NAMESPACE = "Events";
	/** number of event queues -setting id ({@value})*/
	public static final String NROF_SETTING = "nrof";

	/** name of the events class (for class based events) -setting id
	 * ({@value}) */
	public static final String CLASS_SETTING = "class";
	/** name of the package where event generator classes are looked from */
	public static final String CLASS_PACKAGE = "input";
	
	/** number of events to preload from file -setting id ({@value})*/
	public static final String PRELOAD_SETTING = "nrofPreload";
	/** path of external events file -setting id ({@value})*/
	public static final String PATH_SETTING = "filePath";
	
	private List<EventQueueMal> queues;
	
	/**
	 * Creates a new EventQueueHandler which can be queried for 
	 * event queues.
	 */
	public EventQueueHandlerMal() {
		Settings settings = new Settings(SETTINGS_NAMESPACE);
		int nrof = settings.getInt(NROF_SETTING);
		this.queues = new ArrayList<EventQueueMal>();

		for (int i=1; i <= nrof; i++) {
			Settings s = new Settings(SETTINGS_NAMESPACE + i);

			if (s.contains(PATH_SETTING)) { // external events file
				int preload = 0;
				String path = "";
				if (s.contains(PRELOAD_SETTING)) {
					preload = s.getInt(PRELOAD_SETTING);
				}
				path = s.getSetting(PATH_SETTING);

				queues.add((EventQueueMal) new ExternalEventQueueMal(path, preload));
			}
			else if (s.contains(CLASS_SETTING)) { // event generator class
				String className = CLASS_PACKAGE + "." + 
					s.getSetting(CLASS_SETTING);
				EventQueueMal eq = (EventQueueMal)s.createIntializedObject(className);
				
				queues.add((EventQueueMal) eq);
			}
		}
	}
	
	/** 
	 * Returns all the loaded event queues
	 * @return all the loaded event queues
	 */
	public List<EventQueueMal> getEventQueues() {
		return this.queues;
	}
}
