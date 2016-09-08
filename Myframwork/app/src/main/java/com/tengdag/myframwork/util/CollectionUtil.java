package com.tengdag.myframwork.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CollectionUtil {
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
	
	public static boolean isEmpty(Map<?,?> map) {
		return map == null || map.isEmpty();
	}
	
	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Collection<T> clone(Collection<T> collection) {
		if(isEmpty(collection)) {
			return null;
		}
		
		try {
			Collection<T> dest = (Collection<T>)collection.getClass().newInstance();
			for(T item : collection) {
				dest.add(item);
			}
			return dest;
		} catch (Exception e) {
			return null;
		} 
	}
	
	@SuppressWarnings("unchecked")
	public static <K,V> Map<K,V> clone(Map<K,V> map) {
		if(isEmpty(map)) {
			return null;
		}
		
		try {
			Map<K,V> dest = (Map<K,V>)map.getClass().newInstance();
			Set<Entry<K, V>> entrySet = map.entrySet();
			for(Entry<K, V> entry : entrySet) {
				dest.put(entry.getKey(), entry.getValue());
			}
			return dest;
		} catch (Exception e) {
			return null;
		} 
	}

	public static <K,V> Set<K> cloneKeys(Map<K,V> map) {
		if(isEmpty(map)) {
			return null;
		}
		
		try {
			Set<K> dest = new HashSet<K>();
			Set<Entry<K, V>> entrySet = map.entrySet();
			for(Entry<K, V> entry : entrySet) {
				dest.add(entry.getKey());
			}
			return dest;
		} catch (Exception e) {
			return null;
		} 
	}
	
	public static long[] toLongArray(Collection<Long> collection) {
		if(isEmpty(collection)) {
			return null;
		}
		
		try {
			long[] dest = new long[collection.size()];
			int i = 0;
			for(Long item : collection) {
				dest[i++] = item;
			}
			return dest;
		} catch (Exception e) {
			return null;
		} 
	}
}
