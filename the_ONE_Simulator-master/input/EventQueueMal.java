/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package input;

/**
 *
 * @author WINDOWS_X
 */
public interface EventQueueMal {
/**
	 * Returns the next event in the queue or ExternalEvent with time of 
	 * double.MAX_VALUE if there are no events left.
	 * @return The next event
	 */
	public ExternalEventMal nextEvent();
	
	/**
	 * Returns next event's time or Double.MAX_VALUE if there are no 
	 * events left in the queue.
	 * @return Next event's time
	 */
	public double nextEventsTime();

}
