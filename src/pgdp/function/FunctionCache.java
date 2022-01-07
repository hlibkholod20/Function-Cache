package pgdp.function;

import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.function.Function;


public class FunctionCache<T, R> {
	//constant
	private final static int DEFAULT_CACHE_SIZE = 10_000;
	
	//constructions
	private FunctionCache() {
	}
	//inner class
	private static class Pair<T, U> {
		final T firstArgument;
		final U secondArgument;
		
		public Pair (T t, U u) {
			firstArgument = t;
			secondArgument = u;
		}
		
		@Override
		public int hashCode() {
			return firstArgument.hashCode() + 7*secondArgument.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof Pair)) return false;
			else {
				return this.hashCode() == o.hashCode();
			}
		}
	}
	
	//methods for functions
	public static <T, R> Function<T, R> cached(Function<T, R> function, int maximalCacheSize){
		Function<T, R> abc = new Function<T, R>() {
			Cache<T, R> localCache = new Cache<T, R>(maximalCacheSize); 
			public R apply(T t) {
				if (localCache.containsKey(t)) return localCache.get(t);
				else {
					localCache.put(t, function.apply(t));
					return localCache.get(t);
				}
			}
		};
		return abc;
	}
	
	public static <T, R> Function<T, R> cached(Function<T, R> function){
		Function<T, R> abc = new Function<T, R>() {
			Cache<T, R> localCache = new Cache<T, R>(DEFAULT_CACHE_SIZE); 
			public R apply(T t) {
				if (localCache.containsKey(t)) return localCache.get(t);
				else {
					localCache.put(t, function.apply(t));
					return localCache.get(t);
				}
			}
		};
		return abc;
	}
	
	//methods for binary functions
	public static <T, U, R> BiFunction<T, U, R> cached(BiFunction<T, U, R> biFunction, int maximalCacheSize){
			BiFunction<T, U, R> xyz = new BiFunction<T, U, R>() {
			Cache<Pair<T, U>, R> localCache = new Cache<Pair<T, U>, R>(maximalCacheSize);
			@Override
			public R apply(T t, U u) {
				Pair<T, U> abc = new Pair<T, U>(t, u);
				if (localCache.containsKey(abc)) return localCache.get(abc);
				else {
					localCache.put(abc, biFunction.apply(t, u));
					return localCache.get(abc);
				}
			}
		};
		return xyz;
	}
	
	public static <T, U, R> BiFunction<T, U, R> cached(BiFunction<T, U, R> biFunction){
		BiFunction<T, U, R> xyz = new BiFunction<T, U, R>(){
			Cache<Pair<T, U>, R> localCache = new Cache<Pair<T, U>, R>(DEFAULT_CACHE_SIZE);
			@Override
			public R apply(T t, U u) {
				Pair<T, U> abc = new Pair<T, U>(t, u);
				if (localCache.containsKey(abc)) return localCache.get(abc);
				else {
					localCache.put(abc, biFunction.apply(t, u));
					return localCache.get(abc);
				}
			}
		};
		return xyz;
	}
	
	//methods for recursive functions
	public static <T, R> Function<T, R> cachedRecursive(BiFunction<T, Function<T, R>, R> function, int maximalCacheSize){
		Function<T, R> abc = new Function<T, R>() {
			Cache<T, R> localCache = new Cache<T, R>(maximalCacheSize);
			@Override
			public R apply (T t){
				if (localCache.containsKey(t)) return localCache.get(t);
				else {
					localCache.put(t, function.apply(t, this));
					return localCache.get(t);
				}
			}
		};
		return abc;
	}
	
	public static <T, R> Function<T, R> cachedRecursive(BiFunction<T, Function<T, R>, R> function){
		Function<T, R> abc = new Function<T, R>() {
			Cache<T, R> localCache = new Cache<T, R>(DEFAULT_CACHE_SIZE);
			@Override
			public R apply (T t){
				if (localCache.containsKey(t)) return localCache.get(t);
				else {
					localCache.put(t, function.apply(t, this));
					return localCache.get(t);
				}
			}
		};
		return abc;
	}
	
	public static void main (String[] args) {
		Function<BigInteger, BigInteger> fib = cachedRecursive((n, f) -> {
		    if(n.compareTo(BigInteger.TWO) <= 0)
		        return BigInteger.ONE;
		    return f.apply(n.subtract(BigInteger.ONE)).add(f.apply(n.subtract(BigInteger.TWO)));
		});
		System.out.println(fib.apply(BigInteger.valueOf(1000L)));
		BiFunction<Integer, Integer, Integer> blya = cached((t,u) -> t*100 - u);
		System.out.println(blya.apply(4,5));
	}
}
