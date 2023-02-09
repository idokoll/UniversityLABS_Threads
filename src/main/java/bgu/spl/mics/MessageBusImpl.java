package bgu.spl.mics;

import bgu.spl.mics.application.messages.PublishResultsEvent;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TrainModelEvent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private Object lock1;
	private Object lock2;
	private Object lock3;
	private ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> MsQueueMap;
	private ConcurrentHashMap<Event, Future> FutureMap;
	private ConcurrentHashMap<Class<? extends Event>, LinkedList<MicroService>> subscribedEventMsMap;
	private ConcurrentHashMap<Class<? extends Broadcast>, LinkedBlockingQueue<MicroService>> subscribedBrodMsMap;
	private ConcurrentHashMap<Class<? extends Event>, AtomicInteger> eventCounters;

	public ConcurrentHashMap<Class<? extends Broadcast>, LinkedBlockingQueue<MicroService>> getSubscribedBrodMsMap() {
		return subscribedBrodMsMap;
	}

	public ConcurrentHashMap<Class<? extends Event>, AtomicInteger> getEventCounters() {
		return eventCounters;
	}

	public ConcurrentHashMap<Class<? extends Event>, LinkedList<MicroService>> getSubscribedEventMsMap() {
		return subscribedEventMsMap;
	}

	public ConcurrentHashMap<Event, Future> getFutureMap() {
		return FutureMap;
	}

	public ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>> getMsQueueMap() {
		return MsQueueMap;
	}

	private static class singeltonHolder{
		private static MessageBusImpl instance = new MessageBusImpl();
	}
	private MessageBusImpl(){
		MsQueueMap = new ConcurrentHashMap<MicroService, LinkedBlockingQueue<Message>>();
		FutureMap = new ConcurrentHashMap<Event, Future>();
		subscribedEventMsMap = new ConcurrentHashMap<Class<? extends Event>, LinkedList<MicroService>>();
		subscribedBrodMsMap = new ConcurrentHashMap<Class<? extends Broadcast>, LinkedBlockingQueue<MicroService>>();
		eventCounters = new ConcurrentHashMap<Class<? extends Event>, AtomicInteger>();
		lock1 = new Object();
		lock2 = new Object();
		lock3 = new Object();
	}

	@Override
	public synchronized <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if(subscribedEventMsMap.get(type) == null) {
			subscribedEventMsMap.put(type, new LinkedList<MicroService>());
			eventCounters.put(type, new AtomicInteger(-1));
		}
			subscribedEventMsMap.get(type).add(m);
	}

	@Override
	public synchronized void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(subscribedBrodMsMap.get(type) == null)
			subscribedBrodMsMap.put(type, new LinkedBlockingQueue<MicroService>());
		LinkedBlockingQueue<MicroService> bl = subscribedBrodMsMap.get(type);
		bl.add(m);
		subscribedBrodMsMap.put(type, bl);

	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		FutureMap.get(e).resolve(result);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		if(subscribedBrodMsMap.get(b.getClass()) != null) {
			for (MicroService m : subscribedBrodMsMap.get(b.getClass())) {
				synchronized (lock1) {
					try {
						MsQueueMap.get(m).put(b);
					} catch (InterruptedException e) {}
				}
			}
		}
	}

	
	@Override
	public <T>  Future<T> sendEvent(Event<T> e) { // synchronized???
		if(subscribedEventMsMap.get(e.getClass()) != null) {
			Future<T> f = new Future<>();
			FutureMap.put(e, f);
			eventCounters.get(e.getClass()).incrementAndGet();
			if (subscribedEventMsMap.get(e.getClass()).size() != 0) {
				synchronized (lock2) {
					try {
						MsQueueMap.get(subscribedEventMsMap.get(e.getClass()).get(eventCounters.get(e.getClass()).get() % subscribedEventMsMap.get(e.getClass()).size())).put(e);
					} catch (InterruptedException ex) {}
				}
			}
			return f;
		}
		return null;
	}

	@Override
	public void register(MicroService m) {
		synchronized (lock3) {
			MsQueueMap.put(m, new LinkedBlockingQueue<Message>());
		}
	}

	@Override
	public void unregister(MicroService m) {
		MsQueueMap.remove(m);
		for (Class<? extends Event> e: subscribedEventMsMap.keySet()) {
			subscribedEventMsMap.get(e).remove(m);
		}
		for (Class<? extends Broadcast> b: subscribedBrodMsMap.keySet()) {
			subscribedBrodMsMap.get(b).remove(m);
		}
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException
	 {
		 return MsQueueMap.get(m).take();
	}
	public static MessageBusImpl getInstance(){
		return singeltonHolder.instance;
	}

	

}
