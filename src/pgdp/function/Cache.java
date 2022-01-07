package pgdp.function;


import java.util.LinkedHashMap;
import java.util.Map;

public class Cache<K, V> extends LinkedHashMap<K, V> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 100000L;
	//attributes
	private final int MAXIMAL_CACHE_SIZE;
	//constructors
	public Cache (int size) {
		MAXIMAL_CACHE_SIZE = size;
	}
	
	//getter
	public int getMaximalCacheSize() {
		return MAXIMAL_CACHE_SIZE;
	}
	
	@SuppressWarnings("rawtypes")
	protected boolean removeEldestEntry(Map.Entry eldest) {
	        return size() > MAXIMAL_CACHE_SIZE;
	}
}
